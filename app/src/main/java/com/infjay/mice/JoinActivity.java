package com.infjay.mice;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import com.infjay.mice.global.GlobalVariable;
import com.infjay.mice.network.AsyncHttpsTask;

import org.json.JSONException;
import org.json.JSONObject;


public class JoinActivity extends CustomActionBarActivity implements View.OnClickListener{

    EditText etEmail,etPasswd,etRePasswd,etJoinName;
    Button btCheck,btJoinComp;

    private String email,passwd,rePasswd,name;
    private boolean idCheck = false;

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
                            Toast.makeText(getApplicationContext(), "EMAIL_ID_DUPLICATE_ERROR", Toast.LENGTH_SHORT).show();
                        }

                        else if(jobj.get("result").equals("EMAIL_ID_DUPLICATE")){
                            Toast.makeText(getApplicationContext(), "ID Already Exists!", Toast.LENGTH_SHORT).show();
                        }

                        else if(jobj.get("result").equals("EMAIL_ID_NO_DUPLICATE")){
                            Toast.makeText(getApplicationContext(), "Available ID", Toast.LENGTH_SHORT).show();
                            idCheck = true;
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
                            finish();
                            Toast.makeText(getApplicationContext(), "Success in Register", Toast.LENGTH_SHORT).show();
                        }

                        else if(jobj.get("result").equals("EMAIL_SIGN_UP_FAIL")){
                            Toast.makeText(getApplicationContext(), "Fail in Register", Toast.LENGTH_SHORT).show();
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
        setContentView(R.layout.activity_join);

        etEmail = (EditText)findViewById(R.id.etJoinEmail);
        etPasswd = (EditText)findViewById(R.id.etJoinPassword);
        etRePasswd = (EditText)findViewById(R.id.etCheckPassword);
        etJoinName = (EditText)findViewById(R.id.etJoinName);

        btCheck = (Button)findViewById(R.id.btCheckEmail);
        btJoinComp = (Button)findViewById(R.id.btJoinComp);

        setButton();
    }

    public void setEditText(){

    }
    /*
    public void setSpinner(){
        String[] genderList = {"Gender","Male","FeMale"};
        String[] nationList = {"Nation","America","Korea","China","Japan"};

        spGender.setAdapter(new SearchSpinnerArrayAdapter(getApplicationContext(), android.R.layout.simple_spinner_item, genderList));
        spNation.setAdapter(new SearchSpinnerArrayAdapter(getApplicationContext(),android.R.layout.simple_spinner_item,nationList));

        spGender.setOnItemSelectedListener(new GenderAdapterListener());
        spNation.setOnItemSelectedListener(new NationAdapterListener());
    }*/

    public void setButton(){
        btCheck.setOnClickListener(this);
        btJoinComp.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btCheckEmail){
            //Email 중복확인
            JSONObject jobj = new JSONObject();
            email = etEmail.getText()+"";
            try {
                jobj.put("messagetype", "email_id_duplicate_check");
                jobj.put("user_id", email);
            }
            catch(JSONException e){
                e.printStackTrace();
            }

            new AsyncHttpsTask(getApplicationContext(), GlobalVariable.WEB_SERVER_IP, mHandler, jobj, 1, 0);

        }
        else if(v.getId() == R.id.btJoinComp){
            email = etEmail.getText().toString();
            passwd = etPasswd.getText().toString();
            rePasswd = etRePasswd.getText().toString();
            name = etJoinName.getText().toString();

            if(!passwd.equals(rePasswd)){
                //비밀번호랑 확인이 다를때
                Toast.makeText(getApplicationContext(), "Password is not correspond", Toast.LENGTH_SHORT).show();
                return;
            }
            if(!idCheck) {
                Toast.makeText(getApplicationContext(), "Please check your ID", Toast.LENGTH_SHORT).show();
                return;
            }
            if(email.length()==0||passwd.length()==0){
                Toast.makeText(getApplicationContext(), "Fill in All Data!!", Toast.LENGTH_SHORT).show();
            }
            JSONObject jobj = new JSONObject();

            try {
                jobj.put("messagetype", "email_sign_up");
                jobj.put("id_flag", "e");
                jobj.put("user_id", email);
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
    }

}
