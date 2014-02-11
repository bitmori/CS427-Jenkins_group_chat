/**
 * @file ConnectMessage.java
 *
 * @author Ke Yang, Joseph Ciurej
 * @date 04/12/2013
 *
 * @TODO
 * - Integrate constants used by the `ChatMessage` and `ConnectMessage` types
 *   into a single class.
 * - Write tests for this type if any more complex behavior is implemented
 *   (getters and setters are trivial to test and not worthwhile).
 */
package edu.illinois.t25.net;

/**
 * The representation of a connection message sent over the network from
 * the chat server.  These messages are sent to from the server to clients
 * to indicate when other clients connect and disconnect to the server.
 *
 * @see "https://github.com/mrniko/netty-socketio-demo/blob/master/server/src/main/java/com/corundumstudio/socketio/demo/ChatObject.java"
 */
public class ConnectMessage
{
	/// Contructors ///

	/**
	 * Creates a default connection message, which indicates that the chat
	 * server has disconnected.
	 */
	public ConnectMessage()
	{
		mConnector = "chat-server";
		mIsConnecting = false;
	}

	/**
	 * Creates a message with the given connector name and the given 
	 * connection information.
	 *
	 * @param pConnector The name of the client connecting to the server.
	 * @param pIsConnecting A boolean that indicates whether or not the
	 *  given client is connecting to the server.
	 */
	public ConnectMessage( String pConnector, boolean pIsConnecting )
	{
		mConnector = pConnector;
		mIsConnecting = pIsConnecting;
	}

	/// Methods ///

	// Getter/Setter Methods //
	
	/**
	 * @return The name of the person connecting to the server.
	 */
	public String getConnector()
	{
		return mConnector;
	}

	/**
	 * @return True if the message indicates that the connector associated
	 *  with the message is connecting to the server and false if the connector
	 *  is disconnecting.
	 */
	public boolean getConnectorIsConnecting()
	{
		return mIsConnecting;
	}

	/**
     * @param pConnector The name of the connector that will be initialized to
	 *  the connection message instance.
	 */
	public void setConnector( String pConnector )
	{
		mConnector = pConnector;
	}

	/**
	 * @param pIsConnecting True if the connection message instance is to indicate
	 *  that the connector has connected to the chat server and false if the connector
	 *  is disconnecting.
	 */
	public void setConnectorIsConnecting( boolean pIsConnecting )
	{
		mIsConnecting = pIsConnecting;
	}

	/// Fields ///

	/**
	 * The name identifier for the client of the server being described by the
	 * connection message instance.
	 */
	private String mConnector;

	/**
	 * A boolean that indicates whether or not the connector associated with the
	 * message is connecting or disconnecting from the server.
	 */
	private boolean mIsConnecting;

}
