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


public class JoinActivity extends ActionBarActivity implements View.OnClickListener{

    EditText etEmail,etPasswd,etRePasswd;
    Button btCheck,btJoinComp;

    private String email,passwd,rePasswd;

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

                    if(jobj.get("messagetype").equals("email_check")){
                        if(jobj.get("result").equals("EMAIL_CHECK_ERROR")){
                            Toast.makeText(getApplicationContext(), "This Email Already Use.", Toast.LENGTH_SHORT).show();
                        }

                        else if(jobj.get("result").equals("EMAIL_CHECK_SUCCESS")){
                            Toast.makeText(getApplicationContext(), "This Email Can Usable.", Toast.LENGTH_SHORT).show();
                        }

                        else if(jobj.get("result").equals("EMAIL_CHECK_FAIL")){
                            Toast.makeText(getApplicationContext(), "EMAIL_CHECK_FAIL", Toast.LENGTH_SHORT).show();
                        }

                        else{
                            Toast.makeText(getApplicationContext(), "result wrong", Toast.LENGTH_SHORT).show();
                        }
                    }

                    else {
                        Toast.makeText(getApplicationContext(), "messagetype wrong not email_check", Toast.LENGTH_SHORT).show();
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

                    if(jobj.get("messagetype").equals("email_join")){
                        if(jobj.get("result").equals("EMAIL_JOIN_ERROR")){
                            Toast.makeText(getApplicationContext(), "EMAIL_JOIN_ERROR", Toast.LENGTH_SHORT).show();
                        }

                        else if(jobj.get("result").equals("EMAIL_JOIN_SUCCESS")){
                            Toast.makeText(getApplicationContext(), "EMAIL_JOIN_SUCCESS.", Toast.LENGTH_SHORT).show();
                        }

                        else if(jobj.get("result").equals("EMAIL_JOIN_FAIL")){
                            Toast.makeText(getApplicationContext(), "EMAIL_JOIN_FAIL", Toast.LENGTH_SHORT).show();
                        }

                        else{
                            Toast.makeText(getApplicationContext(), "result wrong", Toast.LENGTH_SHORT).show();
                        }
                    }

                    else {
                        Toast.makeText(getApplicationContext(), "messagetype wrong not email_join", Toast.LENGTH_SHORT).show();
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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_join, menu);
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

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btCheckEmail){
            //Email 중복확인
            JSONObject jobj = new JSONObject();

            try {
                jobj.put("messagetype", "email_check");
                jobj.put("id", email);
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

            if(!passwd.equals(rePasswd)){
                //비밀번호랑 확인이 다를때
                Toast.makeText(getApplicationContext(), "PassWord and RePassWord are Not Equal!!", Toast.LENGTH_SHORT).show();
                return;
            }
            if(email.length()==0||passwd.length()==0){
                Toast.makeText(getApplicationContext(), "Fill in All Data!!", Toast.LENGTH_SHORT).show();
            }
            JSONObject jobj = new JSONObject();

            try {
                jobj.put("messagetype", "email_join");
                jobj.put("id", email);
                jobj.put("password",passwd);
            }
            catch(JSONException e){
                e.printStackTrace();
            }

            new AsyncHttpsTask(getApplicationContext(), GlobalVariable.WEB_SERVER_IP, mHandler, jobj, 2, 0);

            Toast.makeText(getApplicationContext(), "Email :"+email+"|| PassWord : "+passwd, Toast.LENGTH_SHORT).show();
        }
    }

}
