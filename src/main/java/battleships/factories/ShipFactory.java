package battleships.factories;

import battleships.models.Ship;
import battleships.enums.ShipType;


public class ShipFactory {
    public static Ship createShip(ShipType type) {
        return new Ship(type.getLength(), type.getDisplayName());
    }

    public static Ship[] createFleet() {
        return new Ship[] {
                createShip(ShipType.CARRIER),
                createShip(ShipType.BATTLESHIP),
                createShip(ShipType.CRUISER),
                createShip(ShipType.CRUISER), // Dwa krążowniki
                createShip(ShipType.DESTROYER)
        };
    }
}
