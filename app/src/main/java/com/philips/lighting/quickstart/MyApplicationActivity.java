package com.philips.lighting.quickstart;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.SeekBar;

import com.philips.lighting.hue.listener.PHLightListener;
import com.philips.lighting.hue.sdk.PHHueSDK;
import com.philips.lighting.hue.sdk.utilities.impl.Color;
import com.philips.lighting.model.PHBridge;
import com.philips.lighting.model.PHBridgeResource;
import com.philips.lighting.model.PHHueError;
import com.philips.lighting.model.PHLight;
import com.philips.lighting.model.PHLightState;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
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
    public SharedPreferences prefs_led_state;

    SeekBar seekHue;
    SeekBar seekSat;
    SeekBar seekBri;

    public String hue;
    public String sat;
    public String bri;
    public String name;

    //tcp client
    private String return_msg;


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

        Intent intent = getIntent();
        name = intent.getExtras().getString("name");

        hue = "hue"+name;
        sat = "sat"+name;
        bri = "bri"+name;

        seekHue = (SeekBar) findViewById(R.id.hue_seekBar);
        seekSat = (SeekBar) findViewById(R.id.sat_seekBar);
        seekBri = (SeekBar) findViewById(R.id.bri_seekBar);

        prefs_led_state = getSharedPreferences("ledFile", MODE_PRIVATE);
        seekHue.setProgress(prefs_led_state.getInt(hue, 0));
        seekSat.setProgress(prefs_led_state.getInt(sat, 0));
        seekBri.setProgress(prefs_led_state.getInt(bri, 0));

        Button sendButton;
        sendButton = (Button) findViewById(R.id.buttonSend);
        sendButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                setLights();

                //socket program is not working in main Thread so make Thread
                TCPclient tp = new TCPclient(name, h, s, b);
                Thread td = new Thread(tp);
                td.start();
            }

        });
    }

    private class TCPclient implements Runnable {
        private final String serverIP = "192.168.0.6";  // ex: 192.168.0.100
        private final int serverPort = 9999; // ex: 5555
        private String tcp_id;
        private int tcp_hue;
        private int tcp_sat;
        private int tcp_bri;

        //private String return_msg;

        public TCPclient(String tcp_id, int tcp_hue, int tcp_sat, int tcp_bri) {
            this.tcp_id = tcp_id;
            this.tcp_hue = tcp_hue;
            this.tcp_sat = tcp_sat;
            this.tcp_bri = tcp_bri;
        }

        @Override
        public void run() {
            // TODO Auto-generated method stub
            try {

                InetAddress serverAddr = InetAddress.getByName(serverIP);

                Log.d("TCP", "C: Connecting...");

                Socket socket = new Socket(serverIP, serverPort);

                try {
                    JSONObject obj = new JSONObject();

                    obj.put("light", tcp_id);
                    obj.put("sat", new  Integer(tcp_hue));
                    obj.put("bri", new Integer(tcp_bri));
                    obj.put("hue", new Integer(tcp_sat));


                    Log.d("TCP", "C: Sending: '" + obj + "'");
                    PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);

                    out.println(obj);
                    Log.d("TCP", "C: Sent.");
                    Log.d("TCP", "C: Done.");

                    BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    return_msg = in.readLine();

                    Log.d("TCP", "C: Server send to me this message -->" + return_msg);
                } catch (Exception e) {
                    Log.e("TCP", "C: Error1", e);
                } finally {
                    socket.close();
                }
            } catch (Exception e) {
                Log.e("TCP", e.getMessage(), e);
            }
        }
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
                startMainActivity_1();
                break;
            case R.id.led_setting :
                startMainActivity_2();
                break;
            case R.id.info_setting :
                startMainActivity_4();

                break;
            case R.id.alam_setting :
                startMainActivity_3();
                break;
        }
        return true;
    }




    public void setLights() {

        RelativeLayout m_RelativeLaout;
        //TextView hsb_result;


        m_RelativeLaout = (RelativeLayout) findViewById(R.id.main_Relation);
        //hsb_result = (TextView) findViewById(R.id.result);

        PHBridge bridge = phHueSDK.getSelectedBridge();

        PHLightState lightState = new PHLightState();



        h = seekHue.getProgress();
        s = seekSat.getProgress();
        b = seekBri.getProgress();
        //print hue, sat, bri
        //hsb_result.append("h : " + h + " s : " + s + "b : " + b);

        lightState.setOn(true);
        huetorgb(h, s, b);


        m_RelativeLaout.setBackgroundColor(Color.argb(255, m_red, m_green, m_blue));

        lightState.setHue(seekHue.getProgress());
        lightState.setBrightness(seekBri.getProgress());
        lightState.setSaturation(seekSat.getProgress());

        //prefs_led_state = getSharedPreferences("ledFile", MODE_PRIVATE);
        Log.d("test", hue+","+sat+","+bri);
        prefs_led_state = getSharedPreferences("ledFile", MODE_PRIVATE);
        SharedPreferences.Editor ed = prefs_led_state.edit();
        ed.putInt(hue, seekHue.getProgress());
        ed.putInt(sat, seekSat.getProgress());
        ed.putInt(bri, seekBri.getProgress());
        ed.commit();

        // To validate your lightstate is valid (before sending to the bridge) you can use:
        // String validState = lightState.validateState();
        if(b == 0) {
            lightState.setOn(false);
        }
        else {
            lightState.setOn(true);
        }
            bridge.updateLightState(name, lightState, listener);

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

    public void startMainActivity_1() {
        Intent intent = new Intent(getApplicationContext(), PHHomeActivity_Find.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
            intent.addFlags(0x8000); // equal to Intent.FLAG_ACTIVITY_CLEAR_TASK which is only available from API level 11
        startActivity(intent);
    }
    public void startMainActivity_2() {
        Intent intent = new Intent(getApplicationContext(), led_display.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
            intent.addFlags(0x8000); // equal to Intent.FLAG_ACTIVITY_CLEAR_TASK which is only available from API level 11
        startActivity(intent);
    }
    public void startMainActivity_3() {
        Intent intent = new Intent(getApplicationContext(), alam_display.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
            intent.addFlags(0x8000); // equal to Intent.FLAG_ACTIVITY_CLEAR_TASK which is only available from API level 11
        startActivity(intent);
    }
    public void startMainActivity_4() {
        Intent intent = new Intent(getApplicationContext(),led_settingActivity.class);
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

        if((m_hue>=0 && m_hue<=60 )||( m_hue>=300 && m_hue<=360))
            m_cr=255;
        else if(m_hue>60 && m_hue<120)
            m_cr=255-i;
        else if(m_hue>240 && m_hue<300)
            m_cr=i;
        else
            m_cr=0;

        if(m_hue>=60 && m_hue<=180)
            m_cg=255;
        else if(m_hue>180 && m_hue<240)
            m_cg=255-i;
        else if(m_hue>0 && m_hue<60)
            m_cg=i;
        else
            m_cg=0;

        if(m_hue>=180 && m_hue<=300 )
            m_cb=255;
        else if(m_hue>300 && m_hue<360)
            m_cb=255-i;
        else if(m_hue>120 && m_hue<180)
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
