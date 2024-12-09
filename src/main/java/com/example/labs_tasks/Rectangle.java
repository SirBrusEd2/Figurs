package com.example.labs_tasks;

import javafx.scene.paint.Color;

class Rectangle extends Shape {
    int x, y, w, h;

    Rectangle(int x, int y, int w, int h, Color color) {
        super(color);
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
    }
}