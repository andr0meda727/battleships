package battleships.utils;

import battleships.enums.attackResult;
import battleships.models.AttackOutcome;
import battleships.models.Cell;
import battleships.models.GameState;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class UIUtils {
    public static void colorGrid(GridPane grid, Cell[][] board) {
        for (Node node : grid.getChildren()) {
            if (node instanceof Rectangle rect) {
                Integer col = GridPane.getColumnIndex(rect);
                Integer row = GridPane.getRowIndex(rect);
                if (row != null && col != null && board[row][col].hasShip()) {
                    rect.setFill(Color.HOTPINK);
                } else {
                    rect.setFill(Color.LIGHTGRAY);
                }
            }
        }
    }
    public static void colorBlank(GridPane grid) {
        for (Node node : grid.getChildren()) {
            if (node instanceof Rectangle rect) {
                rect.setFill(Color.LIGHTGRAY);
            }
        }
    }
    public static boolean colorYourAttack(Rectangle cell, int row, int col){
        attackResult attack = GameState.getEnemy().getBoard().receiveAttack(row, col);
        if (attack == attackResult.HIT){
            cell.setFill(Color.RED);
        } else if (attack == attackResult.MISS) {
            cell.setFill(Color.BLUE);
        }else if (attack == attackResult.SUNK) {
            cell.setFill(Color.RED);
            System.out.println("The ship has sunk");
        }else {
            System.out.println("You shoot here already");
            return false;
        }
        return true;
    }
    public static void colorEnemyAttack(GridPane grid, AttackOutcome outcome){
        int targetRow = outcome.row();
        int targetCol = outcome.column();
        attackResult result = outcome.result();

        for (Node node : grid.getChildren()) {
            Integer rowIndex = GridPane.getRowIndex(node);
            Integer colIndex = GridPane.getColumnIndex(node);

            // w GridPane domyślnie może być null, traktuj jako 0
            int r = rowIndex == null ? 0 : rowIndex;
            int c = colIndex == null ? 0 : colIndex;

            if (r == targetRow && c == targetCol && node instanceof Rectangle cell) {
                switch (result) {
                    case HIT, SUNK -> cell.setFill(Color.RED);
                    case MISS -> cell.setFill(Color.BLUE);
                }
                break;
            }
        }
    }
    public static void showEndGamePopup(String message) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Koniec gry");
            alert.setHeaderText(null);
            alert.setContentText(message);
            alert.showAndWait();
        });
    }
}
