package com.example.labs_tasks;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Paint;

public class FillTypeDecorator extends ShapeDecorator {
    private Paint fillType;

    public FillTypeDecorator(Shape decoratedShape, Paint fillType) {
        super(decoratedShape);
        this.fillType = fillType;
    }

    @Override
    public void draw(GraphicsContext gc, double x, double y, double opacity) {
        gc.setFill(fillType); // Устанавливаем градиент
        super.draw(gc, x, y, opacity); // Рисуем фигуру
        gc.fill(); // Применяем заливку
    }
}