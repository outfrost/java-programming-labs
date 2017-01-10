/*
 *  PhoneBookSaveRequest - class for communicating a "SAVE" request to the server and processing it there
 *
 *  Author: Iwo Bujkiewicz
 *  Date:   09 Jan 2017
 */

import java.io.IOException;
import java.util.UUID;

public class PhoneBookSaveRequest extends PhoneBookRequest {
	
	private final String fileName;
	
	protected PhoneBookSaveRequest(String fileName, UUID clientID) {
		super(clientID);
		this.fileName = fileName;
	}
	
	@Override
	protected PhoneBookResponse processRequest(PhoneBook phoneBook) {
		System.out.println(getLogMessage());
		try {
			phoneBook.save(fileName);
		} catch (IOException e) {
			System.out.println(getRequestIDTail() + " " + e.getMessage());
			return new PhoneBookErrorResponse(e.getMessage(), getClientID(), getRequestID());
		}
		System.out.println(getRequestIDTail() + " Done");
		return new PhoneBookSuccessResponse(getClientID(), getRequestID());
	}
	
	@Override
	protected String getLogMessage() {
		return getRequestIDTail() + " " + getClientIDTail() + ": Requesting to save to " + fileName;
	}
}
