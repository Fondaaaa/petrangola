package petrangola;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
    

import petrangola.controller.GameController;

import petrangola.model.Player;

public class mainClass {

    public static void main(String[] args) throws FileNotFoundException {

        File file = new File("simulation.csv");
        PrintWriter pw = new PrintWriter(file);
        pw.println("numPlayers,threshold,winRate,avgKnock");
        for (int numPlayers = 3; numPlayers < 7; numPlayers++) {
            for (int threshold = 3; threshold < 31; threshold++) {
                double success = 0;
                double total = 0;
                double totalKnock = 0;
                double knockedSuccess = 0;
                double pointSum = 0;
               
                while (totalKnock <= 2000) {
                
                GameController controller = new GameController(numPlayers, true);
                Player dealer = controller.getDealer();
                int lives = dealer.getHP();
                boolean hasKnocked = controller.botFirstTurn(threshold);
                if (hasKnocked) {
                totalKnock++;
                pointSum += dealer.getHand().calcPoints();
                }

                while (controller.getRemainingTurns().orElse(1) > 0) {
                controller.botTakeTurn();
                }
                controller.postGameText();
                if (dealer.getHP() == lives) {
                success++;
                if (hasKnocked)
                knockedSuccess++;
                }

                total++;
                

                }
         
                double winRatio = +((success) / total) * 100;
                double knockWinRatio = 100.0 * knockedSuccess / totalKnock;
                System.out.println("giocatori: " + numPlayers + " threshold: " + threshold + " lossRatio: " + winRatio
                        + " knockWinRatio: " + knockWinRatio +
                        " avgKnockPoints: " + pointSum / totalKnock);
                pw.println(numPlayers + "," + threshold + "," + knockWinRatio + "," + pointSum / totalKnock);
            }

        }
        pw.close();

    }

    public static void main2(String[] args) throws FileNotFoundException {
        File file = new File("simulation.csv");
        PrintWriter pw = new PrintWriter(file);
        pw.println("numPlayers,threshold,winRate");

        for (int numPlayers = 2; numPlayers < 7; numPlayers++) {
            for (int threshold = 3; threshold < 27; threshold++) {

                int totalSamples = 0;
                int totalWins = 0;

                while (totalSamples < 2000) {
                    GameController controller = new GameController(numPlayers, true);
                    Player dealer = controller.getDealer();
                    int startingPoints = dealer.getHand().calcPoints();
                    int initialHP = dealer.getHP();

                    if (startingPoints != threshold)
                        continue;

                    controller.botFirstTurn(60);

                    totalSamples++;

                    while (controller.getRemainingTurns().orElse(1) > 0) {
                        controller.botTakeTurn();
                    }

                    controller.postGameText();

                    if (dealer.getHP() == initialHP) {
                        totalWins++;
                    }

                }

                double winRate = 100.0 * totalWins / totalSamples;
                System.out.printf("Players: %d, Threshold: %d, WinRate: %.2f%%\n",
                        numPlayers, threshold, winRate);
                pw.printf("%d,%d,%.2f\n", numPlayers, threshold, winRate);
            }
        }

        pw.close();
    }
}
