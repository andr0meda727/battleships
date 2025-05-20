package battleships.controllers;

import battleships.enums.Difficulty;
import battleships.models.GameState;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

import java.util.Objects;

public class DifficultyController {

    @FXML
    private ListView<String> difficulty;
    @FXML
    private Label chosen;
    public Label getChosenLabel() {
        return chosen;
    }
    private static final String[] choices = {"easy", "medium", "hard"};
    private Difficulty currentDifficulty;

    @FXML
    public void initialize() {
        difficulty.getItems().addAll(choices);
        difficulty.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldVal, newVal) -> {
                    if (!GameState.isGameStarted()) {
                        if (Objects.equals(newVal, "easy"))
                            currentDifficulty = Difficulty.EASY;
                        else if (Objects.equals(newVal, "medium"))
                            currentDifficulty = Difficulty.MEDIUM;
                        else if (Objects.equals(newVal, "hard"))
                            currentDifficulty = Difficulty.HARD;

                        chosen.setText("Wybrany poziom: " + newVal);
                        GameState.setChosenDifficulty(currentDifficulty);
                    }
                }
        );
    }
}
