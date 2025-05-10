package battleships.views;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class MainMenu
{
    @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }
}