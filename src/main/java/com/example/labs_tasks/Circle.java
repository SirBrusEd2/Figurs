package com.example.labs_tasks;

import javafx.scene.paint.Color;

class Circle extends Shape {
    int x, y, r;

    Circle(int x, int y, int r, Color color) {
        super(color);
        this.x = x;
        this.y = y;
        this.r = r;
    }
}