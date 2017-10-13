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
package com.thewolf.galaxy;

import com.thewolf.galaxy.auth.fake.FakeAuthenticatingFacade;
import com.thewolf.galaxy.auth.support.AuthenticatingFacade;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

import java.util.TimeZone;

@SpringBootApplication
@EnableAspectJAutoProxy
@EnableCaching
@SuppressWarnings("unused")
public class GalaxyApplication {

  /**
   * Main entry point for galaxy application.
   *
   * @param args command line args.
   */
  public static void main(String[] args) {
    // manually set default timezone
    TimeZone.setDefault(TimeZone.getTimeZone("Asia/Shanghai"));
    SpringApplication.run(GalaxyApplication.class, args);
  }

  @Bean
  AuthenticatingFacade authenticatingFacade() {
    return new FakeAuthenticatingFacade();
  }

}
