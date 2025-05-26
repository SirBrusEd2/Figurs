package com.example.labs_tasks.factory;

import com.example.labs_tasks.model.Shape;
import com.example.labs_tasks.model.shapes.*;
import javafx.scene.paint.Color;

public class ShapeFactory {
    public Shape createShape(int shapeType) {
        switch (shapeType) {
            case 1: // Круг (было 0)
                return new Circle(0, 0, 10, Color.BLACK);
            case 2: // Треугольник (было 3)
                return new Triangle(0, 0, 10, Color.BLACK);
            case 3: // Прямоугольник (было 4)
                return new Rectangle(0, 0, 10, 10, Color.BLACK);
            case 4: // Плюс (было 2)
                return new Plus(0, 0, 3, 10, 10, 3, Color.BLACK);
            default:
                return null;
        }
    }
}