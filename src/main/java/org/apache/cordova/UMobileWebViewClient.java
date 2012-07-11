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
package org.apache.cordova;

import java.util.HashMap;
import java.util.Map;

import org.jasig.umobile.app.android.SessionTracking;

import android.util.Log;
import android.webkit.WebView;

public class UMobileWebViewClient extends CordovaWebViewClient {

    protected static final String TAG = "UMobileWebViewClient";
    
    protected static final String APP_FILE = "file:///android_asset/www/index.html";
    
    // TODO: make dynamically configurable
    protected final static Map<String,String> modules = new HashMap<String, String>();    
    static {
        modules.put("/s/location", APP_FILE.concat("?module=map"));
    }
    
    private int sessionTimeout;
    
    private String basePortalUrl;
    
    public UMobileWebViewClient(DroidGap ctx) {
        super(ctx);
    }
    
    public void setSessionTimeout(int sessionTimeout) {
        this.sessionTimeout = sessionTimeout;
    }

    public void setBasePortalUrl(String basePortalUrl) {
        this.basePortalUrl = basePortalUrl;
    }

    @Override
    public void onLoadResource(WebView view, String url) {

        // if this URL is part of the portal, evaluate it for special handling
        if (url.startsWith(basePortalUrl)) {
            
            if (SessionTracking.getLastSessionAccess() != 0 && System.currentTimeMillis() - SessionTracking.getLastSessionAccess() > sessionTimeout) {
                Log.i(TAG, "Session timed out, reloading main page");
                SessionTracking.update();
                this.ctx.loadUrl(APP_FILE);
                return;
            }
            
            SessionTracking.update();
            
            // evaluate this URL against the list of service overrides
            for (Map.Entry<String, String> entry : modules.entrySet()) {
                if (url.contains(entry.getKey())) {
                    // TODO: Add parameter handling
                    Log.d(TAG, "Overriding URL " + url + " with local uMobile module " + url);
                    this.ctx.loadUrl(entry.getValue());
                    return;
                }
            }
            
        }
        
        super.onLoadResource(view, url);
        
    }
}
