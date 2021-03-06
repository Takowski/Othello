package AI.Tests;

import AI.*;
import Core.Board;

public class NegaScoutTest {

    final static int DEPTH = 5;
    final static int GAMES = 1000;
    final static int SIZE  = 8;

    public static void main(String[] args) {
        test1();
        test2();
    }

    //value test
    private static void test1() {
        Board board = new Board();
        NegaScout ns = new NegaScout(DEPTH, board);
        GameTree gameTree = new GameTree(DEPTH);
        Node<Board> root = gameTree.createTree();
        System.out.println("value: " + ns.NegaSAlg(root,Integer.MIN_VALUE, Integer.MAX_VALUE, board.getCurrentPlayer()));
    }

    // generic test
    private static void test2(){

        GameTree gameTree = new GameTree(DEPTH);
        Node<Board> root = gameTree.createTree();

        Stupid s = new Stupid();
        Board board = new Board();
        NS_moveOrdDepth ns = new NS_moveOrdDepth(DEPTH);

        GenericTest.test(s, ns, GAMES, SIZE);
        int negascoutWins = GenericTest.getPlayer2Wins();
        int stupidWins = GenericTest.getPlayer1Wins();
        System.out.println("Negascout wins: " + negascoutWins);
        System.out.println("Stupid wins: " + stupidWins);

    }
}
