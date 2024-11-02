module org.exemple.biblioteca {
    requires javafx.controls;
    requires javafx.fxml;

    requires java.sql;
    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;
    requires java.desktop;

    opens org.exemple.biblioteca.controller to javafx.fxml;
    exports org.exemple.biblioteca.controller;

    opens org.exemple.biblioteca.model to javafx.fxml;
    exports org.exemple.biblioteca.model;

    opens org.exemple.biblioteca.main to javafx.fxml;
    exports org.exemple.biblioteca.main;

    opens org.exemple.biblioteca.dao to javafx.fxml;
    exports org.exemple.biblioteca.dao;
}