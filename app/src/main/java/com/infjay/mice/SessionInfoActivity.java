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
    String title;
    String writer;
    String presenter;

    TextView tvTitle;
    TextView tvWriter;
    TextView tvPresenter;
    TextView tvStartTime;
    TextView tvEndTime;
    TextView tvContents;
    Button addBinder;
    Button downLoad;

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
        tvStartTime = (TextView)findViewById(R.id.tvSessionInfoStartTime);
        tvEndTime = (TextView)findViewById(R.id.tvSessionInfoEndTIme);
        tvContents = (TextView)findViewById(R.id.tvSessionInfoContents);

        addBinder = (Button)findViewById(R.id.btAddBinder);
        downLoad = (Button)findViewById(R.id.btDownloadSessionPdf);

        addBinder.setOnClickListener(this);
        downLoad.setOnClickListener(this);

        makeExampleData();
        setData();

        Toast.makeText(getApplicationContext(), "Title :" + this.title + "Writer : " + this.writer , Toast.LENGTH_SHORT).show();
    }

    public void setData(){
        tvTitle.setText(exampleSession.sessionTitle);
        tvWriter.setText(exampleSession.sessionWriterUserSeq);
        tvPresenter.setText(exampleSession.sessionPresenterUserSeq);
        tvStartTime.setText(exampleSession.sessionStartTime);
        tvEndTime.setText("~ "+exampleSession.sessionEndTime);
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
        exampleSession.sessionStartTime = "2015-06-25 13:30:00";
        exampleSession.sessionEndTime = "2015-06-25 15:30:00";
        exampleSession.sessionAttached = "Nothing";
        exampleSession.regDate = nowDate;
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
