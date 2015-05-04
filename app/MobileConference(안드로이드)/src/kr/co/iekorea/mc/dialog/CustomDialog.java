package kr.co.iekorea.mc.dialog;

import android.app.Dialog;
import android.content.Context;

public class CustomDialog extends Dialog {
	private Context ctx;
	private String contents;

	public CustomDialog(Context context) {
		super(context);
		this.ctx = context;
		// TODO Auto-generated constructor stub
	}

	protected CustomDialog(Context context, boolean cancelable,
			OnCancelListener cancelListener) {
		super(context, cancelable, cancelListener);
		this.ctx = context;
		// TODO Auto-generated constructor stub
	}

	public CustomDialog(Context context, int theme) {
		super(context, theme);
		this.ctx = context;
		// TODO Auto-generated constructor stub
	}
	
	public CustomDialog(Context context, String contents) {
		super(context);
		this.ctx = context;
		this.contents = contents;
		this.init();
	}
	
	public void init(){
		
	}
	
}
