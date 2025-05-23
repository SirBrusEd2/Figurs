package com.example.labs_tasks.model.composite;

import javafx.scene.canvas.GraphicsContext;

public interface Component {
    void draw(GraphicsContext gc, double opacity); // Убраны параметры x, y
    void add(Component component);
    void remove(Component component);
    boolean contains(double x, double y);
}