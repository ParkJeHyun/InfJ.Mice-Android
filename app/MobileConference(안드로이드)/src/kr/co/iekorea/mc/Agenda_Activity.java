package kr.co.iekorea.mc;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import kr.co.iekorea.mc.adapter.AgendaGalleryAdapter;
import kr.co.iekorea.mc.adapter.AgendaViewPagerAdapter;
import kr.co.iekorea.mc.staticdata.StaticData;
import kr.co.iekorea.mc.util.BaseInterface;
import kr.co.iekorea.mc.util.CustomLog;
import kr.co.iekorea.mc.util.ImageDownloader;
import kr.co.iekorea.mc.util.ProcessManager;
import kr.co.iekorea.mc.util.WeakAsyncTask;
import kr.co.iekorea.mc.xml.AgendaSessionDto;
import kr.co.iekorea.mc.xml.MCDao;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Gallery;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class Agenda_Activity extends Activity implements BaseInterface {
	// 03/04
	// Activity control.
	private ProcessManager processManager;
	
	// layout area
	private ImageButton btn_back;
	private LinearLayout layout_header;
	private LinearLayout layout_list_tap;
	private ListView list;
	private LinearLayout layout_progressbar;
	private Gallery gallery_date;
	private ViewPager viewPager;
	private ImageView btn_advertising;
	
	// date
	private SimpleDateFormat sdf;
	private int nowDay;
	private int nowMonth;
	
	private ArrayList<AgendaSessionDto> agendaSessionList = new ArrayList<AgendaSessionDto>();// agenda session list
	
	// adpater
	private AgendaGalleryAdapter GAdapter;
	private AgendaViewPagerAdapter VAdapter;
	
	// contents
	private TextView total_title,text_wait;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.initView();
//		this.getNowDate();
		if (StaticData.logined) {
			this.getXmlResources();
			this.modifyXmlResources();
			new getAgendaList(Agenda_Activity.this).execute();
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
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		this.processManager.deleteActivity(Agenda_Activity.this);
	}
	
	@Override
	public void onBackPressed() {
		Intent intent = new Intent(this, Main_Activity.class);
		startActivity(intent);
		overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
		super.finish();
	}
	
	@Override
	public void initView() {
		this.setContentView(R.layout.activity_agenda);
		this.processManager = ProcessManager.getInstance();
		this.processManager.addActivity(Agenda_Activity.this);
	}

	@Override
	public void getXmlResources() {
		this.btn_back = (ImageButton) this.findViewById(R.id.btn_back);
		
		this.layout_header = (LinearLayout) this.findViewById(R.id.layout_header);
		this.layout_list_tap = (LinearLayout) this.findViewById(R.id.layout_list_tap);
		this.gallery_date = (Gallery) this.findViewById(R.id.gallery_date);
		// next date set area
		this.list = (ListView) this.findViewById(R.id.list);
		this.layout_progressbar = (LinearLayout) this.findViewById(R.id.layout_progressbar);
		this.viewPager = (ViewPager) this.findViewById(R.id.list_viewpager);
		this.btn_advertising = (ImageView) this.findViewById(R.id.btn_advertising);
		
		// contents
		this.total_title = (TextView) this.findViewById(R.id.total_title);
		this.text_wait = (TextView) this.findViewById(R.id.text_wait);
	}

	@Override
	public void modifyXmlResources() {
		LayoutParams param;
        param = new LayoutParams(LayoutParams.MATCH_PARENT,StaticData.device_Height/10);
        this.layout_header.setLayoutParams(param);
        this.layout_list_tap.setLayoutParams(param);

        // 언어 설정에 다른 화면 구성
		switch (StaticData.NOWLANGUAGE) {
		case StaticData.KOREA:
			this.btn_back.setImageResource(R.drawable.b_back_kr);
			this.total_title.setText(getResources().getString(R.string.agenda_ko));
			this.text_wait.setText(getResources().getString(R.string.wait_ko));
			break;
		case StaticData.ENGLISH:
			this.btn_back.setImageResource(R.drawable.b_back_en);
			this.total_title.setText(getResources().getString(R.string.agenda_en));
			this.text_wait.setText(getResources().getString(R.string.wait_en));
			break;
		case StaticData.CHINA:
			this.btn_back.setImageResource(R.drawable.b_back_cn);
			this.total_title.setText(getResources().getString(R.string.agenda_cn));
			this.text_wait.setText(getResources().getString(R.string.wait_cn));
			break;
        }
	}
	
	public void getNowDate(){
		Date date = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		this.nowMonth = cal.get(Calendar.MONTH)+1;
		this.nowDay = cal.get(Calendar.DAY_OF_MONTH);
	}

	@Override
	public void setEventListener() {
//		this.btn_back.setOnClickListener(new AgendaEventListener(this,this));		// back  button event
		this.btn_back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(), Main_Activity.class);
				startActivity(intent);
				overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
				finish();				
			}
		});		// back  button event
		this.gallery_date.setAdapter(GAdapter);

//		this.gallery_date.setSelection(agendaCountList.get(0).nowAgendaCount-1);
		this.viewPager.setAdapter(VAdapter);
		this.viewPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int position) {
				gallery_date.setSelection(position);
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}

			@Override
			public void onPageScrollStateChanged(int value_x) {
			}
		});
		
		this.gallery_date.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				viewPager.setCurrentItem(position, true);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
			}
		});
		
		this.btn_advertising.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				try{
					Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(StaticData.bannderDto.AGENDA_LANDING_URL));
					startActivity(intent);
				}catch(Exception e){}
			}
		});
		
		this.setBannerImage();
		
//		this.gallery_date.setSelection(agendaCountList.get(0).nowAgendaCount-1);
		// area adapter select date in here
	}
	
	public boolean setAgendaAdapter(){
		boolean result = false;
		
		// step1 -> view pager adapter setting in here area
		this.GAdapter = new AgendaGalleryAdapter(this,agendaSessionList);
		this.VAdapter = new AgendaViewPagerAdapter(this, agendaSessionList, this);
		
		return result;
	}
	
	// ------------------------- back ground thread area -----------------------------------------
	/** 
	 * get getAgendaList AsnycTask
	 * 일정의 총 세션 리스트를 가져온다.
	 * */
	class getAgendaList extends WeakAsyncTask<Void,Void,Void,Activity>
	{	
		public getAgendaList(Activity target) {
			super(target);
			// TODO Auto-generated constructor stub
		}

		boolean result = false;
		boolean check = false;
		int count = 0;
		
		@Override
		protected void onPreExecute(Activity target)
		{
			layout_progressbar.setVisibility(LinearLayout.VISIBLE);
		}
		
		@Override
		protected Void doInBackground(Activity target, Void... params) {
			try
			{
				MCDao dao = new MCDao(Agenda_Activity.this);
				if(Agenda_Activity.this.agendaSessionList!= null){
					Agenda_Activity.this.agendaSessionList.clear();
				}
				
				check = dao.getAgendaList();
				if(check){
					Agenda_Activity.this.agendaSessionList = dao.getAgendaListItem();
					if(Agenda_Activity.this.agendaSessionList != null){
						result = true;
					}
				}
			}
			catch(Exception e) {
				result = false;
				e.printStackTrace();
			}
			return null;
		}
		
		@Override
		protected void onPostExecute(Activity target,Void param)
		{
			if(result){
					// start next background
				Agenda_Activity.this.setAgendaAdapter();
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
				// 다시 접속하는 AsnycTask 실행 area
			}
			Agenda_Activity.this.setEventListener();
			layout_progressbar.setVisibility(LinearLayout.GONE);
		}
	}
	
	public void setBannerImage(){
		try{
			CustomLog.e("", "StaticData.bannderDto.AGENDA_BANNER: "+StaticData.bannderDto.AGENDA_BANNER);
			ImageDownloader.download(StaticData.DEFAULT_URL+StaticData.bannderDto.AGENDA_BANNER, btn_advertising);
			btn_advertising.setVisibility(ImageView.VISIBLE);
		}catch(Exception e){
		}
	}
	
//	@Override
//	public void setEventListener() {
//		this.btn_advertising.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(StaticData.bannderDto.MAIN_LANDING_URL));
//				startActivity(intent);
//			}
//		});
//	}
}
