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
package com.thewolf.galaxy.auth.fake;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.thewolf.galaxy.auth.support.AuthenticatingFacade;
import com.thewolf.galaxy.auth.support.AuthenticatingUser;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.apache.shiro.authc.credential.DefaultPasswordService;
import org.apache.shiro.authc.credential.PasswordService;

import java.time.ZonedDateTime;
import java.util.Map;
import java.util.Set;

/**
 * Fake {@code AuthenticatingFacade} that for test.
 */
public class FakeAuthenticatingFacade implements AuthenticatingFacade {

  private PasswordService passwordService = new DefaultPasswordService();

  private Map<String, FakeAuthenticatingUser> users = Maps.newHashMap();

  /**
   * Default constructor.
   */
  public FakeAuthenticatingFacade() {
    ZonedDateTime validAfter = ZonedDateTime.now().minusMonths(1);
    users.put("admin", new FakeAuthenticatingUser("admin", "admin",
      passwordService.encryptPassword("admin"), validAfter, true,
      Sets.newHashSet("admin", "user"), Sets.newHashSet("fake")));
    users.put("user", new FakeAuthenticatingUser("user", "user",
      passwordService.encryptPassword("user"), validAfter, true,
      Sets.newHashSet("user"), Sets.newHashSet("fake:create", "fake:update")));
    users.put("guest", new FakeAuthenticatingUser("guest", "guest",
      passwordService.encryptPassword("guest"), validAfter, true,
      Sets.newHashSet("user"), Sets.newHashSet("fake:read")));
  }

  @Override
  public FakeAuthenticatingUser findUserById(String id) {
    return users.get(id);
  }

  @Override
  public AuthenticatingUser findUserByUsername(String username) {
    return users.get(username);
  }

  @Override
  public Set<String> findRolesByUserId(String id) {
    FakeAuthenticatingUser user = users.get(id);
    return user != null ? user.getRoles() : Sets.newHashSet();
  }

  @Override
  public Set<String> findPermissionsByUserId(String id) {
    FakeAuthenticatingUser user = users.get(id);
    return user != null ? user.getPermissions() : Sets.newHashSet();
  }

  /**
   * Fake {@code AuthenticatingUser}.
   */
  @Getter
  @Setter
  @AllArgsConstructor
  public static class FakeAuthenticatingUser implements AuthenticatingUser {

    private String id;

    private String username;

    private String password;

    private ZonedDateTime validAfter;

    private boolean active;

    private Set<String> roles;

    private Set<String> permissions;

  }
}
