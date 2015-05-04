package kr.co.iekorea.mc.xml;

import java.util.ArrayList;

public class BinderSessionDto {
	
	public String date;																		// session date
	public ArrayList<BinderSessionListDto> list;	// session list of that date
	
	public BinderSessionDto(){
		list = new ArrayList<BinderSessionListDto>();
	}
}
