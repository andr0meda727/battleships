package battleships.config;

public class GameConfig {
    private static volatile GameConfig instance;

    private final int boardSize;
    private final int aiThinkingDelay;
    private final boolean animationsEnabled;

    private GameConfig() {
        this.boardSize = 10;
        this.aiThinkingDelay = 500;
        this.animationsEnabled = true;
    }

    public static GameConfig getInstance() {
        if (instance == null) {
            synchronized (GameConfig.class) {
                if (instance == null) {
                    instance = new GameConfig();
                }
            }
        }
        return instance;
    }

    public int getBoardSize() { return boardSize; }
    public int getAiThinkingDelay() { return aiThinkingDelay; }
    public boolean isAnimationsEnabled() { return animationsEnabled; }
}
