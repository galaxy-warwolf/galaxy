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
package com.thewolf.galaxy.common.web.exception.base;

/**
 * Base exception for service unavailable.
 */
@SuppressWarnings("unused")
public class ServiceUnavailableException extends ApplicationException {

  public static final String DEFAULT_ERROR_CODE = "errors.galaxy.service_unavailable";

  public static final int HTTP_STATUS_CODE = 500;

  /**
   * constructor.
   *
   * @param numericErrorCode numeric error code.
   * @param errorCode        error code.
   * @param pattern          message pattern.
   * @param args             args.
   */
  public ServiceUnavailableException(
    int numericErrorCode, String errorCode, String pattern, Object... args) {
    super(
      HTTP_STATUS_CODE,
      numericErrorCode,
      errorCode != null ? errorCode : DEFAULT_ERROR_CODE,
      pattern,
      args);
  }
}
