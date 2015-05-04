package kr.co.iekorea.mc;

import java.util.ArrayList;

import kr.co.iekorea.mc.staticdata.StaticData;
import kr.co.iekorea.mc.util.BaseInterface;
import kr.co.iekorea.mc.util.ProcessManager;
import kr.co.iekorea.mc.util.WeakAsyncTask;
import kr.co.iekorea.mc.xml.LoginDto;
import kr.co.iekorea.mc.xml.SecurityCodeDao;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

public class MyBriefcaseBusinesscardHolderSecurity_Activity extends Activity
		implements BaseInterface {
	// Activity control.
	private ProcessManager processManager;

	// layout area
	private ImageButton btn_back;
	private LinearLayout layout_header;
	private LinearLayout layout_progressbar;
	
	// for login
	private String user_id;
	private SharedPreferences pref;
	private ArrayList<LoginDto> loginList = new ArrayList<LoginDto>();
	private EditText edit_security_code;
	
	// button 
	private ImageButton btn_security_check;
	
	// 언어
	private TextView total_title;
	private TextView text_wait;
	
	// intent
	private int user_cd = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.getIntentValues();
		this.initView();
		if (StaticData.logined) {
			this.getXmlResources();
			this.modifyXmlResources();
			
			this.setEventListener();
		} else {
			switch (StaticData.NOWLANGUAGE) {
			case StaticData.KOREA:
				Toast.makeText(getApplicationContext(),R.string.please_retry_ko, Toast.LENGTH_SHORT).show();
				break;
			case StaticData.ENGLISH:
				Toast.makeText(getApplicationContext(),R.string.please_retry_en, Toast.LENGTH_SHORT).show();
				break;
			case StaticData.CHINA:
				Toast.makeText(getApplicationContext(),R.string.please_retry_cn, Toast.LENGTH_SHORT).show();
				break;
			}
			Intent intent = new Intent(getApplicationContext(),Login_Activity.class);
			startActivity(intent);
			overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
			finish();
		}
	}
	
	public void getIntentValues(){
		user_cd = getIntent().getIntExtra("user_cd", 0);
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		this.processManager.deleteActivity(this);
	}
	
	@Override
	public void onBackPressed() {
		Intent intent = new Intent(getApplicationContext(), MyBriefcaseBusinesscardHolder_Activity.class);
		startActivity(intent);
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
		this.setContentView(R.layout.activity_mybriefcasebusinesscardsecurity);
		this.processManager = ProcessManager.getInstance();
		this.processManager.addActivity(this);
	}

	@Override
	public void getXmlResources() {
		this.btn_back = (ImageButton) this.findViewById(R.id.btn_back);
		this.layout_header = (LinearLayout) this.findViewById(R.id.layout_header);
		this.layout_progressbar = (LinearLayout) this.findViewById(R.id.layout_progressbar);
		this.edit_security_code = (EditText) this.findViewById(R.id.edit_security_code);
		
		// button area
		this.btn_security_check = (ImageButton) this.findViewById(R.id.btn_security_check);
		
		// 언어
		this.total_title = (TextView) this.findViewById(R.id.total_title);
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
			this.total_title.setText(R.string.security_ko);
			this.text_wait.setText(R.string.wait_ko);
			this.btn_security_check.setImageResource(R.drawable.b_confirm_kr);
			break;
		case StaticData.ENGLISH:
			this.btn_back.setImageResource(R.drawable.b_back_en);
			this.total_title.setText(R.string.security_en);
			this.btn_security_check.setImageResource(R.drawable.b_confirm_en);
			this.text_wait.setText(R.string.wait_en);
			break;
		case StaticData.CHINA:
			this.btn_back.setImageResource(R.drawable.b_back_cn);
			this.total_title.setText(R.string.security_cn);
			this.btn_security_check.setImageResource(R.drawable.b_confirm_cn);
			this.text_wait.setText(R.string.wait_cn);
			break;
		}
		
	}

	@Override
	public void setEventListener() {
		this.btn_back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(), MyBriefcaseBusinesscardHolder_Activity.class);
				startActivity(intent);
				overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
				finish();				
			}
		});
		
		this.btn_security_check.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(edit_security_code.getText().toString().equals("")){
					switch (StaticData.NOWLANGUAGE) {
					case StaticData.KOREA:
						Toast.makeText(getApplicationContext(),R.string.please_enter_the_code_ko, Toast.LENGTH_SHORT).show();
						break;
					case StaticData.ENGLISH:
						Toast.makeText(getApplicationContext(),R.string.please_enter_the_code_en, Toast.LENGTH_SHORT).show();
						break;
					case StaticData.CHINA:
						Toast.makeText(getApplicationContext(),R.string.please_enter_the_code_cn, Toast.LENGTH_SHORT).show();
						break;
					}
				}else{
					new executeCheckCode(MyBriefcaseBusinesscardHolderSecurity_Activity.this).execute();
				}
			}
		});
	}
	
	// back ground thread area
	/** 로그인 check back ground */
	class executeCheckCode extends WeakAsyncTask<Void, Void, Void,Activity> {
		public executeCheckCode(Activity target) {
			super(target);
			// TODO Auto-generated constructor stub
		}
		boolean result = false;

		@Override
		protected void onPreExecute(Activity target) {
			layout_progressbar.setVisibility(LinearLayout.VISIBLE);
		}

		@Override
		protected Void doInBackground(Activity target,Void... params) {
			try {
				SecurityCodeDao dao = new SecurityCodeDao(MyBriefcaseBusinesscardHolderSecurity_Activity.this);
				result = dao.getCheckResult(edit_security_code.getText().toString().trim(), user_cd);
			} catch (Exception e) {
				result = false;
			}

			return null;
		}

		@Override
		protected void onPostExecute(Activity target,Void param) {
			if (this.result) {
				Intent intent = new Intent(getApplicationContext(), MyBriefcaseBusinesscardHolderDetail_Activity.class);
				intent.putExtra("user_cd", user_cd);
				startActivity(intent);
				overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
				finish();
			} else {
				switch (StaticData.NOWLANGUAGE) {
				case StaticData.KOREA:
					Toast.makeText(getApplicationContext(),R.string.please_retry_ko, Toast.LENGTH_SHORT).show();
					break;
				case StaticData.ENGLISH:
					Toast.makeText(getApplicationContext(),R.string.please_retry_en, Toast.LENGTH_SHORT).show();
					break;
				case StaticData.CHINA:
					Toast.makeText(getApplicationContext(),R.string.please_retry_cn, Toast.LENGTH_SHORT).show();
					break;
				}
			}
			layout_progressbar.setVisibility(LinearLayout.GONE);
		}
	}
	
}
