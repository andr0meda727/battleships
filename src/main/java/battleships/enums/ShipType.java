package battleships.enums;

public enum ShipType {
    CARRIER(5, "Lotniskowiec"),
    BATTLESHIP(4, "Pancernik"),
    CRUISER(3, "Krążownik"),
    DESTROYER(2, "Niszczyciel"),
    SUBMARINE(1, "Łódź podwodna");

    private final int length;
    private final String displayName;

    ShipType(int length, String displayName) {
        this.length = length;
        this.displayName = displayName;
    }

    public int getLength() {
        return length;
    }

    public String getDisplayName() {
        return displayName;
    }
}