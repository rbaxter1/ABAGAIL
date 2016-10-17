package func.test;

import shared.ConvergenceTrainer;
import shared.ConvergenceTrainerBP;
import shared.DataSet;
import shared.ErrorMeasure;
import shared.Instance;
import shared.SumOfSquaresError;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Scanner;

import func.nn.backprop.BackPropagationNetwork;
import func.nn.backprop.BackPropagationNetworkFactory;
import func.nn.backprop.BatchBackPropagationTrainer;
import func.nn.backprop.RPROPUpdateRule;
import opt.OptimizationAlgorithm;

/**
 * A simple classification test
 * @author Andrew Guillory gtg008g@mail.gatech.edu
 * @version 1.0
 */
public class MyBackPropTest {

	private static Instance[] testInstances = initializeInstances("titanic_test.txt");
    private static Instance[] trainInstances = initializeInstances("titanic_train.txt");
    private static int inputLayer = 157, hiddenLayer = 100, outputLayer = 1, trainingIterations = /*1000*/10000;
    private static ErrorMeasure measure = new SumOfSquaresError();
    private static DecimalFormat df = new DecimalFormat("0.000");
    private static double threshold = 0.0;
    private static int maxIterations = 100;
    private static String results = "";
    private static BackPropagationNetwork network;
    
    /**
     * Tests out the perceptron with the classic xor test
     * @param args ignored
     * @throws SecurityException 
     * @throws NoSuchMethodException 
     */
    public static void main(String[] args) throws NoSuchMethodException, SecurityException {
        BackPropagationNetworkFactory factory = 
            new BackPropagationNetworkFactory();
        /*
        double[][][] data = {
            { { 1, 1 }, { .1, .9 } },
            { { 0, 1 }, { 0, 1 } },
            { { 0, 0 }, { .9, .1 } }
        };
        
        Instance[] patterns = new Instance[data.length];
        
        for (int i = 0; i < patterns.length; i++) {
            patterns[i] = new Instance(data[i][0]);
            patterns[i].setLabel(new Instance(data[i][1]));
        }
        */
        
        //BackPropagationNetwork
        network = factory.createClassificationNetwork(
           new int[] { inputLayer, hiddenLayer, outputLayer });
        DataSet set = new DataSet(trainInstances);
        ConvergenceTrainerBP trainer = new ConvergenceTrainerBP(
               new BatchBackPropagationTrainer(set, network,
                   new SumOfSquaresError(), new RPROPUpdateRule()),
               threshold, maxIterations, network);
        
        double start = System.nanoTime(), end, trainingTime, testingTime, correct = 0, incorrect = 0;
        
        trainer.train();
        
        end = System.nanoTime();
        trainingTime = end - start;
        trainingTime /= Math.pow(10,9);

        System.out.println("Convergence in " 
            + trainer.getIterations() + " iterations");
        /*
        for (int i = 0; i < testInstances.length; i++) {
            network.setInputValues(testInstances[i].getData());
            network.run();
            System.out.println("~~");
            System.out.println(testInstances[i].getLabel());
            System.out.println(network.getOutputValues());
        }
        */
        
        
        //Instance optimalInstance = oa[i].getOptimal();
        //networks[i].setWeights(optimalInstance.getData());
        
        
        double predicted, actual;
        start = System.nanoTime();
        for(int j = 0; j < testInstances.length; j++) {
            network.setInputValues(testInstances[j].getData());
            network.run();

            predicted = Double.parseDouble(testInstances[j].getLabel().toString());
            actual = Double.parseDouble(network.getOutputValues().toString());

            double trash = Math.abs(predicted - actual) < 0.5 ? correct++ : incorrect++;
        }
        end = System.nanoTime();
        testingTime = end - start;
        testingTime /= Math.pow(10,9);

        results +=  "\nTest Results for Back Propagation: \nCorrectly classified " + correct + " instances." +
                    "\nIncorrectly classified " + incorrect + " instances.\nPercent correctly classified: "
                    + df.format(correct/(correct+incorrect)*100) + "%\nTraining time: " + df.format(trainingTime)
                    + " seconds\nTesting time: " + df.format(testingTime) + " seconds\n";
        
        // TRAINING ERROR
        predicted = 0;
        actual = 0;
        start = System.nanoTime();
        for(int j = 0; j < trainInstances.length; j++) {
            network.setInputValues(trainInstances[j].getData());
            network.run();

            predicted = Double.parseDouble(trainInstances[j].getLabel().toString());
            actual = Double.parseDouble(network.getOutputValues().toString());

            double trash = Math.abs(predicted - actual) < 0.5 ? correct++ : incorrect++;
        }
        end = System.nanoTime();
        testingTime = end - start;
        testingTime /= Math.pow(10,9);

        results +=  "\nTrain Results for Back Propagation: \nCorrectly classified " + correct + " instances." +
                    "\nIncorrectly classified " + incorrect + " instances.\nPercent correctly classified: "
                    + df.format(correct/(correct+incorrect)*100) + "%\nTraining time: " + df.format(trainingTime)
                    + " seconds\nTesting time: " + df.format(testingTime) + " seconds\n";
        
        System.out.println(results);
    }
    
    public static void eval(String dummy)
    {
    	double error = 0;
        for(int j = 0; j < trainInstances.length; j++) {
            network.setInputValues(trainInstances[j].getData());
            network.run();

            Instance output = trainInstances[j].getLabel(), example = new Instance(network.getOutputValues());
            example.setLabel(new Instance(Double.parseDouble(network.getOutputValues().toString())));
            error += measure.value(output, example);
        }
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
