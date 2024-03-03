public class BattleshipPart extends Cell{
    private Battleship parentShip;
    private boolean isAlive;

    BattleshipPart(String value, Battleship parentShip) {
        super(value);
        this.parentShip = parentShip;
        isAlive = true;
    }

    public Battleship getParentShip() {
        return parentShip;
    }

    public void setParentShip(Battleship parentShip) {
        this.parentShip = parentShip;
    }

    public void setAlive(boolean alive) {
        isAlive = alive;
    }

    public void hit() {
        isAlive = false;
        parentShip.notifyHit();
    }

    public boolean isAlive() {
        return isAlive;
    }
}