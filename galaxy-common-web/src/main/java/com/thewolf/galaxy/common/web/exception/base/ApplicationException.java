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

import lombok.Getter;
import lombok.Setter;

/**
 * Application exception, the general exception for galaxy.
 */
@Getter
@Setter
@SuppressWarnings("unused")
public abstract class ApplicationException extends BaseException {

  public static final String DEFAULT_ERROR_CODE = "errors.galaxy.unknown";

  public static final int DEFAULT_NUMERIC_ERROR_CODE = -1;

  protected int numericErrorCode = DEFAULT_NUMERIC_ERROR_CODE;

  protected String errorCode = DEFAULT_ERROR_CODE;

  protected int httpStatusCode = 500;

  /**
   * constructor.
   *
   * @param httpStatusCode   http status code.
   * @param numericErrorCode numeric error code.
   * @param errorCode        string error code.
   * @param pattern          message pattern.
   * @param args             message args.
   */
  public ApplicationException(
    int httpStatusCode, int numericErrorCode, String errorCode, String pattern, Object... args) {
    super(pattern, args);
    this.httpStatusCode = httpStatusCode;
    this.numericErrorCode = numericErrorCode;
    this.errorCode = errorCode != null ? errorCode : DEFAULT_ERROR_CODE;
  }
}
