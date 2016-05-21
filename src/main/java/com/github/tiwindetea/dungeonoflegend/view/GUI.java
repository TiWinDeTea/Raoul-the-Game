package com.github.tiwindetea.dungeonoflegend.view;

import com.github.tiwindetea.dungeonoflegend.events.living_entities.LivingEntityCreationEvent;
import com.github.tiwindetea.dungeonoflegend.events.living_entities.LivingEntityDeletionEvent;
import com.github.tiwindetea.dungeonoflegend.events.living_entities.LivingEntityLOSDefinitionEvent;
import com.github.tiwindetea.dungeonoflegend.events.living_entities.LivingEntityLOSModificationEvent;
import com.github.tiwindetea.dungeonoflegend.events.living_entities.LivingEntityMoveEvent;
import com.github.tiwindetea.dungeonoflegend.events.map.CenterOnTileEvent;
import com.github.tiwindetea.dungeonoflegend.events.map.FogAdditionEvent;
import com.github.tiwindetea.dungeonoflegend.events.map.FogResetEvent;
import com.github.tiwindetea.dungeonoflegend.events.map.MapCreationEvent;
import com.github.tiwindetea.dungeonoflegend.events.map.TileModificationEvent;
import com.github.tiwindetea.dungeonoflegend.events.playerinventory.ObjectClickEvent;
import com.github.tiwindetea.dungeonoflegend.events.players.PlayerCreationEvent;
import com.github.tiwindetea.dungeonoflegend.events.players.PlayerNextTickEvent;
import com.github.tiwindetea.dungeonoflegend.events.players.PlayerStatEvent;
import com.github.tiwindetea.dungeonoflegend.events.players.inventory.InventoryAdditionEvent;
import com.github.tiwindetea.dungeonoflegend.events.players.inventory.InventoryDeletionEvent;
import com.github.tiwindetea.dungeonoflegend.events.requests.CenterViewRequestEvent;
import com.github.tiwindetea.dungeonoflegend.events.requests.InteractionRequestEvent;
import com.github.tiwindetea.dungeonoflegend.events.requests.MoveRequestEvent;
import com.github.tiwindetea.dungeonoflegend.events.requests.inventory.DropRequestEvent;
import com.github.tiwindetea.dungeonoflegend.events.requests.inventory.UsageRequestEvent;
import com.github.tiwindetea.dungeonoflegend.events.static_entities.StaticEntityCreationEvent;
import com.github.tiwindetea.dungeonoflegend.events.static_entities.StaticEntityDeletionEvent;
import com.github.tiwindetea.dungeonoflegend.events.static_entities.StaticEntityLOSDefinitionEvent;
import com.github.tiwindetea.dungeonoflegend.events.tilemap.TileClickEvent;
import com.github.tiwindetea.dungeonoflegend.listeners.game.GameListener;
import com.github.tiwindetea.dungeonoflegend.listeners.playerinventory.PlayerInventoryListener;
import com.github.tiwindetea.dungeonoflegend.listeners.request.RequestListener;
import com.github.tiwindetea.dungeonoflegend.listeners.tilemap.TileMapListener;
import com.github.tiwindetea.dungeonoflegend.model.Direction;
import com.github.tiwindetea.dungeonoflegend.model.Vector2i;
import com.github.tiwindetea.dungeonoflegend.view.entities.LivingEntity;
import com.github.tiwindetea.dungeonoflegend.view.entities.LivingEntityType;
import com.github.tiwindetea.dungeonoflegend.view.entities.StaticEntity;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by maxime on 5/2/16.
 */
public class GUI implements GameListener, TileMapListener, PlayerInventoryListener {
	private final List<RequestListener> listeners = new ArrayList<>();

	public static final Color BOTTOM_BACKGROUND_COLOR = Color.GREEN;
	public static final Color RIGHT_BACKGROUND_COLOR = Color.CRIMSON;
	public static final Color CENTER_BACKGROUND_COLOR = Color.BLACK;

	private final BorderPane borderPane = new BorderPane();
	private final Scene scene = new Scene(this.borderPane);

	private final Pane cPane = new Pane();

	private final Pane bPane = new Pane();
	private final HBox bHBox = new HBox();
	private final TilePane blTilePane = new TilePane();
	private final Pane brMiniMapPain = new Pane();

	private final Pane rPane = new Pane();
	private final VBox rVBox = new VBox();
	private final Pane rIventoryPane = new Pane();

	private final HashMap<Long, LivingEntity> livingEntities = new HashMap<>();
	private final HashMap<Long, StaticEntity> staticEntities = new HashMap<>();

	private final List<PlayerHUD> playersHUD = new ArrayList<>();
	private final List<PlayerInventory> playersInventories = new ArrayList<>();
	private final int maxPlayersNumber = 2;
	private int actualPlayersNumber = 0;

	private final TileMap cTileMap = new TileMap();

	private EventHandler<KeyEvent> onKeyReleasedEventHandler = new EventHandler<KeyEvent>() {

		@Override
		public void handle(KeyEvent event) {
			switch(event.getCode()) {
			case LEFT:
			case Q:
			case A:
				fireMoveRequestEvent(new MoveRequestEvent(Direction.LEFT));
				break;
			case RIGHT:
			case D:
				fireMoveRequestEvent(new MoveRequestEvent(Direction.RIGHT));
				break;
			case UP:
			case Z:
			case W:
				fireMoveRequestEvent(new MoveRequestEvent(Direction.UP));
				break;
			case DOWN:
			case S:
				fireMoveRequestEvent(new MoveRequestEvent(Direction.DOWN));
				break;
			case SPACE:
				fireCenterViewRequestEvent(new CenterViewRequestEvent());
				break;
			default:
				break;
			}
		}
	};

	public GUI() {
		this.init();
	}

	private void init() {

		this.scene.setOnKeyReleased(this.onKeyReleasedEventHandler);

		//Main pane
		//this.rPane.setPrefSize(Double.MAX_VALUE, Double.MAX_VALUE);
		this.borderPane.setCenter(this.cPane);
		this.borderPane.setRight(this.rPane);
		this.borderPane.setBottom(this.bPane);
		this.borderPane.setMinWidth(500);
		this.borderPane.setMinHeight(500);

		//Center pane
		this.cPane.setBackground(new Background(new BackgroundFill(CENTER_BACKGROUND_COLOR, CornerRadii.EMPTY, Insets.EMPTY)));
		this.cPane.getChildren().add(this.cTileMap);
		this.cTileMap.addTileMapListener(this);

		//Right pane
		this.rIventoryPane.setBackground(new Background(new BackgroundFill(Color.CHOCOLATE, CornerRadii.EMPTY, Insets.EMPTY)));

		this.rVBox.setBackground(new Background(new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY)));
		this.rVBox.getChildren().add(new ScoreDisplayer());
		this.rVBox.getChildren().add(this.rIventoryPane);
		this.rVBox.getChildren().add(InformationsDisplayer.getInstance());

		this.rPane.setBackground(new Background(new BackgroundFill(RIGHT_BACKGROUND_COLOR, CornerRadii.EMPTY, Insets.EMPTY)));
		this.rPane.getChildren().add(this.rVBox);

		//Bootom pane
		this.brMiniMapPain.setBackground(new Background(new BackgroundFill(Color.CYAN, CornerRadii.EMPTY, Insets.EMPTY)));
		this.brMiniMapPain.setMinWidth(300);
		this.brMiniMapPain.setMaxWidth(300);
		this.brMiniMapPain.setMaxHeight(200);
		this.brMiniMapPain.setMaxHeight(200);

		this.bHBox.getChildren().addAll(this.blTilePane, this.brMiniMapPain);
		this.bHBox.prefWidthProperty().bind(this.bPane.widthProperty());
		this.bHBox.setBackground(new Background(new BackgroundFill(Color.BLUEVIOLET, CornerRadii.EMPTY, Insets.EMPTY)));

		this.bPane.setBackground(new Background(new BackgroundFill(BOTTOM_BACKGROUND_COLOR, CornerRadii.EMPTY, Insets.EMPTY)));
		this.bPane.getChildren().add(this.bHBox);
		this.bPane.prefHeightProperty().bind(this.bHBox.heightProperty());
	}

	public Scene getScene() {
		return this.scene;
	}

	public boolean[][] computeVisibleTiles() {
		boolean[][] visibleTiles = new boolean[this.cTileMap.getGridSize().x][this.cTileMap.getGridSize().y];
		for(Long currentKey : this.livingEntities.keySet()) {
			LivingEntity livingEntity = this.livingEntities.get(currentKey);
			if(livingEntity.getLOS() != null) {
				boolean[][] LOS = livingEntity.getLOS();
				int LOSPosX = livingEntity.getPosition().x - ((LOS.length - 1) / 2);
				int LOSPosY = livingEntity.getPosition().y - ((LOS[0].length - 1) / 2);
				for(int i = 0; i < LOS.length; i++) {
					for(int j = 0; j < LOS[i].length; j++) {
						if (((LOSPosX + i >= 0) && (LOSPosX + i < visibleTiles.length))
								&& ((LOSPosY + j >= 0) && (LOSPosY + j < visibleTiles[0].length))) {
							visibleTiles[LOSPosX + i][LOSPosY + j] |= LOS[i][j];
						}
					}
				}
			}
		}
		for(Long currentKey : this.staticEntities.keySet()) {
			StaticEntity staticEntity = this.staticEntities.get(currentKey);
			if(staticEntity.getLOS() != null) {
				boolean[][] LOS = staticEntity.getLOS();
				int LOSPosX = staticEntity.getPosition().x - ((LOS.length - 1) / 2);
				int LOSPosY = staticEntity.getPosition().y - ((LOS[0].length - 1) / 2);
				for(int i = 0; i < LOS.length; i++) {
					for(int j = 0; j < LOS[i].length; j++) {
						if (((LOSPosX + i >= 0) && (LOSPosX + i < visibleTiles.length))
								&& ((LOSPosY + j >= 0) && (LOSPosY + j < visibleTiles[0].length))) {
							visibleTiles[LOSPosX + i][LOSPosY + j] |= LOS[i][j];
						}
					}
				}
			}
		}
		return visibleTiles;
	}

	public void addRequestListener(RequestListener listener) {
		this.listeners.add(listener);
	}

	public RequestListener[] getRequestListener() {
		return this.listeners.toArray(new RequestListener[this.listeners.size()]);
	}

	private void fireDropRequestEvent(DropRequestEvent event) {
		for(RequestListener listener : this.getRequestListener()) {
			listener.requestDrop(event);
		}
	}

	private void fireUsageRequestEvent(UsageRequestEvent event) {
		for(RequestListener listener : this.getRequestListener()) {
			listener.requestUsage(event);
		}
	}

	private void fireMoveRequestEvent(MoveRequestEvent event) {
		for(RequestListener listener : this.getRequestListener()) {
			listener.requestMove(event);
		}
	}

	private void fireInteractionRequestEvent(InteractionRequestEvent event) {
		for(RequestListener listener : this.getRequestListener()) {
			listener.requestInteraction(event);
		}
	}

	private void fireCenterViewRequestEvent(CenterViewRequestEvent event) {
		for(RequestListener listener : this.getRequestListener()) {
			listener.requestCenterView(event);
		}
	}

	@Override
	public void addInventory(InventoryAdditionEvent e) {
		System.out.println("addInventory Event");
		if(e == null) {
			return;
		}
		if(e.isEquiped) {
			this.playersInventories.get(e.playerNumber).addEquipedItem(e.objectId, new StaticEntity(e.type, e.description));
		}
		else {
			this.playersInventories.get(e.playerNumber).addInventoryItem(e.objectId, new StaticEntity(e.type, e.description));
		}
	}

	@Override
	public void deleteInventory(InventoryDeletionEvent e) {
		System.out.println("deleteInventory Event");
		if(e == null) {
			return;
		}
		if(e.playerNumber < this.actualPlayersNumber) {
			this.playersInventories.get(e.playerNumber).removeItem(e.objectId);
		}
		else {
			System.out.println("GUI::deleteInventory : invalid player number " + e.playerNumber);
		}
	}

	@Override
	public void createLivingEntity(LivingEntityCreationEvent e) {
		System.out.println("createLivingEntity Event");
		if(e == null) {
			return;
		}
		LivingEntity livingEntity = new LivingEntity(e.type, e.position, e.direction, e.description);
		this.livingEntities.put(e.entityId, livingEntity);
		this.cTileMap.addEntity(livingEntity);
	}

	@Override
	public void deleteLivingEntity(LivingEntityDeletionEvent e) {
		System.out.println("deleteLivingEntity Event");
		if(e == null) {
			return;
		}
		if(this.livingEntities.containsKey(e.entityId)) {
			this.cTileMap.removeEntity(this.livingEntities.get(e.entityId));
			this.livingEntities.remove(e.entityId);
		}
		else {
			System.out.println("GUI::deleteLivingEntity : invalid entity id " + e.entityId);
		}
	}

	@Override
	public void defineLivingEntityLOS(LivingEntityLOSDefinitionEvent e) {
		System.out.println("defineLivingEntityLOS Event");
		if(e == null) {
			return;
		}
		if(this.livingEntities.containsKey(e.entityId)) {
			this.livingEntities.get(e.entityId).setLOS(e.newLOS);
			this.cTileMap.setVisibleTiles(computeVisibleTiles());
		}
		else {
			System.out.println("GUI::defineLivingEntityLOS : invalid entity id " + e.entityId);
		}
	}

	@Override
	public void modifieLivingEntityLOS(LivingEntityLOSModificationEvent e) {
		System.out.println("modifieLivingEntityLOS Event");
		if(e == null) {
			return;
		}
		if(this.livingEntities.containsKey(e.entityId)) {
			this.livingEntities.get(e.entityId).modifieLOS(e.modifiedTilesPositions);
			this.cTileMap.setVisibleTiles(computeVisibleTiles());
		}
		else {
			System.out.println("GUI::modifieLivingEntityLOS : invalid entity id " + e.entityId);
		}
	}

	@Override
	public void moveLivingEntity(LivingEntityMoveEvent e) {
		System.out.println("moveLivingEntity Event");
		if(e == null) {
			return;
		}
		if(this.livingEntities.containsKey(e.entityId)) {
			Vector2i oldPosition = this.livingEntities.get(e.entityId).getPosition();
			Vector2i newPosition = e.newPosition;
			Direction direction;
			if(newPosition.y > oldPosition.y) {
				direction = Direction.DOWN;
			}
			else if(newPosition.y < oldPosition.y) {
				direction = Direction.UP;
			}
			else if(newPosition.x > oldPosition.x) {
				direction = Direction.RIGHT;
			}
			else if(newPosition.x < oldPosition.x) {
				direction = Direction.LEFT;
			}
			else {
				direction = this.livingEntities.get(e.entityId).getDirection();
			}
			this.livingEntities.get(e.entityId).setDirection(direction);
			this.livingEntities.get(e.entityId).setPosition(e.newPosition);
			this.cTileMap.setVisibleTiles(computeVisibleTiles());
		}
		else {
			System.out.println("GUI::moveLivingEntity : invalid entity id " + e.entityId);
		}
	}

	@Override
	public void createPlayer(PlayerCreationEvent e) {
		System.out.println("createPlayer Event");
		if(e == null) {
			return;
		}
		if(this.actualPlayersNumber < this.maxPlayersNumber) {
			LivingEntityType livingEntityType;
			if(e.playerNumber == 1) {
				livingEntityType = LivingEntityType.PLAYER2;
			}
			else {
				livingEntityType = LivingEntityType.PLAYER1;
			}
			LivingEntity livingEntity = new LivingEntity(livingEntityType, e.position, e.direction, e.description);
			this.livingEntities.put(e.entityId, livingEntity);
			this.cTileMap.addEntity(livingEntity);
			++this.actualPlayersNumber;

			ImageView imageView1 = new ImageView(livingEntityType.getImage());
			ImageView imageView2 = new ImageView(livingEntityType.getImage());
			Vector2i spritePosition = livingEntityType.getSpritePosition(e.direction);
			imageView1.setViewport(new Rectangle2D(spritePosition.x * ViewPackage.spritesSize.x, spritePosition.y * ViewPackage.spritesSize.y, ViewPackage.spritesSize.x, ViewPackage.spritesSize.y));
			imageView2.setViewport(new Rectangle2D(spritePosition.x * ViewPackage.spritesSize.x, spritePosition.y * ViewPackage.spritesSize.y, ViewPackage.spritesSize.x, ViewPackage.spritesSize.y));
			PlayerHUD playerHUD = new PlayerHUD(imageView1, e.maxHealth, e.maxHealth, e.maxMana, e.maxMana);
			this.playersHUD.add(playerHUD);
			this.blTilePane.getChildren().add(playerHUD);

			PlayerInventory playerInventory = new PlayerInventory(imageView2);
			this.playersInventories.add(playerInventory);
			this.rIventoryPane.getChildren().add(playerInventory);
			playerInventory.addPlayerInventoryListener(this);
		}
		else {
			System.out.println("GUI::createPlayer : too much players " + this.actualPlayersNumber);
		}
	}

	@Override
	public void changePlayerStat(PlayerStatEvent e) {
		System.out.println("changePlayerStat Event");
		if(e == null) {
			return;
		}
		if(e.playerNumber >= 0 && e.playerNumber < this.actualPlayersNumber) {
			int value = e.value;
			if(value < 0) {
				value = 0;
			}
			switch(e.statType) {
			case HEALTH:
				switch(e.valueType) {
				case ACTUAL:
					this.playersHUD.get(e.playerNumber).setActualHealth(value);
					break;
				case MAX:
					this.playersHUD.get(e.playerNumber).setMaxHealth(value);
					break;
				}
				break;
			case MANA:
				switch(e.valueType) {
				case ACTUAL:
					this.playersHUD.get(e.playerNumber).setActualMana(value);
					break;
				case MAX:
					this.playersHUD.get(e.playerNumber).setMaxMana(value);
					break;
				}
				break;
			}
		}
		else {
			System.out.println("GUI::changePlayerStat : invalid player number " + e.playerNumber);
		}
	}

	@Override
	public void createStaticEntity(StaticEntityCreationEvent e) {
		System.out.println("createStaticEntity Event");
		if(e == null) {
			return;
		}
		StaticEntity staticEntity = new StaticEntity(e.type, e.position, e.description);
		this.staticEntities.put(e.entityId, staticEntity);
		this.cTileMap.addEntity(staticEntity);
	}

	@Override
	public void deleteStaticEntity(StaticEntityDeletionEvent e) {
		System.out.println("deleteStaticEntity Event");
		if(e == null) {
			return;
		}
		if(this.staticEntities.containsKey(e.entityId)) {
			this.cTileMap.removeEntity(this.staticEntities.get(e.entityId));
			this.staticEntities.remove(e.entityId);
		}
		else {
			System.out.println("GUI::deleteStaticEntity : invalid entity id " + e.entityId);
		}
	}

	@Override
	public void defineStaticEntityLOS(StaticEntityLOSDefinitionEvent e) {
		System.out.println("defineStaticEntityLOS Event");
		if(e == null) {
			return;
		}
		if(this.staticEntities.containsKey(e.entityId)) {
			this.staticEntities.get(e.entityId).setLOS(e.newLOS);
			this.cTileMap.setVisibleTiles(computeVisibleTiles());
		}
		else {
			System.out.println("GUI::defineStaticEntityLOS : invalid entity id " + e.entityId);
		}
	}

	@Override
	public void createMap(MapCreationEvent e) {
		System.out.println("createMap Event");
		if(e == null) {
			return;
		}
		this.cTileMap.setMap(e.map);
	}

	@Override
	public void tileClicked(TileClickEvent e) {
		fireInteractionRequestEvent(new InteractionRequestEvent(e.tilePosition));
	}

	@Override
	public void modifieTile(TileModificationEvent e) {
		System.out.println("modifieTile Event");
		this.cTileMap.setTile(e.tileType, e.tilePosition);
	}

	@Override
	public void playerNextTick(PlayerNextTickEvent event) {
		System.out.println("playerNextTick Event");
		for(PlayerHUD playerHUD : this.playersHUD) {
			playerHUD.setMasked(true);
		}
		for(PlayerInventory playersInventory : this.playersInventories) {
			playersInventory.setVisible(false);
		}
		this.playersHUD.get(event.playerNumber).setMasked(false);
		this.playersInventories.get(event.playerNumber).setVisible(true);
	}

	@Override
	public void objectClicked(ObjectClickEvent e) {
		fireUsageRequestEvent(new UsageRequestEvent(e.objectId));
	}

	@Override
	public void addFog(FogAdditionEvent e) {
		System.out.println("addFog Event");
		this.cTileMap.addFoggedTiles(e.fogCenterPosition, e.fog);
	}

	@Override
	public void resetFog(FogResetEvent e) {
		System.out.println("resetFog Event");
		//TODO
	}

	@Override
	public void centerOnTile(CenterOnTileEvent e) {
		this.cTileMap.centerViewOnTile(e.tilePosition);
	}
}
