package com.example.labs_tasks;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

public abstract class Shape {
    protected int x;
    protected int y;
    protected Color color;
    protected Paint gradient;
    protected boolean hasAnimation;

    // Исправленный конструктор
    public Shape(int x, int y, Color color) {
        this.x = x;
        this.y = y;
        this.color = color;
    }

    public abstract void draw(GraphicsContext gc, double x, double y, double opacity);

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setHasAnimation(boolean hasAnimation) {
        this.hasAnimation = hasAnimation;
    }

    public boolean hasAnimation() {
        return hasAnimation;
    }

    // Установка градиента
    public void setGradient(Paint gradient) {
        this.gradient = gradient;
    }

    // Получение градиента
    public Paint getGradient() {
        return gradient;
    }
}