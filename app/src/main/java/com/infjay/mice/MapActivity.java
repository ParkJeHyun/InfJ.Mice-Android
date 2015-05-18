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
import android.widget.Button;

public class MapActivity extends ActionBarActivity {
    Button confMapBtn;
    Button insideBtn;
    Button surroundBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        confMapBtn = (Button)findViewById(R.id.confMapBtn);
        insideBtn = (Button)findViewById(R.id.insideMapBtn);
        surroundBtn = (Button)findViewById(R.id.surroundBtn);

        confMapBtn.setOnClickListener(new MapBtnListener());
        insideBtn.setOnClickListener(new MapBtnListener());
        surroundBtn.setOnClickListener(new MapBtnListener());
    }

    class MapBtnListener implements View.OnClickListener{
        Intent intent;
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.confMapBtn :
                    intent = new Intent(getApplicationContext(),
                            ConfMapActivity.class);
                    startActivity(intent);
                    break;
                case R.id.insideMapBtn :
                    intent = new Intent(getApplicationContext(),
                            InsideMapActivity.class);
                    startActivity(intent);
                    break;
                case R.id.surroundBtn :
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