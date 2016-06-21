/*
 *
 * Copyright 2016 KITAGAWA, Tatsuya (yatatsu)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.github.yatatsu.android.bypush.http;

class RequestBody {

  public static final String CONTENT_JSON_UTF_8 = "application/json; charset=utf-8";

  private final byte[] content;
  private final String contentType;

  RequestBody(String contentType, byte[] content) {
    this.contentType = contentType;
    this.content = content;
  }

  public static RequestBody create(String content) {
    return create(content.getBytes());
  }

  static RequestBody create(byte[] content) {
    if (content == null) {
      throw new NullPointerException("content is null");
    }
    return new RequestBody(CONTENT_JSON_UTF_8, content);
  }

  String getContentType() {
    return contentType;
  }

  public byte[] getContent() {
    return content;
  }
}
