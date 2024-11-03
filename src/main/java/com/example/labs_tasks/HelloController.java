package com.example.labs_tasks;

import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

public class HelloController {

    @FXML
    private Canvas canvas;

    @FXML
    private Button circlePenButton;

    @FXML
    private Button trianglePenButton;

    @FXML
    private Button rectanglePenButton;

    @FXML
    private Button plusPenButton;

    @FXML
    private Button redColorButton;

    @FXML
    private Button blueColorButton;

    @FXML
    private Button yellowColorButton;

    @FXML
    private Button greenColorButton;

    private GraphicsContext gc;
    private ShapeFactory shapeFactory;
    private int circleRadius = 5;
    private double triangleSide = 10;
    private int rectangleWidth = 10;
    private int rectangleHeight = 10;
    private double plusSize = 10;
    private Color currentColor = Color.BLACK;
    private boolean isDrawing = false;
    private double lastX, lastY;
    private boolean isCirclePenSelected = true;
    private boolean isTrianglePenSelected = false;
    private boolean isRectanglePenSelected = false;
    private boolean isPlusPenSelected = false;

    public void initialize() {
        gc = canvas.getGraphicsContext2D();
        shapeFactory = new ShapeFactory();

        canvas.addEventHandler(MouseEvent.MOUSE_PRESSED, this::handleMousePressed);
        canvas.addEventHandler(MouseEvent.MOUSE_DRAGGED, this::handleMouseDragged);
        canvas.addEventHandler(MouseEvent.MOUSE_RELEASED, this::handleMouseReleased);

        circlePenButton.setOnAction(event -> selectCirclePen());
        trianglePenButton.setOnAction(event -> selectTrianglePen());
        rectanglePenButton.setOnAction(event -> selectRectanglePen());
        plusPenButton.setOnAction(event -> selectPlusPen());

        redColorButton.setOnAction(event -> setColor(Color.RED));
        blueColorButton.setOnAction(event -> setColor(Color.BLUE));
        yellowColorButton.setOnAction(event -> setColor(Color.YELLOW));
        greenColorButton.setOnAction(event -> setColor(Color.GREEN));
    }

    private void handleMousePressed(MouseEvent event) {
        isDrawing = true;
        lastX = event.getX();
        lastY = event.getY();
        drawShape(lastX, lastY);
    }

    private void handleMouseDragged(MouseEvent event) {
        if (isDrawing) {
            double newX = event.getX();
            double newY = event.getY();
            drawLine(lastX, lastY, newX, newY);
            lastX = newX;
            lastY = newY;
        }
    }

    private void handleMouseReleased(MouseEvent event) {
        isDrawing = false;
    }

    private void drawShape(double x, double y) {
        if (isCirclePenSelected) {
            drawCircle(x, y);
        } else if (isTrianglePenSelected) {
            drawTriangle(x, y);
        } else if (isRectanglePenSelected) {
            drawRectangle(x, y);
        } else if (isPlusPenSelected) {
            drawPlus(x, y);
        }
    }

    private void drawCircle(double x, double y) {
        gc.setFill(currentColor);
        gc.fillOval(x - circleRadius, y - circleRadius, 2 * circleRadius, 2 * circleRadius);
    }

    private void drawTriangle(double x, double y) {
        double halfSide = triangleSide / 2;
        double height = Math.sqrt(3) * halfSide;

        gc.setFill(currentColor);
        gc.fillPolygon(new double[]{x, x - halfSide, x + halfSide}, new double[]{y - height / 2, y + height / 2, y + height / 2}, 3);
    }

    private void drawRectangle(double x, double y) {
        gc.setFill(currentColor);
        gc.fillRect(x - rectangleWidth / 2, y - rectangleHeight / 2, rectangleWidth, rectangleHeight);
    }

    private void drawPlus(double x, double y) {
        gc.setFill(currentColor);
        gc.fillRect(x - plusSize / 4, y - plusSize / 2, plusSize / 2, plusSize);
        gc.fillRect(x - plusSize / 2, y - plusSize / 4, plusSize, plusSize / 2);
    }

    private void drawLine(double startX, double startY, double endX, double endY) {
        double distance = Math.sqrt(Math.pow(endX - startX, 2) + Math.pow(endY - startY, 2));
        double stepX = (endX - startX) / distance;
        double stepY = (endY - startY) / distance;

        double step = isCirclePenSelected ? circleRadius : (isTrianglePenSelected ? triangleSide : (isRectanglePenSelected ? Math.max(rectangleWidth, rectangleHeight) : plusSize));

        for (double i = 0; i <= distance; i += step) {
            double x = startX + stepX * i;
            double y = startY + stepY * i;
            drawShape(x, y);
        }
    }

    private void selectCirclePen() {
        isCirclePenSelected = true;
        isTrianglePenSelected = false;
        isRectanglePenSelected = false;
        isPlusPenSelected = false;
    }

    private void selectTrianglePen() {
        isCirclePenSelected = false;
        isTrianglePenSelected = true;
        isRectanglePenSelected = false;
        isPlusPenSelected = false;
    }

    private void selectRectanglePen() {
        isCirclePenSelected = false;
        isTrianglePenSelected = false;
        isRectanglePenSelected = true;
        isPlusPenSelected = false;
    }

    private void selectPlusPen() {
        isCirclePenSelected = false;
        isTrianglePenSelected = false;
        isRectanglePenSelected = false;
        isPlusPenSelected = true;
    }

    private void setColor(Color color) {
        currentColor = color;
    }

}