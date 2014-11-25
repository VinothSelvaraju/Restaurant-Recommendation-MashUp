package ub.cse636.project.service;

import com.beust.jcommander.JCommander;
import ub.cse636.project.service.YelpAPI;
import ub.cse636.project.Place;
import java.util.ArrayList;

public class YelpApiUtil 
{
	
	private String input;
	
	public YelpApiUtil() 
	{
	}
	
	public YelpApiUtil(String input) 
	{
		this.input = input;
	}
	
	public ArrayList<Place> start()
	{
		return YelpAPI.getRestaurants(input);
	}
}
