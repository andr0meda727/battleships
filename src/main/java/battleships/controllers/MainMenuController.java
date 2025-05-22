package battleships.controllers;

import battleships.models.AttackOutcome;
import battleships.models.Board;
import battleships.models.GameState;
import battleships.utils.UIUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
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

    @FXML private DifficultyController difficultyBoxController;

    @FXML private Button startButton;

    @FXML
    public void initialize() throws IOException {
        createGrid(playerGrid, "PLAYER");
        createGrid(enemyGrid, "ENEMY");

        //Load manually placement view and access it
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/battleships/views/placement-view.fxml"));
        Node placementNode = loader.load();
        placementBox.getChildren().add(placementNode);

        //link playerGrid to button controller so it can change it
        ButtonController buttonController = loader.getController();
        buttonController.setPlayerGrid(playerGrid);
        buttonController.setEnemyGrid(enemyGrid);
        buttonController.setChosenLabel( difficultyBoxController.getChosenLabel() );


        Board playerBoard = GameState.getPlayer().getBoard();
        //Board enemyBoard = GameState.getEnemy().getBoard();

        //color only player setup, we don't see enemy
        UIUtils.colorGrid(playerGrid, playerBoard.board);

        startButton.setOnMouseClicked(event ->{
            if(!GameState.isGameStarted()) {
                GameState.changeGameStart();
            }
        });
    }
    private void handleEnemyShot(){
        AttackOutcome outcome = GameState.getEnemy().makeMove(GameState.getPlayer().getBoard());
        UIUtils.colorEnemyAttack(playerGrid,outcome);
        GameState.changeTurn();
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
                    cell.setOnMouseClicked(event -> {
                        if(GameState.isGameStarted() && !GameState.isGameEnded()) {
                            if (GameState.isYourTurn()) {
                                if(UIUtils.colorYourAttack(cell, finalRow, finalCol)){
                                    if (GameState.isGameEnded()){
                                        UIUtils.showEndGamePopup("Gratulacje!");
                                    }else{
                                        GameState.changeTurn();
                                        handleEnemyShot();
                                        if (GameState.isGameEnded())
                                            UIUtils.showEndGamePopup("Niestety przegrałeś!");
                                    }

                                }
                            } else {
                                System.out.println("Not your turn!");
                            }
                        }else{
                            System.out.println("Start the game first!");
                        }
                    });
                }
                grid.add(cell, col, row);
            }
        }
    }
}
