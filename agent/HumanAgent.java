package agent;

import gui.Board;

public class HumanAgent extends PlayerAgent {
    // returns 0..6 to indicate which column to play next
    // returns -1 to tell GUI to get human input from drop buttons instead
    public int getNextMove(Board boardState, int playerNum) { // playerNum = {1,2}
        return -1;
    }

    public String displayName() {
        return "Human";
    }    
}