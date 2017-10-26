package com.viaeaibot.viaeaibot.facebookSettings;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;


@Service
public class FrontFacebookMessengerSettings 
{
    /**
     * Since your system uses Spring, you can declare this class as a Component using @Component
     * Rather than having to instantiate this class manually and passing the long
     * constructor in.
     * set the instance variables using spring @Value e.g @Value("${facebook.callbackUrl}").
     * Then you can set this in your environment variable after declaring it in the 
     * application.yml
     * 
     * To use the class, you'll do @Inject or @Autowired in the class that needs it.
     * 
     */
    
    // members //
    @Value("${CALLBACK_URL}")
    public String callbackURL;
    
    
    @Value("${VERIFYTOKEN}")
    public String  verifyToken;
    
     @Value("${ACCESSTOKEN}")
    public String pageAccessToken;
     // end members //

    // full constractor
//    public FrontFacebookMessengerSettings(@Value("{CALLBACK_URL}") String callbackURL,@Value("{ACCESSTOKEN}") String verifyToken,@Value("{VERIFYTOKEN}") String pageAccessToken) 
//    {
//        this.callbackURL = callbackURL;
//        this.verifyToken = verifyToken;
//        this.pageAccessToken = pageAccessToken;
//    }

    ///////////////////
    // get functions //
    ///////////////////
    
    public String getCallbackURL()
    {
        return callbackURL;
    }

    public String getVerifyToken() 
    {
        return verifyToken;
    }

    public String getPageAccessToken() 
    {
        return pageAccessToken;
    }
    
    // --- end get functions --- //

    ///////////////////
    // set functions //
    ///////////////////
    
    public void setCallbackURL(String callbackURL)
    {
        this.callbackURL = callbackURL;
    }

    public void setVerifyToken(String verifyToken) 
    {
        this.verifyToken = verifyToken;
    }

    public void setPageAccessToken(String pageAccessToken) 
    {
        this.pageAccessToken = pageAccessToken;
    }
    
    // --- end set functions --- //

    // --- toString --- //
    @Override
    public String toString() 
    {
        return "[callbackURL = " + callbackURL + ", verifyToken = " + verifyToken + ", pageAccessToken = " + pageAccessToken + "]";
    }
    // --- end toString --- //
}
