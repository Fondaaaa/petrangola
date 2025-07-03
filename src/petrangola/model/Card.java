package petrangola.model;

public class Card implements Comparable<Card>{

    private final Rank rank;
    private final Seed seed;

    public Card(Seed seed, Rank rank) {
        this.rank = rank;
        this.seed = seed;
    }

    public Rank getRank() {
        return rank;
    }

    public Seed getSeed() {
        return seed;
    }

    @Override
    public String toString() {
        return "" + rank + "di" + seed;
    
    }

    public int compareTo(Card c) {
       return this.pos() - c.pos();
    }

    public int pos() {
         return this.getSeed().ordinal()*10 + this.getRank().ordinal();
    }

    public boolean isSeed(Seed seed) {
        return this.seed.equals(seed);
    }   

    public boolean isRank(Rank rank) {
        return this.rank.equals(rank);
    }

    public int points() {
        return rank.getPoints();
    }

    
}