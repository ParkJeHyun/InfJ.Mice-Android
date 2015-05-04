package kr.co.iekorea.mc;

import kr.co.iekorea.mc.event.MessageOutboxDetailEvent;
import kr.co.iekorea.mc.staticdata.StaticData;
import kr.co.iekorea.mc.util.BaseInterface;
import kr.co.iekorea.mc.util.ProcessManager;
import kr.co.iekorea.mc.util.WeakAsyncTask;
import kr.co.iekorea.mc.xml.MessageDAO;
import kr.co.iekorea.mc.xml.MessageDetailDto;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

public class MessageOutboxDetail_Activity extends Activity implements
		BaseInterface {
	// Activity control.
	private ProcessManager processManager;

	// layout area
	private ImageButton btn_back;
	private LinearLayout layout_header;
	private LinearLayout layout_progressbar;
	
	// other layout
	private TextView text_user_name;
	private TextView text_date;
	private TextView text_contents;
	private TextView text_message_title;
	private TextView text_message_type;
	
	// for contents
	private int message_id;
	private MessageDetailDto messageDetailDto;
	
	// 언어
	private TextView total_title;
	private TextView info_type,info_title,info_sender,text_wait;
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.initView();
		if (StaticData.logined) {
			this.getIntentValues();
			this.getXmlResources();
			this.modifyXmlResources();
			
			new setMessageOutBoxDetial(MessageOutboxDetail_Activity.this).execute();
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
	}
	
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		this.processManager.deleteActivity(this);
	}
	
	@Override
	public void onBackPressed() {
		Intent intent = new Intent(getApplicationContext(), MessageOutbox_Activity.class);
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
		this.setContentView(R.layout.activity_message_outbox_detail);
		this.processManager = ProcessManager.getInstance();
		this.processManager.addActivity(this);
		messageDetailDto = new MessageDetailDto();
	}

	@Override
	public void getXmlResources() {
		this.btn_back = (ImageButton) this.findViewById(R.id.btn_back);
		this.layout_header = (LinearLayout) this.findViewById(R.id.layout_header);
		this.layout_progressbar = (LinearLayout) this.findViewById(R.id.layout_progressbar);
		
		// orther layout
		this.text_user_name = (TextView) this.findViewById(R.id.text_user_name);
		this.text_date = (TextView) this.findViewById(R.id.text_date);
		this.text_contents = (TextView) this.findViewById(R.id.text_contents);
		this.text_message_title = (TextView) this.findViewById(R.id.text_message_title);
		this.text_message_type = (TextView) this.findViewById(R.id.text_message_type);
		
		// 언어
		this.total_title = (TextView) this.findViewById(R.id.total_title);
		this.info_type = (TextView) this.findViewById(R.id.info_type);
		this.info_title = (TextView) this.findViewById(R.id.info_title);
		this.info_sender = (TextView) this.findViewById(R.id.info_sender);
		this.text_wait = (TextView) this.findViewById(R.id.text_wait);
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
			this.total_title.setText(R.string.outbox_ko);
			this.info_type.setText(R.string.type_ko);
			this.info_title.setText(R.string.title_ko);
			this.info_sender.setText(R.string.sender_ko);
			this.text_wait.setText(R.string.wait_ko);
			break;
		case StaticData.ENGLISH:
			this.btn_back.setImageResource(R.drawable.b_back_en);
			this.total_title.setText(R.string.outbox_en);
			this.info_type.setText(R.string.type_en);
			this.info_title.setText(R.string.title_en);
			this.info_sender.setText(R.string.sender_en);
			this.text_wait.setText(R.string.wait_en);
			break;
		case StaticData.CHINA:
			this.btn_back.setImageResource(R.drawable.b_back_cn);
			this.total_title.setText(R.string.outbox_cn);
			this.info_type.setText(R.string.type_cn);
			this.info_title.setText(R.string.title_cn);
			this.info_sender.setText(R.string.sender_cn);
			this.text_wait.setText(R.string.wait_cn);
			break;
		}
	}
	
	public void setUpdateUI(){
		this.text_user_name.setText(messageDetailDto.TO_USER_NAME);
		this.text_date.setText(messageDetailDto.REG_DATE);
		this.text_contents.setText(messageDetailDto.CONTENTS);
		this.text_message_title.setText(messageDetailDto.TITLE);
		this.text_message_type.setText(messageDetailDto.MESSAGE_TYPE);
	}

	@Override
	public void setEventListener() {
		this.btn_back.setOnClickListener(new MessageOutboxDetailEvent(this,this));

	}
	
	class setMessageOutBoxDetial extends WeakAsyncTask<Void, Void, Void,Activity>{
		public setMessageOutBoxDetial(Activity target) {
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
			MessageDAO dao = new MessageDAO(MessageOutboxDetail_Activity.this);
			try {
				result = dao.setMessageOutboxDetail(message_id);
				
				if(result){
					messageDetailDto = dao.getMessageOutboxDetail();
				}
				
			} catch (Exception e) {
				result = false;
			}
			return null;
		}
		
		@Override
		protected void onPostExecute(Activity target,Void result) {
			if(this.result){
				// up date ui
				setUpdateUI();
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
			setEventListener();
			layout_progressbar.setVisibility(LinearLayout.GONE);
		}
	}
}
