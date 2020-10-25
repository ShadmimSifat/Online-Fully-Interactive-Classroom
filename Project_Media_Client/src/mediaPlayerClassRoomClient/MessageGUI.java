/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mediaPlayerClassRoomClient;

import static mediaPlayerClassRoomClient.sampleController.ap;
import static mediaPlayerClassRoomClient.sampleController.socket;
import java.util.ArrayList;
import java.util.List;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import static javafx.geometry.Pos.CENTER_LEFT;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import static mediaPlayerClassRoomClient.sampleController.student;

/**
 *
 * @author Shayekh Bin Islam
 */
public class MessageGUI {
    private TextArea messageText = new TextArea();
    private VBox messageVBox = new VBox();
    private HBox messageHBox = new HBox();
    
    private Button replyButton = new Button("Reply");
    private Button editButton = new Button("Edit");
    private Button saveButton = new Button("Save");
    
    private Label timeLabel = new Label();
    private Label senderLabel = new Label();
    
    public Message messageObject = new Message();
    public List<ReplyGUI> replyList = new ArrayList<ReplyGUI>();

    public Message getMessageObject() {
        return messageObject;
    }

    public List<ReplyGUI> getReplyList() {
        return replyList;
    }

    public TextArea getMessageText() {
        return messageText;
    }
    

    public MessageGUI(Message messageObject) {
        this.messageObject = messageObject;
        
        editButton.setOnAction((ActionEvent e) -> {
            messageText.setEditable(true);
            messageHBox.getChildren().add(saveButton);
            
            
            
        });
        
        saveButton.setOnAction((ActionEvent e) -> {
            messageText.setEditable(false);
            messageHBox.getChildren().remove(saveButton);
            
            String receivedMessage = messageText.getText() + "\n";
                   
            System.out.println("Message Editing: " + receivedMessage);
            if (!receivedMessage.equals("")) {
                //socket.sendMessage("Client\n");
                //System.out.println("Edit Sent to Server");
                socket.sendMessage("Edit#" + messageObject.getId() + "#" + receivedMessage);
            }
            
            
        });
        
        replyButton.setOnAction((ActionEvent e) -> {
            Reply replyObject = new Reply(messageObject.getId(),student.getName(),"",replyList.size());            
            ReplyGUI replyGUIObject = new ReplyGUI(replyObject);
            replyList.add(replyGUIObject);
            messageVBox.getChildren().add(replyGUIObject.getReplyVBox());     
        });
        
        messageText.setOnKeyPressed(new EventHandler<KeyEvent>() {
        @Override
        public void handle(KeyEvent keyEvent) {
            if (keyEvent.getCode() == KeyCode.ENTER)  {
                 messageText.setEditable(false);
                messageHBox.getChildren().remove(saveButton);

                String receivedMessage = messageText.getText() + "\n";

                if (!receivedMessage.equals("")) {
                    //socket.sendMessage("Client\n");
                    socket.sendMessage("Edit#" + messageObject.getId() + "#" + receivedMessage);
            }
            }
        }
        });
        
                
        timeLabel.setText("  " + this.messageObject.getDateTime() + "  ");
        senderLabel.setText("#" + this.messageObject.getSender());
        senderLabel.setStyle("-fx-font-weight: bold");
        senderLabel.setStyle("-fx-font-size: 15");
        
        messageText.setMaxHeight(55);
        messageText.setMinHeight(55);
        messageText.setText(this.messageObject.getTheMessageText());
        messageText.setEditable(false);
        
        messageHBox.setAlignment(CENTER_LEFT);
        
        messageHBox.getChildren().add(replyButton);
        messageHBox.getChildren().add(editButton);
        messageHBox.getChildren().add(timeLabel);

        messageVBox.getChildren().add(senderLabel);
        messageVBox.getChildren().add(messageText);
        messageVBox.getChildren().add(messageHBox);
        
        ap.getChildren().add(messageVBox);
    }

    public VBox getMessageVBox() {
        return messageVBox;
    }
    
    public void noEdit(){
        messageHBox.getChildren().remove(editButton);
    }
    
    
    
}
