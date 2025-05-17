package battleships.models.bots;

import battleships.interfaces.AiBot;
import battleships.enums.attackResult;
import battleships.models.Board;

public class AiEasy implements AiBot {
    @Override
    public void makeMove(Board playerBoard) {
        int row = (int) (Math.random() * 10);
        int column = (int) (Math.random() * 10);

        while (playerBoard.receiveAttack(row, column) == attackResult.ALREADY_SHOT) {
            row = (int) (Math.random() * 10);
            column = (int) (Math.random() * 10);
        }
    }
}
