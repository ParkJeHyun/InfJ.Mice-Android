package kr.co.iekorea.mc;

import kr.co.iekorea.mc.event.BinderEventListener;
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

public class Binder_Activity extends Activity implements BaseInterface {
	
	// activity control
	private ProcessManager processManager;
	
	// activity layout
	private LinearLayout layout_header;
	private ImageButton btn_back;
	private ImageButton btn_data,btn_sponsor,btn_stayhome;
	private ImageView btn_advertising;
	
	// 언어
	private TextView total_title;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
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
		this.processManager.deleteActivity(Binder_Activity.this);
		
	}
	
	@Override
	public void onBackPressed() {
		Intent intent = new Intent(getApplicationContext(), Main_Activity.class);
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
		this.setContentView(R.layout.activity_binder);
		this.processManager = ProcessManager.getInstance();
		this.processManager.addActivity(Binder_Activity.this);
	}

	@Override
	public void getXmlResources() {
		this.layout_header = (LinearLayout) this.findViewById(R.id.layout_header);
		this.btn_back = (ImageButton) this.findViewById(R.id.btn_back);
		this.btn_data = (ImageButton) this.findViewById(R.id.btn_data);
		this.btn_sponsor = (ImageButton) this.findViewById(R.id.btn_sponsor);
		this.btn_stayhome = (ImageButton) this.findViewById(R.id.btn_stayhome);
		
		this.btn_advertising = (ImageView) this.findViewById(R.id.btn_advertising);
		
		//언어
		this.total_title = (TextView) this.findViewById(R.id.total_title);
		
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
			this.btn_data.setImageResource(R.drawable.data_kr);
			this.btn_sponsor.setImageResource(R.drawable.sponsor_kr);
			this.total_title.setText(getResources().getString(R.string.binder_ko));
			break;
		case StaticData.ENGLISH:
			this.btn_back.setImageResource(R.drawable.b_back_en);
			this.btn_data.setImageResource(R.drawable.data_en);
			this.btn_sponsor.setImageResource(R.drawable.sponsor_en);
			this.total_title.setText(getResources().getString(R.string.binder_en));
			break;
		case StaticData.CHINA:
			this.btn_back.setImageResource(R.drawable.b_back_cn);
			this.btn_data.setImageResource(R.drawable.data_cn);
			this.btn_sponsor.setImageResource(R.drawable.sponsor_cn);
			this.total_title.setText(getResources().getString(R.string.binder_cn));
			break;
		}
	}

	@Override
	public void setEventListener() {
		this.btn_back.setOnClickListener(new BinderEventListener(this,this));
		this.btn_data.setOnClickListener(new BinderEventListener(this,this));
		this.btn_sponsor.setOnClickListener(new BinderEventListener(this,this));
		this.btn_stayhome.setOnClickListener(new BinderEventListener(this, this));
		
		this.btn_advertising.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				try{
					Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(StaticData.bannderDto.BINDER_LANDING_URL));
					startActivity(intent);
				}catch(Exception e){}
			}
		});
		
		this.setBannerImage();
	}
	
	public void setBannerImage(){
		try{
			ImageDownloader.download(StaticData.DEFAULT_URL+StaticData.bannderDto.BINDER_BANNER, btn_advertising);
			btn_advertising.setVisibility(ImageView.VISIBLE);
		}catch(Exception e){
		}
	}
}