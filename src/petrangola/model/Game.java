package petrangola.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Game {

    public static final int N_MAX_PLAYERS = 12;
    private List<Player> players;
    private Player human;
    private Deck deck;
    private Hand field;

    public Game(int numPlayers) {
        this.deck = new Deck();
        players = new ArrayList<Player>();
        human = new Player();
        players.add(human);
        for (int i = 0; i < numPlayers - 1; i++) {
            players.add(new Bot(i + 1));
        }

        initRound();

    }

    public int numPlayers() {
        return players.size();
    }

    public Hand getField() {
        return this.field;
    }

    public void initRound() {
        Utils.shuffle(players);
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

}
