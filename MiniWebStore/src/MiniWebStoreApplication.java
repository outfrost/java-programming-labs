/*
 *  MiniWebStoreApplication - primitive web store main program
 *
 *  Author: Iwo Bujkiewicz
 *  Date: 22 Oct 2016
 */

import com.sun.javaws.exceptions.InvalidArgumentException;

import java.io.*;
import java.util.ArrayList;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class MiniWebStoreApplication {
	
	public static void main(String[] args) {
		MiniWebStore store = new MiniWebStore();
		UserDialog dialog = new ConsoleUserDialog();
		
		dialog.printInfoMessage("Welcome to MiniWebStore.");
		boolean run = true;
		while (run) {
			int greeterChoice = dialog.enterInt("MENU\n" +
				                                    "1> Sign in\n" +
				                                    "2> Register\n" +
				                                    "3> Load database\n" +
				                                    "4> Save database\n" +
				                                    "0> Exit without saving");
			if (greeterChoice == 0) {
				char confirmation = dialog.enterChar("Are you sure you want to leave without saving? [y/n]");
				if (confirmation == 'y' || confirmation == 'Y')
					run = false;
			}
			else if (greeterChoice == 1)
				startUserSession(dialog, store);
			else if (greeterChoice == 2)
				registerNewAccount(dialog, store);
			else if (greeterChoice == 3) {
				String filename = dialog.enterString("Enter the name of the file to load from.");
				try {
					deserializeStore(store, filename);
				} catch (IOException e) {
					dialog.printErrorMessage("Error reading database file - " + e.getMessage());
				} catch (ClassNotFoundException e) {
					dialog.printErrorMessage("Error - given file does not contain store data.");
				}
			}
			else if (greeterChoice == 4) {
				String filename = dialog.enterString("Choose a filename.");
				try {
					serializeStore(store, filename);
				} catch (IOException e) {
					dialog.printErrorMessage("Error saving database file - " + e.getMessage());
				}
			}
		}
		
	}
	
	private static void registerNewAccount(UserDialog dialog, MiniWebStore store) {
		String username = dialog.enterString("Choose a username. You will use it to sign in.");
		if (username.length() < 3) {
			dialog.printErrorMessage("Cannot use usernames shorter than 3 characters.");
		}
		else if (store.findAccount(username) != null) {
			dialog.printErrorMessage("User with such username already exists.");
		}
		else {
			String password = dialog.enterString("Set a password to secure your account. It has to be at least 4 characters long.");
			if (password.length() < 4) {
				dialog.printErrorMessage("Passwords cannot be shorter than 4 characters.");
			}
			else {
				int passwordHash = password.hashCode();
				String name = dialog.enterString("Enter your name and surname.");
				String address = dialog.enterString("Enter your home address.");
				store.addAccount(new MiniWebStoreAccount(username, passwordHash, name, address));
			}
		}
	}
	
	private static void startUserSession(UserDialog dialog, MiniWebStore store) {
		String username = dialog.enterString("Enter username");
		int password = dialog.enterString("Enter password").hashCode();
		MiniWebStoreAccount sessionUser = store.verifyUser(username, password);
		if (sessionUser == null)
			dialog.printErrorMessage("Wrong username or password.");
		else {
			dialog.printInfoMessage("Hello, " + sessionUser.getName() + ".");
			
			if (sessionUser.hasAdminPrivileges()) {
				boolean sessionRunning = true;
				while (sessionRunning) {
					int adminChoice = dialog.enterInt("Store administration\n" +
						                                 "1> Stock\n" +
						                                 "2> Sales\n" +
						                                 "3> Accounts\n" +
						                                 "4> Change password\n" +
						                                 "0> Sign out");
					if (adminChoice == 0)
						sessionRunning = false;
					else if (adminChoice == 1) {
						boolean inStockMenu = true;
						while (inStockMenu) {
							int stockMenuChoice = dialog.enterInt("Stock\n" +
								                                      "1> List items\n" +
								                                      "2> Add a new item\n" +
								                                      "3> Replenish item stock\n" +
								                                      "4> Remove an item\n" +
								                                      "0> Back to store administration");
							if (stockMenuChoice == 0)
								inStockMenu = false;
							else if (stockMenuChoice == 1)
								listStoreItems(store, dialog);
							else if (stockMenuChoice == 2)
								addNewItemToStore(store, dialog);
							else if (stockMenuChoice == 3)
								replenishStock(store, dialog);
							else if (stockMenuChoice == 4)
								removeItemFromStore(store, dialog);
						}
					}
					else if (adminChoice == 2) {
						dialog.printInfoMessage("Sales up to this moment have amounted to:\n" +
							                        store.getSales() / 100L + "." + store.getSales() % 100L);
					}
					else if (adminChoice == 3)
						listStoreAccounts(store, dialog);
					else if (adminChoice == 4)
						changePassword(sessionUser, dialog);
				}
			}
			else {
				boolean sessionRunning = true;
				while (sessionRunning) {
					int customerChoice = dialog.enterInt("Your account balance is " + sessionUser.getBalance() / 100L + "." + sessionUser.getBalance() % 100 + ".\n" +
						                                     "How can I help you today?\n" +
						                                     "1> Add funds\n" +
						                                     "2> List available items\n" +
						                                     "3> Buy item\n" +
						                                     "4> Change password\n" +
						                                     "0> Sign out");
					if (customerChoice == 0)
						sessionRunning = false;
					else if (customerChoice == 1)
						
				}
			}
		}
	}
	
	private static void serializeStore(MiniWebStore store, String filename) throws IOException {
		FileOutputStream fileOutputStream = new FileOutputStream(filename);
		GZIPOutputStream gzipOutputStream = new GZIPOutputStream(fileOutputStream);
		ObjectOutputStream objectOutputStream = new ObjectOutputStream(gzipOutputStream);
		objectOutputStream.writeObject(store);
		objectOutputStream.flush();
		objectOutputStream.close();
	}
	
	private static void deserializeStore(MiniWebStore store, String filename) throws IOException, ClassNotFoundException {
		FileInputStream fileInputStream = new FileInputStream(filename);
		GZIPInputStream gzipInputStream = new GZIPInputStream(fileInputStream);
		ObjectInputStream objectInputStream = new ObjectInputStream(gzipInputStream);
		MiniWebStore loadedStore = (MiniWebStore)objectInputStream.readObject();
		objectInputStream.close();
		store = loadedStore;
	}
	
	private static void listStoreAccounts(MiniWebStore store, UserDialog dialog) {
		StringBuilder listBuilder = new StringBuilder("Name\tAddress\tAccount balance\tAdmin privileges\n");
		ArrayList<MiniWebStoreAccount> accounts = store.getAccounts();
		for (MiniWebStoreAccount account : accounts)
			listBuilder.append(account.toString() + "\n");
		dialog.printMessage(listBuilder.toString());
	}
	
	private static void listStoreItems(MiniWebStore store, UserDialog dialog) {
		StringBuilder listBuilder = new StringBuilder("#  Item name\tStock\tUnit price\n");
		ArrayList<MiniWebStoreItem> items = store.getItems();
		for (int i = 0; i < items.size(); i++)
			listBuilder.append(i + ". " + items.get(i).toString() + "\n");
		dialog.printMessage(listBuilder.toString());
	}
	
	private static void addNewItemToStore(MiniWebStore store, UserDialog dialog) {
		String name = dialog.enterString("Type a name for the new item.");
		long unitPrice = ((long)dialog.enterDouble("Enter a unit price using '.' as decimal separator.")) * 100L;
		store.addItem(new MiniWebStoreItem(name, unitPrice));
	}
	
	private static void replenishStock(MiniWebStore store, UserDialog dialog) {
		int itemIndex = dialog.enterInt("Enter the item number.");
		MiniWebStoreItem item = store.getItemByIndex(itemIndex);
		if (item == null)
			dialog.printErrorMessage("No such item.");
		else {
			long stockToAdd = 0L;
			boolean longEntered = false;
			while (!longEntered) {
				try {
					stockToAdd = Long.parseLong(dialog.enterString("Enter the number of units by which to modify stock."), 10);
					longEntered = true;
				} catch (NumberFormatException e) {
					longEntered = false;
				}
			}
			char confirmation = dialog.enterChar("You want to add " + stockToAdd + " units to item " + itemIndex + ". " + item.getName() + ". Correct? [y/n]");
			if (confirmation == 'y' || confirmation == 'Y') {
				try {
					item.modifyStock(stockToAdd);
				} catch (InvalidArgumentException e) {
					dialog.printErrorMessage("Cannot reduce item stock below zero.");
				}
			}
			else
				dialog.printInfoMessage("Operation aborted.");
		}
	}
	
	private static void removeItemFromStore(MiniWebStore store, UserDialog dialog) {
		int itemIndex = dialog.enterInt("Enter the item number.");
		MiniWebStoreItem item = store.getItemByIndex(itemIndex);
		if (item == null)
			dialog.printErrorMessage("No such item.");
		else {
			char confirmation = dialog.enterChar("You want to remove item " + itemIndex + ". " + item.getName() + " from store completely. Are you sure? [y/n]");
			if (confirmation == 'y' || confirmation == 'Y')
				store.removeItem(item);
			else
				dialog.printInfoMessage("Operation aborted.");
		}
	}
	
	private static void changePassword(MiniWebStoreAccount account, UserDialog dialog) {
		int currentPassword = dialog.enterString("Enter your current password.").hashCode();
		if (currentPassword != account.getPasswordHash())
			dialog.printErrorMessage("Your current password does not match.");
		else {
			String newPassword = dialog.enterString("Enter a new password.");
			if (newPassword.length() < 4)
				dialog.printErrorMessage("Passwords cannot be shorter than 4 characters.");
			else
				account.setNewPassword(newPassword.hashCode());
		}
	}
	
	private static void addAccountFunds(MiniWebStoreAccount account, UserDialog dialog) {
		long amount = ((long)dialog.enterDouble("Enter the amount you wish to deposit using '.' as decimal separator")) * 100L;
		if (amount < 0L)
			dialog.printErrorMessage("Cannot deposit negative amounts.");
		else {
			try {
				account.modifyBalance(amount);
			} catch (InvalidArgumentException e) {
				dialog.printErrorMessage("Cannot reduce account balance below zero.");
			}
		}
	}
}
