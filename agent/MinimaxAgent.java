package agent;

import gui.Board;
import java.util.*;

public class MinimaxAgent extends PlayerAgent {

    private int[] depthOfSearch;

    private int currentPlayer;

    private int counter;

    private final int[] SEARCH_DEPTH_3 = {4,5,6,8,10,17,7};
    private final int[] SEARCH_DEPTH_1 = {4,5,6,8,10,17,7};
    public static final int[][] weights =
            { {0, 1, 2, 3, 2, 1, 0}, {1, 2, 3, 4, 3, 2, 1}, {2, 3, 4, 5, 4, 3, 2},
                    {3, 4, 5, 6, 5, 4, 3}, {2, 3, 4, 5, 4, 3, 2}, {1, 2, 3, 4, 3, 2, 1}};

    public MinimaxAgent() {
        depthOfSearch = SEARCH_DEPTH_1;
    }

    public int getOpponent(int player) {
        if (player == 1) { return 2; } return 1;
    }

    // returns 0..6 to indicate which column to play next
    // returns -1 to tell GUI to get human input from drop buttons instead
    public int getNextMove(Board boardState, int playerNum) { // playerNum = {1,2}
        currentPlayer = playerNum;

        List<Integer> possibleMoves = boardState.getPossibleMoves();

        List<Double> values = new ArrayList();

        double a = Double.NEGATIVE_INFINITY;
        double b = Double.POSITIVE_INFINITY;

        int bestMove = -1;
        double bestValue = a;

        int depth = 6;
        counter = 0;

        if (possibleMoves.size() == 7) {
            depth = depthOfSearch[0];
        }
        else if (possibleMoves.size() == 6) {
            depth = depthOfSearch[1];
        }
        else if (possibleMoves.size() == 5) {
            depth = depthOfSearch[2];
        }
        else if (possibleMoves.size() == 4) {
            depth = depthOfSearch[3];
        }
        else if (possibleMoves.size() == 3) {
            depth = depthOfSearch[4];
        }
        else if (possibleMoves.size() == 2) {
            depth = depthOfSearch[5];
        }
        else if (possibleMoves.size() == 1) {
            depth = depthOfSearch[6];
        }


        for (int move: possibleMoves) {
            boardState.makeMove(playerNum, move);
            double value = minimax(boardState, getOpponent(playerNum), a, b, depth);
            boardState.undoMove(playerNum, move);

            value += (double)(weights[boardState.whichRow(move)][move]/100);
            values.add(value);

            if (currentPlayer == playerNum && value > bestValue) {
                bestValue = value;
                bestMove = move;
            }
            else if (currentPlayer != playerNum && value < bestValue){
                bestValue = value;
                bestMove = move;
            }
        }

        return bestMove;
    }


    public double minimax(Board boardState, int playerNum, double a, double b, int depth){
        if (boardState.isGameOver() != 0 || depth == 0) {
            counter++;
            return evaluateGame(boardState, playerNum, depth);
        }

        else if (playerNum == currentPlayer) {
            double score = a;
            for (int m : boardState.getPossibleMoves()) {
                boardState.makeMove(playerNum, m);
                score = minimax(boardState, getOpponent(playerNum), a, b, depth-1);
                boardState.undoMove(playerNum, m);
                if (score > a) {
                    a = score;
                }
                if (a >= b) {
                    return a;
                }
            }
            return a;
        }

        else {
            double score = b;
            for (int m : boardState.getPossibleMoves()) {
                boardState.makeMove(playerNum, m);
                score = minimax(boardState, getOpponent(playerNum), a, b, depth-1);
                boardState.undoMove(playerNum, m);
                if (score < b) {
                    b = score;
                }
                if (a >= b) {
                    return b;
                }
            }
            return b;
        }
    }

    public double evaluateGame(Board boardState, int playerNum, int depth){
        List<String> mega = new ArrayList();
        mega.addAll(boardState.getRows());
        mega.addAll(boardState.getColumns());
        mega.addAll(boardState.getDiagonals());

        double result = 0;

        if (currentPlayer == 1) {
            megasearch:
            for (String s : mega) {
                if (s.contains("1111")) {
                    result = 1000;
                    break megasearch;
                }
                else if(s.contains("2222")) {
                    result = -1000;
                    break megasearch;
                }
                if (s.contains("01110")) {
                    result += 150;
                }
                else if(s.contains("0111") || s.contains("1110")) {
                    result += 100;
                }
                else if(s.contains("1011") || s.contains ("1101")) {
                    result += 90;
                }
                else if (s.contains("1100") || s.contains("0011")) {
                    result += 10;
                }
                else if (s.contains("0110")) {
                    result += 10;
                }
                if (s.contains("2111") || s.contains("1112")) {
                    result -= 50;
                }
                else if (s.contains("1211") || s.contains("1121")) {
                    result -= 45;
                }
                if(s.contains("02220")) {
                    result -= 152;
                }
                else if(s.contains("2220") || s.contains("2220")) {
                    result -= 102;
                }
                else if(s.contains("2022") || s.contains ("2202")) {
                    result -= 92;
                }
                else if (s.contains("2200") || s.contains("0022")) {
                    result -= 10;
                }
                else if (s.contains("0220")) {
                    result -= 10;
                }
            }
        } else {
            megasearch:
            for (String s : mega) {
                if (s.contains("2222")) {
                    result = 1000;
                    break megasearch;
                }
                else if(s.contains("1111")) {
                    result = -1000;
                    break megasearch;
                }
                if (s.contains("02220")) {
                    result += 150;
                }
                else if(s.contains("0222") || s.contains("2220")) {
                    result += 100;
                }
                else if(s.contains("2022") || s.contains ("2202")) {
                    result += 90;
                }
                else if (s.contains("220") || s.contains("022")) {
                    result += 10;
                }
                if (s.contains("1222") || s.contains("2111")) {
                    result -= 50;
                }
                else if (s.contains("2122") || s.contains("2212")) {
                    result -= 45;
                }
                if (s.contains("01110")) {
                    result -= 152;
                }
                else if(s.contains("1110") || s.contains("1110")) {
                    result -= 102;
                }
                else if(s.contains("1011") || s.contains ("1101")) {
                    result -= 92;
                }
                else if (s.contains("110") || s.contains("011")) {
                    result -= 10;
                }
            }

        }
        if (result > 0) { result += (double)depth/10; }
        else { result -= (double) depth/10; }
        return result;

    }

    public String displayName() {
        return "Minimax";
    }
}