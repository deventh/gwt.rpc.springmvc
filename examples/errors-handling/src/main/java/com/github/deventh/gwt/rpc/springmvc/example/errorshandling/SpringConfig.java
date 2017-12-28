package com.github.deventh.gwt.rpc.springmvc.example.errorshandling;

import org.springframework.cache.CacheManager;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseFactoryBean;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import javax.sql.DataSource;

@Configuration
@ComponentScan(basePackages = {
    "com.github.deventh"
})
public class SpringConfig extends WebMvcConfigurationSupport {
  @Override
  protected void configureViewResolvers(ViewResolverRegistry registry) {
    registry.jsp("/WEB-INF/jsp/", ".jsp");
  }

  @Bean
  protected CacheManager concurrentMapCacheManager() {
    return new ConcurrentMapCacheManager();
  }

  @Bean
  protected EmbeddedDatabaseFactoryBean embeddedDatabaseFactoryBean() {
    EmbeddedDatabaseFactoryBean embeddedDatabaseFactoryBean = new EmbeddedDatabaseFactoryBean();
    embeddedDatabaseFactoryBean.setDatabaseType(EmbeddedDatabaseType.H2);
    embeddedDatabaseFactoryBean.setDatabasePopulator(new ResourceDatabasePopulator(new ClassPathResource("create.sql")));
    return embeddedDatabaseFactoryBean;
  }

  @Bean
  protected JdbcTemplate jdbcTemplate(DataSource dataSource) {
    return new JdbcTemplate(dataSource, false);
  }
}
