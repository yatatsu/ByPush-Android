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

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.net.HttpURLConnection;

class ResponseBody implements Closeable {

  private final String contentType;

  private final long contentLength;

  private final BufferedInputStream inputStream;

  private HttpURLConnection connection;

  ResponseBody(final String contentType, final long contentLength,
      final BufferedInputStream inputStream, HttpURLConnection connection) {
    this.contentType = contentType;
    this.contentLength = contentLength;
    this.inputStream = inputStream;
    this.connection = connection;
  }

  @Override public void close() {
    Util.close(inputStream);
    connection.disconnect();
    connection = null;
  }

  public String getContentType() {
    return contentType;
  }

  public BufferedInputStream getByteStream() {
    return inputStream;
  }

  public long getContentLength() {
    return contentLength;
  }

  byte[] bytes() throws IOException {
    ByteArrayOutputStream os = new ByteArrayOutputStream();
    try {
      byte[] buffer = new byte[1024];
      int n = 0;
      while (-1 != (n = inputStream.read(buffer))) {
        os.write(buffer, 0, n);
      }
      return os.toByteArray();
    } finally {
      Util.close(os);
      Util.close(this);
    }
  }

  public final String string() throws IOException {
    return new String(bytes(), Util.UTF_8);
  }
}
