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

package com.thewolf.galaxy.common.web.support;

import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Base controller for galaxy web.
 */
public abstract class BaseController {

  /**
   * Send file as attachment.
   *
   * @param filePath file absolute path.
   * @return response entity.
   * @throws IOException          if an I/O error occurs reading from the stream.
   * @throws InvalidPathException if invalid path.
   */
  protected ResponseEntity<InputStreamResource> attachment(String filePath) throws IOException, InvalidPathException {
    Path path = Paths.get(filePath);
    Path fileName = path.getFileName();
    if (fileName == null) {
      throw new InvalidPathException(filePath, "input path not have file name.");
    }
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
    headers.setContentDispositionFormData("attachment", fileName.toString());
    headers.setContentLength(Files.size(path));
    return ResponseEntity.ok().headers(headers).body(new InputStreamResource(Files.newInputStream(path)));
  }

  /**
   * Send file as attachment.
   *
   * @param inputStream input stream.
   * @param fileName    file name.
   * @return response entity.
   */
  protected ResponseEntity<InputStreamResource> attachment(InputStream inputStream, String fileName) {
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
    headers.setContentDispositionFormData("attachment", fileName);
    return ResponseEntity.ok().headers(headers).body(new InputStreamResource(inputStream));
  }

  /**
   * Send file as stream with specific content type.
   *
   * @param filePath    file absolute path.
   * @param contentType content type.
   * @return response entity.
   * @throws IOException if an I/O error occurs reading from the stream.
   */
  protected ResponseEntity<InputStreamResource> sendFile(String filePath, String contentType) throws IOException {
    Path path = Paths.get(filePath);
    if (contentType == null) {
      contentType = Files.probeContentType(path);
    }
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.parseMediaType(contentType));
    headers.setLastModified(Files.getLastModifiedTime(path).toMillis());
    headers.setContentLength(Files.size(path));
    return ResponseEntity.ok().headers(headers).body(new InputStreamResource(Files.newInputStream(path)));
  }

  /**
   * Send file as stream.
   *
   * @param filePath file absolute path.
   * @return response entity.
   * @throws IOException if an I/O error occurs reading from the stream.
   */
  protected ResponseEntity<InputStreamResource> sendFile(String filePath) throws IOException {
    return sendFile(filePath, null);
  }

  /**
   * Send input stream to response.
   *
   * @param inputStream input stream.
   * @param contentType content type.
   * @return response entity.
   */
  protected ResponseEntity<InputStreamResource> sendFile(InputStream inputStream, String contentType) {
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.parseMediaType(contentType));
    return ResponseEntity.ok().headers(headers).body(new InputStreamResource(inputStream));
  }

}
