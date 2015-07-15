package com.infjay.mice;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.infjay.mice.artifacts.CouponInfo;
import com.infjay.mice.database.DBManager;


public class CouponInfoActivity extends CustomActionBarActivity {
    private String couponSeq;
    private CouponInfo coupon;

    private TextView tvCouponName;
    private TextView tvCouponSerial;
    private ImageView ivCouponImg;

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
        //ivCouponImg.setImageBitmap(coupon.couponImg);
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
        if (id == R.id.itDeleteCoupon) {

            return true;
        }

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
