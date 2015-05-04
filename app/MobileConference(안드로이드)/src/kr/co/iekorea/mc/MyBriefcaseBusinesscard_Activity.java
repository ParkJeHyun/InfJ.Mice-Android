package kr.co.iekorea.mc;

import kr.co.iekorea.mc.staticdata.StaticData;
import kr.co.iekorea.mc.util.BaseInterface;
import kr.co.iekorea.mc.util.CustomLog;
import kr.co.iekorea.mc.util.ImageDownloader;
import kr.co.iekorea.mc.util.ProcessManager;
import kr.co.iekorea.mc.util.WeakAsyncTask;
import kr.co.iekorea.mc.xml.BusinessCardDTO;
import kr.co.iekorea.mc.xml.MybriefDAO;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

public class MyBriefcaseBusinesscard_Activity extends Activity implements BaseInterface {
	// Activity control.
	private ProcessManager processManager;

	// layout area
	private ImageButton btn_back;
	private LinearLayout layout_header;
	private LinearLayout layout_progressbar;
	
	// contents area
	private TextView text_user_name,text_user_crop, text_user_position, text_user_phone ,text_user_email,text_user_address,info_email;
	private TextView info_nation,text_nation,info_city,text_user_city,info_state,text_user_state,text_user_address_2;
	private TextView info_postal,text_user_pastal;
	private ImageView img_mypic;
	
	// data area
	public BusinessCardDTO dto = new BusinessCardDTO();

	// button area
	private ImageButton btn_shared,btn_shared_cancel,btn_modify,btn_shared2;
	
	// 언어
	private TextView total_title,info_address,info_phone,info_position,info_company,text_wait;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.initView();
		if (StaticData.logined) {
			this.getXmlResources();
			this.modifyXmlResources();
			new setMyBusinessCard(MyBriefcaseBusinesscard_Activity.this).execute();
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
		
		// test
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		this.processManager.deleteActivity(this);
	}
	
	@Override
	public void onBackPressed() {
		Intent intent = new Intent(getApplicationContext(), MyBriefcase_Activity.class);
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
		this.setContentView(R.layout.activity_mybriefcasebusinesscard);
		this.processManager = ProcessManager.getInstance();
		this.processManager.addActivity(this);
	}

	@Override
	public void getXmlResources() {
		this.btn_back = (ImageButton) this.findViewById(R.id.btn_back);
		this.layout_header = (LinearLayout) this.findViewById(R.id.layout_header);
		this.layout_progressbar = (LinearLayout) this.findViewById(R.id.layout_progressbar);
		
		// contents area
		this.text_user_name = (TextView) this.findViewById(R.id.text_user_name);
		this.text_user_crop = (TextView) this.findViewById(R.id.text_user_crop);
		this.text_user_position = (TextView) this.findViewById(R.id.text_user_position);
		this.text_user_phone = (TextView) this.findViewById(R.id.text_user_phone);
		this.text_user_email = (TextView) this.findViewById(R.id.text_user_email);
		this.text_user_address = (TextView) this.findViewById(R.id.text_user_address);
		this.text_user_address_2 = (TextView) this.findViewById(R.id.text_user_address_2);
		this.img_mypic = (ImageView) this.findViewById(R.id.img_mypic);
		
		this.info_nation = (TextView) this.findViewById(R.id.info_nation);
		this.text_nation = (TextView) this.findViewById(R.id.text_nation);
		this.info_city = (TextView) this.findViewById(R.id.info_city);
		this.text_user_city = (TextView) this.findViewById(R.id.text_user_city);
		this.info_state = (TextView) this.findViewById(R.id.info_state);
		this.text_user_state = (TextView) this.findViewById(R.id.text_user_state);
		
		this.info_postal = (TextView) this.findViewById(R.id.info_postal);
		this.text_user_pastal = (TextView) this.findViewById(R.id.text_user_pastal);
		
//		this.text_nation
//		this.text_user_city
//		this.text_user_state
//		this.info_nation
//		this.info_city
//		this.info_state
		
		// Button area
		this.btn_shared = (ImageButton) this.findViewById(R.id.btn_shared);
		this.btn_shared2 = (ImageButton) this.findViewById(R.id.btn_shared2);
		this.btn_shared_cancel = (ImageButton) this.findViewById(R.id.btn_shared_cancel);
		this.btn_modify = (ImageButton) this.findViewById(R.id.btn_modify);
		
		// 언어
		this.total_title = (TextView) this.findViewById(R.id.total_title);
		this.info_address = (TextView) this.findViewById(R.id.info_address);
		this.info_phone = (TextView) this.findViewById(R.id.info_phone);
		this.info_position = (TextView) this.findViewById(R.id.info_position);
		this.info_company = (TextView) this.findViewById(R.id.info_company);
		this.text_wait = (TextView) this.findViewById(R.id.text_wait);
		this.info_email = (TextView) this.findViewById(R.id.info_email);
		
//		this.total_title
//		this.info_address
//		this.info_phone
//		this.info_position
//		this.info_company
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
			this.total_title.setText(R.string.businesscard_ko);
			this.info_email.setText(R.string.email_ko);
			this.text_wait.setText(R.string.wait_ko);
			this.info_address.setText(R.string.address_ko);
			this.info_phone.setText(R.string.phone_ko);
			this.info_position.setText(R.string.user_position_ko);
			this.info_company.setText(R.string.company_ko);
			
			this.info_nation.setText(R.string.nation_ko);
			this.info_city.setText(R.string.city_ko);
			this.info_state.setText(R.string.state_ko);
			this.info_postal.setText(R.string.postal_ko);
			
			this.btn_shared_cancel.setImageResource(R.drawable.b2_sharecancel_kr);
			this.btn_modify.setImageResource(R.drawable.b2_revise_kr);
			this.btn_shared.setImageResource(R.drawable.b2_entireshare_kr);
			// shared button 2
			this.btn_shared2.setImageResource(R.drawable.b2_share_kr);
			
			break;
		case StaticData.ENGLISH:
			this.btn_back.setImageResource(R.drawable.b_back_en);
			this.total_title.setText(R.string.businesscard_en);
			this.info_address.setText(R.string.address_en);
			this.info_phone.setText(R.string.phone_en);
			this.info_position.setText(R.string.user_position_en);
			this.info_company.setText(R.string.company_en);
			this.text_wait.setText(R.string.wait_en);
			this.info_email.setText(R.string.email_en);
			
			this.info_nation.setText(R.string.nation_en);
			this.info_city.setText(R.string.city_en);
			this.info_state.setText(R.string.state_en);
			this.info_postal.setText(R.string.postal_en);
			
			this.btn_shared.setImageResource(R.drawable.b2_entireshare_en);
			this.btn_shared_cancel.setImageResource(R.drawable.b2_sharecancel_en);
			this.btn_modify.setImageResource(R.drawable.b2_revise_en);
			// shared button 2
			this.btn_shared2.setImageResource(R.drawable.b2_share_en);
			break;
		case StaticData.CHINA:
			this.btn_back.setImageResource(R.drawable.b_back_cn);
			this.total_title.setText(R.string.businesscard_cn);
			this.info_address.setText(R.string.address_cn);
			this.info_phone.setText(R.string.phone_cn);
			this.info_position.setText(R.string.user_position_cn);
			this.info_company.setText(R.string.company_cn);
			this.text_wait.setText(R.string.wait_cn);
			this.info_email.setText(R.string.email_cn);
			
			this.info_nation.setText(R.string.nation_cn);
			this.info_city.setText(R.string.city_cn);
			this.info_state.setText(R.string.state_cn);
			this.info_postal.setText(R.string.postal_cn);
			
			this.btn_shared.setImageResource(R.drawable.b2_entireshare_cn);
			this.btn_shared_cancel.setImageResource(R.drawable.b2_sharecancel_cn);
			this.btn_modify.setImageResource(R.drawable.b2_revise_cn);
			// shared button 2
			this.btn_shared2.setImageResource(R.drawable.b2_share_cn);
			break;
		}
	}
	
	public void UpdateUI(){
		this.text_user_name.setText(dto.USER_NAME);
		this.text_user_crop.setText(dto.COMPANY);
		this.text_user_position.setText(dto.APPELLATION_NAME);
		String temp = dto.PHONE_1 +"-"+dto.PHONE_2+"-"+dto.PHONE_3;
		this.text_user_phone.setText(temp);
		this.text_user_email.setText(dto.EMAIL);
		
		this.text_user_address.setText(dto.STREET_ADDRESS);
		this.text_user_address_2.setText(dto.STREET_ADDRESS_DETAIL);
		
		CustomLog.e("", "dto.NATION :" +dto.NATION);
		CustomLog.e("", "dto.NATION_ID :" +dto.NATION_ID);
		this.text_nation.setText(dto.NATION+"");
		this.text_user_city.setText(dto.CITY);
		this.text_user_state.setText(dto.STATE);
		this.text_user_pastal.setText(dto.POSTAL_CODE);
		
		this.setImage();
		CustomLog.e("updateui", "dto.BUSINESSCARD_SHARE :"+dto.BUSINESSCARD_SHARE);
		
//		if(dto.BUSINESSCARD_SHARE.equals("n")){
//			CustomLog.e("updateui", "dto.BUSINESSCARD_SHARE.equals(n) - in");
//			// 공유를 한번도 사용하지 않았을 경우
//			// 모두 활성화
//			this.btn_shared.setClickable(true);
//			this.btn_shared.setFocusable(true);
//			this.btn_shared.setFocusableInTouchMode(true);
//		}else{
//			CustomLog.e("updateui", " ! dto.BUSINESSCARD_SHARE.equals(n) - in");
//			// 1 or 2 공유를 사용했을 경우
//			// 2, 취소 버튼만 활성화
//			this.btn_shared.setClickable(false);
//			this.btn_shared.setFocusable(false);
//			this.btn_shared.setFocusableInTouchMode(false);
//			
//		}
	}
	
	public void setImage(){
		try{
			ImageDownloader.download(StaticData.DEFAULT_URL+dto.PICTURE, img_mypic);
		}catch(Exception e){
		}
	}

	@Override
	public void setEventListener() {
		
		// back key
		this.btn_back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(), MyBriefcase_Activity.class);
				startActivity(intent);
				overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
				finish();
			}
		});
		
		// 공유 취소
		this.btn_shared_cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String alertTile = getResources().getString(R.string.app_name);
				String buttonMessage = "";
				String buttonYes = "";
				String buttonNo = "";
				switch (StaticData.NOWLANGUAGE) {
				case StaticData.KOREA:
					buttonMessage = getResources().getString(R.string.shared_cancel_ko);
					buttonYes = "네";
					buttonNo = "아니요";
					break;
				case StaticData.ENGLISH:
					buttonMessage = getResources().getString(R.string.shared_cancel_en);
					buttonYes = "Yes";
					buttonNo = "No";
					break;
				case StaticData.CHINA:
					buttonMessage = getResources().getString(R.string.shared_cancel_cn);
					buttonYes = "是";
					buttonNo = "没有";
					break;
				}

				new AlertDialog.Builder(MyBriefcaseBusinesscard_Activity.this)
				.setTitle(alertTile)
				.setMessage(buttonMessage)
				.setPositiveButton(buttonYes, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						new executeSharedCancel(MyBriefcaseBusinesscard_Activity.this).execute();
					}
				})
				.setNegativeButton(buttonNo, null)
				.show();
			}
		});
		
		// 공유하기
		if(dto != null && dto.BUSINESSCARD_SHARE.equals("n") ){
			this.btn_shared.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(getApplicationContext(), MyBriefcaseBusinesscardSecurity_Activity.class);
					startActivity(intent);
					overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
					finish();
				}
			});	
		}
		
		// 공유하기 2
		this.btn_shared2.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String alertTile = getResources().getString(R.string.app_name);
				String buttonMessage = "";
				String buttonYes = "";
				String buttonNo = "";
				switch (StaticData.NOWLANGUAGE) {
				case StaticData.KOREA:
					buttonMessage = getResources().getString(R.string.shared_ko);
					buttonYes = "네";
					buttonNo = "아니요";
					break;
				case StaticData.ENGLISH:
					buttonMessage = getResources().getString(R.string.shared_en);
					buttonYes = "Yes";
					buttonNo = "No";
					break;
				case StaticData.CHINA:
					buttonMessage = getResources().getString(R.string.shared_cn);
					buttonYes = "是";
					buttonNo = "没有";
					break;
				}

				new AlertDialog.Builder(MyBriefcaseBusinesscard_Activity.this)
				.setTitle(alertTile)
				.setMessage(buttonMessage)
				.setPositiveButton(buttonYes, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						Intent intent = new Intent(getApplicationContext(), MyBriefcaseBusinesscardSecurity_Activity.class);
						intent.putExtra("is_share_2", true);
						startActivity(intent);
						overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
						finish();
					}
				})
				.setNegativeButton(buttonNo, null)
				.show();
			}
		});	
		
		
		// 수정하기
		this.btn_modify.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(), MyBriefcaseBusinesscardModify_Activity.class);
				startActivity(intent);
				overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
				finish();
			}
		});
	}
	
	// back ground
	// 해당 유저 명암 가저오기
	class setMyBusinessCard extends WeakAsyncTask<Void, Void, Void, Activity>{
		boolean result = false;
		public setMyBusinessCard(Activity target) {
			super(target);
		}
		@Override
		protected void onPreExecute(Activity target) {
			layout_progressbar.setVisibility(LinearLayout.VISIBLE);
		}
		@Override
		protected Void doInBackground(Activity target, Void... params) {
			MybriefDAO dao = new MybriefDAO(MyBriefcaseBusinesscard_Activity.this);
			try {
				result = dao.setBusinessCard();
			} catch (Exception e) {
				result = false;
			}
			if(result){
				dto = dao.getBusinessCard();
			}
			return null;
		}		
		@Override
		protected void onPostExecute(Activity target, Void result) {
			if(this.result){
				UpdateUI();
			}
			layout_progressbar.setVisibility(LinearLayout.GONE);
			setEventListener();
		}
	}
	
	// 공유 취소 하기
	class executeSharedCancel extends WeakAsyncTask<Void, Void, Void, Activity>{
		boolean result = false;
		public executeSharedCancel(Activity target) {
			super(target);
		}
		@Override
		protected void onPreExecute(Activity target) {
			
		}
		@Override
		protected Void doInBackground(Activity target, Void... params) {
			MybriefDAO dao = new MybriefDAO(MyBriefcaseBusinesscard_Activity.this);
			try {
				result = dao.setSharedBusinesscard('n');
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
					Toast.makeText(getApplicationContext(),R.string.sharing_cancel_ko, Toast.LENGTH_SHORT).show();
					break;
				case StaticData.ENGLISH:
					Toast.makeText(getApplicationContext(),R.string.sharing_cancel_en, Toast.LENGTH_SHORT).show();
					break;
				case StaticData.CHINA:
					Toast.makeText(getApplicationContext(),R.string.sharing_cancel_cn, Toast.LENGTH_SHORT).show();
					break;
				}
				
				Intent intent = new Intent(getApplicationContext(), MyBriefcase_Activity.class);
				startActivity(intent);
				overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
				finish();
			}else{
				switch (StaticData.NOWLANGUAGE) {
				case StaticData.KOREA:
					Toast.makeText(getApplicationContext(),R.string.fail_ko, Toast.LENGTH_SHORT).show();
					break;
				case StaticData.ENGLISH:
					Toast.makeText(getApplicationContext(),R.string.fail_en, Toast.LENGTH_SHORT).show();
					break;
				case StaticData.CHINA:
					Toast.makeText(getApplicationContext(),R.string.fail_cn, Toast.LENGTH_SHORT).show();
					break;
				}
			}
			layout_progressbar.setVisibility(LinearLayout.GONE);
		}
	}

	// 이미지 다운 백그라운드
//	class executeSetImage extends WeakAsyncTask<Void, Void, Void,Activity>{
//		public executeSetImage(Activity target) {
//			super(target);
//		}
//		@Override
//		protected void onPreExecute(Activity target) {
//			layout_progressbar.setVisibility(LinearLayout.VISIBLE);
//		}
//		@Override
//		protected Void doInBackground(Activity target,Void... params) {
//			ImageDownloader.download(StaticData.DEFAULT_URL+dto.PICTURE, img_mypic);
//			return null;
//		}
//		@Override
//		protected void onPostExecute(Activity target,Void result) {
//			layout_progressbar.setVisibility(LinearLayout.GONE);
//		}
//	}
}
