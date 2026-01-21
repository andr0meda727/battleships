package battleships.observers;

import battleships.models.AttackOutcome;
import battleships.enums.AttackResult;

public class StatisticsObserver implements GameObserver {
    private int playerShots, playerHits, aiShots, aiHits;
    private int playerShipsSunk, aiShipsSunk;

    @Override
    public void onPlayerAttack(AttackOutcome outcome) {
        playerShots++;
        if (outcome.result() == AttackResult.HIT || outcome.result() == AttackResult.SUNK) {
            playerHits++;
        }
    }

    @Override
    public void onAiAttack(AttackOutcome outcome) {
        aiShots++;
        if (outcome.result() == AttackResult.HIT || outcome.result() == AttackResult.SUNK) {
            aiHits++;
        }
    }

    @Override
    public void onShipSunk(String shipName, boolean isPlayerShip) {
        if (isPlayerShip) playerShipsSunk++;
        else aiShipsSunk++;
    }

    @Override
    public void onGameEnd(boolean playerWon) {}

    public double getPlayerAccuracy() {
        return playerShots > 0 ? (double) playerHits / playerShots * 100 : 0;
    }

    public double getAiAccuracy() {
        return aiShots > 0 ? (double) aiHits / aiShots * 100 : 0;
    }

    public String getReport() {
        return String.format(
                "Gracz: %d strzałów, %.1f%% celności, %d zatopionych\n" +
                "AI: %d strzałów, %.1f%% celności, %d zatopionych",
                playerShots, getPlayerAccuracy(), aiShipsSunk,
                aiShots, getAiAccuracy(), playerShipsSunk
        );
    }

    @Override
    public void onGameReset() {
        reset();
    }

    public void reset() {
        playerShots = playerHits = aiShots = aiHits = 0;
        playerShipsSunk = aiShipsSunk = 0;
    }
}

