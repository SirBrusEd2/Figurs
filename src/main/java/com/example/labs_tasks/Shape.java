package com.example.labs_tasks;

import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

public abstract class Shape implements Component {
    protected int x;
    protected int y;
    protected Color color;
    protected Paint gradient;
    protected boolean hasAnimation;

    public Shape(int x, int y, Color color) {
        this.x = x;
        this.y = y;
        this.color = color;
    }

    // Реализация методов Component
    @Override
    public void add(Component component) {
        throw new UnsupportedOperationException("Shape cannot have children");
    }

    @Override
    public void remove(Component component) {
        throw new UnsupportedOperationException("Shape cannot have children");
    }

    @Override
    public void draw(GraphicsContext gc, double opacity) {
        draw(gc, x, y, opacity);
    }

    // Методы для градиента и анимации
    public void setGradient(Paint gradient) {
        this.gradient = gradient;
    }

    public void setHasAnimation(boolean hasAnimation) {
        this.hasAnimation = hasAnimation;
    }

    public boolean hasAnimation() {
        return hasAnimation;
    }

    // Абстрактные методы
    public abstract void draw(GraphicsContext gc, double x, double y, double opacity);
    public abstract boolean contains(double x, double y);
    public abstract Rectangle2D getBounds(); // Добавлен абстрактный метод

    public boolean intersects(double rectX1, double rectY1, double rectX2, double rectY2) {
        return this.getBounds().intersects(
                Math.min(rectX1, rectX2),
                Math.min(rectY1, rectY2),
                Math.abs(rectX2 - rectX1),
                Math.abs(rectY2 - rectY1)
        );
    }
    public abstract void drawStroke(GraphicsContext gc, double x, double y);
    // Геттеры
    public int getX() { return x; }
    public int getY() { return y; }
    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }
}