package com.infjay.mice;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.infjay.mice.artifacts.AgendaSessionInfo;
import com.infjay.mice.artifacts.BusinessCardInfo;
import com.infjay.mice.artifacts.CouponInfo;
import com.infjay.mice.artifacts.UserInfo;
import com.infjay.mice.database.DBManager;
import com.infjay.mice.global.GlobalVariable;
import com.infjay.mice.network.AsyncHttpsTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class AddCouponActivity extends CustomActionBarActivity {

    private EditText etSerialNum1;
    private EditText etSerialNum2;
    private Button btComplete;
    private Button btCancel;

    private String serialNum;

    protected Handler mHandlerAdd = new Handler() {
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

                    if(jobj.get("messagetype").equals("add_coupon_by_serial")){
                        if(jobj.get("result").equals("ADD_COUPON_ERROR")){
                            Toast.makeText(getApplicationContext(), "ADD_COUPON_ERROR", Toast.LENGTH_SHORT).show();
                        }

                        else if(jobj.get("result").equals("ADD_COUPON_BY_SERIAL_SUCCESS")){
                            //correct coupon serial number
                            //create couponInfo
                            //insert SQLite
                            //Go to CouponActivity
                            Toast.makeText(getApplicationContext(), "ADD_COUPON_BY_SERIAL_SUCCESS", Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(getApplicationContext(), CouponActivity.class);
                            startActivity(intent);
                            finish();
                        }

                        else if(jobj.get("result").equals("ADD_COUPON_BY_SERIAL_FAIL")){
                            Toast.makeText(getApplicationContext(), "ADD_COUPON_BY_SERIAL_FAIL", Toast.LENGTH_SHORT).show();
                        }

                        else{
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
        setContentView(R.layout.activity_add_coupon);

        etSerialNum1 = (EditText)findViewById(R.id.etSerialNum1);
        etSerialNum2 = (EditText)findViewById(R.id.etSerialNum2);

        btComplete = (Button)findViewById(R.id.btCouponAdd);
        btCancel = (Button)findViewById(R.id.btCouponCancel);

        btComplete.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View view) {
                UserInfo userInfo = DBManager.getManager(getApplicationContext()).getUserInfo();
                if(userInfo == null)
                {
                    return;
                }
                if(etSerialNum1.length()!=0 && etSerialNum2.length()!=0){
                    serialNum = etSerialNum1.getText().toString();
                    serialNum += etSerialNum2.getText().toString();

                    JSONObject jobj = new JSONObject();

                    try {
                        jobj.put("messagetype", "add_coupon_by_serial");
                        jobj.put("coupon_serial", serialNum);
                        jobj.put("user_seq", userInfo.userSeq.toString());
                    }
                    catch(JSONException e){
                        e.printStackTrace();
                    }

                    new AsyncHttpsTask(getApplicationContext(), GlobalVariable.WEB_SERVER_IP, mHandlerAdd, jobj, 1, 0);
                }
            }
        });

        btCancel.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_coupon, menu);
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
