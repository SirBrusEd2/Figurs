package com.example.labs_tasks;

import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ColorPicker;
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
    private ComboBox<String> shapeComboBox;

    @FXML
    private Button undoButton;

    @FXML
    private ColorPicker colorPicker;

    @FXML
    private TextField brushSizeTextField;

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
    private String currentShape = "круг";

    private Deque<Runnable> undoStack = new ArrayDeque<>();
    private Map<String, Runnable> shapeMap = new HashMap<>();
    private Map<String, Double> shapeStepMap = new HashMap<>();

    public void initialize() {
        gc = canvas.getGraphicsContext2D();
        shapeFactory = new ShapeFactory();

        canvas.addEventHandler(MouseEvent.MOUSE_PRESSED, this::handleMousePressed);
        canvas.addEventHandler(MouseEvent.MOUSE_DRAGGED, this::handleMouseDragged);
        canvas.addEventHandler(MouseEvent.MOUSE_RELEASED, this::handleMouseReleased);

        shapeComboBox.getItems().addAll("круг", "треугольник", "прямоугольник", "плюс");
        shapeComboBox.setValue("круг");

        shapeComboBox.setOnAction(event -> {
            currentShape = shapeComboBox.getValue();
        });

        colorPicker.setOnAction(event -> setColor(colorPicker.getValue()));

        undoButton.setOnAction(event -> undoLastAction());

        brushSizeTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            try {
                int size = Integer.parseInt(newValue);
                if (size >= 1 && size <= 200) {
                    updateBrushSize(size);
                }
            } catch (NumberFormatException e) {
                // Ignore invalid input
            }
        });

        shapeMap.put("круг", () -> drawCircle(lastX, lastY));
        shapeMap.put("треугольник", () -> drawTriangle(lastX, lastY));
        shapeMap.put("прямоугольник", () -> drawRectangle(lastX, lastY));
        shapeMap.put("плюс", () -> drawPlus(lastX, lastY));

        shapeStepMap.put("круг", (double) circleRadius);
        shapeStepMap.put("треугольник", triangleSide);
        shapeStepMap.put("прямоугольник", (double) Math.max(rectangleWidth, rectangleHeight));
        shapeStepMap.put("плюс", plusSize);
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
        Runnable shapeAction = shapeMap.get(currentShape);
        if (shapeAction != null) {
            shapeAction.run();
            undoStack.push(() -> clearShape(x, y));
        }
    }

    private void clearShape(double x, double y) {
        gc.clearRect(x - 10, y - 10, 20, 20);
    }

    private void drawCircle(double x, double y) {
        Shape circle = shapeFactory.createCircle((int) x, (int) y, circleRadius, currentColor);
        drawShape(circle);
    }

    private void drawTriangle(double x, double y) {
        Shape triangle = shapeFactory.createTriangle((int) x, (int) y, triangleSide, triangleSide, triangleSide, currentColor);
        drawShape(triangle);
    }

    private void drawRectangle(double x, double y) {
        Shape rectangle = shapeFactory.createRectangle((int) x, (int) y, rectangleWidth, rectangleHeight, currentColor);
        drawShape(rectangle);
    }

    private void drawPlus(double x, double y) {
        Shape plus = shapeFactory.createPlus((int) x, (int) y, plusSize, plusSize, plusSize, plusSize, currentColor);
        drawShape(plus);
    }

    private void drawShape(Shape shape) {
        gc.setFill(shape.color);
        if (shape instanceof Circle) {
            Circle circle = (Circle) shape;
            gc.fillOval(circle.x - circle.r, circle.y - circle.r, 2 * circle.r, 2 * circle.r);
        } else if (shape instanceof Triangle) {
            Triangle triangle = (Triangle) shape;
            double halfSide = triangle.alf / 2;
            double height = Math.sqrt(3) * halfSide;
            gc.fillPolygon(new double[]{triangle.x, triangle.x - halfSide, triangle.x + halfSide}, new double[]{triangle.y - height / 2, triangle.y + height / 2, triangle.y + height / 2}, 3);
        } else if (shape instanceof Rectangle) {
            Rectangle rectangle = (Rectangle) shape;
            gc.fillRect(rectangle.x - rectangle.w / 2, rectangle.y - rectangle.h / 2, rectangle.w, rectangle.h);
        } else if (shape instanceof Plus) {
            Plus plus = (Plus) shape;
            gc.fillRect(plus.x - plus.px / 4, plus.y - plus.py / 2, plus.px / 2, plus.py);
            gc.fillRect(plus.x - plus.px / 2, plus.y - plus.py / 4, plus.px, plus.py / 2);
        }
    }

    private void drawLine(double startX, double startY, double endX, double endY) {
        double distance = Math.sqrt(Math.pow(endX - startX, 2) + Math.pow(endY - startY, 2));
        double stepX = (endX - startX) / distance;
        double stepY = (endY - startY) / distance;

        double step = shapeStepMap.getOrDefault(currentShape, 1.0);

        for (double i = 0; i <= distance; i += step) {
            double x = startX + stepX * i;
            double y = startY + stepY * i;
            drawShape(x, y);
        }
    }

    private void setColor(Color color) {
        currentColor = color;
    }

    private void undoLastAction() {
        if (!undoStack.isEmpty()) {
            Runnable lastAction = undoStack.pop();
            lastAction.run();
        }
    }

    private void updateBrushSize(double size) {
        circleRadius = (int) size;
        triangleSide = size;
        rectangleWidth = (int) size;
        rectangleHeight = (int) size;
        plusSize = size;

        shapeStepMap.put("круг", (double) circleRadius);
        shapeStepMap.put("треугольник", triangleSide);
        shapeStepMap.put("прямоугольник", (double) Math.max(rectangleWidth, rectangleHeight));
        shapeStepMap.put("плюс", plusSize);
    }
}