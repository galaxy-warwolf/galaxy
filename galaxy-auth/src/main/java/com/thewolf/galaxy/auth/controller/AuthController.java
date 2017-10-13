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
package com.thewolf.galaxy.auth.controller;

import com.thewolf.galaxy.auth.exception.ExpiredRefreshTokenException;
import com.thewolf.galaxy.auth.exception.InvalidPasswordException;
import com.thewolf.galaxy.auth.exception.InvalidRefreshTokenException;
import com.thewolf.galaxy.auth.exception.UserDisabledException;
import com.thewolf.galaxy.auth.exception.UserNotFoundException;
import com.thewolf.galaxy.auth.object.request.ExchangeRequest;
import com.thewolf.galaxy.auth.object.request.TokenRequest;
import com.thewolf.galaxy.auth.object.response.AuthUserInfo;
import com.thewolf.galaxy.auth.service.TokenService;
import com.thewolf.galaxy.auth.support.AuthenticatingFacade;
import com.thewolf.galaxy.auth.support.AuthenticatingUser;
import com.thewolf.galaxy.auth.support.RestToken;
import com.thewolf.galaxy.common.web.support.BaseController;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.MapperFacade;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.credential.PasswordService;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * Token controller provides token related operations.
 * TODO: 1. captcha support 2. distinct between access token and refresh token.
 */
@Slf4j
@Api(tags = "auth", description = "Every thing about auth",
  produces = "application/json", consumes = "application/json")
@RequestMapping(value = "/api/auth")
@RestController
@Validated
public class AuthController extends BaseController {

  @Autowired
  private PasswordService passwordService;

  @Autowired
  private AuthenticatingFacade authenticatingFacade;

  @Autowired
  private TokenService tokenService;

  @Autowired
  private MapperFacade mapperFacade;

  /**
   * Get access token.
   *
   * @param tokenRequest token request.
   * @return an instance of {@code RestToken}.
   */
  @ApiResponses({
    @ApiResponse(code = 200, message = "New signed token."),
    @ApiResponse(code = 401, message = "User not found or invalid password or user disabled."),
  })
  @ApiOperation(value = "get access token")
  @PostMapping(value = "/token", produces = "application/json", consumes = "application/json")
  public RestToken token(@Valid @RequestBody TokenRequest tokenRequest) {
    AuthenticatingUser authenticatingUser = authenticatingFacade.findUserByUsername(tokenRequest.getUsername());
    if (authenticatingUser == null) {
      throw new UserNotFoundException();
    }
    if (passwordService.passwordsMatch(tokenRequest.getPassword(), authenticatingUser.getPassword()) == false) {
      throw new InvalidPasswordException();
    }
    if (authenticatingUser.isActive() == false) {
      throw new UserDisabledException(tokenRequest.getUsername());
    }
    return tokenService.signToken(authenticatingUser);
  }

  /**
   * Exchange access token with refresh token.
   *
   * @param exchangeRequest exchange request.
   * @return an instance of {@code RestToken}.
   */
  @ApiResponses({
    @ApiResponse(code = 200, message = "New signed token."),
    @ApiResponse(code = 401, message = "User not found or invalid password or user disabled.")}
  )
  @ApiOperation(value = "exchange access token with refresh token")
  @PostMapping(value = "/exchange", produces = "application/json", consumes = "application/json")
  public RestToken exchange(@Valid @RequestBody ExchangeRequest exchangeRequest) {
    try {
      Claims claims = (Claims) tokenService.parseToken(exchangeRequest.getRefreshToken());
      AuthenticatingUser authenticatingUser = authenticatingFacade.findUserById(claims.getSubject());
      if (authenticatingUser == null) {
        throw new UserNotFoundException();
      }
      if (authenticatingUser.isActive() == false) {
        throw new UserDisabledException(authenticatingUser.getUsername());
      }
      return tokenService.signToken(authenticatingUser);
    } catch (ExpiredJwtException e) {
      throw new ExpiredRefreshTokenException();
    } catch (JwtException e) {
      throw new InvalidRefreshTokenException();
    }
  }


  /**
   * Get authenticated user info that usually for token validating.
   *
   * @return an instance of {@AuthUserInfo}.
   */
  @ApiResponses({
    @ApiResponse(code = 200, message = "Authenticated user info."),
    @ApiResponse(code = 401, message = "User not authenticated.")}
  )
  @ApiOperation(value = "validate token")
  @RequiresAuthentication
  @GetMapping(value = "/info", produces = "application/json")
  public AuthUserInfo info() {
    return mapperFacade.map(SecurityUtils.getSubject().getPrincipal(), AuthUserInfo.class);
  }

}
