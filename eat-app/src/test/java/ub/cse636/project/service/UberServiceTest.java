package ub.cse636.project.service;

import ub.cse636.project.UberProduct;
import ub.cse636.project.UberPrice;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Map;

public class UberServiceTest extends TestCase{

	public UberServiceTest( String testName )
    {
        super( testName );
    }

	//Test uber API - product 
    public void testUberProductSearchAPICall() {
    	
    	double lat = 37.7759792;
		double lng = -122.41823;

    	//null test
		ArrayList<UberProduct> out1 = UberService.uberProductSearchAPICall(null,null);
		assertNull(out1);

		ArrayList<UberProduct> out2 = UberService.uberProductSearchAPICall(lat,null);
		assertNull(out2);

		ArrayList<UberProduct> out3 = UberService.uberProductSearchAPICall(null,lng);
		assertNull(out3);

    	//Positive case
		ArrayList<UberProduct> out = UberService.uberProductSearchAPICall(lat,lng);

		//Test output of API call not NULL
		assertNotNull(out);
		
		//Test each of the value parsed is not NULL
		for(UberProduct itr : out){
			assertNotNull(itr);
			assertNotNull(itr.getDisplayName());
			assertNotNull(itr.getCapacity());
			assertNotNull(itr.getDescription());
			assertNotNull(itr.getProductId());
			assertNotNull(itr.getLattitude());
			assertNotNull(itr.getLongitude());
		}
    }

    //Test uber API - price 
    public void testUberPriceEstimatesAPICall(){

    	double startLatitude = 37.7833;
		double startLongitude = -122.4167;
		double endLatitude = 37.3544;
		double endLongitude = -121.9692;

		//Null test
		Map<String,UberPrice> outMap1 = UberService.uberPriceEstimatesAPICall(null,null,null,null);
		assertNull(outMap1);

		//Positive Test
		Map<String,UberPrice> outMap2 = UberService.uberPriceEstimatesAPICall(startLatitude,startLongitude,
																	endLatitude,endLongitude);
		//Test output of API call not NULL
		assertNotNull(outMap2);

		//Test each of the value parsed is not NULL
		for (Map.Entry<String, UberPrice> entry : outMap2.entrySet()) {
   			
   			String key = entry.getKey();
   			assertNotNull(key);
    		UberPrice value = entry.getValue();
    		assertNotNull(value);

    		assertNotNull(value.getProductId());
			assertNotNull(value.getDisplayName());
			assertNotNull(value.getCurrencyCode());
			assertNotNull(value.getEstimate());
			assertNotNull(value.getLowEstimate());
			assertNotNull(value.getHighEstimate());
			assertNotNull(value.getSurgeMultiplier());
			assertNotNull(value.getDuration());
			assertNotNull(value.getDistance());
		}
    }


    //Test uber API - price
    //uberTimeEstimatesAPICall(Double startLatitude, Double startLongitude)
    public void testUberTimeEstimatesAPICall(){

    	double startLatitude = 37.7833;
		double startLongitude = -122.4167;
	
		//Null test
		Map<String,Long> outMap1 = UberService.uberTimeEstimatesAPICall(null,null);
		assertNull(outMap1);

		//Positive Test
		Map<String,Long> outMap2 = UberService.uberTimeEstimatesAPICall(startLatitude,startLongitude);
																	
		//Test output of API call not NULL
		assertNotNull(outMap2);

		//Test each of the value parsed is not NULL
		for (Map.Entry<String, Long> entry : outMap2.entrySet()) {
   			String key = entry.getKey();
   			assertNotNull(key);
    		Long value = entry.getValue();
    		assertNotNull(value);
		}
    }



    //Test parse JSON for Uber - Product search
    public void testParseUberProductJSON() {
    	
    	double delta = 1e-15;

    	//1. null test
    	ArrayList<UberProduct> uberProdList1 = UberService.parseUberProductJSON(null, null, null);
    	assertNull(uberProdList1);

    	//2. empty test with valid lattitude & longitude
    	ArrayList<UberProduct> uberProdList2 = UberService.parseUberProductJSON("", 22.001, -122.345);
    	assertNull(uberProdList2);

    	//3. Positive test - multiple result
    	String inp1 = "{\"products\":[{"+
		"\"capacity\":4,"+
		"\"image\":\"http:\\/\\/d1a3f4spazzrp4.cloudfront.net\\/car-types\\/mono\\/mono-uberx.png\","+
		"\"display_name\":\"marky339@gmail.com\","+
		"\"product_id\":\"a1111c8c-c720-46c3-8534-2fcdd730040d\","+
		"\"description\":\"The low-cost Uber\""+
		"},"+
		"{"+
		"\"capacity\":6,"+ 
		"\"image\":\"http:\\/\\/d1a3f4spazzrp4.cloudfront.net\\/car-types\\/mono\\/mono-uberxl2.png\","+
		"\"display_name\":\"uberXL\","+ 
		"\"product_id\":\"821415d8-3bd5-4e27-9604-194e4359a449\","+ 
		"\"description\":\"low-cost rides for large groups\""+ 
		"},"+
		"]}";
		ArrayList<UberProduct> uberProdList3 = UberService.parseUberProductJSON(inp1, 22.001, -122.345);
		assertNotNull(uberProdList3);
		for(UberProduct itr : uberProdList3){
			assertNotNull(itr);
			assertNotNull(itr.getCapacity());
			assertNotNull(itr.getDisplayName());
			assertNotNull(itr.getDescription());
			assertNotNull(itr.getProductId());
			assertNotNull(itr.getImageURL());
			assertNotNull(itr.getLattitude());
			assertNotNull(itr.getLongitude());
		}

		//4. Positive test - single result
    	String inp2 = "{\"products\":[{"+
		"\"capacity\":4,"+
		"\"image\":\"http:\\/\\/d1a3f4spazzrp4.cloudfront.net\\/car-types\\/mono\\/mono-uberx.png\","+
		"\"display_name\":\"marky339@gmail.com\","+
		"\"product_id\":\"a1111c8c-c720-46c3-8534-2fcdd730040d\","+
		"\"description\":\"The low-cost Uber\""+
		"}"+
		"]}";
		ArrayList<UberProduct> uberProdList4 = UberService.parseUberProductJSON(inp2, 22.001, -122.345);
		assertNotNull(uberProdList4);
		for(UberProduct itr : uberProdList4){
			assertNotNull(itr);
			
			assertNotNull(itr.getCapacity());
			long cap = 4;
			assertEquals(cap,itr.getCapacity());

			assertNotNull(itr.getDescription());
			assertEquals("The low-cost Uber",itr.getDescription());

			assertNotNull(itr.getDisplayName());
			assertEquals("marky339@gmail.com",itr.getDisplayName());

			assertNotNull(itr.getProductId());
			assertEquals("a1111c8c-c720-46c3-8534-2fcdd730040d",itr.getProductId());

			assertNotNull(itr.getImageURL());
			assertEquals("http://d1a3f4spazzrp4.cloudfront.net/car-types/mono/mono-uberx.png",itr.getImageURL());

			assertNotNull(itr.getLattitude());
			assertEquals(22.001,itr.getLattitude(), delta);

			assertNotNull(itr.getLongitude());
			assertEquals(-122.345,itr.getLongitude(), delta);
		}

		//5. Negative test - Partial fields (Ex: capacity not available) test contents of the fields
    	String inp3 = "{\"products\":[{"+
		"\"image\":\"http:\\/\\/d1a3f4spazzrp4.cloudfront.net\\/car-types\\/mono\\/mono-uberx.png\","+
		"\"display_name\":\"marky339@gmail.com\","+
		"\"product_id\":\"a1111c8c-c720-46c3-8534-2fcdd730040d\","+
		"\"description\":\"The low-cost Uber\""+
		"}"+
		"]}";
		ArrayList<UberProduct> uberProdList5 = UberService.parseUberProductJSON(inp3, 22.001, -122.345);
		assertNotNull(uberProdList5);
		for(UberProduct itr : uberProdList5){
			assertNotNull(itr);
			
			long cap = 0;
			assertEquals(cap,itr.getCapacity());
			
			assertNotNull(itr.getDescription());
			assertEquals("The low-cost Uber",itr.getDescription());

			assertNotNull(itr.getDisplayName());
			assertEquals("marky339@gmail.com",itr.getDisplayName());

			assertNotNull(itr.getProductId());
			assertEquals("a1111c8c-c720-46c3-8534-2fcdd730040d",itr.getProductId());
			
			assertNotNull(itr.getImageURL());
			assertEquals("http://d1a3f4spazzrp4.cloudfront.net/car-types/mono/mono-uberx.png",itr.getImageURL());

			assertNotNull(itr.getLattitude());
			assertEquals(22.001,itr.getLattitude(), delta);

			assertNotNull(itr.getLongitude());
			assertEquals(-122.345,itr.getLongitude(), delta);
		}

		//6. Negative test - Empty API result; test default content of the fields
    	String inp4 = "{\"products\":["+
		"]}";
		ArrayList<UberProduct> uberProdList6 = UberService.parseUberProductJSON(inp4, 22.001, -122.345);
		assertNull(uberProdList6);

		//7. Negative test - single result - fields available in the String but not the lattitude or longitude
    	String inp5 = "{\"products\":[{"+
			"\"capacity\":4,"+
			"\"image\":\"http:\\/\\/d1a3f4spazzrp4.cloudfront.net\\/car-types\\/mono\\/mono-uberx.png\","+
			"\"display_name\":\"marky339@gmail.com\","+
			"\"product_id\":\"a1111c8c-c720-46c3-8534-2fcdd730040d\","+
			"\"description\":\"The low-cost Uber\""+
			"}"+
		"]}";
		ArrayList<UberProduct> uberProdList7 = UberService.parseUberProductJSON(inp5, null, null);
		assertNull(uberProdList6);

		//8. Negative test - Error 422 Invalid Request
		String inp6 = "{"+
			"\"message\": \"Invalid user\","+
			"\"code\": \"invalid\","+
			"\"fields\": {"+
			  "\"first_name\": [\"Required\"]"+
			"}"+
		"}";
		ArrayList<UberProduct> uberProdList8 = UberService.parseUberProductJSON(inp6,  22.001, -122.345);
		assertNull(uberProdList6);
		
    }

   
    public void testParseUberPriceJSON() {

    	double delta = 1e-15;
    	
    	//1. null test
    	Map<String,UberPrice> outMap1 = UberService.parseUberPriceJSON(null);
    	assertNull(outMap1);

    	//2. empty test
    	Map<String,UberPrice> outMap2 = UberService.parseUberPriceJSON("");
    	assertNull(outMap1);

    	//3. Positive test
    	String inp1 = "{\"prices\":[{" + 
					"\"localized_display_name\":\"uberX\"," + 
					"\"duration\":3328," +
					"\"low_estimate\":\"68\"," +
					"\"display_name\":\"uberX\"," + 
					"\"product_id\":\"a1111c8c-c720-46c3-8534-2fcdd730040d\"," +
					"\"distance\":44.87," +
					"\"surge_multiplier\":1.0," +
					"\"estimate\":\"$68-91\"," +
					"\"high_estimate\":\"91\"," +
					"\"currency_code\":\"USD\"" +
					"}]}";
		Map<String,UberPrice> outMap3 = UberService.parseUberPriceJSON(inp1);
		assertNotNull(outMap3);

		for (Map.Entry<String, UberPrice> entry : outMap3.entrySet()) {
   			String key = entry.getKey();
   			assertNotNull(key);
    		UberPrice value = entry.getValue();
    		assertNotNull(value);

    		assertNotNull(value.getProductId());
			assertNotNull(value.getDisplayName());
			assertNotNull(value.getCurrencyCode());
			assertNotNull(value.getEstimate());
			assertNotNull(value.getLowEstimate());
			assertNotNull(value.getHighEstimate());
			assertNotNull(value.getSurgeMultiplier());
			assertNotNull(value.getDuration());
			assertNotNull(value.getDistance());

			//Test values
			assertEquals("a1111c8c-c720-46c3-8534-2fcdd730040d", value.getProductId());
			assertEquals("uberX", value.getDisplayName());
			assertEquals("USD", value.getCurrencyCode());
			assertEquals("$68-91", value.getEstimate());
			assertEquals("68", value.getLowEstimate());
			assertEquals("91", value.getHighEstimate());
			assertEquals(1.0, value.getSurgeMultiplier(), delta);
			assertEquals(3328L, (long) value.getDuration());
			assertEquals(44.87, value.getDistance(), delta);
		}

		//4. Negative test - empty test
    	String inp2 = "{\"prices\":[]}";
    	Map<String,UberPrice> outMap4 = UberService.parseUberPriceJSON(inp2);
		assertNull(outMap4);

		//5. Negative test - Error 422 Invalid Request
		String inp3 = "{"+
			"\"message\": \"Invalid user\","+
			"\"code\": \"invalid\","+
			"\"fields\": {"+
			  "\"first_name\": [\"Required\"]"+
			"}"+
		"}";
		Map<String,UberPrice> outMap5 = UberService.parseUberPriceJSON(inp3);
		assertNull(outMap5);

		//6. Negative test - Partial fields (Ex: distance,  displayName & duration not available) test contents of the fields
    	String inp4 = "{\"prices\":[{" + 
					"\"localized_display_name\":\"uberX\"," + 
					"\"low_estimate\":\"68\"," +
					"\"product_id\":\"a1111c8c-c720-46c3-8534-2fcdd730040d\"," +
					"\"surge_multiplier\":1.0," +
					"\"estimate\":\"$68-91\"," +
					"\"high_estimate\":\"91\"," +
					"\"currency_code\":\"USD\"" +
					"}]}";
		Map<String,UberPrice> outMap6 = UberService.parseUberPriceJSON(inp4);
		assertNotNull(outMap6);

		for (Map.Entry<String, UberPrice> entry : outMap6.entrySet()) {
   			String key = entry.getKey();
   			assertNotNull(key);
    		UberPrice value = entry.getValue();
    		assertNotNull(value);

    		assertNotNull(value.getProductId());
			assertNotNull(value.getDisplayName());
			assertNotNull(value.getCurrencyCode());
			assertNotNull(value.getEstimate());
			assertNotNull(value.getLowEstimate());
			assertNotNull(value.getHighEstimate());
			assertNotNull(value.getSurgeMultiplier());
			assertNotNull(value.getDuration());
			assertNotNull(value.getDistance());

			//Test values
			assertEquals("a1111c8c-c720-46c3-8534-2fcdd730040d", value.getProductId());
			assertEquals("", value.getDisplayName());
			assertEquals("USD", value.getCurrencyCode());
			assertEquals("$68-91", value.getEstimate());
			assertEquals("68", value.getLowEstimate());
			assertEquals("91", value.getHighEstimate());
			assertEquals(1.0, value.getSurgeMultiplier(), delta);
			assertEquals(0L, (long) value.getDuration());
			assertEquals(0, value.getDistance(), delta);
		}

		//7. Negative test - incorrect type returned by API call (Ex: String instead of Double for Distance)
		// String inp5 = "{\"prices\":[{" + 
		// 			"\"localized_display_name\":\"uberX\"," + 
		// 			"\"duration\":3328," +
		// 			"\"low_estimate\":\"68\"," +
		// 			"\"display_name\":\"uberX\"," + 
		// 			"\"product_id\":\"a1111c8c-c720-46c3-8534-2fcdd730040d\"," +
		// 			"\"distance\":incorrect," +
		// 			"\"surge_multiplier\":1.0," +
		// 			"\"estimate\":\"$68-91\"," +
		// 			"\"high_estimate\":\"91\"," +
		// 			"\"currency_code\":\"USD\"" +
		// 			"}]}";
		// Map<String,UberPrice> outMap7 = UberService.parseUberPriceJSON(inp5);
		// assertNotNull(outMap7);

		// for (Map.Entry<String, UberPrice> entry : outMap7.entrySet()) {
  //  			String key = entry.getKey();
  //  			assertNotNull(key);
  //   		UberPrice value = entry.getValue();
  //   		assertNotNull(value);

  //   		assertNotNull(value.getProductId());
		// 	assertNotNull(value.getDisplayName());
		// 	assertNotNull(value.getCurrencyCode());
		// 	assertNotNull(value.getEstimate());
		// 	assertNotNull(value.getLowEstimate());
		// 	assertNotNull(value.getHighEstimate());
		// 	assertNotNull(value.getSurgeMultiplier());
		// 	assertNotNull(value.getDuration());
		// 	assertNotNull(value.getDistance());

		// 	//Test values
		// 	assertEquals("a1111c8c-c720-46c3-8534-2fcdd730040d", value.getProductId());
		// 	assertEquals("uberX", value.getDisplayName());
		// 	assertEquals("USD", value.getCurrencyCode());
		// 	assertEquals("$68-91", value.getEstimate());
		// 	assertEquals("68", value.getLowEstimate());
		// 	assertEquals("91", value.getHighEstimate());
		// 	assertEquals(1.0, value.getSurgeMultiplier(), delta);
		// 	assertEquals(3328L, (long) value.getDuration());

		// 	//Error case
		// 	assertEquals(44.87, value.getDistance(), delta);
		// }
    }

    public void testParseUberTimeJSON() {

    	double delta = 1e-15;
    	
    	//1. null test
    	Map<String,Long> outMap1 = UberService.parseUberTimeJSON(null);
    	assertNull(outMap1);

    	//2. empty test
    	Map<String,Long> outMap2 = UberService.parseUberTimeJSON("");
    	assertNull(outMap2);

    	//3. Positive test
    	String inp1 = "{\"times\":[" +
						"{" +
						"\"localized_display_name\":\"uberX\"," +
						"\"estimate\":101," +
						"\"display_name\":\"uberX\"," +
						"\"product_id\":\"a1111c8c-c720-46c3-8534-2fcdd730040d\"" +
						"}]}";

		Map<String,Long> outMap3 = UberService.parseUberTimeJSON(inp1);
		assertNotNull(outMap3);

		for (Map.Entry<String, Long> entry : outMap3.entrySet()) {
   			String key = entry.getKey();
   			assertNotNull(key);
    		Long value = entry.getValue();
    		assertNotNull(value);

			//Test values
			assertEquals("a1111c8c-c720-46c3-8534-2fcdd730040d", key);
			assertEquals(101L, (long) value);
		}

		//4. Negative test - empty test
    	String inp2 = "{\"prices\":[]}";
    	Map<String,Long> outMap4 = UberService.parseUberTimeJSON(inp2);
		assertNull(outMap4);

		//5. Negative test - Error 422 Invalid Request
		String inp3 = "{"+
			"\"message\": \"Invalid user\","+
			"\"code\": \"invalid\","+
			"\"fields\": {"+
			  "\"first_name\": [\"Required\"]"+
			"}"+
		"}";
		Map<String,Long> outMap5 = UberService.parseUberTimeJSON(inp3);
		assertNull(outMap5);

		//6. Negative test - Partial fields (Ex: estimate not available) test contents of the fields
    	String inp4 = "{\"times\":[" +
						"{" +
						"\"localized_display_name\":\"uberX\"," +
						"\"display_name\":\"uberX\"," +
						"\"product_id\":\"a1111c8c-c720-46c3-8534-2fcdd730040d\"" +
						"}]}";
		Map<String,Long> outMap6 = UberService.parseUberTimeJSON(inp4);
		assertNotNull(outMap6);

		for (Map.Entry<String, Long> entry : outMap6.entrySet()) {
   			String key = entry.getKey();
   			assertNotNull(key);
    		Long value = entry.getValue();
    		assertNotNull(value);

			//Test values
			assertEquals("a1111c8c-c720-46c3-8534-2fcdd730040d", key);
			assertEquals(0L, (long) value);
		}
	}
}