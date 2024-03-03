
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Player {
    private String name;
    private Player opponent;
    private ArrayList<ArrayList<Cell>> shipsBoard = new ArrayList<>();
    private ArrayList<ArrayList<Cell>> gameBoard = new ArrayList<>();
    ArrayList<Battleship> battleships;
    private String ship = BoardCharacters.SHIP.getValue();
    private String mist = BoardCharacters.MIST.getValue();
    private Cell mistCell = new Cell(mist);
    private Scanner scanner = new Scanner(System.in);


    Player (String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setOpponent(Player opponent) {
        this.opponent = opponent;
    }

    public void initializeBoard() {
        shipsBoard.add(new ArrayList<>(Arrays.asList(
                new Cell(" "),
                new Cell("1"),
                new Cell("2"),
                new Cell("3"),
                new Cell("4"),
                new Cell("5"),
                new Cell("6"),
                new Cell("7"),
                new Cell("8"),
                new Cell("9"),
                new Cell("10")
        )));
        shipsBoard.add(new ArrayList<>(Arrays.asList(
                new Cell("A"),
                mistCell,
                mistCell,
                mistCell,
                mistCell,
                mistCell,
                mistCell,
                mistCell,
                mistCell,
                mistCell,
                mistCell
        )));
        shipsBoard.add(new ArrayList<>(Arrays.asList(
                new Cell("B"),
                mistCell,
                mistCell,
                mistCell,
                mistCell,
                mistCell,
                mistCell,
                mistCell,
                mistCell,
                mistCell,
                mistCell
        )));
        shipsBoard.add(new ArrayList<>(Arrays.asList(
                new Cell("C"),
                mistCell,
                mistCell,
                mistCell,
                mistCell,
                mistCell,
                mistCell,
                mistCell,
                mistCell,
                mistCell,
                mistCell
        )));
        shipsBoard.add(new ArrayList<>(Arrays.asList(
                new Cell("D"),
                mistCell,
                mistCell,
                mistCell,
                mistCell,
                mistCell,
                mistCell,
                mistCell,
                mistCell,
                mistCell,
                mistCell
        )));
        shipsBoard.add(new ArrayList<>(Arrays.asList(
                new Cell("E"),
                mistCell,
                mistCell,
                mistCell,
                mistCell,
                mistCell,
                mistCell,
                mistCell,
                mistCell,
                mistCell,
                mistCell
        )));
        shipsBoard.add(new ArrayList<>(Arrays.asList(
                new Cell("F"),
                mistCell,
                mistCell,
                mistCell,
                mistCell,
                mistCell,
                mistCell,
                mistCell,
                mistCell,
                mistCell,
                mistCell
        )));
        shipsBoard.add(new ArrayList<>(Arrays.asList(
                new Cell("G"),
                mistCell,
                mistCell,
                mistCell,
                mistCell,
                mistCell,
                mistCell,
                mistCell,
                mistCell,
                mistCell,
                mistCell
        )));
        shipsBoard.add(new ArrayList<>(Arrays.asList(
                new Cell("H"),
                mistCell,
                mistCell,
                mistCell,
                mistCell,
                mistCell,
                mistCell,
                mistCell,
                mistCell,
                mistCell,
                mistCell
        )));
        shipsBoard.add(new ArrayList<>(Arrays.asList(
                new Cell("I"),
                mistCell,
                mistCell,
                mistCell,
                mistCell,
                mistCell,
                mistCell,
                mistCell,
                mistCell,
                mistCell,
                mistCell
        )));
        shipsBoard.add(new ArrayList<>(Arrays.asList(
                new Cell("J"),
                mistCell,
                mistCell,
                mistCell,
                mistCell,
                mistCell,
                mistCell,
                mistCell,
                mistCell,
                mistCell,
                mistCell
        )));

        gameBoard = new ArrayList<>(shipsBoard.stream()
                .map(ArrayList::new)
                .collect(Collectors.toList()));
    }

    public void populateBattleships() {
        Battleship aircraftCarrier = new Battleship("Aircraft Carrier", 5);
        Battleship battleship = new Battleship("Battleship", 4);
        Battleship submarine = new Battleship("Submarine", 3);
        Battleship cruiser = new Battleship("Cruiser", 3);
        Battleship destroyer = new Battleship("Destroyer", 2);

        battleships = new ArrayList<>(Arrays.asList(
                aircraftCarrier,
                battleship,
                submarine,
                cruiser,
                destroyer
        ));
    }

    public ArrayList<ArrayList<Cell>> getShipsBoard() {
        return shipsBoard;
    }

    public ArrayList<Battleship> getBattleships() {
        return battleships;
    }

    public ArrayList<ArrayList<Cell>> getGameBoard() {
        return gameBoard;
    }

    public void placeShipsOnBoard() {
        Battleship currentBattleship;
        System.out.printf("%s, place your ships on the game field\n", name);
        Utils.displayBoard(shipsBoard);

        for (Battleship battleship : battleships) {
            currentBattleship = battleship;
            char firstCoordinateLetter;
            int firstCoordinateNumber;
            char secondCoordinateLetter;
            int secondCoordinateNumber;
            System.out.printf("Enter the coordinates of the %s (%d cells):\n", currentBattleship.getName(), currentBattleship.getLength());

            inputLoop:
            do {
                String coordinatesLine = scanner.nextLine();
                ArrayList<String> coordinates =  new ArrayList<>(Arrays.asList(coordinatesLine.split(" ")));

                String firstCoordinate = coordinates.get(0);
                int firstCoordinateLength = coordinates.get(0).length();
                firstCoordinateLetter = firstCoordinate.charAt(0);
                String firstCoordinateNumberString = firstCoordinate.substring(1, firstCoordinateLength);
                firstCoordinateNumber = Integer.parseInt(firstCoordinateNumberString);

                String secondCoordinate = coordinates.get(1);
                int secondCoordinateLength = coordinates.get(1).length();
                secondCoordinateLetter = secondCoordinate.charAt(0);
                String secondCoordinateNumberString = secondCoordinate.substring(1, secondCoordinateLength);
                secondCoordinateNumber = Integer.parseInt(secondCoordinateNumberString);

                if (!Utils.areValidCoordinates(firstCoordinateLetter, firstCoordinateNumber, secondCoordinateLetter, secondCoordinateNumber)) {
                    System.out.println("Error! Wrong ship location! Try again:");
                    continue;
                }

                ArrayList<BattleshipPart> battleshipParts = battleship.getBattleshipParts();

                if (firstCoordinateLetter == secondCoordinateLetter) {
                    int length = Math.abs(firstCoordinateNumber - secondCoordinateNumber) + 1;

                    if (length != currentBattleship.getLength()) {
                        System.out.println("Error! Wrong length of the Submarine! Try again:");
                        continue;
                    }

                    int incrementer = firstCoordinateNumber > secondCoordinateNumber ? -1 : 1;
                    int rowIndex = Utils.letterToIndex.get(firstCoordinateLetter);
                    ArrayList<Cell> row = shipsBoard.get(rowIndex);
                    int cell = firstCoordinateNumber;

                    for (int i = 0; i < length; i++) {

                        if (
                                hasShipCellAbove(rowIndex, cell) ||
                                        hasShipNextCell(rowIndex, cell) ||
                                        hasShipPreviousCell(rowIndex, cell) ||
                                        hasShipCellUnder(rowIndex, cell)
                        ) {
                            System.out.println("Error! You placed it too close to another one. Try again:");
                            continue inputLoop;
                        }

                        if (row.get(cell).equals(ship)) {
                            System.out.println("Error! You placed it too close to another one. Try again:");
                            continue inputLoop;
                        }
                        cell += incrementer;
                    }

                    cell = firstCoordinateNumber;

                    for (int i = 0; i < length; i++) {
                        row.set(cell, battleshipParts.get(i));
                        cell += incrementer;
                    }

                } else {
                    int asciiCodeFirstLetter = (int) firstCoordinateLetter;
                    int asciiCodeSecondLetter = (int) secondCoordinateLetter;

                    int length = Math.abs(asciiCodeFirstLetter - asciiCodeSecondLetter) + 1;

                    if (length != currentBattleship.getLength()) {
                        System.out.println("Error! Wrong length of the Submarine! Try again:");
                        continue;
                    }

                    int incrementor = asciiCodeFirstLetter > asciiCodeSecondLetter ? -1 : 1;
                    int currentValue = asciiCodeFirstLetter;

                    for (int i = 0; i < length; i++) {
                        int rowIndex = Utils.letterToIndex.get((char) currentValue);
                        ArrayList<Cell> row = shipsBoard.get(rowIndex);

                        if (
                                hasShipCellAbove(rowIndex, firstCoordinateNumber) ||
                                        hasShipNextCell(rowIndex, firstCoordinateNumber) ||
                                        hasShipPreviousCell(rowIndex, firstCoordinateNumber) ||
                                        hasShipCellUnder(rowIndex, firstCoordinateNumber)
                        ) {
                            System.out.println("Error! You placed it too close to another one. Try again:");
                            continue inputLoop;
                        }

                        if (row.get(firstCoordinateNumber).equals(ship)) {
                            System.out.println("Error! You placed it too close to another one. Try again:");
                            continue inputLoop;
                        }
                        currentValue += incrementor;
                    }

                    currentValue = asciiCodeFirstLetter;

                    for (int i = 0; i < length; i++) {
                        int rowIndex = Utils.letterToIndex.get((char) currentValue);
                        ArrayList<Cell> row = shipsBoard.get(rowIndex);
                        row.set(firstCoordinateNumber, battleshipParts.get(i));
                        currentValue += incrementor;
                    }
                }
                Utils.displayBoard(shipsBoard);
                break;
            } while(true);
        }
        System.out.println("Press Enter and pass the move to another player");
        String enter = scanner.nextLine();
    }

    private boolean hasShipCellAbove(int rowIndex, int cell) {
        return hasShip(rowIndex - 1, cell);
    }

    private boolean hasShipCellUnder(int rowIndex, int cell) {
        return hasShip(rowIndex + 1, cell);
    }

    private boolean hasShipPreviousCell(int rowIndex, int cell) {
        return hasShip(rowIndex, cell - 1);
    }

    private boolean hasShipNextCell(int rowIndex, int cell) {
        return hasShip(rowIndex, cell + 1);
    }

    private boolean hasShip(int rowIndex, int cell) {
        try {
            // Access an element at an index that may cause IndexOutOfBoundsException
            String value = shipsBoard.get(rowIndex).get(cell + 1).getValue();
            return value.equals(ship);
        } catch (IndexOutOfBoundsException e) {
            return false;
        }
    }

    public Player getOpponent() {
        return opponent;
    }

    public void printBoards() {
        Utils.displayBoard(opponent.getGameBoard());
        System.out.println("---------------------");
        Utils.displayBoard(shipsBoard);
    }
}
