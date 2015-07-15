package com.infjay.mice;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.Build;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.infjay.mice.artifacts.UserInfo;
import com.infjay.mice.database.DBManager;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;


public class MyBusinessCardActivity extends CustomActionBarActivity {

    private Button btShareBusinessCard;
    private Button btModifyBusinessCard;
    private boolean shareFlag = false;
    private ImageView ivMyPhoto;
    private UserInfo myInfo;
    private TextView myName,myCompany,myPosition,myPhone,myTel,myEmail,myAddress;

    private final int REQ_CODE_SELECT_IMAGE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_business_card);

        myInfo = DBManager.getManager(getApplicationContext()).getUserInfo();

        myName = (TextView)findViewById(R.id.tvMyName);
        myCompany = (TextView)findViewById(R.id.tvMyCompany);
        myPosition = (TextView)findViewById(R.id.tvMyPosition);
        myPhone = (TextView)findViewById(R.id.tvMyPhonenumber);
        myTel = (TextView)findViewById(R.id.tvMyTel);
        myEmail = (TextView)findViewById(R.id.tvMyEmail);
        myAddress = (TextView)findViewById(R.id.tvMyAddress);

        btModifyBusinessCard = (Button)findViewById(R.id.btModifyBusinessCard);
        btModifyBusinessCard.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent  = new Intent(getApplicationContext(), MyBusinessCardModifyActivity.class);
                startActivity(intent);
            }
        });

        btShareBusinessCard = (Button)findViewById(R.id.btShareBusinessCard);
        btShareBusinessCard.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View view) {
                shareFlag = !shareFlag;
                if(shareFlag == true)
                {
                    btShareBusinessCard.setText("UnShare");
                }
                else
                {
                    btShareBusinessCard.setText("Share");
                }
            }
        });

        ivMyPhoto = (ImageView)findViewById(R.id.ivMyPhoto);
        ivMyPhoto.setOnClickListener(new ImageView.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType(android.provider.MediaStore.Images.Media.CONTENT_TYPE);
                intent.setData(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, REQ_CODE_SELECT_IMAGE);
            }
        });
        setImage();
        //shareFlag = false;
        //setImage();
        ///setText();
        //shareBtn = (Button)findViewById(R.id.shareBtn);
        //shareBtn.setOnClickListener(new myBriefBtnListenner());
        //
        setTextView();
    }

    private void setImage() {

        byte[] resBytes = Base64.decode(myInfo.picture, Base64.DEFAULT);
        Bitmap imageBitmap = BitmapFactory.decodeByteArray(resBytes, 0, resBytes.length);
        ivMyPhoto.setImageBitmap(imageBitmap);
    }

    private void setTextView(){
        if(myInfo.name.equals("null")){
            myName.setText("");
        }
        else {
            myName.setText(myInfo.name);
        }

        if(myInfo.company.equals("null")){
            myCompany.setText("");
        }
        else {
            myCompany.setText(myInfo.company);
        }

        if(myInfo.duty.equals("null")){
            myPosition.setText("");
        }
        else {
            myPosition.setText(myInfo.duty);
        }

        if(myInfo.phone.equals("null")){
            myPhone.setText("");
        }
        else{
            myPhone.setText(myInfo.phone);
        }

        if(myInfo.phone_1.equals("null")){
            myTel.setText("");
        }
        else{
            myTel.setText(myInfo.phone_1);
        }

        if(myInfo.email.equals("null")){
            myEmail.setText("");
        }
        else {
            myEmail.setText(myInfo.email);
        }

        if(myInfo.address.equals("null")){
            myAddress.setText("");
        }
        else {
            myAddress.setText(myInfo.address);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQ_CODE_SELECT_IMAGE)
        {
            if(resultCode== Activity.RESULT_OK)
            {
                try {
                    //Get Image name from Uri
                    //String name_Str = getImageNameToUri(data.getData());

                    //Get Bitmap from Image file
                    Bitmap image_bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());
                    ImageView image = (ImageView)findViewById(R.id.ivMyPhoto);

                    //set to image view
                    image.setImageBitmap(image_bitmap);

                    //set Image size to fit in
                    image.setScaleType(ImageView.ScaleType.FIT_XY);

                    //Toast.makeText(getBaseContext(), "name_Str : "+name_Str , Toast.LENGTH_SHORT).show();

                } catch (FileNotFoundException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }
    }
/*
    public String getImageNameToUri(Uri data)
    {
        String[] proj = { MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(data, proj, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

        cursor.moveToFirst();

        String imgPath = cursor.getString(column_index);
        String imgName = imgPath.substring(imgPath.lastIndexOf("/")+1);

        return imgName;
    }*/
}
