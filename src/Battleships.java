
import java.util.ArrayList;
import java.util.Scanner;

public class Battleships {
    private String ship = BoardCharacters.SHIP.getValue();
    private String hit = BoardCharacters.HIT.getValue();
    private String miss = BoardCharacters.MISS.getValue();
    private String mist = BoardCharacters.MIST.getValue();
    private Cell hitCell = new Cell(hit);
    private Cell missCell = new Cell(miss);
    private Player playerOne = new Player("Player 1");
    private Player playerTwo = new Player("Player 2");
    private Scanner scanner = new Scanner(System.in);
    private ArrayList<Player> players = new ArrayList<>();

    Battleships() {
        playerOne.initializeBoard();
        playerOne.populateBattleships();
        playerOne.placeShipsOnBoard();

        playerTwo.initializeBoard();
        playerTwo.populateBattleships();
        playerTwo.placeShipsOnBoard();

        players.add(playerOne);
        players.add(playerTwo);

        playerOne.setOpponent(playerTwo);
        playerTwo.setOpponent(playerOne);
    }

    public void startGame() {
        do {
            for (Player player : players) {
                ArrayList<ArrayList<Cell>> opponentGameBoard = player.getOpponent().getGameBoard();
                ArrayList<ArrayList<Cell>> opponentShipsBoard = player.getOpponent().getShipsBoard();
                ArrayList<Battleship> opponentBattleships = player.getOpponent().getBattleships();

                player.printBoards();

                System.out.printf("%s, it's your turn:\n", player.getName());

                String coordinate = scanner.nextLine();

                int coordinateLength = coordinate.length();
                char coordinateLetter = coordinate.charAt(0);
                String coordinateNumberString = coordinate.substring(1, coordinateLength);
                int coordinateNumber = Integer.parseInt(coordinateNumberString);

                if (!Utils.isAValidCoordinate(coordinateLetter, coordinateNumber)) {
                    System.out.println("Error! You entered the wrong coordinates! Try again:");
                    continue;
                }

                int rowIndex = Utils.letterToIndex.get(coordinateLetter);
                String value = opponentShipsBoard.get(rowIndex).get(coordinateNumber).getValue();

                if (value.equals(ship)) {
                    BattleshipPart battleshipPart = (BattleshipPart) opponentShipsBoard.get(rowIndex).get(coordinateNumber);

                    opponentShipsBoard.get(rowIndex).set(coordinateNumber, hitCell);
                    opponentGameBoard.get(rowIndex).set(coordinateNumber, hitCell);
                    Utils.displayBoard(opponentGameBoard);

                    battleshipPart.hit();

                    boolean allShipsAreDown = true;

                    for (Battleship battleship : opponentBattleships) {
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
                            "You hit a ship!" :
                            "You sank a ship!";

                    System.out.println(hitMessage);
                } else if (value.equals(mist)) {
                    opponentShipsBoard.get(rowIndex).set(coordinateNumber, missCell);
                    opponentGameBoard.get(rowIndex).set(coordinateNumber, missCell);
                    Utils.displayBoard(opponentGameBoard);
                    System.out.println("You missed!");
                } else if (value.equals(hit)) {
                    Utils.displayBoard(opponentGameBoard);
                    System.out.println("You hit a ship!");
                } else {
                    Utils.displayBoard(opponentGameBoard);
                    System.out.println("You missed!");
                }

                System.out.println("Press Enter and pass the move to another player");
                String enter = scanner.nextLine();
            }
        } while(true);

    }
}
