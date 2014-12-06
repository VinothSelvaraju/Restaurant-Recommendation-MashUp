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
	private static final String NEARBY_SEARCH = "/nearbysearch";
	private static final String OUT_JSON = "/json";

	// KEY!
	private static final String API_KEY = "AIzaSyBqxv1mclccpXKSqBRaVuev_F2FOXpMuC8";


	//Text search service
	public static  ArrayList<Place> textSearchAPICall(String input) {

		//Input check
		if(input == null || input.isEmpty()){
			return null;
		}

		ArrayList<Place> resultList = null;
		String query = input.replace(" ","+").trim();

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
        Input:
            lattitude
            longitude
            radius

        Output:
            "geometry" : {
                "location" : {
                   "lat" : -33.8599827,
                   "lng" : 151.2021282
                },
                "viewport" : {
                   "northeast" : {
                      "lat" : -33.8552624,
                      "lng" : 151.2031401
                   },
                   "southwest" : {
                      "lat" : -33.8657895,
                      "lng" : 151.2000123
                   }
                }
             },
             "icon" : "http://maps.gstatic.com/mapfiles/place_api/icons/geocode-71.png",
             "id" : "92f1bbd4ecab8e9add032bccee40a57a8dfd42b4",
             "name" : "Barangaroo",
             "place_id" : "ChIJ1ZL9NkGuEmsRUEkzFmh9AQU",
             "reference" : "CpQBhAAAAIbacNxLx01ilJApMdLG22t1HAXh8bU6zj7wotdrU8-Uv7XyKZIHIgEy8uIc3NFAB8wgnDnBzoIbrnfzS8DFi3DMIBMhsbAPx6PjzzDsD4UE3sN5uW5FPqOD4Ti_-dhIBSdiVx0irScynqAzDBMNPlu9UM4kSsCQMyPB1Lwk1rq8FLN-GazbuIL9DCftxYrCZRIQQkqnCSNzHqhUQKQoHfBH2xoUHZDHcRAM8XOM5FaMngHuz3rePNk",
             "scope" : "GOOGLE",
             "types" : [ "locality", "political" ],
             "vicinity" : "Barangaroo"
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
			sb.append("&key=" + API_KEY);
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



	//parseTextSearchJSON
	/*
        {
         "geometry" : {
            "location" : {
               "lat" : -33.8599827,
               "lng" : 151.2021282
            },
            "viewport" : {
               "northeast" : {
                  "lat" : -33.8552624,
                  "lng" : 151.2031401
               },
               "southwest" : {
                  "lat" : -33.8657895,
                  "lng" : 151.2000123
               }
            }
         },
         "icon" : "http://maps.gstatic.com/mapfiles/place_api/icons/geocode-71.png",
         "id" : "92f1bbd4ecab8e9add032bccee40a57a8dfd42b4",
         "name" : "Barangaroo",
         "place_id" : "ChIJ1ZL9NkGuEmsRUEkzFmh9AQU",
         "reference" : "CpQBhQAAABLaIQhdFvUDoBox7zWCkGyvq2_qUwxujd1Erypqy4Tt6c8t6mkd-iR-e1oUt16Ii2Kw8ClpbZdsN-UCrDnHH36-z69eOrHkvjj4I5G5PZgsMXQvOW0jaK2xabzvZkE9FyprXNc1hcq_QqFMv30drDxttIz8Ot1tEny7Ouu25_mOMfUoWTuX0bWoiw590a-O8BIQs_KXtGYf98W_OWhe-WXnCRoUlQMeyylctWziYKoI6etzwzL4EEA",
         "scope" : "GOOGLE",
         "types" : [ "locality", "political" ],
         "vicinity" : "Barangaroo"
      }
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


	//parseTextSearchJSON
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