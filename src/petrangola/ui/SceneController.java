package petrangola.ui;

import java.util.HashMap;
import java.util.Map;

import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class SceneController {

    private Scene scene;
    private Stage stage;
    private MenuPane menuPane;
    private MainPane mainPane;

    public SceneController(Stage stage) {

        this.menuPane = new MenuPane(this);

        scene = new Scene(menuPane);

        this.stage = stage;

        stage.setScene(scene);
        
        stage.show();
    }

    public void startGame(int numPlayers) {
        this.mainPane = new MainPane(this, numPlayers);
        scene.setRoot(mainPane);
        fitScreen(0.6);
    }


    public void goToMenu() {
        scene.setRoot(menuPane);
        stage.sizeToScene();
    }

    public void fitScreen(double ratio) {
        Screen screen = Screen.getPrimary();
        Rectangle2D bounds = screen.getVisualBounds(); // Usable screen area (excluding taskbar, etc.)

        double screenWidth = bounds.getWidth();
        double screenHeight = bounds.getHeight();

        stage.setHeight(screenHeight*ratio);
        stage.setWidth(screenWidth*ratio/2);
       
    }

}
