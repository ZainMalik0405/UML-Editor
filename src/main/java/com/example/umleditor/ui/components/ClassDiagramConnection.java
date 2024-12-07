package com.example.umleditor.ui.components;
import java.io.Serializable;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class ClassDiagramConnection implements Serializable{
    private static final long serialVersionUID = 1L;
    private ClassComponent start;
    private ClassComponent end;
    private ArrowType type;
    private String startMultiplicity;
    private String endMultiplicity;

    public ClassDiagramConnection(ClassComponent start, ClassComponent end, ArrowType type, String startMultiplicity, String endMultiplicity) {
        this.start = start;
        this.end = end;
        this.type = type;
        this.startMultiplicity = startMultiplicity;
        this.endMultiplicity = endMultiplicity;
    }

    public void draw(GraphicsContext gc) {
        gc.setLineWidth(2);
        gc.setStroke(Color.BLACK);

        double[] startEdge = getEdgePoint(start, end);
        double[] endEdge = getEdgePoint(end, start);

        // Draw line between start and end
        gc.strokeLine(startEdge[0], startEdge[1], endEdge[0], endEdge[1]);

        switch (type) {
            case GENERALIZATION:
                drawTriangle(gc, endEdge[0], endEdge[1], startEdge[0], startEdge[1]);
                break;
            case AGGREGATION:
                drawDiamond(gc, endEdge[0], endEdge[1], startEdge[0], startEdge[1]);
                break;
            case COMPOSITION:
                drawFilledDiamond(gc, endEdge[0], endEdge[1], startEdge[0], startEdge[1]);
                break;
            case ASSOCIATION:
                drawArrowHead(gc, endEdge[0], endEdge[1], startEdge[0], startEdge[1]);
                break;
        }

        // Draw multiplicity outside the class components with dynamic alignment
        drawMultiplicity(gc, startEdge, endEdge, startMultiplicity, true);
        drawMultiplicity(gc, endEdge, startEdge, endMultiplicity, false);
    }

    private void drawMultiplicity(GraphicsContext gc, double[] edge, double[] oppositeEdge, String multiplicity, boolean isStart) {
        double offset = 20;
        double x = edge[0];
        double y = edge[1];

        // Adjust placement based on the relative position of the opposite edge
        if (isStart) {
            if (oppositeEdge[1] > edge[1]) {
                y += offset;  // Place below if the opposite edge is lower
            } else {
                y -= offset;  // Place above if the opposite edge is higher
            }
            gc.fillText(multiplicity, x - offset, y);
        } else {
            if (oppositeEdge[1] > edge[1]) {
                y += offset;  // Place below if the opposite edge is lower
            } else {
                y -= offset;  // Place above if the opposite edge is higher
            }
            gc.fillText(multiplicity, x + offset, y);
        }
    }

    private double[] getEdgePoint(ClassComponent from, ClassComponent to) {
        double x1 = from.getX();
        double y1 = from.getY();
        double x2 = to.getX();
        double y2 = to.getY();
        double width = from.getWidth() / 2;
        double height = (60 + from.getAttributes().size() * 20 + from.getMethods().size() * 20) / 2;

        double angle = Math.atan2(y2 - y1, x2 - x1);
        double edgeX = x1 + (width * Math.cos(angle));
        double edgeY = y1 + (height * Math.sin(angle));

        // Adjust for bottom of methods block boundary
        if (Math.abs(y2 - y1) > Math.abs(x2 - x1)) {
            edgeY = y1 + height * (y2 > y1 ? 1 : -1);
        } else {
            edgeX = x1 + width * (x2 > x1 ? 1 : -1);
        }

        return new double[]{edgeX, edgeY};
    }

    private void drawTriangle(GraphicsContext gc, double x1, double y1, double x2, double y2) {
        double angle = Math.atan2(y2 - y1, x2 - x1);
        double arrowLength = 15;
        double x3 = x2 - arrowLength * Math.cos(angle - Math.PI / 6);
        double y3 = y2 - arrowLength * Math.sin(angle - Math.PI / 6);
        double x4 = x2 - arrowLength * Math.cos(angle + Math.PI / 6);
        double y4 = y2 - arrowLength * Math.sin(angle + Math.PI / 6);
        gc.strokePolygon(new double[]{x2, x3, x4}, new double[]{y2, y3, y4}, 3);
    }

    private void drawDiamond(GraphicsContext gc, double x1, double y1, double x2, double y2) {
        double angle = Math.atan2(y2 - y1, x2 - x1);
        double diamondLength = 20;
        double diamondSide = 10;
        double x3 = x2 - diamondSide * Math.cos(angle - Math.PI / 6);
        double y3 = y2 - diamondSide * Math.sin(angle - Math.PI / 6);
        double x4 = x2 - diamondSide * Math.cos(angle + Math.PI / 6);
        double y4 = y2 - diamondSide * Math.sin(angle + Math.PI / 6);
        double x5 = x2 - 2 * diamondSide * Math.cos(angle);
        double y5 = y2 - 2 * diamondSide * Math.sin(angle);
        gc.strokePolygon(new double[]{x2, x3, x5, x4}, new double[]{y2, y3, y5, y4}, 4);
    }

    private void drawFilledDiamond(GraphicsContext gc, double x1, double y1, double x2, double y2) {
        double angle = Math.atan2(y2 - y1, x2 - x1);
        double diamondLength = 20;
        double diamondSide = 10;
        double x3 = x2 - diamondSide * Math.cos(angle - Math.PI / 6);
        double y3 = y2 - diamondSide * Math.sin(angle - Math.PI / 6);
        double x4 = x2 - diamondSide * Math.cos(angle + Math.PI / 6);
        double y4 = y2 - diamondSide * Math.sin(angle + Math.PI / 6);
        double x5 = x2 - 2 * diamondSide * Math.cos(angle);
        double y5 = y2 - 2 * diamondSide * Math.sin(angle);
        gc.fillPolygon(new double[]{x2, x3, x5, x4}, new double[]{y2, y3, y5, y4}, 4);
    }

    private void drawArrowHead(GraphicsContext gc, double x1, double y1, double x2, double y2) {
        double angle = Math.atan2(y2 - y1, x2 - x1);
        double arrowLength = 15;
        double x3 = x2 - arrowLength * Math.cos(angle - Math.PI / 6);
        double y3 = y2 - arrowLength * Math.sin(angle - Math.PI / 6);
        double x4 = x2 - arrowLength * Math.cos(angle + Math.PI / 6);
        double y4 = y2 - arrowLength * Math.sin(angle + Math.PI / 6);
        gc.strokeLine(x2, y2, x3, y3);
        gc.strokeLine(x2, y2, x4, y4);
    }

    // Getters and setters
    public ClassComponent getStart() {
        return start;
    }

    public void setStart(ClassComponent start) {
        this.start = start;
    }

    public ClassComponent getEnd() {
        return end;
    }

    public void setEnd(ClassComponent end) {
        this.end = end;
    }

    public ArrowType getType() {
        return type;
    }

    public void setType(ArrowType type) {
        this.type = type;
    }

    public String getStartMultiplicity() {
        return startMultiplicity;
    }

    public void setStartMultiplicity(String startMultiplicity) {
        this.startMultiplicity = startMultiplicity;
    }

    public String getEndMultiplicity() {
        return endMultiplicity;
    }

    public void setEndMultiplicity(String endMultiplicity) {
        this.endMultiplicity = endMultiplicity;
    }
}