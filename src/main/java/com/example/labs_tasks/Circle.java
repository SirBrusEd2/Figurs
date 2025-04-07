package com.example.labs_tasks;

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
            gc.setFill(gradient); // Используем градиент, если он есть
        } else {
            gc.setFill(color); // Иначе используем обычный цвет
        }
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