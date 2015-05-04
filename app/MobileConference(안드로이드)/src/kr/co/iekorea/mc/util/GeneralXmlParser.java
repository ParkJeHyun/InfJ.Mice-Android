package kr.co.iekorea.mc.util;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import kr.co.iekorea.mc.xml.LoginDto;

import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import android.app.Activity;
import android.util.Log;

/** Getting xml document in content category process routine */
public class GeneralXmlParser 
{	
// XML Resource location(push message first line)
	public static final int LOCATION_HTTP_SERVER = 0;
	public static final int LOCATION_LOCAL_STRING = 1;
	
// XML Root Elements names
	public static final String LOGIN = "LOGIN";
	public static final String AGENDACOUNT = "AGENDACOUNT";
	
// XML Root Element Flag
	public static final int FLAG_LOGIN = 0;
	
// Object Member Field
	Activity activity;														// Called Present Activity
	String xmlUrl;															// Responded XML URL
	private String xmlDocument;												// Local based XML Document
	
	private String rootElement;												// RootElement
	private Object receivedContent;											// XML DOM Parsing after generated object
	
// HTTPConnection Object
	private URL url;												
	private URLConnection urlconn;			
	private HttpURLConnection httpconn;
	private int responseCode;
	private InputStream iStream;
	
// XML DOM Parser Object
	private Document xmlDoc = null;											// DOM Object
    private DocumentBuilder builder;										// Document Builder
    private DocumentBuilderFactory factory;									// DOM Object Factory
    private Element root;													// Root element (DOM Object)
    private NodeList nodeItems;												// Node Items
    
    
/** From web source */
	public GeneralXmlParser(String xmlUrl, String rootElement, boolean fromHttp)
	{
		this.xmlUrl = xmlUrl;
		this.rootElement = rootElement;
		
	    try
	    {
	    	this.url = new URL(xmlUrl);
	    	
	    // HTTP URL Connection & Getting XML Documents
			  this.urlconn = url.openConnection();														// URL Object
			  this.httpconn= (HttpURLConnection)urlconn;											// HTTP Connection Source
			  this.httpconn.setConnectTimeout(10000);												// HTTP Timeout
			  this.httpconn.setReadTimeout(10000);													// HTTP Read timeout
			  this.iStream = httpconn.getInputStream();												// HTTP Read Stream
			  this.responseCode = httpconn.getResponseCode();								// HTTP response code(Server Answer)
			  
		//	HTTP Response Code is OK then parsing start
			  if (responseCode == HttpURLConnection.HTTP_OK) 
			  {
				  this.factory = DocumentBuilderFactory.newInstance();
				  this.factory.setCoalescing(true);														//	<[CDATA[[ ]]> Enable
				  this.builder = this.factory.newDocumentBuilder();
				  this.xmlDoc = this.builder.parse(this.iStream);
				  this.xmlDoc.normalize();
				  
				  // DOM Document is valid to operation start 
		    	  	if ( xmlDoc != null) 
		    	  	{
		    	  		this.root = xmlDoc.getDocumentElement();
		    	  		this.nodeItems = root.getElementsByTagName(this.rootElement);
		    	  		this.receivedContent = this.setProcessRoutine();
		    	  	}
			  }
	    }
	    catch(NullPointerException e) {
	    	Log.e("General_Parser", "Null PointerExpcetion : "+e.getMessage());
	    }
	    catch(DOMException e) {
	    	Log.e("General_Parser", "DOMException : "+e.getMessage());
	    }
	    catch(Exception e) {
	    	Log.e("General_Parser", "Exception :"+e.getMessage());
	    }
	    finally
	    {
	    	try{
	    		this.iStream.close();
	    	}catch(Exception e){}
	    }
	}
	
/** From String variable source */
	public GeneralXmlParser(String xmlDocument, String rootElement)
	{
		this.activity = null;
		this.xmlUrl = null;
		this.xmlDocument = xmlDocument;
		this.rootElement = rootElement;
		
		try
		{
			this.factory = DocumentBuilderFactory.newInstance();
			this.factory.setCoalescing(true);														//	<CDATA> Enable
			this.builder = this.factory.newDocumentBuilder();
			
			this.iStream = new ByteArrayInputStream(this.xmlDocument.getBytes("utf-8"));
			this.xmlDoc = this.builder.parse(this.iStream);
			this.xmlDoc.normalize();
			
		// DOM Document is valid to operation start 
	  		if ( xmlDoc != null) 
	  		{
	  			this.root = xmlDoc.getDocumentElement();
	  			this.nodeItems = root.getElementsByTagName(this.rootElement);
	  			this.receivedContent = this.setProcessRoutine();
	  		}
		}
	    catch(NullPointerException e) {
	    	Log.e("General_Parser", "Null PointerExpcetion : "+e.getMessage());
	    }
	    catch(DOMException e) {
	    	Log.e("General_Parser", "DOMException : "+e.getMessage());
	    }
	    catch(Exception e) {
	    	Log.e("General_Parser", "Exception :"+e.getMessage());
	    }
	    finally
	    {
    		try{
    			this.iStream.close();
    		}catch(Exception e){}
	    }
	}
	
/**Classified by Root element parsing process */
	public ArrayList<?> setProcessRoutine()
	{
		ArrayList<?> returnObject=null;
		
		Log.e("Current Root Element", this.rootElement);
		if(this.rootElement.trim().toUpperCase().equals(GeneralXmlParser.LOGIN)) {
			returnObject = this.loginParser();
		}else if(this.rootElement.trim().toUpperCase().equals(GeneralXmlParser.AGENDACOUNT)){
			returnObject = this.agendaCountParser();
		}
		
		return returnObject;
	}
		
/** request target object type verifying after "true or false" return*/
//	public static boolean isObject(Object object, String xmlRootElementName) 
//	{
//	// ALARM
//		if(xmlRootElementName.toUpperCase().equals(GeneralXmlParser.ALARM)) 
//		{
//			if(object.equals(ArrayList.class)) {
//				try {
//					@SuppressWarnings("unchecked")
//					Object subObject = ((ArrayList<Alarm>)object).get(0);
//					if(subObject.equals(Alarm.class)) {
//						return true;
//					}
//				}
//				catch(ClassCastException e) {}
//			}
//		}
//	}
	
	private ArrayList<LoginDto> loginParser()
	{
		Log.e("loginparser","loginparser");
		ArrayList<LoginDto> loginList = new ArrayList<LoginDto>();
		for (int i=0; i<this.nodeItems.getLength(); i++)
		{
			Node item = this.nodeItems.item(i);
			
		// Set root element of child(1st element)
			NodeList properties = item.getChildNodes();
			LoginDto Dto = new LoginDto();
			
			for(int j=0; j<properties.getLength(); j++)
			{
				Node property = properties.item(j);
				String nodeName = property.getNodeName();
				
				if(nodeName.toUpperCase().equals("LOGINFLAG")) {
					Dto.loginFlag = property.getTextContent();
				}else if(nodeName.toUpperCase().equals("USER_CD")){
					Dto.user_cd = Integer.parseInt(property.getTextContent());
				}
			}
			loginList.add(Dto);
		}
		return loginList;
	}
	
	private ArrayList<LoginDto> agendaCountParser()
	{
		Log.e("loginparser","loginparser");
		ArrayList<LoginDto> loginList = new ArrayList<LoginDto>();
		for (int i=0; i<this.nodeItems.getLength(); i++)
		{
			Node item = this.nodeItems.item(i);
			
		// Set root element of child(1st element)
			NodeList properties = item.getChildNodes();
			LoginDto Dto = new LoginDto();
			
			for(int j=0; j<properties.getLength(); j++)
			{
				Node property = properties.item(j);
				String nodeName = property.getNodeName();
				
				if(nodeName.toUpperCase().equals("LOGINFLAG")) {
					Dto.loginFlag = property.getTextContent();
				}
			}
			loginList.add(Dto);
		}
		return loginList;
	}
	
//	private ArrayList<TeamBoardDto> teamBoardParser()
//	{
//		Log.e("teamBoardParser","in here");
//		ArrayList<TeamBoardDto> boardList = new ArrayList<TeamBoardDto>();
//		
//		for (int i=0; i<this.nodeItems.getLength(); i++)
//		{
//			Node item = this.nodeItems.item(i);
//			
//		// Set root element of child(1st element)
//			NodeList properties = item.getChildNodes();
//			TeamBoardDto teamBoard = new TeamBoardDto();
//			
//			for(int j=0; j<properties.getLength(); j++)
//			{
//				Node property = properties.item(j);
//				String nodeName = property.getNodeName();
//				
//				if(nodeName.toUpperCase().equals("ROWNUM")) {
//					teamBoard.row_num = Integer.parseInt(property.getTextContent());
//				}
//				else if(nodeName.toUpperCase().equals("NAME")) {
//					teamBoard.name = property.getTextContent();
//				}
//				else if(nodeName.toUpperCase().equals("VALUE")) {
//					teamBoard.value = property.getTextContent();
//				}
//				else if(nodeName.toUpperCase().equals("TYPE")) {
//					teamBoard.type = Integer.parseInt(property.getTextContent());
//				}
//			}
//			boardList.add(teamBoard);
//		}
//		return boardList;
//	}

// Getter , Setter methods
	public Object getReceivedContent() {
		return receivedContent;
	}
	public void setReceivedContent(Object receivedContent) {
		this.receivedContent = receivedContent;
	}
}
