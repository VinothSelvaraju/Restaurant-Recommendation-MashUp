package ub.cse636.project.service;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;


public class PlacesService{
    private static final String LOG_TAG = "ExampleApp";
    private static final String PLACES_API_BASE = "https://maps.googleapis.com/maps/api/place";
    private static final String TYPE_AUTOCOMPLETE = "/autocomplete";
    private static final String TYPE_DETAILS = "/details";
    private static final String TYPE_SEARCH = "/search";
    private static final String TEXT_SEARCH = "/textsearch";
    private static final String OUT_JSON = "/json";

    // KEY!
    private static final String API_KEY = "AIzaSyBqxv1mclccpXKSqBRaVuev_F2FOXpMuC8";

    //parseJSON
    public static void parseJSON(String s){
        if(s != null){
            try{
                JSONObject jObject = new JSONObject(s);
                JSONObject firstJSONObj = jObject.getJSONObject("formatted_address");

                System.out.println(firstJSONObj.toString());
               
          }catch(JSONException je){
                System.out.println("JSON exception raised");
                je.printStackTrace();
          }

        }
    }

    //Text search service
    public static void textSearch(String input) {
        // ArrayList<Place> resultList = null;
        String query = input.replace(" ","+").trim();

        System.out.println("Processed query: " + query);

        HttpURLConnection conn = null;
        StringBuilder jsonResults = new StringBuilder();
        try {
            StringBuilder sb = new StringBuilder(PLACES_API_BASE);
            
            sb.append(TEXT_SEARCH);
            sb.append(OUT_JSON);
            sb.append("?query=" + query);
            sb.append("&key=" + API_KEY);
            
            System.out.println("formed URL :" + sb.toString());
           
            URL url = new URL(sb.toString());
            conn = (HttpURLConnection) url.openConnection();
            InputStreamReader in = new InputStreamReader(conn.getInputStream());

            int read;
            char[] buff = new char[1024];
            while ((read = in.read(buff)) != -1) {
                jsonResults.append(buff, 0, read);
            }

            System.out.println("Result: ");
            System.out.println(jsonResults.toString());

        } catch (MalformedURLException e) {
            // Log.e(LOG_TAG, "Error processing Places API URL", e);
           
        } catch (IOException e) {
            // Log.e(LOG_TAG, "Error connecting to Places API", e);
            
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }

    }
}