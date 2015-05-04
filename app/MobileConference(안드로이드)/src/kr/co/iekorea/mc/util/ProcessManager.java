package kr.co.iekorea.mc.util;

import java.util.ArrayList;
import android.app.Activity;

public class ProcessManager {     
	private static  ProcessManager  instance = null;     
	private ArrayList<Activity>   mActivityArr;           
		
	private ProcessManager() {         
		// Constructor         
		mActivityArr = new ArrayList<Activity>();     
	}           
		
	public static ProcessManager getInstance() {         
		if(ProcessManager.instance == null) {             
			synchronized(ProcessManager.class) {                 
				if(ProcessManager.instance == null) {                     
					ProcessManager.instance = new ProcessManager();                 
				}             
			}         
		}         
		return ProcessManager.instance;     
	}           
	
	/**      
	  * Activity add Method      
	  * @param activity      
	  */    
	public void addActivity(Activity activity) {         
		if(!isActivity(activity))             
			mActivityArr.add(activity);     
	}           
	
	
	/**      
	  * Activity delete Method      
	  * @param activity      
	  */    
	public void deleteActivity(Activity activity) {         
		if(isActivity(activity)) {             
			activity.finish();             
			mActivityArr.remove(activity);         
		}     
	}           
	
	/**      
	  * @param activity      
	  * @return      
	  */    
	public boolean isActivity(Activity activity) {         
		for(Activity chkActivity:mActivityArr) {             
			if(chkActivity == activity)                 
				return true;         
			}         
		return false;     
	}           
	
	/**      
	  * All activity finish()     
	  */    
	public void allEndActivity() {         
		for(Activity activity:mActivityArr) {             
			activity.finish();         
		}     
	} 
} 
		

