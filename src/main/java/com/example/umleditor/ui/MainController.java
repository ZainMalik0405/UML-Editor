package com.example.umleditor.ui;

import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class MainController {

    @FXML
    private ListView<String> useCaseComponentList; // ListView for Use Case components

    @FXML
    private ListView<String> classComponentList; // ListView for Class Diagram components

    @FXML
    public void initialize() {
        // Populate the Use Case Diagram Components ListView
        ObservableList<String> useCaseComponents = FXCollections.observableArrayList(
                "Actor", "Use Case", "System", "Include", "Extend");
        useCaseComponentList.setItems(useCaseComponents);

        // Populate the Class Diagram Components ListView
        ObservableList<String> classComponents = FXCollections.observableArrayList(
                "Class", "Interface", "Association", "Inheritance", "Aggregation", "Composition");
        classComponentList.setItems(classComponents);
    }
}
