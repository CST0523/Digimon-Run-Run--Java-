package view;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Comparator;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.scene.control.Label;
import model.InfoLabel;
import model.DIGIMON;
import model.DigimonPicker;
import model.SpaceRunnerButton;
import model.SpaceRunnerSubScene;
import model.Score;





public class ViewManager {

	private static final int HEIGHT = 768;
	private static final int WIDTH = 1024;
	private AnchorPane mainPane;
	private Scene mainScene;
	private Stage mainStage;
	private String playerNickname;  // To store the player's nickname

	private final static int MENU_BUTTON_START_X = 100;
	private final static int MENU_BUTTON_START_Y = 220;

	private SpaceRunnerSubScene helpSubScene;
	private SpaceRunnerSubScene scoreSubScene;
	private SpaceRunnerSubScene digimonChooserSubScene;
	private SpaceRunnerSubScene instructionsSubScene;
	private SpaceRunnerSubScene nicknameSubScene;
	private SpaceRunnerSubScene sceneToHide;

	List<SpaceRunnerButton> menuButtons;
	List<DigimonPicker> digimonsList;
	private DIGIMON choosenDIGIMON;





	public ViewManager() {
		menuButtons = new ArrayList<>();
		mainPane = new AnchorPane();
		mainScene = new Scene(mainPane, WIDTH, HEIGHT );
		mainStage = new Stage();
		mainStage.setScene(mainScene);
		createSubScenes();
		CreateButtons();
		createBackground();
		createLogo();

	}



	private void showSubScene(SpaceRunnerSubScene subScene) {
		if (sceneToHide != null) {
			sceneToHide.moveSubScene();
		}
		subScene.moveSubScene();
		sceneToHide = subScene;
	}


	private void createSubScenes() {

		helpSubScene = new SpaceRunnerSubScene();
		instructionsSubScene = new SpaceRunnerSubScene();
		scoreSubScene = new SpaceRunnerSubScene();
		nicknameSubScene = new SpaceRunnerSubScene(); // Initialize nicknameSubScene
		mainPane.getChildren().addAll(scoreSubScene, nicknameSubScene); // Add to pane
		createInstructionsSubScene();
		createDigimonChooserSubScene();
		createHelpSubScene();
		createScoreSubScene();
		createNicknameSubScene();


	}


	private void createDigimonChooserSubScene() {
		digimonChooserSubScene = new SpaceRunnerSubScene();
		mainPane.getChildren().add(digimonChooserSubScene);

		InfoLabel chooseDigimonLabel = new InfoLabel("CHOOSE YOUR CHARACTER");
		chooseDigimonLabel.setLayoutX(110);
		chooseDigimonLabel.setLayoutY(25);
		digimonChooserSubScene.getPane().getChildren().add(chooseDigimonLabel);
		digimonChooserSubScene.getPane().getChildren().add(createDigimonsToChoose());
		digimonChooserSubScene.getPane().getChildren().add(createButtonToShowNickname()); // Use the button to show nickname input
	}

	private SpaceRunnerButton createButtonToShowNickname() {
		SpaceRunnerButton nicknameButton = new SpaceRunnerButton("NEXT");
		nicknameButton.setLayoutX(350);
		nicknameButton.setLayoutY(300);

		nicknameButton.setOnAction(event -> {
			if (choosenDIGIMON != null) {
				showSubScene(nicknameSubScene);  // Show nickname input
			}
		});

		return nicknameButton;
	}

	private void createScoreSubScene() {
		scoreSubScene = new SpaceRunnerSubScene();
		mainPane.getChildren().add(scoreSubScene);
		InfoLabel score = new InfoLabel("<<<< Scores >>>>");
		score.setLayoutX(115);
		score.setLayoutY(20);
		VBox scoreContainer = new VBox();
		scoreContainer.setLayoutX(150);
		scoreContainer.setLayoutY(150);

		Label scoreHeading = new Label("     Nickname			Score   ");
		scoreHeading.setUnderline(true);
		Label score1 = new Label("chew				40");
		Label score2 = new Label("siau				35");
		Label score3 = new Label("teng				30");
		scoreHeading.setFont(Font.font("Verdana",20));
		score1.setFont(Font.font("Verdana",20));
		score2.setFont(Font.font("Verdana",20));
		score3.setFont(Font.font("Verdana",20));
		scoreContainer.setBackground(new Background(new BackgroundFill(Color.MEDIUMAQUAMARINE, new CornerRadii(20), new Insets(-20,-20,-20,-20))));
		scoreContainer.getChildren().addAll(scoreHeading, score1, score2, score3);

		scoreSubScene.getPane().getChildren().addAll(score, scoreContainer);//, score1, score2, score3);

	}

	private void createHelpSubScene() {
		helpSubScene = new SpaceRunnerSubScene();
		mainPane.getChildren().add(helpSubScene);
		InfoLabel help = new InfoLabel("   <<<< Help >>>>");
		help.setLayoutX(150);
		help.setLayoutY(20);
		GridPane helpGrid = new GridPane();
		helpGrid.setLayoutX(100);
		helpGrid.setLayoutY(100);
		helpGrid.setHgap(20);
		helpGrid.setVgap(20);

		ImageView digimon = new ImageView(new Image(DIGIMON.AGUMON.getUrl(), 40, 40, true, false));
		ImageView meteor1 = new ImageView(), meteor2 = new ImageView();
		ImageView star = new ImageView(new Image(GameViewManager.GOLD_STAR_IMAGE, 40, 40, true, false));
		ImageView life = new ImageView(new Image(DIGIMON.AGUMON.getUrlLife(), 40, 40, true, false));

		meteor1.setImage(new Image(GameViewManager.METEOR_BROWN_IMAGE, 40, 40, true, false));
		meteor2.setImage(new Image(GameViewManager.METEOR_GREY_IMAGE, 40, 40, true, false));

		Label digimonHelp 	 = new Label("This is your digimon. Choose character from the \nmenu. Control it with arrow keys.");
		Label meteorHelp1 = new Label("These are pizza.\nTry to get it to earn points.\nOne pizza is 2 points.");
		Label meteorHelp2 = new Label("These are stone.\nAvoid them.\nIf stone hit digimon will remove digimon life.");
		Label starHelp   = new Label("The burger earn point.\nTry to get it to earn points.\nOne burger is 1 points.");
		Label lifeHelp   = new Label("This is player life.\nDisplay how much chance player can hit the stone\nOne player only have 3 chances.");

		/* gridpane:
		 * ___0_|__1_|__2_|_3_
		 * 0|___|____|____|__
		 * 1|___|____|____|__
		 * 2|___|____|____|__
		 * 3|___|____|____|___
		 */

		helpGrid.add(digimon, 0, 0);
		helpGrid.add(digimonHelp, 1, 0);
		helpGrid.add(meteor1, 0, 1);
		helpGrid.add(meteorHelp1, 1, 1);
		helpGrid.add(star, 0, 2);
		helpGrid.add(starHelp, 1, 2);
		helpGrid.add(meteor2, 0, 3);
		helpGrid.add(meteorHelp2, 1, 3);
		helpGrid.add(life, 0, 4);
		helpGrid.add(lifeHelp, 1, 4);
		helpSubScene.getPane().getChildren().addAll(help, helpGrid);

	}


	private void createInstructionsSubScene() {
		instructionsSubScene = new SpaceRunnerSubScene(); // New Instructions SubScene
		mainPane.getChildren().add(instructionsSubScene);

		InfoLabel instructionsLabel = new InfoLabel("GAME INSTRUCTIONS");
		instructionsLabel.setLayoutX(110);
		instructionsLabel.setLayoutY(25);

		Label instructionsText = new Label("1. Use arrow keys to move your digimon.\n If click space will left or right keys will burst. \n" +
				"2. Avoid stones to hit your digimon.\n" +
				"3. Collect burger and pizza to earn points in 30 second.\n" );
		instructionsText.setLayoutX(120);
		instructionsText.setLayoutY(100);

		SpaceRunnerButton startGameButton = createStartGameButton();

		instructionsSubScene.getPane().getChildren().addAll(instructionsLabel, instructionsText, startGameButton);
	}


	private SpaceRunnerButton createStartGameButton() {
		SpaceRunnerButton startGameButton = new SpaceRunnerButton("START");
		startGameButton.setLayoutX(350);
		startGameButton.setLayoutY(300);

		startGameButton.setOnAction(event -> {
			if (choosenDIGIMON != null) {
				GameViewManager gameManager = new GameViewManager();
				gameManager.setPlayerNickname(playerNickname); // Set the nickname
				gameManager.createNewGame(mainStage, choosenDIGIMON);
			}
		});


		return startGameButton;
	}


	private HBox createDigimonsToChoose() {
		HBox box = new HBox();
		box.setSpacing(60);
		digimonsList = new ArrayList<>();
		for (DIGIMON DIGIMON : DIGIMON.values()) {
			DigimonPicker digimonToPick = new DigimonPicker(DIGIMON);
			digimonsList.add(digimonToPick);
			box.getChildren().add(digimonToPick);
			digimonToPick.setOnMouseClicked(new EventHandler<MouseEvent>() {

				@Override
				public void handle(MouseEvent event) {
					for (DigimonPicker digimon : digimonsList) {
						digimon.setIsCircleChoosen(false);
					}
					digimonToPick.setIsCircleChoosen(true);
					choosenDIGIMON = digimonToPick.getDigimon();

				}
			});
		}

		box.setLayoutX(300 - (118*2));
		box.setLayoutY(100);
		return box;
	}


	private SpaceRunnerButton createButtonToShowInstructions() {
		SpaceRunnerButton instructionsButton = new SpaceRunnerButton("NEXT");
		instructionsButton.setLayoutX(350);
		instructionsButton.setLayoutY(300);

		instructionsButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				if (choosenDIGIMON != null) {
					showSubScene(instructionsSubScene);
				}
			}
		});

		return instructionsButton;
	}


	public Stage getMainStage() {
		return mainStage;
	}

	private void AddMenuButtons(SpaceRunnerButton button) {
		button.setLayoutX(MENU_BUTTON_START_X);
		button.setLayoutY(MENU_BUTTON_START_Y + menuButtons.size() * 100);
		menuButtons.add(button);
		mainPane.getChildren().add(button);
	}



	private void CreateButtons() {
		createStartButton();
		createScoresButton();
		createHelpButton();
		createExitButton();
	}

	private void createStartButton() {
		SpaceRunnerButton startButton = new SpaceRunnerButton("PLAY");
		AddMenuButtons(startButton);

		startButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				showSubScene(digimonChooserSubScene);

			}
		});
	}

	private void createScoresButton() {
		SpaceRunnerButton scoresButton = new SpaceRunnerButton("SCORES");
		AddMenuButtons(scoresButton);

		scoresButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				showSubScene(scoreSubScene);

			}
		});
	}

	private void createHelpButton() {
		SpaceRunnerButton helpButton = new SpaceRunnerButton("HELP");
		AddMenuButtons(helpButton);

		helpButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				showSubScene(helpSubScene);

			}
		});
	}


	private void createExitButton() {
		SpaceRunnerButton exitButton = new SpaceRunnerButton("EXIT");
		AddMenuButtons(exitButton);

		exitButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				mainStage.close();

			}
		});

	}

	private void createBackground() {
		Image backgroundImage = new Image("/resources/background2.gif", 256, 256, false, false);
		BackgroundImage background = new BackgroundImage(backgroundImage, BackgroundRepeat.REPEAT, BackgroundRepeat.REPEAT, BackgroundPosition.DEFAULT, null);
		mainPane.setBackground(new Background(background));
	}

	private void createLogo() {
		ImageView logo = new ImageView("/resources/LOGO.png");
		logo.setLayoutX(500);
		logo.setLayoutY(20);

		logo.setOnMouseEntered(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				logo.setEffect(new DropShadow());

			}
		});

		logo.setOnMouseExited(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				logo.setEffect(null);

			}
		});

		mainPane.getChildren().add(logo);

	}

	private void createNicknameSubScene() {
		nicknameSubScene = new SpaceRunnerSubScene();
		mainPane.getChildren().add(nicknameSubScene);

		InfoLabel nicknameLabel = new InfoLabel("ENTER YOUR NICKNAME");
		nicknameLabel.setLayoutX(110);
		nicknameLabel.setLayoutY(25);

		TextField nicknameField = new TextField();
		nicknameField.setPromptText("Enter nickname");
		nicknameField.setLayoutX(120);
		nicknameField.setLayoutY(100);

		SpaceRunnerButton proceedButton = new SpaceRunnerButton("PROCEED");
		proceedButton.setLayoutX(350);
		proceedButton.setLayoutY(150);

		proceedButton.setOnAction(event -> {
			playerNickname = nicknameField.getText();
			if (playerNickname != null && !playerNickname.trim().isEmpty()) {
				showSubScene(instructionsSubScene);
			} else {
				// Handle empty nickname case if needed
				System.out.println("Nickname cannot be empty.");
			}
		});

		nicknameSubScene.getPane().getChildren().addAll(nicknameLabel, nicknameField, proceedButton);
	}

	public void recordScore(String nickname, int score) {
		try (BufferedWriter writer = new BufferedWriter(new FileWriter("scores.txt", true))) {
			writer.write(nickname + ":" + score);
			writer.newLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private List<Score> readScores() {
		List<Score> scores = new ArrayList<>();
		try (BufferedReader reader = new BufferedReader(new FileReader("scores.txt"))) {
			String line;
			while ((line = reader.readLine()) != null) {
				String[] parts = line.split(":");
				if (parts.length == 2) {
					String nickname = parts[0];
					int score = Integer.parseInt(parts[1]);
					scores.add(new Score(nickname, score));
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return scores;
	}

	private void displayTop5Scores() {
		List<Score> scores = readScores();
		scores.sort(Comparator.comparingInt(Score::getScore).reversed());
		int limit = Math.min(5, scores.size());

		VBox scoreBox = new VBox();
		scoreBox.setSpacing(10);
		scoreBox.setLayoutX(100);
		scoreBox.setLayoutY(100);

		for (int i = 0; i < limit; i++) {
			Score score = scores.get(i);
			Label scoreLabel = new Label((i + 1) + ". " + score.getNickname() + " - " + score.getScore());
			scoreBox.getChildren().add(scoreLabel);
		}

		scoreSubScene.getPane().getChildren().clear();
		scoreSubScene.getPane().getChildren().add(scoreBox);
	}



}