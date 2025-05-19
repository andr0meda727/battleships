package battleships.models;

public class GameState {
    private static final Player player = new Player();
    private static final Player enemy = new Player();

    private static boolean playerTurn = true;

    public static Player getPlayer() {
        return player;
    }

    public static boolean isYourTurn(){
        return playerTurn;
    }

    public static Player getEnemy() {
        return enemy;
    }

    public static void changeTurn(){
        playerTurn = !playerTurn;
    }

    public static void resetPlayerBoard() {
        player.getBoard().resetBoard();
    }
}
