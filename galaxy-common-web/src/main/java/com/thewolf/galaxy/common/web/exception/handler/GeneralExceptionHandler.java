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
package com.thewolf.galaxy.common.web.exception.handler;

import com.thewolf.galaxy.common.web.exception.base.ApplicationException;
import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * General exception handler.
 */
@Slf4j
@ControllerAdvice
@SuppressWarnings("unused")
public class GeneralExceptionHandler {

  @Autowired
  private MapperFacade mapperFacade;

  /**
   * handle application exception.
   *
   * @param exception application exception.
   * @param response  http servlet response.
   * @return application error response.
   */
  @ExceptionHandler(ApplicationException.class)
  @ResponseBody
  public ApplicationErrorResponseEntity handleApplicationException(
    ApplicationException exception, HttpServletResponse response) {
    response.setStatus(exception.getHttpStatusCode());
    return mapperFacade.map(exception, ApplicationErrorResponseEntity.class);
  }

  /**
   * handle method argument not valid exception.
   *
   * @param exception method argument not valid exception.
   * @param response  http servlet response.
   * @return validation error response.
   */
  @ExceptionHandler(MethodArgumentNotValidException.class)
  @ResponseBody
  public ValidationErrorResponseEntity handleValidationException(
    MethodArgumentNotValidException exception, HttpServletResponse response) {
    response.setStatus(422);
    return ValidationErrorResponseEntity.fromObjectErrors(exception.getBindingResult().getAllErrors());
  }

  /**
   * handle bad request.
   */
  @ExceptionHandler({
    HttpMessageNotReadableException.class,
    MethodArgumentTypeMismatchException.class
  })
  @ResponseBody
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public void handleBadRequest() {
  }

  /**
   * Handle request/path validation error.
   *
   * @param exception constraint violation exception.
   * @return error map.
   */
  @ExceptionHandler
  @ResponseBody
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public Map handle(ConstraintViolationException exception) {
    return error(
      exception
        .getConstraintViolations()
        .stream()
        .map(ConstraintViolation::getMessage)
        .collect(Collectors.toList()));
  }

  /**
   * generate error map.
   *
   * @param message error object.
   * @return error map.
   */
  private Map error(Object message) {
    return Collections.singletonMap("errors", message);
  }
}
