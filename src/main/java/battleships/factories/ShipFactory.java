package battleships.factories;

import battleships.models.Ship;
import battleships.enums.ShipType;


public class ShipFactory {
    public static Ship createShip(ShipType type) {
        return new Ship(type.getLength(), type.getDisplayName());
    }

    /**
     * Tworzy statek na podstawie długości (dla wstecznej kompatybilności).
     */
    public static Ship createShip(int length) {
        for (ShipType type : ShipType.values()) {
            if (type.getLength() == length) {
                return createShip(type);
            }
        }
        return new Ship(length, "Statek-" + length);
    }

    /**
     * Tworzy pełną flotę statków.
     */
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
