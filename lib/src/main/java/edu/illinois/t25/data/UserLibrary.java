package edu.illinois.t25.data;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;

public class UserLibrary {

	public static Hashtable<InetAddress, JenkinsUser> users = new Hashtable<InetAddress, JenkinsUser>();
	private static String path;

	/**
	 * Default Constructor
	 * 
	 * @throws IOException
	 */
	public UserLibrary() throws IOException {
		getPath();
		parseFile(path);
	}

	/**
	 * Function to parse data file or create a new file if one does not exist
	 * 
	 * @param path
	 *            path of the file name
	 * @throws IOException
	 */
	public void parseFile(String path) throws IOException {
		File f = new File(path);
		if (f.exists()) {
			BufferedReader reader = new BufferedReader(new FileReader(path));
			try {
				String line = reader.readLine();
				while (line != null) {
					JenkinsUser usr = buildUser(line);
					addUser(usr);
					line = reader.readLine();
				}

			} catch (FileNotFoundException e) {
				System.out.println("Here");
				e.printStackTrace();
			}
		} else
			System.out.println(f.createNewFile());
	}

	/**
	 * Function to take a string from file, parse it, and create a jenkins user
	 * 
	 * @param str
	 *            string from file
	 * @return new JenkinsUser
	 * @throws UnknownHostException
	 */
	public JenkinsUser buildUser(String str) throws UnknownHostException {
		String name;
		InetAddress addr;
		String group;

		List<String> elems = Arrays.asList(str.split(","));
		name = elems.get(1);
		addr = InetAddress.getByName(elems.get(0));
		group = elems.get(2);

		return new JenkinsUser(name, addr, group);
	}

	/**
	 * Function to write a JenkinsUser to the file
	 * 
	 * @param path
	 */
	public static void writeToFile(JenkinsUser user) {
		try {
			PrintWriter writer = new PrintWriter(new BufferedWriter(
					new FileWriter(path, true)));
			writer.println(user.toString());
			writer.close();
		} catch (IOException e) {
			System.out.println("Writing user to file IO error...");
		}

	}

	/**
	 * returns the user table
	 * 
	 * @return the users
	 */
	public static Hashtable<InetAddress, JenkinsUser> getUsers() {
		return users;
	}

	/**
	 * set Users to a hashTable
	 * 
	 * @param users
	 *            the users to set
	 */
	public static void setUsers(Hashtable<InetAddress, JenkinsUser> users) {
		UserLibrary.users = users;
	}

	/**
	 * Function to add a user to the hashtable
	 * 
	 * @param user
	 */
	public static void addUser(JenkinsUser user) {
		users.put(user.getAddressInet(), user);
	}

	/**
	 * Function that returns the path of the user library path
	 * 
	 * @return the path
	 */
	public static String getPath() {

		String home = System.getProperty("user.home");
		path = home + "/jenkins/";
		File f = new File(path);
		f.mkdirs();

		path += "client_ip_mapping.txt";

		return path;
	}

	/**
	 * Get and set path of the file
	 * 
	 * @param path
	 *            the path to set
	 */
	public static void setPath(String path) {
		UserLibrary.path = path;
	}
}
