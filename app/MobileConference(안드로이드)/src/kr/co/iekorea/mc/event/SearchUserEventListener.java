package kr.co.iekorea.mc.event;

import kr.co.iekorea.mc.R;
import kr.co.iekorea.mc.Search_Activity;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;

public class SearchUserEventListener implements OnClickListener {
	private Context ctx;
	private Activity activity;
	private Intent intent;
	
	public SearchUserEventListener(Context ctx, Activity activity){
		this.ctx = ctx;
		this.activity = activity;
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.btn_back:
			intent = new Intent(ctx, Search_Activity.class);
			ctx.startActivity(intent);
			activity.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
			activity.finish();
			break;
		}
	}

}
