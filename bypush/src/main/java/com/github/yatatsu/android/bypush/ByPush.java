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

package com.github.yatatsu.android.bypush;

import java.util.Map;

public class ByPush {

  private static ByPushClient tracker;

  /**
   * Send token to server.
   * You should pass {@link com.google.firebase.iid.FirebaseInstanceId#getToken()}
   *
   * @param token token
   */
  public static void saveDeviceToken(String token) {
    // TODO
  }

  /**
   * Send attributes to server.
   *
   * @param attributes attributes
   */
  public static void saveAttributes(Map<String, Object> attributes) {
    // TODO
  }

  /**
   * Track opening notification event.
   */
  public static void trackOpeningNotification() {
    // TODO
  }
}
