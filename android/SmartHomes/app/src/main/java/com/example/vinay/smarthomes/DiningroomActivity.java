package com.example.vinay.smarthomes;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by vinay on 15-01-2018.
 */

public class DiningroomActivity extends AppCompatActivity
{
    String[] devices = {"Light", "Fan", "A.C."};

    // Array of booleans to store toggle button status
    public boolean[] status = {false,false,false};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diningroom);

        /** Restore from the previous state if exists */
        if(savedInstanceState!=null){
            status = savedInstanceState.getBooleanArray("status");
        }

        ListView deviceList = (ListView) findViewById(R.id.deviceListDiningroom);

        AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> listView, View item, int position, long id) {

                ListView lView = (ListView) listView;

                SimpleAdapter adapter = (SimpleAdapter) lView.getAdapter();

                HashMap<String,Object> hm = (HashMap) adapter.getItem(position);

                /** The clicked Item in the ListView */
                RelativeLayout rLayout = (RelativeLayout) item;

                /** Getting the toggle button corresponding to the clicked item */
                ToggleButton tgl = (ToggleButton) rLayout.getChildAt(1);

                String strStatus = "";
                if(tgl.isChecked())
                {
                    int roomId = 4;
                    String path = "http://192.168.1.4:3010/off";
                    new DeviceController().execute(path+"?room="+roomId+"&&deviceId="+position);
                    tgl.setChecked(false);
                    strStatus = "Off";
                    status[position]=false;
                }
                else
                {
                    int roomId = 4;
                    String path = "http://192.168.1.4:3010/on";
                    new DeviceController().execute(path+"?room="+roomId+"&&deviceId="+position);
                    tgl.setChecked(true);
                    strStatus = "On";
                    status[position]=true;
                }
                Toast.makeText(getBaseContext(), (String) hm.get("txt") + " : " + strStatus, Toast.LENGTH_SHORT).show();
            }
        };

        deviceList.setOnItemClickListener(itemClickListener);

        // Each row in the list stores device name and its status
        List<HashMap<String,Object>> aList = new ArrayList<HashMap<String,Object>>();

        for(int i=0;i<devices.length;i++){
            HashMap<String, Object> hm = new HashMap<String,Object>();
            hm.put("txt", devices[i]);
            hm.put("stat",status[i]);
            aList.add(hm);
        }

        // Keys used in Hashmap
        String[] from = {"txt","stat" };

        // Ids of views in listview_layout
        int[] to = { R.id.device_item, R.id.tgl_status};

        // Instantiating an adapter to store each items
        // R.layout.listview_layout defines the layout of each item
        SimpleAdapter adapter = new SimpleAdapter(getBaseContext(), aList, R.layout.activity_devicelist, from, to);

        deviceList.setAdapter(adapter);
    }

    /** Saving the current state of the activity
     * for configuration changes [ Portrait <=> Landscape ]
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBooleanArray("status", status);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_diningroom, menu);
        return true;
    }

}
