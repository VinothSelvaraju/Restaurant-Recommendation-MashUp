package ub.cse636.project.service;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.scribe.builder.ServiceBuilder;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.oauth.OAuthService;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;

import java.util.ArrayList;
import ub.cse636.project.Place;
import java.util.Iterator;

/**
 * Code sample for accessing the Yelp API V2.
 * 
 * This program demonstrates the capability of the Yelp API version 2.0 by using the Search API to
 * query for businesses by a search term and location, and the Business API to query additional
 * information about the top result from the search query.
 * 
 * <p>
 * See <a href="http://www.yelp.com/developers/documentation">Yelp Documentation</a> for more info.
 * 
 */
public class YelpAPI {

	private static final String API_HOST = "api.yelp.com";
	private static final String DEFAULT_TERM = "dinner";
	private static final String DEFAULT_LOCATION = "Buffalo, NY";
	private static final int SEARCH_LIMIT = 20;
	private static final String SEARCH_PATH = "/v2/search";
	private static final String BUSINESS_PATH = "/v2/business";

	/*
	 * Update OAuth credentials below from the Yelp Developers API site:
	 * http://www.yelp.com/developers/getting_started/api_access
	 */
	private static final String CONSUMER_KEY = "LnEHXdAnHU6CKhJOxR-ZJg";
	private static final String CONSUMER_SECRET = "B7hKXDEySTIlmteGsKGI3tF9tNE";
	private static final String TOKEN = "wrsIOP1Q8_wOWVEKR-i-iuJVwHcDSoYG";
	private static final String TOKEN_SECRET = "1XmI_Rn2kTe3OrVk_qmmldIiDhU";

	OAuthService service;
	Token accessToken;

	/**
	 * Setup the Yelp API OAuth credentials.
	 * 
	 * @param consumerKey Consumer key
	 * @param consumerSecret Consumer secret
	 * @param token Token
	 * @param tokenSecret Token secret
	 */
	public YelpAPI(String consumerKey, String consumerSecret, String token, String tokenSecret) {
		this.service =
				new ServiceBuilder().provider(TwoStepOAuth.class).apiKey(consumerKey)
				.apiSecret(consumerSecret).build();
		this.accessToken = new Token(token, tokenSecret);
	}

	/**
	 * Creates and sends a request to the Search API by term and location.
	 * <p>
	 * See <a href="http://www.yelp.com/developers/documentation/v2/search_api">Yelp Search API V2</a>
	 * for more info.
	 * 
	 * @param term <tt>String</tt> of the search term to be queried
	 * @param location <tt>String</tt> of the location
	 * @return <tt>String</tt> JSON Response
	 */
	public String searchForBusinessesByLocation(String term, String location) {
		OAuthRequest request = createOAuthRequest(SEARCH_PATH);
		request.addQuerystringParameter("term", term);
		request.addQuerystringParameter("location", location);
		request.addQuerystringParameter("limit", String.valueOf(SEARCH_LIMIT));
		return sendRequestAndGetResponse(request);
	}

	/**
	 * Creates and sends a request to the Business API by business ID.
	 * <p>
	 * See <a href="http://www.yelp.com/developers/documentation/v2/business">Yelp Business API V2</a>
	 * for more info.
	 * 
	 * @param businessID <tt>String</tt> business ID of the requested business
	 * @return <tt>String</tt> JSON Response
	 */
	public String searchByBusinessId(String businessID) {
		OAuthRequest request = createOAuthRequest(BUSINESS_PATH + "/" + businessID);
		return sendRequestAndGetResponse(request);
	}

	/**
	 * Creates and returns an {@link OAuthRequest} based on the API endpoint specified.
	 * 
	 * @param path API endpoint to be queried
	 * @return <tt>OAuthRequest</tt>
	 */
	private OAuthRequest createOAuthRequest(String path) {
		OAuthRequest request = new OAuthRequest(Verb.GET, "http://" + API_HOST + path);
		return request;
	}

	/**
	 * Sends an {@link OAuthRequest} and returns the {@link Response} body.
	 * 
	 * @param request {@link OAuthRequest} corresponding to the API request
	 * @return <tt>String</tt> body of API response
	 */
	private String sendRequestAndGetResponse(OAuthRequest request) {
		//System.out.println("Querying " + request.getCompleteUrl() + " ...");
		this.service.signRequest(this.accessToken, request);
		Response response = request.send();
		return response.getBody();
	}


	private static ArrayList<Place> queryAPI(YelpAPI yelpApi, String input, String location) {

		//System.out.println("INPUT "+input);
		String searchResponseJSON =
				yelpApi.searchForBusinessesByLocation(input, location);
		JSONParser parser = new JSONParser();
		JSONObject response = null;
		ArrayList<Place> placeList = new ArrayList<Place>();
		try 
		{
			response = (JSONObject) parser.parse(searchResponseJSON);
		} 
		catch (ParseException pe) 
		{
			System.out.println("Error: could not parse JSON response:");
			System.out.println(searchResponseJSON);
			System.exit(1);
		}

		JSONArray businesses = (JSONArray) response.get("businesses");
		for(int i=0; i<businesses.size(); i++)
		{
			JSONObject temp = (JSONObject) businesses.get(i);
			String Name = temp.get("id").toString().replace('-',' ');

			Object ratingObj = temp.get("rating");
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

			Object reviewCountObj = temp.get("review_count");
			Integer reviewCount = null;
			if(reviewCountObj != null){
				if(reviewCountObj instanceof Long){
					Long longReviewCount = (Long) reviewCountObj;
					reviewCount = Integer.parseInt(longReviewCount.toString());
				}
				else if(ratingObj instanceof Integer){
					reviewCount = (Integer) reviewCountObj;
				}
			}
			

			JSONObject addressObj = (JSONObject) temp.get("location");
			JSONObject coord = (JSONObject) addressObj.get("coordinate");
			double lat = (Double) coord.get("latitude");
			double lon = (Double) coord.get("longitude");

			 String address = "";
			 JSONArray addressList = (JSONArray) addressObj.get("display_address");
				Iterator<String> iterator = addressList.iterator();
				while (iterator.hasNext()) {
					address += iterator.next()+" ";
				}
					/*resultArray[i] = tempID+"|"+rating+"|"+review_count+"|"+lat+"|"+lon;
		    System.out.println(resultArray[i]);*/

			//create the place object after parsing from json text - name/address/geo-coordinates
			Place place = new Place();
			place.setName(Name);
			place.setAddress(address);
			if(rating != null){
				place.setRating(rating); 
			}
				place.setReviewCount(reviewCount);
			place.setGeoCoordinates(lat,lon);

			//Add to the output list
			placeList.add(place);
		}
		return placeList;
	}

	public static ArrayList<Place> getRestaurants(String input, String location)
	{
		YelpAPI yelpApi = new YelpAPI(CONSUMER_KEY, CONSUMER_SECRET, TOKEN, TOKEN_SECRET);
		return queryAPI(yelpApi, input, location);
	}

}
