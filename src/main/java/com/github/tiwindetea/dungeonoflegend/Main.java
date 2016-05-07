package com.github.tiwindetea.dungeonoflegend;

import com.github.tiwindetea.dungeonoflegend.events.players.PlayerCreationEvent;
import com.github.tiwindetea.dungeonoflegend.events.players.PlayerStatEvent;
import com.github.tiwindetea.dungeonoflegend.model.Direction;
import com.github.tiwindetea.dungeonoflegend.model.Game;
import com.github.tiwindetea.dungeonoflegend.model.Vector2i;
import com.github.tiwindetea.dungeonoflegend.view.GUI;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
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
		Game game = new Game(0);
		game.addGameListener(GUI);
		GUI.addRequestListener(game);
		game.launch((byte) 1);
		GUI.createPlayer(new PlayerCreationEvent(1, new Vector2i(), Direction.DOWN, 100, 100));
		GUI.createPlayer(new PlayerCreationEvent(2, new Vector2i(), Direction.DOWN, 100, 100));

		GUI.getScene().setOnMouseClicked(
		  new EventHandler<MouseEvent>() {
			  @Override
			  public void handle(MouseEvent event) {
				  if(event.getButton() == MouseButton.PRIMARY) {
					  GUI.changePlayerStat(new PlayerStatEvent(1, PlayerStatEvent.StatType.HEALTH, PlayerStatEvent.ValueType.ACTUAL, 100));
					  GUI.changePlayerStat(new PlayerStatEvent(1, PlayerStatEvent.StatType.MANA, PlayerStatEvent.ValueType.ACTUAL, 20));
				  }
				  else if(event.getButton() == MouseButton.SECONDARY) {
					  GUI.changePlayerStat(new PlayerStatEvent(1, PlayerStatEvent.StatType.HEALTH, PlayerStatEvent.ValueType.ACTUAL, 10));
					  GUI.changePlayerStat(new PlayerStatEvent(1, PlayerStatEvent.StatType.MANA, PlayerStatEvent.ValueType.ACTUAL, 90));

				  }
			  }
		  });
	}

	public static void main(String[] args) {
		launch(args);
	}
}
