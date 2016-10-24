/*
 *  MiniWebStoreItem - serializable class for sellable item management
 *
 *  Author: Iwo Bujkiewicz
 *  Date: 22 Oct 2016
 */

public class FixedPointFormat {
	
	public static String decimal2Places(long number) {
		return number / 100L + "." + ((number % 100L < 10) ? "0" + number % 100L : number % 100L);
	}
}
