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
package com.thewolf.galaxy.common.web.validators;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.ArrayList;
import java.util.List;

/**
 * EnumerationValidator implementation.
 */
public class EnumerationValidatorImpl implements ConstraintValidator<EnumerationValidator, String> {

  boolean nullable = false;
  List<String> values = null;

  @Override
  public void initialize(EnumerationValidator enumerationValidator) {
    nullable = enumerationValidator.nullable();
    values = new ArrayList<>();
    Class<? extends Enum<?>> enumClass = enumerationValidator.value();

    Enum[] enumConstants = enumClass.getEnumConstants();

    for (Enum enumConstant : enumConstants) {
      values.add(enumConstant.toString());
    }
  }

  @Override
  public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
    if (value == null) {
      if (nullable) {
        return true;
      } else {
        return false;
      }
    }
    if (!values.contains(value)) {
      return false;
    }
    return true;
  }
}
