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
package com.thewolf.galaxy.common.web.exception.handler;

import lombok.Getter;
import lombok.Setter;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Validation error message.
 */
@Getter
@Setter
public class ValidationErrorResponseEntity {

  private List<ValidationFieldErrorEntity> errors = new ArrayList<>();

  /**
   * New from validation error response.
   *
   * @param objectErrors collection of validation object error.
   */
  public static ValidationErrorResponseEntity fromObjectErrors(Collection<ObjectError> objectErrors) {
    ValidationErrorResponseEntity responseEntity = new ValidationErrorResponseEntity();
    objectErrors.forEach(
      objectError -> {
        ValidationFieldErrorEntity entity = new ValidationFieldErrorEntity();
        entity.setCode(objectError.getCode());
        entity.setDefaultMessage(objectError.getDefaultMessage());
        if (objectError instanceof FieldError) {
          entity.setField(((FieldError) objectError).getField());
        }
        responseEntity.getErrors().add(entity);
      });
    return responseEntity;
  }

  @Getter
  @Setter
  public static class ValidationFieldErrorEntity {
    private String field;

    private String defaultMessage;

    private String code;
  }
}
