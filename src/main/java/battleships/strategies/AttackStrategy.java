package battleships.strategies;

import battleships.models.AttackOutcome;
import battleships.models.Board;

public interface AttackStrategy {
    AttackOutcome executeAttack(Board playerBoard);

    // Resetuje stan strategii (np. po rozpoczęciu nowej gry).
    void reset();

    // Zwraca nazwę strategii dla celów diagnostycznych.
    String getStrategyName();
}