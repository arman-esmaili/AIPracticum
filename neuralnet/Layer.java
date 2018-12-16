package neuralnet;
import java.util.ArrayList;

//Author: @efg36


//A set of neurons forms a layer. Typically a neural network has an input layer, several hidden layers, and then an output layer.
//A layer consists of a number of neurons (int neuronNum) and the neurons themselves (ArrayList<Neuron> neurons), each with a number of inputs (inputNum).
public class Layer {
	private int neuronNum;
	private ArrayList<Neuron> neurons;
	private int inputNum;
	
	//Constructor for an new arbitrary layer of neuronNum neurons.
	public Layer(int neuronNum, int inputNum) {
		this.neuronNum = neuronNum;
		this.inputNum = inputNum;
		neurons = new ArrayList<Neuron>();
		for(int i = 0;i<neuronNum;i++) {
			neurons.add(new Neuron(inputNum));
		}
	}
	
	//Constructor for a new layer given a nested ArrayList of neurons and their weights
	public Layer(ArrayList<ArrayList<Double>> layerNeurons) {
		neuronNum = layerNeurons.size();
		inputNum = layerNeurons.get(0).size();
		neurons = new ArrayList<Neuron>();
		for(int i = 0;i < neuronNum; i++) {
			neurons.add(new Neuron(layerNeurons.get(i)));
		}
	}
	//Gets the number of neurons in the layer.
	public int getNeuronNum() {
		return neuronNum;
	}
	
	//Returns the number of inputs any neuron in the layer has.
	public int getInputNum() {
		return inputNum;
	}
	
	//Returns the entire layer of neurons.
	public ArrayList<Neuron> getLayer(){
		return neurons;
	}
	
	//Return a specific neuron in the layer:
	public Neuron getNeuron(int num) {
		return neurons.get(num);
	}
}
