package kr.co.iekorea.mc.util;

import android.util.Log;

public class CustomLog {
	
	public static boolean DEV_MODE = true;

	public static void d(String tag, String text) {
		if(DEV_MODE) {
			Log.d(tag, text);
		}
	}
	
	public static void w(String tag, String text) {
		if(DEV_MODE) {
			Log.d(tag, text);
		}
	}
	
	public static void i(String tag, String text) {
		if(DEV_MODE) {
			Log.d(tag, text);
		}
	}
	
	public static void e(String tag, String text) {
		if(DEV_MODE) {
			Log.e(tag, text);
		}
	}
	
}
