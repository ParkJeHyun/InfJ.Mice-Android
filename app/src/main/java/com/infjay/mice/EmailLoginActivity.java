package com.infjay.mice;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class EmailLoginActivity extends ActionBarActivity implements View.OnClickListener{

    private Button btLogin;
    private Button btJoin;
    private String email;
    private String passwd;
    private EditText etEmail;
    private EditText etPasswd;
    private Intent intent;

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
            passwd = etEmail.getText().toString();
            Toast.makeText(getApplicationContext(), email+passwd, Toast.LENGTH_SHORT).show();
            startActivity(intent);
        }
        else if (v.getId() == R.id.btEmailJoin){
            intent = new Intent(getApplicationContext(),
                    JoinActivity.class);
            startActivity(intent);
        }
    }
}
