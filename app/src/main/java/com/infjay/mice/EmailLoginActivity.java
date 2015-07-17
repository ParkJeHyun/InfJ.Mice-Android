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


public class EmailLoginActivity extends ActionBarActivity implements View.OnClickListener{

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
                            JSONObject jobj2 = new JSONObject();

                            try {
                                jobj2.put("messagetype", "get_user_info");
                                jobj2.put("user_id", email);
                            }
                            catch(JSONException e){
                                e.printStackTrace();
                            }

                            new AsyncHttpsTask(getApplicationContext(), GlobalVariable.WEB_SERVER_IP, mHandler, jobj2, 2, 0);

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
                //response 받은거 파싱해서

            }

            if (msg.what == 2) {
                //핸들러 2번일 때
                try {
                    JSONObject jobj = new JSONObject(msg.obj + "");
                    String messageType = jobj.get("messagetype") + "";
                    String result = jobj.get("result") + "";
                    String attach = jobj.get("attach") + "";
                    if (jobj.get("messagetype").equals("get_user_info")) {
                        if(jobj.get("result").equals("GET_USER_INFO_ERROR")){
                            Toast.makeText(getApplicationContext(), "GET_USER_INFO_ERROR", Toast.LENGTH_SHORT).show();
                        }
                        else if(jobj.get("result").equals("GET_USER_INFO_SUCCESS")){
                            //Toast.makeText(getApplicationContext(), "GET_USER_INFO_SUCCESS", Toast.LENGTH_SHORT).show();

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

                            JSONObject jobjConfInfo = new JSONObject();
                            try
                            {
                                jobjConfInfo.put("messagetype", "get_conference_info");
                            }
                            catch(JSONException e)
                            {
                                e.printStackTrace();
                            }
                            new AsyncHttpsTask(getApplicationContext(), GlobalVariable.WEB_SERVER_IP, mHandler, jobjConfInfo, 3, 0);

                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                            startActivity(intent);
                            finish();
                            LoginActivity.loginActivity.finish();

                            Toast.makeText(getApplicationContext(), "로그인에 성공하셨습니다.", Toast.LENGTH_SHORT).show();
                        }
                        else if(jobj.get("result").equals("GET_USER_INFO_FAIL")){
                            Toast.makeText(getApplicationContext(), "GET_USER_INFO_FAIL", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Toast.makeText(getApplicationContext(), "result wrong", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else{
                        Toast.makeText(getApplicationContext(), "messagetype wrong not get user info", Toast.LENGTH_SHORT).show();
                    }
                }
                catch(JSONException e){
                    e.printStackTrace();
                }
            }

            if (msg.what == 3) {
                try
                {
                    JSONObject jobj = new JSONObject(msg.obj + "");
                    if(jobj.get("messagetype").equals("get_conference_info"))
                    {
                        if(jobj.get("result").equals("GET_CONFERENCE_INFO_ERROR"))
                        {
                            Toast.makeText(getApplicationContext(), "Error in getting conference info", Toast.LENGTH_SHORT).show();
                        }
                        else if(jobj.get("result").equals("GET_CONFERENCE_INFO_FAIL"))
                        {
                            Toast.makeText(getApplicationContext(), "Fail in getting congerence info", Toast.LENGTH_SHORT).show();
                        }
                        else if(jobj.get("result").equals("GET_CONFERENCE_INFO_SUCCESS"))
                        {
                            JSONObject conf_jobj = new JSONObject(jobj.get("attach")+"");
                            ConferenceInfo conferenceInfo = new ConferenceInfo();
                            String conference_start_date = conf_jobj.get("conference_start_date").toString().split("Z")[0];
                            String conference_end_date = conf_jobj.get("conference_end_date").toString().split("Z")[0];
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
                            Date d = new Date();
                            Date d2 = new Date();
                            try
                            {
                                d = sdf.parse(conference_start_date);
                                d2 = sdf.parse(conference_end_date);
                            }
                            catch(ParseException e)
                            {
                                e.printStackTrace();
                            }
                            long dd = d.getTime() + (1000 * 60 * 60 * 9);
                            long dd2 = d2.getTime() + (1000 * 60 * 60 * 9);
                            d = new Date(dd);
                            d2 = new Date(dd2);
                            conference_start_date = sdf.format(d);
                            conference_end_date = sdf.format(d2);

                            conferenceInfo.conferenceName = conf_jobj.get("conference_name").toString();
                            conferenceInfo.conferenceStartDate = conference_start_date.split("T")[0];
                            conferenceInfo.conferenceEndDate = conference_end_date.split("T")[0];
                            conferenceInfo.conferenceSummary = conf_jobj.get("summary").toString();

                            //Insert DB
                            int count = DBManager.getManager(getApplicationContext()).getCountConference();
                            if(count == 0)
                            {
                                DBManager.getManager(getApplicationContext()).insertConferenceInfo(conferenceInfo);
                            }
                            else
                            {
                                DBManager.getManager(getApplicationContext()).updateConferenceInfo(conferenceInfo);
                            }
                            //finish();
                            //startActivity(getIntent());
                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(), "Wrong Message result", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(), "Wrong MessageType", Toast.LENGTH_SHORT).show();
                    }

                }
                catch(JSONException e)
                {
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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_email_login, menu);
        return true;
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
