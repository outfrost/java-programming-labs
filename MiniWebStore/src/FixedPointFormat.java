/*
 *  FixedPointFormat - simple shortcut class for printing integers formatted with 2 decimal places
 *
 *  Author: Iwo Bujkiewicz
 *  Date: 24 Oct 2016
 */

/**
 * Provides a static shortcut method for printing integers formatted with 2 decimal places.
 *
 * @author  Iwo Bujkiewicz
 * @version 20161101
 */
public class FixedPointFormat {
	
	/**
	 * Returns a string containing the provided number formatted to display 2 decimal places.
	 * @param number    Number to be formatted
	 * @return          Resulting text representation of the number
	 */
	public static String decimal2Places(long number) {
		return number / 100L + "." + ((number % 100L < 10) ? "0" + number % 100L : number % 100L);
	}
}
