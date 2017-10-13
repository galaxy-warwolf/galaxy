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
package com.thewolf.galaxy.common.web.validators;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Enumeration validator.
 */
@SuppressWarnings({"JavadocMethod"})
@Documented
@Constraint(validatedBy = EnumerationValidatorImpl.class)
@Target({FIELD, PARAMETER})
@Retention(RUNTIME)
public @interface EnumerationValidator {

  Class<? extends Enum<?>> value();

  boolean nullable() default false;

  String message() default "Enum value is not valid";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
