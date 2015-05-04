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
import kr.co.iekorea.mc.xml.MemberSearchContentDto;
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

public class SearchUserDetail_Activity extends Activity implements BaseInterface {
	// Activity control.
	private ProcessManager processManager;

	// layout area
	private ImageButton btn_back;
	private LinearLayout layout_header;
	private LinearLayout layout_progressbar;
	
	// request values to web 
	public int user_cd;
	public int session_id;
	
	// for set values in ui
	private MemberSearchContentDto dto;
	private TextView text_user_name,text_user_title,text_company,text_sex,text_address,text_tel,text_contents_title,
	text_contents_body,text_contents_writer;
	private LinearLayout layout_contents;
	
	// pdf
	private ImageButton btn_pdf_view;
	// dialog
	public static final int DIALOG_DOWNLOAD_PROGRESS = 0;
	private ProgressDialog mProgressDialog;
	
	// initialize root directory
    File rootDir = Environment.getExternalStorageDirectory();
    public String fileName = "";
    public int fileSize = 0;
    
    boolean isOpen= false;
    boolean isPackage = false;
    
    // 언어
    private TextView total_title,text_wait;
    private TextView info_name,info_position,info_company,info_sex,info_address,info_phone;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.initView();
		if (StaticData.logined) {
			this.getDefaultValues();
			this.getXmlResources();
			this.modifyXmlResources();
			
			new setUserInfoDetail(SearchUserDetail_Activity.this).execute();
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
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		this.processManager.deleteActivity(this);
	}
	
	public void getDefaultValues(){
		Intent intent = getIntent();
		this.user_cd = intent.getIntExtra("user_cd", 0);
		this.session_id = intent.getIntExtra("session_id", 0);
	}
	
	@Override
	public void onBackPressed() {
		Intent intent = new Intent(getApplicationContext(), SearchUser_Activity.class);
		startActivity(intent);
		overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
		finish();
	}
	
	@Override
	public void finish() {
		super.finish();
		overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
	}

	@Override
	public void initView() {
		this.setContentView(R.layout.activity_search_user_detail);
		this.processManager = ProcessManager.getInstance();
		this.processManager.addActivity(this);
	}

	@Override
	public void getXmlResources() {
		this.btn_back = (ImageButton) this.findViewById(R.id.btn_back);
		this.layout_header = (LinearLayout) this.findViewById(R.id.layout_header);
		this.layout_progressbar = (LinearLayout) this.findViewById(R.id.layout_progressbar);
		
		this.text_user_name = (TextView) this.findViewById(R.id.text_user_name);
		this.text_user_title = (TextView) this.findViewById(R.id.text_user_title);
		this.text_company = (TextView) this.findViewById(R.id.text_company);
		this.text_sex = (TextView) this.findViewById(R.id.text_sex);
		this.text_address = (TextView) this.findViewById(R.id.text_address);
		this.text_tel = (TextView) this.findViewById(R.id.text_tel);
		this.text_contents_title = (TextView) this.findViewById(R.id.text_contents_title);
		this.text_contents_body = (TextView) this.findViewById(R.id.text_contents_body);
		this.text_contents_writer = (TextView) this.findViewById(R.id.text_contents_writer);
		
		this.layout_contents = (LinearLayout) this.findViewById(R.id.layout_contents);
		this.btn_pdf_view = (ImageButton) this.findViewById(R.id.btn_pdf_view);
		
		this.total_title = (TextView) this.findViewById(R.id.total_title);
		this.text_wait = (TextView) this.findViewById(R.id.text_wait);
		this.info_name = (TextView) this.findViewById(R.id.info_name);
		this.info_position = (TextView) this.findViewById(R.id.info_position);
		this.info_company = (TextView) this.findViewById(R.id.info_company);
		this.info_sex = (TextView) this.findViewById(R.id.info_sex);
		this.info_address = (TextView) this.findViewById(R.id.info_address);
		this.info_phone = (TextView) this.findViewById(R.id.info_phone);
		
	}

	@Override
	public void modifyXmlResources() {
		LayoutParams param;
        param = new LayoutParams(LayoutParams.MATCH_PARENT,StaticData.device_Height/10);
        this.layout_header.setLayoutParams(param);
//        info_name,info_position,info_company,info_sex,info_address,info_phone

        // 언어 설정에 다른 화면 구성
		switch (StaticData.NOWLANGUAGE) {
		case StaticData.KOREA:
			this.btn_back.setImageResource(R.drawable.b_back_kr);
			this.total_title.setText(R.string.important_contents_ko);
			this.text_wait.setText(R.string.wait_ko);
			this.info_name.setText(R.string.name_ko);
			this.info_position.setText(R.string.user_position_ko);
			this.info_company.setText(R.string.company_ko);
			this.info_sex.setText(R.string.sex_ko);
			this.info_address.setText(R.string.address_ko);
			this.info_phone.setText(R.string.phone_ko);
			this.btn_pdf_view.setImageResource(R.drawable.b_view_kr);
			break;
		case StaticData.ENGLISH:
			this.btn_back.setImageResource(R.drawable.b_back_en);
			this.total_title.setText(R.string.important_contents_en);
			this.info_name.setText(R.string.name_en);
			this.info_position.setText(R.string.user_position_en);
			this.info_company.setText(R.string.company_en);
			this.info_sex.setText(R.string.sex_en);
			this.info_address.setText(R.string.address_en);
			this.info_phone.setText(R.string.phone_en);
			this.btn_pdf_view.setImageResource(R.drawable.b_view_en);
			this.text_wait.setText(R.string.wait_en);
			break;
		case StaticData.CHINA:
			this.btn_back.setImageResource(R.drawable.b_back_cn);
			this.total_title.setText(R.string.important_contents_cn);
			this.info_name.setText(R.string.name_cn);
			this.info_position.setText(R.string.user_position_cn);
			this.info_company.setText(R.string.company_cn);
			this.info_sex.setText(R.string.sex_cn);
			this.info_address.setText(R.string.address_cn);
			this.info_phone.setText(R.string.phone_cn);
			this.btn_pdf_view.setImageResource(R.drawable.b_view_cn);
			this.text_wait.setText(R.string.wait_cn);
			break;
		}
	}

	@Override
	public void setEventListener() {
		this.btn_back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(), SearchUser_Activity.class);
				startActivity(intent);
				overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
				finish();
			}
		});
		
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
					new DownloadFileAsync(SearchUserDetail_Activity.this).execute(dto.ATTACHED);
				}				
			}
		});
	}
	
	public void updateUI(){
		this.text_user_name.setText(dto.USER_NAME);
		this.text_user_title.setText(dto.USER_TITLE);
		this.text_company.setText(dto.COMPANY);
		this.text_sex.setText(dto.SEX);
		this.text_address.setText(dto.ADDRESS);
		this.text_tel.setText(dto.PHONE);
		this.text_contents_title.setText(dto.BINDER_TITLE);
		this.text_contents_body.setText(dto.CONTENTS);
		this.text_contents_writer.setText(dto.WRITER);
		
		if(dto.BINDER_TITLE == null){
			this.layout_contents.setVisibility(LinearLayout.GONE);
			this.btn_pdf_view.setVisibility(ImageButton.GONE);
		}else{
			this.layout_contents.setVisibility(LinearLayout.VISIBLE);
			this.btn_pdf_view.setVisibility(ImageButton.VISIBLE);
		}
	}
	
	class setUserInfoDetail extends WeakAsyncTask<Void, Void, Void,Activity> {
		public setUserInfoDetail(Activity target) {
			super(target);
			// TODO Auto-generated constructor stub
		}

		private boolean result;

		@Override
		protected void onPreExecute(Activity target) {
			layout_progressbar.setVisibility(LinearLayout.VISIBLE);
		}

		@Override
		protected Void doInBackground(Activity target,Void... params) {
			SearchDAO dao = new SearchDAO(SearchUserDetail_Activity.this);
			try {
				// input id values 1 for test
				if(session_id == 0){
					result = dao.setUserInfoDetail(user_cd);
				}else{
					result = dao.setUserInfoDetail(user_cd, session_id);					
				}
			} catch (Exception e) {
				result = false;
				e.printStackTrace();
			}
			if (result) {
				if (SearchUserDetail_Activity.this.dto != null) {
					SearchUserDetail_Activity.this.dto = null;
				}
				SearchUserDetail_Activity.this.dto = dao.getMemberDto();
			}
			return null;
		}

		protected void onPostExecute(Activity target,Void param) {
			if (result) {
				updateUI();
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
				
			    try{
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
