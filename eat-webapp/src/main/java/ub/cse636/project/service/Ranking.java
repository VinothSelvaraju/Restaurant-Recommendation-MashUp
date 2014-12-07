package ub.cse636.project.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Collections;
import ub.cse636.project.Place;


public class Ranking 
{
	public static ArrayList<Place> getRankedResults(ArrayList<Place> yelpPlaces, ArrayList<Place> googlePlaces)
	{
		ArrayList<Place> result = new ArrayList<Place>(getByName(yelpPlaces, googlePlaces));
		Collections.sort(result, new MyComparator());
		return result;
	}

	public static HashSet<Place> getByName(ArrayList<Place> yelpPlaces, ArrayList<Place> googlePlaces)
	{
		HashSet<Place> result = new HashSet<Place>();
		int i=0, j=0;
		boolean match = false;
		
		while(j<googlePlaces.size())
		{
			String google = googlePlaces.get(j).getName().toLowerCase().replaceAll("\\'", "");
			//System.out.println(google);
			//System.out.println(google.replaceAll("\\'", ""));
			String tempStr[] = google.split(" ");
			
			for(i=0; i<yelpPlaces.size(); i++)
			{
				String yelp = yelpPlaces.get(i).getName().toLowerCase();
				Double newRating;
				Place temp = new Place(); 
				
				if(tempStr.length > 2)
					google = tempStr[0]+" "+tempStr[1];
				
				//System.out.println(google+" "+yelp);
				
				if(yelp.contains(google))
				{
					//System.out.println("Inside "+google+" "+yelp);
					newRating = (yelpPlaces.get(i).getRating()+googlePlaces.get(j).getRating())/2;
					temp = googlePlaces.get(j);
					temp.setRating(newRating);
					temp.setUpperCase();
					result.add(temp);
					googlePlaces.remove(j);
					yelpPlaces.remove(i);
					j=0;
					match = true;
					break;
				}
			}
			
			if(!match)
				j++;
			else
				match = false;
		}
		
		for(j=0 ; j<googlePlaces.size(); j++)
		{
			googlePlaces.get(j).setUpperCase();
			result.add(googlePlaces.get(j));
		}
		
		for(j=0 ; j<yelpPlaces.size(); j++)
		{
			yelpPlaces.get(j).setUpperCase();
			result.add(yelpPlaces.get(j));
		}
		
		for(j=0 ; j<result.size(); j++)
		{
			
		}

		return result;
	}
}
