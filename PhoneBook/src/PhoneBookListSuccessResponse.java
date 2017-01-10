/*
 *  PhoneBookListSuccessResponse - class for communicating a request success with a list result to the client
 *
 *  Author: Iwo Bujkiewicz
 *  Date:   09 Jan 2017
 */

import java.util.List;
import java.util.StringJoiner;
import java.util.UUID;

public class PhoneBookListSuccessResponse extends PhoneBookSuccessResponse {
	
	private final List<String> list;
	
	protected PhoneBookListSuccessResponse(List<String> list, UUID clientID, UUID requestID) {
		super(clientID, requestID);
		this.list = list;
	}
	
	protected String getMessage() {
		StringJoiner stringJoiner = new StringJoiner("\n");
		stringJoiner.add("OK ~");
		list.forEach((String s) -> { stringJoiner.add(s); });
		return stringJoiner.toString();
	}
}
