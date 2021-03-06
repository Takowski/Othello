package AI.Tests.MCTS_GA;

import AI.MCTS;
import AI.MCTS_TreeReuse;
import AI.Stupid;
import AI.Tests.GenericTest;

public class MCTSTest {

    public static void main(String[] args) {
        int games = 1000;
        int totalSims1 = 20;
        int totalSims2 = 1;
        long timeInMs = 10;
        int size = 8;

        MCTS mcts1 = new MCTS(timeInMs, 0.76);
        MCTS_TreeReuse mcts2 = new MCTS_TreeReuse(timeInMs, 0.76);
        //MCTS mcts2 = new MCTS(timeInMs, 0.76);

        long startTime = System.nanoTime();

        GenericTest.test(mcts1, new Stupid(), games/2, size);
        int mcts1Wins = GenericTest.getPlayer1Wins();
        int mcts2Wins = GenericTest.getPlayer2Wins();

        System.out.println("MCTS 1 wins: " + GenericTest.getPlayer1Wins());
        System.out.println("MCTS 2 wins: " + GenericTest.getPlayer2Wins());
        System.out.println("MCTS draws: " + (games/2-GenericTest.getPlayer1Wins()-GenericTest.getPlayer2Wins()));
        System.out.println("halfway thru");

        GenericTest.test(new Stupid(), mcts1, games/2, size);
        mcts1Wins += GenericTest.getPlayer2Wins();
        mcts2Wins += GenericTest.getPlayer1Wins();

        System.out.println("MCTS 1 wins: " + GenericTest.getPlayer2Wins());
        System.out.println("MCTS 2 wins: " + GenericTest.getPlayer1Wins());
        System.out.println("MCTS draws: " + (games/2-GenericTest.getPlayer1Wins()-GenericTest.getPlayer2Wins()));

        long endTime = System.nanoTime();
        System.out.println(endTime-startTime);
        System.out.println("mcts1 totalsims: " + totalSims1);
        System.out.println("exploration: " + mcts1.getExplorationParameter());
        System.out.println("totalGames: " + games);
        System.out.println("MCTS 1 win%: " + (double)mcts1Wins/games);
        System.out.println("MCTS(reuse) 2 win%: " + (double)mcts2Wins/games);
        System.out.println("MCTS draw%: " + (double)(games-mcts1Wins-mcts2Wins)/games);

//        System.out.println("MCTS 1 wins: " + mcts1Wins);
//        System.out.println("MCTS 2 wins: " + mcts2Wins);
//        System.out.println("MCTS draws: " + (games-mcts1Wins-mcts2Wins));
    }
}