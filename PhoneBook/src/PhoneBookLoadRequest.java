/*
 *  PhoneBookLoadRequest - class for communicating a "LOAD" request to the server and processing it there
 *
 *  Author: Iwo Bujkiewicz
 *  Date:   09 Jan 2017
 */

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

class PhoneBookLoadRequest extends PhoneBookRequest {
	
	private final String fileName;
	
	protected PhoneBookLoadRequest(String fileName, UUID clientID) {
		super(clientID);
		this.fileName = fileName;
	}
	
	@Override
	protected PhoneBookResponse processRequest(PhoneBook phoneBook) {
		System.out.println(getLogMessage());
		try {
			phoneBook = PhoneBook.load(fileName);
		} catch (IOException | ClassNotFoundException e) {
			System.out.println(getRequestIDTail() + " " + e.getMessage());
			return new PhoneBookErrorResponse(e.getMessage(), getClientID(), getRequestID());
		}
		System.out.println(getRequestIDTail() + " Done");
		return new PhoneBookSuccessResponse(getClientID(), getRequestID());
	}
	
	@Override
	protected String getLogMessage() {
		return getRequestIDTail() + " " + getClientIDTail() + ": Requesting to load " + fileName;
	}
}
