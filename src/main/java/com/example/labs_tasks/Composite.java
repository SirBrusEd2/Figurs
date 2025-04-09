package com.example.labs_tasks;

import javafx.scene.canvas.GraphicsContext;
import java.util.ArrayList;
import java.util.List;

public class Composite implements Component {
    private List<Component> children = new ArrayList<>();

    @Override
    public void draw(GraphicsContext gc, double opacity) {
        for (Component child : children) {
            child.draw(gc, opacity);
        }
    }

    @Override
    public void add(Component component) {
        children.add(component);
    }

    @Override
    public void remove(Component component) {
        children.remove(component);
    }

    @Override
    public boolean contains(double x, double y) {
        return children.stream().anyMatch(c -> c.contains(x, y));
    }

    // ИСПРАВЛЕНИЕ: Добавлен метод для проверки наличия компонента
    public boolean containsComponent(Component component) {
        return children.contains(component);
    }
    public List<Component> getChildren() {
        return children;
    }
}