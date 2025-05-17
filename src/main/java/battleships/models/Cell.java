package battleships.models;

public class Cell {
    private Ship ship;
    private boolean wasShot;

    public Cell() {
        this.ship = null;
        this.wasShot = false;
    }

    public boolean hasShip() {
        return ship != null;
    }

    public void setShip(Ship ship) {
        this.ship = ship;
    }

    public Ship getShip() {
        return ship;
    }

    public boolean wasShot() {
        return wasShot;
    }

    public void shoot() {
        this.wasShot = true;
    }
}
