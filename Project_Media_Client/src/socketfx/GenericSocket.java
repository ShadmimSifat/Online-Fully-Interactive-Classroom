package socketfx;

import java.io.*;
import java.lang.invoke.MethodHandles;
import java.net.*;
import java.nio.CharBuffer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableView;
import mediaPlayerClassRoomClient.Videos;
import static mediaPlayerClassRoomClient.sampleController.student;
import static mediaPlayerClassRoomClient.sampleController.videoData;

public abstract class GenericSocket implements SocketListener {
    
    private final static Logger LOGGER =
            Logger.getLogger(MethodHandles.lookup().lookupClass().getName());

    public int port;
    public Socket socketConnection = null;
    private BufferedWriter output = null;
    private BufferedReader input = null;
    private boolean ready = false;
    public Thread socketReaderThread;
    private Thread setupThread;
    private int debugFlags;
    
    private boolean fileReading = false;
    private String strRecv = ""; 
    private String fileName = "";
    //ObservableList<Videos> videoData = FXCollections.observableArrayList();
    private TableView videoList = null;

    public boolean isFileReading() {
        return fileReading;
    }
    

    
    

    public Thread getSocketReaderThread() {
        return socketReaderThread;
    }
    
    
    public boolean debugFlagIsSet(int flag) {
        return ((flag & debugFlags) != 0);
    }

    public void setDebugFlags(int flags) {
        debugFlags = flags;
    }
   
    public int getDebugFlags() {
        return debugFlags;
    }

    public Socket getSocketConnection() {
        return socketConnection;
    }
    

    public void clearDebugFlags() {
        debugFlags = Constants.instance().
                DEBUG_NONE;
    }

    public void connect() {
        try {
  
            setupThread = new SetupThread();
            setupThread.start();
 
            socketReaderThread = new SocketReaderThread();
            socketReaderThread.start();
        } catch (Exception e) {
            if (debugFlagIsSet(Constants.instance().DEBUG_EXCEPTIONS)) {
                LOGGER.info(e.getMessage());
            }
        }  
    }

    public void shutdown() {
        close();
    }


//    public void updateList(ObservableList<Videos> videoData){
//        this.videoData = videoData;
////        this.videoList = videoList;
//    }
    
    private void close() {
        try {
            if (socketConnection != null && !socketConnection.isClosed()) {
                input.close();
                output.close();
                socketConnection.close();
                
            }
         
            closeAdditionalSockets();
            if (debugFlagIsSet(Constants.instance().DEBUG_STATUS)) {
                LOGGER.info("Connection closed");
            }
        
            onClosedStatus(true);
        } catch (IOException e) {
            if (debugFlagIsSet(Constants.instance().DEBUG_EXCEPTIONS)) {
                LOGGER.info(e.getMessage());
            }
        }
    }

 
    protected abstract void initSocketConnection() throws SocketException;

  
    protected abstract void closeAdditionalSockets();

    private synchronized void waitForReady() {
        while (!ready) {
            try {
                wait();
            } catch (InterruptedException e) {
            }
        }
    }

 
    private synchronized void notifyReady() {
        ready = true;
        notifyAll();
    }

 
    public synchronized  void sendMessage(String msg) {
        try {
            output.write(msg, 0, msg.length());
            output.newLine();
            output.flush();
            if (debugFlagIsSet(Constants.instance().DEBUG_SEND)) {
                String logMsg = "send> " + msg;
                LOGGER.info(logMsg);
            }
        } catch (IOException e) {
            if (debugFlagIsSet(Constants.instance().DEBUG_EXCEPTIONS)) {
                LOGGER.info(e.getMessage());
            }
        }
    }

    class SetupThread extends Thread {

        @Override
        public void run() {
            try {
                initSocketConnection();
                if (socketConnection != null && !socketConnection.isClosed()) {
                    /*
                     * Get input and output streams
                     */
                    input = new BufferedReader(new InputStreamReader(
                            socketConnection.getInputStream()));
                    output = new BufferedWriter(new OutputStreamWriter(
                            socketConnection.getOutputStream()));
                    output.flush();
                }
       
                notifyReady();
            } catch (IOException e) {
                if (debugFlagIsSet(Constants.instance().DEBUG_EXCEPTIONS)) {
                    LOGGER.info(e.getMessage());
                }
           
                notifyReady();
            }
        }
    }

    class SocketReaderThread extends Thread {

        @Override
        public void run() {
        
            waitForReady();
         
            if (socketConnection != null && socketConnection.isConnected()) {
                onClosedStatus(false) ;
            }
           
            
            try {
                if (input != null) {
                    String line;
                    //System.out.println(fileReading);
                                        
                    while ((line = input.readLine()) != null ) {
                        //System.out.println("while big");
                        if (debugFlagIsSet(Constants.instance().DEBUG_RECV)) {
                            String logMsg = "recv> " + line;
                            LOGGER.info(logMsg);
                        }
                  
                        String[] parts = line.split("#");


                        if (parts[0].equals("Download")) {

                            fileReading = true;
                            strRecv = parts[1];
                            fileName = "E:\\Student_Video\\" + student.getId() + "\\" + parts[2];
                            
                            //input.close();
                            //System.out.println("if download");
                            if (fileReading) {
                                try
                                {
                                    long filesize=Integer.parseInt(strRecv);	
                                    //System.out.println(filesize);
                                    //the size of the receiving file
                                    byte[] contents = new byte[10000];
                                    FileOutputStream fos = new FileOutputStream(fileName);
                                    BufferedOutputStream bos = new BufferedOutputStream(fos);

                                    InputStream is = socketConnection.getInputStream();
                                    try {
                                    Thread.sleep(1000);
                                } catch (InterruptedException ex) {
                                    Logger.getLogger(GenericSocket.class.getName()).log(Level.SEVERE, null, ex);
                                }

                                    int bytesRead = 0; 
                                    int total = 0;
                                    //int i = 0;//how many bytes read

                                    while(total != filesize)	//loop is continued until received byte=totalfilesize
                                    {
                                        //System.out.println(i++);
                                        bytesRead = is.read(contents);
                                        total += bytesRead;
                                        bos.write(contents, 0, bytesRead);
                                        //System.out.println(total);
                                    }
                                    //System.out.println("flush");
                                    bos.flush();
                                    //System.out.println("bos close");
                                    
                                    bos.close();
                                    //System.out.println("fos close");
                                    fos.close();
                                    fileReading = false;
                                    
                                   // System.out.println("Received Successfully!");
                                    
                                 }
                                    catch(Exception e)
                                    {
                                            //System.err.println("Could not transfer file.");
                                    }
                                File f = new File(fileName);
                                Videos tutorial = new Videos(f);
                                
                                videoData.add(tutorial);
                                
                                }
                        } else {
                            onMessage(line);
                        }
                    }
                }
            } catch (IOException e) {
                if (debugFlagIsSet(Constants.instance().DEBUG_EXCEPTIONS)) {
                    LOGGER.info(e.getMessage());
                }
            } finally {
                close();
            }
        }
    }
    
    public GenericSocket() {
        this(Constants.instance().DEFAULT_PORT,
                Constants.instance().DEBUG_NONE);
    }

    public GenericSocket(int port) {
        this(port, Constants.instance().DEBUG_NONE);
    }

    public GenericSocket(int port, int debugFlags) {
        this.port = port;
        this.debugFlags = debugFlags;
    }
}
