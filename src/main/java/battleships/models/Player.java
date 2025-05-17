package battleships.models;

public class Player {
    private Board board;

    private Player() {
        Board board = new Board();
    }

    public Board getBoard() {
        return board;
    }
}
