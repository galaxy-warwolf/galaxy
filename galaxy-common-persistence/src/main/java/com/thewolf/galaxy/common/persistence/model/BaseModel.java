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
package com.thewolf.galaxy.common.persistence.model;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.enums.FieldFill;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.ZonedDateTime;

/**
 * Base model for galaxy.
 */
@Getter
@Setter
public abstract class BaseModel<T extends Model> extends Model<T> {

  @TableId(value = "id", type = IdType.ID_WORKER)
  protected String id;

  @TableField(value = "created_at", fill = FieldFill.INSERT)
  protected ZonedDateTime createdAt;

  @TableField(value = "updated_at", fill = FieldFill.INSERT_UPDATE)
  protected ZonedDateTime updatedAt;

  @TableField(value = "created_by", fill = FieldFill.INSERT)
  protected String createdBy;

  @TableField(value = "updated_by", fill = FieldFill.INSERT_UPDATE)
  protected String updatedBy;

  @Override
  protected Serializable pkVal() {
    return this.id;
  }
}
