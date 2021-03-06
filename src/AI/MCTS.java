package AI;

import Core.Board;
import java.util.Random;


/**
 * MCTS Algorithm
 * @author Kailhan Hokstam
 */

public class MCTS extends AI {

    private int maxSims;
    private long timeForMoveInMs;

    private Random rand;

    private double explorationParameter;

    public static int currentPlayer;
    public static int totalSims;
    public static final double STANDARD_EXPLORATION_PARAMETER = 0.76;

    /**
     * Creates MCTS using time as a threshold for simulation step
     * @param timeForMoveInMs time in ms for simulation step
     * @param explorationParameter higher values makes MCTS favcr exploring
     */
    public MCTS(long timeForMoveInMs, double explorationParameter ) {
        System.out.println("using time as threshold");
        this.timeForMoveInMs = timeForMoveInMs;
        this.maxSims = 0;
        this.explorationParameter = explorationParameter;
        this.rand = new Random();
    }

    /**
     * Creates MCTS using amount as a threshold for simulation step
     * @param maxSims max amount of simulations
     * @param explorationParameter higher values makes MCTS favcr exploring
     */
    public MCTS(int maxSims, double explorationParameter) {
        this.maxSims = maxSims;
        this.timeForMoveInMs = 0;
        this.explorationParameter = explorationParameter;
        this.rand = new Random();
    }

    public double evaluateFitness(int gamesToBeSimmed, int boardSize) {
        return -1;
    }

    /**
     * Gets best move using MCTS algorithm for board
     * @param board board for which best move needs to be found
     * @return best move
     */
    public int[] getBestMove(Board board) {
        int[] move = new int[2];
        MCTSNode node = findMove(board);
        move[0] = node.getRow();
        move[1] = node.getColumn();
        return move;
    }

    /**
     * Gets best node using MCTS algorithm for board
     * @param board board for which best move needs to be found
     * @return best node
     */
    public MCTSNode findMove(Board board) {
        MCTSNode currentNode = new MCTSNode(new Board(board), explorationParameter);
        MCTSNode moveNode = null;
        boolean reachedThreshold = false;
        int currentAmountOfSims = 0;
        currentPlayer = board.getCurrentPlayer();
        long startTime = System.nanoTime();
        while(!reachedThreshold) {
            MCTSNode bestLeafNode = currentNode.getBestLeafNode();
            bestLeafNode.playoutSimulation();
            currentAmountOfSims++;
            if((currentAmountOfSims >= maxSims) && (maxSims !=0)) {
                reachedThreshold = true; //to enable otherways of thresholds eg time
            }
            if(((System.nanoTime() - startTime)/1000000 >= timeForMoveInMs) && (timeForMoveInMs !=0)) {
                reachedThreshold = true; //to enable otherways of thresholds eg time
            }
        }
        moveNode = currentNode.getBestSimulationChildNode();
        return moveNode;
        }

    public double getExplorationParameter() {
        return explorationParameter;
    }
}
