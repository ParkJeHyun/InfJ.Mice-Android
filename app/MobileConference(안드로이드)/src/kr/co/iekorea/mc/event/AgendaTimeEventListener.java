package kr.co.iekorea.mc.event;

import kr.co.iekorea.mc.R;
import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;

public class AgendaTimeEventListener implements OnClickListener {
	private Context ctx;
	private Activity activity;
	
	public AgendaTimeEventListener(Context ctx, Activity activity){
		this.ctx = ctx;
		this.activity = activity;
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.btn_back:
			activity.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
			activity.finish();
			break;
		}
	}
}
