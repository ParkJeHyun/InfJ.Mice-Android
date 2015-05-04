package kr.co.iekorea.mc.event;

import java.util.Calendar;

import kr.co.iekorea.mc.MessageAppointmentDetailAdd_Activity;
import kr.co.iekorea.mc.MessageAppointment_Activity;
import kr.co.iekorea.mc.MessageAppointmentsDetail_Activity;
import kr.co.iekorea.mc.R;
import kr.co.iekorea.mc.datetime.DateTimePicker;
import kr.co.iekorea.mc.staticdata.StaticData;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MessageAppointmentDetailEventListener implements OnClickListener {
	public Context ctx;
	public Activity activity;
	public Intent intent;
	
	private static final int REQUEST_CODE_2 = 02;
	
	public MessageAppointmentDetailEventListener(Context ctx, Activity activity){
		this.ctx = ctx;
		this.activity = activity;
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.btn_appointments_add:
			intent = new Intent(ctx, MessageAppointmentDetailAdd_Activity.class);
			activity.startActivityForResult(intent, REQUEST_CODE_2);
			activity.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
			break;
			
		case R.id.btn_back:
			intent = new Intent(ctx, MessageAppointment_Activity.class);
			ctx.startActivity(intent);
			activity.finish();
			break;
			
			/** time dialog show area */
		case R.id.btn_appointment_time_add:						
			this.showDateTimeDialog();
			break;
		}

	}
	
	private void showDateTimeDialog() {
		// Create the dialog
		final Dialog mDateTimeDialog = new Dialog(ctx);
		// Inflate the root layout
		final RelativeLayout mDateTimeDialogView = (RelativeLayout) activity.getLayoutInflater().inflate(R.layout.date_time_dialog, null);
		// Grab widget instance
		final DateTimePicker mDateTimePicker = (DateTimePicker) mDateTimeDialogView.findViewById(R.id.DateTimePicker);
		// Check is system is set to use 24h time (this doesn't seem to work as expected though)
		final String timeS = android.provider.Settings.System.getString(activity.getContentResolver(), android.provider.Settings.System.TIME_12_24);
		final boolean is24h = !(timeS == null || timeS.equals("12"));
		// --------------------------------------------
		Button setDateTime = (Button) mDateTimeDialogView.findViewById(R.id.SetDateTime);
		Button CancelDialog = (Button) mDateTimeDialogView.findViewById(R.id.CancelDialog);
		Button ResetDateTime = (Button) mDateTimeDialogView.findViewById(R.id.ResetDateTime);
		
		switch (StaticData.NOWLANGUAGE) {
		case StaticData.KOREA:
			setDateTime.setText(R.string.confirm_ko);
			CancelDialog.setText(R.string.cancel_ko);
//			ResetDateTime.setText(R.string.confirm_ko);
			break;
		case StaticData.ENGLISH:
			setDateTime.setText(R.string.confirm_en);
			CancelDialog.setText(R.string.cancel_en);
//			ResetDateTime.setText(R.string.confirm_ko);
			break;
		case StaticData.CHINA:
			setDateTime.setText(R.string.confirm_cn);
			CancelDialog.setText(R.string.cancel_cn);
//			ResetDateTime.setText(R.string.confirm_ko);
			break;
		}
		// --------------------------------------------
		
		// Update demo TextViews when the "OK" button is clicked 
		setDateTime.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				mDateTimePicker.clearFocus();
				// TODO Auto-generated method stub
				((MessageAppointmentsDetail_Activity)activity).promise_date = "";
				((MessageAppointmentsDetail_Activity)activity).promise_date = mDateTimePicker.get(Calendar.YEAR) + "-" + (mDateTimePicker.get(Calendar.MONTH)+1) + "-"
						+ mDateTimePicker.get(Calendar.DAY_OF_MONTH);
				String tempTime="";
				if(mDateTimePicker.get(Calendar.MINUTE) <10){
					tempTime = "0"+mDateTimePicker.get(Calendar.MINUTE);
				}else{
					tempTime = mDateTimePicker.get(Calendar.MINUTE)+"";
				}
				((MessageAppointmentsDetail_Activity)activity).promise_hour = "";
				((MessageAppointmentsDetail_Activity)activity).promise_hour = mDateTimePicker.get(Calendar.HOUR_OF_DAY) + tempTime+"";
				
				((TextView) activity.findViewById(R.id.text_appointment_date)).setText(mDateTimePicker.get(Calendar.YEAR) + "/" + (mDateTimePicker.get(Calendar.MONTH)+1) + "/"
						+ mDateTimePicker.get(Calendar.DAY_OF_MONTH));
				if (mDateTimePicker.is24HourView()) {
					((TextView) activity.findViewById(R.id.text_appointment_time)).setText(mDateTimePicker.get(Calendar.HOUR_OF_DAY) + ":" + tempTime);
				} else {
					((TextView) activity.findViewById(R.id.text_appointment_time)).setText(mDateTimePicker.get(Calendar.HOUR) + ":" + tempTime + " "
							+ (mDateTimePicker.get(Calendar.AM_PM) == Calendar.AM ? "AM" : "PM"));
				}
				mDateTimeDialog.dismiss();
			}
		});

		// Cancel the dialog when the "Cancel" button is clicked
		CancelDialog.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				mDateTimeDialog.cancel();
			}
		});

		// Reset Date and Time pickers when the "Reset" button is clicked
		ResetDateTime.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				mDateTimePicker.reset();
			}
		});
		
		// Setup TimePicker
		mDateTimePicker.setIs24HourView(is24h);
		// No title on the dialog window
		mDateTimeDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		// Set the dialog content view
		mDateTimeDialog.setContentView(mDateTimeDialogView);
		// Display the dialog
		mDateTimeDialog.show();
	}

}
