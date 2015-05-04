package kr.co.iekorea.mc;

import java.util.ArrayList;

import kr.co.iekorea.mc.adapter.BusinessCardHolderAdapter;
import kr.co.iekorea.mc.staticdata.StaticData;
import kr.co.iekorea.mc.util.BaseInterface;
import kr.co.iekorea.mc.util.CustomLog;
import kr.co.iekorea.mc.util.ProcessManager;
import kr.co.iekorea.mc.util.WeakAsyncTask;
import kr.co.iekorea.mc.xml.BusinessCardHolderDto;
import kr.co.iekorea.mc.xml.MybriefDAO;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MyBriefcaseBusinesscardHolder_Activity extends Activity implements
		BaseInterface {

	// Activity control.
	private ProcessManager processManager;

	// layout area
	private ImageButton btn_back;
	private LinearLayout layout_header;
	private LinearLayout layout_progressbar;
	
	// list
	private ListView list;

	// button
	private ImageButton btn_delete;
	
	// adapter
	public BusinessCardHolderAdapter adapter;
	
	// items
	public ArrayList<BusinessCardHolderDto> items;
	
	// search
	public EditText edit_search;
	public ImageButton btn_search;
	
	// 언어
	private TextView total_title;
	private TextView text_wait;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.initView();
		if (StaticData.logined) {
			this.getXmlResources();
			this.modifyXmlResources();
			
			new setHolderList(MyBriefcaseBusinesscardHolder_Activity.this).execute();
			
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
	
//	public void setTextValues(){
//		this.items = new ArrayList<BusinessCardHolderDto>();
//		for(int i=0; i<3;i++){
//			BusinessCardHolderDto dto = new BusinessCardHolderDto();
//			dto.user_name = i+"번째 이름";
//			dto.user_crop = i+"번째 소속";
//			dto.user_position = i+"번째 직위";
//			this.items.add(dto);
//		}
//		
//		this.setBusinessAdapter();
//	}
	
	public void setBusinessAdapter(){
		this.adapter = new BusinessCardHolderAdapter(this, this, items);
		this.list.setAdapter(adapter);
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		this.processManager.deleteActivity(this);
	}
	
	@Override
	public void onBackPressed() {
		Intent intent = new Intent(getApplicationContext(), MyBriefcase_Activity.class);
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
		this.setContentView(R.layout.activity_mybriefcasebusinesscardholder);
		this.processManager = ProcessManager.getInstance();
		this.processManager.addActivity(this);
	}

	@Override
	public void getXmlResources() {
		this.btn_back = (ImageButton) this.findViewById(R.id.btn_back);
		this.layout_header = (LinearLayout) this.findViewById(R.id.layout_header);
		this.layout_progressbar = (LinearLayout) this.findViewById(R.id.layout_progressbar);
		
		// list
		this.list = (ListView) this.findViewById(R.id.list);
		// button
		this.btn_delete = (ImageButton) this.findViewById(R.id.btn_delete);
		
		// search
		this.btn_search = (ImageButton) this.findViewById(R.id.btn_search);
		this.edit_search = (EditText) this.findViewById(R.id.edit_search);
		
		// 언어
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
			this.total_title.setText(R.string.businesscard_holder_ko);
			this.text_wait.setText(R.string.wait_ko);
			this.btn_search.setImageResource(R.drawable.b_search_kr);
			this.btn_delete.setImageResource(R.drawable.b1_delete_kr);
			this.edit_search.setHint(R.string.please_enter_your_name_ko);
			break;
		case StaticData.ENGLISH:
			this.btn_back.setImageResource(R.drawable.b_back_en);
			this.total_title.setText(R.string.businesscard_holder_en);
			this.btn_search.setImageResource(R.drawable.b_search_en);
			this.btn_delete.setImageResource(R.drawable.b1_delete_en);
			this.edit_search.setHint(R.string.please_enter_your_name_en);
			this.text_wait.setText(R.string.wait_en);
			break;
		case StaticData.CHINA:
			this.btn_back.setImageResource(R.drawable.b_back_cn);
			this.total_title.setText(R.string.businesscard_holder_cn);
			this.btn_search.setImageResource(R.drawable.b_search_cn);
			this.btn_delete.setImageResource(R.drawable.b1_delete_cn);
			this.edit_search.setHint(R.string.please_enter_your_name_cn);
			this.text_wait.setText(R.string.wait_cn);
			break;
		}
	}

	@Override
	public void setEventListener() {
		// 백 버튼
		this.btn_back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(), MyBriefcase_Activity.class);
				startActivity(intent);
				overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
				finish();				
			}
		});
		
		// 삭제버튼
		this.btn_delete.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(adapter != null){
					adapter.ChangeDeleteMode();
				}
			}
		});
		
		// 리스트 리스너
		this.list.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// 해당값을 인텐트로 보냄
				CustomLog.e("list setonclick listener", "items.get(position).BUSINESSCARD_SHARE : "+items.get(position).BUSINESSCARD_SHARE);
				if(items.get(position).BUSINESSCARD_SHARE.equals("s")){
					Intent intent = new Intent(getApplicationContext(), MyBriefcaseBusinesscardHolderSecurity_Activity.class);
					intent.putExtra("user_cd", items.get(position).USER_CD);
					startActivity(intent);
					overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
					finish();
				}else{
					Intent intent = new Intent(getApplicationContext(), MyBriefcaseBusinesscardHolderDetail_Activity.class);
					intent.putExtra("user_cd", items.get(position).USER_CD);
					startActivity(intent);
					overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
					finish();					
				}
			}
		});
		
		this.btn_search.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(checkValues()){
					new setSearchReflash(MyBriefcaseBusinesscardHolder_Activity.this).execute(); 
				}else{
					switch (StaticData.NOWLANGUAGE) {
					case StaticData.KOREA:
						Toast.makeText(getApplicationContext(),R.string.please_enter_search_words_ko, Toast.LENGTH_SHORT).show();
						break;
					case StaticData.ENGLISH:
						Toast.makeText(getApplicationContext(),R.string.please_enter_search_words_en, Toast.LENGTH_SHORT).show();
						break;
					case StaticData.CHINA:
						Toast.makeText(getApplicationContext(),R.string.please_enter_search_words_cn, Toast.LENGTH_SHORT).show();
						break;
					}
				}
			}
		});
	}
	
	public boolean checkValues(){
		boolean result = false;
		if(edit_search.getText().toString().trim().equals("")){
			result = false;
		}else{
			result = true;
		}
		return result;
	}
	
	// back ground area
	// 비즈니스 홀더 리스트 가져오기
	class setHolderList extends WeakAsyncTask<Void, Void, Void, Activity>{
		boolean result = false;
		public setHolderList(Activity target) {
			super(target);
		}
		@Override
		protected void onPreExecute(Activity target) {
			layout_progressbar.setVisibility(LinearLayout.VISIBLE);
		}
		@Override
		protected Void doInBackground(Activity target, Void... params) {
			MybriefDAO dao = new MybriefDAO(MyBriefcaseBusinesscardHolder_Activity.this);
			try {
				result = dao.setHolderList();
			} catch (Exception e) {
				result = false;
			}
			if(result){
				if(items != null){
					items.clear();
				}
				items = dao.getHolderList();
			}
			return null;
		}		
		@Override
		protected void onPostExecute(Activity target, Void result) {
			if(this.result){
				setBusinessAdapter();
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
			setEventListener();
		}
	}
	
	class setSearchReflash extends WeakAsyncTask<Void, Void, Void, Activity>{
		boolean result = false;
		public setSearchReflash(Activity target) {
			super(target);
		}
		@Override
		protected void onPreExecute(Activity target) {
			layout_progressbar.setVisibility(LinearLayout.VISIBLE);
		}
		@Override
		protected Void doInBackground(Activity target, Void... params) {
			MybriefDAO dao = new MybriefDAO(MyBriefcaseBusinesscardHolder_Activity.this);
			try {
				result = dao.setHolderListReflash(edit_search.getText().toString().trim());
			} catch (Exception e) {
				result = false;
			}
			if(result){
				if(items != null){
					items.clear();
				}
				items = dao.getHolderList();
			}
			return null;
		}		
		@Override
		protected void onPostExecute(Activity target, Void result) {
			if(this.result){
				setBusinessAdapter();
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
			setEventListener();
		}
	}
}
