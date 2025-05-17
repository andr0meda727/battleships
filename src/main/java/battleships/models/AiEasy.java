package battleships.models;

import battleships.interfaces.AiBot;
import battleships.models.Board.Board;
import battleships.models.Board.attackResult;

public class AiEasy implements AiBot {
    @Override
    public void makeMove(Board playerBoard) {
        int row = (int) (Math.random() * 10);
        int column = (int) (Math.random() * 10);

        while (playerBoard.receiveAttack(row, column) == attackResult.ALREADY_SHOT
                || playerBoard.receiveAttack(row, column) == attackResult.SUNK) {
            row = (int) (Math.random() * 10);
            column = (int) (Math.random() * 10);
        }
    }
}
