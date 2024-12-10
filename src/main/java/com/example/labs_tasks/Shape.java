package com.example.labs_tasks;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

public abstract class Shape {
    protected Color color;
    protected Paint gradient; // Поле для градиента
    protected boolean hasAnimation;

    public Shape(Color color) {
        this.color = color;
    }

    public abstract void draw(GraphicsContext gc, double x, double y, double opacity);

    public abstract int getX();

    public abstract int getY();

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