/*
 *  PhoneBookErrorResponse - class for communicating a request error to the client
 *
 *  Author: Iwo Bujkiewicz
 *  Date:   09 Jan 2017
 */

import java.util.UUID;

public class PhoneBookErrorResponse extends PhoneBookResponse {
	
	private final String errorText;
	
	protected PhoneBookErrorResponse(String errorText, UUID clientID, UUID requestID) {
		super(clientID, requestID);
		this.errorText = errorText;
	}
	
	@Override
	protected String getMessage() {
		return "ERROR: " + errorText;
	}
}
