package battleships.states;
import battleships.models.Ship;

public class OperationalState implements ShipState {
    @Override
    public ShipState hit(Ship ship) {
        ship.incrementHitCount();

        if (ship.getHitCount() >= ship.getLength()) {
            return new SunkState();
        }

        return new DamagedState();
    }

    @Override
    public boolean isSunk() {
        return false;
    }

    @Override
    public String getStateName() {
        return "Operational";
    }

    @Override
    public String getStateColor() {
        return "#4CAF50"; // Zielony
    }
}
