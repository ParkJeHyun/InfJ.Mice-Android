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

import com.infjay.mice.global.GlobalVariable;
import com.infjay.mice.network.AsyncHttpsTask;

import org.json.JSONException;
import org.json.JSONObject;


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
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(intent);
                            finish();
                            Toast.makeText(getApplicationContext(), "EMAIL_LOGIN_SUCCESS", Toast.LENGTH_SHORT).show();
                        }

                        else if(jobj.get("result").equals("EMAIL_LOGIN_FAIL")){
                            Toast.makeText(getApplicationContext(), "EMAIL_LOGIN_FAIL", Toast.LENGTH_SHORT).show();
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
                jobj.put("id", email);
                jobj.put("passwd", passwd);
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
