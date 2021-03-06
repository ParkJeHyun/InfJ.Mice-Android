package com.infjay.mice;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.infjay.mice.artifacts.MemoInfo;
import com.infjay.mice.database.DBManager;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by KimJS on 2015-06-26.
 */
public class MemoEditActivity extends CustomActionBarActivity {

    boolean isNewMemo;
    String memoContents;
    EditText etMemoEdit;
    MemoInfo mInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memo_edit);
        setTitle("Edit Memo");

        etMemoEdit = (EditText)findViewById(R.id.etMemoEdit);

        Intent intent = getIntent();
        isNewMemo = intent.getExtras().getBoolean("isNew");
        if(isNewMemo == false)
        {
            String memoSeq = intent.getExtras().getString("memoSeq");
            //input memo text into EditText
            mInfo = DBManager.getManager(getApplicationContext()).getMemoByMemoSeq(memoSeq);
            etMemoEdit.setText(mInfo.contents);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_memo_edit, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //Add New Memo
        if (id == R.id.itDeleteMemo) {
            if(isNewMemo)
            {
                finish();
                overridePendingTransition(R.anim.slide_in_from_left, R.anim.slide_out_to_right);
            }
            else
            {
                DBManager.getManager(getApplicationContext()).deleteMemoInfo(mInfo);
                finish();
                overridePendingTransition(R.anim.slide_in_from_left, R.anim.slide_out_to_right);
            }
            return true;
        }
        else if(id == R.id.itCompleteMemo)
        {
            saveMemo();
            finish();
            overridePendingTransition(R.anim.slide_in_from_left, R.anim.slide_out_to_right);
        }
        else if(id == android.R.id.home)
        {
            finish();
            overridePendingTransition(R.anim.slide_in_from_left, R.anim.slide_out_to_right);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //Save to SQLite
        saveMemo();
        finish();
        overridePendingTransition(R.anim.slide_in_from_left, R.anim.slide_out_to_right);
    }

    private void saveMemo()
    {
        memoContents = etMemoEdit.getText().toString();
        Date now = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss E요일");
        String nowDate = sdf.format(now);
        if(isNewMemo)
        {
            if(memoContents.equals(""))
            {
                return;
            }
            MemoInfo newMemoInfo = new MemoInfo();
            newMemoInfo.contents = memoContents;
            newMemoInfo.regDate = nowDate;
            newMemoInfo.modDate = nowDate;
            DBManager.getManager(getApplicationContext()).insertMemoInfo(newMemoInfo);
        }
        else
        {
            if(mInfo.contents.equals(memoContents))
            {
                return;
            }

            mInfo.modDate = nowDate;
            mInfo.contents = memoContents;
            DBManager.getManager(getApplicationContext()).updateMemoInfo(mInfo);
        }
    }
}
