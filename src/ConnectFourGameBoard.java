import java.util.Scanner;

public class ConnectFourGameBoard {

    // 7 columns x 6 rows; Java uses row-major order
    public static final char[][] initialBoard = {
            {'-', '-', '-', '-', '-', '-', '-'},
            {'-', '-', '-', '-', '-', '-', '-'},
            {'-', '-', '-', '-', '-', '-', '-'},
            {'-', '-', '-', '-', '-', '-', '-'},
            {'-', '-', '-', '-', '-', '-', '-'},
            {'-', '-', '-', '-', '-', '-', '-'}
    };

    private char[][] gameBoard = initialBoard;

    private final Scanner scanner = new Scanner(System.in);

    private boolean isPlayer1Turn = true;

    public void startGame() {
        System.out.println("Welcome to Java Connect Four");
        initializeBoard();
        playerSelectSide();

        boolean isGameOver;
        do {
            printGameBoard();
            makeMove();
            isGameOver = isGameResultedInDraw() || isWinnerFound();
            if (!isGameOver) togglePlayerTurn();
        } while (!isGameOver);

        printGameBoard();
        printFinishedMessage();
    }

    private void initializeBoard() {
        gameBoard = initialBoard;
    }

    private void playerSelectSide() {
        char selectedSide;
        boolean isSizeSelected = false;
        do {
            try {
                System.out.print("Please select a side to start the game: [1] x, [2] o: ");
                selectedSide = Character.toLowerCase(scanner.next(".").charAt(0));
                switch (selectedSide) {
                    case '1':
                    case 'x':
                        isPlayer1Turn = true;
                        break;
                    case '2':
                    case 'o':
                        isPlayer1Turn = false;
                        break;
                    default:
                        throw new IllegalArgumentException("Your input was invalid. Please select a side again.");
                }
                isSizeSelected = true;
            } catch (Exception e) {
                System.out.print(e.getMessage());
            }
        } while (!isSizeSelected);
    }

    private void printGameBoard() {
        System.out.println();
        System.out.printf("%1$17s\n", "===============");

        for (int rowIndex = gameBoard.length-1; rowIndex >= 0; rowIndex--) {
            System.out.printf("%1$1s |", rowIndex + 1);
            char[] gameBoardRow = gameBoard[rowIndex];
            for (int columnIndex = 0; columnIndex < gameBoardRow.length; columnIndex++) {
                System.out.print(gameBoardRow[columnIndex]);
                if (columnIndex != gameBoardRow.length - 1) {
                    System.out.print("|");
                }
            }
            System.out.println("|");
        }

        StringBuilder column = new StringBuilder();
        for (int columnIndex = 0; columnIndex < gameBoard[0].length; columnIndex++) {
            column.append(columnIndex + 1);
            if (columnIndex != gameBoard[0].length - 1)
                column.append(" ");
        }

        System.out.printf("%1$17s\n", "===============");
        System.out.printf("%1$16s\n", column);
        System.out.println();
    }

    private void makeMove() {
        boolean isInputValid = false;
        do {
            try {
                System.out.print((isPlayer1Turn ? "Player 1" : "Player 2")
                        + "'s turn. Please make a move by entering the column number: ");
                char input = scanner.next(".").charAt(0);
                int columnIndex = (int) input - 48;
                for (int rowIndex = 0; rowIndex < gameBoard.length; rowIndex++) {
                    if (gameBoard[rowIndex][columnIndex - 1] == '-') {
                        gameBoard[rowIndex][columnIndex - 1] = isPlayer1Turn ? 'x' : 'o';
                    }
                }
                isInputValid = true;
            } catch (IndexOutOfBoundsException e) {
                System.out.println("Your last input is out of the game board boundary!"
                        + " Please make your move again.");
            }
        } while (!isInputValid);
    }

    private boolean isGameResultedInDraw() {
        for (char[] row : gameBoard) {
            for (char column : row)
                if (column != '-') return false;
        }
        return true;
    }

    private boolean isWinnerFound() {
        for (int rowIndex = 0; rowIndex < gameBoard.length; rowIndex++) {
            for (int columnIndex = 0; columnIndex < gameBoard[rowIndex].length - 3; columnIndex++)
                if (hasHorizontalMatch(rowIndex, columnIndex)) return true;
        }

        for (int rowIndex = 0; rowIndex < gameBoard.length - 3; rowIndex++) {
            for (int columnIndex = 0; columnIndex < gameBoard[rowIndex].length; columnIndex++)
                if (hasVerticalMatch(rowIndex, columnIndex)) return true;
        }

        for (int rowIndex = 0; rowIndex < gameBoard.length - 3; rowIndex++) {
            for (int columnIndex = 0; columnIndex < gameBoard[rowIndex].length - 3; columnIndex++)
                if (hasDiagonalMatch(rowIndex, columnIndex)) return true;
        }

        for (int rowIndex = gameBoard.length - 1; rowIndex >= 3; rowIndex--) {
            for (int columnIndex = 0; columnIndex < gameBoard[rowIndex].length - 3; columnIndex++)
                if (hasReversedDiagonalMatch(rowIndex, columnIndex)) return true;
        }

        return false;
    }


    private boolean hasHorizontalMatch(int rowIndex, int columnIndex) {
        try {
            char r1c1 = gameBoard[rowIndex][columnIndex];
            char r1c2 = gameBoard[rowIndex][columnIndex + 1];
            char r1c3 = gameBoard[rowIndex][columnIndex + 2];
            char r1c4 = gameBoard[rowIndex][columnIndex + 3];
            return r1c1 != '-' && r1c1 == r1c2 && r1c2 == r1c3 && r1c3 == r1c4;
        } catch (ArrayIndexOutOfBoundsException e) {
            return false;
        }
    }

    private boolean hasVerticalMatch(int rowIndex, int columnIndex) {
        try {
            char r1c1 = gameBoard[rowIndex][columnIndex];
            char r2c1 = gameBoard[rowIndex + 1][columnIndex];
            char r3c1 = gameBoard[rowIndex + 2][columnIndex];
            char r4c1 = gameBoard[rowIndex + 3][columnIndex];
            return r1c1 != '-' && r1c1 == r2c1 && r2c1 == r3c1 && r3c1 == r4c1;
        } catch (ArrayIndexOutOfBoundsException e) {
            return false;
        }
    }

    private boolean hasDiagonalMatch(int rowIndex, int columnIndex) {
        try {
            char r1c1 = gameBoard[rowIndex][columnIndex];
            char r2c2 = gameBoard[rowIndex + 1][columnIndex + 1];
            char r3c3 = gameBoard[rowIndex + 2][columnIndex + 2];
            char r4c4 = gameBoard[rowIndex + 3][columnIndex + 3];
            return r1c1 != '-' && r1c1 == r2c2 && r2c2 == r3c3 && r3c3 == r4c4;
        } catch (ArrayIndexOutOfBoundsException e) {
            return false;
        }
    }

    private boolean hasReversedDiagonalMatch(int rowIndex, int columnIndex) {
        try {
            char r6c1 = gameBoard[rowIndex][columnIndex];
            char r5c2 = gameBoard[rowIndex - 1][columnIndex + 1];
            char r4c3 = gameBoard[rowIndex - 2][columnIndex + 2];
            char r3c4 = gameBoard[rowIndex - 3][columnIndex + 3];
            return r6c1 != '-' && r6c1 == r5c2 && r5c2 == r4c3 && r4c3 == r3c4;
        } catch (ArrayIndexOutOfBoundsException e) {
            return false;
        }
    }

    private void togglePlayerTurn() {
        isPlayer1Turn = !isPlayer1Turn;
    }

    private void printFinishedMessage() {
        if (isWinnerFound()) {
            System.out.println("The game has settled and we have a winner here! Our winner is "
                + (isPlayer1Turn ? "Player 1" : "Player 2" + "!"));
        } else {
            System.out.println("Well played to each player as the game has resulted in a draw!");
        }
    }

}
