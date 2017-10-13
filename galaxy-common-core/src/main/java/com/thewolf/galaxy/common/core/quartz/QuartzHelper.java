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
package com.thewolf.galaxy.common.core.quartz;

import org.quartz.CronTrigger;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.SimpleTrigger;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;
import org.springframework.scheduling.quartz.SimpleTriggerFactoryBean;

/**
 * Helper class for quartz create trigger and job detail.
 */
@SuppressWarnings("unused")
public class QuartzHelper {

  /**
   * create job detail factory bean.
   *
   * @param jobClass job class.
   * @return job detail factory bean.
   */
  public static JobDetailFactoryBean jobDetailFactoryBean(Class jobClass) {
    return jobDetailFactoryBean(jobClass, null, null, null);
  }

  /**
   * create job detail factory bean.
   *
   * @param jobClass    job class.
   * @param name        job name.
   * @param group       job group.
   * @param description job description.
   * @return job detail factory bean.
   */
  public static JobDetailFactoryBean jobDetailFactoryBean(Class jobClass, String name,
                                                          String group, String description) {
    JobDetailFactoryBean jobDetailFactoryBean = new JobDetailFactoryBean();
    jobDetailFactoryBean.setJobClass(jobClass);
    jobDetailFactoryBean.setName(name);
    jobDetailFactoryBean.setGroup(group);
    jobDetailFactoryBean.setDescription(description);
    // job has to be durable to db
    jobDetailFactoryBean.setDurability(true);
    return jobDetailFactoryBean;
  }

  /**
   * Create simple trigger factory bean.
   *
   * @param jobDetail    job detail.
   * @param intervalInMs interval in ms.
   * @param delayInMs    delay in ms.
   * @return simple trigger factory bean.
   */
  public static SimpleTriggerFactoryBean simpleTriggerFactoryBean(
    JobDetail jobDetail, long delayInMs, long intervalInMs) {
    SimpleTriggerFactoryBean simpleTriggerFactoryBean = new SimpleTriggerFactoryBean();
    simpleTriggerFactoryBean.setJobDetail(jobDetail);
    JobKey jobKey = jobDetail.getKey();
    simpleTriggerFactoryBean.setName(jobKey.getName());
    simpleTriggerFactoryBean.setGroup(jobKey.getGroup());
    simpleTriggerFactoryBean.setDescription(jobDetail.getDescription());
    simpleTriggerFactoryBean.setStartDelay(delayInMs);
    simpleTriggerFactoryBean.setRepeatInterval(intervalInMs);
    simpleTriggerFactoryBean.setRepeatCount(SimpleTrigger.REPEAT_INDEFINITELY);
    simpleTriggerFactoryBean.setMisfireInstruction(
      SimpleTrigger.MISFIRE_INSTRUCTION_RESCHEDULE_NEXT_WITH_REMAINING_COUNT);
    return simpleTriggerFactoryBean;
  }

  /**
   * Create cron trigger factory bean.
   *
   * @param jobDetail      job detail.
   * @param cronExpression cron expression.
   * @return cron trigger factory bean.
   */
  public static CronTriggerFactoryBean cronTriggerFactoryBean(
    JobDetail jobDetail, String cronExpression) {
    CronTriggerFactoryBean cronTriggerFactoryBean = new CronTriggerFactoryBean();
    cronTriggerFactoryBean.setJobDetail(jobDetail);
    JobKey jobKey = jobDetail.getKey();
    cronTriggerFactoryBean.setName(jobKey.getName());
    cronTriggerFactoryBean.setGroup(jobKey.getGroup());
    cronTriggerFactoryBean.setDescription(jobDetail.getDescription());
    cronTriggerFactoryBean.setCronExpression(cronExpression);
    cronTriggerFactoryBean.setMisfireInstruction(CronTrigger.MISFIRE_INSTRUCTION_FIRE_ONCE_NOW);
    return cronTriggerFactoryBean;
  }
}
