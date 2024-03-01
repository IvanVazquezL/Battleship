
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class Battleships {
    private ArrayList<ArrayList<String>> board = new ArrayList<>();
    private Scanner scanner = new Scanner(System.in);
    Battleships() {
        initializeBoard();
        displayBoard();
        startGame();
    }

    private void startGame() {
        System.out.println("Enter the coordinates of the ship:");
        String coordinatesLine = scanner.nextLine();

        ArrayList<String> coordinates =  new ArrayList<>(Arrays.asList(coordinatesLine.split(" ")));
        char firstCoordinateLetter = coordinates.get(0).charAt(0);
        int firstCoordinateNumber = Integer.parseInt(coordinates.get(0).substring(1, coordinates.get(0).length()));

        char secondCoordinateLetter = coordinates.get(1).charAt(0);
        int secondCoordinateNumber = Integer.parseInt(coordinates.get(1).substring(1, coordinates.get(1).length()));

        if (!areValidCoordinates(firstCoordinateLetter, firstCoordinateNumber, secondCoordinateLetter, secondCoordinateNumber)) {
            System.out.println("Error!");
            return;
        }

        if (firstCoordinateLetter == secondCoordinateLetter) {
            int length = Math.abs(firstCoordinateNumber - secondCoordinateNumber) + 1;
            System.out.printf("Length: %d\n", length);
            StringBuilder stringBuilder = new StringBuilder();

            int incrementor = firstCoordinateNumber > secondCoordinateNumber ? -1 : 1;
            int currentValue = firstCoordinateNumber;

            for (int i = 0; i < length; i++) {
                stringBuilder.append(firstCoordinateLetter).append(currentValue);
                if (i < length) {
                    stringBuilder.append(" ");
                }
                currentValue += incrementor;
            }

            System.out.printf("Parts: %s", stringBuilder);
        } else {
            int asciiCodeFirstLetter = (int) firstCoordinateLetter;
            int asciiCodeSecondLetter = (int) secondCoordinateLetter;

            int length = Math.abs(asciiCodeFirstLetter - asciiCodeSecondLetter) + 1;
            System.out.printf("Length: %d\n", length);
            StringBuilder stringBuilder = new StringBuilder();

            int incrementor = asciiCodeFirstLetter > asciiCodeSecondLetter ? -1 : 1;
            int currentValue = asciiCodeFirstLetter;

            for (int i = 0; i < length; i++) {
                char character = (char) currentValue;
                stringBuilder.append(character).append(firstCoordinateNumber);
                if (i < length) {
                    stringBuilder.append(" ");
                }
                currentValue += incrementor;
            }

            System.out.printf("Parts: %s", stringBuilder);


        }


    }

    private boolean areValidCoordinates(char firstCoordinateLetter, int firstCoordinateNumber, char secondCoordinateLetter, int secondCoordinateNumber) {
        int asciiCodeFirstLetter = (int) firstCoordinateLetter;
        int asciiCodeSecondLetter = (int) secondCoordinateLetter;

        if (!(asciiCodeFirstLetter >= 65 && asciiCodeFirstLetter <= 74 &&
                asciiCodeSecondLetter >= 65 && asciiCodeSecondLetter <= 74)
        ) {
            return false;
        }

        if (!(firstCoordinateNumber >= 1 && firstCoordinateNumber <= 10 &&
                secondCoordinateNumber >= 1 && secondCoordinateNumber <= 10)) {
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
