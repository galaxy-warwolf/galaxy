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

package com.thewolf.galaxy.auth.support;

import java.time.ZonedDateTime;

/**
 * Interface for authenticating user.
 */
public interface AuthenticatingUser {
  /**
   * Get user id.
   *
   * @return user id.
   */
  String getId();

  /**
   * Get user name.
   *
   * @return user name.
   */
  String getUsername();

  /**
   * Get hashed user password.
   *
   * @return hashed user password.
   */
  String getPassword();

  /**
   * Indicates token valid after specific time.
   *
   * @return a time instance.
   */
  ZonedDateTime getValidAfter();

  /**
   * Get user status.
   *
   * @return boolean value indicates user active or not.
   */
  boolean isActive();
}
