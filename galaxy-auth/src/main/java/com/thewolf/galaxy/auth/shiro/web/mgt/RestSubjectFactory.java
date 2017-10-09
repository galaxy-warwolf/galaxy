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

import com.thewolf.galaxy.auth.shiro.web.subject.support.RestDelegatingSubject;
import com.thewolf.galaxy.auth.shiro.web.subject.support.RestSubjectContext;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.mgt.SubjectFactory;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.subject.SubjectContext;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * Rest {@code SubjectFactory} implementation that creates {@code Subject} instance.
 */
public class RestSubjectFactory implements SubjectFactory {

  @Override
  public Subject createSubject(SubjectContext subjectContext) {
    checkArgument(subjectContext instanceof RestSubjectContext);
    RestSubjectContext context = (RestSubjectContext) subjectContext;
    SecurityManager securityManager = context.resolveSecurityManager();
    PrincipalCollection principals = context.resolvePrincipals();
    boolean authenticated = context.resolveAuthenticated();
    String host = context.resolveHost();
    ServletRequest request = context.resolveServletRequest();
    ServletResponse response = context.resolveServletResponse();
    return new RestDelegatingSubject(principals, authenticated, host, null, request, response, securityManager);
  }

}
