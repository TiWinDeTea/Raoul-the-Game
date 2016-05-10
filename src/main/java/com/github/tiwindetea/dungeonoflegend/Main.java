package com.github.tiwindetea.dungeonoflegend;

import com.github.tiwindetea.dungeonoflegend.events.living_entities.LivingEntityLOSDefinitionEvent;
import com.github.tiwindetea.dungeonoflegend.events.living_entities.LivingEntityMoveEvent;
import com.github.tiwindetea.dungeonoflegend.events.map.MapCreationEvent;
import com.github.tiwindetea.dungeonoflegend.events.players.PlayerCreationEvent;
import com.github.tiwindetea.dungeonoflegend.events.players.PlayerStatEvent;
import com.github.tiwindetea.dungeonoflegend.model.Direction;
import com.github.tiwindetea.dungeonoflegend.model.Game;
import com.github.tiwindetea.dungeonoflegend.model.Map;
import com.github.tiwindetea.dungeonoflegend.model.Seed;
import com.github.tiwindetea.dungeonoflegend.model.Vector2i;
import com.github.tiwindetea.dungeonoflegend.view.GUI;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.util.ArrayList;

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
		Game game = new Game(new ArrayList<>());
		game.addGameListener(GUI);
		GUI.addRequestListener(game);
		game.launch((byte) 1);
		//GUI.createPlayer(new PlayerCreationEvent(1, 666, new Vector2i(), Direction.DOWN, 100, 100));
		//GUI.createPlayer(new PlayerCreationEvent(2, 42, new Vector2i(), Direction.DOWN, 100, 100));

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


		Map map = new Map(new Seed());
		map.generateLevel(1);
		GUI.createMap(new MapCreationEvent(map.getMapCopy()));

		Vector2i pos = new Vector2i(40, 40);
		GUI.createPlayer(new PlayerCreationEvent(1, 42L, pos, Direction.DOWN, 100, 100));
		boolean[][] LOS = new boolean[9][9];
		for(int i = 0; i < LOS.length; i++) {
			for(int j = 0; j < LOS[i].length; j++) {
				LOS[i][j] = true;
			}
		}
		GUI.defineLivingEntityLOS(new LivingEntityLOSDefinitionEvent(42L, LOS));

		GUI.getScene().setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {
				Direction direction;
				switch(event.getCode()) {
				case LEFT:
					direction = Direction.LEFT;
					break;
				case UP:
					direction = Direction.UP;
					break;
				case RIGHT:
					direction = Direction.RIGHT;
					break;
				case DOWN:
					direction = Direction.DOWN;
					break;
				default:
					System.out.println("nope");
					return;
				}
				GUI.moveLivingEntity(new LivingEntityMoveEvent(42L, pos.add(direction)));
				GUI.defineLivingEntityLOS(new LivingEntityLOSDefinitionEvent(42L, map.getLOS(pos, 4)));
			}
		});
	}

	public static void main(String[] args) {
		launch(args);
	}
}
