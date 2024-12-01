package com.example.umleditor.ui.components;

import com.example.umleditor.data.Actor;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;

public class ActorComponent {
    private Actor actor;
    private boolean isSelected;
    private String text;

    public ActorComponent(Actor actor) {
        this.actor = actor;
        this.isSelected = false;
        this.text = "";
    }

    public void draw(GraphicsContext gc) {
        gc.setLineWidth(2);
        gc.setStroke(Color.BLACK);

        // Draw actor (head + body + legs)
        gc.strokeOval(actor.getX() - actor.getSize() / 2, actor.getY() - actor.getSize() / 2, actor.getSize(), actor.getSize());  // Head
        gc.strokeLine(actor.getX(), actor.getY() + actor.getSize() / 2, actor.getX(), actor.getY() + actor.getSize() * 1.5);  // Body
        gc.strokeLine(actor.getX(), actor.getY() + actor.getSize() / 2, actor.getX() - actor.getSize() * 0.5, actor.getY() + actor.getSize()); // Left Arm
        gc.strokeLine(actor.getX(), actor.getY() + actor.getSize() / 2, actor.getX() + actor.getSize() * 0.5, actor.getY() + actor.getSize()); // Right Arm
        gc.strokeLine(actor.getX(), actor.getY() + actor.getSize() * 1.5, actor.getX() - actor.getSize() * 0.5, actor.getY() + actor.getSize() * 2); // Left Leg
        gc.strokeLine(actor.getX(), actor.getY() + actor.getSize() * 1.5, actor.getX() + actor.getSize() * 0.5, actor.getY() + actor.getSize() * 2); // Right Leg

        // Draw selection box if selected
        if (isSelected) {
            gc.setLineDashes(5);
            gc.setStroke(Color.BLUE);
            gc.strokeRect(actor.getX() - actor.getSize() / 2 - 5, actor.getY() - actor.getSize() / 2 - 5, actor.getSize() + 10, actor.getSize() * 2.5 + 10);
            gc.setLineDashes(0); // Reset line dashes
        }

        // Draw text at the bottom of the actor
        if (!text.isEmpty()) {
            gc.setFill(Color.BLACK);
            gc.setTextAlign(TextAlignment.CENTER);
            gc.fillText(text, actor.getX(), actor.getY() + actor.getSize() * 2.5 + 20);
        }
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Actor getActor() {
        return actor;
    }

    public void setActor(Actor actor) {
        this.actor = actor;
    }
}
