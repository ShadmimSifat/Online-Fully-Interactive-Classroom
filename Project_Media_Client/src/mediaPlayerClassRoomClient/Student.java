/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mediaPlayerClassRoomClient;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.EventType;
import javafx.scene.control.CheckBox;

/**
 *
 * @author Shayekh Bin Islam
 */
public class Student {
    private SimpleStringProperty name;
    private SimpleIntegerProperty id;
    private SimpleStringProperty department;
    private CheckBox select;

    public Student(String name, int id, String department) {
        this.name = new SimpleStringProperty(name);
        this.id = new SimpleIntegerProperty(id);
        this.department = new SimpleStringProperty(department);
        this.select = new CheckBox();
        
        //select.addEventHandler(EventType.ROOT, eventHandler);
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
