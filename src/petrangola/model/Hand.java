package petrangola.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class Hand {
    public final static int HAND_SIZE = 3;
    private List<Card> cards;

    public Hand() {
        this.cards = new ArrayList<Card>();

    }

    public static Hand of(Card card1, Card card2, Card card3) {
        Hand hand = new Hand();
        hand.setCards(new ArrayList<>(List.of(card1, card2, card3)));

        return hand;
    }

    public void setCards(List<Card> cards) {
        if (cards.size() != HAND_SIZE) {
            throw new IllegalArgumentException("Hand must contain exactly " + HAND_SIZE + " cards.");
        }
        this.cards = cards;
    }

    public List<Card> getCards() {
        return cards;
    }

    public void swap(Hand field, List<Integer> handPos, List<Integer> fieldPos) {

        List<Card> fieldCards = field.getCards();
        var newHand = new ArrayList<Card>(cards);
        var newField = new ArrayList<Card>(field.getCards());
        for (int i = 0; i < 3; i++) {
            if (i < handPos.size()) {
                if (handPos.get(i) == i) {
                    Card temp = cards.get(i);
                    newHand.set(i, fieldCards.get(fieldPos.get(i)));
                    newField.set(fieldPos.get(i), temp);
                }
            }

        }
        field.setCards(newField);
        cards = newHand;
    }

    public int calcPoints() {
        int total = 0;
        if (isTris())
            total = 32 + cards.get(0).getRank().ordinal();

        if (isScala()) {
            cards.sort(null);
            if (containsAsso() && cards.get(0).getRank().equals(Rank.DUE))
                total = 42;
            else
                total = 43 + cards.get(0).getRank().ordinal();
        } else {
            for (Card c : cards) {
                if (seedToCount().isPresent()) {
                    if (c.getSeed().equals(seedToCount().get()))
                        total += c.points();
                } else if (total < c.points())
                    total = c.points();
            }
        }
            

        return total;
    }

    public Optional<Seed> seedToCount() {
        Optional<Seed> maxSeed = Optional.empty();
        var seedcountmap = cardsBySeed();
        for (Seed s : seedcountmap.keySet()) {
            if (seedcountmap.get(s) > 1)
                maxSeed = Optional.of(s);
        }

        return maxSeed;
    }

    public boolean isTris() {
        return cards.get(0).getRank().equals(cards.get(1).getRank()) &&
                cards.get(1).getRank().equals(cards.get(2).getRank());
    }

    public boolean isScala() {
        if (!allSameSeed())
            return false;
        sort();

        List<Card> temp = new ArrayList<>(List.copyOf(cards));
        boolean result = false;
        if (containsAsso()) {

            if ((temp.get(0).isRank(Rank.ASSO) || temp.get(2).isRank(Rank.ASSO)) &&
                    temp.get(0).getRank().ordinal() - cards.get(1).getRank().ordinal() == -1)
                result = true;
        } else if (cards.get(0).getRank().ordinal() - cards.get(2).getRank().ordinal() == -2) {
            result = true;
        }

        return result;

    }

    public boolean containsAsso() {
        boolean containsAsso = false;
        for (int i = 0; i < HAND_SIZE && !containsAsso; i++) {
            if (cards.get(i).isRank(Rank.ASSO))
                containsAsso = true;
        }

        return containsAsso;
    }

    public boolean allSameSeed() {
        return cards.get(0).getSeed().equals(cards.get(1).getSeed())
                && cards.get(1).getSeed().equals(cards.get(2).getSeed());
    }

    public void sort() {
        cards.sort(null);
    }

    public Map<Seed, Integer> cardsBySeed() {
        var map = new HashMap<Seed, Integer>();

        for (Seed seed : Seed.values()) {
            int n = 0;
            for (Card c : cards) {
                if (c.isSeed(seed))
                    n++;
            }
            map.put(seed, n);
        }

        return map;
    }

}
