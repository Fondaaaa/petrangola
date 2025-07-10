package test.controller;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import petrangola.controller.GameController;
import petrangola.model.Bot;
import petrangola.model.Card;
import petrangola.model.Game;
import petrangola.model.Hand;
import petrangola.model.Player;
import petrangola.model.Rank;
import petrangola.model.Seed;

public class GameControllerTest {

    private GameController gameController;
    private Bot bot1;
    private Bot bot2;

    @BeforeEach
    public void createGame() {
        gameController = new GameController(2, true);

        gameController.newRound();

        bot1 = (Bot) gameController.players().get(0);
        bot2 = (Bot) gameController.players().get(1);
    }

    @Test
    public void testRemainingTurnsGame() {
        

        while (gameController.getRemainingTurns().orElse(1) > 0) {

            int remainingTurns = gameController.getRemainingTurns().orElse(-1);
            List<Card> swaps = gameController.botTakeTurn();
            boolean knock = swaps.isEmpty();
            if (knock && remainingTurns == -1) {
                int numPlayers = gameController.players().size();
                assertEquals( numPlayers - 1, gameController.getRemainingTurns().get());
            } else if (remainingTurns > 0) {
                assertEquals(remainingTurns - 1, gameController.getRemainingTurns().get());
            } else {
                assertEquals(remainingTurns, -1);
            }

        }

    }
    
    @Test
    public void testPostGame() {

        while(gameController.getRemainingTurns().orElse(-1) > 0) {
            gameController.botTakeTurn();
        }

        String postGameText = gameController.postGameText();
       
        var losers = new ArrayList<Bot>();
        
        if (bot1.getHand().calcPoints() < bot2.getHand().calcPoints())
            losers.add(bot1);
        else if (bot2.getHand().calcPoints() < bot1.getHand().calcPoints())
           losers.add(bot2);
        
        
        assertEquals(gameController.getLosers(),losers);

       
        

        
    }

}