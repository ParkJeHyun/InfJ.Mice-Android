package com.infjay.mice;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.infjay.mice.artifacts.ConferenceInfo;
import com.infjay.mice.artifacts.SponsorInfo;
import com.infjay.mice.database.DBManager;
import com.infjay.mice.global.GlobalVariable;
import com.infjay.mice.network.AsyncHttpsTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by KimJS on 2015-06-05.
 */
public class SponsorInfoActivity extends CustomActionBarActivity {

    private ImageView sponsorDetailImage;
    private SponsorInfo sponsorInfo;
    private String sponsorSeq;

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

                    if(jobj.get("messagetype").equals("get_sponsor_by_seq")) {
                        if (jobj.get("result").equals("GET_SPONSOR_BY_SEQ_ERROR")) {
                            //Toast.makeText(getApplicationContext(), "GET_ALL_COUPON_BY_USER_SEQ_ERROR", Toast.LENGTH_SHORT).show();
                        }
                        else if (jobj.get("result").equals("GET_SPONSOR_BY_SEQ_SUCCESS")) {
                            SponsorInfo sponsorInfo = new SponsorInfo();

                            JSONObject sponsorJobj = new JSONObject(jobj.get("attach")+"");

                            sponsorInfo.sponsorSeq = sponsorJobj.get("sponsor_seq").toString();
                            sponsorInfo.sponsorName = sponsorJobj.get("sponsor_name").toString();
                            sponsorInfo.sponsorExplanation = sponsorJobj.get("explanation").toString();
                            sponsorInfo.detailImagePath = sponsorJobj.get("detail_image").toString();

                            DBManager.getManager(getApplicationContext()).deleteSponsorBySeq(sponsorInfo.sponsorSeq);

                            ArrayList<SponsorInfo> sponsorList = new ArrayList<SponsorInfo>();

                            sponsorList.add(sponsorInfo);
                            /*
                            for (int i = 0; i < sponsorJsonArray.length(); i++) {
                                JSONObject sponsorJobj = new JSONObject(sponsorJsonArray.get(i).toString());
                                sponsorInfo = new SponsorInfo();

                                sponsorInfo.sponsorSeq = sponsorJobj.get("sponsor_seq").toString();
                                sponsorInfo.sponsorName = sponsorJobj.get("sponsor_name").toString();
                                sponsorInfo.sponsorExplanation = sponsorJobj.get("explanation").toString();
                                sponsorInfo.detailImagePath = sponsorJobj.get("detail_image").toString();
                                DBManager.getManager(getApplicationContext()).deleteSponsorBySeq(sponsorInfo.sponsorSeq);

                                sponsorList.add(sponsorInfo);
                            }
                            */
                            DBManager.getManager(getApplicationContext()).insertSponsor(sponsorList);
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

            if (msg.what == 2) {
                //핸들러 2번일 때
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sponsor_info);

        Intent intent = getIntent();
        sponsorSeq = intent.getExtras().getString("sponsor_seq");

        sponsorDetailImage = (ImageView)findViewById(R.id.ivSponsorDetailImage);
        setImage();
    }

    private void setImage(){
        sponsorInfo = DBManager.getManager(getApplicationContext()).getSponsorBySeq(sponsorSeq);
        if(sponsorInfo.detailImagePath.equals("null")){
            JSONObject jobj = new JSONObject();

            try {
                jobj.put("messagetype", "get_sponsor_by_seq");
                jobj.put("sponsor_seq", sponsorSeq);
            }
            catch(JSONException e){
                e.printStackTrace();
            }

            new AsyncHttpsTask(getApplicationContext(), GlobalVariable.WEB_SERVER_IP, mHandler, jobj, 1, 0);
        }
        else{
            byte[] resBytes = Base64.decode(sponsorInfo.detailImagePath, Base64.DEFAULT);
            Bitmap imageBitmap = BitmapFactory.decodeByteArray(resBytes, 0, resBytes.length);
            sponsorDetailImage.setImageBitmap(imageBitmap);
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_sponsor_info, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home) {
            finish();
            overridePendingTransition(R.anim.slide_in_from_left, R.anim.slide_out_to_right);
            return true;
        }

        if(id == R.id.itDownloadSponsorPdf)
        {
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
