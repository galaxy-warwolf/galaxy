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

package com.thewolf.galaxy.auth.shiro.jwt.realm;

import com.thewolf.galaxy.auth.shiro.jwt.filter.authc.JWTAuthenticationToken;
import com.thewolf.galaxy.auth.support.AuthenticatingFacade;
import com.thewolf.galaxy.auth.support.AuthenticatingUser;
import io.jsonwebtoken.Claims;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.DisabledAccountException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

import java.util.Date;
import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * JWT realm implementation.
 */
public class JWTRealm extends AuthorizingRealm {

  private final AuthenticatingFacade authenticatingFacade;

  /**
   * Constructor.
   *
   * @param authenticatingFacade an instance of {@code AuthenticatingFacade}.
   */
  public JWTRealm(AuthenticatingFacade authenticatingFacade) {
    checkNotNull(authenticatingFacade);
    this.authenticatingFacade = authenticatingFacade;
  }

  @Override
  public boolean supports(AuthenticationToken token) {
    return token != null && token instanceof JWTAuthenticationToken;
  }

  @Override
  protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken)
    throws AuthenticationException {
    JWTAuthenticationToken jwtAuthenticationToken = (JWTAuthenticationToken) authenticationToken;
    AuthenticatingUser authenticatingUser = authenticatingFacade.findUserById(jwtAuthenticationToken.getUserId());
    if (authenticatingUser == null || authenticatingUser.getValidAfter() == null) {
      throw new UnknownAccountException("Account not found.");
    } else if (authenticatingUser.isActive() == false) {
      throw new DisabledAccountException("Account disabled.");
    }
    Date validAfter = Date.from(authenticatingUser.getValidAfter().toInstant());
    Claims claims = jwtAuthenticationToken.getClaims();
    if (claims.getIssuedAt().before(validAfter)) {
      throw new IncorrectCredentialsException("Invalid token.");
    }
    return new SimpleAuthenticationInfo(authenticatingUser, authenticationToken.getCredentials(),
      getName());
  }

  @Override
  protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
    AuthenticatingUser authenticatingUser = (AuthenticatingUser) principalCollection.getPrimaryPrincipal();
    String userId = authenticatingUser.getId();
    Set<String> roles = authenticatingFacade.findRolesByUserId(userId);
    Set<String> permissions = authenticatingFacade.findPermissionsByUserId(userId);
    SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo(roles);
    simpleAuthorizationInfo.setStringPermissions(permissions);
    return simpleAuthorizationInfo;
  }

}
