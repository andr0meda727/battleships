package battleships.utils;

import battleships.models.Cell;
import javafx.scene.Node;
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
}
