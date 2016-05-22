package com.github.tiwindetea.dungeonoflegend;

import com.github.tiwindetea.dungeonoflegend.model.Game;
import com.github.tiwindetea.dungeonoflegend.view.GUI;
import com.github.tiwindetea.dungeonoflegend.view.ViewPackage;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by maxime on 5/3/16.
 */
public class Main extends Application {
	@Override
	public void start(Stage primaryStage) throws Exception {
		Group root = new Group();
		GUI GUI = new GUI();
		primaryStage.setTitle("Dungeon Of Legends");
		primaryStage.setScene(GUI.getScene());
		primaryStage.show();
        Game game = new Game("/tmp/LOL");
        game.addGameListener(GUI);
		GUI.addRequestListener(game);
		game.initNew(1);
		ExecutorService executor = Executors.newSingleThreadExecutor();
		executor.submit(game);
		executor.shutdown();
		primaryStage.getIcons().add(ViewPackage.iconImage);
		primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			@Override
			public void handle(WindowEvent event) {
				game.stop();
			}
		});
	}

	public static void main(String[] args) {
		launch(args);
	}
}
