/**
 * @file ChatClientPlugin.java
 *
 * @author Piyush Bathwal, Joseph Ciurej
 * @date Fall 2013
 *
 * @TODO
 * - Consider removing the print statements used to indicate server start-up
 *   and shut-down and replace them with Mojo messages so that they can be
 *   integrated with Maven output.
 * - Add overrides to this type to allow for configuration of the server socket
 *   on which the chat server is launched.
 */
package org.jenkinsci.plugins.chat_client;

import hudson.Plugin;
import hudson.PluginWrapper;
import org.kohsuke.stapler.StaplerRequest;
import org.kohsuke.stapler.StaplerResponse;

import javax.management.Descriptor;
import net.sf.json.JSONObject;
import edu.illinois.t25.net.ChatServer;

/**
 * The plugin extension for the chat client plugin, which contains the logic
 * for plugin set up and tear down functionality.  This type is responsible
 * for acquiring all resources needed by the plugin on Jenkins start up and
 * freeing these resources on shut down.
 */
public class ChatClientPlugin extends Plugin 
{
	/// Constructors ///
	
	/* Nothing */
	
	/// Methods ///
	
	/**
	 * Sets up the chat client plugin by starting up a chat server on port
	 * 1214.
	 *
	 * @throws Exception No exceptions will be thrown (included as requirement
	 *  of Jenkins overload).
	 */
	public void start() throws Exception 
	{
		System.out.println( "Initializing chat server on port 1214..." );
		mChatServer = new ChatServer( 1214 );
	}

	/**
	 * Cleans up resources associated with the chat client plugin and shuts
	 * down the chat server created on start up.
	 *
	 * @throws Exception No exceptions will be thrown (included as requirement
	 *  of Jenkins overload).
	 */
	public void stop() throws Exception 
	{
		System.out.println( "Shutting down chat server..." );
		mChatServer.shutdown();
		mChatServer = null;
	}

	/// Fields ///
	
	/**
	 * The chat server being run by this instance to facilitate communication
	 * between clients connected to the Jenkins server.
	 */
	public static ChatServer mChatServer;

}
