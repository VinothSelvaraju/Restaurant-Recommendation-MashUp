package ub.cse636.project;

public class UberProduct{

	private Long capacity;
	private String productId;
	private String imageURL;
	private String description;
	private String displayName;
	private double lat;
	private double lng;

	public UberProduct(){
		//Constructor to avoid setting null pointer access to the attributes
		capacity = 0L;
		productId = "";
		imageURL = "";
		description = "";
		displayName = "";
		lat = 0;
		lng = 0;
	}
	/*
		Example UBER product API call:
			"products":[
			{
			"capacity":4,
			"image":"http:\/\/d1a3f4spazzrp4.cloudfront.net\/car-types\/mono\/mono-uberx.png",
			"display_name":"marky339@gmail.com",
			"product_id":"a1111c8c-c720-46c3-8534-2fcdd730040d",
			"description":"The low-cost Uber"
			},
	*/
	public void setCapacity(Long capacity){
		this.capacity = capacity;
	}
	public void setProdId(String productId){
		this.productId = productId;
	}
	public void setImageURL(String imageURL){
		this.imageURL = imageURL;
	}
	public void setDisplayName(String displayName){
		this.displayName = displayName;
	}
	public void setDescription(String description){
		this.description = description;
	}
	public void setLattitude(Double lat){
		this.lat = lat;
	}
	public void setLongitude(Double lng){
		this.lng = lng;
	}

	public long getCapacity(){
		return capacity;
	}
	public String getProductId(){
		return productId;
	}
	public String getImageURL(){
		return imageURL;
	}
	public String getDisplayName(){
		return displayName;
	}
	public String getDescription(){
		return description;
	}
	public Double getLattitude(){
		return lat;
	}
	public Double getLongitude(){
		return lng;
	}
}