package com.example.labs_tasks.decorator;

import com.example.labs_tasks.model.Shape;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class HighlightDecorator extends ShapeDecorator {
    private Color highlightColor;
    private double strokeWidth = 2.0;

    public HighlightDecorator(Shape decoratedShape, Color color) {
        super(decoratedShape);
        this.highlightColor = color;
    }

    @Override
    public void draw(GraphicsContext gc, double x, double y, double opacity) {
        super.draw(gc, x, y, opacity);
        gc.setStroke(highlightColor);
        gc.setLineWidth(strokeWidth);
        super.drawStroke(gc, x, y);
    }

    public void setHighlightedColor(Color newColor) {
        this.highlightColor = newColor;
    }
}