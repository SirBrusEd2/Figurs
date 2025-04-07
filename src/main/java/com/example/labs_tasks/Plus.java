package com.example.labs_tasks;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

class Plus extends Shape {
    private double px;
    private double mx;
    private double py;
    private double my;

    Plus(int x, int y, double px, double mx, double py, double my, Color color) {
        super(x, y, color);
        this.px = px;
        this.mx = mx;
        this.py = py;
        this.my = my;
    }

    @Override
    public void draw(GraphicsContext gc, double x, double y, double opacity) {
        if (gradient != null) {
            gc.setFill(gradient);
        } else {
            gc.setFill(color);
        }
        gc.setGlobalAlpha(opacity);

        // Ваша оригинальная логика рисования
        gc.fillRect(x - px / 2, y - py / 2, px, py); // Вертикальная линия
        gc.fillRect(x - mx / 2, y - my / 2, mx, my); // Горизонтальная линия

        gc.setGlobalAlpha(1.0);
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