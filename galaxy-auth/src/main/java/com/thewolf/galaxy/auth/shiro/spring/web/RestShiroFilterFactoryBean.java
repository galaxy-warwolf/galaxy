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
package com.thewolf.galaxy.auth.shiro.spring.web;

import com.google.common.collect.Maps;
import com.thewolf.galaxy.auth.service.TokenService;
import com.thewolf.galaxy.auth.shiro.jwt.filter.authc.TokenServiceAware;
import com.thewolf.galaxy.auth.shiro.web.mgt.RestFilterChainManager;
import com.thewolf.galaxy.auth.shiro.web.mgt.RestSecurityManager;
import com.thewolf.galaxy.auth.shiro.web.servlet.RestShiroFilter;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.util.CollectionUtils;
import org.apache.shiro.util.Nameable;
import org.apache.shiro.web.filter.mgt.FilterChainManager;
import org.apache.shiro.web.filter.mgt.PathMatchingFilterChainResolver;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.config.BeanPostProcessor;

import javax.servlet.Filter;
import java.util.Map;

/**
 * Rest shiro factory bean to initialize {@code RestShiroFilter}.
 */
@Slf4j
@Getter
@Setter
@SuppressWarnings("unused")
public class RestShiroFilterFactoryBean implements FactoryBean, BeanPostProcessor {

  private SecurityManager securityManager;

  private TokenService tokenService;

  private Map<String, Filter> filters;

  private Map<String, String> filterChainDefinitionMap;

  private RestShiroFilter instance;

  /**
   * Default constructor.
   */
  public RestShiroFilterFactoryBean() {
    this.filters = Maps.newLinkedHashMap();
    this.filterChainDefinitionMap = Maps.newLinkedHashMap();
  }

  @Override
  public Object getObject() throws Exception {
    if (instance == null) {
      instance = createInstance();
    }
    return instance;
  }

  @Override
  public Class<?> getObjectType() {
    return RestShiroFilter.class;
  }

  @Override
  public boolean isSingleton() {
    return true;
  }

  @Override
  public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
    if (bean instanceof Filter) {
      log.debug("Found filter chain candidate filter '{}'", beanName);
      Filter filter = (Filter) bean;
      filters.put(beanName, filter);
    } else {
      log.trace("Ignoring non-Filter bean '{}'", beanName);
    }
    return bean;
  }

  @Override
  public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
    return bean;
  }

  /**
   * Create {@code RestShiroFilter}.
   *
   * @return an instance of {@code RestShiroFilter}.
   * @throws Exception exception while creating fail.
   */
  protected RestShiroFilter createInstance() throws Exception {
    log.debug("Creating Shiro Filter instance.");
    if (securityManager == null) {
      String msg = "SecurityManager property must be set.";
      throw new BeanInitializationException(msg);
    }
    if (!(securityManager instanceof RestSecurityManager)) {
      String msg = "The security manager does not implement the RestSecurityManager interface.";
      throw new BeanInitializationException(msg);
    }
    FilterChainManager manager = createFilterChainManager();
    PathMatchingFilterChainResolver chainResolver = new PathMatchingFilterChainResolver();
    chainResolver.setFilterChainManager(manager);
    return new RestShiroFilter((RestSecurityManager) securityManager, chainResolver);
  }

  /**
   * Creates {@code FilterChainManager}.
   *
   * @return an instance of {@code FilterChainManager}.
   */
  protected FilterChainManager createFilterChainManager() {
    RestFilterChainManager manager = new RestFilterChainManager(tokenService);
    if (!CollectionUtils.isEmpty(filters)) {
      for (Map.Entry<String, Filter> entry : filters.entrySet()) {
        String name = entry.getKey();
        Filter filter = entry.getValue();
        if (filter instanceof Nameable) {
          ((Nameable) filter).setName(name);
        }
        if (filter instanceof TokenServiceAware) {
          ((TokenServiceAware) filter).setTokenService(tokenService);
        }
        manager.addFilter(name, filter, false);
      }
    }
    if (!CollectionUtils.isEmpty(filterChainDefinitionMap)) {
      for (Map.Entry<String, String> entry : filterChainDefinitionMap.entrySet()) {
        String url = entry.getKey();
        String chainDefinition = entry.getValue();
        manager.createChain(url, chainDefinition);
      }
    }
    return manager;
  }
}
