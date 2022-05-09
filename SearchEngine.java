package internet;

import java.util.HashMap;
import java.util.ArrayList;

public class SearchEngine {
	public HashMap<String, ArrayList<String> > wordIndex;   // this will contain a set of pairs (word, list of url's with that word)	
	public MyWebGraph internet;
	public XmlParser parser;

	public SearchEngine(String filename) throws Exception{
		this.wordIndex = new HashMap<String, ArrayList<String>>();
		this.internet = new MyWebGraph();
		this.parser = new XmlParser(filename);				
	}
	/* 
	 * This does a graph traversal of the web, starting at the given url.
	 * For each new page seen, it updates the wordIndex, the web graph,
	 * and the set of visited vertices.
	 * 
	 * 	This method will fit in about 30-50 lines (or less)
	 */
	public void crawlAndIndex(String url) throws Exception {
		this.internet.addVertex(url);							//Adds the vertex if it is not already in the graph
		this.internet.setVisited(url, true);					//Updates the current page to be visited
		ArrayList<String> content=this.parser.getContent(url);	//Stores the content of the url into the varaible content
		ArrayList<String> newSite= new ArrayList<String>();
		
		for (String words : content) {  						//Searches through every word in the site
			if(wordIndex.containsKey(words)) {					//If the word is already in the hashmap, add this link to the arraylist at the bucket
				wordIndex.get(words).add(url);					//
			} else {
				newSite.add(url);								//Otherwise I create a new ArrayList containing the current site and add it to the wordIndex
				wordIndex.put(words, newSite);
			}
				
		}
		ArrayList<String> AdjacencyList=this.parser.getLinks(url);//Gets the adjacency list
		for(String adjacent: AdjacencyList) {					  //Adds a vertex and edge to the current vertex
			this.internet.addVertex(adjacent);					  //
			this.internet.addEdge(url, adjacent);					
		}
		for(String adjacent : AdjacencyList) { 					//Loops through
			if(! internet.getVisited(adjacent)) {				 //Recursively calls the adjacent vertex's if they have not already been visited
				crawlAndIndex(adjacent);
			}
		}
	}


	/* 
	 * This computes the pageRanks for every vertex in the web graph.
	 * It will only be called after the graph has been constructed using
	 * crawlAndIndex(). 
	 * To implement this method, refer to the algorithm described in the 
	 * assignment pdf. 
	 * 
	 * This method will probably fit in about 30 lines.
	 */
	public void assignPageRanks(double epsilon) {
		for(String urls : this.internet.getVertices()) {		//Assigns all of the ranks to be 1.  
			internet.setPageRank(urls, 1.00);
		}
		if(this.internet.getVertices().size()<=1) {
			throw new IllegalArgumentException("Graph is too small/empty");
		}
		ArrayList<Double> t1=computeRanks(internet.getVertices());
		double prk1=t1.get(0); 											 //Gets the page rank of the first vertex after 1 iteration
		ArrayList<Double> t2 =  computeRanks(internet.getVertices());
		double prk2=t2.get(0);											 //Gets the page rank of the first vertex after 2 iterations
		while(Math.abs(prk1-prk2)>epsilon) {					 
			prk1=prk2;
			ArrayList<Double> tn = computeRanks(internet.getVertices());  //Updates the tanks again
			prk2=tn.get(0);												
			int i=0;
			for(String urls : this.internet.getVertices()) {			  //Loops through all of the sites and updates the pageRanks of the graph accordingly
				internet.setPageRank(urls, tn.get(i));
				i++;
			}
		}
	}



	/*
	 * The method takes as input an ArrayList<String> representing the urls in the web graph 
	 * and returns an ArrayList<double> representing the newly computed ranks for those urls. 
	 * Note that the double in the output list is matched to the url in the input list using 
	 * their position in the list.
	 */
	public ArrayList<Double> computeRanks(ArrayList<String> vertices) {
		ArrayList<Double> pageRanks= new ArrayList<Double>();
		
		for(String url : this.internet.getVertices()) {								   //loops through all of the vertices.	
			double Value=0.0;
			for(String url2 : this.internet.getEdgesInto(url)) {					   //Loops through all of the vertices with an out edge going into url
					Value+=(internet.getPageRank(url2))/(internet.getOutDegree(url2)); //Adds the result of the page rank divided by its out degree for every vertex with an edge going into the current vertex
					
			}
			double rank=0.5+(0.5*(Value));	
			internet.setPageRank(url, rank);
			pageRanks.add(rank);													   //Adds the value at each iteration to the arrayList.  
		}		
		return pageRanks;
	}


	/* Returns a list of urls containing the query, ordered by rank
	 * Returns an empty list if no web site contains the query.
	 * 
	 * This method should take about 25 lines of code.
	 */
	public ArrayList<String> getResults(String query) {
		ArrayList<String> urls = this.wordIndex.get(query);  		//List of links containing the query word
		HashMap<String, Double> sites = new HashMap<String, Double>();
		for (String url : urls) {
			sites.put(url, internet.getPageRank(url));
		}
		ArrayList<String> orderedSites=Sorting.slowSort(sites);
		return orderedSites;

	}
}
