package kr.co.iekorea.mc.event;

import kr.co.iekorea.mc.MessageInbox_Activity;
import kr.co.iekorea.mc.Message_Activity;
import kr.co.iekorea.mc.R;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;

public class MessageInBoxEventListener implements OnClickListener {
	public Context ctx;
	public Activity activity;
	
	public MessageInBoxEventListener(Context ctx, Activity activity){
		this.ctx = ctx;
		this.activity = activity;
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.btn_back:
			if(((MessageInbox_Activity)activity).adapter != null){
				if(((MessageInbox_Activity)activity).adapter.deleteMode){
					((MessageInbox_Activity)activity).adapter.changeDeleteMode();
				}else{
					Intent intent = new Intent(ctx, Message_Activity.class);
					activity.startActivity(intent);
					activity.finish();				
				}				
			}else{
				Intent intent = new Intent(ctx, Message_Activity.class);
				activity.startActivity(intent);
				activity.finish();
			}
			
			break;
		}
	}

}
