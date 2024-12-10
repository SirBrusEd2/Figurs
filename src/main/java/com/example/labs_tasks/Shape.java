package com.example.labs_tasks;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

abstract class Shape {
    Color color;
    protected int x;
    protected int y;
    protected boolean hasAnimation; // Добавлено поле для хранения информации о наличии анимации

    public Shape(Color color) {
        this.color = color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public abstract void draw(GraphicsContext gc, double x, double y, double opacity);

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public boolean hasAnimation() {
        return hasAnimation;
    }

    public void setHasAnimation(boolean hasAnimation) {
        this.hasAnimation = hasAnimation;
    }
}