package com.infjay.mice;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.infjay.mice.artifacts.UserInfo;
import com.infjay.mice.database.DBManager;
import com.infjay.mice.global.GlobalVariable;
import com.infjay.mice.network.AsyncHttpsTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.IOException;


public class MyBusinessCardModifyActivity extends CustomActionBarActivity {
    private UserInfo modifyInfo;
    private UserInfo myInfo;

    private EditText etMyName;
    private EditText etMyCompany;
    private EditText etMyPosition;
    private EditText etMyPhone;
    private EditText etMyTel;
    private EditText etMyEmail;
    private EditText etMyAddress;
    private EditText etMyNation;

    private Button btMyPhotoUpload;

    private final int REQ_CODE_SELECT_IMAGE = 100;

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

                    if(jobj.get("messagetype").equals("update_business_card")){
                        if(jobj.get("result").equals("UPDATE_BUSINESS_CARD_ERROR")){
                            Toast.makeText(getApplicationContext(), "UPDATE_BUSINESS_CARD_ERROR", Toast.LENGTH_SHORT).show();
                        }

                        else if(jobj.get("result").equals("UPDATE_BUSINESS_CARD_SUCCESS")){
                            //Modify businessCard success
                            //Modify sessionUser Information By ReinsertUserInfo

                            DBManager.getManager(getApplicationContext()).deleteUserInfo();
                            DBManager.getManager(getApplicationContext()).insertUserInfo(modifyInfo);

                            Toast.makeText(getApplicationContext(), "UPDATE_BUSINESS_CARD_SUCCESS", Toast.LENGTH_SHORT).show();
                            finish();
                        }

                        else if(jobj.get("result").equals("UPDATE_BUSINESS_CARD_FAIL")){
                            Toast.makeText(getApplicationContext(), "UPDATE_BUSINESS_CARD_FAIL", Toast.LENGTH_SHORT).show();
                        }

                        else{
                            Toast.makeText(getApplicationContext(), "result wrong", Toast.LENGTH_SHORT).show();
                        }
                    }

                    else {
                        Toast.makeText(getApplicationContext(), "messagetype wrong not add_coupon", Toast.LENGTH_SHORT).show();
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
        setContentView(R.layout.activity_my_business_card_modify);

        myInfo = DBManager.getManager(getApplicationContext()).getUserInfo();

        etMyName = (EditText)findViewById(R.id.etMyName);
        etMyCompany = (EditText)findViewById(R.id.etMyCompany);
        etMyPosition = (EditText)findViewById(R.id.etMyPosition);
        etMyPhone = (EditText)findViewById(R.id.etMyPhone);
        etMyTel = (EditText)findViewById(R.id.etMyTel);
        etMyEmail = (EditText)findViewById(R.id.etMyEmail);
        etMyAddress = (EditText)findViewById(R.id.etMyAddress);
        etMyNation = (EditText)findViewById(R.id.etMyNation);

        setEditText();
/*
        btMyPhotoUpload = (Button)findViewById(R.id.btMyPhotoUpload);

        btMyPhotoUpload.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType(android.provider.MediaStore.Images.Media.CONTENT_TYPE);
                intent.setData(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, REQ_CODE_SELECT_IMAGE);
            }
        });*/
    }

    private void setEditText(){
        if(myInfo.name.equals("null")){
            etMyName.setHint("");
        }
        else {
            etMyName.setHint(myInfo.name);
        }

        if(myInfo.company.equals("null")){
            etMyCompany.setHint("");
        }
        else {
            etMyCompany.setHint(myInfo.company);
        }

        if(myInfo.duty.equals("null")){
            etMyPosition.setHint("");
        }
        else {
            etMyPosition.setHint(myInfo.duty);
        }

        if(myInfo.phone.equals("null")){
            etMyPhone.setHint("");
        }
        else{
            etMyPhone.setHint(myInfo.phone);
        }

        if(myInfo.phone_1.equals("null")){
            etMyTel.setHint("");
        }
        else{
            etMyTel.setHint(myInfo.phone_1);
        }

        if(myInfo.email.equals("null")){
            etMyEmail.setHint(myInfo.userId);
        }
        else {
            etMyEmail.setHint(myInfo.email);
        }

        if(myInfo.address.equals("null")){
            etMyAddress.setHint("");
        }
        else {
            etMyAddress.setHint(myInfo.address);
        }

        if(myInfo.nationCode.equals("null")){
            etMyNation.setHint("");
        }
        else {
            etMyNation.setHint(myInfo.nationCode);
        }
    }

    private void makeModifyInfo(){
        modifyInfo = new UserInfo();
        modifyInfo = myInfo;

        if(etMyName.getText().length()!=0){
            modifyInfo.name = etMyName.getText().toString();
        }

        if(etMyCompany.getText().length()!=0){
            modifyInfo.company = etMyCompany.getText().toString();
        }

        if(etMyPosition.getText().length()!=0){
            modifyInfo.duty = etMyPosition.getText().toString();
        }

        if(etMyPhone.getText().length()!=0){
            modifyInfo.phone = etMyPhone.getText().toString();
        }

        if(etMyTel.getText().length()!=0){
            modifyInfo.phone_1 = etMyTel.getText().toString();
        }

        if(etMyEmail.getText().length()!=0){
            modifyInfo.email = etMyEmail.getText().toString();
        }

        if(etMyAddress.getText().length()!=0){
            modifyInfo.address = etMyAddress.getText().toString();
        }

        if(etMyNation.getText().length()!=0){
            modifyInfo.nationCode = etMyNation.getText().toString();
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_my_businesscard_modify, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.itCancel) {
            finish();
            return true;
        }
        else if (id == R.id.itModify) {
            //set DB
            makeModifyInfo();
            /*
            DBManager.getManager(getApplicationContext()).deleteUserInfo();
            DBManager.getManager(getApplicationContext()).insertUserInfo(modifyInfo);
            */
            JSONObject jobj = new JSONObject();

            try {
                jobj.put("messagetype", "update_business_card");
                //jobj.put("title", selectTitle);
                jobj.put("user_seq", modifyInfo.userSeq);
                jobj.put("name",modifyInfo.name);
                jobj.put("company",modifyInfo.company);
                //jobj.put("picture",modifyInfo.picture);
                jobj.put("phone",modifyInfo.phone);
                jobj.put("email",modifyInfo.email);
                jobj.put("address",modifyInfo.address);
                jobj.put("phone_1",modifyInfo.phone_1 );
                jobj.put("nation_code",modifyInfo.nationCode);
                jobj.put("duty",modifyInfo.duty );

            }
            catch(JSONException e){
                e.printStackTrace();
            }

            new AsyncHttpsTask(getApplicationContext(), GlobalVariable.WEB_SERVER_IP, mHandler, jobj, 1, 0);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
