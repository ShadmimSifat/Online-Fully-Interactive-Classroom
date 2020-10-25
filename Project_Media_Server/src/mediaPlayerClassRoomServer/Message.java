/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mediaPlayerClassRoomServer;

/**
 *
 * @author Shayekh Bin Islam
 */
public class Message {
    private int id;
    private String sender = new String();
    private String receiver = new String();
    private String dateTime = new String();

    public String getTheMessageText() {
        return theMessageText;
    }
    private String theMessageText = new String();

    public Message(int id, String sender, String receiver, String dateTime, String theMessageText) {
        this.id = id;
        this.sender = sender;
        this.receiver = receiver;
        this.dateTime = dateTime;
        this.theMessageText = theMessageText;
    }
    public Message(){
        id = 0;
        sender = null;
        receiver = null;
        dateTime = null;
    }

    public int getId() {
        return id;
    }

    public void setTheMessageText(String theMessageText) {
        this.theMessageText = theMessageText;
    }
    
    public void setId(int id) {
        this.id = id;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }
    
    
}
