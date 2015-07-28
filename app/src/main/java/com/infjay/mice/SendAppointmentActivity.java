package com.infjay.mice;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.infjay.mice.artifacts.MyScheduleInfo;
import com.infjay.mice.database.DBManager;
import com.infjay.mice.database.MiceDB;

import org.w3c.dom.Text;

import java.util.Calendar;


public class SendAppointmentActivity extends CustomActionBarActivity implements View.OnClickListener{

    String targetName;
    String targetSeq;
    String fromActivity;
    String comment;
    TextView tvTarget;
    TextView tvAppointmentSendTime;
    EditText etComment;
    Button btSetAppointmentTime;
    String[] dateTime = {"",""};
    MyScheduleInfo scheduleInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_appointment);

        comment = "";

        Intent intent = getIntent();
        fromActivity = intent.getStringExtra("activity");
        if(fromActivity.equals("FindReceiver")) {
            targetName = intent.getStringExtra("userName");
            targetSeq = intent.getStringExtra("userSeq");
        }
        else{
            targetName = intent.getStringExtra("target");
            comment = intent.getStringExtra("comment");
        }
        scheduleInfo = new MyScheduleInfo();
        scheduleInfo.parterName = targetName;

        tvTarget = (TextView)findViewById(R.id.tvAppointmentTarget);
        tvAppointmentSendTime = (TextView)findViewById(R.id.tvAppointmentSendTIme);
        etComment = (EditText)findViewById(R.id.etAppointmentComment);
        btSetAppointmentTime = (Button)findViewById(R.id.btSetAppointmentTime);

        btSetAppointmentTime.setOnClickListener(this);

        etComment.setText(comment);
        tvTarget.setText(targetName);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_send_appointment, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.itCompleteAppointment) {
            if(dateTime[0].length()==0){
                Toast.makeText(getApplicationContext(), "Select Time When You want to meet!", Toast.LENGTH_SHORT).show();
            }
            else if(dateTime[1].length()==0){
                Toast.makeText(getApplicationContext(), "Select Date When You want to meet!", Toast.LENGTH_SHORT).show();
            }
            else {
                scheduleInfo.comment = etComment.getText().toString();
                scheduleInfo.time = tvAppointmentSendTime.getText().toString();

                DBManager.getManager(getApplicationContext()).insertSchedule(scheduleInfo);
                finish();
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btSetAppointmentTime){

            Calendar c = Calendar.getInstance();
            int mHour = c.get(Calendar.HOUR_OF_DAY);
            int mMinute = c.get(Calendar.MINUTE);
            int mYear = c.get(Calendar.YEAR);
            int mMonth = c.get(Calendar.MONTH);
            int mDay = c.get(Calendar.DAY_OF_MONTH);

            TimePickerDialog tpd = new TimePickerDialog(this,
                    new TimePickerDialog.OnTimeSetListener() {

                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay,
                                              int minute) {
                            // Display Selected time in textbox
                            dateTime[0] = (hourOfDay + ":" + minute);
                            tvAppointmentSendTime.setText(dateTime[1]+" "+dateTime[0]);
                        }
                    }, mHour, mMinute, false);
            tpd.show();

            DatePickerDialog dpd = new DatePickerDialog(this,
                    new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {
                            dateTime[1] = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth + " ";
                            tvAppointmentSendTime.setText(dateTime[1]+" "+dateTime[0]);
                        }
                    }, mYear, mMonth, mDay);
            dpd.show();


        }
    }
}
