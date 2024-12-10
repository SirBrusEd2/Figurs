package com.example.labs_tasks;

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
            gc.setFill(Color.WHITE); // Мигание цветом фона
        } else {
            gc.setFill(decoratedShape.color);
        }
        decoratedShape.draw(gc, x, y, opacity);
        isBlinking = !isBlinking; // Переключение состояния мигания
    }
}