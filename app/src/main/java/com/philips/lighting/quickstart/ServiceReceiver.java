package com.philips.lighting.quickstart;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.philips.lighting.data.HueSharedPreferences;

public class ServiceReceiver extends BroadcastReceiver {
	private String TAG = "CallCatcher";
	static Context context;
	private HueSharedPreferences prefs;
	public static Boolean act = false;



	//static final String logTag = "SmsReceiver";
	//static final String ACTION = "android.provider.Telephony.SMS_RECEIVED";

	@Override
	public void onReceive(Context context, Intent intent) {
		this.context = context;
		Log.i(TAG, "ServiceReceiver->onReceive();");
		prefs = HueSharedPreferences.getInstance(context);


		MyPhoneStateListener phoneListener = new MyPhoneStateListener();
		TelephonyManager telephony = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

		if ("android.provider.Telephony.SMS_RECEIVED".equals(intent.getAction())) {
			Log.i(TAG, "receive sms");
			Intent testActivityIntent = new Intent(context, push_color.class);
			testActivityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			testActivityIntent.addFlags(Intent.FLAG_ACTIVITY_FORWARD_RESULT);
			context.startService(testActivityIntent);
		}


		telephony.listen(phoneListener, PhoneStateListener.LISTEN_SERVICE_STATE);
		telephony.listen(phoneListener, PhoneStateListener.LISTEN_CALL_STATE);


	}
}
