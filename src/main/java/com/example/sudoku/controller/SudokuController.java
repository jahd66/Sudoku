package com.example.sudoku.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

import java.util.Random;
import java.util.ArrayList;

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
    private int helpAttempts = 0; // Contador de intentos de ayuda
    private ArrayList<String> history = new ArrayList<>(); // Historial de movimientos
    private int historyPointer = -1; // Índice actual en el historial

    @FXML
    public void initialize() {
        System.out.println("Initializing SudokuController..."); // Mensaje de depuración
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

//        handleNewGame(); // Llama directamente para verificar que el método se ejecuta

        System.out.println("Start game button clicked!");
    }

    @FXML
    private void handleNewGame() {
        System.out.println("Starting a new game..."); // Mensaje de depuración
        clearGrid();
        setupGrid();
        helpAttempts = 0; // Reinicia el contador de intentos de ayuda
        System.out.println("Number of children in grid: " + grid.getChildren().size()); // Mensaje de depuración
        generateSudokuNumbers(); // Este método llena la cuadrícula con números
        statusLabel.setText("¡Has iniciado un nuevo juego!");
    }

    @FXML
    private void handleHelpButton() {
        if (helpAttempts >= 3) {
            // Muestra un mensaje si ya se ha alcanzado el límite de intentos
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Límite de ayuda alcanzado");
            alert.setHeaderText(null);
            alert.setContentText("Ya has utilizado la ayuda 3 veces. No se permite más sugerencias.");
            alert.showAndWait();
            return; // Sale del método si se ha alcanzado el límite de intentos
        }
        // Busca una celda vacía y sugiere un número
        for (int row = 0; row < 6; row++) {
            for (int col = 0; col < 6; col++) {
                TextField textField = (TextField) grid.getChildren().get(row * 6 + col);
                if (textField.getText().isEmpty()) { // Si la celda está vacía
                    int suggestedNumber = suggestNumber(row, col); // Llama al método para sugerir un número
                    if (suggestedNumber != -1) { // Si se encontró un número válido
                        textField.setText(String.valueOf(suggestedNumber)); // Muestra el número sugerido
                        textField.setStyle("-fx-background-color: green; -fx-font-size: 18;"); // Resalta la celda
                        helpAttempts++; // Incrementa el contador de intentos de ayuda
                    }
                    return; // Salimos después de sugerir un número
                }
            }
        }
    }

    // Método para deshacer el último movimiento
    @FXML
    private void handleUndo() {
        if (historyPointer > 0) {
            historyPointer--; // Mueve el puntero hacia atrás
            restoreGridState(history.get(historyPointer)); // Restaura el estado anterior
        } else {
            // Muestra un mensaje si no hay nada que deshacer
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("No se puede deshacer");
            alert.setContentText("No hay movimientos para deshacer");
            alert.showAndWait();
        }
    }

    // Método para agregar al historial
    private void addToHistory() {
        // Limita el tamaño del historial, elimina lo que haya posterior al puntero
        if (historyPointer < history.size() -1) {
            history.subList(historyPointer +1, history.size()).clear(); // Elimina el historial más allá del puntero
        }
        StringBuilder gridState = new StringBuilder();
        for (int row = 0; row < 6; row++) {
            for (int col = 0; col < 6; col++) {
                TextField textField = (TextField) grid.getChildren().get(row * 6 + col);
                gridState.append(textField.getText()).append(",");
            }
        }
        history.add(gridState.toString()); // Guarda el estado de la cuadrícula
        historyPointer++; // Mueve el puntero al nuevo elemento
    }

    // Método para restaurar el estado de la cuadrícula
    private void restoreGridState(String state) {
        String[] values = state.split(",");
        for (int row = 0; row <6; row++) {
            for (int col = 0; col < 6; col++) {
                TextField textField = (TextField) grid.getChildren().get(row * 6 + col);
                textField.setText(values[row * 6 + col]); // Restaura el texto de cada celda
            }
        }
    }

    // Método que sugiere un número válido para una celda vacía
    private int suggestNumber(int row, int col) {
        // Comprueba los números que ya están en la fila, columna y bloque 2x3
        boolean[] usedNumbers = new boolean[7]; // Índices del 1 al 6

        // Marcar números usados en la fila
        for (int c = 0; c < 6; c++) {
            TextField textField = (TextField) grid.getChildren().get(row * 6 + c);
            if (!textField.getText().isEmpty()) {
                int num = Integer.parseInt(textField.getText());
                usedNumbers[num] = true; // Marca el número como usado
            }
        }

        // Marcar números usados en la columna
        for (int r = 0; r < 6; r++) {
            TextField textField = (TextField) grid.getChildren().get(r * 6 + col);
            if (!textField.getText().isEmpty()) {
                int num = Integer.parseInt(textField.getText());
                usedNumbers[num] = true; // Marca el número como usado
            }
        }

        // Marcar números usados en el bloque 2x3
        int blockRowStart = (row / 2) * 2;
        int blockColStart = (col / 3) * 3;
        for (int r = 0; r < 2; r++) {
            for (int c = 0; c < 3; c++) {
                TextField textField = (TextField) grid.getChildren().get((blockRowStart + r) * 6 + (blockColStart + c));
                if (!textField.getText().isEmpty()) {
                    int num = Integer.parseInt(textField.getText());
                    usedNumbers[num] = true; // Marca el número como usado
                }
            }
        }

        // Sugerir un número que no esté usado
        for (int num = 1; num <= 6; num++) {
            if (!usedNumbers[num]) {
                return num; // Retorna el primer número no usado
            }
        }
        return -1; // Retorna -1 si no hay números disponibles
    }

    private void setupGrid() {
        // Inicializa la cuadrícula de 6x6
        grid.getChildren().clear(); // Limpia cualquier nodo previo para evitar duplicados
        for (int row = 0; row < 6; row++) {
            for (int col = 0; col < 6; col++) {
                TextField textField = new TextField();
                textField.setPrefWidth(50);
                textField.setPrefHeight(50);
                textField.setStyle("-fx-font-size: 18;");

                // Limita la entrada a números del 1 al 6
                textField.setTextFormatter(new TextFormatter<String>(change -> {
                    String newText = change.getControlNewText();
                    if (newText.matches("[1-6]?")) {
                        return change; // Permitir cambios si es un número del 1 al 6 o vacío
                    }
                    return null; // Rechazar cambios no permitidos
                }));

                // Agregar el evento de cambio para validar en tiempo real el número ingresado en el TextField
                textField.textProperty().addListener((observable, oldValue, newValue) -> {
                    validateGrid(); // Llama a la función de validación
                });

                grid.add(textField, col, row); // Agrega el TextField a la cuadrícula
            }
        }

        // Restaurar visibilidad de las líneas de la cuadrícula
        grid.setGridLinesVisible(false);
        grid.setGridLinesVisible(true);

        System.out.println("Grid setup completed. Number of children: " + grid.getChildren().size()); // Mensaje de depuración
    }

    private void validateGrid() {
        // Agrega al historial antes de validar
        addToHistory();
        // Matrices para verificar la existencia de números en filas, columnas y bloques 2x3
        boolean[][] rowCheck = new boolean[6][7]; // 6 filas y números del 1 al 6
        boolean[][] colCheck = new boolean[6][7]; // 6 columnas y números del 1 al 6
        boolean[][] blockCheck = new boolean[6][7]; // 6 bloques 2x3 y números del 1 al 6
        boolean hasError = false; // Marca para verificar si hay algún error

        // Recorre la cuadrícula y verifica las reglas del Sudoku
        for (int row = 0; row < 6; row++) {
            for (int col = 0; col < 6; col++) {
                int blockIndex = (row / 2) * 2 + (col / 3); // Calcula el índice del bloque 2x3
                TextField textField = (TextField) grid.getChildren().get(row * 6 + col);
                String text = textField.getText();

                // Si la celda no está vacía, valida el número
                if (!text.isEmpty()) {
                    int num = Integer.parseInt(text);

                    // Verifica si el número ya está en la fila, columna o bloque
                    if (rowCheck[row][num] || colCheck[col][num] || blockCheck[blockIndex][num]) {
                        // Si se repite, resalta el TextField en rojo
                        textField.setStyle("-fx-background-color: red; -fx-font-size: 18;");
                        hasError = true; // Establece la marca de error
                    } else {
                        // Si no se repite, marca el número como usado
                        rowCheck[row][num] = true;
                        colCheck[col][num] = true;
                        blockCheck[blockIndex][num] = true;
                        // Restablece el estilo del TextField si es válido
                        textField.setStyle("-fx-background-color: white; -fx-font-size: 18;");
                    }
                } else {
                    // Reestablece el estilo para celdas vacías
                    textField.setStyle("-fx-background-color: white; -fx-font-size: 18;");
                }
            }
        }
        // Si hay un error, muestra un mensaje en el StatusLabel
        if (hasError) {
            statusLabel.setText("Error: Número no válido en una celda.");

            // Crea una alerta de error
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error de Validación");
            alert.setHeaderText("Entrada no válida");
            alert.setContentText("Se ha ingresado un número que ya existe en la fila, columna o bloque");
            alert.showAndWait(); // Muestra en pantalla la alerta
        } else {
            statusLabel.setText(""); // Reestablece el mensaje si todo es válido
        }
    }

    private void clearGrid() {
        // Vacía el contenido de los TextFields
        for (int i = 0; i < grid.getChildren().size(); i++) {
            if (grid.getChildren().get(i) instanceof TextField) {
                ((TextField) grid.getChildren().get(i)).setText("");
            }
        }
    }

    private void generateSudokuNumbers() {
        clearGrid(); // Asegura que la cuadrícula esté vacía antes de llenarla

        // Inicializa un conjunto para verificar la existencia de números en filas y columnas
        boolean[][] usedNumbersRows = new boolean[6][7]; // Verificación para filas (6 filas, números del 1 al 6)
        boolean[][] usedNumbersCols = new boolean[6][7]; // Verificación para columnas (6 columnas, números del 1 al 6)

        // Recorre cada bloque 2x3 de la cuadrícula 6x6 y lo rellena
        for (int blockRow = 0; blockRow < 3; blockRow++) { // Tres bloques de 2x3 horizontalmente
            for (int blockCol = 0; blockCol < 2; blockCol++) { // Dos bloques de 2x3 verticalmente

                // Genera dos números aleatorios distintos para el bloque 2x3
                int num1, num2;

                // Genera el primer número aleatorio y verifica que no esté usado en las filas y columnas del bloque
                do {
                    num1 = random.nextInt(6) + 1; // Genera un número entre 1 y 6
                } while (usedNumbersRows[blockRow * 2][num1] || usedNumbersCols[blockCol * 3][num1] ||
                        usedNumbersRows[blockRow * 2 + 1][num1] || usedNumbersCols[blockCol * 3 + 1][num1]);

                // Marca el primer número como usado en las filas y columnas del bloque
                usedNumbersRows[blockRow * 2][num1] = true;
                usedNumbersCols[blockCol * 3][num1] = true;
                usedNumbersRows[blockRow * 2 + 1][num1] = true;
                usedNumbersCols[blockCol * 3 + 1][num1] = true;

                // Genera el segundo número aleatorio y verifica que no sea igual al primero y que no esté usado
                do {
                    num2 = random.nextInt(6) + 1; // Genera un segundo número entre 1 y 6
                } while (num2 == num1 || usedNumbersRows[blockRow * 2][num2] || usedNumbersCols[blockCol * 3][num2] ||
                        usedNumbersRows[blockRow * 2 + 1][num2] || usedNumbersCols[blockCol * 3 + 1][num2]);

                // Marca el segundo número como usado en las filas y columnas de los bloques 2x3
                usedNumbersRows[blockRow * 2][num2] = true;
                usedNumbersCols[blockCol * 3][num2] = true;
                usedNumbersRows[blockRow * 2 + 1][num2] = true;
                usedNumbersCols[blockCol * 3 + 1][num2] = true;

                // Asigna los números generados a las posiciones correspondientes en el bloque 2x3
                for (int row = 0; row < 2; row++) {
                    for (int col = 0; col < 3; col++) {
                        int index = (blockRow * 2 + row) * 6 + (blockCol * 3 + col);
                        if (index < grid.getChildren().size() && grid.getChildren().get(index) instanceof TextField) {
                            TextField textField = (TextField) grid.getChildren().get(index);
                            if ((row == 0 && col == 0) || (row == 1 && col == 1)) {
                                textField.setText(row == 0 ? String.valueOf(num1) : String.valueOf(num2));
                                textField.setEditable(false); // Hace que la celda no sea editable si tiene un número
                            } else {
                                textField.setText(""); // Las otras celdas quedan vacías
                                textField.setEditable(true); // Permitir la edición en las celdas vacías
                            }
                        }
                    }
                }
            }
        }
    }
}