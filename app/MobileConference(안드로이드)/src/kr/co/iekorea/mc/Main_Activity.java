package kr.co.iekorea.mc;

import java.util.ArrayList;

import kr.co.iekorea.mc.staticdata.StaticData;
import kr.co.iekorea.mc.util.BaseInterface;
import kr.co.iekorea.mc.util.CustomLog;
import kr.co.iekorea.mc.util.ImageDownloader;
import kr.co.iekorea.mc.util.ProcessManager;
import kr.co.iekorea.mc.util.WeakAsyncTask;
import kr.co.iekorea.mc.xml.BannerDAO;
import kr.co.iekorea.mc.xml.BannerDTO;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

public class Main_Activity extends Activity implements BaseInterface {

	final String TAG = "Main_Activity";

	// Activity control.
	private ProcessManager processManager;

	// Activity Layout
	private LinearLayout main_laout; // 아이콘이 있는 기본 리니어 레이아웃
	private ImageButton btn_help; // help button
	private LinearLayout layout_progressbar; // progress bar linearlayout
	private ImageView btn_advertising;

	private LinearLayout main_layout_row1, main_layout_row2, main_layout_row3;
	public ArrayList<ImageButton> MCMenuButton = new ArrayList<ImageButton>();
	public Intent intent;
	public ImageView institute_title_image;
	
	public TextView text_wait;
	
	// banner
//	public BannerDTO bannerDto = new BannerDTO(); 

	// for strictmode test
	public static final boolean SUPPORT_STRICT_MODE = Build.VERSION_CODES.FROYO < Build.VERSION.SDK_INT;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		// boolean debaggable = (getApplicationInfo().flags &
		// ApplicationInfo.FLAG_DEBUGGABLE) != 0;
		//
		// if (SUPPORT_STRICT_MODE && debaggable) {
		// if (true && true) {
		// StrictMode.ThreadPolicy.Builder builder = new
		// StrictMode.ThreadPolicy.Builder();
		// // builder.detectCustomSlowCalls(); // api level 11
		// builder.detectNetwork();
		// builder.detectDiskReads();
		// builder.detectDiskWrites();
		// builder.detectNetwork();
		// // 위반시 로그로 알림
		// builder.penaltyLog();
		// // 위반시 dropbox 에 저장
		// builder.penaltyDropBox();
		// // 위반시 다이얼로그로 알림
		// builder.penaltyDialog();
		// // 위반시 강제종료
		// builder.penaltyDeath();
		// // 네트워크 사용 위반시 강제종료 , 반드시 detectNetwork() 가 활성화 되어 있어야함.
		// StrictMode.setThreadPolicy(builder.build());
		//
		// StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
		// .detectLeakedSqlLiteObjects().penaltyLog()
		// .penaltyDeath().penaltyDropBox().build());
		// }
		// }

		super.onCreate(savedInstanceState);
		this.initView();
		this.getXmlResources();
		if (StaticData.logined) {
			this.modifyXmlResources();
			new ExecuteBannerAddress(Main_Activity.this).execute();
		} else {
//			Toast.makeText(getApplicationContext(),"다시 로그인을 해 주세요.",Toast.LENGTH_SHORT).show();
			Intent intent = new Intent(getApplicationContext(),Login_Activity.class);
			startActivity(intent);
			overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
			finish();
		}
	}

	@Override
	public void initView() {
		this.setContentView(R.layout.activity_main);
		this.processManager = ProcessManager.getInstance();
		this.processManager.addActivity(Main_Activity.this);
		StaticData.preActivity_name = null;
	}

	@Override
	public void getXmlResources() {
		// 기본 레이아웃 리소스 가져오기
		this.btn_help = (ImageButton) this.findViewById(R.id.btn_help);
		this.layout_progressbar = (LinearLayout) this.findViewById(R.id.layout_progressbar);
		this.btn_advertising = (ImageView) this.findViewById(R.id.btn_advertising);
		this.institute_title_image = (ImageView) this.findViewById(R.id.institute_title_image); 

		this.main_layout_row1 = (LinearLayout) this.findViewById(R.id.main_layout_row1);
		this.main_layout_row2 = (LinearLayout) this.findViewById(R.id.main_layout_row2);
		this.main_layout_row3 = (LinearLayout) this.findViewById(R.id.main_layout_row3);
		
		this.text_wait = (TextView) this.findViewById(R.id.text_wait);
	}

	private SharedPreferences pref;

	public void createMenuButton() {
		LayoutParams param;
		int width, height;
		pref = PreferenceManager.getDefaultSharedPreferences(this);
		width = pref.getInt("deviceWidth", 0);
		height = pref.getInt("deviceHeight", 0);
		if (width == 0 || height == 0) {
		} else {
			param = new LayoutParams((width - 25) / 3,
					LayoutParams.WRAP_CONTENT);
			if(MCMenuButton != null){
				MCMenuButton.clear();
			}
			for (int i = 0; i < StaticData.permission.length; i++) {
				if (StaticData.permission[i].equals("y")) {
					intent = new Intent();
					ImageButton menu = new ImageButton(this);
					menu.setLayoutParams(param);
					menu.setAdjustViewBounds(true);
					menu.setBackgroundColor(0x00);
					menu.setScaleType(ScaleType.FIT_CENTER);
					menu.setPadding(0, 0, 0, 0);
					switch (i) {
					case 0:
						switch(StaticData.NOWLANGUAGE){
						case StaticData.KOREA:
						case StaticData.ENGLISH:
							menu.setImageResource(R.drawable.main_agenda_en);
							break;
						case StaticData.CHINA:
							menu.setImageResource(R.drawable.main_agenda_cn);
							break;
						}
						menu.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View v) {
								intent = new Intent(getApplicationContext(),
										Agenda_Activity.class);
								startActivity(intent);
								overridePendingTransition(
										android.R.anim.fade_in,
										android.R.anim.fade_out);
								finish();
							}
						});
						break;
					case 1:
						switch(StaticData.NOWLANGUAGE){
						case StaticData.KOREA:
						case StaticData.ENGLISH:
							menu.setImageResource(R.drawable.main_binder_en);
							break;
						case StaticData.CHINA:
							menu.setImageResource(R.drawable.main_binder_cn);
							break;
						}
						menu.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View v) {
								intent = new Intent(getApplicationContext(),
										Binder_Activity.class);
								startActivity(intent);
								overridePendingTransition(
										android.R.anim.fade_in,
										android.R.anim.fade_out);
								finish();
							}
						});
						break;
					case 2:
						switch(StaticData.NOWLANGUAGE){
						case StaticData.KOREA:
						case StaticData.ENGLISH:
							menu.setImageResource(R.drawable.main_mybriefcase_en);
							break;
						case StaticData.CHINA:
							menu.setImageResource(R.drawable.main_mybriefcase_cn);
							break;
						}
						menu.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View v) {
								intent = new Intent(getApplicationContext(),
										MyBriefcase_Activity.class);
								startActivity(intent);
								overridePendingTransition(
										android.R.anim.fade_in,
										android.R.anim.fade_out);
								finish();
							}
						});
						break;
					case 3:
						switch(StaticData.NOWLANGUAGE){
						case StaticData.KOREA:
						case StaticData.ENGLISH:
							menu.setImageResource(R.drawable.main_message_en);
							break;
						case StaticData.CHINA:
							menu.setImageResource(R.drawable.main_message_cn);
							break;
						}
						menu.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View v) {
								intent = new Intent(getApplicationContext(),
										Message_Activity.class);
								startActivity(intent);
								overridePendingTransition(
										android.R.anim.fade_in,
										android.R.anim.fade_out);
								finish();
							}
						});
						break;
					case 4:
						switch(StaticData.NOWLANGUAGE){
						case StaticData.KOREA:
						case StaticData.ENGLISH:
							menu.setImageResource(R.drawable.main_survey_en);
							break;
						case StaticData.CHINA:
							menu.setImageResource(R.drawable.main_survey_cn);
							break;
						}
						menu.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View v) {
								intent = new Intent(getApplicationContext(),
										Survey_Activity.class);
								startActivity(intent);
								overridePendingTransition(
										android.R.anim.fade_in,
										android.R.anim.fade_out);
								finish();
							}
						});
						break;
					case 5:
						switch(StaticData.NOWLANGUAGE){
						case StaticData.KOREA:
						case StaticData.ENGLISH:
							menu.setImageResource(R.drawable.main_map_en);
							break;
						case StaticData.CHINA:
							menu.setImageResource(R.drawable.main_map_cn);
							break;
						}
						menu.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View v) {
								intent = new Intent(getApplicationContext(),Map_Activity.class);
								startActivity(intent);
								overridePendingTransition(
										android.R.anim.fade_in,
										android.R.anim.fade_out);
								finish();
							}
						});
						break;
					case 6:
						switch(StaticData.NOWLANGUAGE){
						case StaticData.KOREA:
						case StaticData.ENGLISH:
							menu.setImageResource(R.drawable.main_qrcode_en);
							break;
						case StaticData.CHINA:
							menu.setImageResource(R.drawable.main_qrcode_cn);
							break;
						}
						menu.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View v) {
								intent = new Intent(getApplicationContext(),QRCode_Activity.class);
								startActivity(intent);
								overridePendingTransition(
										android.R.anim.fade_in,
										android.R.anim.fade_out);
								finish();
							}
						});
						break;
					case 7:
						switch(StaticData.NOWLANGUAGE){
						case StaticData.KOREA:
						case StaticData.ENGLISH:
							menu.setImageResource(R.drawable.main_search_en);
							break;
						case StaticData.CHINA:
							menu.setImageResource(R.drawable.main_search_cn);
							break;
						}
						menu.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View v) {
								intent = new Intent(getApplicationContext(),
										Search_Activity.class);
								startActivity(intent);
								overridePendingTransition(
										android.R.anim.fade_in,
										android.R.anim.fade_out);
								finish();
							}
						});
						break;
					case 8:
						switch(StaticData.NOWLANGUAGE){
						case StaticData.KOREA:
						case StaticData.ENGLISH:
							menu.setImageResource(R.drawable.main_etc_en);
							break;
						case StaticData.CHINA:
							menu.setImageResource(R.drawable.main_etc_cn);
							break;
						}
						menu.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View v) {
								intent = new Intent(getApplicationContext(),Etc_Activity.class);
								startActivity(intent);
								overridePendingTransition(
										android.R.anim.fade_in,
										android.R.anim.fade_out);
								finish();
							}
						});
						break;
					}
					MCMenuButton.add(menu);
				}
			}

			for (int i = 0; i < MCMenuButton.size(); i++) {
				switch (i) {
				case 0:
				case 1:
				case 2:
					this.main_layout_row1.addView(MCMenuButton.get(i));
					break;
				case 3:
				case 4:
				case 5:
					this.main_layout_row2.addView(MCMenuButton.get(i));
					break;
				case 6:
				case 7:
				case 8:
					this.main_layout_row3.addView(MCMenuButton.get(i));
					break;
				}
			}
		}
	}

	@Override
	public void modifyXmlResources() {
		createMenuButton();
		switch (StaticData.NOWLANGUAGE) {
		case StaticData.KOREA:
			text_wait.setText(R.string.wait_ko);
			break;
		case StaticData.ENGLISH:
			text_wait.setText(R.string.wait_en);
			break;
		case StaticData.CHINA:
			text_wait.setText(R.string.wait_cn);
			break;
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		this.processManager.deleteActivity(Main_Activity.this);
	}

	@Override
	public void finish() {
		super.finish();
		overridePendingTransition(android.R.anim.fade_in,
				android.R.anim.fade_out);
	}

	@Override
	public void onBackPressed() {
		String alertTile = getResources().getString(R.string.app_name);
		String buttonMessage = "";
		String buttonYes = "";
		String buttonNo = "";
		switch(StaticData.NOWLANGUAGE){
		case StaticData.KOREA:
			buttonMessage = "프로그램을 \n 종료하시겠습니까?";
			buttonYes = "예";
			buttonNo = "아니오";
			break;
		case StaticData.ENGLISH:
			buttonMessage = " Do you want \n to quit the program?";
			buttonYes = "Yes";
			buttonNo = "No";
			break;
		case StaticData.CHINA:
			buttonMessage = " 确定退出程序吗?";
			buttonYes = "是";
			buttonNo = "没有";
			break;
		}

		new AlertDialog.Builder(this)
				.setTitle(alertTile)
				.setMessage(buttonMessage)
				.setPositiveButton(buttonYes,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								processManager.allEndActivity();
								android.os.Process.killProcess(android.os.Process.myPid());
								overridePendingTransition(
										android.R.anim.fade_in,
										android.R.anim.fade_out);
							}
						}).setNegativeButton(buttonNo, null).show();
	}
	
	public void setBannerImage(){
		try{
			ImageDownloader.download(StaticData.DEFAULT_URL+StaticData.bannderDto.MAIN_BANNER, btn_advertising);
			CustomLog.e(TAG, "StaticData.bannderDto.MAIN_BANNER : "+StaticData.bannderDto.MAIN_BANNER);
			btn_advertising.setVisibility(ImageView.VISIBLE);
		}catch(Exception e){
		}
		try{
			ImageDownloader.download(StaticData.DEFAULT_URL+StaticData.CONFERENCE_BANNER, institute_title_image);
		}catch(Exception e){
		}
	}
	
	@Override
	public void setEventListener() {
		this.btn_advertising.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				try{
					Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(StaticData.bannderDto.MAIN_LANDING_URL));
					startActivity(intent);
				}catch(Exception e){}
			}
		});
	}
	
	// back ground area
	class ExecuteBannerAddress extends WeakAsyncTask<Void, Void, Void, Activity>{
		boolean result = false;
		boolean check = false;

		public ExecuteBannerAddress(Activity target) {
			super(target);
		}
		
		@Override
		protected void onPreExecute(Activity target) {
			layout_progressbar.setVisibility(LinearLayout.VISIBLE);
		}

		@Override
		protected Void doInBackground(Activity target, Void... params) {
			BannerDAO DAO = new BannerDAO(Main_Activity.this);
			try{
				check = DAO.setBanner();
				if(check){
					if(StaticData.bannderDto != null){
						StaticData.bannderDto = new BannerDTO();
					}
					StaticData.bannderDto = DAO.getBanner();
					result = true;
				}
			}catch(Exception e){
				result = false;
			}
			return null;
		}
		
		@Override
		protected void onPostExecute(Activity target, Void result) {
			if(this.result){
				setBannerImage();
			}else{
				switch(StaticData.NOWLANGUAGE){
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
			setEventListener();
			layout_progressbar.setVisibility(LinearLayout.GONE);
		}
	}
}
