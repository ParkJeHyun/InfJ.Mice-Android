package com.infjay.mice;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.widget.EditText;

import com.infjay.mice.artifacts.MemoInfo;
import com.infjay.mice.database.DBManager;

import java.util.Date;

/**
 * Created by KimJS on 2015-06-26.
 */
public class MemoEditActivity extends ActionBarActivity {

    boolean isNewMemo;
    String memoContents;
    EditText etMemoEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memo_edit);
        setTitle("Edit Memo");

        etMemoEdit = (EditText)findViewById(R.id.etMemoEdit);

        Intent intent = getIntent();
        isNewMemo = intent.getExtras().getBoolean("isNew");
        if(isNewMemo)
        {
        }
        else
        {
            //input memo text into EditText
            //memoContents = DBManager.getManager(getApplicationContext()).
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_memo_edit, menu);
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //Save to SQLite
        memoContents = etMemoEdit.getText().toString();
        MemoInfo memoInfo = new MemoInfo();

        memoInfo.contents = memoContents;

        Date now = new Date();

        if(isNewMemo)
        {
            memoInfo.regDate = now.toString();
        }

        now = new Date();
        memoInfo.modDate = now.toString();

        DBManager.getManager(getApplicationContext()).insertMemoInfo(memoInfo);

    }
}
