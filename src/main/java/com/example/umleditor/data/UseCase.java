package com.example.umleditor.data;

import java.io.Serializable;

/**
 * Represents a use case in a UML diagram.
 * This class holds the properties and methods to manipulate use case data.
 */
public class UseCase implements Serializable {
    private double x;
    private double y;
    private double width;
    private double height;
    private String name;

    /**
     * Constructs a UseCase with the specified coordinates, dimensions, and name.
     *
     * @param x      the x-coordinate of the use case
     * @param y      the y-coordinate of the use case
     * @param width  the width of the use case
     * @param height the height of the use case
     * @param name   the name of the use case
     */
    public UseCase(double x, double y, double width, double height, String name) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.name = name;
    }

    /**
     * Gets the x-coordinate of the use case.
     *
     * @return the x-coordinate
     */
    public double getX() {
        return x;
    }

    /**
     * Sets the x-coordinate of the use case.
     *
     * @param x the new x-coordinate
     */
    public void setX(double x) {
        this.x = x;
    }

    /**
     * Gets the y-coordinate of the use case.
     *
     * @return the y-coordinate
     */
    public double getY() {
        return y;
    }

    /**
     * Sets the y-coordinate of the use case.
     *
     * @param y the new y-coordinate
     */
    public void setY(double y) {
        this.y = y;
    }

    /**
     * Gets the width of the use case.
     *
     * @return the width
     */
    public double getWidth() {
        return width;
    }

    /**
     * Sets the width of the use case.
     *
     * @param width the new width
     */
    public void setWidth(double width) {
        this.width = width;
    }

    /**
     * Gets the height of the use case.
     *
     * @return the height
     */
    public double getHeight() {
        return height;
    }

    /**
     * Sets the height of the use case.
     *
     * @param height the new height
     */
    public void setHeight(double height) {
        this.height = height;
    }

    /**
     * Gets the name of the use case.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the use case.
     *
     * @param name the new name
     */
    public void setName(String name) {
        this.name = name;
    }
}
