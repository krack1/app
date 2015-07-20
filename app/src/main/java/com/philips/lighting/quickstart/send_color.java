package com.philips.lighting.quickstart;

import android.util.Log;

import com.philips.lighting.hue.listener.PHLightListener;
import com.philips.lighting.hue.sdk.PHBridgeSearchManager;
import com.philips.lighting.hue.sdk.PHHueSDK;
import com.philips.lighting.model.PHBridge;
import com.philips.lighting.model.PHBridgeResource;
import com.philips.lighting.model.PHHueError;
import com.philips.lighting.model.PHLight;
import com.philips.lighting.model.PHLightState;

import java.util.List;
import java.util.Map;

/**
 * Created by jaeho on 2015-07-20.
 */
public class send_color {
    private PHHueSDK phHueSDK;
    public static final String TAG = "QuickStart";

    public void findBridges() {
        phHueSDK = PHHueSDK.getInstance();
        PHBridgeSearchManager sm = (PHBridgeSearchManager) phHueSDK.getSDKService(PHHueSDK.SEARCH_BRIDGE);
        sm.search(true, true);

    }


    public void setLights() {
        phHueSDK = PHHueSDK.create();



        PHBridge bridge = phHueSDK.getSelectedBridge();

        PHLightState lightState = new PHLightState();

        String light = "3";
        lightState.setHue(35000);
        lightState.setBrightness(200);
        lightState.setSaturation(200);

        // To validate your lightstate is valid (before sending to the bridge) you can use:
        // String validState = lightState.validateState();

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


    public static void main(String args[]) {

        send_color s = new send_color();
        s.findBridges();
        s.setLights();
    }
}
