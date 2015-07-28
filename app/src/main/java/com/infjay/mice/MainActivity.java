package com.infjay.mice;

/**
 * Created by Administrator on 2015-05-02.
 */

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.infjay.mice.artifacts.UserInfo;
import com.infjay.mice.database.DBHelper;
import com.infjay.mice.database.DBManager;

import java.util.ArrayList;


public class MainActivity extends ActionBarActivity {
    String[] permission;
    Intent intent;
    ImageButton adBtn;
    ArrayList<ImageButton> btnList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //getWindow().requestFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //Make ActionBar transparent
        /*
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#00000000")));
        actionBar.setStackedBackgroundDrawable(new ColorDrawable(Color.parseColor("#00000000")));
        */
        //유저 테이블 조회
        UserInfo userInfo = new UserInfo();
        userInfo = DBManager.getManager(getApplicationContext()).getUserInfo();

        if(userInfo.userId != null){
            Toast.makeText(getApplicationContext(), "welcome " + userInfo.userId, Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(getApplicationContext(), "no session ", Toast.LENGTH_SHORT).show();
        }
        //getActionBar().setIcon(R.drawable.ic_launcher);
        //getActionBar().setHomeButtonEnabled(true);

        //ActionBar bar = getActionBar();
        //bar.setHomeButtonEnabled(true);

        /*
        intent = new Intent();
        intent = new Intent(getApplicationContext(),
                LoginActivity.class);
        startActivity(intent);
        */

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
        this.adBtn = (ImageButton)this.findViewById(R.id.btnMainAd);

        for(int i=0;i<permission.length;i++){
            intent = new Intent();
            Button btn;

            switch (i) {
                case 0:
                    btn = (Button)findViewById(R.id.btnAgenda);
                    btn.setOnClickListener(new BtnClickListenner());
                    break;
                case 1:
                    btn = (Button)findViewById(R.id.btnMyBriefCase);
                    btn.setOnClickListener(new BtnClickListenner());
                    break;
                case 2:
                    btn = (Button)findViewById(R.id.btnBinder);
                    btn.setOnClickListener(new BtnClickListenner());
                    break;
                case 3:
                    btn = (Button)findViewById(R.id.btnCoupon);
                    btn.setOnClickListener(new BtnClickListenner());
                    break;
                case 4:
                    btn = (Button)findViewById(R.id.btnMessenger);
                    btn.setOnClickListener(new BtnClickListenner());
                    break;
                case 5:
                    btn = (Button)findViewById(R.id.btnMap);
                    btn.setOnClickListener(new BtnClickListenner());
                    break;
                case 6:
                    btn = (Button)findViewById(R.id.btnSurvey);
                    btn.setOnClickListener(new BtnClickListenner());
                    break;
                case 7:
                    btn = (Button)findViewById(R.id.btnSearch);
                    btn.setOnClickListener(new BtnClickListenner());
                    break;
                case 8:
                    btn = (Button)findViewById(R.id.btnSetting);
                    btn.setOnClickListener(new BtnClickListenner());
                    break;
            }
        }
    }


    class BtnClickListenner implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            intent = new Intent();
            switch (v.getId()){
                case R.id.btnAgenda :
                    if(permission[0].equals("y")){
                        intent = new Intent(getApplicationContext(),
                                AgendaActivity.class);
                        startActivity(intent);
                    }
                    else{
                        Toast.makeText(getApplicationContext(), "권한이 없습니다.", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case R.id.btnMyBriefCase :
                    if(permission[1].equals("y")){
                        intent = new Intent(getApplicationContext(),
                                MyBriefCaseActivity.class);
                        startActivity(intent);
                    }
                    else{
                        Toast.makeText(getApplicationContext(), "권한이 없습니다.", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case R.id.btnBinder :
                    if(permission[2].equals("y")){
                        intent = new Intent(getApplicationContext(),
                                BinderActivity.class);
                        startActivity(intent);
                    }
                    else{
                        Toast.makeText(getApplicationContext(), "권한이 없습니다.", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case R.id.btnCoupon:
                    if(permission[3].equals("y")){
                        intent = new Intent(getApplicationContext(),
                                CouponListActivity.class);
                        startActivity(intent);
                    }
                    else{
                        Toast.makeText(getApplicationContext(), "권한이 없습니다.", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case R.id.btnMessenger:
                    if(permission[4].equals("y")){
                        intent = new Intent(getApplicationContext(),
                                MessengerActivity.class);
                        startActivity(intent);
                    }
                    else{
                        Toast.makeText(getApplicationContext(), "권한이 없습니다.", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case R.id.btnMap :
                    if(permission[5].equals("y")){
                        intent = new Intent(getApplicationContext(),
                                MapActivity.class);
                        startActivity(intent);
                    }
                    else{
                        Toast.makeText(getApplicationContext(), "권한이 없습니다.", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case R.id.btnSurvey :
                    if(permission[6].equals("y")){
                        intent = new Intent(getApplicationContext(),
                                SurveyActivity.class);
                        startActivity(intent);
                    }
                    else{
                        Toast.makeText(getApplicationContext(), "권한이 없습니다.", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case R.id.btnSearch :
                    if(permission[7].equals("y")){
                        intent = new Intent(getApplicationContext(),
                                SearchActivity.class);
                        startActivity(intent);
                    }
                    else{

                    }
                    break;
                case R.id.btnSetting :
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
        //return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.itHelp) {

            return true;
        }

        if (id == R.id.itMyMenu) {

            return true;
        }
        if(id == R.id.itLogout)
        {
            DBManager.getManager(getApplicationContext()).deleteUserInfo();
            DBManager.getManager(getApplicationContext()).deleteAllCoupon();
            DBManager.getManager(getApplicationContext()).deleteAllCardHolder();
            DBManager.getManager(getApplicationContext()).deleteAllMessageInfo();
            DBManager.getManager(getApplicationContext()).deleteAllBinder();
            DBManager.getManager(getApplicationContext()).deleteAllSchedule();
            Intent intent = new Intent(getApplicationContext(), EmailLoginActivity.class);
            startActivity(intent);
            finish();
        }


        return super.onOptionsItemSelected(item);
    }
/*
    @Override
    protected void onResume() {
        super.onResume();

        // Logs 'install' and 'app activate' App Events.
        AppEventsLogger.activateApp(this);
    }
    @Override
    protected void onPause() {
        super.onPause();

        // Logs 'app deactivate' App Event.
        AppEventsLogger.deactivateApp(this);
    }*/
}