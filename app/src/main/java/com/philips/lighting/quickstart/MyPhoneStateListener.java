package com.philips.lighting.quickstart;

import android.content.Intent;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;
public class MyPhoneStateListener extends PhoneStateListener {
	private String TAG = "CallCatcher";
	public Boolean ringing = false;
	@Override
	public void onCallStateChanged(int state, String incomingNumber) {

		switch (state) {
		case TelephonyManager.CALL_STATE_IDLE:

			Log.i(TAG,
					"MyPhoneStateListener->onCallStateChanged() -> CALL_STATE_IDLE "
							+ incomingNumber);
			break;
		case TelephonyManager.CALL_STATE_OFFHOOK:
			Log.i(TAG,
					"MyPhoneStateListener->onCallStateChanged() -> CALL_STATE_OFFHOOK "
							+ incomingNumber);
			break;
		case TelephonyManager.CALL_STATE_RINGING:
			Toast.makeText(ServiceReceiver.context, "incoming call", Toast.LENGTH_SHORT).show();
			Intent testActivityIntent = new Intent(ServiceReceiver.context, CallCatcherActivity.class);
			testActivityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			testActivityIntent.addFlags(Intent.FLAG_ACTIVITY_FORWARD_RESULT);
			ServiceReceiver.context.startActivity(testActivityIntent);

			ringing = true;
			Log.i(TAG,
						"MyPhoneStateListener->onCallStateChanged() -> CALL_STATE_RINGING "
								+ incomingNumber);

			break;
		default:
			Log.i(TAG,
					"MyPhoneStateListener->onCallStateChanged() -> default -> "
							+ Integer.toString(state));
			break;
		}

	}



	/*@Override
	public void onServiceStateChanged(ServiceState serviceState) {
		switch (serviceState.getState()) {
		case ServiceState.STATE_IN_SERVICE:
			Log.i(TAG,
					"MyPhoneStateListener->onServiceStateChanged() -> STATE_IN_SERVICE");
			serviceState.setState(ServiceState.STATE_IN_SERVICE);
			break;
		case ServiceState.STATE_OUT_OF_SERVICE:
			Log.i(TAG,
					"MyPhoneStateListener->onServiceStateChanged() -> STATE_OUT_OF_SERVICE");
			serviceState.setState(ServiceState.STATE_OUT_OF_SERVICE);
			break;
		case ServiceState.STATE_EMERGENCY_ONLY:
			Log.i(TAG,
					"MyPhoneStateListener->onServiceStateChanged() -> STATE_EMERGENCY_ONLY");
			serviceState.setState(ServiceState.STATE_EMERGENCY_ONLY);
			break;
		case ServiceState.STATE_POWER_OFF:
			Log.i(TAG,
					"MyPhoneStateListener->onServiceStateChanged() -> STATE_POWER_OFF");
			serviceState.setState(ServiceState.STATE_POWER_OFF);
			break;
		default:
			Log.i(TAG,
					"MyPhoneStateListener->onServiceStateChanged() -> default -> "
							+ Integer.toString(serviceState.getState()));
			break;
		}
	}*/
}
