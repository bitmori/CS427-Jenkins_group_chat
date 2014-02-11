/**
 * @file ChatServer.java
 *
 * @date Fall 2013
 *
 * @todo
 * - Determine the package structure for the project, placing this file
 *   in the appropriate package based on the finalzied structure.
 * - Write a more detailed class description when more of the details
 *   are fleshed out.
 */

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 * A type that represents a server that facilitates chat communication
 * between connected clients.
 */
public class ChatServer
{
	/// Inner Classes ///
	
	/**
	 * TODO
	 */
	private class ChatGroupMediator implements Runnable
	{

	}

	/**
	 * A type that actively listens to a particular socket for chat client
	 * connection requests, servicing these requests as they're received.
	 */
	private class ChatClientListener implements Runnable
	{
		/// Constructors ///

		/**
		 * Instantiates a client listener as the listener for the given parent
		 * server on the given listening socket.
		 *
		 * @param pParentServer The chat server instance that spawned the listener.
		 * @param pSocket The socket to which the listener will be listening
		 * 	for client requests.
		 */
		public ChatClientListener( ChatServer pParentServer, ServerSocket pSocket )
		{
			mParentServer = pParentServer;
			mListenSocket = pSocket;

			mListenThread = new Thread( this );
			mListenThread.start();
		}

		/// Methods ///

		/**
		 * TODO
		 */
		public void run()
		{
			while( true )
			{
				Socket clientConnection;
				try { clientConnection = mListenSocket.accept(); } 
				catch( IOException e ) { continue; }

				
			}
		}

		/// Fields ///

		/** The chat server instance that spawned the listener.  A reference to
			this instance is saved so that the listener can inform the server
			of new connections. */
		private final ChatServer mParentServer;

		/** The socket to which the client listener listens for incoming client
			connection requests. */
		private final ServerSocket mListenSocket;

		/**
		 * The thread on which the client listening logic is running.
		 */
		private final Thread mListenThread;

	}

	/// Constructors ///
	
	/**
	 * Initializes a chat server on the local host that waits for connections
	 * on the given port.
	 *
	 * @param pPortNumber The number of the port that the chat server will
	 * 	listen to for chat client requests.
	 *
	 * @throws IOException Thrown if an error occurs while setting up the
	 * 	chat server to listen to the given port (typically caused by the
	 * 	port being used on the local host).
	 */
	public ChatServer( int pPortNumber ) throws IOException
	{
		mGroupMediators = new ArrayList<ChatGroupMediators>();

		ServerSocket listenSocket = new ServerSocket( pPortNumber, csMaxBacklog );
		mClientListener = new ChatClientListener( this, listenSocket );
	}

	/// Methods ///

	// TODO: Implement some methods here.

	/// Constants ///
	
	/** The maximum number of incoming client requests that can be processed
		at any given time. */
	private final static int csMaxBacklog = 10;

	/// Fields ///

	/** A client listener that listens to the port assigned to the server
		for client connection requests. */
	private final ChatClientListener mClientListener;

	/** A listing of all the group mediators currently active and facilitating
		the transfer of messages between connected chat users. */
	private final ArrayList<ChatGroupMediator> mGroupMediators;
}
