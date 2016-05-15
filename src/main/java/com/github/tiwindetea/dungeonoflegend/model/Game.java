//////////////////////////////////////////////////////////////////////////////////
//                                                                              //
//     This Source Code Form is subject to the terms of the Mozilla Public      //
//     License, v. 2.0. If a copy of the MPL was not distributed with this      //
//     file, You can obtain one at http://mozilla.org/MPL/2.0/.                 //
//                                                                              //
//////////////////////////////////////////////////////////////////////////////////

package com.github.tiwindetea.dungeonoflegend.model;

import com.github.tiwindetea.dungeonoflegend.events.living_entities.LivingEntityCreationEvent;
import com.github.tiwindetea.dungeonoflegend.events.living_entities.LivingEntityDeletionEvent;
import com.github.tiwindetea.dungeonoflegend.events.living_entities.LivingEntityLOSDefinitionEvent;
import com.github.tiwindetea.dungeonoflegend.events.living_entities.LivingEntityLOSModificationEvent;
import com.github.tiwindetea.dungeonoflegend.events.living_entities.LivingEntityMoveEvent;
import com.github.tiwindetea.dungeonoflegend.events.map.MapCreationEvent;
import com.github.tiwindetea.dungeonoflegend.events.map.TileModificationEvent;
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
import com.github.tiwindetea.dungeonoflegend.listeners.game.GameListener;
import com.github.tiwindetea.dungeonoflegend.listeners.request.RequestListener;
import com.github.tiwindetea.dungeonoflegend.view.entities.LivingEntityType;
import com.github.tiwindetea.dungeonoflegend.view.entities.StaticEntityType;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.Scanner;
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;


/**
 * Game.
 * @author Lucas LAZARE
 */
public class Game implements RequestListener {

	/* Tunning parameters for the entities generation */
	public static final int MIN_MOB_QTT_PER_LEVEL = 15;
	public static final int MAX_MOB_QTT_PER_LEVEL = 20;
	public static final int MIN_TRAPS_QTT_PER_LEVEL = 2;
	public static final int MAX_TRAPS_QTT_PER_LEVEL = 5;
	public static final int MIN_CHEST_QTT_PER_LEVEL = 1;
	public static final int MAX_CHEST_QTT_PER_LEVEL = 5;


	private static Logger logger = Logger.getLogger(MainPackage.name + ".model.Game");
	private int level;
	private Map world;
	private HashMap<Vector2i, Pair<StorableObject>> objectsOnGround = new HashMap<>();
	private ArrayList<Pair<Mob>> mobs = new ArrayList<>();
	private ArrayList<Pair<InteractiveObject>> interactiveObjects = new ArrayList<>();
	private ArrayList<Pair<Player>> players = new ArrayList<>();
	private ArrayList<Pair<Vector2i>> bulbs = new ArrayList<>();
	private Seed seed;
	private final List<GameListener> listeners = new ArrayList<>();

	private int playerTurn;
	private Player currentPlayer;
	private Pair<Consumable> objectToUse = null;
	private ArrayList<Consumable> triggeredObjects = new ArrayList<>();

	/**
	 * The Game name.
	 */
	String gameName;


	/* Level generation variables. Charged at the creation of a game */
	private LivingEntityType[] mobsTypes;
	private int[] mobsBaseHP;
	private int[] mobsBaseDef;
	private int[] mobsBaseAttack;
	private int[] mobsHPPerLevel;
	private int[] mobsDefPerLevel;
	private int[] mobsAttackPerLevel;
	private int[] mobsChaseRange;

	private int[] trapsBaseHP;
	private int[] trapsBaseMana;
	private int[] trapsHPPerLevel;
	private int[] trapsManaPerLevel;

	private ArmorType[] lootsArmorType;
	private int[] lootsArmorBaseDefense;
	private int[] lootsArmorBaseAttack;
	private int[] lootsArmorDefensePerLevel;
	private int[] lootsArmorAttackPerLevel;

	private int[] lootsWeaponRange;
	private int[] lootsWeaponBaseAttack;
	private int[] lootsWeaponManaCost;
	private int[] lootsWeaponAttackPerLevel;
	private int[] lootsWeaponManaCostPerLevel;

	private int[] lootsPotTurns;
	private int[] lootsPotHeal;
	private int[] lootsPotMana;
	private int[] lootsPotAttackMod;
	private int[] lootsPotDefMod;
	private int[] lootsPotHPMod;
	private int[] lootsPotManaMod;
	private int[] lootsPotHealPerLevel;
	private int[] lootsPotManaPerLevel;

	private int[] lootsScrollBaseDamagePerTurn;
	private int[] lootsScrollBaseDamageModPerTurn;
	private int[] lootsScrollDamagePerTurnPerLevel;
	private int[] lootsScrollDamageModPerTurnPerLevel;
	private int[] lootsScrollTurns;

	/**
	 * Instantiates a new Game.
	 *
	 * @param gameName the game name (used as the save/load file)
	 */
	public Game(String gameName) {
		this.gameName = gameName;
	}

	/**
	 * Initialises a game.
	 *
	 * @param players the players
	 */
	public void initNew(Collection<Player> players) {
		this.initNew(players, new Seed(), 1);
	}

	/**
	 * Initiliases a game.
	 *
	 * @param numberOfPlayers the number of players
	 */
	public void initNew(int numberOfPlayers) {
		ArrayList<Player> players = new ArrayList<>();
		ResourceBundle playersBundle = ResourceBundle.getBundle(MainPackage.name + ".Players");

		for (int i = 0; i < numberOfPlayers; i++) {
			String pString = "player" + i + ".";
			players.add(new Player(
					playersBundle.getString(pString + "name"),
					i,
					Integer.parseInt(playersBundle.getString(pString + "los")),
					Integer.parseInt(playersBundle.getString(pString + "explorelos")),
					1,
					Integer.parseInt(playersBundle.getString(pString + "maxStorageCapacity")),
					Integer.parseInt(playersBundle.getString(pString + "baseHealth")),
					Integer.parseInt(playersBundle.getString(pString + "baseMana")),
					Integer.parseInt(playersBundle.getString(pString + "baseAttack")),
					Integer.parseInt(playersBundle.getString(pString + "baseDef")),
					Integer.parseInt(playersBundle.getString(pString + "healthPerLevel")),
					Integer.parseInt(playersBundle.getString(pString + "manaPerLevel")),
					Integer.parseInt(playersBundle.getString(pString + "attackPerLevel")),
					Integer.parseInt(playersBundle.getString(pString + "defensePerLevel"))
			));
		}
		this.initNew(players, new Seed(), 1);
	}

	/**
	 * Initialises a game.
	 *
	 * @param players the players
	 * @param seed    the seed
	 * @param level   the level
	 */
	public void initNew(Collection<Player> players, Seed seed, int level) {
		this.playerTurn = 0;
		this.level = level;
		this.seed = seed;
		this.world = new Map(this.seed);

		this.players = new ArrayList<>(players.size());
		this.players.addAll(players.stream().map(Pair::new).collect(Collectors.toList()));
		for (Pair<Player> player : this.players) {
			firePlayerCreationEvent(new PlayerCreationEvent(
					player.object.getNumber(),
					player.getId(),
					new Vector2i(0, 0),
					Direction.DOWN,
					player.object.getMaxHitPoints(),
					player.object.getMaxMana()
			));
		}
		this.currentPlayer = this.players.get(0).object;
		this.loadMobs();
		this.loadChests();
		this.loadTraps();
		this.generateLevel();
	}

	private void loadMobs() {
		ResourceBundle mobs = ResourceBundle.getBundle(MainPackage.name + ".Mobs");
		int mobQtt = Integer.parseInt(mobs.getString("mobs.qtt"));
		this.mobsTypes = new LivingEntityType[mobQtt];
		this.mobsBaseHP = new int[mobQtt];
		this.mobsBaseDef = new int[mobQtt];
		this.mobsBaseAttack = new int[mobQtt];
		this.mobsHPPerLevel = new int[mobQtt];
		this.mobsDefPerLevel = new int[mobQtt];
		this.mobsAttackPerLevel = new int[mobQtt];
		this.mobsChaseRange = new int[mobQtt];
		for (int i = 0; i < mobQtt; i++) {
			String mobName = "mob" + i + ".";
			this.mobsTypes[i] = LivingEntityType.parseLivingEntity(mobs.getString(mobName + "type"));
			this.mobsBaseHP[i] = Integer.parseInt(mobs.getString(mobName + "base-hp"));
			this.mobsBaseDef[i] = Integer.parseInt(mobs.getString(mobName + "base-def"));
			this.mobsBaseAttack[i] = Integer.parseInt(mobs.getString(mobName + "base-attack"));
			this.mobsHPPerLevel[i] = Integer.parseInt(mobs.getString(mobName + "defense-per-level"));
			this.mobsDefPerLevel[i] = Integer.parseInt(mobs.getString(mobName + "attack-per-level"));
			this.mobsAttackPerLevel[i] = Integer.parseInt(mobs.getString(mobName + "hp-per-level"));
			this.mobsChaseRange[i] = Integer.parseInt(mobs.getString(mobName + "chase-range"));
		}
	}

	private void loadChests() {
		ResourceBundle loots = ResourceBundle.getBundle(MainPackage.name + ".Chests");
		int armorQtt = Integer.parseInt(loots.getString("armors.qtt"));
		this.lootsArmorType = new ArmorType[armorQtt];
		this.lootsArmorBaseDefense = new int[armorQtt];
		this.lootsArmorBaseAttack = new int[armorQtt];
		this.lootsArmorDefensePerLevel = new int[armorQtt];
		this.lootsArmorAttackPerLevel = new int[armorQtt];
		for (int i = 0; i < armorQtt; i++) {
			String armorName = "armor" + i + ".";
			this.lootsArmorType[i] = ArmorType.parseArmorType(loots.getString(armorName + "type"));
			this.lootsArmorBaseDefense[i] = Integer.parseInt(loots.getString(armorName + "base-defense"));
			this.lootsArmorBaseAttack[i] = Integer.parseInt(loots.getString(armorName + "base-attack"));
			this.lootsArmorDefensePerLevel[i] = Integer.parseInt(loots.getString(armorName + "defense-per-level"));
			this.lootsArmorAttackPerLevel[i] = Integer.parseInt(loots.getString(armorName + "attack-per-level"));
		}

		int weaponQtt = Integer.parseInt(loots.getString("weapons.qtt"));
		this.lootsWeaponRange = new int[weaponQtt];
		this.lootsWeaponBaseAttack = new int[weaponQtt];
		this.lootsWeaponManaCost = new int[weaponQtt];
		this.lootsWeaponAttackPerLevel = new int[weaponQtt];
		this.lootsWeaponManaCostPerLevel = new int[weaponQtt];
		for (int i = 0; i < weaponQtt; i++) {
			String weaponName = "weapon" + i + ".";
			this.lootsWeaponRange[i] = Integer.parseInt(loots.getString(weaponName + "range"));
			this.lootsWeaponBaseAttack[i] = Integer.parseInt(loots.getString(weaponName + "base-attack"));
			this.lootsWeaponManaCost[i] = Integer.parseInt(loots.getString(weaponName + "mana-cost"));
			this.lootsWeaponAttackPerLevel[i] = Integer.parseInt(loots.getString(weaponName + "attack-per-level"));
			this.lootsWeaponManaCostPerLevel[i] = Integer.parseInt(loots.getString(weaponName + "mana-cost-per-level"));
		}

		int potQtt = Integer.parseInt(loots.getString("pots.qtt"));
		this.lootsPotTurns = new int[potQtt];
		this.lootsPotHeal = new int[potQtt];
		this.lootsPotMana = new int[potQtt];
		this.lootsPotAttackMod = new int[potQtt];
		this.lootsPotDefMod = new int[potQtt];
		this.lootsPotHPMod = new int[potQtt];
		this.lootsPotManaMod = new int[potQtt];
		this.lootsPotHealPerLevel = new int[potQtt];
		this.lootsPotManaPerLevel = new int[potQtt];
		for (int i = 0; i < potQtt; i++) {
			String potName = "pot" + i + ".";
			this.lootsPotTurns[i] = Integer.parseInt(loots.getString(potName + "turns"));
			this.lootsPotHeal[i] = Integer.parseInt(loots.getString(potName + "base-hp"));
			this.lootsPotMana[i] = Integer.parseInt(loots.getString(potName + "base-mana"));
			this.lootsPotAttackMod[i] = Integer.parseInt(loots.getString(potName + "attack-mod"));
			this.lootsPotDefMod[i] = Integer.parseInt(loots.getString(potName + "def-mod"));
			this.lootsPotHPMod[i] = Integer.parseInt(loots.getString(potName + "hp-mod"));
			this.lootsPotManaMod[i] = Integer.parseInt(loots.getString(potName + "mana-mod"));
			this.lootsPotHealPerLevel[i] = Integer.parseInt(loots.getString(potName + "hp-per-level"));
			this.lootsPotManaPerLevel[i] = Integer.parseInt(loots.getString(potName + "mana-per-level"));
		}

		int scrollQtt = Integer.parseInt(loots.getString("scrolls.qtt"));
		this.lootsScrollBaseDamagePerTurn = new int[scrollQtt];
		this.lootsScrollBaseDamageModPerTurn = new int[scrollQtt];
		this.lootsScrollDamagePerTurnPerLevel = new int[scrollQtt];
		this.lootsScrollDamageModPerTurnPerLevel = new int[scrollQtt];
		this.lootsScrollTurns = new int[scrollQtt];
		for (int i = 0; i < scrollQtt; i++) {
			String scrollName = "scroll" + i + ".";
			this.lootsScrollBaseDamagePerTurn[i] = Integer.parseInt(loots.getString(scrollName + "base-damage-per-turn"));
			this.lootsScrollBaseDamageModPerTurn[i] = Integer.parseInt(loots.getString(scrollName + "base-damage-mod-per-turn"));
			this.lootsScrollDamagePerTurnPerLevel[i] = Integer.parseInt(loots.getString(scrollName + "damage-per-turn-per-level"));
			this.lootsScrollDamageModPerTurnPerLevel[i] = Integer.parseInt(loots.getString(scrollName + "damage-mod-per-turn-per-level"));
			this.lootsScrollTurns[i] = Integer.parseInt(loots.getString(scrollName + "turns"));
		}
	}

	private void loadTraps() {
		ResourceBundle traps = ResourceBundle.getBundle(MainPackage.name + ".Traps", Locale.getDefault());
		int trapsQtt = Integer.parseInt(traps.getString("traps.qtt"));
		this.trapsBaseHP = new int[trapsQtt];
		this.trapsBaseMana = new int[trapsQtt];
		this.trapsHPPerLevel = new int[trapsQtt];
		this.trapsManaPerLevel = new int[trapsQtt];
		for (int i = 0; i < trapsQtt; i++) {
			String trapName = "trap" + i + ".";
			this.trapsBaseHP[i] = Integer.parseInt(traps.getString(trapName + "base-hp"));
			this.trapsBaseMana[i] = Integer.parseInt(traps.getString(trapName + "base-mana"));
			this.trapsHPPerLevel[i] = Integer.parseInt(traps.getString(trapName + "hp-per-level"));
			this.trapsManaPerLevel[i] = Integer.parseInt(traps.getString(trapName + "mana-per-level"));
		}
	}

	/**
	 * Adds a Gamelistener.
	 *
	 * @param listener the listener
	 */
	public void addGameListener(GameListener listener) {
		this.listeners.add(listener);
		Player.addPlayerListener(listener);
	}

	private GameListener[] getGameListeners() {
		return this.listeners.toArray(new GameListener[this.listeners.size()]);
	}

	private void fireLivingEntityCreationEvent(LivingEntityCreationEvent event) {
		for(GameListener listener : this.getGameListeners()) {
			listener.createLivingEntity(event);
		}
	}

	private void fireLivingEntityDeletionEvent(LivingEntityDeletionEvent event) {
		for(GameListener listener : this.getGameListeners()) {
			listener.deleteLivingEntity(event);
		}
	}

	private void fireLivingEntityLOSDefinitionEvent(LivingEntityLOSDefinitionEvent event) {
		for(GameListener listener : this.getGameListeners()) {
			listener.defineLivingEntityLOS(event);
		}
	}

	private void fireLivingEntityLOSModificationEvent(LivingEntityLOSModificationEvent event) {
		for(GameListener listener : this.getGameListeners()) {
			listener.modifieLivingEntityLOS(event);
		}
	}

	private void fireLivingEntityMoveEvent(LivingEntityMoveEvent event) {
		for(GameListener listener : this.getGameListeners()) {
			listener.moveLivingEntity(event);
		}
	}

	private void fireMapCreationEvent(MapCreationEvent event) {
		for(GameListener listener : this.getGameListeners()) {
			listener.createMap(event);
		}
	}

	private void fireInventoryAdditionEvent(InventoryAdditionEvent event) {
		for(GameListener listener : this.getGameListeners()) {
			listener.addInventory(event);
		}
	}

	private void fireInventoryDeletionEvent(InventoryDeletionEvent event) {
		for(GameListener listener : this.getGameListeners()) {
			listener.deleteInventory(event);
		}
	}

	private void firePlayerCreationEvent(PlayerCreationEvent event) {
		for(GameListener listener : this.getGameListeners()) {
			listener.createPlayer(event);
		}
	}

	private void firePlayerStatEvent(PlayerStatEvent event) {
		for(GameListener listener : this.getGameListeners()) {
			listener.changePlayerStat(event);
		}
	}

	private void fireStaticEntityCreationEvent(StaticEntityCreationEvent event) {
		for(GameListener listener : this.getGameListeners()) {
			listener.createStaticEntity(event);
		}
	}

	private void fireStaticEntityDeletionEvent(StaticEntityDeletionEvent event) {
		for(GameListener listener : this.getGameListeners()) {
			listener.deleteStaticEntity(event);
		}
	}

	// TODO : Fire those events
	private void fireStaticEntityLOSDefinitionEvent(StaticEntityLOSDefinitionEvent event) {
		for(GameListener listener : this.getGameListeners()) {
			listener.defineStaticEntityLOS(event);
		}
	}

	private void fireTileModificationEvent(TileModificationEvent event) {
		for (GameListener listener : getGameListeners()) {
			listener.modifieTile(event);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void requestDrop(DropRequestEvent e) {
		logger.entering(MainPackage.name + ".model.Game", "requestDrop", e);
		if (!Tile.isObstructed(this.world.getTile(e.dropPosition))) {
			ArrayList<Stack<Vector2i>> paths = new ArrayList<>(4);
			Direction[] dirs = {Direction.LEFT, Direction.RIGHT, Direction.UP, Direction.DOWN};
			for (Direction direction : dirs) {
				paths.add(this.world.getPath(this.currentPlayer.getPosition(), e.dropPosition.copy().add(direction), false, null));
			}
			if (paths.size() != 0) {
				Stack<Vector2i> theChoosenOne = paths.get(0);
				for (Stack<Vector2i> stack : paths) {
					if (stack != null && stack.size() < theChoosenOne.size()) {
						theChoosenOne = stack;
					}
				}
				this.currentPlayer.setRequestedPath(theChoosenOne);
				this.currentPlayer.setObjectToDrop(e.objectId, e.dropPosition);
				this.nextTurn();
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void requestInteraction(InteractionRequestEvent e) {
		Stack<Vector2i> path = this.world.getPath(this.currentPlayer.getPosition(), e.tilePosition, false, null);
		boolean success = true;
		logger.entering(MainPackage.name + ".model.Game", "requestInteraction", e);
		Tile tile = this.world.getTile(e.tilePosition);
		int distance = Math.max(Math.abs(e.tilePosition.x - this.currentPlayer.getPosition().x),
				Math.abs(e.tilePosition.y - this.currentPlayer.getPosition().y));
		if (distance == 1 && (tile == Tile.CLOSED_DOOR || tile == Tile.OPENED_DOOR)) {
			this.currentPlayer.setRequestedInteraction(e.tilePosition);
		} else if (distance <= this.currentPlayer.getLos()) {
			boolean[][] los = this.world.getLOS(this.currentPlayer.getPosition(), this.currentPlayer.getLos());
			Vector2i p = this.currentPlayer.getPosition();
			if (los[p.x + los.length - e.tilePosition.x][p.y + los[0].length - e.tilePosition.y]) {
				int i = this.mobs.indexOf(new Mob(e.tilePosition));
				if (i >= 0) {
					if (this.objectToUse != null) {
						this.objectToUse.object.trigger(this.mobs.get(i).object);
						this.triggeredObjects.add(this.objectToUse.object);
						fireInventoryDeletionEvent(new InventoryDeletionEvent(this.currentPlayer.getNumber(), this.objectToUse.getId()));
						this.objectToUse = null;
					}
					this.currentPlayer.setRequestedAttack(e.tilePosition);
				} else {
					success = false;
				}
			} else {
				success = false;
			}
		} else {
			success = false;
		}
		if (!success && path != null) {
			this.currentPlayer.setRequestedPath(path);
			success = true;
		}
		if (success) {
			nextTurn();
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void requestUsage(UsageRequestEvent e) {
		logger.entering(MainPackage.name + ".model.Game", "requestUsage", e);
		Pair<StorableObject>[] inventory = this.currentPlayer.getInventory();
		this.objectToUse = null;
		for (Pair<StorableObject> pair : inventory) {
			if (pair.object.getType() == StorableObjectType.CONSUMABLE && pair.getId() == e.objectId) {
				this.objectToUse = new Pair<>(pair.getId(), (Consumable) pair.object);
			}
		}
		if (this.objectToUse != null && this.objectToUse.object.getConsumableType() == ConsumableType.POT) {
			this.triggeredObjects.add(this.objectToUse.object);
			this.objectToUse.object.trigger(this.currentPlayer);
			fireInventoryDeletionEvent(new InventoryDeletionEvent(this.currentPlayer.getNumber(), this.objectToUse.getId()));
			this.objectToUse = null;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void requestMove(MoveRequestEvent e) {
		logger.entering(MainPackage.name + ".model.Game", "requestMove", e);
		Stack<Vector2i> stack = new Stack<>();
		stack.add(this.currentPlayer.getPosition().add(e.moveDirection));
		this.currentPlayer.setRequestedPath(stack);
	}

	/**
	 * generates a level
	 */
	private void generateLevel() {
		/* Next block deletes all entities on the GUI */
		for (Pair<Mob> mob : this.mobs) {
			fireStaticEntityDeletionEvent(new StaticEntityDeletionEvent(mob.getId()));
		}
		for (Pair<InteractiveObject> obj : this.interactiveObjects) {
			fireStaticEntityDeletionEvent(new StaticEntityDeletionEvent(obj.getId()));
		}
		for (Pair<Vector2i> bulb : this.bulbs) {
			fireStaticEntityDeletionEvent(new StaticEntityDeletionEvent(bulb.getId()));
		}
		System.out.println("Entering level " + this.level + " of seed [" + this.seed.getAlphaSeed() + " ; " + this.seed.getBetaSeed() + "]");

		/* Generate the level and bulbs to turn off */
		List<Map.Room> rooms = this.world.generateLevel(this.level);
		fireMapCreationEvent(new MapCreationEvent(this.world.getMapCopy()));
		this.bulbs.clear();
		for (Vector2i bulb : this.world.getBulbPosition()) {
			Pair<Vector2i> p = new Pair<>(bulb);
			this.bulbs.add(p);
			fireStaticEntityCreationEvent(new StaticEntityCreationEvent(p.getId(), StaticEntityType.LIT_BULB, p.object));
		}

		/* Generate the mobs */
		Mob.setMap(this.world.copy());
		Random random = this.seed.getRandomizer(this.level);
		int mobsNbr = random.nextInt(MAX_MOB_QTT_PER_LEVEL - MIN_MOB_QTT_PER_LEVEL) + MIN_MOB_QTT_PER_LEVEL;
		this.mobs = new ArrayList<>(mobsNbr);
		int selectedMob, mobLevel;
		Vector2i mobPos;
		for (int i = 0; i < mobsNbr; ++i) {
			selectedMob = random.nextInt(this.mobsTypes.length);
			mobLevel = random.nextInt(3) + this.level - 1;
			mobPos = this.selectRandomGroundPosition(rooms, random);
			Pair<Mob> pair = new Pair<>(new Mob(mobLevel,
					mobLevel * this.mobsHPPerLevel[selectedMob] + this.mobsBaseHP[selectedMob],
					mobLevel * this.mobsAttackPerLevel[selectedMob] + this.mobsBaseAttack[selectedMob],
					mobLevel * this.mobsDefPerLevel[selectedMob] + this.mobsBaseDef[selectedMob],
					this.mobsChaseRange[selectedMob],
					mobPos.copy()));
			this.mobs.add(pair);
			fireLivingEntityCreationEvent(new LivingEntityCreationEvent(pair.getId(), this.mobsTypes[selectedMob], mobPos.copy(), null));
		}

		/* Generate chests */
		int chestsNbr = random.nextInt(MAX_CHEST_QTT_PER_LEVEL - MIN_CHEST_QTT_PER_LEVEL) + MIN_CHEST_QTT_PER_LEVEL;
		int trapsNbr = random.nextInt(MAX_TRAPS_QTT_PER_LEVEL - MIN_TRAPS_QTT_PER_LEVEL) + MIN_TRAPS_QTT_PER_LEVEL;
		int selection, chestLevel;
		Vector2i pos;
		this.interactiveObjects = new ArrayList<>(chestsNbr + trapsNbr);
		for (int i = 0; i < chestsNbr; i++) {
			Pair<InteractiveObject> pair;
			chestLevel = random.nextInt(3) + this.level - 1;
			pos = selectRandomGroundPosition(rooms, random);
			switch (random.nextInt(4)) {
				case 0:
					selection = random.nextInt(this.lootsArmorType.length);
					Armor armor = new Armor(this.lootsArmorDefensePerLevel[selection] * chestLevel + this.lootsArmorBaseDefense[selection],
							this.lootsArmorAttackPerLevel[selection] * chestLevel + this.lootsArmorBaseAttack[selection],
							this.lootsArmorType[selection]);
					pair = new Pair<>(new InteractiveObject(pos, armor));
					this.interactiveObjects.add(pair);
					fireStaticEntityCreationEvent(new StaticEntityCreationEvent(pair.getId(), armor.getGType(), pos));
					break;
				case 1:
					selection = random.nextInt(this.lootsWeaponRange.length);
					Weapon weapon = new Weapon(this.lootsWeaponBaseAttack[selection] + this.lootsWeaponAttackPerLevel[selection] * chestLevel,
							this.lootsWeaponRange[selection],
							this.lootsWeaponManaCost[selection] + this.lootsWeaponManaCostPerLevel[selection] * chestLevel);
					pair = new Pair<>(new InteractiveObject(pos, weapon));
					this.interactiveObjects.add(pair);
					fireStaticEntityCreationEvent(new StaticEntityCreationEvent(pair.getId(), weapon.getGType(), pos));
					break;
				case 2:
					selection = random.nextInt(this.lootsScrollTurns.length);
					Scroll scroll = new Scroll(this.lootsScrollTurns[selection],
							this.lootsScrollBaseDamagePerTurn[selection] + chestLevel * this.lootsScrollDamagePerTurnPerLevel[selection],
							this.lootsScrollBaseDamageModPerTurn[selection] + chestLevel * this.lootsScrollDamageModPerTurnPerLevel[selection]);
					pair = new Pair<>(new InteractiveObject(pos, scroll));
					this.interactiveObjects.add(pair);
					fireStaticEntityCreationEvent(new StaticEntityCreationEvent(pair.getId(), scroll.getGType(), pos));
					break;
				case 3:
					/* Falls through */
				default:
					selection = random.nextInt(this.lootsPotHeal.length);
					Pot pot = new Pot(this.lootsPotTurns[selection],
							this.lootsPotHeal[selection] + this.lootsPotHealPerLevel[selection] * chestLevel,
							this.lootsPotMana[selection] + this.lootsPotManaPerLevel[selection] * chestLevel,
							this.lootsPotDefMod[selection],
							this.lootsPotAttackMod[selection],
							this.lootsPotHPMod[selection],
							this.lootsPotManaMod[selection]);
					pair = new Pair<>(new InteractiveObject(pos, pot));
					this.interactiveObjects.add(pair);
					fireStaticEntityCreationEvent(new StaticEntityCreationEvent(pair.getId(), pot.getGType(), pos));
					break;
			}
		}

		/* Generate traps */
		for (int i = 0; i < trapsNbr; i++) {
			selection = random.nextInt(this.trapsBaseHP.length);
			pos = selectRandomGroundPosition(rooms, random);
			Pair<InteractiveObject> pair = new Pair<>(new InteractiveObject(pos,
					this.trapsBaseHP[selection] + this.trapsHPPerLevel[selection] * random.nextInt(3) + this.level - 1,
					this.trapsBaseMana[selection] + this.trapsManaPerLevel[selection] * random.nextInt(3) + this.level - 1));
			this.interactiveObjects.add(pair);
			fireStaticEntityCreationEvent(new StaticEntityCreationEvent(pair.getId(), StaticEntityType.TRAP, pos));
		}


		/* Move the players to the right position */
		for (Pair<Player> player : this.players) {
			if (player.object.hasFallen) {
				do {
					player.object.setPosition(this.selectRandomGroundPosition(rooms, random));
				} while (Tile.isObstructed(this.world.getTile(player.object.getPosition())));
				player.object.hasFallen = false;
			} else {
				player.object.setPosition(this.world.getStairsUpPosition());
			}
			fireLivingEntityCreationEvent(new LivingEntityCreationEvent(
					player.getId(),
					player.object.getGType(),
					player.object.getPosition(),
					Direction.DOWN
			));
		}
	}

	//-------------------------------------//
	private Vector2i selectRandomGroundPosition(List<Map.Room> rooms, Random random) {
		Map.Room selectedRoom;
		Vector2i pos = new Vector2i();
		selectedRoom = rooms.get(random.nextInt(rooms.size()));
		if (selectedRoom.top.x != selectedRoom.bottom.x) {
			pos.x = random.nextInt(selectedRoom.bottom.x - selectedRoom.top.x) + selectedRoom.top.x;
		} else {
			pos.x = selectedRoom.top.x;
		}

		if (selectedRoom.top.y != selectedRoom.bottom.y) {
			pos.y = random.nextInt(selectedRoom.bottom.y - selectedRoom.top.y) + selectedRoom.top.y;
		} else {
			pos.y = selectedRoom.top.y;
		}
		return pos;
	}

	/**
	 * Step into the next turn (next player)
	 */
	private void nextTurn() {
		this.objectToUse = null;

		while (++this.playerTurn < this.players.size()
				&& this.players.get(this.playerTurn).object.getFloor() != this.level) ;

		if (this.playerTurn >= this.players.size()) {
			this.playerTurn = 0;
			this.currentPlayer = this.players.get(0).object;
			this.nextTick();
			if (this.currentPlayer.getFloor() != this.level) {

			}
		}

		if (this.currentPlayer.isARequestPending()) {
			this.nextTurn();
		}
		//TODO
		//fireNextTurnEvent(new event)
	}

	/**
	 * Step into the next tick (all entities live)
	 */
	private void nextTick() {
		logger.log(Level.INFO, "Starting players lives");
		Player player;
		Vector2i pos;

		for (int i = 0; i < this.players.size(); i++) {
			player = this.players.get(i).object;
			player.live(this.mobs, this.players);
			Pair<StorableObject> drop;
			if ((pos = player.getRequestedMove()) != null) {
				int distance = Math.abs(player.getPosition().x - pos.x) + Math.abs(player.getPosition().y - pos.y);
				if (distance <= 1 && isAccessible(pos)) {
					player.setPosition(pos);
					fireLivingEntityMoveEvent(new LivingEntityMoveEvent(this.players.get(i).getId(), pos));
				} else if (distance <= 1 && this.world.getTile(pos) == Tile.HOLE) {
					player.setFloor(this.level + 1);
					player.hasFallen = true;
					player.damage(player.getMaxHitPoints() / 5);
				} else {
					logger.log(Level.INFO, "Player " + player.getName() + " requested an invalid move ! (from "
							+ player.getPosition() + " to " + pos + ")");
					player.moveRequestDenied();
				}
			} else if ((pos = player.getRequestedAttack()) != null) {
				if (Math.abs(player.getPosition().x - pos.x) + Math.abs(player.getPosition().y - pos.y) <= player.getAttackRange()) {
					int j = this.mobs.indexOf(new Mob(pos));
					if (j >= 0) {
						player.attack(this.mobs.get(j).object);
					} else {
						// Healing by 20%, because why not
						player.heal(player.getMaxHitPoints() / 5);
					}
				}
				player.setRequestedAttack(null);
			} else if ((pos = player.getRequestedInteraction()) != null) {
				this.world.triggerTile(pos);
			} else if ((drop = player.getObjectToDrop()) != null) {
				pos = player.getDropPos();
				int distance = Math.abs(player.getPosition().x - pos.x) + Math.abs(player.getPosition().y - pos.y);
				if (drop.object != null && distance == 1) {
					fireStaticEntityCreationEvent(new StaticEntityCreationEvent(drop.getId(), drop.object.getGType(), pos));
					fireInventoryDeletionEvent(new InventoryDeletionEvent(player.getNumber(), drop.getId()));
					player.dropRequestAccepted();
					this.objectsOnGround.put(pos, drop);
				}
			} else {
				String msg = "Nothing to do with player " + player.getName()
						+ " (player #" + (player.getNumber() + 1) + ")";
				logger.log(Level.SEVERE, msg);
				throw new RuntimeException(msg);
			}
		}

		Mob mob;
		ArrayList<Integer> toDelete = new ArrayList<>(3);
		for (int i = 0; i < this.mobs.size(); i++) {
			mob = this.mobs.get(i).object;
			if (mob.isAlive()) {
				mob.live(this.mobs, this.players);
				if ((pos = mob.getRequestedMove()) != null) {
					int distance = Math.abs(mob.getPosition().x - pos.x) + Math.abs(mob.getPosition().y - pos.y);
					if (distance <= 1 && isAccessible(pos)) {
						mob.setPosition(pos);
					}
				} else if ((pos = mob.getRequestedAttack()) != null) {
					int distance = Math.abs(mob.getPosition().x - pos.x) + Math.abs(mob.getPosition().y - pos.y);
					if (distance <= 1) {
						int j = this.players.indexOf(new Mob(pos));
						if (j < 0) {
							j = this.mobs.indexOf(new Mob(pos));
							if (j >= 0) {
								mob.attack(this.players.get(j).object);
							}
						} else {
							if (j >= 0) {
								mob.attack(this.players.get(j).object);
							}
						}
					}
				}
			} else {
				toDelete.add(new Integer(i));
				fireLivingEntityDeletionEvent(new LivingEntityDeletionEvent(this.mobs.get(i).getId()));
			}
		}

		toDelete.clear();
		for (int i = 0; i < this.players.size(); i++) {
			if (!this.players.get(i).object.isAlive()) {
				fireLivingEntityDeletionEvent(new LivingEntityDeletionEvent(this.players.get(i).getId()));
				toDelete.add(new Integer(i));
			}
		}

		for (Integer integer : toDelete) {
			this.players.remove(integer.intValue());
		}

		for (Integer integer : toDelete) {
			this.mobs.remove(integer.intValue());
		}

		toDelete.clear();
		for (int i = 0; i < this.triggeredObjects.size(); i++) {
			if (this.triggeredObjects.get(i).nextTick()) {
				toDelete.add(new Integer(i));
			}
		}
		for (Integer integer : toDelete) {
			this.triggeredObjects.remove(integer.intValue());
		}

		boolean nextLevel = true;
		for (Pair<Player> playerPair : this.players) {
			player = playerPair.object;
			pos = player.getPosition();
			Pair<StorableObject> drop = this.objectsOnGround.get(pos);

			while (drop != null && player.addToInventory(drop)) {
				fireStaticEntityDeletionEvent(new StaticEntityDeletionEvent(drop.getId()));
				fireInventoryAdditionEvent(new InventoryAdditionEvent(player.getNumber(), drop.getId(), drop.object.getGType()));
				this.objectsOnGround.remove(pos, drop);
				drop = this.objectsOnGround.get(pos);
			}

			if (player.getPosition().equals(this.world.getStairsDownPosition())) {
				player.setFloor(this.level + 1);
				player.setPosition(new Vector2i(0, 0));
				fireLivingEntityDeletionEvent(new LivingEntityDeletionEvent(playerPair.getId()));
			}

			if (player.getFloor() == this.level) {
				nextLevel = false;
			}
		}

		if (nextLevel) {
			++this.level;
			this.generateLevel();
		}
	}

	//-------------------------------------//
	private boolean isAccessible(Vector2i pos) {
		return !(Tile.isObstructed(this.world.getTile(pos))
				|| this.mobs.contains(new Mob(pos))
				|| this.players.contains(new Mob(pos)));
	}

	/**
	 * Loads a save.
	 */
	public void loadSave() {
		try {
			Scanner file = new Scanner(new FileInputStream(this.gameName));
			this.seed = Seed.parseSeed(file.nextLine());
			String lvl = file.nextLine();
			this.level = Integer.parseInt(lvl.substring(lvl.indexOf('=') + 1));
			this.players.clear();
			while (file.hasNext()) {
				String line = file.nextLine();
				this.players.add(new Pair<>(Player.parsePlayer(line)));
			}
			file.close();
		} catch (FileNotFoundException e) {
			System.out.println("Failed to open the save file");
			e.printStackTrace();
		}
	}

	//-------------------------------------//
	private void save() {
		try {
			FileWriter file = new FileWriter(new File(this.gameName));
			file.write(this.seed.toString() + "\n");
			file.write("level=" + this.level + "\n");
			for (Pair<Player> player : this.players) {
				if (player.object != null) {
					file.write(player.object.toString() + "\n");
				}
			}
			file.close();
		} catch (IOException e) {
			System.out.println("Failed to open the save file");
			e.printStackTrace();
		}
	}
}
