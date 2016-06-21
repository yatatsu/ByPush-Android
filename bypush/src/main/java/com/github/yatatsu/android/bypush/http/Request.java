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

import android.net.Uri;
import android.text.TextUtils;
import java.util.HashMap;
import java.util.Map;

public class Request {
  static final String GET = "GET";
  static final String POST = "POST";
  static final String PUT = "PUT";

  private Uri uri;
  private String method;
  private Map<String, String> headers;
  private RequestBody requestBody;

  private Request(Builder builder) {
    this.uri = builder.uri;
    this.method = builder.method;
    this.headers = builder.headers;
    this.requestBody = builder.requestBody;
  }

  Request() {
    this(new Builder());
  }

  Uri getUri() {
    return uri;
  }

  String getMethod() {
    return method;
  }

  Map<String, String> getHeaders() {
    return headers;
  }

  RequestBody getRequestBody() {
    return requestBody;
  }

  public static final class Builder {
    private Uri uri;
    private String method;
    private Map<String, String> headers;
    private RequestBody requestBody;

    public Builder() {
      method = GET;
      headers = new HashMap<>();
    }

    public Request build() {
      return new Request(this);
    }

    public Builder uri(Uri uri) {
      if (uri == null) {
        throw new IllegalArgumentException("uri is null");
      }
      if (!("http".equals(uri.getScheme()) || "https".equals(uri.getScheme()))) {
        throw new IllegalArgumentException("not network uri");
      }
      this.uri = uri;
      return this;
    }

    public Builder url(String url) {
      if (url == null) {
        throw new IllegalArgumentException("url is null");
      }
      Uri uri = Uri.parse(url);
      return uri(uri);
    }

    public Builder method(String method, RequestBody body) {
      this.method = method;
      this.requestBody = body;
      return this;
    }

    public Builder get() {
      return method(GET, null);
    }

    public Builder post(RequestBody body) {
      return method(POST, body);
    }

    public Builder header(String name, String value) {
      if (TextUtils.isEmpty(name) || TextUtils.isEmpty(value)) {
        throw new IllegalArgumentException("header does not allows empty name/value");
      }
      headers.put(name, value);
      return this;
    }
  }
}
