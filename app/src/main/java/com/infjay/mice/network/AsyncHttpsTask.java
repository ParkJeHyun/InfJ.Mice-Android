package com.infjay.mice.network;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.util.Base64;
import android.util.Log;

import com.infjay.mice.global.GlobalVariable;

import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.KeyStore;

/**
 * Created by HJHOME on 2015-06-22.
 */
public class AsyncHttpsTask extends AsyncTask<Void, Void, String> {

    private Handler mhandler;
    int DataContent;
    int handlernum = 1;

    String responseString;
    String _url;
    Context mContext;
    JSONObject _jobj;

    private static String TAG = "AsyncHttpsTask";

    /**
     * @param context this, getApplicationContext(), getContest()
     * @param urls //https Server URL
     * @param handler Ŭ�����ȿ� ����� �����س��� �ڵ鷯����
     * @param jobj //������ ������ �����Ͱ� ����ִ� json ��ü
     * @param hnum //Handler Number : �߿�. ���⿡ ������ ��ȣ��� �ٸ� ������ �Լ��� ���� (1�̸� �ڵ鷯���� 1�� �ڵ� ����, 2�� �ڵ鷯���� 2�� �ڵ� ����)
     * @param Data //����, �׳� 0 �־�
     */
    public AsyncHttpsTask(Context context, String urls, Handler handler, JSONObject jobj, int hnum, int Data) {
        mhandler = handler;
        mContext = context;
        _url = urls;

        handlernum = hnum;
        DataContent = Data;
        _jobj = jobj;

        super.execute();
    }

    @Override
    protected String doInBackground(Void... urls) {
        // urls[0]�� URL���� �����͸� �о�� String���� ����
        // Log.i("URL", url);
        return Task(_url,_jobj);

    }

    @Override
    public void onPreExecute() {
        // Log.i("Test", "onPreExecute Called on global");
    }

    @Override
    protected void onPostExecute(String responseData) {
        Log.d(TAG, "Handle Type : " + handlernum);
        Log.d(TAG, "Data Type : " + DataContent);

        Message msg = mhandler.obtainMessage();
        msg.what = handlernum;
        msg.obj = responseString;
        msg.arg1 = DataContent;
        mhandler.sendMessage(msg);

        Log.d(TAG, "Return Data : " + responseString);
    }

    public String Task(String urlString, JSONObject jobj) {
        HttpClient httpClient = getHttpClient();

        try {
            URI _url = new URI(urlString);

            HttpPost httpPost = new HttpPost(_url);

            String encodedJSON = Base64.encodeToString(jobj.toString().getBytes(), 0); //�����Ϸ��� JSON�� Base64�� ���ڵ��ؼ� ��������
            StringEntity entity = new StringEntity(encodedJSON, "UTF-8");

            Log.i(TAG, ("send : " + jobj.toString()));
            Log.i(TAG, ("encoded : " + encodedJSON));

            entity.setContentType("application/json");

            httpPost.setEntity(entity);

            HttpResponse response = httpClient.execute(httpPost); //�����κ���
            String base64ReponseString = EntityUtils.toString(response.getEntity(), HTTP.UTF_8); //�����κ��� ���� String ���� Base64 ����
            Log.i(TAG, "res encoded : "+ base64ReponseString);
            byte[] resBytes = Base64.decode(base64ReponseString, Base64.DEFAULT);
            responseString = new String(resBytes, "UTF-8"); //byte[]�� decode�Ͽ� �ٽ� String����
            Log.i(TAG, (responseString));//���ڵ��� String

        }
        catch(URISyntaxException e) {
            e.printStackTrace();
        }
        catch (ClientProtocolException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }


    private HttpClient getHttpClient() {
        try {
            KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
            trustStore.load(null, null);

            SSLSocketFactory sf = new SFSSLSocketFactory(trustStore);
            sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

            HttpParams params = new BasicHttpParams();
            HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
            HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);

            SchemeRegistry registry = new SchemeRegistry();
            registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
            registry.register(new Scheme("https", sf, GlobalVariable.HTTPS_PORT));

            ClientConnectionManager ccm = new ThreadSafeClientConnManager(params, registry);

            return new DefaultHttpClient(ccm, params);
        } catch (Exception e) {
            return new DefaultHttpClient();
        }

    }
}
