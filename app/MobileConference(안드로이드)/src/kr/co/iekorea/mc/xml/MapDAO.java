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
import android.util.Log;

public class MapDAO {
	Activity activity;
	Handler handler;
	String xmlDocument;

	public MapDAO(Activity activity) {
		this.activity = activity;
	}
	
	//map 
	
	public ArrayList<MapDTO> mapList;
	
	public ArrayList<MapDTO> getMapList(){
		return mapList;
	}
	
	public boolean setMapList(){
		String url = StaticData.MAP_URL;
		boolean result = false;
		mapList = new ArrayList<MapDTO>();
		try {
			HttpClient httpClient = new DefaultHttpClient();
			HttpParams params = httpClient.getParams();
			HttpConnectionParams.setConnectionTimeout(params, 10000);
			HttpConnectionParams.setSoTimeout(params, 10000);

			HttpPost httpPost = new HttpPost(url);

			List<NameValuePair> valuePairs = new ArrayList<NameValuePair>(2);
			valuePairs.add(new BasicNameValuePair("conference_id", StaticData.CONFERENCE_ID+ ""));

			CustomLog.e("setMapList", "conference_id :" + StaticData.CONFERENCE_ID);
			Log.e("setMapList", "url :" + url);
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
				CustomLog.e("setMapList", "xmlDocument : " + xmlDocument);

				DocumentBuilderFactory factory = DocumentBuilderFactory
						.newInstance();
				DocumentBuilder builder = factory.newDocumentBuilder();
				Document document = builder.parse(new InputSource(
						new StringReader(xmlDocument.toString())));
				NodeList testList = document.getElementsByTagName("MAP_LIST");
				for (int i = 0; i < testList.getLength(); i++) {
					Node test = testList.item(i);
					NodeList properties = test.getChildNodes();
					MapDTO mapdto = new MapDTO();
					for (int j = 0; j < properties.getLength(); j++) {
						Node property = properties.item(j);
						String nodeName = property.getNodeName().trim();
						if (nodeName.toUpperCase().equals("TITLE")) {
							mapdto.TITLE = property.getTextContent();
						} else if (nodeName.toUpperCase().equals("MAP")) {
							mapdto.MAP = property.getTextContent();
						}
					}
					mapList.add(mapdto);
				}
				result = true;
				// -------------------------------------------------
			} else {
				CustomLog.e("setMapList","httpResponseError : " + httpResponse.getStatusLine());
			}
		} catch (SecurityException e) {
			CustomLog.e("setMapList Error : ","SecurityException : " + e.getMessage());
		} catch (MalformedURLException e) {
			CustomLog.e("setMapList Error : ","MalformedURLException : " + e.getMessage());
		} catch (IOException e) {
			CustomLog.e("setMapList Error : ", "IOException : " + e.getMessage());
		} catch (NumberFormatException e) {
			CustomLog.e("setMapList Error : ","NumberFormatException : " + e.getMessage());
		} catch (NullPointerException e) {
			CustomLog.e("setMapList Error : ","NullPointerException : " + e.getMessage());
		} catch (Exception e) {
			CustomLog.e("setMapList Error : ", "Exception : " + e.getMessage());
		}
		return result;
	}

	
//	public boolean setPresentDetail(int present_id) {
//		String url = StaticData.PRESENTDETAIL_URL;
//		boolean result = false;
//		presentDetailDto = new PresentDetailDto();
//		try {
//			HttpClient httpClient = new DefaultHttpClient();
//			HttpParams params = httpClient.getParams();
//			HttpConnectionParams.setConnectionTimeout(params, 10000);
//			HttpConnectionParams.setSoTimeout(params, 10000);
//
//			HttpPost httpPost = new HttpPost(url);
//
//			List<NameValuePair> valuePairs = new ArrayList<NameValuePair>(2);
//			valuePairs.add(new BasicNameValuePair("present_id", present_id+ ""));
//
//			Log.e("setPresentDetail", "present_id :" + present_id);
//			Log.e("setPresentDetail", "url :" + url);
//			// debug area
//			httpPost.setEntity(new UrlEncodedFormEntity(valuePairs, "UTF-8"));
//			HttpResponse httpResponse = httpClient.execute(httpPost);
//
//			if (httpResponse.getStatusLine().getStatusCode() < 400) {
//				InputStream iStream = httpResponse.getEntity().getContent();
//				InputStreamReader iStreamReader = new InputStreamReader(iStream);
//				BufferedReader readBuffer = new BufferedReader(iStreamReader);
//
//				StringBuilder sBuilder = new StringBuilder();
//				String sLine;
//
//				while ((sLine = readBuffer.readLine()) != null) {
//					sBuilder.append(sLine + "\n");
//				}
//
//				iStream.close();
//
//				if (sBuilder.toString().length() > 1) {
//					xmlDocument = sBuilder.toString();
//				}
//				Log.e("setPresentDetail", "xmlDocument : " + xmlDocument);
//
//				DocumentBuilderFactory factory = DocumentBuilderFactory
//						.newInstance();
//				DocumentBuilder builder = factory.newDocumentBuilder();
//				Document document = builder.parse(new InputSource(
//						new StringReader(xmlDocument.toString())));
//				NodeList testList = document.getElementsByTagName("PRESENT_CONTENTS");
//				for (int i = 0; i < testList.getLength(); i++) {
//					Node test = testList.item(i);
//					NodeList properties = test.getChildNodes();
//					for (int j = 0; j < properties.getLength(); j++) {
//						Node property = properties.item(j);
//						String nodeName = property.getNodeName().trim();
//						if (nodeName.toUpperCase().equals("PRESENT_ID")) {
//							presentDetailDto.PRESENT_ID = Integer.parseInt(property.getTextContent());
//						} else if (nodeName.toUpperCase().equals("MANUFACTURE_NAME")) {
//							presentDetailDto.MANUFACTURE_NAME = property.getTextContent();
//						} else if (nodeName.toUpperCase().equals("PRESENT_NAME")) {
//							presentDetailDto.PRESENT_NAME = property.getTextContent();
//						} else if (nodeName.toUpperCase().equals("PRESENT_CONTENTS")) {
//							presentDetailDto.PRESENT_CONTENTS = property.getTextContent();
//						} else if (nodeName.toUpperCase().equals("PRESENT_IMAGE")) {
//							presentDetailDto.PRESENT_IMAGE = property.getTextContent();
//						}
//					}
//				}
//				result = true;
//				// -------------------------------------------------
//			} else {
//				Log.e("setPresentDetail",
//						"httpResponseError : " + httpResponse.getStatusLine());
//			}
//		} catch (SecurityException e) {
//			Log.e("setPresentDetail Error : ",
//					"SecurityException : " + e.getMessage());
//		} catch (MalformedURLException e) {
//			Log.e("setPresentDetail Error : ",
//					"MalformedURLException : " + e.getMessage());
//		} catch (IOException e) {
//			Log.e("setPresentDetail Error : ", "IOException : " + e.getMessage());
//		} catch (NumberFormatException e) {
//			Log.e("setPresentDetail Error : ",
//					"NumberFormatException : " + e.getMessage());
//		} catch (NullPointerException e) {
//			Log.e("setPresentDetail Error : ",
//					"NullPointerException : " + e.getMessage());
//		} catch (Exception e) {
//			Log.e("setPresentDetail Error : ", "Exception : " + e.getMessage());
//		}
//		return result;
//	}
//	
//	public boolean executeCheckCode(int present_id,String security_cd) {
//		String url = StaticData.PRESENTCHECK_URL;
//		boolean result = false;
//		presentDetailDto = new PresentDetailDto();
//		try {
//			HttpClient httpClient = new DefaultHttpClient();
//			HttpParams params = httpClient.getParams();
//			HttpConnectionParams.setConnectionTimeout(params, 10000);
//			HttpConnectionParams.setSoTimeout(params, 10000);
//
//			HttpPost httpPost = new HttpPost(url);
//
//			List<NameValuePair> valuePairs = new ArrayList<NameValuePair>(2);
//			valuePairs.add(new BasicNameValuePair("present_id", present_id+ ""));
//			valuePairs.add(new BasicNameValuePair("user_cd", StaticData.USER_CD+ ""));
//			valuePairs.add(new BasicNameValuePair("security_cd", security_cd+ ""));
//
//			Log.e("executeCheckCode", "present_id :" + present_id);
//			Log.e("executeCheckCode", "security_cd :" + security_cd);
//			Log.e("executeCheckCode", "url :" + url);
//			// debug area
//			httpPost.setEntity(new UrlEncodedFormEntity(valuePairs, "UTF-8"));
//			HttpResponse httpResponse = httpClient.execute(httpPost);
//
//			if (httpResponse.getStatusLine().getStatusCode() < 400) {
//				InputStream iStream = httpResponse.getEntity().getContent();
//				InputStreamReader iStreamReader = new InputStreamReader(iStream);
//				BufferedReader readBuffer = new BufferedReader(iStreamReader);
//
//				StringBuilder sBuilder = new StringBuilder();
//				String sLine;
//
//				while ((sLine = readBuffer.readLine()) != null) {
//					sBuilder.append(sLine + "\n");
//				}
//
//				iStream.close();
//
//				if (sBuilder.toString().length() > 1) {
//					xmlDocument = sBuilder.toString();
//				}
//				Log.e("executeCheckCode", "xmlDocument : " + xmlDocument);
//
//				DocumentBuilderFactory factory = DocumentBuilderFactory
//						.newInstance();
//				DocumentBuilder builder = factory.newDocumentBuilder();
//				Document document = builder.parse(new InputSource(
//						new StringReader(xmlDocument.toString())));
//				NodeList testList = document.getElementsByTagName("ROOT");
//				for (int i = 0; i < testList.getLength(); i++) {
//					Node test = testList.item(i);
//					NodeList properties = test.getChildNodes();
//					for (int j = 0; j < properties.getLength(); j++) {
//						Node property = properties.item(j);
//						String nodeName = property.getNodeName().trim();
//						if (nodeName.toUpperCase().equals("RESULT")) {
//							if(property.getTextContent().equals("success")){
//								result = true;
//							}
//						} 
//					}
//				}
//				// -------------------------------------------------
//			} else {
//				Log.e("executeCheckCode",
//						"httpResponseError : " + httpResponse.getStatusLine());
//			}
//		} catch (SecurityException e) {
//			Log.e("executeCheckCode Error : ",
//					"SecurityException : " + e.getMessage());
//		} catch (MalformedURLException e) {
//			Log.e("executeCheckCode Error : ",
//					"MalformedURLException : " + e.getMessage());
//		} catch (IOException e) {
//			Log.e("executeCheckCode Error : ", "IOException : " + e.getMessage());
//		} catch (NumberFormatException e) {
//			Log.e("executeCheckCode Error : ",
//					"NumberFormatException : " + e.getMessage());
//		} catch (NullPointerException e) {
//			Log.e("executeCheckCode Error : ",
//					"NullPointerException : " + e.getMessage());
//		} catch (Exception e) {
//			Log.e("executeCheckCode Error : ", "Exception : " + e.getMessage());
//		}
//		return result;
//	}
	
}
