package petrangola.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.StringJoiner;
import java.util.stream.Collectors;

import petrangola.model.Bot;
import petrangola.model.Card;
import petrangola.model.Game;
import petrangola.model.Hand;
import petrangola.model.Player;

public class GameController {
    private Game game;
    private Optional<Integer> remainingTurns;
    private Player currentPlayer;
    private Player dealer;

    public GameController(int numPlayers) {
        this.game = new Game(numPlayers);
        remainingTurns = Optional.empty();
        currentPlayer = game.getPlayers().get(0);

    }

    public GameController(int numPlayers, boolean bool) {
        this.game = new Game(numPlayers, bool);
        remainingTurns = Optional.empty();
        currentPlayer = game.getPlayers().get(0);
        dealer = game.getPlayers().get(players().size() - 1);
    }

    public void newRound() {
        game.initRound();
        remainingTurns = Optional.empty();

    }

    public List<Player> players() {
        return game.getPlayers();
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public Player getDealer() {
        return dealer;
    }

    public void nextPlayer() {
        int n = game.getPlayers().indexOf(currentPlayer);
        if (n < game.getPlayers().size() - 1) {
            currentPlayer = game.getPlayers().get(n + 1);

        } else {
            currentPlayer = game.getPlayers().get(0);
        }

    }

    public void nextDealer() {
        int n = game.getPlayers().indexOf(dealer);
        if (n < game.getPlayers().size() - 1) {
            dealer = game.getPlayers().get(n + 1);

        } else {
            dealer = game.getPlayers().get(0);
        }

    }

    public Hand getField() {
        return game.getField();
    }

    public Hand getHand() {
        return game.human().getHand();
    }

    public void swap(int handPos, int fieldPos) {
        game.human().getHand().swap(game.getField(), handPos, fieldPos);
    }

    public void knock() {
        if (remainingTurns.isEmpty()) {
            remainingTurns = Optional.of(players().size() - 1);
        } else
            updateRemainingTurns();

        nextPlayer();
    }

    public List<Card> botTakeTurn() {

        List<Card> swaps = new ArrayList<Card>();
        swaps = ((Bot) currentPlayer).takeTurn(game.getField());
        if (swaps.isEmpty())
            botKnock();
        else {
            updateRemainingTurns();
            nextPlayer();
        }

        
        return swaps;

    }

    private void botKnock() {
        boolean alreadyKnocked = remainingTurns.isPresent();
        if (!alreadyKnocked) {
            remainingTurns = Optional.of(players().size() - 1); // bussa il bot
        } else {
            updateRemainingTurns();
        }
        nextPlayer();
    }

    private void setCurrentPlayer(Player player) {
        this.currentPlayer = player;
    }

    public boolean botFirstTurn(int threshold) {
        Bot dealBot = (Bot) dealer;
        // punteggio ottimale da tenere
        boolean knock = dealBot.knockFirstTurn(threshold);

        if (knock) {
            setCurrentPlayer(dealBot);
            botKnock();
        } else {
            Hand temp = game.getField();
            game.setField(dealBot.getHand());
            dealBot.setHand(temp);
        }
        return knock;
    }

    public void updateRemainingTurns() {
        if (remainingTurns.isPresent()) {
            int temp = remainingTurns.get();
            remainingTurns = Optional.of(temp - 1);
        }
    }

    public Optional<Integer> getRemainingTurns() {
        return remainingTurns;
    }

    public Map<Player, Integer> points() {
        Map<Player, Integer> pointsMap = new HashMap<>();
        for (Player player : game.getPlayers()) {
            pointsMap.put(player, player.getHand().calcPoints());
        }

        return pointsMap;
    }

    public List<Player> getLosers() {
        List<Player> losers = new ArrayList<Player>();
        Map<Player, Integer> pointsMap = points();
        int min = pointsMap.values().stream().min(Integer::compareTo).get();

        losers = players().stream().filter(player -> pointsMap.get(player) == min).collect(Collectors.toList());
        if (losers.size() == players().size())
            losers = new ArrayList<Player>();
        return losers;
    }

    public String postGameText() {
        List<Player> losers = getLosers();
        List<Player> dead = new ArrayList<Player>();
        StringBuilder sb = new StringBuilder("Vite Rimanenti:\n");
        for (Player player : players()) {
            int oldHp = player.getHP();
            if (losers.contains(player)) {
                player.loseHealth();
            }
            if (player.getHand().isGulon())
                player.gainHealth();
            sb.append(player + " : " + (oldHp) + " -> " + player.getHP() + "\n");

            if (player.getHP() == 0) {
                sb.append("ELIMINATO!\n");
                dead.add(player);
            }

        }

        if(losers.isEmpty())
            sb.append("Pareggio! Nessuno ha perso una vita\n");
        else {
            for(Player player : dead)
                players().remove(player);
        }
        if (players().size() == 1)
            sb.append(players().get(0) + " ha vinto!");

        return sb.toString();
    }

    public String pointsText() {
        
		List<Player> players = players();
		StringJoiner sj = new StringJoiner("\n","\n","\n");
       
        
		for (Player player : players) {
             sj.add("Punti:" + player.getHand().calcPoints()); 
			Hand hand = player.getHand();
            sj.add(player + ": " + hand);
            if (hand.isGulon()) 
                sj.add(player + "ha fatto Gulòn! Ha guadagnato una vita!");
		}

        return sj.toString();
	}
   

}
