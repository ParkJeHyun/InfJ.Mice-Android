package kr.co.iekorea.mc.xml;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
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
import kr.co.iekorea.mc.util.GeneralXmlParser;

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

public class MCDao {
	Activity activity;
	Handler handler;
	String xmlDocument;

	public MCDao(Activity activity) {
		this.activity = activity;
	}

	public MCDao(Activity activity, Handler handler) {
		this.activity = activity;
	}

	/**
	 * @parem id,pw
	 * */
	public ArrayList<LoginDto> getLogin(String id, String pw,String pack) {
		String url = StaticData.LOGIN_URL;
		boolean result = false;
		ArrayList<LoginDto> LoginList = new ArrayList<LoginDto>();
		try {
			HttpClient httpClient = new DefaultHttpClient();
			HttpParams params = httpClient.getParams();
			HttpConnectionParams.setConnectionTimeout(params, 10000);
			HttpConnectionParams.setSoTimeout(params, 10000);

			HttpPost httpPost = new HttpPost(url);

			List<NameValuePair> valuePairs = new ArrayList<NameValuePair>(2);
			valuePairs.add(new BasicNameValuePair("id", id));
			valuePairs.add(new BasicNameValuePair("pw", pw));
			valuePairs.add(new BasicNameValuePair("app_key",pack ));
			
			Log.e("", "pack : "+pack);

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

				if (!xmlDocument.equals(null)) {
					Log.e("Received XML Document", "xml : " + xmlDocument);
					GeneralXmlParser parser = new GeneralXmlParser(xmlDocument,GeneralXmlParser.LOGIN);
					LoginList = (ArrayList<LoginDto>) parser.setProcessRoutine();
					result = true;
					// Logging
					if (LoginList.size() > 0) {
						Log.e("TeamNote", "Generated login Object.. Size is : "
								+ LoginList.size() + "");
					}
				}
			} else {
				Log.e("LoginButtonEvent",
						"httpResponseError : " + httpResponse.getStatusLine());
			}
		} catch (SecurityException e) {
			Log.e("Login Error : ", "SecurityException : " + e.getMessage());
		} catch (MalformedURLException e) {
			Log.e("Login Error : ", "MalformedURLException : " + e.getMessage());
		} catch (IOException e) {
			Log.e("Login Error : ", "IOException : " + e.getMessage());
		} catch (NumberFormatException e) {
			Log.e("Login Error : ", "NumberFormatException : " + e.getMessage());
		} catch (NullPointerException e) {
			Log.e("Login Error : ", "NullPointerException : " + e.getMessage());
		} catch (Exception e) {
			Log.e("Login Error : ", "Exception : " + e.getMessage());
		}
		return LoginList;
	}

	public boolean getAuthoriry(String id) {
		String url = StaticData.AUTHORIRY_URL;
		boolean result = false;

		try {
			HttpClient httpClient = new DefaultHttpClient();
			HttpParams params = httpClient.getParams();
			HttpConnectionParams.setConnectionTimeout(params, 10000);
			HttpConnectionParams.setSoTimeout(params, 10000);

			HttpPost httpPost = new HttpPost(url);

			List<NameValuePair> valuePairs = new ArrayList<NameValuePair>(2);
			valuePairs.add(new BasicNameValuePair("user_cd", StaticData.USER_CD+ ""));
			valuePairs.add(new BasicNameValuePair("platform", "android"));
			valuePairs.add(new BasicNameValuePair("push_key", StaticData.gcmRegistrationId));

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

				Log.e("getAuthoriry", xmlDocument + "");

				DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
				DocumentBuilder builder = factory.newDocumentBuilder();
				Document document = builder.parse(new InputSource(new StringReader(xmlDocument.toString())));

				NodeList testList = document.getElementsByTagName("LOGIN");
				Node test = testList.item(0);
				NodeList properties = test.getChildNodes();

				Log.e("MCDAO", "properties Size : " + properties.getLength());

				for (int j = 0; j < properties.getLength(); j++) {
					Node property = properties.item(j);
					String nodeName = property.getNodeName();

					if (nodeName.toUpperCase().equals("CONFERENCE_ID")) {
						StaticData.CONFERENCE_ID = Integer.parseInt(property
								.getTextContent());
					} else if (nodeName.toUpperCase().equals("CONFERENCE_NAME")) {
						StaticData.CONFERENCE_NAME = property.getTextContent();
					} else if (nodeName.toUpperCase().equals("CONFERENCE_BANNER")) {
						StaticData.CONFERENCE_BANNER = property.getTextContent();
					} else if (nodeName.toUpperCase().equals("CONFERENCE_QR_IMAGE")) {
						StaticData.CONFERENCE_QR_IMAGE = property.getTextContent();
					} else if (nodeName.toUpperCase().equals("CONFERENCE_INFO_IMAGE")) {
						StaticData.CONFERENCE_INFO_IMAGE = property.getTextContent();
					} else if (nodeName.toUpperCase().equals("RESEARCH_AUTHORITY")) {
						StaticData.RESEARCH_AUTHORITY = property.getTextContent();
					} else if (nodeName.toUpperCase().equals("QUESTION_AUTHORITY")) {
						StaticData.QUESTION_AUTHORITY = property.getTextContent();
					} else if (nodeName.toUpperCase().equals("START_DATE")) {
						StaticData.START_DATE = property.getTextContent();
					} else if (nodeName.toUpperCase().equals("END_DATE")) {
						StaticData.END_DATE = property.getTextContent();
					} else if (nodeName.toUpperCase().equals("AUTHORITY")) {
						NodeList authoriryList = property.getChildNodes();
						for (int i = 0; i < authoriryList.getLength(); i++) {
							Node authoriry = authoriryList.item(i);
							String authorirynodeName = authoriry.getNodeName();
							Log.e("MCDAO", "authorirynodeName : "+ authorirynodeName);
							if (authorirynodeName.toUpperCase().equals("AGENDA")) {
								StaticData.AGENDA = authoriry.getTextContent();
								StaticData.permission[0] = authoriry.getTextContent();
							} else if (authorirynodeName.toUpperCase().equals("BINDER")) {
								StaticData.BINDER = authoriry.getTextContent();
								StaticData.permission[1] = authoriry.getTextContent();
							} else if (authorirynodeName.toUpperCase().equals("SEARCH")) {
								StaticData.SEARCH = authoriry.getTextContent();
								StaticData.permission[7] = authoriry.getTextContent();
							} else if (authorirynodeName.toUpperCase().equals("MESSAGE")) {
								StaticData.MESSAGE = authoriry.getTextContent();
								StaticData.permission[3] = authoriry.getTextContent();
							} else if (authorirynodeName.toUpperCase().equals("MYBRIEFCASE")) {
								StaticData.MYBRIEFCASE = authoriry.getTextContent();
								StaticData.permission[2] = authoriry.getTextContent();
							} else if (authorirynodeName.toUpperCase().equals("MAP")) {
								StaticData.MAP = authoriry.getTextContent();
								StaticData.permission[5] = authoriry.getTextContent();
							} else if (authorirynodeName.toUpperCase().equals("RESEARCH")) {
								StaticData.RESEARCH = authoriry.getTextContent();
								StaticData.permission[4] = authoriry.getTextContent();
							} else if (authorirynodeName.toUpperCase().equals("CONFIGURATION")) {
								StaticData.CONFIGURATION = authoriry.getTextContent();
								StaticData.permission[8] = authoriry.getTextContent();
							} else if (authorirynodeName.toUpperCase().equals("BARCODE")) {
								StaticData.BARCODE = authoriry.getTextContent();
								StaticData.permission[6] = authoriry.getTextContent();
							}
						}
					}
				}

				result = true;
				Log.e("getAuthoriry","StaticData.CONFERENCE_ID : " + StaticData.CONFERENCE_ID);
				// Log.e("MCDAO", "data Size : "
				// +StaticData.CONFERENCE_ID+","+StaticData.MENU_1+","+StaticData.MENU_2);
			} else {
				Log.e("getAuthoriry",
						"httpResponseError : " + httpResponse.getStatusLine());
			}
		} catch (SecurityException e) {
			Log.e("getAuthoriry Error : ",
					"SecurityException : " + e.getMessage());
		} catch (MalformedURLException e) {
			Log.e("getAuthoriry Error : ",
					"MalformedURLException : " + e.getMessage());
		} catch (IOException e) {
			Log.e("getAuthoriry Error : ", "IOException : " + e.getMessage());
		} catch (NumberFormatException e) {
			Log.e("getAuthoriry Error : ",
					"NumberFormatException : " + e.getMessage());
		} catch (NullPointerException e) {
			Log.e("getAuthoriry Error : ",
					"NullPointerException : " + e.getMessage());
		} catch (Exception e) {
			Log.e("getAuthoriry Error : ", "Exception : " + e.getMessage());
		}
		return result;
	}

	private ArrayList<AgendaTimeItemsDto> timeList;
	private ArrayList<BinderDetailDto> binderDetailList;
	
	public String mConferenceDate;
	public String getConferenceDate(){
		return mConferenceDate;
	}
	public boolean getDetailAgenda(int session_id) {
		String url = StaticData.AGENDA_URL;
		boolean result = false;

		timeList = new ArrayList<AgendaTimeItemsDto>();

		try {
			HttpClient httpClient = new DefaultHttpClient();
			HttpParams params = httpClient.getParams();
			HttpConnectionParams.setConnectionTimeout(params, 10000);
			HttpConnectionParams.setSoTimeout(params, 10000);

			HttpPost httpPost = new HttpPost(url);

			List<NameValuePair> valuePairs = new ArrayList<NameValuePair>(2);
			valuePairs.add(new BasicNameValuePair("session_id", session_id + ""));
			Log.e("", "session_id : "+session_id);
			Log.e("", "url : "+url);

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

				Log.e("getDetailAgenda", xmlDocument + "");

				DocumentBuilderFactory factory = DocumentBuilderFactory
						.newInstance();
				DocumentBuilder builder = factory.newDocumentBuilder();
				Document document = builder.parse(new InputSource(
						new StringReader(xmlDocument.toString())));

				NodeList testList = document.getElementsByTagName("ROOT");
				Node test = testList.item(0);
				NodeList properties = test.getChildNodes();

				Log.e("MCDAO", "properties Size : " + properties.getLength());

				for (int j = 0; j < properties.getLength(); j++) {
					Node property = properties.item(j);
					String nodeName = property.getNodeName();
					// Log.e("MCDAO", "nodeName : " +nodeName);

					if (nodeName.toUpperCase().equals("CONFERENCE_DATE")) {
						mConferenceDate = property.getTextContent();
					} else if (nodeName.toUpperCase().equals("AGENDA")) {
						AgendaTimeItemsDto dto = new AgendaTimeItemsDto();
						NodeList sessionDetailList = property.getChildNodes();
						for (int i = 0; i < sessionDetailList.getLength(); i++) {

							Node sessionDetail = sessionDetailList.item(i);
							String sessionDetailName = sessionDetail.getNodeName();
							if (sessionDetailName.toUpperCase().equals("AGENDA_ID")) {
								dto.AGENDA_ID = Integer.parseInt(sessionDetail.getTextContent());
							} else if (sessionDetailName.toUpperCase().equals("SESSION_ID")) {
//								dto.SESSION_ID = Integer.parseInt(sessionDetail.getTextContent());
							} else if (sessionDetailName.toUpperCase().equals("USER_CD")) {
								dto.USER_CD = Integer.parseInt(sessionDetail.getTextContent());
							} else if (sessionDetailName.toUpperCase().equals("USER_NAME")) {
								dto.USER_NAME = sessionDetail.getTextContent();
							} else if (sessionDetailName.toUpperCase().equals("START_TIME")) {
								dto.START_TIME = sessionDetail.getTextContent();
							} else if (sessionDetailName.toUpperCase().equals("END_TIME")) {
								dto.END_TIME = sessionDetail.getTextContent();
							} else if (sessionDetailName.toUpperCase().equals("AGENDA_TITLE")) {
								dto.AGENDA_TITLE = sessionDetail.getTextContent();
							} else if (sessionDetailName.toUpperCase().equals("WRITER")) {
								dto.WRITER = sessionDetail.getTextContent();
							} else if (sessionDetailName.toUpperCase().equals("PRESENTER")) {
								dto.PRESENTER = sessionDetail.getTextContent();
							}
						}
						timeList.add(dto);
					}
				}

				result = true;

				// debug
				// Log.e("MCDAO", "timeList Size : " +timeList.size());
				// Log.e("MCDAO", "--------------------------");
				// for(int i=0; i<timeList.size(); i++){
				// Log.e("MCDAO", "timeList agenda_title : "
				// +timeList.get(i).agenda_detail_title);
				// }
				// Log.e("MCDAO", "--------------------------");

			} else {
				Log.e("getDetailAgenda",
						"httpResponseError : " + httpResponse.getStatusLine());
			}
		} catch (SecurityException e) {
			Log.e("getDetailAgenda Error : ",
					"SecurityException : " + e.getMessage());
		} catch (MalformedURLException e) {
			Log.e("getDetailAgenda Error : ",
					"MalformedURLException : " + e.getMessage());
		} catch (IOException e) {
			Log.e("getDetailAgenda Error : ", "IOException : " + e.getMessage());
		} catch (NumberFormatException e) {
			Log.e("getDetailAgenda Error : ",
					"NumberFormatException : " + e.getMessage());
		} catch (NullPointerException e) {
			Log.e("getDetailAgenda Error : ",
					"NullPointerException : " + e.getMessage());
		} catch (Exception e) {
			Log.e("getDetailAgenda Error : ", "Exception : " + e.getMessage());
		}
		return result;
	}

	// binder session detail
	public boolean getBinderSessionDetailAgenda(int session_id) {
		
		String url = StaticData.BINDER_URL;
		boolean result = false;

		binderDetailList = new ArrayList<BinderDetailDto>();

		try {
			HttpClient httpClient = new DefaultHttpClient();
			HttpParams params = httpClient.getParams();
			HttpConnectionParams.setConnectionTimeout(params, 10000);
			HttpConnectionParams.setSoTimeout(params, 10000);

			HttpPost httpPost = new HttpPost(url);

			List<NameValuePair> valuePairs = new ArrayList<NameValuePair>(2);
			valuePairs.add(new BasicNameValuePair("session_id", session_id + ""));

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

				Log.e("getBinderSessionDetailAgenda", xmlDocument + "");

				DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
				DocumentBuilder builder = factory.newDocumentBuilder();
				Document document = builder.parse(new InputSource(new StringReader(xmlDocument.toString())));

				NodeList testList = document.getElementsByTagName("BINDER");
				for(int i =0; i<testList.getLength();i++){
					Node test = testList.item(i);
					NodeList properties = test.getChildNodes();
					Log.e("getBinderSessionDetailAgenda", "properties Size : "+ properties.getLength());
					
					BinderDetailDto dto = new BinderDetailDto();
					for (int j = 0; j < properties.getLength(); j++) {
						Node property = properties.item(j);
						String nodeName = property.getNodeName();
//						Log.e("getBinderSessionDetailAgenda", "rootNodeName : "+ nodeName);
						// Log.e("MCDAO", "nodeName : " +nodeName);

						if (nodeName.toUpperCase().equals("BINDER_ID")) {
							dto.BINDER_ID = Integer.parseInt(property
									.getTextContent());
						} else if (nodeName.toUpperCase().equals("SESSION_ID")) {
							dto.SESSION_ID = Integer.parseInt(property
									.getTextContent());
						} else if (nodeName.toUpperCase().equals("USER_CD")) {
							dto.USER_CD = Integer.parseInt(property
									.getTextContent());
						} else if (nodeName.toUpperCase().equals("USER_NAME")) {
							dto.USER_NAME = property.getTextContent();
						} else if (nodeName.toUpperCase()
								.equals("BINDER_TITLE")) {
							dto.BINDER_TITLE = property.getTextContent();
						} else if (nodeName.toUpperCase().equals("WRITER")) {
							dto.WRITER = property.getTextContent();
						} else if (nodeName.toUpperCase().equals(
								"SESSION_TITLE")) {
							dto.SESSION_TITLE = property.getTextContent();
						}
					}
					binderDetailList.add(dto);
				}
				Log.e("getBinderSessionDetailAgenda", "binderDetailList size : "+binderDetailList.size());

				result = true;

				// debug
				// Log.e("MCDAO", "timeList Size : " +timeList.size());
				// Log.e("MCDAO", "--------------------------");
				// for(int i=0; i<timeList.size(); i++){
				// Log.e("MCDAO", "timeList agenda_title : "
				// +timeList.get(i).agenda_detail_title);
				// }
				// Log.e("MCDAO", "--------------------------");

			} else {
				Log.e("getBinderSessionDetailAgenda", "httpResponseError : "
						+ httpResponse.getStatusLine());
			}
		} catch (SecurityException e) {
			Log.e("getBinderSessionDetailAgenda Error : ",
					"SecurityException : " + e.getMessage());
		} catch (MalformedURLException e) {
			Log.e("getBinderSessionDetailAgenda Error : ",
					"MalformedURLException : " + e.getMessage());
		} catch (IOException e) {
			Log.e("getBinderSessionDetailAgenda Error : ",
					"IOException : " + e.getMessage());
		} catch (NumberFormatException e) {
			Log.e("getBinderSessionDetailAgenda Error : ",
					"NumberFormatException : " + e.getMessage());
		} catch (NullPointerException e) {
			Log.e("getBinderSessionDetailAgenda Error : ",
					"NullPointerException : " + e.getMessage());
		} catch (Exception e) {
			Log.e("getBinderSessionDetailAgenda Error : ",
					"Exception : " + e.getMessage());
		}
		return result;
	}

	public ArrayList<AgendaTimeItemsDto> getTiemList() {

		return timeList;
	}

	public ArrayList<BinderDetailDto> getbinderDetailList() {

		return binderDetailList;
	}

	// for agenda detail contents
	public AgendaContentsDto contentsDto;

	public boolean getDetailContents(int agenda_id) {
		String url = StaticData.AGENDA_CONTENTS_URL;
		boolean result = false;
		Log.e("getDetailContents", "agenda_id : "+agenda_id);

		try {
			HttpClient httpClient = new DefaultHttpClient();
			HttpParams params = httpClient.getParams();
			HttpConnectionParams.setConnectionTimeout(params, 10000);
			HttpConnectionParams.setSoTimeout(params, 10000);

			HttpPost httpPost = new HttpPost(url);

			List<NameValuePair> valuePairs = new ArrayList<NameValuePair>(2);
			valuePairs.add(new BasicNameValuePair("agenda_id",agenda_id + ""));

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

				Log.e("getDetailContents", xmlDocument + "");

				DocumentBuilderFactory factory = DocumentBuilderFactory
						.newInstance();
				DocumentBuilder builder = factory.newDocumentBuilder();
				Document document = builder.parse(new InputSource(
						new StringReader(xmlDocument.toString())));

				NodeList testList = document.getElementsByTagName("AGENDA_CONTENTS");
				for(int i =0; i< testList.getLength(); i++){
					Node test = testList.item(i);
					String testname = test.getNodeName();
//					Log.e("", "testnode name : "+testname);
					if(testname.toUpperCase().equals("AGENDA_CONTENTS")){
						contentsDto = new AgendaContentsDto();
						NodeList properties = test.getChildNodes();
						for (int j = 0; j < properties.getLength(); j++) {
							Node property = properties.item(j);
							String nodeName = property.getNodeName();

							if (nodeName.toUpperCase().equals("DATE")) {
							} else if (nodeName.toUpperCase().equals("AGENDA_ID")) {
								contentsDto.AGENDA_ID = Integer.parseInt(property.getTextContent());
							} else if (nodeName.toUpperCase().equals("USER_CD")) {
								contentsDto.USER_CD = Integer.parseInt(property.getTextContent());
							} else if (nodeName.toUpperCase().equals("SESSION_ID")) {
//								contentsDto.SESSION_ID = Integer.parseInt(property.getTextContent());
							} else if (nodeName.toUpperCase().equals("USER_NAME")) {
								contentsDto.USER_NAME = property.getTextContent();
							} else if (nodeName.toUpperCase().equals("START_TIME")) {
								contentsDto.START_TIME = property.getTextContent();
							} else if (nodeName.toUpperCase().equals("END_TIME")) {
								contentsDto.END_TIME = property.getTextContent();
							} else if (nodeName.toUpperCase().equals("AGENDA_TITLE")) {
								contentsDto.AGENDA_TITLE = property.getTextContent();
							} else if (nodeName.toUpperCase().equals("CONTENTS")) {
								contentsDto.CONTENTS = property.getTextContent();
							} else if (nodeName.toUpperCase().equals("SUMMARY")) {
								contentsDto.SUMMARY = property.getTextContent();
							} else if (nodeName.toUpperCase().equals("WRITER")) {
								contentsDto.WRITER = property.getTextContent();
							} else if (nodeName.toUpperCase().equals("PRESENTER")) {
								contentsDto.PRESENTER = property.getTextContent();
							} else if (nodeName.toUpperCase().equals("ATTACHED")) {
								contentsDto.ATTACHED = property.getTextContent();
							}
						}
						result = true;
					}
				}
			} else {
				Log.e("getDetailContents", "httpResponseError : "
						+ httpResponse.getStatusLine());
			}
		} catch (SecurityException e) {
			Log.e("getDetailContents Error : ",
					"SecurityException : " + e.getMessage());
		} catch (MalformedURLException e) {
			Log.e("getDetailContents Error : ",
					"MalformedURLException : " + e.getMessage());
		} catch (IOException e) {
			Log.e("getDetailContents Error : ",
					"IOException : " + e.getMessage());
		} catch (NumberFormatException e) {
			Log.e("getAuthoriry Error : ",
					"NumberFormatException : " + e.getMessage());
		} catch (NullPointerException e) {
			Log.e("getAuthoriry Error : ",
					"NullPointerException : " + e.getMessage());
		} catch (Exception e) {
			Log.e("getAuthoriry Error : ", "Exception : " + e.getMessage());
		}
		return result;
	}

	public AgendaContentsDto getContentsDto() {
		return contentsDto;
	}

	public BinderContentsDto binderContentsDto;

	public boolean getBinderDetailContents(int binder_id) {
		String url = StaticData.BINDER_CONTENTS_URL;
		boolean result = false;

		try {
			HttpClient httpClient = new DefaultHttpClient();
			HttpParams params = httpClient.getParams();
			HttpConnectionParams.setConnectionTimeout(params, 10000);
			HttpConnectionParams.setSoTimeout(params, 10000);

			HttpPost httpPost = new HttpPost(url);

			List<NameValuePair> valuePairs = new ArrayList<NameValuePair>(2);
			valuePairs.add(new BasicNameValuePair("binder_id",binder_id + ""));

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

				Log.e("getBinderDetailContents", xmlDocument + "");

				DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
				DocumentBuilder builder = factory.newDocumentBuilder();
				Document document = builder.parse(new InputSource(new StringReader(xmlDocument.toString())));

				NodeList testList = document.getElementsByTagName("BINDER");
				Node test = testList.item(0);
				NodeList properties = test.getChildNodes();

				Log.e("getBinderDetailContents", "properties Size : "+ properties.getLength());
				binderContentsDto = new BinderContentsDto();

				for (int j = 0; j < properties.getLength(); j++) {
					Node property = properties.item(j);
					String nodeName = property.getNodeName();

					if (nodeName.toUpperCase().equals("BINDER_ID")) {
						binderContentsDto.BINDER_ID = Integer.parseInt(property.getTextContent());
					} else if (nodeName.toUpperCase().equals("USER_CD")) {
						binderContentsDto.USER_CD = Integer.parseInt(property.getTextContent());
					} else if (nodeName.toUpperCase().equals("SESSION_ID")) {
						binderContentsDto.SESSION_ID = Integer.parseInt(property.getTextContent());
					} else if (nodeName.toUpperCase().equals("USER_NAME")) {
						binderContentsDto.USER_NAME = property.getTextContent();
					} else if (nodeName.toUpperCase().equals("BINDER_TITLE")) {
						binderContentsDto.BINDER_TITLE = property.getTextContent();
					} else if (nodeName.toUpperCase().equals("CONTENTS")) {
						binderContentsDto.CONTENTS = property.getTextContent();
					} else if (nodeName.toUpperCase().equals("WRITER")) {
						binderContentsDto.WRITER = property.getTextContent();
					} else if (nodeName.toUpperCase().equals("ATTACHED")) {
						binderContentsDto.ATTACHED = property.getTextContent();
					} 
				}
				result = true;
				Log.e("getBinderDetailContents", "ATTACHED : "+ binderContentsDto.ATTACHED);
			} else {
				Log.e("getBinderDetailContents", "httpResponseError : "+ httpResponse.getStatusLine());
			}
		} catch (SecurityException e) {
			Log.e("getBinderDetailContents Error : ", "SecurityException : "+ e.getMessage());
		} catch (MalformedURLException e) {
			Log.e("getBinderDetailContents Error : ",
					"MalformedURLException : " + e.getMessage());
		} catch (IOException e) {
			Log.e("getBinderDetailContents Error : ",
					"IOException : " + e.getMessage());
		} catch (NumberFormatException e) {
			Log.e("getBinderDetailContents Error : ",
					"NumberFormatException : " + e.getMessage());
		} catch (NullPointerException e) {
			Log.e("getBinderDetailContents Error : ", "NullPointerException : "
					+ e.getMessage());
		} catch (Exception e) {
			Log.e("getBinderDetailContents Error : ",
					"Exception : " + e.getMessage());
		}
		return result;
	}

	public BinderContentsDto getBinderContentsDto() {
		return binderContentsDto;
	}

	// for agenda list area
	private ArrayList<AgendaSessionDto> items;
	private ArrayList<BinderSessionListDto> binderItems;

	public ArrayList<AgendaSessionDto> getAgendaListItem() {
		return items;
	}

	public ArrayList<BinderSessionListDto> getBinderListItem() {
		return binderItems;
	}

	public boolean getAgendaList() {
		String url = StaticData.BINDER_SESSION_URL;
		boolean result = false;

		items = new ArrayList<AgendaSessionDto>();

		try {
			HttpClient httpClient = new DefaultHttpClient();
			HttpParams params = httpClient.getParams();
			HttpConnectionParams.setConnectionTimeout(params, 10000);
			HttpConnectionParams.setSoTimeout(params, 10000);

			HttpPost httpPost = new HttpPost(url);

			List<NameValuePair> valuePairs = new ArrayList<NameValuePair>(2);
			// type 설정해 줘야 함.
			valuePairs.add(new BasicNameValuePair("conference_id",StaticData.CONFERENCE_ID + ""));
			valuePairs.add(new BasicNameValuePair("gubun","agenda"));

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

				Log.e("getAgendaList", "xml : " + xmlDocument);

				// -------------------------------------------------

				DocumentBuilderFactory factory = DocumentBuilderFactory
						.newInstance();
				DocumentBuilder builder = factory.newDocumentBuilder();
				Document document = builder.parse(new InputSource(
						new StringReader(xmlDocument.toString())));

				NodeList testList = document.getElementsByTagName("DAY");
				Log.e("getAgendaList","testList size : " + testList.getLength());

				for (int f = 0; f < testList.getLength(); f++) {

					AgendaSessionDto dto = new AgendaSessionDto();

					Node test = testList.item(f);
					NodeList properties = test.getChildNodes();

					for (int j = 0; j < properties.getLength(); j++) {

						Node property = properties.item(j);
						String nodeName = property.getNodeName();

						if (nodeName.toUpperCase().equals("DATE")) {
							dto.date = property.getTextContent();
						} else if (nodeName.toUpperCase().equals("SESSION")) {
							NodeList sessionList = property.getChildNodes();
							AgendaSessionListDto dto2 = new AgendaSessionListDto();

							for (int i = 0; i < sessionList.getLength(); i++) {

								Node session = sessionList.item(i);
								String authorirynodeName = session.getNodeName();
								if (authorirynodeName.toUpperCase().equals("SESSION_TITLE")) {
									dto2.SESSION_TITLE = session.getTextContent();
								} else if (authorirynodeName.toUpperCase().equals("SESSION_ID")) {
									dto2.SESSION_ID = Integer.parseInt(session.getTextContent());
								}
							}

							dto.list.add(dto2);
						}
					}
					if(dto.date.equals("0000-00-00")){
					}else{
						items.add(dto);
					}
						
				}

				// for debug
				// Log.e("getAgendalist", "items size : "+items.size());
				// for(int i =0;i< items.size(); i++){
				// Log.e("getAgendalist",
				// "items.get(i).date : "+items.get(i).date);
				// for(int j=0;j<items.get(i).list.size(); j++){
				// Log.e("getAgendalist",
				// "items.get(i).list.get(j).AGENDA_TITLE : "+items.get(i).list.get(j).AGENDA_TITLE);
				// Log.e("getAgendalist",
				// "items.get(i).list.get(j).AGENDA_ID : "+items.get(i).list.get(j).AGENDA_ID);
				// }
				// }

				result = true;
				// -------------------------------------------------
			} else {
				Log.e("getAgendaList",
						"httpResponseError : " + httpResponse.getStatusLine());
			}
		} catch (SecurityException e) {
			Log.e("getAgendaList Error : ",
					"SecurityException : " + e.getMessage());
		} catch (MalformedURLException e) {
			Log.e("getAgendaList Error : ",
					"MalformedURLException : " + e.getMessage());
		} catch (IOException e) {
			Log.e("getAgendaList Error : ", "IOException : " + e.getMessage());
		} catch (NumberFormatException e) {
			Log.e("getAgendaList Error : ",
					"NumberFormatException : " + e.getMessage());
		} catch (NullPointerException e) {
			Log.e("getAgendaList Error : ",
					"NullPointerException : " + e.getMessage());
		} catch (Exception e) {
			Log.e("getAgendaList Error : ", "Exception : " + e.getMessage());
		}
		return result;
	}

	public boolean getBinderSessionList() {
		String url = StaticData.BINDER_SESSION_URL;
		boolean result = false;

		binderItems = new ArrayList<BinderSessionListDto>();

		try {
			HttpClient httpClient = new DefaultHttpClient();
			HttpParams params = httpClient.getParams();
			HttpConnectionParams.setConnectionTimeout(params, 10000);
			HttpConnectionParams.setSoTimeout(params, 10000);

			HttpPost httpPost = new HttpPost(url);

			List<NameValuePair> valuePairs = new ArrayList<NameValuePair>(2);

			// type 설정해 줘야 함.
			valuePairs.add(new BasicNameValuePair("conference_id",StaticData.CONFERENCE_ID + ""));
			valuePairs.add(new BasicNameValuePair("gubun","binder"));

			// debug area

			httpPost.setEntity(new UrlEncodedFormEntity(valuePairs, "UTF-8"));
			HttpResponse httpResponse = httpClient.execute(httpPost);

			if (httpResponse.getStatusLine().getStatusCode() < 400) {
				InputStream iStream = httpResponse.getEntity().getContent();
				byte[] bytes = new byte[4096];
	            ByteArrayOutputStream baos = new ByteArrayOutputStream();
	            while(true){
	                int red = iStream.read(bytes);
	                if(red < 0)
	                    break;
	                baos.write(bytes, 0, red);
	            }
	            String xmlData = baos.toString("utf-8");
	            baos.close();
				iStream.close();
				// -------------------------------------------------

				DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
				factory.setCoalescing(true);
				DocumentBuilder builder = factory.newDocumentBuilder();
				Document document = builder.parse(new InputSource(new StringReader(xmlData.toString())));
				document.normalize();

				NodeList testList = document.getElementsByTagName("SESSION");
				Log.e("getAgendaList","testList size : " + testList.getLength());

				for (int f = 0; f < testList.getLength(); f++) {


					Node test = testList.item(f);
					NodeList properties = test.getChildNodes();
					BinderSessionListDto dto = new BinderSessionListDto();

					for (int j = 0; j < properties.getLength(); j++) {

						Node property = properties.item(j);
						String nodeName = property.getNodeName();

						if (nodeName.toUpperCase().equals("SESSION_TITLE")) {
							dto.SESSION_TITLE = property.getTextContent();
						} else if (nodeName.toUpperCase().equals("SESSION_ID")) {
							dto.SESSION_ID = Integer.parseInt(property.getTextContent());
						}
					}
					binderItems.add(dto);
				}

				// for debug
				// Log.e("getAgendalist", "items size : "+items.size());
				// for(int i =0;i< items.size(); i++){
				// Log.e("getAgendalist",
				// "items.get(i).date : "+items.get(i).date);
				// for(int j=0;j<items.get(i).list.size(); j++){
				// Log.e("getAgendalist",
				// "items.get(i).list.get(j).AGENDA_TITLE : "+items.get(i).list.get(j).AGENDA_TITLE);
				// Log.e("getAgendalist",
				// "items.get(i).list.get(j).AGENDA_ID : "+items.get(i).list.get(j).AGENDA_ID);
				// }
				// }

				result = true;
				// -------------------------------------------------
			} else {
				Log.e("getAgendaList",
						"httpResponseError : " + httpResponse.getStatusLine());
			}
		} catch (SecurityException e) {
			Log.e("getAgendaList Error : ",
					"SecurityException : " + e.getMessage());
		} catch (MalformedURLException e) {
			Log.e("getAgendaList Error : ",
					"MalformedURLException : " + e.getMessage());
		} catch (IOException e) {
			Log.e("getAgendaList Error : ", "IOException : " + e.getMessage());
		} catch (NumberFormatException e) {
			Log.e("getAgendaList Error : ",
					"NumberFormatException : " + e.getMessage());
		} catch (NullPointerException e) {
			Log.e("getAgendaList Error : ",
					"NullPointerException : " + e.getMessage());
		} catch (Exception e) {
			Log.e("getAgendaList Error : ", "Exception : " + e.getMessage());
		}
		return result;
	}

	// Binder data search

	public ArrayList<BinderDataSearchDto> binderDataSearchDtoList;

	public ArrayList<BinderDataSearchDto> getBinderDataSearchDtoList() {

		return binderDataSearchDtoList;
	}
	
	public boolean getBinderDataSearchList() {
		String url = StaticData.BINDER_SEARCH_URL;
		boolean result = false;
		binderDataSearchDtoList = new ArrayList<BinderDataSearchDto>();
		
		try{
			HttpClient httpClient = new DefaultHttpClient();
			HttpParams params = httpClient.getParams();
			HttpConnectionParams.setConnectionTimeout(params, 10000);
			HttpConnectionParams.setSoTimeout(params, 10000);

			HttpPost httpPost = new HttpPost(url);

			List<NameValuePair> valuePairs = new ArrayList<NameValuePair>(2);

			// type 설정해 줘야 함.
			valuePairs.add(new BasicNameValuePair("conference_id",StaticData.CONFERENCE_ID + ""));

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
				Log.e("getBinderDataSearchList", "----------------------------------------");
				
				DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
				DocumentBuilder builder = factory.newDocumentBuilder();
				Document document = builder.parse(new InputSource(new StringReader(xmlDocument.toString())));

				NodeList testList = document.getElementsByTagName("SEARCH_ITEM");
				for(int i =0; i<testList.getLength(); i++){
					Node test = testList.item(i);
					NodeList properties = test.getChildNodes();
					
					BinderDataSearchDto dto = new BinderDataSearchDto();
					for(int j = 0; j< properties.getLength(); j++){
						Node property = properties.item(j);
						String nodeName = property.getNodeName();
						if (nodeName.toUpperCase().equals("BINDER_ID")) {
							dto.BINDER_ID = Integer.parseInt(property.getTextContent());
						}else if(nodeName.toUpperCase().equals("USER_NAME")) {
							dto.USER_NAME = property.getTextContent();
						}else if(nodeName.toUpperCase().equals("BINDER_TITLE")) {
							dto.BINDER_TITLE = property.getTextContent();
						}else if(nodeName.toUpperCase().equals("WRITER")) {
							dto.WRITER = property.getTextContent();
						}else if(nodeName.toUpperCase().equals("SEX")) {
							dto.SEX = property.getTextContent(); 
						}else if(nodeName.toUpperCase().equals("COMPANY")) {
							dto.COMPANY = property.getTextContent();
						}else if(nodeName.toUpperCase().equals("NATION")) {
							dto.NATION = property.getTextContent();
						}
					}
					binderDataSearchDtoList.add(dto);
				}
				Log.e("getBinderDataSearchList","binderDataSearchDtoList size : " + binderDataSearchDtoList.size());
				result = true;
				// -------------------------------------------------
			} else {
				Log.e("getBinderDataSearchList","httpResponseError : " + httpResponse.getStatusLine());
			}
		} catch (SecurityException e) {
			Log.e("getBinderDataSearchList Error : ","SecurityException : " + e.getMessage());
		} catch (MalformedURLException e) {
			Log.e("getBinderDataSearchList Error : ","MalformedURLException : " + e.getMessage());
		} catch (IOException e) {
			Log.e("getBinderDataSearchList Error : ", "IOException : " + e.getMessage());
		} catch (NumberFormatException e) {
			Log.e("getBinderDataSearchList Error : ","NumberFormatException : " + e.getMessage());
		} catch (NullPointerException e) {
			Log.e("getBinderDataSearchList Error : ","NullPointerException : " + e.getMessage());
		} catch (Exception e) {
			Log.e("getBinderDataSearchList Error : ", "Exception : " + e.getMessage());
		}

		return result;
	}
	
	// get nation key
	public ArrayList<NationDto> nationDtoList;

	public ArrayList<NationDto> getnationDtoList() {

		return nationDtoList;
	}
	public boolean setNationDtoList() {
		String url = StaticData.NATION_URL;
		boolean result = false;
		nationDtoList = new ArrayList<NationDto>();
		
		try{
			HttpClient httpClient = new DefaultHttpClient();
			HttpParams params = httpClient.getParams();
			HttpConnectionParams.setConnectionTimeout(params, 10000);
			HttpConnectionParams.setSoTimeout(params, 10000);

			HttpPost httpPost = new HttpPost(url);

			List<NameValuePair> valuePairs = new ArrayList<NameValuePair>(2);

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
				Log.e("setNationDtoList", "----------------------------------------");
				
				DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
				DocumentBuilder builder = factory.newDocumentBuilder();
				Document document = builder.parse(new InputSource(new StringReader(xmlDocument.toString())));

				NodeList testList = document.getElementsByTagName("NATIONAL_ITEM");
				for(int i =0; i<testList.getLength(); i++){
					Node test = testList.item(i);
					NodeList properties = test.getChildNodes();
					
					NationDto dto = new NationDto();
					for(int j = 0; j< properties.getLength(); j++){
						Node property = properties.item(j);
						String nodeName = property.getNodeName();
						if (nodeName.toUpperCase().equals("NATIONAL_ID")) {
							dto.NATIONAL_ID = Integer.parseInt(property.getTextContent());
						}else if(nodeName.toUpperCase().equals("NATIONAL_NAME")) {
							dto.NATIONAL_NAME = property.getTextContent();
						}
					}
					nationDtoList.add(dto);
				}
				Log.e("setNationDtoList","setNationDtoList size : " + nationDtoList.size());
				result = true;
				// -------------------------------------------------
			} else {
				Log.e("setNationDtoList","httpResponseError : " + httpResponse.getStatusLine());
			}
		} catch (SecurityException e) {
			Log.e("setNationDtoList Error : ","SecurityException : " + e.getMessage());
		} catch (MalformedURLException e) {
			Log.e("setNationDtoList Error : ","MalformedURLException : " + e.getMessage());
		} catch (IOException e) {
			Log.e("setNationDtoList Error : ", "IOException : " + e.getMessage());
		} catch (NumberFormatException e) {
			Log.e("setNationDtoList Error : ","NumberFormatException : " + e.getMessage());
		} catch (NullPointerException e) {
			Log.e("setNationDtoList Error : ","NullPointerException : " + e.getMessage());
		} catch (Exception e) {
			Log.e("setNationDtoList Error : ", "Exception : " + e.getMessage());
		}

		return result;
	}
	
	public boolean setNationDtoList(int conf) {
		String url = StaticData.NATION_URL;
		boolean result = false;
		nationDtoList = new ArrayList<NationDto>();
		
		try{
			HttpClient httpClient = new DefaultHttpClient();
			HttpParams params = httpClient.getParams();
			HttpConnectionParams.setConnectionTimeout(params, 10000);
			HttpConnectionParams.setSoTimeout(params, 10000);

			HttpPost httpPost = new HttpPost(url);

			List<NameValuePair> valuePairs = new ArrayList<NameValuePair>(2);
			valuePairs.add(new BasicNameValuePair("conference_id",StaticData.CONFERENCE_ID + ""));
			
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
				Log.e("setNationDtoList", "----------------------------------------");
				
				DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
				DocumentBuilder builder = factory.newDocumentBuilder();
				Document document = builder.parse(new InputSource(new StringReader(xmlDocument.toString())));

				NodeList testList = document.getElementsByTagName("NATIONAL_ITEM");
				for(int i =0; i<testList.getLength(); i++){
					Node test = testList.item(i);
					NodeList properties = test.getChildNodes();
					
					NationDto dto = new NationDto();
					for(int j = 0; j< properties.getLength(); j++){
						Node property = properties.item(j);
						String nodeName = property.getNodeName();
						if (nodeName.toUpperCase().equals("NATIONAL_ID")) {
							dto.NATIONAL_ID = Integer.parseInt(property.getTextContent());
						}else if(nodeName.toUpperCase().equals("NATIONAL_NAME")) {
							dto.NATIONAL_NAME = property.getTextContent();
						}
					}
					nationDtoList.add(dto);
				}
				Log.e("setNationDtoList","setNationDtoList size : " + nationDtoList.size());
				result = true;
				// -------------------------------------------------
			} else {
				Log.e("setNationDtoList","httpResponseError : " + httpResponse.getStatusLine());
			}
		} catch (SecurityException e) {
			Log.e("setNationDtoList Error : ","SecurityException : " + e.getMessage());
		} catch (MalformedURLException e) {
			Log.e("setNationDtoList Error : ","MalformedURLException : " + e.getMessage());
		} catch (IOException e) {
			Log.e("setNationDtoList Error : ", "IOException : " + e.getMessage());
		} catch (NumberFormatException e) {
			Log.e("setNationDtoList Error : ","NumberFormatException : " + e.getMessage());
		} catch (NullPointerException e) {
			Log.e("setNationDtoList Error : ","NullPointerException : " + e.getMessage());
		} catch (Exception e) {
			Log.e("setNationDtoList Error : ", "Exception : " + e.getMessage());
		}

		return result;
	}

	
	// sponsor list
	public ArrayList<SponsorDto> sponsorList;
	public ArrayList<SponsorDto> getSponsorList(){
		return sponsorList;
	}
	
	public boolean setSponsorList() {
		boolean result = false;
		// --------------------------------------------------------------------------------------------
		String url = StaticData.SPONSOR_URL;
		sponsorList = new ArrayList<SponsorDto>();

		try {
			HttpClient httpClient = new DefaultHttpClient();
			HttpParams params = httpClient.getParams();
			HttpConnectionParams.setConnectionTimeout(params, 10000);
			HttpConnectionParams.setSoTimeout(params, 10000);

			HttpPost httpPost = new HttpPost(url);

			List<NameValuePair> valuePairs = new ArrayList<NameValuePair>(2);
			valuePairs.add(new BasicNameValuePair("conference_id",StaticData.CONFERENCE_ID + ""));
			Log.e("setSponsorList", "StaticData.CONFERENCE_ID : "+StaticData.CONFERENCE_ID);

			// debug area
			httpPost.setEntity(new UrlEncodedFormEntity(valuePairs));
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
				Log.e("setSponsorList","xmlDocument : " + xmlDocument);
				DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
				DocumentBuilder builder = factory.newDocumentBuilder();
				Document document = builder.parse(new InputSource(new StringReader(xmlDocument.toString())));
//				factory.setCoalescing(true);
//				document.normalize();
	
				NodeList testList = document.getElementsByTagName("SPONSOR");
				for(int i =0; i<testList.getLength(); i++){
					Node test = testList.item(i);
					NodeList properties = test.getChildNodes();
					
					SponsorDto dto = new SponsorDto();
					for(int j = 0; j< properties.getLength(); j++){
						Node property = properties.item(j);
						String nodeName = property.getNodeName();
						if (nodeName.toUpperCase().equals("BANNER_IMAGE")) {
							dto.BANNER_IMAGE = property.getTextContent();
						}else if(nodeName.toUpperCase().equals("DETAIL_IMAGE")) {
							dto.DETAIL_IMAGE = property.getTextContent();
						}
					}
					sponsorList.add(dto);
				}
				Log.e("setSponsorList","sponsorList size : " + sponsorList.size());
				result = true;
				// -------------------------------------------------
			} else {
				Log.e("setSponsorList","httpResponseError : " + httpResponse.getStatusLine());
			}
		} catch (SecurityException e) {
			Log.e("setSponsorList Error : ","SecurityException : " + e.getMessage());
		} catch (MalformedURLException e) {
			Log.e("setSponsorList Error : ","MalformedURLException : " + e.getMessage());
		} catch (IOException e) {
			Log.e("setSponsorList Error : ", "IOException : " + e.getMessage());
		} catch (NumberFormatException e) {
			Log.e("setSponsorList Error : ","NumberFormatException : " + e.getMessage());
		} catch (NullPointerException e) {
			Log.e("setSponsorList Error : ","NullPointerException : " + e.getMessage());
		} catch (Exception e) {
			Log.e("setSponsorList Error : ", "Exception : " + e.getMessage());
		}

		return result;
			
		// --------------------------------------------------------------------------------------------
		
//		sponsorList = new ArrayList<SponsorDto>();
//		
//		try{
//			HttpClient httpClient = new DefaultHttpClient();
//			HttpParams params = httpClient.getParams();
//			HttpConnectionParams.setConnectionTimeout(params, 10000);
//			HttpConnectionParams.setSoTimeout(params, 10000);
//
//			HttpPost httpPost = new HttpPost(url);
//
//			List<NameValuePair> valuePairs = new ArrayList<NameValuePair>(2);
//
//			// debug area
//			httpPost.setEntity(new UrlEncodedFormEntity(valuePairs, "UTF-8"));
//			valuePairs.add(new BasicNameValuePair("conference_id",StaticData.CONFERENCE_ID+""));
//			Log.e("getSponsorList", "conference_id : "+StaticData.CONFERENCE_ID);
//			
//			HttpResponse httpResponse = httpClient.execute(httpPost);
//
//			if (httpResponse.getStatusLine().getStatusCode() < 400) {
//				InputStream iStream = httpResponse.getEntity().getContent();
//				InputStreamReader iStreamReader = new InputStreamReader(iStream,"utf-8");
//				
//				byte[] bytes = new byte[4096];
//	            ByteArrayOutputStream baos = new ByteArrayOutputStream();
//	            while(true){
//	                int red = iStream.read(bytes);
//	                if(red < 0)
//	                    break;
//	                baos.write(bytes, 0, red);
//	            }
//	            String xmlData = baos.toString("utf-8");
//	            baos.close();
//				Log.e("setSponsorList", "xmlData : "+xmlData);
//	            
//				iStream.close();
//
//				DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
////				factory.setCoalescing(true);
//				DocumentBuilder builder = factory.newDocumentBuilder();
//				Document document = builder.parse(new InputSource(new StringReader(xmlData.toString())));
//				document.normalize();
//
//				NodeList testList = document.getElementsByTagName("SPONSOR");
//				for(int i =0; i<testList.getLength(); i++){
//					Node test = testList.item(i);
//					NodeList properties = test.getChildNodes();
//					
//					SponsorDto dto = new SponsorDto();
//					for(int j = 0; j< properties.getLength(); j++){
//						Node property = properties.item(j);
//						String nodeName = property.getNodeName();
//						if (nodeName.toUpperCase().equals("BANNER_IMAGE")) {
//							dto.BANNER_IMAGE = property.getTextContent();
//						}else if(nodeName.toUpperCase().equals("DETAIL_IMAGE")) {
//							dto.DETAIL_IMAGE = property.getTextContent();
//						}
//					}
//					sponsorList.add(dto);
//				}
//				Log.e("getSponsorList","sponsorList size : " + sponsorList.size());
//				result = true;
//				// -------------------------------------------------
//			} else {
//				Log.e("getSponsorList","httpResponseError : " + httpResponse.getStatusLine());
//			}
//		} catch (SecurityException e) {
//			Log.e("getSponsorList Error : ","SecurityException : " + e.getMessage());
//		} catch (MalformedURLException e) {
//			Log.e("getSponsorList Error : ","MalformedURLException : " + e.getMessage());
//		} catch (IOException e) {
//			Log.e("getSponsorList Error : ", "IOException : " + e.getMessage());
//		} catch (NumberFormatException e) {
//			Log.e("getSponsorList Error : ","NumberFormatException : " + e.getMessage());
//		} catch (NullPointerException e) {
//			Log.e("getSponsorList Error : ","NullPointerException : " + e.getMessage());
//		} catch (Exception e) {
//			Log.e("getSponsorList Error : ", "Exception : " + e.getMessage());
//		}
//
//		return result;
	}
	
	
//	public boolean setSponsorList() {
//		String url = StaticData.SPONSOR_URL;
//		boolean result = false;
//		sponsorList = new ArrayList<SponsorDto>();
//		
//		try{
//			HttpClient httpClient = new DefaultHttpClient();
//			HttpParams params = httpClient.getParams();
//			HttpConnectionParams.setConnectionTimeout(params, 10000);
//			HttpConnectionParams.setSoTimeout(params, 10000);
//
//			HttpPost httpPost = new HttpPost(url);
//
//			List<NameValuePair> valuePairs = new ArrayList<NameValuePair>(2);
//
//			// debug area
//			httpPost.setEntity(new UrlEncodedFormEntity(valuePairs, "UTF-8"));
//			valuePairs.add(new BasicNameValuePair("conference_id",StaticData.CONFERENCE_ID+""));
//			Log.e("getSponsorList", "conference_id : "+StaticData.CONFERENCE_ID);
//			
//			HttpResponse httpResponse = httpClient.execute(httpPost);
//
//			if (httpResponse.getStatusLine().getStatusCode() < 400) {
//				InputStream iStream = httpResponse.getEntity().getContent();
//				InputStreamReader iStreamReader = new InputStreamReader(iStream,"utf-8");
//				
//				byte[] bytes = new byte[4096];
//	            ByteArrayOutputStream baos = new ByteArrayOutputStream();
//	            while(true){
//	                int red = iStream.read(bytes);
//	                if(red < 0)
//	                    break;
//	                baos.write(bytes, 0, red);
//	            }
//	            String xmlData = baos.toString("utf-8");
//	            baos.close();
//				Log.e("setSponsorList", "xmlData : "+xmlData);
//	            
//				iStream.close();
//
//				DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
////				factory.setCoalescing(true);
//				DocumentBuilder builder = factory.newDocumentBuilder();
//				Document document = builder.parse(new InputSource(new StringReader(xmlData.toString())));
//				document.normalize();
//
//				NodeList testList = document.getElementsByTagName("SPONSOR");
//				for(int i =0; i<testList.getLength(); i++){
//					Node test = testList.item(i);
//					NodeList properties = test.getChildNodes();
//					
//					SponsorDto dto = new SponsorDto();
//					for(int j = 0; j< properties.getLength(); j++){
//						Node property = properties.item(j);
//						String nodeName = property.getNodeName();
//						if (nodeName.toUpperCase().equals("BANNER_IMAGE")) {
//							dto.BANNER_IMAGE = property.getTextContent();
//						}else if(nodeName.toUpperCase().equals("DETAIL_IMAGE")) {
//							dto.DETAIL_IMAGE = property.getTextContent();
//						}
//					}
//					sponsorList.add(dto);
//				}
//				Log.e("getSponsorList","sponsorList size : " + sponsorList.size());
//				result = true;
//				// -------------------------------------------------
//			} else {
//				Log.e("getSponsorList","httpResponseError : " + httpResponse.getStatusLine());
//			}
//		} catch (SecurityException e) {
//			Log.e("getSponsorList Error : ","SecurityException : " + e.getMessage());
//		} catch (MalformedURLException e) {
//			Log.e("getSponsorList Error : ","MalformedURLException : " + e.getMessage());
//		} catch (IOException e) {
//			Log.e("getSponsorList Error : ", "IOException : " + e.getMessage());
//		} catch (NumberFormatException e) {
//			Log.e("getSponsorList Error : ","NumberFormatException : " + e.getMessage());
//		} catch (NullPointerException e) {
//			Log.e("getSponsorList Error : ","NullPointerException : " + e.getMessage());
//		} catch (Exception e) {
//			Log.e("getSponsorList Error : ", "Exception : " + e.getMessage());
//		}
//
//		return result;
//	}
	
	
	// vote
	public String getVoteState(int binder_id) {
		String url = StaticData.VOTE_URL;
		String result = "";

		try {
			HttpClient httpClient = new DefaultHttpClient();
			HttpParams params = httpClient.getParams();
			HttpConnectionParams.setConnectionTimeout(params, 10000);
			HttpConnectionParams.setSoTimeout(params, 10000);

			HttpPost httpPost = new HttpPost(url);

			List<NameValuePair> valuePairs = new ArrayList<NameValuePair>(2);
			valuePairs.add(new BasicNameValuePair("conference_id",StaticData.CONFERENCE_ID + ""));
			valuePairs.add(new BasicNameValuePair("user_cd",StaticData.USER_CD + ""));
			valuePairs.add(new BasicNameValuePair("binder_id",binder_id + ""));
			
			Log.e("getBinderDetailContents", "url" + url);
			Log.e("getBinderDetailContents", "conference_id" + StaticData.CONFERENCE_ID);
			Log.e("getBinderDetailContents", "StaticData.USER_CD" + StaticData.USER_CD);
			Log.e("getBinderDetailContents", "binder_id" + binder_id);
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

				Log.e("getBinderDetailContents", xmlDocument + "");

				DocumentBuilderFactory factory = DocumentBuilderFactory
						.newInstance();
				DocumentBuilder builder = factory.newDocumentBuilder();
				Document document = builder.parse(new InputSource(
						new StringReader(xmlDocument.toString())));

				NodeList testList = document.getElementsByTagName("VOTE");
				Node test = testList.item(0);
				NodeList properties = test.getChildNodes();

				Log.e("getBinderDetailContents", "properties Size : "+ properties.getLength());
				binderContentsDto = new BinderContentsDto();

				for (int j = 0; j < properties.getLength(); j++) {
					Node property = properties.item(j);
					String nodeName = property.getNodeName();

					if (nodeName.toUpperCase().equals("FLAG")) {
						result = property.getTextContent();
					}
				}
			} else {
				Log.e("getBinderDetailContents", "httpResponseError : "+ httpResponse.getStatusLine());
			}
		} catch (SecurityException e) {
			Log.e("getBinderDetailContents Error : ", "SecurityException : "+ e.getMessage());
		} catch (MalformedURLException e) {
			Log.e("getBinderDetailContents Error : ","MalformedURLException : " + e.getMessage());
		} catch (IOException e) {
			Log.e("getBinderDetailContents Error : ","IOException : " + e.getMessage());
		} catch (NumberFormatException e) {
			Log.e("getBinderDetailContents Error : ","NumberFormatException : " + e.getMessage());
		} catch (NullPointerException e) {
			Log.e("getBinderDetailContents Error : ", "NullPointerException : "+ e.getMessage());
		} catch (Exception e) {
			Log.e("getBinderDetailContents Error : ","Exception : " + e.getMessage());
		}
		return result;
	}
}
