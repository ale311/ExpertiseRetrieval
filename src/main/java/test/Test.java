package test;
import java.net.URL;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
 
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;
 
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
 
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashSet;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.neo4j.graphdb.GraphDatabaseService;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import neo4j.StartGraphDB;
import redis.clients.jedis.Jedis;

public class Test {
	
	

public static void main(String[] args) throws Exception {
    String url = ("http://api.elsevier.com/content/abstract/scopus_id/84942877228?apiKey=6492f9c867ddf3e84baa10b5971e3e3d");
//	String url = "http://cdn.crunchify.com/wp-content/uploads/code/CrunchifyXMLDocument.xml";
	String xml = crunchifyGetURLContents(url);
	
	try {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		Document document = builder.parse(new InputSource(new StringReader(xml)));

		XPathFactory xPathfactory = XPathFactory.newInstance();
		XPath xpath = xPathfactory.newXPath();
		XPathExpression expr = xpath
//				.compile("/company/company_name/company_item[@name='Facebook Members WordPress Plugin']");
				.compile("/abstracts-retrieval-response/coredata/description/abstract/para");
		String numberOfDownloads = expr.evaluate(document, XPathConstants.STRING).toString();
		System.out.println(numberOfDownloads);
	} catch (Exception e) {
		e.printStackTrace();
	}
} 
	
public static String crunchifyGetURLContents(String myURL) {
	System.out.println("crunchifyGetURLContents() is hitting : " + myURL);
	StringBuilder sb = new StringBuilder();
	URLConnection urlConn = null;
	InputStreamReader in = null;
	try {
		URL url = new URL(myURL);
		urlConn = url.openConnection();
		if (urlConn != null)
			urlConn.setReadTimeout(60 * 1000);
		if (urlConn != null && urlConn.getInputStream() != null) {
			in = new InputStreamReader(urlConn.getInputStream(), Charset.defaultCharset());
			BufferedReader bufferedReader = new BufferedReader(in);
			if (bufferedReader != null) {
				int cp;
				while ((cp = bufferedReader.read()) != -1) {
					sb.append((char) cp);
				}
				bufferedReader.close();
			}
		}
		in.close();
	} catch (Exception e) {
		throw new RuntimeException("Exception while calling URL:" + myURL, e);
	}

	return sb.toString();
}

	private static void inserisciAbstract() throws Exception {
		
		
		String uri =
			    "http://api.elsevier.com/content/abstract/scopus_id/84942877228?apiKey=6492f9c867ddf3e84baa10b5971e3e3d&field=dc:description"; 
			 
		

        
	}
	
		
		
//		
//			//			itero sulle stringhe nel mio insieme: sono gli ID dei documenti, da cui estraggo tutte le info necessarie
//			//			le memorizzo in opportune str.dati e le passo ad un metodo che inserirà opportunamente le info
//			URL url = new URL("http://api.elsevier.com/content/abstract/scopus_id/84942877228?apiKey=6492f9c867ddf3e84baa10b5971e3e3d&field=dc:description");
//			HttpURLConnection request = (HttpURLConnection) url.openConnection();
//			
//			
//			
//			
//			request.connect();
//			JsonElement jsonElement = new JsonParser().parse(new InputStreamReader((InputStream) request.getContent()));
//			
//			String extract = jsonElement.getClass()
////					jsonElement.getAsJsonObject()
////					.get("abstracts-retrieval-response")
////					.getAsJsonObject().get("coredata")
////					.getAsJsonObject().get("ce:para")
////					.getAsJsonObject()
////					.get("abstract")
//					.toString();
//			extract = extract.replaceAll("2015 Elsevier Ltd All rights reserved.", "");
//			//RISOLVERE PROBLEMA DEL RIMPIAZZAMENTO
//			System.out.println(extract);
//			i++;
//			extract = extract.replace("\\n", "");
//			extract = extract.replace("©", "");
//			extract = extract.substring(1, extract.length()-1);
//			extract = extract.trim();
			
	
}
