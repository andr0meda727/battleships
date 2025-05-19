package battleships.utils;

import battleships.enums.attackResult;
import battleships.models.Cell;
import battleships.models.GameState;
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
    public static void colorAttack(Rectangle cell,int row, int col){
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
        }
    }
}
