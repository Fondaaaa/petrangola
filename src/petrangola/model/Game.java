package petrangola.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Game {
    
    public static final int N_MAX_PLAYERS = 12;
    private List<Player> players;
    private Deck deck;
    private Hand field;
    private int currentPlayer;
    private Map<Player,Integer> lives;
    
    public Game(int numPlayers) {
        this.deck = new Deck();
        players = new ArrayList<Player>();
        players.add(new Player());
        for(int i=0; i<numPlayers - 1; i++) {
            players.add(new Bot());
        }

        lives = new HashMap<Player,Integer>();
        for(Player p : players) {
            lives.put(p,Player.STARTING_HP);
        }

        field = new Hand();
        initRound();

        
    }

    public int numPlayers() {
        return players.size();
    }

    public Hand getField()  {
        return this.field;
    }

    public void initRound() {
        players.forEach(p -> p.setHand(deck.drawHand()));
        field = deck.drawHand();

    }
 
   
    
    
 
    
    
    
    
}
