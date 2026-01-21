package battleships.models;

public record Coordinate(int row, int column) {
    public Coordinate {
        if (row < 0 || column < 0) {
            throw new IllegalArgumentException("Coordinates cannot be negative");
        }
    }

    public boolean isValid(int boardSize) {
        return row >= 0 && row < boardSize && column >= 0 && column < boardSize;
    }
}