package kr.co.iekorea.mc;

import java.util.ArrayList;

import kr.co.iekorea.mc.staticdata.StaticData;
import kr.co.iekorea.mc.util.BaseInterface;
import kr.co.iekorea.mc.util.ProcessManager;
import kr.co.iekorea.mc.util.WeakAsyncTask;
import kr.co.iekorea.mc.xml.LoginDto;
import kr.co.iekorea.mc.xml.MCDao;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gcm.GCMRegistrar;

public class Login_Activity extends Activity implements BaseInterface {
	// 1/24
	public static final String TAG = "login activity";
	// Activity layout resource
	private ImageButton btn_check_login;
	private LinearLayout id_input_area;
	private EditText edit_id;
	private EditText edit_pw;
	private LinearLayout layout_progressbar;

	// dp size
	private int defaultWidth, defaultHeight;

	// login area params and size
	private int login_layout_size;
	private String loginID, loginPW;
	private ArrayList<LoginDto> loginList = new ArrayList<LoginDto>();

	private LinearLayout.LayoutParams params;

	// for listener event
	private LoginButtonEventKistener ListenerEvent = new LoginButtonEventKistener();

	// Activity control.
	private ProcessManager processManager;

	// preferences
	private SharedPreferences pref;
	private SharedPreferences.Editor edit;
	private String userID;

	// thread
	private Runnable translateRun;
	private Handler translateHandler;

	// text
	private TextView text_wait;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.initView();
		this.setDefaultValue();
		this.getXmlResources();
		this.setEventListener();
		new getGCMID(Login_Activity.this).execute();
	}

	/**
	 * GCM에 디바이스토큰 삭제
	 */
	private void unregisterToken() {
		if (GCMRegistrar.isRegistered(this)) {
			GCMRegistrar.unregister(this);
		}
	}

	/** 기본 초기화 */
	@Override
	public void initView() {
		this.setContentView(R.layout.activity_login);
		this.processManager = ProcessManager.getInstance();
		this.processManager.addActivity(Login_Activity.this);
	}

	/** 디바이스의 기본 크기를 가져오며 이전 정상적으로 로그인 한 ID를 들고온다 */
	private void setDefaultValue() {
		Display dp = ((WindowManager) getSystemService(WINDOW_SERVICE))
				.getDefaultDisplay();
		this.defaultWidth = StaticData.device_Width = dp.getWidth();
		this.defaultHeight = StaticData.device_Height = dp.getHeight();
		this.login_layout_size = (defaultHeight / 10) * 2;

		// preference
		pref = PreferenceManager.getDefaultSharedPreferences(this);
		userID = pref.getString("user_id", null);
		StaticData.NOWLANGUAGE = pref.getInt("language", 0);
		StaticData.ALRAMSET = pref.getBoolean("alramset", false);
		edit = pref.edit();
		edit.putInt("deviceWidth", this.defaultWidth);
		edit.putInt("deviceHeight", this.defaultHeight);
		edit.commit();
	}

	@Override
	public void getXmlResources() {
		// layout area
		this.id_input_area = (LinearLayout) this
				.findViewById(R.id.id_input_area);
		this.edit_id = (EditText) this.findViewById(R.id.edit_id);
		this.edit_id.setText(userID);
		this.edit_pw = (EditText) this.findViewById(R.id.edit_pw);

		// btn layout
		this.btn_check_login = (ImageButton) this.findViewById(R.id.btn_check);

		// progress
		this.layout_progressbar = (LinearLayout) this
				.findViewById(R.id.layout_progressbar);
		this.text_wait = (TextView) this.findViewById(R.id.text_wait);

		// 언어 설정에 다른 화면 구성
		switch (StaticData.NOWLANGUAGE) {
		case StaticData.KOREA:
			this.btn_check_login.setImageResource(R.drawable.b_login_kr);
			this.text_wait.setText(R.string.wait_ko);
			break;
		case StaticData.ENGLISH:
			this.btn_check_login.setImageResource(R.drawable.b_login_en);
			this.text_wait.setText(R.string.wait_en);
			break;
		case StaticData.CHINA:
			this.btn_check_login.setImageResource(R.drawable.b_login_cn);
			this.text_wait.setText(R.string.wait_cn);
			break;
		}
	}

	@Override
	public void modifyXmlResources() {
	}

	@Override
	public void setEventListener() {
		this.btn_check_login.setOnClickListener(ListenerEvent); // login check
																// 버튼
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		this.processManager.deleteActivity(Login_Activity.this);
	}

	/** 버튼 클릭시 값의 유무 확인 메소드 */
	public boolean checkInputArea() {
		boolean result = false;
		this.loginID = this.loginPW = "";
		this.loginID = this.edit_id.getText().toString().trim();
		this.loginPW = this.edit_pw.getText().toString().trim();

		if (loginID.equals("")) {
			if (StaticData.NOWLANGUAGE == StaticData.KOREA) {
				Toast.makeText(getApplicationContext(),
						R.string.please_enter_the_id_ko, Toast.LENGTH_SHORT)
						.show();
			} else if (StaticData.NOWLANGUAGE == StaticData.ENGLISH) {
				Toast.makeText(getApplicationContext(),
						R.string.please_enter_the_id_en, Toast.LENGTH_SHORT)
						.show();
			} else if (StaticData.NOWLANGUAGE == StaticData.CHINA) {
				Toast.makeText(getApplicationContext(),
						R.string.please_enter_the_id_cn, Toast.LENGTH_SHORT)
						.show();
			}
		} else if (loginPW.equals("")) {
			if (StaticData.NOWLANGUAGE == StaticData.KOREA) {
				Toast.makeText(getApplicationContext(),
						R.string.please_enter_the_pw_ko, Toast.LENGTH_SHORT)
						.show();
			} else if (StaticData.NOWLANGUAGE == StaticData.ENGLISH) {
				Toast.makeText(getApplicationContext(),
						R.string.please_enter_the_pw_en, Toast.LENGTH_SHORT)
						.show();
			} else if (StaticData.NOWLANGUAGE == StaticData.CHINA) {
				Toast.makeText(getApplicationContext(),
						R.string.please_enter_the_pw_cn, Toast.LENGTH_SHORT)
						.show();
			}
		} else {
			result = true;
		}
		return result;
	}

	/** login check button event listener */
	class LoginButtonEventKistener implements OnClickListener {
		@Override
		public void onClick(View v) {
			// check event control area
			switch (v.getId()) {
			case R.id.btn_check:
				if (checkInputArea()) {
					// back ground 실행
					/** GCM Service 실행 */
					// new getGCMID().execute();
					layout_progressbar.setVisibility(LinearLayout.VISIBLE);
					translateRun = new Runnable() {
						@Override
						public void run() {
							new executeCheckLogin(Login_Activity.this)
									.execute();
						}
					};

					translateHandler = new Handler();
					translateHandler.postDelayed(translateRun, 2000); // delay
																		// time
				}
				break;
			}
		}
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
	class executeCheckLogin extends WeakAsyncTask<Void, Void, Void, Activity> {
		public executeCheckLogin(Activity target) {
			super(target);
			// TODO Auto-generated constructor stub
		}

		boolean result = false;
		boolean check = false;
		int count = 0;

		@Override
		protected void onPreExecute(Activity target) {
			// layout_progressbar.setVisibility(LinearLayout.VISIBLE);
		}

		@Override
		protected Void doInBackground(Activity target, Void... params) {
			try {
				MCDao dao = new MCDao(Login_Activity.this);
				if (Login_Activity.this.loginList != null) {
					Login_Activity.this.loginList.clear();
				}
				Login_Activity.this.loginList = dao.getLogin(
						Login_Activity.this.loginID,
						Login_Activity.this.loginPW, getPackageName());
				if (Login_Activity.this.loginList != null) {
					result = true;
				}
			} catch (Exception e) {
				result = false;
				e.printStackTrace();
			}

			if (result) {
				if (Login_Activity.this.checkLoginFormat()) {
					check = true;
				} else {
					check = false;
				}
			}
			return null;
		}

		@Override
		protected void onPostExecute(Activity target, Void param) {
			if (result) {
				if (check) {
					StaticData.logined = true;
					// back ground start area for authoriry
					new executeAutodriry(Login_Activity.this).execute();
				} else {
					layout_progressbar.setVisibility(LinearLayout.GONE);
					switch (StaticData.NOWLANGUAGE) {
					case StaticData.KOREA:
						Toast.makeText(getApplicationContext(),
								R.string.please_retry_ko, Toast.LENGTH_SHORT)
								.show();
						break;
					case StaticData.ENGLISH:
						Toast.makeText(getApplicationContext(),
								R.string.please_retry_en, Toast.LENGTH_SHORT)
								.show();
						break;
					case StaticData.CHINA:
						Toast.makeText(getApplicationContext(),
								R.string.please_retry_cn, Toast.LENGTH_SHORT)
								.show();
						break;
					}
				}
			} else {
				layout_progressbar.setVisibility(LinearLayout.GONE);
				switch (StaticData.NOWLANGUAGE) {
				case StaticData.KOREA:
					Toast.makeText(getApplicationContext(),
							R.string.please_retry_ko, Toast.LENGTH_SHORT)
							.show();
					break;
				case StaticData.ENGLISH:
					Toast.makeText(getApplicationContext(),
							R.string.please_retry_en, Toast.LENGTH_SHORT)
							.show();
					break;
				case StaticData.CHINA:
					Toast.makeText(getApplicationContext(),
							R.string.please_retry_cn, Toast.LENGTH_SHORT)
							.show();
					break;
				}
			}
		}
	}

	/** 권한 요청 background */
	class executeAutodriry extends WeakAsyncTask<Void, Void, Void, Activity> {
		public executeAutodriry(Activity target) {
			super(target);
			// TODO Auto-generated constructor stub
		}

		boolean result = false;

		@Override
		protected void onPreExecute(Activity target) {
			layout_progressbar.setVisibility(LinearLayout.VISIBLE);
		}

		@Override
		protected Void doInBackground(Activity target, Void... params) {
			try {
				MCDao dao = new MCDao(Login_Activity.this);
				result = dao.getAuthoriry(Login_Activity.this.loginID);
			} catch (Exception e) {
				result = false;
				e.printStackTrace();
			}

			return null;
		}

		@Override
		protected void onPostExecute(Activity target, Void param) {
			if (result) {
				edit.putString("user_id", Login_Activity.this.loginID);
				edit.commit();
				Intent intent = new Intent(getApplicationContext(),
						Main_Activity.class);
				startActivity(intent);
				overridePendingTransition(android.R.anim.fade_in,
						android.R.anim.fade_out);
				finish();
			} else {
				switch (StaticData.NOWLANGUAGE) {
				case StaticData.KOREA:
					Toast.makeText(getApplicationContext(),
							R.string.please_retry_ko, Toast.LENGTH_SHORT)
							.show();
					break;
				case StaticData.ENGLISH:
					Toast.makeText(getApplicationContext(),
							R.string.please_retry_en, Toast.LENGTH_SHORT)
							.show();
					break;
				case StaticData.CHINA:
					Toast.makeText(getApplicationContext(),
							R.string.please_retry_cn, Toast.LENGTH_SHORT)
							.show();
					break;
				}
			}
			layout_progressbar.setVisibility(LinearLayout.GONE);
		}
	}

	// GCM id값 background
	class getGCMID extends WeakAsyncTask<Void, Void, Void, Activity> {
		public getGCMID(Activity target) {
			super(target);
			// TODO Auto-generated constructor stub
		}

		@Override
		protected void onPreExecute(Activity target) {
			layout_progressbar.setVisibility(LinearLayout.VISIBLE);
			// unregisterToken();
		}

		@Override
		protected Void doInBackground(Activity target, Void... params) {
			GCMRegistrar.checkDevice(getApplicationContext());
			GCMRegistrar.checkManifest(getApplicationContext());
			final String regId = GCMRegistrar
					.getRegistrationId(getApplicationContext());
			StaticData.gcmRegistrationId = regId;
			Log.e(TAG, "regId :" + regId);
			// 등록된 ID가 없으면 ID값을 얻어옵니다
			if (regId.equals("") || regId == null) {
				GCMRegistrar.register(getApplicationContext(),
						StaticData.SENDER_ID);
				Log.w(TAG, "StaticData.gcmRegistrationId : "
						+ StaticData.gcmRegistrationId);
			} else {
				Log.w(TAG, "Already Registered : " + regId);
			}
			return null;
		}

		@Override
		protected void onPostExecute(Activity target, Void param) {
			// new executeCheckLogin().execute();
			layout_progressbar.setVisibility(LinearLayout.GONE);
		}
	}
}
