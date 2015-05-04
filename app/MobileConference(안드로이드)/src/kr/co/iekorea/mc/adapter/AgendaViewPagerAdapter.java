package kr.co.iekorea.mc.adapter;

import java.util.ArrayList;

import kr.co.iekorea.mc.Agenda_Time_Activity;
import kr.co.iekorea.mc.R;
import kr.co.iekorea.mc.xml.AgendaSessionDto;
import kr.co.iekorea.mc.xml.AgendaSessionListDto;
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
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;

public class AgendaViewPagerAdapter extends PagerAdapter {
	private Context ctx;
	private ArrayList<AgendaSessionDto> items;
	private ArrayList<View> viewList;
	private ArrayList<AgendaSessionListDto> titleList;
	private ArrayList<AgendaSessionListTitleAdapter> adapterList;
	private ArrayList<ListView> listViewList;
	private LayoutInflater layInf;
	private Activity activity;
	
	public AgendaViewPagerAdapter(Context ctx, ArrayList<AgendaSessionDto> items,Activity activity){
		this.ctx = ctx;
		this.items = items;
		this.activity = activity;
		this.init();
	}
	
	public void init(){
		layInf = (LayoutInflater) ctx
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		viewList = new ArrayList<View>();
		Log.e("agenda view pager adapter", items.size()+" items size");
	}
	
	public void setAdapterListener(){
		for(int i =0; i < viewList.size(); i++){
			listViewList.get(i).setAdapter(adapterList.get(i));
		}
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return items.size();
	}
	
	@Override
	public boolean isViewFromObject(View pager, Object view) {

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
			Intent intent = new Intent(ctx, Agenda_Time_Activity.class);
			intent.putExtra("session_id", items.get(position_p).list.get(position).SESSION_ID);
			ctx.startActivity(intent);
			activity.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
		}
	}
	
	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		Log.i("agenda view pager adapter", "instantiateItem");
		View view = (RelativeLayout) layInf.inflate(R.layout.agenda_session_list_layout, null);
		ListView list = (ListView) view.findViewById(R.id.session_list);
		ArrayList<String> arGeneral = new ArrayList<String>();
		for(int i=0; i<items.get(position).list.size(); i++){
			arGeneral.add(items.get(position).list.get(i).SESSION_TITLE);
		}
		
		// 어댑터 준비
		ArrayAdapter<String> Adapter;
		
		Adapter = new ArrayAdapter<String>(ctx, R.layout.agenda_session_title_item, arGeneral);

		list.setAdapter(Adapter);
		list.setOnItemClickListener(new mOnItemClickListener(position));
		
		((ViewPager) container).addView(view, 0);
		return view;
	}
}
