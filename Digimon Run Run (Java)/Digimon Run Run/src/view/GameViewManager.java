package view;

import java.util.Random;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Comparator;

import javafx.animation.AnimationTimer;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import model.DIGIMON;
import model.SmallInfoLabel;
import model.WinInfoLabel;

public class GameViewManager {

	private AnchorPane gamePane;
	private Scene gameScene;
	private Stage gameStage;

	private static final int GAME_WIDTH = 600;
	private static final int GAME_HEIGHT = 800;

	private Stage menuStage;
	private ImageView digimon;

	private boolean isSpaceKeyPressed; // Add this variable to track spacebar state
	private boolean isLeftKeyPressed;
	private boolean isRightKeyPressed;
	private AnimationTimer gameTimer;
	private int angle;
	private String playerNickname;


	private GridPane gridPane1;
	private GridPane gridPane2;
	private final static String BACKGROUND_IMAGE = "/resources/background2.gif";


	public static String METEOR_BROWN_IMAGE = "/resources/pizza.png";
	public final static String METEOR_GREY_IMAGE = "/resources/meteor_grey.png";


	private ImageView[] brownMeteors;
	private ImageView[] greyMeteors;
	Random randomPositionGenerator;
	private SmallInfoLabel timerLabel;
	private int remainingTime = 30; // 30 seconds countdown


	private ImageView star;
	private SmallInfoLabel pointsLabel;
	private ImageView[] playerLifes;
	private int playerLife;
	private int points;
	public static String GOLD_STAR_IMAGE = "/resources/burger.png";

	private final static int STAR_RADIUS = 12;
	private final static int DIGIMON_RADIUS = 27;
	private final static int METEOR_RADIUS = 20;


	public GameViewManager() {
		initializeStage();
		createKeyListeners();
		randomPositionGenerator = new Random();

	}

	private void createKeyListeners() {
		gameScene.setOnKeyPressed(new EventHandler<KeyEvent>() {

			@Override
			public void handle(KeyEvent event) {
				if (event.getCode() == KeyCode.LEFT) {
					isLeftKeyPressed = true;
				} else if (event.getCode() == KeyCode.RIGHT) {
					isRightKeyPressed = true;
				} else if (event.getCode() == KeyCode.SPACE) {
					isSpaceKeyPressed = true; // Detect spacebar press
				}
			}
		});

		gameScene.setOnKeyReleased(new EventHandler<KeyEvent>() {

			@Override
			public void handle(KeyEvent event) {
				if (event.getCode() == KeyCode.LEFT) {
					isLeftKeyPressed = false;
				} else if (event.getCode() == KeyCode.RIGHT) {
					isRightKeyPressed = false;
				} else if (event.getCode() == KeyCode.SPACE) {
					isSpaceKeyPressed = false; // Detect spacebar release
				}
			}
		});
	}



	private void initializeStage() {
		gamePane = new AnchorPane();
		gameScene = new Scene(gamePane, GAME_WIDTH, GAME_HEIGHT);
		gameStage = new Stage();
		gameStage.setScene(gameScene);
	}

	private void loadScores() {
		// Get the resource as a stream
		InputStream inputStream = getClass().getClassLoader().getResourceAsStream("scores.txt");

		if (inputStream == null) {
			System.err.println("Resource not found: scores.txt");
			return;
		}

		try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
			String line;
			while ((line = reader.readLine()) != null) {
				System.out.println(line); // Process each line of the file
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void createNewGame(Stage menuStage, DIGIMON choosenDIGIMON) {

		this.menuStage = menuStage;
		this.menuStage.hide();
		createBackground();
		createDigimon(choosenDIGIMON);
		createGameElements(choosenDIGIMON);
		createGameLoop();
		gameStage.show();
		loadScores();
	}

	private void createGameElements(DIGIMON choosenDIGIMON) {

		playerLife = 2;
		star = new ImageView(GOLD_STAR_IMAGE);
		setNewElementPosition(star);
		gamePane.getChildren().add(star);
		pointsLabel = new SmallInfoLabel("POINTS : 00");
		pointsLabel.setLayoutX(460);
		pointsLabel.setLayoutY(20);
		gamePane.getChildren().add(pointsLabel);
		timerLabel = new SmallInfoLabel("TIME : 30");
		timerLabel.setLayoutX(20); // Position at top left corner
		timerLabel.setLayoutY(20);
		gamePane.getChildren().add(timerLabel);
		playerLifes = new ImageView[3];

		for(int i = 0; i < playerLifes.length; i++) {
			playerLifes[i] = new ImageView(choosenDIGIMON.getUrlLife());
			playerLifes[i].setLayoutX(455 + (i * 50));
			playerLifes[i].setLayoutY(80);
			gamePane.getChildren().add(playerLifes[i]);

		}


		brownMeteors = new ImageView[3];
		for(int i = 0; i < brownMeteors.length; i++) {
			brownMeteors[i] = new ImageView(METEOR_BROWN_IMAGE);
			setNewElementPosition(brownMeteors[i]);
			gamePane.getChildren().add(brownMeteors[i]);
		}
		greyMeteors = new ImageView[3];
		for(int i = 0; i < greyMeteors.length; i++) {
			greyMeteors[i] = new ImageView(METEOR_GREY_IMAGE);
			setNewElementPosition(greyMeteors[i]);
			gamePane.getChildren().add(greyMeteors[i]);
		}
	}


	private void moveGameElements() {

		star.setLayoutY(star.getLayoutY() + 5);
		star.setRotate(star.getRotate() + 5); // Adjust the rotation speed as desired

		for(int i = 0; i < brownMeteors.length; i++) {
			brownMeteors[i].setLayoutY(brownMeteors[i].getLayoutY()+7);
			brownMeteors[i].setRotate(brownMeteors[i].getRotate()+4);
		}

		for(int i = 0; i < greyMeteors.length; i++) {
			greyMeteors[i].setLayoutY(greyMeteors[i].getLayoutY()+7);
			greyMeteors[i].setRotate(greyMeteors[i].getRotate()+4);
		}
	}

	private void checkIfElementAreBehindTheDigimonAndRelocated() {

		if(star.getLayoutY() > 1200) {
			setNewElementPosition(star);
		}

		for(int i = 0; i< brownMeteors.length; i++) {
			if(brownMeteors[i].getLayoutY() > 900) {
				setNewElementPosition(brownMeteors[i]);
			}
		}


		for(int i = 0; i< greyMeteors.length; i++) {
			if(greyMeteors[i].getLayoutY() > 900) {
				setNewElementPosition(greyMeteors[i]);
			}
		}
	}


	private void setNewElementPosition(ImageView image) {

		image.setLayoutX(randomPositionGenerator.nextInt(370));
		image.setLayoutY(-randomPositionGenerator.nextInt(3200)+600);

	}




	private void createDigimon(DIGIMON choosenDIGIMON) {
		digimon = new ImageView(choosenDIGIMON.getUrl());
		digimon.setLayoutX(GAME_WIDTH/2);
		digimon.setLayoutY(GAME_HEIGHT - 90);
		gamePane.getChildren().add(digimon);
	}


	private void createGameLoop() {
		gameTimer = new AnimationTimer() {

			private long lastTime = 0;

			@Override
			public void handle(long now) {
				if (lastTime == 0) {
					lastTime = now;
				}

				moveBackground();
				moveGameElements();
				checkIfElementAreBehindTheDigimonAndRelocated();
				checkIfElementsCollide();
				moveDigimon();

				// Update countdown timer every second
				if ((now - lastTime) >= 1_000_000_000) { // 1 second = 1,000,000,000 nanoseconds
					updateTimer();
					lastTime = now;
				}
			}
		};
		gameTimer.start();
	}

	private void updateTimer() {
		remainingTime--;
		timerLabel.setText("TIME : " + remainingTime);

		if (remainingTime <= 0) {
			handleWinCondition();
		}
	}

	private void saveScore() {
		try (BufferedWriter writer = new BufferedWriter(new FileWriter("scores.txt", true))) {
			writer.write(playerNickname + ": " + points);
			writer.newLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void handleLoseCondition() {
		gameTimer.stop(); // Stop the game loop

		// Display a lose message
		WinInfoLabel loseLabel = new WinInfoLabel("You Lose, Try Again!");
		loseLabel.setLayoutX(GAME_WIDTH / 2 - 150); // Center the label horizontally
		loseLabel.setLayoutY(GAME_HEIGHT / 2 - 100); // Center the label vertically
		loseLabel.setPrefWidth(300); // Set the preferred width to fit the text
		loseLabel.setPrefHeight(100); // Set the preferred height to fit the text
		gamePane.getChildren().add(loseLabel);

		saveScore();

		// Create a button to go back to the main menu
		Button backButton = new Button("Back to Menu");
		backButton.setLayoutX(GAME_WIDTH / 2 - 60); // Center the button below the label
		backButton.setLayoutY(GAME_HEIGHT / 2 + 20); // Position below the lose label
		gamePane.getChildren().add(backButton);

		backButton.setOnAction(e -> {
			gameStage.close(); // Close the game stage
			menuStage.show();  // Show the main menu stage
		});
	}

	// Setter method for playerNickname
	public void setPlayerNickname(String playerNickname) {
		this.playerNickname = playerNickname;
	}

	private void handleWinCondition() {
		gameTimer.stop(); // Stop the game loop
		System.out.println("Player nickname before win message: " + playerNickname);

		// Display a larger win message with the player's score
		//WinInfoLabel winLabel = new WinInfoLabel("CONGRATULATIONS\n, " + playerNickname + "!!\nYOU WIN!\nYour Score: " + points);

		String winMessage = "Congratulations,\n" + playerNickname + "\n You Win!!\n Your Score: " + points;
		WinInfoLabel winLabel = new WinInfoLabel(winMessage);
		winLabel.setLayoutX(GAME_WIDTH / 2 - 150); // Adjust the X position for centering
		winLabel.setLayoutY(GAME_HEIGHT / 2 - 100); // Adjust the Y position for centering
		winLabel.setPrefWidth(300); // Set the preferred width to fit the text
		winLabel.setPrefHeight(100); // Set the preferred height to fit the text
		gamePane.getChildren().add(winLabel);

		saveScore();

		// Create a button to go back to the main menu
		Button backButton = new Button("Back to Menu");
		backButton.setLayoutX(GAME_WIDTH / 2 - 60); // Center the button below the label
		backButton.setLayoutY(GAME_HEIGHT / 2 + 20); // Position below the win label
		gamePane.getChildren().add(backButton);

		backButton.setOnAction(e -> {
			gameStage.close(); // Close the game stage
			menuStage.show();  // Show the main menu stage
		});
	}



	private void moveDigimon() {
		int movementSpeed = isSpaceKeyPressed ? 10 : 3; // Increase speed if spacebar is pressed

		if (isLeftKeyPressed && !isRightKeyPressed) {
			if (angle > -30) {
				angle -= 5;
			}
			digimon.setRotate(angle);
			digimon.setScaleX(-1); // Flip the digimon horizontally when moving left
			if (digimon.getLayoutX() > -20) {
				digimon.setLayoutX(digimon.getLayoutX() - movementSpeed);
			}
		}

		if (isRightKeyPressed && !isLeftKeyPressed) {
			if (angle < 30) {
				angle += 5;
			}
			digimon.setRotate(angle);
			digimon.setScaleX(1); // Set the digimon orientation to normal when moving right
			if (digimon.getLayoutX() < 522) {
				digimon.setLayoutX(digimon.getLayoutX() + movementSpeed);
			}
		}

		if (!isLeftKeyPressed && !isRightKeyPressed) {
			if (angle < 0) {
				angle += 5;
			} else if (angle > 0) {
				angle -= 5;
			}
			digimon.setRotate(angle);
		}

		if (isLeftKeyPressed && isRightKeyPressed) {
			if (angle < 0) {
				angle += 5;
			} else if (angle > 0) {
				angle -= 5;
			}
			digimon.setRotate(angle);
		}
	}



	private void createBackground() {
		gridPane1 = new GridPane();
		gridPane2 = new GridPane();

		for (int i = 0 ; i < 12; i++) {
			ImageView backgroundImage1 = new ImageView(BACKGROUND_IMAGE);
			ImageView backgroundImage2 = new ImageView(BACKGROUND_IMAGE);
			GridPane.setConstraints(backgroundImage1, i% 3, i / 3 );
			GridPane.setConstraints(backgroundImage2, i% 3, i / 3 );
			gridPane1.getChildren().add(backgroundImage1);
			gridPane2.getChildren().add(backgroundImage2);
		}

		gridPane2.setLayoutY(- 1024);
		gamePane.getChildren().addAll(gridPane1, gridPane2);
	}

	private void moveBackground() {
		gridPane1.setLayoutY(gridPane1.getLayoutY() + 0.5);
		gridPane2.setLayoutY(gridPane2.getLayoutY() + 0.5);

		if (gridPane1.getLayoutY() >= 1024) {
			gridPane1.setLayoutY(-1024);
		}

		if (gridPane2.getLayoutY() >= 1024) {
			gridPane2.setLayoutY(-1024);
		}
	}

	private void checkIfElementsCollide() {
		// Collision with the star
		if(DIGIMON_RADIUS + STAR_RADIUS > calculateDistance(digimon.getLayoutX() + 49, star.getLayoutX() + 15,
				digimon.getLayoutY() + 37, star.getLayoutY() + 15)) {
			setNewElementPosition(star);
			addPoints(1); // Add 1 point for collecting a star
		}

		// Collision with brown meteors
		for (int i = 0; i < brownMeteors.length; i++) {
			if (METEOR_RADIUS + DIGIMON_RADIUS > calculateDistance(digimon.getLayoutX() + 49, brownMeteors[i].getLayoutX() + 20,
					digimon.getLayoutY() + 37, brownMeteors[i].getLayoutY() + 20)) {
				setNewElementPosition(brownMeteors[i]);
				addPoints(2); // Add 2 points for hitting a brown meteor
				// Optionally, you can remove this line if you don't want to remove a life on collision with brown meteors
			}
		}

		// Collision with grey meteors
		for (int i = 0; i < greyMeteors.length; i++) {
			if (METEOR_RADIUS + DIGIMON_RADIUS > calculateDistance(digimon.getLayoutX() + 49, greyMeteors[i].getLayoutX() + 20,
					digimon.getLayoutY() + 37, greyMeteors[i].getLayoutY() + 20)) {
				removeLife(); // Only remove life, no points added for grey meteors
				setNewElementPosition(greyMeteors[i]);
			}
		}
	}

	private void addPoints(int pointsToAdd) {
		points += pointsToAdd;
		String textToSet = "POINTS : ";
		if (points < 10) {
			textToSet = textToSet + "0";
		}
		pointsLabel.setText(textToSet + points);
	}


	private void removeLife() {

		gamePane.getChildren().remove(playerLifes[playerLife]);
		playerLife--;
		if (playerLife < 0) {
			handleLoseCondition(); // Show the lose message and button
		}
	}

	private double calculateDistance(double x1, double x2, double y1, double y2) {
		return Math.sqrt(Math.pow(x1-x2, 2) + Math.pow(y1-y2, 2));
	}

}