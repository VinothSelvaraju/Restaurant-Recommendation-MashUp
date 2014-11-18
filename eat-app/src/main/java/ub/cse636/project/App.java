package ub.cse636.project;
import ub.cse636.project.service.PlacesService;
import java.util.ArrayList;

public class App 
{
    public static void main( String[] args )
    {
        //Google Places API - Text search example
        ArrayList<Place> resultList = null;
		PlacesService googlePlacesAPICall = new PlacesService();

		//Sample query
		String query = "restaurant in buffalo";
		
		resultList = googlePlacesAPICall.textSearch(query);

		//printing contents of GooglePlaces API result - Name, Address, Geo-coordinates(Lat/Long), Rating
		System.out.println("Printing conents of List");
        if(resultList != null && resultList.size() > 0){
            System.out.println("list size : " + resultList.size());
            for(Place pl : resultList){
                System.out.println(pl.getName());
                System.out.println(pl.getAddress());
                if(pl.getRating() != null){
                   System.out.println(pl.getRating()); 
                }
                System.out.println(pl.getLattitude());
                System.out.println(pl.getLongitude());
                System.out.println("------------------------");
            }
        }
    }
}
