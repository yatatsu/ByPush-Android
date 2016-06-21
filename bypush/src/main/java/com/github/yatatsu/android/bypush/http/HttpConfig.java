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

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

public class HttpConfig {

  final int connectTimeout;
  final int readTimeout;
  final Executor httpExecutor;
  final Executor callbackExecutor;

  private HttpConfig(Builder builder) {
    this.connectTimeout = builder.connectTimeout;
    this.readTimeout = builder.readTimeout;
    this.httpExecutor = builder.httpExecutor;
    this.callbackExecutor = builder.callbackExecutor;
  }

  HttpConfig() {
    this(new Builder());
  }

  public static class Builder {
    int connectTimeout = (int) TimeUnit.SECONDS.toMillis(10);
    int readTimeout = (int) TimeUnit.SECONDS.toMillis(10);
    Executor httpExecutor = Executors.newFixedThreadPool(3, new ThreadFactory() {
      @Override public Thread newThread(@NonNull Runnable r) {
        Thread thread = new Thread(r);
        thread.setPriority(Thread.MIN_PRIORITY);
        return thread;
      }
    });
    Executor callbackExecutor = new Executor() {

      Handler handler = new Handler(Looper.getMainLooper());

      @Override public void execute(@NonNull Runnable command) {
        handler.post(command);
      }
    };

    Builder connectTimeout(int connectTimeout) {
      this.connectTimeout = connectTimeout;
      return this;
    }

    Builder readTimeout(int readTimeout) {
      this.readTimeout = readTimeout;
      return this;
    }

    Builder httpExecutor(Executor executor) {
      this.httpExecutor = executor;
      return this;
    }

    Builder callbackExecutor(Executor executor) {
      this.callbackExecutor = executor;
      return this;
    }

    public HttpConfig build() {
      return new HttpConfig(this);
    }
  }
}
