package kr.co.iekorea.mc.event;

import kr.co.iekorea.mc.Binder_Data_Activity;
import kr.co.iekorea.mc.Binder_Sponsor_Activity;
import kr.co.iekorea.mc.Main_Activity;
import kr.co.iekorea.mc.R;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;

public class BinderEventListener implements OnClickListener {
	private Context ctx;
	private Activity activity;
	private Intent intent;
	
	public BinderEventListener(Context ctx, Activity activity){
		this.ctx = ctx;
		this.activity = activity;
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.btn_back:
			intent = new Intent(ctx, Main_Activity.class);
			ctx.startActivity(intent);
			activity.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
			activity.finish();
			break;
			
		case R.id.btn_data:
			intent = new Intent(ctx, Binder_Data_Activity.class);
			ctx.startActivity(intent);
			activity.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
			activity.finish();
			
			break;
			
		case R.id.btn_sponsor:
			intent = new Intent(ctx, Binder_Sponsor_Activity.class);
			ctx.startActivity(intent);
			activity.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
			activity.finish();
			break;
			
		case R.id.btn_stayhome:
			break;
			
		}
	}

}
