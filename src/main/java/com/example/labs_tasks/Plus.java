package com.example.labs_tasks;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

class Plus extends Shape {
    int x, y;
    double px, mx, py, my;

    Plus(int x, int y, double px, double mx, double py, double my, Color color) {
        super(color);
        this.x = x;
        this.y = y;
        this.px = px;
        this.mx = mx;
        this.py = py;
        this.my = my;
    }

    @Override
    public void draw(GraphicsContext gc, double x, double y, double opacity) {
        gc.setFill(color);
        gc.setGlobalAlpha(opacity); // Устанавливаем прозрачность
        gc.fillRect(x - px / 4, y - py / 2, px / 2, py);
        gc.fillRect(x - px / 2, y - py / 4, px, py / 2);
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