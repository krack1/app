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

public class CallCatcherActivity extends Activity {
    private PHHueSDK phHueSDK;
    public static final String TAG = "QuickStart";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        phHueSDK = PHHueSDK.create();

        String light = "1";
        int seekHue = 35000;
        int seekBri = 254;
        int seekSat = 254;

        PHBridge bridge = phHueSDK.getSelectedBridge();

        PHLightState lightState = new PHLightState();

        lightState.setHue(seekHue);
        lightState.setBrightness(seekBri);
        lightState.setSaturation(seekSat);

        bridge.updateLightState(light, lightState, listener);

    }

    PHLightListener listener = new PHLightListener() {

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

}