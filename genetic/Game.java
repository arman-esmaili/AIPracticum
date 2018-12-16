package genetic;

import gui.Board;
import agent.PlayerAgent;

public class Game {
    private Board board;
    private PlayerAgent[] players;

    private int whosTurn = 1; // {1,2} will take turns
    private int totalTurnsTaken = 1;

    private int gameStatus = 0; // 0 = not over yet, {1,2} playerNum of winner, 3 = draw

    public Game(PlayerAgent player1, PlayerAgent player2) {
        players = new PlayerAgent[2];
        players[0] = player1;
        players[1] = player2;

        board = new Board();

        // play game until completion
        while (gameStatus == 0) {
            int columnToPlay = players[whosTurn-1].getNextMove(board, whosTurn);
            board.makeMove(whosTurn, columnToPlay);

            totalTurnsTaken++;
            whosTurn = whosTurn == 1 ? 2 : 1;

            gameStatus = board.isGameOver();
        }
    }

    public int getTotalTurns() {
        return totalTurnsTaken;
    }

    public int getWinner() {
        return gameStatus; // expected {1,2} or 3 if game ended in draw
    }
}