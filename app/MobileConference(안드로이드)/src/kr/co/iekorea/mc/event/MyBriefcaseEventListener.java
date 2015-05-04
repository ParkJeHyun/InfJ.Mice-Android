package kr.co.iekorea.mc.event;

import kr.co.iekorea.mc.Main_Activity;
import kr.co.iekorea.mc.MyBriefcaseBusinesscardHolder_Activity;
import kr.co.iekorea.mc.MyBriefcaseBusinesscard_Activity;
import kr.co.iekorea.mc.MyBriefcaseMemo_Activity;
import kr.co.iekorea.mc.MyBriefcasePresent_Activity;
import kr.co.iekorea.mc.MyBriefcaseSchedule_Activity;
import kr.co.iekorea.mc.R;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;

public class MyBriefcaseEventListener implements OnClickListener {
	private Context ctx;
	private Activity activity;
	private Intent intent;
	
	public MyBriefcaseEventListener(Context ctx, Activity activity){
		this.ctx = ctx;
		this.activity = activity;
		
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.btn_back:
			intent = new Intent(ctx,Main_Activity.class);
			ctx.startActivity(intent);
			activity.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
			activity.finish();
			break;
			
		case R.id.btn_businesscard:
			intent = new Intent(ctx,MyBriefcaseBusinesscard_Activity.class);
			ctx.startActivity(intent);
			activity.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
			activity.finish();
			break;
			
		case R.id.btn_businesscard_holder:
			intent = new Intent(ctx,MyBriefcaseBusinesscardHolder_Activity.class);
			ctx.startActivity(intent);
			activity.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
			activity.finish();
			break;
			
		case R.id.btn_memo:
			intent = new Intent(ctx,MyBriefcaseMemo_Activity.class);
			ctx.startActivity(intent);
			activity.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
			activity.finish();
			break;
			
		case R.id.btn_schedule:
			intent = new Intent(ctx,MyBriefcaseSchedule_Activity.class);
			ctx.startActivity(intent);
			activity.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
			activity.finish();
			break;
			
		case R.id.btn_present:
			intent = new Intent(ctx,MyBriefcasePresent_Activity.class);
			ctx.startActivity(intent);
			activity.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
			activity.finish();
			break;
		}
	}
}
