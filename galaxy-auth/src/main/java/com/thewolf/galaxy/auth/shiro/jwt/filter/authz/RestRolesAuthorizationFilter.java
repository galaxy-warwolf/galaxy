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
package com.thewolf.galaxy.auth.shiro.jwt.filter.authz;

import org.apache.shiro.web.filter.authz.RolesAuthorizationFilter;
import org.apache.shiro.web.util.WebUtils;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Rest style {@code RolesAuthorizationFilter} that will issue unauthorized http code while access denied.
 */
public class RestRolesAuthorizationFilter extends RolesAuthorizationFilter {

  @Override
  protected boolean onAccessDenied(ServletRequest servletRequest, ServletResponse servletResponse)
    throws IOException {
    HttpServletResponse httpResponse = WebUtils.toHttp(servletResponse);
    httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    return false;
  }
}
