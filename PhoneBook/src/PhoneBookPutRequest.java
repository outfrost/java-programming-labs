/*
 *  PhoneBookPutRequest
 *
 *  Author: Iwo Bujkiewicz
 *  Date:   09 Jan 2017
 */

import javafx.util.Pair;

import java.util.UUID;

public class PhoneBookPutRequest extends PhoneBookRequest {
	
	private final String name;
	private final String value;
	
	protected PhoneBookPutRequest(String name, String value, UUID clientID) {
		super(clientID);
		this.name = name;
		this.value = value;
	}
	
	@Override
	protected PhoneBookResponse processRequest(PhoneBook phoneBook) {
		System.out.println(getLogMessage());
		if (name == null) {
			System.out.println(getRequestIDTail() + " The provided entry name is null.");
			return new PhoneBookErrorResponse("The provided entry name is null.", getClientID(), getRequestID());
		}
		if (value == null) {
			System.out.println(getRequestIDTail() + " The provided entry value is null.");
			return new PhoneBookErrorResponse("The provided entry value is null.", getClientID(), getRequestID());
		}
		String oldValue = phoneBook.put(name, value);
		System.out.println(getRequestIDTail() + " Done. Old number was " + oldValue);
		return new PhoneBookSuccessResponse(getClientID(), getRequestID());
	}
	
	@Override
	protected String getLogMessage() {
		return getRequestIDTail() + " " + getClientIDTail() + ": Requesting to store the number " + value + " for " + name;
	}
}
