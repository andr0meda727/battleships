package battleships.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

public class DifficultyController {

    @FXML
    private ListView<String> difficulty;
    @FXML
    private Label chosen;

    private static final String[] choices = {"easy", "medium", "hard"};
    private String currentDifficulty;

    @FXML
    public void initialize() {
        difficulty.getItems().addAll(choices);
        difficulty.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldVal, newVal) -> {
                    currentDifficulty = newVal;
                    chosen.setText("Wybrany poziom: " + currentDifficulty);
                }
        );
    }
}
