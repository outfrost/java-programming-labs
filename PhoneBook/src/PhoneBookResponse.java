/*
 *  PhoneBookResponse - abstract class for server-to-client communication, i.e. sending back responses to requests
 *
 *  Author: Iwo Bujkiewicz
 *  Date:   06 Jan 2017
 */

import java.util.UUID;

abstract class PhoneBookResponse {
	
	private final UUID clientID;
	private final UUID requestID;
	
	protected PhoneBookResponse(UUID clientID, UUID requestID) {
		this.clientID = clientID;
		this.requestID = requestID;
	}
	
	protected abstract String getMessage();
}
