package com.stackmob.gigyademo;

import com.gigya.socialize.GSKeyNotFoundException;
import com.gigya.socialize.GSObject;
import com.gigya.socialize.GSResponse;
import com.gigya.socialize.android.GSAPI;	
import com.gigya.socialize.android.event.GSLoginUIListener;
import com.stackmob.android.sdk.common.StackMobAndroid;
import com.stackmob.sdk.api.StackMob;
import com.stackmob.sdk.api.StackMobOptions;
import com.stackmob.sdk.callback.StackMobCallback;
import com.stackmob.sdk.exception.StackMobException;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v4.app.NavUtils;

public class MainActivity extends Activity implements GSLoginUIListener {

	private User user = new User();
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
		StackMobAndroid.init(getApplicationContext(), 0, "YOUR_PUBLIC_KEY");
		StackMob.getStackMob().getSession().getLogger().setLogging(true);
		
		GSAPI gigya = new GSAPI("YOUR_GIGYA_KEY", this);
		gigya.showLoginUI(null, this, null);
    }
    
	@Override
	public void onLogin(String provider, GSObject user, Object context) {
		try {
			String uid = user.getString("UID");
			String timestamp = user.getString("signatureTimestamp");
			String sig = user.getString("UIDSignature");
			this.user.loginWithGigya(uid, timestamp, sig, StackMobOptions.none(), new StackMobCallback() {
				
				@Override
				public void success(String arg0) {
					MainActivity.this.gotLoggedInUser(MainActivity.this.user);
				}
				
				@Override
				public void failure(StackMobException arg0) {
					arg0.printStackTrace();
					
				}
			});
		} catch (GSKeyNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
    public void gotLoggedInUser(User user) {
    	System.out.println("Got user " + user.toJson());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

	@Override
	public void onClose(boolean arg0, Object arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onError(GSResponse arg0, Object arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onLoad(Object arg0) {
		// TODO Auto-generated method stub
		
	}



    
}
