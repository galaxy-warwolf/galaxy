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

package com.thewolf.galaxy.common.web.components;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Http tool kit.
 */
@Component
public final class HttpKit {

  private OkHttpClient client;

  @Autowired
  private ObjectMapper mapper;

  public static final String APPLICATION_JSON_STRING =
    org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

  public static final MediaType APPLICATION_JSON =
    MediaType.parse(org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE);

  @PostConstruct
  public void init() {
    client = new OkHttpClient();
  }

  /**
   * Do http get.
   *
   * @param url request url.
   * @return response.
   * @throws IOException while occur exception.
   */
  public Response doGet(String url) throws IOException {
    Request request = new Request.Builder().url(url).build();
    return client.newCall(request).execute();
  }

  /**
   * Do http get.
   *
   * @param url               request url.
   * @param responseBodyClazz response body class.
   * @param <T>               response body type.
   * @return returned body instance.
   * @throws IOException         while occur exception.
   * @throws StatusCodeException while request not successful.
   */
  public <T> T doGet(String url, Class<T> responseBodyClazz) throws IOException, StatusCodeException {
    Request request = new Request.Builder().addHeader("Accept", APPLICATION_JSON_STRING).url(url).build();
    return transformResponse(client.newCall(request).execute(), responseBodyClazz);
  }

  /**
   * Do http post.
   *
   * @param url      request url.
   * @param postBody post body.
   * @param <P>      post type.
   * @return response.
   * @throws IOException while occur exception.
   */
  public <P> Response doPost(String url, P postBody) throws IOException {
    RequestBody body = RequestBody.create(APPLICATION_JSON, mapper.writeValueAsString(postBody));
    Request request = new Request.Builder().url(url).post(body).build();
    return client.newCall(request).execute();
  }

  /**
   * Do http post.
   *
   * @param url               request url.
   * @param postBody          post body.
   * @param responseBodyClazz response body class.
   * @param <P>               post body type.
   * @param <T>               response body type.
   * @return response body instance.
   * @throws IOException         while occur exception.
   * @throws StatusCodeException while request not successful.
   */
  public <P, T> T doPost(String url, P postBody, Class<T> responseBodyClazz) throws IOException {
    RequestBody body = RequestBody.create(APPLICATION_JSON, mapper.writeValueAsString(postBody));
    Request request = new Request.Builder().addHeader("Accept", APPLICATION_JSON_STRING).url(url).post(body)
      .build();
    return transformResponse(client.newCall(request).execute(), responseBodyClazz);
  }

  /**
   * Do http put.
   *
   * @param url     request url.
   * @param putBody put body.
   * @param <P>     put type.
   * @return response.
   * @throws IOException while occur exception.
   */
  public <P> Response doPut(String url, P putBody) throws IOException {
    RequestBody body = RequestBody.create(APPLICATION_JSON, mapper.writeValueAsString(putBody));
    Request request = new Request.Builder().url(url).put(body).build();
    return client.newCall(request).execute();
  }

  /**
   * Do http put.
   *
   * @param url               request url.
   * @param putBody           put body.
   * @param responseBodyClazz response body class.
   * @param <P>               post body type.
   * @param <T>               response body type.
   * @return response body instance.
   * @throws IOException         while occur exception.
   * @throws StatusCodeException while request not successful.
   */
  public <P, T> T doPut(String url, P putBody, Class<T> responseBodyClazz)
    throws IOException, StatusCodeException {
    RequestBody body = RequestBody.create(APPLICATION_JSON, mapper.writeValueAsString(putBody));
    Request request = new Request.Builder().addHeader("Accept", APPLICATION_JSON_STRING).url(url).put(body)
      .build();
    return transformResponse(client.newCall(request).execute(), responseBodyClazz);
  }

  /**
   * Execute http request.
   *
   * @param request http request.
   * @return response.
   * @throws IOException while occur exception.
   */
  public Response execute(Request request) throws IOException {
    return client.newCall(request).execute();
  }

  /**
   * Transform response body.
   *
   * @param response          response.
   * @param responseBodyClazz response body class.
   * @param <T>               body type.
   * @return transformed body instance.
   * @throws IOException         while occur exception.
   * @throws StatusCodeException while request not successful.
   */
  private <T> T transformResponse(Response response, Class<T> responseBodyClazz)
    throws IOException, StatusCodeException {
    checkNotNull(response);
    if (response.isSuccessful()) {
      return mapper.readValue(response.body().charStream(), responseBodyClazz);
    } else {
      throw new StatusCodeException(response.code(), response.message(), response);
    }
  }

  /**
   * Exception for execute request not successful.
   */
  @Getter
  @Setter
  @AllArgsConstructor
  public static class StatusCodeException extends RuntimeException {
    private int statusCode;
    private String message;
    private transient Response response;
  }

}
