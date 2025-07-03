package ui;

import petrangola.model.Deck;

public class Main {
    
    public static void main(String[] args) {

        int numPlayers = 3;
        int sumScores = 0;
        int numFav = 0;
        int threshold = 10;
        int numRep = 2000;
        for(int i = 0; i < numRep; i++) {
            Deck deck = new Deck();
           // List<Hand> hands = new ArrayList<Hand>();
            for(int j=0; j<numPlayers; j++) {
                int score = deck.drawHand().calcPoints();
                sumScores+=score;
                boolean reached = false;
                if (!reached && score < threshold) {
                    reached = false;
                    numFav++;
                }
                    
            }
         
           
        }

         float percentage = 100*numFav/(numPlayers*numRep);
            System.out.println("numero ripetizioni = " + numPlayers*numRep + "");
            System.out.println("n mani sopra threshold("+ threshold + ") : " + numFav);
            System.out.println("% mani sopra threshold: " + percentage);
    }
}
