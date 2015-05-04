package kr.co.iekorea.mc;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import kr.co.iekorea.mc.staticdata.StaticData;
import kr.co.iekorea.mc.util.BaseInterface;
import kr.co.iekorea.mc.util.ProcessManager;
import kr.co.iekorea.mc.util.WeakAsyncTask;
import kr.co.iekorea.mc.xml.AbstractDetailDto;
import kr.co.iekorea.mc.xml.SearchDAO;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
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

public class SearchAbstractDetail_Activity extends Activity implements BaseInterface {
	// Activity control.
	private ProcessManager processManager;

	// layout area
	private ImageButton btn_back;
	private LinearLayout layout_header;
	private LinearLayout layout_progressbar;
	
	// intent values
	private String search_flag;
	
	// set ui values
	private AbstractDetailDto dto;
	private int serial;
	
	private TextView text_writer,text_binder_title,text_topic_title,text_presenter,
	text_start_time,text_end_time,text_contents_title,text_contents_body,text_conference_date,text_wait;
	private LinearLayout layout_contents,layout_presenter;
	private ImageButton btn_pdf_view;
	
	// dialog
	public static final int DIALOG_DOWNLOAD_PROGRESS = 0;
	private ProgressDialog mProgressDialog;
	
	//initialize root directory
    File rootDir = Environment.getExternalStorageDirectory();
    public String fileName = "";
    public int fileSize = 0;
    
    boolean isOpen= false;
    boolean isPackage = false;
    
    // 언어
    private TextView total_title,text_author,info_title,info_subject,text_speaker,
    info_announcement_date,info_start_time,info_end_time;
    
    @Override
    protected void onPause() {
//    	StaticData.search_flag = search_flag;
//    	StaticData.serial = serial;
    	super.onPause();
    }
    
    @Override
    protected void onResume() {
    	super.onResume();
//    	search_flag = StaticData.search_flag;
//    	serial = StaticData.serial;
    	new setAbstractDetailInfo(SearchAbstractDetail_Activity.this).execute();
    }
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.initView();
		if (StaticData.logined) {
			this.getIntentValues();
			this.getXmlResources();
			this.modifyXmlResources();
			
			new setAbstractDetailInfo(SearchAbstractDetail_Activity.this).execute();
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
	
	public void getIntentValues(){
		Intent intent = getIntent();
		this.search_flag = intent.getStringExtra("search_flag");
		this.serial = intent.getIntExtra("serial",0);
		
		Log.e("", "search_flag : "+search_flag+", serial : "+serial);
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		this.processManager.deleteActivity(this);
	}
	
	@Override
	public void onBackPressed() {
		Intent intent = new Intent(getApplicationContext(), SearchAbstract_Activity.class);
		startActivity(intent);
		finish();
	}
	
	@Override
	public void finish() {
		super.finish();
		overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
	}

	@Override
	public void initView() {
		this.setContentView(R.layout.activity_search_abstract_detail);
		this.processManager = ProcessManager.getInstance();
		this.processManager.addActivity(this);
	}

	@Override
	public void getXmlResources() {
		this.btn_back = (ImageButton) this.findViewById(R.id.btn_back);
		this.layout_header = (LinearLayout) this.findViewById(R.id.layout_header);
		this.layout_progressbar = (LinearLayout) this.findViewById(R.id.layout_progressbar);
		
		this.text_writer = (TextView) this.findViewById(R.id.text_writer);
		this.text_binder_title = (TextView) this.findViewById(R.id.text_binder_title);
		this.text_topic_title = (TextView) this.findViewById(R.id.text_topic_title);
		this.text_presenter  = (TextView) this.findViewById(R.id.text_presenter);
		this.text_start_time = (TextView) this.findViewById(R.id.text_start_time);
		this.text_end_time = (TextView) this.findViewById(R.id.text_end_time);
		this.text_contents_title = (TextView) this.findViewById(R.id.text_contents_title);
		this.text_contents_body = (TextView) this.findViewById(R.id.text_contents_body);
		this.layout_contents = (LinearLayout) this.findViewById(R.id.layout_contents);
		this.btn_pdf_view = (ImageButton) this.findViewById(R.id.btn_pdf_view);
		this.layout_presenter = (LinearLayout) this.findViewById(R.id.layout_presenter);
		this.text_conference_date = (TextView) this.findViewById(R.id.text_conference_date);
		
		// 언어
		this.total_title = (TextView) this.findViewById(R.id.total_title);
		this.text_author = (TextView) this.findViewById(R.id.text_author);
		this.info_title = (TextView) this.findViewById(R.id.info_title);
		this.info_subject = (TextView) this.findViewById(R.id.info_subject);
		this.text_speaker = (TextView) this.findViewById(R.id.text_speaker);
		this.info_announcement_date = (TextView) this.findViewById(R.id.info_announcement_date);
		this.info_start_time = (TextView) this.findViewById(R.id.info_start_time);
		this.info_end_time = (TextView) this.findViewById(R.id.info_end_time);
		this.text_wait = (TextView) this.findViewById(R.id.text_wait);
		
//		this.total_title
//		this.text_author
//		this.info_title
//		this.info_subject
//		this.text_speaker
//		this.info_announcement_date
//		this.info_start_time
//		this.info_end_time;
	}

	@Override
	public void modifyXmlResources() {
		LayoutParams param;
        param = new LayoutParams(LayoutParams.MATCH_PARENT,StaticData.device_Height/10);
        this.layout_header.setLayoutParams(param);

        // 언어 설정에 다른 화면 구성
		switch (StaticData.NOWLANGUAGE) {
		case StaticData.KOREA:
			this.btn_back.setImageResource(R.drawable.b_back_kr);
			this.total_title.setText(R.string.important_contents_ko);
			this.text_author.setText(R.string.author_ko);
			this.info_title.setText(R.string.title_ko);
			this.text_wait.setText(R.string.wait_ko);
			this.info_subject.setText(R.string.subject_ko);
			this.text_speaker.setText(R.string.speaker_ko);
			this.info_announcement_date.setText(R.string.announcement_date_ko);
			this.info_start_time.setText(R.string.start_time_ko);
			this.info_end_time.setText(R.string.end_time_ko);
			this.btn_pdf_view.setImageResource(R.drawable.b2_view_kr);
			break;
		case StaticData.ENGLISH:
			this.btn_back.setImageResource(R.drawable.b_back_en);
			this.total_title.setText(R.string.important_contents_en);
			this.text_author.setText(R.string.author_en);
			this.info_title.setText(R.string.title_en);
			this.info_subject.setText(R.string.subject_en);
			this.text_speaker.setText(R.string.speaker_en);
			this.info_announcement_date.setText(R.string.announcement_date_en);
			this.info_start_time.setText(R.string.start_time_en);
			this.info_end_time.setText(R.string.end_time_en);
			this.btn_pdf_view.setImageResource(R.drawable.b2_view_en);
			this.text_wait.setText(R.string.wait_en);
			break;
		case StaticData.CHINA:
			this.btn_back.setImageResource(R.drawable.b_back_cn);
			this.total_title.setText(R.string.important_contents_cn);
			this.text_author.setText(R.string.author_cn);
			this.info_title.setText(R.string.title_cn);
			this.info_subject.setText(R.string.subject_cn);
			this.text_speaker.setText(R.string.speaker_cn);
			this.info_announcement_date.setText(R.string.announcement_date_cn);
			this.info_start_time.setText(R.string.start_time_cn);
			this.info_end_time.setText(R.string.end_time_cn);
			this.btn_pdf_view.setImageResource(R.drawable.b2_view_cn);
			this.text_wait.setText(R.string.wait_cn);
			break;
		}
	}

	@Override
	public void setEventListener() {
		this.btn_back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(), SearchAbstract_Activity.class);
				overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
				startActivity(intent);
				finish();
			}
		});
		
		// for pdf
		this.btn_pdf_view.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(dto.ATTACHED.equals("")) {
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
					//pdf 연동
					new DownloadFileAsync(SearchAbstractDetail_Activity.this).execute(dto.ATTACHED);
				}				
			}
		});
	}
	
	public void setUiValues(){
		this.text_writer.setText(dto.WRITER);
		this.text_binder_title.setText(dto.BINDER_TITLE);
		this.text_topic_title.setText(dto.TOPIC_TITLE);
		this.text_presenter.setText(dto.PRESENTER);
		this.text_start_time.setText(dto.START_TIME);
		this.text_end_time.setText(dto.END_TIME);
		this.text_contents_title.setText(dto.BINDER_TITLE);
		this.text_contents_body.setText(dto.CONTETNS);
		this.text_conference_date.setText(dto.CONFERENCE_DATE);
		
		if(search_flag.equals("agenda")){
		}else{
			this.layout_presenter.setVisibility(LinearLayout.GONE);
			this.btn_pdf_view.setVisibility(LinearLayout.GONE);
		}
		
	}
	
	// back grpund
	class setAbstractDetailInfo extends WeakAsyncTask<Void, Void, Void,Activity> {
		public setAbstractDetailInfo(Activity target) {
			super(target);
			// TODO Auto-generated constructor stub
		}

		private boolean result = false;

		@Override
		protected void onPreExecute(Activity target) {
			layout_progressbar.setVisibility(LinearLayout.VISIBLE);
		}

		@Override
		protected Void doInBackground(Activity target,Void... params) {
			SearchDAO dao = new SearchDAO(SearchAbstractDetail_Activity.this);
			try {
				// input id values 1 for test
				result = dao.setAbstractDetailInfo(search_flag,serial);
			} catch (Exception e) {
				result = false;
				e.printStackTrace();
			}
			if (result) {
				if(SearchAbstractDetail_Activity.this.dto != null){
					SearchAbstractDetail_Activity.this.dto = new AbstractDetailDto();
				}
				SearchAbstractDetail_Activity.this.dto = dao.getAbstractDetailDto();
			}
			return null;
		}

		protected void onPostExecute(Activity target,Void param) {
			if(result){
				setUiValues();
			}else{
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
			layout_progressbar.setVisibility(LinearLayout.GONE);
			setEventListener();			
		}
	}
	
	// for pdf -----------------------------------------------------
	//our progress bar settings
    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DIALOG_DOWNLOAD_PROGRESS: //we set this to 0
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
	    
  //function to verify if directory exists
    public void checkAndCreateDirectory(String dirName){
        File new_dir = new File( rootDir + dirName );
        if( !new_dir.exists() ){
            new_dir.mkdirs();
        }
    }
    
	class DownloadFileAsync extends WeakAsyncTask<String, String, String,Activity> {
        public DownloadFileAsync(Activity target) {
			super(target);
			// TODO Auto-generated constructor stub
		}

		@Override
        protected void onPreExecute(Activity target) {
            showDialog(DIALOG_DOWNLOAD_PROGRESS);
        }
        
        @Override
        protected String doInBackground(Activity target,String... aurl) {
        	InputStream in = null;
        	
            try {
            	
            	//making sure the download directory exists
                checkAndCreateDirectory("/"+getPackageName()+"/pdf");
                
                fileName = dto.ATTACHED.substring(dto.ATTACHED.lastIndexOf("/") + 1);
                
                File file = new File(rootDir + "/"+getPackageName()+"/pdf/" + fileName);
                Log.e("pdf down", file.getName());
            	
                
                //connecting to url
                URL u = new URL(StaticData.DEFAULT_URL+dto.ATTACHED);
                HttpURLConnection c = (HttpURLConnection) u.openConnection();
                c.setRequestMethod("GET");
                c.setDoOutput(true);
                c.connect();
            	fileSize = c.getContentLength();
//	            	Log.e("pdf down", "fileSize : "+fileSize);
            	Log.e("pdf down","--------------------------------------------------------------");
            	// 파일 다운로드.
            	if (!file.exists()) {
            		
                    //lenghtOfFile is used for calculating download progress
                    int lenghtOfFile = c.getContentLength();
                    
                    //this is where the file will be seen after the download
                    FileOutputStream f = new FileOutputStream(new File(rootDir + "/"+getPackageName()+"/pdf/", fileName));
                    //file input is from the url
                    in = c.getInputStream();
                    
                    //here's the download code
                    byte[] buffer = new byte[1024];
                    int len1 = 0;
                    long total = 0;
                    
                    while ((len1 = in.read(buffer)) > 0) {
                        total += len1; //total = total + len1
                        publishProgress("" + (int)((total*100)/lenghtOfFile));
                        f.write(buffer, 0, len1);
                    }
                    f.close();
            	} else {
            		if((int)file.length() != fileSize) {
            			//기존 파일 제거 후 다시 다운로드
            			file.delete();
            			
            			//lenghtOfFile is used for calculating download progress
                        int lenghtOfFile = c.getContentLength();
                        
                        //this is where the file will be seen after the download
                        FileOutputStream f = new FileOutputStream(new File(rootDir + "/"+getPackageName()+"/pdf/", fileName));
                        //file input is from the url
                        in = c.getInputStream();
                        
                        //here's the download code
                        byte[] buffer = new byte[1024];
                        int len1 = 0;
                        long total = 0;
                        
                        while ((len1 = in.read(buffer)) > 0) {
                            total += len1; //total = total + len1
                            publishProgress("" + (int)((total*100)/lenghtOfFile));
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
        
        protected void onProgressUpdate(String... progress) {
             mProgressDialog.setProgress(Integer.parseInt(progress[0]));
        }

        @Override
        protected void onPostExecute(Activity target,String unused) {
            //dismiss the dialog after the file was downloaded
            dismissDialog(DIALOG_DOWNLOAD_PROGRESS);
            if(isOpen) {
//	            //특정 package 설치 여부 판단
//				PackageManager pm = getApplicationContext().getPackageManager();
//	
//			    Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
//			    mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
//				
//				List<ResolveInfo> appList = pm.queryIntentActivities(mainIntent, 0);
//			    Collections.sort(appList, new ResolveInfo.DisplayNameComparator(pm));
//				
//			    //int temp=0;
//			    for(int i=0; i<appList.size(); i++)
//			    {
//			    	//Log.v("RJ",appList.get(i).loadLabel(pm).toString());
//			    	//Log.v("RJ",appList.get(i).activityInfo.applicationInfo.packageName.toString());
//			    	
//			    	if(appList.get(i).activityInfo.applicationInfo.packageName.toString().equals("com.adobe.reader"))
//			    	{
//			    		//temp = i;
//			    		isPackage = true;
//			    		break;
//			    	}
//			    } 
				
			    try
			    {
			    	File file = new File(rootDir + "/"+getPackageName()+"/pdf/"+fileName);
					Intent intent = new Intent();
			    	intent.setAction(Intent.ACTION_VIEW);
			    	intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
			    	intent.setDataAndType(Uri.fromFile(file), "application/pdf"); 
			    	startActivity(intent);
			    }
			    catch(Exception p)
			    {
			    	String market = "market://details?id="+"com.adobe.reader";
					String url = String.format(market);
					Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
					i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					try {
						getApplicationContext().startActivity(i);
					} catch (Exception e) {
						Log.e("ERROR","ERROR", e);
					}
			    }
            }else{
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
}
