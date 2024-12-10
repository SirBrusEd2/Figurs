package com.example.labs_tasks;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

class Rectangle extends Shape {
    int x, y, width, height;

    Rectangle(int x, int y, int width, int height, Color color) {
        super(color);
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    @Override
    public void draw(GraphicsContext gc, double x, double y, double opacity) {
        if (gradient != null) {
            gc.setFill(gradient); // Используем градиент, если он есть
        } else {
            gc.setFill(color); // Иначе используем обычный цвет
        }
        gc.setGlobalAlpha(opacity); // Устанавливаем прозрачность
        gc.fillRect(x - width / 2, y - height / 2, width, height);
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