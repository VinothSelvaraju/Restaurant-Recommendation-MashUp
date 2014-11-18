package ub.cse636.project;
/*
formatted_address
	geometry
		location
			lat
			long
	icon
	id
	name
	opening_hours
		open_now
		weekday_text
	photos
	place_id
	price_level
	rating
	reference
	types
*/
public class Place{
	
	private String name;
	private String address;
	private int placeId;
	private Double rating;
	private int reviewCount;

	//first element - lattitude; second element - longitude
	private final double[] geoCoordinates = new double[2];

	public void setName(String name){
		this.name = name;
	}

	public void setAddress(String address){
		this.address = address;
	}
	public void setPlaceId(int placeId){
		this.placeId = placeId;
	}
	public void setRating(Double rating){
		this.rating = rating;
	}
	public void setGeoCoordinates(double lattitude, double longitude){
		geoCoordinates[0] = lattitude;
		geoCoordinates[1] = longitude;
	}
	
	public void setReviewCount(int a)
	{
		this.reviewCount = a;
	}
	
	public String getName(){
		return name;
	}
	public String getAddress(){
		return address;
	}
	public int getPlaceId(){
		return placeId;
	}
	public double getLattitude(){
		if(geoCoordinates.length >= 1){
			return geoCoordinates[0];
		}
		return -1;
	}
	public double getLongitude(){
		if(geoCoordinates.length == 2){
			return geoCoordinates[1];
		}
		return -1;
	}
	public Double getRating(){
		return rating;
	}
	public int getReviewCount(){
		return reviewCount;
	}
}