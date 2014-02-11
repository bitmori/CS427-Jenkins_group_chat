/**
 * @file ChatServerTest.java
 *
 * @author Piyush Bathwal, Joseph Ciurej
 * @date Fall 2013
 *
 * @TODO
 * - Many of the tests have similar set up/tear down logic, which may be
 *   able to be refactored into different classes of tests (async tests,
 *   sync tests, et cetera).
 * - Update the message sending test to test for selective message sending
 *   instead of global message sending once the logic is updated.
 * - Add more rigorous message sending tests once the integration step
 *   begins to ensure that messages are only sent to particular clients.
 * - Update the unit tests here once the implementation is updated fully
 *   with the 'netty-socketio' framework.
 */
package edu.illinois.t25;

import edu.illinois.t25.net.*;
import org.junit.Test;
import org.junit.After;
import org.junit.Before;
import static org.junit.Assert.*;

import java.net.InetAddress;
import java.net.Socket;

/**
 * Container class for the test suite that tests the functionality of the 
 * "ChatServer" type.
 */
public class ChatServerTest
{
	/// Testing Constants ///
	
	/**
	 * The port number to which the testing server is initialized.
	 */
	private final static int csTestServerPort = 1214;

	/**
	 * A testing message sent to clients via the message server for testing
	 * purposes.
	 */
	private final static ChatMessage csTestMessage = new ChatMessage( "a", "g", "c" );

	/// Testing Variables ///
	
	/** 
	 * A test chat server object instance on which all the tests are
	 * performed. 
	 */
	private ChatServer mTestServer;

	/// Testing Set Up/Tear Down ///
	
	/**
	 * Sets up all the tests by creating a test server.
	 */
	@Before
	public void TestSetup()
	{
		mTestServer = new ChatServer( csTestServerPort );
	}

	/**
	 * Tears down the server initialized for the previous test.
	 */
	@After
	public void TestTeardown()
	{
		mTestServer.shutdown();
	}

	/// Testing Functions ///
	
	@Test
	public void TestServerSetup()
	{
		// This test is only meant to assert that the server can be initialized
		// on the host machine, so no more implementation is needed here.
	}

}
