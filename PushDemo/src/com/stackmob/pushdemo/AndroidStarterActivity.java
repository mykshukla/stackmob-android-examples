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

package com.stackmob.pushdemo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.stackmob.android.sdk.common.StackMobCommon;
import com.stackmob.pushdemo.R;
import com.stackmob.sdk.api.StackMob;
import com.stackmob.sdk.callback.StackMobCallback;
import com.stackmob.sdk.exception.StackMobException;
import com.stackmob.sdk.model.StackMobUser;
import com.stackmob.sdk.push.StackMobPushToken;

import com.google.android.gcm.GCMRegistrar;
import com.inneractive.api.ads.InneractiveAd;
import com.inneractive.api.ads.InneractiveAd.IaAdType;
import com.inneractive.api.ads.InneractiveAd.IaOptionalParams;


public class AndroidStarterActivity extends Activity {
	
	public static String SENDER_ID = "YOUR_SENDER_ID_HERE";
	private StackMob stackmob;
	private StackMobUser user;
	private static final String TAG = AndroidStarterActivity.class.getCanonicalName();
	private final StackMobCallback standardToastCallback = new StackMobCallback() {
		@Override public void success(String responseBody) {
			threadAgnosticToast(AndroidStarterActivity.this, "response: " + responseBody, Toast.LENGTH_SHORT);
			Log.i(TAG, "request succeeded with " + responseBody);
		}
		@Override public void failure(StackMobException e) {
			threadAgnosticToast(AndroidStarterActivity.this, "error: " + e.getMessage(), Toast.LENGTH_SHORT);
			Log.i(TAG, "request had exception " + e.getMessage());
		}
	};
	
	private BroadcastReceiver inneractiveMessageReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) { 
			String message = intent.getStringExtra("message");
			Toast.makeText(getApplicationContext(), message, 5);
		}

	};
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		StackMobCommon.init(this.getApplicationContext(), StackMob.OAuthVersion.One, 0, "YOUR_API_KEY_HERE", "YOUR_API_SECRET_HERE");
		stackmob = StackMob.getStackMob();
		
		// Register for GCM Push
		try {
			GCMRegistrar.checkDevice(this);
			GCMRegistrar.checkManifest(this);
			final String regId = GCMRegistrar.getRegistrationId(this);
			if (regId.equals("")) {
				registerForPush();
			} else {
				Log.v(TAG, "Already registered");
			}
		} catch(UnsupportedOperationException e) {
			Log.w(TAG, "This device doesn't support gcm. Push will not work");
		} 
    }
    
	public void loginClick(View v) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("username", getUsername());
		params.put("password", getPassword());
		stackmob.login(params, standardToastCallback);
	}
	
	public void createUserClick(View v) {
		user = new User(getUsername(), getPassword());
		user.save();
	}
	
	public void registerRegTokenClick(View w) {
		try {
			final String username = getPushUsername();
			user.registerForPush(new StackMobPushToken(getRegistrationIDHolder().getID()), standardToastCallback);
		}
		catch(	Exception e) {
			threadAgnosticToast(AndroidStarterActivity.this, "no registration ID currently stored", Toast.LENGTH_SHORT);
		}
		
	}
	
	public void sendPushClick(View w) {
		final Map<String, String> payload = new HashMap<String, String>();
		payload.put("payload", getPushPayload());
		user.sendPush(payload, standardToastCallback);
	}
	
	public void getRegTokenClick(View w) {
		try {
			threadAgnosticToast(AndroidStarterActivity.this, getRegistrationIDHolder().getID(), Toast.LENGTH_SHORT);
		}
		catch(PushRegistrationIDHolder.NoStoredRegistrationIDException e) {
			threadAgnosticToast(AndroidStarterActivity.this, "no registration ID currently stored", Toast.LENGTH_SHORT);
		}
	}
	
	public void forceGetRegTokenClick(View w) {
		registerForPush();
		threadAgnosticToast(AndroidStarterActivity.this, "sent intent to get reg ID", Toast.LENGTH_SHORT);
	}
	
	private PushRegistrationIDHolder getRegistrationIDHolder() {
		return new PushRegistrationIDHolder(AndroidStarterActivity.this);
	}
	
	private void registerForPush() {
		GCMRegistrar.register(this, SENDER_ID);
	}

	private EditText getUsernameField() {
		return (EditText)findViewById(R.id.username);
	}
	
	private EditText getPasswordField() {
		return (EditText)findViewById(R.id.password);
	}
	
	private EditText getPushUsernameField() {
		return (EditText)findViewById(R.id.push_username);
	}
	
	private String getUsername() {
		return getUsernameField().getText().toString();
	}
	
	private String getPushUsername() {
		return getPushUsernameField().getText().toString();
	}
	
	private String getPassword() {
		return getPasswordField().getText().toString();
	}
	
	private EditText getPushPayloadField() {
		return (EditText)findViewById(R.id.token_username);
	}
	
	private String getPushPayload() {
		return getPushPayloadField().getText().toString();
	}
	
	private void threadAgnosticToast(final Context ctx, final String txt, final int duration) {
		runOnUiThread(new Runnable() {
			@Override public void run() {
				Toast.makeText(ctx, txt, duration).show();
			}
		});
	}
}
