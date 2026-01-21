package battleships.models;
import battleships.config.GameConfig;

public record Coordinate(int row, int column) {
    public Coordinate {
        if (row < 0 || column < 0) {
            throw new IllegalArgumentException("Coordinates cannot be negative");
        }
    }

    public static boolean isValid(int row, int col) {
        int size = GameConfig.getInstance().getBoardSize();
        return row >= 0 && row < size && col >= 0 && col < size;
    }
}