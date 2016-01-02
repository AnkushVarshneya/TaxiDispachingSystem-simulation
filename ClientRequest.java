/**
 * @(#)ClientRequest.java
 * Assignment#4
 * @author Ankush Varshneya
 * @student# 100853074
 * @version 1.00 2012/3/16
 */

public class ClientRequest {
	private String	pickupLocation;
	private String	dropOffLocation;

	public String	getPickupLocation() { return pickupLocation; }
	public String	getDropoffLocation() { return dropOffLocation; }

	public ClientRequest (String p, String d) {
		pickupLocation = p;
		dropOffLocation = d;
	}

	public String toString() {
		return pickupLocation + " ==> " + dropOffLocation;
	}
}