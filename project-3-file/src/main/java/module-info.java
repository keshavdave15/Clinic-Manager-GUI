module com.example.project3 {
    requires javafx.controls;
    requires javafx.fxml;


    opens project3scheduler to javafx.fxml;
    opens scheduler to javafx.base; // Allow reflection access to scheduler package
    exports project3scheduler;
    exports util;
    opens util to javafx.fxml;
}