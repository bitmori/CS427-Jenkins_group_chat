/**
 * @file ChatClientRunListener.java
 *
 * @author Vikram Jayashankar, Piyush Bathwal, Joseph Ciurej
 * @date Winter 2013
 *
 * @TODO
 * - Add functionality to this type to actually notify clients connected
 *   to the chat server when a build is submitted/run.
 * - Add functionality to notify clients at a finer granularity when builds
 *   are submitted and completed (i.e. only inform team members associated
 *   to build).
 */
package org.jenkinsci.plugins.chat_client;

import hudson.Extension;
import hudson.model.TaskListener;
import hudson.model.AbstractBuild;
import hudson.model.Cause;
import hudson.model.listeners.RunListener;


import javax.management.Descriptor;

/**
 * A build running listener that notifies all connect clients to the chat
 * server when a build is submitted with the information about that build.
 */
@Extension
public class ChatClientRunListener extends RunListener<AbstractBuild<?, ?>> 
{
	/// Methods ///

	/**
	 * Informs all clients connected to the chat server when a Jenkins build
	 * is completed.
	 */
	@Override
	public synchronized void 
		onCompleted( AbstractBuild<?, ?> finishedBuild, TaskListener listener )
	{
		String buildString = 
			"Build Completed: " +
			finishedBuild.getParent().getDisplayName() + " " + 
			finishedBuild.getDisplayName() + " is: " + 
			finishedBuild.getBuildStatusSummary().message;

		System.out.println( "==========================" );
		System.out.println( buildString );
		System.out.println( "==========================" );
	}
}
