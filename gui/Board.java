package gui;

import java.util.*;

public class Board {
    public static final int ROWS = 6;
    public static final int COLUMNS = 7;
    public static final int TARGET = 4;
    
    private int[][] board;

    // returns an empty connect four board.
    public Board() {
        board = new int[ROWS][COLUMNS]; 
    }

    // returns 0 for not played, 1 for red (player 1), or 2 for yellow (player 2).
    public int pieceAt(int row, int column) {
        return board[row][column];
    }

    // returns the columns that are playable, i.e. not yet full.
    public List<Integer> getPossibleMoves() {
        List<Integer> moves = new ArrayList<Integer>();
        for(int i = 0; i < COLUMNS; i++) {
            if (!columnFull(i)) {
                moves.add(i);
            }
        }
        return moves;
    }

    public List<String> getRows() {
        List<String> rows = new ArrayList<String>();
        for(int i = 0; i < ROWS; i++) {
            String rowAsString = "";
            for (int j = 0; j < COLUMNS; j++) {
                rowAsString += board[i][j];
            }
            rows.add(rowAsString);
        }
        return rows;
    }

    public List<String> getColumns() {
        List<String> columns = new ArrayList<String>();
        for(int i = 0; i < COLUMNS; i++) {
            String columnAsString = "";
            for (int j = 0; j < ROWS; j++) {
                columnAsString += board[j][i];
            }
            columns.add(columnAsString);
        }
        return columns;
    }

    public List<String> getDiagonals() {
        List<String> diagonals = new ArrayList<String>();
        int startRow = 3, startColumn = 0;
        for (int i = 0; i < ROWS; i++) { // down-right diagonals
            String diagonalAsString = "";
            int currentRow = startRow;
            int currentColumn = startColumn;
            while (currentRow >= 0 && currentColumn < COLUMNS) {
                diagonalAsString += board[currentRow][currentColumn];
                currentRow--;
                currentColumn++;
            }
            if (startRow != ROWS-1) {
                startRow++;
            } else {
                startColumn++;
            }
            diagonals.add(diagonalAsString);
        }
        startRow = 3; startColumn = COLUMNS-1;
        for (int i = 0; i < ROWS; i++) { // down-left diagonals
            String diagonalAsString = "";
            int currentRow = startRow;
            int currentColumn = startColumn;
            while (currentRow >= 0 && currentColumn >= 0) {
                diagonalAsString += board[currentRow][currentColumn];
                currentRow--;
                currentColumn--;
            }
            if (startRow != ROWS-1) {
                startRow++;
            } else {
                startColumn--;
            }
            diagonals.add(diagonalAsString);
        }
        return diagonals;
    }

    // drops a piece for a given player into a given column.
    public void makeMove(int playerNum, int columnToDrop) {
        board[whichRow(columnToDrop)][columnToDrop] = playerNum;
    }

    // removes the topmost piece from a given column.
    public void undoMove(int playerNum, int columnToDrop) {
        board[whichRow(columnToDrop)+1][columnToDrop] = 0;
    }

    // returns the row at which a piece would land if dropped into a given column.
    public int whichRow(int columnToDrop) {
        for (int column = columnToDrop, currentRow = 0; currentRow < board.length; currentRow++) {
            if (board[currentRow][column] > 0)
                return currentRow - 1;
        }
        return ROWS - 1; // bottom-most row of board
    }

    // returns true if the given column is full, else returns false.
    public boolean columnFull(int column) {
        for (int col = column, row = 0; row < board.length; row++) {
            if (board[row][col] == 0)
                return false;
        }
        return true;
    }

    // returns true if the game board is full, else returns false.
    public boolean isFull() {
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLUMNS; j++) {
                if (board[i][j] == 0)
                    return false;
            }
        }
        return true;
    }

    // returns 0 if there is no winner yet, a playerNum {1,2} of the winner, or 3 to indicate a draw.
    public int isGameOver() {
        for (String row : getRows()) {
            if (row.contains("1111")) {
                return 1;
            } else if (row.contains("2222")) {
                return 2;
            }
        }
        for (String column : getColumns()) {
            if (column.contains("1111")) {
                return 1;
            } else if (column.contains("2222")) {
                return 2;
            }
        }
        for (String diagonal : getDiagonals()) {
            if (diagonal.contains("1111")) {
                return 1;
            } else if (diagonal.contains("2222")) {
                return 2;
            }
        }
        if (isFull()) {
            return 3;
        }
        return 0;
    }
}