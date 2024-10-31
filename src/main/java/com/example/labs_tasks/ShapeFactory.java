package com.example.labs_tasks;

import javafx.scene.paint.Color;

public class ShapeFactory {

    public Shape createCircle(int x, int y, int r, Color color) {
        return new Circle(x, y, r, color);
    }

    public Shape createTriangle(int x, int y, double alf, double bet, double gam, Color color) {
        return new Triangle(x, y, alf, bet, gam, color);
    }

    public Shape createRectangle(int x, int y, int w, int h, Color color) {
        return new Rectangle(x, y, w, h, color);
    }

    public Shape createPlus(int x, int y, double px, double mx, double py, double my, Color color) {
        return new Plus(x, y, px, mx, py, my, color);
    }
}