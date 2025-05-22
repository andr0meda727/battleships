package battleships.models;

public class Player {
    private final Board board;

    public Player() {
       this.board = new Board();
    }

    public Board getBoard() {
        return this.board;
    }
}
