/*
 *  PhoneBookStringSuccessResponse - class for communicating a request success with a string result to the client
 *
 *  Author: Iwo Bujkiewicz
 *  Date:   09 Jan 2017
 */

import java.util.UUID;

public class PhoneBookStringSuccessResponse extends PhoneBookSuccessResponse {
	
	private final String s;
	
	protected PhoneBookStringSuccessResponse(String s, UUID clientID, UUID requestID) {
		super(clientID, requestID);
		this.s = s;
	}
	
	@Override
	protected String getMessage() {
		return "OK: " + s;
	}
}
