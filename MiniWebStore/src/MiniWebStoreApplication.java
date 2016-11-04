/*
 *  MiniWebStoreApplication - primitive web store main program
 *
 *  Author: Iwo Bujkiewicz
 *  Date: 22 Oct 2016
 */

import java.io.*;
import java.util.ArrayList;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * Main class of the MiniWebStore application.
 * Controls the data model and the user interface.
 *
 * @author  Iwo Bujkiewicz
 * @version 20161101
 */
public class MiniWebStoreApplication {
	
	/**
	 * Runs the application synchronously when executed.
	 * @param args  Arguments provided through the console command; not yet in use
	 */
	public static void main(String[] args) {
		MiniWebStore store = new MiniWebStore();
		UserDialog dialog = new ConsoleUserDialog();
		//UserDialog dialog = new JOptionPaneUserDialog();
		
		dialog.printInfoMessage("Welcome to MiniWebStore.");
		boolean run = true;
		while (run) {
			int greeterChoice = dialog.enterInt("MENU\n" +
				                                    "1> Sign in\n" +
				                                    "2> Register\n" +
				                                    "3> Load database\n" +
				                                    "4> Save database\n" +
				                                    "0> Quit");
			if (greeterChoice == 0) {
				char confirmation = dialog.enterChar("Are you sure you want to quit? Any unsaved data will be lost. [y/n]");
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
					store = deserializeStore(filename);
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
	
	/**
	 * Prompts the user to register a new account in a store.
	 * @param dialog    Instantiated user dialog implementation to use
	 * @param store     Store to register in
	 */
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
	
	/**
	 * Prompts the user to sign in to their account and perform store actions once signed in.
	 * @param dialog    Instantiated user dialog implementation to use
	 * @param store     Store to sign into
	 */
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
								                                      "3> Modify item stock\n" +
								                                      "4> Change item price\n" +
								                                      "5> Remove an item\n" +
								                                      "0> Back to store administration");
							if (stockMenuChoice == 0)
								inStockMenu = false;
							else if (stockMenuChoice == 1)
								listStoreItems(store, dialog);
							else if (stockMenuChoice == 2)
								addNewItemToStore(store, dialog);
							else if (stockMenuChoice == 3)
								modifyItemStock(store, dialog);
							else if (stockMenuChoice == 4)
								changeItemPrice(store, dialog);
							else if (stockMenuChoice == 5)
								removeItemFromStore(store, dialog);
						}
					}
					else if (adminChoice == 2) {
						dialog.printInfoMessage("Sales up to this moment have amounted to:\n" +
							                        FixedPointFormat.decimal2Places(store.getSales()));
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
					int customerChoice = dialog.enterInt("Your account balance is " + FixedPointFormat.decimal2Places(sessionUser.getBalance()) + ".\n" +
						                                     "How can I help you today?\n" +
						                                     "1> Add funds\n" +
						                                     "2> List available items\n" +
						                                     "3> Buy item\n" +
						                                     "4> Change password\n" +
						                                     "0> Sign out");
					if (customerChoice == 0)
						sessionRunning = false;
					else if (customerChoice == 1)
						addAccountFunds(sessionUser, dialog);
					else if (customerChoice == 2)
						listStoreItems(store, dialog);
					else if (customerChoice == 3)
						buyItem(store, sessionUser, dialog);
					else if (customerChoice == 4)
						changePassword(sessionUser, dialog);
				}
			}
		}
	}
	
	/**
	 * Puts a store object through a gzip pipeline and writes it to a binary file.
	 * @param store     Store to serialize
	 * @param filename  Name (with extension) of the file to write
	 * @throws IOException  If the file exists but is a directory instead of a regular file, does not exist and cannot be created, cannot be opened, write access to it is denied, a different I/O error occurs, or there is a problem with the store being serialized
	 */
	private static void serializeStore(MiniWebStore store, String filename) throws IOException {
		FileOutputStream fileOutputStream = new FileOutputStream(filename);
		GZIPOutputStream gzipOutputStream = new GZIPOutputStream(fileOutputStream);
		ObjectOutputStream objectOutputStream = new ObjectOutputStream(gzipOutputStream);
		objectOutputStream.writeObject(store);
		objectOutputStream.flush();
		objectOutputStream.close();
	}
	
	/**
	 * Reads a store object from a binary file through a gzip pipeline.
	 * @param filename  Name (with extension) of the file to read from
	 * @return          Store object loaded from the file
	 * @throws IOException              If the file does not exist, exists but is a directory instead of a regular file, cannot be opened, read access to it is denied, or a different I/O error occurs
	 * @throws ClassNotFoundException   If the file does not contain an object of the {@link MiniWebStore} class, or any object data at all
	 */
	private static MiniWebStore deserializeStore(String filename) throws IOException, ClassNotFoundException {
		FileInputStream fileInputStream = new FileInputStream(filename);
		GZIPInputStream gzipInputStream = new GZIPInputStream(fileInputStream);
		ObjectInputStream objectInputStream = new ObjectInputStream(gzipInputStream);
		MiniWebStore loadedStore = (MiniWebStore)objectInputStream.readObject();
		objectInputStream.close();
		return loadedStore;
	}
	
	/**
	 * Prints a list of registered accounts in a store.
	 * @param store     Store to read from
	 * @param dialog    Instantiated user dialog implementation to use
	 */
	private static void listStoreAccounts(MiniWebStore store, UserDialog dialog) {
		StringBuilder listBuilder = new StringBuilder("Name\tAddress\tAccount balance\tAdmin privileges\n");
		ArrayList<MiniWebStoreAccount> accounts = store.getAccounts();
		for (MiniWebStoreAccount account : accounts)
			listBuilder.append(account.toString() + "\n");
		dialog.printMessage(listBuilder.toString());
	}
	
	/**
	 * Prints a list of items available in a store.
	 * @param store     Store to read from
	 * @param dialog    Instantiated user dialog implementation to use
	 */
	private static void listStoreItems(MiniWebStore store, UserDialog dialog) {
		StringBuilder listBuilder = new StringBuilder("#  Item name\tStock\tUnit price\n");
		ArrayList<MiniWebStoreItem> items = store.getItems();
		for (int i = 0; i < items.size(); i++)
			listBuilder.append(i + ". " + items.get(i).toString() + "\n");
		dialog.printMessage(listBuilder.toString());
	}
	
	/**
	 * Prompts the user to add a new item to a store.
	 * @param store     Store to add the item to
	 * @param dialog    Instantiated user dialog implementation to use
	 */
	private static void addNewItemToStore(MiniWebStore store, UserDialog dialog) {
		String name = dialog.enterString("Type a name for the new item.");
		long unitPrice = (long)(dialog.enterDouble("Enter a unit price using '.' as decimal separator.") * 100d);
		if (unitPrice >= 0L)
			store.addItem(new MiniWebStoreItem(name, unitPrice));
		else
			dialog.printErrorMessage("Unit price cannot be negative.");
	}
	
	/**
	 * Prompts the user to modify the number of units of an item in stock.
	 * @param store     Store to modify an item in
	 * @param dialog    Instantiated user dialog implementation to use
	 */
	private static void modifyItemStock(MiniWebStore store, UserDialog dialog) {
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
				} catch (IllegalArgumentException e) {
					dialog.printErrorMessage("Cannot reduce item stock below zero.");
				}
			}
			else
				dialog.printInfoMessage("Operation aborted.");
		}
	}
	
	/**
	 * Prompts the user to change the unit price of an item.
	 * @param store     Store to modify an item in
	 * @param dialog    Instantiated user dialog implementation to use
	 */
	private static void changeItemPrice(MiniWebStore store, UserDialog dialog) {
		int itemIndex = dialog.enterInt("Enter the item number.");
		MiniWebStoreItem item = store.getItemByIndex(itemIndex);
		if (item == null)
			dialog.printErrorMessage("No such item.");
		else {
			long newPrice = (long)(dialog.enterDouble("Current unit price is " + FixedPointFormat.decimal2Places(item.getUnitPrice()) + ". Enter new price.") * 100d);
			if (newPrice < 0L)
				dialog.printErrorMessage("Unit price cannot be negative.");
			else {
				char confirmation = dialog.enterChar("You want to change the unit price of " + itemIndex + ". " + item.getName() + " from " + FixedPointFormat.decimal2Places(item.getUnitPrice()) + " to " + FixedPointFormat.decimal2Places(newPrice) + ". Correct? [y/n]");
				if (confirmation == 'y' || confirmation == 'Y') {
					try {
						item.setUnitPrice(newPrice);
					} catch (IllegalArgumentException e) {
						dialog.printErrorMessage(e.getMessage() + " (Something went terribly wrong, please report the issue.)");
					}
				}
				else
					dialog.printInfoMessage("Operation aborted.");
			}
		}
	}
	
	/**
	 * Promts the user to completely remove an item from a store.
	 * @param store     Store to remove an item from
	 * @param dialog    Instantiated user dialog implementation to use
	 */
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
	
	/**
	 * Prompts the user to change their password.
	 * @param account   The user's account
	 * @param dialog    Instantiated user dialog implementation to use
	 */
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
	
	/**
	 * Prompts the user to add funds to their account balance.
	 * @param account   The user's account
	 * @param dialog    Instantiated user dialog implementation to use
	 */
	private static void addAccountFunds(MiniWebStoreAccount account, UserDialog dialog) {
		long amount = (long)(dialog.enterDouble("Enter the amount you wish to deposit using '.' as decimal separator") * 100d);
		if (amount < 0L)
			dialog.printErrorMessage("Cannot deposit negative amounts.");
		else {
			try {
				account.modifyBalance(amount);
			} catch (IllegalArgumentException e) {
				dialog.printErrorMessage("Cannot reduce account balance below zero. (Something went terribly wrong, please report the issue.)");
			}
		}
	}
	
	/**
	 * Prompts the user to buy an item from a store.
	 * @param store     Store to buy from
	 * @param customer  The customer's account
	 * @param dialog    Instantiated user dialog implementation to use
	 */
	private static void buyItem(MiniWebStore store, MiniWebStoreAccount customer, UserDialog dialog) {
		int itemIndex = dialog.enterInt("Enter the item number.");
		MiniWebStoreItem item = store.getItemByIndex(itemIndex);
		if (item == null)
			dialog.printErrorMessage("No such item.");
		else {
			long units = 0L;
			boolean longEntered = false;
			while (!longEntered) {
				try {
					units = Long.parseLong(dialog.enterString("How many units of the item do you want to buy?"), 10);
					longEntered = true;
				} catch (NumberFormatException e){
					longEntered = false;
				}
			}
			if (units <= 0L)
				dialog.printErrorMessage("Number of units to buy must be positive.");
			else {
				if (item.getStock() < units)
					dialog.printErrorMessage("Sorry, not enough units in stock.");
				else {
					if (customer.getBalance() < item.getUnitPrice() * units)
						dialog.printErrorMessage("You do not have enough funds to buy " + units + " units of " + item.getName() + ".");
					else {
						char confirmation = dialog.enterChar("You want to buy " + units + " units of " + item.getName() + " at a unit price of " + FixedPointFormat.decimal2Places(item.getUnitPrice()) + " for a total of " + FixedPointFormat.decimal2Places(units * item.getUnitPrice()) + ". Correct? [y/n]");
						if (confirmation == 'y' || confirmation == 'Y') {
							try {
								store.sellItem(item, customer, units);
							} catch (IllegalArgumentException e) {
								dialog.printErrorMessage(e.getMessage() + " (Something went terribly wrong, please report the issue.)");
							}
						}
						else
							dialog.printInfoMessage("Order cancelled.");
					}
				}
			}
		}
	}
}
