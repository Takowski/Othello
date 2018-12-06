package AI.Genetic_Algorithm;

import AI.AI;
import AI.EvaluationFunction;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import static AI.EvaluationFunction.WEIGHT_POLY_SIZE;
import static AI.Genetic_Algorithm.Population_EvalFunc.*;

public class GA_DirectEvalFunc {

    public static double SELECTION_RATIO = 2;

    public static void main(String[] args) {
        long startTime = System.nanoTime();
        Population_EvalFunc pop =  new Population_EvalFunc();
        System.out.println("Created a population");
        EvaluationFunction[] selectedIndividuals = new EvaluationFunction[pop.getPopSize()*2];
        EvaluationFunction[] selectedIndividualsChildren = new EvaluationFunction[pop.getPopSize()];
        pop.calculateFitness(Population.GA_GAMES_TO_BE_SIMMED, pop.getBoardSize());
        System.out.println("Calculated fitness of initial population");
        int maxIterations = 25;

        String[] gaLog = new String[(WEIGHT_POLY_SIZE + (pop.getBoardSize() * pop.getBoardSize()) + 2) * (1  + (maxIterations * pop.getPopSize()))];
        int gaLogIndex = 0;
        gaLog[gaLogIndex] = ("attributes"); gaLogIndex++;
        gaLog[gaLogIndex] = ("fitness"); gaLogIndex++;
        gaLog[gaLogIndex] = ("coinWeightPoly0"); gaLogIndex++;
        gaLog[gaLogIndex] = ("coinWeightPoly1"); gaLogIndex++;
        gaLog[gaLogIndex] = ("coinWeightPoly2"); gaLogIndex++;
        gaLog[gaLogIndex] = ("coinWeightPoly3"); gaLogIndex++;
        gaLog[gaLogIndex] = ("cornerWeightPoly0"); gaLogIndex++;
        gaLog[gaLogIndex] = ("cornerWeightPoly1"); gaLogIndex++;
        gaLog[gaLogIndex] = ("cornerWeightPoly2"); gaLogIndex++;
        gaLog[gaLogIndex] = ("cornerWeightPoly3"); gaLogIndex++;
        gaLog[gaLogIndex] = ("moveWeightPoly0"); gaLogIndex++;
        gaLog[gaLogIndex] = ("moveWeightPoly1"); gaLogIndex++;
        gaLog[gaLogIndex] = ("moveWeightPoly2"); gaLogIndex++;
        gaLog[gaLogIndex] = ("moveWeightPoly3"); gaLogIndex++;
        gaLog[gaLogIndex] = ("territoryWeightPoly0"); gaLogIndex++;
        gaLog[gaLogIndex] = ("territoryWeightPoly1"); gaLogIndex++;
        gaLog[gaLogIndex] = ("territoryWeightPoly2"); gaLogIndex++;
        gaLog[gaLogIndex] = ("territoryWeightPoly3"); gaLogIndex++;
        for(int i = 0; i < pop.getBoardSize(); i++) {
            for(int j = 0; j < pop.getBoardSize(); j++) {
                gaLog[gaLogIndex] = ("row" + i + "col" + j);
                gaLogIndex++;
            }
        }
        for(int i = 0; i < maxIterations; i++) {
            System.out.println("Start of iteration");
//            long starttime = System.nanoTime();
//            for(int j = 0; j < pop.getAIs().length; j++) {
//                gaLog[gaLogIndex] = String.valueOf(i); //attribute line
//                gaLogIndex++;
//                EvaluationFunction tmpAI = pop.getAIs()[j];
//                gaLog[gaLogIndex] = String.valueOf(tmpAI.getFitness()); //fitness line
//                gaLogIndex++;
//                for(int k = 0; k < tmpAI.getChromosome().length; k++) {
//                    gaLog[gaLogIndex] = String.valueOf(tmpAI.getChromosome()[k]);
//                    gaLogIndex++;
//                }
//            }
//            long endtime = System.nanoTime();
//            System.out.println("logging done in: " + ((endtime-startTime)/1000000000.0));
            selectedIndividuals = pop.selection(SELECTION_RATIO);
            System.out.println("selection done");
            for(int j = 0; j < pop.getPopSize(); j++) {
                selectedIndividualsChildren[j] = pop.randomWeightedCrossover(selectedIndividuals[j], selectedIndividuals[((pop.getPopSize()*2) - 1) -j]);
            }
            System.out.println("crossover done");
            pop.setAIs(selectedIndividualsChildren);
            //pop.nonUniformBitMutate(0.5, 0.5);
            System.out.println("end of iteration");
            pop.calculateFitness(GA_GAMES_TO_BE_SIMMED, pop.getBoardSize());
            System.out.println("iteration: " + i);
        }
        long endTime = System.nanoTime();
        System.out.println("Completed in: " + ((endTime - startTime)/1000000.0) + " ms");
        AI worstSpecimen = pop.getWorstSpecimen();
        System.out.println("Fitness of worst specimen: " + worstSpecimen.getFitness());
        //worstSpecimen.getEvaluator().printChromosome();
        System.out.println("Wins when having first move: " + worstSpecimen.getWinsFirstMove());
        System.out.println("Wins when having second move: " + worstSpecimen.getWinsSecondMove());

        AI topSpecimen = pop.getTopSpecimen();
        System.out.println("Fitness of top specimen: " + topSpecimen.getFitness());
        //topSpecimen.getEvaluator().printChromosome();
        System.out.println("Wins when having first move: " + topSpecimen.getWinsFirstMove());
        System.out.println("Wins when having second move: " + topSpecimen.getWinsSecondMove());

        StringBuilder gaCSVLogBuilder = new StringBuilder();

        for(int i = 1; i < gaLog.length; i++) {
            gaCSVLogBuilder.append(gaLog[i - 1] + ",");
            if(i % ((WEIGHT_POLY_SIZE + (pop.getBoardSize() * pop.getBoardSize()) + 2)) == 0) gaCSVLogBuilder.append("\n");
        }
        gaCSVLogBuilder.append(gaLog[gaLog.length-1]);
        String gaCSVLog = gaCSVLogBuilder.toString();
        try {
            String fileName  ="_boardSize_" + String.valueOf(GA_BOARD_SIZE) +
                    "_WPB_" + String.valueOf(GA_WEIGHT_POLY_BOUND) +
                    "_TB_" + String.valueOf(GA_TERRITORY_BOUND) +
                    "_GTB_" + String.valueOf(GA_GAMES_TO_BE_SIMMED) +
                    "_popSize_" + String.valueOf(GA_POP_SIZE) +
                    "_selectionRatio" + String.valueOf(SELECTION_RATIO) +
                    "_time_" + String.valueOf(endTime - startTime) +
                    "_maxIter_" + String.valueOf(maxIterations) +
                    ".csv";
            BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
            writer.write(gaCSVLog);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("GA_Eval finished");
    }

}