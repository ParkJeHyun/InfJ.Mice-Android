package kr.co.iekorea.mc;

import kr.co.iekorea.mc.staticdata.StaticData;
import kr.co.iekorea.mc.util.BaseInterface;
import kr.co.iekorea.mc.util.ProcessManager;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

public class SearchStayDetail_Activity extends Activity implements BaseInterface {
	// Activity control.
	private ProcessManager processManager;

	// layout area
	private ImageButton btn_back;
	private LinearLayout layout_header;
	private LinearLayout layout_progressbar;
	private ImageButton btn_call_phone;
	
	private TextView text_flag;
	private TextView text_title;
	private TextView text_address;
	private TextView text_tel;
	
	private String contents;
	private String phone;
	private String address;
	private String title;
	private String state;
	
	private TextView text_stay_contents;
	
	private TextView total_title,text_wait;	
	private TextView info_division,info_company_name,info_address,info_phone;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.initView();
		if (StaticData.logined) {
			this.getIntentValues();
			this.getXmlResources();
			this.modifyXmlResources();
			this.setDataValues();
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
		this.address = intent.getStringExtra("address");
		this.phone = intent.getStringExtra("phone");
		this.title = intent.getStringExtra("title");
		this.state = intent.getStringExtra("state");
		this.contents = intent.getStringExtra("contents");
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
		this.setContentView(R.layout.activity_search_stay_detail);
		this.processManager = ProcessManager.getInstance();
		this.processManager.addActivity(this);
	}

	@Override
	public void getXmlResources() {
		this.btn_back = (ImageButton) this.findViewById(R.id.btn_back);
		this.layout_header = (LinearLayout) this.findViewById(R.id.layout_header);
		this.layout_progressbar = (LinearLayout) this.findViewById(R.id.layout_progressbar);
		
		this.text_flag = (TextView) this.findViewById(R.id.text_flag);
		this.text_title = (TextView) this.findViewById(R.id.text_title);
		this.text_address = (TextView) this.findViewById(R.id.text_address);
		this.text_stay_contents = (TextView) this.findViewById(R.id.text_stay_contents);
		this.text_tel = (TextView) this.findViewById(R.id.text_tel);
		this.btn_call_phone = (ImageButton) this.findViewById(R.id.btn_call_phone);
		
		this.total_title = (TextView) this.findViewById(R.id.total_title);
		this.text_wait = (TextView) this.findViewById(R.id.text_wait);
		this.info_division = (TextView) this.findViewById(R.id.info_division);
		this.info_company_name = (TextView) this.findViewById(R.id.info_company_name);
		this.info_address = (TextView) this.findViewById(R.id.info_address);
		this.info_phone = (TextView) this.findViewById(R.id.info_phone);
		
//		this.info_division
//		this.info_company_name
//		this.info_address
//		this.info_phone
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
			this.total_title.setText(R.string.accommodation_ko);
			this.text_wait.setText(R.string.wait_ko);
			this.info_division.setText(R.string.division_ko);
			this.info_company_name.setText(R.string.stay_company_ko);
			this.info_address.setText(R.string.address_ko);
			this.info_phone.setText(R.string.phone_ko);
			this.btn_call_phone.setImageResource(R.drawable.b_dialing_kr);
			break;
		case StaticData.ENGLISH:
			this.btn_back.setImageResource(R.drawable.b_back_en);
			this.total_title.setText(R.string.accommodation_en);
			this.info_division.setText(R.string.division_en);
			this.info_company_name.setText(R.string.stay_company_en);
			this.info_address.setText(R.string.address_en);
			this.info_phone.setText(R.string.phone_en);
			this.btn_call_phone.setImageResource(R.drawable.b_dialing_en);
			this.text_wait.setText(R.string.wait_en);
			break;
		case StaticData.CHINA:
			this.btn_back.setImageResource(R.drawable.b_back_cn);
			this.total_title.setText(R.string.accommodation_cn);
			this.info_division.setText(R.string.division_cn);
			this.info_company_name.setText(R.string.stay_company_cn);
			this.info_address.setText(R.string.address_cn);
			this.info_phone.setText(R.string.phone_cn);
			this.btn_call_phone.setImageResource(R.drawable.b_dialing_cn);
			this.text_wait.setText(R.string.wait_cn);
			break;
		}
	}

	@Override
	public void setEventListener() {
		this.btn_back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
				finish();
			}
		});
		
		this.btn_call_phone.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				performDial();
			}
		});
	}
	
	public void setDataValues(){
		this.text_address.setText(address);
		this.text_flag.setText(state);
		this.text_title.setText(title);
		this.text_tel.setText(phone);
		this.text_stay_contents.setText(contents);
	}
	
	public void performDial() {
		if (phone != null) {
			try {
				startActivity(new Intent(Intent.ACTION_CALL, Uri.parse("tel:"+ 
						phone.trim())));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
