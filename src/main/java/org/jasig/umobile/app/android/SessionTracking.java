/**
 * Licensed to Jasig under one or more contributor license
 * agreements. See the NOTICE file distributed with this work
 * for additional information regarding copyright ownership.
 * Jasig licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a
 * copy of the License at:
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.jasig.umobile.app.android;

import org.apache.cordova.api.Plugin;
import org.apache.cordova.api.PluginResult;
import org.apache.cordova.api.PluginResult.Status;
import org.json.JSONArray;
import org.json.JSONException;

import android.util.Log;

public class SessionTracking extends Plugin {
    
    protected static final String TAG = "SessionTracking";
    
    public static final String SET_ACTION = "set";
    public static final String GET_ACTION = "get";
    
    public static long lastSessionAccess = 0;
    
    public static void update() {
        lastSessionAccess = System.currentTimeMillis();
    }
    
    public static long getLastSessionAccess() {
        return lastSessionAccess;
    }

    @Override
    public PluginResult execute(String action, JSONArray data, String callbackId) {
        if (SET_ACTION.equals(action)) {
            try {
                lastSessionAccess = (Long) data.get(0);
                Log.d(TAG, "Updated session tracking to " + lastSessionAccess);
            } catch (JSONException e) {
                Log.e(TAG, "Failed to get parameter for set action");
            }
            return new PluginResult(Status.OK);
        } else {
            return new PluginResult(Status.OK, lastSessionAccess);
        }
    }

}
