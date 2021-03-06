package AI;

import AI.Tests.GenericTest;
import Core.Board;
import Core.GameScene;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BinaryOperator;

public class MiniMaxAlph extends AI {

    //private EvaluationFunction evaluator;
    private GameTree gameTree;
    private Node<Board> root;
    private int depth;
    private Board board;

    public MiniMaxAlph(int depth, Board board) {
        this.depth = depth;
        this.evaluator = new EvaluationFunction(board);
    }

    public MiniMaxAlph(int depth, Board board, double[] weights) {
        this.depth = depth;
        this.evaluator = new EvaluationFunction(board,weights);
    }

    public MiniMaxAlph(int depth, Board board, GameTree gameTree) {
        this.depth = depth;
        this.evaluator = new EvaluationFunction(board);
        this.gameTree = gameTree;
    }

    public double evaluateFitness(int gamesToBeSimmed, int boardSize) {
        AI stupid = new Stupid();
        gamesToBeSimmed = (gamesToBeSimmed < 2) ? 2 : gamesToBeSimmed;
        gamesToBeSimmed = (gamesToBeSimmed % 2 != 0) ? gamesToBeSimmed + 1 : gamesToBeSimmed;
        GenericTest.test(this, stupid, gamesToBeSimmed / 2, boardSize);
        winsFirstMove = GenericTest.getPlayer1Wins();
        GenericTest.test(stupid, this, gamesToBeSimmed / 2, boardSize);
        winsSecondMove = GenericTest.getPlayer2Wins();
        //System.out.println(this.getEvaluator().getChromosome()[2]);
        //System.out.println("this.fitness = (winsFirstMove + winsSecondMove)/gamesToBeSimmed;");
        //System.out.println(gamesToBeSimmed);
        fitness = (winsFirstMove + winsSecondMove) / gamesToBeSimmed;

        return (winsFirstMove + winsSecondMove) / gamesToBeSimmed;
    }

    public int[] getBestMove(Board board) {

        Node<Board> root = getRootMinimaxed(board);

        int[] bestMove = new int[2];
        try {
            bestMove[0] = selectMove(root).getRow();
            bestMove[1] = selectMove(root).getColumn();
        }
        catch(NullPointerException e) {
            System.out.println("no more moves");
        }
        return bestMove;
    }

    public double miniMaxAB(Node<Board> currentNode, double alpha, double beta, int playerValue) {
        if (currentNode.getChildren().size() == 0) {
            double value;

//            PlayerModel playerModel = GenericTest.getPlayerModel();
//            if (playerValue == Board.BLACK)
//            {
//                value = playerValue * playerModel.getEvaluationFunction().evaluate(currentNode.getData());
//            }
//            else
//            {
//                value = playerValue * evaluator.evaluate(currentNode.getData());
//            }

            value = playerValue * evaluator.evaluate(currentNode.getData());

            currentNode.setValue(value);
            return value;

        } else if (currentNode.getData().getCurrentPlayer() == playerValue) {
            double value = Integer.MIN_VALUE;
            for (Node<Board> currentChild : currentNode.getChildren()) {
                value = Math.max(value, miniMaxAB(currentChild,alpha,beta, playerValue));
                alpha = Math.max(value,alpha);
                if(alpha >= beta){
                  //  System.out.println("pruned");
                    break;
                }
            }
            currentNode.setValue(value);
            return value;

        } else { //MINVALUE, opponent player
            double value = Integer.MAX_VALUE;
            for (Node<Board> currentChild : currentNode.getChildren()) {
                value = Math.min(value, miniMaxAB(currentChild,alpha,beta,playerValue));
                beta = Math.min(value,beta);
                if(alpha >= beta){
                  //  System.out.println("pruned");
                    break;
                }
            }
            currentNode.setValue(value);
            return value;
        }
    }


    public Node<Board> selectMove(Node<Board> root) {
        for (Node<Board> currentChild : root.getChildren()) {
            if (currentChild.getValue() == root.getValue()) return currentChild;
        }
        return null;
    }

    public Node<Board> selectPV_node(Node<Board> root, int depth){
        Node<Board> currentRoot = root;
        for(int i = 0; i < depth; i++){
            Node<Board> newRoot = selectMove(currentRoot);
            if(newRoot != null) currentRoot = newRoot;
        }
        return currentRoot;
    }

    public void PV_orderTreeLayer(Node<Board> PV_node){
        Node<Board> currentPV_node = PV_node;
        Node<Board> currentParent = PV_node.getParent();
        Node<Board> foundPV_node = null;
        //List<Node<Board>> currentChildren = currentParent.getChildren();

        while(currentParent != null) {
            List<Node<Board>> currentChildren = currentParent.getChildren();
            for (Node<Board> child : currentChildren) {
                if (child == currentPV_node) {
                    foundPV_node = child;
                    //currentChildren.add(0, child);
                }
            }
            currentChildren.remove(foundPV_node);
            currentChildren.add(0, foundPV_node);

            currentPV_node = currentParent;
            currentParent = currentParent.getParent();
        }
    }

    public Node<Board> getRoot() {
        return root;
    }

    public Node<Board> getRootMinimaxed(Board board){
        MiniMaxAlph minimax = new MiniMaxAlph(this.depth, board);
        gameTree = new GameTree(this.depth, board);
        Node<Board> root = gameTree.createTree();

        minimax.miniMaxAB(root, Integer.MIN_VALUE, Integer.MAX_VALUE, board.getCurrentPlayer());

        //Node<Board> moveNode = selectMove(root);

        return root;
    }

    public void MinimaxIterative(Board board, Node<Board> root){
        MiniMaxAlph minimax = new MiniMaxAlph(this.depth, board);
        //Node<Board> root = gameTree.createTree();
        gameTree.addTreeLayer();


        minimax.miniMaxAB(root, Integer.MIN_VALUE, Integer.MAX_VALUE, board.getCurrentPlayer());

        //Node<Board> moveNode = selectMove(root);
    }


    public Node<Board> getBestMoveNodeOrdered(Board board, Node<Board> PV_node){
        MiniMaxAlph minimax = new MiniMaxAlph(this.depth, board);
        GameTree gameTree = new GameTree(this.depth, board, PV_node);
        Node<Board> root = gameTree.createTree();

        minimax.miniMaxAB(root, Integer.MIN_VALUE, Integer.MAX_VALUE, board.getCurrentPlayer());

        return root;
    }

    public int[] getBestMoveFromNode(Node<Board> root){
        int[] bestMove = new int[2];
        try {
            bestMove[0] = selectMove(root).getRow();
            bestMove[1] = selectMove(root).getColumn();
        }
        catch(NullPointerException e) {
            System.out.println("no more moves");
        }
        return bestMove;
    }

    public GameTree getGameTree() {
        return gameTree;
    }
}