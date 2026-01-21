package battleships.controllers;

import battleships.enums.Difficulty;
import battleships.managers.GameStateManager;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

public class DifficultyController {
    @FXML private ListView<String> difficulty;
    @FXML private Label chosen;

    private GameStateManager gameManager;

    public Label getChosenLabel() {
        return chosen;
    }

    @FXML
    public void initialize() {
        difficulty.getItems().addAll("easy", "medium", "hard");
    }

    public void setGameManager(GameStateManager gameManager) {
        this.gameManager = gameManager;

        difficulty.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldVal, newVal) -> {
                    if (newVal == null) return;

                    if (gameManager != null && gameManager.isGameStarted()) {
                        System.out.println("Nie można zmienić trudności w trakcie gry");
                        return;
                    }

                    Difficulty selected = switch (newVal.toLowerCase()) {
                        case "easy" -> Difficulty.EASY;
                        case "medium" -> Difficulty.MEDIUM;
                        case "hard" -> Difficulty.HARD;
                        default -> null;
                    };

                    if (selected != null && gameManager != null) {
                        gameManager.setChosenDifficulty(selected);
                        chosen.setText("Wybrany poziom: " + newVal);
                    }
                }
        );
    }
}
