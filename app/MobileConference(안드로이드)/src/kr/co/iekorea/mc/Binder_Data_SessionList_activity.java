package kr.co.iekorea.mc;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import kr.co.iekorea.mc.adapter.BinderSessionListAdapter;
import kr.co.iekorea.mc.event.BinderDataSessionEventListener;
import kr.co.iekorea.mc.staticdata.StaticData;
import kr.co.iekorea.mc.util.BaseInterface;
import kr.co.iekorea.mc.util.ProcessManager;
import kr.co.iekorea.mc.util.WeakAsyncTask;
import kr.co.iekorea.mc.xml.BinderSessionListDto;
import kr.co.iekorea.mc.xml.MCDao;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class Binder_Data_SessionList_activity extends Activity implements BaseInterface {

	// Activity control.
	private ProcessManager processManager;

	// layout area
	private ImageButton btn_back;
	private LinearLayout layout_header;
//	private LinearLayout layout_list_tap;
	private LinearLayout layout_progressbar;
	private ListView list;
//	private Gallery gallery_date;
//	private ViewPager viewPager;

	// date
	private SimpleDateFormat sdf;
	private int nowDay;
	private int nowMonth;

	private ArrayList<BinderSessionListDto> BinderSessionList = new ArrayList<BinderSessionListDto>();// Binder session list

	// adpater
//	private BinderSessionGalleryAdapter GAdapter;
//	private BinderSessionViewPagerAdapter VAdapter;
	
	private BinderSessionListAdapter adapter;
	
	// contents
	private TextView total_title;
	private TextView text_wait;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.initView();
		if (StaticData.logined) {
			this.getNowDate();
			this.getXmlResources();
			this.modifyXmlResources();
			new getSessionList(Binder_Data_SessionList_activity.this).execute();
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
		this.processManager.deleteActivity(Binder_Data_SessionList_activity.this);
	}

	@Override
	public void onBackPressed() {
		Intent intent = new Intent(this, Binder_Data_Activity.class);
		startActivity(intent);
		overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
		super.finish();
	}

	@Override
	public void initView() {
		this.setContentView(R.layout.activity_binder_session);
		this.processManager = ProcessManager.getInstance();
		this.processManager.addActivity(Binder_Data_SessionList_activity.this);
	}

	@Override
	public void getXmlResources() {
		this.btn_back = (ImageButton) this.findViewById(R.id.btn_back);
		this.layout_header = (LinearLayout) this.findViewById(R.id.layout_header);
		this.list = (ListView) this.findViewById(R.id.list);
		
//		this.layout_list_tap = (LinearLayout) this.findViewById(R.id.layout_list_tap);
//		this.gallery_date = (Gallery) this.findViewById(R.id.gallery_date);
//		this.viewPager = (ViewPager) this.findViewById(R.id.list_viewpager);
		
		// next date set area
		this.layout_progressbar = (LinearLayout) this.findViewById(R.id.layout_progressbar);
		
		// contents
		this.total_title = (TextView) this.findViewById(R.id.total_title);
		this.text_wait = (TextView) this.findViewById(R.id.text_wait);
	}

	@Override
	public void modifyXmlResources() {
		LayoutParams param;
		param = new LayoutParams(LayoutParams.MATCH_PARENT,StaticData.device_Height / 10);
		this.layout_header.setLayoutParams(param);
//		this.layout_list_tap.setLayoutParams(param);
		
		 
        // 언어 설정에 다른 화면 구성
		switch (StaticData.NOWLANGUAGE) {
		case StaticData.KOREA:
			this.btn_back.setImageResource(R.drawable.b_back_kr);
			this.total_title.setText(getResources().getString(R.string.session_list_ko));
			this.text_wait.setText(getResources().getString(R.string.wait_ko));
			break;
		case StaticData.ENGLISH:
			this.btn_back.setImageResource(R.drawable.b_back_en);
			this.total_title.setText(getResources().getString(R.string.session_list_en));
			this.text_wait.setText(getResources().getString(R.string.wait_en));
			break;
		case StaticData.CHINA:
			this.btn_back.setImageResource(R.drawable.b_back_cn);
			this.total_title.setText(getResources().getString(R.string.session_list_cn));
			this.text_wait.setText(getResources().getString(R.string.wait_cn));
			break;
		}
	}

	public void getNowDate() {
		Date date = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		this.nowMonth = cal.get(Calendar.MONTH) + 1;
		this.nowDay = cal.get(Calendar.DAY_OF_MONTH);
	}

	@Override
	public void setEventListener() {
		this.btn_back.setOnClickListener(new BinderDataSessionEventListener(this, this)); // back button event
		this.list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent(getApplicationContext(), Binder_Data_Session_Time_Activity.class);
				intent.putExtra("session_id", BinderSessionList.get(position).SESSION_ID);
				startActivity(intent);
				overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
				finish();
			}
			
		});
		// list view 
//		this.list.setOnClickListener()
		
		
		
//		this.gallery_date.setAdapter(GAdapter);
//
//		// this.gallery_date.setSelection(agendaCountList.get(0).nowAgendaCount-1);
//		this.viewPager.setAdapter(VAdapter);
//		this.viewPager.setOnPageChangeListener(new OnPageChangeListener() {
//
//			@Override
//			public void onPageSelected(int position) {
//				gallery_date.setSelection(position);
//			}
//
//			@Override
//			public void onPageScrolled(int arg0, float arg1, int arg2) {
//			}
//
//			@Override
//			public void onPageScrollStateChanged(int value_x) {
//			}
//		});
//
//		this.gallery_date
//				.setOnItemSelectedListener(new OnItemSelectedListener() {
//
//					@Override
//					public void onItemSelected(AdapterView<?> parent,
//							View view, int position, long id) {
//						viewPager.setCurrentItem(position, true);
//					}
//
//					@Override
//					public void onNothingSelected(AdapterView<?> parent) {
//					}
//				});
		// this.gallery_date.setSelection(agendaCountList.get(0).nowAgendaCount-1);
		// area adapter select date in here
	}

	public void setSessionAdapter() {
//		this.GAdapter = new BinderSessionGalleryAdapter(this, BinderSessionList);
//		this.VAdapter = new BinderSessionViewPagerAdapter(this, BinderSessionList,this);
		this.adapter = new BinderSessionListAdapter(this, this.BinderSessionList);
		this.list.setAdapter(adapter);
	}

	// ------------------------- back ground thread area
	// -----------------------------------------

	/**
	 * get getAgendaList AsnycTask 일정의 총 세션 리스트를 가져온다.
	 * */
	class getSessionList extends WeakAsyncTask<Void, Void, Void, Activity> {
		public getSessionList(Activity target) {
			super(target);
			// TODO Auto-generated constructor stub
		}

		boolean result = false;
		boolean check = false;
		int count = 0;

		@Override
		protected void onPreExecute(Activity target) {
			layout_progressbar.setVisibility(LinearLayout.VISIBLE);
		}

		@Override
		protected Void doInBackground(Activity target,Void... params) {
			try {
				MCDao dao = new MCDao(Binder_Data_SessionList_activity.this);
				if (Binder_Data_SessionList_activity.this.BinderSessionList != null) {
					Binder_Data_SessionList_activity.this.BinderSessionList.clear();
				}

				check = dao.getBinderSessionList();
				if (check) {
					Binder_Data_SessionList_activity.this.BinderSessionList = dao.getBinderListItem();
					if (Binder_Data_SessionList_activity.this.BinderSessionList != null) {
						// for debug
//						Log.e("asnyc task",
//								"items size : " + BinderSessionList.size());
//						for (int i = 0; i < BinderSessionList.size(); i++) {
//							Log.e("asnyc task", "items.get(i).date : "
//									+ BinderSessionList.get(i).date);
//							for (int j = 0; j < BinderSessionList.get(i).list
//									.size(); j++) {
//								Log.e("asnyc task",
//										"items.get(i).list.get(j).AGENDA_TITLE : "
//												+ BinderSessionList.get(i).list
//														.get(j).AGENDA_TITLE);
//								Log.e("asnyc task",
//										"items.get(i).list.get(j).AGENDA_ID : "
//												+ BinderSessionList.get(i).list
//														.get(j).AGENDA_ID);
//							}
//						}
						
						result = true;
					}
				}
			} catch (Exception e) {
				result = false;
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Activity target,Void param) {
			if (result) {
				// start next background
				Binder_Data_SessionList_activity.this.setSessionAdapter();

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
				// 다시 접속하는 AsnycTask 실행 area
			}
			layout_progressbar.setVisibility(LinearLayout.GONE);
			Binder_Data_SessionList_activity.this.setEventListener();
		}
	}
}
