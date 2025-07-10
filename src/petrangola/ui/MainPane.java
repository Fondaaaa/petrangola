package petrangola.ui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.StringTokenizer;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.IndexRange;
import javafx.scene.control.MenuButton;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import petrangola.controller.GameController;
import petrangola.model.*;

import java.awt.Dimension;
import java.awt.MenuBar;
import java.awt.Toolkit;
import java.lang.reflect.Field;

public class MainPane extends BorderPane {
	private SceneController sceneController;
	private GameController controller;
	private GridPane lowerButtons;
	private List<CardButton> fieldButtons;
	private List<CardButton> handButtons;
	private TextArea log;
	private Button swapButton;
	private Button endTurnButton;
	private Button knockButton;
	private Button nextRoundButton;
	private Hand initialHand;
	private Hand initialField;

	public MainPane(SceneController sceneController, int numPlayers) {
		this.sceneController = sceneController;
		this.controller = new GameController(numPlayers);

		initPane();
		newRound();
	}

	public void initPane() {
		initCentralGrid();
		initLowerBox();
		initTopBox();

		setPadding(new Insets(10));

	}

	public void initCentralGrid() {
		GridPane centerGrid = new GridPane(2, 3);
		{
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
			centerGrid.setMinSize(0, 0);
			setCenter(centerGrid);

		}
	}

	public void initLowerBox() {
		lowerButtons = new GridPane(0, 2);
		{
			swapButton = new Button("Scambia");
			swapButton.setOnAction(this::handleSwap);

			endTurnButton = new Button("Conferma");
			endTurnButton.setOnAction(this::handleEndTurn);

			knockButton = new Button("Bussa!");
			knockButton.setOnAction(this::handleKnock);

			nextRoundButton = new Button("Prossimo Round");
			nextRoundButton.setOnAction(this::handleNextRound);

			lowerButtons.addRow(0, swapButton, endTurnButton, knockButton);
			lowerButtons.setAlignment(Pos.CENTER);
		}

		setBottom(lowerButtons);
	}

	public void initTopBox() {

		VBox topBox = new VBox();
		{
			Button startMenuButton = new Button("Torna al menù");
			startMenuButton.setOnAction(this::handleStartMenu);

			log = new TextArea("Partita iniziata!\nNumero giocatori: " + controller.players().size() + "\n"
					+ controller.players() + "\n");

			topBox.getChildren().addAll(startMenuButton, log);
		}

		setTop(topBox);
	}

	public void newRound() {
		controller.newRound();

		log.appendText("inizia " + controller.getCurrentPlayer() + "\n");

		setInitialHandField();

		for (int i = 0; i < Hand.HAND_SIZE; i++) {
			Card card = controller.getField().get(i);
			fieldButtons.get(i).setCard(card);

			card = controller.getHand().get(i);
			handButtons.get(i).setCard(card);
		}
		setDisableButtons(false);

		if (controller.getCurrentPlayer() instanceof Bot)
			handleBotTurns();
	}

	public void setInitialHandField() {

		List<Card> handCards = controller.getHand().getCards().stream().collect(Collectors.toList());
		initialHand = new Hand(handCards);
		List<Card> fieldCards = controller.getField().getCards().stream().collect(Collectors.toList());
		initialField = new Hand(fieldCards);
	}

	public void handleSwap(ActionEvent event) {
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

		log.appendText("Hai scambiato:\n");

		// Scambia le carte
		for (int j = 0; j < selectedMano.size(); j++) {
			int iMano = selectedMano.get(j);
			int iCampo = selectedCampo.get(j);

			Card newField = controller.getHand().get(iMano);
			Card newHand = controller.getField().get(iCampo);

			log.appendText(newField + "con" + newHand + "\n");

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
		if (!initialHand.equals(controller.getHand())) {
			invalidKnockAlert();
			return;
		}
		log.appendText("Hai bussato!\n");
		controller.knock();
		log.appendText("Turni rimanenti: " + controller.getRemainingTurns().get() + "\n");
		handleBotTurns();

	}

	public void invalidKnockAlert() {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setContentText("Hai già scambiato! Vuoi annulare gli scambi?");
		Optional<ButtonType> result = alert.showAndWait();
		if (result.isPresent() && result.get() == ButtonType.CANCEL) {
			alert.close();
		} else if (result.isPresent() && result.get() == ButtonType.OK) {
			controller.getField().setAll(initialField.getCards());
			controller.getHand().setAll(initialHand.getCards());
			for (int i = 0; i < Hand.HAND_SIZE; i++) {
				fieldButtons.get(i).setCard(initialField.get(i));
				handButtons.get(i).setCard(initialHand.get(i));
			}
			deleteSwapFromLog();
			alert.close();
		}

	}

	public void deleteSwapFromLog() {
		var lines = Arrays.asList(log.getText().split("\n")).stream().collect(Collectors.toList());

		String lastLine = lines.get(lines.size() - 1);
		while (!lastLine.equals("Hai scambiato:")) {
			lines.remove(lines.size() - 1);
			lastLine = lines.get(lines.size() - 1);

		}
		lines.remove(lines.size() - 1);
		log.clear();
		log.appendText(String.join("\n", lines) + "\n");

	}

	public void handleNextRound(ActionEvent event) {

		newRound();
		lowerButtons.getChildren().clear();
		initLowerBox();
		handleBotTurns();
	}

	public void handleStartMenu(ActionEvent event) {
		sceneController.goToMenu();
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
			setInitialHandField();
			setDisableButtons(false);
			return;

		}
		setDisableButtons(true);

		PauseTransition pause = new PauseTransition(Duration.seconds(1));
		pause.setOnFinished(e -> {
			Player bot = controller.getCurrentPlayer();

			if (!(bot instanceof Bot)) {
				setInitialHandField();
				setDisableButtons(false);
				return;

			}

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
			updateField();

			// Prossimo turno bot
			handleBotTurns();
		});

		pause.play();
	}

	public void handlePostGame() {

		log.appendText(controller.pointsText());
		log.appendText(controller.postGameText());

		lowerButtons.getChildren().clear();
		if (controller.players().size() > 1) {
			lowerButtons.getChildren().add(nextRoundButton);
		} else {
			Button mainMenuButton = new Button("Torna al menu principale");
			mainMenuButton.setOnAction(this::handleStartMenu);
			lowerButtons.getChildren().add(mainMenuButton);
		}

	}

	public void updateField() {
		for (int i = 0; i < Hand.HAND_SIZE; i++) {
			Card card = controller.getField().get(i);
			fieldButtons.get(i).setCard(card);
		}
	}

}
