package AI;

import Core.Board;
import Core.Logic;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static AI.MCTS_TreeReuse.currentPlayer;
import static Core.Board.BLACK;
import static Core.Board.WHITE;

/**
 * Node structure used by MCTS with TreeReuse
 * @author Kailhan Hokstam
 */

public class MCTSNode_TreeReuse {

    public static final int WIN = 1;
    public static final int DRAW = 0;
    public static final int LOSS = 0;

    private double wins;
    private double sims;
    private double explorationParameter;
    private boolean terminalNode;

    private Board board;
    private static Random rand = new Random();

    private MCTSNode_TreeReuse parentNode;
    private List<MCTSNode_TreeReuse> childNodes = new ArrayList<MCTSNode_TreeReuse>();

    public MCTSNode_TreeReuse(Board board, double explorationParameter) {
        this.board = board;
        this.parentNode = null;
        this.wins = 0;
        this.sims = 0;
        this.explorationParameter = explorationParameter;
        this.terminalNode = false;
    }

    /**
     * Finds on which row this board differs with its parent's boards
     * @return row number
     */
    public int getRow() {
        int row = -1; //makes sure we throw an error if we have not updated our coordinate
        Board parentBoard = new Board(parentNode.getData());
        int[][] parentBoardGrid = parentBoard.getBoardGrid();
        Board currentBoard = new Board(this.getData());
        int[][] currentBoardGrid = currentBoard.getBoardGrid();
        for(int r = 0; r < parentBoardGrid.length; r++) {
            for(int c = 0; c < parentBoardGrid.length; c++) {
                if(parentBoardGrid[r][c] == 0 && currentBoardGrid[r][c] != 0) row = r;
            }
        }
        return row;
    }

    /**
     * Finds on which column this board differs with its parent's boards
     * @return column number
     */
    public int getColumn() {
        int column = -1; //makes sure we throw an error if we have not updated our coordinate
        Board parentBoard = new Board(parentNode.getData());
        int[][] parentBoardGrid = parentBoard.getBoardGrid();
        Board currentBoard = new Board(this.getData());
        int[][] currentBoardGrid = currentBoard.getBoardGrid();
        for(int r = 0; r < parentBoardGrid.length; r++) {
            for(int c = 0; c < parentBoardGrid.length; c++) {
                if(parentBoardGrid[r][c] == 0 && currentBoardGrid[r][c] != 0) column = c;
            }
        }
        return column;
    }

    /**
     * Gets data in node e.g., board
     * @return Board in node
     */
    public Board getData() {
        return board;
    }

    /**
     * Score based on which we traverse tree, balances exploration and exploitation
     * @return tree traversal score
     */
    public double getSelectionScore() {
        double exploitationScore = (sims == 0) ? 0 : ((double) (wins / sims));
        double explorationScore;
        if((wins == 0) && (MCTS_TreeReuse.totalSims == 0)) {
            explorationScore = 0;
        } else if((wins == 0) && !(MCTS_TreeReuse.totalSims == 0)) {
            explorationScore = (explorationParameter * (double) (Math.sqrt(Math.log(MCTS_TreeReuse.totalSims))));
        } else if(!(wins == 0) && (MCTS_TreeReuse.totalSims == 0)) {
            explorationScore = 0;
        } else {
            explorationScore = (explorationParameter * (double) (Math.sqrt(Math.log(MCTS_TreeReuse.totalSims) / wins)));
        }
       double selectionScore = exploitationScore + explorationScore;
        //selectionScore = (sims == 0) ? 1 : 1/sims;
        return selectionScore;
    }

    /**
     * Simulate a game following game logic starting from board in node &&&& backprogagate score
     */
    public void playoutSimulation() {
        Board startBoard = new Board(board);
        Board simulationBoard = new Board(board);
        boolean gameFinished = false;
        int[] move;
        while(!gameFinished) {
            if(Logic.checkMovePossible(simulationBoard)) {
                move = selectMove(simulationBoard, startBoard);
                simulationBoard.applyMove(move);
            } else {
                simulationBoard.applyMove();
                if(!Logic.checkMovePossible(simulationBoard)) {
                    gameFinished = true;
                }
            }
        }

        int gameState = DRAW;
        int numberOfBlackCoins = simulationBoard.getNrSquares(BLACK);
        int numberOfWhiteCoins = simulationBoard.getNrSquares(WHITE);
        if(currentPlayer == BLACK) {
            if(numberOfBlackCoins > numberOfWhiteCoins) gameState = WIN;
            if(numberOfBlackCoins < numberOfWhiteCoins) gameState = LOSS;
        } else if(currentPlayer == WHITE) {
            if(numberOfBlackCoins > numberOfWhiteCoins) gameState = LOSS;
            if(numberOfBlackCoins < numberOfWhiteCoins) gameState = WIN;
        }

        wins += gameState;
        sims++;
        MCTS_TreeReuse.totalSims++;
        MCTSNode_TreeReuse currentNode = this;


        //BACKPROPOGATION
        while(currentNode.getParentNode() != null) {
            if(currentNode.getParentNode().getData().getCurrentPlayer() != currentPlayer) currentNode.getParentNode().setWins(currentNode.getParentNode().getWins() + gameState);
            currentNode.getParentNode().setSims(currentNode.getParentNode().getSims() + 1);
            currentNode = currentNode.getParentNode();
        }
    }

    /**
     * Returns a random move thats legal from simulationBoard
     * @param simulationBoard board from which legal move is determined
     * @param startBoard not used
     * @return a move that can be applied to a (simulation)board
     */
    public int[] selectMove(Board simulationBoard, Board startBoard) {
        int[][] possibleMoves = Logic.getPossibleMoves(simulationBoard);
        return possibleMoves[rand.nextInt(possibleMoves.length)];
    }

    /**
     * Finds node(s) with the best selection score and randomly returns one
     * @return child node with a highest selection score
     */
    public MCTSNode_TreeReuse getBestSelectionChildNode() {
        List<MCTSNode_TreeReuse> potentialChildren = new ArrayList<MCTSNode_TreeReuse>();
        double maxScore = Integer.MIN_VALUE;
        for(MCTSNode_TreeReuse childNode : childNodes) {
            double childNodeScore = childNode.getSelectionScore();
            if(childNodeScore >= maxScore) maxScore = childNodeScore;
        }
        for(MCTSNode_TreeReuse childNode : childNodes) {
            double childNodeScore = childNode.getSelectionScore();
            if(childNodeScore >= maxScore) potentialChildren.add(childNode);
        }
        return potentialChildren.get(rand.nextInt(potentialChildren.size()));
    }

    /**
     * Finds node(s) with the best simulation score (wins/sims) and randomly returns one
     * @return child node with a highest simulation score
     */
    public MCTSNode_TreeReuse getBestSimulationChildNode() {
        List<MCTSNode_TreeReuse> potentialChildren = new ArrayList<MCTSNode_TreeReuse>();
        double maxScore = -1;
        for(MCTSNode_TreeReuse childNode : childNodes) {
            double childNodeScore = (childNode.getSims() == 0) ? 0 : (double)childNode.getWins()/childNode.getSims();
            if(childNodeScore >= maxScore) maxScore = childNodeScore;
        }
        for(MCTSNode_TreeReuse childNode : childNodes) {
            double childNodeScore = (childNode.getSims() == 0) ? 0 : (double)childNode.getWins()/childNode.getSims();
            if(childNodeScore >= maxScore) potentialChildren.add(childNode);
        }
        return potentialChildren.get(rand.nextInt(potentialChildren.size()));
    }

    /**
     * Starting from current node traverse down tree until reaching a leaf node (node with no children)
     * picking from the potential nodes along the traversal the ones with the highest selection score
     * @return leaf node with highest selection score
     */
    public MCTSNode_TreeReuse getBestLeafNode() {
        MCTSNode_TreeReuse currentNode = this;
        while(!currentNode.getChildNodes().isEmpty()) {
            currentNode = currentNode.getBestSelectionChildNode();
        }
        currentNode.createChildren();
        return currentNode;
    }

    /**
     * Find all legal boards that can be constructed from applying one legal move to board in current node
     * and store those as its children
     */
    public void createChildren() {
        Board board = new Board(this.board);
        int[][] possibleMoves;
        if(Logic.checkMovePossible(board)) {
            possibleMoves = Logic.getPossibleMoves(board);
            if(getChildNodes().isEmpty()) {
                for(int i = 0; i < possibleMoves.length; i++){
                    Board possibleBoard = new Board(board);
                    possibleBoard.applyMove(possibleMoves[i]);
                    MCTSNode_TreeReuse possibleNode = new MCTSNode_TreeReuse(possibleBoard, explorationParameter);
                    possibleNode.setParentNode(this);
                    childNodes.add(possibleNode);
                }
            } else {
                for(int i = 0; i < possibleMoves.length; i++){
                    Board possibleBoard = new Board(board);
                    possibleBoard.applyMove(possibleMoves[i]);
                    boolean alreadyHasBoard = false;
                    for(MCTSNode_TreeReuse childNode : getChildNodes()) {
                        if(childNode.getData().isSameBoard(possibleBoard)) alreadyHasBoard = true;
                    }
                    if(!alreadyHasBoard) {
                        MCTSNode_TreeReuse possibleNode = new MCTSNode_TreeReuse(possibleBoard, explorationParameter);
                        possibleNode.setParentNode(this);
                        childNodes.add(possibleNode);
                    }
                }
            }
        } else {
            board.applyMove();
            if(Logic.checkMovePossible(board)) {
                if(getChildNodes().isEmpty()) {
                    Board possibleBoard = new Board(board);
                    MCTSNode_TreeReuse possibleNode = new MCTSNode_TreeReuse(possibleBoard, explorationParameter);
                    possibleNode.setParentNode(this);
                    childNodes.add(possibleNode);
                } else {
                    Board possibleBoard = new Board(board);
                    boolean alreadyHasBoard = false;
                    for(MCTSNode_TreeReuse childNode : getChildNodes()) {
                        if(childNode.getData().isSameBoard(possibleBoard)) alreadyHasBoard = true;
                    }
                    if(!alreadyHasBoard) {
                        MCTSNode_TreeReuse possibleNode = new MCTSNode_TreeReuse(possibleBoard, explorationParameter);
                        possibleNode.setParentNode(this);
                        childNodes.add(possibleNode);
                    }
                }
            } else {
                terminalNode = true;
            }
        }
    }

    /**
     * Determines size of (sub)tree starting from this node
     * @return size of tree in amount of nodes
     */
    public int getTreeSize() {
        int treeSize = 1;
        List<MCTSNode_TreeReuse> queue = new ArrayList<MCTSNode_TreeReuse>();
        queue.add(this);
        while(!queue.isEmpty()){
            MCTSNode_TreeReuse node = queue.remove(0);
            queue.addAll(node.getChildNodes());
            treeSize += node.getChildNodes().size();
        }
        return treeSize;
    }

    /**
     * Determines height of tree starting at rootNode
     * @param rootNode height
     * @return
     */
    public int getHeight(MCTSNode_TreeReuse rootNode){
        int height = 0;
        for(MCTSNode_TreeReuse node : rootNode.getChildNodes()){
            height = Math.max(height, getHeight(node));
        }
        return height+1;
    }

    public MCTSNode_TreeReuse getParentNode() {
        return parentNode;
    }

    public void setParentNode(MCTSNode_TreeReuse parentNode) {
        this.parentNode = parentNode;
    }

    public List<MCTSNode_TreeReuse> getChildNodes() {
        return childNodes;
    }

    public void setChildNodes(List<MCTSNode_TreeReuse> childNodes) {
        this.childNodes = childNodes;
    }

    public double getWins() {
        return wins;
    }

    public void setWins(double wins) {
        this.wins = wins;
    }

    public double getSims() {
        return sims;
    }

    public void setSims(double sims) {
        this.sims = sims;
    }

    public boolean hasChildren() {
        return !getChildNodes().isEmpty();
    }

    public void addChild(MCTSNode_TreeReuse childNode) {
        childNodes.add(childNode);
    }


}