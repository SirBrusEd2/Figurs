package com.example.labs_tasks;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.util.Duration;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
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

    @FXML
    private ComboBox<String> brushTypeComboBox;

    @FXML
    private CheckBox animationCheckBox;

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
    private String currentBrushType = "Обычная кисть";
    private boolean isBlinking = false;
    private Timeline blinkTimeline;
    private double opacity = 1.0;

    private Deque<Runnable> undoStack = new ArrayDeque<>();
    private Map<String, Runnable> shapeMap = new HashMap<>();
    private Map<String, Double> shapeStepMap = new HashMap<>();
    private List<Shape> shapes = new ArrayList<>(); // Список всех нарисованных фигур
    private List<Shape> removedShapes = new ArrayList<>(); // Список удаленных фигур

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

        brushTypeComboBox.getItems().addAll("Обычная кисть", "Градиент");
        brushTypeComboBox.setValue("Обычная кисть");

        brushTypeComboBox.setOnAction(event -> {
            currentBrushType = brushTypeComboBox.getValue();
        });

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

        animationCheckBox.setOnAction(event -> {
            isBlinking = animationCheckBox.isSelected();
            if (isBlinking) {
                startBlinking();
            } else {
                stopBlinking();
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
            // Добавляем действие в стек отмены
            undoStack.push(() -> clearShape(x, y));
        }
    }

    private void clearShape(double x, double y) {
        // Удаляем фигуру из списка shapes
        Shape shapeToRemove = shapes.stream()
                .filter(shape -> shape.getX() == x && shape.getY() == y)
                .findFirst()
                .orElse(null);
        if (shapeToRemove != null) {
            shapes.remove(shapeToRemove);
            removedShapes.add(shapeToRemove);
        }

        // Очищаем область холста
        gc.clearRect(x - 10, y - 10, 20, 20);
    }

    private void drawCircle(double x, double y) {
        Shape circle = shapeFactory.createCircle((int) x, (int) y, circleRadius, currentColor);
        if (currentBrushType.equals("Градиент")) {
            // Создаем градиент для круга
            LinearGradient gradient = new LinearGradient(
                    0, 0, 1, 1, true, CycleMethod.NO_CYCLE,
                    new Stop(0, Color.WHITE),
                    new Stop(1, currentColor)
            );
            circle.setGradient(gradient); // Сохраняем градиент в фигуре
        }
        circle.setHasAnimation(isBlinking); // Устанавливаем состояние анимации
        shapes.add(circle); // Добавляем фигуру в список всех фигур
        drawShape(circle, gc, x, y, opacity);
    }

    private void drawTriangle(double x, double y) {
        Shape triangle = shapeFactory.createTriangle((int) x, (int) y, triangleSide, triangleSide, triangleSide, currentColor);
        if (currentBrushType.equals("Градиент")) {
            // Создаем градиент для треугольника
            LinearGradient gradient = new LinearGradient(
                    0, 0, 1, 1, true, CycleMethod.NO_CYCLE,
                    new Stop(0, Color.WHITE),
                    new Stop(1, currentColor)
            );
            triangle.setGradient(gradient); // Сохраняем градиент в фигуре
        }
        triangle.setHasAnimation(isBlinking); // Устанавливаем состояние анимации
        shapes.add(triangle); // Добавляем фигуру в список всех фигур
        drawShape(triangle, gc, x, y, opacity);
    }

    private void drawRectangle(double x, double y) {
        Shape rectangle = shapeFactory.createRectangle((int) x, (int) y, rectangleWidth, rectangleHeight, currentColor);
        if (currentBrushType.equals("Градиент")) {
            // Создаем градиент для прямоугольника
            LinearGradient gradient = new LinearGradient(
                    0, 0, 1, 1, true, CycleMethod.NO_CYCLE,
                    new Stop(0, Color.WHITE),
                    new Stop(1, currentColor)
            );
            rectangle.setGradient(gradient); // Сохраняем градиент в фигуре
        }
        rectangle.setHasAnimation(isBlinking); // Устанавливаем состояние анимации
        shapes.add(rectangle); // Добавляем фигуру в список всех фигур
        drawShape(rectangle, gc, x, y, opacity);
    }

    private void drawPlus(double x, double y) {
        Shape plus = shapeFactory.createPlus((int)x, (int)y, plusSize, currentColor);

        if (currentBrushType.equals("Градиент")) {
            LinearGradient gradient = new LinearGradient(
                    0, 0, 1, 1, true, CycleMethod.NO_CYCLE,
                    new Stop(0, Color.WHITE),
                    new Stop(1, currentColor)
            );
            plus.setGradient(gradient);
        }
        plus.setHasAnimation(isBlinking);
        shapes.add(plus);
        drawShape(plus, gc, x, y, opacity);
    }

    private void drawShape(Shape shape, GraphicsContext gc, double x, double y, double opacity) {
        shape.draw(gc, x, y, opacity);
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
            // Временно останавливаем анимацию
            stopBlinking();

            // Получаем последнее действие из стека отмены
            Runnable lastAction = undoStack.pop();
            lastAction.run();

            // Удаляем фигуру из списка shapes
            if (!shapes.isEmpty()) {
                Shape shapeToRemove = shapes.remove(shapes.size() - 1);
                removedShapes.add(shapeToRemove);
            }

            // Перерисовываем оставшиеся фигуры
            gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
            for (Shape shape : shapes) {
                if (!removedShapes.contains(shape)) {
                    shape.draw(gc, shape.getX(), shape.getY(), 1.0);
                }
            }

            // Восстанавливаем анимацию, если она была включена
            if (isBlinking) {
                startBlinking();
            }
        }
    }

    private void updateBrushSize(double size) {
        circleRadius = (int) size;
        triangleSide = size;
        rectangleWidth = (int) size;
        rectangleHeight = (int) size;
        plusSize = size; // Обновляем только общий размер

        shapeStepMap.put("круг", (double) circleRadius);
        shapeStepMap.put("треугольник", triangleSide);
        shapeStepMap.put("прямоугольник", (double) Math.max(rectangleWidth, rectangleHeight));
        shapeStepMap.put("плюс", plusSize * 0.3); // Шаг соответствует толщине линии
    }

    private void startBlinking() {
        blinkTimeline = new Timeline(
                new KeyFrame(Duration.seconds(0.5), event -> {
                    opacity = opacity == 1.0 ? 0.0 : 1.0;
                    gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
                    for (Shape shape : shapes) {
                        if (!removedShapes.contains(shape)) {
                            // Перерисовываем только оставшиеся фигуры
                            shape.draw(gc, shape.getX(), shape.getY(), shape.hasAnimation() ? opacity : 1.0);
                        }
                    }
                })
        );
        blinkTimeline.setCycleCount(Timeline.INDEFINITE);
        blinkTimeline.play();
    }

    private void stopBlinking() {
        if (blinkTimeline != null) {
            blinkTimeline.stop();
            opacity = 1.0;
            gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
            for (Shape shape : shapes) {
                if (!removedShapes.contains(shape)) {
                    shape.draw(gc, shape.getX(), shape.getY(), 1.0);
                }
            }
        }
    }

}