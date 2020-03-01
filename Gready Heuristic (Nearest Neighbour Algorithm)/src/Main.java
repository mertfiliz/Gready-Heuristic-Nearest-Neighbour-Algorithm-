import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class Main {

	public static void main(String[] args) throws FileNotFoundException, UnsupportedEncodingException {
		                     								
		 File file = new File("gsp_dunya.txt");          // File file = new File("gsp_dunya.txt"); 
		 Scanner cities_Name = new Scanner(file);
		 Scanner positions = new Scanner(file);
		 Scanner scan = new Scanner(System.in);
		 
		 int n, k;
		 int temp_n;		 
		 double travelListDistance;
		 double totalDistance = 0;
		 
		 List<String> sehirler = new ArrayList<String>();  // all cities will be added to "sehirler" ArrayList 
	     List<Double> enlem = new ArrayList<Double>();     // all latitudes will be added to "enlem" ArrayList 
	     List<Double> boylam = new ArrayList<Double>();	   // all longitudes will be added to "boylam" ArrayList      
	     List<Double> distances = new ArrayList<Double>(); // all calculated distances will be added to "distances" ArrayList
		 
		 System.out.println("How many city do you wanna travel? (Max:55 for gsp_turkiye)");
		 n = scan.nextInt(); 			// n = First n city from the text file.
		 city[] cities = new city[n];   // 55 city for Turkey - 9117 country for the world.
		 
		 final long startTime = System.currentTimeMillis(); // Starts the timer which we'll use to find the program's execution time.
		 
		 temp_n = n;
		 
		 for(int i=0; i<n; i++) {
			 cities[i] = new city(); //using city class.			
		 }
   
        String line = cities_Name.nextLine();
        k = 0;
       
        // Getting first "n" city names.
        while (cities_Name.hasNextLine() && temp_n>0) {
       	cities[k++].city_Name = line;
	       	for(int i = 0; i<4; i++){ // Every 4 line city name changes.
		       	if(cities_Name.hasNextLine())
		       		line = cities_Name.nextLine();
	       	}	
	       	temp_n--;
       }         
       cities_Name.close();
       
       line = positions.nextLine();
       line = positions.nextLine();
       
       k = 0;
       temp_n = n;
       	
       //Getting first "n" longitude and latitude;
       while(positions.hasNextLine() && temp_n>0) {       	 
	       	cities[k].longitude = Double.parseDouble(line);
	       	line = positions.nextLine();
	       	cities[k++].latitude = Double.parseDouble(line);
      	 	
	       	for(int i = 0 ; i < 3 ; i++){
      			if(positions.hasNextLine())
      				line = positions.nextLine();
	       	}
	       	temp_n--;
       }        
       positions.close(); 
     
       //Assigning the initial city name, longitude, latitude which we will change every time we traveled to a different city.
       String initial_city = cities[0].city_Name;
       double initial_longitude = cities[0].longitude;
       double initial_latitude = cities[0].latitude;
       
       // Assigning the final city name, longitude, latitude which is "Istanbul", "41.02" , "28.97". It is gonna be constant.
       String final_city = cities[0].city_Name;
       double final_longitude = cities[0].longitude;
       double final_latitude = cities[0].latitude;
       
       // This is our traveling list with order. Initially, it has "Istanbul" in it.
       List<String> travelList = new ArrayList<String>(); 
       List<Double> travelListLatitude = new ArrayList<Double>();
       List<Double> travelListLongitude = new ArrayList<Double>();
       
       travelList.add("1. " + initial_city + "  ----->  " +" Starting Point");
       travelListLatitude.add(initial_latitude);
       travelListLongitude.add(initial_longitude);
       
       // Assigning every city names, latitude, longitude information which we declared in our "city" class into an ArrayList.
       for(int i=1; i<n; i++) {
		   sehirler.add(cities[i].city_Name);   // All cities with given order according to the text file.
		   enlem.add(cities[i].latitude); 		// All latitudes with given order according to the text file
		   boylam.add(cities[i].longitude);     // All longitudes with given order according to the text file
       }
               
       /// ALGORITHM ///
       
       for(int j=1; j<n;j++) { // j starts with 1 because we already added the first traveling point which is the starting point "Istanbul"
	        for(int i=0; i<sehirler.size(); i++) { // "sehirler" contains n sized data at first.
	        	double returnDistance = CalculateDistance(initial_latitude, initial_longitude, enlem.get(i), boylam.get(i));
				distances.add(returnDistance);
	        }  
	        //Calling CalculateDistance() function to calculate distances.
	        //Then we'll save those distances inside the "distances" ArrayList.
	        //We are doing that until "sehirler" contains no more cities.
	        
	        double min = Collections.min(distances); 
	        // This is for finding the minimum distance among "distances" ArrayList. Because we need the closest city.
	        int lineA = distances.indexOf(min);
	        // This is for finding the index location of that minimum distance.
	        
	        travelListDistance = min;                //Instant distance
	        totalDistance += travelListDistance;     //Total distance
	        
	        travelList.add(j+1 + ". " +sehirler.get(lineA) + "  ----->  " + travelListDistance + " KM traveled." ); // Adding the city that is closest.
	        travelListLongitude.add(boylam.get(lineA));
	        travelListLatitude.add(enlem.get(lineA));	        
	        
	        //Changing initial city details. Because we traveled to another location.
	        initial_city = sehirler.get(lineA); 	        
	        initial_latitude = enlem.get(lineA);
	        initial_longitude = boylam.get(lineA);
	        
	        //Deleting the records that we traveled. 
	        sehirler.remove(sehirler.get(lineA));
	        enlem.remove(enlem.get(lineA));
	        boylam.remove(boylam.get(lineA));	
	        
	        //Clearing the "distances". Because now our starting city is changed. All calculations must be re-calculated. 
	        distances.clear();		        
       }    
              
       // After traveled first "n" closest cities, we had to go back to our starting point which we declared as "final_city".
       double backDistance = CalculateDistance(initial_latitude, initial_longitude, final_latitude, final_longitude);
       totalDistance += backDistance;
       travelList.add("Back to: " + final_city + "  ----->  " + backDistance +" KM traveled.");
       
       // Printing all traveled points with their KM.
       for (String element : travelList) {   
    	   System.out.println(element);    	       	   
       }   
       
       // Printing the total distance.
       System.out.println("\nTotal Distance is  ----->  " + totalDistance + " KM");        
       
       // Stopping the timer.
       final long endTime = System.currentTimeMillis();
       
       // Printing the execution time.
       System.out.println("\nTotal Execution time: " + (endTime - startTime) +" ms" );
       
       
       // Writing all to a text file.
       
       PrintWriter writer = new PrintWriter("nna_gsp_turkiye_" + n + ".txt", "UTF-8"); 
       
       writer.println("[NEAREST NEIGHBOUR ALGORITHM] for the first " + n +" cities: ");
       writer.println("\r\n");
       
       for (String element : travelList) { 
    	   writer.println(element); 
       }
       
       writer.println("\r\nTotal Distance is  ----->  " + totalDistance + " KM");
       writer.println("\r\nTotal Execution time: " + (endTime - startTime) +" ms" );
       
       writer.close();
       
	} 
	
	// Distance calculation with given formula.
	public static double CalculateDistance(double initial_en, double initial_boy, double en, double boy) {
		double cal1 = (Math.cos(Math.toRadians(initial_boy)) * Math.cos(Math.toRadians(boy))) + (Math.sin(Math.toRadians(initial_boy)) * Math.sin(Math.toRadians(boy)));
		double cal2 = Math.cos(Math.toRadians(initial_en)) * Math.cos(Math.toRadians(en));
		double cal3 = Math.sin(Math.toRadians(initial_en)) * Math.sin(Math.toRadians(en));
		double cal4 = (cal1 * cal2) + cal3;
		double cal5 = Math.acos(cal4) * 6400;
		double distance = cal5;		
		return distance;		
	}

}
