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

import android.os.Build;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;

public class HttpEngine {

  private final HttpConfig config;

  HttpEngine(final HttpConfig config) {
    this.config = config;
  }

  Executor getHttpExecutor() {
    return config.httpExecutor;
  }

  Executor getCallbackExecutor() {
    return config.callbackExecutor;
  }

  Response execute(final Request request) throws IOException {
    final URL httpUrl = new URL(request.getUri().toString());
    final HttpURLConnection urlConnection = (HttpURLConnection) httpUrl.openConnection();

    urlConnection.setRequestMethod(request.getMethod());
    urlConnection.setDoInput(true);
    urlConnection.setReadTimeout(config.readTimeout);
    urlConnection.setConnectTimeout(config.connectTimeout);

    final Map<String, String> headers = request.getHeaders();
    if (headers != null && !headers.isEmpty()) {
      addRequestProperties(urlConnection, headers);
    }

    if (isOutput(request)) {
      doOutput(urlConnection, request.getRequestBody());
    }

    urlConnection.connect();
    final int statusCode = urlConnection.getResponseCode();
    final String message = urlConnection.getResponseMessage();
    final Map<String, List<String>> responseHeaders = urlConnection.getHeaderFields();
    final ResponseBody body = doInput(urlConnection);
    return new Response(request, statusCode, message, responseHeaders, body);
  }

  private static void addRequestProperties(final HttpURLConnection urlConnection,
      final Map<String, String> headers) {
    for (final Map.Entry<String, String> entry : headers.entrySet()) {
      final String value = entry.getValue();
      if (value != null) {
        urlConnection.addRequestProperty(entry.getKey(), value);
      }
    }
  }

  private static boolean isOutput(final Request request) {
    if (request.getRequestBody() == null) {
      return false;
    }
    final String method = request.getMethod();
    return Request.POST.equals(method) || Request.PUT.equals(method);
  }

  private static void doOutput(final HttpURLConnection urlConnection, final RequestBody body)
      throws IOException {
    urlConnection.setDoOutput(true);
    final String contentType = body.getContentType();
    if (contentType != null) {
      urlConnection.addRequestProperty("Content-Type", contentType);
    }
    final long contentLength = body.getContent().length;
    if (contentLength > 0) {
      setFixedLengthStreamingMode(urlConnection, contentLength);
      urlConnection.addRequestProperty("Content-Length", Long.toString(contentLength));
    } else {
      urlConnection.setChunkedStreamingMode(0);
    }
    BufferedOutputStream bos = null;
    try {
      bos = new BufferedOutputStream(urlConnection.getOutputStream());
      bos.write(body.getContent(), 0, body.getContent().length);
    } finally {
      Util.close(bos);
    }
  }

  private static ResponseBody doInput(final HttpURLConnection urlConnection) throws IOException {
    final String contentType = urlConnection.getContentType();
    final long contentLength = urlConnection.getContentLength();
    return new ResponseBody(contentType, contentLength,
        new BufferedInputStream(getResponseStream(urlConnection)), urlConnection);
  }

  private static InputStream getResponseStream(final HttpURLConnection urlConnection) {
    try {
      return urlConnection.getInputStream();
    } catch (final IOException e) {
      return urlConnection.getErrorStream();
    }
  }

  private static void setFixedLengthStreamingMode(final HttpURLConnection urlConnection,
      final long contentLength) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
      urlConnection.setFixedLengthStreamingMode(contentLength);
    } else {
      urlConnection.setFixedLengthStreamingMode((int) contentLength);
    }
  }
}
