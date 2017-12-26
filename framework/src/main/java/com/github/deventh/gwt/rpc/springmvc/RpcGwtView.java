package com.github.deventh.gwt.rpc.springmvc;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.servlet.View;

import javax.annotation.Resource;
import javax.servlet.AsyncContext;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Component
class RpcGwtView implements View, ServletContextAware {
  protected final Log logger = LogFactory.getLog(getClass());

  private ServletContext servletContext;

  @Resource
  private ThrowsAdviceExceptionAdvisor throwsAdviceExceptionAdvisor;

  @Override
  public String getContentType() {
    return null;
  }

  @Override
  public void render(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response) {
    Object delegate = model.get(RpcGwtHandlerMapping.DELEGATE_MODEL_KEY);

    ProxyFactory pf = new ProxyFactory();
    pf.setProxyTargetClass(true);
    pf.setTarget(delegate);
    pf.addAdvisor(0, throwsAdviceExceptionAdvisor);
    pf.setFrozen(true);

    RemoteServiceServlet rpcServlet = new RemoteServiceServlet(pf.getProxy()) {
      @Override
      public ServletContext getServletContext() {
        return servletContext;
      }

      @Override
      public String getServletName() {
        return RemoteServiceServlet.class.getName();
      }
    };

    if (request.isAsyncSupported()) {
      AsyncContext asyncContext = request.startAsync(request, response);
      asyncContext.start(() -> {
        try {
          if(logger.isDebugEnabled()) {
            logger.debug("Requesting " + request.getRequestURI());
          }
          rpcServlet.doPost(request, response);
        } finally {
          asyncContext.complete();
        }
      });
    } else {
      rpcServlet.doPost(request, response);
    }
  }

  @Override
  public void setServletContext(ServletContext servletContext) {
    this.servletContext = servletContext;
  }
}
