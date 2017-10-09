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

package com.thewolf.galaxy.auth.shiro.web.subject.support;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.HostAuthenticationToken;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.SessionContext;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.subject.support.DelegatingSubject;
import org.apache.shiro.web.subject.support.WebDelegatingSubject;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 * Rest {@code WebSubject} implementation that additional ensures the ability to retain a servlet request/response pair
 * to be used by internal shiro components as necessary during the request execution.
 */
public class RestDelegatingSubject extends WebDelegatingSubject {

  /**
   * Constructor.
   *
   * @param principals      principals.
   * @param authenticated   authenticated.
   * @param host            host.
   * @param session         session, usually this is null.
   * @param request         servlet request.
   * @param response        servlet response.
   * @param securityManager security manager.
   */
  public RestDelegatingSubject(PrincipalCollection principals, boolean authenticated, String host, Session session,
                               ServletRequest request, ServletResponse response, SecurityManager securityManager) {
    super(principals, authenticated, host, session, false, request, response, securityManager);
  }

  @Override
  public PrincipalCollection getPrincipals() {
    return principals;
  }

  @Override
  public void login(AuthenticationToken token) throws AuthenticationException {
    Subject subject = securityManager.login(this, token);

    PrincipalCollection principals;

    String host = null;

    if (subject instanceof DelegatingSubject) {
      DelegatingSubject delegating = (DelegatingSubject) subject;
      //we have to do this in case there are assumed identities - we don't want to lose the 'real' principals:
      principals = delegating.getPrincipals();
      host = delegating.getHost();
    } else {
      principals = subject.getPrincipals();
    }

    if (principals == null || principals.isEmpty()) {
      String msg = "Principals returned from securityManager.login( token ) returned a null or "
        + "empty value.  This value must be non null and populated with one or more elements.";
      throw new IllegalStateException(msg);
    }
    this.principals = principals;
    this.authenticated = true;
    if (token instanceof HostAuthenticationToken) {
      host = ((HostAuthenticationToken) token).getHost();
    }
    if (host != null) {
      this.host = host;
    }
  }

  @Override
  public void logout() {
    try {
      securityManager.logout(this);
    } finally {
      principals = null;
      authenticated = false;
    }
  }

  @Override
  protected boolean isSessionCreationEnabled() {
    return false;
  }

  @Override
  protected SessionContext createSessionContext() {
    throw new UnsupportedOperationException();
  }

  @Override
  protected Session decorate(Session session) {
    throw new UnsupportedOperationException();
  }

  @Override
  public boolean isRemembered() {
    throw new UnsupportedOperationException();
  }

  @Override
  public Session getSession() {
    throw new UnsupportedOperationException();
  }

  @Override
  public Session getSession(boolean create) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void runAs(PrincipalCollection principals) {
    throw new UnsupportedOperationException();
  }

  @Override
  public boolean isRunAs() {
    throw new UnsupportedOperationException();
  }

  @Override
  public PrincipalCollection getPreviousPrincipals() {
    throw new UnsupportedOperationException();
  }

  @Override
  public PrincipalCollection releaseRunAs() {
    throw new UnsupportedOperationException();
  }

}
