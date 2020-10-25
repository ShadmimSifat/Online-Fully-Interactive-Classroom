/*
 * Copyright (c) 2015, Jim Connors
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *   * Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *   * Redistributions in binary form must reproduce the above
 *     copyright notice, this list of conditions and the following
 *     disclaimer in the documentation and/or other materials provided
 *     with the distribution.
 *   * Neither the name of this project nor the names of its
 *     contributors may be used to endorse or promote products derived
 *     from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 * OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package socketfx;

import java.io.*;
import java.lang.invoke.MethodHandles;
import java.net.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import mediaPlayerClassRoomServer.Student;
import mediaPlayerClassRoomServer.sampleController;
import static mediaPlayerClassRoomServer.sampleController.in;
import static mediaPlayerClassRoomServer.sampleController.out;
import static mediaPlayerClassRoomServer.sampleController.studentData;
import static mediaPlayerClassRoomServer.sampleController.studentTablecp;
public abstract class GenericSocket implements SocketListener {
    
    
    private final static Logger LOGGER =
            Logger.getLogger(MethodHandles.lookup().lookupClass().getName());

    public int port;
    public Socket socketConnection = null;
    private BufferedWriter output = null;
    //private BufferedReader input = null;
    private boolean ready = false;
    public static Thread socketReaderThread;
    private Thread setupThread;
    private int debugFlags;

    public boolean debugFlagIsSet(int flag) {
        return ((flag & debugFlags) != 0);
    }

    public void setDebugFlags(int flags) {
        debugFlags = flags;
    }

    public Socket getSocketConnection() {
        return socketConnection;
    }

    public int getDebugFlags() {
        return debugFlags;
    }

    public void clearDebugFlags() {
        debugFlags = Constants.instance().
                DEBUG_NONE;
    }
    
    public void connect(Socket connectionSocket) {
        socketConnection = connectionSocket;
        try {

            setupThread = new SetupThread();
            setupThread.start();

            socketReaderThread = new SocketReaderThread(connectionSocket);
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

    private void close() {
        try {
            if (socketConnection != null && !socketConnection.isClosed()) {
                socketConnection.close();
            }
            /*
             * closeAdditionalSockets() has to be implemented in a subclass.
             * In some cases nothing may be requied (null method), but for
             * others (e.g. SocketServer), the method can be used to
             * close additional sockets.
             */
            closeAdditionalSockets();
            if (debugFlagIsSet(Constants.instance().DEBUG_STATUS)) {
                LOGGER.info("Connection closed");
            }
            /*
             * The onClosedStatus() method has to be implemented by
             * a sublclass.  If used in conjunction with JavaFX,
             * use Platform.runLater() to force this method to run
             * on the main thread.
             */
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

    public void sendMessage(String msg) {
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
    
    public void sendMessage(String msg,Socket port) throws IOException {
        BufferedWriter output = new BufferedWriter(new OutputStreamWriter(
                            port.getOutputStream()));
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
                    
                    output = new BufferedWriter(new OutputStreamWriter(
                            socketConnection.getOutputStream()));
                    output.flush();
                    out = output;
                }
                /*
                 * Notify SocketReaderThread that it can now start.
                 */
                notifyReady();
            } catch (IOException e) {
                if (debugFlagIsSet(Constants.instance().DEBUG_EXCEPTIONS)) {
                    LOGGER.info(e.getMessage());
                }
                /*
                 * This will notify the SocketReaderThread that it should exit.
                 */
                notifyReady();
            }
        }
    }

    class SocketReaderThread extends Thread {
           Socket socketConnection = null;
           public BufferedReader input = null;

        public BufferedReader getInput() {
            return input;
        }
           
        public SocketReaderThread(Socket socketConnection) {
            this.socketConnection = socketConnection;
        }
        
        
        @Override
        public void run() {
               try {
                   input = new BufferedReader(new InputStreamReader(
                           this.socketConnection.getInputStream()));
                   in = input;
                   //System.out.println(in);
                   
               } catch (IOException ex) {
                   Logger.getLogger(GenericSocket.class.getName()).log(Level.SEVERE, null, ex);
               }

            waitForReady();

            if (this.socketConnection != null && this.socketConnection.isConnected()) {
                onClosedStatus(false) ;
            }

            try {
                if (input != null) {
                   
                    String line;
                    while (input != null) {
                        
                        line = input.readLine();
                        
                        String[] parts = line.split("#");
                        if(parts[0].equals("logout")){
                            findStudent(Integer.parseInt(parts[1]), "No");
                            break;
                        }
                        
                        if (debugFlagIsSet(Constants.instance().DEBUG_RECV)) {
                            String logMsg = "recv> " + line;
                            LOGGER.info(logMsg);
                        }

                        if(input == null){
                            
                            System.out.println("input is null now");break;
                        }
                        if(line != null)
                            onMessage(line);
                    }
                }
                
            } catch (IOException e) {
                System.out.println("input exception");
                   try {
                       input.close();
                   } catch (IOException ex) {
                       Logger.getLogger(GenericSocket.class.getName()).log(Level.SEVERE, null, ex);
                   }
            } finally {
                
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
                        //studentData.get(iterator).getThread().interrupt();
                        //System.out.println(studentData.get(iterator).getInput());
                        
                        
                        studentData.get(iterator).getInput().close();
                        studentData.get(iterator).getOutput().close();
                        studentData.get(iterator).getSock().close();
                        
                                } catch (IOException ex) {
                        Logger.getLogger(sampleController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    //System.out.println(port);
                    studentTablecp.refresh();
                    //studentData.get(iterator).setSock(null);
                    
                    return matchedStd;
                }
            }            
            return null;
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
