package petrangola.model;

import java.util.List;

public class Player {
    
    public static final int STARTING_HP = 3;
    private Hand hand;
    private int hp;

    public Player() {
        this.hand = new Hand();
        this.hp = STARTING_HP;
    }

    public void setHand(Hand hand) {
        this.hand = hand;
    }

    public void swap(Hand field, List<Integer> handPos, List<Integer> fieldPos) {
        this.hand.swap(field, handPos, fieldPos);
    }

    public Hand getHand() {
        return hand;
    }

    public int gainHealth() {
        hp++;
        return hp;
    }

    public int loseHealth() {
        hp--;
        return hp;
    }

    

    

}
