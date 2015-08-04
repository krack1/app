package com.philips.lighting.quickstart;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.philips.lighting.data.ListData_checkbox;
import com.philips.lighting.hue.sdk.PHHueSDK;
import com.philips.lighting.model.PHBridge;
import com.philips.lighting.model.PHLight;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class select_led_activity extends Activity {

    private PHHueSDK phHueSDK;
    public static final String TAG = "QuickStart";
    public SharedPreferences prefs_led_state;

    private ListView cListView = null;
    private CheckBox cAllCheckBox = null;
    private CustomAdapter cCustomAdapter = null;
    private Button cCountBt = null;
    public String fileName;

    private ArrayList<String> cArrayList = new ArrayList<String>();
    private ArrayList<String> bArrayList = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_led_activity);

        setLayout();

        phHueSDK = PHHueSDK.create();

        PHBridge bridge = phHueSDK.getSelectedBridge();

        List<PHLight> allLights = bridge.getResourceCache().getAllLights();

        List<PHLight> myArrayData = new ArrayList<PHLight>();


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

        for (PHLight light : myArrayData) {
            bArrayList.add(light.getName());
            cArrayList.add("");
        }

        cCustomAdapter = new CustomAdapter(select_led_activity.this , cArrayList, bArrayList);
        cListView.setAdapter(cCustomAdapter);
        cListView.setOnItemClickListener(mItemClickListener);

    }

    private AdapterView.OnItemClickListener mItemClickListener = new
            AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> arg0, View arg1,
                                        int position, long arg3) {
                    Toast.makeText(getApplicationContext(), "" + (position + 1),
                            Toast.LENGTH_SHORT).show();

                    cCustomAdapter.setChecked(position);
                    // Data 변경시 호출 Adapter에 Data 변경 사실을 알려줘서 Update 함.
                    cCustomAdapter.notifyDataSetChanged();

                }
            };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_select_led_activity, menu);
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

    class ViewHolder {
        public ImageView cIcon;

        public TextView cText;

        public CheckBox cBox;
    }

    class CustomAdapter extends BaseAdapter {

        private ViewHolder viewHolder = null;

        private LayoutInflater inflater = null;
        private ArrayList<String> sArrayList = new ArrayList<String>();
        private ArrayList<String> pArrayList = new ArrayList<String>();
        private ArrayList<ListData_checkbox> mListData = new ArrayList<>();
        private boolean[] isCheckedConfrim;
        private boolean[] isCheckedConfrim_text;

        public CustomAdapter (Context c , ArrayList<String> mList, ArrayList<String> pList){
            inflater = LayoutInflater.from(c);
            this.sArrayList = mList;
            this.pArrayList = pList;

            this.isCheckedConfrim = new boolean[sArrayList.size()];
            this.isCheckedConfrim_text = new boolean[pArrayList.size()];
        }


        public void setAllChecked(boolean ischeked) {
            int tempSize = isCheckedConfrim.length;
            for(int a=0 ; a<tempSize ; a++){
                isCheckedConfrim[a] = ischeked;
            }
            int tempSize_text = isCheckedConfrim_text.length;
            for(int a=0 ; a<tempSize_text ; a++){
                isCheckedConfrim_text[a] = ischeked;
            }
        }

        public void setChecked(int position) {
            isCheckedConfrim[position] = !isCheckedConfrim[position];
            isCheckedConfrim_text[position] = !isCheckedConfrim_text[position];
        }

        public ArrayList<Integer> getChecked(){
            int tempSize = isCheckedConfrim.length;
            ArrayList<Integer> mArrayList = new ArrayList<Integer>();
            for(int b=0 ; b<tempSize ; b++){
                if(isCheckedConfrim[b]){
                    mArrayList.add(b);
                }
            }
            return mArrayList;
        }





        @Override
        public int getCount() {
            return sArrayList.size();
        }

        @Override
        public Object getItem(int arg0) {
            return null;
        }

        @Override
        public long getItemId(int arg0) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {


            View v = convertView;

            if( v == null ){
                viewHolder = new ViewHolder();

                v = inflater.inflate(R.layout.row, null);
                viewHolder.cIcon = (ImageView) v.findViewById(R.id.cImage);
                viewHolder.cText = (TextView) v.findViewById(R.id.cText);
                viewHolder.cBox = (CheckBox) v.findViewById(R.id.cCheck);

                v.setTag(viewHolder);
            }
            else {
                viewHolder = (ViewHolder)v.getTag();
            }


            viewHolder.cBox.setClickable(false);
            viewHolder.cBox.setFocusable(false);

            viewHolder.cIcon.getResources().getDrawable(R.drawable.ok);
            viewHolder.cText.setText(pArrayList.get(position));
            viewHolder.cBox.setText(sArrayList.get(position));

            viewHolder.cBox.setChecked(isCheckedConfrim[position]);

            return v;
        }
    }

    private void setLayout(){
        final String[] id = new String[1];

        cListView = (ListView) findViewById(R.id.main_listview);

        cCountBt = (Button) findViewById(R.id.check_led);
        cCountBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = getIntent();
                fileName = intent.getExtras().getString("app_select");

                prefs_led_state = getSharedPreferences(fileName, MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs_led_state.edit();
                editor.clear();
                editor.commit();

                prefs_led_state = getSharedPreferences(fileName, MODE_PRIVATE);
                SharedPreferences.Editor ed = prefs_led_state.edit();
                for(int i=0 ; i<cCustomAdapter.getChecked().size() ; i++){
                    Log.d("test", "체크되 있는 Position = " + fileName +" : check" + Integer.toString((cCustomAdapter.getChecked().get(i) + 1)));
                    id[0] = Integer.toString((cCustomAdapter.getChecked().get(i) + 1));

                    ed.putString("check"+id[0], id[0]);
                    ed.commit();

                }

            }
        });

        cAllCheckBox = (CheckBox) findViewById(R.id.main_all_check_box);


        // 전체 체크 버튼 클릭시 Listener
        cAllCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cCustomAdapter.setAllChecked(cAllCheckBox.isChecked());
                // Adapter에 Data에 변화가 생겼을때 Adapter에 알려준다.
                cCustomAdapter.notifyDataSetChanged();
            }
        });
    }



}
