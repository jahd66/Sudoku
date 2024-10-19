package com.example.sudoku;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class SudokuApp extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Imprime la ruta del archivo FXML para verificar que sí está bien referenciado
        System.out.println(getClass().getResource("/com/example/sudoku/view/SudokuView.fxml"));

        // Carga el archivo FXML
        Parent root = FXMLLoader.load(getClass().getResource("/com/example/sudoku/view/SudokuView.fxml"));
        primaryStage.setTitle("Sudoku Game");

//        // Establece tamaño mínimo para la ventana
//        primaryStage.setMinWidth(600);
//        primaryStage.setMinHeight(600);
//
//        // Establece tamaño inicial de la ventana
//        primaryStage.setWidth(600);
//        primaryStage.setHeight(600);

        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }
}
