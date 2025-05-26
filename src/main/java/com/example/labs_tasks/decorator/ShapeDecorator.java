package com.example.labs_tasks.decorator;

import com.example.labs_tasks.model.Shape;
import javafx.scene.canvas.GraphicsContext;
import javafx.geometry.Rectangle2D;

public abstract class ShapeDecorator extends Shape {
    protected final Shape decoratedShape;

    public ShapeDecorator(Shape decoratedShape) {
        super(0, 0, decoratedShape.color);
        this.decoratedShape = decoratedShape;
        this.gradient = decoratedShape.gradient;
        this.hasAnimation = decoratedShape.hasAnimation;
    }

    @Override
    public void draw(GraphicsContext gc, double x, double y, double opacity) {
        decoratedShape.draw(gc, x, y, opacity);
    }

    @Override
    public void drawStroke(GraphicsContext gc, double x, double y) {
        decoratedShape.drawStroke(gc, x, y);
    }

    @Override
    public boolean contains(double x, double y) {
        return decoratedShape.contains(x, y);
    }

    @Override
    public Rectangle2D getBounds() {
        return decoratedShape.getBounds();
    }

    @Override
    public int getX() { return decoratedShape.getX(); }

    @Override
    public int getY() { return decoratedShape.getY(); }

    @Override
    public void setX(int x) { decoratedShape.setX(x); }

    @Override
    public void setY(int y) { decoratedShape.setY(y); }

    public Shape getDecoratedShape() {
        return decoratedShape;
    }
}