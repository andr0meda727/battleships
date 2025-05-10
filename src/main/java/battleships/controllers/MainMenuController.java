package battleships.controllers;

import javafx.fxml.FXML;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class MainMenuController {



    private static final int SIZE = 10;
    private static final int CELL_SIZE = 40;

    @FXML private GridPane playerGrid;
    @FXML private GridPane enemyGrid;

    @FXML
    public void initialize() {
        createGrid(playerGrid, "GRACZ");
        createGrid(enemyGrid, "PRZECIWNIK");
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
