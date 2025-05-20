package battleships.interfaces;

import battleships.models.AttackOutcome;
import battleships.models.Board;

public interface AiBot {
    AttackOutcome makeMove(Board playerBoard);
    Board getBoard();
}
