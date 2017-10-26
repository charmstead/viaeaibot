package com.viaeaibot.viaeaibot.facebookSettings;

import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.restfb.Parameter;
import com.restfb.Version;
import com.restfb.types.send.IdMessageRecipient;
import com.restfb.types.send.SendResponse;

import com.viaeaibot.viaeaibot.message.Message;

/*
 * This class manage the connection from our Message system to the FB Messenger API calls
 * Using http://restfb.com/ and code from https://github.com/restfb/restfb
 */
public class APIFacebookMessenger
{
	// consts //
	public static final String ERROR_TOKEN_RESPONCE = "Error token";
	// end consts //
	
	// members //
	private FrontFacebookMessengerSettings settings;
	// end members //
	
	// full constractor
	public APIFacebookMessenger(FrontFacebookMessengerSettings settings)
	{
		this.settings = settings;
	}
	
	//////////////////
	// get function //
	//////////////////
	
	public FrontFacebookMessengerSettings getSettings()
	{
		return this.settings;
	}

	// --- end get function --- //
	
	//////////////////
	// set function //
	//////////////////
	
	public void setSettings(FrontFacebookMessengerSettings settings)
	{
		this.settings = settings;
	}
	
	// --- end set function --- //?
	
	/////////////////////////
	// operation functions //
	/////////////////////////
	
	// input: some technical facebook API object and message to send
	// output: none, sending the message
	// action: open facebook client interface and send the message
	public void sendMessage(IdMessageRecipient recipient, Message message)
	{
            
		// create facebook client intance
		FacebookClient pageClient = new DefaultFacebookClient(this.settings.getPageAccessToken(), Version.VERSION_2_6);
		
                
		// build the response
		SendResponse response = pageClient.publish(null, SendResponse.class, Parameter.with("recipient", recipient), Parameter.with("message", message));
                
	}
	
	// --- end operation functions --- //
	
	// toString //
	@Override
	public String toString()
	{
		return "[settings = " + this.settings.toString() + "]";
	}
	// end toString //
}
