package petrangola.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import petrangola.model.Bot;
import petrangola.model.Card;
import petrangola.model.Game;
import petrangola.model.Hand;
import petrangola.model.Player;

public class GameController {
    private Game game;
    private Optional<Integer> remainingTurns;
    private Player currentPlayer;

    public GameController(int numPlayers) {
        this.game = new Game(numPlayers);
        remainingTurns = Optional.empty();
        currentPlayer = game.getPlayers().get(0);
    }

    public void newRound() {
        game.initRound();
    }

    public List<Player> players() {
        return game.getPlayers();
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public void nextPlayer() {
        int n = game.getPlayers().indexOf(currentPlayer);
        if (n < game.getPlayers().size() - 1) {
            currentPlayer = game.getPlayers().get(n + 1);

        } else {
            currentPlayer = game.getPlayers().get(0);
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
        }
        else
            updateRemainingTurns();

        nextPlayer();
    }

    public List<Card> botTakeTurn() {

        List<Card> swaps = new ArrayList<Card>();
        swaps = ((Bot) currentPlayer).takeTurn(game.getField());
        boolean isLastRound = remainingTurns.isPresent();
        if (swaps.isEmpty()) {
            if (!isLastRound) {
                remainingTurns = Optional.of(players().size() - 1); // bussa il bot
            }
            Optional.of(players().size() - 1);
        }

        nextPlayer();
        if (isLastRound) {
            updateRemainingTurns();
        }

        return swaps;

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

    public Map<Player,Integer> points() {
        Map<Player,Integer> pointsMap = new HashMap<>();
        for(Player player : game.getPlayers()) {
            pointsMap.put(player,player.getHand().calcPoints());
        }

        return pointsMap;
    }

}
