package com.philips.lighting.quickstart;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.philips.lighting.data.HueSharedPreferences;
import com.philips.lighting.hue.listener.PHBridgeConfigurationListener;
import com.philips.lighting.hue.sdk.PHHueSDK;
import com.philips.lighting.hue.sdk.bridge.impl.PHBridgeResourcesCacheImpl;
import com.philips.lighting.model.PHBridge;
import com.philips.lighting.model.PHBridgeConfiguration;
import com.philips.lighting.model.PHHueError;

import java.util.List;
import java.util.Map;


public class setting_Bridge extends Activity {
    private PHHueSDK phHueSDK;
    private HueSharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting__bridge);

        phHueSDK = PHHueSDK.create();


        final EditText bridge_name;
        TextView ip_name;
        TextView mask_name;
        TextView gateway_name;

        bridge_name = (EditText) findViewById(R.id.get_name);
        ip_name = (TextView) findViewById(R.id.get_ip);
        mask_name = (TextView) findViewById(R.id.get_mask);
        gateway_name = (TextView) findViewById(R.id.get_gateway);

        //set of bridge info
        final PHBridge bridge = phHueSDK.getSelectedBridge();
        final PHBridgeResourcesCacheImpl cache = (PHBridgeResourcesCacheImpl)bridge.getResourceCache();
        final PHBridgeConfiguration newBridgeConfig = cache.getBridgeConfiguration();

        Log.i("test", "id :" + newBridgeConfig.getName() + "," + newBridgeConfig.getIpAddress() + "," + newBridgeConfig.getNetmask() + "," + newBridgeConfig.getGateway());

        bridge_name.setText(newBridgeConfig.getName());
        ip_name.setText(newBridgeConfig.getIpAddress());
        mask_name.setText(newBridgeConfig.getNetmask());
        gateway_name.setText(newBridgeConfig.getGateway());


        Button change_button;
        change_button = (Button) findViewById(R.id.change_name);
        change_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final PHBridgeConfigurationListener listner = new PHBridgeConfigurationListener() {
                    @Override
                    public void onReceivingConfiguration(PHBridgeConfiguration phBridgeConfiguration) {

                    }

                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError(int i, String s) {

                    }

                    @Override
                    public void onStateUpdate(Map<String, String> map, List<PHHueError> list) {

                    }
                };
                newBridgeConfig.setName(bridge_name.getText().toString());
                bridge.updateBridgeConfigurations(newBridgeConfig, listner);
            }

        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_setting__bridge, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
