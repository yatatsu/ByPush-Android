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

import java.util.List;
import java.util.Map;

class Response {

  private final ResponseBody body;

  private final Request request;

  private final int code;

  private final String message;

  private final Map<String, List<String>> headers;

  Response(final Request request, final int code, String message,
      final Map<String, List<String>> headers, final ResponseBody body) {
    this.request = request;
    this.code = code;
    this.message = message;
    this.headers = headers;
    this.body = body;
  }

  public Request getRequest() {
    return request;
  }

  public int getCode() {
    return code;
  }

  public String getMessage() {
    return message;
  }

  public Map<String, List<String>> getHeaders() {
    return headers;
  }

  public ResponseBody getResponseBody() {
    return body;
  }
}
