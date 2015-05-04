package kr.co.iekorea.mc.xml;

import java.util.ArrayList;

public class AppointmentsDto {
	public int PROMISE_ID;				// 약속 아이디
	public String FROM_USER_NAME;
	public int FROM_USER_CD;
	public ArrayList<TOUserDTO> toUserList = new ArrayList<TOUserDTO>();
	public String CANCLE_STAT;
	public String PROMISE_DATE;
	public String PROMISE_HOUR;
	public String TITLE;
	public String RECEIPT;
	public String REG_DATE;
	
}
