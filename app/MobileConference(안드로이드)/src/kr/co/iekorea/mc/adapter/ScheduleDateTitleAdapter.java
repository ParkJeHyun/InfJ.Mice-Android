package kr.co.iekorea.mc.adapter;

import java.util.ArrayList;

import kr.co.iekorea.mc.xml.AgendaSessionListDto;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class ScheduleDateTitleAdapter extends BaseAdapter {
	public Context ctx;
	public ArrayList<AgendaSessionListDto> itemList;
	
	public ScheduleDateTitleAdapter(Context ctx, ArrayList<AgendaSessionListDto> itemList){
		this.ctx = ctx;
		this.itemList = itemList;
		
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return itemList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		return null;
	}

}
