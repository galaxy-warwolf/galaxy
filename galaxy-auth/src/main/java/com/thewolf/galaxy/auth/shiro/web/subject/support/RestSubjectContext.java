/**
 * Copyright 2017 thewolf
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.thewolf.galaxy.auth.shiro.web.subject.support;

import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.subject.support.DefaultSubjectContext;
import org.apache.shiro.web.subject.WebSubjectContext;
import org.apache.shiro.web.subject.support.DefaultWebSubjectContext;

import javax.servlet.ServletRequest;
import java.io.Serializable;


/**
 * Rest web subject context implementation.
 */
public class RestSubjectContext extends DefaultWebSubjectContext {

  private static final long serialVersionUID = 1445127640642001692L;

  private static final String AUTHENTICATED = DefaultSubjectContext.class.getName() + ".AUTHENTICATED";

  /**
   * Default no-arg constructor.
   */
  public RestSubjectContext() {
  }

  /**
   * One-arg constructor.
   *
   * @param context an instance of {@code RestSubjectContext}
   */
  public RestSubjectContext(WebSubjectContext context) {
    super(context);
  }

  @Override
  public Session resolveSession() {
    throw new UnsupportedOperationException();
  }

  @Override
  public PrincipalCollection resolvePrincipals() {
    PrincipalCollection principals = getPrincipals();

    if (isEmpty(principals)) {
      //check to see if they were just authenticated:
      AuthenticationInfo info = getAuthenticationInfo();
      if (info != null) {
        principals = info.getPrincipals();
      }
    }

    if (isEmpty(principals)) {
      Subject subject = getSubject();
      if (subject != null) {
        principals = subject.getPrincipals();
      }
    }

    return principals;
  }

  @Override
  public boolean resolveAuthenticated() {
    Boolean authenticated = getTypedValue(AUTHENTICATED, Boolean.class);
    if (authenticated == null) {
      //see if there is an AuthenticationInfo object.  If so, the very presence of one indicates a successful
      //authentication attempt:
      AuthenticationInfo info = getAuthenticationInfo();
      authenticated = info != null;
    }
    return authenticated;
  }

  @Override
  public String resolveHost() {
    String host = null;
    ServletRequest request = resolveServletRequest();
    if (request != null) {
      host = request.getRemoteHost();
    }
    return host;
  }

  @Override
  public Serializable getSessionId() {
    throw new UnsupportedOperationException();
  }

  @Override
  public void setSessionId(Serializable sessionId) {
    throw new UnsupportedOperationException();
  }

  @Override
  public Session getSession() {
    throw new UnsupportedOperationException();
  }

  @Override
  public void setSession(Session session) {
    throw new UnsupportedOperationException();
  }

  @Override
  public boolean isSessionCreationEnabled() {
    return false;
  }

  /**
   * utility method for determine {@code PrincipalCollection} empty or not.
   *
   * @param pc {@code PrincipalCollection} instance.
   * @return empty or not.
   */
  private static boolean isEmpty(PrincipalCollection pc) {
    return pc == null || pc.isEmpty();
  }
}
