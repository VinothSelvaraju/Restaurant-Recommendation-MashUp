package ub.cse636.project.util;

import java.util.ArrayList;
import java.util.Map;

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

	//Printing the content of Yelp/Google Place API - query search result list
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

	//Printing the content of Uber API - product search result list
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


	//Printing the content of Uber API - prices estimates result map
	public static void printUberPriceEstimateMap(Map<String, UberPrice> map){
		if(map != null && !map.isEmpty()){
			for (Map.Entry<String, UberPrice> entry : map.entrySet()) {
	   			String key = entry.getKey();
	   		
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

	//Printing the content of Uber API - time estimates result map
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


	//Printing the content of Uber API - promotions 
	public static void printPromotionsArray(String[] s){
		if(s != null && s.length == 3){
			System.out.println("display_text : " + s[0]);
			System.out.println("localized_value : " + s[1]);
			System.out.println("type : " + s[2]);
		}
	}


	/*
		Curl command execution
		Input - API key; Query URL
		Output - String

		Ex - Uber API search:
			curl -H 'Authorization: Token ottJav45V1ZXaH0sH8bgMVaYc_Qzkc95Ee0LqhBH' \
			'https://api.uber.com/v1/products?latitude=37.7759792&longitude=-122.41823'
			 https://api.uber.com/v1/products?latitude=37.7759792longitude=-122.41823
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

	public static void waitForSecond(){
		try {
		    Thread.sleep(1000);                 //1000 milliseconds is one second.
		} catch(InterruptedException ex) {
		    Thread.currentThread().interrupt();
		}
	}
}