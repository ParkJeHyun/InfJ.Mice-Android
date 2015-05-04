package kr.co.iekorea.mc;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import kr.co.iekorea.mc.adapter.SearchUserAdapter;
import kr.co.iekorea.mc.event.SearchUserEventListener;
import kr.co.iekorea.mc.staticdata.StaticData;
import kr.co.iekorea.mc.util.BaseInterface;
import kr.co.iekorea.mc.util.ProcessManager;
import kr.co.iekorea.mc.util.WeakAsyncTask;
import kr.co.iekorea.mc.xml.MCDao;
import kr.co.iekorea.mc.xml.NationDto;
import kr.co.iekorea.mc.xml.SearchDAO;
import kr.co.iekorea.mc.xml.SearchSessionDto;
import kr.co.iekorea.mc.xml.SearchUserItemDto;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class SearchUser_Activity extends Activity implements BaseInterface {
	public static final String TAG = "SearchUser_Activity";
	
	// Activity control.
	private ProcessManager processManager;

	// layout area
	private ImageButton btn_back;
	private LinearLayout layout_header;
	private LinearLayout layout_progressbar;
	private ListView list;
	
	// 스피너
	private Spinner spinner_session;				// session 스피너
	private Spinner spinner_type;					// type 스피너
	private Spinner spinner_nation;					// 나라별 스피너
	
	// 스피너 아답타
	private ArrayAdapter spinner_type_items;
	private ArrayAdapter<String> spinner_items_nation;
	private ArrayAdapter<String> spinner_items_session;
	
	// list adapter
	private SearchUserAdapter adapter;
	
	public String nationNameList[];
	public String sessionNameList[];

	// for search layout
	private LinearLayout layout_search_mode;
	private LinearLayout layout_country_mode;
	
	// for index
	private static float sideIndexX;
	private static float sideIndexY;
	private int sideIndexHeight;
	private int indexListSize;
	private ArrayList<Object[]> indexList = null;
	
	// 언어
	private TextView total_title;
	private TextView info_search;
	private TextView text_wait;
	
	///////////////////////////////////////////////////////////////////////////////////
	private LinearLayout sideIndex;
	private static final int STATE_SHOWING = 1;
	private static final int STATE_HIDING = 3;
	private float mAlphaRate;
	private int mState = STATE_HIDING;
	
	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			Log.e(TAG, "handleMessage");
			Log.e(TAG, "mState : "+mState);
			
			switch (mState) {
			case STATE_SHOWING:
				Log.e(TAG, "STATE_SHOWING");
				sideIndex.setVisibility(LinearLayout.VISIBLE);
				break;
			case STATE_HIDING:
				Log.e(TAG, "STATE_HIDING");
				sideIndex.setVisibility(LinearLayout.INVISIBLE);
				break;
			}
		}
	};
	
	private void fade(long delay) {
		Log.e(TAG, "fade");
		mHandler.removeMessages(0);
		mHandler.sendEmptyMessageAtTime(0, SystemClock.uptimeMillis() + delay);
	}
	///////////////////////////////////////////////////////////////////////////////////
	// search 
	private EditText edit_search;
	private ImageButton btn_search_ok;
	
	// for list
	public ArrayList<NationDto> nationDtoList;
	public ArrayList<SearchSessionDto> sessionDtoList;
	public ArrayList<SearchUserItemDto> userDtoList;
	
	// flag for 정렬
	public int nowMode = 0;
	public static final int NameMode = 0;
	public static final int corpMode = 1;
	public static final int countryMode = 2;
	
	public int nowNation = 0;
	public int nowSesstion = 0;
	
	// sort
	private myComparator mComparator;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.initView();
		if (StaticData.logined) {
			this.getXmlResources();
			this.modifyXmlResources();
			
			// get nation key
			new setUserInfo(SearchUser_Activity.this).execute();
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
	
	public void setTypeLayout() {
		switch (nowMode) {
		case NameMode:
		case corpMode:
			this.layout_search_mode.setVisibility(LinearLayout.VISIBLE);
			edit_search.setText(null);
			this.layout_country_mode.setVisibility(LinearLayout.GONE);
			break;

		case countryMode:
			this.layout_search_mode.setVisibility(LinearLayout.GONE);
			this.layout_country_mode.setVisibility(LinearLayout.VISIBLE);
			break;
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		this.processManager.deleteActivity(this);
	}

	@Override
	public void onBackPressed() {
		Intent intent = new Intent(this, Search_Activity.class);
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
		this.setContentView(R.layout.activity_search_user);
		this.processManager = ProcessManager.getInstance();
		this.processManager.addActivity(this);
		
		// for init
		this.nationDtoList = new ArrayList<NationDto>();
		this.sessionDtoList = new ArrayList<SearchSessionDto>(); 
		this.userDtoList = new ArrayList<SearchUserItemDto>();
		
		this.mComparator = new myComparator();
	}

	@Override
	public void getXmlResources() {
		this.btn_back = (ImageButton) this.findViewById(R.id.btn_back);
		this.layout_header = (LinearLayout) this.findViewById(R.id.layout_header);
		this.layout_progressbar = (LinearLayout) this.findViewById(R.id.layout_progressbar);
		this.list = (ListView) this.findViewById(R.id.list);
		this.list.setOnScrollListener(new OnScrollListener() {
			
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				switch (scrollState) {
			     case OnScrollListener.SCROLL_STATE_IDLE :
			    	 list.setClickable(true);
			    	 list.setFocusableInTouchMode(true);
			    ///////////////////////////////////////////////////////////////////////////////////
			    	 mState = STATE_HIDING;
			    	 fade(3000);
			    	///////////////////////////////////////////////////////////////////////////////////
			          break;
			     case OnScrollListener.SCROLL_STATE_TOUCH_SCROLL :
			          break;
			     case OnScrollListener.SCROLL_STATE_FLING :
			    	 list.setClickable(false);
			    	 list.setFocusableInTouchMode(false);
			    	///////////////////////////////////////////////////////////////////////////////////
			    	 mState = STATE_SHOWING;
			    	 fade(0);
			    	///////////////////////////////////////////////////////////////////////////////////
			          break;
			   }				
			}
			
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
			}
		});
		////////////////////////////////////////////////////////////////////
		this.sideIndex = (LinearLayout) findViewById(R.id.sideIndex);
		////////////////////////////////////////////////////////////////////
		
		this.edit_search = (EditText) this.findViewById(R.id.edit_search);
		this.btn_search_ok = (ImageButton) this.findViewById(R.id.btn_search_ok);
		
		// type layout
		this.layout_search_mode = (LinearLayout) this.findViewById(R.id.layout_search_mode);
		this.layout_country_mode = (LinearLayout) this.findViewById(R.id.layout_country_mode);
		
		// spinner
		this.spinner_session = (Spinner) this.findViewById(R.id.spinner_session);
		this.spinner_type = (Spinner) this.findViewById(R.id.spinner_search_type);
		this.spinner_nation = (Spinner) this.findViewById(R.id.spinner_nation);
		
		// 언어
		this.total_title = (TextView) this.findViewById(R.id.total_title);
		this.info_search = (TextView) this.findViewById(R.id.info_search);
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
			this.btn_search_ok.setImageResource(R.drawable.b_search_kr);
			this.total_title.setText(R.string.participant_ko);
			this.info_search.setText(R.string.search_ko);
			this.text_wait.setText(R.string.wait_ko);
			this.edit_search.setHint(R.string.please_enter_search_words_ko);
			break;
		case StaticData.ENGLISH:
			this.btn_back.setImageResource(R.drawable.b_back_en);
			this.btn_search_ok.setImageResource(R.drawable.b_search_en);
			this.total_title.setText(R.string.participant_en);
			this.info_search.setText(R.string.search_en);
			this.edit_search.setHint(R.string.please_enter_search_words_en);
			this.text_wait.setText(R.string.wait_en);
			break;
		case StaticData.CHINA:
			this.btn_back.setImageResource(R.drawable.b_back_cn);
			this.btn_search_ok.setImageResource(R.drawable.b_search_cn);
			this.total_title.setText(R.string.participant_cn);
			this.info_search.setText(R.string.search_cn);
			this.edit_search.setHint(R.string.please_enter_search_words_cn);
			this.text_wait.setText(R.string.wait_cn);
			break;
		}
	}

	@Override
	public void setEventListener() {
		this.btn_back.setOnClickListener(new SearchUserEventListener(this,this));
		
		// list
		this.list.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent(getApplicationContext(),SearchUserDetail_Activity.class);
				intent.putExtra("user_cd", userDtoList.get(position).USER_CD);
				intent.putExtra("session_id", sessionDtoList.get(nowSesstion).SESSION_ID);
				startActivity(intent);
				overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
				finish();
			}
		});
		
		// button
		this.btn_search_ok.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(edit_search.getText().equals("")){
					switch (StaticData.NOWLANGUAGE) {
					case StaticData.KOREA:
					Toast.makeText(getApplicationContext(),
					R.string.please_enter_search_words_ko, Toast.LENGTH_SHORT).show();
					break;
					case StaticData.ENGLISH:
					Toast.makeText(getApplicationContext(),
					R.string.please_enter_search_words_en, Toast.LENGTH_SHORT).show();
					break;
					case StaticData.CHINA:
					Toast.makeText(getApplicationContext(),
					R.string.please_enter_search_words_cn, Toast.LENGTH_SHORT).show();
					break;
					}

				}else{
					new setUserInfoSearch(SearchUser_Activity.this).execute();
				}
			}
		});
		
		// make gesture detector ( 모션 이벤트 처리를 위한 detector )
		mGestureDetector = new GestureDetector(this,new SideIndexGestureListener());
	}
	
	public void setUserList(){
		adapter = new SearchUserAdapter(this, userDtoList);
		this.list.setAdapter(adapter);
	}
	
	public boolean first_s = true,first_t = true, first_n = true;
	public void setSpinnerAdapter() {
		switch (StaticData.NOWLANGUAGE) {
		case StaticData.KOREA:
			this.spinner_type_items = ArrayAdapter.createFromResource(this,R.array.SearchSort_ko,android.R.layout.simple_spinner_item);
			break;
		case StaticData.ENGLISH:
			this.spinner_type_items = ArrayAdapter.createFromResource(this,R.array.SearchSort_en,android.R.layout.simple_spinner_item);
			break;
		case StaticData.CHINA:
			this.spinner_type_items = ArrayAdapter.createFromResource(this,R.array.SearchSort_cn,android.R.layout.simple_spinner_item);
			break;
		}
		// sort type
		this.spinner_type_items.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		this.spinner_type.setAdapter(spinner_type_items);
		this.spinner_type.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent,
					View view, int position, long id) {
				// Log.e("spinner itemselectd listener",spinner_category.getSelectedItemPosition()
				// + "");
				nowMode = spinner_type.getSelectedItemPosition();
				if(first_t){
					first_t = false;
				}else{
					new executeSortList(SearchUser_Activity.this).execute();
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
			}
		});
		
		// nation item
		this.nationNameList = new String[this.nationDtoList.size()];
		for(int i=0; i<nationDtoList.size(); i++){
			this.nationNameList[i] = nationDtoList.get(i).NATIONAL_NAME;
		}
		this.spinner_items_nation = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,nationNameList);
		this.spinner_items_nation.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		this.spinner_nation.setAdapter(spinner_items_nation);
		this.spinner_nation.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent,
					View view, int position, long id) {
				// Log.e("spinner itemselectd listener",spinner_category.getSelectedItemPosition()
				// + "");
				nowNation = spinner_nation.getSelectedItemPosition();
				if(first_n){
					first_n = false;
				}else{
					new setUserInfoSearch(SearchUser_Activity.this).execute();
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
			}
		});
		
		// session item
		this.sessionNameList = new String[this.sessionDtoList.size()];
		for(int i=0; i<sessionDtoList.size(); i++){
			this.sessionNameList[i] = sessionDtoList.get(i).SESSION_TITLE;
		}
		this.spinner_items_session = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,sessionNameList);
		this.spinner_items_session.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		this.spinner_session.setAdapter(spinner_items_session);
		this.spinner_session.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent,
					View view, int position, long id) {
				// Log.e("spinner itemselectd listener",spinner_category.getSelectedItemPosition()
				// + "");
				nowSesstion = spinner_session.getSelectedItemPosition();
				if(first_s){
					first_s = false;
				}else{
					new setUserInfoReflash(SearchUser_Activity.this).execute();
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
			}
		});
		setIndexValues();
	}
	
	class getNationKey extends WeakAsyncTask<Void, Void, Void,Activity> {
		public getNationKey(Activity target) {
			super(target);
			// TODO Auto-generated constructor stub
		}

		private boolean result;

		@Override
		protected void onPreExecute(Activity target) {
			layout_progressbar.setVisibility(LinearLayout.VISIBLE);
		}

		@Override
		protected Void doInBackground(Activity target,Void... params) {
			MCDao dao = new MCDao(SearchUser_Activity.this);
			try {
				// input id values 1 for test
				result = dao.setNationDtoList();
			} catch (Exception e) {
				result = false;
				e.printStackTrace();
			}
			if (result) {
				if (SearchUser_Activity.this.nationDtoList != null) {
					SearchUser_Activity.this.nationDtoList.clear();
				}
				SearchUser_Activity.this.nationDtoList = dao.getnationDtoList();
			}
			return null;
		}

		protected void onPostExecute(Activity target,Void param) {
			if(result){
				new getSessionkey(SearchUser_Activity.this).execute();
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

				layout_progressbar.setVisibility(LinearLayout.GONE);
				setEventListener();
			}
		}
	}
	
	class getSessionkey extends WeakAsyncTask<Void, Void, Void,Activity> {
		public getSessionkey(Activity target) {
			super(target);
			// TODO Auto-generated constructor stub
		}

		private boolean result;

		@Override
		protected void onPreExecute(Activity target) {
			layout_progressbar.setVisibility(LinearLayout.VISIBLE);
		}

		@Override
		protected Void doInBackground(Activity target,Void... params) {
			SearchDAO dao = new SearchDAO(SearchUser_Activity.this);
			try {
				// input id values 1 for test
				result = dao.setSpinnerSession();
			} catch (Exception e) {
				result = false;
				e.printStackTrace();
			}
			if (result) {
				if (SearchUser_Activity.this.sessionDtoList != null) {
					SearchUser_Activity.this.sessionDtoList.clear();
				}
				SearchUser_Activity.this.sessionDtoList = dao.getSessionDtoList();
			}
			return null;
		}

		protected void onPostExecute(Activity target,Void param) {
			if(result){
				setSpinnerAdapter();
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

				setEventListener();
			}
			layout_progressbar.setVisibility(LinearLayout.GONE);
		}
	}
	
	class setUserInfo extends WeakAsyncTask<Void, Void, Void,Activity> {
		public setUserInfo(Activity target) {
			super(target);
			// TODO Auto-generated constructor stub
		}

		private boolean result;

		@Override
		protected void onPreExecute(Activity target) {
			layout_progressbar.setVisibility(LinearLayout.VISIBLE);
		}

		@Override
		protected Void doInBackground(Activity target,Void... params) {
			SearchDAO dao = new SearchDAO(SearchUser_Activity.this);
			try {
				// input id values 1 for test
				result = dao.setUserInfo();
			} catch (Exception e) {
				result = false;
				e.printStackTrace();
			}
			if (result) {
				if (SearchUser_Activity.this.userDtoList != null) {
					SearchUser_Activity.this.userDtoList.clear();
				}
				SearchUser_Activity.this.userDtoList = dao.getSearchUserItemDtoList();
			}
			return null;
		}

		protected void onPostExecute(Activity target,Void param) {
			if(result){
				setUserList();
				new getNationKey(SearchUser_Activity.this).execute();
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

				layout_progressbar.setVisibility(LinearLayout.GONE);
			}
			setEventListener();			
		}
	}
	
	class setUserInfoReflash extends WeakAsyncTask<Void, Void, Void,Activity> {
		public setUserInfoReflash(Activity target) {
			super(target);
			// TODO Auto-generated constructor stub
		}

		private boolean result;

		@Override
		protected void onPreExecute(Activity target) {
			layout_progressbar.setVisibility(LinearLayout.VISIBLE);
		}

		@Override
		protected Void doInBackground(Activity target,Void... params) {
			SearchDAO dao = new SearchDAO(SearchUser_Activity.this);
			try {
				// input id values 1 for test
				if(nowSesstion == 0){
					result = dao.setUserInfo();
				}else{            //conference_id=1&session_id=2
					result = dao.setUserInfoReflash(sessionDtoList.get(nowSesstion).SESSION_ID);
				}
			} catch (Exception e) {
				result = false;
				e.printStackTrace();
			}
			if (result) {
				if (SearchUser_Activity.this.userDtoList != null) {
					SearchUser_Activity.this.userDtoList.clear();
				}
				SearchUser_Activity.this.userDtoList.addAll( dao.getSearchUserItemDtoList());
			}
			return null;
		}

		protected void onPostExecute(Activity target,Void param) {
			if(result){
				new executeSortList(SearchUser_Activity.this).execute();
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
	
	// sort category
	class executeSortList extends WeakAsyncTask<Void, Void, Void,Activity> {

		public executeSortList(Activity target) {
			super(target);
			// TODO Auto-generated constructor stub
		}

		@Override
		protected void onPreExecute(Activity target) {
			layout_progressbar.setVisibility(LinearLayout.VISIBLE);
		}

		@Override
		protected Void doInBackground(Activity target,Void... params) {
			sortMyList(nowMode);
			return null;
		}

		protected void onPostExecute(Activity target,Void param) {
			setTypeLayout();
			adapter.notifyDataSetChanged();
			setIndexValues();
			layout_progressbar.setVisibility(LinearLayout.GONE);
		}
	}
	
	class setUserInfoSearch extends WeakAsyncTask<Void, Void, Void,Activity> {
		public setUserInfoSearch(Activity target) {
			super(target);
			// TODO Auto-generated constructor stub
		}

		private boolean result;

		@Override
		protected void onPreExecute(Activity target) {
			layout_progressbar.setVisibility(LinearLayout.VISIBLE);
		}

		@Override
		protected Void doInBackground(Activity target,Void... params) {
			SearchDAO dao = new SearchDAO(SearchUser_Activity.this);
			try {
				// input id values 1 for test
				String search_flag ="";
				String keyword = "";
				switch(nowMode){
				case 0:							// 이름
					search_flag = "name";
					keyword = edit_search.getText().toString();
					break;
				case 1:							// 소속
					search_flag = "company";
					keyword = edit_search.getText().toString();
					break;
				case 2:							// 국가
					search_flag = "nation";
					if(nowNation == 0){
						keyword = "";
					}else{
						keyword = nationDtoList.get(nowNation).NATIONAL_NAME.toString();
					}
					
					break;
				}
				
				int session_id = sessionDtoList.get(nowSesstion).SESSION_ID;
				result = dao.setUserInfo(search_flag,keyword,session_id);
			} catch (Exception e) {
				result = false;
				e.printStackTrace();
			}
			if (result) {
				if (SearchUser_Activity.this.userDtoList != null) {
					SearchUser_Activity.this.userDtoList.clear();
				}
				SearchUser_Activity.this.userDtoList.addAll(dao.getSearchUserItemDtoList());
				Log.e("", "userDtoList size : "+userDtoList.size());
			}
			return null;
		}

		protected void onPostExecute(Activity target,Void param) {
			if(result){
				new executeSortList(SearchUser_Activity.this).execute();
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
	
	public void sortMyList(int mode) {
		mComparator.changeMode(mode);
		Collections.sort(userDtoList, mComparator);
	}
	
	class myComparator implements Comparator<SearchUserItemDto> {
		private int mode = 0; // name
		private final Collator collator = Collator.getInstance();

		public void changeMode(int mode) {
			this.mode = mode;
		}

		@Override
		public int compare(SearchUserItemDto object1,
				SearchUserItemDto object2) {
			// Log.e("myCollator", mode + "");
			switch (mode) {
			case NameMode:
				return collator.compare(object1.USER_NAME,object2.USER_NAME);

			case corpMode:
				return collator.compare(object1.COMPANY, object2.COMPANY);

			case countryMode:
				return collator.compare(object1.NATIONAL_NAME, object2.NATIONAL_NAME);
			}

			return 0;
		}
	}

	// index
	public synchronized void setIndexValues(){
        Log.e("onwindowfocus", "sideIndexHeight : "+sideIndexHeight);
        sideIndex.removeAllViews();
        TextView tmpTV = null;
        indexList = createIndex(this.userDtoList);
        indexListSize = indexList.size();
        Log.e("onwindowfocus", "indexListSize : "+indexList.size());
        int indexMaxSize = (int) Math.floor(sideIndex.getHeight() / 20);
        Log.e("onwindowfocus", "indexMaxSize : "+indexMaxSize);

        int tmpIndexListSize = indexListSize;

        while (tmpIndexListSize > indexMaxSize)
        {
            tmpIndexListSize = tmpIndexListSize / 2;
        }
        Log.e("onwindowfocus", "tmpIndexListSize : "+tmpIndexListSize);
        double delta = 0;
        if(indexListSize==0 || tmpIndexListSize ==0){
        }else{
        	delta = indexListSize / tmpIndexListSize;
        }

        String tmpLetter = null;
        Object[] tmpIndexItem = null;

        for (double i = 1; i <= indexListSize; i = i + delta)
        {
            tmpIndexItem = indexList.get((int) i - 1);
            tmpLetter = tmpIndexItem[0].toString();
            tmpTV = new TextView(this);
            tmpTV.setText(tmpLetter);
            tmpTV.setGravity(Gravity.CENTER);
            tmpTV.setTextSize(20);
            LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, 1);
            tmpTV.setLayoutParams(params);
            sideIndex.addView(tmpTV);
        }

        // and set a touch listener for it
        sideIndex.setOnTouchListener(new OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
            	mHandler.removeMessages(0);
            	switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					break;
				case MotionEvent.ACTION_UP:
					mState = STATE_HIDING;
					fade(3000);
					return false;
				case MotionEvent.ACTION_MOVE:
					break;
				}
                // now you know coordinates of touch
                sideIndexX = event.getX();
                sideIndexY = event.getY();

                // and can display a proper item it country list
                displayListItem();

                return true;
            }
        });
	}
	
	@Override
    public void onWindowFocusChanged(boolean hasFocus)
    {
        super.onWindowFocusChanged(hasFocus);
        LinearLayout sideIndex = (LinearLayout) findViewById(R.id.sideIndex);
        sideIndexHeight = sideIndex.getHeight();
    }
	
	private ArrayList<Object[]> createIndex(ArrayList<SearchUserItemDto> strArr) {
		 ArrayList<Object[]> tmpIndexList = new ArrayList<Object[]>();
	        Object[] tmpIndexItem = null;

	        int tmpPos = 0;
	        String tmpLetter = "";
	        String currentLetter = null;
	        String strItem = null;
	        Log.e("createindex", "strArr size : "+strArr.size());

	        try{
	        	for (int j = 0; j < strArr.size(); j++)
		        {
		        	switch (nowMode) {
					case NameMode:
						strItem = strArr.get(j).USER_NAME;
						break;
					case corpMode:
						strItem = strArr.get(j).COMPANY;
						break;
					case countryMode:
						strItem = strArr.get(j).NATIONAL_NAME;
						break;
					}
		            currentLetter = strItem.substring(0, 1);

		            if (!currentLetter.equals(tmpLetter))
		            {
		                tmpIndexItem = new Object[3];
		                tmpIndexItem[0] = tmpLetter;
		                tmpIndexItem[1] = tmpPos - 1;
		                tmpIndexItem[2] = j - 1;

		                tmpLetter = currentLetter;
		                tmpPos = j + 1;

		                tmpIndexList.add(tmpIndexItem);
		            }
		        }

		        tmpIndexItem = new Object[3];
		        
		        tmpIndexItem[0] = tmpLetter;
		        tmpIndexItem[1] = tmpPos - 1;
		        tmpIndexItem[2] = strArr.size() - 1;
		        tmpIndexList.add(tmpIndexItem);

		        if (tmpIndexList != null && tmpIndexList.size() > 0)
		        {
		            tmpIndexList.remove(0);
		        }	        	
	        }catch(Exception e){}

	        return tmpIndexList;
	}
	
	public GestureDetector mGestureDetector;

	class SideIndexGestureListener extends GestureDetector.SimpleOnGestureListener {
		@Override
		public boolean onScroll(MotionEvent e1, MotionEvent e2,float distanceX, float distanceY) {
			
			sideIndexX = sideIndexX - distanceX;
			sideIndexY = sideIndexY - distanceY;

			if (sideIndexX >= 0 && sideIndexY >= 0) {
				displayListItem();
			}

			return super.onScroll(e1, e2, distanceX, distanceY);
		}
	}
	
	public void displayListItem()
    {
        double pixelPerIndexItem = (double) sideIndexHeight / indexListSize;

        // compute the item index for given event position belongs to
        int itemPosition = (int) (sideIndexY / pixelPerIndexItem);
        if(itemPosition >= indexListSize){
        	itemPosition = indexListSize -1;
        }

        // compute minimal position for the item in the list
        int minPosition = (int) (itemPosition * pixelPerIndexItem);

        // get the item (we can do it since we know item index)
        Object[] indexItem = indexList.get(itemPosition);

        // and compute the proper item in the country list
        int indexMin = Integer.parseInt(indexItem[1].toString());
        int indexMax = Integer.parseInt(indexItem[2].toString());
        int indexDelta = Math.max(1, indexMax - indexMin);

        double pixelPerSubitem = pixelPerIndexItem / indexDelta;
        int subitemPosition = (int) (indexMin + (sideIndexY - minPosition) / pixelPerSubitem);

//        ListView listView = (ListView) findViewById(R.id.ListView01);
        this.list.setSelection(subitemPosition);
    }
	
//	@Override
//    public boolean onTouchEvent(MotionEvent event)
//    {
//        if (mGestureDetector.onTouchEvent(event))
//        {
//            return true;
//        } else
//        {
//            return false;
//        }
//    }
}
