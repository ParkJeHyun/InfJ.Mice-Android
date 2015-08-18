package com.infjay.mice;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.infjay.mice.artifacts.AgendaSessionInfo;
import com.infjay.mice.database.DBManager;
import com.infjay.mice.global.GlobalVariable;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by HJHOME on 2015-06-07.
 * 세션의 상세 정보를 나타내주는 액티비티
 */
public class SessionInfoActivity extends CustomActionBarActivity{

    private TextView tvTitle;
    private TextView tvWriter;
    private TextView tvPresenter;
    private TextView tvSessionTime;
    private TextView tvContents;

    private String sessionSeq;
    private String activityFrom;
    private AgendaSessionInfo mAgendaSessionInfo;

    private boolean isinBinder = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_session_info);

        Intent intent = getIntent();
        sessionSeq = intent.getExtras().getString("sessionSeq");
        activityFrom = intent.getExtras().getString("activityFrom");
        mAgendaSessionInfo = DBManager.getManager(getApplicationContext()).getSessionFromAgendaBySessionSeq(sessionSeq);

        tvTitle = (TextView)findViewById(R.id.tvSessionInfoTitle);
        tvWriter = (TextView)findViewById(R.id.tvSessionInfoWriter);
        tvPresenter = (TextView)findViewById(R.id.tvSessionInfoPresenter);
        tvSessionTime = (TextView)findViewById(R.id.tvSessionInfoTime);
        tvContents = (TextView)findViewById(R.id.tvSessionInfoContents);

        String userSeq = DBManager.getManager(getApplicationContext()).getUserInfo().userSeq;
        if(DBManager.getManager(getApplicationContext()).getSessionExistInBinderByUserSeqAndSessionSeq(userSeq,sessionSeq)!=0)
        {
            isinBinder = true;
        }
        setData();

    }

    public void setData(){
        tvTitle.setText(mAgendaSessionInfo.sessionTitle);
        tvWriter.setText(mAgendaSessionInfo.sessionWriterUserSeq);
        tvPresenter.setText(mAgendaSessionInfo.sessionPresenterUserSeq);

        String startTime = mAgendaSessionInfo.sessionStartTime.substring(0, 16).replace(" ", " / ");
        String endTime = mAgendaSessionInfo.sessionEndTime.split(" ")[1].substring(0, 5);

        tvSessionTime.setText(startTime + " ~ " + endTime);
        tvContents.setText(mAgendaSessionInfo.sessionContents);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if(isinBinder)
        {
            getMenuInflater().inflate(R.menu.menu_session_info_in_binder, menu);
        }
        else
        {
            getMenuInflater().inflate(R.menu.menu_session_info, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.itAddToBinder) {
            if(isinBinder)
            {
                DBManager.getManager(getApplicationContext()).deleteSessionInBinder(mAgendaSessionInfo);
                Toast.makeText(getApplicationContext(), "Delete from binder success", Toast.LENGTH_SHORT).show();
                finish();
            }
            else
            {
                String userSeq = DBManager.getManager(getApplicationContext()).getUserInfo().userSeq;
                DBManager.getManager(getApplicationContext()).insertSessionTobinder(userSeq,mAgendaSessionInfo);
                Toast.makeText(getApplicationContext(), "Add to binder success", Toast.LENGTH_SHORT).show();
                finish();
            }
            return true;
        }

        if(id == R.id.itDownloadSessionInfo)
        {
            //download
            WebView webView = new WebView(this);
            webView.getSettings().setJavaScriptEnabled(true);

            String pdfUrl = GlobalVariable.WEB_SERVER_IP+":"+GlobalVariable.HTTPS_PORT+"/session_attached/"+mAgendaSessionInfo.sessionAttached;
            System.out.println(pdfUrl);
            webView.loadUrl("http://docs.google.com/gview?embedded=true&url="+pdfUrl);
            setContentView(webView);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
