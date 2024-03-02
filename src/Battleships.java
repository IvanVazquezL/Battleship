
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;

public class Battleships {
    private ArrayList<ArrayList<String>> board = new ArrayList<>();
    private Scanner scanner = new Scanner(System.in);
    private ArrayList<Battleship> battleships;
    private Battleship currentBattleship;
    private HashMap<Character, Integer> letterToIndex = new HashMap<>();
    private String ship = "O";
    private String hit = "X";
    private String miss = "M";
    private String mist = "~";

    Battleships() {
        initializeBoard();
        displayBoard();
        populateBattleships();
        populateLetterToIndex();
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

    public void startGame() {
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

                if (firstCoordinateLetter == secondCoordinateLetter) {
                    int length = Math.abs(firstCoordinateNumber - secondCoordinateNumber) + 1;

                    if (length != currentBattleship.getLength()) {
                        System.out.println("Error! Wrong length of the Submarine! Try again:");
                        continue;
                    }

                    int incrementer = firstCoordinateNumber > secondCoordinateNumber ? -1 : 1;
                    int rowIndex = letterToIndex.get(firstCoordinateLetter);
                    ArrayList<String> row = board.get(rowIndex);
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
                        row.set(cell, ship);
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
                        ArrayList<String> row = board.get(rowIndex);

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
                        ArrayList<String> row = board.get(rowIndex);
                        row.set(firstCoordinateNumber, ship);
                        currentValue += incrementor;
                    }
                }

                displayBoard();
                break;
            } while(true);
        }

        shoot();
    }

    private void shoot() {
        System.out.println("The game starts!");
        displayBoard();

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
            String value = board.get(rowIndex).get(coordinateNumber);

            if (value.equals(ship)) {
                board.get(rowIndex).set(coordinateNumber, hit);
                displayBoard();
                System.out.println("You hit a ship!");
            } else if (value.equals(mist)) {
                board.get(rowIndex).set(coordinateNumber, miss);
                displayBoard();
                System.out.println("You missed!");
            }

            break;
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
            String value = board.get(rowIndex).get(cell + 1);
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
        board.add(new ArrayList<>(Arrays.asList(" ","1","2","3","4","5","6","7","8","9","10")));
        board.add(new ArrayList<>(Arrays.asList("A","~","~","~","~","~","~","~","~","~","~")));
        board.add(new ArrayList<>(Arrays.asList("B","~","~","~","~","~","~","~","~","~","~")));
        board.add(new ArrayList<>(Arrays.asList("C","~","~","~","~","~","~","~","~","~","~")));
        board.add(new ArrayList<>(Arrays.asList("D","~","~","~","~","~","~","~","~","~","~")));
        board.add(new ArrayList<>(Arrays.asList("E","~","~","~","~","~","~","~","~","~","~")));
        board.add(new ArrayList<>(Arrays.asList("F","~","~","~","~","~","~","~","~","~","~")));
        board.add(new ArrayList<>(Arrays.asList("G","~","~","~","~","~","~","~","~","~","~")));
        board.add(new ArrayList<>(Arrays.asList("H","~","~","~","~","~","~","~","~","~","~")));
        board.add(new ArrayList<>(Arrays.asList("I","~","~","~","~","~","~","~","~","~","~")));
        board.add(new ArrayList<>(Arrays.asList("J","~","~","~","~","~","~","~","~","~","~")));
    }

    private void displayBoard() {
        for(ArrayList<String> row : board) {
            System.out.println(String.join(" ", row));
        }
    }

}
