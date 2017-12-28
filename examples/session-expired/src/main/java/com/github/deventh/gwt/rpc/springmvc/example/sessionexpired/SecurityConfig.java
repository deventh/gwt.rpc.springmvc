package com.github.deventh.gwt.rpc.springmvc.example.sessionexpired;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;

import javax.annotation.Resource;
import javax.servlet.Filter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
  @Resource
  private UserDetailsService userDetailsService;
  @Resource
  private Filter sessionExpiredFilter;

  @Override
  public void configure(WebSecurity web) {
    web.ignoring().antMatchers("/assets/**");
  }

  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    auth.userDetailsService(userDetailsService);
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.addFilterBefore(sessionExpiredFilter, AbstractPreAuthenticatedProcessingFilter.class);

    http.logout().logoutUrl("/logout.htm").logoutSuccessUrl("/");
    http.csrf().disable();
    http.rememberMe();
    http.authorizeRequests()
        .antMatchers("/secured.htm").hasAnyAuthority("ROLE_USER")
        .antMatchers("/**/*.gwt").hasAnyAuthority("ROLE_USER")
        .anyRequest().permitAll()
        .and()
        .formLogin()
        .usernameParameter("j_username")
        .passwordParameter("j_password")
        .loginPage("/login.htm")
        .failureUrl("/failed_login.htm")
        .defaultSuccessUrl("/secured.htm")
        .loginProcessingUrl("/login_action.htm")
        .permitAll();
  }
}
