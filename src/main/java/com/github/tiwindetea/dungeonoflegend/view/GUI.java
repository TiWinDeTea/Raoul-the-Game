package com.github.tiwindetea.dungeonoflegend.view;

import com.github.tiwindetea.dungeonoflegend.events.Event;
import com.github.tiwindetea.dungeonoflegend.events.ScoreUpdateEvent;
import com.github.tiwindetea.dungeonoflegend.events.living_entities.LivingEntityCreationEvent;
import com.github.tiwindetea.dungeonoflegend.events.living_entities.LivingEntityDeletionEvent;
import com.github.tiwindetea.dungeonoflegend.events.living_entities.LivingEntityEvent;
import com.github.tiwindetea.dungeonoflegend.events.living_entities.LivingEntityHealthUpdateEvent;
import com.github.tiwindetea.dungeonoflegend.events.living_entities.LivingEntityHealthVisibilityEvent;
import com.github.tiwindetea.dungeonoflegend.events.living_entities.LivingEntityLOSDefinitionEvent;
import com.github.tiwindetea.dungeonoflegend.events.living_entities.LivingEntityLOSModificationEvent;
import com.github.tiwindetea.dungeonoflegend.events.living_entities.LivingEntityMoveEvent;
import com.github.tiwindetea.dungeonoflegend.events.map.CenterOnTileEvent;
import com.github.tiwindetea.dungeonoflegend.events.map.FogAdditionEvent;
import com.github.tiwindetea.dungeonoflegend.events.map.FogResetEvent;
import com.github.tiwindetea.dungeonoflegend.events.map.MapCreationEvent;
import com.github.tiwindetea.dungeonoflegend.events.map.MapEvent;
import com.github.tiwindetea.dungeonoflegend.events.map.TileModificationEvent;
import com.github.tiwindetea.dungeonoflegend.events.playerinventory.ObjectClickEvent;
import com.github.tiwindetea.dungeonoflegend.events.playerinventory.PlayerInventoryEvent;
import com.github.tiwindetea.dungeonoflegend.events.players.PlayerCreationEvent;
import com.github.tiwindetea.dungeonoflegend.events.players.PlayerEvent;
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
import com.github.tiwindetea.dungeonoflegend.events.static_entities.StaticEntityEvent;
import com.github.tiwindetea.dungeonoflegend.events.static_entities.StaticEntityLOSDefinitionEvent;
import com.github.tiwindetea.dungeonoflegend.events.tilemap.TileClickEvent;
import com.github.tiwindetea.dungeonoflegend.events.tilemap.TileMapEvent;
import com.github.tiwindetea.dungeonoflegend.listeners.game.GameListener;
import com.github.tiwindetea.dungeonoflegend.listeners.playerinventory.PlayerInventoryListener;
import com.github.tiwindetea.dungeonoflegend.listeners.request.RequestListener;
import com.github.tiwindetea.dungeonoflegend.listeners.tilemap.TileMapListener;
import com.github.tiwindetea.dungeonoflegend.model.Direction;
import com.github.tiwindetea.dungeonoflegend.model.Vector2i;
import com.github.tiwindetea.dungeonoflegend.view.entities.LivingEntity;
import com.github.tiwindetea.dungeonoflegend.view.entities.LivingEntityType;
import com.github.tiwindetea.dungeonoflegend.view.entities.StaticEntity;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * The type GUI.
 *
 * @author Maxime PINARD.
 */
public class GUI implements GameListener, TileMapListener, PlayerInventoryListener {
	private final List<RequestListener> listeners = new ArrayList<>();

	private static final Duration REFRESH_DURATION = Duration.millis(100);
	private static final int MOVE_REQUEST_DELAY = 80;

	private static final Color BOTTOM_BACKGROUND_COLOR = Color.rgb(0x2E, 0x26, 0x25);
	private static final Color RIGHT_BACKGROUND_COLOR1 = Color.rgb(53, 53, 53);
	private static final Color RIGHT_BACKGROUND_COLOR2 = Color.DARKGRAY;
	private static final Color CENTER_BACKGROUND_COLOR = Color.BLACK;

	private static final Vector2i ANCHOR_PANE_MIN_SIZE = new Vector2i(1000, 500);

	private final AnchorPane anchorPane = new AnchorPane();
	private final Scene scene = new Scene(this.anchorPane);

	private final Pane cPane = new Pane();

	private final Pane bPane = new Pane();
	private final HBox bHBox = new HBox();
	private final TilePane blTilePane = new TilePane();

	private final Pane rPane = new Pane();
	private final VBox rVBox = new VBox();
	private final ScoreDisplayer rScoreDisplayer = new ScoreDisplayer();
	private final Pane rIventoryPane = new Pane();

	private final HashMap<Long, LivingEntity> livingEntities = new HashMap<>(32, 7);
	private final HashMap<Long, StaticEntity> staticEntities = new HashMap<>(32, 7);

	private final List<PlayerHUD> playersHUD = new ArrayList<>();
	private final List<PlayerInventory> playersInventories = new ArrayList<>();
	private final int maxPlayersNumber = 2;
	private int actualPlayersNumber = 0;

	private final TileMap cTileMap = new TileMap();

	private final Queue<Event> eventQueue = new LinkedList<>();

	private boolean moveRencentlyRequested = false;

	private final EventHandler<KeyEvent> onKeyReleasedEventHandler = new EventHandler<KeyEvent>() {

		@Override
		public void handle(KeyEvent event) {
			if(!GUI.this.moveRencentlyRequested) {

				GUI.this.moveRencentlyRequested = true;
				ExecutorService executorService = Executors.newSingleThreadExecutor();
				executorService.submit(new Runnable() {
					@Override
					public void run() {
						try {
							Thread.sleep(MOVE_REQUEST_DELAY);
						} catch(InterruptedException e) {
							e.printStackTrace();
						}
						GUI.this.moveRencentlyRequested = false;
					}
				});
				executorService.shutdown();

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
		}
	};

	private final EventHandler<ActionEvent> eventExecutor = new EventHandler<ActionEvent>() {

		public void addInventory(InventoryAdditionEvent e) {
			if(e.isEquiped) {
				GUI.this.playersInventories.get(e.playerNumber).addEquipedItem(e.objectId, new StaticEntity(e.type, e.description));
			}
			else {
				GUI.this.playersInventories.get(e.playerNumber).addInventoryItem(e.objectId, new StaticEntity(e.type, e.description));
			}
		}

		public void deleteInventory(InventoryDeletionEvent e) {
			if(e.playerNumber < GUI.this.actualPlayersNumber) {
				GUI.this.playersInventories.get(e.playerNumber).removeItem(e.objectId);
			}
			else {
				System.out.println("GUI::deleteInventory : invalid player number " + e.playerNumber);
			}
		}

		public void createLivingEntity(LivingEntityCreationEvent e) {
			LivingEntity livingEntity = new LivingEntity(e.type, e.position, e.direction, e.description);
			GUI.this.livingEntities.put(e.entityId, livingEntity);
			GUI.this.cTileMap.addEntity(livingEntity);
		}

		public void deleteLivingEntity(LivingEntityDeletionEvent e) {
			if(GUI.this.livingEntities.containsKey(e.entityId)) {
				GUI.this.cTileMap.removeEntity(GUI.this.livingEntities.get(e.entityId));
				GUI.this.livingEntities.remove(e.entityId);
			}
			else {
				System.out.println("GUI::deleteLivingEntity : invalid entity id " + e.entityId);
			}
		}

		public void defineLivingEntityLOS(LivingEntityLOSDefinitionEvent e) {
			if(GUI.this.livingEntities.containsKey(e.entityId)) {
				GUI.this.livingEntities.get(e.entityId).setLOS(e.newLOS);
				GUI.this.cTileMap.setVisibleTiles(computeVisibleTiles());
			}
			else {
				System.out.println("GUI::defineLivingEntityLOS : invalid entity id " + e.entityId);
			}
		}

		public void modifieLivingEntityLOS(LivingEntityLOSModificationEvent e) {
			if(GUI.this.livingEntities.containsKey(e.entityId)) {
				GUI.this.livingEntities.get(e.entityId).modifieLOS(e.modifiedTilesPositions);
				GUI.this.cTileMap.setVisibleTiles(computeVisibleTiles());
			}
			else {
				System.out.println("GUI::modifieLivingEntityLOS : invalid entity id " + e.entityId);
			}
		}

		public void moveLivingEntity(LivingEntityMoveEvent e) {
			if(GUI.this.livingEntities.containsKey(e.entityId)) {
				Vector2i oldPosition = GUI.this.livingEntities.get(e.entityId).getPosition();
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
				} else {
					direction = GUI.this.livingEntities.get(e.entityId).getDirection();
				}
				GUI.this.livingEntities.get(e.entityId).setDirection(direction);
				GUI.this.livingEntities.get(e.entityId).setPosition(e.newPosition);
				GUI.this.cTileMap.setVisibleTiles(computeVisibleTiles());
			}
			else {
				System.out.println("GUI::moveLivingEntity : invalid entity id " + e.entityId);
			}
		}

		public void createPlayer(PlayerCreationEvent e) {
			if(GUI.this.actualPlayersNumber < GUI.this.maxPlayersNumber) {
				LivingEntityType livingEntityType;
				if(e.playerNumber == 1) {
					livingEntityType = LivingEntityType.PLAYER2;
				} else {
					livingEntityType = LivingEntityType.PLAYER1;
				}
				LivingEntity livingEntity = new LivingEntity(livingEntityType, e.position, e.direction, e.description);
				GUI.this.livingEntities.put(e.entityId, livingEntity);
				GUI.this.cTileMap.addEntity(livingEntity);
				++GUI.this.actualPlayersNumber;

				ImageView imageView1 = new ImageView(livingEntityType.getImage());
				ImageView imageView2 = new ImageView(livingEntityType.getImage());
				Vector2i spritePosition = livingEntityType.getSpritePosition(e.direction);
				imageView1.setViewport(new Rectangle2D(spritePosition.x * ViewPackage.spritesSize.x, spritePosition.y * ViewPackage.spritesSize.y, ViewPackage.spritesSize.x, ViewPackage.spritesSize.y));
				imageView2.setViewport(new Rectangle2D(spritePosition.x * ViewPackage.spritesSize.x, spritePosition.y * ViewPackage.spritesSize.y, ViewPackage.spritesSize.x, ViewPackage.spritesSize.y));
				PlayerHUD playerHUD = new PlayerHUD(imageView1, e.maxHealth, e.maxHealth, e.maxMana, e.maxMana,0,e.maxXP);
				GUI.this.playersHUD.add(playerHUD);
				GUI.this.blTilePane.getChildren().add(playerHUD);

				PlayerInventory playerInventory = new PlayerInventory(imageView2);
				GUI.this.playersInventories.add(playerInventory);
				GUI.this.rIventoryPane.getChildren().add(playerInventory);
				playerInventory.addPlayerInventoryListener(GUI.this);
			}
			else {
				System.out.println("GUI::createPlayer : too much players " + GUI.this.actualPlayersNumber);
			}
		}

		public void changePlayerStat(PlayerStatEvent e) {
			if(e == null) {
				return;
			}
			if(e.playerNumber >= 0 && e.playerNumber < GUI.this.actualPlayersNumber) {
				int value = e.value;
				if(value < 0) {
					value = 0;
				}
				switch(e.statType) {
				case HEALTH:
					switch(e.valueType) {
					case ACTUAL:
						GUI.this.playersHUD.get(e.playerNumber).setActualHealth(value);
						break;
					case MAX:
						GUI.this.playersHUD.get(e.playerNumber).setMaxHealth(value);
						break;
					}
					break;
				case MANA:
					switch(e.valueType) {
					case ACTUAL:
						GUI.this.playersHUD.get(e.playerNumber).setActualMana(value);
						break;
					case MAX:
						GUI.this.playersHUD.get(e.playerNumber).setMaxMana(value);
						break;
					}
					break;
				case XP:
					switch(e.valueType) {
					case ACTUAL:
						GUI.this.playersHUD.get(e.playerNumber).setActualXP(value);
						break;
					case MAX:
						GUI.this.playersHUD.get(e.playerNumber).setMaxXP(value);
						break;
					}
					break;
				}
			}
			else {
				System.out.println("GUI::changePlayerStat : invalid player number " + e.playerNumber);
			}
		}

		public void createStaticEntity(StaticEntityCreationEvent e) {
			if(e == null) {
				return;
			}
			StaticEntity staticEntity = new StaticEntity(e.type, e.position, e.description);
			GUI.this.staticEntities.put(e.entityId, staticEntity);
			GUI.this.cTileMap.addEntity(staticEntity);
		}

		public void deleteStaticEntity(StaticEntityDeletionEvent e) {
			if(e == null) {
				return;
			}
			if(GUI.this.staticEntities.containsKey(e.entityId)) {
				GUI.this.cTileMap.removeEntity(GUI.this.staticEntities.get(e.entityId));
				GUI.this.staticEntities.remove(e.entityId);
			}
			else {
				System.out.println("GUI::deleteStaticEntity : invalid entity id " + e.entityId);
			}
		}

		public void defineStaticEntityLOS(StaticEntityLOSDefinitionEvent e) {
			if(e == null) {
				return;
			}
			if(GUI.this.staticEntities.containsKey(e.entityId)) {
				GUI.this.staticEntities.get(e.entityId).setLOS(e.newLOS);
				GUI.this.cTileMap.setVisibleTiles(computeVisibleTiles());
			}
			else {
				System.out.println("GUI::defineStaticEntityLOS : invalid entity id " + e.entityId);
			}
		}

		public void createMap(MapCreationEvent e) {
			if(e == null) {
				return;
			}
			GUI.this.cTileMap.setMap(e.map);
		}

		public void tileClicked(TileClickEvent e) {
			fireInteractionRequestEvent(new InteractionRequestEvent(e.tilePosition));
		}

		public void modifieTile(TileModificationEvent e) {
			GUI.this.cTileMap.setTile(e.tileType, e.tilePosition);
		}

		public void playerNextTick(PlayerNextTickEvent event) {
			for(PlayerHUD playerHUD : GUI.this.playersHUD) {
				playerHUD.setMasked(true);
			}
			for(PlayerInventory playersInventory : GUI.this.playersInventories) {
				playersInventory.setVisible(false);
			}
			GUI.this.playersHUD.get(event.playerNumber).setMasked(false);
			GUI.this.playersInventories.get(event.playerNumber).setVisible(true);
		}

		public void objectClicked(ObjectClickEvent e) {
			fireUsageRequestEvent(new UsageRequestEvent(e.objectId));
		}

		public void addFog(FogAdditionEvent e) {
			GUI.this.cTileMap.addFoggedTiles(e.fogCenterPosition, e.fog);
		}

		public void resetFog(FogResetEvent e) {
			GUI.this.cTileMap.setAllTilesFogged(false);
		}

		public void centerOnTile(CenterOnTileEvent e) {
			GUI.this.cTileMap.centerViewOnTile(e.tilePosition);
		}

		public void updateLivingEntityHealth(LivingEntityHealthUpdateEvent e) {
			if(GUI.this.livingEntities.containsKey(e.entityId)) {
				GUI.this.livingEntities.get(e.entityId).setHealthProportion(e.newHealthProportion);
			}
			else {
				System.out.println("GUI::updateLivingEntityHealth : invalid entity id " + e.entityId);
			}
		}

		public void setLivingEntityHealthVisibility(LivingEntityHealthVisibilityEvent e) {
			if(GUI.this.livingEntities.containsKey(e.entityId)) {
				GUI.this.livingEntities.get(e.entityId).setHealthBarVisible(e.healthVisibility);
			}
			else {
				System.out.println("GUI::updateLivingEntityHealth : invalid entity id " + e.entityId);
			}
		}

		public void updateScore(ScoreUpdateEvent e) {
			GUI.this.rScoreDisplayer.setScore(e.newScore);
		}

		@Override
		public void handle(ActionEvent event) {
			Event gameEvent = GUI.this.eventQueue.poll();
			while(gameEvent != null) {
				switch(gameEvent.getType()) {
				case TILEMAP_EVENT: {
					TileMapEvent e = (TileMapEvent) gameEvent;
					switch(e.getSubType()) {
					case TILE_CLICK_EVENT:
						this.tileClicked((TileClickEvent) e);
						break;
					}
					break;
				}
				case STATIC_ENTITY_EVENT: {
					StaticEntityEvent e = (StaticEntityEvent) gameEvent;
					switch(e.getSubType()) {
					case STATIC_ENTITY_CREATION_EVENT:
						this.createStaticEntity((StaticEntityCreationEvent) e);
						break;
					case STATIC_ENTITY_DELETION_EVENT:
						this.deleteStaticEntity((StaticEntityDeletionEvent) e);
						break;
					case STATIC_ENTITY_LOS_DEFINITION_EVENT:
						this.defineStaticEntityLOS((StaticEntityLOSDefinitionEvent) e);
						break;
					}
					break;
				}
				case REQUEST_EVENT: {
					break;
				}
				case PLAYER_EVENT: {
					PlayerEvent e = (PlayerEvent) gameEvent;
					switch(e.getSubType()) {
					case INVENTORY_ADDITION_EVENT:
						this.addInventory((InventoryAdditionEvent) e);
						break;
					case INVENTORY_DELETION_EVENT:
						this.deleteInventory((InventoryDeletionEvent) e);
						break;
					case PLAYER_CREATION_EVENT:
						this.createPlayer((PlayerCreationEvent) e);
						break;
					case PLAYER_STAT_EVENT:
						this.changePlayerStat((PlayerStatEvent) e);
						break;
					case PLAYER_NEXT_TICK_EVENT:
						this.playerNextTick((PlayerNextTickEvent) e);
						break;
					}
					break;
				}
				case PLAYER_INVENTORY_EVENT: {
					PlayerInventoryEvent e = (PlayerInventoryEvent) gameEvent;
					switch(e.getSubType()) {
					case OJECT_CLICK_EVENT:
						this.objectClicked((ObjectClickEvent) e);
						break;
					}
					break;
				}
				case MAP_EVENT: {
					MapEvent e = (MapEvent) gameEvent;
					switch(e.getSubType()) {
					case CENTER_ON_TILE_EVENT:
						this.centerOnTile((CenterOnTileEvent) e);
						break;
					case FOG_ADDITION_EVENT:
						this.addFog((FogAdditionEvent) e);
						break;
					case FOG_RESET_EVENT:
						this.resetFog((FogResetEvent) e);
						break;
					case MAP_CREATION_EVENT:
						this.createMap((MapCreationEvent) e);
						break;
					case TILE_MODIFICATION_EVENT:
						this.modifieTile((TileModificationEvent) e);
						break;
					}
					break;
				}
				case LIVING_ENTITY_EVENT: {
					LivingEntityEvent e = (LivingEntityEvent) gameEvent;
					switch(e.getSubType()) {
					case LIVING_ENTITY_CREATION_EVENT:
						this.createLivingEntity((LivingEntityCreationEvent) e);
						break;
					case LIVING_ENTITY_DELETION_EVENT:
						this.deleteLivingEntity((LivingEntityDeletionEvent) e);
						break;
					case LIVING_ENTITY_LOS_DEFINITION_EVENT:
						this.defineLivingEntityLOS((LivingEntityLOSDefinitionEvent) e);
						break;
					case LIVING_ENTITY_LOS_MODIFICATION_EVENT:
						this.modifieLivingEntityLOS((LivingEntityLOSModificationEvent) e);
						break;
					case LIVING_ENTITY_MOVE_EVENT:
						this.moveLivingEntity((LivingEntityMoveEvent) e);
						break;
					case LIVING_ENTITY_HEALTH_UPDATE_EVENT:
						updateLivingEntityHealth((LivingEntityHealthUpdateEvent) e);
						break;
					case LIVING_ENTITY_HEALTH_VISIBILITY_EVENT:
						setLivingEntityHealthVisibility((LivingEntityHealthVisibilityEvent) e);
						break;
					}
					break;
				}
				case SCORE_UPDATE_EVENT: {
					updateScore((ScoreUpdateEvent) gameEvent);
					break;
				}
				}
				gameEvent = GUI.this.eventQueue.poll();
			}
		}
	};

	private final Timeline timeline = new Timeline(new KeyFrame(REFRESH_DURATION, this.eventExecutor));

	/**
	 * Instantiates a new GUI.
	 */
	public GUI() {
		this.init();
	}

	private void init() {

		this.scene.setOnKeyPressed(this.onKeyReleasedEventHandler);

		//Main pane
		this.anchorPane.setBackground(new Background(new BackgroundFill(Color.RED, CornerRadii.EMPTY, Insets.EMPTY)));
		this.anchorPane.setMinWidth(ANCHOR_PANE_MIN_SIZE.x);
		this.anchorPane.setMinHeight(ANCHOR_PANE_MIN_SIZE.y);

		//Center pane
		this.anchorPane.getChildren().add(this.cPane);
		AnchorPane.setLeftAnchor(this.cPane, 0d);
		this.cPane.prefHeightProperty().bind(this.anchorPane.heightProperty());
		this.cPane.prefWidthProperty().bind(this.anchorPane.widthProperty());
		this.cPane.setBackground(new Background(new BackgroundFill(CENTER_BACKGROUND_COLOR, CornerRadii.EMPTY, Insets.EMPTY)));
		this.cPane.getChildren().add(this.cTileMap);
		this.cTileMap.addTileMapListener(this);

		//Bootom pane
		this.anchorPane.getChildren().add(this.bPane);
		AnchorPane.setBottomAnchor(this.bPane, 0d);
		this.bHBox.getChildren().add(this.blTilePane);
		this.bHBox.prefWidthProperty().bind(this.bPane.widthProperty());
		this.bPane.getChildren().add(this.bHBox);
		this.bPane.setBackground(new Background(new BackgroundFill(BOTTOM_BACKGROUND_COLOR, CornerRadii.EMPTY, Insets.EMPTY)));
		this.bPane.prefWidthProperty().bind(this.anchorPane.widthProperty());
		this.bPane.prefHeightProperty().bind(this.bHBox.heightProperty());
		this.bPane.maxHeightProperty().bind(this.bHBox.heightProperty());

		//Right pane
		this.anchorPane.getChildren().add(this.rPane);
		AnchorPane.setRightAnchor(this.rPane, 0d);
		this.bPane.prefHeightProperty().bind(this.anchorPane.heightProperty());
		this.rVBox.setBackground(new Background(new BackgroundFill(RIGHT_BACKGROUND_COLOR1, CornerRadii.EMPTY, Insets.EMPTY)));
		this.rVBox.getChildren().add(this.rScoreDisplayer);
		this.rVBox.getChildren().add(this.rIventoryPane);
		this.rVBox.getChildren().add(InformationsDisplayer.getInstance());
		this.rPane.setBackground(new Background(new BackgroundFill(RIGHT_BACKGROUND_COLOR2, CornerRadii.EMPTY, Insets.EMPTY)));
		this.rPane.getChildren().add(this.rVBox);

		this.timeline.setCycleCount(Timeline.INDEFINITE);
		this.timeline.play();
	}

	/**
	 * Gets the scene.
	 *
	 * @return the scene
	 */
	public Scene getScene() {
		return this.scene;
	}

	/**
	 * Compute visible tiles.
	 *
	 * @return the tiles visibility
	 */
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

	/**
	 * Add a RequestListener.
	 *
	 * @param listener the listener
	 */
	public void addRequestListener(RequestListener listener) {
		this.listeners.add(listener);
	}

	/**
	 * Get a copy of the list of RequestListener as an array.
	 *
	 * @return the RequestListener array
	 */
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
		this.eventQueue.add(e);
	}

	@Override
	public void deleteInventory(InventoryDeletionEvent e) {
		this.eventQueue.add(e);
	}

	@Override
	public void createLivingEntity(LivingEntityCreationEvent e) {
		this.eventQueue.add(e);
	}

	@Override
	public void deleteLivingEntity(LivingEntityDeletionEvent e) {
		this.eventQueue.add(e);
	}

	@Override
	public void defineLivingEntityLOS(LivingEntityLOSDefinitionEvent e) {
		this.eventQueue.add(e);
	}

	@Override
	public void modifieLivingEntityLOS(LivingEntityLOSModificationEvent e) {
		this.eventQueue.add(e);
	}

	@Override
	public void moveLivingEntity(LivingEntityMoveEvent e) {
		this.eventQueue.add(e);
	}

	@Override
	public void createPlayer(PlayerCreationEvent e) {
		this.eventQueue.add(e);
	}

	@Override
	public void changePlayerStat(PlayerStatEvent e) {
		this.eventQueue.add(e);
	}

	@Override
	public void createStaticEntity(StaticEntityCreationEvent e) {
		this.eventQueue.add(e);
	}

	@Override
	public void deleteStaticEntity(StaticEntityDeletionEvent e) {
		this.eventQueue.add(e);
	}

	@Override
	public void defineStaticEntityLOS(StaticEntityLOSDefinitionEvent e) {
		this.eventQueue.add(e);
	}

	@Override
	public void createMap(MapCreationEvent e) {
		this.eventQueue.add(e);
	}

	@Override
	public void tileClicked(TileClickEvent e) {
		this.eventQueue.add(e);
	}

	@Override
	public void modifieTile(TileModificationEvent e) {
		this.eventQueue.add(e);
	}

	@Override
	public void playerNextTick(PlayerNextTickEvent e) {
		this.eventQueue.add(e);
	}

	@Override
	public void objectClicked(ObjectClickEvent e) {
		this.eventQueue.add(e);
	}

	@Override
	public void addFog(FogAdditionEvent e) {
		this.eventQueue.add(e);
	}

	@Override
	public void resetFog(FogResetEvent e) {
		this.eventQueue.add(e);
	}

	@Override
	public void centerOnTile(CenterOnTileEvent e) {
		this.eventQueue.add(e);
	}

	@Override
	public void updateLivingEntityHealth(LivingEntityHealthUpdateEvent e) {
		this.eventQueue.add(e);
	}

	@Override
	public void setLivingEntityHealthVisibility(LivingEntityHealthVisibilityEvent e) {
		this.eventQueue.add(e);
	}

	@Override
	public void updateScore(ScoreUpdateEvent e) {
		this.eventQueue.add(e);
	}
}
