package com.infjay.mice;

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


public class JoinActivity extends ActionBarActivity implements View.OnClickListener{

    EditText etEmail,etPasswd,etRePasswd,etName,etComapny;
    Spinner spGender,spNation;
    Button btCheck,btJoinComp;

    private String email,passwd,rePasswd,name,company,gender,nation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);

        etEmail = (EditText)findViewById(R.id.etJoinEmail);
        etPasswd = (EditText)findViewById(R.id.etJoinPassword);
        etRePasswd = (EditText)findViewById(R.id.etCheckPassword);
        etName = (EditText)findViewById(R.id.etJoinName);
        etComapny = (EditText)findViewById(R.id.etJoinCompany);

        spGender = (Spinner)findViewById(R.id.spGender);
        spNation = (Spinner)findViewById(R.id.spNation);

        btCheck = (Button)findViewById(R.id.btCheckEmail);
        btJoinComp = (Button)findViewById(R.id.btJoinComp);

        setSpinner();
        setButton();
    }

    public void setEditText(){

    }

    public void setSpinner(){
        String[] genderList = {"Gender","Male","FeMale"};
        String[] nationList = {"Nation","America","Korea","China","Japan"};

        spGender.setAdapter(new SearchSpinnerArrayAdapter(getApplicationContext(), android.R.layout.simple_spinner_item, genderList));
        spNation.setAdapter(new SearchSpinnerArrayAdapter(getApplicationContext(),android.R.layout.simple_spinner_item,nationList));

        spGender.setOnItemSelectedListener(new GenderAdapterListener());
        spNation.setOnItemSelectedListener(new NationAdapterListener());
    }

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
        }
        else if(v.getId() == R.id.btJoinComp){
            email = etEmail.getText().toString();
            passwd = etPasswd.getText().toString();
            rePasswd = etRePasswd.getText().toString();
            name = etName.getText().toString();
            company = etComapny.getText().toString();

            if(!passwd.equals(rePasswd)){
                //비밀번호랑 확인이 다를때
                Toast.makeText(getApplicationContext(), "PassWord and RePassWord are Not Equal!!", Toast.LENGTH_SHORT).show();
                return;
            }
            if(email.length()==0||passwd.length()==0||name.length()==0||company.length()==0||gender.length()==0||nation.length()==0){
                Toast.makeText(getApplicationContext(), "Fill in All Data!!", Toast.LENGTH_SHORT).show();
            }
            Toast.makeText(getApplicationContext(), "Email :"+email+"|| PassWord : "+passwd, Toast.LENGTH_SHORT).show();
        }
    }

    public class GenderAdapterListener implements AdapterView.OnItemSelectedListener{

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            gender = (String)spGender.getItemAtPosition(position);
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }

    public class NationAdapterListener implements AdapterView.OnItemSelectedListener{

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            nation = (String)spNation.getItemAtPosition(position);
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }
}
