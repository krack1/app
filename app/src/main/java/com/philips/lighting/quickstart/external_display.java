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

import java.util.ArrayList;
import java.util.Collections;


public class external_display extends Activity {
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

    prefs_led_state = getSharedPreferences("ledstate", MODE_PRIVATE);

        int i = 1;

        String t = Integer.toString(i);

       while(prefs_led_state.getString("id_" + t, "") != "") {

           Log.i(TAG, "id_"+t+", id :"+prefs_led_state.getString("id_" + t, ""));
           Log.i(TAG, "name_"+t+", name : "+prefs_led_state.getString("name_" + t, ""));

           mAdapter.addItem(getResources().getDrawable(R.drawable.ok), prefs_led_state.getString("id_" + t, ""), prefs_led_state.getString("name_" + t, ""));

           i++;
           t = Integer.toString(i);
        }

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                ListData mData = mAdapter.mListData.get(position);
                Toast.makeText(external_display.this, mData.mTitle, Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(getApplicationContext(), external_MyApplicationActivity.class);
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
        getMenuInflater().inflate(R.menu.menu_external_display, menu);
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
        Intent intent = new Intent(getApplicationContext(), external_display.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
            intent.addFlags(0x8000); // equal to Intent.FLAG_ACTIVITY_CLEAR_TASK which is only available from API level 11
        startActivity(intent);
    }

    protected void onDestroy() {
            super.onDestroy();

    }
}
