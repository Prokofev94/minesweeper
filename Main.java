package minesweeper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;
import java.util.List;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);
    private static final Random random = new Random();
    private static char[][] field;
    private static char[][] displayField;
    private static boolean win = false;
    private static boolean lose = false;
    private static int mines;
    private static boolean fieldCreated = false;
    private static List<Integer[]> cells = new ArrayList<>();

    public static void main(String[] args) {
        startGame();
    }

    public static void startGame() {
        setCountMines();
        createDisplayField();
        printDisplayField();
        while (!fieldCreated) {
            firstTurn();
        }
        while (!win && !lose) {
            nextTurn();
        }
        if (win) {
            System.out.println("Congratulations! You found all the mines!");
        } else {
            printLose();
            System.out.println("You stepped on a mine and failed!");
        }
    }

    public static void setCountMines() {
        System.out.println("How many mines do you want on the field?");
        mines = Integer.parseInt(scanner.nextLine());
    }

    public static void createDisplayField() {
        displayField = new char[9][9];
        for (char[] rows : displayField) {
            Arrays.fill(rows, '.');
        }
    }

    public static void firstTurn() {
        System.out.println("Set/unset mines marks or claim a cell as free:");
        String[] turn = scanner.nextLine().split(" ");
        int i = Integer.parseInt(turn[1]) - 1;
        int j = Integer.parseInt(turn[0]) - 1;
        if ("free".equals(turn[2])) {
            createField(Integer.parseInt(turn[0]), Integer.parseInt(turn[1]));
            explore(i, j);
        } else if ("mine".equals(turn[2])) {
            displayField[i][j] = displayField[i][j] == '.' ? '*' : '.';
        }
        printDisplayField();
    }

    public static void createField(int x, int y) {
        field = new char[9][9];
        for (char[] row : field) {
            Arrays.fill(row, '0');
        }
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                cells.add(new Integer[]{i, j});
            }
        }
        int i;
        int j;
        for (int c = 0; c < mines;) {
            int r = random.nextInt(cells.size());
            i = cells.get(r)[0];
            j = cells.get(r)[1];
            cells.remove(r);
            if (!(i == y - 1 && j == x - 1)) {

                field[i][j] = 'X';
                c++;
                if (i > 0) {
                    field[i - 1][j] = field[i - 1][j] != 'X' ? ++field[i - 1][j] : 'X';
                    if (j > 0) {
                        field[i - 1][j - 1] = field[i - 1][j - 1] != 'X' ? ++field[i - 1][j - 1] : 'X';
                    }
                    if (j < 8) {
                        field[i - 1][j + 1] = field[i - 1][j + 1] != 'X' ? ++field[i - 1][j + 1] : 'X';
                    }
                }
                if (i < 8) {
                    field[i + 1][j] = field[i + 1][j] != 'X' ? ++field[i + 1][j] : 'X';
                    if (j > 0) {
                        field[i + 1][j - 1] = field[i + 1][j - 1] != 'X' ? ++field[i + 1][j - 1] : 'X';
                    }
                    if (j < 8) {
                        field[i + 1][j + 1] = field[i + 1][j + 1] != 'X' ? ++field[i + 1][j + 1] : 'X';
                    }
                }
                if (j > 0) {
                    field[i][j - 1] = field[i][j - 1] != 'X' ? ++field[i][j - 1] : 'X';
                }
                if (j < 8) {
                    field[i][j + 1] = field[i][j + 1] != 'X' ? ++field[i][j + 1] : 'X';
                }
            }
        }
        for (int m = 0; m < 9; m++) {
            for (int n = 0; n < 9; n++) {
                field[m][n] = field[m][n] == '0' ? '/' : field[m][n];
            }
        }
        fieldCreated = true;
    }

    public static void printDisplayField() {
        System.out.println(" |123456789|");
        System.out.println("-|---------|");
        int row = 1;
        for (char[] rows : displayField) {
            System.out.printf("%d|", row++);
            for (char ch : rows) {
                System.out.print(ch);
            }
            System.out.println("|");
        }
        System.out.println("-|---------|");
    }

    public static void printLose() {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (field[i][j] == 'X') {
                    displayField[i][j] = 'X';
                }
            }
        }
        printDisplayField();
    }

    public static void nextTurn() {
        System.out.println("Set/delete mines marks (x and y coordinates):");
        String[] turn = scanner.nextLine().split(" ");
        int i = Integer.parseInt(turn[1]) - 1;
        int j = Integer.parseInt(turn[0]) - 1;
        if ("mine".equals(turn[2])) {
            displayField[i][j] = displayField[i][j] == '.' ? '*' : '.';
        } else if ("free".equals(turn[2])) {
            explore(i, j);
        }
        if (!lose) {
            checkWins();
            printDisplayField();
        }
    }

    public static void explore(int i, int j) {
        if (outBounds(i, j)) {
            return;
        }
        if (!isUnexplored(i, j)) {
            return;
        }
        displayField[i][j] = field[i][j];
        if (field[i][j] == 'X') {
            lose = true;
            return;
        }
        if (field[i][j] != '/') {
            return;
        }
        explore(i - 1, j);
        explore(i - 1, j - 1);
        explore(i - 1, j + 1);
        explore(i + 1, j);
        explore(i + 1, j - 1);
        explore(i + 1, j + 1);
        explore(i, j - 1);
        explore(i, j + 1);
    }

    public static boolean outBounds(int i, int j) {
        return i < 0 || i > 8 || j < 0 || j > 8;
    }

    public static boolean isUnexplored(int i, int j) {
        return displayField[i][j] == '.' || displayField[i][j] == '*';
    }

    public static void checkWins() {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (field[i][j] != displayField[i][j] &&
                        (field[i][j] == 'X' && displayField[i][j] != '*' ||
                        displayField[i][j] == '*' && field[i][j] != 'X')) {
                    return;
                }
            }
        }
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (field[i][j] != 'X' && displayField[i][j] != field[i][j]) {
                    return;
                }
            }
        }
        win = true;
    }
}