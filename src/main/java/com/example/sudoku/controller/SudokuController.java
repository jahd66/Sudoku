package com.example.sudoku.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

public class SudokuController {

    @FXML
    private GridPane grid;

    @FXML
    private Button startGameButton;

    @FXML
    private Button helpButton;

    @FXML
    private Button instructionsButton;

    @FXML
    private Label statusLabel;

    @FXML
    public void initialize() {
        setupGrid();
    }

    @FXML
    private void handleNewGame() {
        clearGrid();
        setupGrid();
        statusLabel.setText("¡Has iniciado un nuevo juego!");
    }

    private void setupGrid() {
        // Aquí debo de agregar luego la lógica para crear y configurar la cuadrícula de 6x6
        for (int row = 0; row < 6; row++) {
            for (int col = 0; col < 6; col++) {
                TextField textField = new TextField();
                textField.setPrefWidth(50);
                textField.setPrefHeight(50);
                textField.setStyle("-fx-font-size: 18;");
                grid.add(textField, col, row);
            }
        }
    }

    private void clearGrid() {
        // Elimina todos los elementos de la cuadrícula
        grid.getChildren().clear();
    }

//    @FXML
//    private void handleNewGame() {
//        // Lógica para reiniciar el juego
//        System.out.println("New game started!");
//    }
}
