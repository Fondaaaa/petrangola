package petrangola.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class Deck {

    private final List<Card> allCards;
    private List<Card> currentCards;

    public Deck() {
        this.allCards = new ArrayList<Card>();
        setUp();

    }
    
    private void setUp() {
        
        
        for (int i = 0; i < Rank.values().length; i++) {
            for (int j = 0; j < Seed.values().length; j++) {
                allCards.add(new Card(Seed.values()[j], Rank.values()[i]));
            }
        }

        currentCards = Utils.copy(allCards);
        Utils.shuffle(currentCards);
    }

    public Hand drawHand() {
        List<Card> cards = new ArrayList<Card>();
        for (int i = 0; i < Hand.HAND_SIZE; i++) {
            cards.add(currentCards.remove(0));
        }
        
        Hand hand = new Hand(cards);
        return hand;
    }

    @Override
    public String toString() {
        return "Cards Remaining: " + currentCards.size() + "\n" + currentCards.toString();
    }
}
