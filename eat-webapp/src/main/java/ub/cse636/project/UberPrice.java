package ub.cse636.project;

public class UberPrice{

	private String productId;
	private String displayName;
	private String currencyCode;
	private String estimate;
	
	private String lowEstimate;
	private String highEstimate;
	private double surgeMultiplier;
	private long duration;
	private double distance;

	/*
		Example UBER prices API call:
			"localized_display_name":"uberX",
			"duration":3328,
			"low_estimate":"68",
			"display_name":"uberX",
			"product_id":"a1111c8c-c720-46c3-8534-2fcdd730040d",
			"distance":44.87,
			"surge_multiplier":1.0,
			"estimate":"$68-91",
			"high_estimate":"91",
			"currency_code":"USD"
	*/

	public UberPrice(){
		//Constructor to avoid setting null pointer access to the attributes
		productId = "";
		displayName = "";
	  	currencyCode = "";
	  	estimate = "";
	  	lowEstimate = "";
	  	highEstimate = "";
	  	surgeMultiplier = 1;
	  	duration = 0L;
	  	distance = 0;
	}
	
	//settter methods
	public void setProductId(String productId){
		this.productId = productId;
	}
	public void setDisplayName(String displayName){
		this.displayName = displayName;
	}
	public void setCurrencyCode(String currencyCode){
		this.currencyCode = currencyCode;
	}
	public void setEstimate(String estimate){
		this.estimate = estimate;
	}
	public void setLowEstimate(String lowEstimate){
		this.lowEstimate = lowEstimate;
	}
	public void setHighEstimate(String highEstimate){
		this.highEstimate = highEstimate;
	}
	public void setSurgeMultiplier(Double surgeMultiplier){
		this.surgeMultiplier = surgeMultiplier;
	}
	public void setDuration(Long duration){
		this.duration = duration;
	}
	public void setDistance(Double distance){
		this.distance = distance;
	}


	//getter methods
	public String getProductId(){
		return productId;
	}
	public String getDisplayName(){
		return displayName;
	}
	public String getCurrencyCode(){
		return currencyCode;
	}
	public String getEstimate(){
		return estimate;
	}
	public String getLowEstimate(){
		return lowEstimate;
	}
	public String getHighEstimate(){
		return highEstimate;
	}
	public Double getSurgeMultiplier(){
		return surgeMultiplier;
	}
	public Long getDuration(){
		return duration;
	}
	public Double getDistance(){
		return distance;
	}
}