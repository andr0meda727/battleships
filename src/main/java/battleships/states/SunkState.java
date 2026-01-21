package battleships.states;

import battleships.models.Ship;

public class SunkState implements ShipState {

    @Override
    public ShipState hit(Ship ship) {
        return this;
    }

    @Override
    public boolean isSunk() {
        return true;
    }
}
