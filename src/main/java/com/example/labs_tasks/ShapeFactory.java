package com.example.labs_tasks;

import javafx.scene.paint.Color;

public class ShapeFactory {
    // Исправленный метод createTriangle с 4 параметрами
    public Shape createTriangle(int x, int y, double side, Color color) {
        return new Triangle(x, y, side, color); // 4 параметра
    }

    public Shape createCircle(int x, int y, int r, Color color) {
        return new Circle(x, y, r, color);
    }

    public Shape createRectangle(int x, int y, int w, int h, Color color) {
        return new Rectangle(x, y, w, h, color);
    }

    public Shape createPlus(int x, int y, double size, Color color) {
        double thickness = size * 0.3;
        return new Plus(x, y, thickness, size, size, thickness, color);
    }
}