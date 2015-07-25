package com.infjay.mice;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.util.Base64;
import android.view.Display;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.infjay.mice.artifacts.UserInfo;
import com.infjay.mice.database.DBManager;
import com.infjay.mice.global.GlobalVariable;
import com.infjay.mice.network.AsyncHttpsTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class MyBusinessCardActivity extends CustomActionBarActivity {

    private Button btShareBusinessCard;
    private Button btModifyBusinessCard;
    private boolean isShared = false;
    private ImageView ivMyPhoto;
    private UserInfo myInfo;
    private TextView myName,myCompany,myPosition,myPhone,myTel,myEmail,myAddress;

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
                //�ڵ鷯 1���� ��
                System.out.println("response : "+msg.obj);

                try {
                    JSONObject jobj = new JSONObject(msg.obj+"");

                    if(jobj.get("messagetype").equals("update_user_picture")){
                        if(jobj.get("result").equals("UPDATE_USER_PICTURE_FAIL")){
                            Toast.makeText(getApplicationContext(), "UPDATE_USER_PICTURE_FAIL", Toast.LENGTH_SHORT).show();
                        }

                        else if(jobj.get("result").equals("UPDATE_USER_PICTURE_SUCCESS")){

                            DBManager.getManager(getApplicationContext()).deleteUserInfo();
                            DBManager.getManager(getApplicationContext()).insertUserInfo(myInfo);

                            Toast.makeText(getApplicationContext(), "UPDATE_USER_PICTURE_SUCCESS", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                        else{
                            Toast.makeText(getApplicationContext(), "result wrong", Toast.LENGTH_SHORT).show();
                        }
                    }

                    else {
                        Toast.makeText(getApplicationContext(), "messagetype wrong not update_user_picture", Toast.LENGTH_SHORT).show();
                    }
                }
                catch(JSONException e) {
                    e.printStackTrace();
                }
            }

            // Share Business Code
            if (msg.what == 2) {
                try {
                    JSONObject jobj = new JSONObject(msg.obj+"");

                    if(jobj.get("messagetype").equals("update_business_card_share_flag")) {
                        if(jobj.get("result").equals("UPDATE_BUSINESS_CARD_SHARE_FLAG_FAIL")){
                            Toast.makeText(getApplicationContext(), "UPDATE_BUSINESS_CARD_SHARE_FLAG_FAIL", Toast.LENGTH_SHORT).show();
                        }

                        else if(jobj.get("result").equals("UPDATE_BUSINESS_CARD_SHARE_FLAG_SUCCESS_ON")){
                            JSONObject jobjCode = new JSONObject(jobj.get("attach")+"");
                            String code = jobjCode.get("business_card_code").toString();
                            showConfirmDialog(code);
                            Toast.makeText(getApplicationContext(), "Share Code Success", Toast.LENGTH_SHORT).show();
                            myInfo.businessCardShareFlag = "1";
                            myInfo.businessCardCode = code;
                            DBManager.getManager(getApplicationContext()).deleteUserInfo();
                            DBManager.getManager(getApplicationContext()).insertUserInfo(myInfo);
                        }
                        else{
                            Toast.makeText(getApplicationContext(), "result wrong", Toast.LENGTH_SHORT).show();
                        }
                    }

                    else {
                        Toast.makeText(getApplicationContext(), "message type wrong", Toast.LENGTH_SHORT).show();
                    }
                }
                catch(JSONException e) {
                    e.printStackTrace();
                }
            }

            //Unshare code
            if(msg.what == 3)
            {
                try {
                    JSONObject jobj = new JSONObject(msg.obj+"");

                    if(jobj.get("messagetype").equals("update_business_card_share_flag")) {
                        if(jobj.get("result").equals("UPDATE_BUSINESS_CARD_SHARE_FLAG_FAIL")){
                            Toast.makeText(getApplicationContext(), "UPDATE_BUSINESS_CARD_SHARE_FLAG_FAIL", Toast.LENGTH_SHORT).show();
                        }

                        else if(jobj.get("result").equals("UPDATE_BUSINESS_CARD_SHARE_FLAG_SUCCESS_OFF")){
                            myInfo.businessCardShareFlag = "0";
                            myInfo.businessCardCode = "";
                            DBManager.getManager(getApplicationContext()).deleteUserInfo();
                            DBManager.getManager(getApplicationContext()).insertUserInfo(myInfo);
                            showConfirmDialog("");
                            Toast.makeText(getApplicationContext(), "Unshare Code Success", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Toast.makeText(getApplicationContext(), "result wrong", Toast.LENGTH_SHORT).show();
                        }
                    }

                    else {
                        Toast.makeText(getApplicationContext(), "message type wrong", Toast.LENGTH_SHORT).show();
                    }
                }
                catch(JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_business_card);

        myName = (TextView)findViewById(R.id.tvMyName);
        myCompany = (TextView)findViewById(R.id.tvMyCompany);
        myPosition = (TextView)findViewById(R.id.tvMyPosition);
        myPhone = (TextView)findViewById(R.id.tvMyPhonenumber);
        myTel = (TextView)findViewById(R.id.tvMyTel);
        myEmail = (TextView)findViewById(R.id.tvMyEmail);
        myAddress = (TextView)findViewById(R.id.tvMyAddress);

        btModifyBusinessCard = (Button)findViewById(R.id.btModifyBusinessCard);
        btModifyBusinessCard.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MyBusinessCardModifyActivity.class);
                startActivity(intent);
            }
        });
        btShareBusinessCard = (Button)findViewById(R.id.btShareBusinessCard);
    }

    @Override
    protected void onResume() {
        super.onResume();

        myInfo = DBManager.getManager(getApplicationContext()).getUserInfo();
        if(myInfo.businessCardShareFlag.equals("0"))
        {
            isShared = false;
        }
        else
        {
            isShared = true;
            btShareBusinessCard.setText("Unshare");
        }

        btShareBusinessCard.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View view) {
                if(isShared)
                {
                    JSONObject jobj = new JSONObject();

                    try {
                        jobj.put("messagetype", "update_business_card_share_flag");
                        jobj.put("user_seq", myInfo.userSeq);
                        jobj.put("business_card_share_flag", 0);
                        jobj.put("time_code", "");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    new AsyncHttpsTask(getApplicationContext(), GlobalVariable.WEB_SERVER_IP, mHandler, jobj, 3, 0);
                }
                else
                {
                    Date now = new Date();
                    String businessCode = now.getTime() + "";
                    businessCode = businessCode.substring(businessCode.length()-4);

                    //send to server
                    JSONObject jobj = new JSONObject();

                    try {
                        jobj.put("messagetype", "update_business_card_share_flag");
                        jobj.put("user_seq", myInfo.userSeq);
                        jobj.put("business_card_share_flag", 1);
                        jobj.put("time_code", businessCode);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    new AsyncHttpsTask(getApplicationContext(), GlobalVariable.WEB_SERVER_IP, mHandler, jobj, 2, 0);
                }
            }
        });

        ivMyPhoto = (ImageView)findViewById(R.id.ivMyPhoto);
        ivMyPhoto.setOnClickListener(new ImageView.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType(android.provider.MediaStore.Images.Media.CONTENT_TYPE);
                intent.setData(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, REQ_CODE_SELECT_IMAGE);
            }
        });
        /*
        if(myInfo.picture != null)
        {
            setImage();
        }*/
        setTextView();
    }

    private void setImage() {
        byte[] resBytes = Base64.decode(myInfo.picture, Base64.DEFAULT);
        Bitmap imageBitmap = BitmapFactory.decodeByteArray(resBytes, 0, resBytes.length);
        ivMyPhoto.setImageBitmap(imageBitmap);
        ivMyPhoto.setScaleType(ImageView.ScaleType.FIT_XY);
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
            if(myInfo.idFlag.equals("e")){
                myEmail.setText(myInfo.email);
            }
            else{
                myEmail.setText("");
            }
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
                    String pathStr = getImageNameToUri(data.getData());


                    //Get Bitmap from Image file
                    Bitmap image_bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());
                    ImageView image = (ImageView) findViewById(R.id.ivMyPhoto);

                    //set to image view
                    image.setImageBitmap(image_bitmap);

                    //set Image size to fit in
                    image.setScaleType(ImageView.ScaleType.FIT_XY);

                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    image_bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                    byte[] b = baos.toByteArray();
                    String imageCode = Base64.encodeToString(b, Base64.DEFAULT);
                    myInfo.picture = imageCode;


                    JSONObject jobj = new JSONObject();

                    try {
                        jobj.put("messagetype", "update_user_picture");
                        jobj.put("user_seq", myInfo.userSeq);
                        jobj.put("picture", pathStr);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    new AsyncHttpsTask(getApplicationContext(), GlobalVariable.WEB_SERVER_IP, mHandler, jobj, 1, 0);

                }/* catch (FileNotFoundException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }*/ catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void showConfirmDialog(String code)
    {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        final TextView tvMsg = new TextView(this);
        String msg;
        if(isShared)
        {
            msg = "Your business card is unshared";
        }
        else
        {
            msg = "Your business card is shared.\nYour current sharing code is " + code;
        }

        tvMsg.setText(msg);
        tvMsg.setTextSize(20);
        alert.setView(tvMsg);

        if (isShared) {
            btShareBusinessCard.setText("Share");
        } else {
            btShareBusinessCard.setText("Unshare");
        }
        isShared = !isShared;

        alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Canceled.
            }
        });

        alert.show();
    }

    public String getImageNameToUri(Uri data)
    {
        String[] proj = { MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(data, proj, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

        cursor.moveToFirst();

        String imgPath = cursor.getString(column_index);
        //String imgName = imgPath.substring(imgPath.lastIndexOf("/")+1);

        return imgPath;
    }
}
