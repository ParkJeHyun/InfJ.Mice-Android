package kr.co.iekorea.mc.adapter;

import java.util.ArrayList;

import kr.co.iekorea.mc.R;
import kr.co.iekorea.mc.xml.BinderSessionListDto;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class BinderSessionListAdapter extends BaseAdapter {
	private Context ctx;
	private Activity activity;
	private LayoutInflater inflater;
	private ArrayList<BinderSessionListDto> items;
	private ViewHolderBinderSessionList viewHolder;
	
	public BinderSessionListAdapter(Context ctx, ArrayList<BinderSessionListDto> items){
		this.ctx = ctx;
		this.activity = activity;
		this.items = items;
		this.init();
	}
	
	public void init(){
		this.inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		return items.size();
	}

	@Override
	public Object getItem(int position) {
		return items.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if(convertView == null){
			convertView = inflater.inflate(R.layout.binder_session_title_item, null);
			viewHolder = new ViewHolderBinderSessionList();
			viewHolder.text_session_title = (TextView) convertView.findViewById(R.id.text_session_title);
			convertView.setTag(viewHolder);
		}else{
			viewHolder = (ViewHolderBinderSessionList) convertView.getTag();
		}
		
		viewHolder.text_session_title.setText(items.get(position).SESSION_TITLE);
		return convertView;
	}

}
