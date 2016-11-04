/*
 *  MiniWebStoreAccount - serializable class for user identity objects
 *
 *  Author: Iwo Bujkiewicz
 *  Date: 22 Oct 2016
 */

import java.io.Serializable;
import java.util.UUID;

/**
 * Provides an object representation of a single user account in a store.
 *
 * Contains information about the account's UUID, username, password hash, user's full name, user's street address, the account's monetary balance, and if the account has administrator privileges.
 *
 * Class is serializable.
 *
 * @author  Iwo Bujkiewicz
 * @version 20161101
 */
public class MiniWebStoreAccount implements Serializable {
	private UUID id;
	private String username;
	private int password;
	private String name;
	private String address;
	private long balance;
	private boolean adminPrivileges;
	
	/**
	 * Creates an account object and initializes fields from provided data.
	 * UUID is generated randomly.
	 * Balance is set to 0.
	 * Administrative privileges are not granted.
	 * @param username  Username (sign in name)
	 * @param password  Password hash (computed integer representation of the password string)
	 * @param name      User's full name
	 * @param address   User's street address
	 */
	public MiniWebStoreAccount(String username, int password, String name, String address) {
		this.id = UUID.randomUUID();
		this.username = username;
		this.password = password;
		this.name = name;
		this.address = address;
		this.balance = 0L;
		this.adminPrivileges = false;
	}
	
	/**
	 * Creates an account object and initializes fields using the default data for the admin account.
	 * UUID is generated randomly.
	 * Username is set to 'admin'.
	 * Password hash is computed from 'Kaer Mohren'.
	 * Name is set to 'Merchant'.
	 * Address is set to 'MiniWebStore'.
	 * Balance is set to 0.
	 * Administrative privileges are GRANTED if the input parameter is set to true.
	 * @param adminAccount  Indicates whether the resulting account should have administrative privileges
	 */
	public MiniWebStoreAccount(boolean adminAccount) {
		this.id = UUID.randomUUID();
		this.username = "admin";
		this.password = "Kaer Mohren".hashCode();
		this.name = "Merchant";
		this.address = "MiniWebStore";
		this.balance = 0L;
		adminPrivileges = adminAccount;
	}
	
	/** Safely returns the account's UUID as a UUID object. */
	public UUID getId() { return new UUID(id.getMostSignificantBits(), id.getLeastSignificantBits()); }
	/** Returns the username. */
	public String getUsername() { return username; }
	/** Returns the password hash. */
	public int getPasswordHash() { return password; }
	/** Returns the user's full name. */
	public String getName() { return name; }
	/** Returns the user's street address. */
	public String getAddress() { return address; }
	/** Returns the account balance. */
	public long getBalance() { return balance; }
	/** Returns true if the account has administrative privileges, false otherwise. */
	public boolean hasAdminPrivileges() { return adminPrivileges; }
	
	/**
	 * Sets a new password hash for the account.
	 * @param newPassword   The new password hash to be assigned
	 */
	public void setNewPassword(int newPassword) { password = newPassword; }
	
	/**
	 * Modifies the account's balance by the provided amount.
	 * Prevents the account's balance from becoming negative.
	 * @param value The amount to add to the balance (can be negative)
	 * @throws IllegalArgumentException Thrown when the account's balance would become negative after the operation
	 */
	public void modifyBalance(long value) throws IllegalArgumentException {
		if (balance + value >= 0L)
			balance += value;
		else
			throw new IllegalArgumentException("Balance cannot be negative");
	}
	
	@Override
	public String toString() {
		return getName() + "\t" + getAddress() + "\t" + FixedPointFormat.decimal2Places(getBalance()) + "\t" + (hasAdminPrivileges() ? "(*)" : "");
	}
}
