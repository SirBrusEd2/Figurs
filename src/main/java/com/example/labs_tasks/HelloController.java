package com.example.labs_tasks;

import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

public class HelloController {

    @FXML
    private Canvas canvas;

    @FXML
    private TextField shapeInput;

    @FXML
    private Button redColorButton;

    @FXML
    private Button blueColorButton;

    @FXML
    private Button yellowColorButton;

    @FXML
    private Button greenColorButton;

    @FXML
    private Button blackColorButton; // Добавляем кнопку для черного цвета

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
    private String currentShape = "круг"; // По умолчанию выбираем круг

    public void initialize() {
        gc = canvas.getGraphicsContext2D();
        shapeFactory = new ShapeFactory();

        canvas.addEventHandler(MouseEvent.MOUSE_PRESSED, this::handleMousePressed);
        canvas.addEventHandler(MouseEvent.MOUSE_DRAGGED, this::handleMouseDragged);
        canvas.addEventHandler(MouseEvent.MOUSE_RELEASED, this::handleMouseReleased);

        shapeInput.textProperty().addListener((observable, oldValue, newValue) -> {
            currentShape = newValue.trim().toLowerCase();
        });

        redColorButton.setOnAction(event -> setColor(Color.RED));
        blueColorButton.setOnAction(event -> setColor(Color.BLUE));
        yellowColorButton.setOnAction(event -> setColor(Color.YELLOW));
        greenColorButton.setOnAction(event -> setColor(Color.GREEN));
        blackColorButton.setOnAction(event -> setColor(Color.BLACK)); // Добавляем обработчик для черного цвета
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
        switch (currentShape) {
            case "круг":
                drawCircle(x, y);
                break;
            case "треугольник":
                drawTriangle(x, y);
                break;
            case "прямоугольник":
                drawRectangle(x, y);
                break;
            case "плюс":
                drawPlus(x, y);
                break;
            default:
                // Если введено неизвестное название фигуры, ничего не делаем
                break;
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

        double step = 0;
        switch (currentShape) {
            case "круг":
                step = circleRadius;
                break;
            case "треугольник":
                step = triangleSide;
                break;
            case "прямоугольник":
                step = Math.max(rectangleWidth, rectangleHeight);
                break;
            case "плюс":
                step = plusSize;
                break;
        }

        for (double i = 0; i <= distance; i += step) {
            double x = startX + stepX * i;
            double y = startY + stepY * i;
            drawShape(x, y);
        }
    }

    private void setColor(Color color) {
        currentColor = color;
    }
}