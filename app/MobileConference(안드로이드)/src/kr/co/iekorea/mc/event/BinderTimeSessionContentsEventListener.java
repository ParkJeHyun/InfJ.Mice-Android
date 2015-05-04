package kr.co.iekorea.mc.event;

import kr.co.iekorea.mc.BinderSendQnaContent_Activity;
import kr.co.iekorea.mc.Binder_Data_Session_Time_Activity;
import kr.co.iekorea.mc.R;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;

public class BinderTimeSessionContentsEventListener implements OnClickListener {
	private Context ctx;
	private Activity activity;
	private int user_cd;
	private String user_name;
	private int binder_id;
	private int session_id;
	
	public BinderTimeSessionContentsEventListener(Context ctx, Activity activity){
		this.ctx = ctx;
		this.activity = activity;
	}
	
	public BinderTimeSessionContentsEventListener(Context ctx, Activity activity, int session_id,int binder_id){
		this.ctx = ctx;
		this.activity = activity;
		this.session_id = session_id;
		this.binder_id = binder_id;
	}
	
	public BinderTimeSessionContentsEventListener(Context ctx, Activity activity, int user_cd, String user_name){
		this.ctx = ctx;
		this.activity = activity;
		this.user_cd = user_cd;
		this.user_name = user_name;
	}

	private Intent intent;
	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.btn_back:
			intent = new Intent(ctx, Binder_Data_Session_Time_Activity.class);
			intent.putExtra("session_id", session_id);
			intent.putExtra("binder_id", binder_id);
			activity.startActivity(intent);
			activity.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
			activity.finish();
			break;
			
		case R.id.btn_qna:
			intent = new Intent(ctx, BinderSendQnaContent_Activity.class);
			intent.putExtra("user_cd", user_cd);
			intent.putExtra("user_name",user_name);
			activity.startActivity(intent);
			activity.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
//			Toast.makeText(ctx, "start send message", Toast.LENGTH_SHORT).show();
			break;
		}
	}
}
