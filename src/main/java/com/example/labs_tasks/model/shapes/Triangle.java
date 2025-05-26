package com.example.labs_tasks.model.shapes;

import com.example.labs_tasks.model.Shape;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Triangle extends Shape {
    private double side;

    public Triangle(int x, int y, double side, Color color) {
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

        double height = (Math.sqrt(3) / 2) * side;
        double halfSide = side / 2;

        gc.beginPath();
        gc.moveTo(x, y - height / 2);
        gc.lineTo(x - halfSide, y + height / 2);
        gc.lineTo(x + halfSide, y + height / 2);
        gc.closePath();
        gc.fill();

        gc.setGlobalAlpha(1.0);
    }

    @Override
    public boolean contains(double x, double y) {
        double triangleHeight = (Math.sqrt(3) / 2) * side;
        double relativeX = x - this.x;
        double relativeY = y - this.y;

        if (relativeY < -triangleHeight / 2 || relativeY > triangleHeight / 2) {
            return false;
        }

        double slope = (triangleHeight / 2) / (side / 2);
        return Math.abs(relativeX) <= (side / 2) - (relativeY * slope);
    }

    @Override
    public Rectangle2D getBounds() {
        double height = (Math.sqrt(3) / 2) * side;
        return new Rectangle2D(
                this.x - side / 2,
                this.y - height / 2,
                side,
                height
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

    public void setSide(double side) {
        this.side = side;
    }
}