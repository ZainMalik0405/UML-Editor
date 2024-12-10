package com.example.umleditor.ui.components;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import java.io.Serializable;

/**
 * Represents a connection (relationship) between components in a UML diagram.
 * This class handles the rendering and management of connections between actors and use cases.
 */
public class Connection implements Serializable {
    private static final long serialVersionUID = 1L;
    private ActorComponent actor;
    private UseCaseComponent useCase;
    private UseCaseComponent useCase1;
    private UseCaseComponent useCase2;
    private String label;

    /**
     * Constructs a Connection between an actor and a use case.
     *
     * @param actor   the actor component
     * @param useCase the use case component
     */
    public Connection(ActorComponent actor, UseCaseComponent useCase) {
        this.actor = actor;
        this.useCase = useCase;
    }

    /**
     * Constructs a Connection between two use cases with a specified label.
     *
     * @param useCase1 the first use case component
     * @param useCase2 the second use case component
     * @param label    the label for the connection
     */
    public Connection(UseCaseComponent useCase1, UseCaseComponent useCase2, String label) {
        this.useCase1 = useCase1;
        this.useCase2 = useCase2;
        this.label = label;
    }

    /**
     * Draws the connection from the actor to the use case on the canvas.
     *
     * @param gc the graphics context used for drawing
     */
    public void draw(GraphicsContext gc) {
        gc.setLineWidth(2);
        gc.setStroke(Color.BLACK);

        // Draw line from actor to use case
        if (actor != null && useCase != null) {
            double[] actorEdge = getEdgePoint(actor.getActor().getX(), actor.getActor().getY(), useCase.getUseCase().getX(), useCase.getUseCase().getY(), actor.getActor().getSize() / 2 + 10);
            double[] useCaseEdge = getEdgePoint(useCase.getUseCase().getX(), useCase.getUseCase().getY(), actor.getActor().getX(), actor.getActor().getY(), Math.max(useCase.getUseCase().getWidth(), useCase.getUseCase().getHeight()) / 2 + 10);
            gc.strokeLine(actorEdge[0], actorEdge[1], useCaseEdge[0], useCaseEdge[1]);
        }
    }

    /**
     * Draws the connection between two use cases on the canvas.
     *
     * @param gc the graphics context used for drawing
     */
    public void drawUseCaseToUseCase(GraphicsContext gc) {
        gc.setLineWidth(2);
        gc.setStroke(Color.BLACK);
        gc.setLineDashes(10); // Set line to be dotted

        // Draw line from use case to use case
        if (useCase1 != null && useCase2 != null) {
            double[] useCase1Edge = getEdgePointToBoundary(useCase1, useCase2);
            double[] useCase2Edge = getEdgePointToBoundary(useCase2, useCase1);
            gc.strokeLine(useCase1Edge[0], useCase1Edge[1], useCase2Edge[0], useCase2Edge[1]);

            // Draw label on top of the line
            double midX = (useCase1Edge[0] + useCase2Edge[0]) / 2;
            double midY = (useCase1Edge[1] + useCase2Edge[1]) / 2;
            gc.setFill(Color.BLACK);
            gc.fillText(label, midX, midY - 5);
        }

        // Reset line dash pattern to solid
        gc.setLineDashes(0);
    }

    /**
     * Calculates the edge point of the use case boundary for the connection.
     *
     * @param from the starting use case component
     * @param to   the ending use case component
     * @return the coordinates of the edge point
     */
    private double[] getEdgePointToBoundary(UseCaseComponent from, UseCaseComponent to) {
        double x1 = from.getUseCase().getX();
        double y1 = from.getUseCase().getY();
        double x2 = to.getUseCase().getX();
        double y2 = to.getUseCase().getY();
        double width = from.getUseCase().getWidth() / 2;
        double height = from.getUseCase().getHeight() / 2;

        double angle = Math.atan2(y2 - y1, x2 - x1);
        double x = x1 + width * Math.cos(angle);
        double y = y1 + height * Math.sin(angle);
        return new double[]{x, y};
    }

    /**
     * Calculates the edge point of the connection.
     *
     * @param x1     the x-coordinate of the starting point
     * @param y1     the y-coordinate of the starting point
     * @param x2     the x-coordinate of the ending point
     * @param y2     the y-coordinate of the ending point
     * @param radius the radius of the actor or use case boundary
     * @return the coordinates of the edge point
     */
    private double[] getEdgePoint(double x1, double y1, double x2, double y2, double radius) {
        double angle = Math.atan2(y2 - y1, x2 - x1);
        double x = x1 + radius * Math.cos(angle);
        double y = y1 + radius * Math.sin(angle);
        return new double[]{x, y};
    }

    // Getters and setters

    /**
     * Gets the actor component of the connection.
     *
     * @return the actor component
     */
    public ActorComponent getActor() {
        return actor;
    }

    /**
     * Sets the actor component of the connection.
     *
     * @param actor the new actor component
     */
    public void setActor(ActorComponent actor) {
        this.actor = actor;
    }

    /**
     * Gets the use case component of the connection.
     *
     * @return the use case component
     */
    public UseCaseComponent getUseCase() {
        return useCase;
    }

    /**
     * Sets the use case component of the connection.
     *
     * @param useCase the new use case component
     */
    public void setUseCase(UseCaseComponent useCase) {
        this.useCase = useCase;
    }

    /**
     * Gets the first use case component of the connection.
     *
     * @return the first use case component
     */
    public UseCaseComponent getUseCase1() {
        return useCase1;
    }

    /**
     * Sets the first use case component of the connection.
     *
     * @param useCase1 the new first use case component
     */
    public void setUseCase1(UseCaseComponent useCase1) {
        this.useCase1 = useCase1;
    }

    /**
     * Gets the second use case component of the connection.
     *
     * @return the second use case component
     */
    public UseCaseComponent getUseCase2() {
        return useCase2;
    }

    /**
     * Sets the second use case component of the connection.
     *
     * @param useCase2 the new second use case component
     */
    public void setUseCase2(UseCaseComponent useCase2) {
        this.useCase2 = useCase2;
    }

    /**
     * Gets the label of the connection.
     *
     * @return the label
     */
    public String getLabel() {
        return label;
    }

    /**
     * Sets the label of the connection.
     *
     * @param label the new label
     */
    public void setLabel(String label) {
        this.label = label;
    }
}
