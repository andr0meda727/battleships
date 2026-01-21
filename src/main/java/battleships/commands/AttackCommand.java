package battleships.commands;

import battleships.enums.AttackResult;
import battleships.managers.GameStateManager;
import battleships.models.AttackOutcome;
import battleships.models.Coordinate;


public class AttackCommand {
    private final GameStateManager gameManager;
    private final Coordinate target;
    private AttackOutcome outcome;

    public AttackCommand(GameStateManager gameManager, Coordinate target) {
        this.gameManager = gameManager;
        this.target = target;
    }

    public boolean execute() {
        if (!gameManager.isGameStarted() || gameManager.isGameEnded()) {
            return false;
        }

        if (!gameManager.isPlayerTurn()) {
            return false;
        }

        AttackResult result = gameManager.getAiPlayer()
                .getBoard()
                .receiveAttack(target.row(), target.column());

        if (result == AttackResult.ALREADY_SHOT) {
            return false;
        }

        this.outcome = new AttackOutcome(target.row(), target.column(), result);
        gameManager.notifyPlayerAttack(outcome);

        return true;
    }

    public AttackOutcome getOutcome() {
        return outcome;
    }
}
