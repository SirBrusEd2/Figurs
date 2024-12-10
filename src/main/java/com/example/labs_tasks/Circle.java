package com.example.labs_tasks;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

class Circle extends Shape {
    int x, y, r;

    Circle(int x, int y, int r, Color color) {
        super(color);
        this.x = x;
        this.y = y;
        this.r = r;
    }

    @Override
    public void draw(GraphicsContext gc, double x, double y, double opacity) {
        gc.setFill(color);
        gc.setGlobalAlpha(opacity); // Устанавливаем прозрачность
        gc.fillOval(x - r, y - r, 2 * r, 2 * r);
        gc.setGlobalAlpha(1.0); // Сбрасываем прозрачность
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