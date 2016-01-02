/**
 * @(#)DispatchCenter.java
 * Assignment#4
 * @author Ankush Varshneya
 * @student# 100853074
 * @version 1.00 2012/3/16
 */

import java.util.*; // Needed for arraylist, hashmaps, Math.random

public class DispatchCenter {

	private HashMap<Integer, Taxi>				taxis; // Stores taxi's with there plate number
	private HashMap<String, ArrayList<Taxi>>	areas; // Stores all areas with an array list of all taxi's currently in that area
	private int[][]								fromToCounter; // Stores sucessfull trips from and to respected areas

    public DispatchCenter() {
    	this.taxis = new HashMap<Integer, Taxi>();
    	this.areas = new HashMap<String, ArrayList<Taxi>>();

		// Add the 6 areas to the areas HashMap
       	this.areas.put("Downtown", new ArrayList<Taxi>());
		this.areas.put("Airport", new ArrayList<Taxi>());
       	this.areas.put("North", new ArrayList<Taxi>());
        this.areas.put("South", new ArrayList<Taxi>());
       	this.areas.put("East", new ArrayList<Taxi>());
      	this.areas.put("West", new ArrayList<Taxi>());

		// Make 50 taxi's
		for(int i = 0 ; i<50; i++) {
			int randomIndex = (int)(Math.random()*6); // Will generate a number from 0 to 5
			// Creates a taxi with a random (unique) plate and allocates it to one of the six areas.
			this.addTaxi(new Taxi(100+i*10+randomIndex), this.getAreaNames().get(randomIndex));
		}

		// Initialize fromToCounter with 0 value
		this.fromToCounter = new int[this.getAreaNames().size()][this.getAreaNames().size()];
		for(int col=0; col<this.getAreaNames().size(); col++)
    		for(int row=0; row<this.getAreaNames().size(); row++)
				this.fromToCounter[col][row] = 0;
    }

	// Returns the FromTOCounter faor a given starts and end area
	public int getFromTOCounterFor(String start, String end) {
		return this.fromToCounter[this.getAreaNames().indexOf(start)][this.getAreaNames().indexOf(end)];
	}

	// Returns all the areas as a String arraylist
	public ArrayList<String> getAreaNames() {
		ArrayList<String> Awnser = new ArrayList<String>();
		for(String S: this.areas.keySet())
			Awnser.add(S);
		return Awnser;
	}

	// Return all taxi's in a given area
	public ArrayList<Taxi> taxisInArea(String s){
		//If area does not exits tell the user via println and get out of the function
		if(!this.areas.containsKey(s)) {
			 System.out.println("area does not exist");
			 return null;
		}
		return this.areas.get(s); // Otherwise return the taxi's in the given area
	}

	// Return available taxi's in a given area
	public ArrayList<Taxi> availableTaxisInArea(String s) {
		ArrayList<Taxi> available = null;
		if(this.taxisInArea(s)!=null) {
			available = new ArrayList<Taxi>();
			// Go through all the taxi's in a given area, if a taxi is available add it to the available arraylist
			for(Taxi T: this.taxisInArea(s))
				if (T.getAvailable())
					available.add(T);
		}
		return available;
	}

	// Return all unavailable taxi's
	public ArrayList<Taxi> getBusyTaxis() {
		ArrayList<Taxi> unavailable = new ArrayList<Taxi>();
		// Go through all the taxi's if a taxi is unavailable add it to the unavailable arraylist
		for(Taxi T: this.taxis.values())
			if (!T.getAvailable())
				unavailable.add(T);
		 return unavailable; // return unavailable taxi array list
	}

	// Add a taxi to a given area
	private void addTaxi(Taxi aTaxi, String area){
		// Add the taxi to HashMap of taxis
		this.taxis.put(aTaxi.getPlateNumber(), aTaxi);
		// Add the taxi to the arraylist in the given area of the area hash map if the area exits
		if(this.taxisInArea(area)!=null) this.taxisInArea(area).add(aTaxi);
	}

	public Taxi sendTaxiForRequest(ClientRequest c) {
		Taxi aTaxi = null; // Hired taxi is null if no taxi is found it stays that way

		// If the pick up area exits and there is a taxi avaliable return it
		if((this.availableTaxisInArea(c.getPickupLocation())!=null)&&(!this.availableTaxisInArea(c.getPickupLocation()).isEmpty())) {
			aTaxi = this.hireTaxi(aTaxi, c.getPickupLocation(), c); // Hire the taxi
			return aTaxi; // Return the taxi
		}
		// If no taxi is avaliable in the pick up area then search all other areas
		else {
			for(String otherArea: this.getAreaNames()) {
				if((this.availableTaxisInArea(otherArea)!=null)&&(!this.availableTaxisInArea(otherArea).isEmpty())) {
					aTaxi = this.hireTaxi(aTaxi, otherArea, c); // Hire the taxi
					return aTaxi; // Return the taxi
				}
			}
		}
		return null;	// If no taxi is avaliable return null
	}

	// Hires a taxi
	private Taxi hireTaxi(Taxi hired, String originalLocation, ClientRequest c) {
		hired = this.availableTaxisInArea(originalLocation).remove(0); // Remove the first avaliable taxi from original Location it came from
		this.taxisInArea(originalLocation).remove(hired); // Remove this taxi from the original Location in main area arraylist
		hired.setAvailable(false); // The taxi is now unavailable
		hired.setDestination(c.getDropoffLocation()); // Set its destination to drop off location
		this.taxisInArea(hired.getDestination()).add(hired); // Add the taxi to destination area
		this.fromToCounter[this.getAreaNames().indexOf(c.getPickupLocation())][this.getAreaNames().indexOf(c.getDropoffLocation())]++; // A trip has been made from pick up location to drop of location so we increase that counter.

		int time = 0;
		if(originalLocation.equals(c.getPickupLocation())) time += 0; // If the original destination is at Pickup Location it will take zero time to get there
		else time += calculateTimeBetween(originalLocation, c.getPickupLocation()); // If the original destination is not Pickup Location calculate the time it will take for the taxi to get to the pickup location, add this time to the total time
		time += calculateTimeBetween(c.getPickupLocation(), c.getDropoffLocation()); // Calculate the time it will take for the taxi to get from the pickup location to drop off location, add this time to the total time
		hired.setEstimatedTimeToDest(time); // Set the time to destination

		return hired;
	}

	// Calculate time to get from one locarion to another
	private int calculateTimeBetween(String start, String end) {
		if(start.equals(end)) return 10;

		else if(start.equals("Airport")&&end.equals("Downtown")) return 40;
		else if(start.equals("Airport")&&end.equals("North")) return 40;
		else if(start.equals("Airport")&&end.equals("South")) return 40;
		else if(start.equals("Airport")&&end.equals("West")) return 60;
		else if(start.equals("Airport")&&end.equals("East")) return 20;

		else if(start.equals("Downtown")&&end.equals("Airport")) return 40;
		else if(start.equals("Downtown")&&end.equals("North")) return 20;
		else if(start.equals("Downtown")&&end.equals("South")) return 20;
		else if(start.equals("Downtown")&&end.equals("West")) return 20;
		else if(start.equals("Downtown")&&end.equals("East")) return 20;

		else if(start.equals("North")&&end.equals("Airport")) return 40;
		else if(start.equals("North")&&end.equals("Downtown")) return 20;
		else if(start.equals("North")&&end.equals("South")) return 40;
		else if(start.equals("North")&&end.equals("West")) return 20;
		else if(start.equals("North")&&end.equals("East")) return 20;

		else if(start.equals("South")&&end.equals("Airport")) return 40;
		else if(start.equals("South")&&end.equals("Downtown")) return 20;
		else if(start.equals("South")&&end.equals("North")) return 40;
		else if(start.equals("South")&&end.equals("West")) return 20;
		else if(start.equals("South")&&end.equals("East")) return 20;

		else if(start.equals("West")&&end.equals("Airport")) return 60;
		else if(start.equals("West")&&end.equals("Downtown")) return 20;
		else if(start.equals("West")&&end.equals("North")) return 20;
		else if(start.equals("West")&&end.equals("South")) return 20;
		else if(start.equals("West")&&end.equals("East")) return 40;

		else if(start.equals("East")&&end.equals("Airport")) return 20;
		else if(start.equals("East")&&end.equals("Downtown")) return 20;
		else if(start.equals("East")&&end.equals("North")) return 20;
		else if(start.equals("East")&&end.equals("South")) return 20;
		else if(start.equals("East")&&end.equals("West")) return 40;

		return 0;
	}
}

