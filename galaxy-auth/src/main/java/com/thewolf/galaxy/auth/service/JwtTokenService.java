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

package com.thewolf.galaxy.auth.service;

import com.google.common.base.Charsets;
import com.thewolf.galaxy.auth.config.ShiroJwtConfiguration;
import com.thewolf.galaxy.auth.support.AuthenticatingUser;
import com.thewolf.galaxy.auth.support.RestToken;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.impl.DefaultClaims;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.codec.Base64;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.time.ZonedDateTime;
import java.util.Date;

/**
 * Token manager provide parse/sign jwt token.
 */
@Slf4j
public class JwtTokenService implements TokenService {

  @Autowired
  private ShiroJwtConfiguration.JwtConfig jwtConfig;

  private SignatureAlgorithm signatureAlgorithm;

  private byte[] secretBytes;

  private byte[] refreshSecretBytes;

  private JwtParser jwtParser;

  private JwtParser refreshJwtParser;

  /**
   * Initialize token service.
   */
  @PostConstruct
  public void init() {
    signatureAlgorithm = SignatureAlgorithm.forName(jwtConfig.getAlgorithm());
    secretBytes = jwtConfig.getSecret().getBytes(Charsets.UTF_8);
    if (Base64.isBase64(secretBytes)) {
      secretBytes = Base64.decode(secretBytes);
    }
    refreshSecretBytes = jwtConfig.getRefreshSecret().getBytes(Charsets.UTF_8);
    if (Base64.isBase64(refreshSecretBytes)) {
      refreshSecretBytes = Base64.decode(refreshSecretBytes);
    }
    jwtParser = Jwts.parser().setSigningKey(secretBytes);
    refreshJwtParser = Jwts.parser().setSigningKey(refreshSecretBytes);
  }

  /**
   * Parse jwt token.
   *
   * @param token jwt token string.
   * @return {@code Claims}.
   */
  public Claims parseToken(@NonNull String token)
    throws ExpiredJwtException, UnsupportedJwtException, MalformedJwtException, SignatureException {
    return parseToken(token, false);
  }

  /**
   * Parse jwt token.
   *
   * @param token     jwt token string.
   * @param isRefresh indicate token is fresh token or not.
   * @return {@code Claims}.
   */
  public Claims parseToken(@NonNull String token, boolean isRefresh)
    throws ExpiredJwtException, UnsupportedJwtException, MalformedJwtException, SignatureException {
    JwtParser parser = isRefresh ? refreshJwtParser : jwtParser;
    return parser.parseClaimsJws(token).getBody();
  }

  /**
   * Sign token.
   *
   * @param authenticatingUser an instance of {@AuthenticatingUser}.
   * @return an instance of {@code JwtToken}.
   */
  public RestToken signToken(AuthenticatingUser authenticatingUser) {
    String subject = authenticatingUser.getId();
    long expiresIn = jwtConfig.getExpiresInSecond();
    long refreshExpiresIn = jwtConfig.getRefreshExpiresInSecond();
    ZonedDateTime now = ZonedDateTime.now();
    ZonedDateTime expiresDateTime = now.plusSeconds(expiresIn);
    ZonedDateTime refreshExpiresDateTime = now.plusSeconds(refreshExpiresIn);
    Date nowDate = Date.from(now.toInstant());
    Claims claims = new DefaultClaims();
    claims.setSubject(subject);
    claims.setIssuedAt(nowDate);
    claims.setExpiration(Date.from(expiresDateTime.toInstant()));
    String accessToken = Jwts.builder().setClaims(claims).signWith(signatureAlgorithm, secretBytes).compact();
    claims.setExpiration(Date.from(refreshExpiresDateTime.toInstant()));
    String refreshToken = Jwts.builder().setClaims(claims).signWith(signatureAlgorithm, refreshSecretBytes).compact();
    RestToken restToken = new RestToken();
    restToken.setAccessToken(accessToken);
    restToken.setExpiresIn(expiresIn);
    restToken.setRefreshToken(refreshToken);
    return restToken;
  }

}
