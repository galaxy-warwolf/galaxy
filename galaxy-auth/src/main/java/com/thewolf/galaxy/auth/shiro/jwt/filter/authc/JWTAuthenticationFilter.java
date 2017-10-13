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
package com.thewolf.galaxy.auth.shiro.jwt.filter.authc;

import com.thewolf.galaxy.auth.service.TokenService;
import com.thewolf.galaxy.auth.support.AuthConstants;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.ExpiredCredentialsException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.web.filter.authc.AuthenticatingFilter;
import org.apache.shiro.web.util.WebUtils;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * JWT authenticating filter that will extract/parse jwt token from header and passed wrapped authentication token to
 * real realm.
 */
@Slf4j
@Getter
@Setter
public class JWTAuthenticationFilter extends AuthenticatingFilter implements TokenServiceAware {

  private TokenService tokenService;

  @Override
  public void setTokenService(TokenService tokenService) {
    this.tokenService = tokenService;
  }

  @Override
  protected AuthenticationToken createToken(ServletRequest servletRequest, ServletResponse servletResponse)
    throws Exception {
    String authorizationHeader = getAuthzHeader(servletRequest);
    log.debug("Attempting to execute login with auth header");
    String jwtToken = getJwtTokenString(authorizationHeader);
    if (jwtToken == null) {
      throw new IncorrectCredentialsException("Invalid token.");
    }
    try {
      Claims claims = (Claims) tokenService.parseToken(jwtToken);
      return new JWTAuthenticationToken(claims.getSubject(), claims);
    } catch (ExpiredJwtException e) {
      throw new ExpiredCredentialsException(e);
    } catch (Exception e) {
      throw new IncorrectCredentialsException(e);
    }
  }

  @Override
  protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
    return getSubject(request, response).isAuthenticated() || !isLoginRequest(request, response);
  }

  @Override
  protected boolean onAccessDenied(ServletRequest servletRequest, ServletResponse servletResponse)
    throws Exception {
    try {
      if (isLoginAttempt(servletRequest, servletResponse)) {
        return executeLogin(servletRequest, servletResponse);
      }
      return false;
    } catch (Exception e) {
      return onLoginFailure(servletResponse);
    }
  }

  @Override
  protected boolean onLoginFailure(AuthenticationToken token, AuthenticationException e,
                                   ServletRequest request, ServletResponse response) {
    HttpServletResponse httpResponse = WebUtils.toHttp(response);
    httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    return false;
  }

  /**
   * On login fail.
   *
   * @param response servlet response.
   * @return boolean value indicate login fail or not.
   */
  protected boolean onLoginFailure(ServletResponse response) {
    HttpServletResponse httpResponse = WebUtils.toHttp(response);
    httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    return false;
  }

  @Override
  protected final boolean isLoginRequest(ServletRequest request, ServletResponse response) {
    return this.isLoginAttempt(request, response);
  }

  /**
   * Detect whether login attempt.
   *
   * @param request  servlet request.
   * @param response servlet response.
   * @return boolean value indicates login attempt.
   */
  @SuppressWarnings("unused")
  protected boolean isLoginAttempt(ServletRequest request, ServletResponse response) {
    String authzHeader = getAuthzHeader(request);
    return authzHeader != null && authzHeader.startsWith(AuthConstants.AUTHORIZATION_TYPE);
  }

  /**
   * Get authorization header.
   *
   * @param request servlet request.
   * @return authorization header.
   */
  protected String getAuthzHeader(ServletRequest request) {
    HttpServletRequest httpRequest = WebUtils.toHttp(request);
    return httpRequest.getHeader(AuthConstants.AUTHORIZATION_HEADER);
  }

  /**
   * Get jwt token from authorization header.
   *
   * @param authorizationHeader authorization header.
   * @return jwt token.
   */
  protected String getJwtTokenString(@NonNull String authorizationHeader) {
    String[] splits = authorizationHeader.split(" ");
    if (splits.length < 2) {
      return null;
    }
    return splits[1];
  }
}
