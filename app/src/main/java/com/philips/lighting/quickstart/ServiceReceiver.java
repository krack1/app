package com.philips.lighting.quickstart;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.philips.lighting.data.HueSharedPreferences;
import com.philips.lighting.hue.sdk.PHAccessPoint;
import com.philips.lighting.hue.sdk.PHBridgeSearchManager;
import com.philips.lighting.hue.sdk.PHHueSDK;

public class ServiceReceiver extends BroadcastReceiver {
	private String TAG = "CallCatcher";
	static Context context;
	private HueSharedPreferences prefs;
	public static Boolean act = false;
	private PHHueSDK phHueSDK;



	//static final String logTag = "SmsReceiver";
	//static final String ACTION = "android.provider.Telephony.SMS_RECEIVED";

	@Override
	public void onReceive(Context context, Intent intent) {

		phHueSDK = PHHueSDK.create();

		prefs = HueSharedPreferences.getInstance(context);		//save space of ip,user
		String lastIpAddress   = prefs.getLastConnectedIPAddress();
		String lastUsername    = prefs.getUsername();

		PHAccessPoint lastAccessPoint = new PHAccessPoint();

		if (lastIpAddress !=null && !lastIpAddress.equals("")) {

			lastAccessPoint.setIpAddress(lastIpAddress);
			lastAccessPoint.setUsername(lastUsername);

			if (!phHueSDK.isAccessPointConnected(lastAccessPoint)) {
				Log.i(TAG, "connecting");
				phHueSDK.connect(lastAccessPoint);
			}
		}
		else {  // First time use, so perform a bridge search.
			doBridgeSearch();
		}


		if (phHueSDK.isAccessPointConnected(lastAccessPoint)) {

			this.context = context;
			Log.i(TAG, "ServiceReceiver->onReceive();");

			MyPhoneStateListener phoneListener = new MyPhoneStateListener();
			TelephonyManager telephony = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

			if ("android.provider.Telephony.SMS_RECEIVED".equals(intent.getAction())) {
				Log.i(TAG, "receive sms");
				Intent testActivityIntent = new Intent(context, push_color.class);
				intent.putExtra("push", "SMS");
				testActivityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				testActivityIntent.addFlags(Intent.FLAG_ACTIVITY_FORWARD_RESULT);
				context.startService(testActivityIntent);
			}


			telephony.listen(phoneListener, PhoneStateListener.LISTEN_SERVICE_STATE);
			telephony.listen(phoneListener, PhoneStateListener.LISTEN_CALL_STATE);
		}
	}

	public void doBridgeSearch() {
		Log.i(TAG, "doBridgeSearch");
		PHBridgeSearchManager sm = (PHBridgeSearchManager) phHueSDK.getSDKService(PHHueSDK.SEARCH_BRIDGE);
		// Start the UPNP Searching of local bridges.
		sm.search(true, true);
	}
}
