package com.infjay.mice;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.infjay.mice.artifacts.AgendaSessionInfo;
import com.infjay.mice.artifacts.BusinessCardInfo;
import com.infjay.mice.artifacts.ConferenceInfo;
import com.infjay.mice.artifacts.CouponInfo;
import com.infjay.mice.artifacts.IndoorMapInfo;
import com.infjay.mice.artifacts.SponsorInfo;
import com.infjay.mice.database.DBManager;
import com.infjay.mice.global.GlobalVariable;
import com.infjay.mice.network.AsyncHttpsTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class BusinessCardActivity extends CustomActionBarActivity {
    private LinearLayout imageLayout;

    private String userSeq;
    private String mySeq;
    private BusinessCardInfo bInfo;

    private TextView tvName, tvCompany, tvDuty, tvPhone, tvTel, tvEmail, tvAddress;
    private ImageView ivProfile;

    private String name = "";
    private String company = "";
    private String title = "";
    private String phone = "";
    private String email = "";
    private String addr = "";

    protected Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            if(msg.what == 1)
            {
                try
                {
                    JSONObject jobj = new JSONObject(msg.obj + "");
                    if(jobj.get("messagetype").equals("get_user_info"))
                    {
                        if(jobj.get("result").equals("GET_USER_INFO_ERROR"))
                        {
                            Toast.makeText(getApplicationContext(), "GET_USER_INFO_ERROR", Toast.LENGTH_SHORT).show();
                        }
                        else if(jobj.get("result").equals("GET_USER_INFO_FAIL"))
                        {
                            Toast.makeText(getApplicationContext(), "GET_USER_INFO_FAIL", Toast.LENGTH_SHORT).show();
                        }
                        else if(jobj.get("result").equals("GET_USER_INFO_SUCCESS"))
                        {
                            JSONObject jobjCardInfo = new JSONObject(jobj.get("attach")+"");
                            bInfo = new BusinessCardInfo();
                            bInfo.userSeq = jobjCardInfo.get("user_seq")+"";
                            bInfo.idFlag = jobjCardInfo.get("id_flag")+"";
                            bInfo.userId = jobjCardInfo.get("user_id")+"";
                            bInfo.name = jobjCardInfo.get("name")+"";
                            bInfo.company = jobjCardInfo.get("company")+"";
                            bInfo.picturePath = "";
                            bInfo.phone = jobjCardInfo.get("phone")+"";
                            bInfo.email = jobjCardInfo.get("email")+"";
                            bInfo.address = jobjCardInfo.get("address")+"";
                            bInfo.authorityKind = jobjCardInfo.get("authority_kind")+"";
                            bInfo.phone_1 = jobjCardInfo.get("phone_1")+"";
                            bInfo.phone_2 = jobjCardInfo.get("phone_2")+"";
                            bInfo.cellPhone_1 = jobjCardInfo.get("cell_phone_1")+"";
                            bInfo.cellPhone_2 = jobjCardInfo.get("cell_phone_2")+"";
                            bInfo.businessCardCode = jobjCardInfo.get("business_card_code")+"";
                            bInfo.businessCardShareFlag = jobjCardInfo.get("business_card_share_flag")+"";
                            bInfo.nationCode = jobjCardInfo.get("nation_code")+"";
                            bInfo.duty = jobjCardInfo.get("duty")+"";

                            DBManager.getManager(getApplicationContext()).deleteBusinessCardInfoByUserSeqAndCardSeq(mySeq, userSeq);
                            DBManager.getManager(getApplicationContext()).insertBusinessCard(mySeq, bInfo);
                            setText();
                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(), "WRONG_RESULT", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(), "WRONG_MASSAGE_TYPE", Toast.LENGTH_SHORT).show();
                    }

                }
                catch(JSONException e)
                {
                    e.printStackTrace();
                }
            }

            if(msg.what == 2)
            {
                try
                {
                    JSONObject jobj = new JSONObject(msg.obj + "");
                    if(jobj.get("messagetype").equals("delete_business_card"))
                    {
                        if(jobj.get("result").equals("DELETE_BUSINESS_CARD_FAIL"))
                        {
                            Toast.makeText(getApplicationContext(), "DELETE_BUSINESS_CARD_FAIL", Toast.LENGTH_SHORT).show();
                        }
                        else if(jobj.get("result").equals("DELETE_BUSINESS_CARD_SUCCESS"))
                        {
                            //Delete Business card Success
                            DBManager.getManager(getApplicationContext()).deleteBusinessCardInfoByUserSeqAndCardSeq(mySeq, userSeq);
                            finish();
                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(), "WRONG_RESULT", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(), "WRONG_MASSAGE_TYPE", Toast.LENGTH_SHORT).show();
                    }

                }
                catch(JSONException e)
                {
                    e.printStackTrace();
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_card);

        Intent intent = getIntent();
        name = intent.getStringExtra("name");
        userSeq = intent.getStringExtra("userSeq");
        mySeq = DBManager.getManager(getApplicationContext()).getUserInfo().userSeq;

        tvName = (TextView)findViewById(R.id.tvCardName);
        tvCompany = (TextView)findViewById(R.id.tvCompany);
        tvDuty = (TextView)findViewById(R.id.tvTitle);
        tvPhone = (TextView)findViewById(R.id.tvPhonenumber);
        tvTel = (TextView)findViewById(R.id.tvTel);
        tvEmail = (TextView)findViewById(R.id.tvEmail);
        tvAddress = (TextView)findViewById(R.id.tvAddr);
        ivProfile = (ImageView)findViewById(R.id.ivProfile);

        //setImage();

    }

    @Override
    protected void onResume() {
        super.onResume();
        if(mySeq == null) {
            return;
        }

        bInfo = DBManager.getManager(getApplicationContext()).getBusinessCardInfoByUserSeqAndCardSeq(mySeq, userSeq);
        setText();

        JSONObject jobj = new JSONObject();
        try
        {
            jobj.put("messagetype", "get_user_info");
            jobj.put("user_seq", userSeq);
            jobj.put("session_user_seq", mySeq);
        }
        catch(JSONException e)
        {
            e.printStackTrace();
        }
        new AsyncHttpsTask(getApplicationContext(), GlobalVariable.WEB_SERVER_IP, mHandler, jobj, 1, 0);
    }

    public void setText() {
        tvName.setText(bInfo.name);
        tvCompany.setText(bInfo.company);
        tvDuty.setText(bInfo.duty);

        if(bInfo.businessCardShareFlag.equals("0"))
        {
            tvPhone.setText("");
            tvTel.setText("");
            tvEmail.setText("");
            tvAddress.setText("");
            Toast.makeText(getApplicationContext(), "BusinessCard is not shared", Toast.LENGTH_SHORT).show();
        }
        else
        {
            tvPhone.setText(bInfo.phone);
            tvTel.setText(bInfo.phone_1);
            tvEmail.setText(bInfo.email);
            tvAddress.setText(bInfo.address);
        }
    }

    public void setImage() {
        LinearLayout.LayoutParams param;

        Display display = getWindowManager().getDefaultDisplay();
        int height = display.getHeight();

        imageLayout = (LinearLayout)findViewById(R.id.briefLinearLayout);

        imageLayout.getLayoutParams().height = height/4;
        ImageView img = new ImageView(this);
        img.setImageResource(R.drawable.test_picture);
        img.setScaleType(ImageView.ScaleType.FIT_CENTER);

        imageLayout.addView(img);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_business_card, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.itSendMessage) {
            Intent intent = new Intent(getApplicationContext(), ChattingActivity.class);
            intent.putExtra("userName", bInfo.name);
            intent.putExtra("userSeq", userSeq);
            startActivity(intent);
            return true;
        }
        if (id == R.id.itDeleteCard) {
            JSONObject jobj = new JSONObject();
            try
            {
                jobj.put("messagetype", "delete_business_card");
                jobj.put("user_seq", mySeq);
                jobj.put("share_user_seq", userSeq);
            }
            catch(JSONException e)
            {
                e.printStackTrace();
            }
            new AsyncHttpsTask(getApplicationContext(), GlobalVariable.WEB_SERVER_IP, mHandler, jobj, 2, 0);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.slide_in_from_left, R.anim.slide_out_to_right);
    }
}
