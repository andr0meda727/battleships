package battleships.models.bots;

import battleships.interfaces.AiBot;
import battleships.enums.attackResult;
import battleships.models.AttackOutcome;
import battleships.models.Board;

public class AiEasy implements AiBot {
    private final Board board;
    public AiEasy() {
        this.board = new Board();
    }

    @Override
    public Board getBoard() {
        return this.board;
    }
    @Override
    public AttackOutcome makeMove(Board playerBoard) {
        int row = (int) (Math.random() * 10);
        int column = (int) (Math.random() * 10);
        attackResult result = playerBoard.receiveAttack(row, column);
        while (result == attackResult.ALREADY_SHOT) {
            row = (int) (Math.random() * 10);
            column = (int) (Math.random() * 10);
            result = playerBoard.receiveAttack(row, column);
        }
        return new AttackOutcome(row, column, result);
    }
}
