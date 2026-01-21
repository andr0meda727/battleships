package battleships.strategies;

import battleships.enums.AttackResult;
import battleships.models.AttackOutcome;
import battleships.models.Board;

import java.util.Random;


public class RandomAttackStrategy implements AttackStrategy {
    private final Random random;

    public RandomAttackStrategy() {
        this.random = new Random();
    }

    @Override
    public AttackOutcome executeAttack(Board playerBoard) {
        int boardSize = playerBoard.getBoardSize();
        int row, column;
        AttackResult result;

        do {
            row = random.nextInt(boardSize);
            column = random.nextInt(boardSize);
            result = playerBoard.receiveAttack(row, column);
        } while (result == AttackResult.ALREADY_SHOT);

        return new AttackOutcome(row, column, result);
    }

    @Override
    public void reset() {
        // Strategia losowa nie ma stanu do zresetowania
    }

    @Override
    public String getStrategyName() {
        return "Random Attack";
    }
}