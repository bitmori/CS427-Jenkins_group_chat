/**
 * @file ChatSever.java
 *
 * @author Piyush Bathwal, Joseph Ciurej
 * @date Fall 2013
 *
 * @todo
 * - Determine the package structure for the project, placing this file
 *   in the appropriate package based on the finalzied structure.
 * - Write a more detailed class description when more of the details
 *   are fleshed out.
 * - Write the logic for packet response and transfer.
 */

import java.io.IOException;
import java.net.Socket;
import java.net.DatagramSocket;
import java.net.DatagramPacket;
import java.util.ArrayList;

/**
 * A type that represents a server that facilitates chat communication
 * between connected clients.
 */
public class ChatServer implements Runnable
{
	/// Constructors ///
	
	/**
	 * Initializes a chat server on the local host that waits for messages
	 * sent by clients on the given host port.
	 *
	 * @param pPortNumber The number of the port that the chat server will
	 * 	listen to for chat client messages.
	 *
	 * @throws IOException Thrown if an error occurs while setting up the
	 * 	chat server to listen to the given port (typically caused by the
	 * 	port being used on the local host).
	 */
	public ChatServer( int pPortNumber ) throws IOException
	{
		mThread = new Thread( this, "chatserv@" + pPortNumber );
		mIsRunning = false;
		mListenSocket = new DatagramSocket( pPortNumber );

		mThread.start();
	}

	/// Methods ///
	
	/**
	 * Runs the primary servicing logic of the chat server, which involves
	 * sending messages to/receiving messages from clients asynchronously.
	 *
	 * @note This function should only be called internally to run the
	 * 	chat server on the internal thread.
	 */
	public void run()
	{
		mIsRunning = true;
		while( mIsRunning )
		{
			try 
			{
				byte[] messageBuffer = new byte[ 256 ];
				DatagramPacket clientPacket =  new DatagramPacket( messageBuffer, messageBuffer.length );

				mListenSocket.receive( clientPacket );
				
				// TODO: Replace this logic with actual message reading!
				System.out.println( "Received a message from " + clientPacket.getAddress() );
				//socket.send(packet);
			}
			catch( IOException netError )
			{
				System.out.println( "Failure to receive packets!" );
			}
		}

		mListenSocket.close();
	}

	/**
	 * Shuts down the server, closing its connection to its associated socket.
	 * 
	 * @note The server will finish processing its current packet before it
	 * 	completely exits.
	 */
	public void shutdown()
	{
		mIsRunning = false;
	}

	/// Fields ///

	/** 
	 * A reference to the thread on which the chat server listening/responding
	 * logic is running. 
	 */
	private final Thread mThread;

	/**
	 * A boolean flag that indicates whether or not the server is currently
	 * running.
	 */
	private boolean mIsRunning;
	
	/** 
	 * The socket to which the chat server listens for client messages. 
	 */
	private final DatagramSocket mListenSocket;

}
