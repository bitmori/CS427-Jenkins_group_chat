/**
 * @file ChatServer.java
 *
 * @author Ke Yang, Piyush Bathwal, Joseph Ciurej
 * @date Fall 2013
 *
 * @TODO
 * - Integrate with the group association backend to facilitate message 
 *   sending to groups of clients.
 * - Refine the process for adding users to the server (use callbacks
 *   and request names instead of generating random names).
 * - Add client listing save on shutdown to the server.
 * - Refactor the messaging system to allow for general messages to be
 *   sent to general groupings of clients (requires grouping backend).
 * - Consider changing the connection inform policy to permit sending
 *   the entire backlog of users in the user listing on new connection.
 */
package edu.illinois.t25.net;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Collection;
import java.net.InetAddress;
import java.net.SocketAddress;
import java.net.InetSocketAddress;
import edu.illinois.t25.data.ClientListing;
import edu.illinois.t25.data.JenkinsUser;

import com.corundumstudio.socketio.listener.*;
import com.corundumstudio.socketio.*;

/**
 * A type that represents a server instance that facilitates chat communication
 * between connected clients.  The server uses web sockets in order to maintain
 * connections to clients and send information asynchronously.
 *
 * @see "https://github.com/mrniko/netty-socketio-demo/blob/master/server/src"
 *
 * @composed 1 - 1 SocketIOServer
 * @navassoc - - - ClientListing
 *
 * @depend - - - ChatMessage
 * @depend - - - ConnectMessage
 * @depend - - - SocketAddressParser
 */
public class ChatServer
{
	/// Constructors ///
	
	/**
	 * Initializes a chat server on the local host that waits for connections
	 * on the given port.
	 *
	 * @param pPortNumber The number of the port that the chat server will
	 * 	listen to for chat client requests.
	 */
	public ChatServer( int pPortNumber )
	{
		mClientListing = new ClientListing();

		Configuration serverConfig = new Configuration();
		serverConfig.setHostname( "localhost" );
		serverConfig.setPort( pPortNumber );

		mServer = new SocketIOServer( serverConfig );
		initializeListeners();
		mServer.start();
		mIsRunning = true;
	}

	/// Methods ///

	/**
	 * Sends the given chat message across the network to all specified destination
	 * clients in the message.
	 *
	 * @param pMessage The message that will be sent over the network to all
	 * 	proper connected server clients.
	 */
	public synchronized void sendMessage( ChatMessage pMessage )
	{
		Collection<SocketIOClient> recipients = getRecipientsOf( pMessage );
		BroadcastOperations recipientGroup = new BroadcastOperations( recipients );

		recipientGroup.sendJsonObject( pMessage );
	}

	/**
	 * Sends the given connection message across the network to all clients
	 * currently connected to the server.
	 *
	 * @param pMessage The connectiong message that will be sent over the network 
	 *  to all connected clients.
	 */
	public synchronized void sendMessage( ConnectMessage pMessage )
	{
		Collection<SocketIOClient> recipients = getConnectedClients();
		BroadcastOperations recipientGroup = new BroadcastOperations( recipients );

		recipientGroup.sendEvent( "connection-change", pMessage );
	}

	/**
	 * Shuts down the server instance, performing all clean up necessary to
	 * close network connections and free network sockets.
	 */
	public void shutdown()
	{
		mIsRunning = false;

		// mClientListing.save();
		mServer.stop();
	}

	// Getter/Setter Methods //

	/**
	 * @return A listing of all clients currently connected to the chat server
	 * 	(where each client is represented by their socket IO client connection).
	 */
	public synchronized Collection<SocketIOClient> getConnectedClients()
	{
		return mServer.getAllClients();
	}

	/**
	 * Returns a listing of all the recipients of the given message that are
	 * currently connected to the server as a list of client socket connections.
	 *
	 * @param pMessage The message for which the connected recipients shall
	 * 	be retrieved.
	 * @return A list of all sockets for recipients of the given message that
	 * 	are currently connected to the server.
	 *
	 * @TODO Integrate this with the group mapping utility to retrieve
	 * 	the proper recipient list (currently sending to singular clients).
	 * @TODO Find a faster way to search for the proper recipient.  Currently
	 *  the process tables O(n) time in number of connected clients.
	 */
	public synchronized Collection<SocketIOClient> getRecipientsOf( ChatMessage pMessage )
	{
		SocketAddress recipientSocket = mClientListing.getClientNamed( pMessage.getGroup() );

		ArrayList<SocketIOClient> recipientList = new ArrayList<SocketIOClient>();
		for( SocketIOClient connectedClient : getConnectedClients() )
			if( recipientSocket.equals( connectedClient.getRemoteAddress() ) )
				recipientList.add( connectedClient );
		
		return recipientList;
	}

	/**
	 * @return True if the server is running and false otherwise.
	 */
	public boolean isRunning()
	{
		return mIsRunning;
	}

	/// Helper Methods ///

	/**
	 * Initializes the listeners for the chat server instance.  The implementation
	 * for all the event listeners for the server is contained in this function.
	 */
	private void initializeListeners()
	{
		final ChatServer parentServer = this;

		// Connection Listener //
		mServer.addConnectListener( new ConnectListener() {
			@Override
			public void onConnect( SocketIOClient pClient ) 
			{
				SocketAddress clientAddr = pClient.getRemoteAddress();
				InetAddress clientInet = SocketAddressParser.parseRemoteAddress( clientAddr );

				// TODO: Refine the process for adding new users by sending out
				// an event and waiting on a callback.
				String clientName = mClientListing.listed( clientAddr ) ?
					getClientUsername( pClient ) : 
					"anonymous" + (int) Math.floor( 100 * Math.random() );
				mClientListing.add( clientAddr, new JenkinsUser(clientName, clientInet, "G") );
				
				// Inform all currently connected clients of the new connection.
				ConnectMessage message = new ConnectMessage( clientName, true );
				parentServer.sendMessage( message );

				// Indicate to the newly connected client all the users currently
				// connected to the server.
				for( SocketIOClient connectedClient : getConnectedClients() )
				{
					SocketAddress connectedAddr = connectedClient.getRemoteAddress();
					String connectedName = mClientListing.getClientAt(connectedAddr).getName();
					ConnectMessage connectMsg = new ConnectMessage( connectedName, true );

					pClient.sendEvent( "connection-change", connectMsg );
				}
			}
		} );

		// Disconnection Listener //
		mServer.addDisconnectListener( new DisconnectListener() {
			@Override
			public void onDisconnect( SocketIOClient pClient ) 
			{
				String clientName = getClientUsername( pClient );

				ConnectMessage message = new ConnectMessage( clientName, false );
				parentServer.sendMessage( message );
			}
		} );

		// Message Listener //
		mServer.addJsonObjectListener( ChatMessage.class, new DataListener<ChatMessage>() {
			@Override
			public void onData( SocketIOClient pClient, ChatMessage pMessage, 
				AckRequest pSender )
			{
				String clientName = getClientUsername( pClient );

				parentServer.sendMessage( new ChatMessage(clientName, 
					pMessage.getGroup(), pMessage.getContents()) );
			}
		} );
	}

	/**
	 * Given a server client, this function returns the user name of this client
	 * or null if this client doesn't exist within the client listing.
	 *
	 * @param pClient A reference to the client for which the user name will
	 *  be returned.
	 * @return The user name of the given client, or null if no such client
	 *  exists.
	 */
	private String getClientUsername( SocketIOClient pClient )
	{
		SocketAddress clientAddr = pClient.getRemoteAddress();
		String clientName = mClientListing.getClientAt(clientAddr).getName();

		return clientName;
	}

	/// Fields ///

	/**
	 * A reference to the SocketIO library server that's used to facilitate
	 * websocket communication between connected clients.
	 */
	private final SocketIOServer mServer;

	/**
	 * A reference to the client listing, which contains references to all
	 * known clients and their associated information.
	 */
	private final ClientListing mClientListing;

	/**
	 * A boolean flag that indicates whether or not the server instance is
	 * currently servicing clients.
	 */
	private boolean mIsRunning;

}
