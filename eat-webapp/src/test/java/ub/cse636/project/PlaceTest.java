package ub.cse636.project;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import static org.junit.Assert.*;

public class PlaceTest extends TestCase{
	
    public PlaceTest( String testName )
    {
        super( testName );
    }

	//Test Lattitude
    public void testGetLattitude() {
    	
        Place place = new Place();
    	
    	//1. Positive case
    	double delta = 1e-15;
    	double lat = 1.2;
    	double lng = 0.55;
    	place.setGeoCoordinates(lat,lng);
    	double res1 = place.getLattitude();
    	assertEquals(1.2, res1, delta);
    	
    	//2. Null case
        assertNotNull(place);
    	place.setGeoCoordinates(null,null);
    	Double res3 = place.getLattitude();
    	assertNull(res3);
    	
    }

    //Test Longitude
     public void testGetLongitude() {
     	
        Place place = new Place();

    	//1. Positive case
    	double delta = 1e-15;
    	double lat = 1.2;
    	double lng = 0.55;
    	place.setGeoCoordinates(lat,lng);
    	double res2 = place.getLongitude();
    	assertEquals(0.55, res2, delta);

    	//2. Null case
        assertNotNull(place);
    	place.setGeoCoordinates(null,null);
    	Double res4 = place.getLongitude();
    	assertNull(res4);

     }
}