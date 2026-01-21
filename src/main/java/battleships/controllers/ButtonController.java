package battleships.controllers;

import battleships.managers.GameStateManager;
import battleships.utils.UIUtils;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

public class ButtonController {
    @FXML private Button placeButton;
    @FXML private Button resetButton;

    private GameStateManager gameManager;
    private GridPane playerGrid;
    private GridPane enemyGrid;
    private Label chosenLabel;
    private Runnable onResetCallback;

    public void initialize(GameStateManager gameManager, GridPane playerGrid,
                           GridPane enemyGrid, Label chosenLabel, Runnable onResetCallback) {
        this.gameManager = gameManager;
        this.playerGrid = playerGrid;
        this.enemyGrid = enemyGrid;
        this.chosenLabel = chosenLabel;
        this.onResetCallback = onResetCallback;

        placeButton.setOnAction(event -> handlePlaceShips());
        resetButton.setOnAction(event -> handleReset());
    }

    private void handlePlaceShips() {
        if (gameManager.isGameStarted()) {
            System.out.println("Nie można zmieniać rozmieszczenia w trakcie gry");
            return;
        }

        gameManager.getPlayer().reset();
        UIUtils.colorGrid(playerGrid, gameManager.getPlayer().getBoard().board);
    }

    private void handleReset() {
        if (gameManager.isGameStarted()) {
            boolean confirmed = UIUtils.showConfirmationDialog(
                    "Reset gry",
                    "Czy na pewno chcesz zresetować grę?"
            );
            if (!confirmed) return;
        }

        gameManager.resetGame();
        UIUtils.colorGrid(playerGrid, gameManager.getPlayer().getBoard().board);
        UIUtils.colorBlank(enemyGrid);
        chosenLabel.setText("Wybrany poziom: ");

        if (onResetCallback != null) {
            onResetCallback.run();
        }
    }
}