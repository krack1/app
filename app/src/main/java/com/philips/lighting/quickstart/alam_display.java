package com.philips.lighting.quickstart;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
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
import java.util.List;


public class alam_display extends Activity {
    private ListView aListView = null;
    private LedListAdapter aAdapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alam_display);

        aListView = (ListView) findViewById(R.id.aList);

        aAdapter = new LedListAdapter(this);
        aListView.setAdapter(aAdapter);

        aAdapter.addItem(getResources().getDrawable(R.drawable.phone), "PHONE", "010-****-****");
        aAdapter.addItem(getResources().getDrawable(R.drawable.sms), "SMS", "010-****-****");

        final PackageManager pm = getPackageManager();
        List<ApplicationInfo> list = pm.getInstalledApplications(PackageManager.GET_UNINSTALLED_PACKAGES);
        for(ApplicationInfo applicationInfo : list) {
            if((applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 1)
            aAdapter.addItem(applicationInfo.loadIcon(pm), String.valueOf(applicationInfo.loadLabel(pm)), applicationInfo.packageName);
        }



        aListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                ListData mData = aAdapter.aListData.get(position);
                Toast.makeText(alam_display.this, mData.mTitle, Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(getApplicationContext(), AlamApplicationActivity.class);
                intent.putExtra("app", mData.mTitle);
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
        private Context aContext = null;
        private ArrayList<ListData> aListData = new ArrayList<ListData>();

        public LedListAdapter(Context aContext) {
            super();
            this.aContext = aContext;
        }

        @Override
        public int getCount() {
            return aListData.size();
        }

        @Override
        public Object getItem(int position) {
            return aListData.get(position);
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

            aListData.add(addInfo);
        }

        public void remove(int position){
            aListData.remove(position);
            dataChange();
        }

        public void sort(){
            Collections.sort(aListData, ListData.ALPHA_COMPARATOR);
            dataChange();
        }

        public void dataChange(){
            aAdapter.notifyDataSetChanged();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();

                LayoutInflater inflater = (LayoutInflater) aContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.selectled_item, null);

                holder.mIcon = (ImageView) convertView.findViewById(R.id.mImage);
                holder.mText = (TextView) convertView.findViewById(R.id.mText);
                holder.mDate = (TextView) convertView.findViewById(R.id.mDate);

                convertView.setTag(holder);
            }else{
                holder = (ViewHolder) convertView.getTag();
            }

            ListData mData = aListData.get(position);

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
        Intent intent = new Intent(getApplicationContext(), PHHomeActivity.class);
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




}
