package petrangola.model;


public class Player {
    
    public static final int STARTING_HP = 3;
    private Hand hand;
    private int hp;

    public Player() {
        this.hp = STARTING_HP;
    }

    public void setHand(Hand hand) {
        this.hand = hand;
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
    
    public int getHP() {
    	return hp;
    }

    @Override
   public String toString() {
      return "Tu";
   }

    

    

}
