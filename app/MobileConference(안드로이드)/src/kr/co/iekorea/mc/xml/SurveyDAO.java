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

public class SurveyDAO {
	Activity activity;
	Handler handler;
	String xmlDocument;

	public SurveyDAO(Activity activity) {
		this.activity = activity;
	}
	
	// 설문
	public ArrayList<SurveyDTO> researchList;
	
	public ArrayList<SurveyDTO> getSurveyList(){
		return researchList;
	}
	
	public boolean setSurveyList() {
		String url = StaticData.RESEARCH_URL;
		boolean result = false;
		researchList = new ArrayList<SurveyDTO>();
		try {
			HttpClient httpClient = new DefaultHttpClient();
			HttpParams params = httpClient.getParams();
			HttpConnectionParams.setConnectionTimeout(params, 10000);
			HttpConnectionParams.setSoTimeout(params, 10000);

			HttpPost httpPost = new HttpPost(url);

			List<NameValuePair> valuePairs = new ArrayList<NameValuePair>(2);
			valuePairs.add(new BasicNameValuePair("user_cd", StaticData.USER_CD+ ""));
			valuePairs.add(new BasicNameValuePair("conference_id", StaticData.CONFERENCE_ID+ ""));

			CustomLog.e("setSurveyList", "conference_id :" + StaticData.CONFERENCE_ID);
			CustomLog.e("setSurveyList", "url :" + url);
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
				CustomLog.e("setSurveyList", "xmlDocument : " + xmlDocument);

				DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
				DocumentBuilder builder = factory.newDocumentBuilder();
				Document document = builder.parse(new InputSource(new StringReader(xmlDocument.toString())));
				NodeList testList = document.getElementsByTagName("RESEARCH_LIST");
				for (int i = 0; i < testList.getLength(); i++) {
					Node test = testList.item(i);
					NodeList properties = test.getChildNodes();
					SurveyDTO dto = new SurveyDTO();
					for (int j = 0; j < properties.getLength(); j++) {
						Node property = properties.item(j);
						String nodeName = property.getNodeName().trim();
						if (nodeName.toUpperCase().equals("RESEARCH_ID")) {
							dto.RESEARCH_ID = Integer.parseInt(property.getTextContent());
						} else if (nodeName.toUpperCase().equals("USER_CD")) {
							dto.USER_CD = Integer.parseInt(property.getTextContent());
						} else if (nodeName.toUpperCase().equals("USER_NAME")) {
							dto.USER_NAME = property.getTextContent();
						} else if (nodeName.toUpperCase().equals("RESEARCH_NUM")) {
							dto.RESEARCH_NUM = Integer.parseInt(property.getTextContent());
						} else if (nodeName.toUpperCase().equals("TITLE")) {
							dto.TITLE = property.getTextContent();
						} else if (nodeName.toUpperCase().equals("STAT")) {
							dto.STAT = property.getTextContent();
						} else if (nodeName.toUpperCase().equals("REG_DATE")) {
							dto.REG_DATE = property.getTextContent();
						}
					}
					
					researchList.add(dto);
				}
				result = true;
				// -------------------------------------------------
			} else {
				CustomLog.e("setSurveyList","httpResponseError : " + httpResponse.getStatusLine());
			}
		} catch (SecurityException e) {
			CustomLog.e("setSurveyList Error : ","SecurityException : " + e.getMessage());
		} catch (MalformedURLException e) {
			CustomLog.e("setSurveyList Error : ","MalformedURLException : " + e.getMessage());
		} catch (IOException e) {
			CustomLog.e("setSurveyList Error : ", "IOException : " + e.getMessage());
		} catch (NumberFormatException e) {
			CustomLog.e("setSurveyList Error : ","NumberFormatException : " + e.getMessage());
		} catch (NullPointerException e) {
			CustomLog.e("setSurveyList Error : ","NullPointerException : " + e.getMessage());
		} catch (Exception e) {
			CustomLog.e("setSurveyList Error : ", "Exception : " + e.getMessage());
		}
		return result;
	}
	
	public boolean addSurveyItem(String title) {
		String url = StaticData.RESEARCH_ADD_URL;
		boolean result = false;
		try {
			HttpClient httpClient = new DefaultHttpClient();
			HttpParams params = httpClient.getParams();
			HttpConnectionParams.setConnectionTimeout(params, 10000);
			HttpConnectionParams.setSoTimeout(params, 10000);

			HttpPost httpPost = new HttpPost(url);

			List<NameValuePair> valuePairs = new ArrayList<NameValuePair>(2);
			valuePairs.add(new BasicNameValuePair("conference_id", StaticData.CONFERENCE_ID+ ""));
			valuePairs.add(new BasicNameValuePair("user_cd", StaticData.USER_CD+ ""));
			valuePairs.add(new BasicNameValuePair("title", title	+ ""));

			CustomLog.e("addSurveyItem", "conference_id :" + StaticData.CONFERENCE_ID);
			CustomLog.e("addSurveyItem", "url :" + url);
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
				CustomLog.e("addSurveyItem", "xmlDocument : " + xmlDocument);

				DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
				DocumentBuilder builder = factory.newDocumentBuilder();
				Document document = builder.parse(new InputSource(new StringReader(xmlDocument.toString())));
				NodeList testList = document.getElementsByTagName("ROOT");
				for (int i = 0; i < testList.getLength(); i++) {
					Node test = testList.item(i);
					NodeList properties = test.getChildNodes();
					for (int j = 0; j < properties.getLength(); j++) {
						Node property = properties.item(j);
						String nodeName = property.getNodeName().trim();
						if (nodeName.toUpperCase().equals("RESEARCH_NUMBER")) {
							if(Integer.parseInt(property.getTextContent()) != 0){
								result = true;
							}
						}
					}
				}
				// -------------------------------------------------
			} else {
				CustomLog.e("addSurveyItem","httpResponseError : " + httpResponse.getStatusLine());
			}
		} catch (SecurityException e) {
			CustomLog.e("addSurveyItem Error : ","SecurityException : " + e.getMessage());
		} catch (MalformedURLException e) {
			CustomLog.e("addSurveyItem Error : ","MalformedURLException : " + e.getMessage());
		} catch (IOException e) {
			CustomLog.e("addSurveyItem Error : ", "IOException : " + e.getMessage());
		} catch (NumberFormatException e) {
			CustomLog.e("addSurveyItem Error : ","NumberFormatException : " + e.getMessage());
		} catch (NullPointerException e) {
			CustomLog.e("addSurveyItem Error : ","NullPointerException : " + e.getMessage());
		} catch (Exception e) {
			CustomLog.e("addSurveyItem Error : ", "Exception : " + e.getMessage());
		}
		return result;
	}
	
	// modify survey item
	
	public boolean modifySurveyItem(int research_id, String title) {
		String url = StaticData.RESEARCH_MODIFY_URL;
		boolean result = false;
		try {
			HttpClient httpClient = new DefaultHttpClient();
			HttpParams params = httpClient.getParams();
			HttpConnectionParams.setConnectionTimeout(params, 10000);
			HttpConnectionParams.setSoTimeout(params, 10000);

			HttpPost httpPost = new HttpPost(url);

			List<NameValuePair> valuePairs = new ArrayList<NameValuePair>(2);
			valuePairs.add(new BasicNameValuePair("research_id", research_id+ ""));
			valuePairs.add(new BasicNameValuePair("title", title	+ ""));

			CustomLog.e("modifySurveyItem", "research_id :" + research_id);
			CustomLog.e("modifySurveyItem", "url :" + url);
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
				CustomLog.e("modifySurveyItem", "xmlDocument : " + xmlDocument);

				DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
				DocumentBuilder builder = factory.newDocumentBuilder();
				Document document = builder.parse(new InputSource(new StringReader(xmlDocument.toString())));
				NodeList testList = document.getElementsByTagName("ROOT");
				for (int i = 0; i < testList.getLength(); i++) {
					Node test = testList.item(i);
					NodeList properties = test.getChildNodes();
					for (int j = 0; j < properties.getLength(); j++) {
						Node property = properties.item(j);
						String nodeName = property.getNodeName().trim();
						if (nodeName.toUpperCase().equals("RESULT")) {
							if(property.getTextContent().equals("success")){
								result = true;
							}
						}
					}
				}
				// -------------------------------------------------
			} else {
				CustomLog.e("modifySurveyItem","httpResponseError : " + httpResponse.getStatusLine());
			}
		} catch (SecurityException e) {
			CustomLog.e("modifySurveyItem Error : ","SecurityException : " + e.getMessage());
		} catch (MalformedURLException e) {
			CustomLog.e("modifySurveyItem Error : ","MalformedURLException : " + e.getMessage());
		} catch (IOException e) {
			CustomLog.e("modifySurveyItem Error : ", "IOException : " + e.getMessage());
		} catch (NumberFormatException e) {
			CustomLog.e("modifySurveyItem Error : ","NumberFormatException : " + e.getMessage());
		} catch (NullPointerException e) {
			CustomLog.e("modifySurveyItem Error : ","NullPointerException : " + e.getMessage());
		} catch (Exception e) {
			CustomLog.e("modifySurveyItem Error : ", "Exception : " + e.getMessage());
		}
		return result;
	}
	
	// question list
	public SurveyQuestionDTO surveyQuestionList;
	
	public SurveyQuestionDTO getQuestionList(){
		return surveyQuestionList;
	}
	public boolean setQuestionList(int research_id){
		String url = StaticData.RESEARCH_LIST_URL;
		boolean result = false;
		surveyQuestionList = new SurveyQuestionDTO();
		try {
			HttpClient httpClient = new DefaultHttpClient();
			HttpParams params = httpClient.getParams();
			HttpConnectionParams.setConnectionTimeout(params, 10000);
			HttpConnectionParams.setSoTimeout(params, 10000);

			HttpPost httpPost = new HttpPost(url);

			List<NameValuePair> valuePairs = new ArrayList<NameValuePair>(2);
			valuePairs.add(new BasicNameValuePair("research_id", research_id+ ""));
			valuePairs.add(new BasicNameValuePair("user_cd", StaticData.USER_CD+ ""));

			CustomLog.e("setQuestionList", "research_id :" + research_id);
			CustomLog.e("setQuestionList", "user_cd :" + StaticData.USER_CD);
			CustomLog.e("setQuestionList", "url :" + url);
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
				CustomLog.e("setQuestionList", "xmlDocument : " + xmlDocument);

				DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
				DocumentBuilder builder = factory.newDocumentBuilder();
				Document document = builder.parse(new InputSource(new StringReader(xmlDocument.toString())));
				NodeList testList = document.getElementsByTagName("RESEARCH");
				for (int i = 0; i < testList.getLength(); i++) {
					Node test = testList.item(i);
					NodeList properties = test.getChildNodes();
					for (int j = 0; j < properties.getLength(); j++) {
						Node property = properties.item(j);
						String nodeName = property.getNodeName().trim();
						if (nodeName.toUpperCase().equals("RESEARCH_NUMBER")) {
							surveyQuestionList.RESEARCH_NUMBER = Integer.parseInt(property.getTextContent());
						} else if (nodeName.toUpperCase().equals("RESEARCH_TITLE")) {
							surveyQuestionList.RESEARCH_TITLE = property.getTextContent();
						} else if (nodeName.toUpperCase().equals("RESEARCH_STAT")) {
							surveyQuestionList.RESEARCH_STAT = property.getTextContent();
						} else if (nodeName.toUpperCase().equals("QUESTION_LIST")) {
							SurveyQuestionSubDTO dto_sub = new SurveyQuestionSubDTO();
							NodeList property_sub = property.getChildNodes();
							for(int k=0; k< property_sub.getLength(); k++){
								Node property_sub2 = property_sub.item(k);
								String nodeName_sub2 = property_sub2.getNodeName().trim();
								if(nodeName_sub2.toUpperCase().equals("QUESTION_ID")){
									if(property_sub2.getTextContent().equals("")){
									}else{
										dto_sub.QUESTION_ID = Integer.parseInt(property_sub2.getTextContent());
									}
								} else if(nodeName_sub2.toUpperCase().equals("QUESTION_NUM")){
									if(property_sub2.getTextContent().equals("")){
									}else{
										dto_sub.QUESTION_NUM = Integer.parseInt(property_sub2.getTextContent());
									}
								} else if(nodeName_sub2.toUpperCase().equals("QUESTION_TITLE")){
									dto_sub.QUESTION_TITLE = property_sub2.getTextContent();
								}
							}
							
							if(dto_sub.QUESTION_ID != 0){
								surveyQuestionList.QUESTION_LIST.add(dto_sub);
							}
						}
					}
				}
				result = true;
				// -------------------------------------------------
			} else {
				CustomLog.e("setQuestionList","httpResponseError : " + httpResponse.getStatusLine());
			}
		} catch (SecurityException e) {
			CustomLog.e("setQuestionList Error : ","SecurityException : " + e.getMessage());
		} catch (MalformedURLException e) {
			CustomLog.e("setQuestionList Error : ","MalformedURLException : " + e.getMessage());
		} catch (IOException e) {
			CustomLog.e("setQuestionList Error : ", "IOException : " + e.getMessage());
		} catch (NumberFormatException e) {
			CustomLog.e("setQuestionList Error : ","NumberFormatException : " + e.getMessage());
		} catch (NullPointerException e) {
			CustomLog.e("setQuestionList Error : ","NullPointerException : " + e.getMessage());
		} catch (Exception e) {
			CustomLog.e("setQuestionList Error : ", "Exception : " + e.getMessage());
		}
		return result;
	}
	
	public boolean deleteSurveyItem(int research_id) {
		String url = StaticData.RESEARCH_DELETE_URL;
		boolean result = false;
		try {
			HttpClient httpClient = new DefaultHttpClient();
			HttpParams params = httpClient.getParams();
			HttpConnectionParams.setConnectionTimeout(params, 10000);
			HttpConnectionParams.setSoTimeout(params, 10000);

			HttpPost httpPost = new HttpPost(url);

			List<NameValuePair> valuePairs = new ArrayList<NameValuePair>(2);
			valuePairs.add(new BasicNameValuePair("research_id", research_id+ ""));
//			valuePairs.add(new BasicNameValuePair("title", title	+ ""));

			CustomLog.e("deleteSurveyItem", "research_id :" + research_id);
			CustomLog.e("deleteSurveyItem", "url :" + url);
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
				CustomLog.e("deleteSurveyItem", "xmlDocument : " + xmlDocument);

				DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
				DocumentBuilder builder = factory.newDocumentBuilder();
				Document document = builder.parse(new InputSource(new StringReader(xmlDocument.toString())));
				NodeList testList = document.getElementsByTagName("ROOT");
				for (int i = 0; i < testList.getLength(); i++) {
					Node test = testList.item(i);
					NodeList properties = test.getChildNodes();
					for (int j = 0; j < properties.getLength(); j++) {
						Node property = properties.item(j);
						String nodeName = property.getNodeName().trim();
						if (nodeName.toUpperCase().equals("RESULT")) {
							if(property.getTextContent().equals("success")){
								result = true;
							}
						}
					}
				}
				// -------------------------------------------------
			} else {
				CustomLog.e("deleteSurveyItem","httpResponseError : " + httpResponse.getStatusLine());
			}
		} catch (SecurityException e) {
			CustomLog.e("deleteSurveyItem Error : ","SecurityException : " + e.getMessage());
		} catch (MalformedURLException e) {
			CustomLog.e("deleteSurveyItem Error : ","MalformedURLException : " + e.getMessage());
		} catch (IOException e) {
			CustomLog.e("deleteSurveyItem Error : ", "IOException : " + e.getMessage());
		} catch (NumberFormatException e) {
			CustomLog.e("deleteSurveyItem Error : ","NumberFormatException : " + e.getMessage());
		} catch (NullPointerException e) {
			CustomLog.e("deleteSurveyItem Error : ","NullPointerException : " + e.getMessage());
		} catch (Exception e) {
			CustomLog.e("deleteSurveyItem Error : ", "Exception : " + e.getMessage());
		}
		return result;
	}

	
	public boolean setInsertQuestion(int research_id,String q_title,String q_1,
			String q_2,String q_3,String q_4,String q_5){
		String url = StaticData.RESEARCH_Q_URL;
		boolean result = false;
		try {
			HttpClient httpClient = new DefaultHttpClient();
			HttpParams params = httpClient.getParams();
			HttpConnectionParams.setConnectionTimeout(params, 10000);
			HttpConnectionParams.setSoTimeout(params, 10000);

			HttpPost httpPost = new HttpPost(url);

			List<NameValuePair> valuePairs = new ArrayList<NameValuePair>(2);
			valuePairs.add(new BasicNameValuePair("research_id", research_id+ ""));
			valuePairs.add(new BasicNameValuePair("q_title", q_title+ ""));
			valuePairs.add(new BasicNameValuePair("q_answer", q_1+ "|_|"
					+q_2+"|_|"+q_3+"|_|"+q_4+"|_|"+q_5));

			CustomLog.e("addSurveyItem", "conference_id :" + StaticData.CONFERENCE_ID);
			CustomLog.e("addSurveyItem", "url :" + url);
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
				CustomLog.e("addSurveyItem", "xmlDocument : " + xmlDocument);

				DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
				DocumentBuilder builder = factory.newDocumentBuilder();
				Document document = builder.parse(new InputSource(new StringReader(xmlDocument.toString())));
				NodeList testList = document.getElementsByTagName("ROOT");
				for (int i = 0; i < testList.getLength(); i++) {
					Node test = testList.item(i);
					NodeList properties = test.getChildNodes();
					for (int j = 0; j < properties.getLength(); j++) {
						Node property = properties.item(j);
						String nodeName = property.getNodeName().trim();
						if (nodeName.toUpperCase().equals("RESULT")) {
							if(property.getTextContent().equals("success")){
								result = true;
							}
						}
					}
				}
				// -------------------------------------------------
			} else {
				CustomLog.e("addSurveyItem","httpResponseError : " + httpResponse.getStatusLine());
			}
		} catch (SecurityException e) {
			CustomLog.e("addSurveyItem Error : ","SecurityException : " + e.getMessage());
		} catch (MalformedURLException e) {
			CustomLog.e("addSurveyItem Error : ","MalformedURLException : " + e.getMessage());
		} catch (IOException e) {
			CustomLog.e("addSurveyItem Error : ", "IOException : " + e.getMessage());
		} catch (NumberFormatException e) {
			CustomLog.e("addSurveyItem Error : ","NumberFormatException : " + e.getMessage());
		} catch (NullPointerException e) {
			CustomLog.e("addSurveyItem Error : ","NullPointerException : " + e.getMessage());
		} catch (Exception e) {
			CustomLog.e("addSurveyItem Error : ", "Exception : " + e.getMessage());
		}
		return result;
	}
	
	public boolean setUpdateQuestion(int research_id,int research_q_id,String q_title,String q_1,
			String q_2,String q_3,String q_4,String q_5){
		String url = StaticData.UPDATE_Q_URL;
		boolean result = false;
		try {
			HttpClient httpClient = new DefaultHttpClient();
			HttpParams params = httpClient.getParams();
			HttpConnectionParams.setConnectionTimeout(params, 10000);
			HttpConnectionParams.setSoTimeout(params, 10000);

			HttpPost httpPost = new HttpPost(url);

			List<NameValuePair> valuePairs = new ArrayList<NameValuePair>(2);
			valuePairs.add(new BasicNameValuePair("research_id", research_id+ ""));
			valuePairs.add(new BasicNameValuePair("research_q_id", research_q_id+ ""));
			valuePairs.add(new BasicNameValuePair("q_title", q_title+ ""));
			valuePairs.add(new BasicNameValuePair("q_answer", q_1+ "|_|"
					+q_2+"|_|"+q_3+"|_|"+q_4+"|_|"+q_5));

			CustomLog.e("addSurveyItem", "conference_id :" + StaticData.CONFERENCE_ID);
			CustomLog.e("addSurveyItem", "url :" + url);
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
				CustomLog.e("addSurveyItem", "xmlDocument : " + xmlDocument);

				DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
				DocumentBuilder builder = factory.newDocumentBuilder();
				Document document = builder.parse(new InputSource(new StringReader(xmlDocument.toString())));
				NodeList testList = document.getElementsByTagName("ROOT");
				for (int i = 0; i < testList.getLength(); i++) {
					Node test = testList.item(i);
					NodeList properties = test.getChildNodes();
					for (int j = 0; j < properties.getLength(); j++) {
						Node property = properties.item(j);
						String nodeName = property.getNodeName().trim();
						if (nodeName.toUpperCase().equals("RESULT")) {
							if(property.getTextContent().equals("success")){
								result = true;
							}
						}
					}
				}
				// -------------------------------------------------
			} else {
				CustomLog.e("addSurveyItem","httpResponseError : " + httpResponse.getStatusLine());
			}
		} catch (SecurityException e) {
			CustomLog.e("addSurveyItem Error : ","SecurityException : " + e.getMessage());
		} catch (MalformedURLException e) {
			CustomLog.e("addSurveyItem Error : ","MalformedURLException : " + e.getMessage());
		} catch (IOException e) {
			CustomLog.e("addSurveyItem Error : ", "IOException : " + e.getMessage());
		} catch (NumberFormatException e) {
			CustomLog.e("addSurveyItem Error : ","NumberFormatException : " + e.getMessage());
		} catch (NullPointerException e) {
			CustomLog.e("addSurveyItem Error : ","NullPointerException : " + e.getMessage());
		} catch (Exception e) {
			CustomLog.e("addSurveyItem Error : ", "Exception : " + e.getMessage());
		}
		return result;
	}
	
	
	// answer list
	public QuestionDTO questionDto;
	
	public QuestionDTO getQuestionInfo(){
		return questionDto;
	}
	
	public boolean setQuestionInfo(int research_id,int research_q_id){
		String url = StaticData.ANSWER_URL;
		boolean result = false;
		questionDto = new QuestionDTO();
		try {
			HttpClient httpClient = new DefaultHttpClient();
			HttpParams params = httpClient.getParams();
			HttpConnectionParams.setConnectionTimeout(params, 10000);
			HttpConnectionParams.setSoTimeout(params, 10000);

			HttpPost httpPost = new HttpPost(url);

			List<NameValuePair> valuePairs = new ArrayList<NameValuePair>(2);
			valuePairs.add(new BasicNameValuePair("research_id", research_id+ ""));
			valuePairs.add(new BasicNameValuePair("research_q_id", research_q_id+ ""));

			CustomLog.e("setQuestionInfo", "research_id :" + research_id);
			CustomLog.e("setQuestionInfo", "research_q_id :" + research_q_id);
			CustomLog.e("setQuestionInfo", "user_cd :" + StaticData.USER_CD);
			CustomLog.e("setQuestionInfo", "url :" + url);
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
				CustomLog.e("setQuestionInfo", "xmlDocument : " + xmlDocument);

				DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
				DocumentBuilder builder = factory.newDocumentBuilder();
				Document document = builder.parse(new InputSource(new StringReader(xmlDocument.toString())));
				NodeList testList = document.getElementsByTagName("ROOT");
				for (int i = 0; i < testList.getLength(); i++) {
					Node test = testList.item(i);
					NodeList properties = test.getChildNodes();
					for (int j = 0; j < properties.getLength(); j++) {
						Node property = properties.item(j);
						String nodeName = property.getNodeName().trim();
						if (nodeName.toUpperCase().equals("RESEARCH_ID")) {
							questionDto.RESEARCH_ID = Integer.parseInt(property.getTextContent());
						} else if (nodeName.toUpperCase().equals("RESEARCH_TITLE")) {
							questionDto.RESEARCH_TITLE = property.getTextContent();
						} else if (nodeName.toUpperCase().equals("RESEARCH_STAT")) {
							questionDto.RESEARCH_STAT = property.getTextContent();
						} else if (nodeName.toUpperCase().equals("RESEARCH_QUESTION")) {
							NodeList property_sub = property.getChildNodes();
							for(int k=0; k< property_sub.getLength(); k++){
								Node property_sub2 = property_sub.item(k);
								String nodeName_sub2 = property_sub2.getNodeName().trim();
								if(nodeName_sub2.toUpperCase().equals("QUESTION_ID")){
									questionDto.QUESTION_ID = Integer.parseInt(property_sub2.getTextContent());
								}else if(nodeName_sub2.toUpperCase().equals("QUESTION_TITLE")){
									questionDto.QUESTION_TITLE = property_sub2.getTextContent();
								}else if(nodeName_sub2.toUpperCase().equals("QUESTION_NUM")){
									questionDto.QUESTION_NUM = Integer.parseInt(property_sub2.getTextContent());
								}else if(nodeName_sub2.toUpperCase().equals("RESEARCH_ANSWER")){
									RESEARCH_ANSWER_DTO dto_sub = new RESEARCH_ANSWER_DTO();
									NodeList property_sub_3 = property_sub2.getChildNodes();
									for(int p=0; p<property_sub_3.getLength(); p++){
										Node property_sub4 = property_sub_3.item(p);
										String nodeName_sub3 = property_sub4.getNodeName().trim();
										if(nodeName_sub3.toUpperCase().equals("ANSWER_NUM")){
											dto_sub.ANSWER_NUM = Integer.parseInt(property_sub4.getTextContent());
										} else if(nodeName_sub3.toUpperCase().equals("ANSWER_TITLE")){
											dto_sub.ANSWER_TITLE = property_sub4.getTextContent();
										}										
									}
									questionDto.RESEARCH_ANSWER.add(dto_sub);									
								}
							}
						}
					}
				}
				result = true;
				// -------------------------------------------------
			} else {
				CustomLog.e("setQuestionInfo","httpResponseError : " + httpResponse.getStatusLine());
			}
		} catch (SecurityException e) {
			CustomLog.e("setQuestionInfo Error : ","SecurityException : " + e.getMessage());
		} catch (MalformedURLException e) {
			CustomLog.e("setQuestionInfo Error : ","MalformedURLException : " + e.getMessage());
		} catch (IOException e) {
			CustomLog.e("setQuestionInfo Error : ", "IOException : " + e.getMessage());
		} catch (NumberFormatException e) {
			CustomLog.e("setQuestionInfo Error : ","NumberFormatException : " + e.getMessage());
		} catch (NullPointerException e) {
			CustomLog.e("setQuestionInfo Error : ","NullPointerException : " + e.getMessage());
		} catch (Exception e) {
			CustomLog.e("setQuestionInfo Error : ", "Exception : " + e.getMessage());
		}
		return result;
	}
	
	
	
	public SurveyResultDTO mSurveyResultDTO = new SurveyResultDTO();
	public SurveyResultDTO getSurveyResult(){
		
		return mSurveyResultDTO;
	}
	
	public boolean setSurveyResult(int research_id){
		String url = StaticData.RESEARCH_RESULT_URL;
		boolean result = false;
		mSurveyResultDTO = new SurveyResultDTO();
		try {
			HttpClient httpClient = new DefaultHttpClient();
			HttpParams params = httpClient.getParams();
			HttpConnectionParams.setConnectionTimeout(params, 10000);
			HttpConnectionParams.setSoTimeout(params, 10000);

			HttpPost httpPost = new HttpPost(url);

			List<NameValuePair> valuePairs = new ArrayList<NameValuePair>(2);
			valuePairs.add(new BasicNameValuePair("research_id", research_id+ ""));

			CustomLog.e("setSurveyResult", "research_id :" + research_id);
			CustomLog.e("setSurveyResult", "url :" + url);
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
				CustomLog.e("setSurveyResult", "xmlDocument : " + xmlDocument);

				DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
				DocumentBuilder builder = factory.newDocumentBuilder();
				Document document = builder.parse(new InputSource(new StringReader(xmlDocument.toString())));
				NodeList testList = document.getElementsByTagName("RESEARCH_RESULT");
				for (int i = 0; i < testList.getLength(); i++) {
					Node test = testList.item(i);
					NodeList properties = test.getChildNodes();
					for (int j = 0; j < properties.getLength(); j++) {
						Node property = properties.item(j);
						String nodeName = property.getNodeName().trim();
						if (nodeName.toUpperCase().equals("RESEARCH_TITLE")) {
							mSurveyResultDTO.RESEARCH_TITLE = property.getTextContent();
						} else if (nodeName.toUpperCase().equals("RESEARCH_MAKER")) {
							mSurveyResultDTO.RESEARCH_MAKER = property.getTextContent();
						} else if (nodeName.toUpperCase().equals("RESEARCH_TOTAL_COUNT")) {
							mSurveyResultDTO.RESEARCH_TOTAL_COUNT = Integer.parseInt(property.getTextContent());
						} else if (nodeName.toUpperCase().equals("RESEARCH_QUESTION")) {
							NodeList property_sub = property.getChildNodes();
							RESEARCH_QUESTION_DTO dto_1 = new RESEARCH_QUESTION_DTO();
							for(int k=0; k< property_sub.getLength(); k++){
								Node property_sub2 = property_sub.item(k);
								String nodeName_sub2 = property_sub2.getNodeName().trim();
								if(nodeName_sub2.toUpperCase().equals("QUESTION_ID")){
									dto_1.QUESTION_ID = Integer.parseInt(property_sub2.getTextContent());
								}else if(nodeName_sub2.toUpperCase().equals("QUESTION_TITLE")){
									dto_1.QUESTION_TITLE = property_sub2.getTextContent();
								}else if(nodeName_sub2.toUpperCase().equals("QUESTION_NUM")){
									dto_1.QUESTION_NUM = Integer.parseInt(property_sub2.getTextContent());
								}else if(nodeName_sub2.toUpperCase().equals("RESEARCH_ANSWER")){
									RESEARCH_ANSWER_DTO dto_sub = new RESEARCH_ANSWER_DTO();
									NodeList property_sub_3 = property_sub2.getChildNodes();
									for(int p=0; p<property_sub_3.getLength(); p++){
										Node property_sub4 = property_sub_3.item(p);
										String nodeName_sub3 = property_sub4.getNodeName().trim();
										if(nodeName_sub3.toUpperCase().equals("ANSWER_NUM")){
											dto_sub.ANSWER_NUM = Integer.parseInt(property_sub4.getTextContent());
										} else if(nodeName_sub3.toUpperCase().equals("ANSWER_TITLE")){
											dto_sub.ANSWER_TITLE = property_sub4.getTextContent();
										} else if(nodeName_sub3.toUpperCase().equals("ANSWER_RATE")){
											dto_sub.ANSWER_RATE = property_sub4.getTextContent();
										}										
									}
									dto_1.RESEARCH_ANSWER.add(dto_sub);									
								}
							}
							mSurveyResultDTO.RESEARCH_QUESTION.add(dto_1);
						}
					}
				}
				result = true;
				// -------------------------------------------------
				CustomLog.e("setSurveyResult","mSurveyResultDTO.RESEARCH_QUESTION.size() : " + mSurveyResultDTO.RESEARCH_QUESTION.size());
			} else {
				CustomLog.e("setSurveyResult","httpResponseError : " + httpResponse.getStatusLine());
			}
		} catch (SecurityException e) {
			CustomLog.e("setSurveyResult Error : ","SecurityException : " + e.getMessage());
		} catch (MalformedURLException e) {
			CustomLog.e("setSurveyResult Error : ","MalformedURLException : " + e.getMessage());
		} catch (IOException e) {
			CustomLog.e("setSurveyResult Error : ", "IOException : " + e.getMessage());
		} catch (NumberFormatException e) {
			CustomLog.e("setSurveyResult Error : ","NumberFormatException : " + e.getMessage());
		} catch (NullPointerException e) {
			CustomLog.e("setSurveyResult Error : ","NullPointerException : " + e.getMessage());
		} catch (Exception e) {
			CustomLog.e("setSurveyResult Error : ", "Exception : " + e.getMessage());
		}
		return result;
	}
	
	public boolean startSurvey(int research_id) {
		String url = StaticData.RESEARCH_START_URL;
		boolean result = false;
		try {
			HttpClient httpClient = new DefaultHttpClient();
			HttpParams params = httpClient.getParams();
			HttpConnectionParams.setConnectionTimeout(params, 10000);
			HttpConnectionParams.setSoTimeout(params, 10000);

			HttpPost httpPost = new HttpPost(url);

			List<NameValuePair> valuePairs = new ArrayList<NameValuePair>(2);
			valuePairs.add(new BasicNameValuePair("research_id", research_id+ ""));

			CustomLog.e("modifySurveyItem", "research_id :" + research_id);
			CustomLog.e("modifySurveyItem", "url :" + url);
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
				CustomLog.e("modifySurveyItem", "xmlDocument : " + xmlDocument);

				DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
				DocumentBuilder builder = factory.newDocumentBuilder();
				Document document = builder.parse(new InputSource(new StringReader(xmlDocument.toString())));
				NodeList testList = document.getElementsByTagName("ROOT");
				for (int i = 0; i < testList.getLength(); i++) {
					Node test = testList.item(i);
					NodeList properties = test.getChildNodes();
					for (int j = 0; j < properties.getLength(); j++) {
						Node property = properties.item(j);
						String nodeName = property.getNodeName().trim();
						if (nodeName.toUpperCase().equals("RESULT")) {
							if(property.getTextContent().equals("success")){
								result = true;
							}
						}
					}
				}
				// -------------------------------------------------
			} else {
				CustomLog.e("modifySurveyItem","httpResponseError : " + httpResponse.getStatusLine());
			}
		} catch (SecurityException e) {
			CustomLog.e("modifySurveyItem Error : ","SecurityException : " + e.getMessage());
		} catch (MalformedURLException e) {
			CustomLog.e("modifySurveyItem Error : ","MalformedURLException : " + e.getMessage());
		} catch (IOException e) {
			CustomLog.e("modifySurveyItem Error : ", "IOException : " + e.getMessage());
		} catch (NumberFormatException e) {
			CustomLog.e("modifySurveyItem Error : ","NumberFormatException : " + e.getMessage());
		} catch (NullPointerException e) {
			CustomLog.e("modifySurveyItem Error : ","NullPointerException : " + e.getMessage());
		} catch (Exception e) {
			CustomLog.e("modifySurveyItem Error : ", "Exception : " + e.getMessage());
		}
		return result;
	}

	public boolean deleteQuestion(int research_id, int research_q_id) {
		String url = StaticData.RESEARCH_Q_DELETE_URL;
		boolean result = false;
		try {
			HttpClient httpClient = new DefaultHttpClient();
			HttpParams params = httpClient.getParams();
			HttpConnectionParams.setConnectionTimeout(params, 10000);
			HttpConnectionParams.setSoTimeout(params, 10000);

			HttpPost httpPost = new HttpPost(url);

			List<NameValuePair> valuePairs = new ArrayList<NameValuePair>(2);
			valuePairs.add(new BasicNameValuePair("research_id", research_id+ ""));
			valuePairs.add(new BasicNameValuePair("research_q_id", research_q_id+ ""));

			CustomLog.e("modifySurveyItem", "research_id :" + research_id);
			CustomLog.e("modifySurveyItem", "url :" + url);
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
				CustomLog.e("modifySurveyItem", "xmlDocument : " + xmlDocument);

				DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
				DocumentBuilder builder = factory.newDocumentBuilder();
				Document document = builder.parse(new InputSource(new StringReader(xmlDocument.toString())));
				NodeList testList = document.getElementsByTagName("ROOT");
				for (int i = 0; i < testList.getLength(); i++) {
					Node test = testList.item(i);
					NodeList properties = test.getChildNodes();
					for (int j = 0; j < properties.getLength(); j++) {
						Node property = properties.item(j);
						String nodeName = property.getNodeName().trim();
						if (nodeName.toUpperCase().equals("RESULT")) {
							if(property.getTextContent().equals("success")){
								result = true;
							}
						}
					}
				}
				// -------------------------------------------------
			} else {
				CustomLog.e("modifySurveyItem","httpResponseError : " + httpResponse.getStatusLine());
			}
		} catch (SecurityException e) {
			CustomLog.e("modifySurveyItem Error : ","SecurityException : " + e.getMessage());
		} catch (MalformedURLException e) {
			CustomLog.e("modifySurveyItem Error : ","MalformedURLException : " + e.getMessage());
		} catch (IOException e) {
			CustomLog.e("modifySurveyItem Error : ", "IOException : " + e.getMessage());
		} catch (NumberFormatException e) {
			CustomLog.e("modifySurveyItem Error : ","NumberFormatException : " + e.getMessage());
		} catch (NullPointerException e) {
			CustomLog.e("modifySurveyItem Error : ","NullPointerException : " + e.getMessage());
		} catch (Exception e) {
			CustomLog.e("modifySurveyItem Error : ", "Exception : " + e.getMessage());
		}
		return result;
	}

	public boolean endSurvey(int research_id) {
		String url = StaticData.RESEARCH_COMPLETE_URL;
		boolean result = false;
		try {
			HttpClient httpClient = new DefaultHttpClient();
			HttpParams params = httpClient.getParams();
			HttpConnectionParams.setConnectionTimeout(params, 10000);
			HttpConnectionParams.setSoTimeout(params, 10000);

			HttpPost httpPost = new HttpPost(url);

			List<NameValuePair> valuePairs = new ArrayList<NameValuePair>(2);
			valuePairs.add(new BasicNameValuePair("research_id", research_id+ ""));

			CustomLog.e("modifySurveyItem", "research_id :" + research_id);
			CustomLog.e("modifySurveyItem", "url :" + url);
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
				CustomLog.e("modifySurveyItem", "xmlDocument : " + xmlDocument);

				DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
				DocumentBuilder builder = factory.newDocumentBuilder();
				Document document = builder.parse(new InputSource(new StringReader(xmlDocument.toString())));
				NodeList testList = document.getElementsByTagName("ROOT");
				for (int i = 0; i < testList.getLength(); i++) {
					Node test = testList.item(i);
					NodeList properties = test.getChildNodes();
					for (int j = 0; j < properties.getLength(); j++) {
						Node property = properties.item(j);
						String nodeName = property.getNodeName().trim();
						if (nodeName.toUpperCase().equals("RESULT")) {
							if(property.getTextContent().equals("success")){
								result = true;
							}
						}
					}
				}
				// -------------------------------------------------
			} else {
				CustomLog.e("modifySurveyItem","httpResponseError : " + httpResponse.getStatusLine());
			}
		} catch (SecurityException e) {
			CustomLog.e("modifySurveyItem Error : ","SecurityException : " + e.getMessage());
		} catch (MalformedURLException e) {
			CustomLog.e("modifySurveyItem Error : ","MalformedURLException : " + e.getMessage());
		} catch (IOException e) {
			CustomLog.e("modifySurveyItem Error : ", "IOException : " + e.getMessage());
		} catch (NumberFormatException e) {
			CustomLog.e("modifySurveyItem Error : ","NumberFormatException : " + e.getMessage());
		} catch (NullPointerException e) {
			CustomLog.e("modifySurveyItem Error : ","NullPointerException : " + e.getMessage());
		} catch (Exception e) {
			CustomLog.e("modifySurveyItem Error : ", "Exception : " + e.getMessage());
		}
		return result;
	}
	
	public SurveyResponsDTO mSurveyResponsDTO = new SurveyResponsDTO();
	public SurveyResponsDTO getSurveyRespons(){
		
		return mSurveyResponsDTO;
	}
	public boolean setSurveyRespons(int research_id){
		String url = StaticData.RESEARCH_RESPONS_URL;
		boolean result = false;
		mSurveyResponsDTO = new SurveyResponsDTO();
		try {
			HttpClient httpClient = new DefaultHttpClient();
			HttpParams params = httpClient.getParams();
			HttpConnectionParams.setConnectionTimeout(params, 10000);
			HttpConnectionParams.setSoTimeout(params, 10000);

			HttpPost httpPost = new HttpPost(url);

			List<NameValuePair> valuePairs = new ArrayList<NameValuePair>(2);
			valuePairs.add(new BasicNameValuePair("research_id", research_id+ ""));

			CustomLog.e("setSurveyRespons", "research_id :" + research_id);
			CustomLog.e("setSurveyRespons", "url :" + url);
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
				CustomLog.e("setSurveyRespons", "xmlDocument : " + xmlDocument);

				DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
				DocumentBuilder builder = factory.newDocumentBuilder();
				Document document = builder.parse(new InputSource(new StringReader(xmlDocument.toString())));
				NodeList testList = document.getElementsByTagName("ROOT");
				for (int i = 0; i < testList.getLength(); i++) {
					Node test = testList.item(i);
					NodeList properties = test.getChildNodes();
					for (int j = 0; j < properties.getLength(); j++) {
						Node property = properties.item(j);
						String nodeName = property.getNodeName().trim();
						if (nodeName.toUpperCase().equals("RESEARCH_TITLE")) {
							mSurveyResponsDTO.RESEARCH_TITLE = property.getTextContent();
						} else if (nodeName.toUpperCase().equals("RESEARCH_STAT")) {
							mSurveyResponsDTO.RESEARCH_STAT = property.getTextContent();
						} else if (nodeName.toUpperCase().equals("USER_CD")) {
							mSurveyResponsDTO.USER_CD = Integer.parseInt(property.getTextContent());
						} else if (nodeName.toUpperCase().equals("RESEARCH_QUESTION")) {
							NodeList property_sub = property.getChildNodes();
							RESEARCH_QUESTION_DTO dto_1 = new RESEARCH_QUESTION_DTO();
							for(int k=0; k< property_sub.getLength(); k++){
								Node property_sub2 = property_sub.item(k);
								String nodeName_sub2 = property_sub2.getNodeName().trim();
								if(nodeName_sub2.toUpperCase().equals("QUESTION_ID")){
									dto_1.QUESTION_ID = Integer.parseInt(property_sub2.getTextContent());
								}else if(nodeName_sub2.toUpperCase().equals("QUESTION_TITLE")){
									dto_1.QUESTION_TITLE = property_sub2.getTextContent();
								}else if(nodeName_sub2.toUpperCase().equals("QUESTION_NUM")){
									dto_1.QUESTION_NUM = Integer.parseInt(property_sub2.getTextContent());
								}else if(nodeName_sub2.toUpperCase().equals("RESEARCH_ANSWER")){
									RESEARCH_ANSWER_DTO dto_sub = new RESEARCH_ANSWER_DTO();
									NodeList property_sub_3 = property_sub2.getChildNodes();
									for(int p=0; p<property_sub_3.getLength(); p++){
										Node property_sub4 = property_sub_3.item(p);
										String nodeName_sub3 = property_sub4.getNodeName().trim();
										if(nodeName_sub3.toUpperCase().equals("ANSWER_NUM")){
											dto_sub.ANSWER_NUM = Integer.parseInt(property_sub4.getTextContent());
										} else if(nodeName_sub3.toUpperCase().equals("ANSWER_TITLE")){
											dto_sub.ANSWER_TITLE = property_sub4.getTextContent();
										} else if(nodeName_sub3.toUpperCase().equals("ANSWER_RATE")){
											dto_sub.ANSWER_RATE = property_sub4.getTextContent();
										}										
									}
									dto_1.RESEARCH_ANSWER.add(dto_sub);									
								}
							}
							mSurveyResponsDTO.RESEARCH_QUESTION.add(dto_1);
						}
					}
				}
				result = true;
				// -------------------------------------------------
				CustomLog.e("setSurveyRespons","mSurveyResultDTO.RESEARCH_QUESTION.size() : " + mSurveyResponsDTO.RESEARCH_QUESTION.size());
			} else {
				CustomLog.e("setSurveyRespons","httpResponseError : " + httpResponse.getStatusLine());
			}
		} catch (SecurityException e) {
			CustomLog.e("setSurveyRespons Error : ","SecurityException : " + e.getMessage());
		} catch (MalformedURLException e) {
			CustomLog.e("setSurveyRespons Error : ","MalformedURLException : " + e.getMessage());
		} catch (IOException e) {
			CustomLog.e("setSurveyRespons Error : ", "IOException : " + e.getMessage());
		} catch (NumberFormatException e) {
			CustomLog.e("setSurveyRespons Error : ","NumberFormatException : " + e.getMessage());
		} catch (NullPointerException e) {
			CustomLog.e("setSurveyRespons Error : ","NullPointerException : " + e.getMessage());
		} catch (Exception e) {
			CustomLog.e("setSurveyRespons Error : ", "Exception : " + e.getMessage());
		}
		return result;
	}

	public boolean sendSurveyResult(int research_id,String respond){
		String url = StaticData.RESEARCH_RESPOND;
		boolean result = false;
		try {
			HttpClient httpClient = new DefaultHttpClient();
			HttpParams params = httpClient.getParams();
			HttpConnectionParams.setConnectionTimeout(params, 10000);
			HttpConnectionParams.setSoTimeout(params, 10000);

			HttpPost httpPost = new HttpPost(url);

			List<NameValuePair> valuePairs = new ArrayList<NameValuePair>(2);
			valuePairs.add(new BasicNameValuePair("user_cd", StaticData.USER_CD+ ""));
			valuePairs.add(new BasicNameValuePair("research_id", research_id+ ""));
			valuePairs.add(new BasicNameValuePair("respond", respond));

			CustomLog.e("sendSurveyResult", "user_cd :" + StaticData.USER_CD);
			CustomLog.e("sendSurveyResult", "research_id :" + research_id);
			CustomLog.e("sendSurveyResult", "respond :" + respond);
			CustomLog.e("sendSurveyResult", "url :" + url);
			
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
				CustomLog.e("sendSurveyResult", "xmlDocument : " + xmlDocument);

				DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
				DocumentBuilder builder = factory.newDocumentBuilder();
				Document document = builder.parse(new InputSource(new StringReader(xmlDocument.toString())));
				NodeList testList = document.getElementsByTagName("ROOT");
				for (int i = 0; i < testList.getLength(); i++) {
					Node test = testList.item(i);
					NodeList properties = test.getChildNodes();
					for (int j = 0; j < properties.getLength(); j++) {
						Node property = properties.item(j);
						String nodeName = property.getNodeName().trim();
						if (nodeName.toUpperCase().equals("RESULT")) {
							if(property.getTextContent().equals("success")){
								result = true;
							}
						}
					}
				}
				// -------------------------------------------------
			} else {
				CustomLog.e("sendSurveyResult","httpResponseError : " + httpResponse.getStatusLine());
			}
		} catch (SecurityException e) {
			CustomLog.e("sendSurveyResult Error : ","SecurityException : " + e.getMessage());
		} catch (MalformedURLException e) {
			CustomLog.e("sendSurveyResult Error : ","MalformedURLException : " + e.getMessage());
		} catch (IOException e) {
			CustomLog.e("sendSurveyResult Error : ", "IOException : " + e.getMessage());
		} catch (NumberFormatException e) {
			CustomLog.e("sendSurveyResult Error : ","NumberFormatException : " + e.getMessage());
		} catch (NullPointerException e) {
			CustomLog.e("sendSurveyResult Error : ","NullPointerException : " + e.getMessage());
		} catch (Exception e) {
			CustomLog.e("sendSurveyResult Error : ", "Exception : " + e.getMessage());
		}
		return result;
	}
	
	public boolean setPush() {
		String url = StaticData.PUSHAUTH_URL;
		boolean result = false;
		try {
			HttpClient httpClient = new DefaultHttpClient();
			HttpParams params = httpClient.getParams();
			HttpConnectionParams.setConnectionTimeout(params, 10000);
			HttpConnectionParams.setSoTimeout(params, 10000);

			HttpPost httpPost = new HttpPost(url);

			List<NameValuePair> valuePairs = new ArrayList<NameValuePair>(2);
			valuePairs.add(new BasicNameValuePair("user_cd", StaticData.USER_CD+ ""));

			CustomLog.e("setPush", "user_cd :" + StaticData.USER_CD);
			CustomLog.e("setPush", "url :" + url);
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
				CustomLog.e("setPush", "xmlDocument : " + xmlDocument);

				DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
				DocumentBuilder builder = factory.newDocumentBuilder();
				Document document = builder.parse(new InputSource(new StringReader(xmlDocument.toString())));
				NodeList testList = document.getElementsByTagName("ROOT");
				for (int i = 0; i < testList.getLength(); i++) {
					Node test = testList.item(i);
					NodeList properties = test.getChildNodes();
					for (int j = 0; j < properties.getLength(); j++) {
						Node property = properties.item(j);
						String nodeName = property.getNodeName().trim();
						if (nodeName.toUpperCase().equals("RESULT")) {
							if(property.getTextContent().equals("success")){
								result = true;
							}
						}
					}
				}
				// -------------------------------------------------
			} else {
				CustomLog.e("setPush","httpResponseError : " + httpResponse.getStatusLine());
			}
		} catch (SecurityException e) {
			CustomLog.e("setPush Error : ","SecurityException : " + e.getMessage());
		} catch (MalformedURLException e) {
			CustomLog.e("setPush Error : ","MalformedURLException : " + e.getMessage());
		} catch (IOException e) {
			CustomLog.e("setPush Error : ", "IOException : " + e.getMessage());
		} catch (NumberFormatException e) {
			CustomLog.e("setPush Error : ","NumberFormatException : " + e.getMessage());
		} catch (NullPointerException e) {
			CustomLog.e("setPush Error : ","NullPointerException : " + e.getMessage());
		} catch (Exception e) {
			CustomLog.e("setPush Error : ", "Exception : " + e.getMessage());
		}
		return result;
	}

}
