package kr.co.iekorea.mc.adapter;

import java.util.ArrayList;

import kr.co.iekorea.mc.MyBriefcaseScheduleDetail_Activity;
import kr.co.iekorea.mc.R;
import kr.co.iekorea.mc.xml.ScheduleDateDto;
import kr.co.iekorea.mc.xml.ScheduleListDto;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.RelativeLayout;

public class ScheduleViewPagerAdapter extends PagerAdapter {
	private Context ctx;
	private ArrayList<ScheduleDateDto> items;
	private ArrayList<View> viewList;
	private ArrayList<ScheduleListDto> titleList;
	private ArrayList<ScheduleContentsAdapter> adapterList;
	private ArrayList<ListView> listViewList;
	private LayoutInflater layInf;
	private Activity activity;
	
	public ScheduleViewPagerAdapter(Context ctx, ArrayList<ScheduleDateDto> items,Activity activity){
		this.ctx = ctx;
		this.items = items;
		this.activity = activity;
		this.init();
	}
	
	public void init(){
		layInf = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		viewList = new ArrayList<View>();
		adapterList = new ArrayList<ScheduleContentsAdapter>();
		for(int i =0; i<items.size(); i++){
			ScheduleContentsAdapter adapter = 
					new ScheduleContentsAdapter(ctx,items.get(i).list,activity,this);
			adapterList.add(adapter);
		}
		Log.e("agenda view pager adapter", items.size()+" items size");
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return items.size();
	}
	
	@Override
	public boolean isViewFromObject(View pager, Object view) {
		Log.i("agenda view pager adapter", "isViewFromObject");
		return (pager == view);
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object view) {
		Log.i("agenda view pager adapter", "destroyItem");
		((ViewPager) container).removeView((View) view);
	}
	
	class mOnItemClickListener implements OnItemClickListener {
		private int position_p;
		public mOnItemClickListener(int position_p){
			this.position_p = position_p;
		}

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			Log.e("onclick", "p_position : "+position_p+", position : "+position);
			if(deletemode){
			}else{
				Intent intent = new Intent(ctx, MyBriefcaseScheduleDetail_Activity.class);
				intent.putExtra("schedule_id", items.get(position_p).list.get(position).SCHEDUEL_ID);
				ctx.startActivity(intent);
				activity.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
				activity.finish();				
			}
		}
	}
	
	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		Log.i("agenda view pager adapter", "instantiateItem");
		Log.i("agenda view pager adapter", "position : "+position);
		View view = (RelativeLayout) layInf.inflate(R.layout.agenda_session_list_layout, null);
		ListView list = (ListView) view.findViewById(R.id.session_list);
		ArrayList<String> arGeneral = new ArrayList<String>();
		for(int i=0; i<items.get(position).list.size(); i++){
			arGeneral.add(items.get(position).list.get(i).TITLE);
		}
		
		// 어댑터 준비
//		ScheduleContentsAdapter adapter = new ScheduleContentsAdapter(ctx,items.get(position).list,activity);
		
		list.setAdapter(adapterList.get(position));
		list.setOnItemClickListener(new mOnItemClickListener(position));
		
		((ViewPager) container).addView(view, 0);
		return view;
	}
	
	public boolean deletemode = false;
	public void changeDeleteMode(){
		if(deletemode){
			deletemode = false;
		}else{
			deletemode = true;
		}
		for(int i=0; i<items.size();i++){
			adapterList.get(i).changeDeleteMode();
		}
	}
	
	@Override
	public void notifyDataSetChanged() {
		
		super.notifyDataSetChanged();
	}
}
