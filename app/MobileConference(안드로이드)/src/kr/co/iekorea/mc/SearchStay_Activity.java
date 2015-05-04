package kr.co.iekorea.mc;

import java.util.ArrayList;
import java.util.List;

import kr.co.iekorea.mc.event.SearchStayEventListener;
import kr.co.iekorea.mc.map.OnSingleTapListener;
import kr.co.iekorea.mc.map.SimpleItemizedOverlay;
import kr.co.iekorea.mc.map.TapControlledMapView;
import kr.co.iekorea.mc.staticdata.StaticData;
import kr.co.iekorea.mc.util.BaseInterface;
import kr.co.iekorea.mc.util.ProcessManager;
import kr.co.iekorea.mc.util.WeakAsyncTask;
import kr.co.iekorea.mc.xml.SearchDAO;
import kr.co.iekorea.mc.xml.StayDto;
import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

public class SearchStay_Activity extends MapActivity implements BaseInterface {
	// Activity control.
	private ProcessManager processManager;

	// layout area
	private ImageButton btn_back;
	private LinearLayout layout_header;
	private LinearLayout layout_progressbar;
	
	// for map
	public TapControlledMapView mapView; // use the custom TapControlledMapView
	public List<Overlay> mapOverlays;
	public Drawable drawable;
	public Drawable drawable2;
	public SimpleItemizedOverlay itemizedOverlay;
	public SimpleItemizedOverlay itemizedOverlay2;
	
	Bundle savedInstanceState;
	ArrayList<StayDto> items = new ArrayList<StayDto>();
	ArrayList<GeoPoint> pointList = new ArrayList<GeoPoint>();
	
	// 언어
	private TextView total_title,text_wait;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.savedInstanceState = savedInstanceState;
		this.initView();
		if (StaticData.logined) {
			this.getXmlResources();
			this.modifyXmlResources();
			
			// for test
			new getSearchStayInformation(SearchStay_Activity.this).execute();
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
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		
		// example saving focused state of overlays
		if (itemizedOverlay.getFocus() != null) outState.putInt("focused_1", itemizedOverlay.getLastFocusedIndex());
		if (itemizedOverlay2.getFocus() != null) outState.putInt("focused_2", itemizedOverlay2.getLastFocusedIndex());
		super.onSaveInstanceState(outState);
	}
	
	public void getPointMap(Bundle savedInstanceState){
		this.mapView = (TapControlledMapView) findViewById(R.id.mapview);
		mapView.setBuiltInZoomControls(true);
		
		// dismiss balloon upon single tap of MapView (iOS behavior) 
		mapView.setOnSingleTapListener(new OnSingleTapListener() {		
			@Override
			public boolean onSingleTap(MotionEvent e) {
				itemizedOverlay.hideAllBalloons();
				return true;
			}
		});
		
		mapOverlays = mapView.getOverlays();
		
		// first overlay													// 숙박
		drawable = getResources().getDrawable(R.drawable.bg_lodge);
		itemizedOverlay = new SimpleItemizedOverlay(drawable, mapView,this);
		// set iOS behavior attributes for overlay
		itemizedOverlay.setShowClose(false);
		itemizedOverlay.setShowDisclosure(true);
		itemizedOverlay.setSnapToCenter(false);
		
		// second overlay													// 식당
		drawable2 = getResources().getDrawable(R.drawable.bg_restaurant);
		itemizedOverlay2 = new SimpleItemizedOverlay(drawable2, mapView, this);
		// set iOS behavior attributes for overlay
		itemizedOverlay2.setShowClose(false);
		itemizedOverlay2.setShowDisclosure(true);
		itemizedOverlay2.setSnapToCenter(false);
		
		for(int i=0; i< items.size(); i++){
			StayDto temp = items.get(i);
			GeoPoint tempPoint = new GeoPoint((int)(temp.Lat*1E6),(int)(temp.Lon*1E6));
			OverlayItem overlayItem = new OverlayItem(tempPoint, temp.title,temp.subTitle);
			if(temp.state.equals("숙박")){
				itemizedOverlay.addOverlay(overlayItem);
				itemizedOverlay.addStateItem(temp);
			}else{
				itemizedOverlay2.addOverlay(overlayItem);
				itemizedOverlay2.addStateItem(temp);
			}
			pointList.add(tempPoint);
		}
		if(itemizedOverlay.size() > 0) mapOverlays.add(itemizedOverlay);
		if(itemizedOverlay2.size() > 0) mapOverlays.add(itemizedOverlay2);
		
		if (savedInstanceState == null) {
			
			final MapController mc = mapView.getController();
			
			if(pointList.size() != 0){
				mc.animateTo(pointList.get(0));				
			}
			
			mc.setZoom(14);
		} else {
			
			// example restoring focused state of overlays
			int focused;
			focused = savedInstanceState.getInt("focused_1", -1);
			if (focused >= 0) {
				itemizedOverlay.setFocus(itemizedOverlay.getItem(focused));
			}
			focused = savedInstanceState.getInt("focused_2", -1);
			if (focused >= 0) {
				itemizedOverlay2.setFocus(itemizedOverlay2.getItem(focused));
			}
		}
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		this.processManager.deleteActivity(this);
	}
	
	@Override
	public void onBackPressed() {
		super.onBackPressed();
//		Intent intent = new Intent(this, Search_Activity.class);
//		startActivity(intent);
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
		this.setContentView(R.layout.activity_search_stay);
		this.processManager = ProcessManager.getInstance();
		this.processManager.addActivity(this);
	}

	@Override
	public void getXmlResources() {
		this.btn_back = (ImageButton) this.findViewById(R.id.btn_back);
		this.layout_header = (LinearLayout) this.findViewById(R.id.layout_header);
		this.layout_progressbar = (LinearLayout) this.findViewById(R.id.layout_progressbar);
		
		// for map
		this.mapView = (TapControlledMapView) findViewById(R.id.mapview);
		
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
			this.total_title.setText(R.string.accommodation_ko);
			this.text_wait.setText(R.string.wait_ko);
			break;
		case StaticData.ENGLISH:
			this.btn_back.setImageResource(R.drawable.b_back_en);
			this.total_title.setText(R.string.accommodation_en);
//			this.total_title.setTextSize(11);
			this.text_wait.setText(R.string.wait_en);
			break;
		case StaticData.CHINA:
			this.btn_back.setImageResource(R.drawable.b_back_cn);
			this.total_title.setText(R.string.accommodation_cn);
			this.text_wait.setText(R.string.wait_cn);
			break;
		}
	}

	@Override
	public void setEventListener() {
		this.btn_back.setOnClickListener(new SearchStayEventListener(this,this));
	}
	
	class getSearchStayInformation extends WeakAsyncTask<Void,Void,Void,Activity>
	{	
		public getSearchStayInformation(Activity target) {
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
		protected Void doInBackground(Activity target,Void... params) 
		{
			try
			{
				SearchDAO dao = new SearchDAO(SearchStay_Activity.this);
				result = dao.setStayInformaion();
				if(result){
					if(SearchStay_Activity.this.items != null){
						SearchStay_Activity.this.items.clear();
					}
					SearchStay_Activity.this.items = dao.getStayList();
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
				// 해당 좌표 및 정보를 받아오기가 성공하였다면 해당 좌표대로 map에 표시해 준다.
				getPointMap(savedInstanceState);
			}else{
				// 값을 받아오지 못했다면 다음 에러 메세지 출력.
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
			// 프로그래스 사라짐.
			setEventListener();
			layout_progressbar.setVisibility(LinearLayout.GONE);
		}
	}
	
//	// for map
//	class Cus_Marker extends BalloonItemizedOverlay<OverlayItem>{
//		
//		private ArrayList<OverlayItem> m_overlays = new ArrayList<OverlayItem>();
//		private Context c;
//
//		public Cus_Marker(Drawable defaultMarker) {
//			super(defaultMarker,mapView);
//			boundCenterBottom(defaultMarker);
//			c = mapView.getContext();
//		}
//		
//		public void addOverylay(OverlayItem overlay){
//			m_overlays.add(overlay);
//			populate();
//		}
//
//		@Override
//		protected OverlayItem createItem(int i) {
//			return m_overlays.get(i);
//		}
//
//		@Override
//		public int size() {
//			return m_overlays.size();
//		}
//		
//		@Override
//		protected boolean onBalloonTap(int index) {
//			Toast.makeText(c, "말풍선을 클릭", Toast.LENGTH_SHORT).show();
//			mControl.setCenter(mapView.getItem(index).getPoint());
//			return true;
//		}
//	}
//	
//	// 현재 위치를 지도 센터로 옯기는 기능
//	@Override
//	protected void onResume() {
//		super.onResume();
//		mLocation.enableMyLocation();
//		mLocation.enableCompass();
//	}
//	
//	@Override
//	protected void onPause() {
//		super.onPause();
//		mLocation.disableMyLocation();
//		mLocation.disableCompass();
//	}
	
//	class MyLocation extends MyLocationOverlay{
//
//		public MyLocation(Context context, MapView mapView) {
//			super(context, mapView);
//			// TODO Auto-generated constructor stub
//		}
//		
//		// 현제 위치를 클릭했을때 실행.
//		public boolean dispatchTap(){
//			Toast.makeText(getApplicationContext(), "현재 위치 입니다", Toast.LENGTH_SHORT).show();
//			return false;
//		}
//		
//	}

	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}
}
