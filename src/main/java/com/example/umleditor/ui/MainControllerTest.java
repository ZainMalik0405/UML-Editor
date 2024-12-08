package com.example.umleditor.ui;

import com.example.umleditor.data.Actor;
import com.example.umleditor.data.UseCase;
import com.example.umleditor.ui.components.*;
import javafx.embed.swing.JFXPanel;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MainControllerTest {

    private MainController controller;

    @BeforeEach
    void setUp() {
        // Initialize JavaFX toolkit
        new JFXPanel();

        controller = new MainController();
        controller.useCaseComponentList = new ListView<>();
        controller.classComponentList = new ListView<>();
        controller.drawingCanvas = new Canvas();
        controller.saveMenuItem = new MenuItem();
        controller.codeGenerate = new MenuItem();
        controller.loadAsXML = new MenuItem();
        controller.saveAsXML = new MenuItem();

        controller.initialize();
    }

    @Test
    void testAddClassDiagramConnection() {
        ClassComponent class1 = new ClassComponent(100, 100, 150, 75, "Class1", false);
        ClassComponent class2 = new ClassComponent(200, 200, 150, 75, "Class2", false);

        controller.classes.add(class1);
        controller.classes.add(class2);
        controller.selectedClass = class1;

        // Simulate user click on the second class component
        Mockito.doAnswer(invocation -> {
            ((Runnable) invocation.getArguments()[0]).run();
            return null;
        }).when(controller.drawingCanvas).setOnMouseClicked(Mockito.any());

        controller.addClassDiagramConnection();

        assertFalse(controller.classConnections.isEmpty());
        assertTrue(controller.classConnections.stream()
                .anyMatch(connection -> connection.getStart().equals(class1) && connection.getEnd().equals(class2)));
    }

    @Test
    void testRemoveClassDiagramConnection() {
        ClassComponent class1 = new ClassComponent(100, 100, 150, 75, "Class1", false);
        ClassComponent class2 = new ClassComponent(200, 200, 150, 75, "Class2", false);

        controller.classes.add(class1);
        controller.classes.add(class2);
        controller.selectedClass = class1;

        ClassDiagramConnection connection = new ClassDiagramConnection(class1, class2, ArrowType.GENERALIZATION, "1", "1");
        controller.classConnections.add(connection);
        class1.addCon(connection);
        class2.addCon(connection);

        // Simulate user click on the second class component
        Mockito.doAnswer(invocation -> {
            ((Runnable) invocation.getArguments()[0]).run();
            return null;
        }).when(controller.drawingCanvas).setOnMouseClicked(Mockito.any());

        controller.removeClassDiagramConnection();

        assertTrue(controller.classConnections.isEmpty());
    }

    @Test
    void testDeleteClassDiagramComponents() {
        ClassComponent class1 = new ClassComponent(100, 100, 150, 75, "Class1", false);
        ClassComponent class2 = new ClassComponent(200, 200, 150, 75, "Class2", false);
        class1.setSelected(true);

        controller.classes.add(class1);
        controller.classes.add(class2);

        ClassDiagramConnection connection = new ClassDiagramConnection(class1, class2, ArrowType.GENERALIZATION, "1", "1");
        controller.classConnections.add(connection);
        class1.addCon(connection);
        class2.addCon(connection);

        controller.deleteClassDiagramComponents();

        assertFalse(controller.classes.contains(class1));
        assertTrue(controller.classes.contains(class2));
        assertTrue(controller.classConnections.isEmpty());
    }

    // Additional tests for other methods can be added here
}
