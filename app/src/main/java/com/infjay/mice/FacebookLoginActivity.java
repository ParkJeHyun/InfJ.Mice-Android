package com.infjay.mice;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.HttpMethod;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.model.GraphUser;
import com.infjay.mice.artifacts.ConferenceInfo;
import com.infjay.mice.artifacts.UserInfo;
import com.infjay.mice.database.DBManager;
import com.infjay.mice.global.GlobalVariable;
import com.infjay.mice.network.AsyncHttpsTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;


public class FacebookLoginActivity extends CustomActionBarActivity {
    Intent intent;
    Session.StatusCallback statusCallback = new SessionStatusCallback();
    String email;
    String passwd;
    String name;

    protected Handler mHandlerLogin = new Handler() {
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

                            JSONObject jobjConfInfo = new JSONObject();
                            try
                            {
                                jobjConfInfo.put("messagetype", "get_conference_info");
                            }
                            catch(JSONException e)
                            {
                                e.printStackTrace();
                            }
                            new AsyncHttpsTask(getApplicationContext(), GlobalVariable.WEB_SERVER_IP, mHandler, jobjConfInfo, 2, 0);

                            Toast.makeText(getApplicationContext(), "로그인에 성공하셨습니다.", Toast.LENGTH_SHORT).show();

                            //new AsyncHttpsTask(getApplicationContext(), GlobalVariable.WEB_SERVER_IP, mHandler, jobj2, 2, 0);

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

                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(intent);
                            LoginActivity.loginActivity.finish();
                            finish();

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

                    if(jobj.get("messagetype").equals("email_id_duplicate_check")){
                        if(jobj.get("result").equals("EMAIL_ID_DUPLICATE_ERROR")){
                            Toast.makeText(getApplicationContext(), "중복체크 오류", Toast.LENGTH_SHORT).show();
                        }

                        else if(jobj.get("result").equals("EMAIL_ID_DUPLICATE")){
                            Toast.makeText(getApplicationContext(), "중복된 아이디가 있습니다.", Toast.LENGTH_SHORT).show();
                            //login 진행
                            makeSession();
                        }

                        else if(jobj.get("result").equals("EMAIL_ID_NO_DUPLICATE")){
                            Toast.makeText(getApplicationContext(), "사용 가능한 아이디 입니다.", Toast.LENGTH_SHORT).show();
                            //회원가입
                            emailJoin();
                        }

                        else{
                            Toast.makeText(getApplicationContext(), "result wrong", Toast.LENGTH_SHORT).show();
                        }
                    }

                    else {
                        Toast.makeText(getApplicationContext(), "messagetype wrong not dup check", Toast.LENGTH_SHORT).show();
                    }
                }
                catch(JSONException e) {
                    e.printStackTrace();
                }
                //response 받은거 파싱해서

            }

            if (msg.what == 2) {
                //핸들러 2번일 때
                System.out.println("response : "+msg.obj);

                try {
                    JSONObject jobj = new JSONObject(msg.obj+"");

                    if(jobj.get("messagetype").equals("email_sign_up")){
                        if(jobj.get("result").equals("EMAIL_SIGN_UP_SUCCESS")){
                            makeSession();

                        }
                        else if(jobj.get("result").equals("EMAIL_SIGN_UP_FAIL")){
                            Toast.makeText(getApplicationContext(), "회원가입에 실패했습니다.", Toast.LENGTH_SHORT).show();
                        }

                        else{
                            Toast.makeText(getApplicationContext(), "result wrong", Toast.LENGTH_SHORT).show();
                        }
                    }

                    else {
                        Toast.makeText(getApplicationContext(), "messagetype wrong not sign up", Toast.LENGTH_SHORT).show();
                    }
                }
                catch(JSONException e) {
                    e.printStackTrace();
                }
                //response 받은거 파싱해서

            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facebook_login);

        int sessionCount = DBManager.getManager(getApplicationContext()).getUserInfoCount();

        //세션에 레코드가 있으면 메인액티비티로 바로 가
        if(sessionCount != 0){
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            LoginActivity.loginActivity.finish();
            finish();
        }

        checkFacebookSession(this);
    }

    public void checkFacebookSession(Context context){
        Session session = Session.getActiveSession();

        if(session == null){
            session = Session.openActiveSessionFromCache(context);
            if(session == null){
                Session.openActiveSession(this, true, statusCallback);
            }
            else{
                getFaceBookMe(session);
            }
        }
        else{
            System.out.println("세션 있음");
            getFaceBookMe(session);
        }
    }


    private class SessionStatusCallback implements Session.StatusCallback {
        List<String> permissions = new ArrayList<String>();

        @Override
        public void call(Session session, SessionState state, Exception exception) {
            boolean isContainPermit = true;
            permissions.add("email");
            if (session.isOpened()) {
                //session 이 열려있음
                for(int i=0;i<permissions.size();i++){
                    if(!session.getPermissions().contains(permissions.get(i))){
                        isContainPermit = false;
                        break;
                    }
                }
                if(!isContainPermit){
                    Session.NewPermissionsRequest newPermissionsRequest = new Session.NewPermissionsRequest(FacebookLoginActivity.this,permissions);
                    session.requestNewReadPermissions(newPermissionsRequest);
                }
                else{
                    getFaceBookMe(session);
                }
            }
            else {
                //session안열림
            }
        }
    }

    private void getFaceBookMe(Session session) {
        if (session.isOpened()) {
            Request.newMeRequest(session, new Request.GraphUserCallback() {
                @Override
                public void onCompleted(GraphUser me, Response response) {
                    try {
                        response.getError();
                        email = me.getProperty("email").toString();
                        passwd = me.getId();
                        name = me.getName();
                        checkEmail();
                        //System.out.println("Email :::: " + email);
                        // 정보가져오기 성공.
                        /*
                        intent = new Intent(getApplicationContext(),
                                MainActivity.class);
                        startActivity(intent);

                        Toast.makeText(getApplicationContext(), me.getName(), Toast.LENGTH_SHORT).show();*/
                        finish();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).executeAsync();

        } else {
        }
    }
    public void checkEmail(){
        JSONObject jobj = new JSONObject();

        try {
            jobj.put("messagetype", "email_id_duplicate_check");
            jobj.put("user_id", passwd);
        }
        catch(JSONException e){
            e.printStackTrace();
        }

        new AsyncHttpsTask(getApplicationContext(), GlobalVariable.WEB_SERVER_IP, mHandler, jobj, 1, 0);
    }

    public void emailJoin(){
        JSONObject jobj = new JSONObject();

        try {
            jobj.put("messagetype", "email_sign_up");
            jobj.put("id_flag", "f");
            jobj.put("user_id", passwd);
            jobj.put("password",passwd);
            jobj.put("name",name);
            jobj.put("authority_kind", "normal");
            jobj.put("business_card_share_flag", "0");
            jobj.put("platform", "android");
        }
        catch(JSONException e){
            e.printStackTrace();
        }

        new AsyncHttpsTask(getApplicationContext(), GlobalVariable.WEB_SERVER_IP, mHandler, jobj, 2, 0);
    }

    public void makeSession(){
        JSONObject jobj = new JSONObject();

        try {
            jobj.put("messagetype", "email_login");
            jobj.put("user_id", passwd);
            jobj.put("password", passwd);
        }
        catch(JSONException e){
            e.printStackTrace();
        }

        new AsyncHttpsTask(getApplicationContext(), GlobalVariable.WEB_SERVER_IP, mHandlerLogin, jobj, 1, 0);

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Session.getActiveSession().onActivityResult(this, requestCode, resultCode, data);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
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
