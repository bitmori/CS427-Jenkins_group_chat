package edu.illinois.t25;

import edu.illinois.t25.data.*;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class UserLibraryTest {
	
/**
 *  Notes to run test cases:
 * 	tests are build off of user input of "tim" as user name and "t25" for the group, failure
 * 	to enter these input parameters will result in failed tests	
 */
	ByteArrayInputStream in;
	ByteArrayInputStream in2;
	@Before
	public void init()
	{
		in = new ByteArrayInputStream("tim\nt25".getBytes());
		in2 = new ByteArrayInputStream("joe\nt24".getBytes());
		
		try {
			UserLibrary ulib = new UserLibrary();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void TestLibraryCreation()
	{	
		File f = new File(UserLibrary.getPath());
		assertTrue(f.exists());
	}
	
	@Test
	public void TestInsertNewUser()
	{
		System.setIn(in2);
		
		try {
			new JenkinsUser(InetAddress.getByName("192.17.202.47"));
			assertTrue(UserLibrary.users.get(InetAddress.getByName("192.17.202.47")) != null);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void TestGetUserName()
	{
		try {
			assertTrue(UserLibrary.users.get(InetAddress.getByName("192.17.202.47")).getName().equals("joe"));
			assertTrue(UserLibrary.users.get(InetAddress.getByName("192.17.202.47")).getGroup().equals("t24"));
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void TestGetUserFromHash()
	{
		System.setIn(in);
		
		System.out.println("Enter 'tim' as user name, 't25' as group.");
		JenkinsUser.loadIP();
		
		InetAddress addr;
		try {
	        addr = InetAddress.getLocalHost();
	    } catch (Exception e) {
	    	addr = null;
	        e.printStackTrace();
	    }
		
		assertTrue(UserLibrary.users.get(addr) != null);
		assertTrue(UserLibrary.users.get(addr).getName().equals("tim"));
		assertTrue(UserLibrary.users.get(addr).getGroup().equals("t25"));
	}
	
	@Test
	public void TestGetUserFromFile()
	{
		String lastline = "";
		String curr;
		
		try {
			BufferedReader br = new BufferedReader(new FileReader(UserLibrary.getPath()));
		
			while((curr = br.readLine()) != null)
			{
				lastline = curr;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		System.out.println(lastline);
		//assertEquals("192.17.193.85,tim,t25", lastline);
		//assertEquals("127.0.1.1,tim,t25", lastline);
	}
}
