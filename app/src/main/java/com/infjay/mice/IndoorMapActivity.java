package com.infjay.mice;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.infjay.mice.adapter.IndoorMapAdapter;
import com.infjay.mice.adapter.MemoAdapter;
import com.infjay.mice.adapter.ViewHolder;
import com.infjay.mice.artifacts.AgendaSessionInfo;
import com.infjay.mice.artifacts.IndoorMapInfo;
import com.infjay.mice.artifacts.MemoInfo;
import com.infjay.mice.database.DBManager;
import com.infjay.mice.global.GlobalVariable;
import com.infjay.mice.network.AsyncHttpsTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class IndoorMapActivity extends CustomActionBarActivity {

    private boolean drawFlag = true;

    private ListView lvIndoorMapList;

    private IndoorMapAdapter adapter;
    private IndoorMapInfo imInfo;
    private ArrayList<IndoorMapInfo> indoorMapArrayList;

    protected Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            if(msg.what == 1)
            {
                try
                {
                    JSONObject jobj = new JSONObject(msg.obj + "");
                    if(jobj.get("messagetype").equals("get_indoor_map"))
                    {
                        if(jobj.get("result").equals("GET_INDOOR_MAP_ERROR"))
                        {
                            Toast.makeText(getApplicationContext(), "Error in getting indoor map information", Toast.LENGTH_SHORT).show();
                        }
                        else if(jobj.get("result").equals("GET_INDOOR_MAP_FAIL"))
                        {
                            Toast.makeText(getApplicationContext(), "Fail in getting indoor map information", Toast.LENGTH_SHORT).show();
                        }
                        else if(jobj.get("result").equals("GET_INDOOR_MAP_SUCCESS"))
                        {
                            JSONArray indoorMapJsonArray = new JSONArray(jobj.get("attach").toString());
                            IndoorMapInfo indoorMapInfo;
                            indoorMapArrayList = new ArrayList<IndoorMapInfo>();
                            for(int i = 0; i < indoorMapJsonArray.length(); i++)
                            {
                                JSONObject indoorMapJsonObj = new JSONObject(indoorMapJsonArray.get(i).toString());
                                indoorMapInfo = new IndoorMapInfo();

                                indoorMapInfo.indoorMapSeq = indoorMapJsonObj.get("indoor_map_seq").toString();
                                indoorMapInfo.order = indoorMapJsonObj.get("map_order").toString();
                                indoorMapInfo.title = indoorMapJsonObj.get("title").toString();
                                indoorMapInfo.imagePath = "null";

                                indoorMapArrayList.add(indoorMapInfo);
                            }

                            DBManager.getManager(getApplicationContext()).deleteIndoorMapInfo();
                            DBManager.getManager(getApplicationContext()).insertIndoorMapInfo(indoorMapArrayList);
                            setIndoorMap();
                            Toast.makeText(getApplicationContext(), "Refresh Done", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(), "Wrong Message result", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(), "Wrong MessageType", Toast.LENGTH_SHORT).show();
                    }

                }
                catch(JSONException e)
                {
                    e.printStackTrace();
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_indoor_map);

        setListView();
    }

    public void setListView(){
        int count = DBManager.getManager(getApplicationContext()).getIndoorMapCount();

        if(count == 0){
            refresh();
        }
        else{
            setIndoorMap();
        }
    }

    public void setIndoorMap(){
        lvIndoorMapList = (ListView)findViewById(R.id.lvIndoorMapList);

        indoorMapArrayList = new ArrayList<IndoorMapInfo>();

        indoorMapArrayList = DBManager.getManager(getApplicationContext()).getAllIndoorMap();
        adapter = new IndoorMapAdapter(getApplication(), R.layout.list_row_indoor_map, indoorMapArrayList);

        lvIndoorMapList.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        lvIndoorMapList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ViewHolder vh = (ViewHolder)view.getTag();
                String mapSeq = vh.indoorMapSeq;

                Intent intent = new Intent(getApplicationContext(), IndoorMapInfoActivity.class);
                intent.putExtra("mapSeq", mapSeq);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_from_right, R.anim.slide_out_to_left);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_indoor_map, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.itRefreshIndoorMapInfo) {
            refresh();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void refresh()
    {
        //request to server
        JSONObject jobj = new JSONObject();
        try
        {
            jobj.put("messagetype", "get_indoor_map");
        }
        catch(JSONException e)
        {
            e.printStackTrace();
        }
        new AsyncHttpsTask(getApplicationContext(), GlobalVariable.WEB_SERVER_IP, mHandler, jobj, 1, 0);
    }
}
