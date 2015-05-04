package kr.co.iekorea.mc.adapter;

import java.util.ArrayList;

import kr.co.iekorea.mc.R;
import kr.co.iekorea.mc.xml.RESEARCH_QUESTION_DTO;
import kr.co.iekorea.mc.xml.ScheduleListDto;
import android.app.Activity;
import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class SurveyResultViewPagerAdapter extends PagerAdapter {
	private Context ctx;
	private ArrayList<RESEARCH_QUESTION_DTO> items;
	private ArrayList<View> viewList;
	private ArrayList<ScheduleListDto> titleList;
	private ArrayList<SurveyResultListAdapter> adapterList;
	private ArrayList<ListView> listViewList;
	private LayoutInflater layInf;
	private Activity activity;
	
	public SurveyResultViewPagerAdapter(Context ctx, ArrayList<RESEARCH_QUESTION_DTO> items,Activity activity){
		this.ctx = ctx;
		this.items = items;
		this.activity = activity;
		this.init();
	}
	
	public void init(){
		layInf = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		viewList = new ArrayList<View>();
		adapterList = new ArrayList<SurveyResultListAdapter>();
		for(int i =0; i<items.size(); i++){
			View view = (LinearLayout) layInf.inflate(R.layout.survey_question_viewpager, null);
			// RESEARCH_ANSWER
			ListView RESEARCH_ANSWER = (ListView) view.findViewById(R.id.RESEARCH_ANSWER);
			// QUESTION_TITLE
			TextView QUESTION_TITLE = (TextView) view.findViewById(R.id.QUESTION_TITLE);
			// QUESTION_NUM
			TextView QUESTION_NUM = (TextView) view.findViewById(R.id.QUESTION_NUM);
			
			QUESTION_NUM.setText(items.get(i).QUESTION_NUM+"");
			QUESTION_TITLE.setText(items.get(i).QUESTION_TITLE);
			
			SurveyResultListAdapter adapter =new SurveyResultListAdapter(ctx,items.get(i).RESEARCH_ANSWER);
			RESEARCH_ANSWER.setAdapter(adapter);
			
			viewList.add(view);
			adapterList.add(adapter);
			
		}
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
	
//	class mOnItemClickListener implements OnItemClickListener {
//		private int position_p;
//		public mOnItemClickListener(int position_p){
//			this.position_p = position_p;
//		}
//
//		@Override
//		public void onItemClick(AdapterView<?> parent, View view, int position,
//				long id) {
//			Log.e("onclick", "p_position : "+position_p+", position : "+position);
//			if(deletemode){
//			}else{
//				Intent intent = new Intent(ctx, MyBriefcaseScheduleDetail_Activity.class);
//				intent.putExtra("schedule_id", items.get(position_p).list.get(position).SCHEDUEL_ID);
//				ctx.startActivity(intent);
//				activity.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
//				activity.finish();				
//			}
//		}
//	}
	
	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		Log.i("agenda view pager adapter", "instantiateItem");
		Log.i("agenda view pager adapter", "position : "+position);
		((ViewPager) container).addView(viewList.get(position), 0);
		return viewList.get(position);
	}
	
//	layInf = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//	View view = (LinearLayout) layInf.inflate(R.layout.survey_question_viewpager, null);
//	// RESEARCH_ANSWER
//	ListView RESEARCH_ANSWER = (ListView) view.findViewById(R.id.RESEARCH_ANSWER);
//	// QUESTION_TITLE
//	TextView QUESTION_TITLE = (TextView) view.findViewById(R.id.QUESTION_TITLE);
//	// QUESTION_NUM
//	TextView QUESTION_NUM = (TextView) view.findViewById(R.id.QUESTION_NUM);
//	
//	QUESTION_NUM.setText(items.get(position).QUESTION_NUM+"");
//	QUESTION_TITLE.setText(items.get(position).QUESTION_TITLE);
//	
////	SurveyResultListAdapter adapter =new SurveyResultListAdapter(ctx,items.get(position).RESEARCH_ANSWER);
//	RESEARCH_ANSWER.setAdapter(adapterList.get(position));
//		
//	((ViewPager) container).addView(view, 0);
//	return view;
	
//	public boolean deletemode = false;
//	public void changeDeleteMode(){
//		if(deletemode){
//			deletemode = false;
//		}else{
//			deletemode = true;
//		}
//		for(int i=0; i<items.size();i++){
//			adapterList.get(i).changeDeleteMode();
//		}
//	}
//	
//	@Override
//	public void notifyDataSetChanged() {
//		
//		super.notifyDataSetChanged();
//	}
}
