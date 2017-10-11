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

package com.thewolf.galaxy.common.web.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(prefix = "cors", value = "enabled", havingValue = "true", matchIfMissing = true)
@ConfigurationProperties("cors")
@Setter
@Getter
public class CorsProperties {
  private String[] urlMappings = {"/**"};
  private String[] allowedOrigins = {"*"};
  private String[] allowedMethods = {"GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"};
  private String[] allowedHeaders = {"Content-Type", "Accept", "Authorization", "Origin"};
  private Long maxAge = 3600L;
}
