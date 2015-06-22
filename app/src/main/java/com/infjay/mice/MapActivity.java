package com.infjay.mice;

/**
 * Created by Administrator on 2015-05-02.
 */

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.infjay.mice.artifacts.IndoorMapInfo;

import java.util.ArrayList;

public class MapActivity extends ActionBarActivity {
    Button btnConferenceMap;
    Button btnIndoorMap;
    Button btnSurroundings;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        btnConferenceMap = (Button)findViewById(R.id.btnConferenceMap);
        btnIndoorMap = (Button)findViewById(R.id.btnIndoorMap);
        btnSurroundings = (Button)findViewById(R.id.btnSurroundings);

        btnConferenceMap.setOnClickListener(new MapBtnListener());
        btnIndoorMap.setOnClickListener(new MapBtnListener());
        btnSurroundings.setOnClickListener(new MapBtnListener());


    }

    class MapBtnListener implements View.OnClickListener{
        Intent intent;
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.btnConferenceMap :
                    intent = new Intent(getApplicationContext(),
                            ConfMapActivity.class);
                    startActivity(intent);
                    break;
                case R.id.btnIndoorMap :
                    intent = new Intent(getApplicationContext(),
                            IndoorMapActivity.class);
                    startActivity(intent);
                    break;
                case R.id.btnSurroundings :
                    intent = new Intent(getApplicationContext(),
                            SurroundActivity.class);
                    startActivity(intent);
            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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