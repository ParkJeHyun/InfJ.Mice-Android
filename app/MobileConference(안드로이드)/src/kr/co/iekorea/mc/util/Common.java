package kr.co.iekorea.mc.util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;

public class Common {
	/* 접속 URL*/
	static final float DEFAULT_HDIP_DENSITY_SCALE = 1.5f;
	
	public static final int DEFAULT_IMAGE_WIDTH = 640;
	public static final int DEFAULT_IMAGE_HEIGHT = 96;
	
	public static final int SERVER_TIMEOUT = 10000;
	
	/**
 	 * Null check
 	 */
	public static String nullChk(String inval) 
	{
        String  resultStr    = "";
        if (inval == null){
            resultStr    = "";
        }else if (inval.equals("null")){
            resultStr    = "";
        }else{
            resultStr    = inval;
        }
        return resultStr;
    }
	
	public static Typeface getFont(Context context){
		Typeface face = Typeface.createFromAsset(context.getAssets(), "fonts/DROIDSANS.TTF");
		return face;
	}
	
	public static int DPFromPixel(Context context, int pixel)  {    
		float scale = context.getResources().getDisplayMetrics().density;        
		return (int)(pixel / DEFAULT_HDIP_DENSITY_SCALE * scale);  
	}
	
	public static int PixelFromDP(Context context, int dp)  {    
		float scale = context.getResources().getDisplayMetrics().density;        
		return (int)(dp * scale);  
	}
	
	/**
	 * 항목 미 입력시 , Alert_Message
	 */
    public static void Alert_Message(Context context, String msg)
    {
    	AlertDialog.Builder alert = new AlertDialog.Builder(context);
		alert.setMessage(msg);
		alert.setPositiveButton("닫기", new DialogInterface.OnClickListener() {
//			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		alert.show();
    }
}
