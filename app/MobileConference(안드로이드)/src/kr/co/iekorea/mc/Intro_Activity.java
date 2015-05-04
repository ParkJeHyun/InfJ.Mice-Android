package kr.co.iekorea.mc;

import kr.co.iekorea.mc.util.BaseInterface;
import kr.co.iekorea.mc.util.ProcessManager;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class Intro_Activity extends Activity implements BaseInterface{
	public static final String TAG = "Intro_activity";

	private Runnable translateRun;
	private Handler translateHandler;
	
	//Activity control.
	ProcessManager processManager;
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.initView();
        
    }
    
    @Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		processManager.deleteActivity(Intro_Activity.this);
	}

	@Override
	public void initView() {
		// TODO Auto-generated method stub
		this.setContentView(R.layout.activity_intro);
        
        processManager = ProcessManager.getInstance();
		processManager.addActivity(Intro_Activity.this);
        
        translateRun = new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				Intent i = new Intent(getApplicationContext(), Login_Activity.class);
		    	startActivity(i);
		    	overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
		    	finish();
			}
		};
		
		translateHandler = new Handler();
	    translateHandler.postDelayed(translateRun, 2000); //delay time
	}

	@Override
	public void getXmlResources() {
	}

	@Override
	public void modifyXmlResources() {
	}

	@Override
	public void setEventListener() {
	}
	
}
