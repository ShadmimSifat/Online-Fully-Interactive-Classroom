/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mediaPlayerClassRoomServer;

import javafx.beans.binding.Bindings;
import javafx.beans.value.ObservableValue;
import javafx.scene.layout.StackPane;
import javafx.scene.control.MenuBar;
import javafx.stage.FileChooser;
import javafx.scene.image.*;
import javafx.scene.media.*;
import javafx.util.Duration;
import javafx.scene.control.SplitPane;
import java.util.Iterator;
import java.util.List;
import com.jfoenix.controls.JFXSlider;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.invoke.MethodHandles;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.Socket;
import java.net.SocketException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.WindowEvent;
import javafx.util.Callback;
import static mediaPlayerClassRoomServer.sampleController.ap;
import static mediaPlayerClassRoomServer.sampleController.getCurrentTime;
import static mediaPlayerClassRoomServer.sampleController.msg;
import static mediaPlayerClassRoomServer.sampleController.socket;
import static mediaPlayerClassRoomServer.Sifat_030119_mp4Support_basicGui.primaryStage;
import org.fredy.jsrt.api.SRT;
import org.fredy.jsrt.api.SRTInfo;
import org.fredy.jsrt.api.SRTReader;
import socketfx.Constants;
import socketfx.FxSocketServer;
import socketfx.SocketListener;


public class sampleController  implements Initializable  {
/*    @FXML
    private 
*/
    @FXML
    private AnchorPane acPenRoot;
    @FXML
    private TextArea message;
    @FXML
    public VBox apMessage;
    @FXML
    private Button playPauseButton;
    @FXML
    private Button stopButton;
    @FXML
    private Button volumeButton;
    @FXML
    private  MediaView mediaView  = new MediaView();
    @FXML
    private static MediaPlayer mediaPlayer;
    @FXML
    private JFXSlider volumeSlider;
    @FXML
    private JFXSlider seekSlider ;
    @FXML
    private Label currentTimeLabel;
    @FXML
    private Label totalTimeLabel;
    @FXML
    private StackPane stackPane;
    @FXML
    private MenuBar menuBar;
    @FXML
    private VBox rootVBox;
    @FXML
    private AnchorPane anchorPane1;
    @FXML
    private VBox lowestVbox;
    @FXML
    private SplitPane splitPane1;
    @FXML
    private HBox topHB;
    @FXML
    private MenuItem repeat = new MenuItem();
    @FXML
    private TableView studentTable;    
    @FXML
    private TableView videoTable;
    @FXML
    private TextField stdName; 
    @FXML
    private TextField stdID; 
    @FXML
    private TextField stdDept; 
    @FXML
    private TableColumn nameColumn;
    @FXML
    private TableColumn idColumn;
    @FXML
    private TableColumn departmentColumn;    
    @FXML
    private TableColumn titleColumn;
    @FXML
    private TableColumn selectColumn;
    @FXML
    private TableColumn videoSelectColumn;    
    @FXML
    private ToggleButton showConsoleButton;     
    @FXML
    private HBox rootHB;
    @FXML
    private VBox chatBox;
    @FXML
    private HBox teacherConsole;  
    Student student = null;
    @FXML
    ToggleButton editProfile;
    @FXML 
    private TableColumn connectedColumn;
    @FXML 
    private CheckBox selectAllStudent;
    @FXML
    private ProgressBar individualProgress;
    @FXML
    private TextField teacherAddress;
    @FXML
    private Button addressButton;
    @FXML
    private Label nowPlaying;
    @FXML
    private ImageView iv2;
    @FXML
    private Label closedCaption;   
    
    private MenuItem loadSub;    
    public static TextArea msg = new TextArea();      
    public static VBox ap = new VBox();    
    private final static Logger LOGGER =
        Logger.getLogger(MethodHandles.lookup().lookupClass().getName());
    private ObservableList<String> rcvdMsgsData;
    private ObservableList<String> sentMsgsData;
    private ArrayList<MessageGUI> MsgObj = new ArrayList<>();
    //private ArrayList<MessageGUI> receivedMsgObj = new ArrayList<MessageGUI>();
    ColorAdjust colorAdjust = new ColorAdjust();      
    private boolean isConnected;
    public static FxSocketServer socket;      
    private boolean isPaused = false;
    private Iterator<String> iterator;
    List<String> list = new ArrayList<>();
    //public static Student currentStudent = null;    
    public static ObservableList<Student> studentData = FXCollections.observableArrayList();
    ObservableList<Videos> videoData = FXCollections.observableArrayList();
    public static TableView studentTablecp; 
    public static BufferedReader in = null;
    
    public enum ConnectionDisplayState {
        DISCONNECTED, WAITING, CONNECTED, AUTOCONNECTED, AUTOWAITING
    }   

    private void connect(Socket connectionSocket) {
        
        socket.connect(connectionSocket);
    }

//    private void displayState(ConnectionDisplayState state) {
//        switch (state) {
//            case DISCONNECTED:
//
//                break;
//            case WAITING:
//            case AUTOWAITING:
//                break;
//            case CONNECTED:
//                break;
//            case AUTOCONNECTED:
//                break;
//        }
//    }
    
/*    @FXML
      private ; 
*/       
    
    public static List<String> allText = new ArrayList<String>();
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //iv2.setImage(image);
        iv2.setFitWidth(60);
        iv2.setPreserveRatio(true);
        iv2.setSmooth(true);
        iv2.setCache(true);       
        
        currentTimeLabel.setText("00 : 00 : 00");
        totalTimeLabel.setText("00 : 00 : 00");
        
        Image image = new Image(getClass().getResourceAsStream("/images_and_data/pause_57w.png"));
        playPauseButton.setGraphic(new ImageView(image));        
        image = new Image(getClass().getResourceAsStream("/images_and_data/volume.png"));
        volumeButton.setGraphic(new ImageView(image));
        
        final Delta dragDelta = new Delta();
        rootVBox.setOnMousePressed(new EventHandler<MouseEvent>() {
          @Override 
          public void handle(MouseEvent mouseEvent) {
            // record a delta distance for the drag and drop operation.
            dragDelta.x = primaryStage.getX() - mouseEvent.getScreenX();
            dragDelta.y = primaryStage.getY() - mouseEvent.getScreenY();
          }
        });
        
        rootVBox.setOnMouseDragged(new EventHandler<MouseEvent>() {
          @Override public void handle(MouseEvent mouseEvent) {
            primaryStage.setX(mouseEvent.getScreenX() + dragDelta.x);
            primaryStage.setY(mouseEvent.getScreenY() + dragDelta.y);
          }
        });
        
        loadStudentData();
                   
        studentTable.setItems(studentData);          
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        departmentColumn.setCellValueFactory(new PropertyValueFactory<>("department"));
        connectedColumn.setCellValueFactory(new PropertyValueFactory<>("connected"));
        selectColumn.setCellValueFactory(new PropertyValueFactory<>("select"));        
        
        File f = new File("./src/mediaPlayerClassRoomServer/Video_Demo.mp4");
        Videos tutorial = new Videos(f);
        videoData.add(tutorial);
        videoTable.setItems(videoData);              
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        videoSelectColumn.setCellValueFactory(new PropertyValueFactory<>("select"));        
        
        final Delta dragDelta2 = new Delta();
         
        acPenRoot.setOnMousePressed((MouseEvent mouseEvent) -> {
            // record a delta distance for the drag and drop operation.
            dragDelta2.x = primaryStage.getX() - mouseEvent.getScreenX();
            dragDelta2.y = primaryStage.getY() - mouseEvent.getScreenY();
        });
        acPenRoot.setOnMouseDragged((MouseEvent mouseEvent) -> {
            primaryStage.setX(mouseEvent.getScreenX() + dragDelta2.x);
            primaryStage.setY(mouseEvent.getScreenY() + dragDelta2.y);
        });
        ap = apMessage;
        msg = message;        
        isConnected = false;
        sentMsgsData = FXCollections.observableArrayList();
        rcvdMsgsData = FXCollections.observableArrayList();
        
        Runtime.getRuntime().addShutdownHook(new sampleController.ShutDownThread());
        if(primaryStage!= null)
            primaryStage.setOnCloseRequest((WindowEvent te) -> {                  
                socket.shutdown();                
                Platform.exit();
                System.exit(0);
        });
      
       message.setOnKeyPressed((KeyEvent keyEvent) -> {
           if (keyEvent.getCode() == KeyCode.ENTER)  {
               String receivedMessage = message.getText() + "\n";
               try {
                   Platform.runLater(() -> {
                       message.positionCaret( 0 );
                       message.clear();
                   });
               } catch (Exception e) {
               }
                allText.add(receivedMessage);
               
               if (!receivedMessage.equals("\n") && socket != null) {
                   for (int it = 0; it < studentData.size(); it++) {
                       Student s = studentData.get(it);
                       if(s.getConnected().equals("Yes")){
                           try {
//                              String outTxt = ;
//                              allText.add(outTxt);                              
                               
                                socket.sendMessage(receivedMessage, s.getSock());
                                //sentMsgsData.add(receivedMessage);
                           } catch (IOException ex) {
                               Logger.getLogger(sampleController.class.getName()).log(Level.SEVERE, null, ex);
                           }
                       }                       
                   }
                   //socket.sendMessage(receivedMessage);
                   sentMsgsData.add(receivedMessage);
                   Message m = new Message(MessageCounter++, "Teacher", "Client", getCurrentTime() , receivedMessage);
                   MessageGUI mg = new MessageGUI(m);
                   MsgObj.add(mg);
               }
           }
        });
       
       showConsoleButton.setOnAction(evt -> {
            if (showConsoleButton.isSelected()) {
                rootVBox.getChildren().add(teacherConsole);
                rootHB.getChildren().add(chatBox);
                primaryStage.setWidth(1010);
            } else {
                rootVBox.getChildren().remove(teacherConsole);
                rootHB.getChildren().remove(chatBox);
                primaryStage.setWidth(570);
            }
        });
//       editProfile.setOnAction(e->{           
//           if(editProfile.isSelected()){
//               stdName.setEditable(true);
//            stdID.setEditable(true);
//            stdDept.setEditable(true);
//               editProfile.setText("Save");
//           }
//           else{
//               stdName.setEditable(false);
//                stdID.setEditable(false);
//                stdDept.setEditable(false);
//               editProfile.setText("Edit Profile");
//           }
//           
//           
//       });
        selectAllStudent.setOnAction(e->{
            if(selectAllStudent.isSelected()){
                for (int iterator = 0; iterator < studentData.size(); iterator++) {
                    studentData.get(iterator).getSelect().setSelected(true);
                }
            }
            else{
                for (int iterator = 0; iterator < studentData.size(); iterator++) {
                    studentData.get(iterator).getSelect().setSelected(false);
                }
            }            
        });       
        socket = new FxSocketServer(new sampleController.FxSocketListener(),
            Integer.valueOf("6788"), Constants.instance().DEBUG_NONE);        
        mediaView.setEffect(colorAdjust);        
        (new Thread(new ConnectedTask())).start();        
        teacherAddress.setText(IPTeller());
        addressButton.setOnAction((e)->{
            teacherAddress.setText(IPTeller());
        });
        studentTablecp = studentTable;
//        stackPane.setOnKeyReleased(new EventHandler<KeyEvent>() {
//            @Override
//            public void handle(KeyEvent event) {
//                switch (event.getCode()) {
//                    case LEFT: ; 
//                            break;
//                    case RIGHT: 
//                        Duration d = new Duration(5);
//                        mediaPlayer.seek(mediaPlayer.getCurrentTime().add(d)); 
//                        break;
//                    //case SHIFT: ; break;
//                }
//            }
//        });                   
    }
    
    class ConnectedTask implements Runnable{
        @Override
        public void run(){
            while (true) {
                for (int iterator = 0; iterator < studentData.size(); iterator++) {
                    if(studentData.get(iterator).getConnected().equals("No")){
                        studentData.get(iterator).getSelect().setDisable(true);
                    }
                    else{
                        studentData.get(iterator).getSelect().setDisable(false);
                    }
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(sampleController.class.getName()).log(Level.SEVERE, null, ex);
                    }            
                }
            }
        }        
    }
    
    public void loadStudentData(){
        FileReader fr = null;
        try {
            String fileName = 
            "./src/mediaPlayerClassRoomServer/studentData.txt";
            File file = new File(fileName);
            fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr);
            String line;
            try {
                while((line = br.readLine()) != null){
                    String[] parts = line.split(", ");
                    studentData.add(new Student(parts[0], Integer.parseInt(parts[1]), parts[2]));                    
                    //process the line
                    //System.out.println(line);
                }
            } catch (IOException ex) {                
            }
        } catch (FileNotFoundException ex) {            
        } finally {
            try {
                fr.close();
            } catch (IOException ex) {                
            }
        }
    }
    /**
     *
     * @param filePath
     * @throws NullPointerException
     */
    public static File subtitle = null;//"./src/in.srt";
    public static Media md = null;
    @FXML
    public void addImage(){
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter filter;
        filter = new FileChooser.ExtensionFilter("Select Image(*.png)", new String[]{"*.png"});
        fileChooser.getExtensionFilters().add(filter);
        File file = fileChooser.showOpenDialog(null);
        
        String filePath = file.toURI().toString();
        if(filePath != null)
            iv2.setImage(new Image(filePath));
    }   
    @FXML
    public void subLoad(){
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter filter;
        filter = new FileChooser.ExtensionFilter("Select Subtitle(*.srt)", new String[]{"*.srt"});
        fileChooser.getExtensionFilters().add(filter);
        File file = fileChooser.showOpenDialog(null);
        subtitle = new File(file.getAbsolutePath());
        //System.out.println(subtitle);
        if(subtitle != null  && md != null){
            SRTInfo info = SRTReader.read(subtitle);
            //System.out.println("thread started .............");
            for (SRT ss : info) {
                String t = "";                 
                for (String line : ss.text) {
                    t =  t + "    " + line;
                }                              
                md.getMarkers().put(t, Duration.millis(ss.startTime.getHours()*3600000 
                    + ss.startTime.getSeconds()*1000 + ss.startTime.getMinutes() * 60000));                
            }          
            mediaPlayer.setOnMarker((MediaMarkerEvent event) ->
                closedCaption.setText(event.getMarker().getKey())
            );
            
        }                
    }   
  
    public void setPlayer(String filePath ) throws NullPointerException
    {
       try {
            if (filePath!=null) {               
            try {           
                File f = new File(filePath);
                String s = f.getName();
                s = (s.replaceAll("%20"," "));
                nowPlaying.setText(s);               
                           
                Media media = new Media(filePath);
                md = media;
                mediaPlayer = new MediaPlayer(media);
                mediaView.setMediaPlayer(mediaPlayer);              
                if(subtitle != null){
                    SRTInfo info = SRTReader.read(subtitle);
             //System.out.println("thread started .............");
                for (SRT ss : info) {
                    String t = "";                 
                    for (String line : ss.text) {
                        t =  t + "    " + line;
                    }


                    media.getMarkers().put(t, Duration.millis(ss.startTime.getHours()*3600000 + 
                        ss.startTime.getSeconds() *1000 + ss.startTime.getMinutes() * 60000)); 
                   //media.getMarkers().put("    ", Duration.millis
            //(ss.endTime.getHours()*3600000 + ss.endTime.getSeconds()*1000 + ss.endTime.getMinutes() * 60000));

                }          
                mediaPlayer.setOnMarker((MediaMarkerEvent event) ->
                    closedCaption.setText(event.getMarker().getKey())
                );            
            }           
            
            mediaView.fitWidthProperty().bind(stackPane.widthProperty());
            mediaView.fitHeightProperty().bind(stackPane.heightProperty());
            
//            DoubleProperty width = mediaView.fitWidthProperty();
//            DoubleProperty height = mediaView.fitHeightProperty();
//
//            width.bind(Bindings.selectDouble(mediaView.sceneProperty(), "width"));
//            height.bind(Bindings.selectDouble(mediaView.sceneProperty(), "height"));
            //mediaView.setPreserveRatio(true);

            volumeSlider.setValue(mediaPlayer.getVolume() * 50);
            volumeSlider.valueProperty().addListener(e ->
                mediaPlayer.setVolume(volumeSlider.getValue() / 100));            
            //volumeSlider.valueProperty().bindBidirectional(mediaPlayer.volumeProperty());
            try {
                if(seekSlider !=null  && mediaPlayer!= null)
                    seekSlider.maxProperty().bind(Bindings.createDoubleBinding(
                        () -> mediaPlayer.getTotalDuration().toSeconds(),
                        mediaPlayer.totalDurationProperty()));
            } catch (NullPointerException e) {
            }               
                
            if(totalTimeLabel!= null && mediaPlayer!= null){
                try {           
                    totalTimeLabel.textProperty().bind(
                        Bindings.createStringBinding(() -> {
                        Duration totalDuration = mediaPlayer.getTotalDuration();                    
                        return String.format("%02d : %02d : %02d",
                            (int) totalDuration.toHours(),
                            (int) totalDuration.toMinutes() % 60,
                            (int)(totalDuration.toSeconds() % 3600) % 60);
                        },mediaPlayer.currentTimeProperty()));
                } catch (Exception e) {
                }
            }
             
            seekSlider.setValueFactory(slider ->
                Bindings.createStringBinding(() ->  {
                Duration time = mediaPlayer.getCurrentTime();
                return String.format("%02d:%02d:%02d",
                    (int) time.toHours(),
                    (int) time.toMinutes() % 60,
                    (int)(time.toSeconds() % 3600) % 60);
                }, mediaPlayer.currentTimeProperty())
            );
            
            try 
            {
                currentTimeLabel.textProperty().bind(
                Bindings.createStringBinding(() -> {
                    Duration time = mediaPlayer.getCurrentTime();
                    return String.format("%02d : %02d : %02d",
                            (int) time.toHours(),
                            (int) time.toMinutes() % 60,
                            (int)(time.toSeconds() % 3600) % 60);
                }, mediaPlayer.currentTimeProperty()));            
            
            mediaPlayer.currentTimeProperty().
               addListener((ObservableValue<? extends Duration> 
                observable, Duration oldValue, Duration newValue) -> {
                seekSlider.setValue(newValue.toSeconds());
                });
            } catch (Exception e) {
            }                    
            subClear.setOnAction(value->{
                media.getMarkers().clear();
                closedCaption.setText("");           
            });                       

            seekSlider.setOnMousePressed((MouseEvent event) -> {
                mediaPlayer.seek(Duration.seconds(seekSlider.getValue()));
            });
            
            seekSlider.setOnMouseDragged((MouseEvent event) -> {
                mediaPlayer.seek(Duration.seconds(seekSlider.getValue()));
            });

            mediaPlayer.play();                       
            primaryStage.setFullScreenExitHint("");                               
            
            mediaPlayer.setOnEndOfMedia(() -> {
              mediaPlayer.stop();
              subtitle = null;
              closedCaption.setText("");
              //Image image = new Image(getClass().getResourceAsStream("play57w.png"));
              //currentTimeLabel.setText("00 : 00 : 00");
              // playPauseButton.setGraphic(new ImageView(image));
              
              isPaused = true;
              mediaPlayer.seek(new Duration(0));
              currentTimeLabel.setText("00 : 00 : 00");
              
              if (iterator.hasNext()) {
                  //Plays the subsequent files recursively
                  setPlayer(iterator.next());
              }
            });
            } catch (Exception e) {
            }
        }                              
        } catch (Exception e) {
        }
    }

    @FXML
    private void openSingleFileButtonClicked() {
        String  selectDialog = "Select Media(*.mp4, *.wav,  *.m4a, *.m4v, *.mp3)";
        String[] ext =  new String[]{"*.mp4","*.mp3", "*.wav", "*.m4a", "*.m4v"};
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter filter;
        filter = new FileChooser.ExtensionFilter(selectDialog, ext);
        fileChooser.getExtensionFilters().add(filter);
        File file = fileChooser.showOpenDialog(null);
        String filePath = file.toURI().toString();
        if(filePath != null){
            if(list != null)
            list.clear();
        
        if(mediaPlayer!=null)
            mediaPlayer.stop();        
        list.add(filePath);
        if(list != null){
            iterator = list.iterator();
            //Plays the first file
            try {
             setPlayer(iterator.next());   
            } catch (NullPointerException e) {
            }        
        } 
        }
               
    }
    @FXML
    private  void openMultiFileMenuClicked()
    {
        FileChooser fileChooser = new FileChooser();
        String  selectDialog = "Select Media(*.mp4,  *.wav,  *.m4a, *.m4v, *.mp3)";
        String[] ext =  new String[]{"*.mp4","*.mp3", "*.wav", "*.m4a", "*.m4v"};
        
        FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter(selectDialog, ext);
        fileChooser.getExtensionFilters().add(filter);

        List<File> selectedFiles = fileChooser.showOpenMultipleDialog(null);
        //list.clear();
        if(mediaPlayer!=null)
            mediaPlayer.stop();
        if(selectedFiles!=null)
        {
            for (int iterator=0;iterator<selectedFiles.size();iterator++)
            {
                list.add(selectedFiles.get(iterator).toURI().toString());
               // System.out.println(list.get(i));
            }
            iterator = list.iterator();
            //Plays the first file
            try {
             setPlayer(iterator.next());   
            } catch (Exception e) {
            }
        }
    }
    
  
    @FXML
    public void playPauseButtonClicked(ActionEvent event) {
        if (isPaused == false) {
            try {
                mediaPlayer.pause();
            } catch (Exception e) {
            }
            
            Image image = new Image(getClass().getResourceAsStream("/images_and_data/play57w.png"));
            playPauseButton.setGraphic(new ImageView(image));
            isPaused = true;
        } else {
            Image image = new Image(getClass().getResourceAsStream("/images_and_data/pause_57w.png"));
            playPauseButton.setGraphic(new ImageView(image));
            try {
                mediaPlayer.play();
            } catch (Exception e) {
            }
            isPaused = false;
        }
    }

    @FXML
    public void stopButtonClicked(ActionEvent event) {
        try {
            mediaPlayer.stop();
            mediaPlayer.setRate(1);
            } catch (Exception e) {
            }
        Image image = new Image(getClass().getResourceAsStream("/images_and_data/play57w.png"));
        playPauseButton.setGraphic(new ImageView(image));        
        isPaused = true;
        currentTimeLabel.setText("00 : 00 : 00");
    }
    @FXML
    public void forwardButtonClicked(ActionEvent event) {
        //System.out.println(mediaPlayer.getRate());
        if(mediaPlayer != null)
        mediaPlayer.setRate(mediaPlayer.getRate() + 0.13);

    }
    @FXML
    public void backwardButtonClicked(ActionEvent event) {
        // System.out.println(mediaPlayer.getRate());
        if(mediaPlayer != null)
        mediaPlayer.setRate(mediaPlayer.getRate() - 0.13);
    }
    @FXML
    public void volumeButtonClicked(ActionEvent event) {
        try {
            if (!mediaPlayer.isMute()){
                mediaPlayer.setMute(true);
                Image image = new Image(getClass().getResourceAsStream("/images_and_data/mute.png"));
                volumeButton.setGraphic(new ImageView(image));            
            }
            else{ 
                mediaPlayer.setMute(false);
                Image image = new Image(getClass().getResourceAsStream("/images_and_data/volume.png"));
                volumeButton.setGraphic(new ImageView(image));
            }
        } catch (Exception e) {
        }                
    }
    @FXML 
    public void exitSystemButton(ActionEvent event){
        System.exit(0);
    }
    
    @FXML
    public void minimizeClicked(ActionEvent event){
        primaryStage.setIconified(true);        
    }
    
    static int i = 0;
    @FXML
    public void maximizeClicked(ActionEvent event){
         
          i++;
          if(i == 1){
            primaryStage.setMaximized(true);
          }
          else if(i == 2){
              primaryStage.setMaximized(false);
              i = 0;
          }    
    }    
    @FXML
    public void exitClicked(ActionEvent event){
          System.exit(0);
    }
    
    @FXML
    public void dragDropOver(DragEvent event) {
        Dragboard db = event.getDragboard();
        if (db.hasFiles() || db.hasUrl()) {
            /* allow for both copying and moving, whatever user chooses */
           event.acceptTransferModes(TransferMode.ANY);
        }
        event.consume();
    }
    @FXML
    public void dragdropdrop(DragEvent event) {
        Dragboard db = event.getDragboard();
        String url = null;
        if (db.hasFiles()) {
                url = db.getFiles().get(0).toURI().toString();
        } else if (db.hasUrl()) {
                url = db.getUrl();
        }
        if (url != null) {
            list.clear();
            if(mediaPlayer!=null)
                mediaPlayer.stop();
            list.add(url);
            iterator = list.iterator();
            //Plays the first file           
            try {
             setPlayer(iterator.next());   
            } catch (NullPointerException e) {
            }                                  
            event.setDropCompleted(url != null);
            event.consume();
        }
   }
    @FXML
    public void repeatHandler(ActionEvent event){
        //if(repeat != null)
       // repeat.setText("Repeat" + "\u2713");
        //System.out.println("Repeat \u2713");
    }
    int once = 0;
    public static Socket connectionSocket = null;
    @FXML
    public void clientHandle(ActionEvent event){
        if(once == 0){
            (new Thread(new ConnectionInit())).start();
            once = 1;
        }            
    }
    public static Thread currentThread = null;
    public static Socket port = null;
    
    class ConnectionInit implements Runnable{        
        @Override
        public void run(){                   
            while(true)
            {
                try {
                    //System.out.println(socket.getServerSocket());
                    
                    connectionSocket = socket.getServerSocket().accept();                    
                    port = connectionSocket;
                    if(connectionSocket!=null){
                        Thread t = new Thread(new ConnectionTask(connectionSocket));
                        currentThread = t;
                        t.start();
                    }                      
                    
                } catch (IOException ex) {
                   // System.out.println(socket.getServerSocket()); 
                    try {
                        Thread.currentThread().sleep(2000);
                    } catch (InterruptedException ex1) {
                        Logger.getLogger(sampleController.class.getName()).log(Level.SEVERE, null, ex1);
                    }
                }
            }        
        }
    }
        
    
    class ConnectionTask implements Runnable{
        private Socket connectionSocket;
        ConnectionTask(Socket connectionSocket){
            this.connectionSocket = connectionSocket;
        }
        public void run(){
            try {
               connect(connectionSocket); 
            } catch (Exception e) {
                
            }                    
        }
    }
    
    @FXML
    public void controlTeacherConsole(ActionEvent event){
        
    }
    
    @FXML
    public void addVideoHandle(ActionEvent event){
        FileChooser fileChooser = new FileChooser();
        String  selectDialog = "Select Media(*.mp4,  *.wav,  *.m4a, *.m4v, *.mp3)";
        String[] ext =  new String[]{"*.mp4","*.mp3", "*.wav", "*.m4a", "*.m4v"};
        
        FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter(selectDialog, ext);
        fileChooser.getExtensionFilters().add(filter);

        List<File> selectedFiles = fileChooser.showOpenMultipleDialog(null);
        if(selectedFiles != null){
            for (File selectedFile : selectedFiles) {
            Videos tutorial = new Videos(selectedFile);
            videoData.add(tutorial);
        }
        }       
        
        
    }
    @FXML
    public void addStudentHandle(ActionEvent event){
       Dialog<Student> dialog = new Dialog<>();
        dialog.setTitle("Student Info");
        dialog.setHeaderText("Enter Student Information");
    dialog.setResizable(true);
 
        Label label1 = new Label("Name: ");
        Label label2 = new Label("ID: ");
        Label label3 = new Label("Department: ");
        
        TextField text1 = new TextField();
        TextField text2 = new TextField();
        TextField text3 = new TextField();

        GridPane grid = new GridPane();
        grid.add(label1, 1, 1);
        grid.add(text1, 2, 1);
        grid.add(label2, 1, 2);
        grid.add(text2, 2, 2);
        grid.add(label3, 1, 3);
        grid.add(text3, 2, 3);
        dialog.getDialogPane().setContent(grid);

        ButtonType buttonTypeOk = new ButtonType("OK", ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().add(buttonTypeOk);

        dialog.setResultConverter((ButtonType b) -> {
            if (b == buttonTypeOk) {                
                return new Student(text1.getText(), Integer.parseInt(text2.getText()) , text3.getText());
            }            
            return null;
       });
         
        Optional<Student> result = dialog.showAndWait();
        if(result!=null){
            studentData.add(result.get());
        studentTable.refresh();
        try { 
  
            // Open given file in append mode. 
            BufferedWriter out = new BufferedWriter( 
                   new FileWriter("./src/mediaPlayerClassRoomServer/studentData.txt", true)); 
            out.write("\n"); 
            out.write(result.get().getName() + ", ");
            out.write(result.get().getId() + ", ");
            out.write(result.get().getDepartment());
            out.close(); 
        } 
        catch (IOException e) { 
            System.out.println("exception occoured" + e); 
        } 
        }      

    }
    
    @FXML
    private ProgressBar videoProgress;
    @FXML
    public void sendVideoButtonHandle(ActionEvent event){
        for (int it = 0; it <   studentData.size(); it++) {
            Student st = studentData.get(it);
            if(st.getConnected().equals("Yes") && st.getSelect().isSelected() == true){
                (new Thread(new SendVideoTask(it))).start();       
            }     
            else if(st.getConnected().equals("No") && selectAllStudent.isSelected() == true){
                 (new Thread(new  ScheduleSend (it))).start(); 
            }
        }        
    }
    class ScheduleSend implements Runnable{
        private Student st;
        public ScheduleSend(int id) {
            this.st = studentData.get(id);
        }              
        
        public void run(){
            while(st.getConnected().equals("No")){
                
            }
            for (int j = 0; j < videoData.size(); j++) {
                if(videoData.get(j).getSelect().isSelected() == true){
                try
                {           
                    Socket s = st.getSock();
                    File file = videoData.get(j).getFile();
                    FileInputStream fis = new FileInputStream(file);
                    BufferedInputStream bis = new BufferedInputStream(fis);
                    
                    byte[] contents;
                    long fileLength = file.length();
                    //System.out.println(fileLength);
                    
                    socket.sendMessage("Download#" + fileLength + "#" + file.getName() + "\n", s);
                    Thread.sleep(1000);
                    
                    OutputStream os = s.getOutputStream();
                    
                    long current = 0;
                    individualProgress.setProgress(0.0);
                    //(new Thread(new Progress(current, fileLength))).start();
                    //progressVideoStage(;
                    while(current!=fileLength){ 
                        int size = 10000;
                        if(fileLength - current >= size)
                            current += size;    
                        else{ 
                            size = (int)(fileLength - current); 
                            current = fileLength;
                        } 
                        contents = new byte[size]; 
                        bis.read(contents, 0, size); 
                        os.write(contents);
                        //System.out.println(current);
                        //System.out.println("Sending file ... "+(current*100)/fileLength+"% complete!");
                        individualProgress.setProgress((double)current/fileLength);
                    }
                    os.flush(); 
                    fis.close();
                    bis.close();
                    //System.out.println("File sent successfully!");
                    Thread.sleep(1000);
                }
                    catch(Exception e)
                {
                    //System.err.println("Could not transfer file.");
                }
            
                }
            }
        }
    }
    
    class SendVideoTask implements Runnable{
        private Student st;

        public SendVideoTask(int id) {
            this.st =  studentData.get(id);
        }
        
        public void run(){
            int totalVideos = 0, sentVideos = 0;
            for (int j = 0; j < videoData.size(); j++) {
            if(videoData.get(j).getSelect().isSelected() == true){
                totalVideos++;
            }
            }
            videoProgress.setProgress(0.0);
            
            for (int j = 0; j < videoData.size(); j++) {
                if(videoData.get(j).getSelect().isSelected() == true){
                try{           
                    Socket s = st.getSock();
                    File file = videoData.get(j).getFile();
                    FileInputStream fis = new FileInputStream(file);
                    BufferedInputStream bis = new BufferedInputStream(fis);
                    
                    byte[] contents;
                    long fileLength = file.length();
                    //System.out.println(fileLength);
                    
                    socket.sendMessage("Download#" + fileLength + "#" + file.getName() + "\n", s);
                    Thread.sleep(1000);
                    
                    OutputStream os = s.getOutputStream();
                    
                    long current = 0;
                    individualProgress.setProgress(0.0);
                    //(new Thread(new Progress(current, fileLength))).start();
                    //progressVideoStage(;
                    while(current!=fileLength){ 
                        int size = 10000;
                        if(fileLength - current >= size)
                                current += size;    
                        else{ 
                                size = (int)(fileLength - current); 
                                current = fileLength;
                        } 
                        contents = new byte[size]; 
                        bis.read(contents, 0, size); 
                        os.write(contents);
                        //System.out.println(current);
                        //System.out.println("Sending file ... "+(current*100)/fileLength+"% complete!");
                        individualProgress.setProgress((double)current/fileLength);
                    }
                    os.flush(); 
                    fis.close();
                    bis.close();
                    //Thread.sleep(500);
                    //System.out.println("File sent successfully!");
                    
                    sentVideos++;
                    //Thread.sleep(1000);
                    videoProgress.setProgress((double)sentVideos/totalVideos);
                    Thread.sleep(1000);
                    }
                    catch(Exception e)
                    {
                    //System.err.println("Could not transfer file.");
                    }
                }                    
            }           
        }
    }
    @FXML
    private MenuItem subClear;
    
    private static int MessageCounter = 0;
    @FXML
    public void sendHandle(ActionEvent event){
            String receivedMessage = message.getText() + "\n";
               try {
                   Platform.runLater(() -> {
                       message.positionCaret( 0 );
                       message.clear();
                   });
               } catch (Exception e) {
               }
               allText.add(receivedMessage);
               
               if (!receivedMessage.equals("\n") && socket != null) {
                   for (int it = 0; it < studentData.size(); it++) {
                       Student s = studentData.get(it);
                       if(s.getConnected().equals("Yes")){
                           try {
                               
                               socket.sendMessage(receivedMessage, s.getSock());
                               //sentMsgsData.add(receivedMessage);
                           } catch (IOException ex) {
                               Logger.getLogger(sampleController.class.getName()).log(Level.SEVERE, null, ex);
                           }
                       }
                       
                   }
                   //socket.sendMessage(receivedMessage);
                   sentMsgsData.add(receivedMessage);
                   Message m = new Message(MessageCounter++, "Teacher", "Client", getCurrentTime() , receivedMessage);
                   MessageGUI mg = new MessageGUI(m);
                   MsgObj.add(mg);
               }
    }
    @FXML
    public void resetColor(ActionEvent event){
        resetColor();
    }
    @FXML
    public void increaseBrightness(ActionEvent event){
        increaseBrightness();
    }
    @FXML
    public void decreaseBrightness(ActionEvent event){
        decreaseBrightness();
    }
    @FXML
    public void increaseHue(ActionEvent event){
        increaseHue();
    }
    @FXML
    public void decreaseHue(ActionEvent event){
        decreaseHue();
    }
    @FXML
    public void increaseSaturation(ActionEvent event){
        increaseSaturation();
    }
    @FXML
    public void decreaseSaturation(ActionEvent event){
        decreaseSaturation();
    }
    @FXML
    public void increaseContrast(ActionEvent event){
        increaseContrast();
    }
    @FXML
    public void decreaseContrast(ActionEvent event){
        decreaseContrast();
    }    

    public void resetColor(){
        colorAdjust.setContrast(0);
        colorAdjust.setHue(0);
        colorAdjust.setBrightness(0);
        colorAdjust.setSaturation(0);
    }
    
    public void increaseBrightness(){
        double currentBrightness = colorAdjust.getBrightness();        
        if(currentBrightness < 1.0){
            colorAdjust.setBrightness(currentBrightness + .02);
        }
    }
    
    public void decreaseBrightness(){        
        double currentBrightness = colorAdjust.getBrightness();
        if(currentBrightness > - 1.0){
            colorAdjust.setBrightness(currentBrightness - .02);
        }
    }
    
    public void increaseHue(){
        double currentHue = colorAdjust.getHue();        
        if(currentHue < 1.0){
            colorAdjust.setHue(currentHue + .02);
        }
    }
    
    public void decreaseHue(){
        double currentHue = colorAdjust.getHue();
        if(currentHue > - 1.0){
            colorAdjust.setHue(currentHue - .02);
        }
    }
    
    public void increaseSaturation(){
        double currentSaturation = colorAdjust.getSaturation();        
        if(currentSaturation < 1.0){
            colorAdjust.setSaturation(currentSaturation + .02);
        }
    }
    
    public void decreaseSaturation(){
        double currentSaturation = colorAdjust.getSaturation();
        if(currentSaturation > - 1.0){
            colorAdjust.setSaturation(currentSaturation - .02);
        }
    }
   
    public void increaseContrast(){
        double currentContrast = colorAdjust.getContrast();        
        if(currentContrast < 1.0){
            colorAdjust.setContrast(currentContrast + .02);
        }
    }
    
    public void decreaseContrast(){
        double currentContrast = colorAdjust.getContrast();
        if(currentContrast > - 1.0){
            colorAdjust.setContrast(currentContrast - .02);
        }
    }
 
    public static BufferedWriter out = null;
    public static String getCurrentTime() {
        SimpleDateFormat sdfDate;//dd/MM/yyyy
        sdfDate = new SimpleDateFormat("  dd/MM/yyyy HH:mm  ");
        Date now = new Date();
        String strDate = sdfDate.format(now);
        return strDate;
    }
    
    class ShutDownThread extends Thread {
        @Override
        public void run() {
            if (socket != null) {
                if (socket.debugFlagIsSet(Constants.instance().DEBUG_STATUS)) {
                    LOGGER.info("ShutdownHook: Shutting down Server Socket");    
                }
                socket.shutdown();
            }
        }
    }
    
    
    class FxSocketListener implements SocketListener {
        private Student currentStudent = null;
        @Override
        synchronized  public void  onMessage(String line) {
            if (line != null && !line.equals("")) {
                line = line + "\n";
                
                //System.out.println(line);
                rcvdMsgsData.add(line);
                String[] parts = line.split("#");
                //System.out.println(line);
                
                //for(String s : parts){System.out.println(s);}
                if(parts[0].equals("Reply")){
                    //System.out.println("Inside if");
                    String name = null;
                    int id = Integer.parseInt(parts[2]);
                    for (int iterator = 0; iterator < studentData.size(); iterator++){ 
                        if(studentData.get(iterator).getId() == id){
                            name = studentData.get(iterator).getName();
                            break;
                        }
                    }
                    
                    MessageGUI ms = MsgObj.get(Integer.parseInt(parts[1]));
                    Message mo = ms.getMessageObject();
//                    try {
//                        Thread.sleep(500);
//                    } catch (InterruptedException ex) {
//                        Logger.getLogger(sampleController.class.getName()).log(Level.SEVERE, null, ex);
//                    }
                    String outTxt = "BroadCastReply#" + parts[1]+ "#" + name + " (" + id
                                + " )#" + parts[3] ;
                    allText.add(outTxt);
                    for (int iterator = 0; iterator < studentData.size(); iterator++) {
                    if(studentData.get(iterator).getConnected().equals("Yes")
                            && studentData.get(iterator).getId() != id ){
                        try {
                         
                        socket.sendMessage("BroadCastReply#" + parts[1]+ "#" + name + " (" + id
                                + " )#" + parts[3] 
                                , studentData.get(iterator).getSock());
                        } catch (IOException ex) {
                            Logger.getLogger(sampleController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }                                     
                    Reply replyObject = new Reply(mo.getId(),name ,parts[3],ms.getReplyList().size());
                    ReplyGUI replyGUIObject = new ReplyGUI(replyObject);
                    replyGUIObject.handleIncomingReply();
                    ms.getReplyList().add(replyGUIObject);
                    ms.getMessageVBox().getChildren().add(replyGUIObject.getReplyVBox());
                    
                }
                else if(parts[0].equals("Edit")){       
                    //System.out.println("Editing Server");
                    MessageGUI ms = MsgObj.get(Integer.parseInt(parts[1]));
                    Message mo = ms.getMessageObject();
                    mo.setTheMessageText(parts[2]);
                    ms.getMessageText().setText(parts[2]);
                    String outTxt = line;
                    allText.add(outTxt);

                    for (int iterator = 0; iterator < studentData.size(); iterator++){                                    
                        if(studentData.get(iterator).getConnected().equals("Yes")){
                            try {
                                //System.out.println("Sent Edit to: ");
                                //System.out.println(studentData.get(iterator).getId());
                                socket.sendMessage(line, studentData.get(iterator).getSock());
                                
                                
                            } catch (IOException ex) {
                                Logger.getLogger(sampleController.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                        
                    }
                   // System.out.println("done");                    
                }
                else if(parts[0].equals("EditReply")){
                    MessageGUI ms = MsgObj.get(Integer.parseInt(parts[1]));
                    Message mo = ms.getMessageObject();
                    ReplyGUI rg = ms.getReplyList().get(Integer.parseInt(parts[2]));
                    rg.getReplyText().setText(parts[3]);
                    for (int iterator = 0; iterator < studentData.size(); iterator++){                                    
                        if(studentData.get(iterator).getConnected().equals("Yes")){
                            try {
                                socket.sendMessage(line, studentData.get(iterator).getSock());
                                String outTxt = line;
                                allText.add(outTxt);
                                
                            } catch (IOException ex) {
                                Logger.getLogger(sampleController.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }                        
                    }                                                           
                } 
                else if(parts[0].equals("login")){
                    currentStudent =  findStudent(Integer.parseInt(parts[1]));
                    if(currentStudent != null){
                        try {
                        socket.sendMessage("loginSuccess#" + currentStudent.getName() + "#"
                                + currentStudent.getDepartment()+ "\n", currentStudent.getSock() );
                        if(allText != null){
                            int currentSize = allText.size();
                            //System.out.println(currentSize);
                        for (int itr = 0; itr < currentSize; itr++) {
                             //System.out.println("queued " + allText.get(itr));
                             socket.sendMessage(allText.get(itr),currentStudent.getSock());
                        }
                        }
                        
                    }catch (IOException ex) {
                        Logger.getLogger(sampleController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    }                                                                   
                }
                else if(parts[0].equals("logout")){
                      findStudent(Integer.parseInt(parts[1]), "No");
                }
                else{
                    String name = null;
                    int id = Integer.parseInt(parts[0]);
                    for (int iterator = 0; iterator < studentData.size(); iterator++){ 
                        if(studentData.get(iterator).getId() == id){
                            name = studentData.get(iterator).getName();
                            break;
                        }
                    }
                    String outTxt = "BroadCastMessage#" + name + " (" + parts[0]
                        + ")" + "#" + parts[1];
                    allText.add(outTxt);
                    
                    for (int iterator = 0; iterator < studentData.size(); iterator++){                                    
                        if(studentData.get(iterator).getConnected().equals("Yes")
                             && studentData.get(iterator).getId() != id){
                        try {                                
                            socket.sendMessage("BroadCastMessage#" + name + " (" + parts[0]
                                + ")" + "#" + parts[1], studentData.get(iterator).getSock());
                        } catch (IOException ex) {
                            Logger.getLogger(sampleController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        }                        
                    }
                    Message m = new Message(MessageCounter++, name, 
                        "Teacher", getCurrentTime() , parts[1]);
                    MessageGUI mg = new MessageGUI(m); 
                    MsgObj.add(mg); 
                    mg.noEdit();                    
                }                
            }
        }
        public   Student findStudent(int ID, String status){
            Student matchedStd = null;            
            for (int iterator = 0; iterator < studentData.size(); iterator++) {
                if(studentData.get(iterator).getId() == ID){
                    matchedStd = studentData.get(iterator);
                    studentData.get(iterator).setConnected(status);
                    try {
                        //System.out.println(studentData.get(iterator).getInput());
                        studentData.get(iterator).getSock().close();
                        
                    } catch (IOException ex) {
                        Logger.getLogger(sampleController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    studentTable.refresh();
                    return matchedStd;
                }
            }            
            return null;
        }       
        
        public Student findStudent(int ID){
            Student matchedStd = null;            
            for (int iterator = 0; iterator < studentData.size(); iterator++) {
                if(studentData.get(iterator).getId() == ID){
                    matchedStd = studentData.get(iterator);
                    studentData.get(iterator).setConnected("Yes");
                    studentData.get(iterator).setSock(port);
                    studentData.get(iterator).setInput(in);
                    studentData.get(iterator).setOutput(out);                    
                    //System.out.println(port);                    
                    studentTable.refresh();                    
                    return matchedStd;
                }
            }            
            return null;
        }       

        @Override
        public void onClosedStatus(boolean isClosed) {
            if (isClosed) {
                isConnected = false;
            } else {
                isConnected = true;
            }
        }        
    }    
    
    public String IPTeller(){
        try {
        String IP;
        Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();

        while (interfaces.hasMoreElements()) {
            NetworkInterface networkInterface = interfaces.nextElement();
            if (!networkInterface.isUp())
                continue;
            
            Enumeration<InetAddress> addresses = networkInterface.getInetAddresses();
            while(addresses.hasMoreElements()) {
                InetAddress addr = addresses.nextElement();
                String key = "Wi-Fi Adapter";
                String hayStack = networkInterface.getDisplayName();
                
                if(hayStack.contains(key) ){
                    IP = addr.getHostAddress();
                    if(IP.length() <= 15){
                        return IP;
                    }                    
                }                                                                   
            }            
        }
        } catch (SocketException ex) {
            Logger.getLogger(sampleController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }        
}

class Delta 
{ 
    double x;
    double y; 
}