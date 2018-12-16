package neuralnet;
import java.util.ArrayList;
import genetic.*;

//Author: @efg36


//Creates a neural network using the NeuralNetwork interface.
public class NeuralNetwork implements NeuralNetworkInterface{
	private int inputNum; //The number of inputs into the neural network.
	private int outputNum; //The number of outputs from the neural network.
	private int hiddenNum; //The number of hidden layers in the neural network.
	private ArrayList<Integer> neuronsInLayers; //The number of neurons in each layer of the neural network.
	private ArrayList<Layer> layers; //A list of each layer in the neural network.
	
	
	//Constructor to create a neural network from standard inputs.
	public NeuralNetwork(int inputNum, int outputNum, int hiddenNum, ArrayList<Integer> neuronsInLayers) {
		this.inputNum = inputNum;
		this.outputNum = outputNum;
		this.hiddenNum = hiddenNum;
		this.neuronsInLayers = neuronsInLayers;
		
		createNetwork();
	}
	

	//Constructor to create a neural network from only a string.
	public NeuralNetwork(String input) {
		createNetworkFromString(input);
	}
	
	//Constructor to create a neural network from a Genome.
	public NeuralNetwork(Genome input) {
		createNetworkFromGenome(input);
	}
	
	//Sets up a neural network using the set attributes of the class.
	public void createNetwork() {
		layers = new ArrayList<Layer>();
		if(hiddenNum == neuronsInLayers.size()) {
			layers.add(new Layer(neuronsInLayers.get(0), inputNum));
			for(int i = 1;i<hiddenNum;i++) {
				layers.add(new Layer(neuronsInLayers.get(i), neuronsInLayers.get(i-1)));
			}
			layers.add(new Layer(outputNum, neuronsInLayers.get(hiddenNum - 1)));
		}
	}
	
	//Sets up a neural network using a Genome object.
	public void createNetworkFromGenome(Genome input) {
		layers = new ArrayList<Layer>();
		neuronsInLayers = new ArrayList<Integer>();
		
		ArrayList<ArrayList<ArrayList<Double>>> totalInfo = input.weights;
		Layer inputLayer = new Layer(totalInfo.get(0));
		inputNum = inputLayer.getInputNum();
		for(int i = 1;i<totalInfo.size()-1;i++){
			Layer newLayer = new Layer(totalInfo.get(i));
			layers.add(newLayer);
			neuronsInLayers.add(newLayer.getNeuronNum());
		}
		Layer outputLayer = new Layer(totalInfo.get(totalInfo.size()-1));
		outputNum = outputLayer.getNeuronNum();
		
		hiddenNum = layers.size() - 1;
	}
	
	public Genome toGenome(){
		ArrayList<ArrayList<ArrayList<Double>>> format = new ArrayList<ArrayList<ArrayList<Double>>>();
		for(Layer layer : layers){
			ArrayList<ArrayList<Double>> newList = new ArrayList<ArrayList<Double>>();
			for(Neuron neuron : layer.getLayer()){
				newList.add(neuron.getWeights());
			}
			format.add(newList);
		}
		return new Genome(format);
	}
	
	
	
	@Override
	public int getInputNum() {
		return inputNum;
	}

	@Override
	public int getWeightNum() {
		int count = 0;
		for(Layer each : layers) {
			for(Neuron each2 : each.getLayer()) {
				count += each2.getInputNum();
			}
		}
		return count;
	}

	@Override
	public int getOutputNum() {
		return outputNum;
	}

	@Override
	public int getHiddenNum() {
		return hiddenNum;
	}

	@Override
	public ArrayList<Integer> getNeuronsInLayers() {
		return neuronsInLayers;
	}

	@Override
	public ArrayList<Double> getWeights(int layerNum, int neuronNum) {
		if(layerNum >= layers.size()) {
			return new ArrayList<Double>();
		}else {
			Layer layerInQuestion = layers.get(layerNum);
			if(neuronNum >= layerInQuestion.getNeuronNum()) {
				return new ArrayList<Double>();
			}else {
				return layerInQuestion.getNeuron(neuronNum).getWeights();
			}
		}
	}

	@Override
	public void setWeights(ArrayList<Double> newWeights, int layerNum, int neuronNum) {
		if(layerNum >= layers.size()) return;
		Layer layerInQuestion = layers.get(layerNum);
		if(neuronNum >= layerInQuestion.getNeuronNum()) return;
		layerInQuestion.getNeuron(neuronNum).setWeights(newWeights);
	}

	@Override
	public ArrayList<Double> getOutputs(ArrayList<Double> inputs) {
		ArrayList<Double> outputs = inputs;
		if(inputs.size() != inputNum) {
			System.err.println("Wrong input size: expecting " + inputNum + ", " + "got " + inputs.size());
			return null;
		} else {
			for(Layer layer: layers) {
				ArrayList<Double> signals = new ArrayList<Double>();
				for(Neuron neuron: layer.getLayer()) {
					signals.add(calculateSignal(outputs, neuron.getWeights()));
				}
				outputs = signals;
			}
		}
		
		
		return outputs;
	}
	
	public double calculateSignal(ArrayList<Double> inputs, ArrayList<Double> weights) {
		double signal = 0;
		if(inputs.size() != weights.size() - 1) return signal;
		for(int i = 0;i < inputs.size();i++) {
			signal += weights.get(i)*inputs.get(i);
		}
		signal+= -1*weights.get(inputs.size());
		return sigmoid(signal, 1.0);
	}
	
	@Override
	public double sigmoid(double activation, double response) {
		return (1 / (1 + Math.pow(Math.E, (-activation/response))));
	}
	
	String weightSep = ",";
	String neuronSep = ";";
	String neuralLayerSep = "'";
	private String cutLastChar(String str) {
        return str.substring(0, str.length() -1);
    }
	public String toStringNoLine(){
        String returnString = "";

        for(Layer layer: layers){
            for(Neuron neuron: layer.getLayer()){
                for(double weight: neuron.getWeights()){
                    returnString += weight + weightSep;
                }
                returnString = cutLastChar(returnString);
                returnString += neuronSep;
            }
            returnString = cutLastChar(returnString);
            returnString += neuralLayerSep;
        }
        returnString = cutLastChar(returnString);

        return returnString;
    }
	public static NeuralNetwork initFromFile(String name) {
        String data = IO.readFile(name);
        return new NeuralNetwork(data);
    }
	
	//Sets up a neural network using an input String.
	private void createNetworkFromString(String str) {
        layers = new ArrayList<Layer>();
        neuronsInLayers = new ArrayList<Integer>();
        
        String[] stringLayers = str.split(neuralLayerSep);

        for (int i = 0; i < stringLayers.length; i++) {
            String stringLayer = stringLayers[i];
            Layer nLayer = createLayerFromString(stringLayer);
            layers.add(nLayer);

            if (i == stringLayers.length - 1) {
                outputNum = nLayer.getNeuronNum();
            } else {
                neuronsInLayers.add(nLayer.getNeuronNum());
            }
            if (i == 0) {
                inputNum = nLayer.getInputNum();
            } 
        }
        hiddenNum = layers.size() - 1; 
    }
	private Layer createLayerFromString(String str) {
        String[] strNeurons = str.split(neuronSep);
        String[] neuronWeight = strNeurons[0].split(weightSep);

        int weightsNum = neuronWeight.length -1;
        int neuronsNum = strNeurons.length;

        Layer nLayer = new Layer(neuronsNum, weightsNum);
        ArrayList<Neuron> neurons = nLayer.getLayer();

        for (int i = 0; i < neuronsNum; i++) {
            ArrayList<Double> newWeights = 
                getWeightsFromString(strNeurons[i]);
            neurons.get(i).setWeights(newWeights);
        }

        return nLayer;
    }
	private ArrayList<Double> getWeightsFromString(String str) {
        String[] strWeights = str.split(weightSep);
        int length = strWeights.length - 1;
        
        ArrayList<Double> weights = new ArrayList<Double>();
        for (int i = 0; i < length+1; i++) {
            weights.add(Double.parseDouble(strWeights[i]));
        }
        
        return weights;
    }

}
