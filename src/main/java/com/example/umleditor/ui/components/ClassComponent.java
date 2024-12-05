package com.example.umleditor.ui.components;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;

import java.util.ArrayList;
import java.util.List;

public class ClassComponent {
    private double x;
    private double y;
    private double width;
    private double height;
    private String name;
    private boolean isSelected;
    private List<String> attributes;
    private List<String> methods;

    public ClassComponent(double x, double y, double width, double height, String name) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.name = name;
        this.isSelected = false;
        this.attributes = new ArrayList<>();
        this.methods = new ArrayList<>();
    }

    // Draw the class component on the canvas
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
        gc.fillText(name, x, y - newHeight / 2 + 20);

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
    public double getX() { return x; }
    public void setX(double x) { this.x = x; }
    public double getY() { return y; }
    public void setY(double y) { this.y = y; }
    public double getWidth() { return width; }
    public void setWidth(double width) { this.width = width; }
    public double getHeight() { return height; }
    public void setHeight(double height) { this.height = height; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public boolean isSelected() { return isSelected; }
    public void setSelected(boolean selected) { isSelected = selected; }
    public List<String> getAttributes() { return attributes; }
    public List<String> getMethods() { return methods; }
    public void addAttribute(String attribute) { attributes.add(attribute); }
    public void addMethod(String method) { methods.add(method);
    }
}