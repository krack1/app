package com.philips.lighting.quickstart;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.util.Log;

import com.philips.lighting.hue.listener.PHLightListener;
import com.philips.lighting.hue.sdk.PHHueSDK;
import com.philips.lighting.model.PHBridge;
import com.philips.lighting.model.PHBridgeResource;
import com.philips.lighting.model.PHHueError;
import com.philips.lighting.model.PHLight;
import com.philips.lighting.model.PHLightState;

import java.util.List;
import java.util.Map;


public class push_color extends Service {
    private PHHueSDK phHueSDK;
    public static final String TAG = "QuickStart";
    public SharedPreferences prefs_led_state;
    public String app;
    private String led;
    private String hue;
    private String sat;
    private String bri;
    private String light;

    @Override
    public void onCreate() {
        super.onCreate();
        phHueSDK = PHHueSDK.create();
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        app = intent.getExtras().getString("app");

        //led = "led_"+app;
        hue = "hue_"+app;
        sat = "sat_"+app;
        bri = "bri_"+app;


        try {
            if(ServiceReceiver.act == false || NotificationReceive.act2 == false) { // if call, sms ServiceReceiver but if app NotificationReceive
                setLights();
            }
            else{
                return 0;
            }
            ServiceReceiver.act = true;
            NotificationReceive.act2 = true;

        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return START_STICKY;
    }
    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return null;
    }

    public void setLights() throws InterruptedException {

        PHBridge bridge = phHueSDK.getSelectedBridge();

        PHLightState lightState = new PHLightState();

        List<PHLight> allLights = bridge.getResourceCache().getAllLights();

        //change led state setting alam
        for(PHLight lights : allLights) {

            prefs_led_state = getSharedPreferences(app, MODE_PRIVATE);
            light = prefs_led_state.getString("check" + lights.getIdentifier(), "0");

            prefs_led_state = getSharedPreferences("ledFile", MODE_PRIVATE);
            lightState.setHue(prefs_led_state.getInt(hue, 0));
            lightState.setBrightness(prefs_led_state.getInt(bri, 0));
            lightState.setSaturation(prefs_led_state.getInt(sat, 0));


            // To validate your lightstate is valid (before sending to the bridge) you can use:
            // String validState = lightState.validateState();
            if(light!="0") {
                bridge.updateLightState(light, lightState, listener_a);
            }
        }
        Thread.sleep(3000);

        //return to orignal setting
        List<PHLight> allLightss = bridge.getResourceCache().getAllLights();

        for(PHLight lightss : allLightss) {
            String light_origin = lightss.getIdentifier();
            Log.d("test", "hue"+light_origin+", sat"+light_origin+", bri"+light_origin);
            prefs_led_state = getSharedPreferences("ledFile", MODE_PRIVATE);
            lightState.setHue(prefs_led_state.getInt("hue" + light_origin, 0));
            lightState.setSaturation(prefs_led_state.getInt("sat" + light_origin, 0));
            lightState.setBrightness(prefs_led_state.getInt("bri" + light_origin, 0));

            bridge.updateLightState(light_origin, lightState, listener_a);

            Thread.sleep(500);
        }


        //  bridge.updateLightState(light, lightState);   // If no bridge response is required then use this simpler form.
    }

    PHLightListener listener_a = new PHLightListener() {

        @Override
        public void onSuccess() {
        }

        @Override
        public void onStateUpdate(Map<String, String> arg0, List<PHHueError> arg1) {
            Log.w(TAG, "Light has updated");
        }

        @Override
        public void onError(int arg0, String arg1) {}

        @Override
        public void onReceivingLightDetails(PHLight arg0) {}

        @Override
        public void onReceivingLights(List<PHBridgeResource> arg0) {}

        @Override
        public void onSearchComplete() {}
    };

    public void onDestroy() {
        PHBridge bridge = phHueSDK.getSelectedBridge();
        if (bridge != null) {
            if (phHueSDK.isHeartbeatEnabled(bridge)) {        //if close app disconnect bridge
                phHueSDK.disableHeartbeat(bridge);
            }
            phHueSDK.disconnect(bridge);
            super.onDestroy();
        }
    }



}

