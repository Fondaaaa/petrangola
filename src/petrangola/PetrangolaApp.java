package petrangola;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import petrangola.controller.GameController;
import petrangola.ui.MainPane;


public class PetrangolaApp extends Application {

    @Override
    public void init() {

    }

    @Override
    public void start(Stage stage) {
        GameController controller = new GameController(3);

        BorderPane pane = new MainPane(controller);
        Scene scene = new Scene(pane);
        stage.setScene(scene);
        stage.show();

    }
}
