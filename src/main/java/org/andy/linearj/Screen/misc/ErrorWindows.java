package org.andy.linearj.Screen.misc;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

public class ErrorWindows {
    public static void displayInformative(String dialog){
        Alert informativeGenericMSG = new Alert(Alert.AlertType.INFORMATION, dialog);
        informativeGenericMSG.showAndWait().filter(response -> response == ButtonType.OK);
    }

    public static void displayError(String dialog){
        Alert errorGenericMSG = new Alert(Alert.AlertType.ERROR, dialog);
        errorGenericMSG.showAndWait().filter(response -> response == ButtonType.OK);
    }


}
