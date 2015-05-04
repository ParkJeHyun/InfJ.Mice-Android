package kr.co.iekorea.mc.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class getAndSetDate {
	
	/**
	 * @param int movedate : 움직인 날짜 입력 ( 예 : 1 -> 내일, -1 -> 어제)
	 * @param Sring formatter : format 형식 ( 예 : yyyy-MM-dd ) 
	 *  */
	public String setterDate(int movedate, String formatter){
		Date today = new Date();
		Date selDate = new Date();
		SimpleDateFormat simple = new SimpleDateFormat(formatter);
		selDate.setTime(today.getTime() + (1000 * 60 * 60 * 24) * movedate);
		return simple.format(selDate);
	}
}
