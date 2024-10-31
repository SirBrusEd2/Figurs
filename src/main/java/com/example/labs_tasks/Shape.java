package com.example.labs_tasks;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

abstract class Shape {
    int x;
    int y;
    Color color;

    public Shape(Color color) {
        this.color = color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

}
