package com.infjay.mice;

/**
 * Created by Administrator on 2015-05-02.
 */

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.Toast;

import java.util.ArrayList;


public class MainActivity extends ActionBarActivity {
    String[] permission;
    Intent intent;
    ImageButton adBtn;
    ArrayList<ImageButton> btnList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnList = new ArrayList<ImageButton>();

        permission = new String[9];
        for(int i=0;i<9;i++){
            permission[i] = "y";
        }
        //permission[5] = "n";
        //버튼 세팅
        setBtn();
    }

    public void setBtn(){
        this.adBtn = (ImageButton)this.findViewById(R.id.adBtn);

        for(int i=0;i<permission.length;i++){
            intent = new Intent();
            Button tmpBtn;

            switch (i) {
                case 0:
                    tmpBtn = (Button)findViewById(R.id.agendaBtn);
                    tmpBtn.setOnClickListener(new BtnClickListenner());
                    break;
                case 1:
                    tmpBtn = (Button)findViewById(R.id.briefBtn);
                    tmpBtn.setOnClickListener(new BtnClickListenner());
                    break;
                case 2:
                    tmpBtn = (Button)findViewById(R.id.binderBtn);
                    tmpBtn.setOnClickListener(new BtnClickListenner());
                    break;
                case 3:
                    tmpBtn = (Button)findViewById(R.id.couponBtn);
                    tmpBtn.setOnClickListener(new BtnClickListenner());
                    break;
                case 4:
                    tmpBtn = (Button)findViewById(R.id.msgBtn);
                    tmpBtn.setOnClickListener(new BtnClickListenner());
                    break;
                case 5:
                    tmpBtn = (Button)findViewById(R.id.mapBtn);
                    tmpBtn.setOnClickListener(new BtnClickListenner());
                    break;
                case 6:
                    tmpBtn = (Button)findViewById(R.id.surveyBtn);
                    tmpBtn.setOnClickListener(new BtnClickListenner());
                    break;
                case 7:
                    tmpBtn = (Button)findViewById(R.id.searchBtn);
                    tmpBtn.setOnClickListener(new BtnClickListenner());
                    break;
                case 8:
                    tmpBtn = (Button)findViewById(R.id.settingBtn);
                    tmpBtn.setOnClickListener(new BtnClickListenner());
                    break;
            }
        }
    }


    class BtnClickListenner implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            intent = new Intent();
            switch (v.getId()){
                case R.id.agendaBtn :
                    if(permission[0].equals("y")){
                        intent = new Intent(getApplicationContext(),
                                AgendaActivity.class);
                        startActivity(intent);
                    }
                    else{
                        Toast.makeText(getApplicationContext(), "권한이 없습니다.", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case R.id.briefBtn :
                    if(permission[1].equals("y")){
                        intent = new Intent(getApplicationContext(),
                                BriefCaseActivity.class);
                        startActivity(intent);
                    }
                    else{
                        Toast.makeText(getApplicationContext(), "권한이 없습니다.", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case R.id.binderBtn :
                    if(permission[2].equals("y")){
                        intent = new Intent(getApplicationContext(),
                                BinderActivity.class);
                        startActivity(intent);
                    }
                    else{
                        Toast.makeText(getApplicationContext(), "권한이 없습니다.", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case R.id.couponBtn :
                    if(permission[3].equals("y")){
                        intent = new Intent(getApplicationContext(),
                                CouponActivity.class);
                        startActivity(intent);
                    }
                    else{
                        Toast.makeText(getApplicationContext(), "권한이 없습니다.", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case R.id.msgBtn :
                    if(permission[4].equals("y")){
                        intent = new Intent(getApplicationContext(),
                                MessengerActivity.class);
                        startActivity(intent);
                    }
                    else{
                        Toast.makeText(getApplicationContext(), "권한이 없습니다.", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case R.id.mapBtn :
                    if(permission[5].equals("y")){
                        intent = new Intent(getApplicationContext(),
                                MapActivity.class);
                        startActivity(intent);
                    }
                    else{
                        Toast.makeText(getApplicationContext(), "권한이 없습니다.", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case R.id.surveyBtn :
                    if(permission[6].equals("y")){
                        intent = new Intent(getApplicationContext(),
                                SurveyActivity.class);
                        startActivity(intent);
                    }
                    else{
                        Toast.makeText(getApplicationContext(), "권한이 없습니다.", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case R.id.searchBtn :
                    if(permission[7].equals("y")){
                        intent = new Intent(getApplicationContext(),
                                SearchActivity.class);
                        startActivity(intent);
                    }
                    else{

                    }
                    break;
                case R.id.settingBtn :
                    if(permission[8].equals("y")){
                        intent = new Intent(getApplicationContext(),
                                SettingActivity.class);
                        startActivity(intent);
                    }
                    else{
                        Toast.makeText(getApplicationContext(), "권한이 없습니다.", Toast.LENGTH_SHORT).show();
                    }
                    break;
                default ://image버튼
                    break;
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