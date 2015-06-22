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
import android.widget.Toast;

import com.facebook.widget.LoginButton;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.plus.PlusClient;
import com.infjay.mice.global.GlobalVariable;
import com.infjay.mice.network.AsyncHttpsTask;

import org.json.JSONException;
import org.json.JSONObject;


public class LoginActivity extends ActionBarActivity implements View.OnClickListener {
    //LoginButton facebookLoginBtn;
    Button facebookLoginBtn;
    Intent intent;

    //google
    Button googleLoginBtn;

    //AsyncTask �ڵ鷯, �Ѱ� ��Ƽ��Ƽ���� �������� �۾��� 1���� �ƴ� �� �ֱ� ������ �ڵ鷯 ���
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
                //�ڵ鸵 1�϶� �� ��
                System.out.println("response : "+msg.obj);
            }

            if (msg.what == 2) {
                //�ڵ鸵 2�϶� �� ��
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        facebookLoginBtn = (Button)findViewById(R.id.facebookLoginBtn);
        googleLoginBtn = (Button)findViewById(R.id.googleLoginBtn);

        googleLoginBtn.setOnClickListener(this);
        facebookLoginBtn.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.googleLoginBtn) {
            intent = new Intent(getApplicationContext(), GoogleLoginActivity.class);

            //////////////////////////AsyncTask Sample

            JSONObject jobj = new JSONObject();

            //��� �ӽõ�������
            //������ ���� JSON ��ü ����
            //messagetype�� ������ �׿� ���� �ļ� �����͵���
            //��űԾ� �����ؼ� ����ȭ ��ų ����
            try {
                jobj.put("messagetype", "login");
                jobj.put("user_id", "testid");
                jobj.put("password", "testpw");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            //new AsyncHttpsTask(getApplicationContext(), GlobalVariable.WEB_SERVER_IP, mHandler, jobj, 1, 0); //AsyncTask�̿��ؼ� ������ json ��ü ����.

            //////////////////////////AsyncTask Sample END
            startActivity(intent);
        }
        if (view.getId() == R.id.facebookLoginBtn){
            intent = new Intent(getApplicationContext(),
                    FacebookLoginActivity.class);
            startActivity(intent);
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
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
