package com.infjay.mice;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.infjay.mice.adapter.CardholderAdapter;
import com.infjay.mice.adapter.CouponListAdapter;
import com.infjay.mice.adapter.ViewHolder;
import com.infjay.mice.artifacts.AgendaSessionInfo;
import com.infjay.mice.artifacts.BusinessCardInfo;
import com.infjay.mice.artifacts.CouponInfo;
import com.infjay.mice.database.DBManager;
import com.infjay.mice.global.GlobalVariable;
import com.infjay.mice.network.AsyncHttpsTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class CouponListActivity extends ActionBarActivity {

    private ListView lvCouponList;
    private CouponListAdapter adapter;
    private ArrayList<CouponInfo> arrayList;

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

                    if(jobj.get("messagetype").equals("get_all_coupon_by_user_seq")) {
                        if (jobj.get("result").equals("GET_ALL_COUPON_BY_USER_SEQ_ERROR")) {
                            //Toast.makeText(getApplicationContext(), "GET_ALL_COUPON_BY_USER_SEQ_ERROR", Toast.LENGTH_SHORT).show();
                        }
                        else if (jobj.get("result").equals("GET_ALL_COUPON_BY_USER_SEQ_SUCCESS")) {
                            //correct coupon serial number
                            //create couponInfo
                            //insert SQLite
                            //Go to CouponActivity
                            //Toast.makeText(getApplicationContext(), "GET_ALL_COUPON_BY_USER_SEQ_SUCCESS", Toast.LENGTH_SHORT).show();

                            CouponInfo couponInfo;

                            JSONArray couponJsonArray = new JSONArray(jobj.get("attach").toString());

                            couponInfo = new CouponInfo();
                            ArrayList<CouponInfo> couponList = new ArrayList<CouponInfo>();

                            for (int i = 0; i < couponJsonArray.length(); i++) {
                                JSONObject couponJobj = new JSONObject(couponJsonArray.get(i).toString());
                                couponInfo = new CouponInfo();

                                couponInfo.couponSeq = couponJobj.get("coupon_seq").toString();
                                couponInfo.couponName = couponJobj.get("coupon_name").toString();
                                couponInfo.couponSerial = couponJobj.get("coupon_serial").toString();
                                couponInfo.couponImg = couponJobj.get("coupon_img").toString();
                                couponInfo.couponExplanation = couponJobj.get("coupon_explanation").toString();

                                couponList.add(couponInfo);
                            }
                            DBManager.getManager(getApplicationContext()).deleteAllCoupon();
                            DBManager.getManager(getApplicationContext()).insertCoupon(couponList);

                            Toast.makeText(getApplicationContext(), "ADD COUPON COMPLETE", Toast.LENGTH_SHORT).show();
                            arrayList = couponList;

                        }
                        else if (jobj.get("result").equals("GET_ALL_COUPON_BY_USER_SEQ_FAIL")) {
                            //Toast.makeText(getApplicationContext(), "GET_ALL_COUPON_BY_USER_SEQ_FAIL", Toast.LENGTH_SHORT).show();
                        }
                        else {
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
        setContentView(R.layout.activity_coupon_list);

        lvCouponList = (ListView)findViewById(R.id.lvCouponList);
        arrayList = new ArrayList<CouponInfo>();

        //arrayList = DBManager.getManager(getApplicationContext()).getAllCoupon();

        makeCouponListByServer();

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
    public void makeCouponListByServer(){
        JSONObject jobj = new JSONObject();

        try {
            jobj.put("messagetype", "get_all_coupon_by_user_seq");
            jobj.put("user_seq", DBManager.getManager(getApplicationContext()).getUserInfo().userSeq.toString());
        }
        catch(JSONException e){
            e.printStackTrace();
        }

        new AsyncHttpsTask(getApplicationContext(), GlobalVariable.WEB_SERVER_IP, mHandler, jobj, 1, 0);
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
