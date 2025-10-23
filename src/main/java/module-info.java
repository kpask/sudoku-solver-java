module com.example.sudokusolver {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.sudokusolver to javafx.fxml;
    exports com.example.sudokusolver;
}