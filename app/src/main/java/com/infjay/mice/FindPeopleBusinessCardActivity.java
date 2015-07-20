package com.infjay.mice;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
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

/**
 * Created by KimJS on 2015-07-20.
 */
public class FindPeopleBusinessCardActivity extends CustomActionBarActivity {

    private String userSeq;
    private UserInfo uInfo;
    private TextView tvName, tvCompany, tvDuty, tvPhone, tvTel, tvEmail, tvAddress;

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

                    if(jobj.get("messagetype").equals("get_user_info")){
                        if(jobj.get("result").equals("GET_USER_INFO_ERROR")){
                            Toast.makeText(getApplicationContext(), "GET_USER_INFO_ERROR", Toast.LENGTH_SHORT).show();
                        }

                        else if(jobj.get("result").equals("GET_USER_INFO_SUCCESS")){
                            //로그인 성공했을 때 세션테이블에 저장하고 메인으로 가자
                            //user info 받은거 세션 테이블로 집어넣기 ㄱㄱ
                            JSONObject user_jobj = new JSONObject(jobj.get("attach")+"");

                            uInfo.userSeq = user_jobj.get("user_seq")+"";
                            uInfo.idFlag = user_jobj.get("id_flag")+"";
                            uInfo.userId = user_jobj.get("user_id")+"";
                            //userInfo.password = user_jobj.get("password")+"";
                            uInfo.name = user_jobj.get("name")+"";
                            uInfo.company = user_jobj.get("company")+"";
                            uInfo.picture = user_jobj.get("picture")+"";
                            uInfo.phone = user_jobj.get("phone")+"";
                            uInfo.email = user_jobj.get("email")+"";
                            uInfo.address = user_jobj.get("address")+"";
                            uInfo.authorityKind = user_jobj.get("authority_kind")+"";
                            uInfo.phone_1 = user_jobj.get("phone_1")+"";
                            uInfo.phone_2 = user_jobj.get("phone_2")+"";
                            uInfo.cellPhone_1 = user_jobj.get("cell_phone_1")+"";
                            uInfo.cellPhone_2 = user_jobj.get("cell_phone_2")+"";
                            uInfo.businessCardCode = user_jobj.get("business_card_code")+"";
                            uInfo.businessCardShareFlag = user_jobj.get("business_card_share_flag")+"";
                            uInfo.nationCode = user_jobj.get("nation_code")+"";
                            uInfo.duty = user_jobj.get("duty")+"";

                            if(uInfo.businessCardShareFlag.equals("0"))
                            {
                                Toast.makeText(getApplicationContext(), "Business card is not shared", Toast.LENGTH_SHORT).show();
                            }
                            setTitle(uInfo.name);
                            setBusinessCard();
                        }

                        else if(jobj.get("result").equals("GET_USER_INFO_FAIL")){
                            Toast.makeText(getApplicationContext(), "GET_USER_INFO_FAIL", Toast.LENGTH_SHORT).show();
                        }

                        else{
                            Toast.makeText(getApplicationContext(), "result wrong", Toast.LENGTH_SHORT).show();
                        }
                    }

                    else {
                        Toast.makeText(getApplicationContext(), "messagetype wrong not get_user_info", Toast.LENGTH_SHORT).show();
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
        setContentView(R.layout.activity_business_card);

        tvName = (TextView)findViewById(R.id.tvCardName);
        tvCompany = (TextView)findViewById(R.id.tvCompany);
        tvDuty = (TextView)findViewById(R.id.tvTitle);
        tvPhone = (TextView)findViewById(R.id.tvPhonenumber);
        tvTel = (TextView)findViewById(R.id.tvTel);
        tvEmail = (TextView)findViewById(R.id.tvEmail);
        tvAddress = (TextView)findViewById(R.id.tvAddr);
        uInfo = new UserInfo();

        Intent intent = getIntent();
        userSeq = intent.getStringExtra("userSeq");

        JSONObject jobj = new JSONObject();

        String currentUserSeq = DBManager.getManager(getApplicationContext()).getUserInfo().userSeq;
        if(currentUserSeq == null)
        {
            return;
        }
        try {
            jobj.put("messagetype", "get_user_info");
            jobj.put("user_seq", userSeq);
            jobj.put("session_user_seq", currentUserSeq);
        }
        catch(JSONException e){
            e.printStackTrace();
        }

        new AsyncHttpsTask(getApplicationContext(), GlobalVariable.WEB_SERVER_IP, mHandler, jobj, 1, 0);

    }

    public void setBusinessCard(){
        String str = uInfo.name;
        if(!str.equals("null")) {
            tvName.setText(str);
        }

        str = uInfo.company;
        if(!str.equals("null")) {
            tvCompany.setText(str);
        }

        str = uInfo.duty;
        if(!str.equals("null")) {
            tvDuty.setText(str);
        }

        str = uInfo.phone;
        if(!str.equals("null")) {
            tvPhone.setText(str);
        }

        str = uInfo.phone_1;
        if(!str.equals("null")) {
            tvTel.setText(str);
        }

        str = uInfo.email;
        if(!str.equals("null")) {
            tvEmail.setText(str);
        }

        str = uInfo.address;
        if(!str.equals("null")) {
            tvAddress.setText(str);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_find_people_business_card, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.itSendMessage) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
