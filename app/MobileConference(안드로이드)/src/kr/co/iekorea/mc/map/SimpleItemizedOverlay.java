/***
 * Copyright (c) 2010 readyState Software Ltd
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may
 * not use this file except in compliance with the License. You may obtain
 * a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 */

package kr.co.iekorea.mc.map;

import java.util.ArrayList;

import kr.co.iekorea.mc.SearchStayDetail_Activity;
import kr.co.iekorea.mc.xml.StayDto;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.widget.Toast;

import com.google.android.maps.MapView;
import com.google.android.maps.OverlayItem;

public class SimpleItemizedOverlay extends BalloonItemizedOverlay<OverlayItem> {

	private ArrayList<OverlayItem> m_overlays = new ArrayList<OverlayItem>();
	private ArrayList<StayDto> stayList = new ArrayList<StayDto>();
	private Context c;
	private Activity activity;
	
	public SimpleItemizedOverlay(Drawable defaultMarker, MapView mapView, Activity activity) {
		super(boundCenter(defaultMarker), mapView);
		c = mapView.getContext();
		this.activity = activity;
	}

	public void addOverlay(OverlayItem overlay) {
	    m_overlays.add(overlay);
	    populate();
	}

	@Override
	protected OverlayItem createItem(int i) {
		return m_overlays.get(i);
	}

	@Override
	public int size() {
		return m_overlays.size();
	}

	@Override
	protected boolean onBalloonTap(int index, OverlayItem item) {
		// new intent start in here
		Intent intent = new Intent(c, SearchStayDetail_Activity.class);
		intent.putExtra("contents",stayList.get(index).Contents.toString());
		intent.putExtra("phone",stayList.get(index).PHONE.toString());
		intent.putExtra("address",stayList.get(index).ADDRESS.toString());
		intent.putExtra("title",stayList.get(index).title.toString());
		intent.putExtra("state",stayList.get(index).state.toString());
		
		c.startActivity(intent);
		activity.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
//		Toast.makeText(c, "해당 상세 내용 : " + stayList.get(index).Contents,Toast.LENGTH_SHORT).show();
		return true;
	}

	public void addStateItem(StayDto temp) {
		// TODO Auto-generated method stub
		stayList.add(temp);
	}
	
}
