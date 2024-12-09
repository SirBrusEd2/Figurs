package com.example.labs_tasks;

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
}