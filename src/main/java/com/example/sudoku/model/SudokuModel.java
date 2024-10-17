package com.example.sudoku.model;

import java.util.Random;

public class SudokuModel {
    private final int gridSize = 6;
    private final Random random = new Random();
    private boolean[][] usedNumbersRows = new boolean[gridSize][gridSize + 1]; // Verificación para filas
    private boolean[][] usedNumbersCols = new boolean[gridSize][gridSize + 1]; // Verificación para columnas

    public SudokuModel() {
        // Inicializar las matrices de verificación
        resetUsedNumbers();
    }

    public void resetUsedNumbers() {
        // Restablecer las matrices de verificación
        usedNumbersRows = new boolean[gridSize][gridSize + 1];
        usedNumbersCols = new boolean[gridSize][gridSize + 1];
    }

    public int[][] generateSudokuNumbers() {
        int[][] grid = new int[gridSize][gridSize];
        resetUsedNumbers();

        // Recorre cada bloque 2x3 de la cuadrícula 6x6 y lo rellena
        for (int blockRow = 0; blockRow < 3; blockRow++) {
            for (int blockCol = 0; blockCol < 2; blockCol++) {
                int num1, num2;
                // Genera el primer número aleatorio
                do {
                    num1 = random.nextInt(gridSize) + 1;
                } while (usedNumbersRows[blockRow * 2][num1] || usedNumbersCols[blockCol * 3][num1] ||
                        usedNumbersRows[blockRow * 2 + 1][num1] || usedNumbersCols[blockCol * 3 + 1][num1]);

                markNumberAsUsed(blockRow * 2, blockCol * 3, num1);
                markNumberAsUsed(blockRow * 2 + 1, blockCol * 3, num1);

                // Genera el segundo número aleatorio
                do {
                    num2 = random.nextInt(gridSize) + 1;
                } while (num2 == num1 || usedNumbersRows[blockRow * 2][num2] || usedNumbersCols[blockCol * 3][num2] ||
                        usedNumbersRows[blockRow * 2 + 1][num2] || usedNumbersCols[blockCol * 3 + 1][num2]);

                markNumberAsUsed(blockRow * 2, blockCol * 3 + 1, num2);
                markNumberAsUsed(blockRow * 2 + 1, blockCol * 3 + 1, num2);

                // Coloca los números generados en la cuadrícula
                grid[blockRow * 2][blockCol * 3] = num1;
                grid[blockRow * 2 + 1][blockCol * 3 + 1] = num2;
            }
        }
        return grid;
    }

    private void markNumberAsUsed(int row, int col, int num) {
        usedNumbersRows[row][num] = true;
        usedNumbersCols[col][num] = true;
    }

    public boolean isNumberValid(int row, int col, int num, int[][] grid) {
        // Comprueba si el número ya está presente en la fila, columna o bloque
        for (int i = 0; i < gridSize; i++) {
            if (grid[row][i] == num || grid[i][col] == num) {
                return false;
            }
        }
        int blockRowStart = (row / 2) * 2;
        int blockColStart = (col / 3) * 3;
        for (int r = blockRowStart; r < blockRowStart + 2; r++) {
            for (int c = blockColStart; c < blockColStart + 3; c++) {
                if (grid[r][c] == num) {
                    return false;
                }
            }
        }
        return true;
    }

    public int suggestNumber(int row, int col, int[][] grid) {
        // Sugerir un número que no esté presente en la fila, columna o bloque
        for (int num = 1; num <= gridSize; num++) {
            if (isNumberValid(row, col, num, grid)) {
                return num;
            }
        }
        return -1; // Retorna -1 si no hay sugerencias válidas
    }
}
