package com.infjay.mice;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

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

                    intent = new Intent(getApplicationContext(),
                            SponsorActivity.class);

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