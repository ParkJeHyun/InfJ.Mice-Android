package com.infjay.mice;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.infjay.mice.adapter.IndoorMapAdapter;
import com.infjay.mice.adapter.ViewHolder;
import com.infjay.mice.artifacts.IndoorMapInfo;

import java.util.ArrayList;


public class IndoorMapActivity extends ActionBarActivity {

    private ListView lvIndoorMapList;

    private IndoorMapAdapter adapter;
    private IndoorMapInfo imInfo;
    private ArrayList<IndoorMapInfo> imArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_indoor_map);


        lvIndoorMapList = (ListView)findViewById(R.id.lvIndoorMapList);

        imArrayList = new ArrayList<IndoorMapInfo>();

        imInfo = new IndoorMapInfo();
        imInfo.title = "지하1층";
        imArrayList.add(imInfo);
        imInfo = new IndoorMapInfo();
        imInfo.title = "1층";
        imArrayList.add(imInfo);
        imInfo = new IndoorMapInfo();
        imInfo.title = "2층";
        imArrayList.add(imInfo);

        adapter = new IndoorMapAdapter(getApplication(), R.layout.list_row, imArrayList);
        lvIndoorMapList.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        lvIndoorMapList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ViewHolder vh = (ViewHolder)view.getTag();
                String rowName = vh.tvIndoorMapTitle.getText().toString();

                //start Activity about sponser clicked
                Toast.makeText(getApplicationContext(), rowName + " clicked()", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(getApplicationContext(), IndoorMapInfoActivity.class);
                startActivity(intent);
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

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
