package com.example.labs_tasks;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

class Triangle extends Shape {
    int x, y;
    double alf, bet, gam;

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
        double halfSide = alf / 2;
        double height = Math.sqrt(3) * halfSide;
        gc.setFill(color);
        gc.setGlobalAlpha(opacity); // Устанавливаем прозрачность
        gc.fillPolygon(new double[]{x, x - halfSide, x + halfSide}, new double[]{y - height / 2, y + height / 2, y + height / 2}, 3);
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