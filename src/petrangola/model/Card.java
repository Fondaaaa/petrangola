package petrangola.model;
import java.net.URL;
import java.util.Objects;

import javafx.scene.image.Image;

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

    public URL getImageURL() {
        String path = "/resources/images/" + seed.toString() + (rank.ordinal() + 1) + ".png";
        return getClass().getResource(path.toLowerCase());
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof Card)) {
            return false;
        }
        Card card = (Card) o;
        return Objects.equals(rank, card.rank) && Objects.equals(seed, card.seed);
    }

    @Override
    public int hashCode() {
        return Objects.hash(rank, seed);
    }
    

    
}