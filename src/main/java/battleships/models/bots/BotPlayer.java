package battleships.models.bots;

import battleships.models.AttackOutcome;
import battleships.models.Board;
import battleships.strategies.AttackStrategy;

public class BotPlayer {
    private final Board board;
    private final AttackStrategy strategy;

    public BotPlayer(AttackStrategy strategy) {
        this.board = new Board();
        this.strategy = strategy;
    }

    public AttackOutcome makeMove(Board playerBoard) {
        return strategy.executeAttack(playerBoard);
    }

    public Board getBoard() { return board; }
    public AttackStrategy getStrategy() { return strategy; }

    public void reset() {
        board.resetBoard();
        strategy.reset();
    }
}
