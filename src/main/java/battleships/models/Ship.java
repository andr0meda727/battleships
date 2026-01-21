package battleships.models;

import battleships.states.OperationalState;
import battleships.states.ShipState;


public class Ship {
    private final int length;
    private int hitCount;
    private ShipState state;
    private final String name;

    public Ship(int length, String name) {
        if (length <= 0) {
            throw new IllegalArgumentException("Ship length must be positive");
        }
        this.length = length;
        this.hitCount = 0;
        this.state = new OperationalState();
        this.name = name;
    }

    public Ship(int length) {
        this(length, "Ship-" + length);
    }


    public void hit() {
        state = state.hit(this);
    }

    public void incrementHitCount() {
        if (hitCount < length) {
            hitCount++;
        }
    }

    public boolean isSunk() {
        return state.isSunk();
    }

    public int getLength() {
        return length;
    }

    public int getHitCount() {
        return hitCount;
    }

    public ShipState getState() {
        return state;
    }

    public String getName() {
        return name;
    }

    public double getDamagePercentage() {
        return (double) hitCount / length * 100;
    }
}
