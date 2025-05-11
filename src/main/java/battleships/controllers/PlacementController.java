package battleships.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class PlacementController {

    @FXML private Button  placeButton;
    @FXML private Button  resetButton;

    @FXML
    public void initialize() {
        placeButton.setOnAction(event -> System.out.println("clicked place"));

        resetButton.setOnAction(event -> System.out.println("clicked reset"));
    }
}
