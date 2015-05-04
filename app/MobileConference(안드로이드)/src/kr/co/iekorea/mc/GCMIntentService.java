package kr.co.iekorea.mc;

import kr.co.iekorea.mc.staticdata.StaticData;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gcm.GCMBaseIntentService;

public class GCMIntentService extends GCMBaseIntentService {
	private static final String TAG = "GCM";
	private static final String INSERT_PAGE = "http://자신의 서버 주소/insert_registration.ph";
//	private static final String SENDER_ID = "784613207481";
	private GCMHttpConnect httpConnect = null;
	private GCMHttpConnect.Request httpRequest = new GCMHttpConnect.Request() {
		
		@Override
		public void OnComplete() {
			// TODO Auto-generated method stub
			showToast();
		}
	};
	
	public GCMIntentService() {
		super(StaticData.SENDER_ID);
		Log.e(TAG, "GCMIntentService()");
	}

	@Override
	protected void onMessage(Context context, Intent intent) {
		Log.e(TAG, "onMessage()");
		if (intent.getAction().equals("com.google.android.c2dm.intent.RECEIVE")) {
			showMessage(context, intent);
		}
	}

	@Override
	protected void onError(Context context, String msg) {
		// TODO Auto-generated method stub
		Log.w(TAG, "onError!! " + msg);
	}

	@Override
	protected void onRegistered(Context context, String regID) {
		// TODO Auto-generated method stub
		Log.e(TAG, "onRegistered()");
		if(!regID.equals("") || regID != null){
			Log.w(TAG, "onRegistered!! " + regID);
			StaticData.gcmRegistrationId = regID;
			
//			단일 전송일때 주석처리
//			insertRegistrationID(regID);
		}
	}

	@Override
	protected void onUnregistered(Context context, String regID) {
		// TODO Auto-generated method stub
		Log.w(TAG, "onUnregistered!! " + regID);
		StaticData.gcmRegistrationId = null;
	}
	
	public void showToast(){
		Toast.makeText(this, "RegID 등록완료", Toast.LENGTH_LONG).show();
	}
	
	PendingIntent pendingIntent;
	private void showMessage(Context context, Intent intent){
		String title = intent.getStringExtra("title");
		String msg = intent.getStringExtra("msg");
		String ticker = intent.getStringExtra("ticker");
		String msgType = intent.getStringExtra("msgType");
		
		NotificationManager notificationManager = (NotificationManager)context.getSystemService(Activity.NOTIFICATION_SERVICE);
		
		// 노티피 케이션 클릭시 진행
		// 현 로그인이 되어 있다면 수신함으로
		if(StaticData.logined){
			// 약속이라면 약속으로
			if(msgType.equals("promise")){ // other key values is "message"
				pendingIntent = PendingIntent.getActivity(context, 0, 
						new Intent(context, MessageAppointment_Activity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK), 0);
			}
			else // 약속이 아니라면 수신함으로
			{
				pendingIntent = PendingIntent.getActivity(context, 0, 
						new Intent(context, MessageInbox_Activity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK), 0);				
			}
		}
		// 로그인이 되어있지 않은 상태일 경우 로그인 페이지로 이동
		else		
		{
			pendingIntent = PendingIntent.getActivity(context, 0, 
					new Intent(context, Login_Activity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK), 0);
		}
		
		//		해당어플을 실행하는 이벤트를 하고싶을때 아래주석 플기
//		PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, 
//				new Intent(context, 어플이 시작되는 처음 엑티비티.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK), 0);
		
//		PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, new Intent(), 0);
		
		Notification notification = new Notification();
		notification.icon = R.drawable.ic_launcher;
		notification.tickerText = ticker;
		notification.when = System.currentTimeMillis();
		notification.vibrate = new long[] { 500, 100, 500, 100 };
		notification.sound = Uri.parse("/system/media/audio/notifications/20_Cloud.ogg");
		notification.flags = Notification.FLAG_AUTO_CANCEL;
		notification.setLatestEventInfo(context, title, msg, pendingIntent);
		
		notificationManager.notify(0, notification);
	}
	
	public void insertRegistrationID(String id){
		httpConnect = new GCMHttpConnect(INSERT_PAGE + "?regID=" + id, httpRequest);
		httpConnect.start();
	}
}
