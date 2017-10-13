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
package com.thewolf.galaxy.auth.shiro.web.mgt;

import com.google.common.collect.Maps;
import com.thewolf.galaxy.auth.shiro.jwt.filter.authc.JWTAuthenticationFilter;
import com.thewolf.galaxy.auth.shiro.jwt.filter.authz.RestPermissionsAuthorizationFilter;
import com.thewolf.galaxy.auth.shiro.jwt.filter.authz.RestRolesAuthorizationFilter;
import org.apache.shiro.util.ClassUtils;

import javax.servlet.Filter;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import java.util.Map;

/**
 * Rest style default filters definition.
 */
public enum RestDefaultFilter {
  jwt(JWTAuthenticationFilter.class),
  perms(RestPermissionsAuthorizationFilter.class),
  roles(RestRolesAuthorizationFilter.class);

  private final Class<? extends Filter> filterClass;

  RestDefaultFilter(Class<? extends Filter> filterClass) {
    this.filterClass = filterClass;
  }

  public Filter newInstance() {
    return (Filter) ClassUtils.newInstance(this.filterClass);
  }

  @SuppressWarnings("unused")
  public Class<? extends Filter> getFilterClass() {
    return this.filterClass;
  }

  /**
   * create filter maps based on default filters.
   *
   * @param config filter configuration.
   * @return a default filter map.
   */
  @SuppressWarnings("unused")
  public static Map<String, Filter> createInstanceMap(FilterConfig config) {
    Map<String, Filter> filters = Maps.newLinkedHashMapWithExpectedSize(values().length);
    for (RestDefaultFilter defaultFilter : values()) {
      Filter filter = defaultFilter.newInstance();
      if (config != null) {
        try {
          filter.init(config);
        } catch (ServletException e) {
          String msg = "Unable to correctly init default filter instance of type "
            + filter.getClass().getName();
          throw new IllegalStateException(msg, e);
        }
      }
      filters.put(defaultFilter.name(), filter);
    }
    return filters;
  }
}
