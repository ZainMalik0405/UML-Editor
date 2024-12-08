module com.example.umleditor {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;
    requires javafx.swing;
    requires org.junit.jupiter.api;
    requires mockito.all;

    opens com.example.umleditor to javafx.fxml;
    exports com.example.umleditor;
    exports com.example.umleditor.ui;
    opens com.example.umleditor.ui to javafx.fxml;
}