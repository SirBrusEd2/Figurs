package com.example.labs_tasks;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

class Triangle extends Shape {
    private double alf;
    private double bet;
    private double gam;

    Triangle(int x, int y, double alf, double bet, double gam, Color color) {
        super(x, y, color);
        this.alf = alf;
        this.bet = bet;
        this.gam = gam;
    }

    @Override
    public void draw(GraphicsContext gc, double x, double y, double opacity) {
        if (gradient != null) {
            gc.setFill(gradient);
        } else {
            gc.setFill(color);
        }
        gc.setGlobalAlpha(opacity);

        double halfAlf = alf / 2;
        double height = (Math.sqrt(3) / 2) * alf;

        gc.beginPath();
        gc.moveTo(x, y - height / 2);
        gc.lineTo(x - halfAlf, y + height / 2);
        gc.lineTo(x + halfAlf, y + height / 2);
        gc.closePath();
        gc.fill();

        gc.setGlobalAlpha(1.0);
    }
}