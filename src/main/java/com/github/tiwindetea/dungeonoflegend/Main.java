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
		game.initNew(2);
		//GUI.createPlayer(new PlayerCreationEvent(1, 666, new Vector2i(), Direction.DOWN, 100, 100));
		//GUI.createPlayer(new PlayerCreationEvent(2, 42, new Vector2i(), Direction.DOWN, 100, 100));
		primaryStage.getIcons().add(ViewPackage.icon);

		/*GUI.getScene().setOnMouseClicked(
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
		System.out.println("Seed: {" + map.getSeed().getAlphaSeed() + ";" + map.getSeed().getBetaSeed() + "}");
		map.generateLevel(1);
		GUI.createMap(new MapCreationEvent(map.getMapCopy()));

		Vector2i pos = map.getStairsUpPosition();
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
		});*/
	}

	public static void main(String[] args) {
		launch(args);
	}
}
