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
    AgendaSessionInfo exampleSession;
    private String title;
    private String writer;
    private String presenter;

    private TextView tvTitle;
    private TextView tvWriter;
    private TextView tvPresenter;
    private TextView tvSessionTime;
    private TextView tvContents;
    private Button btAddBinder;
    private Button btDownload;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_session_info);


        Intent intent = getIntent();
        this.title = (String)intent.getSerializableExtra("title");
        this.writer = (String)intent.getSerializableExtra("writer");
        this.presenter = (String)intent.getSerializableExtra("presenter");

        tvTitle = (TextView)findViewById(R.id.tvSessionInfoTitle);
        tvWriter = (TextView)findViewById(R.id.tvSessionInfoWriter);
        tvPresenter = (TextView)findViewById(R.id.tvSessionInfoPresenter);
        tvSessionTime = (TextView)findViewById(R.id.tvSessionInfoTime);
        tvContents = (TextView)findViewById(R.id.tvSessionInfoContents);

        btAddBinder = (Button)findViewById(R.id.btAddBinder);
        btDownload = (Button)findViewById(R.id.btDownloadSessionPdf);

        btAddBinder.setOnClickListener(this);
        btDownload.setOnClickListener(this);

        makeExampleData();
        setData();

        Toast.makeText(getApplicationContext(), "Title :" + this.title + "Writer : " + this.writer , Toast.LENGTH_SHORT).show();
    }

    public void setData(){
        tvTitle.setText(exampleSession.sessionTitle);
        tvWriter.setText(exampleSession.sessionWriterUserSeq);
        tvPresenter.setText(exampleSession.sessionPresenterUserSeq);
        tvSessionTime.setText(exampleSession.sessionStartTime + " ~ " + exampleSession.sessionEndTime);
        tvContents.setText(exampleSession.sessionContents);
    }
    public void makeExampleData(){
        Date now = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss E요일");
        String nowDate = sdf.format(now);

        exampleSession = new AgendaSessionInfo();
        exampleSession.agendaSessionSeq = "1";
        exampleSession.sessionTitle = "Session No.1";
        exampleSession.sessionContents = "This is Example Session! By ParkJeHyun";
        exampleSession.sessionSumarry = "Example Session";
        exampleSession.sessionWriterUserSeq = "Park Je Hyun";
        exampleSession.sessionPresenterUserSeq = "Park Je Hyun";
        exampleSession.sessionStartTime = "13:30:00";
        exampleSession.sessionEndTime = "15:30:00";
        exampleSession.sessionAttached = "Nothing";
        exampleSession.regDate = "6.24";
        exampleSession.modDate = nowDate;
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btAddBinder){
            DBManager.getManager(getApplicationContext()).insertSessionTobinder(exampleSession);
        }
        if(v.getId() == R.id.btDownloadSessionPdf){

        }
    }
}
