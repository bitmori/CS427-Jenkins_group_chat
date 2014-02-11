package edu.illinois.t25.data;

import java.net.InetAddress;
import java.util.Scanner;

public class JenkinsUser {
	private String name;
	private InetAddress address;
	private String group;

	// / Initialize ///
	/**
	 * Function to load username from hashtable or create a new username if it
	 * it's a new user
	 * 
	 * @param addr
	 * @return
	 */
	public String loadName() {
		String n = UserLibrary.users.get(address).getName();
		return n;
	}

	/**
	 * 
	 * @return the existing Jenkins User if exists else return the new Jenkins
	 *         User
	 */
	public static JenkinsUser loadIP() {
		InetAddress addr;
		try {
			InetAddress thisIp = InetAddress.getLocalHost();
			System.out.println("IP:" + thisIp.getHostAddress());
			addr = thisIp;
		} catch (Exception e) {
			System.out.println("Failed obtaining IP address.");
			addr = null;
			e.printStackTrace();
		}

		JenkinsUser tmpuser = UserLibrary.users.get(addr);
		if (tmpuser != null)
			return tmpuser;

		if (addr != null)
			return new JenkinsUser(addr);
		else
			return null;
	}

	// / Constructor ///
	/**
	 * 
	 * @param name
	 *            name of user
	 * @param address
	 *            ip address of user
	 * @param group
	 *            group name of user
	 */
	public JenkinsUser(String name, InetAddress address, String group) {
		this.name = name;
		this.address = address;
		this.group = group;
	}

	/**
	 * Default constructor - gets IP address and loads user name from disk or
	 * creates new user
	 */
	public JenkinsUser(InetAddress addr) {
		if (UserLibrary.users.get(addr) == null) {
			this.address = addr;
			group = "";

			Scanner scan = new Scanner(System.in);
			System.out.println("New User: Please enter a user name:");
			String uname = scan.nextLine();
			System.out.println("Please enter a group name:");
			String newgroup = scan.nextLine();

			// TODO: Possible input type check here on group and valid user name

			this.name = uname;
			this.group = newgroup;

			toHashtable();
			toFile();
		} else
			System.out.println("User already exists in library.");
	}

	/**
	 * Constructor
	 * 
	 * @param name
	 *            user name
	 * @param address
	 *            IP address of user
	 */
	public JenkinsUser(String name, InetAddress address) {
		this.name = name;
		this.address = address;
	}

	// / Methods ///
	/**
	 * Adds a new user to the hash table
	 */
	private void toHashtable() {
		UserLibrary.addUser(this);
	}

	/**
	 * Adds a new user to the file
	 */
	private void toFile() {
		UserLibrary.writeToFile(this);
	}

	/**
	 * Function to turn JenkinsUser obj into a string to output to file
	 * 
	 * @return returns the string form of the user
	 */
	@Override
	public String toString() {
		return this.getAddressStr() + "," + this.name + "," + group;
	}

	/**
	 * equals function that checks if two objects are equal
	 * 
	 * @param object
	 *            the object that is compared
	 * @return True if the given object is a Jenkins user with identical fields
	 *         and false otherwise.
	 */
	@Override
	public boolean equals(Object object) {
		if (object instanceof JenkinsUser) {
			JenkinsUser other = (JenkinsUser) object;
			return name.equals(other.name) && address.equals(other.address)
					&& group.equals(other.group);
		} else {
			return false;
		}
	}

	// ------------ GETTERS AND SETTERS ------------ //

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the address
	 */
	public InetAddress getAddressInet() {
		return address;
	}

	/**
	 * Funtion to get the address in String format
	 * 
	 * @return address as string
	 */
	public String getAddressStr() {
		int index = address.toString().lastIndexOf('/');
		return address.toString().substring(index + 1,
				address.toString().length());
	}

	/**
	 * @param address
	 *            the address to set
	 */
	public void setAddress(InetAddress address) {
		this.address = address;
	}

	/**
	 * @return the group
	 */
	public String getGroup() {
		return group;
	}

	/**
	 * @param group
	 *            the group to set
	 */
	public void setGroup(String group) {
		this.group = group;
	}
}
