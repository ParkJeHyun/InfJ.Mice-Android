package com.infjay.mice;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.infjay.mice.database.DBManager;
/*
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
*/

public class LoginActivity extends ActionBarActivity{
    //LoginButton facebookLoginBtn;
    Button facebookLoginBtn;
    Intent intent;

    //Email
    Button EmailLoginBtn;

    public static Activity loginActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginActivity = this;
    }
    @Override
    public void onResume(){
        super.onResume();
        int sessionCount = DBManager.getManager(getApplicationContext()).getUserInfoCount();

        //세션에 레코드가 있으면 메인액티비티로 바로 가
        if(sessionCount != 0){
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
        }

        facebookLoginBtn = (Button)findViewById(R.id.btFacebookLogin);
        EmailLoginBtn = (Button)findViewById(R.id.btEmailLogin);

        facebookLoginBtn.setOnClickListener(new LoginButtonListener());
        EmailLoginBtn.setOnClickListener(new LoginButtonListener());
    }

    class LoginButtonListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            if(v.getId() == R.id.btEmailLogin){
                intent = new Intent(getApplicationContext(),EmailLoginActivity.class);
                startActivity(intent);
                //finish();
            }
            else if(v.getId() == R.id.btFacebookLogin){
                intent = new Intent(getApplicationContext(),FacebookLoginActivity.class);
                startActivity(intent);

            }
        }
    }

}
