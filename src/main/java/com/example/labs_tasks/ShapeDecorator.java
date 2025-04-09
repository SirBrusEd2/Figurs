package com.example.labs_tasks;

import javafx.scene.canvas.GraphicsContext;

public abstract class ShapeDecorator extends Shape {
    protected Shape decoratedShape;

    public ShapeDecorator(Shape decoratedShape) {
        // Обращение напрямую к полям класса Shape
        super(decoratedShape.getX(), decoratedShape.getY(), decoratedShape.color);
        this.decoratedShape = decoratedShape;
        this.gradient = decoratedShape.gradient;      // Прямой доступ к полю gradient
        this.hasAnimation = decoratedShape.hasAnimation; // Прямой доступ к полю hasAnimation
    }

    @Override
    public void draw(GraphicsContext gc, double x, double y, double opacity) {
        decoratedShape.draw(gc, x, y, opacity);
    }
    public Shape getDecoratedShape() {
        return decoratedShape;
    }
    @Override
    public void setX(int x) {
        decoratedShape.setX(x);
        super.setX(x); // Обновляем координату декоратора
    }

    @Override
    public void setY(int y) {
        decoratedShape.setY(y);
        super.setY(y);
    }
}