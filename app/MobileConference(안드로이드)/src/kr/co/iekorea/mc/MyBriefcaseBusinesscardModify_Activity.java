package kr.co.iekorea.mc;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import kr.co.iekorea.mc.adapter.AddressAdapter;
import kr.co.iekorea.mc.staticdata.StaticData;
import kr.co.iekorea.mc.util.BaseInterface;
import kr.co.iekorea.mc.util.CustomLog;
import kr.co.iekorea.mc.util.ImageDownloader;
import kr.co.iekorea.mc.util.MultipartRequestUtility;
import kr.co.iekorea.mc.util.ProcessManager;
import kr.co.iekorea.mc.util.WeakAsyncTask;
import kr.co.iekorea.mc.xml.AddressDto;
import kr.co.iekorea.mc.xml.AppellationKeyDto;
import kr.co.iekorea.mc.xml.BusinessCardDTO;
import kr.co.iekorea.mc.xml.MCDao;
import kr.co.iekorea.mc.xml.MybriefDAO;
import kr.co.iekorea.mc.xml.NationDto;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class MyBriefcaseBusinesscardModify_Activity extends Activity implements
		BaseInterface {
	// Activity control.
	private ProcessManager processManager;

	// layout area
	private ImageButton btn_back;
	private LinearLayout layout_header;
	private LinearLayout layout_progressbar;
	
	// contents
	private ImageView img_mypic;
	private EditText edit_user_name,edit_user_corp,
	edit_user_phone1,edit_user_phone2,
//	private EditText edit_user_position,;
	edit_user_phone3,edit_user_email;
	
	private EditText edit_user_address;
	
	private TextView info_nation,info_city,info_state;
	private TextView info_postal;
	
	private EditText edit_user_city,edit_user_state,edit_user_address_2,edit_user_pastal;
//	private EditText edit_nation,;
	private int now_nation_id = 0;
	
	public ImageButton btn_save;
	
	// data area
	public BusinessCardDTO dto = new BusinessCardDTO();
	public ArrayList<AddressDto> addressList;
	
	// for address
	public LinearLayout layout_input_address;
	public LinearLayout layout_contents;
	public ImageButton btn_address_check;
	public EditText edit_address_text;
	public ListView list_address;
	public boolean isSearchMode = false;
	public ImageButton btn_ok,btn_cancel;
	public EditText edit_address_1,edit_address_2;
	public LinearLayout layout_detail_address;
	
	// position key
	public ArrayList<AppellationKeyDto> positionKey;
	public Spinner spinner_position;
	
	
	// position
	public String positionNameList[];
	public ArrayAdapter<String> spinner_items_position;
	
	// for update
	public String zipCode;
	public int appellationID;
	
	// 언어
	private TextView total_title,info_name,info_position,info_phone,info_address,info_company,text_wait,info_email;
	
	/** 외장메모리 표준 저장 위치 */
	public static final String STR_EXTERNAL_SAVEPATH = Environment.getExternalStorageDirectory() + "/Appsone/";
	
/** 사진 촬영후 저장되는 외장메모리 파일 위치 */
	public static final String STRSAVERPATH_SHOTIMAGE = STR_EXTERNAL_SAVEPATH+".Shotimages/";
	
// FLAG
	private final int IMAGE_SELECT = 120;
	private final int VIDEO_SELECT = 121;
	private final int IMAGE_TAKE = 122;
	private final int VIDEO_PICK = 123;
	
	// spinner
	private Spinner spinner_nation;					// 나라별 스피너
	private ArrayAdapter<String> spinner_items_nation;
	public ArrayList<NationDto> nationDtoList;
	public String nationNameList[];
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.initView();
		if (StaticData.logined) {
			this.getXmlResources();
			this.modifyXmlResources();
			
			new setMyBusinessCard(MyBriefcaseBusinesscardModify_Activity.this).execute();
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
	
	public void UpdateUI(){
		this.edit_user_name.setText(dto.USER_NAME);
		this.edit_user_corp.setText(dto.COMPANY);
		this.edit_user_phone1.setText(dto.PHONE_1);
		this.edit_user_phone2.setText(dto.PHONE_2);
		this.edit_user_phone3.setText(dto.PHONE_3);
		this.edit_user_email.setText(dto.EMAIL);
		this.edit_user_address.setText(dto.STREET_ADDRESS);
		
		this.edit_user_address_2.setText(dto.STREET_ADDRESS_DETAIL);
//		this.edit_nation.setText(dto.NATION);
		this.edit_user_city.setText(dto.CITY);
		this.edit_user_state.setText(dto.STATE);
		this.edit_user_pastal.setText(dto.POSTAL_CODE);
		
		this.setImage();
		
		this.zipCode = dto.ZIP_CODE;
		this.appellationID = dto.APPELLATION_ID;
		try{
			this.now_nation_id = Integer.parseInt(dto.NATION_ID);
		}catch(Exception e){
			e.printStackTrace();
		}
//		this.edit_user_position.setText(dto.APPELLATION_NAME);
		
	}
	
	public void setImage(){
		
		try{
			ImageDownloader.download(StaticData.DEFAULT_URL+dto.PICTURE, img_mypic);
		}catch(Exception e){
		}
		// temp 사진 만들기
//		try{
//			Drawable d = img_mypic.getDrawable();
//			Bitmap selectedImage = ((BitmapDrawable)d).getBitmap();
//			this.SaveBitmapToFileCache(selectedImage);			
//		}catch(Exception e){
//			e.printStackTrace();
//		}
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		this.processManager.deleteActivity(this);
	}
	
	@Override
	public void onBackPressed() {
		if(isSearchMode){
			changeView();
		}else{
			Intent intent = new Intent(getApplicationContext(), MyBriefcaseBusinesscard_Activity.class);
			startActivity(intent);
			overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
			finish();			
		}
	}
	
	public void changeView(){
		if(isSearchMode){
			isSearchMode = false;
			layout_input_address.setVisibility(LinearLayout.GONE);
			edit_address_text.setText("");
			layout_detail_address.setVisibility(LinearLayout.GONE);
			edit_address_1.setText("");
			edit_address_2.setText("");
			selectedAddressPosition = 0;
			addressList.clear();
			if(adapter != null){
				adapter.notifyDataSetChanged();
			}
			layout_contents.setVisibility(LinearLayout.VISIBLE);
		}else{
			isSearchMode = true;
			layout_input_address.setVisibility(LinearLayout.VISIBLE);
			layout_contents.setVisibility(LinearLayout.GONE);
		}
	}
	
	@Override
	public void finish() {
		super.finish();
		overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
	}

	@Override
	public void initView() {
		this.setContentView(R.layout.activity_mybriefcasebusinesscardmodify);
		this.processManager = ProcessManager.getInstance();
		this.processManager.addActivity(this);
	}

	@Override
	public void getXmlResources() {
		this.btn_back = (ImageButton) this.findViewById(R.id.btn_back);
		this.layout_header = (LinearLayout) this.findViewById(R.id.layout_header);
		this.layout_progressbar = (LinearLayout) this.findViewById(R.id.layout_progressbar);
		
		// contents 
		this.img_mypic = (ImageView) this.findViewById(R.id.img_mypic);
		this.edit_user_name = (EditText) this.findViewById(R.id.edit_user_name);
		this.edit_user_corp = (EditText) this.findViewById(R.id.edit_user_corp);
		this.spinner_position = (Spinner) this.findViewById(R.id.spinner_position);
//		this.edit_user_position = (EditText) this.findViewById(R.id.edit_user_position);
		this.edit_user_phone1 = (EditText) this.findViewById(R.id.edit_user_phone1);
		this.edit_user_phone2 = (EditText) this.findViewById(R.id.edit_user_phone2);
		this.edit_user_phone3 = (EditText) this.findViewById(R.id.edit_user_phone3);
		this.edit_user_email = (EditText) this.findViewById(R.id.edit_user_email);
		this.edit_user_address = (EditText) this.findViewById(R.id.edit_user_address);
		
		this.btn_save = (ImageButton) this.findViewById(R.id.btn_save);
		this.spinner_nation = (Spinner) this.findViewById(R.id.spinner_nation);
		
		// address
		this.layout_input_address = (LinearLayout) this.findViewById(R.id.layout_input_address);
		this.layout_contents = (LinearLayout) this.findViewById(R.id.layout_contents);
		this.btn_address_check = (ImageButton) this.findViewById(R.id.btn_address_check);
		this.edit_address_text = (EditText) this.findViewById(R.id.edit_address_text);
		this.list_address = (ListView) this.findViewById(R.id.list_address);
		this.addressList = new ArrayList<AddressDto>();
		this.btn_ok = (ImageButton) this.findViewById(R.id.btn_ok);
		this.btn_cancel = (ImageButton) this.findViewById(R.id.btn_cancel);
//		this.edit_address_1 = (EditText) this.findViewById(R.id.edit_address_1);
//		this.edit_address_2 = (EditText) this.findViewById(R.id.edit_address_2);
		this.layout_detail_address = (LinearLayout) this.findViewById(R.id.layout_detail_address);
		
		// 언어
		this.total_title = (TextView) this.findViewById(R.id.total_title);
		this.info_email = (TextView) this.findViewById(R.id.info_email);
		this.info_name = (TextView) this.findViewById(R.id.info_name);
		this.info_position = (TextView) this.findViewById(R.id.info_position);
		this.info_phone = (TextView) this.findViewById(R.id.info_phone);
		this.info_address = (TextView) this.findViewById(R.id.info_address);
		this.info_company = (TextView) this.findViewById(R.id.info_company);
		this.text_wait = (TextView) this.findViewById(R.id.text_wait);
		
		this.info_nation = (TextView) this.findViewById(R.id.info_nation);
		this.info_city = (TextView) this.findViewById(R.id.info_city);
		this.info_state = (TextView) this.findViewById(R.id.info_state);
		this.info_postal = (TextView) this.findViewById(R.id.info_postal);
		
//		this.edit_nation = (EditText) this.findViewById(R.id.edit_user_nation);
		this.edit_user_city = (EditText) this.findViewById(R.id.edit_user_city);
		this.edit_user_state = (EditText) this.findViewById(R.id.edit_user_state);
		this.edit_user_address_2 = (EditText) this.findViewById(R.id.edit_user_address2);
		this.edit_user_pastal = (EditText) this.findViewById(R.id.edit_user_postal);
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
			this.total_title.setText(R.string.modify_ko);
			this.info_email.setText(R.string.email_ko);
			this.text_wait.setText(R.string.wait_ko);
			this.info_name.setText(R.string.name_ko);
			this.info_address.setText(R.string.address_ko);
			this.info_phone.setText(R.string.phone_ko);
			this.info_position.setText(R.string.user_position_ko);
			this.btn_save.setImageResource(R.drawable.b_save_kr);
			this.btn_address_check.setImageResource(R.drawable.b_search_kr);
			this.info_company.setText(R.string.company_ko);
			this.edit_address_text.setHint(R.string.please_enter_search_words_ko);
			this.btn_ok.setImageResource(R.drawable.b2_registration_kr);
			this.btn_cancel.setImageResource(R.drawable.b2_cancel_kr);
			
			this.info_nation.setText(R.string.nation_ko);
			this.info_city.setText(R.string.city_ko);
			this.info_state.setText(R.string.state_ko);
			this.info_postal.setText(R.string.postal_ko);
			break;
		case StaticData.ENGLISH:
			this.info_email.setText(R.string.email_en);
			this.btn_back.setImageResource(R.drawable.b_back_en);
			this.total_title.setText(R.string.modify_en);
			this.info_name.setText(R.string.name_en);
			this.info_address.setText(R.string.address_en);
			this.info_phone.setText(R.string.phone_en);
			this.info_position.setText(R.string.user_position_en);
			this.btn_save.setImageResource(R.drawable.b_save_en);
			this.btn_address_check.setImageResource(R.drawable.b_search_en);
			this.info_company.setText(R.string.company_en);
			this.edit_address_text.setHint(R.string.please_enter_search_words_en);
			this.btn_ok.setImageResource(R.drawable.b2_registration_en);
			this.btn_cancel.setImageResource(R.drawable.b2_cancel_en);
			this.text_wait.setText(R.string.wait_en);
			
			this.info_nation.setText(R.string.nation_en);
			this.info_city.setText(R.string.city_en);
			this.info_state.setText(R.string.state_en);
			this.info_postal.setText(R.string.postal_en);
			break;
		case StaticData.CHINA:
			this.info_email.setText(R.string.email_cn);
			this.btn_back.setImageResource(R.drawable.b_back_cn);
			this.total_title.setText(R.string.modify_cn);
			this.info_name.setText(R.string.name_cn);
			this.info_address.setText(R.string.address_cn);
			this.info_phone.setText(R.string.phone_cn);
			this.info_position.setText(R.string.user_position_cn);
			this.btn_save.setImageResource(R.drawable.b_save_cn);
			this.btn_address_check.setImageResource(R.drawable.b_search_cn);
			this.info_company.setText(R.string.company_cn);
			this.edit_address_text.setHint(R.string.please_enter_search_words_cn);
			this.btn_ok.setImageResource(R.drawable.b2_registration_cn);
			this.btn_cancel.setImageResource(R.drawable.b2_cancel_cn);
			this.text_wait.setText(R.string.wait_cn);
			
			this.info_nation.setText(R.string.nation_cn);
			this.info_city.setText(R.string.city_cn);
			this.info_state.setText(R.string.state_cn);
			this.info_postal.setText(R.string.postal_cn);
			break;
		}
	}
	
	public boolean checkValues(){
		boolean result = false;
		if(edit_user_name.getText().toString().equals("")
				|| edit_user_corp.getText().toString().trim().equals("")
				|| edit_user_phone1.getText().toString().trim().equals("")
				|| edit_user_phone2.getText().toString().trim().equals("")
				|| edit_user_phone3.getText().toString().trim().equals("")
				|| edit_user_address.getText().toString().trim().equals("")
				|| edit_user_email.getText().toString().trim().equals("")
				
				|| edit_user_address_2.getText().toString().trim().equals("")
				|| edit_user_state.getText().toString().trim().equals("")
				|| edit_user_city.getText().toString().trim().equals("")
				|| edit_user_pastal.getText().toString().trim().equals("")
				
				){
			switch (StaticData.NOWLANGUAGE) {
			case StaticData.KOREA:
				Toast.makeText(getApplicationContext(), R.string.please_enter_the_contents_ko,	 Toast.LENGTH_SHORT).show();
				break;
			case StaticData.ENGLISH:
				Toast.makeText(getApplicationContext(), R.string.please_enter_the_contents_en,	 Toast.LENGTH_SHORT).show();
				break;
			case StaticData.CHINA:
				Toast.makeText(getApplicationContext(), R.string.please_enter_the_contents_cn,	 Toast.LENGTH_SHORT).show();
				break;
			}
			
			// =======================================================
//			if(edit_user_name.getText().toString().equals("")){
//				
//			}else if(edit_user_corp.getText().toString().equals("")){
//				
//			}else if(edit_user_phone1.getText().toString().equals("")
//					|| edit_user_phone2.getText().toString().equals("")
//					|| edit_user_phone3.getText().toString().equals("")){
//				
//			}else if(text_user_address.getText().toString().equals("")){
//				
//			}else if(edit_user_email.getText().toString().equals("")){
//				switch (StaticData.NOWLANGUAGE) {
//				case StaticData.KOREA:
//					Toast.makeText(getApplicationContext(), R.string.please_enter_the_contents_ko,	 Toast.LENGTH_SHORT).show();
//					break;
//				case StaticData.ENGLISH:
//					Toast.makeText(getApplicationContext(), R.string.please_enter_the_contents_en,	 Toast.LENGTH_SHORT).show();
//					break;
//				case StaticData.CHINA:
//					Toast.makeText(getApplicationContext(), R.string.please_enter_the_contents_cn,	 Toast.LENGTH_SHORT).show();
//					break;
//				}
			// =======================================================
		}else{
			result = true;
		}
		return result;
	}

	@Override
	public void setEventListener() {
		this.btn_back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(isSearchMode){
					changeView();
				}else{
					Intent intent = new Intent(getApplicationContext(), MyBriefcaseBusinesscard_Activity.class);
					startActivity(intent);
					overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
					finish();					
				}
			}
		});
		
//		this.text_user_address.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				changeView();
//			}
//		});
		
		// 이미지 터치 리스너
		this.img_mypic.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				executeCorp();
			}
		});
		
		// 수정 업로드 버튼
		this.btn_save.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// for test
//				new setMyBusinessCardUpdate(MyBriefcaseBusinesscardModify_Activity.this).execute();
				// 업데이트를 한다.
				if(checkValues()){
					new setMyBusinessCardUpdate(MyBriefcaseBusinesscardModify_Activity.this).execute();					
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
			}
		});
		
		// address check button
		this.btn_address_check.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(edit_address_text.getText().toString().equals("")){
//					Toast.makeText(getApplicationContext(), "동을 입력해 주세요", Toast.LENGTH_SHORT).show();
				}else{
					new setAddress(MyBriefcaseBusinesscardModify_Activity.this).execute();					
				}
			}
		});
		this.list_address.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// 동 겁색 ui gone
				layout_input_address.setVisibility(LinearLayout.GONE);
				// 동 검색 edit 값 지우기
				edit_address_text.setText("");
				// 주소입력 필드 visible
				layout_detail_address.setVisibility(LinearLayout.VISIBLE);
				edit_address_1.setText(addressList.get(position).ADDRESS);
				selectedAddressPosition = position;
			}
			
		});
		
//		this.btn_ok.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				if(edit_address_2.getText().toString().equals("")){
////					Toast.makeText(getApplicationContext(), "상세주소를 입력해 주세요", Toast.LENGTH_SHORT).show();
//				}else{
//					// zip code를 가지고 있어야 한다. (update를 위해)
//					zipCode = addressList.get(selectedAddressPosition).ZIPCODE;
//					// 주소1과 2를 조합 기존값에 저장한다.
//					text_user_address.setText(edit_address_1.getText().toString()+
//							edit_address_2.getText().toString());
//					// 화면전환을 처음으로 돌린다.
//					changeView();
//				}
//			}
//		});
		
		this.btn_cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				changeView();
			}
		});
	}
	
	public int selectedAddressPosition;
	public int firstSelectedNation= 0;
	
	public int firstSelectedPosition;
	public void setSpinnerAdapter() {
		// position
		this.positionNameList = new String[this.positionKey.size()];
		for(int i=0; i<positionKey.size(); i++){
			this.positionNameList[i] = positionKey.get(i).APPELLATION_NAME;
			if(positionKey.get(i).APPELLATION_ID == dto.APPELLATION_ID){
				firstSelectedPosition = i;
			}
		}
		this.spinner_items_position = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,positionNameList);
		this.spinner_items_position.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		this.spinner_position.setAdapter(spinner_items_position);
		this.spinner_position.setSelection(firstSelectedPosition);
		this.spinner_position.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent,
					View view, int position, long id) {
				appellationID = positionKey.get(position).APPELLATION_ID;
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
			}
		});
		
		
		this.nationNameList = new String[this.nationDtoList.size()];
		for(int i=0; i<nationDtoList.size(); i++){
			this.nationNameList[i] = nationDtoList.get(i).NATIONAL_NAME;
			try{
				if(nationDtoList.get(i).NATIONAL_ID == Integer.parseInt(dto.NATION_ID)){
					firstSelectedNation = i;
				}				
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		this.spinner_items_nation = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,nationNameList);
		this.spinner_items_nation.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		this.spinner_nation.setAdapter(spinner_items_nation);
		this.spinner_nation.setSelection(firstSelectedNation);
		this.spinner_nation.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent,
					View view, int position, long id) {
				// Log.e("spinner itemselectd listener",spinner_category.getSelectedItemPosition()
				// + "");
				now_nation_id = nationDtoList.get(position).NATIONAL_ID;
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
			}
		});
	}
	
	public AddressAdapter adapter;
	public void SetAddressAdapter(){
		adapter = new AddressAdapter(getApplicationContext(), addressList);
		this.list_address.setAdapter(adapter);
	}
	
	
	// back ground
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
			MCDao dao = new MCDao(MyBriefcaseBusinesscardModify_Activity.this);
			try {
				// input id values 1 for test
				result = dao.setNationDtoList(StaticData.CONFERENCE_ID);
			} catch (Exception e) {
				result = false;
				e.printStackTrace();
			}
			if (result) {
				if (MyBriefcaseBusinesscardModify_Activity.this.nationDtoList != null) {
					MyBriefcaseBusinesscardModify_Activity.this.nationDtoList.clear();
				}
				MyBriefcaseBusinesscardModify_Activity.this.nationDtoList = dao.getnationDtoList();
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
			}
			layout_progressbar.setVisibility(LinearLayout.GONE);
			setEventListener();
		}
	}
	// 해당 유저 명암 가저오기
	class setMyBusinessCard extends WeakAsyncTask<Void, Void, Void, Activity>{
		boolean result = false;
		public setMyBusinessCard(Activity target) {
			super(target);
		}
		@Override
		protected void onPreExecute(Activity target) {
			layout_progressbar.setVisibility(LinearLayout.VISIBLE);
		}
		@Override
		protected Void doInBackground(Activity target, Void... params) {
			MybriefDAO dao = new MybriefDAO(MyBriefcaseBusinesscardModify_Activity.this);
			try {
				result = dao.setBusinessCard();
			} catch (Exception e) {
				result = false;
			}
			if(result){
				dto = dao.getBusinessCard();
			}
			return null;
		}
		@Override
		protected void onPostExecute(Activity target, Void result) {
			if(this.result){
				UpdateUI();
				new getPositionKey(MyBriefcaseBusinesscardModify_Activity.this).execute();
			}else{
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
				layout_progressbar.setVisibility(LinearLayout.GONE);
				setEventListener();
			}
			// for test
//			try{
//				CustomLog.e(TAG, "----------");
//				Drawable d = img_mypic.getDrawable();
//				Bitmap selectedImage = ((BitmapDrawable)d).getBitmap();
//				SaveBitmapToFileCache(selectedImage);			
//			}catch(Exception e){
//				e.printStackTrace();
//			}
//			CustomLog.e(TAG, "----------");
		}
	}
	
	// 직위 들고오기
	class getPositionKey extends WeakAsyncTask<Void, Void, Void,Activity> {
		public getPositionKey(Activity target) {
			super(target);
		}

		private boolean result;

		@Override
		protected void onPreExecute(Activity target) {
			layout_progressbar.setVisibility(LinearLayout.VISIBLE);
		}

		@Override
		protected Void doInBackground(Activity target,Void... params) {
			MybriefDAO dao = new MybriefDAO(MyBriefcaseBusinesscardModify_Activity.this);
			try {
				// input id values 1 for test
				result = dao.setPositionDtoList();
			} catch (Exception e) {
				result = false;
				e.printStackTrace();
			}
			if (result) {
				if (positionKey != null) {
					positionKey.clear();
				}
				positionKey = dao.getPositionDtoList();
			}
			return null;
		}

		protected void onPostExecute(Activity target,Void param) {
			if(result){
				new getNationKey(MyBriefcaseBusinesscardModify_Activity.this).execute();
			}else{
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
				layout_progressbar.setVisibility(LinearLayout.GONE);
				setEventListener();
			}
		}
	}
	
//	class setAddressList extends WeakAsyncTask<Void, Void, Void, Activity>{
//		boolean result = false;
//		public setAddressList(Activity target) {
//			super(target);
//		}
//		@Override
//		protected void onPreExecute(Activity target) {
//			layout_progressbar.setVisibility(LinearLayout.VISIBLE);
//		}
//		@Override
//		protected Void doInBackground(Activity target, Void... params) {
//			MybriefDAO dao = new MybriefDAO(MyBriefcaseBusinesscardModify_Activity.this);
//			try {
//				result = dao.setAddressList(edit_address_text.getText().toString());
//			} catch (Exception e) {
//				result = false;
//			}
//			if(result){
//				dto = dao.getBusinessCard();
//			}
//			return null;
//		}
//		@Override
//		protected void onPostExecute(Activity target, Void result) {
//			if(this.result){
////				UpdateUI();
//				new getPositionKey(MyBriefcaseBusinesscardModify_Activity.this).execute();
//			}else{
//				switch (StaticData.NOWLANGUAGE) {
//				case StaticData.KOREA:
//					Toast.makeText(getApplicationContext(),R.string.please_retry_ko, Toast.LENGTH_SHORT).show();
//					break;
//				case StaticData.ENGLISH:
//					Toast.makeText(getApplicationContext(),R.string.please_retry_en, Toast.LENGTH_SHORT).show();
//					break;
//				case StaticData.CHINA:
//					Toast.makeText(getApplicationContext(),R.string.please_retry_cn, Toast.LENGTH_SHORT).show();
//					break;
//				}
//				layout_progressbar.setVisibility(LinearLayout.GONE);
//				setEventListener();
//			}
//		}
//	}
	
	// 해당 유저 명암 수정 업데이트
	class setMyBusinessCardModify extends WeakAsyncTask<Void, Void, Void, Activity>{
		boolean result = false;
		public setMyBusinessCardModify(Activity target) {
			super(target);
		}
		
		@Override
		protected void onPreExecute(Activity target) {
			layout_progressbar.setVisibility(LinearLayout.VISIBLE);
		}
		
		@Override
		protected Void doInBackground(Activity target, Void... params) {
			MybriefDAO dao = new MybriefDAO(MyBriefcaseBusinesscardModify_Activity.this);
			try {
				result = dao.setBusinessCard();
			} catch (Exception e) {
				result = false;
			}
			if(result){
				dto = dao.getBusinessCard();
			}
			return null;
		}
		
		@Override
		protected void onPostExecute(Activity target, Void result) {
			if(this.result){
				UpdateUI();
			}
			layout_progressbar.setVisibility(LinearLayout.GONE);
			setEventListener();
		}
	}
	
	// 우편번호 및 주소 들고오기
	class setAddress extends WeakAsyncTask<Void, Void, Void, Activity>{
		boolean result = false;
		public setAddress(Activity target) {
			super(target);
		}
		
		@Override
		protected void onPreExecute(Activity target) {
			layout_progressbar.setVisibility(LinearLayout.VISIBLE);
		}
		
		@Override
		protected Void doInBackground(Activity target, Void... params) {
			MybriefDAO dao = new MybriefDAO(MyBriefcaseBusinesscardModify_Activity.this);
			try {
				result = dao.setAddressList(edit_address_text.getText().toString());
			} catch (Exception e) {
				result = false;
			}
			if(result){
				if(addressList != null){
					addressList.clear();
				}
				addressList = dao.getAddressList();
			}
			return null;
		}		
		
		@Override
		protected void onPostExecute(Activity target, Void result) {
			if(this.result){
				SetAddressAdapter();
			}
			layout_progressbar.setVisibility(LinearLayout.GONE);
		}
	}
	
	// update
	class setMyBusinessCardUpdate extends WeakAsyncTask<Void, Void, Void, Activity>{
		boolean result = false;
		public setMyBusinessCardUpdate(Activity target) {
			super(target);
		}
		
		@Override
		protected void onPreExecute(Activity target) {
			layout_progressbar.setVisibility(LinearLayout.VISIBLE);
		}
		
		@Override
		protected Void doInBackground(Activity target, Void... params) {
//			MybriefDAO dao = new MybriefDAO(MyBriefcaseBusinesscardModify_Activity.this);
			try {
//				result = dao.setBusinessCardUpdate(
//						dto.COMPANY, appellationID, edit_user_phone1.getText().toString(), 
//						edit_user_phone2.getText().toString(), 
//						edit_user_phone3.getText().toString(), 
//						text_user_address.getText().toString(),
//						zipCode,
//						edit_user_name.getText().toString(),
//						edit_user_email.getText().toString()
//						);
				
				result = upload();
			} catch (Exception e) {
				result = false;
			}
			return null;
		}
		
		@Override
		protected void onPostExecute(Activity target, Void result) {
			if(this.result){
				switch (StaticData.NOWLANGUAGE) {
				case StaticData.KOREA:
					Toast.makeText(getApplicationContext(),R.string.success_ko, Toast.LENGTH_SHORT).show();
					break;
				case StaticData.ENGLISH:
					Toast.makeText(getApplicationContext(),R.string.success_en, Toast.LENGTH_SHORT).show();
					break;
				case StaticData.CHINA:
					Toast.makeText(getApplicationContext(),R.string.success_cn, Toast.LENGTH_SHORT).show();
					break;
				}
				Intent intent = new Intent(getApplicationContext(), MyBriefcaseBusinesscard_Activity.class);
				startActivity(intent);
				overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
				finish();
			}else{
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
			}
			layout_progressbar.setVisibility(LinearLayout.GONE);
		}
	}
	
	
	public void executeCorp() {
		try {
			Intent intent = new Intent(Intent.ACTION_PICK);
			intent.setType("image/*");
			startActivityForResult(Intent.createChooser(intent, "select"),PHOTO_PICKED);
		} catch (ActivityNotFoundException e) {
			Log.e("Log", e.getMessage());
		}
	}
	
	
	private static final String TEMP_PHOTO_FILE = "temp.jpg"; // 임시 저장파일명
	private static final int PHOTO_PICKED = 0;
	private static final int CROP_FROM_CAMERA = 1;
	Uri mImageCaptureUri;
	boolean modifiedImg = false;		// 사진 수정여부에 관한 플레그
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (resultCode == RESULT_OK) {
			switch (requestCode) {
			// 사진 선택시
			case PHOTO_PICKED:
				try {
					mImageCaptureUri = data.getData();

					Intent intent = new Intent("com.android.camera.action.CROP");
					intent.setType("image/*");
					 List<ResolveInfo> list = getPackageManager().queryIntentActivities( intent, 0 );
					intent.setData(mImageCaptureUri);
					// Crop options
					 intent.putExtra("outputX", 300);
					 intent.putExtra("outputY", 400);
					 intent.putExtra("aspectX", 3);
					 intent.putExtra("aspectY", 4);
					 intent.putExtra("scale", true);
					intent.putExtra(MediaStore.EXTRA_OUTPUT, getTempUri()); // 파일 임시 저장

					Intent i = new Intent(intent);
					ResolveInfo res = list.get(0);
					i.setComponent(new ComponentName(
							res.activityInfo.packageName, res.activityInfo.name));
					startActivityForResult(i, CROP_FROM_CAMERA);
					modifiedImg = true;
				} catch (Exception e) {
					Log.e("Log", e.getMessage());
					modifiedImg = false;
				}

				break;

			// 잘라낸 이미지를 보여줌
			case CROP_FROM_CAMERA:
				String path = getTempUri().getPath();
				file = new File(path);
				Bitmap selectedImage = BitmapFactory.decodeFile(path);
				CustomLog.e("", "bitmap getDensity() : "+selectedImage.getDensity());
				img_mypic.setImageBitmap(selectedImage);
				break;
			}

		} else {
			Log.e("Log", "실패");
		}
	}
	
	private void SaveBitmapToFileCache(Bitmap bitmap)
	{
	        File fileCacheItem = new File(Environment.getExternalStorageDirectory(),TEMP_PHOTO_FILE);
	    	OutputStream out = null;

	    	try
	    	{
	    		fileCacheItem.createNewFile();
	    		out = new FileOutputStream(fileCacheItem);
	    		            
	    		bitmap.compress(CompressFormat.JPEG, 100, out);
	    	}
	    	catch (Exception e) 
	    	{
	    		e.printStackTrace();
	    	}
	    	finally
	    	{
	    		try
	    		{
	    			out.close();
	    		}
	    		catch (IOException e)
	    		{
	    			e.printStackTrace();
	    		}
	    	}
	}
	
	public File file;
	
	private Uri getTempUri() {
	File f = new File(Environment.getExternalStorageDirectory(),TEMP_PHOTO_FILE);
		try {
			f.createNewFile();
		} catch (IOException e) {
			Log.e("Log", e.getMessage());
		}
		return Uri.fromFile(f);
	}
	
	
	public final String TAG = "00000";
	long percentPerFileSize = 0;
	/** MultiPartRequest를 이용한 File Upload 수행(With text parameter included) */
	private boolean upload() 
	{
		boolean result = false;

		CustomLog.e(TAG, "ProgressDialogFileUpload URL : " + StaticData.BUSINESSCARDUODATE_URL);
		
		HttpURLConnection conn = null;
		DataOutputStream dos = null;
		FileInputStream fis = null;
		InputStream iStream = null;
		
		try 
		{
			Log.e("chang-gi", "File uploading...");
			
			URL url = new URL(StaticData.BUSINESSCARDUODATE_URL);
			
			

		// Meta data string
	        StringBuffer postParameterBuilder = new StringBuffer();
	        
	        
//			edit_user_email.getText().toString()
	     // Text parameter
	        postParameterBuilder.append(MultipartRequestUtility.MULTIPART_DELIMITER);
	        postParameterBuilder.append(MultipartRequestUtility.setValue("user_cd", StaticData.USER_CD+""));
	        postParameterBuilder.append(MultipartRequestUtility.MULTIPART_DELIMITER);
	        postParameterBuilder.append(MultipartRequestUtility.setValue("company", dto.COMPANY+""));
	        postParameterBuilder.append(MultipartRequestUtility.MULTIPART_DELIMITER);
	        postParameterBuilder.append(MultipartRequestUtility.setValue("appellation_id", appellationID+""));
	        postParameterBuilder.append(MultipartRequestUtility.MULTIPART_DELIMITER);
	        postParameterBuilder.append(MultipartRequestUtility.setValue("phone1", edit_user_phone1.getText().toString().trim()+""));
	        postParameterBuilder.append(MultipartRequestUtility.MULTIPART_DELIMITER);
	        postParameterBuilder.append(MultipartRequestUtility.setValue("phone2", edit_user_phone2.getText().toString().trim()+""));
	        postParameterBuilder.append(MultipartRequestUtility.MULTIPART_DELIMITER);
	        postParameterBuilder.append(MultipartRequestUtility.setValue("phone3", edit_user_phone3.getText().toString().trim()+""));
	        postParameterBuilder.append(MultipartRequestUtility.MULTIPART_DELIMITER);
	        postParameterBuilder.append(MultipartRequestUtility.setValue("name", edit_user_name.getText().toString().trim()+""));
	        postParameterBuilder.append(MultipartRequestUtility.MULTIPART_DELIMITER);
	        postParameterBuilder.append(MultipartRequestUtility.setValue("email", edit_user_email.getText().toString().trim()+""));
	        postParameterBuilder.append(MultipartRequestUtility.MULTIPART_DELIMITER);
	        
	        postParameterBuilder.append(MultipartRequestUtility.setValue("street_address", edit_user_address.getText().toString().trim()+""));
	        postParameterBuilder.append(MultipartRequestUtility.MULTIPART_DELIMITER);
	        postParameterBuilder.append(MultipartRequestUtility.setValue("street_address_detail", edit_user_address_2.getText().toString().trim()+""));
	        postParameterBuilder.append(MultipartRequestUtility.MULTIPART_DELIMITER);
	        postParameterBuilder.append(MultipartRequestUtility.setValue("city", edit_user_city.getText().toString().trim()+""));
	        postParameterBuilder.append(MultipartRequestUtility.MULTIPART_DELIMITER);
	        postParameterBuilder.append(MultipartRequestUtility.setValue("state", edit_user_state.getText().toString().trim()+""));
	        postParameterBuilder.append(MultipartRequestUtility.MULTIPART_DELIMITER);
	        postParameterBuilder.append(MultipartRequestUtility.setValue("postal_code", edit_user_pastal.getText().toString().trim()+""));
	        postParameterBuilder.append(MultipartRequestUtility.MULTIPART_DELIMITER);
	        postParameterBuilder.append(MultipartRequestUtility.setValue("nation_id", now_nation_id+""));
	        
//	        postParameterBuilder.append(MultipartRequestUtility.setValue("address", edit_user_address.getText().toString().trim()+""));
//	        postParameterBuilder.append(MultipartRequestUtility.MULTIPART_DELIMITER);
//	        postParameterBuilder.append(MultipartRequestUtility.setValue("zipcode", zipCode+""));
//	        postParameterBuilder.append(MultipartRequestUtility.MULTIPART_DELIMITER);
//	        postParameterBuilder.append(MultipartRequestUtility.MULTIPART_DELIMITER);
//	        postParameterBuilder.append(MultipartRequestUtility.setValue("user_cd", StaticData.USER_CD+""));
//	        postParameterBuilder.append(MultipartRequestUtility.MULTIPART_DELIMITER);
//	        postParameterBuilder.append(MultipartRequestUtility.setValue("company", 1+""));
//	        postParameterBuilder.append(MultipartRequestUtility.MULTIPART_DELIMITER);
//	        postParameterBuilder.append(MultipartRequestUtility.setValue("appellation_id", 1+""));
//	        postParameterBuilder.append(MultipartRequestUtility.MULTIPART_DELIMITER);
//	        postParameterBuilder.append(MultipartRequestUtility.setValue("phone1", 1+""));
//	        postParameterBuilder.append(MultipartRequestUtility.MULTIPART_DELIMITER);
//	        postParameterBuilder.append(MultipartRequestUtility.setValue("phone2", 1+""));
//	        postParameterBuilder.append(MultipartRequestUtility.MULTIPART_DELIMITER);
//	        postParameterBuilder.append(MultipartRequestUtility.setValue("phone3", 1+""));
//	        postParameterBuilder.append(MultipartRequestUtility.MULTIPART_DELIMITER);
//	        postParameterBuilder.append(MultipartRequestUtility.setValue("address", 1+""));
//	        postParameterBuilder.append(MultipartRequestUtility.MULTIPART_DELIMITER);
//	        postParameterBuilder.append(MultipartRequestUtility.setValue("zipcode", 1+""));
//	        postParameterBuilder.append(MultipartRequestUtility.MULTIPART_DELIMITER);
//	        postParameterBuilder.append(MultipartRequestUtility.setValue("name", 1+""));
//	        postParameterBuilder.append(MultipartRequestUtility.MULTIPART_DELIMITER);
//	        postParameterBuilder.append(MultipartRequestUtility.setValue("email", 1+""));
	        
	        if(modifiedImg){
	        	/***** Meta data 작성 *****/
	    		// 업로드 파일의 확장자 추출 및 파일이름 변경
    	        String extentionName = this.file.getName().substring(this.file.getName().lastIndexOf("."));
    	        String uploadedFileName = "user_"+StaticData.USER_CD+"_"+StaticData.CONFERENCE_ID+extentionName;

	   	     // File parameter
		        postParameterBuilder.append(MultipartRequestUtility.MULTIPART_DELIMITER);
		        postParameterBuilder.append(MultipartRequestUtility.setFileHeader("picture", uploadedFileName.trim()+""));	        	
	        }
	        postParameterBuilder.append("\r\n");
	        
	        int fullPacketSize = 0;
	     // 전체 사이즈
	        if(modifiedImg){
	        	fullPacketSize = postParameterBuilder.toString().length()+(int)this.file.length()+MultipartRequestUtility.MULTIPART_END_DELIMITER.length()+MultipartRequestUtility.MULTIPART_LINEEND.length();
	        }else{
	        	fullPacketSize = postParameterBuilder.toString().length()+MultipartRequestUtility.MULTIPART_END_DELIMITER.length()+MultipartRequestUtility.MULTIPART_LINEEND.length();
	        }
	        
//	        Log.e("0000","Sending file size : "+this.file.length());
//	        Log.e("0000","Sending full packet size : "+fullPacketSize);
	        
	        conn = (HttpURLConnection) url.openConnection();
			conn.setDoInput(true);
			conn.setDoOutput(true);
			conn.setUseCaches(false);
			conn.setRequestMethod("POST");
			conn.setConnectTimeout(1000*60*60);
			conn.setRequestProperty("Connection", "KeepAlive");
			conn.setRequestProperty("Content-Type", "multipart/form-data;boundary="+MultipartRequestUtility.MULTIPART_BOUNDARY);
	        
//		// 대용량 파일 전송모드 (data 전송 사이즈를 고정)
//			conn.setFixedLengthStreamingMode(fullPacketSize);
	        
	    // 1% 당 전송 바이트 지정(Progress 용)
//	        this.percentPerFileSize = this.file.length() / 100;
	        
	    // Stream 전송
	        dos = new DataOutputStream(new BufferedOutputStream(conn.getOutputStream()));
	        dos.writeUTF(postParameterBuilder.toString());
	        
	        if(modifiedImg){
	        	fis = new FileInputStream(this.file);
	        	int maxBufferSize = 1024;
		        int bufferSize = Math.min(fis.available(), maxBufferSize);
		        byte[] buffer = new byte[bufferSize];
		 
		        // 버퍼 크기만큼 파일로부터 바이트 데이터를 읽는다.
		        int byteRead = fis.read(buffer, 0, bufferSize);
		 
		        // 전송
		        while (byteRead > 0) {
		            dos.write(buffer);
		            bufferSize = Math.min(fis.available(), maxBufferSize);
		            byteRead = fis.read(buffer, 0, bufferSize);
		        }
	        }
	        
	        dos.writeBytes(MultipartRequestUtility.MULTIPART_END_DELIMITER);
	        dos.flush();
	        
		// Response message receive
			iStream = conn.getInputStream();
			// -----------------------------------------------------------------------------------------
			InputStreamReader iStreamReader = new InputStreamReader(iStream);
			BufferedReader readBuffer = new BufferedReader(iStreamReader);

			StringBuilder sBuilder = new StringBuilder();
			String sLine;

			while ((sLine = readBuffer.readLine()) != null) {
				sBuilder.append(sLine + "\n");
			}

			iStream.close();
			String xmlDocument="";

			if (sBuilder.toString().length() > 1) {
				xmlDocument = sBuilder.toString();
			}
			Log.e("setAddressList", "xmlDocument : " + xmlDocument);

			DocumentBuilderFactory factory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document document = builder.parse(new InputSource(
					new StringReader(xmlDocument.toString())));
			NodeList testList = document.getElementsByTagName("BUSINESSCARD_UPDATE");
			for (int i = 0; i < testList.getLength(); i++) {
				Node test = testList.item(i);
				NodeList properties = test.getChildNodes();
				AddressDto addressdto = new AddressDto();
				for (int j = 0; j < properties.getLength(); j++) {
					Node property = properties.item(j);
					String nodeName = property.getNodeName().trim();
					if (nodeName.toUpperCase().equals("FLAG")) {
						if(property.getTextContent().equals("success")){
							result = true;
						}
					}
				}
			}
			// -----------------------------------------------------------------------------------------
			
//			String responseMessage = MultipartRequestUtility.convertStreamToString(iStream);
//			
//			Log.e("0000","Media file upload server response message is :\n"+responseMessage);
//			if(responseMessage.contains("Success")){
//				result = true;
//				Log.e("0000", "Image File upload is successfully.");
//			}
//			if (responseMessage.replace("\n", "").equals("Success")) 
//			{
//				result = true;
//				Log.e("0000", "Image File upload is successfully.");
//			}
		}
		catch (MalformedURLException e) {
			e.printStackTrace();
		} 
		catch (IOException e) {
			Log.e("0000", "Error, IOException : " + e.getMessage());
			e.printStackTrace();
		} 
		catch (NullPointerException e) {
			Log.e("0000", "Error, NullPointerException : " + e.getMessage());
			e.printStackTrace();
		} 
		catch (Exception e) {
			Log.e("0000", "Error, Exception : " + e.getMessage());
		}
		finally // Release I.O Resources 
		{
			if(fis!=null) {
				try {fis.close();} 
				catch (IOException e) {}
			}
			if(conn!=null) {
				conn.disconnect();
				Log.e("0000","Server has disconnect.");
			}
			if(dos!=null) {
				try {dos.close();} 
				catch (IOException e) {}
			}
			if(iStream!=null) {
				try {iStream.close();} 
				catch (IOException e) {}
			}
		}

		return result;
	}
		
}
