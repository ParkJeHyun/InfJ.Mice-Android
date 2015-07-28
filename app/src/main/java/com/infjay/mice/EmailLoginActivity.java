package com.infjay.mice;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.infjay.mice.artifacts.ConferenceInfo;
import com.infjay.mice.artifacts.UserInfo;
import com.infjay.mice.database.DBManager;
import com.infjay.mice.global.GlobalVariable;
import com.infjay.mice.network.AsyncHttpsTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class EmailLoginActivity extends CustomActionBarActivity implements View.OnClickListener{

    private Button btLogin;
    private Button btJoin;
    private String email;
    private String passwd;
    private EditText etEmail;
    private EditText etPasswd;
    private Intent intent;

    protected Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            // IF Sucessfull no timeout
            System.out.println("in handler");
            if (msg.what == -1) {
                //   BreakTimeout();
                //ConnectionError();
                System.out.println("handler error");
            }


            if (msg.what == 1) {
                //핸들러 1번일 때
                System.out.println("response : "+msg.obj);

                try {
                    JSONObject jobj = new JSONObject(msg.obj+"");

                    if(jobj.get("messagetype").equals("email_login")){
                        if(jobj.get("result").equals("EMAIL_LOGIN_ERROR")){
                            Toast.makeText(getApplicationContext(), "EMAIL_LOGIN_ERROR", Toast.LENGTH_SHORT).show();
                        }

                        else if(jobj.get("result").equals("EMAIL_LOGIN_SUCCESS")){
                            //로그인 성공했을 때 세션테이블에 저장하고 메인으로 가자
                            //user info 받은거 세션 테이블로 집어넣기 ㄱㄱ
                            JSONObject user_jobj = new JSONObject(jobj.get("attach")+"");

                            UserInfo userInfo = new UserInfo();
                            userInfo.userSeq = user_jobj.get("user_seq")+"";
                            userInfo.idFlag = user_jobj.get("id_flag")+"";
                            userInfo.userId = user_jobj.get("user_id")+"";
                            //userInfo.password = user_jobj.get("password")+"";
                            userInfo.name = user_jobj.get("name")+"";
                            userInfo.company = user_jobj.get("company")+"";
                            userInfo.picture = user_jobj.get("picture")+"";
                            userInfo.phone = user_jobj.get("phone")+"";
                            userInfo.email = user_jobj.get("email")+"";
                            userInfo.address = user_jobj.get("address")+"";
                            userInfo.authorityKind = user_jobj.get("authority_kind")+"";
                            userInfo.phone_1 = user_jobj.get("phone_1")+"";
                            userInfo.phone_2 = user_jobj.get("phone_2")+"";
                            userInfo.cellPhone_1 = user_jobj.get("cell_phone_1")+"";
                            userInfo.cellPhone_2 = user_jobj.get("cell_phone_2")+"";
                            userInfo.businessCardCode = user_jobj.get("business_card_code")+"";
                            userInfo.businessCardShareFlag = user_jobj.get("business_card_share_flag")+"";
                            userInfo.nationCode = user_jobj.get("nation_code")+"";
                            userInfo.platform = user_jobj.get("platform")+"";
                            userInfo.regDate = ((user_jobj.get("reg_date")+"").replace('Z', ' ')).replace('T', ' ');
                            userInfo.modDate =((user_jobj.get("mod_date")+"").replace('Z', ' ')).replace('T', ' ');
                            userInfo.duty = user_jobj.get("duty")+"";

                            DBManager.getManager(getApplicationContext()).insertUserInfo(userInfo);
                            System.out.println("세션 저장 성공");

                            Toast.makeText(getApplicationContext(), "로그인에 성공하셨습니다.", Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(getApplicationContext(), LoadingActivity.class);
                            startActivity(intent);
                            finish();
                        }

                        else if(jobj.get("result").equals("EMAIL_LOGIN_FAIL")){
                            Toast.makeText(getApplicationContext(), "로그인에 실패하셨습니다.", Toast.LENGTH_SHORT).show();
                        }

                        else{
                            Toast.makeText(getApplicationContext(), "result wrong", Toast.LENGTH_SHORT).show();
                        }
                    }

                    else {
                        Toast.makeText(getApplicationContext(), "messagetype wrong not email_login", Toast.LENGTH_SHORT).show();
                    }
                }
                catch(JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_login);

        btLogin = (Button)findViewById(R.id.btEmailLoginComp);
        btJoin = (Button)findViewById(R.id.btEmailJoin);
        etEmail = (EditText)findViewById(R.id.etEmail);
        etPasswd = (EditText)findViewById(R.id.etPassword);

        btLogin.setOnClickListener(this);
        btJoin.setOnClickListener(this);
/*
        if(GlobalVariable.login){
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
        }*/
    }

    @Override
    protected void onResume() {
        super.onResume();
        int sessionCount = DBManager.getManager(getApplicationContext()).getUserInfoCount();
        //세션에 레코드가 있으면 메인액티비티로 바로 가
        if(sessionCount != 0){
            Intent intent = new Intent(getApplicationContext(), LoadingActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btEmailLoginComp) {
            intent = new Intent(getApplicationContext(),
                    MainActivity.class);
            email = etEmail.getText().toString();
            passwd = etPasswd.getText().toString();

            if(email.length()==0||passwd.length()==0){
                Toast.makeText(getApplicationContext(), "Fill in Data!!", Toast.LENGTH_SHORT).show();
            }
            //AsyncTask Sample Code START

            JSONObject jobj = new JSONObject();

            try {
                jobj.put("messagetype", "email_login");
                jobj.put("user_id", email);
                jobj.put("password", passwd);
            }
            catch(JSONException e){
                e.printStackTrace();
            }

            new AsyncHttpsTask(getApplicationContext(), GlobalVariable.WEB_SERVER_IP, mHandler, jobj, 1, 0);

            //AsyncTask Sample Code END
            //startActivity(intent);
        }
        else if (v.getId() == R.id.btEmailJoin){
            intent = new Intent(getApplicationContext(),
                    JoinActivity.class);
            startActivity(intent);
        }
    }
}
