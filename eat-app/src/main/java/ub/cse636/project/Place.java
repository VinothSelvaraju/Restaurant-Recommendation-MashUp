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

	public Place(){
		name = "";
		address = "";
		placeId = 0;
		rating = new Double(0);
		reviewCount = 0;
	}

	//first element - lattitude; second element - longitude
	private final Double[] geoCoordinates = new Double[2];

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
	public void setGeoCoordinates(Double lattitude, Double longitude){
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
	public Double getLattitude(){
		double out = 0;
		if(geoCoordinates.length >= 1){
			return geoCoordinates[0];
		}
		return out;
	}
	public Double getLongitude(){
		double out = 0;
		if(geoCoordinates.length == 2){
			return geoCoordinates[1];
		}
		return out;
	}
	public Double getRating(){
		return rating;
	}
	public int getReviewCount(){
		return reviewCount;
	}
}