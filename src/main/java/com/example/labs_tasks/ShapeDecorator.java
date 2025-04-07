package com.example.labs_tasks;

import javafx.scene.canvas.GraphicsContext;

public abstract class ShapeDecorator extends Shape {
    protected Shape decoratedShape;

    public ShapeDecorator(Shape decoratedShape) {
        // Вызываем правильный конструктор базового класса
        super(decoratedShape.getX(), decoratedShape.getY(), decoratedShape.color);
        this.decoratedShape = decoratedShape;
        this.gradient = decoratedShape.getGradient();
        this.hasAnimation = decoratedShape.hasAnimation();
    }

    @Override
    public void draw(GraphicsContext gc, double x, double y, double opacity) {
        decoratedShape.draw(gc, x, y, opacity);
    }
}