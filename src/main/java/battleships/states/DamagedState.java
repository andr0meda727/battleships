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

    @Override
    public String getStateName() {
        return "Damaged";
    }

    @Override
    public String getStateColor() {
        return "#FF9800"; // Pomarańczowy
    }
}
