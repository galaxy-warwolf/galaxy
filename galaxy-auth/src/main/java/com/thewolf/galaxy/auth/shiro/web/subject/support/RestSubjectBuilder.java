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
package com.thewolf.galaxy.auth.shiro.web.subject.support;

import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.SubjectContext;
import org.apache.shiro.web.subject.WebSubject;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.Serializable;

/**
 * A builder performs the same functions the {@link org.apache.shiro.subject.Subject.Builder Subject.Builder}.
 */
@SuppressWarnings("unused")
public class RestSubjectBuilder extends WebSubject.Builder {

  /**
   * Constructor.
   *
   * @param request  servlet request.
   * @param response servlet response.
   */
  public RestSubjectBuilder(ServletRequest request, ServletResponse response) {
    super(request, response);
  }

  /**
   * Constructor.
   *
   * @param securityManager security manager.
   * @param request         servlet request.
   * @param response        servlet response.
   */
  public RestSubjectBuilder(SecurityManager securityManager, ServletRequest request, ServletResponse response) {
    super(securityManager, request, response);
  }

  @Override
  protected SubjectContext newSubjectContextInstance() {
    return new RestSubjectContext();
  }

  @Override
  public RestSubjectBuilder session(Session session) {
    throw new UnsupportedOperationException();
  }

  @Override
  public RestSubjectBuilder sessionId(Serializable sessionId) {
    throw new UnsupportedOperationException();
  }

}
