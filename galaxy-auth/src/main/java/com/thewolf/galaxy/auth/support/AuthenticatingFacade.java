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
package com.thewolf.galaxy.auth.support;

import java.util.Set;

/**
 * Interface for authenticating service.
 */
public interface AuthenticatingFacade {

  /**
   * Find user by user id.
   *
   * @param id user id.
   * @return an instance of {@code AuthenticatingUser} or null if not found.
   */
  AuthenticatingUser findUserById(String id);

  /**
   * Find user by user name.
   *
   * @param username user name.
   * @return an instance of {@code AuthenticatingUser} or null of not found.
   */
  AuthenticatingUser findUserByUsername(String username);

  /**
   * Find roles by user id.
   *
   * @param id user id.
   * @return roles list or empty set while there's none.
   */
  Set<String> findRolesByUserId(String id);

  /**
   * Find permissions by user id.
   *
   * @param id user id.
   * @return string permission list or empty set while there's none.
   */
  Set<String> findPermissionsByUserId(String id);
}
