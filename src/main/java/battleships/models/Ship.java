package battleships.models;

public class Ship {
    private int length;
    private int hitCount;

    public Ship(int length) {
        this.length = length;
        this.hitCount = 0;
    }

    public void hit() {
        if (!isSunk()) {
            hitCount++;
        }
    }

    public int getLength() {
        return length;
    }

    public boolean isSunk() {
        return hitCount == length;
    }
}
