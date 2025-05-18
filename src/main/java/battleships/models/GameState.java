package battleships.models;

public class GameState {
    private static final Player player = new Player();
    private static final Player enemy = new Player();

    public static Player getPlayer() {
        return player;
    }

    public static Player getEnemy() {
        return enemy;
    }
    public static void resetPlayerBoard() {
        player.getBoard().resetBoard();
    }
}
