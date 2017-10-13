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
package com.thewolf.galaxy.common.web.swagger;

import com.google.common.base.Predicate;
import io.swagger.annotations.Api;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.ResponseEntity;
import springfox.bean.validators.configuration.BeanValidatorPluginsConfiguration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.List;

/**
 * swagger configuration.
 */
@Configuration
@ConditionalOnProperty(value = "swagger.enabled", havingValue = "true")
@Import(BeanValidatorPluginsConfiguration.class)
@EnableSwagger2
@SuppressWarnings("unused")
public class SwaggerConfiguration {

  /**
   * build global operation parameters.
   *
   * @return parameter list.
   */
  public static List<Parameter> globalOperationParameters() {
    return Arrays.asList(
      new ParameterBuilder()
        .name("Authorization")
        .parameterType("header")
        .modelRef(new ModelRef("string"))
        .build());
  }

  /**
   * New api info.
   *
   * @param title       api title.
   * @param description api description.
   * @param version     api version.
   * @return a new api info.
   */
  public static ApiInfo newApiInfo(String title, String description, String version) {
    return new ApiInfoBuilder()
      .title(title)
      .description(description)
      .version(version)
      .license("Apache License Version 2.0")
      .build();
  }

  /**
   * New swagger docket.
   *
   * @param groupName group name.
   * @param apiInfo   api info.
   * @param paths     path selector.
   * @return a new docket.
   */
  public static Docket newDocket(String groupName, ApiInfo apiInfo, Predicate<String> paths) {
    return new Docket(DocumentationType.SWAGGER_2)
      .groupName(groupName)
      .select()
      .apis(RequestHandlerSelectors.withClassAnnotation(Api.class))
      .paths(PathSelectors.any())
      .build()
      .pathMapping("/")
      .directModelSubstitute(LocalDate.class, String.class)
      .directModelSubstitute(ZonedDateTime.class, String.class)
      .genericModelSubstitutes(ResponseEntity.class)
      .useDefaultResponseMessages(false)
      .globalOperationParameters(globalOperationParameters())
      .apiInfo(apiInfo);
  }
}
