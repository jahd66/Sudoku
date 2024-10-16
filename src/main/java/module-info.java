module com.example.sudoku {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.example.sudoku to javafx.fxml; // Permite acceso al paquete raíz
    opens com.example.sudoku.controller to javafx.fxml; // Permite acceso al paquete controller
    exports com.example.sudoku; // Exporta el paquete raíz
}