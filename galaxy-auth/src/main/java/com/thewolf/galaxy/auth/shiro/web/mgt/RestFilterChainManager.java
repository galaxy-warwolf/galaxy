/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package com.thewolf.galaxy.auth.shiro.web.mgt;

import com.thewolf.galaxy.auth.service.TokenService;
import com.thewolf.galaxy.auth.shiro.jwt.filter.authc.TokenServiceAware;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.web.filter.mgt.DefaultFilterChainManager;

import javax.servlet.FilterConfig;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Rest style {@code FilterChainManager} implementation.
 */
@Slf4j
@Getter
public class RestFilterChainManager extends DefaultFilterChainManager {

  private final TokenService tokenService;

  /**
   * Constructor.
   *
   * @param tokenService an instance of {@code TokenService}.
   */
  public RestFilterChainManager(TokenService tokenService) {
    super();
    checkNotNull(tokenService);
    this.tokenService = tokenService;
    initFilters();
  }

  /**
   * Constructor.
   *
   * @param tokenService an instance of {@code TokenService}.
   * @param filterConfig an instance of {@filterConfig}.
   */
  public RestFilterChainManager(TokenService tokenService, FilterConfig filterConfig) {
    super(filterConfig);
    checkNotNull(tokenService);
    this.tokenService = tokenService;
    initFilters();
  }

  @Override
  protected void addDefaultFilters(boolean init) {
    for (RestDefaultFilter defaultFilter : RestDefaultFilter.values()) {
      addFilter(defaultFilter.name(), defaultFilter.newInstance(), init, false);
    }
  }

  /**
   * Initialize filters.
   */
  protected void initFilters() {
    getFilters().values().stream().forEach(filter -> {
      if (filter instanceof TokenServiceAware) {
        ((TokenServiceAware) filter).setTokenService(tokenService);
      }
    });
  }

}
