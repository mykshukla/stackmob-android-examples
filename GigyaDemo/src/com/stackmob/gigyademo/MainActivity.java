package com.stackmob.gigyademo;

import gigya.LoginUIListener;

import com.gigya.socialize.android.GSAPI;
import com.stackmob.android.sdk.common.StackMobAndroid;
import com.stackmob.sdk.api.StackMob;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v4.app.NavUtils;

public class MainActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
		StackMobAndroid.init(getApplicationContext(), 0, "YOUR_PUBLIC_KEY");
		StackMob.getStackMob().getSession().getLogger().setLogging(true);
		
		GSAPI gigya = new GSAPI("YOUR_GIGYA_KEY", this);
		gigya.showLoginUI(null, new LoginUIListener(this), null);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
    
    public void gotLoggedInUser(User user) {
    	System.out.println("Got user " + user.toJson());
    }

    
}
