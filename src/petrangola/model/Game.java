package petrangola.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Game {

    public static final int N_MAX_PLAYERS = 11;
    private List<Player> players;
    private Player human;
    private Deck deck;
    private Hand field;
    private int numPlayers;

    public Game(int numPlayers) {
        
        players = new ArrayList<Player>();
        human = new Player();
        players.add(human);
        for (int i = 0; i < numPlayers - 1; i++) {
            players.add(new Bot(i + 1));
        }
        Utils.shuffle(players);
        initRound();

    }

    public Game(int numPlayers, boolean bool) {
        players = new ArrayList<Player>();
        for (int i = 0; i < numPlayers; i++) {
            players.add(new Bot(i + 1));
        }
        Utils.shuffle(players);
        initRound();

    }


    public int numPlayers() {
        return players.size();
    }

    public Hand getField() {
        return this.field;
    }

    public void initRound() {
        this.deck = new Deck();
        for (Player player : players) {
            player.setHand(deck.drawHand());
        }
        field = deck.drawHand();

    }

    public List<Player> getPlayers() {
        return players;
    }

    public Player human() {
        return human;
    }
 
    public void setField(Hand hand) {
        this.field = hand;
    }

    public boolean isFirstRound() {
        int lives = 0;
        for(Player player : players) {
            player.getHP();
        }

        return lives != Player.STARTING_HP*numPlayers;
    }

}
