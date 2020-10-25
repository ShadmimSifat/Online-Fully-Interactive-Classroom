package mediaPlayerClassRoomClient;

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
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
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
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.Tooltip;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.WindowEvent;
import static mediaPlayerClassRoomClient.sampleController.ap;
import static mediaPlayerClassRoomClient.sampleController.getCurrentTime;
import static mediaPlayerClassRoomClient.sampleController.msg;
import static mediaPlayerClassRoomClient.sampleController.socket;
import static mediaPlayerClassRoomClient.Sifat_030119_mp4Support_basicGui.primaryStage;
import org.fredy.jsrt.api.SRT;
import org.fredy.jsrt.api.SRTInfo;
import org.fredy.jsrt.api.SRTReader;
import socketfx.Constants;
import socketfx.FxSocketClient;
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
    private TextField stdName; 
    @FXML
    private TextField stdID; 
    @FXML
    private TextField stdDept; 
    @FXML
    ToggleButton editProfile;     
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
    private JFXSlider seekSlider = new JFXSlider();
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
    private Button playDownloaded; 
    @FXML
    private ToggleButton showConsoleButton;     
    @FXML
    private HBox rootHB;
    @FXML
    private VBox chatBox;    
    @FXML
    private TableColumn titleColumn;
    @FXML
    private TableColumn videoSelectColumn;    
    @FXML
    private HBox teacherConsole;
    @FXML
    private ImageView profileImage;
    @FXML
    private Label addProfilePicture;
    
    public static TextArea msg = new TextArea();    
    public static VBox ap = new VBox();
    private final static Logger LOGGER
        = Logger.getLogger(MethodHandles.lookup().lookupClass().getName());
    private ObservableList<String> rcvdMsgsData;
    private ObservableList<String> sentMsgsData;    
    private ArrayList<MessageGUI> MsgObj = new ArrayList<>();
    private ArrayList<ReplyGUI> RplyObj = new ArrayList<>();
    private boolean connected;
    private volatile boolean isAutoConnected;
    private static final int DEFAULT_RETRY_INTERVAL = 2000; // in milliseconds
    public static FxSocketClient socket;
    public static Student student;// = new Student("Jane", 2705001, "CSE");
    String teacherIP = null;
    ObservableList<Student> studentData = FXCollections.observableArrayList();
    public static ObservableList<Videos> videoData = FXCollections.observableArrayList();
    private boolean isPaused = false;
    private Iterator<String> iterator = null;
    List<String> list = new ArrayList<>();     
    private int screenHeight;
    private int screenWidth;    
    private ColorAdjust colorAdjust = new ColorAdjust();
    
    public enum ConnectionDisplayState {
        DISCONNECTED, ATTEMPTING, CONNECTED, AUTOCONNECTED, AUTOATTEMPTING
    }    

    private synchronized void waitForDisconnect() {
        while (connected) {
            try {
                wait();
            } catch (InterruptedException e) {
            }
        }
    }

    private synchronized void notifyDisconnected() {
        connected = false;
        notifyAll();
    }

    private synchronized void setIsConnected(boolean connected) {
        this.connected = connected;
    }
    
    private synchronized boolean isConnected() {
        return (connected);
    }    

    private void connect() {
        socket = new FxSocketClient(new FxSocketListener(), teacherIP,
            Integer.valueOf("6788"), Constants.instance().DEBUG_NONE);
        socket.connect();   
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent t) {                                       
                FileWriter writer = null;
                try {
                    socket.sendMessage("logout#" + student.getId()+  "#\n");
                    //socket.getSocketConnection().close();
                    socket.shutdown();
                    writer = new FileWriter("output.txt");
                    for (int itr = 0; itr < videoData.size(); itr++) {
                        writer.write(videoData.get(itr).getFile().getAbsolutePath() + "#");
                    }
                } catch (IOException ex) {
                    Logger.getLogger(sampleController.class.getName()).log(Level.SEVERE, null, ex);
                } finally {
                    try {
                        writer.close();
                    } catch (IOException ex) {
                        Logger.getLogger(sampleController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        });        
    }

    private void autoConnect() {
        new Thread() {
            @Override
            public void run() {
                while (isAutoConnected) {
                    if (!isConnected()) {
                        socket = new FxSocketClient(new FxSocketListener(), teacherIP,
                            Integer.valueOf("6788"), Constants.instance().DEBUG_NONE);
                        socket.connect();
                    }
                    waitForDisconnect();
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException ex) {
                    }
                }
            }
        }.start();
    }
    
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        mediaView.setEffect(colorAdjust);
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
        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        screenWidth = gd.getDisplayMode().getWidth();
        screenHeight = gd.getDisplayMode().getHeight();
        
        String videoDat = "";
        FileReader fr = null;
        try {
             fr=new FileReader("output.txt");    
            int i;    
            while((i=fr.read())!=-1)    {
                videoDat = videoDat + (char)i;
            }
            
           
            
        } catch (FileNotFoundException ex) {
            Logger.getLogger(sampleController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(sampleController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        String parts[] = videoDat.split("#");
        if(videoDat.length() >= 3)
            for (int ite = 0; ite < parts.length; ite+=1) {
                videoData.add(new Videos(new File(parts[ite])));            
            }
        try {  
            fr.close();
        } catch (IOException ex) {
            Logger.getLogger(sampleController.class.getName()).log(Level.SEVERE, null, ex);
        }                   

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
        setIsConnected(false);
        isAutoConnected = false;
        sentMsgsData = FXCollections.observableArrayList();
        rcvdMsgsData = FXCollections.observableArrayList();
        Runtime.getRuntime().addShutdownHook(new ShutDownThread());        
        message.setOnKeyPressed(new EventHandler<KeyEvent>() {
        @Override
        public void handle(KeyEvent keyEvent) {
            if (keyEvent.getCode() == KeyCode.ENTER)  {
                sendUtility();
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
        stdName.setEditable(false);
        stdDept.setEditable(false);
        editProfile.setOnAction(e->{           
            if(editProfile.isSelected()){
                stdID.setEditable(true);
                editProfile.setText("Save");
            }
            else{               
                 stdID.setEditable(false);               
                editProfile.setText("Edit Profile");
            }          
        });
    }
    
    @FXML
    public void addImage(){
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter filter;
        filter = new FileChooser.ExtensionFilter("Select Image(*.png)", new String[]{"*.png"});
        fileChooser.getExtensionFilters().add(filter);
        File file = fileChooser.showOpenDialog(null);
        String filePath = file.toURI().toString();
        if(filePath != null){
            profileImage.setImage(new Image(filePath));
        }
        
    }    
    
    public static File subtitle = null;//"./src/in.srt";
    public static Media md = null;
    @FXML
    public void subLoad(){
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter filter;
        filter = new FileChooser.ExtensionFilter("Select Subtitle(*.srt)", new String[]{"*.srt"});
        fileChooser.getExtensionFilters().add(filter);
        File file = fileChooser.showOpenDialog(null);
        subtitle = new File(file.getAbsolutePath());
        //System.out.println(subtitle);
        if(subtitle != null && md != null){
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
    @FXML
    private Label closedCaption;
    @FXML
    private MenuItem subClear;
    @FXML
    private Label nowPlaying;
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
            mediaPlayer = new MediaPlayer(media);
            mediaView.setMediaPlayer(mediaPlayer);    
            md = media;

            if(subtitle != null){
                SRTInfo info = SRTReader.read(subtitle);
            //System.out.println("thread started .............");
            for (SRT ss : info) {
                String t = "";                 
                 for (String line : ss.text) {
                    t =  t + "    " + line;
                }
                 
                 
                media.getMarkers().put(t, Duration.millis(ss.startTime.getHours()*3600000 
                    + ss.startTime.getSeconds()*1000 + ss.startTime.getMinutes() * 60000)); 
            }          
            mediaPlayer.setOnMarker((MediaMarkerEvent event) ->
                closedCaption.setText(event.getMarker().getKey())
            );            
            }            
            mediaView.fitWidthProperty().bind(stackPane.widthProperty());
            mediaView.fitHeightProperty().bind(stackPane.heightProperty());
            //DoubleProperty width = mediaView.fitWidthProperty();
            //DoubleProperty height = mediaView.fitHeightProperty();
            //width.bind(Bindings.selectDouble(mediaView.sceneProperty(), "width"));
            //height.bind(Bindings.selectDouble(mediaView.sceneProperty(), "height"));
            //mediaView.setPreserveRatio(true);

            volumeSlider.setValue(mediaPlayer.getVolume() * 50);
            volumeSlider.valueProperty().addListener(e ->
                mediaPlayer.setVolume(volumeSlider.getValue() / 100));            
            //volumeSlider.valueProperty().bindBidirectional(mediaPlayer.volumeProperty());
            try {
                if(seekSlider !=null  || mediaPlayer!= null)
                    seekSlider.maxProperty().bind(Bindings.createDoubleBinding(
                () -> mediaPlayer.getTotalDuration().toSeconds(),
                mediaPlayer.totalDurationProperty()));
            } catch (Exception e) {
            }
                
            try {
                if(totalTimeLabel!= null || mediaPlayer!= null)
                    totalTimeLabel.textProperty().bind(
                        Bindings.createStringBinding(() -> {
                        Duration totalDuration = mediaPlayer.getTotalDuration();                    
                        return String.format("%02d : %02d : %02.0f",
                            (int) totalDuration.toHours(),
                            (int) totalDuration.toMinutes() % 60,
                            (totalDuration.toSeconds() % 3600) % 60);
                        },mediaPlayer.currentTimeProperty()));
            } catch (Exception e) {
            }
            
            try {
                currentTimeLabel.textProperty().bind(
                Bindings.createStringBinding(() -> {
                    Duration time = mediaPlayer.getCurrentTime();
                    return String.format("%02d : %02d : %02.0f",
                            (int) time.toHours(),
                            (int) time.toMinutes() % 60,
                            (time.toSeconds() % 3600) % 60);
                }, mediaPlayer.currentTimeProperty()));            
            
                mediaPlayer.currentTimeProperty().addListener((ObservableValue<? extends Duration> 
                    observable, Duration oldValue, Duration newValue) -> {
                    seekSlider.setValue(newValue.toSeconds());
                    });
            } catch (Exception e) {
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

            seekSlider.setOnMousePressed((MouseEvent event) -> {
                mediaPlayer.seek(Duration.seconds(seekSlider.getValue()));
            });
            
            seekSlider.setOnMouseDragged((MouseEvent event) -> {
                mediaPlayer.seek(Duration.seconds(seekSlider.getValue()));
            });
            mediaPlayer.play();                       
            primaryStage.setFullScreenExitHint("");          

            subClear.setOnAction(value->{
            media.getMarkers().clear();
            closedCaption.setText("");           
       });
           
            
            mediaPlayer.setOnEndOfMedia(() -> {
              mediaPlayer.stop();
              isPaused = true;
              closedCaption.setText("");
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
        String  selectDialog = "Select Media(*.mp4, *.wav,  *.m4a, *.m4v, *.mp3)";
        String[] ext =  new String[]{"*.mp4","*.mp3", "*.wav", "*.m4a", "*.m4v"};
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter filter;
        filter = new FileChooser.ExtensionFilter(selectDialog, ext);
        fileChooser.getExtensionFilters().add(filter);
        List<File> selectedFiles = fileChooser.showOpenMultipleDialog(null);
        //list.clear();
        if(mediaPlayer!=null)
            mediaPlayer.stop();
        if(selectedFiles!=null)
        {
            for (int i=0;i<selectedFiles.size();i++)
            {
                list.add(selectedFiles.get(i).toURI().toString());
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
            volumeButton.setTooltip(new Tooltip("Unmute"));
            }
            else{ 
                mediaPlayer.setMute(false);
                Image image = new Image(getClass().getResourceAsStream("/images_and_data/volume.png"));
                volumeButton.setGraphic(new ImageView(image));
                volumeButton.setTooltip(new Tooltip("Mute"));
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
    
    /**
     *
     */
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
    public void playDownloadedHandle(ActionEvent event){
        if(list!=null)
            list.clear();
        if(mediaPlayer!=null)
            mediaPlayer.stop();
        String url;
          for (int j = 0; j < videoData.size(); j++) {
              if(videoData.get(j).isSelected()){
                  //System.out.println(videoData.get(j).getTitle());
                  url = videoData.get(j).getFile().toURI().toString();
                  list.add(url);                     
              }
                       
        }
          iterator = list.iterator();
          try {
             if(iterator != null)
                setPlayer(iterator.next());   
            } catch (Exception e) {
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
            } catch (Exception e) {
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
    
    
/*    
    @FXML
    public void (ActionEvent event){
        
    }
*/
    @FXML
    public void controlTeacherConsole(ActionEvent event){
        
    }
    

    @FXML
    private TextField ipInput;
    
    @FXML
    public void clientHandle(ActionEvent event) throws InterruptedException{
        teacherIP = ipInput.getText();
        connect(); 
        Thread.sleep(500);
        student = new Student("",Integer.parseInt(stdID.getText()),"");
        (new File("E:\\Student_Video\\")).mkdir();
        (new File("E:\\Student_Video\\" + student.getId())).mkdir();
        
        
        //student = new Student(stdName.getText(), Integer.parseInt(stdID.getText()), stdDept.getText());
        socket.sendMessage("login#" +  student.getId()+ "#" + "hi\n");
        
    }

    public void sendUtility(){
        String receivedMessage = new String(student.getId() + "#"  +  message.getText() + "#\n");
                try {
                    Platform.runLater( new Runnable() {
                        @Override
                        public void run() {
                            message.positionCaret( 0 );
                            message.clear();
                        }
                    });
                } catch (Exception e) {
                }

                if (!receivedMessage.equals("\n")) {
                    socket.sendMessage(receivedMessage);
                    sentMsgsData.add(message.getText());
                    
                    Message m = new Message(MessageCounter++, student.getName(), 
                            "Teacher", getCurrentTime() , message.getText());
                    MessageGUI mg = new MessageGUI(m); 
                    MsgObj.add(mg);
                }
    }
    
    private static int MessageCounter = 0;
    public void sendHandle(ActionEvent event){
        sendUtility();     
    }
    
    
    public static String getCurrentTime() {
        SimpleDateFormat sdfDate = new SimpleDateFormat("  dd/MM/yyyy HH:mm  ");//dd/MM/yyyy
        Date now = new Date();
        String strDate = sdfDate.format(now);
        return strDate;
    }
    @FXML
    private Button send;
    
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
        @Override 
        public void onMessage(String line) {
            if (line != null && !line.equals("")) {
                line = line + "\n";
                //System.out.println(line);
                rcvdMsgsData.add(line);
                String[] parts = line.split("#");
                //for(String s : parts){System.out.println(s);}
                
                if(parts[0].equals("Reply")){
                    //System.out.println("Inside if");
                    MessageGUI ms = MsgObj.get(Integer.parseInt(parts[1]));
                    Message mo = ms.getMessageObject();
                    
                    Reply replyObject = new Reply(mo.getId(),"Teacher",parts[2],ms.getReplyList().size());
                    ReplyGUI replyGUIObject = new ReplyGUI(replyObject);
                    
                    replyGUIObject.handleIncomingReply();
                    
                    ms.getReplyList().add(replyGUIObject);
                    ms.getMessageVBox().getChildren().add(replyGUIObject.getReplyVBox());
                }
                else if(parts[0].equals("BroadCastReply")){
                                        
                    
                    MessageGUI ms = MsgObj.get(Integer.parseInt(parts[1]));
                    Message mo = ms.getMessageObject();
                    
                    Reply replyObject = new Reply(mo.getId(),parts[2],parts[3],ms.getReplyList().size());
                    ReplyGUI replyGUIObject = new ReplyGUI(replyObject);
                    
                    replyGUIObject.handleIncomingReply();
                    
                    ms.getReplyList().add(replyGUIObject);
                    ms.getMessageVBox().getChildren().add(replyGUIObject.getReplyVBox());
                }
                else if(parts[0].equals("Edit")){
                   // System.out.println("Editing");
                    MessageGUI ms = MsgObj.get(Integer.parseInt(parts[1]));
                    Message mo = ms.getMessageObject();
                    mo.setTheMessageText(parts[2]);
                    ms.getMessageText().setText(parts[2]);
                    //System.out.println(parts[2]);
                }
                else if(parts[0].equals("EditReply")){
                    MessageGUI ms = MsgObj.get(Integer.parseInt(parts[1]));
                    Message mo = ms.getMessageObject();
                    ReplyGUI rg = ms.getReplyList().get(Integer.parseInt(parts[2]));
                    rg.getReplyText().setText(parts[3]);
                    
                }
                else if(parts[0].equals("BroadCastMessage")){
                    
                    Message m = new Message(MessageCounter++, parts[1], 
                            Integer.toString(student.getId()), getCurrentTime() , parts[2]);
                    MessageGUI mg = new MessageGUI(m); 
                    MsgObj.add(mg);
                    mg.noEdit();
                }
                else if(parts[0].equals("loginSuccess")){
                    student.setName(parts[1]);
                    student.setDepartment(parts[2]);
                    stdName.setText(student.getName());
                    stdDept.setText(student.getDepartment());
                    message.setDisable(false);
                    send.setDisable(false);
                }
                
                else{
                    //System.out.println(line);
                    Message m = new Message(MessageCounter++, "Teacher", 
                            Integer.toString(student.getId()), getCurrentTime() , line);
                    MessageGUI mg = new MessageGUI(m); 
                    MsgObj.add(mg);
                    mg.noEdit();
                }
            }
        }

        @Override
        public void onClosedStatus(boolean isClosed) {
            if (isClosed) {
                notifyDisconnected();
                if (isAutoConnected) {
                   // displayState(ConnectionDisplayState.AUTOATTEMPTING);
                } else {
                   // displayState(ConnectionDisplayState.DISCONNECTED);
                }
            } else {
                setIsConnected(true);
                if (isAutoConnected) {
                    //displayState(ConnectionDisplayState.AUTOCONNECTED);
                } else {
                    //displayState(ConnectionDisplayState.CONNECTED);
                }
            }
        }
     
    }
        
}

class Delta 
{ 
    double x;
    double y; 
} 