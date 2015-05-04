package kr.co.iekorea.mc.adapter;

import java.util.ArrayList;

import kr.co.iekorea.mc.R;
import kr.co.iekorea.mc.staticdata.StaticData;
import kr.co.iekorea.mc.xml.BinderDataSearchDto;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class BinderDataSearchAdpater extends BaseAdapter {
	public Context ctx;
	public ArrayList<BinderDataSearchDto> itemList;
	public LayoutInflater inflater;
	private BinderDataSearchViewHolder viewHolder;
	
	public BinderDataSearchAdpater(Context ctx, ArrayList<BinderDataSearchDto> itemList){
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
			convertView = inflater.inflate(R.layout.binder_data_search_list_item,null);
			viewHolder = new BinderDataSearchViewHolder();
			viewHolder.title = (TextView) convertView.findViewById(R.id.text_title);
			viewHolder.writer = (TextView) convertView.findViewById(R.id.text_writer);
			viewHolder.sex = (TextView) convertView.findViewById(R.id.text_sex);
			viewHolder.company = (TextView) convertView.findViewById(R.id.text_company);
			viewHolder.nation = (TextView) convertView.findViewById(R.id.text_nation);
			
			viewHolder.text_author = (TextView) convertView.findViewById(R.id.text_author);
			viewHolder.info_sex = (TextView) convertView.findViewById(R.id.info_sex);
			viewHolder.info_company = (TextView) convertView.findViewById(R.id.info_company);
			viewHolder.info_nation = (TextView) convertView.findViewById(R.id.info_nation);
			
			convertView.setTag(viewHolder);
		}else{
			viewHolder = (BinderDataSearchViewHolder) convertView.getTag();
		}
		
		BinderDataSearchDto dto = itemList.get(position);
		viewHolder.title.setText(dto.BINDER_TITLE);
		viewHolder.writer.setText(dto.WRITER);
		viewHolder.sex.setText(dto.SEX);
		viewHolder.company.setText(dto.COMPANY);
		viewHolder.nation.setText(dto.NATION);
		
		// 언어
		switch (StaticData.NOWLANGUAGE) {
		case StaticData.KOREA:
			viewHolder.text_author.setText(ctx.getResources().getString(R.string.author_ko));
			viewHolder.info_sex.setText(ctx.getResources().getString(R.string.sex_ko));
			viewHolder.info_company.setText(ctx.getResources().getString(R.string.company_ko));
			viewHolder.info_nation.setText(ctx.getResources().getString(R.string.nation_ko));
			break;
		case StaticData.ENGLISH:
			viewHolder.text_author.setText(ctx.getResources().getString(R.string.author_en));
			viewHolder.info_sex.setText(ctx.getResources().getString(R.string.sex_en));
			viewHolder.info_company.setText(ctx.getResources().getString(R.string.company_en));
			viewHolder.info_nation.setText(ctx.getResources().getString(R.string.nation_en));
			break;
		case StaticData.CHINA:
			viewHolder.text_author.setText(ctx.getResources().getString(R.string.author_cn));
			viewHolder.info_sex.setText(ctx.getResources().getString(R.string.sex_cn));
			viewHolder.info_company.setText(ctx.getResources().getString(R.string.company_cn));
			viewHolder.info_nation.setText(ctx.getResources().getString(R.string.nation_cn));
			break;
		}
		
		// TODO Auto-generated method stub
		return convertView;
	}

}
