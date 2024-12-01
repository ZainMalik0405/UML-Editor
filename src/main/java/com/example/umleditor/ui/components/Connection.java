package com.example.umleditor.ui.components;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Connection {
    private ActorComponent actor;
    private UseCaseComponent useCase;

    public Connection(ActorComponent actor, UseCaseComponent useCase) {
        this.actor = actor;
        this.useCase = useCase;
    }

    public void draw(GraphicsContext gc) {
        gc.setLineWidth(2);
        gc.setStroke(Color.BLACK);

        // Draw line from actor to use case
        double[] actorEdge = getEdgePoint(actor.getActor().getX(), actor.getActor().getY(), useCase.getUseCase().getX(), useCase.getUseCase().getY(), actor.getActor().getSize() / 2 + 10);
        double[] useCaseEdge = getEdgePoint(useCase.getUseCase().getX(), useCase.getUseCase().getY(), actor.getActor().getX(), actor.getActor().getY(), Math.max(useCase.getUseCase().getWidth(), useCase.getUseCase().getHeight()) / 2 + 10);
        gc.strokeLine(actorEdge[0], actorEdge[1], useCaseEdge[0], useCaseEdge[1]);
    }

    private double[] getEdgePoint(double x1, double y1, double x2, double y2, double radius) {
        double angle = Math.atan2(y2 - y1, x2 - x1);
        double x = x1 + radius * Math.cos(angle);
        double y = y1 + radius * Math.sin(angle);
        return new double[]{x, y};
    }

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
}
