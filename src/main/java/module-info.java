module org.andy.linearj {
    requires javafx.controls;
    requires javafx.fxml;
    requires ejml.core;
    requires ejml.simple;
    requires java.sql;

    exports org.andy.linearj.Screen.controllers;
    opens org.andy.linearj.Screen.controllers to javafx.fxml;

    exports org.andy.linearj.Screen.toplevel;
    opens org.andy.linearj.Screen.toplevel to javafx.fxml;

    exports org.andy.linearj.Circuit;
}
