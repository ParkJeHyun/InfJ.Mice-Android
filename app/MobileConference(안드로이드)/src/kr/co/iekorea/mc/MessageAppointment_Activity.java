package kr.co.iekorea.mc;

import java.util.ArrayList;

import kr.co.iekorea.mc.adapter.MessageAppointmentAdapter;
import kr.co.iekorea.mc.event.MessageAppointmentEventListener;
import kr.co.iekorea.mc.staticdata.StaticData;
import kr.co.iekorea.mc.util.BaseInterface;
import kr.co.iekorea.mc.util.ProcessManager;
import kr.co.iekorea.mc.util.WeakAsyncTask;
import kr.co.iekorea.mc.xml.AppointmentsDto;
import kr.co.iekorea.mc.xml.MessageDAO;
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

public class MessageAppointment_Activity extends Activity implements BaseInterface {
	// Activity control
	private ProcessManager processManager;
	
	// layout
	public LinearLayout layout_progressbar;
	private LinearLayout layout_header;
	private ImageButton btn_back;
	private ImageButton btn_appointment_add;
	private ImageButton btn_appointment_delete;
	private ListView list;
	
	// for adapter
	public MessageAppointmentAdapter adapter;
	private ArrayList<AppointmentsDto> items;
	
	// contents
	private TextView total_title,text_wait;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.initView();
		if (StaticData.logined) {
			this.getXmlResources();
			this.modifyXmlResources();
			
			new setAppointmentAdd(MessageAppointment_Activity.this).execute();
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
	
	public void setAppointmentsAdapter(){
		this.adapter = new MessageAppointmentAdapter(this,items,this);
		this.list.setAdapter(adapter);
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		this.processManager.deleteActivity(MessageAppointment_Activity.this);
	}
	
	@Override
	public void onBackPressed() {
		if(adapter == null){
			Intent intent = new Intent(getApplicationContext(), Message_Activity.class);
			startActivity(intent);
			overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
			finish();
		}else{
			if(adapter.deleteMode){
				adapter.changeDeleteMode();
			}else{
				Intent intent = new Intent(getApplicationContext(), Message_Activity.class);
				startActivity(intent);
				overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
				finish();
			}
		}
	}
	
	@Override
	public void finish() {
		super.finish();
		overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
	}

	@Override
	public void initView() {
		this.setContentView(R.layout.activity_message_appointments);
		this.processManager = ProcessManager.getInstance();
		this.processManager.addActivity(MessageAppointment_Activity.this);
	}

	@Override
	public void getXmlResources() {
		this.layout_header = (LinearLayout) this.findViewById(R.id.layout_header);
		this.btn_back = (ImageButton) this.findViewById(R.id.btn_back);
		this.btn_appointment_add = (ImageButton) this.findViewById(R.id.btn_appointment_add);
		this.btn_appointment_delete = (ImageButton) this.findViewById(R.id.btn_appointment_delete);
		this.list = (ListView) this.findViewById(R.id.list);
		this.layout_progressbar = (LinearLayout) this.findViewById(R.id.layout_progressbar);
		
		this.total_title = (TextView) this.findViewById(R.id.total_title);
		this.text_wait = (TextView) this.findViewById(R.id.text_wait);
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
			this.btn_appointment_add.setImageResource(R.drawable.b2_add_kr);
			this.btn_appointment_delete.setImageResource(R.drawable.b2_delete_kr);
			this.total_title.setText(getResources().getString(R.string.appointments_ko));
			this.text_wait.setText(getResources().getString(R.string.wait_ko));
			break;
		case StaticData.ENGLISH:
			this.btn_back.setImageResource(R.drawable.b_back_en);
			this.btn_appointment_add.setImageResource(R.drawable.b2_add_en);
			this.btn_appointment_delete.setImageResource(R.drawable.b2_delete_en);
			this.total_title.setText(getResources().getString(R.string.appointments_en));
			this.text_wait.setText(getResources().getString(R.string.wait_en));
			break;
		case StaticData.CHINA:
			this.btn_back.setImageResource(R.drawable.b_back_cn);
			this.btn_appointment_add.setImageResource(R.drawable.b2_add_cn);
			this.btn_appointment_delete.setImageResource(R.drawable.b2_delete_cn);
			this.total_title.setText(getResources().getString(R.string.appointments_cn));
			this.text_wait.setText(getResources().getString(R.string.wait_cn));
			break;
		}
	}

	@Override
	public void setEventListener() {
		this.btn_back.setOnClickListener(new MessageAppointmentEventListener(this,this));
		this.btn_appointment_add.setOnClickListener(new MessageAppointmentEventListener(this, this));
		this.btn_appointment_delete.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				adapter.changeDeleteMode();
			}
		});
		
		this.list.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if(adapter.deleteMode){
				}else{
//				// item click 시 해당 id를 가져와 뿌려준다.
					Intent intent = new Intent(getApplicationContext(), MessageAppointmentsShow_Activity.class);
					intent.putExtra("promise_id", items.get(position).PROMISE_ID);
					startActivity(intent);
					overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
					finish();
				}
			}
		});
	}
	
	class setAppointmentAdd extends WeakAsyncTask<Void, Void, Void,Activity>{
		public setAppointmentAdd(Activity target) {
			super(target);
			// TODO Auto-generated constructor stub
		}

		boolean result;
		@Override
		protected void onPreExecute(Activity target) {
			layout_progressbar.setVisibility(LinearLayout.VISIBLE);
		}
		
		@Override
		protected Void doInBackground(Activity target,Void... params) {
			MessageDAO dao = new MessageDAO(MessageAppointment_Activity.this);
			try {
				result = dao.setAppointmentsList();
			} catch (Exception e) {
				result = false;
			}
			
			if(result){
				if(items != null){
					items.clear();
				}
				items = dao.getAppointmentsDtoList();
			}
			return null;
		}
		
		@Override
		protected void onPostExecute(Activity target,Void result) {
			if(this.result){
				setAppointmentsAdapter();
				setEventListener();
			}else{
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

			}
			layout_progressbar.setVisibility(LinearLayout.GONE);
		}
	}
}
