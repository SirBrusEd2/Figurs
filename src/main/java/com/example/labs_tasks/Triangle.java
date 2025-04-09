package com.example.labs_tasks;

import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

class Triangle extends Shape {
    private double side; // Длина стороны равностороннего треугольника

    // Конструктор с параметрами: координаты (x, y), длина стороны, цвет
    Triangle(int x, int y, double side, Color color) {
        super(x, y, color);
        this.side = side;
    }

    @Override
    public void draw(GraphicsContext gc, double x, double y, double opacity) {
        if (gradient != null) {
            gc.setFill(gradient);
        } else {
            gc.setFill(color);
        }
        gc.setGlobalAlpha(opacity);

        // Отрисовка равностороннего треугольника
        double height = (Math.sqrt(3) / 2) * side; // Высота треугольника
        double halfSide = side / 2;

        gc.beginPath();
        gc.moveTo(x, y - height / 2);          // Верхняя вершина
        gc.lineTo(x - halfSide, y + height / 2); // Левая нижняя вершина
        gc.lineTo(x + halfSide, y + height / 2); // Правая нижняя вершина
        gc.closePath();
        gc.fill();

        gc.setGlobalAlpha(1.0); // Сброс прозрачности
    }

    @Override
    public boolean contains(double x, double y) {
        // Проверка вхождения точки через геометрию треугольника
        double triangleHeight = (Math.sqrt(3) / 2) * side;
        double relativeX = x - this.x;
        double relativeY = y - this.y;

        // Верхняя половина треугольника (выше центра)
        if (relativeY < -triangleHeight / 2 || relativeY > triangleHeight / 2) {
            return false;
        }

        // Проверка по горизонтали внутри границ
        double slope = (triangleHeight / 2) / (side / 2);
        return Math.abs(relativeX) <= (side / 2) - (relativeY * slope);
    }

    @Override
    public Rectangle2D getBounds() {
        // Границы, охватывающие треугольник
        double height = (Math.sqrt(3) / 2) * side;
        return new Rectangle2D(
                this.x - side / 2, // Минимальный X
                this.y - height / 2, // Минимальный Y
                side, // Ширина
                height // Высота
        );
    }
    @Override
    public void drawStroke(GraphicsContext gc, double x, double y) {
        double height = (Math.sqrt(3) / 2) * side;
        double halfSide = side / 2;

        gc.beginPath();
        gc.moveTo(x, y - height / 2);
        gc.lineTo(x - halfSide, y + height / 2);
        gc.lineTo(x + halfSide, y + height / 2);
        gc.closePath();
        gc.stroke();
    }
}