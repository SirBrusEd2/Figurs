package com.example.labs_tasks.controller;

import com.example.labs_tasks.decorator.HighlightDecorator;
import com.example.labs_tasks.factory.ShapeFactory;
import com.example.labs_tasks.model.Shape;
import com.example.labs_tasks.model.composite.Component;
import com.example.labs_tasks.model.composite.Composite;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
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

    @FXML private Canvas canvas;
    @FXML private ComboBox<String> shapeComboBox;
    @FXML private Button undoButton;
    @FXML private ColorPicker colorPicker;
    @FXML private TextField brushSizeTextField;
    @FXML private ComboBox<String> brushTypeComboBox;
    @FXML private CheckBox animationCheckBox;

    private GraphicsContext gc;
    private ShapeFactory shapeFactory;
    private Color currentColor = Color.BLACK;
    private boolean isDrawing = false;
    private double lastX, lastY;
    private String currentShapeType = "круг";
    private String currentBrushType = "Обычная кисть";
    private boolean isBlinking = false;
    private Timeline blinkTimeline;
    private double opacity = 1.0;

    private Deque<Runnable> undoStack = new ArrayDeque<>();
    private Map<String, Integer> shapeSidesMap = new HashMap<>();
    private Map<String, Double> shapeSizeMap = new HashMap<>();
    private List<Shape> shapes = new ArrayList<>();
    private Composite selectedComponents = new Composite();
    private double selectionStartX, selectionStartY;
    private double selectionEndX, selectionEndY;
    private boolean isSelecting = false;
    private boolean isMoving = false;
    private double moveStartX, moveStartY;
    private List<Double> initialShapeX = new ArrayList<>();
    private List<Double> initialShapeY = new ArrayList<>();

    public void initialize() {
        gc = canvas.getGraphicsContext2D();
        shapeFactory = new ShapeFactory();

        // Соответствие между названием фигуры и числом сторон
        shapeSidesMap.put("круг", 1);
        shapeSidesMap.put("треугольник", 2);
        shapeSidesMap.put("прямоугольник", 3);
        shapeSidesMap.put("плюс", 4);

        // Размеры по умолчанию для каждой фигуры
        shapeSizeMap.put("круг", 10.0);
        shapeSizeMap.put("треугольник", 10.0);
        shapeSizeMap.put("прямоугольник", 10.0);
        shapeSizeMap.put("плюс", 10.0);

        // Обработчики событий мыши
        canvas.addEventHandler(MouseEvent.MOUSE_PRESSED, this::handleMousePressed);
        canvas.addEventHandler(MouseEvent.MOUSE_DRAGGED, this::handleMouseDragged);
        canvas.addEventHandler(MouseEvent.MOUSE_RELEASED, this::handleMouseReleased);

        // Настройка ComboBox
        shapeComboBox.getItems().addAll("круг", "треугольник", "прямоугольник", "плюс");
        shapeComboBox.setValue("круг");
        shapeComboBox.setOnAction(e -> currentShapeType = shapeComboBox.getValue());

        // Настройка ColorPicker
        colorPicker.setValue(currentColor);
        colorPicker.setOnAction(e -> {
            currentColor = colorPicker.getValue();
            if (!selectedComponents.getChildren().isEmpty()) {
                handleColorChange();
            }
        });

        // Настройка Undo
        undoButton.setOnAction(e -> undoLastAction());

        // Настройка типа кисти
        brushTypeComboBox.getItems().addAll("Обычная кисть", "Градиент");
        brushTypeComboBox.setValue("Обычная кисть");
        brushTypeComboBox.setOnAction(e -> currentBrushType = brushTypeComboBox.getValue());

        // Настройка размера кисти
        brushSizeTextField.setText("10");
        brushSizeTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            try {
                int size = Integer.parseInt(newValue);
                if (size >= 1 && size <= 200) {
                    updateBrushSize(size);
                }
            } catch (NumberFormatException ignored) {}
        });

        // Настройка анимации
        animationCheckBox.setOnAction(e -> {
            isBlinking = animationCheckBox.isSelected();
            if (isBlinking) startBlinking();
            else stopBlinking();
        });
    }

    private void handleMousePressed(MouseEvent event) {
        if (event.isPrimaryButtonDown()) {
            if (!selectedComponents.getChildren().isEmpty()) {
                startMovingSelection(event.getX(), event.getY());
            } else {
                startDrawing(event.getX(), event.getY());
            }
        } else if (event.isSecondaryButtonDown()) {
            startSelection(event.getX(), event.getY());
        }
    }

    private void handleMouseDragged(MouseEvent event) {
        if (isMoving) {
            moveSelection(event.getX(), event.getY());
        } else if (isDrawing) {
            continueDrawing(event.getX(), event.getY());
        } else if (isSelecting) {
            updateSelection(event.getX(), event.getY());
        }
    }

    private void handleMouseReleased(MouseEvent event) {
        if (isMoving) {
            stopMoving();
        } else if (isSelecting) {
            completeSelection();
        }
        isDrawing = false;
    }

    private void startDrawing(double x, double y) {
        isDrawing = true;
        lastX = x;
        lastY = y;
        drawShape(x, y);
    }

    private void continueDrawing(double x, double y) {
        drawLine(lastX, lastY, x, y);
        lastX = x;
        lastY = y;
    }

    private void drawShape(double x, double y) {
        Shape shape = createNewShape(x, y);
        if (shape != null) {
            shapes.add(shape);
            shape.draw(gc, x, y, opacity);
            undoStack.push(() -> removeShapeAt(x, y));
        }
    }

    private Shape createNewShape(double x, double y) {
        Integer sides = shapeSidesMap.get(currentShapeType);
        if (sides == null) return null;

        Shape shape = shapeFactory.createShape(sides);
        shape.setX((int)x);
        shape.setY((int)y);
        shape.color = currentColor;

        // Установка размера в зависимости от типа фигуры
        double size = shapeSizeMap.get(currentShapeType);
        if (shape instanceof com.example.labs_tasks.model.shapes.Circle) {
            ((com.example.labs_tasks.model.shapes.Circle) shape).setRadius((int)size);
        } else if (shape instanceof com.example.labs_tasks.model.shapes.Triangle) {
            ((com.example.labs_tasks.model.shapes.Triangle) shape).setSide(size);
        } else if (shape instanceof com.example.labs_tasks.model.shapes.Rectangle) {
            int intSize = (int)size;
            ((com.example.labs_tasks.model.shapes.Rectangle) shape).setWidth(intSize);
            ((com.example.labs_tasks.model.shapes.Rectangle) shape).setHeight(intSize);
        } else if (shape instanceof com.example.labs_tasks.model.shapes.Plus) {
            double thickness = size * 0.3;
            ((com.example.labs_tasks.model.shapes.Plus) shape).setVerticalSize(thickness, size);
            ((com.example.labs_tasks.model.shapes.Plus) shape).setHorizontalSize(size, thickness);
        }

        applyStyle(shape);
        return shape;
    }

    private void applyStyle(Shape shape) {
        if (currentBrushType.equals("Градиент")) {
            LinearGradient gradient = new LinearGradient(
                    0, 0, 1, 1, true, CycleMethod.NO_CYCLE,
                    new Stop(0, Color.WHITE),
                    new Stop(1, currentColor)
            );
            shape.setGradient(gradient);
        } else {
            shape.setGradient(null);
        }
        shape.setHasAnimation(isBlinking);
    }

    private void drawLine(double startX, double startY, double endX, double endY) {
        double distance = Math.sqrt(Math.pow(endX - startX, 2) + Math.pow(endY - startY, 2));
        double step = shapeSizeMap.get(currentShapeType) / 2.0;
        if (step <= 0) step = 1;

        double stepX = (endX - startX) / distance * step;
        double stepY = (endY - startY) / distance * step;

        for (double i = 0; i <= distance; i += step) {
            drawShape(startX + stepX * i, startY + stepY * i);
        }
    }

    private void removeShapeAt(double x, double y) {
        Shape toRemove = shapes.stream()
                .filter(s -> s.contains(x, y))
                .findFirst()
                .orElse(null);

        if (toRemove != null) {
            shapes.remove(toRemove);
            redraw();
        }
    }

    private void updateBrushSize(double size) {
        shapeSizeMap.replaceAll((k, v) -> (double)size);
    }

    // Методы для работы с выделением
    private void startSelection(double x, double y) {
        isSelecting = true;
        selectionStartX = selectionEndX = x;
        selectionStartY = selectionEndY = y;
        selectedComponents = new Composite();
    }

    private void updateSelection(double x, double y) {
        selectionEndX = x;
        selectionEndY = y;
        redraw();
    }

    private void completeSelection() {
        isSelecting = false;
        double minX = Math.min(selectionStartX, selectionEndX);
        double maxX = Math.max(selectionStartX, selectionEndX);
        double minY = Math.min(selectionStartY, selectionEndY);
        double maxY = Math.max(selectionStartY, selectionEndY);

        selectedComponents = new Composite();
        shapes.stream()
                .filter(shape -> shape.intersects(minX, minY, maxX, maxY))
                .forEach(shape -> selectedComponents.add(new HighlightDecorator(shape, Color.RED)));

        redraw();
    }

    // Методы для перемещения выделения
    private void startMovingSelection(double x, double y) {
        isMoving = true;
        moveStartX = x;
        moveStartY = y;
        initialShapeX.clear();
        initialShapeY.clear();

        selectedComponents.getChildren().forEach(component -> {
            if (component instanceof HighlightDecorator) {
                Shape shape = ((HighlightDecorator) component).getDecoratedShape();
                initialShapeX.add((double)shape.getX());
                initialShapeY.add((double)shape.getY());
            }
        });
    }

    private void moveSelection(double x, double y) {
        double deltaX = x - moveStartX;
        double deltaY = y - moveStartY;

        for (int i = 0; i < selectedComponents.getChildren().size(); i++) {
            Component component = selectedComponents.getChildren().get(i);
            if (component instanceof HighlightDecorator) {
                Shape shape = ((HighlightDecorator) component).getDecoratedShape();
                shape.setX((int)(initialShapeX.get(i) + deltaX));
                shape.setY((int)(initialShapeY.get(i) + deltaY));
            }
        }
        redraw();
    }

    private void stopMoving() {
        isMoving = false;
        initialShapeX.clear();
        initialShapeY.clear();
    }

    // Методы для анимации
    private void startBlinking() {
        blinkTimeline = new Timeline(
                new KeyFrame(Duration.seconds(0.5), e -> {
                    opacity = opacity == 1.0 ? 0.0 : 1.0;
                    redraw();
                })
        );
        blinkTimeline.setCycleCount(Timeline.INDEFINITE);
        blinkTimeline.play();
    }

    private void stopBlinking() {
        if (blinkTimeline != null) {
            blinkTimeline.stop();
            opacity = 1.0;
            redraw();
        }
    }

    // Другие вспомогательные методы
    private void undoLastAction() {
        if (!undoStack.isEmpty()) {
            stopBlinking();
            undoStack.pop().run();
            if (isBlinking) startBlinking();
        }
    }

    @FXML
    private void handleColorChange() {
        Color newColor = colorPicker.getValue();
        selectedComponents.getChildren().forEach(component -> {
            if (component instanceof HighlightDecorator) {
                ((HighlightDecorator) component).setHighlightedColor(newColor);
            }
        });
        redraw();
    }

    private void redraw() {
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

        // Рисуем все фигуры
        shapes.forEach(shape -> shape.draw(gc, shape.hasAnimation() ? opacity : 1.0));

        // Рисуем выделение
        selectedComponents.draw(gc, 1.0);

        // Рисуем рамку выделения (если нужно)
        if (isSelecting) {
            gc.setStroke(Color.BLUE);
            gc.setLineWidth(2);
            double x = Math.min(selectionStartX, selectionEndX);
            double y = Math.min(selectionStartY, selectionEndY);
            double width = Math.abs(selectionEndX - selectionStartX);
            double height = Math.abs(selectionEndY - selectionStartY);
            gc.strokeRect(x, y, width, height);
        }
    }
}