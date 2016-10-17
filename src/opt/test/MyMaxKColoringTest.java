package opt.test;

import java.util.Arrays;
import java.util.Random;

import opt.ga.MaxKColorFitnessFunction;
import opt.ga.Vertex;

import dist.DiscreteDependencyTree;
import dist.DiscretePermutationDistribution;
import dist.DiscretePermutationDistribution_array;
import dist.DiscreteUniformDistribution;
import dist.Distribution;
import opt.DiscreteChangeOneNeighbor;
import opt.EvaluationFunction;
import opt.SwapNeighbor;
import opt.GenericHillClimbingProblem;
import opt.HillClimbingProblem;
import opt.NeighborFunction;
import opt.RandomizedHillClimbing;
import opt.SimulatedAnnealing;
import opt.ga.CrossoverFunction;
import opt.ga.DiscreteChangeOneMutation;
import opt.ga.SingleCrossOver;
import opt.ga.SwapMutation;
import opt.ga.GenericGeneticAlgorithmProblem;
import opt.ga.GeneticAlgorithmProblem;
import opt.ga.MutationFunction;
import opt.ga.StandardGeneticAlgorithm;
import opt.ga.UniformCrossOver;
import opt.prob.GenericProbabilisticOptimizationProblem;
import opt.prob.MIMIC;
import opt.prob.ProbabilisticOptimizationProblem;
import shared.FixedIterationTrainer;

/**
 * 
 * @author kmandal
 * @version 1.0
 */
public class MyMaxKColoringTest {
    /** The n value */
    private static final int N = 50; // number of vertices
    private static final int L =4; // L adjacent nodes per vertex
    private static final int K = 6; // K possible colors
    /**
     * The test main
     * @param args ignored
     */
    public static void main(String[] args) {
        // create the random velocity
       Random random = new Random((long) N*L);
       Vertex[] vertices = new Vertex[N];
                for (int i = 0; i < N; i++) {
                    vertices[i] = new Vertex();
                }
                for (int i = 0; i < N; i++) {
                    vertices[i].setAdjMatrixSize(L);
                    int j=0; int runAway=0;
                    while(vertices[i].getAadjacencyColorMatrix().size() < L){
                        int aNeighbor=random.nextInt(N);
                        if(aNeighbor>i &&
                                !vertices[i].getAadjacencyColorMatrix().contains(aNeighbor)
                                && vertices[aNeighbor].getAadjacencyColorMatrix().size()<L) {
                            vertices[i].getAadjacencyColorMatrix().add(aNeighbor);
                            vertices[aNeighbor].getAadjacencyColorMatrix().add(i);
                            runAway=0;
                        } else runAway++;
                        if(runAway>1000) {
                            System.out.println("can't find solution");break;
                        }
                    }
                }     
        System.out.println("Adjacency graph");
        for (int i = 0; i < N; i++) {
            Vertex vertex = vertices[i];
            System.out.println(i+" "+Arrays.toString(vertex.getAadjacencyColorMatrix().toArray()));
        }
        System.out.printf("---------------");
        // for rhc, sa, and ga we use a permutation based encoding
        int[] ranges = new int[N];
        Arrays.fill(ranges, K);
        MaxKColorFitnessFunction ef = new MaxKColorFitnessFunction(vertices);
        Distribution odd = new DiscretePermutationDistribution_array(ranges);    
        NeighborFunction nf = new SwapNeighbor();
        MutationFunction mf = new SwapMutation();
        UniformCrossOver cf = new UniformCrossOver();
        Distribution df = new DiscreteDependencyTree(.1);
        HillClimbingProblem hcp = new GenericHillClimbingProblem(ef, odd, nf);
        GeneticAlgorithmProblem gap = new GenericGeneticAlgorithmProblem(ef, odd, mf, cf);
        ProbabilisticOptimizationProblem pop = new GenericProbabilisticOptimizationProblem(ef, odd, df);
        
        long starttime = System.currentTimeMillis();
        RandomizedHillClimbing rhc = new RandomizedHillClimbing(hcp);      
        //FixedIterationTrainer fit = new FixedIterationTrainer(rhc, 20000);
        FixedIterationTrainer fit;
        /*
        fit = new FixedIterationTrainer(rhc, ef, 200000, "KColor_RHC");
        
        fit.train();
        System.out.println("RHC: " + ef.value(rhc.getOptimal()));
        System.out.println(ef.foundConflict());
        System.out.println("Time : "+ (System.currentTimeMillis() - starttime));
        
        System.out.println("============================");
        
        starttime = System.currentTimeMillis();
        SimulatedAnnealing sa = new SimulatedAnnealing(1E12, .1, hcp);
        fit = new FixedIterationTrainer(sa, ef, 20000, "KColor_SA");
        fit.train();
        System.out.println("SA: " + ef.value(sa.getOptimal()));
        System.out.println(ef.foundConflict());
        System.out.println("Time : "+ (System.currentTimeMillis() - starttime));
        
        System.out.println("============================");
        
        starttime = System.currentTimeMillis();
        StandardGeneticAlgorithm ga = new StandardGeneticAlgorithm(200, 10, 60, gap);
        fit = new FixedIterationTrainer(ga, ef, 50000, "KColor_GA");
        fit.train();
        System.out.println("GA: " + ef.value(ga.getOptimal()));
        System.out.println(ef.foundConflict());
        System.out.println("Time : "+ (System.currentTimeMillis() - starttime));
        
        System.out.println("============================");
        */
        MIMIC mimic;
        
        starttime = System.currentTimeMillis();
        
        mimic = new MIMIC(500, 100, pop);
        fit = new FixedIterationTrainer(mimic, ef, 1000, "KColor_MIMIC_500_100");
        fit.train();
        System.out.println("MIMIC: " + ef.value(mimic.getOptimal()));  
        System.out.println(ef.foundConflict());
        System.out.println("Time : "+ (System.currentTimeMillis() - starttime));
        
        starttime = System.currentTimeMillis();
        mimic = new MIMIC(1000, 100, pop);
        fit = new FixedIterationTrainer(mimic, ef, 1000, "KColor_MIMIC_1000_100");
        fit.train();
        System.out.println("MIMIC: " + ef.value(mimic.getOptimal()));  
        System.out.println(ef.foundConflict());
        System.out.println("Time : "+ (System.currentTimeMillis() - starttime));
        
        
        starttime = System.currentTimeMillis();
        mimic = new MIMIC(1000, 500, pop);
        fit = new FixedIterationTrainer(mimic, ef, 1000, "KColor_MIMIC_1000_500");
        fit.train();
        System.out.println("MIMIC: " + ef.value(mimic.getOptimal()));  
        System.out.println(ef.foundConflict());
        System.out.println("Time : "+ (System.currentTimeMillis() - starttime));
        
        starttime = System.currentTimeMillis();
        mimic = new MIMIC(10000, 5000, pop);
        fit = new FixedIterationTrainer(mimic, ef, 1000, "KColor_MIMIC_10000_5000");
        fit.train();
        System.out.println("MIMIC: " + ef.value(mimic.getOptimal()));  
        System.out.println(ef.foundConflict());
        System.out.println("Time : "+ (System.currentTimeMillis() - starttime));
        
        starttime = System.currentTimeMillis();
        mimic = new MIMIC(200, 10, pop);
        fit = new FixedIterationTrainer(mimic, ef, 800, "KColor_MIMIC_200_10");
        fit.train();
        System.out.println("MIMIC: " + ef.value(mimic.getOptimal()));  
        System.out.println(ef.foundConflict());
        System.out.println("Time : "+ (System.currentTimeMillis() - starttime));
        
    }
}
