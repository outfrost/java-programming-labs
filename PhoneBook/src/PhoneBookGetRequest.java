/*
 *  PhoneBookLoadRequest - class for communicating a "LOAD" request to the server and processing it there
 *
 *  Author: Iwo Bujkiewicz
 *  Date:   09 Jan 2017
 */

import java.util.UUID;

public class PhoneBookGetRequest extends PhoneBookRequest {
	
	private final String name;
	
	protected PhoneBookGetRequest(String name, UUID clientID) {
		super(clientID);
		this.name = name;
	}
	
	@Override
	protected PhoneBookResponse processRequest(PhoneBook phoneBook) {
		System.out.println(getLogMessage());
		if (name == null) {
			System.out.println(getRequestIDTail() + " The requested entry name is null.");
			return new PhoneBookErrorResponse("The requested entry name is null.", getClientID(), getRequestID());
		}
		String result = phoneBook.get(name);
		if (result == null) {
			System.out.println(getRequestIDTail() + " No such entry in the phone book.");
			return new PhoneBookErrorResponse("No entry in the phone book for " + name, getClientID(), getRequestID());
		}
		System.out.println(getRequestIDTail() + " Done");
		return new PhoneBookStringSuccessResponse(result, getClientID(), getRequestID());
	}
	
	@Override
	protected String getLogMessage() {
		return getRequestIDTail() + " " + getClientIDTail() + ": Requesting the number for " + name;
	}
}
