package com.infjay.mice;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.IOException;


public class MyBusinessCardModifyActivity extends ActionBarActivity {

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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_business_card_modify);
        setTitle("");
        etMyName = (EditText)findViewById(R.id.etMyName);
        etMyCompany = (EditText)findViewById(R.id.etMyCompany);
        etMyPosition = (EditText)findViewById(R.id.etMyPosition);
        etMyPhone = (EditText)findViewById(R.id.etMyPhone);
        etMyTel = (EditText)findViewById(R.id.etMyTel);
        etMyEmail = (EditText)findViewById(R.id.etMyEmail);
        etMyAddress = (EditText)findViewById(R.id.etMyAddress);
        etMyNation = (EditText)findViewById(R.id.etMyNation);
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

            /*
            JSONObject userInfo = new JSONObject();
            userInfo.put("", etMyName);
            userInfo.put("", etMyCompany);
            userInfo.put("", etMyPosition);
            userInfo.put("", etMyPhone);
            userInfo.put("", etMyTel);
            userInfo.put("", etMyEmail);
            userInfo.put("", etMyAddress);
            userInfo.put("", etMyNation);
            */
            //send to server
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
