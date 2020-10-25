/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mediaPlayerClassRoomClient;

import static mediaPlayerClassRoomClient.sampleController.socket;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import static javafx.geometry.Pos.CENTER_LEFT;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import static mediaPlayerClassRoomClient.sampleController.socket;
import static mediaPlayerClassRoomClient.sampleController.student;

/**
 *
 * @author Shayekh Bin Islam
 */
public class ReplyGUI {
    private TextArea replyText = new TextArea();
    private VBox replyVBox = new VBox();
    private HBox replyHBox = new HBox();  
    
    private Button replyButton = new Button("Send");
    private Button editButton = new Button("Edit");
    private Button saveButton = new Button("Save");
    
    private Label timeLabel = new Label();
    private Label senderLabel = new Label();
    
    private Reply replyObject = new Reply();

    public TextArea getReplyText() {
        return replyText;
    }

    private void sendReply(){
        replyText.setEditable(false);
        replyHBox.getChildren().remove(replyButton);
        String receivedMessage = replyText.getText() + "\n";
        replyObject.setTheReplyText(receivedMessage);

        if (!receivedMessage.equals("")) {
            //socket.sendMessage("Client\n");
            socket.sendMessage("Reply#" + replyObject.getMessageId()+ "#"
                    + student.getId() + "#" + receivedMessage);
        }
    }
    
    
    public ReplyGUI(Reply replyObject) {
        this.replyObject = replyObject;
        editButton.setOnAction((ActionEvent e) -> {
            replyText.setEditable(true);
            replyHBox.getChildren().add(saveButton);
        });

        saveButton.setOnAction((ActionEvent e) -> {
            replyText.setEditable(false);
            replyHBox.getChildren().remove(saveButton);
            
            String receivedMessage = replyText.getText() + "\n";
            System.out.println("Reply Editing: " + receivedMessage);
                    
            if (!receivedMessage.equals("")) {
                //socket.sendMessage("Client\n");
                socket.sendMessage("EditReply#" + replyObject.getMessageId() 
                        + "#" + replyObject.getReplyId() + "#" + receivedMessage);
            }
        });
        
        

        replyButton.setOnAction((ActionEvent e) -> {
            sendReply();
        });
        
        replyText.setOnKeyPressed((KeyEvent keyEvent) -> {
            if (keyEvent.getCode() == KeyCode.ENTER)  {
                 sendReply();
            }
        });
        
        replyText.setMaxHeight(55);
        replyText.setMinHeight(55);
        replyText.setText(this.replyObject.getTheReplyText());
        replyText.setEditable(true);
        
        senderLabel.setText("#" + this.replyObject.getSenderReply());
        timeLabel.setText(this.replyObject.getDateTime());
        senderLabel.setStyle("-fx-font-weight: bold");
        senderLabel.setStyle("-fx-font-size: 15");
        replyVBox.setPadding(new Insets(0, 0, 0, 30));
        replyHBox.setAlignment(CENTER_LEFT);
        
        replyHBox.getChildren().add(replyButton);
        replyHBox.getChildren().add(editButton);
        replyHBox.getChildren().add(timeLabel);
        
        replyVBox.getChildren().add(senderLabel);
        replyVBox.getChildren().add(replyText);
        replyVBox.getChildren().add(replyHBox);
        
        //add replyVBox to messageVBox in later
        
    }

    public VBox getReplyVBox() {
        return replyVBox;
    }
    
    public void handleIncomingReply(){
        replyText.setEditable(false);
        replyHBox.getChildren().remove(replyButton);
        replyHBox.getChildren().remove(editButton);
    }
    
}
