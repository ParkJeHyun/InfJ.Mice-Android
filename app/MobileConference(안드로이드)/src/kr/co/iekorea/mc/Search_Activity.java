package kr.co.iekorea.mc;

import kr.co.iekorea.mc.event.SearchEventListener;
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

public class Search_Activity extends Activity implements BaseInterface {
	// Activity control.
	private ProcessManager processManager;

	// layout area
	private ImageButton btn_back;
	private LinearLayout layout_header;
	private LinearLayout layout_progressbar;
	private ImageView btn_advertising;
	
	// button
	private ImageButton btn_search_user,btn_search_abstract,btn_search_stay;
	
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
		this.processManager.deleteActivity(this);
	}

	@Override
	public void onBackPressed() {
		Intent intent = new Intent(this,Main_Activity.class);
		startActivity(intent);
		overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
		finish();
	}

	@Override
	public void finish() {
		super.finish();
		overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
	}

	@Override
	public void initView() {
		this.setContentView(R.layout.activity_search);
		this.processManager = ProcessManager.getInstance();
		this.processManager.addActivity(this);
	}

	@Override
	public void getXmlResources() {
		this.btn_back = (ImageButton) this.findViewById(R.id.btn_back);
		this.layout_header = (LinearLayout) this.findViewById(R.id.layout_header);
		this.layout_progressbar = (LinearLayout) this.findViewById(R.id.layout_progressbar);
		
		this.btn_search_user = (ImageButton) this.findViewById(R.id.btn_search_user);
		this.btn_search_abstract = (ImageButton) this.findViewById(R.id.btn_search_abstract);
		this.btn_search_stay = (ImageButton) this.findViewById(R.id.btn_search_stay);
		this.btn_advertising = (ImageView) this.findViewById(R.id.btn_advertising);
		
		this.total_title = (TextView) this.findViewById(R.id.total_title);
	}

	@Override
	public void modifyXmlResources() {
		LayoutParams param;
		param = new LayoutParams(LayoutParams.MATCH_PARENT,StaticData.device_Height / 10);
		this.layout_header.setLayoutParams(param);

        // 언어 설정에 다른 화면 구성
		switch (StaticData.NOWLANGUAGE) {
		case StaticData.KOREA:
			this.btn_back.setImageResource(R.drawable.b_back_kr);
			this.total_title.setText(R.string.search_ko);
			this.btn_search_user.setImageResource(R.drawable.participant_kr);
			this.btn_search_abstract.setImageResource(R.drawable.abstract_kr);
			this.btn_search_stay.setImageResource(R.drawable.accommodation_restaurant_kr);
			break;
		case StaticData.ENGLISH:
			this.btn_back.setImageResource(R.drawable.b_back_en);
			this.total_title.setText(R.string.search_en);
			this.btn_search_user.setImageResource(R.drawable.participant_en);
			this.btn_search_abstract.setImageResource(R.drawable.abstract_en);
			this.btn_search_stay.setImageResource(R.drawable.accommodation_restaurant_en);
			break;
		case StaticData.CHINA:
			this.btn_back.setImageResource(R.drawable.b_back_cn);
			this.total_title.setText(R.string.search_cn);
			this.btn_search_user.setImageResource(R.drawable.participant_cn);
			this.btn_search_abstract.setImageResource(R.drawable.abstract_cn);
			this.btn_search_stay.setImageResource(R.drawable.accommodation_restaurant_cn);
			break;
		}
	}

	@Override
	public void setEventListener() {
		this.btn_back.setOnClickListener(new SearchEventListener(this,this));
		
		this.btn_search_user.setOnClickListener(new SearchEventListener(this, this));		// 참가자
		this.btn_search_stay.setOnClickListener(new SearchEventListener(this, this));		// 숙박
		this.btn_search_abstract.setOnClickListener(new SearchEventListener(this, this));	// 초록
		
		this.btn_advertising.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				try{
					Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(StaticData.bannderDto.SEARCH_LANDING_URL));
					startActivity(intent);
				}catch(Exception e){}
			}
		});
		
		this.setBannerImage();
	}
	
	public void setBannerImage(){
		try{
			ImageDownloader.download(StaticData.DEFAULT_URL+StaticData.bannderDto.SEARCH_BANNER, btn_advertising);
		}catch(Exception e){
		}
	}
}
