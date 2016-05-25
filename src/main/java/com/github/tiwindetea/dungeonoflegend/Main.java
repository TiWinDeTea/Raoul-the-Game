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
	private Scene menu = new Scene(this.root);
	private Button solo = new Button("Solo");
	private Button multiplayer = new Button("2 Players");
	private Button load = new Button("Load game");
	private Button resume = new Button("Resume game");
	private Button exit = new Button("exit");
	private final String buttonStyle =
			".button {" +
					"-fx-background-color:" +
					"		linear-gradient(#f2f2f2, #d6d6d6)," +
					"		linear-gradient(#fcfcfc 0%, #d9d9d9 20%, #d6d6d6 100%)," +
					"		linear-gradient(#dddddd 0%, #f6f6f6 50%);" +
					"	-fx-background-radius: 8,7,6;" +
					"	-fx-background-insets: 0,1,2;" +
					"	-fx-text-fill: black;" +
					"	-fx-effect: dropshadow( three-pass-box , rgba(0,0,0,0.6) , 5, 0.0 , 0 , 1 );" +
					"}";

	@Override
	public void start(Stage primaryStage) throws Exception {

		this.GUI.getScene().addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {
				if (event.getCode() == KeyCode.ESCAPE) {
					updateButtons();
					primaryStage.setScene(Main.this.menu);
					Main.this.root.setAlignment(Pos.CENTER);
					Main.this.game.pause();
				}
			}
		});

		primaryStage.setTitle("Dungeon Of Legends");
		primaryStage.getIcons().add(ViewPackage.iconImage);
		this.menu.setFill(Color.DARKSLATEGRAY);
		this.game.addGameListener(this.GUI);
		this.GUI.addRequestListener(this.game);
		this.root.setAlignment(Pos.CENTER);
		this.root.setPadding(new Insets(20));
		this.root.setSpacing(10);
		this.root.setBackground(new Background(new BackgroundFill(Color.DARKSLATEGREY, CornerRadii.EMPTY, Insets.EMPTY)));
		this.root.getChildren().addAll(this.solo, this.multiplayer, this.resume, this.load, this.exit);
		this.solo.defaultButtonProperty().bind(this.solo.focusedProperty());
		this.solo.setStyle(this.buttonStyle);
		this.solo.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				Main.this.game.initNew(1);
				Main.this.startGame(primaryStage);
			}
		});
		this.multiplayer.defaultButtonProperty().bind(this.multiplayer.focusedProperty());
		this.multiplayer.setStyle(this.buttonStyle);
		this.multiplayer.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				Main.this.game.initNew(2);
				startGame(primaryStage);
			}
		});
		this.load.defaultButtonProperty().bind(this.load.focusedProperty());
		this.load.setStyle(this.buttonStyle);
		this.load.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				Main.this.game.loadSave();
				startGame(primaryStage);
			}
		});
		this.resume.defaultButtonProperty().bind(this.resume.focusedProperty());
		this.resume.setStyle(this.buttonStyle);
		this.resume.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				Main.this.game.resume();
				primaryStage.setScene(Main.this.GUI.getScene());
			}
		});
		this.exit.setCancelButton(true);
		this.exit.setStyle(this.buttonStyle);
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
		primaryStage.setScene(this.menu);
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
