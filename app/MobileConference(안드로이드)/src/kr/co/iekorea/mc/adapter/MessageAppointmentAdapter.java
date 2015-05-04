package kr.co.iekorea.mc.adapter;

import java.util.ArrayList;

import kr.co.iekorea.mc.R;
import kr.co.iekorea.mc.staticdata.StaticData;
import kr.co.iekorea.mc.util.WeakAsyncTask;
import kr.co.iekorea.mc.xml.AppointmentsDto;
import kr.co.iekorea.mc.xml.MessageDAO;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MessageAppointmentAdapter extends BaseAdapter {
	
	private Context ctx;
	private ArrayList<AppointmentsDto> items;
	private LayoutInflater inflater;
	private ViewHolderAppointments viewHolder;
	public  Activity activity;
	
	public boolean deleteMode = false;
	
	public MessageAppointmentAdapter(Context ctx, ArrayList<AppointmentsDto> items, Activity activity){
		this.ctx = ctx;
		this.items = items;
		this.activity = activity;
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
			viewHolder = new ViewHolderAppointments();
			convertView = inflater.inflate(R.layout.appointment_items, null);
			viewHolder.name = (TextView) convertView.findViewById(R.id.text_name);
			viewHolder.DATE = (TextView) convertView.findViewById(R.id.text_date);
			viewHolder.APOINTMENTTITLE = (TextView) convertView.findViewById(R.id.text_title);
			viewHolder.BTN_DELETE = (ImageView) convertView.findViewById(R.id.btn_delete);
			viewHolder.lisn_cancel = (View) convertView.findViewById(R.id.lisn_cancel);
			viewHolder.info_date = (TextView) convertView.findViewById(R.id.info_date);
			viewHolder.info_title = (TextView) convertView.findViewById(R.id.info_title);
			viewHolder.info_name = (TextView) convertView.findViewById(R.id.info_name);
			convertView.setTag(viewHolder);
		}else{
			viewHolder = (ViewHolderAppointments) convertView.getTag();
		}
		
		StringBuffer temp = new StringBuffer();
		for(int i =0; i<items.get(position).toUserList.size(); i++){
			temp.append(items.get(position).toUserList.get(i).TO_USER_NAME);
			if(i !=items.get(position).toUserList.size()-1)
			temp.append(",");
		}
		
		viewHolder.name.setText(temp.toString());
		viewHolder.DATE.setText(items.get(position).PROMISE_DATE+" "+items.get(position).PROMISE_HOUR);
		viewHolder.APOINTMENTTITLE.setText(items.get(position).TITLE);
		
		  // 언어 설정에 다른 화면 구성
		switch (StaticData.NOWLANGUAGE) {
		case StaticData.KOREA:
			viewHolder.BTN_DELETE.setImageResource(R.drawable.btn_delete_kr);
			viewHolder.info_date.setText(ctx.getResources().getString(R.string.date_ko));
			viewHolder.info_title.setText(ctx.getResources().getString(R.string.title_ko));
			viewHolder.info_name.setText(ctx.getResources().getString(R.string.name_ko));
			break;
		case StaticData.ENGLISH:
			viewHolder.BTN_DELETE.setImageResource(R.drawable.btn_delete_en);
			viewHolder.info_date.setText(ctx.getResources().getString(R.string.date_en));
			viewHolder.info_title.setText(ctx.getResources().getString(R.string.title_en));
			viewHolder.info_name.setText(ctx.getResources().getString(R.string.name_en));
			break;
		case StaticData.CHINA:
			viewHolder.BTN_DELETE.setImageResource(R.drawable.btn_delete_cn);
			viewHolder.info_date.setText(ctx.getResources().getString(R.string.date_cn));
			viewHolder.info_title.setText(ctx.getResources().getString(R.string.title_cn));
			viewHolder.info_name.setText(ctx.getResources().getString(R.string.name_cn));
			break;
		}
		
		if(deleteMode){
			viewHolder.BTN_DELETE.setVisibility(ImageView.VISIBLE);
			viewHolder.BTN_DELETE.setOnClickListener(new DeleteListener(position));
		}else{
			viewHolder.BTN_DELETE.setVisibility(ImageView.GONE);
		}
		
		if(items.get(position).CANCLE_STAT.equals("y")){
			viewHolder.lisn_cancel.setVisibility(View.VISIBLE);
		}else{
			viewHolder.lisn_cancel.setVisibility(View.GONE);
		}
		
		return convertView;
	}
	
	public void changeDeleteMode(){
		if(deleteMode){
			deleteMode = false;
		}else{
			deleteMode = true;
		}
		notifyDataSetChanged();
	}
	
	class DeleteListener implements OnClickListener{
		public int position;
		public DeleteListener(int position){
			this.position = position;
		}

		@Override
		public void onClick(View v) {
			String alertTile = "";
			String buttonMessage = "";
			String buttonYes = "";
			String buttonNo = "";

			switch (StaticData.NOWLANGUAGE) {
			case StaticData.KOREA:
				alertTile = ctx.getResources().getString(R.string.check_delete_ko);
				buttonMessage = ctx.getResources().getString(R.string.delete_ko);
				buttonYes = ctx.getResources().getString(R.string.yes_ko);
				buttonNo = ctx.getResources().getString(R.string.no_ko);
				break;
			case StaticData.ENGLISH:
				alertTile = ctx.getResources().getString(R.string.check_delete_en);
				buttonMessage = ctx.getResources().getString(R.string.delete_en);
				buttonYes = ctx.getResources().getString(R.string.yes_en);
				buttonNo = ctx.getResources().getString(R.string.no_en);
				break;
			case StaticData.CHINA:
				alertTile = ctx.getResources().getString(R.string.check_delete_cn);
				buttonMessage = ctx.getResources().getString(R.string.delete_cn);
				buttonYes = ctx.getResources().getString(R.string.yes_cn);
				buttonNo = ctx.getResources().getString(R.string.no_cn);
				break;
			}
			
			
			new AlertDialog.Builder(ctx)
			.setTitle(alertTile)
			.setMessage(buttonMessage)
			.setPositiveButton(buttonYes, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					deleteSelectItem(position);
					dialog.dismiss();
				}
			})
			.setNegativeButton(buttonNo, null)
			.show();
		}
	}
	
	
	public int nowDeletePosition;
	public void deleteSelectItem(int position){
		nowDeletePosition = position;
		new deleteItem(activity).execute();
	}
	
	class deleteItem extends WeakAsyncTask<Void, Void, Void,Activity>{
		public deleteItem(Activity target) {
			super(target);
			// TODO Auto-generated constructor stub
		}

		boolean result;
		
		@Override
		protected void onPreExecute(Activity target) {
		}

		@Override
		protected Void doInBackground(Activity target,Void... params) {
			MessageDAO dao = new MessageDAO(activity);
			try {
				result = dao.setDeletePromise(items.get(nowDeletePosition).PROMISE_ID);
			} catch (Exception e) {
				result = false;
			}
			return null;
		}
		
		@Override
		protected void onPostExecute(Activity target,Void result) {
			if(this.result){
				switch (StaticData.NOWLANGUAGE) {
				case StaticData.KOREA:
				Toast.makeText(ctx,
				R.string.success_ko, Toast.LENGTH_SHORT).show();
				break;
				case StaticData.ENGLISH:
					Toast.makeText(ctx,
				R.string.success_en, Toast.LENGTH_SHORT).show();
				break;
				case StaticData.CHINA:
					Toast.makeText(ctx,
				R.string.success_cn, Toast.LENGTH_SHORT).show();
				break;
				}
				items.remove(nowDeletePosition);
				nowDeletePosition = 0;
				notifyDataSetChanged();
			}else{
				switch (StaticData.NOWLANGUAGE) {
				case StaticData.KOREA:
				Toast.makeText(ctx,
				R.string.please_retry_ko, Toast.LENGTH_SHORT).show();
				break;
				case StaticData.ENGLISH:
					Toast.makeText(ctx,
				R.string.please_retry_en, Toast.LENGTH_SHORT).show();
				break;
				case StaticData.CHINA:
					Toast.makeText(ctx,
				R.string.please_retry_cn, Toast.LENGTH_SHORT).show();
				break;
				}
			}
		}
	}
}
