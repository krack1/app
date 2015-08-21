package com.philips.lighting.quickstart;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.philips.lighting.data.ListData;

import java.util.ArrayList;
import java.util.Collections;


public class led_settingActivity extends Activity {
    public static final String TAG = "QuickStart";
    private ListView sListView = null;
    private LedListAdapter sAdapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.app_name);
        setContentView(R.layout.activity_led_setting);

        sListView = (ListView) findViewById(R.id.sList);

        sAdapter = new LedListAdapter(this);
        sListView.setAdapter(sAdapter);

        sAdapter.addItem("BRIDGE");
        sAdapter.addItem("LIGHT");
        sAdapter.addItem("NOTIFICATION");

        sListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                ListData mData = sAdapter.mListData.get(position);
                Toast.makeText(led_settingActivity.this, mData.mTitle, Toast.LENGTH_SHORT).show();
                Intent intent;
                switch(mData.mTitle) {
                    case "BRIDGE":
                        intent = new Intent(getApplicationContext(), setting_Bridge.class);
                        intent.putExtra("name", mData.mTitle);
                        startActivity(intent);
                        break;
                    case "LIGHT":
                        intent = new Intent(getApplicationContext(), light_settingActivity.class);
                        startActivity(intent);
                        break;
                    case "NOTIFICATION":
                        intent = new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS");
                        startActivity(intent);


                }

            }
        });
    }

    @Override
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

    private class ViewHolder {
        public TextView mText;
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

        public void addItem(String mTitle){
            ListData addInfo = null;
            addInfo = new ListData();
            addInfo.mTitle = mTitle;

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
            sAdapter.notifyDataSetChanged();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();

                LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.selectsetting_item, null);

                holder.mText = (TextView) convertView.findViewById(R.id.mText);

                convertView.setTag(holder);
            }else{
                holder = (ViewHolder) convertView.getTag();
            }

            ListData mData = mListData.get(position);

            holder.mText.setText(mData.mTitle);

            return convertView;
        }
    }
}
