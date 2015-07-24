package com.philips.lighting.quickstart;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class light_change extends Service {
    @Override
    public void onCreate() {
    }
    public int onStartCommand(Intent intent, int flags, int startId) {

        return START_STICKY;
    }
    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
