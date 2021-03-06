package AI.Tests;

import AI.*;
import Core.Board;

import java.util.Random;

public class NSvsRandom {

    final static long TIME = 10;
    final static int DEPTH = 7;
    final static int GAMES = 1000;
    final static int SIZE  = 8;

    public static void main(String[] args) {
        //test1();
        test2();
    }

    //value test
    private static void test1() {
        Board board = new Board();
        NS_moveOrdering ns = new NS_moveOrdering(TIME);
        GameTree gameTree = new GameTree(DEPTH);
        Node<Board> root = gameTree.createTree();
        //System.out.println("value: " + ns.NegaSAlg(root,Integer.MIN_VALUE, Integer.MAX_VALUE, board.getCurrentPlayer()));
    }

    // generic test
    private static void test2(){

        GameTree gameTree = new GameTree(DEPTH);
        Node<Board> root = gameTree.createTree();

        Stupid r = new Stupid();
        Board board = new Board();
        NS_moveOrdering ns = new NS_moveOrdering(TIME);

        GenericTest.test(r, ns, GAMES, SIZE);
        int negascoutWins = GenericTest.getPlayer2Wins();
        int randomWins = GenericTest.getPlayer1Wins();
        System.out.println("Negascout wins: " + negascoutWins);
        System.out.println("Random wins: " + randomWins);

    }
}
