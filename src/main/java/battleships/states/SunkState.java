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

    @Override
    public String getStateName() {
        return "Sunk";
    }

    @Override
    public String getStateColor() {
        return "#F44336"; // Czerwony
    }
}
