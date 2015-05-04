package kr.co.iekorea.mc;

import kr.co.iekorea.mc.staticdata.StaticData;
import kr.co.iekorea.mc.util.BaseInterface;
import kr.co.iekorea.mc.util.ImageDownloader;
import kr.co.iekorea.mc.util.ProcessManager;
import kr.co.iekorea.mc.util.WeakAsyncTask;
import net.appsone.oneprint.util.ImgViewPintTouch;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

public class SponsorView_Activity extends Activity implements BaseInterface {
	// activity control
	private ProcessManager processManager;
	private String detail_url;
	
	// layout area
	private ImageButton btn_back;
	private LinearLayout layout_header;
	private LinearLayout layout_progressbar;
	
	private ImgViewPintTouch image_sponsorview;
	
	// contents
	private TextView total_title;
	private TextView text_wait;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.initView();
		if (StaticData.logined) {
			this.getDefaultValues();
			this.getXmlResources();
			this.modifyXmlResources();
			this.setImage();
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
	
	public void getDefaultValues(){
		Intent intent = getIntent();
		this.detail_url = intent.getStringExtra("detail_url");
	}
	
	public void setImage(){
		new executeSetImage(SponsorView_Activity.this).execute();
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		this.processManager.deleteActivity(SponsorView_Activity.this);
	}
	
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		finish();
	}
	
	@Override
	public void finish() {
		super.finish();
		overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
	}

	@Override
	public void initView() {
		this.setContentView(R.layout.activity_binder_sponsor_view);
		this.processManager = ProcessManager.getInstance();
		this.processManager.addActivity(SponsorView_Activity.this);
	}

	@Override
	public void getXmlResources() {
		this.btn_back = (ImageButton) this.findViewById(R.id.btn_back);
		this.layout_header = (LinearLayout) this
				.findViewById(R.id.layout_header);
		this.layout_progressbar = (LinearLayout) this
				.findViewById(R.id.layout_progressbar);
		
		this.image_sponsorview = (ImgViewPintTouch) this.findViewById(R.id.image_sponsorview);
		
		// contents
		this.total_title = (TextView) this.findViewById(R.id.total_title);
		this.text_wait = (TextView) this.findViewById(R.id.text_wait);
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
			this.total_title.setText(getResources().getString(R.string.sponsor_ko));
			this.text_wait.setText(getResources().getString(R.string.wait_ko));
			break;
		case StaticData.ENGLISH:
			this.btn_back.setImageResource(R.drawable.b_back_en);
			this.total_title.setText(getResources().getString(R.string.sponsor_en));
			this.text_wait.setText(getResources().getString(R.string.wait_en));
			break;
		case StaticData.CHINA:
			this.btn_back.setImageResource(R.drawable.b_back_cn);
			this.total_title.setText(getResources().getString(R.string.sponsor_cn));
			this.text_wait.setText(getResources().getString(R.string.wait_cn));
			break;
		}
	}

	@Override
	public void setEventListener() {
		this.btn_back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}
	
	class executeSetImage extends WeakAsyncTask<Void, Void, Void,Activity>{
		
		public executeSetImage(Activity target) {
			super(target);
		}

		@Override
		protected void onPreExecute(Activity target) {
			layout_progressbar.setVisibility(LinearLayout.VISIBLE);
		}

		@Override
		protected Void doInBackground(Activity target,Void... params) {
			ImageDownloader.download(StaticData.DEFAULT_URL+detail_url, image_sponsorview);
			return null;
		}
		
		@Override
		protected void onPostExecute(Activity target,Void result) {
			setEventListener();
			layout_progressbar.setVisibility(LinearLayout.GONE);
		}
	}
}
