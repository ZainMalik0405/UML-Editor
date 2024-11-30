package com.example.umleditor.ui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.StackPane;

public class MainController {

    @FXML
    private Button useCaseButton;

    @FXML
    private Button classDiagramButton;

    @FXML
    private ListView<String> useCaseComponentList;

    @FXML
    private ListView<String> classComponentList;

    @FXML
    private StackPane componentStackPane; // The container that holds both ListViews

    @FXML
    public void initialize() {
        // Populate the Use Case Diagram Components ListView
        useCaseComponentList.getItems().addAll("Actor", "Use Case", "System", "Include", "Extend");

        // Populate the Class Diagram Components ListView
        classComponentList.getItems().addAll("Class", "Interface", "Association", "Inheritance", "Aggregation", "Composition");

        // Set button actions
        useCaseButton.setOnAction(event -> showUseCaseComponents());
        classDiagramButton.setOnAction(event -> showClassDiagramComponents());
    }

    private void showUseCaseComponents() {
        // Show Use Case Components and hide Class Diagram Components
        useCaseComponentList.setVisible(true);
        classComponentList.setVisible(false);
    }

    private void showClassDiagramComponents() {
        // Show Class Diagram Components and hide Use Case Components
        classComponentList.setVisible(true);
        useCaseComponentList.setVisible(false);
    }
}
