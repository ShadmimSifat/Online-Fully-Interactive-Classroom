/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mediaPlayerClassRoomServer;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.net.Socket;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.EventType;
import javafx.scene.control.CheckBox;
import socketfx.*;

/**
 *
 * @author Shayekh Bin Islam
 */
public class Student {
    private SimpleStringProperty name;
    private SimpleIntegerProperty id;
    private SimpleStringProperty department;
    private CheckBox select;
    private SimpleStringProperty connected;
    private Socket sock = null;

   public BufferedReader input = null;
   public BufferedWriter output = null;

    public BufferedWriter getOutput() {
        return output;
    }

    public void setOutput(BufferedWriter output) {
        this.output = output;
    }
   
   

    public BufferedReader getInput() {
        return input;
    }

    public void setInput(BufferedReader input) {
        this.input = input;
    }
   
    
    public Socket getSock() {
        return sock;
    }

    public void setSock(Socket sock) {
        this.sock = sock;
    }
    

    public Student(String name, int id, String department) {
        this.name = new SimpleStringProperty(name);
        this.id = new SimpleIntegerProperty(id);
        this.department = new SimpleStringProperty(department);
        this.select = new CheckBox();
        this.connected = new SimpleStringProperty("No");
        
        //select.addEventHandler(EventType.ROOT, eventHandler);
    }

    public String getConnected() {
        return connected.get();
    }

    public void setConnected(String connected) {
        this.connected.set(connected);
    }
    

    public CheckBox getSelect() {
        return select;
    }

    public void setSelect(CheckBox select) {
        this.select = select;
    }
    

    public String getName() {
        return name.get();
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public int getId() {
        return id.get();
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public String getDepartment() {
        return department.get();
    }

    public void setDepartment(String department) {
        this.department.set(department);
    }
    
    
    
    
    
}
