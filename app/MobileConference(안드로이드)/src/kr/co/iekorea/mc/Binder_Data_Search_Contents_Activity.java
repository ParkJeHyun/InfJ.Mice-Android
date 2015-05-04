package kr.co.iekorea.mc;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import kr.co.iekorea.mc.event.BinderTimeSessionContentsEventListener;
import kr.co.iekorea.mc.staticdata.StaticData;
import kr.co.iekorea.mc.util.BaseInterface;
import kr.co.iekorea.mc.util.ProcessManager;
import kr.co.iekorea.mc.util.WeakAsyncTask;
import kr.co.iekorea.mc.xml.BinderContentsDto;
import kr.co.iekorea.mc.xml.MCDao;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

public class Binder_Data_Search_Contents_Activity extends Activity implements
		BaseInterface {
	ProcessManager processManager;

	// activity layout
	private LinearLayout layout_header;
	private ImageButton btn_back;
	private LinearLayout layout_progressbar;
	private TextView text_title, text_writer, text_user, text_contents_body,
			text_contents_title;
	private ImageButton btn_add_vote, btn_pdf_view, btn_qna;
	private int binder_id;

	public BinderContentsDto dto;

	// dialog
	public static final int DIALOG_DOWNLOAD_PROGRESS = 0;
	private ProgressDialog mProgressDialog;

	// initialize root directory
	File rootDir = Environment.getExternalStorageDirectory();
	public String fileName = "";
	public int fileSize = 0;

	boolean isOpen = false;
	boolean isPackage = false;
	
	public TextView info_title,info_user,text_author,total_title,text_wait;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.initView();
		if (StaticData.logined) {
			this.getDefaultValues();
			this.getXmlResources();
			this.modifyXmlResources();
			new executeAgendaContents(Binder_Data_Search_Contents_Activity.this)
					.execute();
		} else {
			switch (StaticData.NOWLANGUAGE) {
			case StaticData.KOREA:
				Toast.makeText(getApplicationContext(),
						R.string.please_retry_ko, Toast.LENGTH_SHORT).show();
				break;
			case StaticData.ENGLISH:
				Toast.makeText(getApplicationContext(),
						R.string.please_retry_en, Toast.LENGTH_SHORT).show();
				break;
			case StaticData.CHINA:
				Toast.makeText(getApplicationContext(),
						R.string.please_retry_cn, Toast.LENGTH_SHORT).show();
				break;
			}

			Intent intent = new Intent(getApplicationContext(),Login_Activity.class);
			startActivity(intent);
			overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
			finish();
		}
	}

	public void getDefaultValues() {
		Intent intent = getIntent();
		this.binder_id = intent.getIntExtra("binder_id", 0);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		this.processManager
				.deleteActivity(Binder_Data_Search_Contents_Activity.this);
	}

	@Override
	public void onBackPressed() {
		Intent intent = new Intent(getApplicationContext(),
				Binder_Data_Search_activity.class);
		startActivity(intent);
		overridePendingTransition(android.R.anim.fade_in,
				android.R.anim.fade_out);
		finish();
	}

	@Override
	public void finish() {
		super.finish();
		overridePendingTransition(android.R.anim.fade_in,
				android.R.anim.fade_out);
	}

	@Override
	public void initView() {
		this.setContentView(R.layout.activity_binder_data_session_time_contents);
		this.processManager = ProcessManager.getInstance();
		this.processManager
				.addActivity(Binder_Data_Search_Contents_Activity.this);
	}

	@Override
	public void getXmlResources() {
		this.btn_back = (ImageButton) this.findViewById(R.id.btn_back);
		this.layout_header = (LinearLayout) this
				.findViewById(R.id.layout_header);
		this.layout_progressbar = (LinearLayout) this
				.findViewById(R.id.layout_progressbar);
		this.text_title = (TextView) this.findViewById(R.id.text_title);
		this.text_writer = (TextView) this.findViewById(R.id.text_writer);
		this.text_user = (TextView) this.findViewById(R.id.text_user);
		this.text_contents_title = (TextView) this.findViewById(R.id.text_contents_title);

		this.text_contents_body = (TextView) this.findViewById(R.id.text_contents_body);

		this.btn_pdf_view = (ImageButton) this.findViewById(R.id.btn_pdf_view);
		this.btn_add_vote = (ImageButton) this.findViewById(R.id.btn_add_vote);
		this.btn_qna = (ImageButton) this.findViewById(R.id.btn_qna);
		
		this.info_title = (TextView) this.findViewById(R.id.info_title);
		this.info_user = (TextView) this.findViewById(R.id.info_user);
		this.text_author = (TextView) this.findViewById(R.id.text_author);
		this.total_title = (TextView) this.findViewById(R.id.total_title);
		this.text_wait = (TextView) this.findViewById(R.id.text_wait);
	}

	@Override
	public void modifyXmlResources() {
		LayoutParams param;
		param = new LayoutParams(LayoutParams.MATCH_PARENT,StaticData.device_Height / 10);
		this.layout_header.setLayoutParams(param);
		
		 
        // 언어 설정에 다른 화면 구성
		switch (StaticData.NOWLANGUAGE) {
		case StaticData.KOREA:
			this.btn_back.setImageResource(R.drawable.b_back_kr);
			this.btn_pdf_view.setImageResource(R.drawable.b3_view_kr);
			this.btn_add_vote.setImageResource(R.drawable.b3_vote_kr);
			this.info_title.setText(getResources().getString(R.string.title_ko));
			this.info_user.setText(getResources().getString(R.string.user_ko));
			this.text_author.setText(getResources().getString(R.string.author_ko));
			this.total_title.setText(getResources().getString(R.string.important_contents_ko));
			this.text_wait.setText(getResources().getString(R.string.wait_ko));
			break;
		case StaticData.ENGLISH:
			this.btn_back.setImageResource(R.drawable.b_back_en);
			this.btn_pdf_view.setImageResource(R.drawable.b3_view_en);
			this.btn_add_vote.setImageResource(R.drawable.b3_vote_en);
			this.info_title.setText(getResources().getString(R.string.title_en));
			this.info_user.setText(getResources().getString(R.string.user_en));
			this.text_author.setText(getResources().getString(R.string.author_en));
			this.total_title.setText(getResources().getString(R.string.important_contents_en));
			this.text_wait.setText(getResources().getString(R.string.wait_en));
			break;
		case StaticData.CHINA:
			this.btn_back.setImageResource(R.drawable.b_back_cn);
			this.btn_pdf_view.setImageResource(R.drawable.b3_view_cn);
			this.btn_add_vote.setImageResource(R.drawable.b3_vote_cn);
			this.info_title.setText(getResources().getString(R.string.title_cn));
			this.info_user.setText(getResources().getString(R.string.user_cn));
			this.text_author.setText(getResources().getString(R.string.author_cn));
			this.total_title.setText(getResources().getString(R.string.important_contents_cn));
			this.text_wait.setText(getResources().getString(R.string.wait_cn));
			break;
		}
	}

	public void setContentsValues() {
		if (dto != null) {
			this.text_title.setText(dto.BINDER_TITLE);
			this.text_writer.setText(dto.WRITER);
			this.text_user.setText(dto.USER_NAME);

			this.text_contents_body.setText(dto.CONTENTS);
			this.text_contents_title.setText(dto.BINDER_TITLE);
		}
	}

	@Override
	public void setEventListener() {
		this.btn_back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(),
						Binder_Data_Search_activity.class);
				startActivity(intent);
				overridePendingTransition(android.R.anim.fade_in,
						android.R.anim.fade_out);
				finish();
			}
		});
		this.btn_add_vote.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Log.e("", "binder id : " + binder_id);
				new executeVote(Binder_Data_Search_Contents_Activity.this)
						.execute();
			}
		});

		this.btn_pdf_view.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (dto.ATTACHED.equals("")) {
					switch (StaticData.NOWLANGUAGE) {
					case StaticData.KOREA:
						Toast.makeText(getApplicationContext(),
								R.string.please_retry_ko, Toast.LENGTH_SHORT).show();
						break;
					case StaticData.ENGLISH:
						Toast.makeText(getApplicationContext(),
								R.string.please_retry_en, Toast.LENGTH_SHORT).show();
						break;
					case StaticData.CHINA:
						Toast.makeText(getApplicationContext(),
								R.string.please_retry_cn, Toast.LENGTH_SHORT).show();
						break;
					}

				} else {
					// pdf 연동
					new DownloadFileAsync(
							Binder_Data_Search_Contents_Activity.this)
							.execute(dto.ATTACHED);
				}
			}
		});

		this.btn_qna.setOnClickListener(new BinderTimeSessionContentsEventListener(this, this, dto.USER_CD, dto.USER_NAME));
	}

	class executeAgendaContents extends WeakAsyncTask<Void, Void, Void, Activity> {
		public executeAgendaContents(Activity target) {
			super(target);
			// TODO Auto-generated constructor stub
		}

		boolean result = false;

		@Override
		protected void onPreExecute(Activity target) {
			layout_progressbar.setVisibility(LinearLayout.VISIBLE);
		}

		@Override
		protected Void doInBackground(Activity target, Void... params) {
			MCDao dao = new MCDao(Binder_Data_Search_Contents_Activity.this);
			try {
				result = dao
						.getBinderDetailContents(Binder_Data_Search_Contents_Activity.this.binder_id);
			} catch (Exception e) {
				result = false;
				e.printStackTrace();
			}

			if (result) {
				dto = new BinderContentsDto();
				dto = dao.getBinderContentsDto();
			}

			return null;
		}

		@Override
		protected void onPostExecute(Activity target, Void param) {
			if (result) {
				Binder_Data_Search_Contents_Activity.this.setContentsValues();
			} else {
				switch (StaticData.NOWLANGUAGE) {
				case StaticData.KOREA:
					Toast.makeText(getApplicationContext(),
							R.string.please_retry_ko, Toast.LENGTH_SHORT).show();
					break;
				case StaticData.ENGLISH:
					Toast.makeText(getApplicationContext(),
							R.string.please_retry_en, Toast.LENGTH_SHORT).show();
					break;
				case StaticData.CHINA:
					Toast.makeText(getApplicationContext(),
							R.string.please_retry_cn, Toast.LENGTH_SHORT).show();
					break;
				}

			}
			Binder_Data_Search_Contents_Activity.this.setEventListener();
			layout_progressbar.setVisibility(LinearLayout.GONE);
		}
	}

	// for pdf -----------------------------------------------------
	// our progress bar settings
	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DIALOG_DOWNLOAD_PROGRESS: // we set this to 0
			mProgressDialog = new ProgressDialog(this);
			mProgressDialog.setMessage("Downloading file...");
			mProgressDialog.setIndeterminate(false);
			mProgressDialog.setMax(100);
			mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
			mProgressDialog.setCancelable(true);
			mProgressDialog.show();
			return mProgressDialog;
		default:
			return null;
		}
	}

	// function to verify if directory exists
	public void checkAndCreateDirectory(String dirName) {
		File new_dir = new File(rootDir + dirName);
		if (!new_dir.exists()) {
			new_dir.mkdirs();
		}
	}

	class DownloadFileAsync extends
			WeakAsyncTask<String, String, String, Activity> {
		public DownloadFileAsync(Activity target) {
			super(target);
			// TODO Auto-generated constructor stub
		}

		@Override
		protected void onPreExecute(Activity target) {
			showDialog(DIALOG_DOWNLOAD_PROGRESS);
		}

		@Override
		protected String doInBackground(Activity target, String... aurl) {
			InputStream in = null;

			try {

				// making sure the download directory exists
				checkAndCreateDirectory("/" + getPackageName() + "/pdf");

				fileName = dto.ATTACHED
						.substring(dto.ATTACHED.lastIndexOf("/") + 1);

				File file = new File(rootDir + "/" + getPackageName() + "/pdf/"
						+ fileName);
				Log.e("pdf down", file.getName());

				// connecting to url
				URL u = new URL(StaticData.DEFAULT_URL + dto.ATTACHED);
				HttpURLConnection c = (HttpURLConnection) u.openConnection();
				c.setRequestMethod("GET");
				c.setDoOutput(true);
				c.connect();
				fileSize = c.getContentLength();
				// Log.e("pdf down", "fileSize : "+fileSize);
				Log.e("pdf down",
						"--------------------------------------------------------------");
				// 파일 다운로드.
				if (!file.exists()) {

					// lenghtOfFile is used for calculating download progress
					int lenghtOfFile = c.getContentLength();

					// this is where the file will be seen after the download
					FileOutputStream f = new FileOutputStream(new File(rootDir
							+ "/" + getPackageName() + "/pdf/", fileName));
					// file input is from the url
					in = c.getInputStream();

					// here's the download code
					byte[] buffer = new byte[1024];
					int len1 = 0;
					long total = 0;

					while ((len1 = in.read(buffer)) > 0) {
						total += len1; // total = total + len1
						publishProgress(""
								+ (int) ((total * 100) / lenghtOfFile));
						f.write(buffer, 0, len1);
					}
					f.close();
				} else {
					if ((int) file.length() != fileSize) {
						// 기존 파일 제거 후 다시 다운로드
						file.delete();

						// lenghtOfFile is used for calculating download
						// progress
						int lenghtOfFile = c.getContentLength();

						// this is where the file will be seen after the
						// download
						FileOutputStream f = new FileOutputStream(new File(
								rootDir + "/" + getPackageName() + "/pdf/",
								fileName));
						// file input is from the url
						in = c.getInputStream();

						// here's the download code
						byte[] buffer = new byte[1024];
						int len1 = 0;
						long total = 0;

						while ((len1 = in.read(buffer)) > 0) {
							total += len1; // total = total + len1
							publishProgress(""
									+ (int) ((total * 100) / lenghtOfFile));
							f.write(buffer, 0, len1);
						}
						f.close();
					}
				}
				isOpen = true;

			} catch (Exception e) {
				e.printStackTrace();
				isOpen = false;
				Log.e(getPackageName().toString(), "File Save/Load failed");
			} finally {
				if (in != null) {
					try {
						in.close();
					} catch (IOException e) {
						Log.e("ERROR", "Socket Exception", e);
					}
				}
			}

			return null;
		}

		@Override
		protected void onProgressUpdate(String... progress) {
			mProgressDialog.setProgress(Integer.parseInt(progress[0]));
		}

		@Override
		protected void onPostExecute(Activity target, String unused) {
			// dismiss the dialog after the file was downloaded
			dismissDialog(DIALOG_DOWNLOAD_PROGRESS);
			if (isOpen) {
				// 특정 package 설치 여부 판단
				PackageManager pm = getApplicationContext().getPackageManager();

//				Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
//				mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
//
//				List<ResolveInfo> appList = pm.queryIntentActivities(
//						mainIntent, 0);
//				Collections.sort(appList,
//						new ResolveInfo.DisplayNameComparator(pm));
//
//				// int temp=0;
//				for (int i = 0; i < appList.size(); i++) {
//					// Log.v("RJ",appList.get(i).loadLabel(pm).toString());
//					// Log.v("RJ",appList.get(i).activityInfo.applicationInfo.packageName.toString());
//
//					if (appList.get(i).activityInfo.applicationInfo.packageName
//							.toString().equals("com.adobe.reader")) {
//						// temp = i;
//						isPackage = true;
//						break;
//					}
//				}

//				if (isPackage) {
//					File file = new File(rootDir + "/" + getPackageName()
//							+ "/pdf/" + fileName);
//					Intent intent = new Intent();
//					intent.setAction(Intent.ACTION_VIEW);
//					intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
//							| Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
//					intent.setDataAndType(Uri.fromFile(file), "application/pdf");
//					startActivity(intent);
//				} else {
//					String market = "market://details?id=" + "com.adobe.reader";
//					String url = String.format(market);
//					Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
//					i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//					try {
//						getApplicationContext().startActivity(i);
//					} catch (Exception e) {
//						Log.e("ERROR", "ERROR", e);
//					}
//				}

				try {
					File file = new File(rootDir + "/" + getPackageName()
							+ "/pdf/" + fileName);
					Intent intent = new Intent();
					intent.setAction(Intent.ACTION_VIEW);
					intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
							| Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
					intent.setDataAndType(Uri.fromFile(file), "application/pdf");
					startActivity(intent);
				} catch(Exception p) {
					String market = "market://details?id=" + "com.adobe.reader";
					String url = String.format(market);
					Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
					i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					try {
						getApplicationContext().startActivity(i);
					} catch (Exception e) {
						Log.e("ERROR", "ERROR", e);
					}
				}
			} else {
				switch (StaticData.NOWLANGUAGE) {
				case StaticData.KOREA:
					Toast.makeText(getApplicationContext(),
							R.string.please_retry_ko, Toast.LENGTH_SHORT).show();
					break;
				case StaticData.ENGLISH:
					Toast.makeText(getApplicationContext(),
							R.string.please_retry_en, Toast.LENGTH_SHORT).show();
					break;
				case StaticData.CHINA:
					Toast.makeText(getApplicationContext(),
							R.string.please_retry_cn, Toast.LENGTH_SHORT).show();
					break;
				}

			}
		}
	}

	// vote

	public void checkVoteState(String result) {
		if (result.equals("true")) {
			switch (StaticData.NOWLANGUAGE) {
			case StaticData.KOREA:
				Toast.makeText(getApplicationContext(),R.string.vote_ko, Toast.LENGTH_SHORT).show();
				break;
			case StaticData.ENGLISH:
				Toast.makeText(getApplicationContext(),R.string.vote_en, Toast.LENGTH_SHORT).show();
				break;
			case StaticData.CHINA:
				Toast.makeText(getApplicationContext(),R.string.vote_cn, Toast.LENGTH_SHORT).show();
				break;
			}
		} else if (result.equals("duplicate")) {
			switch (StaticData.NOWLANGUAGE) {
			case StaticData.KOREA:
				Toast.makeText(getApplicationContext(),R.string.already_vote_ko, Toast.LENGTH_SHORT).show();
				break;
			case StaticData.ENGLISH:
				Toast.makeText(getApplicationContext(),R.string.already_vote_en, Toast.LENGTH_SHORT).show();
				break;
			case StaticData.CHINA:
				Toast.makeText(getApplicationContext(),R.string.already_vote_cn, Toast.LENGTH_SHORT).show();
				break;
			}
		} else if (result.equals("false")) {
			switch (StaticData.NOWLANGUAGE) {
			case StaticData.KOREA:
				Toast.makeText(getApplicationContext(),R.string.please_retry_ko, Toast.LENGTH_SHORT).show();
				break;
			case StaticData.ENGLISH:
				Toast.makeText(getApplicationContext(),R.string.please_retry_en, Toast.LENGTH_SHORT).show();
				break;
			case StaticData.CHINA:
				Toast.makeText(getApplicationContext(),R.string.please_retry_cn, Toast.LENGTH_SHORT).show();
				break;
			}
		} else {
			switch (StaticData.NOWLANGUAGE) {
			case StaticData.KOREA:
				Toast.makeText(getApplicationContext(),R.string.please_retry_ko, Toast.LENGTH_SHORT).show();
				break;
			case StaticData.ENGLISH:
				Toast.makeText(getApplicationContext(),R.string.please_retry_en, Toast.LENGTH_SHORT).show();
				break;
			case StaticData.CHINA:
				Toast.makeText(getApplicationContext(),R.string.please_retry_cn, Toast.LENGTH_SHORT).show();
				break;
			}

		}
		layout_progressbar.setVisibility(LinearLayout.GONE);
	}

	class executeVote extends WeakAsyncTask<Void, Void, Void, Activity> {
		public executeVote(Activity target) {
			super(target);
			// TODO Auto-generated constructor stub
		}

		String result = "";

		@Override
		protected void onPreExecute(Activity target) {
			layout_progressbar.setVisibility(LinearLayout.VISIBLE);
		}

		@Override
		protected Void doInBackground(Activity target, Void... params) {
			MCDao dao = new MCDao(Binder_Data_Search_Contents_Activity.this);
			try {
				result = dao
						.getVoteState(Binder_Data_Search_Contents_Activity.this.binder_id);
			} catch (Exception e) {
				result = "";
				e.printStackTrace();
			}

			return null;
		}

		@Override
		protected void onPostExecute(Activity target, Void param) {
			checkVoteState(result);
		}
	}

}
