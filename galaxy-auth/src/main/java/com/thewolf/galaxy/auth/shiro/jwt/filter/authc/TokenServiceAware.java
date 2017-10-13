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
package com.thewolf.galaxy.auth.shiro.jwt.filter.authc;

import com.thewolf.galaxy.auth.service.TokenService;

/**
 * Interface to be implemented by any object that wishes to be notified
 * of the {@link TokenService} that it runs in.
 */
public interface TokenServiceAware {

  /**
   * Set {@code TokenService}.
   *
   * @param tokenService {@code TokenService} implementation.
   */
  void setTokenService(TokenService tokenService);

}
