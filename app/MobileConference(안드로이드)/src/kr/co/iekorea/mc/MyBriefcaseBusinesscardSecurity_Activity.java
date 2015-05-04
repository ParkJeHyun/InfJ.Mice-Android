package kr.co.iekorea.mc;

import java.util.ArrayList;

import kr.co.iekorea.mc.staticdata.StaticData;
import kr.co.iekorea.mc.util.BaseInterface;
import kr.co.iekorea.mc.util.CGRandomString;
import kr.co.iekorea.mc.util.ProcessManager;
import kr.co.iekorea.mc.util.WeakAsyncTask;
import kr.co.iekorea.mc.xml.LoginDto;
import kr.co.iekorea.mc.xml.MCDao;
import kr.co.iekorea.mc.xml.MybriefDAO;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

public class MyBriefcaseBusinesscardSecurity_Activity extends Activity
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
	private boolean is_share_2 = false;
	
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
		this.is_share_2 = getIntent().getBooleanExtra("is_share_2", false);
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		this.processManager.deleteActivity(this);
	}
	
	@Override
	public void onBackPressed() {
		Intent intent = new Intent(getApplicationContext(), MyBriefcaseBusinesscard_Activity.class);
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
		if(is_share_2){
			this.setContentView(R.layout.activity_mybriefcasebusinesscardsecurity_2);
		}else{
			this.setContentView(R.layout.activity_mybriefcasebusinesscardsecurity);
		}
		this.processManager = ProcessManager.getInstance();
		this.processManager.addActivity(this);
	}

	@Override
	public void getXmlResources() {
		this.btn_back = (ImageButton) this.findViewById(R.id.btn_back);
		this.layout_header = (LinearLayout) this.findViewById(R.id.layout_header);
		this.layout_progressbar = (LinearLayout) this.findViewById(R.id.layout_progressbar);
		this.edit_security_code = (EditText) this.findViewById(R.id.edit_security_code);
		
		// contents
		this.pref = PreferenceManager.getDefaultSharedPreferences(this);
		this.user_id = pref.getString("user_id", null); 
		
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
		
		if(is_share_2){
			// 공유하기 2 버튼 클릭시 들어옴
			// back ground start
			new executeShared2(MyBriefcaseBusinesscardSecurity_Activity.this).execute();
		}
	}

	@Override
	public void setEventListener() {
		this.btn_back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(), MyBriefcaseBusinesscard_Activity.class);
				startActivity(intent);
				overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
				finish();				
			}
		});
		
		this.btn_security_check.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(is_share_2){
					Intent intent = new Intent(getApplicationContext(), MyBriefcase_Activity.class);
					startActivity(intent);
					overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
					finish();
				}else{
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
						new executeCheckLogin(MyBriefcaseBusinesscardSecurity_Activity.this).execute();
					}
				}
			}
		});
	}
	
	/** 실 유요한 ID이라면 ture 리턴을 받고 true시 권한및 regID 서버에 전송을 위한 check값 리턴 */
	public boolean checkLoginFormat() {
		boolean result = false;
		Log.e("###", "checkloginformat");
		for (int i = 0; i < loginList.size(); i++) {
			LoginDto dto = loginList.get(i);
			if (dto.loginFlag.equals("true")) {
				result = true;
				StaticData.USER_CD = dto.user_cd;
			}
		}
		return result;
	}
	
	// back ground thread area
	/** 로그인 check back ground */
	class executeCheckLogin extends WeakAsyncTask<Void, Void, Void,Activity> {
		public executeCheckLogin(Activity target) {
			super(target);
			// TODO Auto-generated constructor stub
		}

		boolean result = false;
		boolean check = false;
		int count = 0;

		@Override
		protected void onPreExecute(Activity target) {
			layout_progressbar.setVisibility(LinearLayout.VISIBLE);
		}

		@Override
		protected Void doInBackground(Activity target,Void... params) {
			try {
				MCDao dao = new MCDao(MyBriefcaseBusinesscardSecurity_Activity.this);
				if (MyBriefcaseBusinesscardSecurity_Activity.this.loginList != null) {
					MyBriefcaseBusinesscardSecurity_Activity.this.loginList.clear();
				}
				MyBriefcaseBusinesscardSecurity_Activity.this.loginList = dao.getLogin(
						user_id,
						edit_security_code.getText().toString(), getPackageName());
				if (MyBriefcaseBusinesscardSecurity_Activity.this.loginList != null) {
					result = true;
				}
			} catch (Exception e) {
				result = false;
				e.printStackTrace();
			}

			if (result) {
				if (MyBriefcaseBusinesscardSecurity_Activity.this.checkLoginFormat()) {
					check = true;
				} else {
					check = false;
				}
			}
			return null;
		}

		@Override
		protected void onPostExecute(Activity target,Void param) {
			if (result) {
				if (check) {
					// execute shared business card
					new executeShared(MyBriefcaseBusinesscardSecurity_Activity.this).execute();
				} else {
					layout_progressbar.setVisibility(LinearLayout.GONE);
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
			} else {
				layout_progressbar.setVisibility(LinearLayout.GONE);
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
		}
	}
	
	public void updateRandomText(){
		this.edit_security_code.setText(randomString);
		this.edit_security_code.setClickable(false);
		this.edit_security_code.setFocusable(false);
		this.edit_security_code.setFocusableInTouchMode(false);
	}
	
	// 공유하기
	class executeShared extends WeakAsyncTask<Void, Void, Void, Activity>{
		boolean result = false;
		public executeShared(Activity target) {
			super(target);
		}
		@Override
		protected void onPreExecute(Activity target) {
			
		}
		@Override
		protected Void doInBackground(Activity target, Void... params) {
			MybriefDAO dao = new MybriefDAO(MyBriefcaseBusinesscardSecurity_Activity.this);
			try {
				result = dao.setSharedBusinesscard('y');
			} catch (Exception e) {
				result = false;
			}
			return null;
		}		
		@Override
		protected void onPostExecute(Activity target, Void result) {
			if(this.result){
				switch (StaticData.NOWLANGUAGE) {
				case StaticData.KOREA:
					Toast.makeText(getApplicationContext(),R.string.sharing_success_ko, Toast.LENGTH_SHORT).show();
					break;
				case StaticData.ENGLISH:
					Toast.makeText(getApplicationContext(),R.string.sharing_success_en, Toast.LENGTH_SHORT).show();
					break;
				case StaticData.CHINA:
					Toast.makeText(getApplicationContext(),R.string.sharing_success_cn, Toast.LENGTH_SHORT).show();
					break;
				}
				Intent intent = new Intent(getApplicationContext(), MyBriefcase_Activity.class);
				startActivity(intent);
				overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
				finish();
			}else{
				switch (StaticData.NOWLANGUAGE) {
				case StaticData.KOREA:
					Toast.makeText(getApplicationContext(),R.string.sharing_failed_ko, Toast.LENGTH_SHORT).show();
					break;
				case StaticData.ENGLISH:
					Toast.makeText(getApplicationContext(),R.string.sharing_failed_en, Toast.LENGTH_SHORT).show();
					break;
				case StaticData.CHINA:
					Toast.makeText(getApplicationContext(),R.string.sharing_failed_cn, Toast.LENGTH_SHORT).show();
					break;
				}
			}
			layout_progressbar.setVisibility(LinearLayout.GONE);
		}
	}
	
	public String randomString = "";
	
	class executeShared2 extends WeakAsyncTask<Void, Void, Void, Activity>{
		boolean result = false;
		public executeShared2(Activity target) {
			super(target);
		}
		@Override
		protected void onPreExecute(Activity target) {
			layout_progressbar.setVisibility(LinearLayout.VISIBLE);
			randomString = new CGRandomString().getRandomString(6);
			
		}
		@Override
		protected Void doInBackground(Activity target, Void... params) {
			MybriefDAO dao = new MybriefDAO(MyBriefcaseBusinesscardSecurity_Activity.this);
			try {
				result = dao.setSharedBusinesscard('s',randomString);
			} catch (Exception e) {
				result = false;
			}
			return null;
		}		
		@Override
		protected void onPostExecute(Activity target, Void result) {
			if(this.result){
				switch (StaticData.NOWLANGUAGE) {
				case StaticData.KOREA:
					Toast.makeText(getApplicationContext(),R.string.sharing_success_ko, Toast.LENGTH_SHORT).show();
					break;
				case StaticData.ENGLISH:
					Toast.makeText(getApplicationContext(),R.string.sharing_success_en, Toast.LENGTH_SHORT).show();
					break;
				case StaticData.CHINA:
					Toast.makeText(getApplicationContext(),R.string.sharing_success_cn, Toast.LENGTH_SHORT).show();
					break;
				}
				updateRandomText();
			}else{
				switch (StaticData.NOWLANGUAGE) {
				case StaticData.KOREA:
					Toast.makeText(getApplicationContext(),R.string.sharing_failed_ko, Toast.LENGTH_SHORT).show();
					break;
				case StaticData.ENGLISH:
					Toast.makeText(getApplicationContext(),R.string.sharing_failed_en, Toast.LENGTH_SHORT).show();
					break;
				case StaticData.CHINA:
					Toast.makeText(getApplicationContext(),R.string.sharing_failed_cn, Toast.LENGTH_SHORT).show();
					break;
				}
				
				Intent intent = new Intent(getApplicationContext(), MyBriefcase_Activity.class);
				startActivity(intent);
				overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
				finish();
			}
			layout_progressbar.setVisibility(LinearLayout.GONE);
		}
	}
}
