package com.example.labs_tasks;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

class Rectangle extends Shape {
    private int width;
    private int height;

    Rectangle(int x, int y, int width, int height, Color color) {
        super(x, y, color);
        this.width = width;
        this.height = height;
    }

    @Override
    public void draw(GraphicsContext gc, double x, double y, double opacity) {
        if (gradient != null) {
            gc.setFill(gradient);
        } else {
            gc.setFill(color);
        }
        gc.setGlobalAlpha(opacity);
        gc.fillRect(x - width / 2.0, y - height / 2.0, width, height);
        gc.setGlobalAlpha(1.0);
    }
}