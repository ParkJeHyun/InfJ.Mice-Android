package kr.co.iekorea.mc;

import java.util.ArrayList;

import kr.co.iekorea.mc.event.MessageAppointmentDetailEventListener;
import kr.co.iekorea.mc.staticdata.StaticData;
import kr.co.iekorea.mc.util.BaseInterface;
import kr.co.iekorea.mc.util.ProcessManager;
import kr.co.iekorea.mc.util.WeakAsyncTask;
import kr.co.iekorea.mc.xml.InputUserDto;
import kr.co.iekorea.mc.xml.MessageDAO;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

public class MessageAppointmentsDetail_Activity extends Activity implements
		BaseInterface {

	// Activity control.
	private ProcessManager processManager;

	// layout area
	private ImageButton btn_back;
	private LinearLayout layout_header;
	private LinearLayout layout_progressbar;
	
	// detail layout resource
	private EditText edit_input_user;								// 참가자 이름 에디
	private ImageButton btn_appointments_add;						// 참가자 추가 버튼
	private EditText edit_appointment_title;							// 약속 타이틀
	private EditText edit_appointment_contents;						// 약속 내용
	private TextView text_appointment_time;							// 약속 시간
	private TextView text_appointment_date;							// 약속 날짜
	private ImageButton  btn_appointment_time_add;					// 약속 시간 추가 버튼
	private ImageButton btn_apointments_check;
	public String to_user_cd="";
	public String promise_date=""; 
	public String promise_hour="";
	
	public ArrayList<InputUserDto> userDtoList;
	
	// 언어
	public TextView total_title,info_attendant,text_wait;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.initView();
		if (StaticData.logined) {
			this.getXmlResources();
			this.modifyXmlResources();
			
			this.setEventListener();
		} else {
			switch (StaticData.NOWLANGUAGE) {
			case StaticData.KOREA:
			Toast.makeText(getApplicationContext(),
			R.string.please_retry_ko, Toast.LENGTH_SHORT).show();
			break;
			case StaticData.ENGLISH:
			Toast.makeText(getApplicationContext(),
			R.string.please_retry_en, Toast.LENGTH_SHORT).show();
			break;
			case StaticData.CHINA:
			Toast.makeText(getApplicationContext(),
			R.string.please_retry_cn, Toast.LENGTH_SHORT).show();
			break;
			}

			Intent intent = new Intent(getApplicationContext(),Login_Activity.class);
			startActivity(intent);
			overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
			finish();
		}
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		this.processManager.deleteActivity(this);
	}
	
	@Override
	public void onBackPressed() {
		Intent intent = new Intent(this, MessageAppointment_Activity.class);
		startActivity(intent);
		finish();
	}
	
	@Override
	public void finish() {
		super.finish();
		overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
	}

	@Override
	public void initView() {
		this.setContentView(R.layout.activity_message_appointment_detail);
		this.processManager = ProcessManager.getInstance();
		this.processManager.addActivity(this);
		this.userDtoList = new ArrayList<InputUserDto>();
	}

	@Override
	public void getXmlResources() {
		this.btn_back = (ImageButton) this.findViewById(R.id.btn_back);
		this.layout_header = (LinearLayout) this.findViewById(R.id.layout_header);
		this.layout_progressbar = (LinearLayout) this.findViewById(R.id.layout_progressbar);
		
		this.edit_input_user = (EditText) this.findViewById(R.id.edit_input_user);
		this.btn_appointments_add = (ImageButton) this.findViewById(R.id.btn_appointments_add);
		this.edit_appointment_title = (EditText) this.findViewById(R.id.edit_appointment_title);
		this.edit_appointment_contents = (EditText) this.findViewById(R.id.edit_appointment_contents);
		this.text_appointment_time = (TextView) this.findViewById(R.id.text_appointment_time);
		this.text_appointment_date = (TextView) this.findViewById(R.id.text_appointment_date);
		this.btn_appointment_time_add = (ImageButton) this.findViewById(R.id.btn_appointment_time_add);
		this.btn_apointments_check = (ImageButton) this.findViewById(R.id.btn_apointments_check);
		
		// 언어
		this.total_title = (TextView) this.findViewById(R.id.total_title);
		this.text_wait = (TextView) this.findViewById(R.id.text_wait);
		this.info_attendant = (TextView) this.findViewById(R.id.info_attendant);
	}

	@Override
	public void modifyXmlResources() {
		LayoutParams param;
        param = new LayoutParams(LayoutParams.MATCH_PARENT,StaticData.device_Height/10);
        this.layout_header.setLayoutParams(param);
        
        // 언어 설정에 다른 화면 구성
		switch (StaticData.NOWLANGUAGE) {
		case StaticData.KOREA:
			this.btn_back.setImageResource(R.drawable.btn_back_kr);
			this.btn_appointments_add.setImageResource(R.drawable.btn_add_kr);
			this.btn_appointment_time_add.setImageResource(R.drawable.btn_selecttime_kr);
			this.btn_apointments_check.setImageResource(R.drawable.b_save_kr);
			this.total_title.setText(getResources().getString(R.string.appointments_ko));
			this.text_wait.setText(getResources().getString(R.string.wait_ko));
			this.info_attendant.setText(getResources().getString(R.string.attendant_ko));
			this.text_appointment_date.setHint(R.string.appointments_date_ko);
			this.text_appointment_time.setHint(R.string.appointments_time_ko);
			this.edit_appointment_title.setHint(R.string.please_enter_the_title_ko);
			this.edit_appointment_contents.setHint(R.string.please_enter_the_contents_ko);
			break;
		case StaticData.ENGLISH:
			this.btn_back.setImageResource(R.drawable.btn_back_en);
			this.btn_appointments_add.setImageResource(R.drawable.btn_add_en);
			this.btn_appointment_time_add.setImageResource(R.drawable.btn_selecttime_en);
			this.btn_apointments_check.setImageResource(R.drawable.b_save_en);
			this.total_title.setText(getResources().getString(R.string.appointments_en));
			this.info_attendant.setText(getResources().getString(R.string.attendant_en));
			this.text_appointment_date.setHint(R.string.appointments_date_en);
			this.text_appointment_time.setHint(R.string.appointments_time_en);
			this.edit_appointment_title.setHint(R.string.please_enter_the_title_en);
			this.edit_appointment_contents.setHint(R.string.please_enter_the_contents_en);
			this.text_wait.setText(getResources().getString(R.string.wait_en));
			break;
		case StaticData.CHINA:
			this.btn_back.setImageResource(R.drawable.btn_back_cn);
			this.btn_appointments_add.setImageResource(R.drawable.btn_add_cn);
			this.btn_appointment_time_add.setImageResource(R.drawable.btn_selecttime_cn);
			this.btn_apointments_check.setImageResource(R.drawable.b_save_cn);
			this.total_title.setText(getResources().getString(R.string.appointments_cn));
			this.info_attendant.setText(getResources().getString(R.string.attendant_cn));
			this.text_appointment_date.setHint(R.string.appointments_date_cn);
			this.text_appointment_time.setHint(R.string.appointments_time_cn);
			this.edit_appointment_title.setHint(R.string.please_enter_the_title_cn);
			this.edit_appointment_contents.setHint(R.string.please_enter_the_contents_cn);
			this.text_wait.setText(getResources().getString(R.string.wait_cn));
			break;
		}
	}

	@Override
	public void setEventListener() {
		this.btn_appointments_add.setOnClickListener(new MessageAppointmentDetailEventListener(this,this));
		this.btn_back.setOnClickListener(new MessageAppointmentDetailEventListener(this,this));
		this.btn_appointment_time_add.setOnClickListener(new MessageAppointmentDetailEventListener(this,this));
		
		this.btn_apointments_check.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(edit_appointment_title.getText().toString().equals("")){
					switch (StaticData.NOWLANGUAGE) {
					case StaticData.KOREA:
						Toast.makeText(getApplicationContext(), R.string.please_enter_the_title_ko,	 Toast.LENGTH_SHORT).show();
						break;
					case StaticData.ENGLISH:
						Toast.makeText(getApplicationContext(), R.string.please_enter_the_title_en,	 Toast.LENGTH_SHORT).show();
						break;
					case StaticData.CHINA:
						Toast.makeText(getApplicationContext(), R.string.please_enter_the_title_cn,	 Toast.LENGTH_SHORT).show();
						break;
					}
				}else if(edit_appointment_contents.getText().toString().equals("")){
					switch (StaticData.NOWLANGUAGE) {
					case StaticData.KOREA:
						Toast.makeText(getApplicationContext(), R.string.please_enter_the_contents_ko,	 Toast.LENGTH_SHORT).show();
						break;
					case StaticData.ENGLISH:
						Toast.makeText(getApplicationContext(), R.string.please_enter_the_contents_en,	 Toast.LENGTH_SHORT).show();
						break;
					case StaticData.CHINA:
						Toast.makeText(getApplicationContext(), R.string.please_enter_the_contents_en,	 Toast.LENGTH_SHORT).show();
						break;
					}
				}else if(promise_date.equals("")
						|| promise_hour.equals("")){
					switch (StaticData.NOWLANGUAGE) {
					case StaticData.KOREA:
						Toast.makeText(getApplicationContext(), R.string.please_enter_the_times_ko,	 Toast.LENGTH_SHORT).show();
						break;
					case StaticData.ENGLISH:
						Toast.makeText(getApplicationContext(), R.string.please_enter_the_times_en,	 Toast.LENGTH_SHORT).show();
						break;
					case StaticData.CHINA:
						Toast.makeText(getApplicationContext(), R.string.please_enter_the_times_cn,	 Toast.LENGTH_SHORT).show();
						break;
					}
				}else if(edit_input_user.getText().toString().equals("")){
				}else{
					new setAppointmentAdd(MessageAppointmentsDetail_Activity.this).execute();
				}
				
			}
		});
	}
	
	public void setUserInputText(){
		Log.e("Result", "setUserInputText");
		this.edit_input_user.setText("");
		StringBuffer tempNames = new StringBuffer();
		for(int i =0; i<userDtoList.size(); i++){
			tempNames.append(userDtoList.get(i).USER_NAME);
			if(i != userDtoList.size()-1){
				tempNames.append(",");
			}
		}
		this.edit_input_user.setText(tempNames.toString());
	}
	
	public void makePromiseUser(){
		to_user_cd = "";
		StringBuffer temp = new StringBuffer();
		for(int i=0; i<userDtoList.size(); i++){
			temp.append(userDtoList.get(i).USER_CD+"");
			if(i != userDtoList.size()-1){
				temp.append(",");
			}
		}
		to_user_cd = temp.toString();
		
		int test = Integer.parseInt(promise_hour);
		promise_hour = pad(test);
	}
	
	private static String pad(int c) {
		if (c >= 1000) {
			return String.valueOf(c);
		} else
			return "0" + String.valueOf(c);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Log.e("Result", "onActivityResult");
		if(resultCode == Activity.RESULT_OK){
			InputUserDto dto = new InputUserDto();
			dto.USER_CD = data.getIntExtra("user_cd", 0);
			dto.USER_NAME = data.getStringExtra("user_name");
			Log.e("Result", "user_cd : "+dto.USER_CD);
			Log.e("Result", "user_name : "+dto.USER_NAME);
			
			if(dto.USER_CD != 0){
				userDtoList.add(dto);
			}
			this.setUserInputText();
		}
	}

	// appointments add area
	class setAppointmentAdd extends WeakAsyncTask<Void, Void, Void,Activity>{
		public setAppointmentAdd(Activity target) {
			super(target);
			// TODO Auto-generated constructor stub
		}

		boolean result;
		@Override
		protected void onPreExecute(Activity target) {
			layout_progressbar.setVisibility(LinearLayout.VISIBLE);
			makePromiseUser();
		}
		
		@Override
		protected Void doInBackground(Activity target,Void... params) {
			MessageDAO dao = new MessageDAO(MessageAppointmentsDetail_Activity.this);
			try {
				result = dao.setAppointmentsAdd(
						to_user_cd,promise_date,promise_hour,
						edit_appointment_title.getText().toString()
						,edit_appointment_contents.getText().toString());
			} catch (Exception e) {
				result = false;
			}
			return null;
		}
		
		@Override
		protected void onPostExecute(Activity target,Void result) {
			if(this.result){
				switch (StaticData.NOWLANGUAGE) {
				case StaticData.KOREA:
				Toast.makeText(getApplicationContext(),
				R.string.success_ko, Toast.LENGTH_SHORT).show();
				break;
				case StaticData.ENGLISH:
				Toast.makeText(getApplicationContext(),
				R.string.success_en, Toast.LENGTH_SHORT).show();
				break;
				case StaticData.CHINA:
				Toast.makeText(getApplicationContext(),
				R.string.success_cn, Toast.LENGTH_SHORT).show();
				break;
				}

				Intent intent = new Intent(getApplicationContext(), MessageAppointment_Activity.class);
				startActivity(intent);
				finish();
			}else{
				switch (StaticData.NOWLANGUAGE) {
				case StaticData.KOREA:
				Toast.makeText(getApplicationContext(),
				R.string.please_retry_ko, Toast.LENGTH_SHORT).show();
				break;
				case StaticData.ENGLISH:
				Toast.makeText(getApplicationContext(),
				R.string.please_retry_en, Toast.LENGTH_SHORT).show();
				break;
				case StaticData.CHINA:
				Toast.makeText(getApplicationContext(),
				R.string.please_retry_cn, Toast.LENGTH_SHORT).show();
				break;
				}

			}
			layout_progressbar.setVisibility(LinearLayout.VISIBLE);
		}
	}
}
