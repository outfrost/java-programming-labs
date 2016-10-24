/*
 *  FixedPointFormat - simple shortcut class for printing integers formatted with 2 decimal places
 *
 *  Author: Iwo Bujkiewicz
 *  Date: 24 Oct 2016
 */

public class FixedPointFormat {
	
	public static String decimal2Places(long number) {
		return number / 100L + "." + ((number % 100L < 10) ? "0" + number % 100L : number % 100L);
	}
}
