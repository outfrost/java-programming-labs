/*
 *  MiniWebStoreApplication - primitive web store main program
 *
 *  Author: Iwo Bujkiewicz
 *  Date: 22 Oct 2016
 */

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class MiniWebStoreApplication {
	
	public static void main(String[] args) {
		MiniWebStore store = new MiniWebStore();
		UserDialog dialog = new ConsoleUserDialog();
		
		// SIGN IN or REGISTER or LOAD or SAVE
		dialog.printInfoMessage("Welcome to MiniWebStore.");
		boolean run = true;
		while (run) {
			int greeterChoice = dialog.enterInt("How can I help you today?\n" +
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
			if (sessionUser.hasAdminPrivileges()) {
				
			}
			else {
				// CUSTOMER TASKS
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
	
	private static void addNewItemToStore(MiniWebStore store, UserDialog dialog) {
		String name = dialog.enterString("Type a name for the new item.");
		long unitPrice = ((long)dialog.enterDouble("Enter a unit price using '.' as decimal separator.")) * 100L;
		store.addItem(new MiniWebStoreItem());
	}
}
