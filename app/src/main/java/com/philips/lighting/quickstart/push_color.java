package com.philips.lighting.quickstart;

import android.app.Activity;
import android.os.Bundle;
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


public class push_color extends Activity {
    private PHHueSDK phHueSDK;
    public static final String TAG = "QuickStart";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        phHueSDK = PHHueSDK.create();

        try {
            setLights();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
    public void setLights() throws InterruptedException {



        PHBridge bridge = phHueSDK.getSelectedBridge();

        PHLightState lightState = new PHLightState();


        String light = "4";
        lightState.setHue(35000);
        lightState.setBrightness(200);
        lightState.setSaturation(200);

        // To validate your lightstate is valid (before sending to the bridge) you can use:
        // String validState = lightState.validateState();

        bridge.updateLightState(light, lightState, listener_a);
        Thread.sleep(3000);

        lightState.setHue(5000);
        lightState.setBrightness(200);
        lightState.setSaturation(200);

        bridge.updateLightState(light, lightState, listener_a);


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

    protected void onDestroy() {
        PHBridge bridge = phHueSDK.getSelectedBridge();
        if (bridge != null) {

            if (phHueSDK.isHeartbeatEnabled(bridge)) {
                phHueSDK.disableHeartbeat(bridge);
            }

            phHueSDK.disconnect(bridge);
            super.onDestroy();
        }
    }



}

