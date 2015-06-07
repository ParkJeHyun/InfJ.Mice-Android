package com.infjay.mice;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.facebook.widget.LoginButton;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.plus.PlusClient;


public class LoginActivity extends ActionBarActivity implements View.OnClickListener {
    //LoginButton facebookLoginBtn;
    Button facebookLoginBtn;
    Intent intent;

    //google
    Button googleLoginBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        facebookLoginBtn = (Button)findViewById(R.id.facebookLoginBtn);
        googleLoginBtn = (Button)findViewById(R.id.googleLoginBtn);

        googleLoginBtn.setOnClickListener(this);
        facebookLoginBtn.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.googleLoginBtn) {
            intent = new Intent(getApplicationContext(),
                    GoogleLoginActivity.class);
            startActivity(intent);
        }
        if (view.getId() == R.id.facebookLoginBtn){
            intent = new Intent(getApplicationContext(),
                    FacebookLoginActivity.class);
            startActivity(intent);
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
