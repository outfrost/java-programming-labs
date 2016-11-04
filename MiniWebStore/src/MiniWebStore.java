/*
 *  MiniWebStore - serializable class for web store data management
 *
 *  Author: Iwo Bujkiewicz
 *  Date: 22 Oct 2016
 */

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Objects;

/**
 * Provides an object representation of the data model of the store.
 *
 * Contains a list of registered user accounts, a list of items available for sale, and the sum of sales made so far.
 *
 * Class is serializable.
 *
 * @author  Iwo Bujkiewicz
 * @version 20161030
 */
class MiniWebStore implements Serializable {
	private ArrayList<MiniWebStoreAccount> accounts;
	private ArrayList<MiniWebStoreItem> items;
	private long sales;
	
	/**
	 * Creates a store object and initializes fields with default values.
	 * A list containing the default admin account with administrative privileges is assigned to the list of registered accounts.
	 * An empty list of items is assigned to the list of available items.
	 * Sales are set to 0.
	 */
	MiniWebStore() {
		this.accounts = new ArrayList<>();
		this.accounts.add(new MiniWebStoreAccount(true));
		this.items = new ArrayList<>();
		this.sales = 0L;
	}
	
	/** Safely returns a copy of the list of items available for purchase. */
	ArrayList<MiniWebStoreItem> getItems() { return new ArrayList<>(items); }
	/** Safely returns a copy of the list of registered accounts. */
	ArrayList<MiniWebStoreAccount> getAccounts() { return new ArrayList<>(accounts); }
	/** Returns the sum of sales made so far. */
	long getSales() { return sales; }
	
	/**
	 * Returns a reference to an item based on the item's index in the list if present, null otherwise.
	 * @param index Index of the item to find
	 * @return      If the provided index is present in the list, a reference to the item located at that index; otherwise, null
	 */
	MiniWebStoreItem getItemByIndex(int index) {
		MiniWebStoreItem item;
		try {
			item = items.get(index);
		} catch (IndexOutOfBoundsException e) {
			item = null;
		}
		return item;
	}
	
	/**
	 * Finds the account with the given username and returns a reference to it if it exists in the list, null otherwise.
	 * @param username  Username associated with the account to find
	 * @return          If an account with the provided username is present in the list, a reference to that account; otherwise, null
	 */
	MiniWebStoreAccount findAccount(String username) {
		for (MiniWebStoreAccount account : accounts) {
			if (Objects.equals(account.getUsername(), username))
				return account;
		}
		return null;
	}
	
	/**
	 * Returns a reference to an account corresponding to the given username and password hash if there is one, null otherwise.
	 * @param username  Username associated with the account to find
	 * @param password  Password hash to match with the account's data
	 * @return          If an account with the provided username and password hash is found in the list, a reference to that account; otherwise, null
	 */
	MiniWebStoreAccount verifyUser(String username, int password) {
		MiniWebStoreAccount account = findAccount(username);
		return (account != null) ? ((account.getPasswordHash() == password) ? account : null) : null;
	}
	
	/**
	 * Adds a new account to the list of registered accounts.
	 * @param account   The account object to add
	 */
	void addAccount(MiniWebStoreAccount account) { accounts.add(account); }
	
	/**
	 * Adds a new item to the list of available items.
	 * @param item  The item object to add
	 */
	void addItem(MiniWebStoreItem item) { items.add(item); }
	
	/**
	 * Removes the item matching the provided reference from the list of available items.
	 * @param item  The item object to find and remove
	 */
	void removeItem(MiniWebStoreItem item) { items.remove(item); }
	
	/**
	 * Sells the provided quantity of the provided item to the provided consumer.
	 * The provided quantity must be positive.
	 * There must be enough units of said item in stock, and the customer must have enough account balance to pay for the order.
	 * @param item      The item to sell
	 * @param customer  The customer's account
	 * @param units     Number of units to sell
	 * @throws IllegalArgumentException If the provided number of units is not positive, there are not enough units available, or the customer does not have enough balance
	 */
	void sellItem(MiniWebStoreItem item, MiniWebStoreAccount customer, long units) throws IllegalArgumentException {
		if (units <= 0L)
			throw new IllegalArgumentException("Number of units to sell must be positive.");
		else {
			if (item.getStock() < units)
				throw new IllegalArgumentException("Not enough items in stock");
			else {
				if (customer.getBalance() < units * item.getUnitPrice())
					throw new IllegalArgumentException("Customer does not have enough balance.");
				else {
					customer.modifyBalance(-1L * units * item.getUnitPrice());
					item.modifyStock(-1L * units);
					sales += units * item.getUnitPrice();
				}
			}
		}
	}
}
