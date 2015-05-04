package kr.co.iekorea.mc.adapter;

import java.util.ArrayList;

import kr.co.iekorea.mc.R;
import kr.co.iekorea.mc.xml.ScheduleDateDto;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ScheduleGalleryAdapter extends BaseAdapter {
	public Context ctx;
	public ArrayList<ScheduleDateDto> scheduleDateList;
	public LayoutInflater inflater;
	public int startDateYear;
	public int startDateMonth;
	public int startDateDay;
	public AgendaGalleryViewHolder viewHolder;
	
	public ScheduleGalleryAdapter(Context ctx,ArrayList<ScheduleDateDto> scheduleDateList){
		this.ctx = ctx;
		this.scheduleDateList = scheduleDateList;
		init();
	}
	
	public void init(){
		inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		Log.e("AgendaCalleryAdapter", "agendaCountList.size() :"+this.scheduleDateList.size());
//		this.setSeparateDate();
//		this.getSDate();
	}
	
//	public void setSeparateDate(){
//		String Date = dto.startDate;
//		String[] separaterDate = Date.split("-");
//		for(int i =0; i<separaterDate.length; i++){
//			System.out.println(separaterDate[i]);
//		}
//		this.startDateYear = Integer.parseInt(separaterDate[0]);
//		this.startDateMonth = Integer.parseInt(separaterDate[1]);
//		this.startDateDay = Integer.parseInt(separaterDate[2]);
//	}
	
	
	// 특정 날짜 구하기
//	public void getSDate(){
//		Calendar cal= Calendar.getInstance ();
//		
//		cal.set(Calendar.YEAR, startDateYear);
//	    cal.set(Calendar.MONTH, startDateMonth-1);
//	    cal.set(Calendar.DATE, startDateDay);
//	    
//	    
//	    for(int i= 0; i< dto.TotlaAgendaCount; i++){
//	    	StringBuffer sbDate=new StringBuffer ( );
//	    	int n = 0;
//	    	if(i>=1){
//	    		n=1;
//	    	}
//	    	cal.add(Calendar.DAY_OF_MONTH, n);
//	    	
//	    	int nYear = cal.get ( Calendar.YEAR );
//	    	int nMonth = cal.get ( Calendar.MONTH ) + 1;
//	    	int nDay = cal.get ( Calendar.DAY_OF_MONTH );
//
//	    	sbDate.append ( nYear );
//	    	sbDate.append ( "/" );
//	    	if ( nMonth < 10 )
//	    	sbDate.append ( "0" );
//	    	sbDate.append ( nMonth );
//	    	sbDate.append ( "/" );
//	    	if ( nDay < 10 )
//	    	sbDate.append ( "0" );
//	    	sbDate.append ( nDay );
//	    
//	    	dateList.add(sbDate.toString());
//	    }
//	}
	// 특정 날짜로 부터 다음날 구하기

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return scheduleDateList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return scheduleDateList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if(convertView == null){
			viewHolder = new AgendaGalleryViewHolder();
			convertView = inflater.inflate(R.layout.agenda_gallery_adapter_item, null);
			viewHolder.text = (TextView) convertView.findViewById(R.id.text_date_item);
			convertView.setTag(viewHolder);
		}else{
			viewHolder = (AgendaGalleryViewHolder) convertView.getTag();
		}
		
		viewHolder.text.setText(scheduleDateList.get(position).date);
		
		return convertView;
	}
}
