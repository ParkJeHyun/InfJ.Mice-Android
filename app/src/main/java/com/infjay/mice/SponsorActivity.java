package com.infjay.mice;

/**
 * Created by Administrator on 2015-05-02.
 */

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.infjay.mice.adapter.CouponListAdapter;
import com.infjay.mice.adapter.SponsorAdapter;
import com.infjay.mice.adapter.ViewHolder;
import com.infjay.mice.artifacts.CouponInfo;
import com.infjay.mice.artifacts.SponsorInfo;
import com.infjay.mice.artifacts.UserInfo;
import com.infjay.mice.database.DBManager;
import com.infjay.mice.global.GlobalVariable;
import com.infjay.mice.network.AsyncHttpsTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SponsorActivity extends CustomActionBarActivity {

    private boolean drawFlag = true;

    private ListView lvSponsorList;
    private SponsorInfo sInfo;
    private ArrayList<SponsorInfo> sponsorArrayList;

    private String TAG = "SponsorActivity";

    private SponsorAdapter adapter;

    protected Handler mHandlerReceive = new Handler() {
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

                    if(jobj.get("messagetype").equals("get_all_sponsor")) {
                        if (jobj.get("result").equals("GET_ALL_SPONSOR_ERROR")) {
                            //Toast.makeText(getApplicationContext(), "GET_ALL_COUPON_BY_USER_SEQ_ERROR", Toast.LENGTH_SHORT).show();
                        }
                        else if (jobj.get("result").equals("GET_ALL_SPONSOR_SUCCESS")) {
                            //correct coupon serial number
                            //create couponInfo
                            //insert SQLite
                            //Go to CouponActivity
                            //Toast.makeText(getApplicationContext(), "GET_ALL_COUPON_BY_USER_SEQ_SUCCESS", Toast.LENGTH_SHORT).show();

                            SponsorInfo sponsorInfo;

                            JSONArray sponsorJsonArray = new JSONArray(jobj.get("attach").toString());

                            ArrayList<SponsorInfo> sponsorList = new ArrayList<SponsorInfo>();

                            for (int i = 0; i < sponsorJsonArray.length(); i++) {
                                JSONObject sponsorJobj = new JSONObject(sponsorJsonArray.get(i).toString());
                                sponsorInfo = new SponsorInfo();

                                sponsorInfo.sponsorSeq = sponsorJobj.get("sponsor_seq").toString();
                                sponsorInfo.sponsorName = sponsorJobj.get("sponsor_name").toString();
                                sponsorInfo.sponsorExplanation = sponsorJobj.get("explanation").toString();
                                sponsorInfo.attached = sponsorJobj.get("attached").toString();

                                sponsorList.add(sponsorInfo);
                            }
                            DBManager.getManager(getApplicationContext()).deleteAllSponsor();
                            DBManager.getManager(getApplicationContext()).insertSponsor(sponsorList);
                            makeSponsorList();

                            Toast.makeText(getApplicationContext(), "Refresh Sponsor Complete", Toast.LENGTH_SHORT).show();
                            sponsorArrayList = sponsorList;

                        }
                        else if (jobj.get("result").equals("GET_ALL_SPONSOR_FAIL")) {
                            //Toast.makeText(getApplicationContext(), "GET_ALL_COUPON_BY_USER_SEQ_FAIL", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Toast.makeText(getApplicationContext(), "result wrong", Toast.LENGTH_SHORT).show();
                        }
                    }

                    else {
                        Toast.makeText(getApplicationContext(), "messagetype wrong not get_all_sponsor", Toast.LENGTH_SHORT).show();
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
        setContentView(R.layout.activity_spon);

        setListView();
    }

    public void setListView(){
        int count = DBManager.getManager(getApplicationContext()).getSponsorCount();

        if(count==0){
            refresh();
        }
        else{
            makeSponsorList();
        }
    }

    public void makeSponsorList()
    {
        lvSponsorList = (ListView)findViewById(R.id.lvSponsorList);
        sponsorArrayList = new ArrayList<SponsorInfo>();
        sponsorArrayList = DBManager.getManager(getApplicationContext()).getAllSponsor();

        adapter = new SponsorAdapter(this, R.layout.list_row_sponser, sponsorArrayList);
        lvSponsorList.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        lvSponsorList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ViewHolder vh = (ViewHolder) view.getTag();
                String seq = vh.sponsorSeq;

                Intent intent = new Intent(SponsorActivity.this, SponsorInfoActivity.class);
                intent.putExtra("sponsor_seq", seq);
                startActivity(intent);

                overridePendingTransition(R.anim.slide_in_from_right, R.anim.slide_out_to_left);
            }
        });
    }

/*
    @Override
    protected void onResume() {
        super.onResume();
        makeSponsorList();
    }
*/
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_sponsor, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.itRefreshSponsorInfo) {

            refresh();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void refresh()
    {
        JSONObject jobj = new JSONObject();

        try {
            jobj.put("messagetype", "get_all_sponsor");
        }
        catch(JSONException e){
            e.printStackTrace();
        }

        new AsyncHttpsTask(getApplicationContext(), GlobalVariable.WEB_SERVER_IP, mHandlerReceive, jobj, 1, 0);

    }
}