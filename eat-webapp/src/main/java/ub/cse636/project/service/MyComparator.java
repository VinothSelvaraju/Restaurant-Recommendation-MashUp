package ub.cse636.project.service;

import java.util.Comparator;
import ub.cse636.project.Place;

public class MyComparator implements Comparator<Place>
{

    public int compare(Place one, Place another){
        int returnVal = 0;

    if(one.getRating() > another.getRating()){
        returnVal =  -1;
    }else if(one.getRating() < another.getRating()){
        returnVal =  1;
    }else if(one.getRating() == another.getRating()){
        returnVal =  0;
    }
    return returnVal;

    }
}