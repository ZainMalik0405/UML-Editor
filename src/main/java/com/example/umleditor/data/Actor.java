package com.example.umleditor.data;
import java.io.Serializable;
public class Actor implements Serializable {
    private double x;
    private double y;
    private double size;
    private String name;

    public Actor(double x, double y, double size, String name) {
        this.x = x;
        this.y = y;
        this.size = size;
        this.name = name;
    }

    // Getters and setters
    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getSize() {
        return size;
    }

    public void setSize(double size) {
        this.size = size;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name=name;
    }
}