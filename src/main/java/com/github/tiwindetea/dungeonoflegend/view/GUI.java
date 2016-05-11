package com.github.tiwindetea.dungeonoflegend.view;

import com.github.tiwindetea.dungeonoflegend.events.living_entities.LivingEntityCreationEvent;
import com.github.tiwindetea.dungeonoflegend.events.living_entities.LivingEntityDeletionEvent;
import com.github.tiwindetea.dungeonoflegend.events.living_entities.LivingEntityLOSDefinitionEvent;
import com.github.tiwindetea.dungeonoflegend.events.living_entities.LivingEntityLOSModificationEvent;
import com.github.tiwindetea.dungeonoflegend.events.living_entities.LivingEntityMoveEvent;
import com.github.tiwindetea.dungeonoflegend.events.map.MapCreationEvent;
import com.github.tiwindetea.dungeonoflegend.events.players.PlayerCreationEvent;
import com.github.tiwindetea.dungeonoflegend.events.players.PlayerStatEvent;
import com.github.tiwindetea.dungeonoflegend.events.players.inventory.InventoryAdditionEvent;
import com.github.tiwindetea.dungeonoflegend.events.players.inventory.InventoryDeletionEvent;
import com.github.tiwindetea.dungeonoflegend.events.requests.InteractionRequestEvent;
import com.github.tiwindetea.dungeonoflegend.events.requests.MoveRequestEvent;
import com.github.tiwindetea.dungeonoflegend.events.requests.inventory.DropRequestEvent;
import com.github.tiwindetea.dungeonoflegend.events.requests.inventory.UsageRequestEvent;
import com.github.tiwindetea.dungeonoflegend.events.static_entities.StaticEntityCreationEvent;
import com.github.tiwindetea.dungeonoflegend.events.static_entities.StaticEntityDeletionEvent;
import com.github.tiwindetea.dungeonoflegend.events.static_entities.StaticEntityLOSDefinitionEvent;
import com.github.tiwindetea.dungeonoflegend.events.tilemap.TileClickEvent;
import com.github.tiwindetea.dungeonoflegend.listeners.game.GameListener;
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
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by maxime on 5/2/16.
 */
public class GUI implements GameListener, TileMapListener {
	private final List<RequestListener> listeners = new ArrayList<>();

	public static final Color BOTTOM_BACKGROUND_COLOR = Color.GREEN;
	public static final Color RIGHT_BACKGROUND_COLOR = Color.CRIMSON;
	public static final Color CENTER_BACKGROUND_COLOR = Color.BLACK;

	private final BorderPane borderPane = new BorderPane();
	private final Scene scene = new Scene(this.borderPane);
	private final Pane rPane = new Pane();
	private final Pane cPane = new Pane();

	private final Pane bPane = new Pane();
	private final HBox bHBox = new HBox();
	private final TilePane blTilePane = new TilePane();
	private final Pane brMiniMapPain = new Pane();

	private final HashMap<Long, LivingEntity> livingEntities = new HashMap<>();
	private final HashMap<Long, StaticEntity> staticEntities = new HashMap<>();

	private final List<PlayerHUD> playersHUD = new ArrayList<>();
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
			}
		}
	};

	public GUI() {
		this.init();
	}

	private void init() {

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
		this.rPane.setBackground(new Background(new BackgroundFill(RIGHT_BACKGROUND_COLOR, CornerRadii.EMPTY, Insets.EMPTY)));
		this.rPane.setMaxWidth(300);
		this.rPane.setMinWidth(300);

		this.brMiniMapPain.setBackground(new Background(new BackgroundFill(Color.CYAN, CornerRadii.EMPTY, Insets.EMPTY)));
		this.brMiniMapPain.setMinWidth(300);
		this.brMiniMapPain.setMaxWidth(300);

		this.bHBox.getChildren().addAll(this.blTilePane, this.brMiniMapPain);
		this.bHBox.setMinWidth(2 * PlayerHUD.getSize().x + this.brMiniMapPain.getMinWidth());

		this.bPane.setBackground(new Background(new BackgroundFill(BOTTOM_BACKGROUND_COLOR, CornerRadii.EMPTY, Insets.EMPTY)));
		this.bPane.setMinHeight(this.playersHUD.size() * PlayerHUD.getSize().y);
		this.bPane.getChildren().add(this.bHBox);
		this.bPane.widthProperty().addListener(e -> {
			if(this.playersHUD.size() > 1) {
				if((this.bPane.getWidth() - this.rPane.getWidth()) < (this.playersHUD.size() * PlayerHUD.getSize().x)) {
					this.bPane.setMinHeight(this.playersHUD.size() * PlayerHUD.getSize().y);
				}
				else {
					this.bPane.setMinHeight(PlayerHUD.getSize().y);
				}
			}
			this.blTilePane.setMaxWidth(this.bPane.getWidth() - this.rPane.getWidth());
		});

		this.scene.setOnKeyReleased(this.onKeyReleasedEventHandler);
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

	@Override
	public void addInventory(InventoryAdditionEvent e) {
		if(e == null) {
			return;
		}
		//TODO
	}

	@Override
	public void deleteInventory(InventoryDeletionEvent e) {
		if(e == null) {
			return;
		}
		//TODO
	}

	@Override
	public void createLivingEntity(LivingEntityCreationEvent e) {
		if(e == null) {
			return;
		}
		LivingEntity livingEntity = new LivingEntity(e.type, e.position, e.direction);
		this.livingEntities.put(e.entityId, livingEntity);
		this.cTileMap.addEntity(livingEntity);
	}

	@Override
	public void deleteLivingEntity(LivingEntityDeletionEvent e) {
		if(e == null) {
			return;
		}
		this.cTileMap.removeEntity(this.livingEntities.get(e.entityId));
		this.livingEntities.remove(e.entityId);
	}

	@Override
	public void defineLivingEntityLOS(LivingEntityLOSDefinitionEvent e) {
		if(e == null) {
			return;
		}
		this.livingEntities.get(e.entityId).setLOS(e.newLOS);
		this.cTileMap.setVisibleTiles(computeVisibleTiles());
	}

	@Override
	public void modifieLivingEntityLOS(LivingEntityLOSModificationEvent e) {
		if(e == null) {
			return;
		}
		this.livingEntities.get(e.entityId).modifieLOS(e.modifiedTilesPositions);
		this.cTileMap.setVisibleTiles(computeVisibleTiles());
	}

	@Override
	public void moveLivingEntity(LivingEntityMoveEvent e) {
		if(e == null) {
			return;
		}
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

	@Override
	public void createPlayer(PlayerCreationEvent e) {
		if(e == null) {
			return;
		}
		if(this.actualPlayersNumber < this.maxPlayersNumber) {
			LivingEntityType livingEntityType;
			if(e.playerNumber == 2) {
				livingEntityType = LivingEntityType.PLAYER2;
			}
			else {
				livingEntityType = LivingEntityType.PLAYER1;
			}
			LivingEntity livingEntity = new LivingEntity(livingEntityType, e.position, e.direction);
			this.livingEntities.put(e.entityId, livingEntity);
			this.cTileMap.addEntity(livingEntity);
			++this.actualPlayersNumber;

			ImageView imageView = new ImageView(livingEntityType.getImage());
			Vector2i spritePosition = livingEntityType.getSpritePosition(e.direction);
			imageView.setViewport(new Rectangle2D(spritePosition.x * ViewPackage.spritesSize.x, spritePosition.y * ViewPackage.spritesSize.y, ViewPackage.spritesSize.x, ViewPackage.spritesSize.y));
			PlayerHUD playerHUD = new PlayerHUD(imageView, e.maxHealth, e.maxHealth, e.maxMana, e.maxMana);
			this.playersHUD.add(playerHUD);
			this.blTilePane.getChildren().add(playerHUD);
			if(this.playersHUD.size() > 1) {
				if((this.bPane.getWidth() - this.rPane.getWidth()) < (this.playersHUD.size() * PlayerHUD.getSize().x)) {
					this.bPane.setMinHeight(this.playersHUD.size() * PlayerHUD.getSize().y);
				}
				else {
					this.bPane.setMinHeight(PlayerHUD.getSize().y);
				}
			}

			//TODO: inventory
		}
	}

	@Override
	public void changePlayerStat(PlayerStatEvent e) {
		if(e == null) {
			return;
		}
		if(e.playerNumber > 0 && e.playerNumber <= this.actualPlayersNumber) {
			int value = e.value;
			if(value < 0) {
				value = 0;
			}
			switch(e.statType) {
			case HEALTH:
				switch(e.valueType) {
				case ACTUAL:
					this.playersHUD.get(e.playerNumber - 1).setActualHealth(value);
					break;
				case MAX:
					this.playersHUD.get(e.playerNumber - 1).setMaxHealth(value);
					break;
				}
				break;
			case MANA:
				switch(e.valueType) {
				case ACTUAL:
					this.playersHUD.get(e.playerNumber - 1).setActualMana(value);
					break;
				case MAX:
					this.playersHUD.get(e.playerNumber - 1).setMaxMana(value);
					break;
				}
				break;
			}
		}
	}

	@Override
	public void createStaticEntity(StaticEntityCreationEvent e) {
		if(e == null) {
			return;
		}
		StaticEntity staticEntity = new StaticEntity(e.type, e.position);
		this.staticEntities.put(e.entityId, staticEntity);
		this.cTileMap.addEntity(staticEntity);
	}

	@Override
	public void deleteStaticEntity(StaticEntityDeletionEvent e) {
		if(e == null) {
			return;
		}
		this.cTileMap.removeEntity(this.staticEntities.get(e.entityId));
		this.staticEntities.remove(e.entityId);
	}

	@Override
	public void defineStaticEntityLOS(StaticEntityLOSDefinitionEvent e) {
		if(e == null) {
			return;
		}
		this.staticEntities.get(e.entityId).setLOS(e.newLOS);
		this.cTileMap.setVisibleTiles(computeVisibleTiles());
	}

	@Override
	public void createMap(MapCreationEvent e) {
		if(e == null) {
			return;
		}
		this.cTileMap.setMap(e.map);
	}

	@Override
	public void tileClicked(TileClickEvent e) {
		fireInteractionRequestEvent(new InteractionRequestEvent(e.tilePosition));
	}
}
