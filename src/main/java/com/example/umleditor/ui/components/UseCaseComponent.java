package com.example.umleditor.ui.components;

import com.example.umleditor.data.UseCase;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;
import java.io.Serializable;

public class UseCaseComponent implements Serializable {
    private static final long serialVersionUID = 1L;
    private UseCase useCase;
    private boolean isSelected;
    private String text;

    public UseCaseComponent(UseCase useCase) {
        this.useCase = useCase;
        this.isSelected = false;
        this.text = "";
    }

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

    public UseCase getUseCase() {
        return useCase;
    }

    public void setUseCase(UseCase useCase) {
        this.useCase = useCase;
    }
}