package com.example.umleditor.ui.components;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import java.io.Serializable;

public class Connection implements Serializable {
    private static final long serialVersionUID = 1L;
    private ActorComponent actor;
    private UseCaseComponent useCase;
    private UseCaseComponent useCase1;
    private UseCaseComponent useCase2;
    private String label;

    public Connection(ActorComponent actor, UseCaseComponent useCase) {
        this.actor = actor;
        this.useCase = useCase;
    }

    public Connection(UseCaseComponent useCase1, UseCaseComponent useCase2, String label) {
        this.useCase1 = useCase1;
        this.useCase2 = useCase2;
        this.label = label;
    }

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

    private double[] getEdgePoint(double x1, double y1, double x2, double y2, double radius) {
        double angle = Math.atan2(y2 - y1, x2 - x1);
        double x = x1 + radius * Math.cos(angle);
        double y = y1 + radius * Math.sin(angle);
        return new double[]{x, y};
    }

    // Getters and setters
    public ActorComponent getActor() {
        return actor;
    }

    public void setActor(ActorComponent actor) {
        this.actor = actor;
    }

    public UseCaseComponent getUseCase() {
        return useCase;
    }

    public void setUseCase(UseCaseComponent useCase) {
        this.useCase = useCase;
    }

    public UseCaseComponent getUseCase1() {
        return useCase1;
    }

    public void setUseCase1(UseCaseComponent useCase1) {
        this.useCase1 = useCase1;
    }

    public UseCaseComponent getUseCase2() {
        return useCase2;
    }

    public void setUseCase2(UseCaseComponent useCase2) {
        this.useCase2 = useCase2;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label =label;
    }
}