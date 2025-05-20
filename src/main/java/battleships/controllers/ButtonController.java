package battleships.controllers;

import battleships.models.GameState;
import battleships.utils.UIUtils;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

public class ButtonController {

    @FXML private Button  placeButton;
    @FXML private Button  resetButton;

    private GridPane playerGrid;
    private GridPane enemyGrid;
    private Label chosen;

    public void setChosenLabel(Label chosen){
        this.chosen = chosen;
    }

    public void setPlayerGrid(GridPane grid) {
        this.playerGrid = grid;
    }
    public void setEnemyGrid(GridPane grid) {
        this.enemyGrid = grid;
    }


    @FXML
    public void initialize() {
        placeButton.setOnAction(event -> {
            if(!GameState.isGameStarted()) {
                GameState.resetPlayerBoard(); //reset array
                if (playerGrid != null) {
                    UIUtils.colorGrid(playerGrid, GameState.getPlayer().getBoard().board); //color after reset
                }
            }else{
                System.out.println("Game started, can't change!");
            }
        });

        resetButton.setOnAction(event -> {
            GameState.resetGame();
            UIUtils.colorGrid(playerGrid, GameState.getPlayer().getBoard().board); //color after reset
            UIUtils.colorBlank(enemyGrid);
            chosen.setText("Wybrany poziom: ");
        });
    }
}
