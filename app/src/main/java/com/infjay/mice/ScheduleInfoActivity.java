package com.infjay.mice;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.infjay.mice.artifacts.MyScheduleInfo;
import com.infjay.mice.database.DBManager;

/**
 * Created by HJHOME on 2015-06-07.
 */
public class ScheduleInfoActivity extends CustomActionBarActivity{
    private int scheduleSeq;
    private MyScheduleInfo scheduleInfo;
    private TextView tvName;
    private TextView tvTime;
    private TextView tvComment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_info);

        Intent intent = getIntent();
        scheduleSeq = intent.getExtras().getInt("scheduleSeq");

        scheduleInfo = DBManager.getManager(getApplicationContext()).getScheduleBySeq(scheduleSeq);
        setTextView();
    }

    public void setTextView(){
        tvName = (TextView)findViewById(R.id.tvScheduleInfoName);
        tvTime = (TextView)findViewById(R.id.tvScheduleInfoTime);
        tvComment = (TextView)findViewById(R.id.tvScheduleInfoComment);

        tvName.setText(scheduleInfo.parterName);
        tvTime.setText(scheduleInfo.time);
        tvComment.setText(scheduleInfo.comment);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_schedule_info, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.itDeleteSchedule) {
            DBManager.getManager(getApplicationContext()).deleteScheduleBySeq(scheduleSeq);
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
