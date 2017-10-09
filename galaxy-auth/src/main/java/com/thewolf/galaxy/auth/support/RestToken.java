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

package com.thewolf.galaxy.auth.support;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * Rest token object.
 */
@Getter
@Setter
public class RestToken {

  @ApiModelProperty(name = "access_token", required = true)
  @JsonProperty("access_token")
  private String accessToken;

  @ApiModelProperty(name = "token_type", required = true)
  @JsonProperty("token_type")
  private String tokenType = AuthConstants.TOKEN_TYPE;

  @ApiModelProperty(name = "expires_in", required = true)
  @JsonProperty("expires_in")
  private long expiresIn;

  @ApiModelProperty(name = "refresh_token", required = true)
  @JsonProperty("refresh_token")
  private String refreshToken;

}
