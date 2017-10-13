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
package com.thewolf.galaxy.auth.shiro.web.mgt;

import com.thewolf.galaxy.auth.shiro.web.subject.support.RestSubjectContext;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.mgt.AuthorizingSecurityManager;
import org.apache.shiro.mgt.SubjectFactory;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.SessionException;
import org.apache.shiro.session.mgt.SessionContext;
import org.apache.shiro.session.mgt.SessionKey;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.subject.SubjectContext;
import org.apache.shiro.web.subject.WebSubjectContext;

import java.util.Collection;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * Stateless Rest security manager.
 */
@Getter
@Setter
@Slf4j
@SuppressWarnings("unused")
public class RestSecurityManager extends AuthorizingSecurityManager {

  protected SubjectFactory subjectFactory;

  /**
   * Default no-arg constructor.
   */
  public RestSecurityManager() {
    super();
    subjectFactory = new RestSubjectFactory();
  }

  /**
   * One-arg constructor.
   *
   * @param singleRealm single realm.
   */
  public RestSecurityManager(Realm singleRealm) {
    this();
    setRealm(singleRealm);
  }

  /**
   * One-arg constructor.
   *
   * @param realms realms.
   */
  public RestSecurityManager(Collection<Realm> realms) {
    this();
    setRealms(realms);
  }

  @Override
  public Subject login(Subject subject, AuthenticationToken authenticationToken) throws AuthenticationException {
    return createSubject(authenticationToken, authenticate(authenticationToken), subject);
  }

  @Override
  public void logout(Subject subject) {
  }

  @Override
  public Session start(SessionContext sessionContext) {
    throw new UnsupportedOperationException();
  }

  @Override
  public Session getSession(SessionKey sessionKey) throws SessionException {
    throw new UnsupportedOperationException();
  }

  @Override
  public Subject createSubject(SubjectContext subjectContext) {
    //create a copy so we don't modify the argument's backing map:
    SubjectContext context = copy(subjectContext);

    //ensure that the context has a SecurityManager instance, and if not, add one:
    context = ensureSecurityManager(context);

    return doCreateSubject(context);
  }

  /**
   * Creates a {@code Subject} instance for the user represented by the given method arguments.
   *
   * @param authenticationToken the {@code AuthenticationToken} submitted for the successful authentication.
   * @param authenticationInfo  the {@code AuthenticationInfo} of a newly authenticated user.
   * @param existing            the existing {@code Subject} instance that initiated the authentication attempt.
   * @return the {@code Subject} instance that represents the context for the newly authenticated subject.
   */
  protected Subject createSubject(AuthenticationToken authenticationToken, AuthenticationInfo authenticationInfo,
                                  Subject existing) {
    SubjectContext context = createSubjectContext();
    context.setAuthenticated(true);
    context.setAuthenticationToken(authenticationToken);
    context.setAuthenticationInfo(authenticationInfo);
    if (existing != null) {
      context.setSubject(existing);
    }
    return createSubject(context);
  }

  /**
   * Creates a new {@code SubjectContext}.
   *
   * @return a newly created {@code SubjectContext}.
   */
  protected SubjectContext createSubjectContext() {
    return new RestSubjectContext();
  }

  /**
   * Copy a {@coce SubjectContext}.
   *
   * @param subjectContext a {@code SubjectContext} instance.
   * @return a clone of {@code SubjectContext}.
   */
  protected SubjectContext copy(SubjectContext subjectContext) {
    checkArgument(subjectContext instanceof WebSubjectContext);
    return new RestSubjectContext((WebSubjectContext) subjectContext);
  }

  /**
   * Determines if there's a {@code SecurityManager} instance in the context, if not, add 'this' to the context.
   *
   * @param context the subject context data that may contain a {@code SecurityManager} instance.
   * @return The SubjectContext to use to pass to a {@link SubjectFactory} for subject creation.
   */
  protected SubjectContext ensureSecurityManager(SubjectContext context) {
    if (context.resolveSecurityManager() != null) {
      log.trace("Context already contains a SecurityManager instance.  Returning.");
      return context;
    }
    log.trace("No SecurityManager found in context.  Adding self reference.");
    context.setSecurityManager(this);
    return context;
  }

  /**
   * Actually creates a {@code Subject} instance by delegating to the internal
   * {@link #getSubjectFactory() subjectFactory}.
   *
   * @param context the populated context to be used by the {@code SubjectFactory} when creating a {@code Subject}
   *                instance.
   * @return a {@code Subject} instance reflecting the data in the specified {@code SubjectContext}.
   */
  protected Subject doCreateSubject(SubjectContext context) {
    return getSubjectFactory().createSubject(context);
  }
}
