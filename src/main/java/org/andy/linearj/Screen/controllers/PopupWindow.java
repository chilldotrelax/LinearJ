package org.andy.linearj.Screen.controllers;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

abstract class PopupWindow {
    private static final int WINDOW_WIDTH = 350;
    private static final int WINDOW_HEIGHT = 350;

    private final String resourcePath;;
    private final String windowTitle;


    PopupWindow(String resourcePath, String windowTitle){
        this.resourcePath = resourcePath;
        this.windowTitle = windowTitle;
    }

    public void openWindow() throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(this.resourcePath)));

        Stage stageRoot = new Stage();
        stageRoot.setTitle(this.windowTitle);
        stageRoot.setScene(new Scene(root));
        stageRoot.show();

    }

}
