package kr.co.iekorea.mc.event;

import kr.co.iekorea.mc.MessageInboxDetail_Activity;
import kr.co.iekorea.mc.MessageInboxReplay_Activity;
import kr.co.iekorea.mc.MessageInbox_Activity;
import kr.co.iekorea.mc.R;
import kr.co.iekorea.mc.xml.MessageDetailDto;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;

public class MessageInboxDetailEvent implements OnClickListener {
	public Context ctx;
	public Activity activity;
	public Intent intent;
	public String user_name;
	public String message_type;
	public MessageDetailDto messageDetailDto;
	
	public MessageInboxDetailEvent(Context ctx, Activity activity){
		this.ctx = ctx;
		this.activity = activity;
	}
	
	public MessageInboxDetailEvent(Context ctx, Activity activity, String user_name, String message_type
			,MessageDetailDto messageDetailDto){
		this.ctx = ctx;
		this.activity = activity;
		this.user_name = user_name;
		this.message_type = message_type;
		this.messageDetailDto = messageDetailDto;
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.btn_back:
			intent = new Intent(ctx, MessageInbox_Activity.class);
			activity.startActivity(intent);
			activity.finish();
			break;
			
		case R.id.btn_replay:
			intent = new Intent(ctx, MessageInboxReplay_Activity.class);
			intent.putExtra("message_id", ((MessageInboxDetail_Activity)activity).message_id);
			intent.putExtra("user_name", user_name);
			intent.putExtra("message_type", message_type);
			intent.putExtra("messageDetailDto", messageDetailDto);
			activity.startActivity(intent);
			activity.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
//			activity.finish();
			break;
		}
	}

}
