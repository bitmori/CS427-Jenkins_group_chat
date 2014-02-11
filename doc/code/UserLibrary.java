package edu.illinois.t25.data;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.util.Hashtable;
import java.util.Properties;
import java.util.Set;

public class UserLibrary {
	
	private static Hashtable<InetAddress, String> users = new Hashtable<InetAddress, String>();
	private Properties props = null;
	
	/**
	 * Default Constructor
	 */
    public UserLibrary()
    {   
    	String home = System.getProperty("user.home");
		File f = new File(home + "/jenkins_chat/users.properties");
		
		// File with users exist, parse file and store in users ht
		if(f.exists())
		{
	        InputStream in_stream = null;
	        
	        try {
	            this.props = new Properties();
	            
	            in_stream = this.getClass().getResourceAsStream(home + "/jenkins_chat/users.properties");
	            props.load(in_stream);
	        } catch (FileNotFoundException e) {
	            e.printStackTrace();
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
		}
		else
			System.out.println("DOES NOT EXIST");
    }
     
    public Set<Object> getAllKeys(){
        Set<Object> keys = props.keySet();
        return keys;
    }
     
    public String getPropertyValue(String key){
        return this.props.getProperty(key);
    }
     
    public static void main(String a[]){
         
    	UserLibrary mpc = new UserLibrary();
        Set<Object> keys = mpc.getAllKeys();
        for(Object k:keys){
        	InetAddress addr = (InetAddress)k;
            String key = (String)k;
            String name = mpc.getPropertyValue(key);
            
            getUsers().put(addr, name);
        }
    }

	/**
	 * @return the users
	 */
	public static Hashtable<InetAddress, String> getUsers() {
		return users;
	}

	/**
	 * @param users the users to set
	 */
	public static void setUsers(Hashtable<InetAddress, String> users) {
		UserLibrary.users = users;
	}
}
