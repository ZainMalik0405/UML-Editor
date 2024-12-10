package com.example.umleditor.ui.components;

import com.example.umleditor.data.Actor;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;
import java.io.Serializable;

/**
 * Represents an actor component in a UML diagram.
 * This class handles the rendering and selection of actors.
 */
public class ActorComponent implements Serializable {
    private static final long serialVersionUID = 1L;
    private Actor actor;
    private boolean isSelected;
    private String text;

    /**
     * Constructs an ActorComponent with the specified actor data.
     *
     * @param actor the actor data
     */
    public ActorComponent(Actor actor) {
        this.actor = actor;
        this.isSelected = false;
        this.text = "";
    }

    /**
     * Draws the actor component on the canvas.
     *
     * @param gc the graphics context used for drawing
     */
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

    /**
     * Checks if the actor component is selected.
     *
     * @return true if the component is selected, false otherwise
     */
    public boolean isSelected() {
        return isSelected;
    }

    /**
     * Sets the selection state of the actor component.
     *
     * @param selected the new selection state
     */
    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    /**
     * Gets the text associated with the actor component.
     *
     * @return the text
     */
    public String getText() {
        return text;
    }

    /**
     * Sets the text associated with the actor component.
     *
     * @param text the new text
     */
    public void setText(String text) {
        this.text = text;
    }

    /**
     * Gets the actor data associated with the component.
     *
     * @return the actor data
     */
    public Actor getActor() {
        return actor;
    }

    /**
     * Sets the actor data associated with the component.
     *
     * @param actor the new actor data
     */
    public void setActor(Actor actor) {
        this.actor = actor;
    }
}
