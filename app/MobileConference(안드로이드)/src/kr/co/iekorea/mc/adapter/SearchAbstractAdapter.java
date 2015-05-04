package kr.co.iekorea.mc.adapter;

import java.util.ArrayList;

import kr.co.iekorea.mc.R;
import kr.co.iekorea.mc.staticdata.StaticData;
import kr.co.iekorea.mc.xml.AbstractDto;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SearchAbstractAdapter extends BaseAdapter {
	public Context ctx;
	public ArrayList<AbstractDto> items;
	public LayoutInflater inflater;
	public ViewHolderAbstract viewHolder;
	
	public SearchAbstractAdapter(Context ctx, ArrayList<AbstractDto> items){
		this.ctx = ctx;
		this.items = items;
		init();
	}
	
	public void init(){
		inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
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
			viewHolder = new ViewHolderAbstract();
			convertView = inflater.inflate(R.layout.search_abstract_info_list_item, null);
			viewHolder.PRESENTER = (TextView) convertView.findViewById(R.id.text_presenter);
			viewHolder.layout_presenter = (LinearLayout) convertView.findViewById(R.id.layout_presenter);
			viewHolder.WRITER = (TextView) convertView.findViewById(R.id.text_writer);
			viewHolder.BINDER_TITLE = (TextView) convertView.findViewById(R.id.text_binder_title);
			viewHolder.TOPIC_TITLE = (TextView) convertView.findViewById(R.id.text_topic);
			
			viewHolder.info_subject = (TextView) convertView.findViewById(R.id.info_subject);
			viewHolder.info_title = (TextView) convertView.findViewById(R.id.info_title);
			viewHolder.text_author = (TextView) convertView.findViewById(R.id.text_author);
			viewHolder.text_speaker = (TextView) convertView.findViewById(R.id.text_speaker);
			
			convertView.setTag(viewHolder);
		}else{
			viewHolder = (ViewHolderAbstract) convertView.getTag();
		}
		if(items.get(position).PRESENTER == null){
			viewHolder.layout_presenter.setVisibility(LinearLayout.GONE);
		}else{
			viewHolder.layout_presenter.setVisibility(LinearLayout.VISIBLE);
			viewHolder.PRESENTER.setText(items.get(position).PRESENTER);
		}
		viewHolder.WRITER.setText(items.get(position).WRITER);
		viewHolder.BINDER_TITLE.setText(items.get(position).BINDER_TITLE);
		viewHolder.TOPIC_TITLE.setText(items.get(position).TOPIC_TITLE);
		

		// 언어
		switch (StaticData.NOWLANGUAGE) {
		case StaticData.KOREA:
			viewHolder.text_author.setText(ctx.getResources().getString(R.string.author_ko));
			viewHolder.info_subject.setText(ctx.getResources().getString(R.string.subject_ko));
			viewHolder.info_title.setText(ctx.getResources().getString(R.string.title_ko));
			viewHolder.text_speaker.setText(ctx.getResources().getString(R.string.speaker_ko));
			break;
		case StaticData.ENGLISH:
			viewHolder.text_author.setText(ctx.getResources().getString(R.string.author_en));
			viewHolder.info_subject.setText(ctx.getResources().getString(R.string.subject_en));
			viewHolder.info_title.setText(ctx.getResources().getString(R.string.title_en));
			viewHolder.text_speaker.setText(ctx.getResources().getString(R.string.speaker_en));
			break;
		case StaticData.CHINA:
			viewHolder.text_author.setText(ctx.getResources().getString(R.string.author_cn));
			viewHolder.info_subject.setText(ctx.getResources().getString(R.string.subject_cn));
			viewHolder.info_title.setText(ctx.getResources().getString(R.string.title_cn));
			viewHolder.text_speaker.setText(ctx.getResources().getString(R.string.speaker_cn));
			break;
		}
		
		return convertView;
	}
}
