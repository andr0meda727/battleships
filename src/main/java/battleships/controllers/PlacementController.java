package battleships.controllers;

import battleships.models.GameState;
import battleships.utils.UIUtils;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;

public class PlacementController {

    @FXML private Button  placeButton;
    @FXML private Button  resetButton;

    private GridPane playerGrid;

    public void setPlayerGrid(GridPane grid) {
        this.playerGrid = grid;
    }


    @FXML
    public void initialize() {
        placeButton.setOnAction(event -> {
            GameState.resetPlayerBoard(); //reset array
            if (playerGrid != null) {
                UIUtils.colorGrid(playerGrid, GameState.getPlayer().getBoard().board); //color after reset
            }
        });

        resetButton.setOnAction(event -> System.out.println("clicked reset"));
    }
}
