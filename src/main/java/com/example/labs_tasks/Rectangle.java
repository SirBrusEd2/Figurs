package com.example.labs_tasks;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

class Rectangle extends Shape {
    int x, y, w, h;

    Rectangle(int x, int y, int w, int h, Color color) {
        super(color);
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
    }

    @Override
    public void draw(GraphicsContext gc, double x, double y, double opacity) {
        gc.setFill(color);
        gc.setGlobalAlpha(opacity); // Устанавливаем прозрачность
        gc.fillRect(x - w / 2, y - h / 2, w, h);
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