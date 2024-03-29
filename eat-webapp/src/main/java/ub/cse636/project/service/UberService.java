package  ub.cse636.project.service;

import java.lang.StringBuilder;
import java.lang.Exception;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import  ub.cse636.project.UberProduct;
import  ub.cse636.project.util.Util;
import ub.cse636.project.util.Util.API;
import  ub.cse636.project.UberPrice;


public class UberService{

	private static final String UBER_KEY = Util.getAPIKeyPropFile(API.UBER_KEY);

	/*
	 * @author: Vinoth Selvaraju
	 * 
	 * Description: 
	 * 		Product Uber API call - The Products endpoint returns information about the Uber products offered at a given location 
     * Input: 
     *   	latitude
	 *		longitude
     * Output:
     *   	List<UberProduct>
	 * 		(OR)
	 * 		null
	 */
	public static ArrayList<UberProduct> uberProductSearchAPICall(Double lat, Double lng){

		//input NULL check
		if(lat == null || lng == null){
			return null;
		}

		StringBuilder query = new StringBuilder();
		String outputFormat = "json";

		//Form query URL
		query.append("https://api.uber.com/v1/products?");
		query.append("latitude=");
		query.append(lat);
		query.append("&");
		query.append("longitude=");
		query.append(lng);

		String output = Util.executeCurlCommand(UberService.UBER_KEY,query.toString(), outputFormat);
		return parseUberProductJSON(output, lat, lng);
	}
	
	
	/*
	 * @author: Vinoth Selvaraju
	 * 
	 * Description: 
	 * 		Price Estimate Uber API call - The Price Estimates endpoint returns an estimated price range for each product offered at a given location. 
			The price estimate is provided as a formatted string with the full price range and the localized currency symbol. 
     * Input: 
     *   	start_latitude
	 *		start_longitude
	 *		end_latitude
	 *		end_longitude
     * Output:
     *   	List<UberPrice>
	 * 		(OR)
	 * 		null
	 */
	public static Map<String,UberPrice> uberPriceEstimatesAPICall(Double startLatitude, 
			Double startLongitude, Double endLatitude, Double endLongitude){

		//Input check
		if (startLatitude != null && startLongitude != null && endLatitude != null && endLongitude != null) {

			StringBuilder query = new StringBuilder();
			String outputFormat = "json";

			//Form query URL
			query.append("https://api.uber.com/v1/estimates/price?");
			query.append("start_latitude=");
			query.append(startLatitude);
			query.append("&");
			query.append("start_longitude=");
			query.append(startLongitude);
			query.append("&");
			query.append("end_latitude=");
			query.append(endLatitude);
			query.append("&");
			query.append("end_longitude=");
			query.append(endLongitude);

			String output = Util.executeCurlCommand(UberService.UBER_KEY,query.toString(), outputFormat);

			return parseUberPriceJSON(output);
		}
		return null;
	}

	
	/*
	 * @author: Vinoth Selvaraju
	 * 
	 * Description: 
	 * 		Time Estimates Uber API call - The Time Estimates endpoint returns ETAs for all products offered at a given location, with the responses expressed as integers in seconds.
     * Input: 
     *   	start_latitude
	 *		start_longitude

     * Output:
     *   	Map<String,Long>
	 * 		(OR)
	 * 		null
	 */
	public static Map<String,Long> uberTimeEstimatesAPICall(Double startLatitude, Double startLongitude){

		//Input check
		if (startLatitude != null && startLongitude != null) {

			StringBuilder query = new StringBuilder();
			String outputFormat = "json";

			//Form query URL
			query.append("https://api.uber.com/v1/estimates/time?");
			query.append("start_latitude=");
			query.append(startLatitude);
			query.append("&");
			query.append("start_longitude=");
			query.append(startLongitude);
			// query.append("&");
			// query.append("product_id=");
			// query.append(productId);

			String output = Util.executeCurlCommand(UberService.UBER_KEY,query.toString(), outputFormat);

			return parseUberTimeJSON(output);
		}
		return null;
	}

	
	/*
	 * @author: Vinoth Selvaraju
	 * 
	 * Description: 
	 * 		Promotions Uber API call - The Promotions endpoint returns information about the promotion that will be available to a new user based on their activity's location
     * Input: 
     *   	start_latitude
	 *		start_longitude
	 *		end_latitude
	 *		end_longitude

     * Output:
     *   	String[] of size 3
	 * 		(OR)
	 * 		null
	 */
	public static String[] uberPromotionsAPICall(Double startLatitude, 
			Double startLongitude, Double endLatitude, Double endLongitude){


		//Input check
		if (startLatitude != null && startLongitude != null && endLatitude != null && endLongitude != null) {

			StringBuilder query = new StringBuilder();
			String outputFormat = "json";

			//Form query URL
			query.append("https://api.uber.com/v1/promotions?");
			query.append("start_latitude=");
			query.append(startLatitude);
			query.append("&");
			query.append("start_longitude=");
			query.append(startLongitude);
			query.append("&");
			query.append("end_latitude=");
			query.append(endLatitude);
			query.append("&");
			query.append("end_longitude=");
			query.append(endLongitude);

			String output = Util.executeCurlCommand(UberService.UBER_KEY,query.toString(), outputFormat);

			return parseUberPromotionsJSON(output);
		}
		return null;
	}



	/*
	 * @author: Vinoth Selvaraju
	 * 
	 * Description: 
	 * 		parse JSON out of the UberProduct API call into List of UberProduct object 
     * Input: 
     *   	String
     * Output:
     *   	ArrayList of UberProduct object
	 * 
	 */
	public static ArrayList<UberProduct> parseUberProductJSON(String s, Double lat, Double lng){

		if(s != null && !s.isEmpty() && lat != null && lng != null){
			ArrayList<UberProduct> uberProdList;
			uberProdList = new ArrayList<UberProduct>();

			JSONParser parser = new JSONParser();
			try{
				Object obj = parser.parse(s);
				if(obj != null){
					JSONObject jsonObject = (JSONObject) obj;
					if(jsonObject != null){
						JSONArray results = (JSONArray) jsonObject.get("products");
						if(results != null && results.toString().contains("product_id")){
							for(int i=0;i<results.size();i++){

								JSONObject jsonObject1 = (JSONObject) results.get(i);
								if(jsonObject1 != null){
									Long capacity = (Long) jsonObject1.get("capacity");
									String description = (String) jsonObject1.get("description");
									String displayName = (String) jsonObject1.get("display_name");
									String imageURL = (String) jsonObject1.get("image");
									String productId = (String) jsonObject1.get("product_id");

									//create UberProduct object after parsing from json text - lat/lng/capacity/description/imageURL/productId
									UberProduct uberProduct = new UberProduct();
									uberProduct.setLattitude(lat);
									uberProduct.setLongitude(lng);

									if(capacity != null){
										uberProduct.setCapacity(capacity);
									}
									if(description != null){
										uberProduct.setDescription(description);	
									}
									if(displayName != null){
										uberProduct.setDisplayName(displayName);	
									}
									if(imageURL != null){
										uberProduct.setImageURL(imageURL);
									}
									if(productId != null){
										uberProduct.setProdId(productId);
									}

									//Add to the output list
									uberProdList.add(uberProduct);
								}
							}
							return uberProdList;
						}
					}
				}
			}
			catch(ParseException e){
				e.printStackTrace();
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}
		return null;
	}


	/*
	 * @author: Vinoth Selvaraju
	 * 
	 * Description: 
	 * 		parse JSON out of the UberPrice API call into Map of ProductID as key and UberPrice object as it's value 
     * Input: 
     *   	String
     * Output:
     *   	Map<String,UberPrice>
	 * 
	 */
	public static Map<String,UberPrice> parseUberPriceJSON(String s){

		if(s != null && !s.isEmpty()){
			Map<String,UberPrice> uberProductPriceEstMap = new HashMap<String,UberPrice>();

			JSONParser parser = new JSONParser();

			try{
				Object obj = parser.parse(s);
				if(obj != null){
					JSONObject jsonObject = (JSONObject) obj;
					if(jsonObject != null){
						JSONArray results = (JSONArray) jsonObject.get("prices");
						if(results != null && results.toString().contains("product_id")){
							for(int i=0;i<results.size();i++){
								JSONObject jsonObject1 = (JSONObject) results.get(i);

								if(jsonObject1 != null){
									String productId = (String) jsonObject1.get("product_id");
									String displayName = (String) jsonObject1.get("display_name");
									String currencyCode = (String) jsonObject1.get("currency_code");
									String estimate = (String) jsonObject1.get("estimate");

									String lowEstimate = (String) jsonObject1.get("low_estimate");
									String highEstimate = (String) jsonObject1.get("high_estimate");
									Double surgeMultiplier = (Double) jsonObject1.get("surge_multiplier");
									Long duration = (Long) jsonObject1.get("duration");
									Double distance = (Double) jsonObject1.get("distance");

									//create UberPrice object after parsing from json text - productId/displayName/currencyCode/estimate/lowEstimate/highEstimate/surgeMultiplier/duration/distance
									UberPrice uberPrice = new UberPrice();
									if(productId != null){
										uberPrice.setProductId(productId);
									}
									if(displayName != null){
										uberPrice.setDisplayName(displayName);	
									}
									if(currencyCode != null){
										uberPrice.setCurrencyCode(currencyCode);
									}
									if(estimate != null){
										uberPrice.setEstimate(estimate);
									}
									if(lowEstimate != null){
										uberPrice.setLowEstimate(lowEstimate);
									}
									if(highEstimate != null){
										uberPrice.setHighEstimate(highEstimate);	
									}
									if(surgeMultiplier != null){
										uberPrice.setSurgeMultiplier(surgeMultiplier);
									}
									if(duration != null){
										uberPrice.setDuration(duration);
									}
									if(distance != null){
										uberPrice.setDistance(distance);
									}

									//Add to the output list
									if(productId != null){
										uberProductPriceEstMap.put(productId, uberPrice);
									}
								}           
							}
							return uberProductPriceEstMap;
						}
					}
				}
			}
			catch(ParseException e){
				e.printStackTrace();
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}
		return null;
	}


	/*
	 * @author: Vinoth Selvaraju
	 * 
	 * Description: 
	 * 		parse JSON out of the UberTime API call into a Map of productId as key and time in seconds as it's value 
     * Input: 
     *   	String
     * Output:
     *   	Map<String,Long
	 * 
	 */
	public static Map<String,Long> parseUberTimeJSON(String s){

		if(s != null && !s.isEmpty()){
			// System.out.println("parseTime : " + s);
			Long timeEstimate = 0L;
			Map<String,Long>  uberProdTimeEstimateMap = new HashMap<String,Long>();
			JSONParser parser = new JSONParser();

			try{
				Object obj = parser.parse(s);
				if(obj != null){
					JSONObject jsonObject = (JSONObject) obj;
					if(jsonObject != null){
						JSONArray results = (JSONArray) jsonObject.get("times");
						if(results != null && results.toString().contains("product_id")){
							for(int i=0;i<results.size();i++){

								JSONObject jsonObject1 = (JSONObject) results.get(i);
								if(jsonObject1 != null){
									timeEstimate = (Long) jsonObject1.get("estimate");
									if(timeEstimate == null){
										timeEstimate = (long) 0L;
									}
									String productId = (String) jsonObject1.get("product_id");

									if(productId != null){
										uberProdTimeEstimateMap.put(productId,timeEstimate);
									}

								}

							}
							return uberProdTimeEstimateMap;
						}
					}
				}
			}
			catch(ParseException e){
				e.printStackTrace();
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}
		return null;
	}

	/*
	 * @author: Vinoth Selvaraju
	 * 
	 * Description: 
	 * 		parse JSON out of the UberPromotions API call into a String array of promotions 
     * Input: 
     *   	String
     * Output:
     *   	String[]
	 * 
	 */
	public static String[] parseUberPromotionsJSON(String s){

		if(s != null && !s.isEmpty()){

			String[] promotionsArray = new String[3];
			promotionsArray[0] = "";
			promotionsArray[1] = "";
			promotionsArray[2] = "";
			JSONParser parser = new JSONParser();

			try{
				Object obj = parser.parse(s);
				if(obj != null){
					JSONObject jsonObject = (JSONObject) obj;
					if(jsonObject != null && jsonObject.toString().contains("display_text")){

						String displayText = (String) jsonObject.get("display_text");
						if(displayText != null){
							promotionsArray[0] = displayText;
						}
						String value = (String) jsonObject.get("localized_value");
						if(value != null){
							promotionsArray[1] = value;
						}
						String type = (String) jsonObject.get("type");
						if(type != null){
							promotionsArray[2] = type;
						}

						return promotionsArray;
					}
				}
			}
			catch(ParseException e){
				e.printStackTrace();
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}
		return null;
	}
}