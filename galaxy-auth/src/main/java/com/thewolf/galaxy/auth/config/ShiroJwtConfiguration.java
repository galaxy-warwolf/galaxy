/**
 * Copyright 2017 thewolf
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.thewolf.galaxy.auth.config;

import com.thewolf.galaxy.auth.service.JwtTokenService;
import com.thewolf.galaxy.auth.service.TokenService;
import com.thewolf.galaxy.auth.shiro.jwt.realm.JWTRealm;
import com.thewolf.galaxy.auth.shiro.spring.web.RestShiroFilterFactoryBean;
import com.thewolf.galaxy.auth.shiro.web.mgt.RestSecurityManager;
import com.thewolf.galaxy.auth.shiro.web.servlet.RestShiroFilter;
import com.thewolf.galaxy.auth.support.AuthConstants;
import com.thewolf.galaxy.auth.support.AuthenticatingFacade;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;

/**
 * Shiro jwt configuration.
 */
@SuppressWarnings("JavadocMethod")
@ConditionalOnWebApplication
@ConditionalOnProperty(prefix = "shiro.jwt", value = "enabled", havingValue = "true")
public class ShiroJwtConfiguration {

  @Bean
  @ConfigurationProperties("shiro.jwt")
  public JwtConfig jwtConfig() {
    return new JwtConfig();
  }

  @Bean
  public TokenService tokenService() {
    return new JwtTokenService();
  }

  @Bean
  public JWTRealm jwtRealm(AuthenticatingFacade authenticatingFacade) {
    return new JWTRealm(authenticatingFacade);
  }

  @Bean
  public RestSecurityManager securityManager(JWTRealm jwtRealm) {
    RestSecurityManager restSecurityManager = new RestSecurityManager();
    restSecurityManager.setRealm(jwtRealm);
    return restSecurityManager;
  }

  @Bean
  public RestShiroFilterFactoryBean restShiroFilterFactoryBean(TokenService tokenService,
                                                               ShiroProperties shiroConfiguration,
                                                               RestSecurityManager restSecurityManager) {
    RestShiroFilterFactoryBean restShiroFilterFactoryBean = new RestShiroFilterFactoryBean();
    restShiroFilterFactoryBean.setSecurityManager(restSecurityManager);
    restShiroFilterFactoryBean.setTokenService(tokenService);
    restShiroFilterFactoryBean.setFilterChainDefinitionMap(shiroConfiguration.getFilterChains());
    return restShiroFilterFactoryBean;
  }

  @Bean
  public FilterRegistrationBean restShiroFilterRegistrationBean(RestShiroFilter restShiroFilter,
                                                                ShiroProperties shiroConfiguration) {
    FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
    filterRegistrationBean.setFilter(restShiroFilter);
    filterRegistrationBean.setUrlPatterns(shiroConfiguration.getFilterUrlPatterns());
    return filterRegistrationBean;
  }

  /**
   * Jwt configuration for setting secret, algorithm & expires etc.
   */
  @Setter
  @Getter
  public static class JwtConfig {

    private String algorithm = AuthConstants.ALGORITHM;

    private String secret = AuthConstants.SECRET_DEFAULT;

    private String refreshSecret = AuthConstants.REFRESH_SECRET_DEFAULT;

    private long expiresInSecond = AuthConstants.EXPIRES_IN_SECOND;

    private long refreshExpiresInSecond = AuthConstants.REFRESH_EXPIRES_IN_SECOND;

  }
}
