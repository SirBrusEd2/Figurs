package com.example.labs_tasks;

import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

class Plus extends Shape {
    private double verticalWidth;
    private double verticalHeight;
    private double horizontalWidth;
    private double horizontalHeight;

    Plus(int x, int y,
         double verticalWidth, double verticalHeight,
         double horizontalWidth, double horizontalHeight,
         Color color) {
        super(x, y, color);
        this.verticalWidth = verticalWidth;
        this.verticalHeight = verticalHeight;
        this.horizontalWidth = horizontalWidth;
        this.horizontalHeight = horizontalHeight;
    }

    @Override
    public void draw(GraphicsContext gc, double x, double y, double opacity) {
        if (gradient != null) {
            gc.setFill(gradient);
        } else {
            gc.setFill(color);
        }
        gc.setGlobalAlpha(opacity);

        // Вертикальная линия
        gc.fillRect(
                x - verticalWidth/2,
                y - verticalHeight/2,
                verticalWidth,
                verticalHeight
        );

        // Горизонтальная линия
        gc.fillRect(
                x - horizontalWidth/2,
                y - horizontalHeight/2,
                horizontalWidth,
                horizontalHeight
        );

        gc.setGlobalAlpha(1.0);
    }

    @Override
    public boolean contains(double x, double y) {
        boolean inVertical =
                x >= this.x - verticalWidth/2 &&
                        x <= this.x + verticalWidth/2 &&
                        y >= this.y - verticalHeight/2 &&
                        y <= this.y + verticalHeight/2;

        boolean inHorizontal =
                x >= this.x - horizontalWidth/2 &&
                        x <= this.x + horizontalWidth/2 &&
                        y >= this.y - horizontalHeight/2 &&
                        y <= this.y + horizontalHeight/2;

        return inVertical || inHorizontal;
    }

    @Override
    public Rectangle2D getBounds() {
        double left = Math.min(
                x - verticalWidth/2,
                x - horizontalWidth/2
        );
        double top = Math.min(
                y - verticalHeight/2,
                y - horizontalHeight/2
        );
        double right = Math.max(
                x + verticalWidth/2,
                x + horizontalWidth/2
        );
        double bottom = Math.max(
                y + verticalHeight/2,
                y + horizontalHeight/2
        );

        return new Rectangle2D(
                left,
                top,
                right - left,
                bottom - top
        );
    }
    @Override
    public void drawStroke(GraphicsContext gc, double x, double y) {
        // Вертикальная линия
        gc.strokeRect(
                x - verticalWidth/2,
                y - verticalHeight/2,
                verticalWidth,
                verticalHeight
        );

        // Горизонтальная линия
        gc.strokeRect(
                x - horizontalWidth/2,
                y - horizontalHeight/2,
                horizontalWidth,
                horizontalHeight
        );
    }
}