/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mediaPlayerClassRoomClient;

import java.io.File;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.CheckBox;

/**
 *
 * @author Shayekh Bin Islam
 */
public class Videos {
    File file;
    private SimpleStringProperty directory;
    private SimpleStringProperty title;
    private SimpleLongProperty size;
    private CheckBox select;

    public File getFile() {
        return file;
    }
    

    public Videos(File file) {
        this.file = file;
        this.select = new CheckBox();
                
        this.directory = new SimpleStringProperty(file.getAbsolutePath());
        this.title = new SimpleStringProperty(file.getName());
        this.size = new SimpleLongProperty(file.length());
        
        System.out.println(directory);
    }

    public CheckBox getSelect() {
        return select;
    }
    
    public boolean isSelected(){
        return this.select.isSelected();
    }

    public void setSelect(CheckBox select) {
        this.select = select;
    }
    
    

    public String getDirectory() {
        return directory.get();
    }

    public void setDirectory(String directory) {
        this.directory.set(directory);
    }

    public String getTitle() {
        return title.get();
    }

    public void setTitle(String title) {
        this.title.set(title);
    }

    public long getSize() {
        return size.get();
    }

    public void setSize(long size) {
        this.size.set(size);
    }
    
    
}
