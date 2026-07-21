package org.andy.linearj.Screen.controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;

public class MenuBarUnitController {
    @FXML
    private void triggerAboutMenu() {
        PopupWindow aboutWindow = new AboutWindow();
        aboutWindow.openWindow();
    }

    @FXML
    private void quitApp() {
        Platform.exit();
    }

}