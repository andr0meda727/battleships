package battleships.models;

import battleships.enums.Difficulty;
import battleships.interfaces.AiBot;
import battleships.models.bots.AiEasy;
import battleships.models.bots.AiHard;
import battleships.models.bots.AiMedium;

public class GameState {
    private static final Player player = new Player();
    private static AiBot enemy = null;
    private static Difficulty chosenDifficulty = null;
    private static boolean playerTurn = true;
    private static boolean gameEnded = false;

    public static boolean isGameEnded() {
        return gameEnded;
    }

    public static void endGame() {
        gameEnded = true;
    }

    private static boolean gameStarted = false;

    public static boolean isGameStarted() {
        return gameStarted;
    }

    public static void resetGame(){
        gameStarted = false;
        playerTurn = true;
        chosenDifficulty = null;
        gameEnded = false;
        resetEnemyBoard();

        resetPlayerBoard();
    }
    public static void changeGameStart() {
        if(chosenDifficulty == null){
            System.out.println("Pick difficulty first!");
        }else{
            gameStarted = !gameStarted;
            if(chosenDifficulty == Difficulty.EASY) {
                enemy = new AiEasy();
            }
            else if(chosenDifficulty == Difficulty.MEDIUM) {
                enemy = new AiMedium();
            } else if (chosenDifficulty == Difficulty.HARD) {
                enemy = new AiHard();
            }
        }
    }

    public static void setChosenDifficulty(Difficulty chosenDifficulty) {
        GameState.chosenDifficulty = chosenDifficulty;
    }

    public static Player getPlayer() {
        return player;
    }

    public static boolean isYourTurn(){
        return playerTurn;
    }

    public static AiBot getEnemy() {
        return enemy;
    }

    public static void changeTurn(){
        playerTurn = !playerTurn;
    }

    public static void resetPlayerBoard() {
        player.getBoard().resetBoard();
        player.getBoard().resetSunk();
    }
    public static void resetEnemyBoard() {
        enemy.getBoard().resetBoard();
        enemy.getBoard().resetSunk();
    }
}
