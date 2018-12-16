package data;

import genetic.*;
import agent.*;
import java.util.*;
import java.io.*;
import java.nio.file.*;
import java.nio.charset.Charset;

public class Main {

    private static final String genomeFolder = "GenomeInfo/";
    private static final String parametersUsed = "popSize = 100,\n"+
    "keepNBest = 50,\n"+
    "mutationRate = 0.7,\n"+
    "crossoverRate = 0.6,\n"+
    "maxMutation = 0.2,\n"+
    "roundRobin = false";
    
    public static void main(String ... args) {
        File f = new File(genomeFolder);
        ArrayList<String> genomeNames = new ArrayList<String>(Arrays.asList(f.list()));
        Collections.sort(genomeNames);
        ArrayList<String> outputLines = new ArrayList<String>();
        String csvHeader = "generation,# of moves until defeat playing first against minimax,# of moves until defeat playing second against minimax";
        outputLines.add(csvHeader);

        long startTime = System.currentTimeMillis();
        
        for (String name : genomeNames) {
            String[] components = name.split("best_genome_gen");
            if (components.length != 2) {
                continue;
            }
            System.out.println("testing genome " + components[1] + " :)");
            NeuralNetAgent netAgent = new NeuralNetAgent(genomeFolder+name);
            MinimaxAgent minimaxAgent = new MinimaxAgent();
            String outputLine = components[1] + ",";
            Game game1 = new Game(netAgent, minimaxAgent);
            outputLine += game1.getTotalTurns() + (game1.getWinner() == 1 ? "*" : "") + ","; // * means neural net won... not expecting this
            Game game2 = new Game(minimaxAgent, netAgent);
            outputLine += game2.getTotalTurns() + (game2.getWinner() == 2 ? "*" : ""); // * means neural net won... not expecting this
            outputLines.add(outputLine);
        }
        
        outputLines.add("\nTime elapsed: " + (System.currentTimeMillis() - startTime));
        outputLines.add("Parameters used:\n" + parametersUsed);

        try {
            Path file = Paths.get("results" + System.currentTimeMillis() + ".txt");
            Files.write(file, outputLines, Charset.forName("UTF-8"));
        } catch (Exception e) {
            System.err.println("error writing data results file");
        }
    }
}