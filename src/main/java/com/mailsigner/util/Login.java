package com.mailsigner.util;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class Login {
	
	public static String getXMLVar(String tag, Document doc){
		NodeList tagList = doc.getElementsByTagName(tag);
		Element tagElement = (Element) tagList.item(0);
		NodeList tagElementList = tagElement.getChildNodes();
		return tagElementList.item(0).getNodeValue();
	}
	
	/**
	 * @param args
	 * @throws IOException  Is thrown when the socket cannot connect to the host
	 */
	public String[] performLogin(String mail, String pass) throws IOException {
		HttpURLConnection connection = null;

		URL serverAddress = null;

		try {
			serverAddress = new URL("http://www.mailsigner.com/restLogin.php?user=" + mail + "&pass=" + pass);
			// set up out communications stuff
			connection = null;

			// Set up the initial connection
			connection = (HttpURLConnection) serverAddress.openConnection();
			connection.setRequestMethod("GET");
			connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 5.1; en-US; rv:1.9.2.8) Gecko/20100722 Firefox/3.6.8");
			connection.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
			connection.setRequestProperty("Accept-Language", "en-us,en;q=0.5");
			connection.setRequestProperty("Accept-Charset", "ISO-8859-1,utf-8;q=0.7,*;q=0.7");
			connection.setRequestProperty("Keep-Alive", "115");
			connection.setRequestProperty("Connection", "keep-alive");
			connection.setDoOutput(true);
			connection.setReadTimeout(10000);

			connection.connect();

			if(connection.getResponseCode() == HttpURLConnection.HTTP_UNAUTHORIZED){
				//TODO: log this error
			} else {
				DocumentBuilder docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
				Document doc = docBuilder.parse(connection.getInputStream());
				
				String[] registration = new String[4];
				
				registration[0] = getXMLVar("companyName", doc);
				registration[1] = getXMLVar("firstName", doc);
				registration[2] = getXMLVar("lastName", doc);
				registration[3] = getXMLVar("users", doc);
				
				return registration;
				
			}
			//TODO: handle these errors accordingly
		} catch (MalformedURLException e) {
			System.out.println("1");
		} catch (ProtocolException e) {
			System.out.println("2");
		} catch (SAXException e) {
			System.out.println("4");
		} catch (ParserConfigurationException e) {
			System.out.println("5");
		} finally {
			// close the connection, set all objects to null
			connection.disconnect();
			connection = null;
		}
		return null;
	}
}
