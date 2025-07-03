package petrangola.controller;

import java.util.List;
import java.util.Optional;

import petrangola.model.Hand;
import petrangola.model.Player;

public class PlayerController {

    private GameController gameController;

    public PlayerController(GameController gameController) {
        this.gameController = gameController;
    }

    // public boolean takeTurn(Player player, List<Integer> handPos, List<Integer> fieldPos) {
    
    //     boolean bussa = false;
    //     if(!handPos.isEmpty()) {
    //         player.swap(, handPos, fieldPos);;
    //     }
    // }
}