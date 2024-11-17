package com.example.labs_tasks;

import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;

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

    @FXML
    private Button undoButton; // Добавляем кнопку для отката назад

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

    private Deque<Runnable> undoStack = new ArrayDeque<>();
    private Map<String, Runnable> shapeMap = new HashMap<>();

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

        undoButton.setOnAction(event -> undoLastAction()); // Добавляем обработчик для кнопки отката назад (используем очередь)

        // Инициализация shapeMap (вместо оператора switch)
        shapeMap.put("круг", () -> drawCircle(lastX, lastY));
        shapeMap.put("треугольник", () -> drawTriangle(lastX, lastY));
        shapeMap.put("прямоугольник", () -> drawRectangle(lastX, lastY));
        shapeMap.put("плюс", () -> drawPlus(lastX, lastY));
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

    // Использование shapeMap для выбора фигуры
    private void drawShape(double x, double y) {
        Runnable shapeAction = shapeMap.get(currentShape);
        if (shapeAction != null) {
            shapeAction.run();
            undoStack.push(() -> clearShape(x, y)); // Добавляем действие для отката
        }
    }

    private void clearShape(double x, double y) {
        gc.clearRect(x - 10, y - 10, 20, 20); // Очищаем область вокруг точки
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
    // Отмена последнего действия с помощью очереди
    private void undoLastAction() {
        if (!undoStack.isEmpty()) {
            Runnable lastAction = undoStack.pop();
            lastAction.run();
        }
    }
}