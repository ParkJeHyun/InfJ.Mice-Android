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

public class MapActivity extends CustomActionBarActivity {
    Button btnConferenceMap;
    Button btnIndoorMap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        btnConferenceMap = (Button)findViewById(R.id.btnConferenceMap);
        btnIndoorMap = (Button)findViewById(R.id.btnIndoorMap);

        btnConferenceMap.setOnClickListener(new MapBtnListener());
        btnIndoorMap.setOnClickListener(new MapBtnListener());


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
            }
        }
    }
}