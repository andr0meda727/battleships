package battleships.observers;

import battleships.models.AttackOutcome;


public interface GameObserver {

    // Powiadomienie o ataku gracza.
    void onPlayerAttack(AttackOutcome outcome);

    // Powiadomienie o ataku AI.
    void onAiAttack(AttackOutcome outcome);

    // Powiadomienie o zatopioniu statku.
    void onShipSunk(String shipName, boolean isPlayerShip);

    // Powiadomienie o zakończeniu gry.
    void onGameEnd(boolean playerWon);
}
