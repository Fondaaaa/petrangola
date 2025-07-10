package petrangola.ui;

import java.util.ArrayList;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Menu;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class MenuPane extends BorderPane {

    private SceneController sceneController;
    private ObservableList<String> obsPlayers;

    public MenuPane(SceneController sceneController) {
        this.sceneController = sceneController;
        initPane();
    }

    public void initPane() {
        VBox centerBox = new VBox();
        {
            Label label = new Label("Seleziona giocatori");
            List<String> players = new ArrayList<String>();
            players.add("Player 1");
            players.add("CPU 2");
            obsPlayers = FXCollections.observableArrayList(players);
            ListView<String> playersView = new ListView<String>(obsPlayers);

            centerBox.getChildren().addAll(label, playersView);
        }
        setCenter(centerBox);

        HBox bottomBox = new HBox();
        {
            ObservableList<Integer> obsNumbers = FXCollections
                    .observableArrayList(List.of(2, 3, 4, 5, 6, 7, 8, 9, 10, 11));
            ComboBox<Integer> numbersBox = new ComboBox<>(obsNumbers);
            numbersBox.setValue(obsNumbers.get(0));
            numbersBox.setOnAction(event -> {
                int selected = numbersBox.getValue();

                if (selected > obsPlayers.size()) {
                    for (int i = obsPlayers.size(); i < selected; i++) {
                        obsPlayers.add("CPU" + (i + 1));
                    }

                } else {
                    for (int i = obsPlayers.size() - 1; i >= selected; i--) {
                        obsPlayers.remove(i);
                    }
                }
            });

            Button startButton = new Button("Nuova Partita");
            startButton.setOnAction(this::handleGameStart);

            bottomBox.getChildren().setAll(numbersBox, startButton);

        }

        setBottom(bottomBox);
    }


    private void handleGameStart(ActionEvent event) {
        sceneController.startGame(obsPlayers.size());
    }

}
