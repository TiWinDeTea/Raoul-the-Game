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
import com.github.tiwindetea.dungeonoflegend.view.listeners.entities.EntityListener;
import com.github.tiwindetea.dungeonoflegend.view.listeners.map.MapListener;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.SplitPane;
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
public class GUI implements EntityListener, MapListener {
	private Map<Integer, StaticEntity> entityMap = new HashMap<>();
	private final BorderPane borderPane = new BorderPane();
	private final Scene scene = new Scene(this.borderPane);
	private final Pane rPane = new Pane();

	private final Pane bPane = new Pane();
	private final SplitPane bSplitPane = new SplitPane();

	private final Pane cPane = new Pane();

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
		this.bPane.setBackground(new Background(new BackgroundFill(Color.PURPLE, CornerRadii.EMPTY, Insets.EMPTY)));
		this.borderPane.setBottom(this.bPane);
		this.cPane.setBackground(new Background(new BackgroundFill(Color.BROWN, CornerRadii.EMPTY, Insets.EMPTY)));
		this.borderPane.setCenter(this.cPane);
		this.borderPane.setMinWidth(500);
		this.borderPane.setMinHeight(500);
		//center: map -> caneva, TilePane ?
		//right: inventory
		//bottom: life and mana

		PlayerHUD player1HUD = new PlayerHUD(new StaticEntity(StaticEntityType.PLAYER1, new Vector2i()).getImageView(), 100, 70, 100, 30);
		PlayerHUD player2HUD = new PlayerHUD(new StaticEntity(StaticEntityType.PLAYER2, new Vector2i()).getImageView(), 100, 70, 100, 30);

		TilePane tilePane = new TilePane();
		tilePane.getChildren().addAll(player1HUD.getMainGroup(), player2HUD.getMainGroup());

		Pane miniMapPain = new Pane();
		miniMapPain.setBackground(new Background(new BackgroundFill(Color.CYAN, CornerRadii.EMPTY, Insets.EMPTY)));
		miniMapPain.setMinWidth(300);
		miniMapPain.setMaxWidth(300);

		HBox hBox = new HBox();
		hBox.getChildren().addAll(tilePane, miniMapPain);
		hBox.setMinWidth(2 * PlayerHUD.getSize().x + miniMapPain.getMinWidth());
		this.bPane.getChildren().add(hBox);

		this.bPane.widthProperty().addListener(e -> {
			if((this.bPane.getWidth() - this.rPane.getWidth()) < (2 * PlayerHUD.getSize().x)) {
				player1HUD.setActualHealth(10);
				player1HUD.setActualMana(10);
				player2HUD.setActualHealth(100);
				player2HUD.setActualMana(100);
				this.bPane.setMinHeight(2 * PlayerHUD.getSize().y);
			}
			else {
				player1HUD.setActualHealth(100);
				player1HUD.setActualMana(100);
				player2HUD.setActualHealth(10);
				player2HUD.setActualMana(10);
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
