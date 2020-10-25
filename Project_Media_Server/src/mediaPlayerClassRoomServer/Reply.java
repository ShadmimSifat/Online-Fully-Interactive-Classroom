/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mediaPlayerClassRoomServer;

import static mediaPlayerClassRoomServer.sampleController.getCurrentTime;

/**
 *
 * @author Shayekh Bin Islam
 */
public class Reply {
    private int messageId;
    private  int replyId = 0;
    private String senderReply = new String();
    private String dateTime = new String();
    private String theReplyText = new String();

    public Reply() {
        messageId = 0;
        senderReply = null;
        dateTime = getCurrentTime();
        theReplyText = null;
        this.replyId = 0;
    }
    public Reply(int messageId, String senderReply, String theReplyText,int replyId) {
        this.theReplyText = theReplyText;
        this.messageId = messageId;
        this.senderReply = senderReply;
        this.dateTime = getCurrentTime();
        this.replyId = replyId;
    }

    public int getReplyId() {
        return replyId;
    }
    
    
    
    

    public String getTheReplyText() {
        return theReplyText;
    }

    public void setTheReplyText(String theReplyText) {
        this.theReplyText = theReplyText;
    }
    
    
    public Reply(int messageId, String senderReply) {
        this.messageId = messageId;
        this.senderReply = senderReply;
        this.dateTime = getCurrentTime();
    }

    public int getMessageId(){
        return messageId;
    }

    public void setMessageId(int messageId) {
        this.messageId = messageId;
    }

    public String getSenderReply() {
        return senderReply;
    }

    public void setSenderReply(String senderReply) {
        this.senderReply = senderReply;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    

   
    
    
}
