package com.philips.lighting.quickstart;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.philips.lighting.data.ListData;
import com.philips.lighting.hue.sdk.PHHueSDK;
import com.philips.lighting.model.PHBridge;
import com.philips.lighting.model.PHLight;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class led_display extends Activity {
    private PHHueSDK phHueSDK;
    public static final String TAG = "QuickStart";
    private ListView mListView = null;
    private LedListAdapter mAdapter = null;
    public SharedPreferences prefs_led_state;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.app_name);
        setContentView(R.layout.activity_led_display);

        mListView = (ListView) findViewById(R.id.mList);

        mAdapter = new LedListAdapter(this);
        mListView.setAdapter(mAdapter);


        phHueSDK = PHHueSDK.create();

        PHBridge bridge = phHueSDK.getSelectedBridge();

        List<PHLight> allLights = bridge.getResourceCache().getAllLights();

        List<PHLight> myArrayData = new ArrayList<PHLight>();

        //List sorted led
        for(int i = 0; i< allLights.size(); i++) {
            myArrayData.add(allLights.get(i));
        }

        Comparator<PHLight> myComparator = new Comparator<PHLight>() {
            private final Collator collator = Collator.getInstance();
            @Override
            public int compare(PHLight phLight, PHLight t1) {
                return collator.compare(phLight.getIdentifier(), t1.getIdentifier());
            }
        };

        Collections.sort(myArrayData, myComparator);


        prefs_led_state = getSharedPreferences("ledstate", MODE_PRIVATE);
        SharedPreferences.Editor ed = prefs_led_state.edit();
        for (PHLight light : myArrayData) {

            ed.putString("id_" + light.getIdentifier(), light.getIdentifier());
            ed.putString("name_" + light.getIdentifier(), light.getName());

            Log.i(TAG, "id_" + light.getIdentifier() + ", " + light.getIdentifier());
            Log.i(TAG, "name_"+light.getIdentifier()+", "+light.getName());
            mAdapter.addItem(getResources().getDrawable(R.drawable.ok), light.getIdentifier(), light.getName());
        }
        ed.commit();

/*
        mAdapter.addItem(null,
                "check1",
                "2014-02-18");
        mAdapter.addItem(null,
                "check2",
                "2014-02-01");
        mAdapter.addItem(null,
                "check3",
                "2014-02-04");
        mAdapter.addItem(null,
                "check4",
                "2014-02-15");
*/
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            public void onItemClick(AdapterView<?> parent, View v, int position, long id){
                ListData mData = mAdapter.mListData.get(position);
                Toast.makeText(led_display.this, mData.mTitle, Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(getApplicationContext(), MyApplicationActivity.class);
                intent.putExtra("name", mData.mTitle);
                startActivity(intent);

            }
        });


    }

    private class ViewHolder {
        public ImageView mIcon;

        public TextView mText;

        public TextView mDate;
    }

    private class LedListAdapter extends BaseAdapter {
        private Context mContext = null;
        private ArrayList<ListData> mListData = new ArrayList<ListData>();

        public LedListAdapter(Context mContext) {
            super();
            this.mContext = mContext;
        }

        @Override
        public int getCount() {
            return mListData.size();
        }

        @Override
        public Object getItem(int position) {
            return mListData.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        public void addItem(Drawable icon, String mTitle, String mDate){
            ListData addInfo = null;
            addInfo = new ListData();
            addInfo.mIcon = icon;
            addInfo.mTitle = mTitle;
            addInfo.mDate = mDate;

            mListData.add(addInfo);
        }

        public void remove(int position){
            mListData.remove(position);
            dataChange();
        }

        public void sort(){
            Collections.sort(mListData, ListData.ALPHA_COMPARATOR);
            dataChange();
        }

        public void dataChange(){
            mAdapter.notifyDataSetChanged();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();

                LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.selectled_item, null);

                holder.mIcon = (ImageView) convertView.findViewById(R.id.mImage);
                holder.mText = (TextView) convertView.findViewById(R.id.mText);
                holder.mDate = (TextView) convertView.findViewById(R.id.mDate);

                convertView.setTag(holder);
            }else{
                holder = (ViewHolder) convertView.getTag();
            }

            ListData mData = mListData.get(position);

            if (mData.mIcon != null) {
                holder.mIcon.setVisibility(View.VISIBLE);
                holder.mIcon.setImageDrawable(mData.mIcon);
            }else{
                holder.mIcon.setVisibility(View.GONE);
            }

            holder.mText.setText(mData.mTitle);
            holder.mDate.setText(mData.mDate);

            return convertView;
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.start, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
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
        //return super.onOptionsItemSelected(item);
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
        Intent intent = new Intent(getApplicationContext(), led_settingActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
            intent.addFlags(0x8000); // equal to Intent.FLAG_ACTIVITY_CLEAR_TASK which is only available from API level 11
        startActivity(intent);
    }

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
