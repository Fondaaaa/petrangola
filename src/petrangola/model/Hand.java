package petrangola.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.Objects;

public class Hand {
    public final static int HAND_SIZE = 3;
    private List<Card> cards;
    private boolean isGulon;

    public Hand(List<Card> cards) {
        this.cards = cards;

    }

    public static Hand of(Card card1, Card card2, Card card3) {
        return new Hand(List.of(card1, card2, card3).stream().collect(Collectors.toList()));
    }

    public void setAll(List<Card> cards) {
        this.cards = cards.stream().collect(Collectors.toList());
    }

    public void set(Card card, int pos) {
        cards.set(pos, card);
    }

    public List<Card> getCards() {
        return cards;
    }

    public Card get(int pos) {
        return cards.get(pos);
    }

    public void swap(Hand field, int handPos, int fieldPos) {
        Card temp = cards.get(handPos);
        cards.set(handPos, field.get(fieldPos));
        field.set(temp, fieldPos);

    }

    public int calcPoints() {
        int total = 0;
        if (isTris()) {
            total = 31 + cards.get(0).getRank().ordinal();
            if (cards.get(0).getRank().equals(Rank.ASSO))
                total += 10;
        }
        if (isScala()) {
            cards.sort(null);
            if (cards.get(0).isRank(Rank.ASSO) && cards.get(1).isRank(Rank.CAVALLO)) {
                isGulon = true;
                total = 52;
            }

            else
                total = 42 + cards.get(0).getRank().ordinal();
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

    public boolean isGulon() {
        calcPoints();
        return isGulon;
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

        boolean result = cards.get(2).getRank().ordinal() - cards.get(0).getRank().ordinal() == 2;
        if (cards.get(0).isRank(Rank.ASSO) & cards.get(1).isRank(Rank.CAVALLO)) {
            result = cards.get(2).isRank(Rank.RE);
        }

        return result;

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

    public static Hand bestCombinedHand(Hand a, Hand b) {
        int maxPoints = Integer.MIN_VALUE;
        Hand bestHand = null;

        for (int mask = 0; mask < 8; mask++) { // 2^3 combinazioni
            List<Card> combo = new ArrayList<>();

            for (int i = 0; i < Hand.HAND_SIZE; i++) {
                // Se il bit i-esimo è 0, prendi da a; altrimenti da b
                if ((mask & (1 << i)) == 0)
                    combo.add(a.get(i));
                else
                    combo.add(b.get(i));
            }

            Hand h = new Hand(combo);
            int points = h.calcPoints();

            if (points > maxPoints) {
                maxPoints = points;
                bestHand = h;
            }
        }

        return bestHand;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof Hand)) {
            return false;
        }
        Hand hand = (Hand) o;

        return Objects.equals(cards, hand.cards);
    }

    @Override
    public int hashCode() {
        int hashCode = 0;
        for (Card card : cards) {
            hashCode += card.hashCode();
        }

        return hashCode;
    }

    @Override
    public String toString() {
        return cards.get(0) + " , " + cards.get(1) + " , " + cards.get(2);
    }

}
