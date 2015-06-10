package com.infjay.mice;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

/**
 * Created by HJHOME on 2015-06-07.
 * 세션의 상세 정보를 나타내주는 액티비티
 */
public class SessionInfoActivity extends Activity{
    String title;
    String writer;
    String presenter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_session_info);


        Intent intent = getIntent();
        this.title = (String)intent.getSerializableExtra("title");
        this.writer = (String)intent.getSerializableExtra("writer");
        this.presenter = (String)intent.getSerializableExtra("presenter");

        Toast.makeText(getApplicationContext(), "Title :" + this.title + "Writer : " + this.writer , Toast.LENGTH_SHORT).show();
    }
}
