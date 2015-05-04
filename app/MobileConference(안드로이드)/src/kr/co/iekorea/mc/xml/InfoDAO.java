package kr.co.iekorea.mc.xml;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import kr.co.iekorea.mc.staticdata.StaticData;
import kr.co.iekorea.mc.util.CustomLog;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import android.app.Activity;
import android.os.Handler;

public class InfoDAO {
	Activity activity;
	Handler handler;
	String xmlDocument;

	public InfoDAO(Activity activity) {
		this.activity = activity;
	}
	
	public String IMAGEURL;
	public String getInfoImage(){
		return IMAGEURL;
	}
	public boolean SetInfoImage() {
		String url = StaticData.INFOMATION_URL;
		boolean result = false;
		IMAGEURL = "";
		try {
			HttpClient httpClient = new DefaultHttpClient();
			HttpParams params = httpClient.getParams();
			HttpConnectionParams.setConnectionTimeout(params, 10000);
			HttpConnectionParams.setSoTimeout(params, 10000);

			HttpPost httpPost = new HttpPost(url);

			List<NameValuePair> valuePairs = new ArrayList<NameValuePair>(2);
			valuePairs.add(new BasicNameValuePair("conference_id", StaticData.CONFERENCE_ID+ ""));

			CustomLog.e("SetInfoImage", "conference_id :" + StaticData.CONFERENCE_ID);
			CustomLog.e("SetInfoImage", "url :" + url);
			// debug area
			httpPost.setEntity(new UrlEncodedFormEntity(valuePairs, "UTF-8"));
			HttpResponse httpResponse = httpClient.execute(httpPost);

			if (httpResponse.getStatusLine().getStatusCode() < 400) {
				InputStream iStream = httpResponse.getEntity().getContent();
				InputStreamReader iStreamReader = new InputStreamReader(iStream);
				BufferedReader readBuffer = new BufferedReader(iStreamReader);

				StringBuilder sBuilder = new StringBuilder();
				String sLine;

				while ((sLine = readBuffer.readLine()) != null) {
					sBuilder.append(sLine + "\n");
				}

				iStream.close();

				if (sBuilder.toString().length() > 1) {
					xmlDocument = sBuilder.toString();
				}
				CustomLog.e("SetInfoImage", "xmlDocument : " + xmlDocument);

				DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
				DocumentBuilder builder = factory.newDocumentBuilder();
				Document document = builder.parse(new InputSource(new StringReader(xmlDocument.toString())));
				NodeList testList = document.getElementsByTagName("INFOMAION_LIST");
				for (int i = 0; i < testList.getLength(); i++) {
					Node test = testList.item(i);
					NodeList properties = test.getChildNodes();
					for (int j = 0; j < properties.getLength(); j++) {
						Node property = properties.item(j);
						String nodeName = property.getNodeName().trim();
						if (nodeName.toUpperCase().equals("IMAGE")) {
							IMAGEURL = property.getTextContent();
						} 
					}
				}
				result = true;
				// -------------------------------------------------
			} else {
				CustomLog.e("SetInfoImage","httpResponseError : " + httpResponse.getStatusLine());
			}
		} catch (SecurityException e) {
			CustomLog.e("SetInfoImage Error : ","SecurityException : " + e.getMessage());
		} catch (MalformedURLException e) {
			CustomLog.e("SetInfoImage Error : ","MalformedURLException : " + e.getMessage());
		} catch (IOException e) {
			CustomLog.e("SetInfoImage Error : ", "IOException : " + e.getMessage());
		} catch (NumberFormatException e) {
			CustomLog.e("SetInfoImage Error : ","NumberFormatException : " + e.getMessage());
		} catch (NullPointerException e) {
			CustomLog.e("SetInfoImage Error : ","NullPointerException : " + e.getMessage());
		} catch (Exception e) {
			CustomLog.e("SetInfoImage Error : ", "Exception : " + e.getMessage());
		}
		return result;
	}
}
