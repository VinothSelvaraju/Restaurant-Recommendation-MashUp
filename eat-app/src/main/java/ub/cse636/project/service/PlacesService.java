package ub.cse636.project.service;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import ub.cse636.project.Place;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;


public class PlacesService{
    
    private static final String PLACES_API_BASE = "https://maps.googleapis.com/maps/api/place";
    private static final String TYPE_AUTOCOMPLETE = "/autocomplete";
    private static final String TYPE_DETAILS = "/details";
    private static final String TYPE_SEARCH = "/search";
    private static final String TEXT_SEARCH = "/textsearch";
    private static final String OUT_JSON = "/json";

    // KEY!
    private static final String API_KEY = "AIzaSyBqxv1mclccpXKSqBRaVuev_F2FOXpMuC8";

    //Text search service
    public static  ArrayList<Place> textSearchAPICall(String input) {
        ArrayList<Place> resultList = null;
        String query = input.replace(" ","+").trim();

        // System.out.println("Processed query: " + query);

        HttpURLConnection conn = null;
        StringBuilder jsonResults = new StringBuilder();
        try {
            StringBuilder sb = new StringBuilder(PLACES_API_BASE);
            
            sb.append(TEXT_SEARCH);
            sb.append(OUT_JSON);
            sb.append("?query=" + query);
            sb.append("&key=" + API_KEY);
            
            // System.out.println("formed URL :" + sb.toString());
           
            URL url = new URL(sb.toString());
            conn = (HttpURLConnection) url.openConnection();
            InputStreamReader in = new InputStreamReader(conn.getInputStream());

            int read;
            char[] buff = new char[1024];
            while ((read = in.read(buff)) != -1) {
                jsonResults.append(buff, 0, read);
            }

            resultList = parseJSON(jsonResults.toString());

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
             e.printStackTrace();
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
        return resultList;
    }


    //parseJSON
    public static ArrayList<Place> parseJSON(String s){
        ArrayList<Place> placeList = null;
        if(s != null){
            placeList = new ArrayList<Place>();
        
            JSONParser parser = new JSONParser();
            try{
                Object obj = parser.parse(s);
                JSONObject jsonObject = (JSONObject) obj;
                JSONArray results = (JSONArray) jsonObject.get("results");
                
                for(int i=0;i<results.size();i++){
                    
                    JSONObject jsonObject1 = (JSONObject) results.get(i);
                    String name = (String) jsonObject1.get("name");
                    String address = (String) jsonObject1.get("formatted_address");
                    
                    //Rating doesn't always available in the api; sometime long is returned; converts long to double
                    Object ratingObj = jsonObject1.get("rating");
                    Double rating = null;
                    if(ratingObj != null){
                        if(ratingObj instanceof Long){
                            Long longRating = (Long) ratingObj;
                            rating = Double.parseDouble(longRating.toString());
                        }
                        else if(ratingObj instanceof Double){
                            rating = (Double) ratingObj;
                        }
                    }
                    
                    JSONObject jsonGeometryObject = (JSONObject) jsonObject1.get("geometry");
                    JSONObject jsonLocationObject = (JSONObject) jsonGeometryObject.get("location");
                    double lattitude = (Double) jsonLocationObject.get("lat");
                    double longitude = (Double) jsonLocationObject.get("lng");
                    
                    //create the place object after parsing from json text - name/address/geo-coordinates
                    Place place = new Place();
                    place.setName(name);
                    place.setAddress(address);
                    if(rating != null){
                       place.setRating(rating); 
                    }
                    place.setGeoCoordinates(lattitude,longitude);
                    
                    //Add to the output list
                    placeList.add(place);
                }
            }
            catch(ParseException e){
                e.printStackTrace();
            }
        }
        return placeList;
    }
}