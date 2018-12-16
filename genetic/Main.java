package genetic;

import neuralnet.*;
import java.util.ArrayList;
import java.io.File;

public class Main {

    public static void main(String[] args) {
        runGeneticAlgorithm("GenomeInfo");
    }

    public static void runGeneticAlgorithm(String dir) {
        //create the writer (write to file) thread
        WriterRunnable writer = new WriterRunnable();
        (new Thread(writer)).start();
        //add a shutdown hook that will terminate writer
        Runtime.getRuntime().addShutdownHook(new TerminatorThread(writer));
        //create the directory
        File directory = new File(dir);
        directory.mkdir();

        //initialize the GeneticAlgorithm
        ArrayList<Integer> sizes = new ArrayList<Integer>();
        sizes.add(126);     // CHANGE THESE VALUES
        sizes.add(126);
        NeuralNetwork newNet = new NeuralNetwork(125, 7, 2, sizes);
        ConnectGeneticAlgorithm genAlg = new ConnectGeneticAlgorithm(20, 10, .2, .7, 0.2, newNet);
        genAlg.calculateFitness();
        genAlg.updateFitness();
        System.out.println("Generation 1");
        System.out.println("Best: " + genAlg.bestFitness());

        for (int i = 0; i < 99999; i++) {
            genAlg.createNextGeneration();
            System.out.println("Generation " + (i + 2));
            System.out.println("Best: " + genAlg.bestFitness());
            System.out.println("Writing best genome to file");
            String filename = dir + "/best_genome_gen" + (i + 2);
            writer.addRequest(filename, genAlg.bestGenome());
        }
    }
}