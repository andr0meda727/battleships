package battleships.observers;

import battleships.models.AttackOutcome;


public interface GameObserver {

    void onPlayerAttack(AttackOutcome outcome);

    void onAiAttack(AttackOutcome outcome);

    void onShipSunk(String shipName, boolean isPlayerShip);

    void onGameEnd(boolean playerWon);

    void onGameReset();
}
