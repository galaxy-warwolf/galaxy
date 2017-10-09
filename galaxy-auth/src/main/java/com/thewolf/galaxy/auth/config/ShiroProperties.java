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

package com.thewolf.galaxy.auth.config;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Shiro configuration from application configuration.
 */
@ConditionalOnWebApplication
@ConditionalOnProperty(prefix = "shiro", value = "enabled", havingValue = "true")
@ConfigurationProperties("shiro")
@Setter
@Getter
public class ShiroProperties {

  private List<String> chains = Lists.newLinkedList();

  private List<String> filterUrlPatterns = Lists.newLinkedList();

  /**
   * Get filter chains.
   *
   * @return filter chains.
   */
  public Map<String, String> getFilterChains() {
    return chains.stream().map(chain -> Splitter.on("=").limit(2).splitToList(chain))
      .collect(Collectors.toMap(list -> list.get(0), list -> list.get(1)));
  }

}
