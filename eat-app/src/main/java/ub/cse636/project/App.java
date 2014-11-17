package ub.cse636.project;
import ub.cse636.project.service.PlacesService;
/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        System.out.println( "Hello World!" );

        //Google Places API - Text search example
		PlacesService googlePlacesAPICall = new PlacesService();
		String query = "restaurant in buffalo";
		googlePlacesAPICall.textSearch(query);
    }
}
