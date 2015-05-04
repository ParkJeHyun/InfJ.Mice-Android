package kr.co.iekorea.mc;

import java.util.ArrayList;

import kr.co.iekorea.mc.adapter.MessageBoxAdapter;
import kr.co.iekorea.mc.event.MessageInBoxEventListener;
import kr.co.iekorea.mc.staticdata.StaticData;
import kr.co.iekorea.mc.util.BaseInterface;
import kr.co.iekorea.mc.util.ProcessManager;
import kr.co.iekorea.mc.util.WeakAsyncTask;
import kr.co.iekorea.mc.xml.MessageBoxDto;
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

public class MessageInbox_Activity extends Activity implements BaseInterface {
	// Activity control.
	private ProcessManager processManager;

	// layout area
	private ImageButton btn_back;
	private LinearLayout layout_header;
	private LinearLayout layout_progressbar;
	
	// layout detail
	private ListView list;
	private ImageButton btn_delete;
	
	// adapter
	public MessageBoxAdapter adapter;
	private ArrayList<MessageBoxDto> items;
	
	// flag
	private boolean boxState = true;
	
	// 언어
	private TextView total_title,text_wait;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.initView();
		
		if (StaticData.logined) {
			this.getXmlResources();
			this.modifyXmlResources();

			new setMessageInbox(MessageInbox_Activity.this).execute();
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
	
	public void setMessageBoxAdapter(){
		this.adapter = new MessageBoxAdapter(this, items, boxState, this);
		this.list.setAdapter(adapter);
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		this.processManager.deleteActivity(this);
	}
	
	@Override
	public void onBackPressed() {
		if(adapter != null){
			if(adapter.deleteMode){
				adapter.changeDeleteMode();
			}else{
				Intent intent = new Intent(this, Message_Activity.class);
				startActivity(intent);
				finish();
			}
		}else{
			Intent intent = new Intent(this, Message_Activity.class);
			startActivity(intent);
			finish();			
		}
	}
	
	@Override
	public void finish() {
		super.finish();
		overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
	}

	@Override
	public void initView() {
		this.setContentView(R.layout.activity_message_inbox);
		this.processManager = ProcessManager.getInstance();
		this.processManager.addActivity(this);
		
		this.items = new ArrayList<MessageBoxDto>();
	}

	@Override
	public void getXmlResources() {
		this.btn_back = (ImageButton) this.findViewById(R.id.btn_back);
		this.layout_header = (LinearLayout) this.findViewById(R.id.layout_header);
		this.layout_progressbar = (LinearLayout) this.findViewById(R.id.layout_progressbar);
		
		this.btn_delete = (ImageButton) this.findViewById(R.id.btn_delete);
		this.list = (ListView) this.findViewById(R.id.list);
		
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
			this.btn_delete.setImageResource(R.drawable.b1_delete_kr);
			this.total_title.setText(R.string.inbox_ko);
			this.text_wait.setText(R.string.wait_ko);
			break;
		case StaticData.ENGLISH:
			this.btn_back.setImageResource(R.drawable.b_back_en);
			this.btn_delete.setImageResource(R.drawable.b1_delete_en);
			this.total_title.setText(R.string.inbox_en);
			this.text_wait.setText(R.string.wait_en);
			break;
		case StaticData.CHINA:
			this.btn_back.setImageResource(R.drawable.b_back_cn);
			this.btn_delete.setImageResource(R.drawable.b1_delete_cn);
			this.total_title.setText(R.string.inbox_cn);
			this.text_wait.setText(R.string.wait_cn);
			break;
		}
	}

	@Override
	public void setEventListener() {
		this.btn_back.setOnClickListener(new MessageInBoxEventListener(this,this));
		this.list.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
				if(adapter.deleteMode){
				}else{
					Intent intent = new Intent(getApplicationContext(), MessageInboxDetail_Activity.class);
					
					// 수신함 from
					intent.putExtra("message_id", items.get(position).MESSAGE_ID);
					startActivity(intent);
					overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
					finish();
				}
			}
		});

		/** 삭제 버튼 */
		this.btn_delete.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				adapter.changeDeleteMode();
			}
		});
	}
	
	// 수신함 확인
	class setMessageInbox extends WeakAsyncTask<Void, Void, Void,Activity>{
		public setMessageInbox(Activity target) {
			super(target);
			// TODO Auto-generated constructor stub
		}

		boolean result = false;
		
		@Override
		protected void onPreExecute(Activity target) {
			layout_progressbar.setVisibility(LinearLayout.VISIBLE);
		}

		@Override
		protected Void doInBackground(Activity target,Void... params) {
			MessageDAO dao = new MessageDAO(MessageInbox_Activity.this);
			try {
				// input id values 1 for test
				result = dao.setMessageInbox();
				if (result) {
					if (items != null) {
						items.clear();
					}
					items = dao.getMessageInboc();
				}
			} catch (Exception e) {
				result = false;
				e.printStackTrace();
			}
			return null;
		}
		
		@Override
		protected void onPostExecute(Activity target,Void parm) {
			if(result){
				setMessageBoxAdapter();
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
			setEventListener();
			layout_progressbar.setVisibility(LinearLayout.GONE);
		}
		
	}
}
