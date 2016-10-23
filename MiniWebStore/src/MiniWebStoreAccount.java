/*
 *  MiniWebStoreAccount - serializable class for user identity objects
 *
 *  Author: Iwo Bujkiewicz
 *  Date: 22 Oct 2016
 */

import com.sun.javaws.exceptions.InvalidArgumentException;

import java.io.Serializable;
import java.util.UUID;

public class MiniWebStoreAccount implements Serializable {
	private UUID id;
	private String username;
	private int password;
	private String name;
	private String address;
	private long balance;
	private boolean adminPrivileges;
	
	public MiniWebStoreAccount(String username, int password, String name, String address) {
		this.id = UUID.randomUUID();
		this.username = username;
		this.password = password;
		this.name = name;
		this.address = address;
		this.balance = 0L;
		this.adminPrivileges = false;
	}
	
	public MiniWebStoreAccount(boolean adminAccount) {
		this.id = UUID.randomUUID();
		this.username = "admin";
		this.password = "Kaer Mohren".hashCode();
		this.name = "Merchant";
		this.address = "MiniWebStore";
		this.balance = 0L;
		adminPrivileges = adminAccount;
	}
	
	public UUID getId() {
		return new UUID(id.getMostSignificantBits(), id.getLeastSignificantBits());
	}
	public String getUsername() {
		return username;
	}
	public int getPasswordHash() {
		return password;
	}
	public String getName() {
		return name;
	}
	public String getAddress() {
		return address;
	}
	public long getBalance() {
		return balance;
	}
	public boolean hasAdminPrivileges() { return adminPrivileges; }
	public void setNewPassword(int newPassword) {
		password = newPassword;
	}
	
	public void modifyBalance(long value) throws InvalidArgumentException {
		if (balance + value >= 0L)
			balance += value;
		else
			throw new InvalidArgumentException(new String[]{"Balance cannot be negative"});
	}
	
	@Override
	public String toString() {
		return getName() + "\t" + getAddress() + "\t" + getBalance() + (hasAdminPrivileges() ? "\t(*)" : "");
	}
}
