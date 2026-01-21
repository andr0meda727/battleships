package battleships.managers;

import battleships.enums.Difficulty;
import battleships.models.bots.BotPlayer;
import battleships.models.AttackOutcome;
import battleships.models.Player;
import battleships.models.Ship;
import battleships.observers.GameObserver;
import battleships.strategies.*;

import java.util.ArrayList;
import java.util.List;


public class GameStateManager {
    private final Player player;
    private BotPlayer botPlayer;
    private Difficulty chosenDifficulty;
    private boolean playerTurn;
    private boolean gameStarted;
    private boolean gameEnded;

    private final List<GameObserver> observers;

    public GameStateManager() {
        this.player = new Player();
        this.playerTurn = true;
        this.gameStarted = false;
        this.gameEnded = false;
        this.observers = new ArrayList<>();
    }

    public void addObserver(GameObserver observer) {
        observers.add(observer);
    }

    public void removeObserver(GameObserver observer) {
        observers.remove(observer);
    }

    public void notifyPlayerAttack(AttackOutcome outcome) {
        observers.forEach(o -> o.onPlayerAttack(outcome));

        if (outcome.result() == battleships.enums.AttackResult.SUNK) {
            Ship ship = botPlayer.getBoard().board[outcome.row()][outcome.column()].getShip();
            observers.forEach(o -> o.onShipSunk(ship.getName(), false));
        }
    }

    public void notifyAiAttack(AttackOutcome outcome) {
        observers.forEach(o -> o.onAiAttack(outcome));

        if (outcome.result() == battleships.enums.AttackResult.SUNK) {
            Ship ship = player.getBoard().board[outcome.row()][outcome.column()].getShip();
            observers.forEach(o -> o.onShipSunk(ship.getName(), true));
        }
    }

    private void notifyGameEnd(boolean playerWon) {
        observers.forEach(o -> o.onGameEnd(playerWon));
    }

    public void startGame(Difficulty difficulty) {
        if (difficulty == null) {
            throw new IllegalStateException("Wybierz poziom trudności");
        }

        this.chosenDifficulty = difficulty;
        this.botPlayer = createAiPlayer(difficulty);
        this.gameStarted = true;
        this.gameEnded = false;
        this.playerTurn = true;
    }

    private BotPlayer createAiPlayer(Difficulty difficulty) {
        AttackStrategy strategy = switch (difficulty) {
            case EASY -> new RandomAttackStrategy();
            case MEDIUM -> new HuntTargetStrategy();
            case HARD -> new ProbabilityAttackStrategy();
        };

        return new BotPlayer(strategy);
    }

    public void resetGame() {
        gameStarted = false;
        gameEnded = false;
        playerTurn = true;
        chosenDifficulty = null;

        player.reset();
        if (botPlayer != null) {
            botPlayer.reset();
            botPlayer = null;
        }

        observers.forEach(GameObserver::onGameReset);
    }

    public void endGame(boolean playerWon) {
        gameEnded = true;
        notifyGameEnd(playerWon);
    }

    public void switchTurn() {
        playerTurn = !playerTurn;
    }

    // Gettery
    public Player getPlayer() { return player; }
    public BotPlayer getAiPlayer() { return botPlayer; }
    public boolean isPlayerTurn() { return playerTurn; }
    public boolean isGameStarted() { return gameStarted; }
    public boolean isGameEnded() { return gameEnded; }
    public Difficulty getChosenDifficulty() { return chosenDifficulty; }
    public List<GameObserver> getObservers() { return new ArrayList<>(observers); }

    public void setChosenDifficulty(Difficulty difficulty) {
        if (gameStarted) {
            throw new IllegalStateException("Nie można zmienić trudności w trakcie gry");
        }
        this.chosenDifficulty = difficulty;
    }
}