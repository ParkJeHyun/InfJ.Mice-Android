package com.infjay.mice;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

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
            }
            else if(v.getId() == R.id.btFacebookLogin){
                intent = new Intent(getApplicationContext(),FacebookLoginActivity.class);
                startActivity(intent);

            }
        }
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
