package agent;

import gui.Board;

public abstract class PlayerAgent {
    // returns 0..6 to indicate which column to play next
    // returns -1 to tell GUI to get human input from drop buttons instead
    public abstract int getNextMove(Board boardState, int playerNum); // playerNum = {1,2}

    // display name of agent
    public abstract String displayName();
}