package project_package;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;

public class RetrieveTicketsID {




   private static String readAll(Reader rd) throws IOException {
	      StringBuilder sb = new StringBuilder();
	      int cp;
	      while ((cp = rd.read()) != -1) {
	         sb.append((char) cp);
	      }
	      return sb.toString();
	   }


   public static JSONObject readJsonFromUrl(String url) throws IOException, JSONException {
      InputStream is = new URL(url).openStream();
      try {
         BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
         String jsonText = readAll(rd);
         JSONObject json = new JSONObject(jsonText);
         return json;
       } finally {
         is.close();
       }
   }


  
	public ArrayList<String> retrieveTicketsIDs() throws IOException, JSONException {
			   
		String projName ="LIBCLOUD";
		Integer j = 0, i = 0, total = 1;
		ArrayList<String> IDList = new ArrayList<String>();
	      //Get JSON API for closed bugs w/ AV in the project
		do {
	         //Only gets a max of 1000 at a time, so must do this multiple times if bugs >1000
	         j = i + 1000;
	         String url = "https://issues.apache.org/jira/rest/api/2/search?jql=project=%22"
	                + projName + "%22AND(%22status%22=%22closed%22OR"//%22issueType%22=%22Bug%22AND
	                + "%22status%22=%22resolved%22)AND%22resolution%22=%22fixed%22&fields=key,resolutiondate,versions,created&startAt="
	                + i.toString() + "&maxResults=" + j.toString();
	         JSONObject json = readJsonFromUrl(url);
	         JSONArray issues = json.getJSONArray("issues");
	         total = json.getInt("total");
	         for (; i < total && i < j; i++) {
	            //Iterate through each bug
	            String key = issues.getJSONObject(i%1000).get("key").toString();
	            IDList.add(key);
	         }  
	      } while (i < total);
	      return IDList;
	}
}
