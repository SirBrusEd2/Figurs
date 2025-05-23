module com.example.labs_tasks {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.labs_tasks to javafx.fxml;
    exports com.example.labs_tasks;
    exports com.example.labs_tasks.controller;
    opens com.example.labs_tasks.controller to javafx.fxml;
    exports com.example.labs_tasks.model.composite;
    opens com.example.labs_tasks.model.composite to javafx.fxml;
    exports com.example.labs_tasks.model;
    opens com.example.labs_tasks.model to javafx.fxml;
    exports com.example.labs_tasks.decorator;
    opens com.example.labs_tasks.decorator to javafx.fxml;
    exports com.example.labs_tasks.factory;
    opens com.example.labs_tasks.factory to javafx.fxml;
}