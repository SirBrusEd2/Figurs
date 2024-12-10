package com.example.labs_tasks;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

class Plus extends Shape {
    int x, y;
    double px, mx, py, my; // Параметры для плюса

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
        if (gradient != null) {
            gc.setFill(gradient); // Используем градиент, если он есть
        } else {
            gc.setFill(color); // Иначе используем обычный цвет
        }
        gc.setGlobalAlpha(opacity); // Устанавливаем прозрачность

        // Рисуем плюс
        gc.fillRect(x - px / 2, y - py / 2, px, py); // Вертикальная линия
        gc.fillRect(x - mx / 2, y - my / 2, mx, my); // Горизонтальная линия

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