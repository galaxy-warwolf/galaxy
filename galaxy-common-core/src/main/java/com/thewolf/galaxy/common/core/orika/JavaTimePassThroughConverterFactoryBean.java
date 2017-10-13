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
package com.thewolf.galaxy.common.core.orika;

import ma.glasnost.orika.converter.builtin.PassThroughConverter;
import org.springframework.beans.factory.config.AbstractFactoryBean;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.ZonedDateTime;

/**
 * Java 8 time core pass through converter.
 */
@Component
@SuppressWarnings("unused")
public class JavaTimePassThroughConverterFactoryBean
  extends AbstractFactoryBean<PassThroughConverter> {

  @Override
  public Class<?> getObjectType() {
    return PassThroughConverter.class;
  }

  @Override
  protected PassThroughConverter createInstance() throws Exception {
    return new PassThroughConverter(
      ZonedDateTime.class, LocalDateTime.class, LocalDate.class, Instant.class, Period.class);
  }
}
