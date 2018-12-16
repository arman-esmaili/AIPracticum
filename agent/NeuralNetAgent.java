package agent;

import gui.Board;

import java.util.*;
import genetic.*;
import neuralnet.*;

public class NeuralNetAgent extends PlayerAgent {

    private NeuralNetwork network;

    public NeuralNetAgent() {
        network = NeuralNetwork.initFromFile("agent/neural_net");
    }

    public NeuralNetAgent(String path) {
        network = NeuralNetwork.initFromFile(path);
    }
    
    public NeuralNetAgent(Genome genome) {
        network = new NeuralNetwork(genome);
    }

    // returns 0..6 to indicate which column to play next
    // returns -1 to tell GUI to get human input from drop buttons instead
    public int getNextMove(Board boardState, int playerNum) { // playerNum = {1,2}
        List<Integer> possibleMoves = boardState.getPossibleMoves();

        //Calculating, using the neural net, the outputs from the board state inputs, part not implemented yet
        //Inputs are in groups of 3; 0 - blank slot, 1 - first player filled, 2 - second player filled
        ArrayList<Double> inputs = new ArrayList<>();
        for (int c = 0; c < 7; c++) {
            for (int r = 0; r < 6; r++) {
                int x = boardState.pieceAt(r,c);
                if (x == 0) {
                    inputs.add(1.0);
                    inputs.add(0.0);
                    inputs.add(0.0);
                } else if (x == 1) {
                    inputs.add(0.0);
                    inputs.add(1.0);
                    inputs.add(0.0);
                } else {
                    inputs.add(0.0);
                    inputs.add(0.0);
                    inputs.add(1.0);
                }
            }
        }

        List<Double> outputs = network.getOutputs(inputs);

        int bestMove = -1;
        double bestOutput = Double.NEGATIVE_INFINITY;
        //Calculating which move has the best results from the neural net
        for (int i : possibleMoves) {
            if (outputs.get(i) > bestOutput) {
                bestMove = i;
                bestOutput = outputs.get(i);
            }
            //In the case of a tie, only switch to new column if it's closer to the center of the board
            else if (outputs.get(i) == bestOutput && Math.abs(i - 3) < Math.abs(bestMove - 3)) {
                bestMove = i;
                bestOutput = outputs.get(i);
            }
        }
        return bestMove;
    }

    public String displayName() {
        return "Neural Net";
    }
}