package org.andy.linearj.Screen.toplevel;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class LinearJParent extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(LinearJParent.class.getResource("/org/andy/linearj/ParentWindow.fxml"));

        Scene scene = new Scene(fxmlLoader.load(), 912 , 517);
        stage.setTitle("LinearJ -- Matrix Solver & Circuit Solver Utility");
        stage.setScene(scene);
        stage.show();

    }
}
