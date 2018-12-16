package neuralnet;
import java.util.ArrayList;

//Author: @efg36


//A single instance of a neuron: the atom of a neural network. A layer of the neural network is made
//of a set of neurons. A neuron takes in a fixed number of inputs (usually a connection to a previous layer of the neural net)
//and assigns each input a weight of how influential the input should be.
public class Neuron {
	private int inputNum; //the total number of inputs
	private ArrayList<Double> weights; //
	
	//The constructor for a neuron with a fixed number of inputs. Begin by assigning each input a random weight between -1 and 1. 
	//While the number of weights is fixed, the weights themselves can be set via the setWeights method.
	public Neuron(int inputNum) {
		this.inputNum = inputNum;
		weights = new ArrayList<Double>();
		for(int i = 0;i<=inputNum;i++) {
			weights.add(Math.random()*2 - 1);
		}
	}
	
	//The constructor for a neuron given its weights
	public Neuron(ArrayList<Double> initWeights) {
		inputNum = initWeights.size();
		weights = initWeights;
	}
	
	//Returns number of inputs.
	public int getInputNum() {
		return inputNum;
	}
	
	//Returns list of weights
	public ArrayList<Double> getWeights(){
		return weights;
	}
	
	//Sets list of weights. Input should be an ArrayList of size inputNum with values typically between -1 and 1.
	public void setWeights(ArrayList<Double> newWeights) {
		weights = newWeights;
	}
}
