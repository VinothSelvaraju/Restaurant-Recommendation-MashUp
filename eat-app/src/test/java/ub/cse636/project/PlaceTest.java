package ub.cse636.project;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import static org.junit.Assert.*;

class PlaceTest{
	
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
    	place.setGeoCoordinates(null,null);
    	double res3 = place.getLattitude();
    	assertEquals(null, res3, delta);
    	
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
    	place.setGeoCoordinates(null,null);
    	double res4 = place.getLongitude();
    	assertEquals(null, res4, delta);

     }
}