package authorOperations;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import neo4j.StartGraphDB;
import scopusextraction.XMLExtraction;

public class TextsByDocumentId {
	protected static String getTextByScopusId (String scopus_id, String APIKEY) throws IOException {
		URL url = new URL("http://api.elsevier.com/content/abstract/scopus_id/"+scopus_id+"?apiKey="+APIKEY+"&field=dc:description");
		String surl = (String) url.toString();
		String extract = XMLExtraction.estraiAbstract(surl);
		return extract;
	}
}
