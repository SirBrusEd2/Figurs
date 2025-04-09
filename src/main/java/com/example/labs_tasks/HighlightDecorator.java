package com.example.labs_tasks;

import javafx.geometry.Rectangle2D;
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
        // Рисуем оригинальную фигуру
        super.draw(gc, x, y, opacity);

        // Добавляем контур выделения
        gc.setStroke(highlightColor);
        gc.setLineWidth(strokeWidth);
        decoratedShape.drawStroke(gc, x, y); // Вызываем метод обводки
    }

    // Реализация метода drawStroke (делегируем вызов decoratedShape)
    @Override
    public void drawStroke(GraphicsContext gc, double x, double y) {
        decoratedShape.drawStroke(gc, x, y);
    }

    @Override
    public Rectangle2D getBounds() {
        return decoratedShape.getBounds();
    }

    @Override
    public boolean contains(double x, double y) {
        return decoratedShape.contains(x, y);
    }
    public void setHighlightedColor(Color newColor) {
        // Если decoratedShape является Shape, меняем его цвет
        if (decoratedShape instanceof Shape) {
            ((Shape) decoratedShape).color = newColor;
        }
    }
}