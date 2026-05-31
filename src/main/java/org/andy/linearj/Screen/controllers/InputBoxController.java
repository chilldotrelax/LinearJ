package org.andy.linearj.Screen.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import org.andy.linearj.Screen.misc.exception.EmptyInputException;

public class InputBoxController {
    @FXML
    private TextArea inputBox;

    @FXML
    public String getText(){
        if (inputBox.getText().isBlank()){
            throw new EmptyInputException("Empty input.");
        }
        else{
            return inputBox.getText();
        }
    }

    @FXML
    public void setText(String input){
        inputBox.setText(input);

    }
    @FXML
    public void clearInputBox(){
        inputBox.clear();
    }


}
