package com.example.sudoku.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

import java.util.Random;

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

    private Random random = new Random(); // Este método ayuda a generar números aleatorios

    @FXML
    public void initialize() {
        setupGrid();
    }


    @FXML
    private void handleStartGame() {
        // Alerta de confirmación
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Iniciar Nuevo Juego");
        alert.setHeaderText("Estás seguro de que quieres iniciar un nuevo juego?");
        alert.setContentText("Se perderán los progresos actuales.");

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                handleNewGame(); // Llama al método para iniciar el juego nuevamente
            }
        });
        // Lógica para manejar el evento cuando se hace click en el botón
        System.out.println("Start game button clicked!");
    }

    @FXML
    private void handleNewGame() {
        clearGrid();
        setupGrid();
        generateSudokuNumbers(); // Este método llena la cuadrícula con números
        statusLabel.setText("¡Has iniciado un nuevo juego!");
    }

    private void setupGrid() {
        // Inicializa la cuadrícula de 6x6
        for (int row = 0; row < 6; row++) {
            for (int col = 0; col < 6; col++) {
                TextField textField = new TextField();
                textField.setPrefWidth(50);
                textField.setPrefHeight(50);
                textField.setStyle("-fx-font-size: 18;");
                textField.setEditable(false); // Se encarga de que los campos no sean editables, es decir, solo de lectura
                grid.add(textField, col, row);
            }
        }
    }

    private void clearGrid() {
        // Elimina todos los elementos de la cuadrícula
        grid.getChildren().clear();
    }

    private void generateSudokuNumbers() {
        // Lógica simple para llenar la cuadrícula 6x6
        // Por ahora, simplemente rellena con números aleatorios de 1 a 6
        for (int row = 0; row < 6; row++) {
            for (int col = 0; col < 6; col++) {
                int number = random.nextInt(6) + 1; // Genera un número aleatorio entre 1 y 6
                TextField textField = (TextField) grid.getChildren().get(row * 6 + col); // Accede a la lista de nodos hijos del GridPane
                textField.setText(String.valueOf(number)); // Establece el número en el TextField
                textField.setEditable(true); // Habilita la edición después de establecer los números
            }
        }
    }
//    @FXML
//    private void handleNewGame() {
//        // Lógica para reiniciar el juego
//        System.out.println("New game started!");
//    }
}