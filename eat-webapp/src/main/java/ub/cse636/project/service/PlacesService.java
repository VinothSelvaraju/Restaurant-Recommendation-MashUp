package ub.cse636.project.service;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import ub.cse636.project.Place;
import ub.cse636.project.util.Util;
import ub.cse636.project.util.Util.API;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;


public class PlacesService{

	private static final String PLACES_API_BASE = "https://maps.googleapis.com/maps/api/place";
	private static final String TEXT_SEARCH = "/textsearch";
	private static final String NEARBY_SEARCH = "/nearbysearch";
	private static final String OUT_JSON = "/json";

	// KEY!
	private static final String PLCAES_KEY = Util.getAPIKeyPropFile(API.PLACES_KEY);

	/*
	 * @author: Vinoth Selvaraju
	 * 
	 * Description: 
	 * 		text search Places API call with mandatory parameters only - Returns places nearby to the provided location
     * Input: 
     *   	search string (Space separated input query)
     * Output:
     *   	ArrayList of Place object
	 * 
	 */
	public static  ArrayList<Place> textSearchAPICall(String input) {

		//Input check
		if(input == null || input.isEmpty()){
			return null;
		}

		//process input search query
		String query = "";
		if(input.contains(" ")){
			query = input.replace(" ","+").trim();
		}
		if(!input.toLowerCase().contains("restaurant")){
			query = query + "+restaurant";
		}
		
		HttpURLConnection conn = null;
		ArrayList<Place> resultList = null;
		StringBuilder jsonResults = new StringBuilder();
		try {
			StringBuilder sb = new StringBuilder(PLACES_API_BASE);

			sb.append(TEXT_SEARCH);
			sb.append(OUT_JSON);
			sb.append("?query=" + query);
			sb.append("&key=" + PLCAES_KEY);

			URL url = new URL(sb.toString());
			conn = (HttpURLConnection) url.openConnection();
			InputStreamReader in = new InputStreamReader(conn.getInputStream());

			int read;
			char[] buff = new char[1024];
			while ((read = in.read(buff)) != -1) {
				jsonResults.append(buff, 0, read);
			}

			resultList = parseTextSearchJSON(jsonResults.toString());

		} catch (MalformedURLException e) {
			e.printStackTrace();
			return resultList;
		} catch (IOException e) {
			e.printStackTrace();
			return resultList;
		} finally {
			if (conn != null) {
				conn.disconnect();
			}
		}
		return resultList;
	}


	/*
	 * @author: Vinoth Selvaraju
	 * 
	 * Description: 
	 * 		Nearby search Places API call with mandatory parameters only - Returns places nearby to the provided location
     * Input: 
     *   	Latitude, Longitude and radius of the search
     * Output:
     *   	ArrayList of Place object
	 * 
	 */
	public static ArrayList<Place> nearbySearchAPICall(Double lat, Double lng, int radius) {

		//Input check - return null when the input is null or empty
		if(lat ==null || lng == null){
			return null;
		}

		HttpURLConnection conn = null;
		StringBuilder jsonResults = new StringBuilder();
		try {
			StringBuilder sb = new StringBuilder(PLACES_API_BASE);
			sb.append(NEARBY_SEARCH);
			sb.append(OUT_JSON);
			sb.append("?");
			sb.append("location=" + String.valueOf(lat) + "," + String.valueOf(lng));
			sb.append("&radius=" + String.valueOf(radius));
			sb.append("&key=" + PLCAES_KEY);
			// sb.append("&keyword=" + URLEncoder.encode(keyword, "utf8"));


			URL url = new URL(sb.toString());
			conn = (HttpURLConnection) url.openConnection();
			InputStreamReader in = new InputStreamReader(conn.getInputStream());

			int read;
			char[] buff = new char[1024];
			while ((read = in.read(buff)) != -1) {
				jsonResults.append(buff, 0, read);
			}
		} catch (MalformedURLException e) {
			return null;
		} catch (IOException e) {
			return null;
		} finally {
			if (conn != null) {
				conn.disconnect();
			}
		}

		//Parse JSON and return as List<Place>
		return parseNearbySearchJSON(jsonResults.toString());
	}
	
	/*
	 * @author: Vinoth Selvaraju
	 * 
	 * Description: 
	 * 		Nearby search Places API call with mandatory parameters & type list (Categories that needs 
	 * 		to be returned - optional) - Returns places nearby to the provided location
     * Input: 
     *   	Latitude, Longitude, radius and type list of the search
     * Output:
     *   	ArrayList of Place object
	 * 
	 */
	public static ArrayList<Place> nearbySearchAPICall(Double lat, Double lng, Integer radius, ArrayList<String> typeList) {

		//Input check - return null when the input is null
		if(lat == null || lng == null || radius == null || typeList == null){
			return null;
		}

		HttpURLConnection conn = null;
		StringBuilder jsonResults = new StringBuilder();
		try {
			StringBuilder sb = new StringBuilder(PLACES_API_BASE);
			sb.append(NEARBY_SEARCH);
			sb.append(OUT_JSON);
			sb.append("?");
			sb.append("location=" + String.valueOf(lat) + "," + String.valueOf(lng));
			sb.append("&radius=" + String.valueOf(radius));
			
			//Append types to the query if the type list is non-empty
			if(!typeList.isEmpty()){
				sb.append("&types=");
				for(int i = 0;i < typeList.size() && typeList.get(i) != null;i++){
					sb.append(typeList.get(i));
					if(i < typeList.size()-1){
						sb.append("|");
					}
				}
			}
			
			sb.append("&key=" + PLCAES_KEY);
			URL url = new URL(sb.toString());
			conn = (HttpURLConnection) url.openConnection();
			InputStreamReader in = new InputStreamReader(conn.getInputStream());

			int read;
			char[] buff = new char[1024];
			while ((read = in.read(buff)) != -1) {
				jsonResults.append(buff, 0, read);
			}
		} catch (MalformedURLException e) {
			return null;
		} catch (IOException e) {
			return null;
		} finally {
			if (conn != null) {
				conn.disconnect();
			}
		}
		//Parse JSON and return as List<Place>
		return parseNearbySearchJSON(jsonResults.toString());
	}

	 /* @author: Vinoth Selvaraju
	 * 
	 * Description: 
	 * 		parse JSON out of the nearby search API call into List of Place object 
     * Input: 
     *   	String
     * Output:
     *   	ArrayList of Place object
	 * 
	 */
	public static ArrayList<Place> parseTextSearchJSON(String s){
		ArrayList<Place> placeList = null;
		if(s != null){
			placeList = new ArrayList<Place>();

			JSONParser parser = new JSONParser();
			try{
				Object obj = parser.parse(s);
				if(obj != null){
					JSONObject jsonObject = (JSONObject) obj;
					if(jsonObject != null){
						JSONArray results = (JSONArray) jsonObject.get("results");
						if(results != null){
							for(int i=0;i<results.size();i++){

								JSONObject jsonObject1 = (JSONObject) results.get(i);
								if(jsonObject1 != null){

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

									//parse geometry co-ordinates
									Double lattitude = null;
									Double longitude = null;
									JSONObject jsonGeometryObject = (JSONObject) jsonObject1.get("geometry");
									if(jsonGeometryObject != null){
										JSONObject jsonLocationObject = (JSONObject) jsonGeometryObject.get("location");
										if(jsonLocationObject != null){
											lattitude = (Double) jsonLocationObject.get("lat");
											longitude = (Double) jsonLocationObject.get("lng");
										}
									}

									//parse type list
									ArrayList<String> typeList = null;
									JSONArray typeArray = (JSONArray) jsonObject.get("types");
									if(typeArray != null){
										typeList = new ArrayList<String>();
										for(int j=0;j<typeArray.size();j++){
											if(typeArray.get(j) != null){
												typeList.add((String) typeArray.get(j));
											}
										}
									}

									//create the place object after parsing from json text - name/address/geo-coordinates
									Place place = new Place();
									if(name != null){
										place.setName(name); 
									}
									if(address != null){
										place.setAddress(address);
									}
									if(rating != null){
										place.setRating(rating); 
									}
									if(lattitude != null && longitude != null){
										place.setGeoCoordinates(lattitude,longitude);    
									}
									if(typeList != null){
										place.setTypeList(typeList);
									}

									//Add to the output list
									placeList.add(place);
								}
							}
						}
					}

				}
			}
			catch(ParseException e){
				e.printStackTrace();
			}
		}
		return placeList;
	}


	/*
	 * @author: Vinoth Selvaraju
	 * 
	 * Description: 
	 * 		parse JSON out of the text search API call into List of Place object 
     * Input: 
     *   	String
     * Output:
     *   	ArrayList of Place object
	 * 
	 */
	public static ArrayList<Place> parseNearbySearchJSON(String s){

		//Input check - return null when s is null or empty()
		if(s == null || s.isEmpty() || !s.contains("results")){
			return null;
		}

		ArrayList<Place> placeList = new ArrayList<Place>();
		JSONParser parser = new JSONParser();

		try{
			Object obj = parser.parse(s);
			JSONObject jsonObject = (JSONObject) obj;
			if(jsonObject != null){
				JSONArray results = (JSONArray) jsonObject.get("results");
				if(results != null){

					//Initialize parsed fields
					Double lattitude = null;
					Double longitude = null;
					String name = null;
					Double rating = null;

					for(int i=0;i<results.size();i++){

						JSONObject jsonObject1 = (JSONObject) results.get(i);
						if(jsonObject1 != null){

							//name
							name = (String) jsonObject1.get("name");

							//Rating doesn't always available in the api; sometime long is returned; converts long to double
							Object ratingObj = jsonObject1.get("rating");
							if(ratingObj != null){ 
								if(ratingObj instanceof Long){
									Long longRating = (Long) ratingObj;
									rating = Double.parseDouble(longRating.toString());
								}
								else if(ratingObj instanceof Double){
									rating = (Double) ratingObj;
								}
							}

							//Lattitude & longitude
							JSONObject jsonGeometryObject = (JSONObject) jsonObject1.get("geometry");
							if(jsonGeometryObject != null){
								JSONObject jsonLocationObject = (JSONObject) jsonGeometryObject.get("location");
								if(jsonLocationObject != null){
									lattitude = (Double) jsonLocationObject.get("lat");
									longitude = (Double) jsonLocationObject.get("lng");    
								}
							}

							//TypeList
							ArrayList<String> typeList = null;
							JSONArray typeArray = (JSONArray) jsonObject.get("types");
							if(typeArray != null){
								typeList = new ArrayList<String>();
								for(int j=0;j<typeArray.size();j++){
									if(typeArray.get(j) != null){
										typeList.add((String) typeArray.get(j));
									}
								}
							}

							//create the place object after parsing from json text - name/address/geo-coordinates
							Place place = new Place();
							if(name != null){
								place.setName(name);    
							}
							if(rating != null){
								place.setRating(rating); 
							}
							if(lattitude != null && longitude != null){
								place.setGeoCoordinates(lattitude,longitude);
							}
							if(typeList != null){
								place.setTypeList(typeList);
							}

							//Add to the output list
							placeList.add(place);
						}
					}
				}
			}    
		}
		catch(ParseException e){
			e.printStackTrace();
		}
		return placeList;      
	}
}