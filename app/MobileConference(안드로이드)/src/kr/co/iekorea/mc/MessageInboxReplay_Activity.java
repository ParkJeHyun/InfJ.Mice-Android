package kr.co.iekorea.mc;

import kr.co.iekorea.mc.event.MessageInboxReplayEventListener;
import kr.co.iekorea.mc.staticdata.StaticData;
import kr.co.iekorea.mc.util.BaseInterface;
import kr.co.iekorea.mc.util.ProcessManager;
import kr.co.iekorea.mc.util.WeakAsyncTask;
import kr.co.iekorea.mc.xml.MessageDAO;
import kr.co.iekorea.mc.xml.MessageDetailDto;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

public class MessageInboxReplay_Activity extends Activity implements BaseInterface {
	// Activity control.
	private ProcessManager processManager;

	// layout area
	private ImageButton btn_back;
	private LinearLayout layout_header;
	private LinearLayout layout_progressbar;
	
	public int message_id;
	
	// detail layout resource
	private EditText edit_input_user;								// 참가자 이름 에디
	private EditText edit_appointment_title;							// 약속 타이틀
	private EditText edit_appointment_contents;						// 약속 내용
//	private TextView text_appointment_time;							// 약속 시간
//	private TextView text_appointment_date;							// 약속 날짜
	private ImageButton btn_replay_check;
	private String user_name;
	private String message_type;
	private int to_user_cd;
	private MessageDetailDto messageDetailDto;
	
	// 언어
	private TextView info_replay_taget,text_wait,total_title;
	
	// 쪽지,QnA,질문
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.initView();
		if (StaticData.logined) {
			this.getIntentValues();
			this.getXmlResources();
			this.modifyXmlResources();
			this.setUpdateUI();
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
	
	
	public void getIntentValues(){
		Intent intent = getIntent();
		this.message_id = intent.getIntExtra("message_id", 0);
		this.user_name = intent.getStringExtra("user_name");
		this.message_type = intent.getStringExtra("message_type");
		this.messageDetailDto = intent.getParcelableExtra("messageDetailDto");
	}
	
	public void setUpdateUI(){
		this.edit_input_user.setText(user_name);
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		this.processManager.deleteActivity(this);
	}
	
	@Override
	public void onBackPressed() {
		overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
		finish();
	}
	
	@Override
	public void finish() {
		super.finish();
		overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
	}

	@Override
	public void initView() {
		this.setContentView(R.layout.activity_message_inbox_replay);
		this.processManager = ProcessManager.getInstance();
		this.processManager.addActivity(this);
	}

	@Override
	public void getXmlResources() {
		this.btn_back = (ImageButton) this.findViewById(R.id.btn_back);
		this.layout_header = (LinearLayout) this.findViewById(R.id.layout_header);
		this.layout_progressbar = (LinearLayout) this.findViewById(R.id.layout_progressbar);
		
		this.edit_input_user = (EditText) this.findViewById(R.id.edit_input_user);
		this.edit_appointment_title = (EditText) this.findViewById(R.id.edit_appointment_title);
		this.edit_appointment_contents = (EditText) this.findViewById(R.id.edit_appointment_contents);
		this.btn_replay_check = (ImageButton) this.findViewById(R.id.btn_replay_check);
		
		this.info_replay_taget = (TextView) this.findViewById(R.id.info_replay_taget);
		this.text_wait = (TextView) this.findViewById(R.id.text_wait);
		this.total_title = (TextView) this.findViewById(R.id.total_title);
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
			this.btn_replay_check.setImageResource(R.drawable.b_save_kr);
			this.info_replay_taget.setText(R.string.recive_ko);
			this.total_title.setText(R.string.inbox_ko);
			this.text_wait.setText(R.string.wait_ko);
			this.edit_appointment_title.setHint(R.string.please_enter_the_title_ko);
			this.edit_appointment_contents.setHint(R.string.please_enter_the_contents_ko);
			break;
		case StaticData.ENGLISH:
			this.btn_back.setImageResource(R.drawable.btn_back_en);
			this.btn_replay_check.setImageResource(R.drawable.b_save_en);
			this.info_replay_taget.setText(R.string.recive_en);
			this.total_title.setText(R.string.inbox_en);
			this.text_wait.setText(R.string.wait_en);
			this.edit_appointment_title.setHint(R.string.please_enter_the_title_en);
			this.edit_appointment_contents.setHint(R.string.please_enter_the_contents_en);
			break;
		case StaticData.CHINA:
			this.btn_back.setImageResource(R.drawable.btn_back_cn);
			this.btn_replay_check.setImageResource(R.drawable.b_save_cn);
			this.info_replay_taget.setText(R.string.recive_cn);
			this.total_title.setText(R.string.inbox_cn);
			this.text_wait.setText(R.string.wait_cn);
			this.edit_appointment_title.setHint(R.string.please_enter_the_title_cn);
			this.edit_appointment_contents.setHint(R.string.please_enter_the_contents_cn);
			break;
		}
	}

	@Override
	public void setEventListener() {
		this.btn_back.setOnClickListener(new MessageInboxReplayEventListener(this, this));
		this.btn_replay_check.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(edit_appointment_title.getText().toString().equals("")){
					switch (StaticData.NOWLANGUAGE) {
					case StaticData.KOREA:
					Toast.makeText(getApplicationContext(),
					R.string.please_enter_the_title_ko, Toast.LENGTH_SHORT).show();
					break;
					case StaticData.ENGLISH:
					Toast.makeText(getApplicationContext(),
					R.string.please_enter_the_title_en, Toast.LENGTH_SHORT).show();
					break;
					case StaticData.CHINA:
					Toast.makeText(getApplicationContext(),
					R.string.please_enter_the_title_cn, Toast.LENGTH_SHORT).show();
					break;
					}
					
				}else if(edit_appointment_contents.getText().toString().equals("")){
					switch (StaticData.NOWLANGUAGE) {
					case StaticData.KOREA:
					Toast.makeText(getApplicationContext(),
					R.string.please_enter_the_contents_ko, Toast.LENGTH_SHORT).show();
					break;
					case StaticData.ENGLISH:
					Toast.makeText(getApplicationContext(),
					R.string.please_enter_the_contents_en, Toast.LENGTH_SHORT).show();
					break;
					case StaticData.CHINA:
					Toast.makeText(getApplicationContext(),
					R.string.please_enter_the_contents_cn, Toast.LENGTH_SHORT).show();
					break;
					}
				}else{
					new setMessageReplay(MessageInboxReplay_Activity.this).execute();
				}
			}
		});
	}
	
	
	// get values
	class setMessageReplay extends WeakAsyncTask<Void, Void, Void,Activity>{
		public setMessageReplay(Activity target) {
			super(target);
			// TODO Auto-generated constructor stub
		}

		boolean result;

		@Override
		protected void onPreExecute(Activity target) {
			layout_progressbar.setVisibility(LinearLayout.VISIBLE);
		}
		
		@Override
		protected Void doInBackground(Activity target,Void... params) {
			MessageDAO dao = new MessageDAO(MessageInboxReplay_Activity.this);
			if(messageDetailDto.MESSAGE_TYPE.equals("쪽지")){
				try {
					result = dao.setSendMessage(messageDetailDto.FROM_USER_CD, StaticData.USER_CD,
							edit_appointment_contents.getText().toString(),
							edit_appointment_title.getText().toString(),messageDetailDto.MESSAGE_ID);
				} catch (Exception e) {
					result = false;
				}
			}else if(messageDetailDto.MESSAGE_TYPE.equals("QnA")){
				try {
					result = dao.setSendQnA(messageDetailDto.FROM_USER_CD, StaticData.USER_CD,
							edit_appointment_contents.getText().toString(),
							edit_appointment_title.getText().toString(),messageDetailDto.MESSAGE_ID);
				} catch (Exception e) {
					result = false;
				}
			}else if(messageDetailDto.MESSAGE_TYPE.equals("질문")){
				try {
					result = dao.setSendQuestion(messageDetailDto.FROM_USER_CD, StaticData.USER_CD,
							edit_appointment_contents.getText().toString(),
							edit_appointment_title.getText().toString(),messageDetailDto.MESSAGE_ID);
				} catch (Exception e) {
					result = false;
				}
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

				overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
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
			setEventListener();
		}
		
	}
}
