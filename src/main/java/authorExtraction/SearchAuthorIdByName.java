package authorExtraction;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashSet;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

public class SearchAuthorIdByName {
	protected static HashSet<Author> searchIDbyName(String lastName, String firstName, String affiliation,   String APIKEY) throws IOException{
		String authorID;
		HashSet<Author> result = new HashSet<Author>();
		lastName = lastName.replace(" ", "%20");
		firstName = firstName.replace(" ", "%20");
		affiliation = affiliation.replace(" " ,"%20");
		URL url = new URL("http://api.elsevier.com/content/search/author?query=authlast("+lastName+")%20and%20authfirst("+firstName+")%20and%20affil("+affiliation+")&apikey="+APIKEY+"&httpAccept=application/json");
		HttpURLConnection request = (HttpURLConnection) url.openConnection();
		request.connect();
		JsonElement jsonElement = new JsonParser().parse(new InputStreamReader((InputStream) request.getContent()));
		JsonElement pages = jsonElement.getAsJsonObject().get("search-results").getAsJsonObject().get("entry");
		JsonArray array = pages.getAsJsonArray();
		int i = 0;
		for(JsonElement jse : array){
			String id = jse.getAsJsonObject().get("dc:identifier").toString();
			id = id.substring(11, id.length()-1);
			
			String surname = jse.getAsJsonObject().get("preferred-name").getAsJsonObject().get("surname").toString();
			surname = surname.substring(1, surname.length()-1);
			
			String givenName = jse.getAsJsonObject().get("preferred-name").getAsJsonObject().get("given-name").toString();
			givenName = givenName.substring(1, givenName.length()-1);
			
			String affilId = jse.getAsJsonObject().get("affiliation-current").getAsJsonObject().get("affiliation-id").toString();
			affilId = affilId.substring(1, affilId.length()-1);
			
			String affilName = jse.getAsJsonObject().get("affiliation-current").getAsJsonObject().get("affiliation-name").toString();
			affilName = affilName.substring(1, affilName.length()-1);
			
			Author currentAuthor = new Author(id,givenName,surname,affilId, affilName);
			result.add(currentAuthor);
		}
		return result;
	}
}
