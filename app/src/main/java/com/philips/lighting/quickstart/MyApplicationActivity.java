package com.philips.lighting.quickstart;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.philips.lighting.hue.listener.PHLightListener;
import com.philips.lighting.hue.sdk.PHHueSDK;
import com.philips.lighting.hue.sdk.utilities.impl.Color;
import com.philips.lighting.model.PHBridge;
import com.philips.lighting.model.PHBridgeResource;
import com.philips.lighting.model.PHHueError;
import com.philips.lighting.model.PHLight;
import com.philips.lighting.model.PHLightState;

import java.util.List;
import java.util.Map;

/**
 * MyApplicationActivity - The starting point for creating your own Hue App.  
 * Currently contains a simple view with a button to change your lights to random colours.  Remove this and add your own app implementation here! Have fun!
 * 
 * @author SteveyO
 *
 */

public class MyApplicationActivity extends Activity {
    private PHHueSDK phHueSDK;
    private static final int MAX_HUE=65535;
    public static final String TAG = "QuickStart";
    public int h, s, b;
    public int m_red, m_green, m_blue;

    private SeekBar.OnSeekBarChangeListener seekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {
        public void onStopTrackingTouch(SeekBar seekBar)
        {
        }
        public void onStartTrackingTouch(SeekBar seekBar)
        {
        }

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
        {
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.app_name);
        setContentView(R.layout.activity_main);
        phHueSDK = PHHueSDK.create();
        Button randomButton;
        randomButton = (Button) findViewById(R.id.buttonSend);
        randomButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                startMainActivity();
            }

        });

    }

    public boolean onCreateOptionsMenu(Menu menu) {
        Log.w(TAG, "Inflating home menu");
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.start, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.back_start:
                startMainActivity();
                break;
        }
        return true;
    }


    public void setLights() {
        SeekBar seekHue;
        SeekBar seekSat;
        EditText textId;
        SeekBar seekBri;
        RelativeLayout m_RelativeLaout;
        TextView hsb_result;

        textId = (EditText) findViewById(R.id.edit_Id);
        seekHue = (SeekBar) findViewById(R.id.hue_seekBar);
        seekSat = (SeekBar) findViewById(R.id.sat_seekBar);
        seekBri = (SeekBar) findViewById(R.id.bri_seekBar);
        m_RelativeLaout = (RelativeLayout) findViewById(R.id.main_Relation);
        hsb_result = (TextView) findViewById(R.id.result);

        PHBridge bridge = phHueSDK.getSelectedBridge();

        PHLightState lightState = new PHLightState();

        h = seekHue.getProgress();
        s = seekSat.getProgress();
        b = seekBri.getProgress();

        hsb_result.append("h : " + h+" s : "+ s + "b : " + b);

        lightState.setOn(true);
        huetorgb(h, s, b);


        m_RelativeLaout.setBackgroundColor(Color.argb(255, m_red, m_green, m_blue));

        String light = textId.getText().toString();
        lightState.setHue(seekHue.getProgress());
        lightState.setBrightness(seekBri.getProgress());
        lightState.setSaturation(seekSat.getProgress());

        // To validate your lightstate is valid (before sending to the bridge) you can use:
        // String validState = lightState.validateState();
        if(b == 0) {
            lightState.setOn(false);
        }
        else {
            lightState.setOn(true);
        }
            bridge.updateLightState(light, lightState, listener);

            //  bridge.updateLightState(light, lightState);   // If no bridge response is required then use this simpler form.


    }
    // If you want to handle the response from the bridge, create a PHLightListener object.
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
    
    @Override
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

    public void startMainActivity() {
        Intent intent = new Intent(getApplicationContext(), CallCatcherActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
            intent.addFlags(0x8000); // equal to Intent.FLAG_ACTIVITY_CLEAR_TASK which is only available from API level 11
        startActivity(intent);
    }

    public void huetorgb(int hue, int bri, int sat) {
        double i;
        double m_hue, m_sat, m_bri, m_cr, m_cg, m_cb, sr, sg, sb;

        m_hue = (hue*360)/65535;
        m_sat = (sat*100)/254;
        m_bri = (bri*100)/254;

        i = ((m_hue%60)/60.00)*255;

        if((m_hue>=0 && m_hue<=60 )||( m_hue>=300 && m_hue<=360))   // Red 값이 255로 고정된 부분(Magenta~Yellow)
            m_cr=255;
        else if(m_hue>60 && m_hue<120)  // Red 값이 감소하는 위치 (Yellow ~ Green)
            m_cr=255-i;
        else if(m_hue>240 && m_hue<300) // Red 값이 상승하는 위치 (Blue ~ Magenta)
            m_cr=i;
        else
            m_cr=0;

        if(m_hue>=60 && m_hue<=180)      // Green 값이 고장되어 있는 위치 (Yellow ~ Cyan)
            m_cg=255;
        else if(m_hue>180 && m_hue<240) // Green값이 감소하는 위치 (Cyan ~ Blue)
            m_cg=255-i;
        else if(m_hue>0 && m_hue<60)     // Green값이 상승하는 위치 (Red ~ Yellow)
            m_cg=i;
        else
            m_cg=0;

        if(m_hue>=180 && m_hue<=300 )   // Blue 값이 고정되어 있는 위치 (Cyan ~ Magenta)
            m_cb=255;
        else if(m_hue>300 && m_hue<360) // Blue 값이 감소하는 위치 (Magenta ~ Red)
            m_cb=255-i;
        else if(m_hue>120 && m_hue<180) // Blue 값이 상승하는 위치 (Green ~ Cyan)
            m_cb=i;
        else
            m_cb=0;

        sr =m_cr+(255-m_cr)*(100-m_sat)/100.00;
        sg =m_cg+(255-m_cg)*(100-m_sat)/100.00;
        sb =m_cb+(255-m_cb)*(100-m_sat)/100.00;

        m_red = (int)(sr*m_bri/100);
        m_green = (int)(sg*m_bri/100);
        m_blue = (int)(sb*m_bri/100);
    }

}
