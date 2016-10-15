package shared;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;

import opt.EvaluationFunction;
import opt.OptimizationAlgorithm;

/**
 * A fixed iteration trainer
 * @author Andrew Guillory gtg008g@mail.gatech.edu
 * @version 1.0
 */
public class FixedIterationTrainer implements Trainer {
    
    /**
     * The inner trainer
     */
	private static DecimalFormat df = new DecimalFormat("0.000");
	private EvaluationFunction ef;
	private OptimizationAlgorithm trainer;
    private String filename;
	//private Trainer trainer;
    
    /**
     * The number of iterations to train
     */
    private int iterations;
    
    /**
     * Make a new fixed iterations trainer
     * @param t the trainer
     * @param iter the number of iterations
     */
    
    
    public FixedIterationTrainer(OptimizationAlgorithm t,
    		EvaluationFunction ef,
    		int iter, String filename) {
        trainer = t;
        iterations = iter;
        this.ef = ef;
        this.filename = filename;
    }
    /*
    public FixedIterationTrainer(Trainer t, int iter) {
        trainer = t;
        iterations = iter;
    }
	*/
    /**
     * @see shared.Trainer#train()
     */
    public double train() {
    	
    	double sum = 0;
    	
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
	  		
            for (int i = 0; i < iterations; i++) {
            	double start = System.nanoTime(), end, trainingTime = 0;
            	
                sum += trainer.train();
                end = System.nanoTime();
	            trainingTime = end - start;
	            trainingTime /= Math.pow(10,9);
	            cumTrainTime += trainingTime;
	            
                String mycontent = Integer.toString(i) + "," + df.format(ef.value(trainer.getOptimal())) + "," + df.format(trainingTime) + "," + df.format(cumTrainTime) + System.lineSeparator();
                
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
        
        return sum / iterations;
    }
    

}
