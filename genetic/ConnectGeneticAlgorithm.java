package genetic;

import java.util.ArrayList;
import neuralnet.*;
import agent.*;

public class ConnectGeneticAlgorithm extends GeneticAlgorithm {

    private boolean roundRobin;

    public ConnectGeneticAlgorithm (int popSize, int keepNBest,
                         double mutationRate, double crossoverRate,
                         double maxMutation, NeuralNetwork net) {
        super(popSize, keepNBest, mutationRate, crossoverRate, maxMutation);
        ArrayList<Genome> pop = getGenomePopulation();
        int inputs = net.getInputNum();
        int outputs = net.getInputNum();
        int hiddenLayers = net.getHiddenNum();
        ArrayList<Integer> sizes = net.getNeuronsInLayers();
        while (pop.size() < popSize){
            NeuralNetwork temp = new NeuralNetwork(inputs,outputs,hiddenLayers,sizes);
            pop.add(temp.toGenome());
        }
    }

    /** Determines the fitness of the population. */
    public void calculateFitness() {
        ArrayList<Genome> population = getGenomePopulation();
        for (Genome g : population) {
            if (g.getCalc()){
                // skip
            } else {
                double temp =calculateFitness(g);
                double temp2 = g.getFitness();
                g.setFitness(temp);
            }
        }
        System.out.println("");
    }


    public double calculateFitness(Genome gen) {
        Game game1 = new Game(new NeuralNetAgent(gen), new MinimaxAgent());
        Game game2 = new Game(new MinimaxAgent(), new NeuralNetAgent(gen));
        double score1 = game1.getTotalTurns();
        double score2 = game2.getTotalTurns();
        if (game1.getWinner() == 1)
            score1 = 200;
        else if (game1.getWinner() == 3)
            score1 = 46;
        if (game2.getWinner() == 2)
            score2 = 200;
        else if (game2.getWinner() == 3)
            score2 = 46;
        return (score1 + score2) / 2;
    }
}