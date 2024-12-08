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
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class MainController {

    public MenuItem codeGenerate;
    @FXML
    private Button useCaseButton;

    @FXML
    private Button classDiagramButton;

    @FXML
    public ListView<String> useCaseComponentList;

    @FXML
    public ListView<String> classComponentList;

    @FXML
    public Canvas drawingCanvas;

    @FXML
    public MenuItem saveMenuItem;

    @FXML
    public MenuItem loadAsXML;

    @FXML
    public MenuItem saveAsXML;

    private String selectedTool = null;
    private List<ActorComponent> actors = new ArrayList<>();
    private List<UseCaseComponent> useCases = new ArrayList<>();
    public List<ClassComponent> classes=new ArrayList<>();
    private List<Connection> connections = new ArrayList<>();
    public List<ClassDiagramConnection> classConnections=new ArrayList<>();
    private ContextMenu contextMenu = new ContextMenu();
    private ContextMenu contextMenu2 = new ContextMenu();
    private ActorComponent selectedActor = null;
    private UseCaseComponent selectedUseCase = null;
    public ClassComponent selectedClass = null;
    private boolean creatingConnection = false;
    private boolean deletingConnection = false;
    private boolean showSystemBoundary = false;
    private boolean isDiagramModified = false;
    private boolean DiagramType;
    private ClassComponent startClass = null;
    private ArrowType selectedArrowType = null;

    @FXML
    public void initialize() {
        // Set up the component lists
        useCaseComponentList.getItems().addAll("Actor", "Use Case");
        classComponentList.getItems().addAll("Class / Interface");

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

        MenuItem removeArrow=new MenuItem("Remove Arrow");
        removeArrow.setOnAction(event->removeClassDiagramConnection());

        MenuItem delete=new MenuItem("Delete");
        delete.setOnAction(event->deleteClassDiagramComponents());

        contextMenu2.getItems().addAll(addAttribute,addMethod,addArrow,removeArrow,delete);
        // Set up save menu item
        saveMenuItem.setOnAction(event->saveDiagramAsImage());
        codeGenerate.setOnAction(event->generateCode());
        loadAsXML.setOnAction(event->loadDiagram());
        saveAsXML.setOnAction(event->saveDiagram());
    }

    private void showUseCaseComponents() {
        clearCanvas();
        useCaseComponentList.setVisible(true);
        classComponentList.setVisible(false);
        showSystemBoundary = true;
        DiagramType=true;
        drawComponents();
    }

    private void showClassDiagramComponents() {
        clearCanvas();
        classComponentList.setVisible(true);
        useCaseComponentList.setVisible(false);
        showSystemBoundary = false;
        DiagramType=false;
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
            else if ("Class / Interface".equals(selectedTool)) {
                Dialog<ClassComponent> dialog = new Dialog<>();
                dialog.setTitle("Class / Interface Component");
                dialog.setHeaderText("Enter the details:");

                // Set the button types
                ButtonType okButtonType = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
                dialog.getDialogPane().getButtonTypes().addAll(okButtonType, ButtonType.CANCEL);

                // Create the class/interface selection and name input fields
                ToggleGroup toggleGroup = new ToggleGroup();
                RadioButton classButton = new RadioButton("Class");
                classButton.setToggleGroup(toggleGroup);
                classButton.setSelected(true); // Default to class
                RadioButton interfaceButton = new RadioButton("Interface");
                interfaceButton.setToggleGroup(toggleGroup);

                TextField nameField = new TextField();
                nameField.setPromptText("Name");

                VBox vbox = new VBox(classButton, interfaceButton, nameField);
                dialog.getDialogPane().setContent(vbox);

                dialog.setResultConverter(dialogButton -> {
                    if (dialogButton == okButtonType) {
                        boolean isInterface = interfaceButton.isSelected();
                        String name = nameField.getText();
                        return new ClassComponent(event.getX(), event.getY(), 150, 75, name, isInterface);
                    }
                    return null;
                });

                Optional<ClassComponent> result = dialog.showAndWait();
                result.ifPresent(classComponent -> {
                    classes.add(classComponent);
                    drawComponents();
                    selectedTool = null;
                });
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
            dialog.setHeaderText("Enter attributes (comma separated):");
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
            dialog.setHeaderText("Enter methods (comma separated):");
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

    public void deleteClassDiagramComponents() {
        // Identify selected classes to be removed
        List<ClassComponent> toBeRemoved = classes.stream()
                .filter(ClassComponent::isSelected)
                .collect(Collectors.toList());

        // Remove connections associated with the selected classes
        for (ClassComponent classComponent : toBeRemoved) {
            classConnections.removeIf(connection ->
                    connection.getStart().equals(classComponent) ||
                            connection.getEnd().equals(classComponent)
            );
        }

        // Remove the selected classes
        classes.removeIf(ClassComponent::isSelected);

        // Redraw components to reflect changes
        drawComponents();
    }

    public void generateCode() { CodeGenerator.generateCode(classes); }

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

    public void saveDiagram()
    {   FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Class Diagram");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Serialized Files", "*.ser"));
        File file = fileChooser.showSaveDialog(null);
        if (file != null) {
            try (FileOutputStream fileOut = new FileOutputStream(file);
                 ObjectOutputStream out = new ObjectOutputStream(fileOut)) {
                if(DiagramType == false){
                    out.writeObject(classes);
                    out.writeObject(classConnections);
                    System.out.println("Class diagram saved to " + file.getAbsolutePath());
                    JOptionPane.showMessageDialog(null,"Class Diagram Saved");
                }
                else
                {
                    out.writeObject(actors);
                    out.writeObject(useCases);
                    out.writeObject(connections);
                    System.out.println("Use case diagram saved to " + file.getAbsolutePath());
                    JOptionPane.showMessageDialog(null,"Use Case Diagram Saved");
                }
            } catch (IOException i) {
                i.printStackTrace();
            }
        }
    }

    public void loadDiagram() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Class Diagram");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Serialized Files", "*.ser"));
        File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            try (FileInputStream fileIn = new FileInputStream(file);
                 ObjectInputStream in = new ObjectInputStream(fileIn)) {
                if(DiagramType==false){
                    List<ClassComponent> deserializedClasses = (List<ClassComponent>) in.readObject();
                    List<ClassDiagramConnection> deserializedConnections = (List<ClassDiagramConnection>) in.readObject(); // Clear existing diagram
                    classes.clear();
                    connections.clear();
                    // Add deserialized components to lists
                    classes.addAll(deserializedClasses);
                    classConnections.addAll(deserializedConnections); // Redraw the canvas
                    drawComponents();
                    System.out.println("Class diagram loaded from " + file.getAbsolutePath());
                    JOptionPane.showMessageDialog(null,"Class Diagram Loaded Successfully");
                }
                else {
                    List<ActorComponent> deserializedActors = (List<ActorComponent>) in.readObject();
                    List<UseCaseComponent> deserializedUseCases = (List<UseCaseComponent>) in.readObject();
                    List<Connection> deserializedConnections = (List<Connection>) in.readObject();

                    clearCanvas();

                    // Add deserialized components to lists
                    actors.addAll(deserializedActors);
                    useCases.addAll(deserializedUseCases);
                    connections.addAll(deserializedConnections);

                    // Redraw the canvas
                    drawSystemBoundary();
                    drawComponents();
                    System.out.println("Use case diagram loaded from " + file.getAbsolutePath());
                    JOptionPane.showMessageDialog(null,"Use Case Diagram Loaded Successfully");

                }
            } catch (IOException | ClassNotFoundException | ClassCastException e) {
                System.err.println("Error loading file: " + e.getMessage());
                e.printStackTrace(); // Show error alert
                javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.ERROR);
                alert.setTitle("Load Error");
                alert.setHeaderText("Invalid File");
                if(DiagramType==false)
                    alert.setContentText("The file you selected is not a valid class diagram file.");
                else
                    alert.setContentText("The file you selected is not a valid use case diagram file.");
                alert.showAndWait();
            }
        }
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

    public void addClassDiagramConnection() {
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
                                firstClass.addCon(newArrow);
                                secondClass.addCon(newArrow);
                                drawComponents();
                            });

                            // Reset mouse click handler
                            drawingCanvas.setOnMouseClicked(this::handleCanvasClick);
                        });
                    }
                }
            });
        }
    }



    public void removeClassDiagramConnection() {
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
                        // Confirm deletion
                        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                        alert.setTitle("Delete Connection");
                        alert.setHeaderText("Are you sure you want to delete the connection between these classes?");
                        Optional<ButtonType> result = alert.showAndWait();

                        if (result.isPresent() && result.get() == ButtonType.OK) {
                            classConnections.removeIf(connection ->
                                    (connection.getStart().equals(firstClass) && connection.getEnd().equals(secondClass)) ||
                                            (connection.getStart().equals(secondClass) && connection.getEnd().equals(firstClass))
                            );
                            drawComponents();
                        }

                        // Reset mouse click handler
                        drawingCanvas.setOnMouseClicked(this::handleCanvasClick);
                        break;
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
            return"";
        }
    }
}