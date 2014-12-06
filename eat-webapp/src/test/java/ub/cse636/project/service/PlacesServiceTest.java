package ub.cse636.project.service;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import static org.junit.Assert.*;

import java.util.ArrayList;

import ub.cse636.project.Place;

public class PlacesServiceTest extends TestCase{

	public PlacesServiceTest( String testName )
	{
		super( testName );
	}

	//Test places API - nearbysearch
	//ArrayList<Place> nearbySearchAPICall(Double lat, Double lng, int radius)
	public void testNearbySearchAPICall() {

		double lat = -33.8670522;
		double lng = 151.1957362;
		int radius = 5000;	

		//null test
		ArrayList<Place> out1 = PlacesService.nearbySearchAPICall(null,null, radius);
		assertNull(out1);

		ArrayList<Place> out2 = PlacesService.nearbySearchAPICall(lat,null, radius);
		assertNull(out2);

		ArrayList<Place> out3 = PlacesService.nearbySearchAPICall(null,lng, radius);
		assertNull(out3);

		//Positive case
		ArrayList<Place> out4 = PlacesService.nearbySearchAPICall(lat,lng, radius);

		//Test output of API call not NULL
		assertNotNull(out4);

		// Test each of the value parsed is not NULL
		for(Place itr : out4){
			assertNotNull(itr);
			assertNotNull(itr.getName());
			assertNotNull(itr.getLattitude());
			assertNotNull(itr.getLongitude());
			assertNotNull(itr.getRating());
		}
	}

	//Test parse JSON for Places - Nearby search
	public void testParseNearbySearchJSON() {

		double delta = 1e-15;

		//1. null test
		ArrayList<Place> outList1 = PlacesService.parseNearbySearchJSON(null);
		assertNull(outList1);

		//2. empty test
		ArrayList<Place> outList2 = PlacesService.parseNearbySearchJSON("");
		assertNull(outList2);

		//3. Positive test
		String inp1 = "{\"results\" : [{" +
				"\"geometry\" : {" +
				"\"location\" : {"  +
				"\"lat\" : -33.8599827,"    +
				"\"lng\" : 151.2021282"   +
				"},"  +
				"\"viewport\" : {" +
				"\"northeast\" : {"  +
				"\"lat\" : -33.8552624,"   +
				"\"lng\" : 151.2031401"  + 
				"}," +
				"\"southwest\" : {"  +
				"\"lat\" : -33.8657895,"    +
				"\"lng\" : 151.2000123"    +
				" }" +
				"}"  +
				"},"  +
				"\"rating\" : 4.5,"  +
				"\"icon\" : \"http:\\/\\/maps.gstatic.com\\/mapfiles\\/place_api\\/icons\\/geocode-71.png\"," +
				"\"id\" : \"92f1bbd4ecab8e9add032bccee40a57a8dfd42b4\"," +
				"\"name\" : \"Barangaroo\","  +
				"\"place_id\" : \"ChIJ1ZL9NkGuEmsRUEkzFmh9AQU\"," +
				"\"reference\" : \"CpQBhQAAABLaIQhdFvUDoBox7zWCkGyvq2_qUwxujd1Erypqy4Tt6c8t6mkd-iR-e1oUt16Ii2Kw8ClpbZdsN-UCrDnHH36-z69eOrHkvjj4I5G5PZgsMXQvOW0jaK2xabzvZkE9FyprXNc1hcq_QqFMv30drDxttIz8Ot1tEny7Ouu25_mOMfUoWTuX0bWoiw590a-O8BIQs_KXtGYf98W_OWhe-WXnCRoUlQMeyylctWziYKoI6etzwzL4EEA\"," +
				"\"scope\" : \"GOOGLE\","  +
				"\"types\" : [ \"locality\", \"political\" ]," +
				"\"vicinity\" : \"Barangaroo\"" +
				"}]}" ;

		ArrayList<Place> outList3 = PlacesService.parseNearbySearchJSON(inp1);
		assertNotNull(outList3);

		//Test content of the list
		for(Place place : outList3){
			assertNotNull(place.getName());
			assertNotNull(place.getLattitude());
			assertNotNull(place.getLongitude());
			assertNotNull(place.getRating());
			ArrayList<String> typeList = place.getTypeList();
			assertNotNull(typeList);

			assertEquals(4.5,place.getRating(),delta);
			assertEquals("Barangaroo",place.getName());
			assertEquals(-33.8599827,place.getLattitude(),delta);
			assertEquals(151.2021282,place.getLongitude(),delta);
			String[] inp = {"locality","political"};
			for(int i=0;i<typeList.size();i++){
				assertEquals(inp[i],typeList.get(i));
			}
		}

		//4. Negative test - empty test
		String inp2 = "{}";
		ArrayList<Place> outList4 = PlacesService.parseNearbySearchJSON(inp2);
		assertNull(outList4);

		//5. Negative test - Error 422 Invalid Request
		String inp3 = "{"+
				"\"message\": \"Invalid user\","+
				"\"code\": \"invalid\","+
				"\"fields\": {"+
				"\"first_name\": [\"Required\"]"+
				"}"+
				"}";
		ArrayList<Place> outList5 = PlacesService.parseNearbySearchJSON(inp3);
		assertNull(outList5);

		//6. Positive test - Partial result (Test default values i.e. empty for unavailable fields)
		String inp4 = "{\"results\" : [{" +
				"\"geometry\" : {" +
				"\"location\" : {"  +

            "\"lng\" : 151.2021282"   +
            "},"  +
            "\"viewport\" : {" +
            "\"northeast\" : {"  +
            "\"lat\" : -33.8552624,"   +
            "\"lng\" : 151.2031401"  + 
            "}," +
            "\"southwest\" : {"  +
            "\"lat\" : -33.8657895,"    +
            "\"lng\" : 151.2000123"    +
            " }" +
            "}"  +
            "},"  +
            "\"icon\" : \"http:\\/\\/maps.gstatic.com\\/mapfiles\\/place_api\\/icons\\/geocode-71.png\"," +
            "\"id\" : \"92f1bbd4ecab8e9add032bccee40a57a8dfd42b4\"," +
            "\"name\" : \"Barangaroo\","  +
            "\"place_id\" : \"ChIJ1ZL9NkGuEmsRUEkzFmh9AQU\"," +
            "\"reference\" : \"CpQBhQAAABLaIQhdFvUDoBox7zWCkGyvq2_qUwxujd1Erypqy4Tt6c8t6mkd-iR-e1oUt16Ii2Kw8ClpbZdsN-UCrDnHH36-z69eOrHkvjj4I5G5PZgsMXQvOW0jaK2xabzvZkE9FyprXNc1hcq_QqFMv30drDxttIz8Ot1tEny7Ouu25_mOMfUoWTuX0bWoiw590a-O8BIQs_KXtGYf98W_OWhe-WXnCRoUlQMeyylctWziYKoI6etzwzL4EEA\"," +
            "\"scope\" : \"GOOGLE\","  +
            "\"types\" : [ \"locality\", \"political\" ]," +
            "\"vicinity\" : \"Barangaroo\"" +
            "}]}" ;

		ArrayList<Place> outList6 = PlacesService.parseNearbySearchJSON(inp4);
		assertNotNull(outList6);

		for(Place place : outList6){
			assertNotNull(place.getName());
			assertNotNull(place.getLattitude());
			assertNotNull(place.getLongitude());
			assertNotNull(place.getRating());

			//Checking default value of the rating if the field is not available
			assertEquals(0,place.getRating(),delta);
			assertEquals(0.0,place.getLattitude(),delta);

		}
	}

}