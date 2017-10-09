/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package com.thewolf.galaxy.auth;

import com.thewolf.galaxy.auth.config.AuthSwaggerConfiguration;
import com.thewolf.galaxy.auth.config.ShiroAnnotationsConfiguration;
import com.thewolf.galaxy.auth.config.ShiroJwtConfiguration;
import com.thewolf.galaxy.auth.config.ShiroProperties;
import com.thewolf.galaxy.auth.config.ShiroWebMvcConfiguration;
import org.apache.shiro.authc.credential.DefaultPasswordService;
import org.apache.shiro.authc.credential.PasswordService;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * Rest shiro configuration.
 */
@SuppressWarnings("JavadocMethod")
@Configuration
@EnableConfigurationProperties(ShiroProperties.class)
@ConditionalOnProperty(value = "shiro.enabled", havingValue = "true")
@Import({ShiroJwtConfiguration.class, ShiroAnnotationsConfiguration.class,
  ShiroWebMvcConfiguration.class, AuthSwaggerConfiguration.class})
public class ShiroAutoConfigure {

  @Bean
  @ConditionalOnMissingBean(PasswordService.class)
  public PasswordService passwordService() {
    return new DefaultPasswordService();
  }

  @Bean
  public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
    return new LifecycleBeanPostProcessor();
  }

}
