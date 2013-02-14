package gigya;

import java.util.Arrays;

import android.sax.StartElementListener;

import com.gigya.socialize.GSKeyNotFoundException;
import com.gigya.socialize.GSObject;
import com.gigya.socialize.GSResponse;
import com.gigya.socialize.android.event.GSLoginUIListener;
import com.stackmob.gigyademo.MainActivity;
import com.stackmob.gigyademo.User;
import com.stackmob.sdk.api.StackMob;
import com.stackmob.sdk.api.StackMobOptions;
import com.stackmob.sdk.callback.StackMobCallback;
import com.stackmob.sdk.exception.StackMobException;

public class LoginUIListener implements GSLoginUIListener {
	
	private MainActivity activity;
	private User user;
	
	public LoginUIListener(MainActivity activity) {
		this.activity = activity;
		this.user = new User();
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

	@Override
	public void onLogin(String provider, GSObject user, Object context) {
		try {
			String uid = user.getString("UID");
			String timestamp = user.getString("signatureTimestamp");
			String sig = user.getString("UIDSignature");
			this.user.loginWithGigya(uid, timestamp, sig, StackMobOptions.none(), new StackMobCallback() {
				
				@Override
				public void success(String arg0) {
					LoginUIListener.this.activity.gotLoggedInUser(LoginUIListener.this.user);
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

}
