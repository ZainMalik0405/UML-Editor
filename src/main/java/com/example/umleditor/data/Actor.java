package com.example.umleditor.data;

import java.io.Serializable;

/**
 * Represents an actor in a UML diagram.
 * This class holds the properties and methods to manipulate actor data.
 */
public class Actor implements Serializable {
    private double x;
    private double y;
    private double size;
    private String name;

    /**
     * Constructs an Actor with the specified coordinates, size, and name.
     *
     * @param x     the x-coordinate of the actor
     * @param y     the y-coordinate of the actor
     * @param size  the size of the actor
     * @param name  the name of the actor
     */
    public Actor(double x, double y, double size, String name) {
        this.x = x;
        this.y = y;
        this.size = size;
        this.name = name;
    }

    /**
     * Gets the x-coordinate of the actor.
     *
     * @return the x-coordinate
     */
    public double getX() {
        return x;
    }

    /**
     * Sets the x-coordinate of the actor.
     *
     * @param x the new x-coordinate
     */
    public void setX(double x) {
        this.x = x;
    }

    /**
     * Gets the y-coordinate of the actor.
     *
     * @return the y-coordinate
     */
    public double getY() {
        return y;
    }

    /**
     * Sets the y-coordinate of the actor.
     *
     * @param y the new y-coordinate
     */
    public void setY(double y) {
        this.y = y;
    }

    /**
     * Gets the size of the actor.
     *
     * @return the size
     */
    public double getSize() {
        return size;
    }

    /**
     * Sets the size of the actor.
     *
     * @param size the new size
     */
    public void setSize(double size) {
        this.size = size;
    }

    /**
     * Gets the name of the actor.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the actor.
     *
     * @param name the new name
     */
    public void setName(String name) {
        this.name = name;
    }
}
