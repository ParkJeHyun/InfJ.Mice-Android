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

import android.R.integer;
import android.app.Activity;
import android.os.Handler;
import android.util.Log;

public class MessageDAO {
	Activity activity;
	Handler handler;
	String xmlDocument;

	public MessageDAO(Activity activity) {
		this.activity = activity;
	}

	public boolean setSendMessage(int to_user_cd, int from_user_cd,
			String contents, String title) {
		String url = StaticData.MESSAGE_URL;
		// conference_id : params default to_user_cd from_user_cd contents
		boolean result = false;

		try {
			HttpClient httpClient = new DefaultHttpClient();
			HttpParams params = httpClient.getParams();
			HttpConnectionParams.setConnectionTimeout(params, 10000);
			HttpConnectionParams.setSoTimeout(params, 10000);

			HttpPost httpPost = new HttpPost(url);

			List<NameValuePair> valuePairs = new ArrayList<NameValuePair>(2);
			valuePairs.add(new BasicNameValuePair("conference_id",StaticData.CONFERENCE_ID + ""));
			valuePairs.add(new BasicNameValuePair("to_user_cd", to_user_cd + ""));
			valuePairs.add(new BasicNameValuePair("from_user_cd", from_user_cd+ ""));
			valuePairs.add(new BasicNameValuePair("contents", contents.trim()));
			valuePairs.add(new BasicNameValuePair("title", title.trim()));

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
				Log.e("setSendMessage","----------------------------------------");
				Log.e("setSendMessage","xmlDocument : "+xmlDocument);

				DocumentBuilderFactory factory = DocumentBuilderFactory
						.newInstance();
				DocumentBuilder builder = factory.newDocumentBuilder();
				Document document = builder.parse(new InputSource(
						new StringReader(xmlDocument.toString())));

				NodeList testList = document.getElementsByTagName("ROOT");
				for (int i = 0; i < testList.getLength(); i++) {
					Node test = testList.item(i);
					NodeList properties = test.getChildNodes();
					for (int j = 0; j < properties.getLength(); j++) {
						Node property = properties.item(j);
						String nodeName = property.getNodeName().trim();
						if (nodeName.toUpperCase().equals("RESULT")) {
							if (property.getTextContent().equals("success")) {
								result = true;
							} else {
								result = false;
							}
						}
					}
					Log.e("setSendMessage", "setSendMessage result : " + result);
				}
				// -------------------------------------------------
			} else {
				Log.e("setSendMessage","httpResponseError : " + httpResponse.getStatusLine());
			}
		} catch (SecurityException e) {
			Log.e("setSendMessage Error : ","SecurityException : " + e.getMessage());
		} catch (MalformedURLException e) {
			Log.e("setSendMessage Error : ","MalformedURLException : " + e.getMessage());
		} catch (IOException e) {
			Log.e("setSendMessage Error : ", "IOException : " + e.getMessage());
		} catch (NumberFormatException e) {
			Log.e("setSendMessage Error : ","NumberFormatException : " + e.getMessage());
		} catch (NullPointerException e) {
			Log.e("setSendMessage Error : ","NullPointerException : " + e.getMessage());
		} catch (Exception e) {
			Log.e("setSendMessage Error : ", "Exception : " + e.getMessage());
		}
		return result;
	}
	
	public boolean setSendMessage(int to_user_cd, int from_user_cd,
			String contents, String title, int reply) {
		String url = StaticData.MESSAGE_URL;
		// conference_id : params default to_user_cd from_user_cd contents
		boolean result = false;

		try {
			HttpClient httpClient = new DefaultHttpClient();
			HttpParams params = httpClient.getParams();
			HttpConnectionParams.setConnectionTimeout(params, 10000);
			HttpConnectionParams.setSoTimeout(params, 10000);

			HttpPost httpPost = new HttpPost(url);

			List<NameValuePair> valuePairs = new ArrayList<NameValuePair>(2);
			valuePairs.add(new BasicNameValuePair("conference_id",StaticData.CONFERENCE_ID + ""));
			valuePairs.add(new BasicNameValuePair("to_user_cd", to_user_cd + ""));
			valuePairs.add(new BasicNameValuePair("from_user_cd", from_user_cd+ ""));
			valuePairs.add(new BasicNameValuePair("contents", contents.trim()));
			valuePairs.add(new BasicNameValuePair("title", title.trim()));
			valuePairs.add(new BasicNameValuePair("reply", reply+""));

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
				Log.e("setSendMessage","----------------------------------------");
				Log.e("setSendMessage","xmlDocument : "+xmlDocument);

				DocumentBuilderFactory factory = DocumentBuilderFactory
						.newInstance();
				DocumentBuilder builder = factory.newDocumentBuilder();
				Document document = builder.parse(new InputSource(
						new StringReader(xmlDocument.toString())));

				NodeList testList = document.getElementsByTagName("ROOT");
				for (int i = 0; i < testList.getLength(); i++) {
					Node test = testList.item(i);
					NodeList properties = test.getChildNodes();
					for (int j = 0; j < properties.getLength(); j++) {
						Node property = properties.item(j);
						String nodeName = property.getNodeName().trim();
						if (nodeName.toUpperCase().equals("RESULT")) {
							if (property.getTextContent().equals("success")) {
								result = true;
							} else {
								result = false;
							}
						}
					}
					Log.e("setSendMessage", "setSendMessage result : " + result);
				}
				// -------------------------------------------------
			} else {
				Log.e("setSendMessage","httpResponseError : " + httpResponse.getStatusLine());
			}
		} catch (SecurityException e) {
			Log.e("setSendMessage Error : ","SecurityException : " + e.getMessage());
		} catch (MalformedURLException e) {
			Log.e("setSendMessage Error : ","MalformedURLException : " + e.getMessage());
		} catch (IOException e) {
			Log.e("setSendMessage Error : ", "IOException : " + e.getMessage());
		} catch (NumberFormatException e) {
			Log.e("setSendMessage Error : ","NumberFormatException : " + e.getMessage());
		} catch (NullPointerException e) {
			Log.e("setSendMessage Error : ","NullPointerException : " + e.getMessage());
		} catch (Exception e) {
			Log.e("setSendMessage Error : ", "Exception : " + e.getMessage());
		}
		return result;
	}
	
	public boolean setSendQnA(int to_user_cd, int from_user_cd,
			String contents, String title) {
		String url = StaticData.QNA_URL;
		// conference_id : params default to_user_cd from_user_cd contents
		boolean result = false;

		try {
			HttpClient httpClient = new DefaultHttpClient();
			HttpParams params = httpClient.getParams();
			HttpConnectionParams.setConnectionTimeout(params, 10000);
			HttpConnectionParams.setSoTimeout(params, 10000);

			HttpPost httpPost = new HttpPost(url);

			List<NameValuePair> valuePairs = new ArrayList<NameValuePair>(2);
			valuePairs.add(new BasicNameValuePair("conference_id",StaticData.CONFERENCE_ID + ""));
			valuePairs.add(new BasicNameValuePair("to_user_cd", to_user_cd + ""));
			valuePairs.add(new BasicNameValuePair("from_user_cd", from_user_cd+ ""));
			valuePairs.add(new BasicNameValuePair("contents", contents.trim()));
			valuePairs.add(new BasicNameValuePair("title", title.trim()));

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
				Log.e("setSendMessage","----------------------------------------");
				Log.e("setSendMessage","xmlDocument : "+xmlDocument);

				DocumentBuilderFactory factory = DocumentBuilderFactory
						.newInstance();
				DocumentBuilder builder = factory.newDocumentBuilder();
				Document document = builder.parse(new InputSource(
						new StringReader(xmlDocument.toString())));

				NodeList testList = document.getElementsByTagName("ROOT");
				for (int i = 0; i < testList.getLength(); i++) {
					Node test = testList.item(i);
					NodeList properties = test.getChildNodes();
					for (int j = 0; j < properties.getLength(); j++) {
						Node property = properties.item(j);
						String nodeName = property.getNodeName().trim();
						if (nodeName.toUpperCase().equals("RESULT")) {
							if (property.getTextContent().equals("success")) {
								result = true;
							} else {
								result = false;
							}
						}
					}
					Log.e("setSendMessage", "setSendMessage result : " + result);
				}
				// -------------------------------------------------
			} else {
				Log.e("setSendMessage","httpResponseError : " + httpResponse.getStatusLine());
			}
		} catch (SecurityException e) {
			Log.e("setSendMessage Error : ","SecurityException : " + e.getMessage());
		} catch (MalformedURLException e) {
			Log.e("setSendMessage Error : ","MalformedURLException : " + e.getMessage());
		} catch (IOException e) {
			Log.e("setSendMessage Error : ", "IOException : " + e.getMessage());
		} catch (NumberFormatException e) {
			Log.e("setSendMessage Error : ","NumberFormatException : " + e.getMessage());
		} catch (NullPointerException e) {
			Log.e("setSendMessage Error : ","NullPointerException : " + e.getMessage());
		} catch (Exception e) {
			Log.e("setSendMessage Error : ", "Exception : " + e.getMessage());
		}
		return result;
	}
	
	public boolean setSendQnA(int to_user_cd, int from_user_cd,
			String contents, String title, int reply) {
		String url = StaticData.QNA_URL;
		// conference_id : params default to_user_cd from_user_cd contents
		boolean result = false;

		try {
			HttpClient httpClient = new DefaultHttpClient();
			HttpParams params = httpClient.getParams();
			HttpConnectionParams.setConnectionTimeout(params, 10000);
			HttpConnectionParams.setSoTimeout(params, 10000);

			HttpPost httpPost = new HttpPost(url);

			List<NameValuePair> valuePairs = new ArrayList<NameValuePair>(2);
			valuePairs.add(new BasicNameValuePair("conference_id",StaticData.CONFERENCE_ID + ""));
			valuePairs.add(new BasicNameValuePair("to_user_cd", to_user_cd + ""));
			valuePairs.add(new BasicNameValuePair("from_user_cd", from_user_cd+ ""));
			valuePairs.add(new BasicNameValuePair("contents", contents.trim()));
			valuePairs.add(new BasicNameValuePair("title", title.trim()));
			valuePairs.add(new BasicNameValuePair("reply", reply+""));

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
				Log.e("setSendMessage","----------------------------------------");
				Log.e("setSendMessage","xmlDocument : "+xmlDocument);

				DocumentBuilderFactory factory = DocumentBuilderFactory
						.newInstance();
				DocumentBuilder builder = factory.newDocumentBuilder();
				Document document = builder.parse(new InputSource(
						new StringReader(xmlDocument.toString())));

				NodeList testList = document.getElementsByTagName("ROOT");
				for (int i = 0; i < testList.getLength(); i++) {
					Node test = testList.item(i);
					NodeList properties = test.getChildNodes();
					for (int j = 0; j < properties.getLength(); j++) {
						Node property = properties.item(j);
						String nodeName = property.getNodeName().trim();
						if (nodeName.toUpperCase().equals("RESULT")) {
							if (property.getTextContent().equals("success")) {
								result = true;
							} else {
								result = false;
							}
						}
					}
					Log.e("setSendMessage", "setSendMessage result : " + result);
				}
				// -------------------------------------------------
			} else {
				Log.e("setSendMessage","httpResponseError : " + httpResponse.getStatusLine());
			}
		} catch (SecurityException e) {
			Log.e("setSendMessage Error : ","SecurityException : " + e.getMessage());
		} catch (MalformedURLException e) {
			Log.e("setSendMessage Error : ","MalformedURLException : " + e.getMessage());
		} catch (IOException e) {
			Log.e("setSendMessage Error : ", "IOException : " + e.getMessage());
		} catch (NumberFormatException e) {
			Log.e("setSendMessage Error : ","NumberFormatException : " + e.getMessage());
		} catch (NullPointerException e) {
			Log.e("setSendMessage Error : ","NullPointerException : " + e.getMessage());
		} catch (Exception e) {
			Log.e("setSendMessage Error : ", "Exception : " + e.getMessage());
		}
		return result;
	}
	
	public boolean setSendQuestion(int to_user_cd, int from_user_cd,
			String contents, String title) {
		String url = StaticData.QUESTION_URL;
		// conference_id : params default to_user_cd from_user_cd contents
		boolean result = false;

		try {
			HttpClient httpClient = new DefaultHttpClient();
			HttpParams params = httpClient.getParams();
			HttpConnectionParams.setConnectionTimeout(params, 10000);
			HttpConnectionParams.setSoTimeout(params, 10000);

			HttpPost httpPost = new HttpPost(url);

			List<NameValuePair> valuePairs = new ArrayList<NameValuePair>(2);
			valuePairs.add(new BasicNameValuePair("conference_id",StaticData.CONFERENCE_ID + ""));
			valuePairs.add(new BasicNameValuePair("to_user_cd", to_user_cd + ""));
			valuePairs.add(new BasicNameValuePair("from_user_cd", from_user_cd+ ""));
			valuePairs.add(new BasicNameValuePair("contents", contents.trim()));
			valuePairs.add(new BasicNameValuePair("title", title.trim()));

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
				Log.e("setSendQuestion","----------------------------------------");
				Log.e("setSendQuestion","xmlDocument : "+xmlDocument);

				DocumentBuilderFactory factory = DocumentBuilderFactory
						.newInstance();
				DocumentBuilder builder = factory.newDocumentBuilder();
				Document document = builder.parse(new InputSource(
						new StringReader(xmlDocument.toString())));

				NodeList testList = document.getElementsByTagName("ROOT");
				for (int i = 0; i < testList.getLength(); i++) {
					Node test = testList.item(i);
					NodeList properties = test.getChildNodes();
					for (int j = 0; j < properties.getLength(); j++) {
						Node property = properties.item(j);
						String nodeName = property.getNodeName().trim();
						if (nodeName.toUpperCase().equals("RESULT")) {
							if (property.getTextContent().equals("success")) {
								result = true;
							} else {
								result = false;
							}
						}
					}
					Log.e("setSendQuestion", "setSendMessage result : " + result);
				}
				// -------------------------------------------------
			} else {
				Log.e("setSendQuestion",
						"httpResponseError : " + httpResponse.getStatusLine());
			}
		} catch (SecurityException e) {
			Log.e("setSendQuestion Error : ","SecurityException : " + e.getMessage());
		} catch (MalformedURLException e) {
			Log.e("setSendQuestion Error : ","MalformedURLException : " + e.getMessage());
		} catch (IOException e) {
			Log.e("setSendQuestion Error : ", "IOException : " + e.getMessage());
		} catch (NumberFormatException e) {
			Log.e("setSendQuestion Error : ","NumberFormatException : " + e.getMessage());
		} catch (NullPointerException e) {
			Log.e("setSendQuestion Error : ","NullPointerException : " + e.getMessage());
		} catch (Exception e) {
			Log.e("setSendQuestion Error : ", "Exception : " + e.getMessage());
		}
		return result;
	}

	public boolean setSendQuestion(int to_user_cd, int from_user_cd,
			String contents, String title, int reply) {
		String url = StaticData.QUESTION_URL;
		// conference_id : params default to_user_cd from_user_cd contents
		boolean result = false;

		try {
			HttpClient httpClient = new DefaultHttpClient();
			HttpParams params = httpClient.getParams();
			HttpConnectionParams.setConnectionTimeout(params, 10000);
			HttpConnectionParams.setSoTimeout(params, 10000);

			HttpPost httpPost = new HttpPost(url);

			List<NameValuePair> valuePairs = new ArrayList<NameValuePair>(2);
			valuePairs.add(new BasicNameValuePair("conference_id",StaticData.CONFERENCE_ID + ""));
			valuePairs.add(new BasicNameValuePair("to_user_cd", to_user_cd + ""));
			valuePairs.add(new BasicNameValuePair("from_user_cd", from_user_cd+ ""));
			valuePairs.add(new BasicNameValuePair("contents", contents.trim()));
			valuePairs.add(new BasicNameValuePair("title", title.trim()));
			valuePairs.add(new BasicNameValuePair("reply", reply+""));

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
				Log.e("setSendQuestion","----------------------------------------");
				Log.e("setSendQuestion","xmlDocument : "+xmlDocument);

				DocumentBuilderFactory factory = DocumentBuilderFactory
						.newInstance();
				DocumentBuilder builder = factory.newDocumentBuilder();
				Document document = builder.parse(new InputSource(
						new StringReader(xmlDocument.toString())));

				NodeList testList = document.getElementsByTagName("ROOT");
				for (int i = 0; i < testList.getLength(); i++) {
					Node test = testList.item(i);
					NodeList properties = test.getChildNodes();
					for (int j = 0; j < properties.getLength(); j++) {
						Node property = properties.item(j);
						String nodeName = property.getNodeName().trim();
						if (nodeName.toUpperCase().equals("RESULT")) {
							if (property.getTextContent().equals("success")) {
								result = true;
							} else {
								result = false;
							}
						}
					}
					Log.e("setSendQuestion", "setSendMessage result : " + result);
				}
				// -------------------------------------------------
			} else {
				Log.e("setSendQuestion",
						"httpResponseError : " + httpResponse.getStatusLine());
			}
		} catch (SecurityException e) {
			Log.e("setSendQuestion Error : ","SecurityException : " + e.getMessage());
		} catch (MalformedURLException e) {
			Log.e("setSendQuestion Error : ","MalformedURLException : " + e.getMessage());
		} catch (IOException e) {
			Log.e("setSendQuestion Error : ", "IOException : " + e.getMessage());
		} catch (NumberFormatException e) {
			Log.e("setSendQuestion Error : ","NumberFormatException : " + e.getMessage());
		} catch (NullPointerException e) {
			Log.e("setSendQuestion Error : ","NullPointerException : " + e.getMessage());
		} catch (Exception e) {
			Log.e("setSendQuestion Error : ", "Exception : " + e.getMessage());
		}
		return result;
	}
	
	
	// message inbox area
	public ArrayList<MessageBoxDto> messageBoxDtoList;
	
	public ArrayList<MessageBoxDto> getMessageInboc(){
		return messageBoxDtoList;
	}
	public boolean setMessageInbox() {
		String url = StaticData.MESSAGE_RECEIVE_URL;
		// conference_id : params default to_user_cd from_user_cd contents
		boolean result = false;
		messageBoxDtoList = new ArrayList<MessageBoxDto>();
		try {
			HttpClient httpClient = new DefaultHttpClient();
			HttpParams params = httpClient.getParams();
			HttpConnectionParams.setConnectionTimeout(params, 10000);
			HttpConnectionParams.setSoTimeout(params, 10000);

			HttpPost httpPost = new HttpPost(url);

			List<NameValuePair> valuePairs = new ArrayList<NameValuePair>(2);
			valuePairs.add(new BasicNameValuePair("conference_id",StaticData.CONFERENCE_ID + ""));
			valuePairs.add(new BasicNameValuePair("user_cd", StaticData.USER_CD + ""));
			
			Log.e("setMessageInbox", "conference_id : "+StaticData.CONFERENCE_ID);
			Log.e("setMessageInbox", "user_cd :" +StaticData.USER_CD);
			Log.e("setMessageInbox", "url :" + url);
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
				Log.e("setMessageInbox","----------------------------------------");
				Log.e("setMessageInbox","xmlDocument : "+xmlDocument);

				DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
				DocumentBuilder builder = factory.newDocumentBuilder();
				Log.e("setMessageInbox","----------------------------------------builder");
				Document document = builder.parse(new InputSource(new StringReader(xmlDocument.toString().trim())));
				Log.e("setMessageInbox","----------------------------------------document");
				NodeList testList = document.getElementsByTagName("MESSAGE_ITEM");
				Log.e("setMessageInbox","----------------------------------------MESSAGE_ITEM");
				for (int i = 0; i < testList.getLength(); i++) {
					MessageBoxDto dto = new MessageBoxDto();
					Node test = testList.item(i);
					NodeList properties = test.getChildNodes();
					for (int j = 0; j < properties.getLength(); j++) {
						Node property = properties.item(j);
						String nodeName = property.getNodeName().trim();
						if (nodeName.toUpperCase().equals("MESSAGE_ID")) {
							Log.e("setMessageInbox","----------------------------------------MESSAGE_ID");
							dto.MESSAGE_ID = Integer.parseInt(property.getTextContent());
						}else if(nodeName.toUpperCase().equals("MESSAGE_TYPE")) {
							Log.e("setMessageInbox","----------------------------------------MESSAGE_TYPE");
							dto.MESSAGE_TYPE = property.getTextContent();
						}else if(nodeName.toUpperCase().equals("TO_USER_NAME")) {
							Log.e("setMessageInbox","----------------------------------------TO_USER_NAME");
							dto.TO_USER_NAME = property.getTextContent();
						}else if(nodeName.toUpperCase().equals("TO_USER_CD")) {
							Log.e("setMessageInbox","----------------------------------------TO_USER_CD");
							dto.TO_USER_CD = Integer.parseInt(property.getTextContent());
						}else if(nodeName.toUpperCase().equals("FROM_USER_NAME")) {
							Log.e("setMessageInbox","----------------------------------------FROM_USER_NAME");
							dto.FROM_USER_NAME = property.getTextContent();
						}else if(nodeName.toUpperCase().equals("FROM_USER_CD")) {
							Log.e("setMessageInbox","----------------------------------------FROM_USER_CD");
							dto.FROM_USER_CD = Integer.parseInt(property.getTextContent());
						}else if(nodeName.toUpperCase().equals("TITLE")) {
							Log.e("setMessageInbox","----------------------------------------TITLE");
							dto.TITLE = property.getTextContent();
						}else if(nodeName.toUpperCase().equals("REPLY")) {
							Log.e("setMessageInbox","----------------------------------------REPLY");
							dto.REPLY = property.getTextContent();
						}else if(nodeName.toUpperCase().equals("RECEIPT")) {
							Log.e("setMessageInbox","----------------------------------------RECEIPT");
							dto.RECEIPT = property.getTextContent();
						}else if(nodeName.toUpperCase().equals("REG_DATE")) {
							Log.e("setMessageInbox","----------------------------------------REG_DATE");
							dto.REG_DATE = property.getTextContent();
						}
					}
					messageBoxDtoList.add(dto);
				}
				result = true;
				Log.e("setMessageInbox","messageBoxDtoList.size : " + messageBoxDtoList.size());
				// -------------------------------------------------
			} else {
				Log.e("setMessageInbox","httpResponseError : " + httpResponse.getStatusLine());
			}
		} catch (SecurityException e) {
			Log.e("setMessageInbox Error : ","SecurityException : " + e.getMessage());
		} catch (MalformedURLException e) {
			Log.e("setMessageInbox Error : ","MalformedURLException : " + e.getMessage());
		} catch (IOException e) {
			Log.e("setMessageInbox Error : ", "IOException : " + e.getMessage());
		} catch (NumberFormatException e) {
			Log.e("setMessageInbox Error : ","NumberFormatException : " + e.getMessage());
		} catch (NullPointerException e) {
			Log.e("setMessageInbox Error : ","NullPointerException : " + e.getMessage());
		} catch (Exception e) {
			Log.e("setMessageInbox Error : ", "Exception : " + e.getMessage());
		}
		return result;
	}
	
	//============================ message out box
	public ArrayList<MessageBoxDto> messageOutBoxDtoList;
	
	public ArrayList<MessageBoxDto> getMessageOutbox(){
		return messageOutBoxDtoList;
	}
	// message out box
	public boolean setMessageOutbox() {
		String url = StaticData.MESSAGE_DISOATCH_URL;
		boolean result = false;
		messageOutBoxDtoList = new ArrayList<MessageBoxDto>();
		try {
			HttpClient httpClient = new DefaultHttpClient();
			HttpParams params = httpClient.getParams();
			HttpConnectionParams.setConnectionTimeout(params, 10000);
			HttpConnectionParams.setSoTimeout(params, 10000);

			HttpPost httpPost = new HttpPost(url);

			List<NameValuePair> valuePairs = new ArrayList<NameValuePair>(2);
			valuePairs.add(new BasicNameValuePair("conference_id",StaticData.CONFERENCE_ID + ""));
			valuePairs.add(new BasicNameValuePair("user_cd", StaticData.USER_CD + ""));
			
			Log.e("setMessageOutbox", "conference_id : "+StaticData.CONFERENCE_ID);
			Log.e("setMessageOutbox", "user_cd :" +StaticData.USER_CD);
			Log.e("setMessageOutbox", "url :" + url);
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
				Log.e("setMessageOutbox","----------------------------------------");
				Log.e("setMessageOutbox","xmlDocument : "+xmlDocument);

				DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
				DocumentBuilder builder = factory.newDocumentBuilder();
				Document document = builder.parse(new InputSource(new StringReader(xmlDocument.toString())));
				NodeList testList = document.getElementsByTagName("MESSAGE_ITEM");
				for (int i = 0; i < testList.getLength(); i++) {
					MessageBoxDto dto = new MessageBoxDto();
					Node test = testList.item(i);
					NodeList properties = test.getChildNodes();
					for (int j = 0; j < properties.getLength(); j++) {
						Node property = properties.item(j);
						String nodeName = property.getNodeName().trim();
						if (nodeName.toUpperCase().equals("MESSAGE_ID")) {
							dto.MESSAGE_ID = Integer.parseInt(property.getTextContent());
						}else if(nodeName.toUpperCase().equals("MESSAGE_TYPE")) {
							dto.MESSAGE_TYPE = property.getTextContent();
						}else if(nodeName.toUpperCase().equals("TO_USER_NAME")) {
							dto.TO_USER_NAME = property.getTextContent();
						}else if(nodeName.toUpperCase().equals("TO_USER_CD")) {
							dto.TO_USER_CD = Integer.parseInt(property.getTextContent());
						}else if(nodeName.toUpperCase().equals("FROM_USER_NAME")) {
							dto.FROM_USER_NAME = property.getTextContent();
						}else if(nodeName.toUpperCase().equals("FROM_USER_CD")) {
							dto.FROM_USER_CD = Integer.parseInt(property.getTextContent());
						}else if(nodeName.toUpperCase().equals("TITLE")) {
							dto.TITLE = property.getTextContent();
						}else if(nodeName.toUpperCase().equals("REPLY")) {
							dto.REPLY = property.getTextContent();
						}else if(nodeName.toUpperCase().equals("RECEIPT")) {
							dto.RECEIPT = property.getTextContent();
						}else if(nodeName.toUpperCase().equals("REG_DATE")) {
							dto.REG_DATE = property.getTextContent();
						}
					}
					messageOutBoxDtoList.add(dto);
				}
				Log.e("setMessageOutbox","messageOutBoxDtoList.size : " + messageOutBoxDtoList.size());
				result = true;
				// -------------------------------------------------
			} else {
				Log.e("setMessageOutbox","httpResponseError : " + httpResponse.getStatusLine());
			}
		} catch (SecurityException e) {
			Log.e("setMessageOutbox Error : ","SecurityException : " + e.getMessage());
		} catch (MalformedURLException e) {
			Log.e("setMessageOutbox Error : ","MalformedURLException : " + e.getMessage());
		} catch (IOException e) {
			Log.e("setMessageOutbox Error : ", "IOException : " + e.getMessage());
		} catch (NumberFormatException e) {
			Log.e("setMessageOutbox Error : ","NumberFormatException : " + e.getMessage());
		} catch (NullPointerException e) {
			Log.e("setMessageOutbox Error : ","NullPointerException : " + e.getMessage());
		} catch (Exception e) {
			Log.e("setMessageOutbox Error : ", "Exception : " + e.getMessage());
		}
		return result;
	}
	
	// =========================== message detail =======================================
	
	public MessageDetailDto messageDetailDto;
	
	public MessageDetailDto getMessageOutboxDetail(){
		return messageDetailDto; 
	}
	
	public boolean setMessageOutboxDetail(int message_id){
		String url = StaticData.MESSAGE_DETAIL_URL;
		boolean result = false;
		messageDetailDto = new MessageDetailDto();
		try {
			HttpClient httpClient = new DefaultHttpClient();
			HttpParams params = httpClient.getParams();
			HttpConnectionParams.setConnectionTimeout(params, 10000);
			HttpConnectionParams.setSoTimeout(params, 10000);

			HttpPost httpPost = new HttpPost(url);

			List<NameValuePair> valuePairs = new ArrayList<NameValuePair>(2);
			valuePairs.add(new BasicNameValuePair("conference_id",StaticData.CONFERENCE_ID + ""));
			valuePairs.add(new BasicNameValuePair("message_id", message_id + ""));
			
			Log.e("setMessageOutboxDetail", "conference_id : "+StaticData.CONFERENCE_ID);
			Log.e("setMessageOutboxDetail", "message_id :" +message_id);
			Log.e("setMessageOutboxDetail", "url :" + url);
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
				Log.e("setMessageOutboxDetail","----------------------------------------");
				Log.e("setMessageOutboxDetail","xmlDocument : "+xmlDocument);

				DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
				DocumentBuilder builder = factory.newDocumentBuilder();
				Document document = builder.parse(new InputSource(new StringReader(xmlDocument.toString())));
				NodeList testList = document.getElementsByTagName("MESSAGE_ITEM");
				for (int i = 0; i < testList.getLength(); i++) {
					Node test = testList.item(i);
					NodeList properties = test.getChildNodes();
					for (int j = 0; j < properties.getLength(); j++) {
						Node property = properties.item(j);
						String nodeName = property.getNodeName().trim();
						if (nodeName.toUpperCase().equals("MESSAGE_ID")) {
							messageDetailDto.MESSAGE_ID = Integer.parseInt(property.getTextContent());
						}else if(nodeName.toUpperCase().equals("MESSAGE_TYPE")) {
							messageDetailDto.MESSAGE_TYPE = property.getTextContent();
						}else if(nodeName.toUpperCase().equals("TO_USER_NAME")) {
							messageDetailDto.TO_USER_NAME = property.getTextContent();
						}else if(nodeName.toUpperCase().equals("TO_USER_CD")) {
							messageDetailDto.TO_USER_CD = Integer.parseInt(property.getTextContent());
						}else if(nodeName.toUpperCase().equals("FROM_USER_NAME")) {
							messageDetailDto.FROM_USER_NAME = property.getTextContent();
						}else if(nodeName.toUpperCase().equals("FROM_USER_CD")) {
							messageDetailDto.FROM_USER_CD = Integer.parseInt(property.getTextContent());
						}else if(nodeName.toUpperCase().equals("TITLE")) {
							messageDetailDto.TITLE = property.getTextContent();
						}else if(nodeName.toUpperCase().equals("REPLY")) {
							messageDetailDto.REPLY = property.getTextContent();
						}else if(nodeName.toUpperCase().equals("CONTENTS")) {
							messageDetailDto.CONTENTS = property.getTextContent();
						}else if(nodeName.toUpperCase().equals("REG_DATE")) {
							messageDetailDto.REG_DATE = property.getTextContent();
						}
					}
				}
				Log.e("setMessageOutboxDetail","messageDetailDto : " + messageDetailDto);
				result = true;
				// -------------------------------------------------
			} else {
				Log.e("setMessageOutboxDetail","httpResponseError : " + httpResponse.getStatusLine());
			}
		} catch (SecurityException e) {
			Log.e("setMessageOutboxDetail Error : ","SecurityException : " + e.getMessage());
		} catch (MalformedURLException e) {
			Log.e("setMessageOutboxDetail Error : ","MalformedURLException : " + e.getMessage());
		} catch (IOException e) {
			Log.e("setMessageOutboxDetail Error : ", "IOException : " + e.getMessage());
		} catch (NumberFormatException e) {
			Log.e("setMessageOutboxDetail Error : ","NumberFormatException : " + e.getMessage());
		} catch (NullPointerException e) {
			Log.e("setMessageOutboxDetail Error : ","NullPointerException : " + e.getMessage());
		} catch (Exception e) {
			Log.e("setMessageOutboxDetail Error : ", "Exception : " + e.getMessage());
		}
		return result;
	}
	
	public boolean setDeleteMessage(int message_id) {
		String url = StaticData.MESSAGE_DELETE_URL;
		// conference_id : params default to_user_cd from_user_cd contents
		boolean result = false;

		try {
			HttpClient httpClient = new DefaultHttpClient();
			HttpParams params = httpClient.getParams();
			HttpConnectionParams.setConnectionTimeout(params, 10000);
			HttpConnectionParams.setSoTimeout(params, 10000);

			HttpPost httpPost = new HttpPost(url);

			List<NameValuePair> valuePairs = new ArrayList<NameValuePair>(2);
			valuePairs.add(new BasicNameValuePair("conference_id",StaticData.CONFERENCE_ID + ""));
			valuePairs.add(new BasicNameValuePair("message_id", message_id + ""));

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
				Log.e("setDeleteMessage","----------------------------------------");
				Log.e("setDeleteMessage","xmlDocument : "+xmlDocument);

				DocumentBuilderFactory factory = DocumentBuilderFactory
						.newInstance();
				DocumentBuilder builder = factory.newDocumentBuilder();
				Document document = builder.parse(new InputSource(
						new StringReader(xmlDocument.toString())));

				NodeList testList = document.getElementsByTagName("MESSAGE_DELETE");
				for (int i = 0; i < testList.getLength(); i++) {
					Node test = testList.item(i);
					NodeList properties = test.getChildNodes();
					for (int j = 0; j < properties.getLength(); j++) {
						Node property = properties.item(j);
						String nodeName = property.getNodeName().trim();
						if (nodeName.toUpperCase().equals("FLAG")) {
							if (property.getTextContent().equals("success")) {
								result = true;
							} else {
								result = false;
							}
						}
					}
					Log.e("setDeleteMessage", "setSendMessage result : " + result);
				}
				// -------------------------------------------------
			} else {
				Log.e("setDeleteMessage","httpResponseError : " + httpResponse.getStatusLine());
			}
		} catch (SecurityException e) {
			Log.e("setDeleteMessage Error : ","SecurityException : " + e.getMessage());
		} catch (MalformedURLException e) {
			Log.e("setDeleteMessage Error : ","MalformedURLException : " + e.getMessage());
		} catch (IOException e) {
			Log.e("setDeleteMessage Error : ", "IOException : " + e.getMessage());
		} catch (NumberFormatException e) {
			Log.e("setDeleteMessage Error : ","NumberFormatException : " + e.getMessage());
		} catch (NullPointerException e) {
			Log.e("setDeleteMessage Error : ","NullPointerException : " + e.getMessage());
		} catch (Exception e) {
			Log.e("setDeleteMessage Error : ", "Exception : " + e.getMessage());
		}
		return result;
	}
	
	// appointments add area
	public boolean setAppointmentsAdd(String to_user_cd, String  promise_date,
			String promise_hour,String contents, String title) {
		String url = StaticData.PROMISE_URL;
		// conference_id
		// from_user_cd 
		// to_user_cd = 3,4,5,6 
		// promise_date = 2013-01-12 
		// promise_hour = 1230 
		// title =
		// contents
		boolean result = false;

		try {
			HttpClient httpClient = new DefaultHttpClient();
			HttpParams params = httpClient.getParams();
			HttpConnectionParams.setConnectionTimeout(params, 10000);
			HttpConnectionParams.setSoTimeout(params, 10000);

			HttpPost httpPost = new HttpPost(url);

			List<NameValuePair> valuePairs = new ArrayList<NameValuePair>(2);
			valuePairs.add(new BasicNameValuePair("conference_id",StaticData.CONFERENCE_ID + ""));
			valuePairs.add(new BasicNameValuePair("from_user_cd", StaticData.USER_CD+ ""));
			valuePairs.add(new BasicNameValuePair("to_user_cd", to_user_cd));
			valuePairs.add(new BasicNameValuePair("promise_date", promise_date));
			valuePairs.add(new BasicNameValuePair("promise_hour", promise_hour));
			
			valuePairs.add(new BasicNameValuePair("title", title.trim()));
			valuePairs.add(new BasicNameValuePair("contents", contents.trim()));
			
			CustomLog.e("-----------", "promise_date : "+promise_date);
			CustomLog.e("-----------", "promise_hour : "+promise_hour);

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
				Log.e("setAppointmentsAdd","----------------------------------------");
				Log.e("setAppointmentsAdd","xmlDocument : "+xmlDocument);

				DocumentBuilderFactory factory = DocumentBuilderFactory
						.newInstance();
				DocumentBuilder builder = factory.newDocumentBuilder();
				Document document = builder.parse(new InputSource(
						new StringReader(xmlDocument.toString())));

				NodeList testList = document.getElementsByTagName("ROOT");
				for (int i = 0; i < testList.getLength(); i++) {
					Node test = testList.item(i);
					NodeList properties = test.getChildNodes();
					for (int j = 0; j < properties.getLength(); j++) {
						Node property = properties.item(j);
						String nodeName = property.getNodeName().trim();
						if (nodeName.toUpperCase().equals("RESULT")) {
							if (property.getTextContent().equals("success")) {
								result = true;
							} else {
								result = false;
							}
						}
					}
					Log.e("setAppointmentsAdd", "setAppointmentsAdd result : " + result);
				}
				// -------------------------------------------------
			} else {
				Log.e("setAppointmentsAdd","httpResponseError : " + httpResponse.getStatusLine());
			}
		} catch (SecurityException e) {
			Log.e("setAppointmentsAdd Error : ","SecurityException : " + e.getMessage());
		} catch (MalformedURLException e) {
			Log.e("setAppointmentsAdd Error : ","MalformedURLException : " + e.getMessage());
		} catch (IOException e) {
			Log.e("setAppointmentsAdd Error : ", "IOException : " + e.getMessage());
		} catch (NumberFormatException e) {
			Log.e("setAppointmentsAdd Error : ","NumberFormatException : " + e.getMessage());
		} catch (NullPointerException e) {
			Log.e("setAppointmentsAdd Error : ","NullPointerException : " + e.getMessage());
		} catch (Exception e) {
			Log.e("setAppointmentsAdd Error : ", "Exception : " + e.getMessage());
		}
		return result;
	}
	
	
	//============================ appointments area
	public ArrayList<AppointmentsDto> AppointmentsDtoList;
	
	public ArrayList<AppointmentsDto> getAppointmentsDtoList(){
		return AppointmentsDtoList;
	}
	// message out box
	public boolean setAppointmentsList() {
		String url = StaticData.PROMISE_LIST_URL;
		boolean result = false;
		AppointmentsDtoList = new ArrayList<AppointmentsDto>();
		try {
			HttpClient httpClient = new DefaultHttpClient();
			HttpParams params = httpClient.getParams();
			HttpConnectionParams.setConnectionTimeout(params, 10000);
			HttpConnectionParams.setSoTimeout(params, 10000);

			HttpPost httpPost = new HttpPost(url);

			List<NameValuePair> valuePairs = new ArrayList<NameValuePair>(2);
			valuePairs.add(new BasicNameValuePair("conference_id",StaticData.CONFERENCE_ID + ""));
			valuePairs.add(new BasicNameValuePair("user_cd", StaticData.USER_CD + ""));
			
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
				Log.e("setAppointmentsList","----------------------------------------");
				Log.e("setAppointmentsList","xmlDocument : "+xmlDocument);

				DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
				DocumentBuilder builder = factory.newDocumentBuilder();
				Document document = builder.parse(new InputSource(new StringReader(xmlDocument.toString())));
				NodeList testList = document.getElementsByTagName("PROMISE_LIST");
				for (int i = 0; i < testList.getLength(); i++) {
					AppointmentsDto dto = new AppointmentsDto();
					Node test = testList.item(i);
					NodeList properties = test.getChildNodes();
					for (int j = 0; j < properties.getLength(); j++) {
						Node property = properties.item(j);
						String nodeName = property.getNodeName().trim();
						if (nodeName.toUpperCase().equals("PROMISE_ID")) {
							dto.PROMISE_ID = Integer.parseInt(property.getTextContent());
						}else if(nodeName.toUpperCase().equals("FROM_USER_NAME")) {
							dto.FROM_USER_NAME = property.getTextContent();
						}else if(nodeName.toUpperCase().equals("FROM_USER_CD")) {
							dto.FROM_USER_CD = Integer.parseInt(property.getTextContent());
						}else if(nodeName.toUpperCase().equals("TO_USER")) {
							NodeList toUserlist = property.getChildNodes();
							Log.e("", "toUserlist.getLength() : "+toUserlist.getLength());
							TOUserDTO subDto = new TOUserDTO();
							for(int k=0; k<toUserlist.getLength(); k++){
								Node toUser = toUserlist.item(k);
								String userName = toUser.getNodeName().trim();
								Log.e("", "userName : "+userName);
								if(userName.toUpperCase().equals("TO_USER_NAME")){
									subDto.TO_USER_NAME = toUser.getTextContent();
								}else if(userName.toUpperCase().equals("TO_USER_CD")){
									subDto.TO_USER_CD = Integer.parseInt(toUser.getTextContent());
								}
							}
							dto.toUserList.add(subDto);
						}else if(nodeName.toUpperCase().equals("CANCLE_STAT")) {
							dto.CANCLE_STAT = property.getTextContent();
						}else if(nodeName.toUpperCase().equals("PROMISE_DATE")) {
							dto.PROMISE_DATE = property.getTextContent();
						}else if(nodeName.toUpperCase().equals("PROMISE_HOUR")) {
							dto.PROMISE_HOUR = property.getTextContent();
						}else if(nodeName.toUpperCase().equals("TITLE")) {
							dto.TITLE = property.getTextContent();
						}else if(nodeName.toUpperCase().equals("RECEIPT")) {
							dto.RECEIPT = property.getTextContent();
						}else if(nodeName.toUpperCase().equals("REG_DATE")) {
							dto.REG_DATE = property.getTextContent();
						}
					}
					AppointmentsDtoList.add(dto);
				}
				Log.e("setAppointmentsList","AppointmentsDtoList.size : " + AppointmentsDtoList.size());
				result = true;
				// -------------------------------------------------
			} else {
				Log.e("setAppointmentsList","httpResponseError : " + httpResponse.getStatusLine());
			}
		} catch (SecurityException e) {
			Log.e("setAppointmentsList Error : ","SecurityException : " + e.getMessage());
		} catch (MalformedURLException e) {
			Log.e("setAppointmentsList Error : ","MalformedURLException : " + e.getMessage());
		} catch (IOException e) {
			Log.e("setAppointmentsList Error : ", "IOException : " + e.getMessage());
		} catch (NumberFormatException e) {
			Log.e("setAppointmentsList Error : ","NumberFormatException : " + e.getMessage());
		} catch (NullPointerException e) {
			Log.e("setAppointmentsList Error : ","NullPointerException : " + e.getMessage());
		} catch (Exception e) {
			Log.e("setAppointmentsList Error : ", "Exception : " + e.getMessage());
		}
		return result;
	}
	
	// appointments show area
	public AppointmentsShowDto appointmentsShowDto;
	
	public AppointmentsShowDto getAppointmentsShowDto(){
		return appointmentsShowDto; 
	}
	
	public boolean setAppointmentsShowDto(int promise_id){
		String url = StaticData.PROMISE_DETAIL_URL;
		boolean result = false;
		appointmentsShowDto = new AppointmentsShowDto();
		try {
			HttpClient httpClient = new DefaultHttpClient();
			HttpParams params = httpClient.getParams();
			HttpConnectionParams.setConnectionTimeout(params, 10000);
			HttpConnectionParams.setSoTimeout(params, 10000);

			HttpPost httpPost = new HttpPost(url);

			List<NameValuePair> valuePairs = new ArrayList<NameValuePair>(2);
			valuePairs.add(new BasicNameValuePair("conference_id",StaticData.CONFERENCE_ID + ""));
			valuePairs.add(new BasicNameValuePair("promise_id", promise_id + ""));
			
			Log.e("setAppointmentsShowDto", "conference_id : "+StaticData.CONFERENCE_ID);
			Log.e("setAppointmentsShowDto", "message_id :" +promise_id);
			Log.e("setAppointmentsShowDto", "url :" + url);
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
				Log.e("setAppointmentsShowDto","----------------------------------------");
				Log.e("setAppointmentsShowDto","xmlDocument : "+xmlDocument);

				DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
				DocumentBuilder builder = factory.newDocumentBuilder();
				Document document = builder.parse(new InputSource(new StringReader(xmlDocument.toString())));
				NodeList testList = document.getElementsByTagName("PROMISE_ITEM");
				for (int i = 0; i < testList.getLength(); i++) {
					Node test = testList.item(i);
					NodeList properties = test.getChildNodes();
					for (int j = 0; j < properties.getLength(); j++) {
						Node property = properties.item(j);
						String nodeName = property.getNodeName().trim();
						if (nodeName.toUpperCase().equals("PROMISE_ID")) {
							appointmentsShowDto.PROMISE_ID = Integer.parseInt(property.getTextContent());
						}else if(nodeName.toUpperCase().equals("FROM_USER_NAME")) {
							appointmentsShowDto.FROM_USER_NAME = property.getTextContent();
						}else if(nodeName.toUpperCase().equals("FROM_USER_CD")) {
							appointmentsShowDto.FROM_USER_CD = Integer.parseInt(property.getTextContent());
						}else if(nodeName.toUpperCase().equals("TO_USER")) {
							NodeList toUserlist = property.getChildNodes();
							Log.e("", "toUserlist.getLength() : "+toUserlist.getLength());
							TOUserDTO subDto = new TOUserDTO();
							for(int k=0; k<toUserlist.getLength(); k++){
								Node toUser = toUserlist.item(k);
								String userName = toUser.getNodeName().trim();
								Log.e("", "userName : "+userName);
								if(userName.toUpperCase().equals("TO_USER_NAME")){
									subDto.TO_USER_NAME = toUser.getTextContent();
								}else if(userName.toUpperCase().equals("TO_USER_CD")){
									subDto.TO_USER_CD = Integer.parseInt(toUser.getTextContent());
								}
							}
							appointmentsShowDto.toUserList.add(subDto);
						}else if(nodeName.toUpperCase().equals("PROMISE_DATE")) {
							appointmentsShowDto.PROMISE_DATE = property.getTextContent();
						}else if(nodeName.toUpperCase().equals("PROMISE_HOUR")) {
							appointmentsShowDto.PROMISE_HOUR = property.getTextContent();
						}else if(nodeName.toUpperCase().equals("TITLE")) {
							appointmentsShowDto.TITLE = property.getTextContent();
						}else if(nodeName.toUpperCase().equals("CONTENTS")) {
							appointmentsShowDto.CONTENTS = property.getTextContent();
						}else if(nodeName.toUpperCase().equals("REG_DATE")) {
							appointmentsShowDto.REG_DATE = property.getTextContent();
						}
					}
				}
				Log.e("setAppointmentsList","appointmentsShowDto : " + appointmentsShowDto);
				result = true;
				// -------------------------------------------------
			} else {
				Log.e("setAppointmentsShowDto","httpResponseError : " + httpResponse.getStatusLine());
			}
		} catch (SecurityException e) {
			Log.e("setAppointmentsShowDto Error : ","SecurityException : " + e.getMessage());
		} catch (MalformedURLException e) {
			Log.e("setAppointmentsShowDto Error : ","MalformedURLException : " + e.getMessage());
		} catch (IOException e) {
			Log.e("setMessageOutboxDetail Error : ", "IOException : " + e.getMessage());
		} catch (NumberFormatException e) {
			Log.e("setMessageOutboxDetail Error : ","NumberFormatException : " + e.getMessage());
		} catch (NullPointerException e) {
			Log.e("setAppointmentsShowDto Error : ","NullPointerException : " + e.getMessage());
		} catch (Exception e) {
			Log.e("setAppointmentsShowDto Error : ", "Exception : " + e.getMessage());
		}
		return result;
	}
	
	public boolean setDeletePromise(int promise_id) {
		String url = StaticData.PROMISE_DELETE_URL;
		// conference_id : params default to_user_cd from_user_cd contents
		boolean result = false;

		try {
			HttpClient httpClient = new DefaultHttpClient();
			HttpParams params = httpClient.getParams();
			HttpConnectionParams.setConnectionTimeout(params, 10000);
			HttpConnectionParams.setSoTimeout(params, 10000);

			HttpPost httpPost = new HttpPost(url);

			List<NameValuePair> valuePairs = new ArrayList<NameValuePair>(2);
			valuePairs.add(new BasicNameValuePair("user_cd",StaticData.USER_CD + ""));
			valuePairs.add(new BasicNameValuePair("promise_id", promise_id + ""));
			
			Log.e("delete promise", "StaticData.CONFERENCE_ID :"+StaticData.CONFERENCE_ID);
			Log.e("delete promise", "promise_id :"+promise_id);
			Log.e("delete promise", "url :"+url);
			
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
				Log.e("setDeletePromise","----------------------------------------");
				Log.e("setDeletePromise","xmlDocument : "+xmlDocument);

				DocumentBuilderFactory factory = DocumentBuilderFactory
						.newInstance();
				DocumentBuilder builder = factory.newDocumentBuilder();
				Document document = builder.parse(new InputSource(
						new StringReader(xmlDocument.toString())));

				NodeList testList = document.getElementsByTagName("PROMISE_DELETE");
				for (int i = 0; i < testList.getLength(); i++) {
					Node test = testList.item(i);
					NodeList properties = test.getChildNodes();
					for (int j = 0; j < properties.getLength(); j++) {
						Node property = properties.item(j);
						String nodeName = property.getNodeName().trim();
						if (nodeName.toUpperCase().equals("FLAG")) {
							if (property.getTextContent().equals("success")) {
								result = true;
							} else {
								result = false;
							}
						}
					}
					Log.e("setDeleteMessage", "setdeletepromise result : " + result);
				}
				// -------------------------------------------------
			} else {
				Log.e("setDeleteMessage","httpResponseError : " + httpResponse.getStatusLine());
			}
		} catch (SecurityException e) {
			Log.e("setDeleteMessage Error : ","SecurityException : " + e.getMessage());
		} catch (MalformedURLException e) {
			Log.e("setDeleteMessage Error : ","MalformedURLException : " + e.getMessage());
		} catch (IOException e) {
			Log.e("setDeleteMessage Error : ", "IOException : " + e.getMessage());
		} catch (NumberFormatException e) {
			Log.e("setDeleteMessage Error : ","NumberFormatException : " + e.getMessage());
		} catch (NullPointerException e) {
			Log.e("setDeleteMessage Error : ","NullPointerException : " + e.getMessage());
		} catch (Exception e) {
			Log.e("setDeleteMessage Error : ", "Exception : " + e.getMessage());
		}
		return result;
	}
	
}
