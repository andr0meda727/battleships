package battleships.controllers;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class MainMenuController {



    private static final int SIZE = 10;
    private static final int CELL_SIZE = 40;

    @FXML private GridPane playerGrid;
    @FXML private GridPane enemyGrid;

    @FXML private VBox difficultyBox;
    @FXML private VBox placementBox;

    @FXML
    public void initialize() {
        createGrid(playerGrid, "GRACZ");
        createGrid(enemyGrid, "PRZECIWNIK");

        int[][] coordinates = {
                {0, 0, 1, 0, 0, 0, 0, 0, 1, 0},
                {0, 0, 1, 0, 0, 0, 1, 0, 1, 0},
                {0, 0, 1, 0, 0, 0, 1, 0, 0, 0},
                {0, 0, 0, 0, 1, 1, 1, 0, 0, 0},
                {1, 1, 1, 1, 0, 0, 0, 1, 1, 1},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {1, 0, 0, 1, 0, 0, 0, 1, 0, 0},
                {0, 0, 0, 1, 0, 0, 0, 1, 0, 0},
                {0, 0, 0, 1, 1, 1, 1, 1, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0}
        };
        colorGrid(playerGrid,coordinates);
    }

    private void colorGrid(GridPane grid, int[][] coordinates){
        for (Node node : grid.getChildren()) {
            if (node instanceof Rectangle rect) {
                int col = GridPane.getColumnIndex(rect);
                int row = GridPane.getRowIndex(rect);
                if (coordinates[row][col] == 1)
                    rect.setFill(Color.HOTPINK);
            }
        }
    }
    private void createGrid(GridPane grid, String label) {
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                Rectangle cell = new Rectangle(CELL_SIZE, CELL_SIZE);
                cell.setFill(Color.LIGHTGRAY);
                cell.setStroke(Color.BLACK);

                final int finalRow = row;
                final int finalCol = col;
                cell.setOnMouseClicked(e -> {
                    System.out.println("Klik w " + label + " (" + finalRow + ", " + finalCol + ")");
                    cell.setFill(Color.HOTPINK);
                });

                grid.add(cell, col, row);
            }
        }
    }
}
