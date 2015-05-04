package kr.co.iekorea.mc.event;

import kr.co.iekorea.mc.R;
import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

public class AgendaTimeSessionContentsEventListener implements OnClickListener {
	private Context ctx;
	private Activity activity;
	
	public AgendaTimeSessionContentsEventListener(Context ctx, Activity activity){
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
			
		case R.id.btn_add_schedule:
			Toast.makeText(ctx, "click add schedule", Toast.LENGTH_SHORT).show();
			break;
		}
	}

}
