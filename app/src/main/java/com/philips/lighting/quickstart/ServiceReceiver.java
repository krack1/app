package com.philips.lighting.quickstart;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

public class ServiceReceiver extends BroadcastReceiver {
	private String TAG = "CallCatcher";
	static final String logTag = "SmsReceiver";
	static final String ACTION = "android.provider.Telephony.SMS_RECEIVED";

	@Override
	public void onReceive(Context context, Intent intent) {
		Log.i(TAG, "ServiceReceiver->onReceive();");

		if(intent.getAction().equals(ACTION)) {
			Intent smsintent = new Intent(context, CallCatcherActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			intent.addFlags(Intent.FLAG_ACTIVITY_FORWARD_RESULT);
			context.startActivity(smsintent);
		}

		MyPhoneStateListener phoneListener = new MyPhoneStateListener();
		TelephonyManager telephony = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);

		telephony.listen(phoneListener, PhoneStateListener.LISTEN_SERVICE_STATE);
		telephony.listen(phoneListener, PhoneStateListener.LISTEN_CALL_STATE);

		Intent testActivityIntent = new Intent(context, CallCatcherActivity.class);
		testActivityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		testActivityIntent.addFlags(Intent.FLAG_ACTIVITY_FORWARD_RESULT);
		context.startActivity(testActivityIntent);



	}
}
