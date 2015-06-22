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

public class SurveyActivity extends ActionBarActivity {
    Button doSurvBtn;
    Button makeSurvBtn;
    Button statBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey);
        doSurvBtn = (Button)findViewById(R.id.btnDoSurvey);
        makeSurvBtn = (Button)findViewById(R.id.btnMakeSurvey);
        statBtn = (Button)findViewById(R.id.btnSurveyStatistics);

        doSurvBtn.setOnClickListener(new SurveyBtnListener());
        makeSurvBtn.setOnClickListener(new SurveyBtnListener());
        statBtn.setOnClickListener(new SurveyBtnListener());


    }

    class SurveyBtnListener implements View.OnClickListener{
        Intent intent;
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.btnDoSurvey :
                    intent = new Intent(getApplicationContext(),
                            DoSurvActivity.class);
                    startActivity(intent);
                    break;
                case R.id.btnMakeSurvey :
                    intent = new Intent(getApplicationContext(),
                            MakeSurvActivity.class);
                    startActivity(intent);
                    break;
                case R.id.btnSurveyStatistics :
                    intent = new Intent(getApplicationContext(),
                            StatActivity.class);
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