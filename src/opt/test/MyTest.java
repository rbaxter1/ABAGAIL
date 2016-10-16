package opt.test;

import dist.*;
import opt.*;
import opt.example.*;
import opt.ga.*;
import shared.*;
import func.nn.backprop.*;

import java.util.*;
import java.io.*;
import java.text.*;

/**
 * Implementation of randomized hill climbing, simulated annealing, and genetic algorithm to
 * find optimal weights to a neural network that is classifying abalone as having either fewer 
 * or more than 15 rings. 
 *
 * @author Hannah Lau
 * @version 1.0
 */
public class MyTest {
    //private static Instance[] instances = initializeInstances("titanic.txt");
    private static Instance[] testInstances = initializeInstances("titanic_test.txt");
    private static Instance[] trainInstances = initializeInstances("titanic_train.txt");

    private static int inputLayer = 157, hiddenLayer = 100, outputLayer = 1, trainingIterations = /*1000*/10000;
    private static BackPropagationNetworkFactory factory = new BackPropagationNetworkFactory();
    
    private static ErrorMeasure measure = new SumOfSquaresError();

    private static DataSet set = new DataSet(trainInstances);

    private static BackPropagationNetwork networks[] = new BackPropagationNetwork[3];
    private static NeuralNetworkOptimizationProblem[] nnop = new NeuralNetworkOptimizationProblem[3];

    private static OptimizationAlgorithm[] oa = new OptimizationAlgorithm[3];
    //private static String[] oaNames = {"RHC", "SA", "GA"};
    //private static String[] oaNames = {"SA01", "SA50", "SA80"};
    private static String[] oaNames = {"GA1000_100_10", "GA100_100_10", "GA500_100_10"};
    //, "GA1000_250_10", "GA100_50_10", "GA500_10_10"};
    
    private static String results = "";

    private static DecimalFormat df = new DecimalFormat("0.000");

    public static void main(String[] args) {
        for(int i = 0; i < oa.length; i++) {
            networks[i] = factory.createClassificationNetwork(
                new int[] {inputLayer, hiddenLayer, outputLayer});
            nnop[i] = new NeuralNetworkOptimizationProblem(set, networks[i], measure);
        }
        
        //oa[0] = new RandomizedHillClimbing(nnop[0]);
        //oa[1] = new SimulatedAnnealing(1E11, .95, nnop[1]);
        //oa[1] = new SimulatedAnnealing(1E11, .99, nnop[1]);
        //oa[2] = new StandardGeneticAlgorithm(200, 100, 10, nnop[2]);
        
        //oa[0] = new SimulatedAnnealing(1E11, .01, nnop[0]);
        //oa[1] = new SimulatedAnnealing(1E11, .50, nnop[1]);
        //oa[2] = new SimulatedAnnealing(1E11, .80, nnop[2]);
        
        oa[0] = new StandardGeneticAlgorithm(1000, 100, 10, nnop[0]);
        oa[1] = new StandardGeneticAlgorithm(100, 100, 10, nnop[1]);
        oa[2] = new StandardGeneticAlgorithm(500, 100, 10, nnop[2]);
        //oa[3] = new StandardGeneticAlgorithm(1000, 250, 100, nnop[0]);
        //oa[4] = new StandardGeneticAlgorithm(100, 50, 10, nnop[1]);
        //oa[5] = new StandardGeneticAlgorithm(500, 10, 10, nnop[2]);
        

        for(int i = 0; i < oa.length; i++) {
            double start = System.nanoTime(), end, trainingTime, testingTime, correct = 0, incorrect = 0;
            train(oa[i], networks[i], oaNames[i]); //trainer.train();
            end = System.nanoTime();
            trainingTime = end - start;
            trainingTime /= Math.pow(10,9);

            Instance optimalInstance = oa[i].getOptimal();
            networks[i].setWeights(optimalInstance.getData());

            double predicted, actual;
            start = System.nanoTime();
            for(int j = 0; j < testInstances.length; j++) {
                networks[i].setInputValues(testInstances[j].getData());
                networks[i].run();

                predicted = Double.parseDouble(testInstances[j].getLabel().toString());
                actual = Double.parseDouble(networks[i].getOutputValues().toString());

                double trash = Math.abs(predicted - actual) < 0.5 ? correct++ : incorrect++;
            }
            end = System.nanoTime();
            testingTime = end - start;
            testingTime /= Math.pow(10,9);

            results +=  "\nTest Results for " + oaNames[i] + ": \nCorrectly classified " + correct + " instances." +
                        "\nIncorrectly classified " + incorrect + " instances.\nPercent correctly classified: "
                        + df.format(correct/(correct+incorrect)*100) + "%\nTraining time: " + df.format(trainingTime)
                        + " seconds\nTesting time: " + df.format(testingTime) + " seconds\n";
            
            // TRAINING ERROR
            predicted = 0;
            actual = 0;
            start = System.nanoTime();
            for(int j = 0; j < trainInstances.length; j++) {
                networks[i].setInputValues(trainInstances[j].getData());
                networks[i].run();

                predicted = Double.parseDouble(trainInstances[j].getLabel().toString());
                actual = Double.parseDouble(networks[i].getOutputValues().toString());

                double trash = Math.abs(predicted - actual) < 0.5 ? correct++ : incorrect++;
            }
            end = System.nanoTime();
            testingTime = end - start;
            testingTime /= Math.pow(10,9);

            results +=  "\nTrain Results for " + oaNames[i] + ": \nCorrectly classified " + correct + " instances." +
                        "\nIncorrectly classified " + incorrect + " instances.\nPercent correctly classified: "
                        + df.format(correct/(correct+incorrect)*100) + "%\nTraining time: " + df.format(trainingTime)
                        + " seconds\nTesting time: " + df.format(testingTime) + " seconds\n";
            
        }

        System.out.println(results);
    }

    private static void train(OptimizationAlgorithm oa, BackPropagationNetwork network, String oaName) {
        System.out.println("\nError results for " + oaName + "\n---------------------------");

        FileOutputStream fos = null;
        File file;
        try {
        	//Specify the file path here
	  	  	file = new File(oaName + ".txt");
	  	  	fos = new FileOutputStream(file);
	
            /* This logic will check whether the file
	  	     * exists or not. If the file is not found
	  	     * at the specified location it would create
	  	     * a new file*/
	  	  	if (!file.exists()) {
	  	  		file.createNewFile();
	  	  	}
	  	  	double cumTrainTime = 0;
	  			  
	        for(int i = 0; i < trainingIterations; i++) {
	        	double start = System.nanoTime(), end, trainingTime = 0;

	        	oa.train();
	        	end = System.nanoTime();
	            trainingTime = end - start;
	            trainingTime /= Math.pow(10,9);
	            cumTrainTime += trainingTime;
	
	            double error = 0;
	            for(int j = 0; j < trainInstances.length; j++) {
	                network.setInputValues(trainInstances[j].getData());
	                network.run();
	
	                Instance output = trainInstances[j].getLabel(), example = new Instance(network.getOutputValues());
	                example.setLabel(new Instance(Double.parseDouble(network.getOutputValues().toString())));
	                error += measure.value(output, example);
	            }
	
	            //System.out.println("Train error: " + df.format(error) + " Iter: " + Integer.toString(i));
	            
	            double testError = 0;
	            for(int j = 0; j < testInstances.length; j++) {
	                network.setInputValues(testInstances[j].getData());
	                network.run();
	
	                Instance output = testInstances[j].getLabel(), example = new Instance(network.getOutputValues());
	                example.setLabel(new Instance(Double.parseDouble(network.getOutputValues().toString())));
	                testError += measure.value(output, example);
	            }

	            String mycontent = Integer.toString(i) + "," + df.format(error) + "," + df.format(testError) + "," + df.format(cumTrainTime) + System.lineSeparator();
	            byte[] bytesArray = mycontent.getBytes();

	      	  fos.write(bytesArray);
	      	  fos.flush();
	      	  
	      	  System.out.println(mycontent);
	        
	        }
        }
    	catch (IOException ioe) {
    		ioe.printStackTrace();
    	} 
    	finally {
    		try {
    			if (fos != null) 
    			{
    				fos.close();
    			}
    		} 
    		catch (IOException ioe) {
    			System.out.println("Error in closing the Stream");
    		}
    	}
    }

    private static Instance[] initializeInstances(String fileName) {

        double[][][] attributes = new double[158][][];

        try {        	
            BufferedReader br = new BufferedReader(new FileReader(new File(fileName)));

            for(int i = 0; i < attributes.length; i++) {
                Scanner scan = new Scanner(br.readLine());
                scan.useDelimiter(",");

				//(891, 158)
                attributes[i] = new double[2][];
                attributes[i][0] = new double[157]; // 157 attributes
                attributes[i][1] = new double[1];

                for(int j = 0; j < 157; j++)
                    attributes[i][0][j] = Double.parseDouble(scan.next());

                attributes[i][1][0] = Double.parseDouble(scan.next());
            }
        }
        catch(Exception e) {
            e.printStackTrace();
        }

        Instance[] instances = new Instance[attributes.length];

        for(int i = 0; i < instances.length; i++) {
            instances[i] = new Instance(attributes[i][0]);
            // classifications range from 0 to 30; split into 0 - 14 and 15 - 30
            instances[i].setLabel(new Instance(attributes[i][1][0] < 1 ? 0 : 1));
        }

        return instances;
    }
}
