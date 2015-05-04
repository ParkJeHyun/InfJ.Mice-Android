package kr.co.iekorea.mc.adapter;

import java.util.ArrayList;

import kr.co.iekorea.mc.R;
import kr.co.iekorea.mc.staticdata.StaticData;
import kr.co.iekorea.mc.xml.BinderDetailDto;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class BinderSessionTimeAdapter extends BaseAdapter {
	public Context ctx;
	public ArrayList<BinderDetailDto> itemList;
	public LayoutInflater inflater;
	private ViewHolderBinderDetailList viewHolder;
	
	public BinderSessionTimeAdapter(Context ctx, ArrayList<BinderDetailDto> itemList){
		this.ctx = ctx;
		this.itemList = itemList;
		this.init();
	}
	
	public void init(){
		inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return itemList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return itemList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if(convertView == null){
			convertView = inflater.inflate(R.layout.binder_detail_list_item,null);
			viewHolder = new ViewHolderBinderDetailList();
			viewHolder.BINDER_TITLE = (TextView) convertView.findViewById(R.id.text_title);
			viewHolder.USER_NAME = (TextView) convertView.findViewById(R.id.text_user_name);
			viewHolder.WRITER = (TextView) convertView.findViewById(R.id.text_writer);
			// 언어
			viewHolder.text_author = (TextView) convertView.findViewById(R.id.text_author);
			viewHolder.info_title = (TextView) convertView.findViewById(R.id.info_title);
			viewHolder.info_user = (TextView) convertView.findViewById(R.id.info_user);
			convertView.setTag(viewHolder);
		}else{
			viewHolder = (ViewHolderBinderDetailList) convertView.getTag();
		}
		
		BinderDetailDto dto = itemList.get(position);
		viewHolder.BINDER_TITLE.setText(dto.BINDER_TITLE);
		viewHolder.USER_NAME.setText(dto.USER_NAME);
		viewHolder.WRITER.setText(dto.WRITER);
		
		switch (StaticData.NOWLANGUAGE) {
		case StaticData.KOREA:
			viewHolder.text_author.setText(ctx.getResources().getString(R.string.author_ko));
			viewHolder.info_title.setText(ctx.getResources().getString(R.string.title_ko));
			viewHolder.info_user.setText(ctx.getResources().getString(R.string.user_ko));
			break;
		case StaticData.ENGLISH:
			viewHolder.text_author.setText(ctx.getResources().getString(R.string.author_en));
			viewHolder.info_title.setText(ctx.getResources().getString(R.string.title_en));
			viewHolder.info_user.setText(ctx.getResources().getString(R.string.user_en));
			break;
		case StaticData.CHINA:
			viewHolder.text_author.setText(ctx.getResources().getString(R.string.author_cn));
			viewHolder.info_title.setText(ctx.getResources().getString(R.string.title_cn));
			viewHolder.info_user.setText(ctx.getResources().getString(R.string.user_cn));
			break;
		}
		
		
		// TODO Auto-generated method stub
		return convertView;
	}

}
