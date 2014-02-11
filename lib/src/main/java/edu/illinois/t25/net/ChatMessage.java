/**
 * @file ChatMessage.java
 *
 * @author Piyush Bathwal, Joseph Ciurej
 * @date Fall 2013
 *
 * @TODO
 * - Finalize the name of the global group and update it in the default
 *   constructor for the chat message.
 */
package edu.illinois.t25.net;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

/**
 * The representation of a chat message sent over the network to/from the
 * chat server.  The primary components of a chat message are the author, 
 * the recipient group, and the contents of the message.
 *
 * @see "https://github.com/mrniko/netty-socketio-demo/blob/master/server/src/main/java/com/corundumstudio/socketio/demo/ChatObject.java"
 */
public class ChatMessage
{
	/// Constructors ///
	
	/**
	 * Creates a default message, which is a simple server message that's
	 * addressed to all connected clients.
	 */
	public ChatMessage()
	{
		mAuthor = "chat-server";
		mRecipientGroup = "Global";
		mContents = "Ping from the chat server.";
	}

	/**
	 * Creates a message initialized with all the specified fields for
	 * author, recipient group, and contents.
	 *
	 * @param pAuthor The author of the constructed message.
	 * @param pGroup The recipient group of the constructed message.
	 * @param pContents The contents of the constructed message.
	 */
	public ChatMessage( String pAuthor, String pGroup, String pContents )
	{
		// Note: The empty adds guarantee that the contents of the message
		// are non-null.
		mAuthor = "" + pAuthor;
		mRecipientGroup = "" + pGroup;
		mContents = "" + pContents;
	}

	/**
	 * Constructs a message from the given string formatted as a JSON object.  
	 * The JSON object's author", "group", and "content" fields will map to 
	 * the author, recipient group, and message contents, repsectively.
	 *
	 * @param pJSONString A string representation of a JSON object that 
	 *  contains the attributes of the chat message.
	 */
	public ChatMessage( String pJSONString )
	{
		JSONObject JSON = (JSONObject)JSONValue.parse( pJSONString );

		// Note: The empty adds guarantee that the contents of the message
		// are non-null.
		mAuthor = "" + JSON.get( "author" );
		mRecipientGroup = "" + JSON.get( "group" );
		mContents = "" + JSON.get( "content" );
	}

	/// Methods ///

	/**
	 * Transforms a chat message into a string formatted as a JSON object that 
	 * can be sent over the network.
	 *
	 * @return A string representing a JSON object containing the message 
	 *  information of the form { "author": , "group": , "content": }.
	 */
	public String toJSONString()
	{
		JSONObject messageJSON = new JSONObject();
		messageJSON.put( "author", mAuthor );
		messageJSON.put( "group", mRecipientGroup );
		messageJSON.put( "content", mContents );

		return messageJSON.toString();
	}

	// Getter/Setter Methods //
	
	/**
	 * @return The author of the chat message.
	 */
	public String getAuthor()
	{
		return mAuthor;
	}

	/**
	 * @return The recipient group of the chat message.
	 */
	public String getGroup()
	{
		return mRecipientGroup;
	}

	/**
	 * @return The contents of the chat message.
	 */
	public String getContents()
	{
		return mContents;
	}
	
	/**
     * @param pAuthor The author that will be set as the author of the message.
	 */
	public void setAuthor( String pAuthor )
	{
        mAuthor = pAuthor;
	}

	/**
     * @param pGroup The group that will be set as the recipient group of 
     *  the message.
	 */
	public void setGroup( String pGroup )
	{
        mRecipientGroup = pGroup;
	}

	/**
     * @param pContents The contents that will be set as the contents of the message.
	 */
	public void setContents( String pContents )
	{
        mContents = pContents;
	}

	/// Fields ///
	
	/**
	 * An identifier for the person that initially sent the message.
	 */
	private String mAuthor;

	/**
	 * An identifier for the group to which the message should be sent.
	 */
	private String mRecipientGroup;

	/**
	 * The raw content of the messages sent over the network.
	 */
	private String mContents;

}
