package battleships.controllers;

import battleships.models.Board;
import battleships.models.GameState;
import battleships.utils.UIUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.io.IOException;
import java.util.Objects;

public class MainMenuController {

    private static final int SIZE = 10;
    private static final int CELL_SIZE = 40;

    @FXML private GridPane playerGrid;
    @FXML private GridPane enemyGrid;

    @FXML private VBox difficultyBox;
    @FXML private VBox placementBox;

    @FXML
    public void initialize() throws IOException {
        createGrid(playerGrid, "PLAYER");
        createGrid(enemyGrid, "ENEMY");

        //Load manually placement view and access it
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/battleships/views/placement-view.fxml"));
        Node placementNode = loader.load();
        placementBox.getChildren().add(placementNode);

        //link playerGrid to button controller so it can change it
        PlacementController placementController = loader.getController();
        placementController.setPlayerGrid(playerGrid);

        Board playerBoard = GameState.getPlayer().getBoard();
        Board enemyBoard = GameState.getEnemy().getBoard();

        //color only player setup, we don't see enemy
        UIUtils.colorGrid(playerGrid, playerBoard.board);
    }

    private void createGrid(GridPane grid, String label) {
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                Rectangle cell = new Rectangle(CELL_SIZE, CELL_SIZE);
                cell.setFill(Color.LIGHTGRAY);
                cell.setStroke(Color.BLACK);
                if(Objects.equals(label, "ENEMY")) {
                    //needed to pass to color function
                    int finalRow = row;
                    int finalCol = col;
                    cell.setOnMouseClicked(e -> {
                        if(GameState.isYourTurn()) {
                            UIUtils.colorAttack(cell, finalRow, finalCol);
                            GameState.changeTurn();
                        }
                        else {
                            System.out.println("Not your turn!");
                        }
                    });
                }
                grid.add(cell, col, row);
            }
        }
    }
}
