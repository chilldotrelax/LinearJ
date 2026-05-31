package org.andy.linearj.Screen.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;


public class OutputDisplayController {
    @FXML
    private TextArea outputBox;


    public void setOutputBox(String desiredText){
        LocalTime time = LocalTime.now();
        DateTimeFormatter formatted = DateTimeFormatter.ofPattern("HH:mm:ss");
        outputBox.appendText("\n"+ "(" + time.format(formatted) + ")" + " "+ desiredText);
    }

    public void clearOutputBox(){
        //Balls
        outputBox.clear();
    }


}
