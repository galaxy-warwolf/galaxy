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

package com.thewolf.galaxy.auth.shiro.web.servlet;

import com.google.common.base.Throwables;
import com.thewolf.galaxy.auth.shiro.web.mgt.RestSecurityManager;
import com.thewolf.galaxy.auth.shiro.web.subject.support.RestSubjectBuilder;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.ExecutionException;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.mgt.FilterChainResolver;
import org.apache.shiro.web.servlet.OncePerRequestFilter;
import org.apache.shiro.web.subject.WebSubject;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Rest Shiro filter.
 */
@Getter
@Setter
@Slf4j
@SuppressWarnings("unused")
public class RestShiroFilter extends OncePerRequestFilter {

  private static final String STATIC_INIT_PARAM_NAME = "staticSecurityManagerEnabled";

  private RestSecurityManager securityManager;
  private FilterChainResolver filterChainResolver;
  private boolean staticSecurityManagerEnabled;

  /**
   * Constructor.
   *
   * @param restSecurityManager an instance of {@code RestSecurityManager}.
   * @param filterChainResolver an instance of {@code FilterChainResolver}.
   */
  public RestShiroFilter(RestSecurityManager restSecurityManager, FilterChainResolver filterChainResolver) {
    super();
    checkNotNull(restSecurityManager);
    this.securityManager = restSecurityManager;
    this.filterChainResolver = filterChainResolver;
  }

  @Override
  protected final void onFilterConfigSet() throws Exception {
    applyStaticSecurityManagerEnabledConfig();
    init();
    ensureSecurityManager();
    if (isStaticSecurityManagerEnabled()) {
      SecurityUtils.setSecurityManager(securityManager);
    }
  }

  /**
   * init filter.
   *
   * @throws Exception exception.
   */
  public void init() throws Exception {
  }

  /**
   * Ensure {@code SecurityManager}.
   */
  private void ensureSecurityManager() {
    if (securityManager == null) {
      log.info("No SecurityManager configured.  Creating default.");
      securityManager = createDefaultSecurityManager();
    }
  }

  /**
   * Create default {@code RestSecurityManager}.
   *
   * @return an instance of {@code RestSecurityManager}.
   */
  private RestSecurityManager createDefaultSecurityManager() {
    return new RestSecurityManager();
  }

  /**
   * Check if the init-param that configures the filter to use static memory has been configured, and if so,
   * set the {@link #setStaticSecurityManagerEnabled(boolean)} attribute with the configured value.
   */
  private void applyStaticSecurityManagerEnabledConfig() {
    String value = getInitParam(STATIC_INIT_PARAM_NAME);
    if (value != null) {
      Boolean b = Boolean.valueOf(value);
      if (b != null) {
        staticSecurityManagerEnabled = b;
      }
    }
  }

  /**
   * Prepare {@ServletRequest}.
   *
   * @param request  servlet request.
   * @param response servlet response.
   * @param chain    filter chain.
   * @return prepared servlet request.
   */
  protected ServletRequest prepareServletRequest(ServletRequest request, ServletResponse response, FilterChain chain) {
    ServletRequest toUse = request;
    if (request instanceof HttpServletRequest) {
      HttpServletRequest httpServletRequest = (HttpServletRequest) request;
      toUse = new RestHttpServletRequest(httpServletRequest);
    }
    return toUse;
  }

  /**
   * Prepare {@ServletResponse}.
   *
   * @param request  servlet request.
   * @param response servlet response.
   * @param chain    filter chain.
   * @return prepared servlet response.
   */
  protected ServletResponse prepareServletResponse(ServletRequest request, ServletResponse response,
                                                   FilterChain chain) {
    ServletResponse toUse = response;
    if ((response instanceof HttpServletResponse) && (request instanceof RestHttpServletRequest)) {
      HttpServletResponse httpServletResponse = (HttpServletResponse) response;
      toUse = new RestHttpServletResponse(httpServletResponse, (RestHttpServletRequest) request);
    }
    return toUse;
  }

  /**
   * Create subject.
   *
   * @param request  servlet request.
   * @param response servlet response.
   * @return web subject.
   */
  protected WebSubject createSubject(ServletRequest request, ServletResponse response) {
    return new RestSubjectBuilder(getSecurityManager(), request, response).buildWebSubject();
  }

  @Override
  protected void doFilterInternal(ServletRequest servletRequest, ServletResponse servletResponse,
                                  FilterChain filterChain) throws ServletException, IOException {
    Throwable t = null;
    try {
      final ServletRequest request = prepareServletRequest(servletRequest, servletResponse, filterChain);
      final ServletResponse response = prepareServletResponse(request, servletResponse, filterChain);

      final Subject subject = createSubject(request, response);

      subject.execute(() -> {
        executeChain(request, response, filterChain);
        return null;
      });
    } catch (ExecutionException ex) {
      t = ex.getCause();
    } catch (Throwable throwable) {
      t = throwable;
    }
    if (t != null) {
      Throwables.throwIfInstanceOf(t, ServletException.class);
      Throwables.throwIfInstanceOf(t, IOException.class);
      //otherwise it's not one of the two exceptions expected by the filter method signature - wrap it in one:
      String msg = "Filtered request failed.";
      throw new ServletException(msg, t);
    }
  }

  /**
   * Execute chain.
   *
   * @param request     servlet request.
   * @param response    servlet response.
   * @param filterChain filter chain
   * @throws IOException      if the underlying do filter call results in an {@code IOException}.
   * @throws ServletException if the underlying do filter call results in a {@code ServletException}.
   */
  protected void executeChain(ServletRequest request, ServletResponse response, FilterChain filterChain)
    throws IOException, ServletException {
    FilterChain chain = getExecutionChain(request, response, filterChain);
    chain.doFilter(request, response);
  }

  /**
   * Get filter chain.
   *
   * @param request     servlet request.
   * @param response    servlet response.
   * @param filterChain filter chain
   * @return an instance of {@code FilterChain}.
   */
  protected FilterChain getExecutionChain(ServletRequest request, ServletResponse response, FilterChain filterChain) {
    if (filterChainResolver == null) {
      log.debug("No FilterChainResolver configured.  Returning original FilterChain.");
      return filterChain;
    }

    FilterChain chain = filterChain;
    FilterChain resolved = filterChainResolver.getChain(request, response, filterChain);
    if (resolved != null) {
      log.trace("Resolved a configured FilterChain for the current request.");
      chain = resolved;
    } else {
      log.trace("No FilterChain configured for the current request.  Using the default.");
    }

    return chain;
  }

}
