package petrangola.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.util.Duration;
import petrangola.controller.GameController;
import petrangola.model.*;

import java.awt.Dimension;
import java.awt.Toolkit;;

public class MainPane extends BorderPane {
	private GameController controller;
	private GridPane centerGrid;
	private GridPane lowerButtons;
	private List<CardButton> fieldButtons;
	private List<CardButton> handButtons;
	private TextArea log;
	private Button swapButton;
	private Button endTurnButton;
	private Button knockButton;
	private Hand initialHand;

	public MainPane(GameController controller) {
		this.controller = controller;

		initPane();
		newGame();
	}

	public void initPane() {
		initCentralGrid();
		initLowerBox();

		setTop(log);
		centerGrid.setMinSize(0, 0);
		setCenter(centerGrid);

		// Use preferred height as minimum for top and bottom

		if (controller.getCurrentPlayer() instanceof Bot)
			handleBotTurns();

	}

	public void initCentralGrid() {
		centerGrid = new GridPane(2, 3);
		{

			log = new TextArea("Partita iniziata!\nNumero giocatori: " + controller.players().size() + "\n"
					+ controller.players() + "\n");

			this.fieldButtons = new ArrayList<CardButton>();
			this.handButtons = new ArrayList<CardButton>();

			for (int i = 0; i < Hand.HAND_SIZE; i++) {
				// FIELD

				CardButton cardButton = new CardButton("field" + (i + 1));
				fieldButtons.add(cardButton);
				ImageView imgView = cardButton.getImageView();
				centerGrid.add(imgView, i, 0);
				centerGrid.add(cardButton, i, 1);

				// HAND
				cardButton = new CardButton("hand" + (i + 1));
				handButtons.add(cardButton);
				centerGrid.add(cardButton.getImageView(), i, 2);
				centerGrid.add(cardButton, i, 3);

			}
			centerGrid.setAlignment(Pos.CENTER);

		}
	}

	public void initLowerBox() {
		lowerButtons = new GridPane(0, 2);
		{
			swapButton = new Button("Scambia");
			swapButton.setOnAction(this::handleMove);

			endTurnButton = new Button("Conferma");
			endTurnButton.setOnAction(this::handleEndTurn);
			knockButton = new Button("Bussa!");
			knockButton.setOnAction(this::handleKnock);

			lowerButtons.addRow(0, swapButton, endTurnButton, knockButton);
			lowerButtons.setAlignment(Pos.CENTER);
		}

		setBottom(lowerButtons);
	}

	public void newGame() {
		controller.newRound();

		log.appendText("inizia " + controller.getCurrentPlayer() + "\n");

		initialHand = new Hand(controller.getHand().getCards().stream().collect(Collectors.toList()));

		for (int i = 0; i < Hand.HAND_SIZE; i++) {
			Card card = controller.getField().get(i);
			fieldButtons.get(i).setCard(card);

			card = controller.getHand().get(i);
			handButtons.get(i).setCard(card);
		}
	}

	public void handleMove(ActionEvent event) {
		List<Integer> selectedMano = new ArrayList<>();
		List<Integer> selectedCampo = new ArrayList<>();

		for (int i = 0; i < 3; i++) {
			if (handButtons.get(i).isSelected()) {
				selectedMano.add(i);
			}
			if (fieldButtons.get(i).isSelected()) {
				selectedCampo.add(i);
			}
		}

		if (selectedMano.size() != selectedCampo.size()) {
			showWarning("Devi selezionare lo stesso numero di carte da mano e da campo!");
			return;
		}

		if (selectedMano.size() == 0) {
			showWarning("Seleziona almeno una carta!");
		}

		log.appendText("Hai scambiato:" + "\n");

		// Scambia le carte
		for (int j = 0; j < selectedMano.size(); j++) {
			int iMano = selectedMano.get(j);
			int iCampo = selectedCampo.get(j);

			Card newField = controller.getHand().get(iMano);
			Card newHand = controller.getField().get(iCampo);

			log.appendText(newField + "con" + newHand + "\n");
			// controller.getHand().set(newHand, iMano);
			// handButtons.get(iMano).setCard(newHand);
			// controller.getField().set(newField, iCampo);
			// fieldButtons.get(iCampo).setCard(newField);

			controller.swap(iMano, iCampo);
			handButtons.get(iMano).setCard(newHand);
			fieldButtons.get(iCampo).setCard(newField);

		}

		// Opzionale: reset selezioni
		handButtons.forEach(btn -> btn.setSelected(false));
		fieldButtons.forEach(btn -> btn.setSelected(false));

	}

	public void handleEndTurn(ActionEvent event) {
		if (controller.getHand().equals(initialHand)) {
			showWarning("Non hai scambiato carte!");
			return;
		}
		controller.updateRemainingTurns();
		controller.nextPlayer();
		handleBotTurns();

	}

	public void handleKnock(ActionEvent event) {
		log.appendText("Hai bussato!\n");
		controller.knock();
		log.appendText("Turni rimanenti: " + controller.getRemainingTurns().get() + "\n");
		handleBotTurns();
	}

	public void handleNewGame(ActionEvent event) {
		newGame();
		initLowerBox();
		handleBotTurns();
	}

	public void setDisableButtons(boolean bool) {
		knockButton.setDisable(bool);
		endTurnButton.setDisable(bool);
		swapButton.setDisable(bool);
	}

	public void showWarning(String errorText) {
		Alert alert = new Alert(AlertType.WARNING);
		alert.setContentText(errorText);
		Optional<ButtonType> result = alert.showAndWait();
		if (result.isPresent() && result.get() == ButtonType.OK) {
			alert.close();
		}

	}

	private void handleBotTurns() {
		// Stop if partita è finita
		if (controller.getRemainingTurns().orElse(1) == 0) {
			handlePostGame();
			return;
		}

		Player currentPlayer = controller.getCurrentPlayer();

		// Se non è un bot, aspetta l'interazione del giocatore
		if (!(currentPlayer instanceof Bot)) {
			setDisableButtons(false);
			return;

		}
		setDisableButtons(true);

		PauseTransition pause = new PauseTransition(Duration.seconds(1));
		pause.setOnFinished(e -> {
			Player bot = currentPlayer;
			List<Card> swaps = controller.botTakeTurn();

			if (swaps.isEmpty()) {

				log.appendText(bot + " ha bussato!\n");
			} else {
				for (int i = 0; i < swaps.size(); i += 2) {
					log.appendText(bot + " ha scambiato " + swaps.get(i) + " con " + swaps.get(i + 1) + "\n");
				}
			}

			if (controller.getRemainingTurns().isPresent()) {
				log.appendText("Turni Rimanenti:" + controller.getRemainingTurns().get() + "\n");
			}

			// Aggiorna UI delle carte (opzionale se hai modificato carte)
			updateCardButtons();

			// Prossimo turno bot
			handleBotTurns();
		});

		pause.play();
	}

	public void handlePostGame() {
		List<Player> players = controller.players();
		log.appendText("\nPunti:");
		for (Player player : players) {
			log.appendText(player + ": " + player.getHand().calcPoints()+ "\nmano : " + player.getHand() + "\n");
		}
		log.appendText("\n");
		handleLives();
		
		if(players.size()==1)
			log.appendText(players.get(0) +" HA VINTO!" +"\n");
		else {
			newGame();
		}

	}

	public void handleLives() {
		log.appendText("Vite rimanenti: " + "\n");
		for (Player player : controller.players()) {
			int hp = player.getHP();
			if (controller.getLosers().contains(player)) {
				
				if (hp > 1) {
					
					log.appendText(player + ": " + hp + " -> " + (hp - 1) +"\n");
					player.loseHealth();
				} else if (hp == 0) {
					log.appendText(player + " è stato eliminato\n");
					controller.players().remove(player);
				}
			} else {
				log.appendText(player + ": "+ hp +" -> " +hp +"\n");

		}

	}

	}

	public void updateCardButtons() {
		for (int i = 0; i < Hand.HAND_SIZE; i++) {
			Card card = controller.getField().get(i);
			fieldButtons.get(i).setCard(card);
		}
	}

}
