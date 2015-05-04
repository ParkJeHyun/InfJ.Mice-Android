package kr.co.iekorea.mc;

import java.util.ArrayList;

import kr.co.iekorea.mc.adapter.AgendaTimeAdapter;
import kr.co.iekorea.mc.staticdata.StaticData;
import kr.co.iekorea.mc.util.BaseInterface;
import kr.co.iekorea.mc.util.ProcessManager;
import kr.co.iekorea.mc.util.WeakAsyncTask;
import kr.co.iekorea.mc.xml.AgendaTimeItemsDto;
import kr.co.iekorea.mc.xml.MCDao;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class Agenda_Time_Activity extends Activity implements BaseInterface {
	private ProcessManager processManager;
	
	// activity layout
	private LinearLayout layout_header;
	private ImageButton btn_back;
	private ListView list;
	private LinearLayout layout_progressbar;
	
	// for test
	private int session_id;
	private ArrayList<AgendaTimeItemsDto> timeList;
	
	// for list
	private AgendaTimeAdapter adapter;
	
	// contents
	private TextView total_title; 
	private TextView total_wait; 
	private String conferenceDate;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.initView();
		if (StaticData.logined) {
			this.getIntentValues();
			this.getXmlResources();
			this.modifyXmlResources();
			new getDetailAgenda(Agenda_Time_Activity.this).execute();
		} else {
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
			Intent intent = new Intent(getApplicationContext(),Login_Activity.class);
			startActivity(intent);
			overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
			finish();
		}
	}
	
	public void getIntentValues(){
		Intent intent = getIntent();
		this.session_id = intent.getIntExtra("session_id", 0);
	}
	
	public void setAdapter(){
		this.adapter = new AgendaTimeAdapter(this, timeList);
		this.list.setAdapter(adapter);
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		this.processManager.deleteActivity(Agenda_Time_Activity.this);
	}
	
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		this.finish();
	}
	
	@Override
	public void finish() {
		super.finish();
		overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
	}

	@Override
	public void initView() {
		this.setContentView(R.layout.activity_agenda_time);
		this.processManager = ProcessManager.getInstance();
		this.processManager.addActivity(Agenda_Time_Activity.this);
		this.timeList = new ArrayList<AgendaTimeItemsDto>();
	}

	@Override
	public void getXmlResources() {
		this.btn_back = (ImageButton) this.findViewById(R.id.btn_back);
		this.layout_header = (LinearLayout) this.findViewById(R.id.layout_header);
		this.list = (ListView) this.findViewById(R.id.list);
		this.layout_progressbar = (LinearLayout) this.findViewById(R.id.layout_progressbar);
		this.total_title = (TextView)  this.findViewById(R.id.total_title);
		this.total_wait = (TextView)  this.findViewById(R.id.text_wait);
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
			this.total_title.setText(getResources().getString(R.string.sessioninfo_ko));
			this.total_wait.setText(getResources().getString(R.string.wait_ko));
			break;
		case StaticData.ENGLISH:
			this.btn_back.setImageResource(R.drawable.b_back_en);
			this.total_title.setText(getResources().getString(R.string.sessioninfo_en));
			this.total_wait.setText(getResources().getString(R.string.wait_en));
			break;
		case StaticData.CHINA:
			this.btn_back.setImageResource(R.drawable.b_back_cn);
			this.total_title.setText(getResources().getString(R.string.sessioninfo_cn));
			this.total_wait.setText(getResources().getString(R.string.wait_cn));
			break;
        }
	}

	@Override
	public void setEventListener() {
//		this.btn_back.setOnClickListener(new AgendaTimeEventListener(this,this));
		this.btn_back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
				finish();				
			}
		});
		
		this.list.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent(getApplicationContext(),AgendaTimeSessionContents_Activity.class);
				intent.putExtra("agenda_id", timeList.get(position).AGENDA_ID);
				intent.putExtra("conferenceDate", conferenceDate);
				startActivity(intent);
				overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
			}
		});
	}
	
	class getDetailAgenda extends WeakAsyncTask<Void,Void,Void,Activity>
	{	
		public getDetailAgenda(Activity target) {
			super(target);
		}

		boolean result = false;
		boolean check = false;
		
		@Override
		protected void onPreExecute(Activity target)
		{
			layout_progressbar.setVisibility(LinearLayout.VISIBLE);
		}
		
		@Override
		protected Void doInBackground(Activity target,Void... params) 
		{
			MCDao dao = new MCDao(Agenda_Time_Activity.this);
			try
			{
				result = dao.getDetailAgenda(Agenda_Time_Activity.this.session_id);
			}
			catch(Exception e) {
				result = false;
				e.printStackTrace();
			}
			if(result){
				if(Agenda_Time_Activity.this.timeList != null){
					Agenda_Time_Activity.this.timeList.clear();
				}
				conferenceDate = dao.getConferenceDate();
				Agenda_Time_Activity.this.timeList = dao.getTiemList();
			}
			return null;
		}
		
		@Override
		protected void onPostExecute(Activity target,Void param)
		{
			if(result){
				Agenda_Time_Activity.this.setAdapter();
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
			layout_progressbar.setVisibility(LinearLayout.GONE);
			Agenda_Time_Activity.this.setEventListener();
		}
	}
}
