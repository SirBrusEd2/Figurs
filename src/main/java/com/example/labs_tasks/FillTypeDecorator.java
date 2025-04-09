package com.example.labs_tasks;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Paint;
import javafx.geometry.Rectangle2D;

public class FillTypeDecorator extends ShapeDecorator {
    private Paint fillType;

    public FillTypeDecorator(Shape decoratedShape, Paint fillType) {
        super(decoratedShape);
        this.fillType = fillType;
    }

    @Override
    public void draw(GraphicsContext gc, double x, double y, double opacity) {
        gc.setFill(fillType);
        super.draw(gc, x, y, opacity);
        gc.fill();
    }

    // Реализация метода drawStroke
    @Override
    public void drawStroke(GraphicsContext gc, double x, double y) {
        decoratedShape.drawStroke(gc, x, y); // Делегируем вызов оригинальной фигуре
    }

    @Override
    public boolean contains(double x, double y) {
        return decoratedShape.contains(x, y);
    }

    @Override
    public void add(Component component) {
        throw new UnsupportedOperationException("FillTypeDecorator cannot add components");
    }

    @Override
    public void remove(Component component) {
        throw new UnsupportedOperationException("FillTypeDecorator cannot remove components");
    }

    @Override
    public Rectangle2D getBounds() {
        return decoratedShape.getBounds();
    }
}