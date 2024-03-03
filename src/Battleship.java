import java.util.ArrayList;

public class Battleship {
    private String name;
    private int length;

    public ArrayList<BattleshipPart> getBattleshipParts() {
        return battleshipParts;
    }

    public void setBattleshipParts(ArrayList<BattleshipPart> battleshipParts) {
        this.battleshipParts = battleshipParts;
    }

    public int getIntactParts() {
        return intactParts;
    }

    public void setIntactParts(int intactParts) {
        this.intactParts = intactParts;
    }

    private ArrayList<BattleshipPart> battleshipParts = new ArrayList<>();
    private int intactParts;


    Battleship(String name, int length) {
        this.name = name;
        this.length = length;
        populateBattleshipParts();
        this.intactParts = length;
    }

    private void populateBattleshipParts() {
        for (int i = 0; i < length; i++) {
            battleshipParts.add(new BattleshipPart(BoardCharacters.SHIP.getValue(), this));
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public void notifyHit() {
        intactParts--;
    }

    public boolean isAlive() {
        return intactParts > 0;
    }
}
