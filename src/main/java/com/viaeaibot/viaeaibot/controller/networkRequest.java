/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viaeaibot.viaeaibot.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.restfb.BinaryAttachment;
import com.restfb.DefaultFacebookClient;
import com.restfb.DefaultJsonMapper;
import com.restfb.FacebookClient;
import com.restfb.JsonMapper;
import com.restfb.Parameter;
import com.restfb.Version;
import com.restfb.types.GraphResponse;
import com.restfb.types.send.IdMessageRecipient;
import com.restfb.types.send.Message;
import com.restfb.types.webhook.WebhookEntry;
import com.restfb.types.webhook.WebhookObject;
import com.restfb.types.webhook.messaging.MessagingItem;
import com.viaeaibot.viaeaibot.chatbotconfig.FacebookMessageMapper;
import com.viaeaibot.viaeaibot.facebookSettings.FrontFacebookMessengerSettings;
import java.io.IOException;
import static java.util.Objects.isNull;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.eclipse.jetty.server.Response;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
/**
 *
 * @author tomide
 */
@RestController
@RequestMapping("/webhook")
public class networkRequest {
    
    @Autowired
    private FrontFacebookMessengerSettings fbConfig;
    
    @Autowired
    FacebookMessageMapper msgMapper;
    
    ObjectMapper objMap=new ObjectMapper();
    
    
    @GetMapping
    public void processGet(HttpServletResponse resp, @RequestParam("hub.mode") String mode,
                                                     @RequestParam("hub.verify_token") String verifyToken,
                                                     @RequestParam("hub.challenge") String challenge) throws IOException{
        
        System.out.println("the challenge is "+fbConfig.getVerifyToken() );
        if ("subscribe".equals(mode.trim()) //
            && fbConfig.getVerifyToken().equals(verifyToken.trim())
            && !isNull(challenge))
        {
            System.out.println("what is happening");
            resp.getWriter().append(challenge);
            
        } else {
            System.out.println("what is happening");
          resp.setStatus(Response.SC_BAD_REQUEST);
          
        }
        
    }
    
    @PostMapping
    public void processPost(HttpServletRequest req, HttpServletResponse resp) throws IOException{
        
        final String body = req.getReader().lines().reduce("", (accumulator, actual) -> accumulator + actual);

        JsonMapper mapper = new DefaultJsonMapper();
        
        WebhookObject whObject = mapper.toJavaObject(body, WebhookObject.class);

         for (final WebhookEntry entry : whObject.getEntryList()) {
                for (final MessagingItem item : entry.getMessaging()) {
                    
                    final FacebookClient sendClient =
                           new DefaultFacebookClient(fbConfig.getPageAccessToken(), Version.VERSION_2_7);
                  
                    com.viaeaibot.viaeaibot.message.Message viaeaiMsg = null;
                    Message simpleMsg=null;
                    if(!isNull(item.getMessage())){
                        
                        viaeaiMsg = new com.viaeaibot.viaeaibot.message.Message()
                                                    .setMessageId(Long.parseLong(entry.getId()))
                                                    .setMessage_time(entry.getTime()+"")
                                                    .setCreatorId(Long.parseLong(item.getSender().getId()));
                        
                        if(!isNull(item.getMessage().getText())){
                            viaeaiMsg.setMessageBody("conversion: "+item.getMessage().getText());
                        }
                        if(!isNull(item.getMessage().getAttachments()) && item.getMessage().getAttachments().size()>0){
                            viaeaiMsg.setMessageBody("conversion: "+item.getMessage().getAttachments().get(0).getUrl());
                        }
                        simpleMsg = msgMapper.mapToFacebookMessage(viaeaiMsg);
                    }
                    
                    System.out.println("THIS CUSTOM MESSAGE TYPE.\n\n"
                                +objMap.writerWithDefaultPrettyPrinter().writeValueAsString(simpleMsg)
                                +"\nIS MAPPED TO>>>>>>>>>>>"
                            );
                    
                    
                    
                    Object map = new ObjectMapper().readValue(mapper.toJson(simpleMsg), Object.class);
                    System.out.println(
                                new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(map));
                    System.out.println("\n\n\ncustom message succefully mapped to facebook type");
              
                    IdMessageRecipient recipient = new IdMessageRecipient(item.getSender().getId());
                    
                    System.out.println("\n\nSending the message to facebook.");
                    
                
                
                    
                    if(!isNull(item.getMessage())&&!isNull(item.getMessage().getText()) &&!isNull(simpleMsg) && !item.getMessage().isEcho()){
                        sendClient.publish("me/messages", GraphResponse.class, Parameter.with("recipient",recipient ),
                        Parameter.with("message", simpleMsg));
                        
                        
                        
                         //You can do whatever you want with the object
                    //perhaps it correspond to a specific action in your system
                        System.out.println("mapping facebook message type to custom message type");
                        com.viaeaibot.viaeaibot.message.Message viaeaiMessage = msgMapper.maptoViaeaiMessage(entry, item);
                        
                        System.out.println("THE FACEBOOK MESSAGE>>>>>>>\n"+
                                mapper.toJson(item.getMessage())+"\n IS MAPPED TO");
                        System.out.println("The resolved mapping is \n"+
                                    new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(viaeaiMessage)
                                                +"\n\n\n\n"                    
                                );
                    }
                 //http://restfb.com/documentation/#publishing-photo
                 //https://github.com/restfb/restfb
                 
//                 sendClient.publish("me/messages", GraphResponse.class, 
//                         msgMapper.mapToFacebookMessageWithAttachmentAlt(new viaeai.Message(),"filename"),
//                         Parameter.with("recipient", entry.getId()),
//                         Parameter.with("message", "description")
//                         
//                 );   
                 
                 
//                 
                    
                   
                    
                
                }//end of messagingItem Loop

        }   
        
               
//              sendClient.publish("me/messages", GraphResponse.class, Parameter.with("recipient", recipient),
//                Parameter.with("message", simpleMsg));
        
        
    }
    
    
    
    
}
