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
package com.thewolf.galaxy.common.core.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * Application context utils.
 */
@Component
@SuppressWarnings("unused")
public class SpringContextUtils implements ApplicationContextAware {

  private static ApplicationContext applicationContext;

  /**
   * Set application context.
   *
   * @param applicationContext application context.
   */
  private static void setApplicationContextStatic(ApplicationContext applicationContext) {
    SpringContextUtils.applicationContext = applicationContext;
  }

  @Override
  public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
    setApplicationContextStatic(applicationContext);
  }

  /**
   * Get application context.
   *
   * @return application context.
   */
  public static ApplicationContext getApplicationContext() {
    return applicationContext;
  }

  /**
   * Get bean by name.
   *
   * @param name bean name.
   * @return bean.
   */
  public static Object getBean(String name) {
    return applicationContext.getBean(name);
  }

  /**
   * Get bean by name and type.
   *
   * @param name         bean name.
   * @param requiredType type the bean must match.
   * @param <T>          bean class.
   * @return bean.
   */
  public static <T> T getBean(String name, Class<T> requiredType) {
    return applicationContext.getBean(name, requiredType);
  }

  /**
   * Get bean by type.
   *
   * @param requiredType type the bean must match.
   * @param <T>          bean class.
   * @return bean.
   */
  public static <T> T getBean(Class<T> requiredType) {
    return applicationContext.getBean(requiredType);
  }

  /**
   * Contains bean or not.
   *
   * @param name bean name.
   * @return contains or not.
   */
  public static boolean containsBean(String name) {
    return applicationContext.containsBean(name);
  }

  /**
   * Is bean singleton or not.
   *
   * @param name bean name.
   * @return is singleton or not.
   */
  public static boolean isSingleton(String name) {
    return applicationContext.isSingleton(name);
  }

  /**
   * Get bean type.
   *
   * @param name bean name.
   * @return bean type.
   */
  public static Class<? extends Object> getType(String name) {
    return applicationContext.getType(name);
  }
}
