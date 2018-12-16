package genetic;

import java.util.ArrayList;

public class Genome implements Comparable {

    public ArrayList<ArrayList<ArrayList<Double>>> weights;
    public double fitness;
    public boolean calc = false;
    public static int genomeCounter = 0;
    public static int genomeNumber;

    public Genome(ArrayList<ArrayList<ArrayList<Double>>> weights) {
        genomeNumber = genomeCounter;
        genomeCounter++;
        this.weights = weights;
    }

    /** Creates a copy of a genome. */
    public Genome makeCopy() {
        ArrayList<ArrayList<ArrayList<Double>>> copy = new ArrayList<ArrayList<ArrayList<Double>>>();
        ArrayList<ArrayList<ArrayList<Double>>> copyWeights = getWeights();
        int len1 = copyWeights.size();
        for (int i = 0; i < len1; i++) {
            ArrayList<ArrayList<Double>> copy1 = new ArrayList<ArrayList<Double>>();
            int len2 = copyWeights.get(i).size();
            for (int j = 0; j < len2; j++) {
                ArrayList<Double> cloneB = new ArrayList<Double>();
                int len3 = copyWeights.get(i).get(j).size();
                for (int k = 0; k < len3 ; k++){
                    cloneB.add(copyWeights.get(i).get(j).get(k));
                }
                copy1.add(cloneB);
            }
            copy.add(copy1);
        }
        return new Genome(copy);
    }

    public void setFitness(double fitness) {
        this.fitness = fitness;
        calc = true;
    }

    public double getFitness() {
        return fitness;
    }

    public boolean getCalc(){
        return calc;
    }

    public int compareTo(Object comp) {
        Genome otherGenome = (Genome) comp;
        int result = (int) (-1.*(fitness - otherGenome.fitness)/0.0001);
        return result;
    }

    public ArrayList<ArrayList<ArrayList<Double>>> getWeights() {
        return weights;
    }


    @Override
    public String toString() {
        String fitness = "" + this.fitness;
        if (calc == false)
            fitness = "uncal";
        return "[[Genome: " + genomeNumber + " ; Fitness: " + fitness + "]]";
    }
}