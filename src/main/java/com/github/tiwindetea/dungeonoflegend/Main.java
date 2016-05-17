package com.github.tiwindetea.dungeonoflegend;

import com.github.tiwindetea.dungeonoflegend.model.Game;
import com.github.tiwindetea.dungeonoflegend.view.GUI;
import com.github.tiwindetea.dungeonoflegend.view.ViewPackage;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.stage.Stage;

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
		primaryStage.getIcons().add(ViewPackage.icon);
	}

	public static void main(String[] args) {
		launch(args);
	}
}
