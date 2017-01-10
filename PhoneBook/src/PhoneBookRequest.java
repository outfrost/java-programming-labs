/*
 *  PhoneBookRequest - abstract class for client-to-server communication and processing client's requests on the server
 *
 *  Author: Iwo Bujkiewicz
 *  Date:   06 Jan 2017
 */

import java.util.UUID;

abstract class PhoneBookRequest {
	
	private final UUID clientID;
	private final UUID requestID;
	
	protected PhoneBookRequest(UUID clientID) {
		this.clientID = clientID;
		this.requestID = UUID.randomUUID();
	}
	
	protected abstract PhoneBookResponse processRequest(PhoneBook phoneBook);
	protected abstract String getLogMessage();
	
	protected String getClientIDTail() {
		return "~" + Long.toHexString(clientID.getLeastSignificantBits() % 0x10000L);
	}
	
	protected String getRequestIDTail() {
		return "[~" + Long.toHexString(requestID.getLeastSignificantBits() % 0x10000L) + "]";
	}
	
	protected UUID getClientID() {
		return new UUID(clientID.getMostSignificantBits(), clientID.getLeastSignificantBits());
	}
	
	protected UUID getRequestID() {
		return new UUID(requestID.getMostSignificantBits(), requestID.getLeastSignificantBits());
	}
}
