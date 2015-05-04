package kr.co.iekorea.mc;

import kr.co.iekorea.mc.event.MyBriefcaseEventListener;
import kr.co.iekorea.mc.staticdata.StaticData;
import kr.co.iekorea.mc.util.BaseInterface;
import kr.co.iekorea.mc.util.ImageDownloader;
import kr.co.iekorea.mc.util.ProcessManager;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

public class MyBriefcase_Activity extends Activity implements BaseInterface {
	// activity control
	private ProcessManager processmanager;
	
	// activity layout
	private LinearLayout layout_header;
	private ImageButton btn_back;
	private ImageButton btn_businesscard;
	private ImageButton btn_businesscard_holder;
	private ImageButton btn_memo;
	private ImageButton btn_schedule;
	private ImageButton btn_present;
	private ImageView btn_advertising;
	
	// 언어
	private TextView total_title;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.initView();
		if (StaticData.logined) {
			this.getXmlResources();
			this.modifyXmlResources();
			this.setEventListener();
		} else {
			Toast.makeText(getApplicationContext(), "다시 로그인을 해 주세요.",Toast.LENGTH_SHORT).show();
			Intent intent = new Intent(getApplicationContext(),Login_Activity.class);
			startActivity(intent);
			overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
			finish();
		}
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		this.processmanager.deleteActivity(MyBriefcase_Activity.this);
	}
	
	@Override
	public void onBackPressed() {
		Intent intent = new Intent(getApplicationContext(), Main_Activity.class);
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
		this.setContentView(R.layout.activity_mybriefcase);
		this.processmanager = ProcessManager.getInstance();
		this.processmanager.addActivity(MyBriefcase_Activity.this);
	}

	@Override
	public void getXmlResources() {
		this.layout_header = (LinearLayout) this.findViewById(R.id.layout_header);
		this.btn_back = (ImageButton) this.findViewById(R.id.btn_back);
		this.btn_businesscard = (ImageButton) this.findViewById(R.id.btn_businesscard);
		this.btn_businesscard_holder = (ImageButton) this.findViewById(R.id.btn_businesscard_holder);
		this.btn_memo = (ImageButton) this.findViewById(R.id.btn_memo);
		this.btn_schedule = (ImageButton) this.findViewById(R.id.btn_schedule);
		this.btn_present = (ImageButton) this.findViewById(R.id.btn_present);
		this.btn_advertising = (ImageView) this.findViewById(R.id.btn_advertising);
		
		// 언어
		this.total_title = (TextView) this.findViewById(R.id.total_title);
	}

	@Override
	public void modifyXmlResources() {
		LayoutParams param;
        param = new LayoutParams(LayoutParams.MATCH_PARENT,StaticData.device_Height/10);
        this.layout_header.setLayoutParams(param);
	}

	@Override
	public void setEventListener() {
		this.btn_back.setOnClickListener(new MyBriefcaseEventListener(this,this));
		this.btn_businesscard.setOnClickListener(new MyBriefcaseEventListener(this,this));
		this.btn_businesscard_holder.setOnClickListener(new MyBriefcaseEventListener(this,this));
		this.btn_memo.setOnClickListener(new MyBriefcaseEventListener(this, this));
		this.btn_schedule.setOnClickListener(new MyBriefcaseEventListener(this, this));
		this.btn_present.setOnClickListener(new MyBriefcaseEventListener(this, this));
		
		this.btn_advertising.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				try{
					Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(StaticData.bannderDto.MYBRIEFACASE_LANDING_URL));
					startActivity(intent);
				}catch(Exception e){}
			}
		});
		
		this.setBannerImage();
		
        // 언어 설정에 다른 화면 구성
		switch (StaticData.NOWLANGUAGE) {
		case StaticData.KOREA:
			this.btn_back.setImageResource(R.drawable.b_back_kr);
			this.total_title.setText(R.string.mybriefcase_ko);
			this.btn_businesscard.setImageResource(R.drawable.businesscard_kr);
			this.btn_businesscard_holder.setImageResource(R.drawable.businesscardholder_kr);
			this.btn_memo.setImageResource(R.drawable.memo_kr);
			this.btn_schedule.setImageResource(R.drawable.schedule_kr);
			this.btn_present.setImageResource(R.drawable.gift_kr);
			break;
		case StaticData.ENGLISH:
			this.btn_back.setImageResource(R.drawable.b_back_en);
			this.total_title.setText(R.string.mybriefcase_en);
			this.btn_businesscard.setImageResource(R.drawable.businesscard_en);
			this.btn_businesscard_holder.setImageResource(R.drawable.businesscardholder_en);
			this.btn_memo.setImageResource(R.drawable.memo_en);
			this.btn_schedule.setImageResource(R.drawable.schedule_en);
			this.btn_present.setImageResource(R.drawable.gift_en);
			break;
		case StaticData.CHINA:
			this.btn_back.setImageResource(R.drawable.b_back_cn);
			this.total_title.setText(R.string.mybriefcase_cn);
			this.btn_businesscard.setImageResource(R.drawable.businesscard_cn);
			this.btn_businesscard_holder.setImageResource(R.drawable.businesscardholder_cn);
			this.btn_memo.setImageResource(R.drawable.memo_cn);
			this.btn_schedule.setImageResource(R.drawable.schedule_cn);
			this.btn_present.setImageResource(R.drawable.gift_cn);
			break;
		}
	}
	public void setBannerImage(){
		try{
			ImageDownloader.download(StaticData.DEFAULT_URL+StaticData.bannderDto.MYBRIEFACASE_BANNER, btn_advertising);
			btn_advertising.setVisibility(ImageView.VISIBLE);
		}catch(Exception e){
		}
	}
}
