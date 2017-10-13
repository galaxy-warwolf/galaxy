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
package com.thewolf.galaxy.common.core.quartz;

import org.quartz.Trigger;
import org.quartz.spi.JobFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.util.CollectionUtils;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

/**
 * Scheduler configuration.
 */
@Configuration
@ConditionalOnProperty(name = "quartz.enabled", havingValue = "true")
@ConditionalOnBean(DataSource.class)
public class SchedulerConfiguration {

  @Value("${quartz.properties:quartz.properties}")
  private String propertiesLocation;

  /**
   * initialize job factory.
   *
   * @param applicationContext application context.
   * @return job factory.
   */
  @Bean
  public JobFactory jobFactory(ApplicationContext applicationContext) {
    AutowireCapableSpringBeanJobFactory jobFactory = new AutowireCapableSpringBeanJobFactory();
    jobFactory.setApplicationContext(applicationContext);
    return jobFactory;
  }

  /**
   * return quartz properties.
   *
   * @return quartz properties.
   * @throws IOException io exception.
   */
  public Properties quartzProperties() throws IOException {
    PropertiesFactoryBean propertiesFactoryBean = new PropertiesFactoryBean();
    propertiesFactoryBean.setLocation(new ClassPathResource(propertiesLocation));
    propertiesFactoryBean.afterPropertiesSet();
    return propertiesFactoryBean.getObject();
  }

  /**
   * initialize quartz scheduler factory bean.
   *
   * @param dataSourceProvider datasource provider.
   * @param jobFactory         job factory.
   * @param triggersProvider   quartz triggers provider.
   * @return quartz factory bean.
   * @throws IOException io exception.
   */
  @Bean
  public SchedulerFactoryBean schedulerFactoryBean(
    ObjectProvider<DataSource> dataSourceProvider, JobFactory jobFactory,
    ObjectProvider<List<Trigger>> triggersProvider) throws IOException {
    SchedulerFactoryBean factoryBean = new SchedulerFactoryBean();
    // this allows to update triggers in DB while job setting change
    factoryBean.setOverwriteExistingJobs(true);
    factoryBean.setDataSource(dataSourceProvider.getIfUnique());
    factoryBean.setJobFactory(jobFactory);
    factoryBean.setQuartzProperties(quartzProperties());
    List<Trigger> triggers = triggersProvider.getIfUnique();
    if (!CollectionUtils.isEmpty(triggers)) {
      factoryBean.setTriggers(triggers.toArray(new Trigger[triggers.size()]));
    }
    return factoryBean;
  }
}
