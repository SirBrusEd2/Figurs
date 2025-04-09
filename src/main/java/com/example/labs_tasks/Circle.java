package com.example.labs_tasks;

import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

class Circle extends Shape {
    int r;

    Circle(int x, int y, int r, Color color) {
        super(x, y, color);
        this.r = r;
    }

    @Override
    public void draw(GraphicsContext gc, double x, double y, double opacity) {
        if (gradient != null) {
            gc.setFill(gradient);
        } else {
            gc.setFill(color);
        }
        gc.setGlobalAlpha(opacity);
        gc.fillOval(x - r, y - r, 2 * r, 2 * r);
        gc.setGlobalAlpha(1.0);
    }

    @Override
    public boolean contains(double x, double y) {
        double dx = x - this.x;
        double dy = y - this.y;
        return dx*dx + dy*dy <= r*r;
    }

    @Override
    public Rectangle2D getBounds() {
        return new Rectangle2D(x - r, y - r, 2*r, 2*r);
    }
    @Override
    public void drawStroke(GraphicsContext gc, double x, double y) {
        gc.strokeOval(x - r, y - r, 2 * r, 2 * r);
    }
}