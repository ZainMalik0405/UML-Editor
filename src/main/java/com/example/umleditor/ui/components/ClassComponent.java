package com.example.umleditor.ui.components;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a class or interface component in a UML class diagram.
 * This class handles the properties and methods related to class components,
 * including rendering, selection, and connections to other class components.
 */
public class ClassComponent implements Serializable {
    private static final long serialVersionUID = 1L;
    private double x;
    private double y;
    private double width;
    private double height;
    private String name;
    private boolean isSelected;
    private List<String> attributes;
    private List<String> methods;
    private List<ClassDiagramConnection> con;
    private boolean isInterface;

    /**
     * Constructs a ClassComponent with the specified properties.
     *
     * @param x         the x-coordinate of the class component
     * @param y         the y-coordinate of the class component
     * @param width     the width of the class component
     * @param height    the height of the class component
     * @param name      the name of the class or interface
     * @param isInterface specifies if the component is an interface
     */
    public ClassComponent(double x, double y, double width, double height, String name, boolean isInterface) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.name = name;
        this.isSelected = false;
        this.attributes = new ArrayList<>();
        this.methods = new ArrayList<>();
        this.isInterface = isInterface;
        this.con = new ArrayList<>();
    }

    /**
     * Adds a connection to the class component.
     *
     * @param c the connection to add
     */
    public void addCon(ClassDiagramConnection c) {
        con.add(c);
    }

    /**
     * Gets the list of connections associated with the class component.
     *
     * @return the list of connections
     */
    public List<ClassDiagramConnection> getConnections() {
        return con;
    }

    /**
     * Draws the class component on the canvas.
     *
     * @param gc the graphics context used for drawing
     */
    public void draw(GraphicsContext gc) {
        // Calculate dynamic height based on attributes and methods
        double newHeight = 60 + attributes.size() * 20 + methods.size() * 20;

        gc.setLineWidth(2);
        gc.setStroke(Color.BLACK);
        gc.setFill(Color.WHITE);

        // Draw the class component rectangle
        gc.fillRect(x - width / 2, y - newHeight / 2, width, newHeight);
        gc.strokeRect(x - width / 2, y - newHeight / 2, width, newHeight);

        // Draw the class name section
        gc.strokeLine(x - width / 2, y - newHeight / 2 + 30, x + width / 2, y - newHeight / 2 + 30);
        gc.setTextAlign(TextAlignment.CENTER);
        gc.setFill(Color.BLACK);
        String displayName = isInterface ? "<<Interface>> " + name : name;
        gc.fillText(displayName, x, y - newHeight / 2 + 20);

        // Draw the attributes section
        gc.strokeLine(x - width / 2, y - newHeight / 2 + 30 + attributes.size() * 20 + 10, x + width / 2, y - newHeight / 2 + 30 + attributes.size() * 20 + 10);
        gc.setTextAlign(TextAlignment.LEFT);
        double attrY = y - newHeight / 2 + 50;
        for (String attribute : attributes) {
            gc.fillText(attribute, x - width / 2 + 10, attrY);
            attrY += 20;
        }

        // Draw the methods section
        double methodY = attrY + 10;
        for (String method : methods) {
            gc.fillText(method, x - width / 2 + 10, methodY);
            methodY += 20;
        }

        // Draw selection highlight if selected
        if (isSelected) {
            gc.setLineDashes(5);
            gc.setStroke(Color.BLUE);
            gc.strokeRect(x - width / 2 - 5, y - newHeight / 2 - 5, width + 10, newHeight + 10);
            gc.setLineDashes(0); // Reset line dashes
        }
    }

    // Getters and setters
    /**
     * Gets the x-coordinate of the class component.
     *
     * @return the x-coordinate
     */
    public double getX() { return x; }

    /**
     * Sets the x-coordinate of the class component.
     *
     * @param x the new x-coordinate
     */
    public void setX(double x) { this.x = x; }

    /**
     * Gets the y-coordinate of the class component.
     *
     * @return the y-coordinate
     */
    public double getY() { return y; }

    /**
     * Sets the y-coordinate of the class component.
     *
     * @param y the new y-coordinate
     */
    public void setY(double y) { this.y = y; }

    /**
     * Gets the width of the class component.
     *
     * @return the width
     */
    public double getWidth() { return width; }

    /**
     * Sets the width of the class component.
     *
     * @param width the new width
     */
    public void setWidth(double width) { this.width = width; }

    /**
     * Gets the height of the class component.
     *
     * @return the height
     */
    public double getHeight() { return height; }

    /**
     * Sets the height of the class component.
     *
     * @param height the new height
     */
    public void setHeight(double height) { this.height = height; }

    /**
     * Gets the name of the class component.
     *
     * @return the name
     */
    public String getName() { return name; }

    /**
     * Sets the name of the class component.
     *
     * @param name the new name
     */
    public void setName(String name) { this.name = name; }

    /**
     * Checks if the class component is selected.
     *
     * @return true if the component is selected, false otherwise
     */
    public boolean isSelected() { return isSelected; }

    /**
     * Sets the selection state of the class component.
     *
     * @param selected the new selection state
     */
    public void setSelected(boolean selected) { isSelected = selected; }

    /**
     * Gets the list of attributes of the class component.
     *
     * @return the list of attributes
     */
    public List<String> getAttributes() { return attributes; }

    /**
     * Gets the list of methods of the class component.
     *
     * @return the list of methods
     */
    public List<String> getMethods() { return methods; }

    /**
     * Adds an attribute to the class component.
     *
     * @param attribute the attribute to add
     */
    public void addAttribute(String attribute) { attributes.add(attribute); }

    /**
     * Adds a method to the class component.
     *
     * @param method the method to add
     */
    public void addMethod(String method) { methods.add(method); }

    /**
     * Checks if the component is an interface.
     *
     * @return true if the component is an interface, false otherwise
     */
    public boolean isInterface() { return isInterface; }
}
