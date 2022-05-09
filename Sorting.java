package internet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry; // You may need it to implement fastSort

public class Sorting {

	/*
	 * This method takes as input an HashMap with values that are Comparable. 
	 * It returns an ArrayList containing all the keys from the map, ordered 
	 * in descending order based on the values they mapped to. 
	 * 
	 * The time complexity for this method is O(n^2) as it uses bubble sort, where n is the number 
	 * of pairs in the map. 
	 */
    public static <K, V extends Comparable> ArrayList<K> slowSort (HashMap<K, V> results) {
        ArrayList<K> sortedUrls = new ArrayList<K>();
        sortedUrls.addAll(results.keySet());	//Start with unsorted list of urls

        int N = sortedUrls.size();
        for(int i=0; i<N-1; i++){
			for(int j=0; j<N-i-1; j++){
				if(results.get(sortedUrls.get(j)).compareTo(results.get(sortedUrls.get(j+1))) <0){
					K temp = sortedUrls.get(j);
					sortedUrls.set(j, sortedUrls.get(j+1));
					sortedUrls.set(j+1, temp);					
				}
			}
        }
        return sortedUrls;                    
    }
    
    
	/*
	 * This method takes as input an HashMap with values that are Comparable. 
	 * It returns an ArrayList containing all the keys from the map, ordered 
	 * in descending order based on the values they mapped to. 
	 * 
	 * The time complexity for this method is O(n*log(n)), where n is the number 
	 * of pairs in the map. 
	 */
    public static <K, V extends Comparable> ArrayList<K> fastSort(HashMap<K, V> results) {
    	ArrayList<K> list= new ArrayList<K>();		    	//Initialize an arrayList 
    	list.addAll(results.keySet());   					//Adds keys (unsorted list of url's containg the query) to list
    	V leftIndex=results.get(list.get(0));				//Initializes leftIndex as the pageRank of the first url in the list of url's containing the query
    	V rightIndex=(results.get(list.get(list.size()-1)));//Initialize rightIndex as the pageRank of the last index in the list of url's contaianing the query
    	return helpFastSort(results,list,leftIndex, rightIndex); //Sorts the list
    }    
    
    //Helper method which recursively executes FastSort
    private static <K,V extends Comparable> ArrayList<K>helpFastSort(HashMap<K, V> results, ArrayList<K> list, V leftIndex, V rightIndex){
    	//Base Case
    	if(leftIndex.compareTo(rightIndex)>=0) { //
    		return list;
    	} 
    	else {
    		int i=placeAndDivide(results, list, leftIndex, rightIndex);			//Places the rightMost element(pivot) into the correct location
    		helpFastSort(results, list, leftIndex, results.get(list.get(i-1)));	//Recursively calls itself with the left of the pivot
    		helpFastSort(results, list, results.get(list.get(i+1)), rightIndex);//Recursively calls itself with the right of the pivot    		
    	}
    	return list;
    }
    //TODO: Why do I need <K, V extends Comparable???
    //A helper method which places the last element in it's correct location.  
    private static <K,V extends Comparable> int placeAndDivide (HashMap<K, V> results, ArrayList<K> list, V leftIndex, V rightIndex) {
    	V pivot=rightIndex;		    			//Set the pivot to be the rightMost element.  **Can change this if need be.
    	int wall=0;								//Wall starts as before the first element. 
    	for(int i=0; i<list.size(); i++) {		//Iterate through the list 
    		V cur=results.get(list.get(i));		//cur represents the value at each iteration
    		if(cur.compareTo(pivot)<0) {		//If the current is smaller than the pivot, then:
    			K temp = list.get(wall);		//Sets the temp to be the key at the given index
    			wall++;							//Wall moves forward by one position
    		
    			list.set(wall, list.get(i));	//Swap the Key/Value pair right behind the wall with the current Key/Value pair.  
    			list.set(i, temp); 				//TODO:  Is this how you swap Key Value Pairs???!!
    		}
    	}
		K temp = list.get(wall+1);				//Swap the pivot with the element to the right of the wall
		list.set(wall+1, list.get(list.size()));//Puts the last element (pivot) into the position to the right of the wall
		list.set(list.size(), temp); 			//Puts the element that was to the right of the wall into the last position
		return wall+1;							//Returns the position of the pivot.
    											
    }
    
    /*
     * 				if(results.get(sortedUrls.get(j)).compareTo(results.get(sortedUrls.get(j+1))) <0){
					K temp = sortedUrls.get(j);
					sortedUrls.set(j, sortedUrls.get(j+1));
					sortedUrls.set(j+1, temp);					
				}
     * 
     */
}