/**
 * Copyright 2011 StackMob
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

package com.stackmob.android.sdk.common;

import android.content.Context;

import com.stackmob.android.sdk.callback.*;
import com.stackmob.sdk.callback.StackMobRedirectedCallback;
import com.stackmob.sdk.api.StackMob;
import com.stackmob.sdk.api.StackMob.OAuthVersion;

public class StackMobCommon {
	public static String TWITTER_CONSUMER_KEY = "YOUR_TWITTER_CONSUMER_KEY_HERE";
	public static String TWITTER_CONSUMER_SECRET = "YOUR_TWITTER_CONSUMER_SECRET_HERE";
	
	public static String FACEBOOK_APP_ID = "YOUR_FACEBOOK_APP_ID_HERE";

	public static StackMobTwitterCallback TwitterCallback = null;
	public static StackMobFacebookCallback FacebookCallback = null;

	// Init with minimal information and the most basic defaults
	public static void init(Context c, int apiVersionNumber, String apiKey) {
		new StackMob(apiVersionNumber, apiKey);
		setAndroidSession(c);
	}
	
	// Init with minimal information for oauth1 and the most basic defaults
	public static void init(Context c, OAuthVersion oauthVersion, int apiVersionNumber, String apiKey, String apiSecret) {
		new StackMob(oauthVersion, apiVersionNumber, apiKey, apiSecret);
		setAndroidSession(c);
	}
	
	// Init specifying all options
	public static void init(Context c, OAuthVersion oauthVersion, int apiVersionNumber, String apiKey, String apiSecret, String apiHost, String pushHost, String userSchema, String userIdName, String passwordFieldName, StackMobRedirectedCallback redirectedCallback) {
		new StackMob(oauthVersion, apiVersionNumber, apiKey, apiSecret, apiHost, pushHost, userSchema, userIdName, passwordFieldName, redirectedCallback);
		setAndroidSession(c);
	}
	
	private static void setAndroidSession(Context c) {
		StackMob.getStackMob().setSession(new StackMobAndroidSession(c, StackMob.getStackMob().getSession()));	
	}
}
