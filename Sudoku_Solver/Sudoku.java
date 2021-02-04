import java.io.*;
import java.util.*;

class Sudoku {

  private static Scanner scanner;
  // instance of a board, stored as 2D int array
  private int[][] board;
  // some constants
  public static final int EMPTY = 0;
  public static final int SIZE = 9;

  // constructor for the board
  public Sudoku(int[][] input) {
    board = new int[SIZE][SIZE];

    for (int i = 0; i < SIZE; i++) {
      for (int j = 0; j < SIZE; j++) {
        board[i][j] = input[i][j];
      }
    }
  }

  public static void main(String[] args) {
    // making a new board to take in user input
    int[][] input = new int[SIZE][SIZE];

    scanner = new Scanner(System.in);
    for (int i = 0; i < SIZE; i++) {
      System.out.println("Please enter Row " + (i+1) + " of your grid, with spaces between each number and 0 for empty cells:");

      // converting input line into numbers
      String[] numLine = scanner.nextLine().split(" ");
      // check that user inputed exactly 9 values
      if (numLine.length != SIZE) {
        System.out.println("Invalid input for row!");
        return;
      }
      // populating the board with user input
      for(int j = 0; j < SIZE; j++) input[i][j] = Integer.parseInt(numLine[j]);
    }

    Sudoku sudoku = new Sudoku(input);
    System.out.println("The initial board to solve:");
    sudoku.displayBoard();

    // try to solve the board
    if (sudoku.solve()) {
      System.out.println("The solved board:");
      sudoku.displayBoard();
    } else {
      System.out.println("This board cannot be solved :(");
    }
  }

/*--------------------CHECKING THE 3 SUDOKU RULES---------------------*/

  // Rule 1: Each row of the grid must contain all of the digits from 1 to 9
  public boolean validRow(int row, int num) {
    for (int i = 0; i < SIZE; i++) {
      if (board[row][i] == num) return false;
    }
    return true;
  }

  // Rule 2: Each column of the grid must contain all of the digits from 1 to 9
  public boolean validColumn(int col, int num) {
    for (int i = 0; i < SIZE; i++) {
      if (board[i][col] == num) return false;
    }
    return true;
  }

  // Rule 3: Each 3x3 sub-box of the grid must contain all of the digits from 1 to 9
  public boolean validSubBox(int row, int col, int num) {
    int r = row - row % 3;
    int c = col - col % 3;

    for (int i = r; i < r+3; i++) {
      for (int j = c; j < c+3; j++) {
        if (board[i][j] == num) return false;
      }
    }
    return true;
  }

  // A valid Sudoku board will need to satisfy all 3 rules
  public boolean validBoard(int row, int col, int num) {
    return validRow(row, num) && validColumn(col, num) && validSubBox(row, col, num);
  }

  /*--------------------SOLVING USING BACKTRACKING---------------------*/
  public boolean solve() {
    for (int row = 0; row < SIZE; row++) {
      for (int col = 0; col < SIZE; col++) {
        // search for an empty cell
        if (board[row][col] == EMPTY) {
          // trying possible numbers 1-9
          for (int n = 1; n <= 9; n++) {
            // the number satisfies the board's constraints
            if (validBoard(row, col, n)) {
              board[row][col] = n;

              // recursively bactrack
              if (solve()) return true;
              // bactrack unsuccessful, so reset cell as empty and continue
              else board[row][col] = EMPTY;
            }
          }
          // no number 1-9 satisfies that empty cell, return false
          return false;
        }
      }
    }
    // able to fill all empty cells, solved sudoku board
    return true;
  }

  /*--------------------DISPLAY THE BOARD---------------------*/
  public void displayBoard() {
    for (int i = 0; i < SIZE; i++) {
      for (int j = 0; j < SIZE; j++) System.out.print("|" + board[i][j]);
      // next row
      System.out.print("\n------------------\n");
    }
    System.out.println();
  }

}
