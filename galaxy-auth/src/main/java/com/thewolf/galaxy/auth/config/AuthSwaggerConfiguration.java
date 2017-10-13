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
package com.thewolf.galaxy.auth.config;

import com.thewolf.galaxy.common.web.swagger.SwaggerConfiguration;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spring.web.plugins.Docket;

import static com.thewolf.galaxy.common.web.swagger.SwaggerConfiguration.newApiInfo;
import static com.thewolf.galaxy.common.web.swagger.SwaggerConfiguration.newDocket;
import static springfox.documentation.builders.PathSelectors.regex;

/**
 * Auth swagger configuration.
 */
@ConditionalOnBean(SwaggerConfiguration.class)
public class AuthSwaggerConfiguration {

  /**
   * Api info.
   *
   * @return api info.
   */
  @Bean(name = "authApiInfo")
  ApiInfo authApiInfo() {
    return newApiInfo("Galaxy Auth API", "Token based authentication strategy", "1.0");
  }

  /**
   * Galaxy Auth api docket.
   *
   * @param apiInfo api info.
   * @return coupons docket.
   */
  @Bean
  public Docket authApi(@Qualifier("authApiInfo") ApiInfo apiInfo) {
    return newDocket("auth-api", apiInfo, regex("/api/auth/.*"));
  }
}
