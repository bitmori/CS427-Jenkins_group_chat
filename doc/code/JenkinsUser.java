package edu.illinois.t25.data;

import java.net.InetAddress;

public class JenkinsUser 
{
	private String name;
	private InetAddress address;

	/**
	 * 
	 * @param addr
	 * @return
	 */
	public static String loadName(InetAddress addr)
	{
		return null;
	}
	
	/**
	 * Default constructor - gets IP address and loads user name from disk or creates new user
	 */
	public JenkinsUser()
	{
		try {
	        InetAddress thisIp = InetAddress.getLocalHost();
	        System.out.println("IP:" + thisIp.getHostAddress());
	        this.address = thisIp;
	    } catch (Exception e) {
	    	System.out.println("Failed obtaining IP address.");
	    	this.address = null;
	        e.printStackTrace();
	    }
		
		if(this.address != null)
			this.name = loadName(this.address);
		else
			newUser();
	}
	
	public void newUser()
	{
		
	}
	
	/**
	 * Constructor
	 * @param name user name
	 * @param address IP address of user
	 */
	public JenkinsUser(String name, InetAddress address)
	{
		this.name = name;
		this.address = address;
	}

// ------------ GETTERS AND SETTERS ------------ //
	
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the address
	 */
	public InetAddress getAddress() {
		return address;
	}

	/**
	 * @param address the address to set
	 */
	public void setAddress(InetAddress address) {
		this.address = address;
	}
	
	public static void main(String [] args)
	{
		
	}
}
