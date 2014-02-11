/**
 * @file SocketAddressParserTest.java
 *
 * @author Ke Yang, Joseph Ciurej
 * @date 04/12/2013
 *
 * @TODO
 */
package edu.illinois.t25;

import edu.illinois.t25.net.SocketAddressParser;
import org.junit.Test;
import org.junit.After;
import org.junit.Before;
import static org.junit.Assert.*;

import java.net.InetAddress;
import java.net.SocketAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;

/**
 * Container class for the test suite that tests the functionality of the 
 * "SocketAddressParser" type.
 */
public class SocketAddressParserTest
{
	/// Testing Constants ///
	
	/**
	 * The port for the test socket.
	 */
	private final static int csSocketPort = 1214;

	/**
	 * The socket on which all the testing functions will perform parsing
	 * tests.
	 */
	private static InetSocketAddress sTestSocket;

	/// Testing Set Up/Tear Down ///
	
	/**
	 * Sets up all the tests by creating a test socket.
	 *
	 * @throws UnknownHostException Thrown if the name of the localhost cannot be
	 *  resolved (shouldn't happen for any network-compatible system).
	 */
	@Before
	public void TestSetup() throws UnknownHostException
	{
		sTestSocket = new InetSocketAddress( InetAddress.getLocalHost(), csSocketPort );
	}

	/**
	 * Destroys the previously established testing socket.
	 */
	@After
	public void TestTeardown()
	{
		sTestSocket = null;
	}


	/// Testing Functions ///

	@Test
	public void TestRemoteHostnameParsing() throws UnknownHostException
	{
		String hostname = SocketAddressParser.parseRemoteHostname( sTestSocket );

		assertEquals( "Hostname for local remote host should be local host's name.",
			InetAddress.getLocalHost().getHostName(), hostname );
	}

	@Test
	public void TestRemoteIPParsing()
	{
		String ip = SocketAddressParser.parseRemoteIP( sTestSocket );

		assertEquals( "IP for local remote host should be localhost address.",
			"127.0.1.1", ip );
	}

	@Test
	public void TestRemotePortParsing()
	{
		String port = SocketAddressParser.parseRemotePort( sTestSocket );

		assertEquals( "Port for local remote host should be connection port.",
			"" + csSocketPort, port );
	}

	@Test
	public void TestRemoteAddressParsing() throws UnknownHostException
	{
		InetAddress address = SocketAddressParser.parseRemoteAddress( sTestSocket );

		assertEquals( "IP Address for local remote host should be local host address.",
			InetAddress.getLocalHost(), address );
	}

	@Test
	public void TestSocketAddressParsing()
	{
		SocketAddress socketAddr = 
			SocketAddressParser.parseSocketAddress( sTestSocket.toString() );

		assertEquals(
			"Socket's parsed string representation should result in the original socket.",
			sTestSocket, socketAddr 
		);
	}

}
