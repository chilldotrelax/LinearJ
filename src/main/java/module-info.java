//Modules = grouping of one or more packages.
//You use it to declare dependencies, services, etc.


/*
Modules best practices
    1) Ignore modules
Why?
    1) Not widely adopted
    2) No evidence that will significantly change
    3) Vital for JDK evolution, not for individual apps
    4) Incomplete enforcement of module boundaries.
 */


module org.andy.linearj {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.compiler;
    requires java.desktop;
    requires ejml.simple;
    requires ejml.ddense;
    requires ejml.core;
    requires java.sql;

    exports org.andy.linearj.Screen.controllers;
    opens org.andy.linearj.Screen.controllers to javafx.fxml;

    exports org.andy.linearj.Screen.toplevel;
    opens org.andy.linearj.Screen.toplevel to javafx.fxml;

    exports org.andy.linearj.Circuit;
}