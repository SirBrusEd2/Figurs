package com.example.labs_tasks;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public abstract class ShapeDecorator extends Shape {
    protected Shape decoratedShape;
    protected int x;
    protected int y;

    public ShapeDecorator(Shape decoratedShape) {
        super(decoratedShape.color);
        this.decoratedShape = decoratedShape;
        this.x = decoratedShape.getX();
        this.y = decoratedShape.getY();
    }

    @Override
    public void draw(GraphicsContext gc, double x, double y, double opacity) {
        decoratedShape.draw(gc, x, y, opacity);
    }

    @Override
    public int getX() {
        return x;
    }

    @Override
    public int getY() {
        return y;
    }
}