package genetic;

import java.util.LinkedList;
import neuralnet.*;

public class WriterRunnable extends IO implements Runnable {

    public LinkedList<String> stringQ = new LinkedList<String>();
    public LinkedList<Genome> genomeQ = new LinkedList<Genome>();
    public  boolean endSignal = false;

    public void addRequest(String file, Genome genome) {
        genomeQ.addLast(genome);
        stringQ.addLast(file);
    }

    public void end() {
        endSignal = true;
        System.out.println("Terminating WriterRunnable thread.");
    }

    public void run() {
        System.out.println("Starting WriterRunnable thread.");
        while (!endSignal || (stringQ.size() != 0) ) {
            if (stringQ.size() != 0) {
                String request = stringQ.removeFirst();
                Genome genome = genomeQ.removeFirst();
                NeuralNetwork nn = new NeuralNetwork(genome);
                String data = nn.toStringNoLine();
                writeToFile(request, data);
            }
            if (stringQ.size() == 0) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    //do nothing
                }
            }
        }
    }

}