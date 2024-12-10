package com.example.labs_tasks;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

class Triangle extends Shape {
    int x, y;
    double alf, bet, gam; // Стороны треугольника

    Triangle(int x, int y, double alf, double bet, double gam, Color color) {
        super(color);
        this.x = x;
        this.y = y;
        this.alf = alf;
        this.bet = bet;
        this.gam = gam;
    }

    @Override
    public void draw(GraphicsContext gc, double x, double y, double opacity) {
        if (gradient != null) {
            gc.setFill(gradient); // Используем градиент, если он есть
        } else {
            gc.setFill(color); // Иначе используем обычный цвет
        }
        gc.setGlobalAlpha(opacity); // Устанавливаем прозрачность

        // Рисуем треугольник
        double halfAlf = alf / 2;
        double height = (Math.sqrt(3) / 2) * alf;

        gc.beginPath();
        gc.moveTo(x, y - height / 2);
        gc.lineTo(x - halfAlf, y + height / 2);
        gc.lineTo(x + halfAlf, y + height / 2);
        gc.closePath();
        gc.fill();

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