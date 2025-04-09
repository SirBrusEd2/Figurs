package com.example.labs_tasks;

import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class BlinkingShapeDecorator extends ShapeDecorator {
    private boolean isBlinking = false;

    public BlinkingShapeDecorator(Shape decoratedShape) {
        super(decoratedShape);
    }

    @Override
    public void draw(GraphicsContext gc, double x, double y, double opacity) {
        if (isBlinking) {
            gc.setFill(Color.WHITE);
        } else {
            // Берём актуальный цвет из базовой фигуры
            gc.setFill(decoratedShape.color);
        }
        super.draw(gc, x, y, opacity);
        isBlinking = !isBlinking;
    }

    // Реализация метода drawStroke()
    @Override
    public void drawStroke(GraphicsContext gc, double x, double y) {
        decoratedShape.drawStroke(gc, x, y); // Делегируем вызов оригинальной фигуре
    }

    @Override
    public Rectangle2D getBounds() {
        return decoratedShape.getBounds();
    }

    @Override
    public boolean contains(double x, double y) {
        return decoratedShape.contains(x, y);
    }

    @Override
    public void add(Component component) {
        throw new UnsupportedOperationException("BlinkingShapeDecorator cannot add components");
    }

    @Override
    public void remove(Component component) {
        throw new UnsupportedOperationException("BlinkingShapeDecorator cannot remove components");
    }
}