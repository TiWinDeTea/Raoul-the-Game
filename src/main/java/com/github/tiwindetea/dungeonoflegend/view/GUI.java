package com.github.tiwindetea.dungeonoflegend.view;

import com.github.tiwindetea.dungeonoflegend.model.Vector2i;
import com.github.tiwindetea.dungeonoflegend.model.events.living_entities.LivingEntityCreationEvent;
import com.github.tiwindetea.dungeonoflegend.model.events.living_entities.LivingEntityDeletionEvent;
import com.github.tiwindetea.dungeonoflegend.model.events.living_entities.LivingEntityLOSDefinitionEvent;
import com.github.tiwindetea.dungeonoflegend.model.events.living_entities.LivingEntityLOSModificationEvent;
import com.github.tiwindetea.dungeonoflegend.model.events.living_entities.LivingEntityMoveEvent;
import com.github.tiwindetea.dungeonoflegend.model.events.map.MapCreationEvent;
import com.github.tiwindetea.dungeonoflegend.model.events.players.PlayerCreationEvent;
import com.github.tiwindetea.dungeonoflegend.model.events.players.PlayerStatEvent;
import com.github.tiwindetea.dungeonoflegend.model.events.players.inventory.InventoryAdditionEvent;
import com.github.tiwindetea.dungeonoflegend.model.events.players.inventory.InventoryDeletionEvent;
import com.github.tiwindetea.dungeonoflegend.model.events.static_entities.StaticEntityCreationEvent;
import com.github.tiwindetea.dungeonoflegend.model.events.static_entities.StaticEntityDeletionEvent;
import com.github.tiwindetea.dungeonoflegend.model.events.static_entities.StaticEntityLOSDefinitionEvent;
import com.github.tiwindetea.dungeonoflegend.view.entities.StaticEntity;
import com.github.tiwindetea.dungeonoflegend.view.entities.StaticEntityType;
import com.github.tiwindetea.dungeonoflegend.view.listeners.GameListener;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.SplitPane;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.TilePane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by maxime on 5/2/16.
 */
public class GUI implements GameListener {
	private Map<Integer, StaticEntity> entityMap = new HashMap<>();
	private final BorderPane borderPane = new BorderPane();
	private final Scene scene = new Scene(this.borderPane);
	private final Pane rPane = new Pane();

	private final Pane bPane = new Pane();
	private final SplitPane bSplitPane = new SplitPane();

	private final Pane cPane = new Pane();

	private PlayerHUD player1HUD;
	private PlayerHUD player2HUD;

	public GUI() {
		this.init();
	}

	private void init() {


		this.rPane.setBackground(new Background(new BackgroundFill(Color.CRIMSON, CornerRadii.EMPTY, Insets.EMPTY)));
		this.rPane.setMaxWidth(300);
		this.rPane.setMinHeight(100);
		this.rPane.setMinWidth(300);
		//this.rPane.setPrefSize(Double.MAX_VALUE, Double.MAX_VALUE);
		Rectangle rect = new Rectangle(50, 50, Color.BLACK);
		this.rPane.getChildren().add(rect);
		this.borderPane.setRight(this.rPane);
		this.borderPane.setBottom(this.bPane);
		this.cPane.setBackground(new Background(new BackgroundFill(Color.BROWN, CornerRadii.EMPTY, Insets.EMPTY)));
		this.borderPane.setCenter(this.cPane);
		this.borderPane.setMinWidth(500);
		this.borderPane.setMinHeight(500);
		//center: map -> caneva, TilePane ?
		//right: inventory
		//bottom: life and mana

		this.player1HUD = new PlayerHUD(new StaticEntity(StaticEntityType.PLAYER1, new Vector2i()).getImageView(), 100, 70, 100, 30);
		this.player2HUD = new PlayerHUD(new StaticEntity(StaticEntityType.PLAYER2, new Vector2i()).getImageView(), 100, 70, 100, 30);

		TilePane tilePane = new TilePane();
		tilePane.getChildren().addAll(this.player1HUD.getMainGroup(), this.player2HUD.getMainGroup());

		Pane miniMapPain = new Pane();
		miniMapPain.setBackground(new Background(new BackgroundFill(Color.CYAN, CornerRadii.EMPTY, Insets.EMPTY)));
		miniMapPain.setMinWidth(300);
		miniMapPain.setMaxWidth(300);

		HBox hBox = new HBox();
		hBox.getChildren().addAll(tilePane, miniMapPain);
		hBox.setMinWidth(2 * PlayerHUD.getSize().x + miniMapPain.getMinWidth());
		this.bPane.getChildren().add(hBox);

		this.bPane.setBackground(new Background(new BackgroundFill(Color.GREEN, CornerRadii.EMPTY, Insets.EMPTY)));

		this.bPane.setMinHeight(2 * PlayerHUD.getSize().y);

		this.player1HUD.getMainGroup().setOnMouseClicked(
		  new EventHandler<MouseEvent>() {
			  @Override
			  public void handle(MouseEvent event) {
				  if(event.getButton() == MouseButton.PRIMARY) {
					  changePlayerStat(new PlayerStatEvent((byte) 1, PlayerStatEvent.StatType.HEALTH, PlayerStatEvent.ValueType.ACTUAL, 100));
				  }
				  else if(event.getButton() == MouseButton.SECONDARY) {
					  changePlayerStat(new PlayerStatEvent((byte) 1, PlayerStatEvent.StatType.HEALTH, PlayerStatEvent.ValueType.ACTUAL, 10));
				  }
			  }
		  }
		);

		this.player2HUD.getMainGroup().setOnMouseClicked(
		  new EventHandler<MouseEvent>() {
			  @Override
			  public void handle(MouseEvent event) {
				  if(event.getButton() == MouseButton.PRIMARY) {
					  changePlayerStat(new PlayerStatEvent((byte) 2, PlayerStatEvent.StatType.HEALTH, PlayerStatEvent.ValueType.ACTUAL, 100));
				  }
				  else if(event.getButton() == MouseButton.SECONDARY) {
					  changePlayerStat(new PlayerStatEvent((byte) 2, PlayerStatEvent.StatType.HEALTH, PlayerStatEvent.ValueType.ACTUAL, 10));
				  }
			  }
		  }
		);

		this.bPane.widthProperty().addListener(e -> {
			if((this.bPane.getWidth() - this.rPane.getWidth()) < (2 * PlayerHUD.getSize().x)) {
				this.bPane.setMinHeight(2 * PlayerHUD.getSize().y);
			}
			else {
				this.bPane.setMinHeight(PlayerHUD.getSize().y);
			}
			tilePane.setMaxWidth(this.bPane.getWidth() - this.rPane.getWidth());
		});
	}

	public Scene getScene() {
		return this.scene;
	}

	@Override
	public void addInventory(InventoryAdditionEvent e) {

	}

	@Override
	public void deleteInventory(InventoryDeletionEvent e) {

	}

	@Override
	public void createLivingEntity(LivingEntityCreationEvent e) {

	}

	@Override
	public void deleteLivingEntity(LivingEntityDeletionEvent e) {

	}

	@Override
	public void defineLivingEntityLOS(LivingEntityLOSDefinitionEvent e) {

	}

	@Override
	public void modifieLivingEntityLOS(LivingEntityLOSModificationEvent e) {

	}

	@Override
	public void moveLivingEntity(LivingEntityMoveEvent e) {

	}

	@Override
	public void createPlayer(PlayerCreationEvent e) {

	}

	@Override
	public void changePlayerStat(PlayerStatEvent e) {
		//for tests
		//TODO
		PlayerHUD[] playersHUD = new PlayerHUD[2];
		playersHUD[0] = this.player1HUD;
		playersHUD[1] = this.player2HUD;
		switch(e.statType) {
		case HEALTH:
			switch(e.valueType) {
			case ACTUAL:
				playersHUD[e.playerNumber - 1].setActualHealth(e.value);
				break;
			case MAX:
				playersHUD[e.playerNumber - 1].setMaxHealth(e.value);
				break;
			}
			break;
		case MANA:
			switch(e.valueType) {
			case ACTUAL:
				playersHUD[e.playerNumber - 1].setActualMana(e.value);
				break;
			case MAX:
				playersHUD[e.playerNumber - 1].setMaxMana(e.value);
				break;
			}
			break;
		}
	}

	@Override
	public void createStaticEntity(StaticEntityCreationEvent e) {

	}

	@Override
	public void deleteStaticEntity(StaticEntityDeletionEvent e) {

	}

	@Override
	public void defineStaticEntityLOS(StaticEntityLOSDefinitionEvent e) {

	}

	@Override
	public void createMap(MapCreationEvent e) {

	}
}
