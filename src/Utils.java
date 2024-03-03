
import java.util.ArrayList;
import java.util.HashMap;

public class Utils {
    public static HashMap<Character, Integer> letterToIndex = new HashMap<>();

    static {
        populateLetterToIndex();
    }

    private static void populateLetterToIndex() {
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

    public static void displayBoard(ArrayList<ArrayList<Cell>> board) {
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

    public static boolean areValidCoordinates(char firstCoordinateLetter, int firstCoordinateNumber, char secondCoordinateLetter, int secondCoordinateNumber) {

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

    public static boolean isAValidCoordinate(char coordinateLetter, int coordinateNumber) {
        int asciiCodeLetter = (int) coordinateLetter;

        return (asciiCodeLetter >= 65 && asciiCodeLetter <= 74) &&
                (coordinateNumber >= 1 && coordinateNumber <= 10);
    }
}
