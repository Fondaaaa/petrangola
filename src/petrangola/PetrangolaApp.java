package petrangola;

import javafx.application.Application;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import petrangola.controller.GameController;
import petrangola.ui.MainPane;
import petrangola.ui.SceneController;

public class PetrangolaApp extends Application {

    @Override
    public void init() {

    }

    @Override
    public void start(Stage stage) {

        new SceneController(stage);


    }

    public static void main(String[] args) {
        launch(args);
    }
}
