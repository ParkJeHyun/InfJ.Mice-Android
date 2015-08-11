package com.infjay.mice;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.infjay.mice.artifacts.CouponInfo;
import com.infjay.mice.artifacts.SponsorInfo;
import com.infjay.mice.database.DBManager;
import com.infjay.mice.global.GlobalVariable;
import com.infjay.mice.network.AsyncHttpsTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class CouponInfoActivity extends CustomActionBarActivity {
    private String couponSeq;
    private CouponInfo coupon;

    private TextView tvCouponName;
    private TextView tvCouponSerial;
    private ImageView ivCouponImg;

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

                    if(jobj.get("messagetype").equals("get_coupon_by_seq")) {
                        if (jobj.get("result").equals("GET_COUPON_BY_SEQ_ERROR")) {
                            //Toast.makeText(getApplicationContext(), "GET_ALL_COUPON_BY_USER_SEQ_ERROR", Toast.LENGTH_SHORT).show();
                        }
                        else if (jobj.get("result").equals("GET_COUPON_BY_SEQ_SUCCESS")) {
                            CouponInfo couponInfo = new CouponInfo();

                            JSONObject sponsorJobj = new JSONObject(jobj.get("attach")+"");

                            couponInfo.couponSeq = sponsorJobj.get("coupon_seq").toString();
                            couponInfo.couponName = sponsorJobj.get("coupon_name").toString();
                            couponInfo.couponExplanation = sponsorJobj.get("coupon_explanation").toString();
                            couponInfo.couponSerial = sponsorJobj.get("coupon_serial").toString();
                            couponInfo.couponImg = sponsorJobj.get("coupon_img").toString();

                            DBManager.getManager(getApplicationContext()).deleteCouponBySeq(couponInfo.couponSeq);

                            ArrayList<CouponInfo> couponList = new ArrayList<CouponInfo>();

                            couponList.add(couponInfo);

                            DBManager.getManager(getApplicationContext()).insertCoupon(couponList);
                            setImage();
                        }
                        else if (jobj.get("result").equals("GET_SPONSOR_BY_SEQ_FAIL")) {
                            //Toast.makeText(getApplicationContext(), "GET_ALL_COUPON_BY_USER_SEQ_FAIL", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Toast.makeText(getApplicationContext(), "result wrong", Toast.LENGTH_SHORT).show();
                        }
                    }

                    else {
                        Toast.makeText(getApplicationContext(), "messagetype wrong not get_sponsor_by_seq", Toast.LENGTH_SHORT).show();
                    }
                }
                catch(JSONException e) {
                    e.printStackTrace();
                }
                //response 받은거 파싱해서

            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coupon_info);

        Intent intent = getIntent();
        this.couponSeq = (String)intent.getSerializableExtra("coupon_seq");

        tvCouponName = (TextView)findViewById(R.id.tvCouponName);
        tvCouponSerial = (TextView)findViewById(R.id.tvCouponSerialNumber);
        ivCouponImg = (ImageView)findViewById(R.id.ivCouponImg);

        coupon = DBManager.getManager(getApplicationContext()).getCouponBySeq(couponSeq);

        tvCouponName.setText(coupon.couponName);
        tvCouponSerial.setText(coupon.couponSerial);

        setImage();
    }

    private void setImage(){

        coupon = DBManager.getManager(getApplicationContext()).getCouponBySeq(couponSeq);

        if(coupon.couponImg.equals("null")){
            JSONObject jobj = new JSONObject();

            try {
                jobj.put("messagetype", "get_coupon_by_seq");
                jobj.put("coupon_seq", couponSeq);
            }
            catch(JSONException e){
                e.printStackTrace();
            }

            new AsyncHttpsTask(getApplicationContext(), GlobalVariable.WEB_SERVER_IP, mHandler, jobj, 1, 0);
        }
        else {
            byte[] resBytes = Base64.decode(coupon.couponImg, Base64.DEFAULT);
            Bitmap imageBitmap = BitmapFactory.decodeByteArray(resBytes, 0, resBytes.length);
            ivCouponImg.setImageBitmap(imageBitmap);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_coupon_info, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        /*
        if (id == R.id.itDeleteCoupon) {

            return true;
        }*/

        if(id == android.R.id.home)
        {
            finish();
            overridePendingTransition(R.anim.slide_in_from_left, R.anim.slide_out_to_right);
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
