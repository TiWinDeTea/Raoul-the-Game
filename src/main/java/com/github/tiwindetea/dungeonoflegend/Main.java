package com.github.tiwindetea.dungeonoflegend;

import com.github.tiwindetea.dungeonoflegend.model.Game;
import com.github.tiwindetea.dungeonoflegend.view.GUI;
import com.github.tiwindetea.dungeonoflegend.view.ViewPackage;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
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
public class Main extends Application {

	private GUI GUI = new GUI();
	private final String SAVE_FILE_NAME = "Save";
	private Game game = new Game(this.SAVE_FILE_NAME);
	private VBox root = new VBox();
	private Scene scene = new Scene(this.root);
	private Button solo = new Button("Solo");
	private Button multiplayer = new Button("2 Players");
	private Button load = new Button("Load game");
	private Button resume = new Button("Resume game");
	private Button exit = new Button("exit");

	@Override
	public void start(Stage primaryStage) throws Exception {

		this.GUI.getScene().addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {
				if (event.getCode() == KeyCode.ESCAPE) {
					updateButtons();
					primaryStage.setScene(Main.this.scene);
					Main.this.game.pause();
				}
			}
		});

		primaryStage.setTitle("Dungeon Of Legends");
		primaryStage.getIcons().add(ViewPackage.iconImage);
		this.scene.setFill(Color.LIGHTSLATEGREY);
		this.game.addGameListener(this.GUI);
		this.GUI.addRequestListener(this.game);
		this.root.setAlignment(Pos.CENTER);
		this.root.setPadding(new Insets(20));
		this.root.setSpacing(10);
		this.root.setBackground(new Background(new BackgroundFill(Color.LIGHTSLATEGREY, CornerRadii.EMPTY, Insets.EMPTY)));
		this.root.getChildren().addAll(this.solo, this.multiplayer, this.resume, this.load, this.exit);
		this.solo.defaultButtonProperty().bind(this.solo.focusedProperty());
		this.solo.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				Main.this.game.initNew(1);
				Main.this.startGame(primaryStage);
			}
		});
		this.multiplayer.defaultButtonProperty().bind(this.multiplayer.focusedProperty());
		this.multiplayer.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				Main.this.game.initNew(2);
				startGame(primaryStage);
			}
		});
		this.load.defaultButtonProperty().bind(this.load.focusedProperty());
		this.load.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				Main.this.game.loadSave();
				startGame(primaryStage);
			}
		});
		this.resume.defaultButtonProperty().bind(this.resume.focusedProperty());
		this.resume.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				Main.this.game.resume();
				primaryStage.setScene(Main.this.GUI.getScene());
			}
		});
		this.exit.setCancelButton(true);
		this.exit.defaultButtonProperty().bind(this.exit.focusedProperty());
		this.exit.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				primaryStage.close();
				Main.this.game.stop();
				return;
			}
		});

		updateButtons();
		primaryStage.setScene(this.scene);
		primaryStage.show();
	}

	private void startGame(Stage primaryStage) {
		primaryStage.setScene(this.GUI.getScene());
		ExecutorService executor = Executors.newSingleThreadExecutor();
		executor.submit(this.game);
		executor.shutdown();
		primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			@Override
			public void handle(WindowEvent event) {
				Main.this.game.stop();
			}
		});
	}

	private void updateButtons() {
		this.resume.setDisable(!this.game.isRunning());
		this.load.setDisable(!new File(this.SAVE_FILE_NAME).isFile());
	}

	public static void main(String[] args) {
		launch(args);
	}
}
