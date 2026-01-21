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
            boolean confirmed = UIUtils.showConfirmationDialog("Reset gry", "Czy na pewno chcesz zresetować grę?");
            if (!confirmed) return;
        }

        // 1. Reset logiki gry
        gameManager.resetGame();

        // 2. Reset Twojej planszy (korzysta z Twojej nowej logiki ze stroke)
        UIUtils.colorGrid(playerGrid, gameManager.getPlayer().getBoard().board);

        // 3. Reset planszy bota (czyścimy znaki X i kropki)
        UIUtils.resetEnemyGrid(enemyGrid);

        chosenLabel.setText("Wybrany poziom: ");

        if (onResetCallback != null) {
            onResetCallback.run();
        }
    }
}