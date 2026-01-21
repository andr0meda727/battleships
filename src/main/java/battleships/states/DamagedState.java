package battleships.states;

import battleships.models.Ship;

public class DamagedState implements ShipState {

    @Override
    public ShipState hit(Ship ship) {
        ship.incrementHitCount();

        if (ship.getHitCount() >= ship.getLength()) {
            return new SunkState();
        }

        return this; // Pozostaje uszkodzony
    }

    @Override
    public boolean isSunk() {
        return false;
    }
}
