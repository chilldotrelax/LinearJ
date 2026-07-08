package org.andy.linearj.Screen.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;

import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class OutputUnitController {
    @FXML
    private Button clearBtn;
    @FXML
    private TextArea outputBox;

    public void setOutputBox(String desiredText){
        LocalTime time = LocalTime.now(ZoneId.systemDefault());
        DateTimeFormatter formatted = DateTimeFormatter.ofPattern("HH:mm:ss");
        outputBox.appendText("\n"+ "(" + time.format(formatted) + ")" + " "+ desiredText);
        clearBtn.setDisable(false);
    }

    @FXML
    private void clearOutputBox(){
        clearBtn.setDisable(true);
        outputBox.clear();
    }


}
