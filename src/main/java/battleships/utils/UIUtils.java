package battleships.utils;

import battleships.enums.AttackResult;
import battleships.managers.GameStateManager;
import battleships.models.AttackOutcome;
import battleships.models.Cell;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class UIUtils {

    public static void colorGrid(GridPane grid, Cell[][] board) {
        grid.getChildren().forEach(node -> {
            if (node instanceof Rectangle rect) {
                Integer col = GridPane.getColumnIndex(rect);
                Integer row = GridPane.getRowIndex(rect);

                if (row != null && col != null) {
                    rect.setFill(board[row][col].hasShip() ? Color.HOTPINK : Color.LIGHTGRAY);
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

    public static void colorAiAttack(GridPane grid, AttackOutcome outcome) {
        grid.getChildren().forEach(node -> {
            Integer rowIndex = GridPane.getRowIndex(node);
            Integer colIndex = GridPane.getColumnIndex(node);

            int r = rowIndex == null ? 0 : rowIndex;
            int c = colIndex == null ? 0 : colIndex;

            if (r == outcome.row() && c == outcome.column() && node instanceof Rectangle cell) {
                Color color = switch (outcome.result()) {
                    case HIT, SUNK -> Color.RED;
                    case MISS -> Color.BLUE;
                    case ALREADY_SHOT -> null;
                };

                if (color != null) {
                    cell.setFill(color);
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
