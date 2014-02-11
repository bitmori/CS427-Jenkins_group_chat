/**
 * @file ClientListing.java
 *
 * @author Timothy Madigan, Ke Yang, Joseph Ciurej
 * @date 04/12/2013
 *
 * @TODO
 * - Add functionality to remove clients listing in a given instance listing
 *   if needed.
 */
package edu.illinois.t25.data;

import java.util.Arrays;
import java.util.Hashtable;
import java.util.Map;
import java.util.List;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.SocketAddress;
import java.net.InetSocketAddress;
import edu.illinois.t25.net.SocketAddressParser;

/**
 * A container type that manages all the clients that have connected to the chat
 * server and their registered names. Essentially, this type serves as an
 * abstracted table from client network addresses to their registered names.
 *
 * @depend - - - JenkinsUser
 */
public class ClientListing 
{
	/// Contructors ///

	/**
	 * Constructs a client listing from the client information file at the
	 * default system path.
	 * 
	 * @note The default system path for the client information file is the path
	 *  "~/.jenkins/plugins/chat-client/client-listing.csv".
	 */
	public ClientListing() 
	{
		this( "~/.jenkins/plugins/chat-client/client-listing.csv" );
	}

	/**
	 * Constructs a client listing from the client information file at the given
	 * system location.
	 * 
	 * @param pListingPath A string representing the file path to the client listing
	 *  file.
	 */
	public ClientListing( String pListingPath ) 
	{
		mListingPath = pListingPath;
		mClientMap = new Hashtable<SocketAddress, JenkinsUser>();
		mAddressMap = new Hashtable<String, SocketAddress>();

		File listingFile = new File( pListingPath );
		if( listingFile.exists() )
			importMappingFrom( listingFile );
	}

	/// Methods ///

	/**
	 * Adds the given client to the listing of clients if it doesn't already
	 * exist. If the client already exists, the new information is simply
	 * ignored.
	 * 
	 * @param pClientAddr The address for the client that will be added to the
	 *  listing.
	 * @param pClient A reference to the client object that will be added to the
	 *  instance listing.
	 */
	public void add( SocketAddress pClientAddr, JenkinsUser pClient ) 
	{
		if( !mClientMap.containsKey(pClientAddr) )
		{
			mClientMap.put( pClientAddr, pClient );
			mAddressMap.put( pClient.getName(), pClientAddr );
		}
	}

	/**
	 * Returns the client associated with the given address in the listing, or
	 * null if no such client exists.
	 * 
	 * @param pClientAddr The address of the remote client for which the information
	 *  will be returned.
	 * @return A reference to the JenkinsUser that represents the remote client
	 *  at the given address, or null if no such client exists.
	 */
	public JenkinsUser getClientAt( SocketAddress pClientAddr ) 
	{
		return mClientMap.get( pClientAddr );
	}

	/**
	 * Returns the remote network address of the client specified by the given
	 * user name, or null if no such client exists.
	 *
	 * @param pClientName The user name of the client for which the remote address
	 *  will be returned.
	 * @return The remote address of the client with the given user name, or
	 *  null if no such client exists.
	 */
	public SocketAddress getClientNamed( String pClientName )
	{
		return mAddressMap.get( pClientName );
	}

	/**
	 * Returns true if the given address is listed in the client listing and
	 * false otherwise.
	 * 
	 * @param pClientAddr The address that will be checked for containment in the
	 *  listing.
	 * @return True if the given address is listed, false otherwise.
	 */
	public boolean listed( SocketAddress pClientAddr ) 
	{
		return mClientMap.containsKey( pClientAddr );
	}

	/**
	 * Returns true if the given user name is listed in the client listing and
	 * false otherwise.
	 * 
	 * @param pClientName The user name that will be checked for containment 
	 *  in the listing.
	 * @return True if the given address is listed, false otherwise.
	 */
	public boolean listed( String pClientName ) 
	{
		return mAddressMap.containsKey( pClientName );
	}

	/**
	 * Saves the client mapping information contained in the current instance
	 * back to the file from which the listing was loaded.
	 */
	public void save() 
	{
		save( mListingPath );
	}

	/**
	 * Saves the contents of the instance client mapping to the file at the
	 * specified path.
	 * 
	 * @param pListingPath The path of the file to which the client information 
	 *  will be saved.
	 */
	public void save( String pListingPath ) 
	{
		File listingFile = new File( pListingPath );
		try 
		{
			listingFile.createNewFile();
			exportMappingTo( listingFile );
		} 
		catch( Exception e ) { /* TODO: Indicate failed write. */ }
	}

	/// Helper Methods ///

	/**
	 * Given a listing file that exists, this function imports the listing
	 * information encoded into this file.
	 * 
	 * @param pListingFile A reference to the file from which the listing information
	 *  will be read.
	 */
	private void importMappingFrom( File pListingFile ) 
	{
		try 
		{
			BufferedReader fileReader = new BufferedReader(new FileReader(
					pListingFile));

			String fileLine = null;
			while( (fileLine = fileReader.readLine()) != null ) 
			{
				List<String> lineElems = Arrays.asList( fileLine.split(",") );
				InetSocketAddress lineUserAddr = 
					SocketAddressParser.parseSocketAddress( lineElems.get(2) );
				JenkinsUser lineUser = new JenkinsUser(
					lineElems.get(0),
					SocketAddressParser.parseRemoteAddress(lineUserAddr),
					lineElems.get(1)
				);

				this.add( lineUserAddr, lineUser );
			}

			fileReader.close();
		} 
		catch( Exception e ) { /* Nothing */ }
	}

	/**
	 * Given a listing file that exists, this function exports the listing
	 * information in the instance object to this file.
	 * 
	 * @param pListingFile A reference to the file to which the listing information 
	 *  in the instance will be written.
	 */
	private void exportMappingTo( File pListingFile ) 
	{
		try 
		{
			PrintWriter fileWriter = new PrintWriter(new BufferedWriter(
					new FileWriter(pListingFile)));

			for( Map.Entry<SocketAddress, JenkinsUser> clientEntry : mClientMap.entrySet() )
			{
				SocketAddress clientAddr = clientEntry.getKey();
				JenkinsUser client = clientEntry.getValue();
				String clientString = client.getName() + "," + client.getGroup() + 
					"," + clientAddr.toString();

				fileWriter.println( clientString );
			}

			fileWriter.close();
		} 
		catch(Exception e) { /* TODO: Indicate failed write. */ }
	}

	/// Fields ///

	/**
	 * A hash table that associates a given network address to a server client,
	 * including the client's name and group information.
	 */
	private Hashtable<SocketAddress, JenkinsUser> mClientMap;

	/**
	 * A hash table that associates a given client user name to the address of
	 * a remove server client.  This serves to associate network addresses to
	 * names in the opposite direction as the `mClientMap` field.
	 */
	private Hashtable<String, SocketAddress> mAddressMap;

	/**
	 * The path of the file from where the client mapping information was
	 * initially read. This value is saved so that changes can be saved back to
	 * that file.
	 */
	private final String mListingPath;

}
