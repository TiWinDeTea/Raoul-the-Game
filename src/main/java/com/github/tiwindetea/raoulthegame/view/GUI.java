//////////////////////////////////////////////////////////////////////////////////
//                                                                              //
//     This Source Code Form is subject to the terms of the Mozilla Public      //
//     License, v. 2.0. If a copy of the MPL was not distributed with this      //
//     file, You can obtain one at http://mozilla.org/MPL/2.0/.                 //
//                                                                              //
//////////////////////////////////////////////////////////////////////////////////

package com.github.tiwindetea.raoulthegame.view;

import com.github.tiwindetea.raoulthegame.events.Event;
import com.github.tiwindetea.raoulthegame.events.game.LevelUpdateEvent;
import com.github.tiwindetea.raoulthegame.events.game.ScoreUpdateEvent;
import com.github.tiwindetea.raoulthegame.events.game.SpellSelectionEvent;
import com.github.tiwindetea.raoulthegame.events.game.living_entities.LivingEntityCreationEvent;
import com.github.tiwindetea.raoulthegame.events.game.living_entities.LivingEntityDeletionEvent;
import com.github.tiwindetea.raoulthegame.events.game.living_entities.LivingEntityEvent;
import com.github.tiwindetea.raoulthegame.events.game.living_entities.LivingEntityHealthUpdateEvent;
import com.github.tiwindetea.raoulthegame.events.game.living_entities.LivingEntityHealthVisibilityEvent;
import com.github.tiwindetea.raoulthegame.events.game.living_entities.LivingEntityLOSDefinitionEvent;
import com.github.tiwindetea.raoulthegame.events.game.living_entities.LivingEntityLOSModificationEvent;
import com.github.tiwindetea.raoulthegame.events.game.living_entities.LivingEntityManaUpdateEvent;
import com.github.tiwindetea.raoulthegame.events.game.living_entities.LivingEntityMoveEvent;
import com.github.tiwindetea.raoulthegame.events.game.living_entities.LivingEntityXpUpdateEvent;
import com.github.tiwindetea.raoulthegame.events.game.map.CenterOnTileEvent;
import com.github.tiwindetea.raoulthegame.events.game.map.FogAdditionEvent;
import com.github.tiwindetea.raoulthegame.events.game.map.FogResetEvent;
import com.github.tiwindetea.raoulthegame.events.game.map.MapCreationEvent;
import com.github.tiwindetea.raoulthegame.events.game.map.MapEvent;
import com.github.tiwindetea.raoulthegame.events.game.map.TileModificationEvent;
import com.github.tiwindetea.raoulthegame.events.game.players.PlayerCreationEvent;
import com.github.tiwindetea.raoulthegame.events.game.players.PlayerDeletionEvent;
import com.github.tiwindetea.raoulthegame.events.game.players.PlayerEvent;
import com.github.tiwindetea.raoulthegame.events.game.players.PlayerNextTickEvent;
import com.github.tiwindetea.raoulthegame.events.game.players.PlayerStatEvent;
import com.github.tiwindetea.raoulthegame.events.game.players.inventory.InventoryAdditionEvent;
import com.github.tiwindetea.raoulthegame.events.game.players.inventory.InventoryDeletionEvent;
import com.github.tiwindetea.raoulthegame.events.game.spells.SpellCooldownUpdateEvent;
import com.github.tiwindetea.raoulthegame.events.game.spells.SpellCreationEvent;
import com.github.tiwindetea.raoulthegame.events.game.spells.SpellDeletionEvent;
import com.github.tiwindetea.raoulthegame.events.game.spells.SpellDescriptionUpdateEvent;
import com.github.tiwindetea.raoulthegame.events.game.spells.SpellEvent;
import com.github.tiwindetea.raoulthegame.events.game.static_entities.StaticEntityCreationEvent;
import com.github.tiwindetea.raoulthegame.events.game.static_entities.StaticEntityDeletionEvent;
import com.github.tiwindetea.raoulthegame.events.game.static_entities.StaticEntityEvent;
import com.github.tiwindetea.raoulthegame.events.game.static_entities.StaticEntityLOSDefinitionEvent;
import com.github.tiwindetea.raoulthegame.events.gui.playerhud.HUDSpellClickEvent;
import com.github.tiwindetea.raoulthegame.events.gui.playerinventory.ObjectClickEvent;
import com.github.tiwindetea.raoulthegame.events.gui.playerinventory.ObjectDragEvent;
import com.github.tiwindetea.raoulthegame.events.gui.playerinventory.PlayerInventoryEvent;
import com.github.tiwindetea.raoulthegame.events.gui.requests.CastSpellRequestEvent;
import com.github.tiwindetea.raoulthegame.events.gui.requests.CenterViewRequestEvent;
import com.github.tiwindetea.raoulthegame.events.gui.requests.InteractionRequestEvent;
import com.github.tiwindetea.raoulthegame.events.gui.requests.LockViewRequestEvent;
import com.github.tiwindetea.raoulthegame.events.gui.requests.MoveRequestEvent;
import com.github.tiwindetea.raoulthegame.events.gui.requests.SpellSelectedRequestEvent;
import com.github.tiwindetea.raoulthegame.events.gui.requests.inventory.DropRequestEvent;
import com.github.tiwindetea.raoulthegame.events.gui.requests.inventory.EquipRequestEvent;
import com.github.tiwindetea.raoulthegame.events.gui.requests.inventory.UsageRequestEvent;
import com.github.tiwindetea.raoulthegame.events.gui.spellselector.SelectorSpellClickEvent;
import com.github.tiwindetea.raoulthegame.events.gui.tilemap.TileClickEvent;
import com.github.tiwindetea.raoulthegame.events.gui.tilemap.TileDragEvent;
import com.github.tiwindetea.raoulthegame.events.gui.tilemap.TileMapEvent;
import com.github.tiwindetea.raoulthegame.listeners.game.GameListener;
import com.github.tiwindetea.raoulthegame.listeners.gui.playerhud.HUDSpellClickListener;
import com.github.tiwindetea.raoulthegame.listeners.gui.playerinventory.PlayerInventoryListener;
import com.github.tiwindetea.raoulthegame.listeners.gui.request.RequestListener;
import com.github.tiwindetea.raoulthegame.listeners.gui.spellselector.SelectorSpellClickListener;
import com.github.tiwindetea.raoulthegame.listeners.gui.tilemap.TileMapListener;
import com.github.tiwindetea.raoulthegame.model.space.Direction;
import com.github.tiwindetea.raoulthegame.model.space.Vector2i;
import com.github.tiwindetea.raoulthegame.view.entities.LivingEntity;
import com.github.tiwindetea.raoulthegame.view.entities.StaticEntity;
import com.github.tiwindetea.soundplayer.Sound;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.SimpleBooleanProperty;
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
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
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
public class GUI implements GameListener, TileMapListener, PlayerInventoryListener, HUDSpellClickListener, SelectorSpellClickListener {
	private final List<RequestListener> listeners = new ArrayList<>();

	private static final Duration REFRESH_DURATION = Duration.millis(100);
	private static final int MOVE_REQUEST_DELAY = 80;

	private static final Color BOTTOM_BACKGROUND_COLOR = Color.TRANSPARENT;
	private static final Color RIGHT_BACKGROUND_COLOR1 = Color.rgb(53, 53, 53);
	private static final Color RIGHT_BACKGROUND_COLOR2 = Color.DARKGRAY;
	private static final Color CENTER_BACKGROUND_COLOR = Color.BLACK;
	private static final Color BLOCKING_BACKGROUND_COLOR = Color.rgb(100, 100, 100, 0.9);

	private static final Vector2i ANCHOR_PANE_MIN_SIZE = new Vector2i(1000, 500);

	private final AnchorPane anchorPane = new AnchorPane();
	private final Scene scene = new Scene(this.anchorPane);

	private final Pane cPane = new Pane();

	private final Pane bPane = new Pane();
	private final VBox bVBox = new VBox();

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

	private final SimpleBooleanProperty blockInput = new SimpleBooleanProperty(false);
	private final StackPane blockPane = new StackPane();

	private final Queue<Event> eventQueue = new LinkedList<>();

	private boolean moveRencentlyRequested = false;

	private final EventHandler<KeyEvent> onKeyReleasedEventHandler = new EventHandler<KeyEvent>() {

		@Override
		public void handle(KeyEvent event) {
			if(!GUI.this.moveRencentlyRequested && !GUI.this.blockInput.get()) {

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
				case M:
					Sound.player.setStopped(!Sound.player.getStopped());
					break;
				case Y:
					fireLockViewRequestEvent(new LockViewRequestEvent());
					break;
				default:
					break;
				}
			}
		}
	};

	private final EventHandler<ActionEvent> eventExecutor = new EventHandler<ActionEvent>() {

		public void addInventory(InventoryAdditionEvent e) {
			if(e.isEquiped()	) {
				GUI.this.playersInventories.get(e.getPlayerNumber()).addEquipedItem(e.getObjectId(), new StaticEntity(e.getStaticEntityType(), e.getDescription(), e.getObjectId()));
			}
			else {
				GUI.this.playersInventories.get(e.getPlayerNumber()).addInventoryItem(e.getObjectId(), new StaticEntity(e.getStaticEntityType(), e.getDescription(), e.getObjectId()));
			}
		}

		public void deleteInventory(InventoryDeletionEvent e) {
			if(e.getPlayerNumber() < GUI.this.actualPlayersNumber) {
				GUI.this.playersInventories.get(e.getPlayerNumber()).removeItem(e.getObjectId());
			}
			else {
				System.out.println("GUI::deleteInventory : invalid player number " + e.getPlayerNumber());
			}
		}

		public void createLivingEntity(LivingEntityCreationEvent e) {
			LivingEntity livingEntity = new LivingEntity(e.getLivingEntityType(), e.getPosition(), e.getDirection(), e.getDescription());
			GUI.this.livingEntities.put(e.getEntityId(), livingEntity);
			GUI.this.cTileMap.addEntity(livingEntity);
		}

		public void deleteLivingEntity(LivingEntityDeletionEvent e) {
			if(GUI.this.livingEntities.containsKey(e.getEntityId())) {
				GUI.this.cTileMap.removeEntity(GUI.this.livingEntities.get(e.getEntityId()));
				GUI.this.livingEntities.remove(e.getEntityId());
			}
			else {
				System.out.println("GUI::deleteLivingEntity : invalid entity id " + e.getEntityId());
			}
		}

		public void defineLivingEntityLOS(LivingEntityLOSDefinitionEvent e) {
			if(GUI.this.livingEntities.containsKey(e.getEntityId())) {
				GUI.this.livingEntities.get(e.getEntityId()).setLOS(e.getNewLOS());
				GUI.this.cTileMap.setVisibleTiles(computeVisibleTiles());
			}
			else {
				System.out.println("GUI::defineLivingEntityLOS : invalid entity id " + e.getEntityId());
			}
		}

		public void modifieLivingEntityLOS(LivingEntityLOSModificationEvent e) {
			if(GUI.this.livingEntities.containsKey(e.getEntityId())) {
				GUI.this.livingEntities.get(e.getEntityId()).modifieLOS(e.getModifiedTilesPositions());
				GUI.this.cTileMap.setVisibleTiles(computeVisibleTiles());
			}
			else {
				System.out.println("GUI::modifieLivingEntityLOS : invalid entity id " + e.getEntityId());
			}
		}

		public void moveLivingEntity(LivingEntityMoveEvent e) {
			if(GUI.this.livingEntities.containsKey(e.getEntityId())) {
				Vector2i oldPosition = GUI.this.livingEntities.get(e.getEntityId()).getPosition();
				Vector2i newPosition = e.getNewPosition();
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
					direction = GUI.this.livingEntities.get(e.getEntityId()).getDirection();
				}
				GUI.this.livingEntities.get(e.getEntityId()).setDirection(direction);
				GUI.this.livingEntities.get(e.getEntityId()).setPosition(e.getNewPosition());
				GUI.this.cTileMap.setVisibleTiles(computeVisibleTiles());
			}
			else {
				System.out.println("GUI::moveLivingEntity : invalid entity id " + e.getEntityId());
			}
		}

		public void createPlayer(PlayerCreationEvent e) {
			if(GUI.this.actualPlayersNumber < GUI.this.maxPlayersNumber) {

				ImageView imageView1 = new ImageView(e.getPlayerType().getImage());
				ImageView imageView2 = new ImageView(e.getPlayerType().getImage());
				Vector2i spritePosition = e.getPlayerType().getSpritePosition(Direction.DOWN);
				imageView1.setViewport(new Rectangle2D(spritePosition.x * ViewPackage.SPRITES_SIZE.x, spritePosition.y * ViewPackage.SPRITES_SIZE.y, ViewPackage.SPRITES_SIZE.x, ViewPackage.SPRITES_SIZE.y));
				imageView2.setViewport(new Rectangle2D(spritePosition.x * ViewPackage.SPRITES_SIZE.x, spritePosition.y * ViewPackage.SPRITES_SIZE.y, ViewPackage.SPRITES_SIZE.x, ViewPackage.SPRITES_SIZE.y));
				PlayerHUD playerHUD = new PlayerHUD(GUI.this.actualPlayersNumber, imageView1, e.getMaxHealth(), e.getMaxHealth(), e.getMaxMana(), e.getMaxMana(), 0, e.getMaxXP(), e.getLevel(), e.getPlayerName(), GUI.this);
				GUI.this.playersHUD.add(playerHUD);
				GUI.this.bVBox.getChildren().add(playerHUD);

				PlayerInventory playerInventory = new PlayerInventory(imageView2);
				GUI.this.playersInventories.add(playerInventory);
				GUI.this.rIventoryPane.getChildren().add(playerInventory);
				playerInventory.addPlayerInventoryListener(GUI.this);
				++GUI.this.actualPlayersNumber;
			}
			else {
				System.out.println("GUI::createPlayer : too much players - " + GUI.this.actualPlayersNumber);
			}
		}

		public void deletePlayer(PlayerDeletionEvent e) {
			if(GUI.this.actualPlayersNumber > 0) {
				--GUI.this.actualPlayersNumber;
				GUI.this.bVBox.getChildren().remove(GUI.this.playersHUD.get(e.getPlayerNumber()));
				GUI.this.playersHUD.remove(e.getPlayerNumber());
				GUI.this.rIventoryPane.getChildren().remove(GUI.this.playersInventories.get(e.getPlayerNumber()));
				GUI.this.playersInventories.remove(e.getPlayerNumber());
			}
			else {
				System.out.println("GUI::deletePlayer : invalid player number - " + e.getPlayerNumber());
			}
		}

		public void changePlayerStat(PlayerStatEvent e) {
			if(e == null) {
				return;
			}
			if(e.getPlayerNumber() >= 0 && e.getPlayerNumber() < GUI.this.actualPlayersNumber) {
				int value = e.getValue();
				if(value < 0) {
					value = 0;
				}
				switch(e.getStatType()) {
				case HEALTH:
					switch(e.getValueType()) {
					case ACTUAL:
						GUI.this.playersHUD.get(e.getPlayerNumber()).setActualHealth(value);
						break;
					case MAX:
						GUI.this.playersHUD.get(e.getPlayerNumber()).setMaxHealth(value);
						break;
					}
					break;
				case MANA:
					switch(e.getValueType()) {
					case ACTUAL:
						GUI.this.playersHUD.get(e.getPlayerNumber()).setActualMana(value);
						break;
					case MAX:
						GUI.this.playersHUD.get(e.getPlayerNumber()).setMaxMana(value);
						break;
					}
					break;
				case XP:
					switch(e.getValueType()) {
					case ACTUAL:
						GUI.this.playersHUD.get(e.getPlayerNumber()).setActualXP(value);
						break;
					case MAX:
						GUI.this.playersHUD.get(e.getPlayerNumber()).setMaxXP(value);
						break;
					}
					break;
				case LEVEL:
					GUI.this.playersHUD.get(e.getPlayerNumber()).setActualLevel(value);
					break;
				case DAMAGES:
					GUI.this.playersHUD.get(e.getPlayerNumber()).setActualDamages(value);
					break;
				case ARMOR:
					GUI.this.playersHUD.get(e.getPlayerNumber()).setActualArmor(value);
					break;
				case RANGE:
					GUI.this.playersHUD.get(e.getPlayerNumber()).setActualRange(value);
					break;
				case POWER_GRADE:
					GUI.this.playersHUD.get(e.getPlayerNumber()).setActualPowerGrade(value);
					break;
				}
			}
			else {
				System.out.println("GUI::changePlayerStat : invalid player number " + e.getPlayerNumber());
			}
		}

		public void createStaticEntity(StaticEntityCreationEvent e) {
			if(e == null) {
				return;
			}
			StaticEntity staticEntity = new StaticEntity(e.getStaticEntityType(), e.getPosition(), e.getDescription());
			GUI.this.staticEntities.put(e.getEntityId(), staticEntity);
			GUI.this.cTileMap.addEntity(staticEntity);
		}

		public void deleteStaticEntity(StaticEntityDeletionEvent e) {
			if(e == null) {
				return;
			}
			if(GUI.this.staticEntities.containsKey(e.getEntityId())) {
				StaticEntity entity = GUI.this.staticEntities.remove(e.getEntityId());
				GUI.this.cTileMap.removeEntity(entity);
				if (entity != null) {
					entity.stopAnimation();
				}
			}
			else {
				System.out.println("GUI::deleteStaticEntity : invalid entity id " + e.getEntityId());
			}
		}

		public void defineStaticEntityLOS(StaticEntityLOSDefinitionEvent e) {
			if(e == null) {
				return;
			}
			if(GUI.this.staticEntities.containsKey(e.getEntityId())) {
				GUI.this.staticEntities.get(e.getEntityId()).setLOS(e.getNewLOS());
				GUI.this.cTileMap.setVisibleTiles(computeVisibleTiles());
			}
			else {
				System.out.println("GUI::defineStaticEntityLOS : invalid entity id " + e.getEntityId());
			}
		}

		public void createMap(MapCreationEvent e) {
			if(e == null) {
				return;
			}
			GUI.this.cTileMap.setMap(e.getMap());
		}

		public void tileClicked(TileClickEvent e) {
			fireInteractionRequestEvent(new InteractionRequestEvent(e.getTilePosition()));
		}

		public void tileDragged(TileDragEvent e) {
			fireDropRequestEvent(new DropRequestEvent(e.getObjectId(), e.getTilePosition()));
		}

		public void modifieTile(TileModificationEvent e) {
			GUI.this.cTileMap.setTile(e.getTileType(), e.getTilePosition());
		}

		public void playerNextTick(PlayerNextTickEvent event) {
			for(PlayerHUD playerHUD : GUI.this.playersHUD) {
				playerHUD.setMasked(true);
			}
			for(PlayerInventory playersInventory : GUI.this.playersInventories) {
				playersInventory.setVisible(false);
			}
			GUI.this.playersHUD.get(event.getPlayerNumber()).setMasked(false);
			GUI.this.playersInventories.get(event.getPlayerNumber()).setVisible(true);
		}

		public void objectClicked(ObjectClickEvent e) {
			fireUsageRequestEvent(new UsageRequestEvent(e.getObjectId()));
		}

		public void objectDragged(ObjectDragEvent e) {
			fireEquipEvent(new EquipRequestEvent(e.getObjectId()));
		}

		public void addFog(FogAdditionEvent e) {
			GUI.this.cTileMap.addFoggedTiles(e.getFogCenterPosition(), e.getFog());
		}

		public void resetFog(FogResetEvent e) {
			GUI.this.cTileMap.setAllTilesFogged(false);
		}

		public void centerOnTile(CenterOnTileEvent e) {
			GUI.this.cTileMap.centerViewOnTile(e.getTilePosition());
		}

		public void updateLivingEntityHealth(LivingEntityHealthUpdateEvent e) {
			if(GUI.this.livingEntities.containsKey(e.getEntityId())) {
				GUI.this.livingEntities.get(e.getEntityId()).setHealthProportion(e.getNewHealthProportion());
				GUI.this.livingEntities.get(e.getEntityId()).displayHealthModification(e.getHealthDiff());
			}
			else {
				System.out.println("GUI::updateLivingEntityHealth : invalid entity id " + e.getEntityId());
			}
		}

		public void setLivingEntityHealthVisibility(LivingEntityHealthVisibilityEvent e) {
			if(GUI.this.livingEntities.containsKey(e.getEntityId())) {
				GUI.this.livingEntities.get(e.getEntityId()).setHealthBarVisible(e.isHealthVisibility());
			}
			else {
				System.out.println("GUI::setLivingEntityHealthVisibility : invalid entity id " + e.getEntityId());
			}
		}

		public void updateScore(ScoreUpdateEvent e) {
			GUI.this.rScoreDisplayer.setScore(e.getNewScore());
		}

		public void updateLevel(LevelUpdateEvent e) {
			GUI.this.rScoreDisplayer.setLevel(e.getNewLevel());
		}

		public void updateLivingEntityMana(LivingEntityManaUpdateEvent e) {
			if(GUI.this.livingEntities.containsKey(e.getEntityId())) {
				GUI.this.livingEntities.get(e.getEntityId()).displayManaModification(e.getManaDiff());
			}
			else {
				System.out.println("GUI::updateLivingEntityMana : invalid entity id " + e.getEntityId());
			}
		}

		public void updateLivingEntityXp(LivingEntityXpUpdateEvent e) {
			if(GUI.this.livingEntities.containsKey(e.getEntityId())) {
				GUI.this.livingEntities.get(e.getEntityId()).displayXpModification(e.getXpDiff());
			}
			else {
				System.out.println("GUI::updateLivingEntityXp : invalid entity id " + e.getEntityId());
			}
		}

		public void createSpell(SpellCreationEvent e) {
			if(e.getPlayerNumber() >= 0 && e.getPlayerNumber() < GUI.this.actualPlayersNumber) {
				PlayerHUD playerHUD = GUI.this.playersHUD.get(e.getPlayerNumber());
				playerHUD.setSpellType(e.getSpellNumber(), e.getSpellType());
				playerHUD.setSpellBaseCooldown(e.getSpellNumber(), e.getBaseCooldown());
				playerHUD.setSpellCooldown(e.getSpellNumber(), 0);
				playerHUD.setSpellDescription(e.getSpellNumber(), e.getDescription());
				playerHUD.enableSpell(e.getSpellNumber());
			}
			else {
				System.out.println("GUI::changePlayerStat : invalid player number " + e.getPlayerNumber());
			}
		}

		public void deleteSpell(SpellDeletionEvent e) {
			if(e.getPlayerNumber() >= 0 && e.getPlayerNumber() < GUI.this.actualPlayersNumber) {
				PlayerHUD playerHUD = GUI.this.playersHUD.get(e.getPlayerNumber());
				playerHUD.disableSpell(e.getSpellNumber());
			}
			else {
				System.out.println("GUI::deleteSpell : invalid player number " + e.getPlayerNumber());
			}
		}

		public void updateSpellCooldown(SpellCooldownUpdateEvent e) {
			if(e.getPlayerNumber() >= 0 && e.getPlayerNumber() < GUI.this.actualPlayersNumber) {
				PlayerHUD playerHUD = GUI.this.playersHUD.get(e.getPlayerNumber());
				playerHUD.setSpellBaseCooldown(e.getSpellNumber(), e.getBaseCooldown());
				playerHUD.setSpellCooldown(e.getSpellNumber(), e.getCooldown());
			}
			else {
				System.out.println("GUI::updateSpellCooldown : invalid player number " + e.getPlayerNumber());
			}
		}

		public void updateSpellDescription(SpellDescriptionUpdateEvent e) {
			if(e.getPlayerNumber() >= 0 && e.getPlayerNumber() < GUI.this.actualPlayersNumber) {
				PlayerHUD playerHUD = GUI.this.playersHUD.get(e.getPlayerNumber());
				playerHUD.setSpellDescription(e.getSpellNumber(), e.getDescription());
			}
			else {
				System.out.println("GUI::updateSpellDescription : invalid player number " + e.getPlayerNumber());
			}
		}

		public void HUDSpellClicked(HUDSpellClickEvent e) {
			fireCastSpellRequestEvent(new CastSpellRequestEvent(e.getPlayerNumber(), e.getSpellNumber()));
		}

		public void selectorSpellClicked(SelectorSpellClickEvent e) {
			fireSpellSelectedRequestEvent(new SpellSelectedRequestEvent(e.getSpellType()));
			GUI.this.blockPane.getChildren().clear();
			GUI.this.blockInput.set(false);
		}

		private void selectSpell(SpellSelectionEvent e) {
			SpellSelector spellSelector = new SpellSelector(e.getText(), e.getSpellTypes(), GUI.this);
			GUI.this.blockPane.getChildren().add(spellSelector);
			GUI.this.blockInput.set(true);
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
						case TILE_DRAG_EVENT:
							this.tileDragged((TileDragEvent) e);
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
					case PLAYER_DELETION_EVENT:
						this.deletePlayer((PlayerDeletionEvent) e);
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
						case OBJECT_DRAG_EVENT:
							this.objectDragged((ObjectDragEvent) e);
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
						this.updateLivingEntityHealth((LivingEntityHealthUpdateEvent) e);
						break;
					case LIVING_ENTITY_HEALTH_VISIBILITY_EVENT:
						this.setLivingEntityHealthVisibility((LivingEntityHealthVisibilityEvent) e);
						break;
					case LIVING_ENTITY_MANA_UPDATE_EVENT:
						this.updateLivingEntityMana((LivingEntityManaUpdateEvent) e);
						break;
					case LIVING_ENTITY_XP_UPDATE_EVENT:
						this.updateLivingEntityXp((LivingEntityXpUpdateEvent) e);
						break;
					}
					break;
				}
				case SCORE_UPDATE_EVENT: {
					updateScore((ScoreUpdateEvent) gameEvent);
					break;
				}
				case LEVEL_UPDATE_EVENT: {
					updateLevel((LevelUpdateEvent) gameEvent);
					break;
				}
				case SPELL_EVENT:{
					SpellEvent e = (SpellEvent) gameEvent;
					switch(e.getSubType()) {
					case SPELL_CREATION_EVENT:{
						createSpell((SpellCreationEvent) e);
						break;
					}
					case SPELL_DELETION_EVENT:{
						deleteSpell((SpellDeletionEvent) e);
						break;
					}
					case SPELL_COOLDOWN_UPDATE_EVENT:{
						updateSpellCooldown((SpellCooldownUpdateEvent) e);
						break;
					}
					case SPELL_DESCRIPTION_UPDATE_EVENT:{
						updateSpellDescription((SpellDescriptionUpdateEvent) e);
						break;
					}
					}
					break;
				}
				case HUD_SPELL_CLICK_EVENT: {
					HUDSpellClicked((HUDSpellClickEvent) gameEvent);
					break;
				}
				case SELECTOR_SPELL_CLICK_EVENT: {
					selectorSpellClicked((SelectorSpellClickEvent) gameEvent);
					break;
				}
				case SPELL_SELECTION_EVENT: {
					selectSpell((SpellSelectionEvent) gameEvent);
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
		this.bPane.getChildren().add(this.bVBox);
		this.bPane.prefHeightProperty().bind(this.bVBox.heightProperty());
		this.bPane.maxHeightProperty().bind(this.bVBox.heightProperty());

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

		this.anchorPane.getChildren().add(this.blockPane);
		this.blockPane.setBackground(new Background(new BackgroundFill(BLOCKING_BACKGROUND_COLOR, CornerRadii.EMPTY, Insets.EMPTY)));
		AnchorPane.setTopAnchor(this.blockPane, 0d);
		AnchorPane.setBottomAnchor(this.blockPane, 0d);
		AnchorPane.setLeftAnchor(this.blockPane, 0d);
		AnchorPane.setRightAnchor(this.blockPane, 0d);
		this.blockPane.visibleProperty().bind(this.blockInput);

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

	private void fireLockViewRequestEvent(LockViewRequestEvent event) {
		for (RequestListener listener : this.getRequestListener()) {
			listener.handle(event);
		}
	}

	private void fireDropRequestEvent(DropRequestEvent event) {
		for(RequestListener listener : this.getRequestListener()) {
			listener.handle(event);
		}
	}

	private void fireUsageRequestEvent(UsageRequestEvent event) {
		for(RequestListener listener : this.getRequestListener()) {
			listener.handle(event);
		}
	}

	private void fireEquipEvent(EquipRequestEvent event) {
		for (RequestListener listener : getRequestListener()) {
			listener.handle(event);
		}
	}

	private void fireMoveRequestEvent(MoveRequestEvent event) {
		for(RequestListener listener : this.getRequestListener()) {
			listener.handle(event);
		}
	}

	private void fireInteractionRequestEvent(InteractionRequestEvent event) {
		for(RequestListener listener : this.getRequestListener()) {
			listener.handle(event);
		}
	}

	private void fireCenterViewRequestEvent(CenterViewRequestEvent event) {
		for(RequestListener listener : this.getRequestListener()) {
			listener.handle(event);
		}
	}

	private void fireCastSpellRequestEvent(CastSpellRequestEvent event) {
		for(RequestListener listener : this.getRequestListener()) {
			listener.handle(event);
		}
	}

	private void fireSpellSelectedRequestEvent(SpellSelectedRequestEvent event) {
		for(RequestListener listener : this.getRequestListener()) {
			listener.handle(event);
		}
	}

	@Override
	public void handle(InventoryAdditionEvent e) {
		this.eventQueue.add(e);
	}

	@Override
	public void handle(InventoryDeletionEvent e) {
		this.eventQueue.add(e);
	}

	@Override
	public void handle(LivingEntityCreationEvent e) {
		this.eventQueue.add(e);
	}

	@Override
	public void handle(LivingEntityDeletionEvent e) {
		this.eventQueue.add(e);
	}

	@Override
	public void handle(LivingEntityLOSDefinitionEvent e) {
		this.eventQueue.add(e);
	}

	@Override
	public void handle(LivingEntityLOSModificationEvent e) {
		this.eventQueue.add(e);
	}

	@Override
	public void handle(LivingEntityMoveEvent e) {
		this.eventQueue.add(e);
	}

	@Override
	public void handle(PlayerCreationEvent e) {
		this.eventQueue.add(e);
	}

	@Override
	public void handle(PlayerStatEvent e) {
		this.eventQueue.add(e);
	}

	@Override
	public void handle(StaticEntityCreationEvent e) {
		this.eventQueue.add(e);
	}

	@Override
	public void handle(StaticEntityDeletionEvent e) {
		this.eventQueue.add(e);
	}

	@Override
	public void handle(StaticEntityLOSDefinitionEvent e) {
		this.eventQueue.add(e);
	}

	@Override
	public void handle(MapCreationEvent e) {
		this.eventQueue.add(e);
	}

	@Override
	public void handle(TileClickEvent e) {
		this.eventQueue.add(e);
	}

	@Override
	public void handle(TileModificationEvent e) {
		this.eventQueue.add(e);
	}

	@Override
	public void handle(PlayerNextTickEvent e) {
		this.eventQueue.add(e);
	}

	@Override
	public void handle(ObjectClickEvent e) {
		this.eventQueue.add(e);
	}

	@Override
	public void handle(FogAdditionEvent e) {
		this.eventQueue.add(e);
	}

	@Override
	public void handle(FogResetEvent e) {
		this.eventQueue.add(e);
	}

	@Override
	public void handle(CenterOnTileEvent e) {
		this.eventQueue.add(e);
	}

	@Override
	public void handle(LivingEntityHealthUpdateEvent e) {
		this.eventQueue.add(e);
	}

	@Override
	public void handle(LivingEntityHealthVisibilityEvent e) {
		this.eventQueue.add(e);
	}

	@Override
	public void handle(ScoreUpdateEvent e) {
		this.eventQueue.add(e);
	}

	@Override
	public void handle(TileDragEvent e) {
		this.eventQueue.add(e);
	}

	@Override
	public void handle(PlayerDeletionEvent e) {
		this.eventQueue.add(e);
	}

	@Override
	public void handle(LevelUpdateEvent e) {
		this.eventQueue.add(e);
	}

	@Override
	public void handle(ObjectDragEvent e) {
		this.eventQueue.add(e);
	}

	@Override
	public void handle(LivingEntityManaUpdateEvent e) {
		this.eventQueue.add(e);
	}

	@Override
	public void handle(LivingEntityXpUpdateEvent e) {
		this.eventQueue.add(e);
	}

	@Override
	public void handle(SpellCooldownUpdateEvent e) {
		this.eventQueue.add(e);
	}

	@Override
	public void handle(SpellDescriptionUpdateEvent e) {
		this.eventQueue.add(e);
	}

	@Override
	public void handle(SpellCreationEvent e) {
		this.eventQueue.add(e);
	}

	@Override
	public void handle(SpellDeletionEvent e) {
		this.eventQueue.add(e);
	}

	@Override
	public void handle(HUDSpellClickEvent e) {
		this.eventQueue.add(e);
	}

	@Override
	public void handle(SelectorSpellClickEvent e) {
		this.eventQueue.add(e);
	}

	@Override
	public void handle(SpellSelectionEvent e) {
		this.eventQueue.add(e);
	}

}
