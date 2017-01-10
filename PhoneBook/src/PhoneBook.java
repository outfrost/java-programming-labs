/*
 *  PhoneBook - serializable class representing a virtual phone book for server-side lookup
 *
 *  Author: Iwo Bujkiewicz
 *  Date:   06 Jan 2017
 */

import java.io.*;
import java.net.Socket;
import java.util.Enumeration;
import java.util.StringJoiner;
import java.util.concurrent.ConcurrentHashMap;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

class PhoneBook extends ConcurrentHashMap<String, String> {
	
	private static final long serialVersionUID = 0L;
	
	protected static PhoneBook load(String fileName) throws IOException, ClassNotFoundException {
		FileInputStream fileInputStream = new FileInputStream(fileName);
		GZIPInputStream gzipInputStream = new GZIPInputStream(fileInputStream);
		ObjectInputStream objectInputStream = new ObjectInputStream(gzipInputStream);
		PhoneBook loadedPhoneBook = (PhoneBook)objectInputStream.readObject();
		objectInputStream.close();
		return loadedPhoneBook;
	}
	
	protected void save(String fileName) throws IOException {
		FileOutputStream fileOutputStream = new FileOutputStream(fileName);
		GZIPOutputStream gzipOutputStream = new GZIPOutputStream(fileOutputStream);
		ObjectOutputStream objectOutputStream = new ObjectOutputStream(gzipOutputStream);
		objectOutputStream.writeObject(this);
		objectOutputStream.flush();
		objectOutputStream.close();
	}
	
	protected String listNames() {
		StringJoiner listJoiner = new StringJoiner("\n");
		Enumeration<String> names = this.keys();
		while (names.hasMoreElements())
			listJoiner.add(names.nextElement());
		return listJoiner.toString();
	}
}
