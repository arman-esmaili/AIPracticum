package genetic;

import java.util.ArrayList;
import java.util.Collections;

public abstract class GeneticAlgorithm {

    private ArrayList<Genome> genomes;
    private int numGenomes;
    private double bestFitness;
    private double worstFitness;
    private double totalFitness;
    private Genome mostFitGenome;
    public int generation = 0;
    double mutationRate = 0.3;
    double crossoverRate = 0.7;
    double maxMutation = 0.1;
    int keepNBest;

    public GeneticAlgorithm(int popSize, int keepNBest, double mutationRate,
                  double crossoverRate,double maxMutation) {
        this.numGenomes = popSize;
        this.mutationRate = mutationRate;
        this.crossoverRate = crossoverRate;
        this.maxMutation = maxMutation;
        this.genomes = new ArrayList<Genome>();
        this.keepNBest = keepNBest;
    }

    public Genome[] crossover(Genome dad, Genome mom) {
        Genome[] result = new Genome[2];
        result[0] = dad.makeCopy();
        result[1] = mom.makeCopy();

        ArrayList<ArrayList<ArrayList<Double>>> daddy =
                result[0].getWeights();

        ArrayList<ArrayList<ArrayList<Double>>> mommy =
                result[1].getWeights();

        for (int layer = 0; layer < daddy.size(); layer++) {
            for (int neuron = 0; neuron < daddy.get(layer).size(); neuron++) {
                if (Math.random() < crossoverRate) {
                    crossover(daddy.get(layer).get(neuron),
                            mommy.get(layer).get(neuron));
                }
            }
        }
        return result;
    }

    public void crossover(ArrayList<Double> parent1, ArrayList<Double> parent2) {
        int cp = (int) (Math.random()*(parent1.size() - 1) + 1);
        ArrayList<Double> parent1cross = new ArrayList<Double>(parent1.subList(cp,parent1.size()));
        parent1 = new ArrayList<Double>(parent1.subList(0,cp));
        ArrayList<Double> parent2cross = new ArrayList<Double>(parent2.subList(cp,parent1.size()));
        parent2 = new ArrayList<Double>(parent2.subList(0,cp));
        parent1.addAll(parent2cross);
        parent2.addAll(parent1cross);
    }

    public double mutateWeight(double weight) {
        return weight + (2 * Math.random() * maxMutation - maxMutation);
    }

    public Genome mutate(Genome gen) {
        Genome result = gen.makeCopy();
        for (ArrayList<ArrayList<Double>> lst1 : result.getWeights()) {
            for (ArrayList<Double> weights : lst1) {
                for (int i = 0; i < weights.size(); i++) {
                    //mutate
                    if (Math.random() < mutationRate) {
                        weights.set(i,mutateWeight(weights.get(i)));
                    }
                }
            }
        }
        return result;
    }

    public ArrayList<Genome> getNBest(int n) {
        return new ArrayList<Genome>(genomes.subList(0,n));
    }

    public void createNextGeneration() {
        ArrayList<Genome> newPop = getNBest(keepNBest);
        while (newPop.size() < numGenomes) {
            Genome mom = getSample();
            Genome dad = getSample();
            Genome[] children = crossover(mom,dad);
            Genome b1 = mutate(children[0]);
            Genome b2 = mutate(children[1]);
            newPop.add(b1);
            if (numGenomes != newPop.size()) {
                newPop.add(b2);
            }
        }
        genomes = newPop;
        calculateFitness();
        updateFitness();
    }

    // returns a genome based on a sampling algorithm
    public Genome getSample() {
        return rouletteWheelSample();
    }

    // roulette wheel getSample
    public Genome rouletteWheelSample() {
        double roulette = (Math.random() * totalFitness);
        double total = 0;
        for (Genome g : genomes) {
            total += g.getFitness();

            if (total > roulette)
                return g;
        }

        // should never be called
        System.err.println ("roulette wheel sampling returned nothing");
        return null;
    }

    public abstract void calculateFitness();

    public double bestFitness() {
        return bestFitness;
    }

    public Genome bestGenome() {
        return mostFitGenome;
    }

    public void updateFitness() {
        totalFitness = 0;
        bestFitness = Double.NEGATIVE_INFINITY;
        worstFitness = Double.POSITIVE_INFINITY;
        mostFitGenome = null;

        //now, find the best
        for (Genome genome : genomes) {
            double currFitness = genome.getFitness();
            totalFitness += currFitness;
            if (currFitness > bestFitness) {
                bestFitness = currFitness;
                mostFitGenome = genome;
            }
            else if (currFitness < worstFitness) {
                worstFitness = currFitness;
            }
        }
        Collections.reverse(genomes);
        Collections.sort(genomes);
    }
    public ArrayList<Genome> getGenomePopulation() {
        return genomes;
    }

}