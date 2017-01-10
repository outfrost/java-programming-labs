/*
 *  PhoneBookSuccessResponse - class for communicating a request success to the client
 *
 *  Author: Iwo Bujkiewicz
 *  Date:   09 Jan 2017
 */

import java.util.UUID;

public class PhoneBookSuccessResponse extends PhoneBookResponse {
	
	protected PhoneBookSuccessResponse(UUID clientID, UUID requestID) {
		super(clientID, requestID);
	}
	
	@Override
	protected String getMessage() {
		return "OK";
	}
}
