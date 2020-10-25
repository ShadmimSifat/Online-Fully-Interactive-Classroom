/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mediaPlayerClassRoomServer;


import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.StageStyle;


public class Sifat_030119_mp4Support_basicGui extends Application {

public  static Stage primaryStage=null;
public static Scene scene = null;

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("MediaPlayer with Classroom : Teacher's GUI");
        //primaryStage.initStyle(StageStyle.UNDECORATED);
        // primaryStage.setFullScreen(true);
        
        root.getStylesheets().add(getClass().getResource("newCascadeStyleSheet0.css").toExternalForm());

        Scene scene;
        scene = new Scene(root,1010,468);
        
        scene.setFill(Color.BLACK);

        primaryStage.setScene(scene);


        scene.setOnMouseClicked((MouseEvent event) -> {
            if(event.getClickCount()==2)
                primaryStage.setFullScreen(true);
        });
        this.primaryStage=primaryStage;
        this.scene = scene;
        primaryStage.getIcons().add(new Image(("/images_and_data/stageIcon.png")));
        
        primaryStage.show();
        
        Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
        primaryStage.setX((primScreenBounds.getWidth() - primaryStage.getWidth()) / 2); 
        primaryStage.setY((primScreenBounds.getHeight() - primaryStage.getHeight()) / 4);  
    }


    public static void main(String[] args) {
        launch(args);
    }
}
