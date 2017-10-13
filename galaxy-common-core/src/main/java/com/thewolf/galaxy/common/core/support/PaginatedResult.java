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
package com.thewolf.galaxy.common.core.support;

import lombok.Getter;
import lombok.Setter;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * Paginated result.
 *
 * @param <T> entity class.
 */
@Getter
@Setter
public class PaginatedResult<T> {

  private Integer page;

  private Integer size;

  private Integer count;

  private Long total;

  private List<T> data;

  /**
   * Constructor without total.
   *
   * @param page page index
   * @param size page size
   * @param data content
   */
  public PaginatedResult(Integer page, Integer size, List<T> data) {
    this(page, size, null, data);
  }

  /**
   * Constructor with total.
   *
   * @param page  page index.
   * @param size  page size.
   * @param total total count.
   * @param data  data.
   */
  public PaginatedResult(Integer page, Integer size, Long total, List<T> data) {
    this.page = page;
    this.size = size;
    this.total = total;
    this.data = data;
    if (!CollectionUtils.isEmpty(data)) {
      this.count = data.size();
    } else {
      this.count = 0;
    }
  }
}
