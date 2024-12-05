package com.example.umleditor.ui;

import com.example.umleditor.data.Actor;
import com.example.umleditor.data.UseCase;
import com.example.umleditor.ui.components.*;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.embed.swing.SwingFXUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
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

    @FXML
    private MenuItem saveMenuItem;

    private String selectedTool = null;
    private List<ActorComponent> actors = new ArrayList<>();
    private List<UseCaseComponent> useCases = new ArrayList<>();
    private List<ClassComponent> classes=new ArrayList<>();
    private List<Connection> connections = new ArrayList<>();
    private List<ClassDiagramConnection> classConnections=new ArrayList<>();
    private ContextMenu contextMenu = new ContextMenu();
    private ContextMenu contextMenu2 = new ContextMenu();
    private ActorComponent selectedActor = null;
    private UseCaseComponent selectedUseCase = null;
    private ClassComponent selectedClass = null;
    private boolean creatingConnection = false;
    private boolean deletingConnection = false;
    private boolean showSystemBoundary = false;
    private boolean isDiagramModified = false;

    private ClassComponent startClass = null;
    private ArrowType selectedArrowType = null;

    @FXML
    public void initialize() {
        // Set up the component lists
        useCaseComponentList.getItems().addAll("Actor", "Use Case");
        classComponentList.getItems().addAll("Class", "Arrow");

        useCaseButton.setOnAction(event -> showUseCaseComponents());
        classDiagramButton.setOnAction(event -> showClassDiagramComponents());

        // Handle canvas mouse events
        drawingCanvas.setOnMouseClicked(this::handleCanvasClick);
        drawingCanvas.setOnMouseDragged(this::handleCanvasDrag);
        drawingCanvas.setOnMouseReleased(this::handleCanvasRelease);

        // Set up drag-and-drop events on the actor component
        useCaseComponentList.setOnMouseClicked(event -> handleComponentSelection());
        classComponentList.setOnMouseClicked(event -> handleComponentSelection());
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


        MenuItem addAttribute = new MenuItem("Add Attribute");
        addAttribute.setOnAction(event->addAttributesFunction());

        MenuItem addMethod = new MenuItem("Add Method");
        addMethod.setOnAction(event->addMethodsFunction());

        MenuItem addArrow=new MenuItem("Add Arrow");
        addArrow.setOnAction(event->addClassDiagramConnection());

        contextMenu2.getItems().addAll(addAttribute,addMethod,addArrow);
        // Set up save menu item
        saveMenuItem.setOnAction(event -> saveDiagramAsImage());
    }

    private void showUseCaseComponents() {
        clearCanvas();
        useCaseComponentList.setVisible(true);
        classComponentList.setVisible(false);
        showSystemBoundary = true;

        drawComponents();
    }

    private void showClassDiagramComponents() {
        classComponentList.setVisible(true);
        useCaseComponentList.setVisible(false);
        showSystemBoundary = false;
        clearCanvas();
        drawComponents();
    }

    private void handleComponentSelection() {
        // Check if "Actor" or "Use Case" is selected
        if(useCaseComponentList.isVisible())
            selectedTool = useCaseComponentList.getSelectionModel().getSelectedItem();
        else if(classComponentList.isVisible())
            selectedTool=classComponentList.getSelectionModel().getSelectedItem();
    }

    private void handleCanvasClick(MouseEvent event) {
        isDiagramModified = true;
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
            for (ClassComponent c : classes) {
                if (c.isSelected()) {
                    contextMenu2.show(drawingCanvas, event.getScreenX(), event.getScreenY());
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
            }
            else if("Class".equals(selectedTool))
            {
                TextInputDialog dialog = new TextInputDialog("Class Name");
                dialog.setTitle("Class Name");
                dialog.setHeaderText("Enter the class name:");
                Optional<String> result = dialog.showAndWait();
                result.ifPresent(className -> {
                    ClassComponent newClass=new ClassComponent(event.getX(),event.getY(),150,75,className);
                    classes.add(newClass);
                    drawComponents();
                    selectedTool=null;});
            }
            else {
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
                        if (deletingConnection) {
                            // Delete the connection involving the selected actor
                            deleteConnection(actor);
                        }
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
                        } else if (creatingConnection && selectedUseCase != null) {
                            // Ask user for connection type (<<extend>> or <<include>>)
                            TextInputDialog dialog = new TextInputDialog();
                            dialog.setTitle("Connection Type");
                            dialog.setHeaderText("Enter connection type (<<extend>> or <<include>>):");
                            Optional<String> result = dialog.showAndWait();
                            result.ifPresent(type -> {
                                // Create a connection between the selected use cases
                                Connection connection = new Connection(selectedUseCase, useCase, type);
                                connection.setLabel(type);
                                connections.add(connection);
                                selectedUseCase = null;
                                creatingConnection = false;
                                drawComponents();
                            });
                        } else if (creatingConnection && selectedUseCase == null) {
                            // Select the first use case for connection
                            selectedUseCase = useCase;
                        } else if (deletingConnection) {
                            // Delete the connection involving the selected use case
                            deleteConnection(useCase);
                        }
                    } else {
                        useCase.setSelected(false);
                    }
                }
                for(ClassComponent cls : classes)
                {
                    double x =event.getX();
                    double y= event.getY();
                    double clsX = cls.getX();
                    double clsY = cls.getY();
                    double width = cls.getWidth();
                    double height = cls.getHeight();
                    if (x >= clsX - width / 2 && x <= clsX + width / 2 && y >= clsY - height / 2 && y <= clsY + height / 2)
                    { cls.setSelected(true); selectedClass = cls; }
                    else
                    { cls.setSelected(false); }

                }
                drawComponents();
            }
        }
    }

    private void addAttributesFunction() {
        if (selectedClass != null && selectedClass.isSelected()) {
            // Show a dialog to edit attributes and methods
            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("Edit Class");
            dialog.setHeaderText("Enter attributes or methods (comma separated):");
            Optional<String> result = dialog.showAndWait();

            result.ifPresent(input -> {
                String[] parts = input.split(",");
                for (String part : parts) {
                    if (part.contains("(") && part.contains(")")) {
                        //selectedClass.addMethod(part.trim());
                    } else {
                        selectedClass.addAttribute(part.trim());
                    }
                }
                selectedClass.setSelected(false);
                drawComponents();

            });
            selectedClass.setSelected(false);
            drawComponents();

        }
    }

    private void addMethodsFunction() {
        if (selectedClass != null && selectedClass.isSelected()) {
            // Show a dialog to edit attributes and methods
            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("Edit Class");
            dialog.setHeaderText("Enter attributes or methods (comma separated):");
            Optional<String> result = dialog.showAndWait();

            result.ifPresent(input -> {
                String[] parts = input.split(",");
                for (String part : parts) {
                    if (part.contains("(") && part.contains(")")) {
                        selectedClass.addMethod(part.trim());
                    } else {
                        //selectedClass.addAttribute(part.trim());
                    }
                }
                selectedClass.setSelected(false);
                drawComponents();

            });
            selectedClass.setSelected(false);
            drawComponents();

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
        for (ClassComponent c : classes) {
            if (c.isSelected()) {
                // Update use case's position as it's dragged
                c.setX(event.getX());
                c.setY(event.getY());
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
        for (ClassComponent cls : classes) {
            cls.draw(gc);
        }
        for (Connection connection : connections) {
            if (connection.getActor() != null) {
                connection.draw(gc);
            } else {
                connection.drawUseCaseToUseCase(gc);
            }
        }
        for(ClassDiagramConnection c:classConnections)
        {
            if(c.getStart()!=null && c.getEnd()!=null)
            {
                c.draw(gc);
            }
            else {
                System.out.println("Error");
            }
        }
    }

    private void drawSystemBoundary() {
        GraphicsContext gc = drawingCanvas.getGraphicsContext2D();
        gc.setLineWidth(2);
        gc.setStroke(Color.GRAY);
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

    private void deleteConnection(UseCaseComponent useCase) {
        boolean connectionFound = false;
        List<Connection> connectionsToRemove = new ArrayList<>();
        for (Connection connection : connections) {
            if (connection.getUseCase1() == useCase || connection.getUseCase2() == useCase) {
                connectionsToRemove.add(connection);
                connectionFound = true;
            }
        }
        connections.removeAll(connectionsToRemove);
        if (!connectionFound) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("No Connection Found");
            alert.setContentText("There is no connection with the selected use case.");
            alert.showAndWait();
        } else {
            deletingConnection = false; // Reset the flag after successful deletion
        }
        drawComponents();
    }


    private void deleteConnection(ActorComponent actor) {
        boolean connectionFound = false;
        List<Connection> connectionsToRemove = new ArrayList<>();
        for (Connection connection : connections) {
            if (connection.getActor() == actor) {
                connectionsToRemove.add(connection);
                connectionFound = true;
            }
        }
        connections.removeAll(connectionsToRemove);
        if (!connectionFound) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("No Connection Found");
            alert.setContentText("There is no connection with the selected actor.");
            alert.showAndWait();
        } else {
            deletingConnection = false; // Reset the flag after successful deletion
        }
        drawComponents();
    }


    private void switchToClassDiagram() {
        if (isDiagramModified) {
            Alert alert = new Alert(AlertType.WARNING, "Are you sure you want to switch to the class diagram? If not saved, then SAVE the diagram", ButtonType.YES, ButtonType.NO);
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.YES) {

                clearCanvas();
                showClassDiagramComponents();
            }
        }
    }

    private void clearCanvas() {
        GraphicsContext gc = drawingCanvas.getGraphicsContext2D();
        gc.clearRect(0, 0, drawingCanvas.getWidth(), drawingCanvas.getHeight());
        actors.clear();
        useCases.clear();
        connections.clear();
        classes.clear();
        classConnections.clear();
    }

    private void saveDiagramAsImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Diagram");
        fileChooser.getExtensionFilters().addAll(
                new ExtensionFilter("PNG Files", "*.png"),
                new ExtensionFilter("JPEG Files", ".jpg", ".jpeg")
        );
        File file = fileChooser.showSaveDialog(drawingCanvas.getScene().getWindow());
        if (file != null) {
            try {
                WritableImage writableImage = new WritableImage((int) drawingCanvas.getWidth(), (int) drawingCanvas.getHeight());
                drawingCanvas.snapshot(null, writableImage);
                BufferedImage bufferedImage = SwingFXUtils.fromFXImage(writableImage, null);
                ImageIO.write(bufferedImage, getFileExtension(file), file);
            } catch (IOException ex) {
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Could not save image");
                alert.setContentText("An error occurred while saving the image: " + ex.getMessage());
                alert.showAndWait();
            }
        }
    }

    private void addClassDiagramConnection() {
        if (selectedClass != null) {
            ClassComponent firstClass = selectedClass;
            selectedClass = null;

            // Wait for user to click on the second class component
            drawingCanvas.setOnMouseClicked(event -> {
                for (ClassComponent secondClass : classes) {
                    double x = event.getX();
                    double y = event.getY();
                    double clsX = secondClass.getX();
                    double clsY = secondClass.getY();
                    double width = secondClass.getWidth();
                    double height = secondClass.getHeight();

                    if (x >= clsX - width / 2 && x <= clsX + width / 2 && y >= clsY - height / 2 && y <= clsY + height / 2) {
                        // Prompt for arrow type and multiplicity
                        Dialog<ArrowType> arrowTypeDialog = new Dialog<>();
                        arrowTypeDialog.setTitle("Select Arrow Type");

                        // Set the button types
                        ButtonType okButtonType = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
                        arrowTypeDialog.getDialogPane().getButtonTypes().addAll(okButtonType, ButtonType.CANCEL);

                        // Create the arrow type selection
                        ToggleGroup toggleGroup = new ToggleGroup();
                        RadioButton generalizationButton = new RadioButton("Generalization");
                        generalizationButton.setToggleGroup(toggleGroup);
                        generalizationButton.setSelected(true);
                        RadioButton aggregationButton = new RadioButton("Aggregation");
                        aggregationButton.setToggleGroup(toggleGroup);
                        RadioButton compositionButton = new RadioButton("Composition");
                        compositionButton.setToggleGroup(toggleGroup);
                        RadioButton associationButton = new RadioButton("Association");
                        associationButton.setToggleGroup(toggleGroup);

                        VBox vbox = new VBox(generalizationButton, aggregationButton, compositionButton, associationButton);
                        arrowTypeDialog.getDialogPane().setContent(vbox);

                        arrowTypeDialog.setResultConverter(dialogButton -> {
                            if (dialogButton == okButtonType) {
                                if (generalizationButton.isSelected()) {
                                    return ArrowType.GENERALIZATION;
                                } else if (aggregationButton.isSelected()) {
                                    return ArrowType.AGGREGATION;
                                } else if (compositionButton.isSelected()) {
                                    return ArrowType.COMPOSITION;
                                } else {
                                    return ArrowType.ASSOCIATION;
                                }
                            }
                            return null;
                        });

                        Optional<ArrowType> arrowTypeResult = arrowTypeDialog.showAndWait();

                        arrowTypeResult.ifPresent(arrowType -> {
                            // Prompt for multiplicity
                            TextInputDialog multiplicityDialog = new TextInputDialog("1,1");
                            multiplicityDialog.setTitle("Multiplicity");
                            multiplicityDialog.setHeaderText("Enter start and end multiplicity (comma separated):");
                            Optional<String> multiplicityResult = multiplicityDialog.showAndWait();

                            multiplicityResult.ifPresent(multiplicity -> {
                                String[] multiplicityParts = multiplicity.split(",");
                                ClassDiagramConnection newArrow = new ClassDiagramConnection(firstClass, secondClass, arrowType, multiplicityParts[0], multiplicityParts[1]);
                                classConnections.add(newArrow);
                                drawComponents();
                            });

                            // Reset mouse click handler
                            drawingCanvas.setOnMouseClicked(this::handleCanvasClick);
                            //break;
                        });
                    }
                }
            });
        }
    }




    private String getFileExtension(File file) {
        String fileName = file.getName();
        if (fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0) {
            return fileName.substring(fileName.lastIndexOf(".") + 1);
        } else {
            return "";
 }
}
}