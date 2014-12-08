package ub.cse636.project.util;

import java.util.ArrayList;
import java.util.Map;
import java.util.Properties;
import java.lang.StringBuilder;
import java.lang.ProcessBuilder;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import ub.cse636.project.Place;
import ub.cse636.project.UberProduct;
import ub.cse636.project.UberPrice;

public class Util{
	
	/*
	 * @author: Vinoth Selvaraju
	 * API used as constants
	*/
	public enum API{
		UBER_KEY,YELP_CONSUMER_KEY,YELP_CONSUMER_SECRET,YELP_TOKEN, 
		YELP_TOKEN_SECRET,PLACES_KEY,MAPS_KEY
	}
	
	/*
	 * @author: Vinoth Selvaraju
	 * Printing the content of Yelp/Google Place API - query search result list
	*/
	public static void printPlaceList(ArrayList<Place> list){
		System.out.println("PRINTING PLACES LIST: ");
		if(list != null && !list.isEmpty()){
			System.out.println("list size : " + list.size());
			for(Place pl : list){
				if(pl != null){
					System.out.println("Name: " + pl.getName());
					System.out.println("Address: " + pl.getAddress());
					System.out.println("Rating: " + pl.getRating()); 
					System.out.println("ReviewCount: " + pl.getReviewCount()); 
					System.out.println("Lattitude: " + pl.getLattitude());
					System.out.println("Longitude: " + pl.getLongitude());
					System.out.println("------------------------");	
				}
			}
		}
	}
	
	/*
	 * @author: Vinoth Selvaraju
	 * Printing the content of Uber API - product search result list
	*/
	public static void printUberProdList(ArrayList<UberProduct> list){
		if(list != null && !list.isEmpty()){
			System.out.println("PRINTING UBER PRODUCT LIST: ");
			System.out.println("list size : " + list.size());
			for(UberProduct prod : list){
				if(prod != null){
					System.out.println("ProductId: " + prod.getProductId());
					System.out.println("Capacity: " + prod.getCapacity());
					System.out.println("Description: " + prod.getDescription());
					System.out.println("ImageURL: " + prod.getImageURL());
					System.out.println("------------------------");
				}
			}
		}
	}

	
	/*
	 * @author: Vinoth Selvaraju
	 * Printing the content of Uber API - prices estimates result map
	*/
	public static void printUberPriceEstimateMap(Map<String, UberPrice> map){
		if(map != null && !map.isEmpty()){
			for (Map.Entry<String, UberPrice> entry : map.entrySet()) {
	   			UberPrice value = entry.getValue();
	    		if(value != null){
	    			System.out.println(value.getProductId());
					System.out.println(value.getDisplayName());
					System.out.println(value.getCurrencyCode());
					System.out.println(value.getEstimate());
					System.out.println(value.getLowEstimate());
					System.out.println(value.getHighEstimate());
					System.out.println(value.getSurgeMultiplier());
					System.out.println(value.getDuration());
					System.out.println(value.getDistance());
	    		}
	    		System.out.println("------------------------");
			}
		}
	}
	
	/*
	 * @author: Vinoth Selvaraju
	 * Printing the content of Uber API - time estimates result map
	*/
	public static void printUberTimeEstimateMap(Map<String, Long> map){
		if(map != null && !map.isEmpty()){
			for (Map.Entry<String, Long> entry : map.entrySet()) {
	   			String key = entry.getKey();
	    		Long value = entry.getValue();
	    		
	    		if(key != null){
	    			System.out.println(key);
	    		}
	    		if(value != null){
	    			System.out.println(value);
	    		}
	    		System.out.println("------------------------");
			}
		}
	}


	/*
	 * @author: Vinoth Selvaraju
	 * 
	 * Curl command execution	
	 * Input - API key; Query URL
	 * Output - String
	 * Ex - Uber API search:
	 *	curl -H 'Authorization: Token ottJav45V1ZXaH0sH8bgMVaYc_Qzkc95Ee0LqhBH' \
	 *		'https://api.uber.com/v1/products?latitude=37.7759792&longitude=-122.41823'
	 *		 https://api.uber.com/v1/products?latitude=37.7759792longitude=-122.41823
	*/
	public static String executeCurlCommand(String apiKey, String query, String outputFormat){
		
		if(apiKey != null && query != null && outputFormat!= null){
			
			BufferedReader reader = null;
			StringBuilder out = new StringBuilder();

			try{
		 		ProcessBuilder p = new ProcessBuilder("curl","-H","Accept: application/" + outputFormat,"-H", "Authorization: Token "+ apiKey, query);
			 	final Process shell = p.start();
			 	InputStream inputSteam = shell.getInputStream();

			 	reader = new BufferedReader(new InputStreamReader(inputSteam));
		       
		        String line;
		        while ((line = reader.readLine()) != null) {
		            out.append(line);
		        }

		        return out.toString();
			}
			catch(IOException e){
			 	e.printStackTrace();
			}
			catch(Exception e){
				e.printStackTrace();
			}
			finally{
			 	try{
			 		if(reader != null){
			 			reader.close();
			 		}
			 	}
			 	catch(IOException e){
			 		e.printStackTrace();
			 	}
			}
		}
		return null;
	}
	
	/*
	 * @author: Vinoth Selvaraju
	 * Wait for 1000 ms
	*/
	public static void waitForSecond(){
		try {
		    Thread.sleep(1000);                 //1000 milliseconds is one second.
		} catch(InterruptedException ex) {
		    Thread.currentThread().interrupt();
		}
	}
	
	/*
	 * @author: Vinoth Selvaraju
	 * Retreive API key from properties file
	*/
	public static String getAPIKeyPropFile(API apiName) {
		 
		Properties prop = new Properties();
		String propFileName = "config.properties";
		
		try{
			InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(propFileName);
			prop.load(inputStream);
		}
		catch(IOException e){
			e.printStackTrace();
		}
		
		switch (apiName) {
        case UBER_KEY:  
        	return prop.getProperty("UBER_KEY");
        case YELP_CONSUMER_KEY:  
        	return prop.getProperty("YELP_CONSUMER_KEY");
        case YELP_CONSUMER_SECRET:  
        	return prop.getProperty("YELP_CONSUMER_SECRET");
        case YELP_TOKEN:  
        	return prop.getProperty("YELP_TOKEN");
        case YELP_TOKEN_SECRET:  
        	return prop.getProperty("YELP_TOKEN_SECRET");
        case PLACES_KEY:  
        	return prop.getProperty("PLACES_KEY");
        default:
        	return null;
		}
	}
}