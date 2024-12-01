package com.example.umleditor.ui;

import com.example.umleditor.data.Actor;
import com.example.umleditor.data.UseCase;
import com.example.umleditor.ui.components.ActorComponent;
import com.example.umleditor.ui.components.Connection;
import com.example.umleditor.ui.components.UseCaseComponent;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
    private Canvas drawingCanvas;

    private String selectedTool = null;
    private List<ActorComponent> actors = new ArrayList<>();
    private List<UseCaseComponent> useCases = new ArrayList<>();
    private List<Connection> connections = new ArrayList<>();
    private ContextMenu contextMenu = new ContextMenu();
    private ActorComponent selectedActor = null;
    private boolean creatingConnection = false;
    private boolean deletingConnection = false;
    private boolean showSystemBoundary = false;

    @FXML
    public void initialize() {
        // Set up the component lists
        useCaseComponentList.getItems().addAll("Actor", "Use Case", "System");
        classComponentList.getItems().addAll("Class", "Interface");

        useCaseButton.setOnAction(event -> showUseCaseComponents());
        classDiagramButton.setOnAction(event -> showClassDiagramComponents());

        // Handle canvas mouse events
        drawingCanvas.setOnMouseClicked(this::handleCanvasClick);
        drawingCanvas.setOnMouseDragged(this::handleCanvasDrag);
        drawingCanvas.setOnMouseReleased(this::handleCanvasRelease);

        // Set up drag-and-drop events on the actor component
        useCaseComponentList.setOnMouseClicked(event -> handleComponentSelection());

        // Set up context menu
        MenuItem deleteItem = new MenuItem("Delete");
        deleteItem.setOnAction(event -> deleteSelectedComponent());
        MenuItem addTextItem = new MenuItem("Add Text");
        addTextItem.setOnAction(event -> addTextToSelectedComponent());
        MenuItem createConnectionItem = new MenuItem("Create Connection");
        createConnectionItem.setOnAction(event -> startCreatingConnection());
        MenuItem deleteConnectionItem = new MenuItem("Delete Connection");
        deleteConnectionItem.setOnAction(event -> startDeletingConnection());
        contextMenu.getItems().addAll(deleteItem, addTextItem, createConnectionItem, deleteConnectionItem);
    }

    private void showUseCaseComponents() {
        useCaseComponentList.setVisible(true);
        classComponentList.setVisible(false);
        showSystemBoundary = true;
        drawComponents();
    }

    private void showClassDiagramComponents() {
        classComponentList.setVisible(true);
        useCaseComponentList.setVisible(false);
        showSystemBoundary = false;
        drawComponents();
    }

    private void handleComponentSelection() {
        // Check if "Actor" or "Use Case" is selected
        selectedTool = useCaseComponentList.getSelectionModel().getSelectedItem();
    }

    private void handleCanvasClick(MouseEvent event) {
        if (event.getButton() == MouseButton.SECONDARY) {
            // Handle right-click for context menu
            for (ActorComponent actor : actors) {
                if (actor.isSelected()) {
                    contextMenu.show(drawingCanvas, event.getScreenX(), event.getScreenY());
                    return;
                }
            }
            for (UseCaseComponent useCase : useCases) {
                if (useCase.isSelected()) {
                    contextMenu.show(drawingCanvas, event.getScreenX(), event.getScreenY());
                    return;
                }
            }
        } else {
            contextMenu.hide();
            if ("Actor".equals(selectedTool)) {
                // Place new actor on the canvas
                ActorComponent newActor = new ActorComponent(new Actor(event.getX(), event.getY(), 30, "Actor"));
                actors.add(newActor);
                drawComponents();
                selectedTool = null; // Reset selected tool
            } else if ("Use Case".equals(selectedTool)) {
                // Place new use case on the canvas
                UseCaseComponent newUseCase = new UseCaseComponent(new UseCase(event.getX(), event.getY(), 150, 75, "Use Case"));
                useCases.add(newUseCase);
                drawComponents();
                selectedTool = null; // Reset selected tool
            } else {
                // Check if the click is within any actor's or use case's bounds
                for (ActorComponent actor : actors) {
                    double x = event.getX();
                    double y = event.getY();
                    double actorX = actor.getActor().getX();
                    double actorY = actor.getActor().getY();
                    double size = actor.getActor().getSize();

                    if (x >= actorX - size / 2 && x <= actorX + size / 2 && y >= actorY - size / 2 && y <= actorY + size * 1.5) {
                        actor.setSelected(true);
                        selectedActor = actor;
                    } else {
                        actor.setSelected(false);
                    }
                }
                for (UseCaseComponent useCase : useCases) {
                    double x = event.getX();
                    double y = event.getY();
                    double useCaseX = useCase.getUseCase().getX();
                    double useCaseY = useCase.getUseCase().getY();
                    double width = useCase.getUseCase().getWidth();
                    double height = useCase.getUseCase().getHeight();

                    if (x >= useCaseX - width / 2 && x <= useCaseX + width / 2 && y >= useCaseY - height / 2 && y <= useCaseY + height / 2) {
                        useCase.setSelected(true);
                        if (creatingConnection && selectedActor != null) {
                            // Create a connection between the selected actor and use case
                            Connection connection = new Connection(selectedActor, useCase);
                            connections.add(connection);
                            selectedActor = null;
                            creatingConnection = false;
                            drawComponents();
                        } else if (deletingConnection && selectedActor != null) {
                            // Delete the connection between the selected actor and use case
                            boolean connectionFound = false;
                            for (Connection connection : connections) {
                                if (connection.getActor() == selectedActor && connection.getUseCase() == useCase) {
                                    connections.remove(connection);
                                    connectionFound = true;
                                    break;
                                }
                            }
                            if (!connectionFound) {
                                Alert alert = new Alert(AlertType.ERROR);
                                alert.setTitle("Error");
                                alert.setHeaderText("No Connection Found");
                                alert.setContentText("There is no connection between the selected actor and use case.");
                                alert.showAndWait();
                            }
                            selectedActor = null;
                            deletingConnection = false;
                            drawComponents();
                        }
                    } else {
                        useCase.setSelected(false);
                    }
                }
                drawComponents();
            }
        }
    }

    private void handleCanvasDrag(MouseEvent event) {
        for (ActorComponent actor : actors) {
            if (actor.isSelected()) {
                // Update actor's position as it's dragged
                actor.getActor().setX(event.getX());
                actor.getActor().setY(event.getY());
                drawComponents();
                break;
            }
        }
        for (UseCaseComponent useCase : useCases) {
            if (useCase.isSelected()) {
                // Update use case's position as it's dragged
                useCase.getUseCase().setX(event.getX());
                useCase.getUseCase().setY(event.getY());
                drawComponents();
                break;
            }
        }
    }

    private void handleCanvasRelease(MouseEvent event) {
        // Handle mouse release events if needed
    }

    private void drawComponents() {
        GraphicsContext gc = drawingCanvas.getGraphicsContext2D();
        gc.clearRect(0, 0, drawingCanvas.getWidth(), drawingCanvas.getHeight());
        if (showSystemBoundary) {
            drawSystemBoundary();
        }
        for (ActorComponent actor : actors) {
            actor.draw(gc);
        }
        for (UseCaseComponent useCase : useCases) {
            useCase.draw(gc);
        }
        for (Connection connection : connections) {
            connection.draw(gc);
        }
    }

    private void drawSystemBoundary() {
        GraphicsContext gc = drawingCanvas.getGraphicsContext2D();
        gc.setLineWidth(2); gc.setStroke(Color.GRAY);
        gc.strokeRect(150, 10, drawingCanvas.getWidth() - 200, drawingCanvas.getHeight() - 50); // Adjusted height
         }

    private void deleteSelectedComponent() {
        actors.removeIf(ActorComponent::isSelected);
        useCases.removeIf(UseCaseComponent::isSelected);
        connections.removeIf(connection -> connection.getActor().isSelected() || connection.getUseCase().isSelected());
        drawComponents();
    }

    private void startCreatingConnection() {
        creatingConnection = true;
    }

    private void startDeletingConnection() {
        deletingConnection = true;
    }
    private void addTextToSelectedComponent() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Add Text");
        dialog.setHeaderText("Enter text to add to the selected component:");
        Optional<String> result = dialog.showAndWait();
        result.ifPresent(text -> {
            for (ActorComponent actor : actors) {
                if (actor.isSelected()) {
                    actor.setText(text);
                }
            }
            for (UseCaseComponent useCase : useCases) {
                if (useCase.isSelected()) {
                    useCase.setText(text);
                }
            }
            drawComponents();
        });
    }

}