package neuralnet;
import java.util.ArrayList;

//Author: @efg36


//The overview for the NeuralNetwork class. Any neural network created must subscribe to the following methods:
//getInputNum: the number of inputs for the neural network.
//getWeightNum: the number of weights for the neural network.
//getOutputNum: the number of outputs for the neural network.
//getHiddenNum: the number of hidden layers for the neural network.
//getNeuronsInLayers: the number of neurons in each layer of the neural network.
//getWeights: for a specified neuron in a specified layer, get the current weight settings on that neuron's inputs.
//setWeights: for a specified neuron in a specified layer, change the weights on that neuron's inputs.
//getOutputs: given a set of inputs, calculate the outputs.
//sigmoid: given an activation and a response, give the typical sigmoid curve for the network
public interface NeuralNetworkInterface {
	public int getInputNum();
	public int getWeightNum();
	public int getOutputNum();
	public int getHiddenNum();
	public ArrayList<Integer> getNeuronsInLayers();
	public ArrayList<Double> getWeights(int layerNum, int neuronNum);
	public void setWeights(ArrayList<Double> newWeights, int layerNum, int neuronNum);
	public ArrayList<Double> getOutputs(ArrayList<Double> inputs);
	public double sigmoid(double activation, double response);
}
