package kr.co.iekorea.mc.xml;

import java.util.ArrayList;

public class AgendaSessionDto {
	
	public String date;																		// session date
	public ArrayList<AgendaSessionListDto> list;	// session list of that date
	
	public AgendaSessionDto(){
		list = new ArrayList<AgendaSessionListDto>();
	}
}
