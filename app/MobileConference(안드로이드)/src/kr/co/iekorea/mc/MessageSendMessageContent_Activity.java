package kr.co.iekorea.mc;

import java.util.ArrayList;

import kr.co.iekorea.mc.event.MessageSendMessageContentEventListener;
import kr.co.iekorea.mc.staticdata.StaticData;
import kr.co.iekorea.mc.util.BaseInterface;
import kr.co.iekorea.mc.util.ProcessManager;
import kr.co.iekorea.mc.util.WeakAsyncTask;
import kr.co.iekorea.mc.xml.InputUserDto;
import kr.co.iekorea.mc.xml.MessageDAO;
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

public class MessageSendMessageContent_Activity extends Activity implements
		BaseInterface {

	// Activity control.
	private ProcessManager processManager;

	// layout area
	private ImageButton btn_back;
	private LinearLayout layout_header;
	private LinearLayout layout_progressbar;
	
	// detail layout resource
	private EditText edit_send_message_username;							// 쪽지 대상 유저 이름
	private EditText edit_send_message_content;							// 쪽지 내용
	private ImageButton btn_send_message;								// 쪽지 보내기 버튼
	private int USER_CD;													// 쪽지 대상 CD
	private String USER_NAME;											// 쪽지 대상 이름
	private EditText edit_send_message_title;							// 쪽지 타이틀
	
	private ArrayList<InputUserDto> userDtoList;
	
	// 언어
	private TextView total_title,text_wait,info_title;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.initView();
		if (StaticData.logined) {
			this.getIntentValues();
			this.getXmlResources();
			this.modifyXmlResources();
			this.setIntentValues();
			
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
	
	public void setIntentValues(){
		this.edit_send_message_username.setText(this.USER_NAME);
	}
	
	public void getIntentValues(){
		Intent intent = getIntent();
		this.USER_CD = intent.getIntExtra("user_cd", 0);
		this.USER_NAME = intent.getStringExtra("user_name");
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		this.processManager.deleteActivity(this);
	}
	
	@Override
	public void onBackPressed() {
		Intent intent = new Intent(this, MessageSendMessage_Activity.class);
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
		this.setContentView(R.layout.activity_message_send_message_content);
		this.processManager = ProcessManager.getInstance();
		this.processManager.addActivity(this);
		this.userDtoList = new ArrayList<InputUserDto>();
	}

	@Override
	public void getXmlResources() {
		this.btn_back = (ImageButton) this.findViewById(R.id.btn_back);
		this.layout_header = (LinearLayout) this.findViewById(R.id.layout_header);
		this.layout_progressbar = (LinearLayout) this.findViewById(R.id.layout_progressbar);
		
		this.edit_send_message_username = (EditText) this.findViewById(R.id.edit_send_message_username);
		this.edit_send_message_content = (EditText) this.findViewById(R.id.edit_send_message_content);
		this.btn_send_message = (ImageButton) this.findViewById(R.id.btn_send_message);
		this.edit_send_message_title = (EditText) this.findViewById(R.id.edit_send_message_title);
		
		this.total_title = (TextView) this.findViewById(R.id.total_title);
		this.text_wait = (TextView) this.findViewById(R.id.text_wait);
		this.info_title = (TextView) this.findViewById(R.id.info_title);
	}

	@Override
	public void modifyXmlResources() {
		LayoutParams param;
        param = new LayoutParams(LayoutParams.MATCH_PARENT,StaticData.device_Height/10);
        this.layout_header.setLayoutParams(param);

        // 언어 설정에 다른 화면 구성
		switch (StaticData.NOWLANGUAGE) {
		case StaticData.KOREA:
			this.btn_back.setImageResource(R.drawable.b_back_kr);
			this.btn_send_message.setImageResource(R.drawable.b_send_kr);
			this.total_title.setText(R.string.question_ko);
			this.info_title.setText(R.string.title_ko);
			this.text_wait.setText(R.string.wait_ko);
			this.edit_send_message_title.setHint(R.string.please_enter_the_title_ko);
			this.edit_send_message_content.setHint(R.string.please_enter_the_contents_ko);
			break;
		case StaticData.ENGLISH:
			this.btn_back.setImageResource(R.drawable.b_back_en);
			this.btn_send_message.setImageResource(R.drawable.b_send_en);
			this.total_title.setText(R.string.question_en);
			this.edit_send_message_title.setHint(R.string.please_enter_the_title_en);
			this.edit_send_message_content.setHint(R.string.please_enter_the_contents_en);
			this.text_wait.setText(R.string.wait_en);
			this.info_title.setText(R.string.title_en);
			break;
		case StaticData.CHINA:
			this.btn_back.setImageResource(R.drawable.b_back_cn);
			this.btn_send_message.setImageResource(R.drawable.b_send_cn);
			this.total_title.setText(R.string.question_cn);
			this.edit_send_message_title.setHint(R.string.please_enter_the_title_cn);
			this.edit_send_message_content.setHint(R.string.please_enter_the_contents_cn);
			this.text_wait.setText(R.string.wait_cn);
			this.info_title.setText(R.string.title_cn);
			break;
		}
	}

	@Override
	public void setEventListener() {
		this.btn_back.setOnClickListener(new MessageSendMessageContentEventListener(this,this));
		this.btn_send_message.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(edit_send_message_content.getText().toString().equals("") ){
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
				}else if(edit_send_message_title.getText().toString().equals("")){
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
				}else{
					new setSendMessage(MessageSendMessageContent_Activity.this).execute();
				}
			}
		});
	}
	
	class setSendMessage extends WeakAsyncTask<Void, Void, Void,Activity>{
		public setSendMessage(Activity target) {
			super(target);
			// TODO Auto-generated constructor stub
		}

		boolean result = false;
		boolean check = false;

		@Override
		protected void onPreExecute(Activity target) {
			layout_progressbar.setVisibility(LinearLayout.VISIBLE);
		}

		@Override
		protected Void doInBackground(Activity target,Void... params) {
			MessageDAO dao = new MessageDAO(MessageSendMessageContent_Activity.this);
			try {
				result = dao.setSendMessage(USER_CD, StaticData.USER_CD,edit_send_message_content.getText().toString()
						, edit_send_message_title.getText().toString());
			} catch (Exception e) {
				result = false;
			}
			return null;
		}

		@Override
		protected void onPostExecute(Activity target,Void param) {
			layout_progressbar.setVisibility(LinearLayout.GONE);
			if (result) {
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

				// go message send message activity
				Intent intent = new Intent(getApplicationContext(), MessageSendMessage_Activity.class);
				startActivity(intent);
				overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
				finish();
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

			}
			layout_progressbar.setVisibility(LinearLayout.GONE);
		}
	}
	
}
