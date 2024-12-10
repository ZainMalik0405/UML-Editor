package com.example.umleditor;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Main application class for the UML Editor.
 * This class extends the JavaFX Application class and serves as the entry point for the application.
 */
public class UMLEditorApplication extends Application {

    /**
     * Starts the JavaFX application.
     * Loads the FXML layout and applies the stylesheet.
     *
     * @param stage the primary stage for this application
     * @throws IOException if the FXML file cannot be loaded
     */
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(UMLEditorApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 800, 600);

        // Manually apply the stylesheet
        scene.getStylesheets().add(UMLEditorApplication.class.getResource("styles.css").toExternalForm());

        stage.setTitle("UML Editor");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * The main method for launching the application.
     *
     * @param args the command-line arguments
     */
    public static void main(String[] args) {
        launch();
    }
}
