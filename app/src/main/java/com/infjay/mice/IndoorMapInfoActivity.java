package com.infjay.mice;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Base64;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import com.infjay.mice.artifacts.CouponInfo;
import com.infjay.mice.artifacts.IndoorMapInfo;
import com.infjay.mice.database.DBManager;
import com.infjay.mice.global.GlobalVariable;
import com.infjay.mice.network.AsyncHttpsTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by HJHOME on 2015-06-07.
 */
public class IndoorMapInfoActivity extends CustomActionBarActivity{
    private String mapSeq;
    private IndoorMapInfo indoorMapInfo;
    private ImageView ivMapImage;

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

                    if(jobj.get("messagetype").equals("get_indoor_map_by_seq")) {
                        if (jobj.get("result").equals("GET_INDOOR_MAP_BY_SEQ_ERROR")) {
                            Toast.makeText(getApplicationContext(), "get_indoor_map_by_seq_ERROR", Toast.LENGTH_SHORT).show();
                        }
                        else if (jobj.get("result").equals("GET_INDOOR_MAP_BY_SEQ_SUCCESS")) {
                            IndoorMapInfo indoorMapInfo = new IndoorMapInfo();

                            JSONObject sponsorJobj = new JSONObject(jobj.get("attach")+"");

                            indoorMapInfo.indoorMapSeq = sponsorJobj.get("indoor_map_seq").toString();
                            indoorMapInfo.order = sponsorJobj.get("map_order").toString();
                            indoorMapInfo.title = sponsorJobj.get("title").toString();
                            indoorMapInfo.imagePath = sponsorJobj.get("image").toString();

                            DBManager.getManager(getApplicationContext()).deleteIndoorMapInfoBySeq(indoorMapInfo.indoorMapSeq);

                            ArrayList<IndoorMapInfo> indoorMapList = new ArrayList<IndoorMapInfo>();

                            indoorMapList.add(indoorMapInfo);

                            DBManager.getManager(getApplicationContext()).insertIndoorMapInfo(indoorMapList);
                            setImage();
                        }
                        else if (jobj.get("result").equals("GET_INDOOR_MAP_BY_SEQ_FAIL")) {
                            Toast.makeText(getApplicationContext(), "get_indoor_map_by_seq_FAIL", Toast.LENGTH_SHORT).show();
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
        setContentView(R.layout.activity_indoor_map_info);

        Intent intent = getIntent();
        mapSeq = intent.getStringExtra("mapSeq");

        ivMapImage = (ImageView)findViewById(R.id.ivIndoorMapImage);

        setImage();
    }

    public void setImage(){
        indoorMapInfo = DBManager.getManager(getApplicationContext()).getIndoorMapByMapSeq(mapSeq);
        if(indoorMapInfo.imagePath.equals("null")){
            JSONObject jobj = new JSONObject();

            try {
                jobj.put("messagetype", "get_indoor_map_by_seq");
                jobj.put("indoor_map_seq", indoorMapInfo.indoorMapSeq);
            }
            catch(JSONException e){
                e.printStackTrace();
            }

            new AsyncHttpsTask(getApplicationContext(), GlobalVariable.WEB_SERVER_IP, mHandler, jobj, 1, 0);
        }
        else {
            byte[] resBytes = Base64.decode(indoorMapInfo.imagePath, Base64.DEFAULT);
            Bitmap imageBitmap = BitmapFactory.decodeByteArray(resBytes, 0, resBytes.length);
            ivMapImage.setImageBitmap(imageBitmap);
            ivMapImage.setScaleType(ImageView.ScaleType.FIT_XY);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

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
