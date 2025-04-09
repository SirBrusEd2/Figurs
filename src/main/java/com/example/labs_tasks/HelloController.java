package com.example.labs_tasks;

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
    private List<Shape> shapes = new ArrayList<>();
    private List<Shape> removedShapes = new ArrayList<>();
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

        // Обработчики для левой кнопки (рисование + перемещение)
        canvas.addEventHandler(MouseEvent.MOUSE_PRESSED, event -> {
            if (event.isPrimaryButtonDown()) {
                if (!selectedComponents.getChildren().isEmpty()) {
                    // Начало перемещения выделенных объектов
                    isMoving = true;
                    moveStartX = event.getX();
                    moveStartY = event.getY();

                    // Сохраняем начальные позиции
                    initialShapeX.clear();
                    initialShapeY.clear();
                    selectedComponents.getChildren().forEach(component -> {
                        if (component instanceof HighlightDecorator) {
                            Shape shape = ((HighlightDecorator) component).getDecoratedShape();
                            initialShapeX.add((double) shape.getX());
                            initialShapeY.add((double) shape.getY());
                        }
                    });
                } else {
                    // Начало рисования
                    isDrawing = true;
                    lastX = event.getX();
                    lastY = event.getY();
                    drawShape(lastX, lastY);
                }
            }
        });

        canvas.addEventHandler(MouseEvent.MOUSE_DRAGGED, event -> {
            if (isMoving) {
                // Перемещение выделенных объектов
                double deltaX = event.getX() - moveStartX;
                double deltaY = event.getY() - moveStartY;

                for (int i = 0; i < selectedComponents.getChildren().size(); i++) {
                    Component component = selectedComponents.getChildren().get(i);
                    if (component instanceof HighlightDecorator) {
                        Shape shape = ((HighlightDecorator) component).getDecoratedShape();
                        shape.setX((int) (initialShapeX.get(i) + deltaX));
                        shape.setY((int) (initialShapeY.get(i) + deltaY));
                    }
                }
                redraw();
            } else if (isDrawing) {
                // Рисование
                double newX = event.getX();
                double newY = event.getY();
                drawLine(lastX, lastY, newX, newY);
                lastX = newX;
                lastY = newY;
            }
        });

        canvas.addEventHandler(MouseEvent.MOUSE_RELEASED, event -> {
            if (isMoving) {
                // Завершение перемещения
                isMoving = false;
                initialShapeX.clear();
                initialShapeY.clear();
            } else if (isDrawing) {
                // Завершение рисования
                isDrawing = false;
            }
        });

        // Обработчики для правой кнопки (выделение)
        canvas.addEventHandler(MouseEvent.MOUSE_PRESSED, event -> {
            if (event.isSecondaryButtonDown()) {
                isSelecting = true;
                selectionStartX = event.getX();
                selectionStartY = event.getY();
                selectionEndX = selectionStartX;
                selectionEndY = selectionStartY;
                selectedComponents = new Composite();
            }
        });

        canvas.addEventHandler(MouseEvent.MOUSE_DRAGGED, event -> {
            if (isSelecting) {
                selectionEndX = event.getX();
                selectionEndY = event.getY();
                redraw();
            }
        });

        canvas.addEventHandler(MouseEvent.MOUSE_RELEASED, event -> {
            if (isSelecting) {
                isSelecting = false;
                double minX = Math.min(selectionStartX, selectionEndX);
                double maxX = Math.max(selectionStartX, selectionEndX);
                double minY = Math.min(selectionStartY, selectionEndY);
                double maxY = Math.max(selectionStartY, selectionEndY);

                // Удаляем предыдущее выделение
                selectedComponents = new Composite();

                // Добавляем ТОЛЬКО декораторы, не копируя фигуры
                shapes.stream()
                        .filter(shape -> shape.intersects(minX, minY, maxX, maxY))
                        .forEach(shape ->
                                selectedComponents.add(new HighlightDecorator(shape, Color.RED))
                        );
                redraw();
            }
        });

        // Настройка ComboBox
        shapeComboBox.getItems().addAll("круг", "треугольник", "прямоугольник", "плюс");
        shapeComboBox.setValue("круг");
        shapeComboBox.setOnAction(e -> currentShape = shapeComboBox.getValue());

        // Настройка ColorPicker
        colorPicker.setOnAction(e -> {
            if (!selectedComponents.getChildren().isEmpty()) {
                handleColorChange();
            } else {
                currentColor = colorPicker.getValue();
            }
        });

        // Настройка Undo
        undoButton.setOnAction(e -> undoLastAction());

        // Настройка типа кисти
        brushTypeComboBox.getItems().addAll("Обычная кисть", "Градиент");
        brushTypeComboBox.setValue("Обычная кисть");
        brushTypeComboBox.setOnAction(e -> currentBrushType = brushTypeComboBox.getValue());

        // Настройка размера кисти
        brushSizeTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            try {
                int size = Integer.parseInt(newValue);
                if (size >= 1 && size <= 200) updateBrushSize(size);
            } catch (NumberFormatException ignored) {}
        });

        // Настройка анимации
        animationCheckBox.setOnAction(e -> {
            isBlinking = animationCheckBox.isSelected();
            if (isBlinking) startBlinking();
            else stopBlinking();
        });

        // Инициализация фигур
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
        // Обработка нажатия (уже интегрирована в initialize())
    }

    private void handleMouseDragged(MouseEvent event) {
        // Обработка перемещения (уже интегрирована в initialize())
    }

    private void handleMouseReleased(MouseEvent event) {
        // Обработка отпускания (уже интегрирована в initialize())
    }

    private void drawShape(double x, double y) {
        Runnable shapeAction = shapeMap.get(currentShape);
        if (shapeAction != null) {
            shapeAction.run();
            undoStack.push(() -> clearShape(x, y));
        }
    }

    private void clearShape(double x, double y) {
        Shape shapeToRemove = shapes.stream()
                .filter(shape -> shape.getX() == x && shape.getY() == y)
                .findFirst()
                .orElse(null);
        if (shapeToRemove != null) {
            shapes.remove(shapeToRemove);
            removedShapes.add(shapeToRemove);
            gc.clearRect(x - 10, y - 10, 20, 20);
        }
    }

    private void drawCircle(double x, double y) {
        Shape circle = shapeFactory.createCircle((int) x, (int) y, circleRadius, currentColor);
        applyStyle(circle);
        shapes.add(circle);
        drawShape(circle, gc, x, y, opacity);
    }

    private void drawTriangle(double x, double y) {
        Shape triangle = shapeFactory.createTriangle(
                (int) x,
                (int) y,
                triangleSide,
                currentColor
        );
        applyStyle(triangle);
        shapes.add(triangle);
        drawShape(triangle, gc, x, y, opacity);
    }

    private void drawRectangle(double x, double y) {
        Shape rectangle = shapeFactory.createRectangle(
                (int) x,
                (int) y,
                rectangleWidth,
                rectangleHeight,
                currentColor
        );
        applyStyle(rectangle);
        shapes.add(rectangle);
        drawShape(rectangle, gc, x, y, opacity);
    }

    private void drawPlus(double x, double y) {
        Shape plus = shapeFactory.createPlus((int) x, (int) y, plusSize, currentColor);
        applyStyle(plus);
        shapes.add(plus);
        drawShape(plus, gc, x, y, opacity);
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

    private void drawShape(Shape shape, GraphicsContext gc, double x, double y, double opacity) {
        shape.draw(gc, x, y, opacity);
    }

    private void drawLine(double startX, double startY, double endX, double endY) {
        double distance = Math.sqrt(Math.pow(endX - startX, 2) + Math.pow(endY - startY, 2));
        double stepX = (endX - startX) / distance;
        double stepY = (endY - startY) / distance;
        double step = shapeStepMap.getOrDefault(currentShape, 1.0);

        for (double i = 0; i <= distance; i += step) {
            double currentX = startX + stepX * i;
            double currentY = startY + stepY * i;
            drawShape(currentX, currentY);
        }
    }

    private void setColor(Color color) {
        currentColor = color;
    }

    private void undoLastAction() {
        if (!undoStack.isEmpty()) {
            stopBlinking();
            Runnable lastAction = undoStack.pop();
            lastAction.run();
            if (!shapes.isEmpty()) {
                Shape shapeToRemove = shapes.remove(shapes.size() - 1);
                removedShapes.add(shapeToRemove);
            }
            redraw();
            if (isBlinking) startBlinking();
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
        shapeStepMap.put("плюс", plusSize * 0.3);
    }

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

    private void redraw() {
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

        // 1. Рисуем ВСЕ фигуры из списка shapes (включая перемещённые)
        shapes.forEach(s -> s.draw(gc, s.hasAnimation() ? opacity : 1.0));

        // 2. Рисуем выделение поверх фигур
        selectedComponents.draw(gc, 1.0);

        // 3. Рисуем рамку выделения (если нужно)
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

    @FXML
    private void handleColorChange() {
        if (!selectedComponents.getChildren().isEmpty()) {
            Color newColor = colorPicker.getValue();
            selectedComponents.getChildren().forEach(component -> {
                if (component instanceof HighlightDecorator) {
                    ((HighlightDecorator) component).setHighlightedColor(newColor);
                }
            });
            redraw();
        }
    }
}