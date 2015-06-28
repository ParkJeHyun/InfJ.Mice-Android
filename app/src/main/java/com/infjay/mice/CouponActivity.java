package com.infjay.mice;

/**
 * Created by Administrator on 2015-05-02.
 */
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class CouponActivity extends ActionBarActivity implements View.OnClickListener{
    Button btAddCoupon;
    Button btCouponList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coupon);

        btAddCoupon = (Button)findViewById(R.id.btnAddCoupon);
        btCouponList = (Button)findViewById(R.id.btnCouponList);

        btAddCoupon.setOnClickListener(this);
        btCouponList.setOnClickListener(this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
        if(v.getId() == R.id.btnAddCoupon){
            Intent intent = new Intent(getApplicationContext(),
                    AddCouponActivity.class);
            startActivity(intent);
        }
        if(v.getId() == R.id.btnCouponList){
            Intent intent = new Intent(getApplicationContext(),
                    CouponListActivity.class);
            startActivity(intent);
        }

    }
}