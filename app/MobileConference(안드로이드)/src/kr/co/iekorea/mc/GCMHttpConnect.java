package kr.co.iekorea.mc;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class GCMHttpConnect extends Thread{
	private static final String TAG = "HTTP";
	private Request mRequest;
	private String mString;
	
	public GCMHttpConnect(String url, Request request) {
		mString = url;				
		mRequest = request;
	}
	
	@Override
	public void run() {
		download(mString);
		
		Message	msg = new Message();
		msg.what = 0;
		mHandler.sendMessage(msg);
	}
	
	Handler	mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			if (mRequest != null) {		
				mRequest.OnComplete();
			}
		}		
	};
	
	public static interface Request {
		public void OnComplete();
	}
	
	public void download(String address) {
		StringBuilder jsonHtml = new StringBuilder();
    	try{
    		//���� url ����
    		URL url = new URL(address);
    		//���ؼ� ��ü ��
    		HttpURLConnection conn = (HttpURLConnection)url.openConnection();
    		conn.setDefaultUseCaches(false);                                           
            conn.setDoInput(true);                         // �������� �б� ��� ����
            conn.setDoOutput(false);                       // ������ ���� ��� ���� 
            conn.setRequestMethod("POST");         // ��� ����� POST
            
    		//����Ǿ��
    		if(conn != null){
    			conn.setConnectTimeout(10000);
    			conn.setUseCaches(false);
    			//����Ȯ�� �ڵ尡 ���ϵǾ��� ��
    			if(conn.getResponseCode() == HttpURLConnection.HTTP_OK){
    				BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
    				for(;;){
    					String line = br.readLine();
    					if(line == null) break;
    					jsonHtml.append(line);
    				}
    				br.close();
    			}
    			conn.disconnect();
    		}
    	}catch(Exception e){
    		Log.w(TAG, e.getMessage());
    	}
	}
}
