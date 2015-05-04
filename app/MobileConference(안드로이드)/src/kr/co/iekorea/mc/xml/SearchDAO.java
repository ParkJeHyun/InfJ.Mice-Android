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

public class SearchDAO {
	Activity activity;
	Handler handler;
	String xmlDocument;

	public SearchDAO(Activity activity) {
		this.activity = activity;
	}
	
	public ArrayList<SearchSessionDto> sessionDtoList;
	
	public ArrayList<SearchSessionDto> getSessionDtoList(){
		return sessionDtoList;
	}

	public boolean setSpinnerSession(){
		String url = StaticData.MEMBER_SEARCH_SESSION;
		boolean result = false;
		sessionDtoList = new ArrayList<SearchSessionDto>();
		
		try{
			HttpClient httpClient = new DefaultHttpClient();
			HttpParams params = httpClient.getParams();
			HttpConnectionParams.setConnectionTimeout(params, 10000);
			HttpConnectionParams.setSoTimeout(params, 10000);

			HttpPost httpPost = new HttpPost(url);

			List<NameValuePair> valuePairs = new ArrayList<NameValuePair>(2);
			valuePairs.add(new BasicNameValuePair("conference_id", StaticData.CONFERENCE_ID+""));

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
				Log.e("getSpinnerSession", "----------------------------------------");
				
				DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
				DocumentBuilder builder = factory.newDocumentBuilder();
				Document document = builder.parse(new InputSource(new StringReader(xmlDocument.toString())));

				NodeList testList = document.getElementsByTagName("SESSION");
				for(int i =0; i<testList.getLength(); i++){
					Node test = testList.item(i);
					NodeList properties = test.getChildNodes();
					
					SearchSessionDto dto = new SearchSessionDto();
					for(int j = 0; j< properties.getLength(); j++){
						Node property = properties.item(j);
						String nodeName = property.getNodeName();
						if (nodeName.toUpperCase().equals("SESSION_ID")) {
							dto.SESSION_ID = Integer.parseInt(property.getTextContent());
						}else if(nodeName.toUpperCase().equals("SESSION_TITLE")) {
							dto.SESSION_TITLE = property.getTextContent();
						}
					}
					sessionDtoList.add(dto);
				}
				Log.e("setSpinnerSession","sessionDtoList size : " + sessionDtoList.size());
				result = true;
				// -------------------------------------------------
			} else {
				Log.e("getSpinnerSession","httpResponseError : " + httpResponse.getStatusLine());
			}
		} catch (SecurityException e) {
			Log.e("getSpinnerSession Error : ","SecurityException : " + e.getMessage());
		} catch (MalformedURLException e) {
			Log.e("getSpinnerSession Error : ","MalformedURLException : " + e.getMessage());
		} catch (IOException e) {
			Log.e("getSpinnerSession Error : ", "IOException : " + e.getMessage());
		} catch (NumberFormatException e) {
			Log.e("getSpinnerSession Error : ","NumberFormatException : " + e.getMessage());
		} catch (NullPointerException e) {
			Log.e("getSpinnerSession Error : ","NullPointerException : " + e.getMessage());
		} catch (Exception e) {
			Log.e("getSpinnerSession Error : ", "Exception : " + e.getMessage());
		}
		return result;
	}
	
	
	public SearchUserItemDto dto;
	public ArrayList<SearchUserItemDto> SearchUserItemDtoList;
	public ArrayList<SearchUserItemDto> getSearchUserItemDtoList(){
		return SearchUserItemDtoList;
	}
	
	public boolean setUserInfo(){
		String url = StaticData.MEMBER_SEARCH;
		boolean result = false;
		SearchUserItemDtoList = new ArrayList<SearchUserItemDto>();
		
		try{
			HttpClient httpClient = new DefaultHttpClient();
			HttpParams params = httpClient.getParams();
			HttpConnectionParams.setConnectionTimeout(params, 10000);
			HttpConnectionParams.setSoTimeout(params, 10000);

			HttpPost httpPost = new HttpPost(url);

			List<NameValuePair> valuePairs = new ArrayList<NameValuePair>(2);
			valuePairs.add(new BasicNameValuePair("conference_id", StaticData.CONFERENCE_ID+""));

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
				Log.e("setUserInfo", "----------------------------------------");
				
				DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
				DocumentBuilder builder = factory.newDocumentBuilder();
				Document document = builder.parse(new InputSource(new StringReader(xmlDocument.toString())));

				NodeList testList = document.getElementsByTagName("MEMBER_SEARCH");
				for(int i =0; i<testList.getLength(); i++){
					Node test = testList.item(i);
					NodeList properties = test.getChildNodes();
					
					SearchUserItemDto dto = new SearchUserItemDto();
					for(int j = 0; j< properties.getLength(); j++){
						Node property = properties.item(j);
						String nodeName = property.getNodeName();
						if (nodeName.toUpperCase().equals("USER_CD")) {
							dto.USER_CD = Integer.parseInt(property.getTextContent());
						}else if(nodeName.toUpperCase().equals("USER_NAME")) {
							dto.USER_NAME = property.getTextContent();
						}else if(nodeName.toUpperCase().equals("COMPANY")) {
							dto.COMPANY = property.getTextContent();
						}else if(nodeName.toUpperCase().equals("NATIONAL_NAME")) {
							dto.NATIONAL_NAME = property.getTextContent();
						}
					}
					SearchUserItemDtoList.add(dto);
				}
				Log.e("setUserInfo","SearchUserItemDtoList size : " + SearchUserItemDtoList.size());
				result = true;
				// -------------------------------------------------
			} else {
				Log.e("setUserInfo","httpResponseError : " + httpResponse.getStatusLine());
			}
		} catch (SecurityException e) {
			Log.e("setUserInfo Error : ","SecurityException : " + e.getMessage());
		} catch (MalformedURLException e) {
			Log.e("setUserInfo Error : ","MalformedURLException : " + e.getMessage());
		} catch (IOException e) {
			Log.e("setUserInfo Error : ", "IOException : " + e.getMessage());
		} catch (NumberFormatException e) {
			Log.e("setUserInfo Error : ","NumberFormatException : " + e.getMessage());
		} catch (NullPointerException e) {
			Log.e("setUserInfo Error : ","NullPointerException : " + e.getMessage());
		} catch (Exception e) {
			Log.e("setUserInfo Error : ", "Exception : " + e.getMessage());
		}
		return result;
	}
	
	
	public boolean setUserInfoReflash(int session_id){
		String url = StaticData.MEMBER_SEARCH;
		boolean result = false;
		SearchUserItemDtoList = new ArrayList<SearchUserItemDto>();
		
		try{
			HttpClient httpClient = new DefaultHttpClient();
			HttpParams params = httpClient.getParams();
			HttpConnectionParams.setConnectionTimeout(params, 10000);
			HttpConnectionParams.setSoTimeout(params, 10000);

			HttpPost httpPost = new HttpPost(url);

			List<NameValuePair> valuePairs = new ArrayList<NameValuePair>(2);
			valuePairs.add(new BasicNameValuePair("conference_id", StaticData.CONFERENCE_ID+""));
			valuePairs.add(new BasicNameValuePair("session_id", session_id+""));
			
			Log.e("setUserInfoReflash", "session_id : "+session_id);

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
				Log.e("setUserInfoReflash", "----------------------------------------");
				
				DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
				DocumentBuilder builder = factory.newDocumentBuilder();
				Document document = builder.parse(new InputSource(new StringReader(xmlDocument.toString())));

				NodeList testList = document.getElementsByTagName("MEMBER_SEARCH");
				for(int i =0; i<testList.getLength(); i++){
					Node test = testList.item(i);
					NodeList properties = test.getChildNodes();
					
					SearchUserItemDto dto = new SearchUserItemDto();
					for(int j = 0; j< properties.getLength(); j++){
						Node property = properties.item(j);
						String nodeName = property.getNodeName();
						if (nodeName.toUpperCase().equals("USER_CD")) {
							dto.USER_CD = Integer.parseInt(property.getTextContent());
						}else if(nodeName.toUpperCase().equals("USER_NAME")) {
							dto.USER_NAME = property.getTextContent();
						}else if(nodeName.toUpperCase().equals("COMPANY")) {
							dto.COMPANY = property.getTextContent();
						}else if(nodeName.toUpperCase().equals("NATIONAL_NAME")) {
							dto.NATIONAL_NAME = property.getTextContent();
						}
					}
					SearchUserItemDtoList.add(dto);
				}
				Log.e("setUserInfoReflash","SearchUserItemDtoList size : " + SearchUserItemDtoList.size());
				result = true;
				// -------------------------------------------------
			} else {
				Log.e("setUserInfoReflash","httpResponseError : " + httpResponse.getStatusLine());
			}
		} catch (SecurityException e) {
			Log.e("setUserInfoReflash Error : ","SecurityException : " + e.getMessage());
		} catch (MalformedURLException e) {
			Log.e("setUserInfoReflash Error : ","MalformedURLException : " + e.getMessage());
		} catch (IOException e) {
			Log.e("setUserInfoReflash Error : ", "IOException : " + e.getMessage());
		} catch (NumberFormatException e) {
			Log.e("setUserInfoReflash Error : ","NumberFormatException : " + e.getMessage());
		} catch (NullPointerException e) {
			Log.e("setUserInfoReflash Error : ","NullPointerException : " + e.getMessage());
		} catch (Exception e) {
			Log.e("setUserInfoReflash Error : ", "Exception : " + e.getMessage());
		}
		return result;
	}
	
	public boolean setUserInfo(String search_flag, String keyword, int session_id){
		String url = StaticData.MEMBER_SEARCH;
		boolean result = false;
		SearchUserItemDtoList = new ArrayList<SearchUserItemDto>();
		
		try{
			HttpClient httpClient = new DefaultHttpClient();
			HttpParams params = httpClient.getParams();
			HttpConnectionParams.setConnectionTimeout(params, 10000);
			HttpConnectionParams.setSoTimeout(params, 10000);

			HttpPost httpPost = new HttpPost(url);

			List<NameValuePair> valuePairs = new ArrayList<NameValuePair>(2);
			valuePairs.add(new BasicNameValuePair("conference_id", StaticData.CONFERENCE_ID+""));
			valuePairs.add(new BasicNameValuePair("session_id", session_id+""));
			valuePairs.add(new BasicNameValuePair("search_flag", search_flag));
			valuePairs.add(new BasicNameValuePair("keyword", keyword));
			
			Log.e("", "search_flag : "+search_flag);
			Log.e("", "keyword : "+keyword);

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
				Log.e("setUserInforeflsh", "----------------------------------------");
				Log.e("setUserInforeflsh", "xmlDocument : "+xmlDocument);
				
				DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
				DocumentBuilder builder = factory.newDocumentBuilder();
				Document document = builder.parse(new InputSource(new StringReader(xmlDocument.toString())));

				NodeList testList = document.getElementsByTagName("MEMBER_SEARCH");
				for(int i =0; i<testList.getLength(); i++){
					Node test = testList.item(i);
					NodeList properties = test.getChildNodes();
					
					SearchUserItemDto dto = new SearchUserItemDto();
					for(int j = 0; j< properties.getLength(); j++){
						Node property = properties.item(j);
						String nodeName = property.getNodeName();
						if (nodeName.toUpperCase().equals("USER_CD")) {
							dto.USER_CD = Integer.parseInt(property.getTextContent());
						}else if(nodeName.toUpperCase().equals("USER_NAME")) {
							dto.USER_NAME = property.getTextContent();
						}else if(nodeName.toUpperCase().equals("COMPANY")) {
							dto.COMPANY = property.getTextContent();
						}else if(nodeName.toUpperCase().equals("NATIONAL_NAME")) {
							dto.NATIONAL_NAME = property.getTextContent();
						}
					}
					SearchUserItemDtoList.add(dto);
				}
				Log.e("setUserInfo","SearchUserItemDtoList size : " + SearchUserItemDtoList.size());
				result = true;
				// -------------------------------------------------
			} else {
				Log.e("setUserInfo","httpResponseError : " + httpResponse.getStatusLine());
			}
		} catch (SecurityException e) {
			Log.e("setUserInfo Error : ","SecurityException : " + e.getMessage());
		} catch (MalformedURLException e) {
			Log.e("setUserInfo Error : ","MalformedURLException : " + e.getMessage());
		} catch (IOException e) {
			Log.e("setUserInfo Error : ", "IOException : " + e.getMessage());
		} catch (NumberFormatException e) {
			Log.e("setUserInfo Error : ","NumberFormatException : " + e.getMessage());
		} catch (NullPointerException e) {
			Log.e("setUserInfo Error : ","NullPointerException : " + e.getMessage());
		} catch (Exception e) {
			Log.e("setUserInfo Error : ", "Exception : " + e.getMessage());
		}
		return result;
	}
	
	
	public MemberSearchContentDto memberDto;
	public MemberSearchContentDto getMemberDto(){ 
		return memberDto;
	}
	public boolean setUserInfoDetail(int user_cd, int session_id){
		String url = StaticData.MEMBER_SEARCH_CONTENTS;
		boolean result = false;
		
		try{
			HttpClient httpClient = new DefaultHttpClient();
			HttpParams params = httpClient.getParams();
			HttpConnectionParams.setConnectionTimeout(params, 10000);
			HttpConnectionParams.setSoTimeout(params, 10000);

			HttpPost httpPost = new HttpPost(url);

			List<NameValuePair> valuePairs = new ArrayList<NameValuePair>(2);
			valuePairs.add(new BasicNameValuePair("conference_id", StaticData.CONFERENCE_ID+""));
			valuePairs.add(new BasicNameValuePair("user_cd", user_cd+""));
			valuePairs.add(new BasicNameValuePair("session_id", session_id+""));

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
				Log.e("setUserInfoDetail", "----------------------------------------");
				
				DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
				DocumentBuilder builder = factory.newDocumentBuilder();
				Document document = builder.parse(new InputSource(new StringReader(xmlDocument.toString())));

				NodeList testList = document.getElementsByTagName("MEMBER_SEARCH_CONTENTS");
				for(int i =0; i<testList.getLength(); i++){
					Node test = testList.item(i);
					NodeList properties = test.getChildNodes();
					
					memberDto = new MemberSearchContentDto();
					for(int j = 0; j< properties.getLength(); j++){
						Node property = properties.item(j);
						String nodeName = property.getNodeName();
						if (nodeName.toUpperCase().equals("USER_ID")) {
							memberDto.USER_ID = property.getTextContent();
						}else if(nodeName.toUpperCase().equals("USER_NAME")) {
							memberDto.USER_NAME = property.getTextContent();
						}else if(nodeName.toUpperCase().equals("COMPANY")) {
							memberDto.COMPANY = property.getTextContent();
						}else if(nodeName.toUpperCase().equals("SEX")) {
							memberDto.SEX = property.getTextContent();
						}else if(nodeName.toUpperCase().equals("USER_TITLE")) {
							memberDto.USER_TITLE = property.getTextContent();
						}else if(nodeName.toUpperCase().equals("EMAIL")) {
							memberDto.EMAIL = property.getTextContent();
						}else if(nodeName.toUpperCase().equals("PHONE")) {
							memberDto.PHONE = property.getTextContent();
						}else if(nodeName.toUpperCase().equals("ADDRESS")) {
							memberDto.ADDRESS = property.getTextContent();
						}else if(nodeName.toUpperCase().equals("BINDER")) {
							NodeList binderlist = property.getChildNodes();
							for(int k=0; k<binderlist.getLength(); k++){
								Node binder = binderlist.item(j);
								String nodeNamebinder = binder.getNodeName();
								if (nodeNamebinder.toUpperCase().equals("BINDER_TITLE")) {
									memberDto.BINDER_TITLE = binder.getTextContent();
								}else if (nodeNamebinder.toUpperCase().equals("CONTENTS")) {
									memberDto.CONTENTS = binder.getTextContent();
								}else if (nodeNamebinder.toUpperCase().equals("WRITER")) {
									memberDto.WRITER = binder.getTextContent();
								}else if (nodeNamebinder.toUpperCase().equals("ATTACHED")) {
									memberDto.ATTACHED = binder.getTextContent();
								}
							}
						}
					}
				}
				Log.e("setUserInfoDetail","memberDto bindertitle : " + memberDto.BINDER_TITLE);
				result = true;
				// -------------------------------------------------
			} else {
				Log.e("setUserInfoDetail","httpResponseError : " + httpResponse.getStatusLine());
			}
		} catch (SecurityException e) {
			Log.e("setUserInfoDetail Error : ","SecurityException : " + e.getMessage());
		} catch (MalformedURLException e) {
			Log.e("setUserInfoDetail Error : ","MalformedURLException : " + e.getMessage());
		} catch (IOException e) {
			Log.e("setUserInfoDetail Error : ", "IOException : " + e.getMessage());
		} catch (NumberFormatException e) {
			Log.e("setUserInfoDetail Error : ","NumberFormatException : " + e.getMessage());
		} catch (NullPointerException e) {
			Log.e("setUserInfoDetail Error : ","NullPointerException : " + e.getMessage());
		} catch (Exception e) {
			Log.e("setUserInfoDetail Error : ", "Exception : " + e.getMessage());
		}
		return result;
	}
	
	public boolean setUserInfoDetail(int user_cd){
		String url = StaticData.MEMBER_SEARCH_CONTENTS;
		boolean result = false;
		memberDto = new MemberSearchContentDto();
		
		try{
			HttpClient httpClient = new DefaultHttpClient();
			HttpParams params = httpClient.getParams();
			HttpConnectionParams.setConnectionTimeout(params, 10000);
			HttpConnectionParams.setSoTimeout(params, 10000);

			HttpPost httpPost = new HttpPost(url);

			List<NameValuePair> valuePairs = new ArrayList<NameValuePair>(2);
			valuePairs.add(new BasicNameValuePair("conference_id", StaticData.CONFERENCE_ID+""));
			valuePairs.add(new BasicNameValuePair("user_cd", user_cd+""));

			Log.e("", "url : "+url);
			Log.e("", "conference_id : "+StaticData.CONFERENCE_ID);
			Log.e("", "user_cd : "+user_cd);
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
				Log.e("setUserInfoDetail user cd", "----------------------------------------");
				Log.e("setUserInfoDetail user cd", "xmlDocument : "+xmlDocument);
				Log.e("setUserInfoDetail user cd", "user cd : "+user_cd);
				
				DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
				DocumentBuilder builder = factory.newDocumentBuilder();
				Document document = builder.parse(new InputSource(new StringReader(xmlDocument.toString())));

				NodeList testList = document.getElementsByTagName("MEMBER_SEARCH_CONTENTS");
				for(int i =0; i<testList.getLength(); i++){
					Node test = testList.item(i);
					NodeList properties = test.getChildNodes();
					Log.e("", "testList.getLength() : "+testList.getLength());
					Log.e("", "properties.getLength() : "+properties.getLength());
					for(int j = 0; j< properties.getLength(); j++){
						Node property = properties.item(j);
						String nodeName = property.getNodeName();
						if (nodeName.toUpperCase().equals("USER_ID")) {
							memberDto.USER_ID = property.getTextContent();
						}else if(nodeName.toUpperCase().equals("USER_NAME")) {
							memberDto.USER_NAME = property.getTextContent();
						}else if(nodeName.toUpperCase().equals("COMPANY")) {
							memberDto.COMPANY = property.getTextContent();
						}else if(nodeName.toUpperCase().equals("SEX")) {
							memberDto.SEX = property.getTextContent();
						}else if(nodeName.toUpperCase().equals("USER_TITLE")) {
							memberDto.USER_TITLE = property.getTextContent();
						}else if(nodeName.toUpperCase().equals("EMAIL")) {
							memberDto.EMAIL = property.getTextContent();
						}else if(nodeName.toUpperCase().equals("PHONE")) {
							memberDto.PHONE = property.getTextContent();
						}else if(nodeName.toUpperCase().equals("ADDRESS")) {
							memberDto.ADDRESS = property.getTextContent();
						}else if(nodeName.toUpperCase().equals("BINDER")) {
							
							NodeList binderlist = property.getChildNodes();
							for(int k=0; k<binderlist.getLength(); k++){
								Node binder = binderlist.item(k);
								String nodeNamebinder = binder.getNodeName();
								if (nodeNamebinder.toUpperCase().equals("BINDER_TITLE")) {
									memberDto.BINDER_TITLE = binder.getTextContent();
								}else if (nodeNamebinder.toUpperCase().equals("CONTENTS")) {
									memberDto.CONTENTS = binder.getTextContent();
								}else if (nodeNamebinder.toUpperCase().equals("WRITER")) {
									memberDto.WRITER = binder.getTextContent();
								}else if (nodeNamebinder.toUpperCase().equals("ATTACHED")) {
									memberDto.ATTACHED = binder.getTextContent();
								}
							}
						}
					}
				}
				Log.e("setUserInfoDetail","memberDto bindertitle : " + memberDto.BINDER_TITLE);
				result = true;
				// -------------------------------------------------
			} else {
				Log.e("setUserInfoDetail","httpResponseError : " + httpResponse.getStatusLine());
			}
		} catch (SecurityException e) {
			Log.e("setUserInfoDetail Error : ","SecurityException : " + e.getMessage());
		} catch (MalformedURLException e) {
			Log.e("setUserInfoDetail Error : ","MalformedURLException : " + e.getMessage());
		} catch (IOException e) {
			Log.e("setUserInfoDetail Error : ", "IOException : " + e.getMessage());
		} catch (NumberFormatException e) {
			Log.e("setUserInfoDetail Error : ","NumberFormatException : " + e.getMessage());
		} catch (NullPointerException e) {
			Log.e("setUserInfoDetail Error : ","NullPointerException : " + e.getMessage());
		} catch (Exception e) {
			Log.e("setUserInfoDetail Error : ", "Exception : " + e.getMessage());
		}
		return result;
	}
	
	
	// stay information
	
	public ArrayList<StayDto> stayList;
	public ArrayList<StayDto> getStayList(){
		return stayList;
	}
	public boolean setStayInformaion(){
		String url = StaticData.ADDITIONAL;
		boolean result = false;
		stayList = new ArrayList<StayDto>();
		
		try{
			HttpClient httpClient = new DefaultHttpClient();
			HttpParams params = httpClient.getParams();
			HttpConnectionParams.setConnectionTimeout(params, 10000);
			HttpConnectionParams.setSoTimeout(params, 10000);

			HttpPost httpPost = new HttpPost(url);

			List<NameValuePair> valuePairs = new ArrayList<NameValuePair>(2);
			valuePairs.add(new BasicNameValuePair("conference_id", StaticData.CONFERENCE_ID+""));

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
				
				Log.e("setStayInformaion", "-StaticData.CONFERENCE_ID - "+StaticData.CONFERENCE_ID);
				Log.e("setStayInformaion", "xmlDocument : "+xmlDocument);
				
				DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
				DocumentBuilder builder = factory.newDocumentBuilder();
				Document document = builder.parse(new InputSource(new StringReader(xmlDocument.toString())));

				NodeList testList = document.getElementsByTagName("ADDITIONAL_ITEM");
				for(int i =0; i<testList.getLength(); i++){
					Node test = testList.item(i);
					NodeList properties = test.getChildNodes();
					
					StayDto dto = new StayDto();
					for(int j = 0; j< properties.getLength(); j++){
						Node property = properties.item(j);
						String nodeName = property.getNodeName();
						if (nodeName.toUpperCase().equals("FLAG")) {
							dto.state = property.getTextContent();
						}else if(nodeName.toUpperCase().equals("LATITUDE")) {
							if(!property.getTextContent().equals("null")){
								dto.Lat = Double.parseDouble(property.getTextContent());								
							}
						}else if(nodeName.toUpperCase().equals("LONGITUDE")) {
							if(!property.getTextContent().equals("null")){
								dto.Lon = Double.parseDouble(property.getTextContent());
							}
						}else if(nodeName.toUpperCase().equals("MAIN_TITLE")) {
							dto.title = property.getTextContent();
						}else if(nodeName.toUpperCase().equals("SUB_TITLE")) {
							dto.subTitle = property.getTextContent();
						}else if(nodeName.toUpperCase().equals("CONTENTS")) {
							dto.Contents = property.getTextContent();
						}else if(nodeName.toUpperCase().equals("PHONE")) {
							dto.PHONE = property.getTextContent();
						}else if(nodeName.toUpperCase().equals("ADDRESS")) {
							dto.ADDRESS = property.getTextContent();
						}
					}
					stayList.add(dto);
				}
				Log.e("setStayInformaion","stayList size : " + stayList.size());
				result = true;
				// -------------------------------------------------
			} else {
				Log.e("setStayInformaion","httpResponseError : " + httpResponse.getStatusLine());
			}
		} catch (SecurityException e) {
			Log.e("setStayInformaion Error : ","SecurityException : " + e.getMessage());
		} catch (MalformedURLException e) {
			Log.e("setStayInformaion Error : ","MalformedURLException : " + e.getMessage());
		} catch (IOException e) {
			Log.e("setStayInformaion Error : ", "IOException : " + e.getMessage());
		} catch (NumberFormatException e) {
			Log.e("setStayInformaion Error : ","NumberFormatException : " + e.getMessage());
		} catch (NullPointerException e) {
			Log.e("setStayInformaion Error : ","NullPointerException : " + e.getMessage());
		} catch (Exception e) {
			Log.e("setStayInformaion Error : ", "Exception : " + e.getMessage());
		}
		return result;
	}
	
	// abstract list
	
	public ArrayList<AbstractDto> abstractDtoList= new ArrayList<AbstractDto>();
	public ArrayList<AbstractDto> getAbstractList(){
		return abstractDtoList;
	}
	
	public boolean setAbstractInfo(){
		String url = StaticData.DISSERTATION_SEARCH;
		boolean result = false;

		if(abstractDtoList != null){
			abstractDtoList.clear();
		}
		
		try{
			HttpClient httpClient = new DefaultHttpClient();
			HttpParams params = httpClient.getParams();
			HttpConnectionParams.setConnectionTimeout(params, 10000);
			HttpConnectionParams.setSoTimeout(params, 10000);

			HttpPost httpPost = new HttpPost(url);

			List<NameValuePair> valuePairs = new ArrayList<NameValuePair>(2);
			valuePairs.add(new BasicNameValuePair("conference_id", StaticData.CONFERENCE_ID+""));

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
				Log.e("setAbstractInfo", "----------------------------------------");
				Log.e("setAbstractInfo", "xmlDocument : "+xmlDocument);
				
				DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
				DocumentBuilder builder = factory.newDocumentBuilder();
				Document document = builder.parse(new InputSource(new StringReader(xmlDocument.toString())));

				NodeList testList = document.getElementsByTagName("DISSERTATION_SEARCH");
				for(int i =0; i<testList.getLength(); i++){
					Node test = testList.item(i);
					NodeList properties = test.getChildNodes();
					
					AbstractDto dto = new AbstractDto();
					for(int j = 0; j< properties.getLength(); j++){
						Node property = properties.item(j);
						String nodeName = property.getNodeName();
						if (nodeName.toUpperCase().equals("BINDER_ID")) {
							dto.BINDER_ID = Integer.parseInt(property.getTextContent());
						}else if(nodeName.toUpperCase().equals("WRITER")) {
							dto.WRITER = property.getTextContent();
						}else if(nodeName.toUpperCase().equals("TOPIC_TITLE")) {
							dto.TOPIC_TITLE = property.getTextContent();
						}else if(nodeName.toUpperCase().equals("BINDER_TITLE")) {
							dto.BINDER_TITLE = property.getTextContent();
						}else if(nodeName.toUpperCase().equals("USER_ID")) {
							dto.USER_ID = property.getTextContent();
						}else if(nodeName.toUpperCase().equals("USER_NAME")) {
							dto.USER_NAME = property.getTextContent();
						}else if(nodeName.toUpperCase().equals("USER_CD")) {
							dto.USER_CD = Integer.parseInt(property.getTextContent());
						}
					} 
					abstractDtoList.add(dto);
				}
				Log.e("setAbstractInfo","abstractDtoList size : " + abstractDtoList.size());
				result = true;
				// -------------------------------------------------
			} else {
				Log.e("setAbstractInfo","httpResponseError : " + httpResponse.getStatusLine());
			}
		} catch (SecurityException e) {
			Log.e("setAbstractInfo Error : ","SecurityException : " + e.getMessage());
		} catch (MalformedURLException e) {
			Log.e("setAbstractInfo Error : ","MalformedURLException : " + e.getMessage());
		} catch (IOException e) {
			Log.e("setAbstractInfo Error : ", "IOException : " + e.getMessage());
		} catch (NumberFormatException e) {
			Log.e("setAbstractInfo Error : ","NumberFormatException : " + e.getMessage());
		} catch (NullPointerException e) {
			Log.e("setAbstractInfo Error : ","NullPointerException : " + e.getMessage());
		} catch (Exception e) {
			Log.e("setAbstractInfo Error : ", "Exception : " + e.getMessage());
		}
		return result;
	}
	
	public static final int WRITER = 0;
	public static final int CONTENTS = 1;
	public static final int TOPPIC = 2;
	public static final int BINDERTITLE = 3;
	public static final int PRESENTER = 4;
	
	public boolean setAbstractInfo(int session_id,int searchMode){
		String url = StaticData.DISSERTATION_SEARCH;
		boolean result = false;
		String searchType = "";
		switch(searchMode){
		case WRITER:
			searchType = "writer";
			break;
		case CONTENTS:
			searchType = "contents";
			break;
		case TOPPIC:
			searchType = "topic_title";
			break;
		case BINDERTITLE:
			searchType = "binder_title";
			break;
		case PRESENTER:
			searchType = "presenter";
			break;
		}
		
		if(abstractDtoList != null){
			abstractDtoList.clear();
		}
		
		try{
			HttpClient httpClient = new DefaultHttpClient();
			HttpParams params = httpClient.getParams();
			HttpConnectionParams.setConnectionTimeout(params, 10000);
			HttpConnectionParams.setSoTimeout(params, 10000);

			HttpPost httpPost = new HttpPost(url);
// dissertation_search.jsp?conference_id=15&search_flag=writer&session_id=1
			List<NameValuePair> valuePairs = new ArrayList<NameValuePair>(2);
			valuePairs.add(new BasicNameValuePair("conference_id", StaticData.CONFERENCE_ID+""));
			valuePairs.add(new BasicNameValuePair("search_flag", searchType+""));
			valuePairs.add(new BasicNameValuePair("session_id", session_id+""));
			
			Log.e("setAbstractInfo(,) ","url : "+url);
			Log.e("setAbstractInfo(,) ","conference_id : "+StaticData.CONFERENCE_ID);
			Log.e("setAbstractInfo(,) ","session_id : "+session_id);

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
				Log.e("setAbstractInfo(,)", "----------------------------------------");
				Log.e("setAbstractInfo(,)", "xmlDocument : "+xmlDocument);
				
				DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
				DocumentBuilder builder = factory.newDocumentBuilder();
				Document document = builder.parse(new InputSource(new StringReader(xmlDocument.toString())));

				NodeList testList = document.getElementsByTagName("DISSERTATION_SEARCH");
				for(int i =0; i<testList.getLength(); i++){
					Node test = testList.item(i);
					NodeList properties = test.getChildNodes();
					
					AbstractDto dto = new AbstractDto();
					for(int j = 0; j< properties.getLength(); j++){
						Node property = properties.item(j);
						String nodeName = property.getNodeName();
						if (nodeName.toUpperCase().equals("BINDER_ID")) {
							dto.BINDER_ID = Integer.parseInt(property.getTextContent());
						}else if(nodeName.toUpperCase().equals("WRITER")) {
							dto.WRITER = property.getTextContent();
						}else if(nodeName.toUpperCase().equals("TOPIC_TITLE")) {
							dto.TOPIC_TITLE = property.getTextContent();
						}else if(nodeName.toUpperCase().equals("BINDER_TITLE")) {
							dto.BINDER_TITLE = property.getTextContent();
						}else if(nodeName.toUpperCase().equals("AGEND_ID")) {
							dto.AGEND_ID = Integer.parseInt(property.getTextContent());
						}else if(nodeName.toUpperCase().equals("PRESENTER")) {
							dto.PRESENTER = property.getTextContent();
						}else if(nodeName.toUpperCase().equals("TOPIC_ID")) {
							dto.TOPIC_ID = Integer.parseInt(property.getTextContent());
						}else if(nodeName.toUpperCase().equals("USER_ID")) {
							dto.USER_ID = property.getTextContent();
						}else if(nodeName.toUpperCase().equals("USER_NAME")) {
							dto.USER_NAME = property.getTextContent();
						}
					}
					abstractDtoList.add(dto);
				}
				Log.e("setAbstractInfo(,)","abstractDtoList size : " + abstractDtoList.size());
				result = true;
				// -------------------------------------------------
			} else {
				Log.e("setAbstractInfo(,)","httpResponseError : " + httpResponse.getStatusLine());
			}
		} catch (SecurityException e) {
			Log.e("setAbstractInfo(,) Error : ","SecurityException : " + e.getMessage());
		} catch (MalformedURLException e) {
			Log.e("setAbstractInfo(,) Error : ","MalformedURLException : " + e.getMessage());
		} catch (IOException e) {
			Log.e("setAbstractInfo(,) Error : ", "IOException : " + e.getMessage());
		} catch (NumberFormatException e) {
			Log.e("setAbstractInfo(,) Error : ","NumberFormatException : " + e.getMessage());
		} catch (NullPointerException e) {
			Log.e("setAbstractInfo(,) Error : ","NullPointerException : " + e.getMessage());
		} catch (Exception e) {
			Log.e("setAbstractInfo(,) Error : ", "Exception : " + e.getMessage());
		}
		return result;
	}
	
	public boolean setAbstractInfoSearch(int session_id,int searchMode,String keyword){
		String url = StaticData.DISSERTATION_SEARCH;
		boolean result = false;
		String searchType = "";
		switch(searchMode){
		case WRITER:
			searchType = "writer";
			break;
		case CONTENTS:
			searchType = "contents";
			break;
		case TOPPIC:
			searchType = "topic_title";
			break;
		case BINDERTITLE:
			searchType = "binder_title";
			break;
		case PRESENTER:
			searchType = "presenter";
			break;
		}
		
		if(abstractDtoList != null){
			abstractDtoList.clear();
		}
		
		try{
			HttpClient httpClient = new DefaultHttpClient();
			HttpParams params = httpClient.getParams();
			HttpConnectionParams.setConnectionTimeout(params, 10000);
			HttpConnectionParams.setSoTimeout(params, 10000);

			HttpPost httpPost = new HttpPost(url);

			List<NameValuePair> valuePairs = new ArrayList<NameValuePair>(2);
			valuePairs.add(new BasicNameValuePair("conference_id", StaticData.CONFERENCE_ID+""));
			valuePairs.add(new BasicNameValuePair("search_flag", searchType));
			valuePairs.add(new BasicNameValuePair("keyword", keyword));

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
				Log.e("setAbstractInfo", "----------------------------------------");
				Log.e("setAbstractInfo", "xmlDocument : "+xmlDocument);
				
				DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
				DocumentBuilder builder = factory.newDocumentBuilder();
				Document document = builder.parse(new InputSource(new StringReader(xmlDocument.toString())));

				NodeList testList = document.getElementsByTagName("DISSERTATION_SEARCH");
				for(int i =0; i<testList.getLength(); i++){
					Node test = testList.item(i);
					NodeList properties = test.getChildNodes();
					
					AbstractDto dto = new AbstractDto();
					for(int j = 0; j< properties.getLength(); j++){
						Node property = properties.item(j);
						String nodeName = property.getNodeName();
						if (nodeName.toUpperCase().equals("BINDER_ID")) {
							dto.BINDER_ID = Integer.parseInt(property.getTextContent());
						}else if(nodeName.toUpperCase().equals("WRITER")) {
							dto.WRITER = property.getTextContent();
						}else if(nodeName.toUpperCase().equals("TOPIC_TITLE")) {
							dto.TOPIC_TITLE = property.getTextContent();
						}else if(nodeName.toUpperCase().equals("BINDER_TITLE")) {
							dto.BINDER_TITLE = property.getTextContent();
						}else if(nodeName.toUpperCase().equals("AGEND_ID")) {
							dto.AGEND_ID = Integer.parseInt(property.getTextContent());
						}else if(nodeName.toUpperCase().equals("PRESENTER")) {
							dto.PRESENTER = property.getTextContent();
						}else if(nodeName.toUpperCase().equals("TOPIC_ID")) {
							dto.TOPIC_ID = Integer.parseInt(property.getTextContent());
						}
					}
					abstractDtoList.add(dto);
				}
				Log.e("setAbstractInfo","abstractDtoList size : " + abstractDtoList.size());
				result = true;
				// -------------------------------------------------
			} else {
				Log.e("setAbstractInfo","httpResponseError : " + httpResponse.getStatusLine());
			}
		} catch (SecurityException e) {
			Log.e("setAbstractInfo Error : ","SecurityException : " + e.getMessage());
		} catch (MalformedURLException e) {
			Log.e("setAbstractInfo Error : ","MalformedURLException : " + e.getMessage());
		} catch (IOException e) {
			Log.e("setAbstractInfo Error : ", "IOException : " + e.getMessage());
		} catch (NumberFormatException e) {
			Log.e("setAbstractInfo Error : ","NumberFormatException : " + e.getMessage());
		} catch (NullPointerException e) {
			Log.e("setAbstractInfo Error : ","NullPointerException : " + e.getMessage());
		} catch (Exception e) {
			Log.e("setAbstractInfo Error : ", "Exception : " + e.getMessage());
		}
		return result;
	}
	
	// abstract detail
	public AbstractDetailDto abstractDetailDto;
	public AbstractDetailDto getAbstractDetailDto(){
		return abstractDetailDto;
	}
	public boolean setAbstractDetailInfo(String search_flag, int serial ){
		String url = StaticData.DISSERTATION_SEARCH_CONTENTS;
		boolean result = false;
		
		abstractDetailDto = new AbstractDetailDto();
		
		try{
			HttpClient httpClient = new DefaultHttpClient();
			HttpParams params = httpClient.getParams();
			HttpConnectionParams.setConnectionTimeout(params, 10000);
			HttpConnectionParams.setSoTimeout(params, 10000);

			HttpPost httpPost = new HttpPost(url);

			List<NameValuePair> valuePairs = new ArrayList<NameValuePair>(2);
			valuePairs.add(new BasicNameValuePair("conference_id", StaticData.CONFERENCE_ID+""));
			valuePairs.add(new BasicNameValuePair("search_flag", search_flag));
			valuePairs.add(new BasicNameValuePair("serial", serial+""));
			
			Log.e("", "serial : "+serial);

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
				Log.e("setAbstractDetailInfo", "----------------------------------------");
				Log.e("setAbstractDetailInfo", "xmlDocument : "+xmlDocument);
				
				DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
				DocumentBuilder builder = factory.newDocumentBuilder();
				Document document = builder.parse(new InputSource(new StringReader(xmlDocument.toString())));

				NodeList testList = document.getElementsByTagName("DISSERTATION_SEARCH");
				for(int i =0; i<testList.getLength(); i++){
					Node test = testList.item(i);
					NodeList properties = test.getChildNodes();
					
					for(int j = 0; j< properties.getLength(); j++){
						Node property = properties.item(j);
						String nodeName = property.getNodeName();
						if (nodeName.toUpperCase().equals("WRITER")) {
							abstractDetailDto.WRITER = property.getTextContent();
						}else if(nodeName.toUpperCase().equals("BINDER_TITLE")) {
							abstractDetailDto.BINDER_TITLE = property.getTextContent();
						}else if(nodeName.toUpperCase().equals("TOPIC_TITLE")) {
							abstractDetailDto.TOPIC_TITLE = property.getTextContent();
						}else if(nodeName.toUpperCase().equals("CONTETNS")) {
							abstractDetailDto.CONTETNS = property.getTextContent();
						}else if(nodeName.toUpperCase().equals("PRESENTER")) {
							abstractDetailDto.PRESENTER = property.getTextContent();
						}else if(nodeName.toUpperCase().equals("CONFERENCE_DATE")) {
							abstractDetailDto.CONFERENCE_DATE = property.getTextContent();
						}else if(nodeName.toUpperCase().equals("START_TIME")) {
							abstractDetailDto.START_TIME = property.getTextContent();
						}else if(nodeName.toUpperCase().equals("END_TIME")) {
							abstractDetailDto.END_TIME = property.getTextContent();
						}else if(nodeName.toUpperCase().equals("ATTACHED")) {
							abstractDetailDto.ATTACHED = property.getTextContent();
						}
					}
				}
				Log.e("setAbstractDetailInfo","abstractDetailDto : " + abstractDetailDto);
				result = true;
				// -------------------------------------------------
			} else {
				Log.e("setAbstractDetailInfo","httpResponseError : " + httpResponse.getStatusLine());
			}
		} catch (SecurityException e) {
			Log.e("setAbstractDetailInfo Error : ","SecurityException : " + e.getMessage());
		} catch (MalformedURLException e) {
			Log.e("setAbstractDetailInfo Error : ","MalformedURLException : " + e.getMessage());
		} catch (IOException e) {
			Log.e("setAbstractDetailInfo Error : ", "IOException : " + e.getMessage());
		} catch (NumberFormatException e) {
			Log.e("setAbstractDetailInfo Error : ","NumberFormatException : " + e.getMessage());
		} catch (NullPointerException e) {
			Log.e("setAbstractDetailInfo Error : ","NullPointerException : " + e.getMessage());
		} catch (Exception e) {
			Log.e("setAbstractDetailInfo Error : ", "Exception : " + e.getMessage());
		}
		return result;
	}
	
}
