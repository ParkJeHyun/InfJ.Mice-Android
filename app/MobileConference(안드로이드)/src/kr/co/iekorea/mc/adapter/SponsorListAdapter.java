package kr.co.iekorea.mc.adapter;

import java.util.ArrayList;

import kr.co.iekorea.mc.R;
import kr.co.iekorea.mc.SponsorView_Activity;
import kr.co.iekorea.mc.staticdata.StaticData;
import kr.co.iekorea.mc.util.ImageDownloader;
import kr.co.iekorea.mc.xml.SponsorDto;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

public class SponsorListAdapter extends BaseAdapter{
	private Context ctx;
	private Activity activity;
	private ArrayList<SponsorDto> items;
	private LayoutInflater inflater;
	private BinderSponsorViewHolder viewHolder;
	
	private ImageDownloader downLoader;
	
	public SponsorListAdapter(Context ctx, ArrayList<SponsorDto> items, Activity activity){
		this.ctx = ctx;
		this.items = items;
		this.activity = activity;
		this.init();
	}
	
	public void init(){
		inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		downLoader = new ImageDownloader();
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
			viewHolder = new BinderSponsorViewHolder();
			convertView = (RelativeLayout) inflater.inflate(R.layout.sponsor_items, null);
			viewHolder.image_sponsor = (ImageView) convertView.findViewById(R.id.image_sponsor);
			viewHolder.progress = (ProgressBar) convertView.findViewById(R.id.progress);
			convertView.setTag(viewHolder);
		}else{
			viewHolder = (BinderSponsorViewHolder) convertView.getTag();
		}
		
//		viewHolder.image_sponsor.setImageResource(R.drawable.test_sponsor);
		try{
			ImageDownloader.download(StaticData.DEFAULT_URL+items.get(position).BANNER_IMAGE, viewHolder.image_sponsor);
		}catch(RuntimeException e){}
		
		viewHolder.image_sponsor.setOnClickListener(new itemClickListener(position));
		return convertView;
	}
	
	class itemClickListener implements OnClickListener{
		private int position;
		public itemClickListener(int position){
			this.position = position;
		}

		@Override
		public void onClick(View v) {
			Intent intent = new Intent(ctx, SponsorView_Activity.class);
			intent.putExtra("detail_url", items.get(position).DETAIL_IMAGE);
			ctx.startActivity(intent);
			activity.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
		}
	}
	
//	public static Bitmap loadImage(String imageURL) throws URISyntaxException,
//			ClientProtocolException, IOException {
//		Bitmap image = null;
//
//		if (!imageURL.equals("")) {
//			InputStream is = null;
//			HttpGet httpRequest = null;
//
//			try {
//				httpRequest = new HttpGet(imageURL);
//				HttpParams httpParams = new BasicHttpParams();
//				int timeoutConnection = 5000;
//				HttpConnectionParams.setConnectionTimeout(httpParams,
//						timeoutConnection);
//				int timeoutSocket = 5000;
//				HttpConnectionParams.setSoTimeout(httpParams, timeoutSocket);
//				HttpClient httpClient = new DefaultHttpClient(httpParams);
//				HttpResponse response = (HttpResponse) httpClient
//						.execute(httpRequest);
//
//				HttpEntity entity = response.getEntity();
//				BufferedHttpEntity bufferedHttpEntity = new BufferedHttpEntity(
//						entity);
//
//				is = bufferedHttpEntity.getContent();
//
//				image = BitmapFactory.decodeStream(is);
//
//			} catch (Throwable e) {
//				Log.e("timeout", "Timeout on getting images.", null);
//				image = null;
//			} finally {
//				if (is != null) {
//					try {
//						is.close();
//					} catch (IOException e) {
//						Log.e("socket", "Socket Exception", e);
//					}
//				}
//
//			}
//		}
//		return image;
//	}
}
