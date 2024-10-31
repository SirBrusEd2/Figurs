module com.example.labs_tasks {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.labs_tasks to javafx.fxml;
    exports com.example.labs_tasks;
}