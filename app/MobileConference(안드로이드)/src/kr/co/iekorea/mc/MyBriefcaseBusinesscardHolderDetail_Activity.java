package kr.co.iekorea.mc;

import kr.co.iekorea.mc.staticdata.StaticData;
import kr.co.iekorea.mc.util.BaseInterface;
import kr.co.iekorea.mc.util.ImageDownloader;
import kr.co.iekorea.mc.util.ProcessManager;
import kr.co.iekorea.mc.util.WeakAsyncTask;
import kr.co.iekorea.mc.xml.BusinessCardDTO;
import kr.co.iekorea.mc.xml.MybriefDAO;
import android.app.Activity;
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

public class MyBriefcaseBusinesscardHolderDetail_Activity extends Activity
		implements BaseInterface {

	// Activity control.
	private ProcessManager processManager;

	// layout area
	private ImageButton btn_back;
	private LinearLayout layout_header;
	private LinearLayout layout_progressbar;
	
	// contents area
	private TextView text_user_name,text_user_crop,text_user_position
	,text_user_phone,text_user_email,text_user_address,info_email;
	private ImageView img_mypic;
	
	// data area
	public BusinessCardDTO dto = new BusinessCardDTO();
	public int user_cd;
	
	// 언어
	private TextView total_title,info_address,info_phone,info_position,info_company,text_wait;
	
	private TextView info_nation,text_nation,info_city,text_user_city,info_state,text_user_state,text_user_address_2;
	private TextView info_postal,text_user_pastal;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.initView();
		if (StaticData.logined) {
			this.getIntentValues();
			this.getXmlResources();
			this.modifyXmlResources();
			
			new setMyBusinessCard(MyBriefcaseBusinesscardHolderDetail_Activity.this).execute();
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
		this.setContentView(R.layout.activity_mybriefcasebusinesscardholderdetail);
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
		this.img_mypic = (ImageView) this.findViewById(R.id.img_mypic);
		
		// 언어
		this.total_title = (TextView) this.findViewById(R.id.total_title);
		this.info_email = (TextView) this.findViewById(R.id.info_email);
		this.info_address = (TextView) this.findViewById(R.id.info_address);
		this.info_phone = (TextView) this.findViewById(R.id.info_phone);
		this.info_position = (TextView) this.findViewById(R.id.info_position);
		this.info_company = (TextView) this.findViewById(R.id.info_company);
		this.text_wait = (TextView) this.findViewById(R.id.text_wait);
		
		this.info_nation = (TextView) this.findViewById(R.id.info_nation);
		this.info_city = (TextView) this.findViewById(R.id.info_city);
		this.info_state = (TextView) this.findViewById(R.id.info_state);
		this.info_postal = (TextView) this.findViewById(R.id.info_postal);
		
		this.text_nation = (TextView) this.findViewById(R.id.text_nation);
		this.text_user_city = (TextView) this.findViewById(R.id.text_user_city);
		this.text_user_state = (TextView) this.findViewById(R.id.text_user_state);
		this.text_user_address_2 = (TextView) this.findViewById(R.id.text_user_address_2);
		this.text_user_pastal = (TextView) this.findViewById(R.id.text_user_pastal);
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
			this.total_title.setText(R.string.businesscard_holder_ko);
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
			
			break;
		case StaticData.ENGLISH:
			this.btn_back.setImageResource(R.drawable.b_back_en);
			this.total_title.setText(R.string.businesscard_holder_en);
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
			break;
		case StaticData.CHINA:
			this.btn_back.setImageResource(R.drawable.b_back_cn);
			this.total_title.setText(R.string.businesscard_holder_cn);
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
	}
	
	public void UpdateUI(){
		this.text_user_name.setText(dto.USER_NAME);
		this.text_user_crop.setText(dto.COMPANY);
		this.text_user_position.setText(dto.APPELLATION_NAME);
		String temp = dto.PHONE_1 +"-"+dto.PHONE_2+"-"+dto.PHONE_3;
		this.text_user_phone.setText(temp);
		this.text_user_email.setText(dto.EMAIL);
		this.text_user_address.setText(dto.STREET_ADDRESS);
		this.setImage();
		
		this.text_nation.setText(dto.NATION);
		this.text_user_city.setText(dto.CITY);
		this.text_user_state.setText(dto.STATE);
		this.text_user_address_2.setText(dto.STREET_ADDRESS_DETAIL);
		this.text_user_pastal.setText(dto.POSTAL_CODE);
	}
	
	public void setImage(){
		try{
			ImageDownloader.download(StaticData.DEFAULT_URL+dto.PICTURE, img_mypic);
		}catch(Exception e){
		}
	}
	
	// back ground area
	// detail holder
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
			MybriefDAO dao = new MybriefDAO(MyBriefcaseBusinesscardHolderDetail_Activity.this);
			try {
				result = dao.setBusinessCard(user_cd);
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

}
