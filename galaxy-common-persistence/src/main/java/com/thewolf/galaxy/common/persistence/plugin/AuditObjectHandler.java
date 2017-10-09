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

package com.thewolf.galaxy.common.persistence.plugin;

import com.baomidou.mybatisplus.mapper.MetaObjectHandler;
import com.thewolf.galaxy.common.core.utils.SpringContextUtils;
import org.apache.ibatis.reflection.MetaObject;

import java.time.ZonedDateTime;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Meta object handler for auditing.
 */
public class AuditObjectHandler extends MetaObjectHandler {

  private AuditorAware auditorAware;

  private AuditorAware checkAuditorAware() {
    if (auditorAware == null) {
      auditorAware = SpringContextUtils.getBean(AuditorAware.class);
      checkNotNull(auditorAware);
    }
    return auditorAware;
  }

  @Override
  public void insertFill(MetaObject metaObject) {
    ZonedDateTime now = ZonedDateTime.now();
    setFieldValByName("createdAt", now, metaObject);
    setFieldValByName("updatedAt", now, metaObject);
    String auditor = checkAuditorAware().getCurrentAuditor();
    if (auditor != null) {
      setFieldValByName("createdBy", auditor, metaObject);
      setFieldValByName("updatedBy", auditor, metaObject);
    }
  }

  @Override
  public void updateFill(MetaObject metaObject) {
    setFieldValByName("updatedAt", ZonedDateTime.now(), metaObject);
    String auditor = checkAuditorAware().getCurrentAuditor();
    if (auditor != null) {
      setFieldValByName("updatedBy", auditor, metaObject);
    }
  }
}
