package com.philips.lighting.quickstart;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;

/**
 * Created by jaeho on 2015-08-20.
 */

@SuppressLint("OverrideAbstract")
@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
public class NotificationReceive extends NotificationListenerService {
    public static Boolean act2 = false;
    private String TAG = this.getClass().getSimpleName();
    @Override
    public void onCreate() {
        super.onCreate();
    }
    public void onDestroy() {
        super.onDestroy();

    }
    //receive the message in notification action
    public void onNotificationPosted(StatusBarNotification sbn) {
        Log.i(TAG, "**********  onNotificationPosted");
        Log.i(TAG, "ID :" + sbn.getId() + "\t" + sbn.getNotification().tickerText + "\t" + sbn.getPackageName());
        act2 = false;
        Intent i = new Intent(getApplicationContext(), push_color.class);
        i.putExtra("app",sbn.getPackageName());
        startService(i);
        Log.i(TAG, "**********  onNotificationPosted");
    }

}
