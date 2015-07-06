package com.infjay.mice;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.infjay.mice.artifacts.AgendaSessionInfo;
import com.infjay.mice.database.DBManager;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by HJHOME on 2015-06-07.
 * 세션의 상세 정보를 나타내주는 액티비티
 */
public class SessionInfoActivity extends Activity implements View.OnClickListener{

    private TextView tvTitle;
    private TextView tvWriter;
    private TextView tvPresenter;
    private TextView tvSessionTime;
    private TextView tvContents;
    private Button btAddBinder;
    private Button btDownload;

    private String sessionSeq;
    private String activityFrom;
    private AgendaSessionInfo mAgendaSessionInfo;

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

        btAddBinder = (Button)findViewById(R.id.btAddBinder);
        btDownload = (Button)findViewById(R.id.btDownloadSessionPdf);

        String userSeq = DBManager.getManager(getApplicationContext()).getUserInfo().userSeq;
        if(DBManager.getManager(getApplicationContext()).getSessionExistInBinderByUserSeqAndSessionSeq(userSeq,sessionSeq)!=0)
        {
            //binder에 있는 경우
            btAddBinder.setText("Delete from Binder");
        }

        btAddBinder.setOnClickListener(this);
        btDownload.setOnClickListener(this);

        setData();

    }

    public void setData(){
        tvTitle.setText(mAgendaSessionInfo.sessionTitle);
        tvWriter.setText(mAgendaSessionInfo.sessionWriterUserSeq);
        tvPresenter.setText(mAgendaSessionInfo.sessionPresenterUserSeq);
        tvSessionTime.setText(mAgendaSessionInfo.sessionStartTime + " ~ " + mAgendaSessionInfo.sessionEndTime);
        tvContents.setText(mAgendaSessionInfo.sessionContents);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btAddBinder){
            if(activityFrom.equals("SessionActivity"))
            {
                String userSeq = DBManager.getManager(getApplicationContext()).getUserInfo().userSeq;
                DBManager.getManager(getApplicationContext()).insertSessionTobinder(userSeq,mAgendaSessionInfo);
                finish();
            }
            else if(activityFrom.equals("BinderActivity"))
            {
                DBManager.getManager(getApplicationContext()).deleteSessionInBinder(mAgendaSessionInfo);
                finish();
            }
            else
            {
                Toast.makeText(getApplicationContext(), "Error in activityFrom", Toast.LENGTH_SHORT).show();
            }

        }
        if(v.getId() == R.id.btDownloadSessionPdf){

        }
    }
}
