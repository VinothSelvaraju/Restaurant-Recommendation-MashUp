package ub.cse636.project;

import ub.cse636.project.service.PlacesService;
import ub.cse636.project.service.UberService;
import ub.cse636.project.service.YelpApiUtil;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;

import ub.cse636.project.util.Util;

public class App 
{
	public static void main( String[] args )
	{
		
		ArrayList<Place> resultListGooglePlaces = null;
		ArrayList<Place> resultListYelp = null;
		ArrayList<UberProduct> resultListUberProduct = null;
		Map<String, UberPrice> resultMapUberProdPriceEst = null;
		Map<String, Long> resultMapUberProdTimeEst = null;

		//Sample query
		String query = "mexican restaurant in buffalo";
		String query1 = "mexican restaurant";
		
		//Google places API call
		// resultListGooglePlaces = PlacesService.textSearchAPICall(query);

		//Yelp API call
		// YelpApiUtil yelpAPICall = new YelpApiUtil(query1);
		// resultListYelp = yelpAPICall.start();
		
		
		//printing contents of GooglePlaces API result - Name, Address, Geo-coordinates(Lat/Long), Rating
		// System.out.println("Printing conents of List - GooglePlaces");
		// Util.printPlaceList(resultListGooglePlaces);

		//printing contents of Yelp API result 
		// System.out.println("Printing conents of List - Yelp");
		// Util.printPlaceList(resultListYelp);
		
		// HashSet<Place> googlePlacesSet = new HashSet(resultListGooglePlaces);
		// HashSet<Place> yelpSet = new HashSet(resultListYelp);


		//Uber api call - product types
		double lat = 37.7759792;
		double lng = -122.41823;
		resultListUberProduct = UberService.uberProductSearchAPICall(lat,lng);
		//Print content of Uber Prod object (After time estimate is received)
		Util.printUberProdList(resultListUberProduct);
		

		//Uber api call - Time Estimates : returns a Map of productID & timeEstimate
		Util.waitForSecond();
		resultMapUberProdTimeEst = UberService.uberTimeEstimatesAPICall(lat,lng);
		//Print content of uberTimeEstimate map
		Util.printUberTimeEstimateMap(resultMapUberProdTimeEst);
		

		
		//Uber api call - prices Estimates : returns a Map of productID & UberPrice object (currencyCode, displayName, estimate, lowEstimate, highEstimate, surgeMultiplier, duration, distance)
		Util.waitForSecond();
		double startLatitude = 37.7833;
		double startLongitude = -122.4167;
		double endLatitude = 37.3544;
		double endLongitude = -121.9692;
		resultMapUberProdPriceEst = UberService.uberPriceEstimatesAPICall(startLatitude, startLongitude, endLatitude, endLongitude);
		//Print content of uberPriceEstimate map
		Util.printUberPriceEstimateMap(resultMapUberProdPriceEst);
		
	}
}
