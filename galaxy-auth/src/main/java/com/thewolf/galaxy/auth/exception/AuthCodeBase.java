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
package com.thewolf.galaxy.auth.exception;

/**
 * Code base for galaxy authentication.
 */
public interface AuthCodeBase {

  /**
   * Numeric error codes range base for auth service specific exceptions.
   */
  int NUMERIC_ERROR_CODE_RANGE_BASE = 201000;

  /**
   * Numeric error codes range base for "bad request" family exceptions.
   */
  int NUMERIC_UNAUTHORIZED_RANGE_BASE = NUMERIC_ERROR_CODE_RANGE_BASE;

  /**
   * Numeric error codes range base for "bad request" family exceptions.
   */
  int NUMERIC_BAD_REQUEST_RANGE_BASE = NUMERIC_ERROR_CODE_RANGE_BASE + 100;

  /**
   * Numeric error codes range base for "not found" family exceptions.
   */
  int NUMERIC_NOT_FOUND_ERROR_BASE = NUMERIC_ERROR_CODE_RANGE_BASE + 400;

  /**
   * Numeric error codes range base for "conflict" family exceptions.
   */
  int NUMERIC_CONFLICT_ERROR_BASE = NUMERIC_ERROR_CODE_RANGE_BASE + 900;

  /**
   * String error codes base for auth service specific exceptions.
   */
  String ERROR_CODE_BASE = "errors.galaxy.auth.";

  /**
   * String error codes base for "bad request" family exceptions.
   */
  String UNAUTHORIZED_ERROR_BASE = ERROR_CODE_BASE + "unauthorized.";

  /**
   * String error codes base for "bad request" family exceptions.
   */
  String BAD_REQUEST_ERROR_BASE = ERROR_CODE_BASE + "bad_request.";

  /**
   * String error codes base for "not found" family exceptions.
   */
  String NOT_FOUND_ERROR_BASE = ERROR_CODE_BASE + "not_found.";

  /**
   * String error codes base for "conflict" family exceptions.
   */
  String CONFLICT_ERROR_BASE = ERROR_CODE_BASE + "conflict.";
}
