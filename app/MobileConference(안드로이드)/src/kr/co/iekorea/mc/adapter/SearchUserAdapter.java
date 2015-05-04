package kr.co.iekorea.mc.adapter;

import java.util.ArrayList;

import kr.co.iekorea.mc.R;
import kr.co.iekorea.mc.staticdata.StaticData;
import kr.co.iekorea.mc.xml.SearchUserItemDto;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class SearchUserAdapter extends BaseAdapter {
	private Context ctx;
	private ArrayList<SearchUserItemDto> items;
	private LayoutInflater inflater;
	private ViewHolder_SearchUserInfo viewHolder;
	
	public SearchUserAdapter(Context ctx, ArrayList<SearchUserItemDto> items){
		this.ctx = ctx;
		this.items = items;
		init();
	}
	
	public void init(){
		inflater =(LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		return items.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return items.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if(convertView == null){
			viewHolder = new ViewHolder_SearchUserInfo();
			convertView = inflater.inflate(R.layout.search_user_info_list_item, null);
			viewHolder.USER_NAME = (TextView) convertView.findViewById(R.id.text_user);
			viewHolder.COMPANY = (TextView) convertView.findViewById(R.id.text_company);
			viewHolder.NATION = (TextView) convertView.findViewById(R.id.text_nation);
			
			viewHolder.info_participant = (TextView) convertView.findViewById(R.id.info_participant);
			viewHolder.info_company = (TextView) convertView.findViewById(R.id.info_company);
			viewHolder.info_nation = (TextView) convertView.findViewById(R.id.info_nation);
			
			convertView.setTag(viewHolder);
		}else{
			viewHolder = (ViewHolder_SearchUserInfo) convertView.getTag();
		}
		viewHolder.USER_NAME.setText(items.get(position).USER_NAME);
		viewHolder.COMPANY.setText(items.get(position).COMPANY);
		viewHolder.NATION.setText(items.get(position).NATIONAL_NAME);
		
		// 언어
		switch (StaticData.NOWLANGUAGE) {
		case StaticData.KOREA:
			viewHolder.info_participant.setText(ctx.getResources().getString(R.string.attendant_ko));
			viewHolder.info_company.setText(ctx.getResources().getString(R.string.sex_ko));
			viewHolder.info_nation.setText(ctx.getResources().getString(R.string.nation_ko));
			break;
		case StaticData.ENGLISH:
			viewHolder.info_participant.setText(ctx.getResources().getString(R.string.attendant_en));
			viewHolder.info_company.setText(ctx.getResources().getString(R.string.company_en));
			viewHolder.info_nation.setText(ctx.getResources().getString(R.string.nation_en));
			break;
		case StaticData.CHINA:
			viewHolder.info_participant.setText(ctx.getResources().getString(R.string.attendant_cn));
			viewHolder.info_company.setText(ctx.getResources().getString(R.string.company_cn));
			viewHolder.info_nation.setText(ctx.getResources().getString(R.string.nation_cn));
			break;
		}
		
		return convertView;
	}

}
