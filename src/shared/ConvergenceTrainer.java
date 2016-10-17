package shared;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;

import opt.EvaluationFunction;
import opt.OptimizationAlgorithm;

/**
 * A convergence trainer trains a network
 * until convergence, using another trainer
 * @author Andrew Guillory gtg008g@mail.gatech.edu
 * @version 1.0
 */
public class ConvergenceTrainer implements Trainer {
    /** The default threshold */
    private static final double THRESHOLD = 1E-10;
    /** The maxium number of iterations */
    private static final int MAX_ITERATIONS = 500;

    /**
     * The trainer
     */
    private static DecimalFormat df = new DecimalFormat("0.000");
	private EvaluationFunction ef;
	private OptimizationAlgorithm trainer;
    private String filename;
    //private Trainer trainer;

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

    /**
     * Create a new convergence trainer
     * @param trainer the thrainer to use
     * @param threshold the error threshold
     * @param maxIterations the maximum iterations
     */
    public ConvergenceTrainer(OptimizationAlgorithm t,
    		EvaluationFunction ef,
    		double threshold,
    		int maxIterations,
    		String filename) {
        trainer = t;
        this.ef = ef;
        this.filename = filename;
        this.threshold = threshold;
        this.maxIterations = maxIterations;
    }
    /*
    public ConvergenceTrainer(Trainer trainer,
            double threshold, int maxIterations) {
        this.trainer = trainer;
        this.threshold = threshold;
        this.maxIterations = maxIterations;
    }
    */

    /**
     * Create a new convergence trainer
     * @param trainer the trainer to use
     */
    //public ConvergenceTrainer(Trainer trainer) {
    //    this(trainer, THRESHOLD, MAX_ITERATIONS);
    //}

    /**
     * @see Trainer#train()
     */
    /*
    public double train() {
        double lastError;
        double error = Double.MAX_VALUE;
        do {
           iterations++;
           lastError = error;
           error = trainer.train();
        } while (Math.abs(error - lastError) > threshold
             && iterations < maxIterations);
        return error;
    }
    */
    public double train() {
    	
    	//double sum = 0;
    	double lastError;
        double error = Double.MAX_VALUE;
        
    	
    	FileOutputStream fos = null;
        File file;
        try {
        	//Specify the file path here
	  	  	file = new File(filename + ".txt");
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
	            
                String mycontent = Integer.toString(iterations) + "," + df.format(ef.value(trainer.getOptimal())) + "," + df.format(trainingTime) + "," + df.format(cumTrainTime) + System.lineSeparator();
                
                byte[] bytesArray = mycontent.getBytes();
                fos.write(bytesArray);
                fos.flush();
  	      	  
                //System.out.println(mycontent);
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
    

}
