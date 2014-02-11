/**
 * @file SocketAddressParser.java
 *
 * @author Martin Helmhout, Ke Yang, Joseph Ciurej
 * @date 04/12/2013
 *
 * @TODO
 */
package edu.illinois.t25.net;

import java.net.SocketAddress;
import java.net.InetSocketAddress;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * A container type for parsing functions associated with socket address types.
 * This type can be used to obtain more information about a remote host given
 * the connection on which the local host is communicating with this host.
 *
 * @see "https://svn.apache.org/repos/asf/mina/sandbox/mwebb/mina/src/main/java/org/apache/mina/example/xml/parser/SocketAddressParser.java"
 */
public class SocketAddressParser
{
	/// Static Methods ///

	/**
	 * Given the string representation of a socket address, this function
	 * parses that string and returns a corresponding `SocketAddress` object.
	 *
	 * @param pSocketString The string representation of the socket object
	 *  to be returned.
	 * @return The `SocketAddress` object corresponding to the given string.
	 */
	public static InetSocketAddress parseSocketAddress( String pSocketString )
	{
		String[] socketInfo = parseSocketInfo( pSocketString );

		InetAddress socketAddress;
		try
		{ 
			String hostname = socketInfo[ 1 ];
			socketAddress = InetAddress.getByName( hostname ); 
		}
		catch( UnknownHostException e ) 
		{ 
			socketAddress = InetAddress.getLoopbackAddress(); 
		}

		return new InetSocketAddress( socketAddress, Integer.parseInt(socketInfo[2]) );
	}

	/**
	 * Retrieves the fully-formed IP address of the remote host given the
	 * socket on which communication with this host occurs.
	 *
	 * @param pSocket The socket for which the IP address of the remote
	 *  host will be returned.
	 * @return The fully-formed IP address of the remote host connected
	 *  by the given socket.
	 */
	public static InetAddress parseRemoteAddress( SocketAddress pSocket )
	{
		return parseSocketAddress( pSocket.toString() ).getAddress();
	}

	/**
	 * Extracts and returns the hostname associated with the remote client
	 * on the given socket.
	 *
	 * @param pSocket The socket for which the remote host information will
	 *  be retrieved.
	 * @return The name of the remote host as a string.
	 */
	public static String parseRemoteHostname( SocketAddress pSocket ) 
	{
		return parseSocketInfo( pSocket )[ 0 ];
	}

	/**
	 * Extracts and returns the IP address associated with the remote client
	 * on the given socket.
	 *
	 * @param pSocket The socket for which the remote IP information will
	 *  be retrieved.
	 * @return The IP address of the remote host as a string.
	 */
	public static String parseRemoteIP( SocketAddress pSocket ) 
	{
		return parseSocketInfo( pSocket )[ 1 ];
	}

	/**
	 * Extracts and returns the connection port associated with the remote client
	 * on the given socket.
	 *
	 * @param pSocket The socket for which the remote connection port information 
	 *  will be retrieved.
	 * @return The remote connection port of the remote host as a string.
	 */
	public static String parseRemotePort( SocketAddress pSocket ) 
	{
		return parseSocketInfo( pSocket )[ 2 ];
	}

	/// Helper Methods ///

	/**
	 * Given a socket, this function parses out the relevant information associated
	 * with the socket and returns this information as an array of strings.
	 * 
	 * @param pSocket The socket that will have its relevant information (hostname,
	 *  IP address) parsed and returned.
	 * @return An array of the form [ HN, IP, PO ] where HN is the hostname of the
	 *  remote host, IP is the address, and PO is the communication port.
	 */
	private static String[] parseSocketInfo( SocketAddress pSocket ) 
	{
		return parseSocketInfo( pSocket.toString() );
	}

	/**
	 * Given a string representation of a socket, this function parses out the 
	 * relevant information associated with the socket and returns this 
	 * information as an array of strings.
	 *
	 * @param pSocketString The string representation fo the socket that will
	 *  have its relevant information returned.
	 * @return An array of the form [ HN, IP, PO ] where HN is the hostname of the
	 *  remote host, IP is the address, and PO is the communication port.

	 */
	private static String[] parseSocketInfo( String pSocketString )
	{
		String[] hostname_ipport = pSocketString.split("/");
		String[] ip_port = hostname_ipport[1].split(":");
		String[] hostname_ip_port = new String[3];

		hostname_ip_port[0] = hostname_ipport[0];
		hostname_ip_port[1] = ip_port[0];
		hostname_ip_port[2] = ip_port[1];

		return hostname_ip_port;
	}

}

