package com.example.umleditor.ui.components;

import com.example.umleditor.data.UseCase;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;
import java.io.Serializable;

/**
 * Represents a use case component in a UML use case diagram.
 * This class handles the rendering and selection of use cases.
 */
public class UseCaseComponent implements Serializable {
    private static final long serialVersionUID = 1L;
    private UseCase useCase;
    private boolean isSelected;
    private String text;

    /**
     * Constructs a UseCaseComponent with the specified use case data.
     *
     * @param useCase the use case data
     */
    public UseCaseComponent(UseCase useCase) {
        this.useCase = useCase;
        this.isSelected = false;
        this.text = "";
    }

    /**
     * Draws the use case component on the canvas.
     *
     * @param gc the graphics context used for drawing
     */
    public void draw(GraphicsContext gc) {
        gc.setLineWidth(2);
        gc.setStroke(Color.BLACK);

        // Draw use case (ellipse)
        gc.strokeOval(useCase.getX() - useCase.getWidth() / 2, useCase.getY() - useCase.getHeight() / 2, useCase.getWidth(), useCase.getHeight());

        // Draw selection box if selected
        if (isSelected) {
            gc.setLineDashes(5);
            gc.setStroke(Color.BLUE);
            gc.strokeRect(useCase.getX() - useCase.getWidth() / 2 - 5, useCase.getY() - useCase.getHeight() / 2 - 5, useCase.getWidth() + 10, useCase.getHeight() + 10);
            gc.setLineDashes(0); // Reset line dashes
        }

        // Draw text inside the use case
        if (!text.isEmpty()) {
            gc.setFill(Color.BLACK);
            gc.setTextAlign(TextAlignment.CENTER);
            gc.fillText(text, useCase.getX(), useCase.getY());
        }
    }

    /**
     * Checks if the use case component is selected.
     *
     * @return true if the component is selected, false otherwise
     */
    public boolean isSelected() {
        return isSelected;
    }

    /**
     * Sets the selection state of the use case component.
     *
     * @param selected the new selection state
     */
    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    /**
     * Gets the text associated with the use case component.
     *
     * @return the text
     */
    public String getText() {
        return text;
    }

    /**
     * Sets the text associated with the use case component.
     *
     * @param text the new text
     */
    public void setText(String text) {
        this.text = text;
    }

    /**
     * Gets the use case data associated with the component.
     *
     * @return the use case data
     */
    public UseCase getUseCase() {
        return useCase;
    }

    /**
     * Sets the use case data associated with the component.
     *
     * @param useCase the new use case data
     */
    public void setUseCase(UseCase useCase) {
        this.useCase = useCase;
    }
}
