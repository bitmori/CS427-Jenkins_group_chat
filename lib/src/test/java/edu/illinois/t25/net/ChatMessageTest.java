/**
 * @file ChatMessageTest.java
 *
 * @author Piyush Bathwal, Joseph Ciurej
 * @date Fall 2013
 *
 * @TODO
 * - Expand upon the tests in this file when more of the chat message type
 *   is expanded upon.
 */
package edu.illinois.t25;

import edu.illinois.t25.net.ChatMessage;
import org.junit.Test;
import org.junit.After;
import org.junit.Before;
import static org.junit.Assert.*;

import org.json.simple.JSONObject;

/**
 * Container class for the test suite that tests the functionality of the 
 * "ChatMessage" type.
 */
public class ChatMessageTest
{
	/// Testing Variables ///
	
	/** 
	 * A test chat server object instance on which all the tests are
	 * performed. 
	 */
	private ChatMessage mTestMessage;

	/// Testing Functions ///
	
	@Test
	public void TestDefaultConstructor()
	{
		mTestMessage = new ChatMessage();

		assertEquals( "Default message author should be 'chat-server'.",
			"chat-server", mTestMessage.getAuthor() );
		assertEquals( "Default message recipient group should be all users.",
			"Global", mTestMessage.getGroup() );
		assertNotNull( "Default message contents should be some ping message.",
			mTestMessage.getContents() );
	}

	@Test
	public void TestExplicitConstructorWithValidFields()
	{
		String msgAuthor = "Author";
		String msgGroup = "Group";
		String msgContent = "Content";
		mTestMessage = new ChatMessage( msgAuthor, msgGroup, msgContent );

		assertEquals( "Message author should correspond exactly to given author.",
			msgAuthor, mTestMessage.getAuthor() );
		assertEquals( "Message group should correspond exactly to given group.",
			msgGroup, mTestMessage.getGroup() );
		assertEquals( "Message content should correspond exactly to given content.",
			msgContent, mTestMessage.getContents() );
	}

	@Test
	public void TestExplicitConstructorWithNullFields()
	{
		mTestMessage = new ChatMessage( null, null, null );

		assertEquals( "Author should be corrected to be 'null' for null inputs.",
			"null", mTestMessage.getAuthor() );
		assertEquals( "Group should be corrected to be 'null' for null inputs.",
			"null", mTestMessage.getGroup() );
		assertEquals( "Contents should be corrected to be 'null' for null inputs.",
			"null", mTestMessage.getContents() );
	}

	@Test
	public void TestJSONConstructorWithValidFields()
	{
		JSONObject testJSON = new JSONObject();
		testJSON.put( "author", "Author" );
		testJSON.put( "group", "Group" );
		testJSON.put( "content", "Content" );

		mTestMessage = new ChatMessage( testJSON.toString() );

		assertEquals( "Message author should correspond exactly to given author.",
			"Author", mTestMessage.getAuthor() );
		assertEquals( "Message group should correspond exactly to given group.",
			"Group", mTestMessage.getGroup() );
		assertEquals( "Message content should correspond exactly to given content.",
			"Content", mTestMessage.getContents() );
	}

	@Test
	public void TestJSONConstructorWithNullFields()
	{
		mTestMessage = new ChatMessage( (new JSONObject()).toString() );

		assertEquals( "Author should be corrected to be 'null' for null inputs.",
			"null", mTestMessage.getAuthor() );
		assertEquals( "Group should be corrected to be 'null' for null inputs.",
			"null", mTestMessage.getGroup() );
		assertEquals( "Contents should be corrected to be 'null' for null inputs.",
			"null", mTestMessage.getContents() );
	}

}
