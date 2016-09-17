//////////////////////////////////////////////////////////////////////////////////
//                                                                              //
//     This Source Code Form is subject to the terms of the Mozilla Public      //
//     License, v. 2.0. If a copy of the MPL was not distributed with this      //
//     file, You can obtain one at http://mozilla.org/MPL/2.0/.                 //
//                                                                              //
//////////////////////////////////////////////////////////////////////////////////

package com.github.tiwindetea.raoulthegame;

import com.github.tiwindetea.raoulthegame.model.Game;
import com.github.tiwindetea.raoulthegame.model.Seed;
import com.github.tiwindetea.raoulthegame.view.GUI;
import com.github.tiwindetea.raoulthegame.view.ViewPackage;
import com.github.tiwindetea.soundplayer.Sound;
import com.github.tiwindetea.soundplayer.Sounds;
import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by maxime on 5/3/16.
 */
public class MainMenu extends Application {

	private final GUI GUI = new GUI();
	private final Game game = new Game(Settings.SAVE_PATH);
	private boolean wait = false;

	private final VBox buttonsVBox = new VBox();
	private final AnchorPane menuAnchorPane = new AnchorPane();
	private final Scene menuScene = new Scene(this.menuAnchorPane);
	private final TextField seedTF = new TextField("Enter your seed here");
	private final Button soloButton = new Button("Solo");
	private final Button multiplayerButton = new Button("2 Players");
	private final Button loadButton = new Button("Load game");
	private final Button resumeButton = new Button("Resume game");
	private final Button exitButton = new Button("exit");
	private final String buttonsStyle = ViewPackage.BUTTONS_STYLE_FILE_PATH;
	private Stage primaryStage;

	private final ImageView soloImageView = new ImageView(ViewPackage.SOLO_IMAGE);
	private final ImageView multiplayerImageView = new ImageView(ViewPackage.MULTIPLAYER_IMAGE);
	private final ImageView loadImageView = new ImageView(ViewPackage.LOAD_IMAGE);
	private final ImageView resumeImageView = new ImageView(ViewPackage.RESUME_IMAGE);
	private final ImageView greenLogoImageView = new ImageView(ViewPackage.GREEN_LOGO_IMAGE);
	private final ImageView orangeLogoImageView = new ImageView(ViewPackage.ORANGE_LOGO_IMAGE);

	private static final Color BACK_GROUND_COLOR_1 = Color.rgb(43, 97, 169);
	private static final Color BACK_GROUND_COLOR_2 = Color.rgb(23, 110, 67);

	private static final int PICTURES_HEIGHT = 600;
	private static final int PICTURES_ESTIMATED_MAX_WIDTH = 700;
	private static final int BUTTONS_VBOX_MIN_WIDTH = 300;
	private static final int BUTTONS_WIDTH = 200;
	private static final int LOGOS_HEIGHT = 200;

	@Override
	public void start(Stage primaryStage) throws Exception {

		// Loading sounds (cannot create some MediaPlayer if `launch(args)` hasn't been called)
		try {
			for (Sounds sound : Sounds.values()) {
				Sound.player.put(sound, sound.toURL(), sound.isLooping());
			}
			Sound.player.play(Sounds.MAIN_MENU_START_SOUND);
		} catch (Exception e){
			System.out.println("javafx.scene.media.MediaPlayer seems to be unsupported for your system.");
			System.out.println("Sounds will be disabled");
			Sound.player.setStopped(true);
		}
		this.primaryStage = primaryStage;
		primaryStage.setTitle("Raoul, the Game");
		primaryStage.getIcons().add(ViewPackage.ICON_IMAGE);

		this.game.addGameListener(this.GUI);
		this.GUI.addRequestListener(this.game);
		this.GUI.getScene().addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {
				if(event.getCode() == KeyCode.ESCAPE) {
					updateButtons();
					primaryStage.setScene(MainMenu.this.menuScene);
					MainMenu.this.buttonsVBox.setAlignment(Pos.CENTER);
					MainMenu.this.game.pause();
				}
			}
		});


		this.seedTF.minWidthProperty().bind(this.menuAnchorPane.widthProperty().divide(2.5));
		this.seedTF.maxWidthProperty().bind(this.menuAnchorPane.widthProperty().divide(2.5));
		this.menuScene.setOnKeyPressed(event -> {
			if (event.getCode() == KeyCode.ESCAPE && !this.soloButton.isVisible()) {
				this.buttonsVBox.getChildren().remove(this.seedTF);
				this.buttonsVBox.setAlignment(Pos.CENTER);
				this.showButtons();
				this.wait = true;
				this.seedTF.setText("Enter your seed here");
			}
		});

		this.menuAnchorPane.getChildren().addAll(this.soloImageView, this.multiplayerImageView, this.loadImageView, this.resumeImageView, this.greenLogoImageView, this.orangeLogoImageView);
		this.menuAnchorPane.setMinWidth(BUTTONS_VBOX_MIN_WIDTH + PICTURES_ESTIMATED_MAX_WIDTH);
		this.menuAnchorPane.setMinHeight(PICTURES_HEIGHT + LOGOS_HEIGHT);

		this.soloImageView.setPreserveRatio(true);
		this.soloImageView.setFitHeight(PICTURES_HEIGHT);
		AnchorPane.setRightAnchor(this.soloImageView, 0d);
		AnchorPane.setBottomAnchor(this.soloImageView, 0d);

		this.multiplayerImageView.setPreserveRatio(true);
		this.multiplayerImageView.setFitHeight(PICTURES_HEIGHT);
		AnchorPane.setRightAnchor(this.multiplayerImageView, 0d);
		AnchorPane.setBottomAnchor(this.multiplayerImageView, 0d);

		this.loadImageView.setPreserveRatio(true);
		this.loadImageView.setFitHeight(PICTURES_HEIGHT);
		AnchorPane.setRightAnchor(this.loadImageView, 0d);
		AnchorPane.setBottomAnchor(this.loadImageView, 0d);

		this.resumeImageView.setPreserveRatio(true);
		this.resumeImageView.setFitHeight(PICTURES_HEIGHT);
		AnchorPane.setRightAnchor(this.resumeImageView, 0d);
		AnchorPane.setBottomAnchor(this.resumeImageView, 0d);

		this.greenLogoImageView.setPreserveRatio(true);
		this.greenLogoImageView.setFitHeight(LOGOS_HEIGHT);
		AnchorPane.setRightAnchor(this.greenLogoImageView, 0d);
		AnchorPane.setTopAnchor(this.greenLogoImageView, 0d);

		this.orangeLogoImageView.setPreserveRatio(true);
		this.orangeLogoImageView.setFitHeight(LOGOS_HEIGHT);
		AnchorPane.setRightAnchor(this.orangeLogoImageView, 0d);
		AnchorPane.setTopAnchor(this.orangeLogoImageView, 0d);
		setSoloLayout();

		this.menuAnchorPane.getChildren().add(this.buttonsVBox);
		AnchorPane.setLeftAnchor(this.buttonsVBox, 0d);
		AnchorPane.setBottomAnchor(this.buttonsVBox, 0d);
		AnchorPane.setTopAnchor(this.buttonsVBox, 0d);
		this.menuScene.setFill(Color.BLACK);
		this.buttonsVBox.setAlignment(Pos.CENTER);
		this.buttonsVBox.setPadding(new Insets(20));
		this.buttonsVBox.setSpacing(10);
		this.buttonsVBox.prefWidthProperty().bind(Bindings.max(this.menuAnchorPane.widthProperty().subtract(PICTURES_ESTIMATED_MAX_WIDTH), BUTTONS_VBOX_MIN_WIDTH));
		this.buttonsVBox.getChildren().addAll(this.soloButton, this.multiplayerButton, this.resumeButton, this.loadButton, this.exitButton);

		initButtons();
		updateButtons();
		this.primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			@Override
			public void handle(WindowEvent event) {
				MainMenu.this.game.stop();
			}
		});
		primaryStage.setScene(this.menuScene);
		primaryStage.show();
	}

	private void startGame() {
		this.game.resume();
		this.primaryStage.setScene(this.GUI.getScene());
		ExecutorService executor = Executors.newSingleThreadExecutor();
		executor.submit(this.game);
		executor.shutdown();
	}

	private void setSoloLayout() {
		this.soloImageView.setVisible(true);
		this.multiplayerImageView.setVisible(false);
		this.loadImageView.setVisible(false);
		this.resumeImageView.setVisible(false);
		this.greenLogoImageView.setVisible(true);
		this.orangeLogoImageView.setVisible(false);
		this.menuAnchorPane.setBackground(new Background(new BackgroundFill(BACK_GROUND_COLOR_1, CornerRadii.EMPTY, Insets.EMPTY)));
	}

	private void setMultiplayerLayout() {
		this.soloImageView.setVisible(false);
		this.multiplayerImageView.setVisible(true);
		this.loadImageView.setVisible(false);
		this.resumeImageView.setVisible(false);
		this.greenLogoImageView.setVisible(false);
		this.orangeLogoImageView.setVisible(true);
		this.menuAnchorPane.setBackground(new Background(new BackgroundFill(BACK_GROUND_COLOR_2, CornerRadii.EMPTY, Insets.EMPTY)));
	}

	private void setLoadLayout() {
		this.soloImageView.setVisible(false);
		this.multiplayerImageView.setVisible(false);
		this.loadImageView.setVisible(true);
		this.resumeImageView.setVisible(false);
		this.greenLogoImageView.setVisible(true);
		this.orangeLogoImageView.setVisible(false);
		this.menuAnchorPane.setBackground(new Background(new BackgroundFill(BACK_GROUND_COLOR_1, CornerRadii.EMPTY, Insets.EMPTY)));
	}

	private void setResumeLayout() {
		this.soloImageView.setVisible(false);
		this.multiplayerImageView.setVisible(false);
		this.loadImageView.setVisible(false);
		this.resumeImageView.setVisible(true);
		this.greenLogoImageView.setVisible(false);
		this.orangeLogoImageView.setVisible(true);
		this.menuAnchorPane.setBackground(new Background(new BackgroundFill(BACK_GROUND_COLOR_2, CornerRadii.EMPTY, Insets.EMPTY)));
	}

	private void updateButtons() {
		this.resumeButton.setDisable(!this.game.isRunning());
		this.loadButton.setDisable(!new File(Settings.SAVE_PATH).isFile());
	}

	private void hideButtons() {
		this.soloButton.setVisible(false);
		this.multiplayerButton.setVisible(false);
		this.loadButton.setVisible(false);
		this.resumeButton.setVisible(false);
		this.exitButton.setVisible(false);
	}

	private void showButtons() {
		this.soloButton.setVisible(true);
		this.multiplayerButton.setVisible(true);
		this.loadButton.setVisible(true);
		this.resumeButton.setVisible(true);
		this.exitButton.setVisible(true);
	}

	private void initButtons() {

		this.soloButton.defaultButtonProperty().bind(this.soloButton.focusedProperty());
		this.soloButton.setPrefWidth(BUTTONS_WIDTH);
		this.soloButton.getStylesheets().add(this.buttonsStyle);
		this.soloButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				MainMenu.this.buttonsVBox.getChildren().add(MainMenu.this.seedTF);
				MainMenu.this.buttonsVBox.setAlignment(Pos.BASELINE_CENTER);
				MainMenu.this.hideButtons();
				MainMenu.this.seedTF.setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent event) {
						if (MainMenu.this.seedTF.getText().equals("Enter your seed here")) {
							MainMenu.this.game.initNew(1);
						} else {
							MainMenu.this.game.initNew(1, new Seed(MainMenu.this.seedTF.getText()));
						}
						MainMenu.this.startGame();
						MainMenu.this.showButtons();
						MainMenu.this.buttonsVBox.getChildren().remove(MainMenu.this.seedTF);
						MainMenu.this.seedTF.setText("Enter your seed here");
					}
				});
			}
		});
		this.soloButton.setOnMouseEntered(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				setSoloLayout();
				MainMenu.this.soloButton.requestFocus();
			}
		});

		this.multiplayerButton.defaultButtonProperty().bind(this.multiplayerButton.focusedProperty());
		this.multiplayerButton.setPrefWidth(BUTTONS_WIDTH);
		this.multiplayerButton.getStylesheets().add(this.buttonsStyle);
		this.multiplayerButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				MainMenu.this.game.initNew(2);
				startGame();
			}
		});
		this.multiplayerButton.setOnMouseEntered(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				setMultiplayerLayout();
				MainMenu.this.multiplayerButton.requestFocus();
			}
		});

		this.loadButton.defaultButtonProperty().bind(this.loadButton.focusedProperty());
		this.loadButton.setPrefWidth(BUTTONS_WIDTH);
		this.loadButton.getStylesheets().add(this.buttonsStyle);
		this.loadButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				MainMenu.this.game.loadSave();
				startGame();
			}
		});
		this.loadButton.setOnMouseEntered(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				setLoadLayout();
				MainMenu.this.loadButton.requestFocus();
			}
		});

		this.resumeButton.defaultButtonProperty().bind(this.resumeButton.focusedProperty());
		this.resumeButton.setPrefWidth(BUTTONS_WIDTH);
		this.resumeButton.getStylesheets().add(this.buttonsStyle);
		this.resumeButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				MainMenu.this.game.resume();
				MainMenu.this.primaryStage.setScene(MainMenu.this.GUI.getScene());
			}
		});
		this.resumeButton.setOnMouseEntered(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				setResumeLayout();
				MainMenu.this.resumeButton.requestFocus();
			}
		});

		this.exitButton.setCancelButton(true);
		this.exitButton.setPrefWidth(BUTTONS_WIDTH);
		this.exitButton.getStylesheets().add(this.buttonsStyle);
		this.exitButton.defaultButtonProperty().bind(this.exitButton.focusedProperty());
		this.exitButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				if (MainMenu.this.wait) {
					MainMenu.this.wait = false;
				} else {
					MainMenu.this.primaryStage.close();
					MainMenu.this.game.stop();
				}
			}
		});
		this.exitButton.setOnMouseEntered(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				setLoadLayout();
				MainMenu.this.exitButton.requestFocus();
			}
		});
	}

	public static void main(String[] args) {
		launch(args);
	}
}
