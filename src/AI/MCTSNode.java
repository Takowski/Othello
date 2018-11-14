package AI;

import Core.Board;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Integer.MIN_VALUE;

public class MCTSNode extends Node {

    private int scoreTotal;
    private int simulations;
    private int x;
    private int y;
    private Board board;
    public MCTSNode(Board board, int x, int y) {
        super(board);
        this.board = board;
        this.x = x;
        this.y = y;
    }

    public MCTSNode(Board board) {
        this(board, MIN_VALUE, MIN_VALUE);
    }

    public Board getBoard() {
        return board;
    }

    public int getSimulations() {
        return simulations;
    }

    public void setSimulations(int simulations) {
        this.simulations = simulations;
    }

    public int getScoreTotal() {
        return scoreTotal;
    }

    public void setScoreTotal(int scoreTotal) {
        this.scoreTotal = scoreTotal;
    }

    public List<MCTSNode> getChildren(Node root) {
        List<MCTSNode> mctsNodeList = new ArrayList();
        for(Object child : root.getChildren()) {
            mctsNodeList.add((MCTSNode)child);
        }
        return mctsNodeList;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }
}