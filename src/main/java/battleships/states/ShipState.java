package battleships.states;

import battleships.models.Ship;

public interface ShipState {
    ShipState hit(Ship ship);
    boolean isSunk();
}