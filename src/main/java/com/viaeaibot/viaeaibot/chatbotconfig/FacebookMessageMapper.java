/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viaeaibot.viaeaibot.chatbotconfig;



import com.restfb.BinaryAttachment;

import com.restfb.types.send.MediaAttachment;
import com.restfb.types.send.Message;
import com.restfb.types.webhook.WebhookEntry;
import com.restfb.types.webhook.messaging.MessageItem;
import com.restfb.types.webhook.messaging.MessagingItem;

import static java.util.Objects.isNull;
import com.viaeaibot.viaeaibot.messageTypes.MessageType;
import org.springframework.stereotype.Service;




/**
 *
 * @author tomide
 */
@Service
public class FacebookMessageMapper {
    
    /**
     * This maps viaeai.Message to FB message when attachment is
     * not needed
     * @param message the viaeai messageType to map to FB message
     * @returns facebook message type
     */
    public Message mapToFacebookMessage(com.viaeaibot.viaeaibot.message.Message message){
        
        Message simpleTextMessage = new Message(message.getBody());
        
        return simpleTextMessage;
    }
    
    /**
     * This maps viaeai.Message with attachment to facebook MessageAttachement
     * type. use this with only viaeai.Message with attachment
     * @param message
     * @return 
     */
    public static Message mapToFacebookMessageWithAttachment(com.viaeaibot.viaeaibot.message.Message message){
        
            if(!message.isFile()){
                return null;
            }
            
            MediaAttachment attachment;
            
            switch(message.getType()){
                
                case audio:
                    attachment = new MediaAttachment(MediaAttachment.Type.AUDIO, message.getFileURL());
                    break;
                    
                case image:
                    attachment = new MediaAttachment(MediaAttachment.Type.IMAGE, message.getFileURL());
                    break;
                    
                case video:
                    attachment=new MediaAttachment(MediaAttachment.Type.VIDEO, message.getFileURL());
                    break;
                    
                default:
                    attachment=new MediaAttachment(MediaAttachment.Type.FILE, message.getFileURL());
                    break;
                
            }
        
            return new Message(attachment);
            
           
    }
    
    /**
     * This maps viaeai.Message with attachment to facebook MessageAttachement
     * type. This alternative way of sending attachment attaches a files as binary
     * resource.
     * @param message the message with attachment
     * @param fileName the name of the file
     * @return 
     */
    public BinaryAttachment mapToFacebookMessageWithAttachmentAlt(com.viaeaibot.viaeaibot.message.Message message,String fileName){
        
        if(!message.isFile()){
            return null;
        }
        
        
        return BinaryAttachment.with(fileName, getClass().getResourceAsStream(message.getFileURL()));
    }
    
    /**
     * The facebook webhookObject can contain entryList of several messages.
     * You'll have to 
     * @param whObject webhookObject pageRequest sent from FB to your bot.
     * @return Set of all messages in the webhookObject converted to viaeai.Message
     */
    public com.viaeaibot.viaeaibot.message.Message maptoViaeaiMessage(WebhookEntry entry,MessagingItem item){
        
       
                 com.viaeaibot.viaeaibot.message.Message message= new com.viaeaibot.viaeaibot.message.Message();
            
                    //gets the id of this message entry
                    long id = Long.parseLong(entry.getId());
                    
                    //gets time of the message
                    String time = entry.getTime().getTime()+"";
                    
                    //gets the sender id
                    long senderId = Long.parseLong(item.getSender().getId());
                    long recipientId = Long.parseLong(item.getRecipient().getId());
                    boolean isBot =true;
                    String body=null;
                    String fileUrl=null;
                              
                    // sets the messageBody
                    if (!isNull(item.getMessage())){
                    
                        if(!isNull(item.getMessage().getText())){
                            body = item.getMessage().getText();
                        }
                       
                        isBot=item.getMessage().isEcho();
                            
                        
                        fileUrl=hasAttachMent(item.getMessage());    
                    }     
                    
                 
                      
                      
                    
                    
                    
                    //The Graph API does not support sending messages with attachments.
                    //Either the message hasBody or it is an Attachment
                    //i.e You may get a text or attachment but not both
                    
                    
                    MessageType msgType = (fileUrl !=null)?com.viaeaibot.viaeaibot.message.Message.getFileType(fileUrl):MessageType.text;
                    
                    //this is where i used the object builder i added to the message class
                        message.setMessageId(id)
                            .setCreatorId(senderId)
                            .setIsBot(isBot)
                            .setMessage_time(time)
                            .setMessageBody(body)
                            .setFileUrl(fileUrl)
                            .setIsFile(isNull(fileUrl))
                            .setMessageType(msgType);
                        
                   
                 return message; 
        }
         
    /**
     * Checks if the msg object contains an attachment.
     * @param msg The message object to check for attachment
     * @return returns the fileUrl in the msg attachment
     */
    private String hasAttachMent(MessageItem msg){
          
        if(!isNull(msg.getAttachments()) && msg.getAttachments().size()>0){
            return msg.getAttachments().get(0).getUrl();
        }
        
        return null;
    }
}