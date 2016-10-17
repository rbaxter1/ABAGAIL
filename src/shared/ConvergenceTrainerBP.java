package shared;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.util.Scanner;

import func.nn.backprop.BackPropagationNetwork;

/**
 * A convergence trainer trains a network
 * until convergence, using another trainer
 * @author Andrew Guillory gtg008g@mail.gatech.edu
 * @version 1.0
 */
public class ConvergenceTrainerBP implements Trainer {
    /** The default threshold */
    private static final double THRESHOLD = 1E-10;
    /** The maxium number of iterations */
    private static final int MAX_ITERATIONS = 500;

    /**
     * The trainer
     */
    private Trainer trainer;

    /**
     * The threshold
     */
    private double threshold;
    
    /**
     * The number of iterations trained
     */
    private int iterations;
    
    /**
     * The maximum number of iterations to use
     */
    private int maxIterations;
    
    private BackPropagationNetwork network;
    private static ErrorMeasure measure = new SumOfSquaresError();
    private static Instance[] testInstances = initializeInstances("titanic_test.txt");
    private static Instance[] trainInstances = initializeInstances("titanic_train.txt");
    private static DecimalFormat df = new DecimalFormat("0.000");
    
    /**
     * Create a new convergence trainer
     * @param trainer the thrainer to use
     * @param threshold the error threshold
     * @param maxIterations the maximum iterations
     */
    public ConvergenceTrainerBP(Trainer trainer,
            double threshold, int maxIterations,
            BackPropagationNetwork network) {
        this.trainer = trainer;
        this.threshold = threshold;
        this.maxIterations = maxIterations;
        this.network = network;
        
    }
    

    /**
     * Create a new convergence trainer
     * @param trainer the trainer to use
     * @throws SecurityException 
     * @throws NoSuchMethodException 
     */
    
    //public ConvergenceTrainer(Trainer trainer) {
    //    this(trainer, THRESHOLD, MAX_ITERATIONS);
    //}

    /**
     * @see Trainer#train()
     */
    public double train() {
        double lastError;
        double error = Double.MAX_VALUE;
        
        FileOutputStream fos = null;
        File file;
        try {
        	//Specify the file path here
	  	  	file = new File("BackProp.txt");
	  	  	fos = new FileOutputStream(file);
	
            /* This logic will check whether the file
	  	     * exists or not. If the file is not found
	  	     * at the specified location it would create
	  	     * a new file*/
	  	  	if (!file.exists()) {
	  	  		file.createNewFile();
	  	  	}
	  	  	
	  	  double cumTrainTime = 0;
			  
	        	            
	        do {
	           iterations++;
	           lastError = error;
	           
	           double start = System.nanoTime(), end, trainingTime = 0;

	           error = trainer.train();
	        	end = System.nanoTime();
	            trainingTime = end - start;
	            trainingTime /= Math.pow(10,9);
	            cumTrainTime += trainingTime;

	           
	           double error2 = 0;
	           for(int j = 0; j < trainInstances.length; j++) {
	               network.setInputValues(trainInstances[j].getData());
	               network.run();
	
	               Instance output = trainInstances[j].getLabel(), example = new Instance(network.getOutputValues());
	               example.setLabel(new Instance(Double.parseDouble(network.getOutputValues().toString())));
	               error2 += measure.value(output, example);
	           }
	           
	           double testError = 0;
	           for(int j = 0; j < testInstances.length; j++) {
	               network.setInputValues(testInstances[j].getData());
	               network.run();
	
	               Instance output = testInstances[j].getLabel(), example = new Instance(network.getOutputValues());
	               example.setLabel(new Instance(Double.parseDouble(network.getOutputValues().toString())));
	               testError += measure.value(output, example);
	           }
	
	           String mycontent = Integer.toString(iterations) + "," + df.format(error2) + "," + df.format(testError) + "," + df.format(cumTrainTime) + System.lineSeparator();
	           byte[] bytesArray = mycontent.getBytes();
	
	           fos.write(bytesArray);
	           fos.flush();
      	  
	           System.out.println(mycontent);
	           
           
	        } while (Math.abs(error - lastError) > threshold
	             && iterations < maxIterations);
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
        
        return error;
    }
    
    
    /**
     * Get the number of iterations used
     * @return the number of iterations
     */
    public int getIterations() {
        return iterations;
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
