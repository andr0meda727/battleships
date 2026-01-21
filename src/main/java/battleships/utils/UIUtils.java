package battleships.utils;

import battleships.enums.AttackResult;
import battleships.managers.GameStateManager;
import battleships.models.AttackOutcome;
import battleships.models.Cell;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class UIUtils {
    public static final Color COLOR_WATER = Color.web("#2c3e50");
    public static final Color COLOR_SHIP = Color.web("#95a5a6");
    public static final Color COLOR_HIT = Color.web("#e74c3c");
    public static final Color COLOR_MISS = Color.web("#3498db");
    public static final Color COLOR_SUNK = Color.web("#c0392b");

    public static void colorGrid(GridPane grid, Cell[][] board) {
        grid.getChildren().forEach(node -> {
            if (node instanceof StackPane pane) {
                Rectangle rect = (Rectangle) pane.getChildren().get(0);
                Label mark = (Label) pane.getChildren().get(1);

                Integer col = GridPane.getColumnIndex(pane);
                Integer row = GridPane.getRowIndex(pane);

                if (row != null && col != null) {
                    mark.setText(""); // Reset znaku X

                    // ZAWSZE ustawiamy tło wody, aby statki nie miały wypełnienia
                    rect.setFill(COLOR_WATER);

                    if (board[row][col].hasShip()) {
                        // Tylko zielone obramowanie dla Twoich statków
                        rect.setStroke(Color.web("#2ecc71"));
                        rect.setStrokeWidth(3.0);
                    } else {
                        // Standardowe obramowanie dla pustej wody
                        rect.setStroke(Color.web("#16213e"));
                        rect.setStrokeWidth(0.5);
                    }
                }
            }
        });
    }

    public static void colorBlank(GridPane grid) {
        grid.getChildren().forEach(node -> {
            if (node instanceof Rectangle rect) {
                rect.setFill(Color.LIGHTGRAY);
            }
        });
    }

    public static boolean colorPlayerAttack(Rectangle cell, AttackResult result) {
        Color color = switch (result) {
            case HIT -> Color.RED;
            case MISS -> Color.BLUE;
            case SUNK -> Color.DARKRED;
            case ALREADY_SHOT -> null;
        };

        if (color != null) {
            cell.setFill(color);
            return true;
        }
        return false;
    }

    public static void updateAttackUI(StackPane pane, AttackResult result) {
        Rectangle rect = (Rectangle) pane.getChildren().get(0);
        Label mark = (Label) pane.getChildren().get(1);

        switch (result) {
            case HIT -> {
                // Tylko znak X, tło zostaje (woda lub heatmapa)
                mark.setText("X");
                mark.setStyle("-fx-text-fill: #ff4d4d; -fx-font-size: 22px; -fx-font-weight: bold;");
                // Nie zmieniamy rect.setFill!
            }
            case SUNK -> {
                // Znak X i zmiana koloru ramki na czerwony
                mark.setText("X");
                mark.setStyle("-fx-text-fill: #b30000; -fx-font-size: 22px; -fx-font-weight: bold;");
                rect.setStroke(Color.web("#b30000")); // Czerwona ramka dla zatopionego
                rect.setStrokeWidth(3.0);
            }
            case MISS -> {
                mark.setText("•");
                mark.setStyle("-fx-text-fill: #3498db; -fx-font-size: 20px;");
                rect.setFill(COLOR_WATER);
                rect.setStroke(Color.web("#16213e"));
                rect.setStrokeWidth(0.5);
            }
        }
    }

    public static void colorAiAttack(GridPane grid, AttackOutcome outcome) {
        grid.getChildren().forEach(node -> {
            if (node instanceof StackPane pane) {
                Integer r = GridPane.getRowIndex(pane);
                Integer c = GridPane.getColumnIndex(pane);
                if (r != null && c != null && r == outcome.row() && c == outcome.column()) {
                    updateAttackUI(pane, outcome.result());
                }
            }
        });
    }

    public static void showEndGamePopup(String message, GameStateManager gameManager) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Koniec gry");
            alert.setHeaderText(null);
            alert.setContentText(message);
            alert.showAndWait();
        });
    }

    public static boolean showConfirmationDialog(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);

        return alert.showAndWait()
                .filter(response -> response == ButtonType.OK)
                .isPresent();
    }
}
