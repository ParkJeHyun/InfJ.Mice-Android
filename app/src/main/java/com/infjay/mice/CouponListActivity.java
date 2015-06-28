package com.infjay.mice;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.infjay.mice.adapter.CardholderAdapter;
import com.infjay.mice.adapter.CouponListAdapter;
import com.infjay.mice.adapter.ViewHolder;
import com.infjay.mice.artifacts.BusinessCardInfo;
import com.infjay.mice.artifacts.CouponInfo;
import com.infjay.mice.database.DBManager;

import java.util.ArrayList;


public class CouponListActivity extends ActionBarActivity {

    private ListView lvCouponList;
    private CouponListAdapter adapter;
    private ArrayList<CouponInfo> arrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coupon_list);

        lvCouponList = (ListView)findViewById(R.id.lvCouponList);
        arrayList = new ArrayList<CouponInfo>();

        arrayList = DBManager.getManager(getApplicationContext()).getAllCoupon();

        adapter = new CouponListAdapter(this, R.layout.list_row_coupon, arrayList);
        lvCouponList.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        lvCouponList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ViewHolder vh = (ViewHolder) view.getTag();
                String seq = vh.couponSeq;

                Intent intent = new Intent(CouponListActivity.this,CouponInfoActivity.class);
                intent.putExtra("coupon_seq", seq);
                startActivity(intent);

                overridePendingTransition(R.anim.slide_in_from_right, R.anim.slide_out_to_left);

                //start Activity about sponser clicked
                //Toast.makeText(getApplicationContext(), name + ", " + company + " clicked()", Toast.LENGTH_SHORT).show();
                //Intent intent = new Intent(getApplicationContext(), ViewBusinessCardActivity.class);
                //startActivity(intent);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_coupon_list, menu);
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
}
