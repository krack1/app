package com.philips.lighting.quickstart;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;

/**
 * Created by jaeho on 2015-08-20.
 */

@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
public class NotificationReceive extends NotificationListenerService {

    private String TAG = this.getClass().getSimpleName();
    @Override
    public void onCreate() {
        super.onCreate();
    }
    public void onDestroy() {
        super.onDestroy();
    }

    public void onNotificationPosted(StatusBarNotification sbn) {
        Log.i(TAG, "**********  onNotificationPosted");
        Log.i(TAG, "ID :" + sbn.getId() + "\t" + sbn.getNotification().tickerText + "\t" + sbn.getPackageName());
        Intent i = new  Intent(ServiceReceiver.context, push_color.class);
        i.putExtra("app",sbn.getPackageName());
        ServiceReceiver.context.startService(i);
    }

}
