package com.infjay.mice;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.infjay.mice.artifacts.ConferenceInfo;
import com.infjay.mice.database.DBManager;
import com.infjay.mice.global.GlobalVariable;
import com.infjay.mice.network.AsyncHttpsTask;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2015-05-02.
 */

public class AgendaActivity extends ActionBarActivity {
    Button confBtn,sessionBtn,sponBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agenda);

        confBtn = (Button)findViewById(R.id.conInfoBtn);
        sessionBtn = (Button)findViewById(R.id.sessionBtn);
        sponBtn = (Button)findViewById(R.id.sponBtn);

        confBtn.setOnClickListener(new AgendaBtnListenner());
        sessionBtn.setOnClickListener(new AgendaBtnListenner());
        sponBtn.setOnClickListener(new AgendaBtnListenner());

    }

    class AgendaBtnListenner implements View.OnClickListener{
        Intent intent;
        @Override
        public void onClick(View v) {
            switch(v.getId()){
                case R.id.conInfoBtn :
                    intent = new Intent(getApplicationContext(), ConfInfoActivity.class);
                    startActivity(intent);
                    break;
                case R.id.sessionBtn :
                    intent = new Intent(getApplicationContext(), SessionActivity.class);
                    startActivity(intent);
                    break;
                case R.id.sponBtn :
                    intent = new Intent(getApplicationContext(), SponsorActivity.class);
                    startActivity(intent);
                    break;
            }
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_basic, menu);
        return true;
    }

}