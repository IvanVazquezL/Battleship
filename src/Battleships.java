import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Battleships {
    private ArrayList<ArrayList<Cell>> shipsBoard = new ArrayList<>();
    private ArrayList<ArrayList<Cell>> gameBoard = new ArrayList<>();
    private Scanner scanner = new Scanner(System.in);
    private ArrayList<Battleship> battleships;
    private Battleship currentBattleship;
    private HashMap<Character, Integer> letterToIndex = new HashMap<>();
    private String ship = BoardCharacters.SHIP.getValue();
    private String hit = BoardCharacters.HIT.getValue();
    private String miss = BoardCharacters.MISS.getValue();
    private String mist = BoardCharacters.MIST.getValue();
    private Cell shipCell = new Cell(ship);
    private Cell hitCell = new Cell(hit);
    private Cell missCell = new Cell(miss);
    private Cell mistCell = new Cell(mist);

    Battleships() {
        initializeBoard();
        displayBoard(shipsBoard);
        populateBattleships();
        populateLetterToIndex();
        placeShipsOnBoard();
    }

    private void populateLetterToIndex() {
        letterToIndex.put('A', 1);
        letterToIndex.put('B', 2);
        letterToIndex.put('C', 3);
        letterToIndex.put('D', 4);
        letterToIndex.put('E', 5);
        letterToIndex.put('F', 6);
        letterToIndex.put('G', 7);
        letterToIndex.put('H', 8);
        letterToIndex.put('I', 9);
        letterToIndex.put('J', 10);
    }

    private void populateBattleships() {
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

    public void placeShipsOnBoard() {
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

                if (!areValidCoordinates(firstCoordinateLetter, firstCoordinateNumber, secondCoordinateLetter, secondCoordinateNumber)) {
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
                    int rowIndex = letterToIndex.get(firstCoordinateLetter);
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
                        int rowIndex = letterToIndex.get((char) currentValue);
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
                        int rowIndex = letterToIndex.get((char) currentValue);
                        ArrayList<Cell> row = shipsBoard.get(rowIndex);
                        row.set(firstCoordinateNumber, battleshipParts.get(i));
                        currentValue += incrementor;
                    }
                }

                displayBoard(shipsBoard);
                break;
            } while(true);
        }
    }

    public void startGame() {
        System.out.println("The game starts!");
        displayBoard(gameBoard);

        System.out.println("Take a shot!");

        do {
            String coordinate = scanner.nextLine();

            int coordinateLength = coordinate.length();
            char coordinateLetter = coordinate.charAt(0);
            String coordinateNumberString = coordinate.substring(1, coordinateLength);
            int coordinateNumber = Integer.parseInt(coordinateNumberString);

            if (!isAValidCoordinate(coordinateLetter, coordinateNumber)) {
                System.out.println("Error! You entered the wrong coordinates! Try again:");
                continue;
            }

            int rowIndex = letterToIndex.get(coordinateLetter);
            String value = shipsBoard.get(rowIndex).get(coordinateNumber).getValue();

            if (value.equals(ship)) {
                BattleshipPart battleshipPart = (BattleshipPart) shipsBoard.get(rowIndex).get(coordinateNumber);

                shipsBoard.get(rowIndex).set(coordinateNumber, hitCell);
                gameBoard.get(rowIndex).set(coordinateNumber, hitCell);
                displayBoard(gameBoard);

                battleshipPart.hit();

                boolean allShipsAreDown = true;

                for (Battleship battleship : battleships) {
                    if (battleship.isAlive()) {
                        allShipsAreDown = false;
                    }
                }

                if (allShipsAreDown) {
                    System.out.println("You sank the last ship. You won. Congratulations!");
                    break;
                }

                Battleship parentShip = battleshipPart.getParentShip();
                String hitMessage = parentShip.isAlive() ?
                        "You hit a ship! Try again:" :
                        "You sank a ship! Specify a new target:";

                System.out.println(hitMessage);
            } else if (value.equals(mist)) {
                shipsBoard.get(rowIndex).set(coordinateNumber, missCell);
                gameBoard.get(rowIndex).set(coordinateNumber, missCell);
                displayBoard(gameBoard);
                System.out.println("You missed. Try again:");
            } else if (value.equals(hit)) {
                displayBoard(gameBoard);
                System.out.println("You hit a ship! Try again:");
            } else {
                displayBoard(gameBoard);
                System.out.println("You missed. Try again:");
            }
        } while(true);
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

    private boolean areValidCoordinates(char firstCoordinateLetter, int firstCoordinateNumber, char secondCoordinateLetter, int secondCoordinateNumber) {

        if (!isAValidCoordinate(firstCoordinateLetter, firstCoordinateNumber) &&
                !isAValidCoordinate(secondCoordinateLetter, secondCoordinateNumber)) {
            return false;
        }

        // If the ship is on the same row
        if (firstCoordinateLetter == secondCoordinateLetter) {
            // Ship must be between 1 and 10 to be valid
            return firstCoordinateNumber >= 1 && firstCoordinateNumber <= 10 &&
                    secondCoordinateNumber >= 1 && secondCoordinateNumber <= 10;
        }

        //The ship must be on the same column to be valid
        return firstCoordinateNumber == secondCoordinateNumber;
    }

    private boolean isAValidCoordinate(char coordinateLetter, int coordinateNumber) {
        int asciiCodeLetter = (int) coordinateLetter;

        return (asciiCodeLetter >= 65 && asciiCodeLetter <= 74) &&
                (coordinateNumber >= 1 && coordinateNumber <= 10);
    }

    private void initializeBoard() {
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

    private void displayBoard(ArrayList<ArrayList<Cell>> board) {
        for (ArrayList<Cell> row : board) {
            for (int i = 0; i < row.size(); i++) {
                System.out.print(row.get(i).getValue());
                if (i < row.size() - 1) {
                    System.out.print(" ");
                }
            }
            System.out.println();
        }
    }
}