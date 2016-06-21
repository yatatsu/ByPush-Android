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

import java.io.IOException;
import java.util.concurrent.Executor;

public class HttpCall {

  private final Request request;
  private final HttpEngine engine;

  public static HttpCall create(Request request, HttpEngine engine) {
    return new HttpCall(request, engine);
  }

  HttpCall(Request request, HttpEngine engine) {
    this.request = request;
    this.engine = engine;
  }

  public Response execute() throws IOException {
    return engine.execute(request);
  }

  public void enqueue(final Callback callback) {
    final Executor callbackExecutor = engine.getCallbackExecutor();
    engine.getHttpExecutor().execute(new Runnable() {
      @Override public void run() {
        try {
          final Response response = execute();
          callbackExecutor.execute(new Runnable() {
            @Override public void run() {
              callback.onResponse(response);
            }
          });
        } catch (final Throwable throwable) {
          callbackExecutor.execute(new Runnable() {
            @Override public void run() {
              callback.onFailure(throwable);
            }
          });
        }
      }
    });
  }
}
