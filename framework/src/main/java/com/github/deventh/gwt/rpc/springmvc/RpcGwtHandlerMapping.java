package com.github.deventh.gwt.rpc.springmvc;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.Cache.ValueWrapper;
import org.springframework.cache.CacheManager;
import org.springframework.cache.support.NoOpCacheManager;
import org.springframework.stereotype.Component;
import org.springframework.util.ClassUtils;
import org.springframework.web.servlet.handler.AbstractDetectingUrlHandlerMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * MVC Handler Mapping for GWT RPC calls
 */
@Component
public class RpcGwtHandlerMapping extends AbstractDetectingUrlHandlerMapping {
  private final Log logger = LogFactory.getLog(getClass());

  public static final String DELEGATE_MODEL_KEY = "delegate";

  @Autowired(required = false)
  private CacheManager cacheManager = new NoOpCacheManager();

  @Override
  protected String[] determineUrlsForHandler(String beanName) {
    BeanFactory context = getApplicationContext();

    if (!context.isTypeMatch(beanName, RemoteService.class)) {
      return null;
    }

    final Class<?> type = AopUtils.getTargetClass(
        context.getBean(beanName, RemoteService.class)
    );

    final String[] urls = doFindRelativePaths(type);

    if (logger.isDebugEnabled()) {
      logger.debug("Found url(s): " + Arrays.toString(urls) + " for the type: " + type);
    }

    Set<String> unqiue = new HashSet<>(Arrays.asList(urls));

    if(unqiue.size() != urls.length) {
      final String errorMessage =
          "URLs collected from remote services are not unique. " +
          "So that 2 or more candidates are for the relative paths. " +
          "And not clear mapping guaranteed since " + RemoteServiceRelativePath.class.getName() + " does not contain module name!";
      logger.error(errorMessage);
      throw new RuntimeException(errorMessage);
    }

    return urls;
  }

  private String[] doFindRelativePaths(Class<?> beanClass) {
    return Arrays.stream(ClassUtils.getAllInterfacesForClass(beanClass))
        .distinct()
        .filter(RemoteService.class::isAssignableFrom)
        .filter(i -> i.getAnnotation(RemoteServiceRelativePath.class) != null)
        .map(i -> i.getAnnotation(RemoteServiceRelativePath.class))
        .map(RemoteServiceRelativePath::value)
        .toArray(String[]::new);
  }

  @Override
  public Object lookupHandler(String urlPath, HttpServletRequest request) throws Exception {
    ValueWrapper cached = getCache().get(urlPath);
    if (cached != null) {
      return cached.get();
    }

    for (String relativePath : getHandlerMap().keySet()) {
      if (urlPath.endsWith(relativePath)) {
        Object value = getHandlerMap().get(relativePath);

        getCache().put(urlPath, value);

        return value;
      }
    }

    return null;
  }

  private Cache getCache() {
    final String name = getClass().getCanonicalName();
    return cacheManager.getCache(name);
  }
}
