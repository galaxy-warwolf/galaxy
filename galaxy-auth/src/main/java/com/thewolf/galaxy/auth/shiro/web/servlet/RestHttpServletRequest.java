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
package com.thewolf.galaxy.auth.shiro.web.servlet;

import lombok.NonNull;
import lombok.Value;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpSession;
import java.security.Principal;

/**
 * Rest {@code HttpServletRequestWrapper}.
 */
public class RestHttpServletRequest extends HttpServletRequestWrapper {

  /**
   * Constructor.
   *
   * @param request original {@code HttpServletRequest}.
   */
  public RestHttpServletRequest(HttpServletRequest request) {
    super(request);
  }

  @Override
  public String getRemoteUser() {
    String remoteUser;
    Object scPrincipal = getSubjectPrincipal();
    if (scPrincipal != null) {
      if (scPrincipal instanceof String) {
        return (String) scPrincipal;
      } else if (scPrincipal instanceof Principal) {
        remoteUser = ((Principal) scPrincipal).getName();
      } else {
        remoteUser = scPrincipal.toString();
      }
    } else {
      remoteUser = super.getRemoteUser();
    }
    return remoteUser;
  }

  /**
   * Get subject principal.
   *
   * @return subject principal.
   */
  protected Object getSubjectPrincipal() {
    Object userPrincipal = null;
    Subject subject = getSubject();
    if (subject != null) {
      userPrincipal = subject.getPrincipal();
    }
    return userPrincipal;
  }

  /**
   * Get subject.
   *
   * @return subject.
   */
  protected Subject getSubject() {
    return SecurityUtils.getSubject();
  }

  @Override
  public boolean isUserInRole(String s) {
    Subject subject = getSubject();
    boolean inRole = (subject != null && subject.hasRole(s));
    if (!inRole) {
      inRole = super.isUserInRole(s);
    }
    return inRole;
  }

  @Override
  public Principal getUserPrincipal() {
    Principal userPrincipal;
    Object scPrincipal = getSubjectPrincipal();
    if (scPrincipal != null) {
      if (scPrincipal instanceof Principal) {
        userPrincipal = (Principal) scPrincipal;
      } else {
        userPrincipal = new RestHttpServletRequest.ObjectPrincipal(scPrincipal);
      }
    } else {
      userPrincipal = super.getUserPrincipal();
    }
    return userPrincipal;
  }


  @Override
  public HttpSession getSession(boolean create) {
    HttpSession session = super.getSession(false);
    if (session == null && create) {
      throw new UnsupportedOperationException();
    }
    return session;
  }

  @Override
  public HttpSession getSession() {
    return getSession(true);
  }

  /**
   * Object Principal.
   */
  @Value
  private static class ObjectPrincipal implements java.security.Principal {
    private final Object object;

    /**
     * All-arg constructor.
     *
     * @param object object.
     */
    public ObjectPrincipal(@NonNull Object object) {
      this.object = object;
    }

    @Override
    public String getName() {
      return getObject().toString();
    }
  }
}
