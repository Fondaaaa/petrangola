package petrangola.controller;

import petrangola.model.Game;

public class GameController {
    private PlayerController playerController;
    private BotController botController;
    private Game game;

    public GameController(int numPlayers) {
        this.game = new Game(numPlayers);
        this.playerController = new PlayerController(this);
        this.botController = new BotController(this);
    }
    
}
