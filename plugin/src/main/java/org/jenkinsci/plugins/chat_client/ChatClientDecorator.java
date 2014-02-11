/**
 * @file ChatClientDecorator.java
 *
 * @author Vikram Jayashankar, Joseph Ciurej
 * @date Fall 2013
 *
 * @TODO
 * - Figure out what all the unknown items within this class do/what function
 *   they perform.
 * - Remove the functionality associated with the alert message implemented
 *   during the spike and replace it with retrieving information about the
 *   chat server.
 */
package org.jenkinsci.plugins.chat_client;

import hudson.Launcher;
import hudson.Extension;
import hudson.Util;
import hudson.model.Build;
import hudson.model.BuildListener;
import hudson.model.AbstractBuild;
import hudson.model.PageDecorator;

import org.kohsuke.stapler.StaplerRequest;
import org.kohsuke.stapler.DataBoundConstructor;
import javax.management.Descriptor;
import net.sf.json.JSONObject;

/**
 * The page decorator extension for the client client plugin, which augments
 * the Jenkins view pages with extra Javascript that allows for communication
 * between Jenkins users.
 */
@Extension
public class ChatClientDecorator extends PageDecorator 
{
	/// Constructors ///
	
    /**
     * This annotation tells Hudson to call this constructor, with
     * values from the configuration form page with matching parameter names.
	 *
	 * @param pAlertMessage The alert message for the decorator.
     */
    @DataBoundConstructor
    public ChatClientDecorator( String pAlertMessage ) 
	{
		this();
		mAlertMessage = pAlertMessage;
    }

	/**
	 * TODO: Figure out what's happening here.
	 */
	public ChatClientDecorator() 
	{
		load();
	}

	/// Methods ///

	/**
	 * Configures the page decorator by allowing the user to change the alert
	 * message.
	 *
	 * @param pReq TODO: Figure out what this is.
	 * @param pJSON JSON object representing the form submitted to change the
	 * 	page descriptor state.
	 *
	 * @return A boolean describing the status of the configuration (which is
	 * 	always true in this case).
	 *
	 * @throws FormException Thrown if the form given information read in by
	 *  the configuration methods doesn't match the presented form by Jenkins.
	 */
	@Override
	public boolean configure( StaplerRequest pReq, JSONObject pJSON ) throws FormException 
	{
		mAlertMessage = pJSON.getString( "alert_message" );
		save();

		return true;
	}
	
	// Getter/Setter Methods //
	
	/**
	 * @return The string alert message that will be displayed by the decorator.
	 */
	public String getAlertMessage() 
	{
		return mAlertMessage;
	}

	/**
	 * @return The display name for the decorator, which is 'Chat Client
	 *  Plugin' for this decorator.
	 */
	@Override
	public String getDisplayName() 
	{
		return "Chat Client Plugin";
	}

	/// Fields ///
	
	/** 
	 * The alert message that will appear when the button is pressed. 
	 */
	private String mAlertMessage = "Default Message";

}
