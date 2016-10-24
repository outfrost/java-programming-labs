/*
 *  MiniWebStore - serializable class for web store data management
 *
 *  Author: Iwo Bujkiewicz
 *  Date: 22 Oct 2016
 */

import com.sun.javaws.exceptions.InvalidArgumentException;

import java.io.Serializable;
import java.util.ArrayList;

class MiniWebStore implements Serializable {
	private ArrayList<MiniWebStoreAccount> accounts;
	private ArrayList<MiniWebStoreItem> items;
	private long sales;
	
	MiniWebStore() {
		this.accounts = new ArrayList<>();
		this.accounts.add(new MiniWebStoreAccount(true));
		this.items = new ArrayList<>();
		this.sales = 0L;
	}
	
	ArrayList<MiniWebStoreItem> getItems() { return new ArrayList<>(items); }
	ArrayList<MiniWebStoreAccount> getAccounts() { return new ArrayList<>(accounts); }
	long getSales() { return sales; }
	
	MiniWebStoreItem getItemByIndex(int index) {
		MiniWebStoreItem item;
		try {
			item = items.get(index);
		} catch (IndexOutOfBoundsException e) {
			item = null;
		}
		return item;
	}
	
	MiniWebStoreAccount findAccount(String username) {
		for (MiniWebStoreAccount account : accounts) {
			if (account.getUsername() == username)
				return account;
		}
		return null;
	}
	
	MiniWebStoreAccount verifyUser(String username, int password) {
		MiniWebStoreAccount account = findAccount(username);
		return (account.getPasswordHash() == password) ? account : null;
	}
	
	void addAccount(MiniWebStoreAccount account) {
		accounts.add(account);
	}
	
	void addItem(MiniWebStoreItem item) {
		items.add(item);
	}
	
	void removeItem(MiniWebStoreItem item) {
		items.remove(item);
	}
	
	void sellItem(MiniWebStoreItem item, MiniWebStoreAccount customer, long units) throws InvalidArgumentException {
		if (units <= 0L)
			throw new InvalidArgumentException(new String[] { "Number of units to sell must be positive." });
		else
		{
			if (item.getStock() < units)
				throw new InvalidArgumentException(new String[] { "Not enough items in stock" });
			else {
				if (customer.getBalance() < units * item.getUnitPrice())
					throw new InvalidArgumentException(new String[] { "Customer does not have enough balance." });
				else {
					customer.modifyBalance(-1L * units * item.getUnitPrice());
					item.modifyStock(-1L * units);
					sales += units * item.getUnitPrice();
				}
			}
		}
	}
}
