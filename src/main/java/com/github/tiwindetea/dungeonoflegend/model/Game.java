//////////////////////////////////////////////////////////////////////////////////
//                                                                              //
//     This Source Code Form is subject to the terms of the Mozilla Public      //
//     License, v. 2.0. If a copy of the MPL was not distributed with this      //
//     file, You can obtain one at http://mozilla.org/MPL/2.0/.                 //
//                                                                              //
//////////////////////////////////////////////////////////////////////////////////


package com.github.tiwindetea.dungeonoflegend.model;

import com.github.tiwindetea.dungeonoflegend.events.LevelUpdateEvent;
import com.github.tiwindetea.dungeonoflegend.events.ScoreUpdateEvent;
import com.github.tiwindetea.dungeonoflegend.events.living_entities.LivingEntityCreationEvent;
import com.github.tiwindetea.dungeonoflegend.events.living_entities.LivingEntityDeletionEvent;
import com.github.tiwindetea.dungeonoflegend.events.living_entities.LivingEntityLOSDefinitionEvent;
import com.github.tiwindetea.dungeonoflegend.events.living_entities.LivingEntityLOSModificationEvent;
import com.github.tiwindetea.dungeonoflegend.events.map.CenterOnTileEvent;
import com.github.tiwindetea.dungeonoflegend.events.map.FogAdditionEvent;
import com.github.tiwindetea.dungeonoflegend.events.map.MapCreationEvent;
import com.github.tiwindetea.dungeonoflegend.events.map.TileModificationEvent;
import com.github.tiwindetea.dungeonoflegend.events.players.PlayerCreationEvent;
import com.github.tiwindetea.dungeonoflegend.events.players.PlayerDeletionEvent;
import com.github.tiwindetea.dungeonoflegend.events.players.PlayerNextTickEvent;
import com.github.tiwindetea.dungeonoflegend.events.requests.CenterViewRequestEvent;
import com.github.tiwindetea.dungeonoflegend.events.requests.InteractionRequestEvent;
import com.github.tiwindetea.dungeonoflegend.events.requests.LockViewRequestEvent;
import com.github.tiwindetea.dungeonoflegend.events.requests.MoveRequestEvent;
import com.github.tiwindetea.dungeonoflegend.events.requests.RequestEvent;
import com.github.tiwindetea.dungeonoflegend.events.requests.inventory.DropRequestEvent;
import com.github.tiwindetea.dungeonoflegend.events.requests.inventory.EquipRequestEvent;
import com.github.tiwindetea.dungeonoflegend.events.requests.inventory.UsageRequestEvent;
import com.github.tiwindetea.dungeonoflegend.events.static_entities.StaticEntityCreationEvent;
import com.github.tiwindetea.dungeonoflegend.events.static_entities.StaticEntityDeletionEvent;
import com.github.tiwindetea.dungeonoflegend.events.static_entities.StaticEntityLOSDefinitionEvent;
import com.github.tiwindetea.dungeonoflegend.listeners.game.GameListener;
import com.github.tiwindetea.dungeonoflegend.listeners.request.RequestListener;
import com.github.tiwindetea.dungeonoflegend.view.entities.LivingEntityType;
import com.github.tiwindetea.dungeonoflegend.view.entities.StaticEntityType;
import com.github.tiwindetea.oggplayer.Sound;
import com.github.tiwindetea.oggplayer.Sounds;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.Scanner;
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Game.
 * @author Lucas LAZARE
 */
public class Game implements RequestListener, Runnable, Stoppable {

	private class AlreadyRunningException extends RuntimeException{}

	/* Tunning parameters for the entities generation */
	private static final int MIN_MOB_PER_LEVEL = 1;
	private static final int MAX_MOB_PER_LEVEL = 3;
	private static final int MIN_MOB_PER_ROOM = 0; // >= 0 ; extra mobs (doesn't count in min_mob_per_level ; (negative to ignore)
	private static final int MAX_MOB_PER_ROOM = 3; // >= min_mob_per_room ; same
	private static final int MIN_TRAPS_QTT_PER_LEVEL = 8;
	private static final int MAX_TRAPS_QTT_PER_LEVEL = 12;
	private static final int MIN_CHEST_QTT_PER_LEVEL = 2;
	private static final int MAX_CHEST_QTT_PER_LEVEL = 4;
	private static final int MIN_MOBS_LOOTS_PER_LEVEL = 5;
	private static final int MAX_MOBS_LOOTS_PER_LEVEL = 10;
	private static final int BULB_LOS = 2;
	private static final int MINIMUN_TURN_TIME_MS = 100;
	private static final int REFRESH_TIME_MS = 100;

	private static Logger logger = Logger.getLogger(MainPackage.name + ".model.Game");

	private Queue<RequestEvent> requestedEvent = new LinkedList<>();

	private volatile boolean isRunning = false;
	private volatile boolean isPaused = false;
	private volatile boolean viewIsLocked = false;
	private int level;
	private Map world;
	private List<javafx.util.Pair<Vector2i, Pair<StorableObject>>> objectsOnGround = new LinkedList<>();
	private List<Mob> mobs = new ArrayList<>();
	private List<Pair<InteractiveObject>> interactiveObjects = new ArrayList<>();
	private List<Player> players = new ArrayList<>();
	private List<Player> playersOnNextLevel = new ArrayList<>();
	private List<Pair<Vector2i>> bulbsOn = new ArrayList<>();
	private List<Pair<Vector2i>> bulbsOff = new ArrayList<>();
	private Seed seed;
	private int mobsLootsQtt;
	private final List<GameListener> listeners = new ArrayList<>();

	private int playerTurn;
	private Player currentPlayer;
	private Pair<Consumable> objectToUse = null;
	private List<Consumable> triggeredObjects = new ArrayList<>();

	/**
	 * The Game name.
	 */
	private String gameName;
	private static final Level debugLevel = Level.FINEST; //debug


	/* Level generation variables. Charged at the creation of a game */
	private LivingEntityType[] mobsTypes;
	private double[] mobsBaseHP;
	private double[] mobsBaseDef;
	private double[] mobsBaseAttack;
	private double[] mobsHPPerLevel;
	private double[] mobsDefPerLevel;
	private double[] mobsAttackPerLevel;
	private int[] mobsChaseRange;
	private int[] mobsBaseXpGain;
	private int[] mobsXpGainPerLevel;
	private int[] mobsLikelihood;
	private String[] mobsName;
	private int mobsLikelihoodSum;

	private double[] trapsBaseHP;
	private double[] trapsBaseMana;
	private double[] trapsHPPerLevel;
	private double[] trapsManaPerLevel;

	private ArmorType[] lootsArmorType;
	private double[] lootsArmorBaseDefense;
	private double[] lootsArmorBaseAttack;
	private double[] lootsArmorDefensePerLevel;
	private double[] lootsArmorAttackPerLevel;
	private int[] lootsArmorLikelihood;
	private int lootsArmorLikelihoodSum;

	private int[] lootsWeaponRange;
	private double[] lootsWeaponBaseAttack;
	private double[] lootsWeaponManaCost;
	private double[] lootsWeaponAttackPerLevel;
	private double[] lootsWeaponManaCostPerLevel;
	private int[] lootsWeaponLikelihood;
	private int lootsWeaponLikelihoodSum;

	private int[] lootsPotTurns;
	private double[] lootsPotHeal;
	private double[] lootsPotMana;
	private double[] lootsPotAttackMod;
	private double[] lootsPotDefMod;
	private double[] lootsPotHPMod;
	private double[] lootsPotManaMod;
	private double[] lootsPotHealPerLevel;
	private double[] lootsPotManaPerLevel;
	private int[] lootsPotLikelihood;
	private int lootsPotLikelihoodSum;

	private double[] lootsScrollBaseDamagePerTurn;
	private double[] lootsScrollBaseDamageModPerTurn;
	private double[] lootsScrollDamagePerTurnPerLevel;
	private double[] lootsScrollDamageModPerTurnPerLevel;
	private int[] lootsScrollTurns;
	private int[] lootsScrollLikelihood;
	private int lootsScrollLikelihoodSum;

	private int lootsLikelihoodSum;
	private int globalScore;
	private int bulbXp;
	private int bulbXpGainPerLevel;

	/**
	 * Instantiates a new Game.
	 *
	 * @param gameName the game name (used as the save/load file)
	 */
	public Game(String gameName) {
		this.gameName = gameName;
		this.loadMobs();
		this.loadChests();
		this.loadTraps();
		this.loadBulb();
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
		this.clearAll();
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
					Integer.parseInt(playersBundle.getString(pString + "base-leveling-xp")),
					Integer.parseInt(playersBundle.getString(pString + "leveling-xp-per-level")),
					1,
					Integer.parseInt(playersBundle.getString(pString + "maxStorageCapacity")),
					Double.parseDouble(playersBundle.getString(pString + "baseHealth")),
					Double.parseDouble(playersBundle.getString(pString + "baseMana")),
					Double.parseDouble(playersBundle.getString(pString + "baseAttack")),
					Double.parseDouble(playersBundle.getString(pString + "baseDef")),
					Double.parseDouble(playersBundle.getString(pString + "healthPerLevel")),
					Double.parseDouble(playersBundle.getString(pString + "manaPerLevel")),
					Double.parseDouble(playersBundle.getString(pString + "attackPerLevel")),
					Double.parseDouble(playersBundle.getString(pString + "defensePerLevel"))
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
		this.clearAll();

		this.players = new ArrayList<>(players.size());
		this.players.addAll(players);
		Tile[][] tiles = new Tile[1][1];
		for (int i = 0; i < tiles.length; i++) {
			for (int j = 0; j < tiles[i].length; j++) {
				tiles[i][j] = Tile.UNKNOWN;
			}
		}
		fireMapCreationEvent(new MapCreationEvent(tiles));

		for (Player player : players) {
			player.addToInventory(new Pair<>(new Pot(4, 15, 15, 0, 0, 0, 0)));
			player.addToInventory(new Pair<>(new Scroll(10, 1, 1)));
			player.addToInventory(new Pair<>(new Weapon(5, 1, 0)));
		}
		this.currentPlayer = this.players.get(0);
		fireNextTickEvent(new PlayerNextTickEvent(0));
		this.generateLevel();
		this.globalScore = 0;
	}

	private void loadMobs() {
		/* Loads the mobs stats from the .properties */
		this.mobsLikelihoodSum = 0;
		ResourceBundle mobs = ResourceBundle.getBundle(MainPackage.name + ".Mobs");
		int mobQtt = Integer.parseInt(mobs.getString("mobs.qtt"));
		this.mobsName = new String[mobQtt];
		this.mobsTypes = new LivingEntityType[mobQtt];
		this.mobsBaseHP = new double[mobQtt];
		this.mobsBaseDef = new double[mobQtt];
		this.mobsBaseAttack = new double[mobQtt];
		this.mobsHPPerLevel = new double[mobQtt];
		this.mobsDefPerLevel = new double[mobQtt];
		this.mobsAttackPerLevel = new double[mobQtt];
		this.mobsChaseRange = new int[mobQtt];
		this.mobsBaseXpGain = new int[mobQtt];
		this.mobsXpGainPerLevel = new int[mobQtt];
		this.mobsLikelihood = new int[mobQtt];
		for (int i = 0; i < mobQtt; i++) {
			String mobName = "mob" + i + ".";
			this.mobsTypes[i] = LivingEntityType.parseLivingEntity(mobs.getString(mobName + "type"));
			this.mobsName[i] = mobs.getString(mobName + "name");
			this.mobsBaseHP[i] = Double.parseDouble(mobs.getString(mobName + "base-hp"));
			this.mobsBaseDef[i] = Double.parseDouble(mobs.getString(mobName + "base-def"));
			this.mobsBaseAttack[i] = Double.parseDouble(mobs.getString(mobName + "base-attack"));
			this.mobsHPPerLevel[i] = Double.parseDouble(mobs.getString(mobName + "defense-per-level"));
			this.mobsDefPerLevel[i] = Double.parseDouble(mobs.getString(mobName + "attack-per-level"));
			this.mobsAttackPerLevel[i] = Double.parseDouble(mobs.getString(mobName + "hp-per-level"));
			this.mobsChaseRange[i] = Integer.parseInt(mobs.getString(mobName + "chase-range"));
			this.mobsBaseXpGain[i] = Integer.parseInt(mobs.getString(mobName + "base-xp-gain"));
			this.mobsXpGainPerLevel[i] = Integer.parseInt(mobs.getString(mobName + "xp-gain-per-level"));
			this.mobsLikelihood[i] = Integer.parseInt(mobs.getString(mobName + "likelihood"));
			this.mobsLikelihoodSum += this.mobsLikelihood[i];
		}
	}

	private void loadChests() {
		/* Loads the possible chests from the .properties */
		ResourceBundle loots = ResourceBundle.getBundle(MainPackage.name + ".Chests");
		int armorQtt = Integer.parseInt(loots.getString("armors.qtt"));
		this.lootsArmorType = new ArmorType[armorQtt];
		this.lootsArmorBaseDefense = new double[armorQtt];
		this.lootsArmorBaseAttack = new double[armorQtt];
		this.lootsArmorDefensePerLevel = new double[armorQtt];
		this.lootsArmorAttackPerLevel = new double[armorQtt];
		this.lootsArmorLikelihood = new int[armorQtt];
		this.lootsArmorLikelihoodSum = 0;
		for (int i = 0; i < armorQtt; i++) {
			String armorName = "armor" + i + ".";
			this.lootsArmorType[i] = ArmorType.parseArmorType(loots.getString(armorName + "type"));
			this.lootsArmorBaseDefense[i] = Double.parseDouble(loots.getString(armorName + "base-defense"));
			this.lootsArmorBaseAttack[i] = Double.parseDouble(loots.getString(armorName + "base-attack"));
			this.lootsArmorDefensePerLevel[i] = Double.parseDouble(loots.getString(armorName + "defense-per-level"));
			this.lootsArmorAttackPerLevel[i] = Double.parseDouble(loots.getString(armorName + "attack-per-level"));
			this.lootsArmorLikelihood[i] = Integer.parseInt(loots.getString(armorName + "likelihood"));
			this.lootsArmorLikelihoodSum += this.lootsArmorLikelihood[i];
		}

		int weaponQtt = Integer.parseInt(loots.getString("weapons.qtt"));
		this.lootsWeaponRange = new int[weaponQtt];
		this.lootsWeaponBaseAttack = new double[weaponQtt];
		this.lootsWeaponManaCost = new double[weaponQtt];
		this.lootsWeaponAttackPerLevel = new double[weaponQtt];
		this.lootsWeaponManaCostPerLevel = new double[weaponQtt];
		this.lootsWeaponLikelihood = new int[weaponQtt];
		this.lootsWeaponLikelihoodSum = 0;
		for (int i = 0; i < weaponQtt; i++) {
			String weaponName = "weapon" + i + ".";
			this.lootsWeaponRange[i] = Integer.parseInt(loots.getString(weaponName + "range"));
			this.lootsWeaponBaseAttack[i] = Double.parseDouble(loots.getString(weaponName + "base-attack"));
			this.lootsWeaponManaCost[i] = Double.parseDouble(loots.getString(weaponName + "mana-cost"));
			this.lootsWeaponAttackPerLevel[i] = Double.parseDouble(loots.getString(weaponName + "attack-per-level"));
			this.lootsWeaponManaCostPerLevel[i] = Double.parseDouble(loots.getString(weaponName + "mana-cost-per-level"));
			this.lootsWeaponLikelihood[i] = Integer.parseInt(loots.getString(weaponName + "likelihood"));
			this.lootsWeaponLikelihoodSum += this.lootsWeaponLikelihood[i];
		}

		int potQtt = Integer.parseInt(loots.getString("pots.qtt"));
		this.lootsPotTurns = new int[potQtt];
		this.lootsPotHeal = new double[potQtt];
		this.lootsPotMana = new double[potQtt];
		this.lootsPotAttackMod = new double[potQtt];
		this.lootsPotDefMod = new double[potQtt];
		this.lootsPotHPMod = new double[potQtt];
		this.lootsPotManaMod = new double[potQtt];
		this.lootsPotHealPerLevel = new double[potQtt];
		this.lootsPotManaPerLevel = new double[potQtt];
		this.lootsPotLikelihood = new int[potQtt];
		this.lootsPotLikelihoodSum = 0;
		for (int i = 0; i < potQtt; i++) {
			String potName = "pot" + i + ".";
			this.lootsPotTurns[i] = Integer.parseInt(loots.getString(potName + "turns"));
			this.lootsPotHeal[i] = Double.parseDouble(loots.getString(potName + "base-hp"));
			this.lootsPotMana[i] = Double.parseDouble(loots.getString(potName + "base-mana"));
			this.lootsPotAttackMod[i] = Double.parseDouble(loots.getString(potName + "attack-mod"));
			this.lootsPotDefMod[i] = Double.parseDouble(loots.getString(potName + "def-mod"));
			this.lootsPotHPMod[i] = Double.parseDouble(loots.getString(potName + "hp-mod"));
			this.lootsPotManaMod[i] = Double.parseDouble(loots.getString(potName + "mana-mod"));
			this.lootsPotHealPerLevel[i] = Double.parseDouble(loots.getString(potName + "hp-per-level"));
			this.lootsPotManaPerLevel[i] = Double.parseDouble(loots.getString(potName + "mana-per-level"));
			this.lootsPotLikelihood[i] = Integer.parseInt(loots.getString(potName + "likelihood"));
			this.lootsPotLikelihoodSum += this.lootsPotLikelihood[i];
		}

		int scrollQtt = Integer.parseInt(loots.getString("scrolls.qtt"));
		this.lootsScrollBaseDamagePerTurn = new double[scrollQtt];
		this.lootsScrollBaseDamageModPerTurn = new double[scrollQtt];
		this.lootsScrollDamagePerTurnPerLevel = new double[scrollQtt];
		this.lootsScrollDamageModPerTurnPerLevel = new double[scrollQtt];
		this.lootsScrollTurns = new int[scrollQtt];
		this.lootsScrollLikelihood = new int[scrollQtt];
		this.lootsScrollLikelihoodSum = 0;
		for (int i = 0; i < scrollQtt; i++) {
			String scrollName = "scroll" + i + ".";
			this.lootsScrollBaseDamagePerTurn[i] = Double.parseDouble(loots.getString(scrollName + "base-damage-per-turn"));
			this.lootsScrollBaseDamageModPerTurn[i] = Double.parseDouble(loots.getString(scrollName + "base-damage-mod-per-turn"));
			this.lootsScrollDamagePerTurnPerLevel[i] = Double.parseDouble(loots.getString(scrollName + "damage-per-turn-per-level"));
			this.lootsScrollDamageModPerTurnPerLevel[i] = Double.parseDouble(loots.getString(scrollName + "damage-mod-per-turn-per-level"));
			this.lootsScrollTurns[i] = Integer.parseInt(loots.getString(scrollName + "turns"));
			this.lootsScrollLikelihood[i] = Integer.parseInt(loots.getString(scrollName + "likelihood"));
			this.lootsScrollLikelihoodSum += this.lootsScrollLikelihood[i];
		}

		this.lootsLikelihoodSum = this.lootsScrollLikelihoodSum + this.lootsPotLikelihoodSum + this.lootsWeaponLikelihoodSum + this.lootsArmorLikelihoodSum;
	}

	private void loadTraps() {
		/* Loads the possible traps from the .properties */
		ResourceBundle traps = ResourceBundle.getBundle(MainPackage.name + ".Traps", Locale.getDefault());
		int trapsQtt = Integer.parseInt(traps.getString("traps.qtt"));
		this.trapsBaseHP = new double[trapsQtt];
		this.trapsBaseMana = new double[trapsQtt];
		this.trapsHPPerLevel = new double[trapsQtt];
		this.trapsManaPerLevel = new double[trapsQtt];
		for (int i = 0; i < trapsQtt; i++) {
			String trapName = "trap" + i + ".";
			this.trapsBaseHP[i] = Double.parseDouble(traps.getString(trapName + "base-hp"));
			this.trapsBaseMana[i] = Double.parseDouble(traps.getString(trapName + "base-mana"));
			this.trapsHPPerLevel[i] = Double.parseDouble(traps.getString(trapName + "hp-per-level"));
			this.trapsManaPerLevel[i] = Double.parseDouble(traps.getString(trapName + "mana-per-level"));
		}
	}

	private void loadBulb() {
		ResourceBundle bulbs = ResourceBundle.getBundle(MainPackage.name + ".StaticEntity", Locale.getDefault());
		this.bulbXp = Integer.parseInt(bulbs.getString("lit-bulb.xp.gain"));
		this.bulbXpGainPerLevel = Integer.parseInt(bulbs.getString("lit-bulb.xp.gain-per-level"));
	}

	/**
	 * Adds a Gamelistener.
	 *
	 * @param listener the listener
	 */
	public void addGameListener(GameListener listener) {
		this.listeners.add(listener);
		LivingThing.addGameListener(listener);
	}

	private GameListener[] getGameListeners() {
		return this.listeners.toArray(new GameListener[this.listeners.size()]);
	}

	private void fireLivingEntityCreationEvent(LivingEntityCreationEvent event) {
		for (GameListener listener : this.getGameListeners()) {
			listener.createLivingEntity(event);
		}
	}

	private void fireLivingEntityDeletionEvent(LivingEntityDeletionEvent event) {
		for (GameListener listener : this.getGameListeners()) {
			listener.deleteLivingEntity(event);
		}
	}

	private void fireLivingEntityLOSDefinitionEvent(LivingEntityLOSDefinitionEvent event) {
		for (GameListener listener : this.getGameListeners()) {
			listener.defineLivingEntityLOS(event);
		}
	}

	private void fireLivingEntityLOSModificationEvent(LivingEntityLOSModificationEvent event) {
		for (GameListener listener : this.getGameListeners()) {
			listener.modifyLivingEntityLOS(event);
		}
	}

	private void fireScoreUpdateEvent(ScoreUpdateEvent event) {
		for (GameListener listener : getGameListeners()) {
			listener.updateScore(event);
		}
	}

	private void fireMapCreationEvent(MapCreationEvent event) {
		for (GameListener listener : this.getGameListeners()) {
			listener.createMap(event);
		}
	}

	private void firePlayerCreationEvent(PlayerCreationEvent event) {
		for (GameListener listener : this.getGameListeners()) {
			listener.createPlayer(event);
		}
	}

	private void fireStaticEntityCreationEvent(StaticEntityCreationEvent event) {
		for (GameListener listener : this.getGameListeners()) {
			listener.createStaticEntity(event);
		}
	}

	private void fireStaticEntityDeletionEvent(StaticEntityDeletionEvent event) {
		for (GameListener listener : this.getGameListeners()) {
			listener.deleteStaticEntity(event);
		}
	}

	private void fireStaticEntityLOSDefinitionEvent(StaticEntityLOSDefinitionEvent event) {
		for (GameListener listener : this.getGameListeners()) {
			listener.defineStaticEntityLOS(event);
		}
	}

	private void fireTileModificationEvent(TileModificationEvent event) {
		for (GameListener listener : getGameListeners()) {
			listener.modifyTile(event);
		}
	}

	private void fireNextTickEvent(PlayerNextTickEvent event) {
		for (GameListener listener : getGameListeners()) {
			listener.playerNextTick(event);
		}
	}

	private void fireFogAdditionEvent(FogAdditionEvent event) {
		for (GameListener listener : getGameListeners()) {
			listener.addFog(event);
		}
	}

	private void fireCenterOnTileEvent(CenterOnTileEvent event) {
		for (GameListener listener : getGameListeners()) {
			listener.centerOnTile(event);
		}
	}

	private void firePlayerDeletionEvent(PlayerDeletionEvent event) {
		for (GameListener listener : getGameListeners()) {
			listener.deletePlayer(event);
		}
	}

	private void fireLevelUpdateEvent(LevelUpdateEvent event) {
		for (GameListener listener : getGameListeners()) {
			listener.updateLevel(event);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void requestDrop(DropRequestEvent e) {
		this.requestedEvent.add(e);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void requestInteraction(InteractionRequestEvent e) {
		this.requestedEvent.add(e);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void requestUsage(UsageRequestEvent e) {
		this.requestedEvent.add(e);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void requestMove(MoveRequestEvent e) {
		this.requestedEvent.add(e);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void requestCenterView(CenterViewRequestEvent e) {
		this.requestedEvent.add(e);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void requestEquipping(EquipRequestEvent e) {
		this.requestedEvent.add(e);
	}

	/**
	 * {@inhericDoc}
	 */
	@Override
	public void requestLockView(LockViewRequestEvent e) {
		this.viewIsLocked = !this.viewIsLocked;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void stop() {
		this.isRunning = false;
	}

	/**
	 * generates a level
	 */
	private void generateLevel() {

		Sound.player.play(Sounds.NEXT_FLOOR_SOUND);
		System.out.println("Entering level " + this.level + " of seed [" + this.seed.getAlphaSeed() + " ; " + this.seed.getBetaSeed() + "]");
		fireLevelUpdateEvent(new LevelUpdateEvent(this.level));

		/* Generate the level and bulbs to turn off */
		List<Map.Room> rooms = this.world.generateLevel(this.level);
		fireMapCreationEvent(new MapCreationEvent(this.world.getMapCopy()));

		for (Vector2i bulb : this.world.getBulbPosition()) {
			Pair<Vector2i> p = new Pair<>(bulb);
			Pair<Vector2i> p2 = new Pair<>(bulb);
			this.bulbsOn.add(p);
			this.bulbsOff.add(p2);
			fireStaticEntityCreationEvent(new StaticEntityCreationEvent(p2.getId(), StaticEntityType.UNLIT_BULB, bulb, "An Unlit bulb"));
			fireStaticEntityCreationEvent(new StaticEntityCreationEvent(p.getId(), StaticEntityType.LIT_BULB, bulb, "A Lit bulb"));
			fireStaticEntityLOSDefinitionEvent(new StaticEntityLOSDefinitionEvent(
					p.getId(),
					this.world.getLOS(bulb, BULB_LOS)
			));
		}

		Random random = this.seed.getRandomizer(this.level);
		int chestsNbr = random.nextInt(MAX_CHEST_QTT_PER_LEVEL - MIN_CHEST_QTT_PER_LEVEL + 1) + MIN_CHEST_QTT_PER_LEVEL;
		int trapsNbr = random.nextInt(MAX_TRAPS_QTT_PER_LEVEL - MIN_TRAPS_QTT_PER_LEVEL + 1) + MIN_TRAPS_QTT_PER_LEVEL;
		int selection;
		Vector2i pos;

		/* Generate traps */
		for (int i = 0; i < trapsNbr; i++) {
			selection = random.nextInt(this.trapsBaseHP.length);
			pos = selectRandomGroundPosition(rooms, random);
			Pair<InteractiveObject> pair = new Pair<>(new InteractiveObject(pos,
					this.trapsBaseHP[selection] + this.trapsHPPerLevel[selection] * random.nextInt(3) + this.level - 1,
					this.trapsBaseMana[selection] + this.trapsManaPerLevel[selection] * random.nextInt(3) + this.level - 1));
			this.interactiveObjects.add(pair);
			fireStaticEntityCreationEvent(new StaticEntityCreationEvent(pair.getId(), StaticEntityType.TRAP, pos, pair.object.getDescription()));
		}

		/* Generate some chests */
		for (int i = 0; i < chestsNbr; i++) {
			Pair<InteractiveObject> pair;
			//TODOchestLevel = random.nextInt(3) + this.level - 1;
			pos = selectRandomGroundPosition(rooms, random);
			pair = new Pair<>(new InteractiveObject(pos, lootSelector(random)));
			this.interactiveObjects.add(pair);
			fireStaticEntityCreationEvent(new StaticEntityCreationEvent(pair.getId(), StaticEntityType.CHEST, pos, pair.object.getDescription()));
		}

		/* Generate the mobs */
		Mob.setMap(this.world);
		int mobsNbr = random.nextInt(MAX_MOB_PER_LEVEL - MIN_MOB_PER_LEVEL + 1) + MIN_MOB_PER_LEVEL;
		this.mobs = new ArrayList<>(mobsNbr);
		for (int i = 0; i < mobsNbr; ++i) {
			do {
				pos = selectRandomGroundPosition(rooms, random);
			} while (pos.equals(this.world.getStairsUpPosition()));
			this.addRandomMob(pos, random);
		}

		if (MIN_MOB_PER_ROOM >= 0) {
			for (Map.Room room : rooms) {
				mobsNbr = random.nextInt(MAX_MOB_PER_ROOM - MIN_MOB_PER_ROOM + 1) + MIN_MOB_PER_ROOM;
				for (int i = 0; i < mobsNbr; i++) {
					ArrayList<Map.Room> r = new ArrayList<>();
					r.add(room);
					do {
						pos = selectRandomGroundPosition(r, random);
					} while (pos.equals(this.world.getStairsUpPosition()));
					this.addRandomMob(pos, random);
				}
			}
		}

		this.mobsLootsQtt = random.nextInt(MAX_MOBS_LOOTS_PER_LEVEL - MIN_MOBS_LOOTS_PER_LEVEL + 1) + MIN_MOBS_LOOTS_PER_LEVEL;

		this.players.addAll(this.playersOnNextLevel);
		this.playersOnNextLevel.clear();
		this.triggeredObjects.clear();

		/* Move the players to the right position */
		for (Player player : this.players) {
			fireLivingEntityCreationEvent(new LivingEntityCreationEvent(
					player.getId(),
					player.getGType(),
					new Vector2i(0, 0),
					Direction.DOWN,
					player.getDescription()
			));
			if (player.hasFallen) {
				do {
					player.setPosition(this.selectRandomGroundPosition(rooms, random));
				} while (Tile.isObstructed(this.world.getTile(player.getPosition())));
				player.hasFallen = false;
			} else {
				player.heal(player.getMaxHitPoints() / 3);
				player.addMana(player.getMaxMana());
				player.setPosition(this.world.getStairsUpPosition());
			}
			fireLivingEntityLOSDefinitionEvent(new LivingEntityLOSDefinitionEvent(
					player.getId(),
					this.world.getLOS(player.getPosition(), player.getLos())
			));
		}
		fireCenterOnTileEvent(new CenterOnTileEvent(this.players.get(0).getPosition()));
	}

	//-------------------------------------//
	private Vector2i selectRandomGroundPosition(List<Map.Room> rooms, Random random) {
		Map.Room selectedRoom;
		Vector2i pos = new Vector2i();
		do {
			selectedRoom = rooms.get(random.nextInt(rooms.size()));
			pos.x = random.nextInt(selectedRoom.bottom.x - selectedRoom.top.x + 1) + selectedRoom.top.x;
			pos.y = random.nextInt(selectedRoom.bottom.y - selectedRoom.top.y + 1) + selectedRoom.top.y;
		} while (!isAccessible(pos));
		return pos;
	}

	/**
	 * Step into the next tick (next player)
	 */
	private void nextTick() {
		if (this.players.isEmpty()) {
			return;
		}
		this.objectToUse = null;
		do {
			++this.playerTurn;
		}
		while (this.playerTurn < this.players.size()
				&& this.players.get(this.playerTurn).getFloor() != this.level);

		if (this.playerTurn >= this.players.size()) {
			this.playerTurn = 0;
			this.currentPlayer = this.players.get(0);
			long time = System.currentTimeMillis();
			this.nextTurn();
			time = System.currentTimeMillis() - time;
			if (time < MINIMUN_TURN_TIME_MS) {
				try {
					Thread.sleep(MINIMUN_TURN_TIME_MS - time);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		} else {
			this.currentPlayer = this.players.get(this.playerTurn);
		}
		if (!this.players.isEmpty()) {
			fireNextTickEvent(new PlayerNextTickEvent(this.currentPlayer.getNumber()));
		}

		if (this.players.size() == 0 && this.playersOnNextLevel.size() > 0) {
			++this.level;
			this.save();
			this.clearLevel();
			this.generateLevel(); // Hot dog !
		}

		if (this.viewIsLocked) {
			fireCenterOnTileEvent(new CenterOnTileEvent(this.currentPlayer.getPosition()));
		}
	}

	/**
	 * Step into the next turn (all entities live)
	 */
	private void nextTurn() {
		logger.log(this.debugLevel, "Starting players lives");
		Vector2i pos;
		HashMap<Integer, Pair<StorableObject>> objectsJustDropped = new HashMap<>();

		/* Letting the players to play */
		for (int i = 0; i < this.players.size(); i++) {
			Player player = this.players.get(i);
			Vector2i playerPos = player.getPosition();
			int distance;
			Pair<StorableObject> drop;
			if ((pos = player.getRequestedMove()) != null) {
			/*See if the player can move as he wanted to */
				distance = Math.abs(playerPos.x - pos.x) + Math.abs(playerPos.y - pos.y);
				if (distance <= 1 && isAccessible(pos)) {
					player.setPosition(pos);
					fireLivingEntityLOSDefinitionEvent(new LivingEntityLOSDefinitionEvent(player.getId(), this.world.getLOS(pos, player.getLos())));
				} else if (distance <= 1 && this.world.getTile(pos) == Tile.HOLE) {
					player.setFloor(this.level + 1);
					player.setPosition(new Vector2i(0, 0));
					player.hasFallen = true;
					player.damage(player.getMaxHitPoints() / 5);
					fireLivingEntityDeletionEvent(new LivingEntityDeletionEvent(player.getId()));
					this.playersOnNextLevel.add(player);
					this.players.remove(i);
					--i;
				} else {
					logger.log(this.debugLevel, "Player " + player.getName() + " requested an invalid move ! (from "
							+ playerPos + " to " + pos + ")");
					player.moveRequestDenied();
				}
			} else if ((pos = player.getRequestedAttack()) != null) {
			/* See if the player can attack as he wants to (ignoring the los) [TODO ?] */
				if (playerPos.squaredDistance(pos) <= Math.pow(player.getAttackRange(), 2)) {
					boolean[][] los = this.world.getLOS(playerPos, player.getLos());
					if (los[los.length / 2 - playerPos.x + pos.x][los[0].length / 2 - playerPos.y + pos.y]) {
						int j = this.mobs.indexOf(new Mob(pos));
						if (j >= 0) {
							player.attack(this.mobs.get(j));
						} else {
							// Healing by 20%, because why not
							player.heal(player.getMaxHitPoints() / 5);
						}
					}
				}
				player.setRequestedAttack(null);
			} else if ((pos = player.getRequestedInteraction()) != null) {
			/* Make the player to interact with the map */
				distance = Math.max(Math.abs(playerPos.x - pos.x), Math.abs(playerPos.y - pos.y));
				if (distance == 1) {
					this.world.triggerTile(pos);
					fireTileModificationEvent(new TileModificationEvent(pos, this.world.getTile(pos)));
				}
				player.setRequestedInteraction(null);
			} else if ((drop = player.getObjectToDrop()) != null) {
				pos = player.getDropPos();
				distance = Math.abs(playerPos.x - pos.x) + Math.abs(playerPos.y - pos.y);
				if (drop.object != null && distance <= 1) {
					player.dropRequestAccepted();
					if (this.world.getTile(pos) != Tile.HOLE) {
						objectsJustDropped.put(new Integer(player.getNumber()), drop);
						fireStaticEntityCreationEvent(new StaticEntityCreationEvent(
								drop.getId(),
								drop.object.getGType(),
								pos,
								drop.object.getDescription()
						));
						this.objectsOnGround.add(new javafx.util.Pair<>(pos, drop));
					}
				}
			} else {
				String msg = "Nothing to do with player " + player.getName()
						+ " (player #" + (player.getNumber() + 1) + ")";
				logger.log(this.debugLevel, msg);
			}
		}

		ArrayList<Mob> mobToDelete = new ArrayList<>(3);
		/* Letting the mobs to play */
		for (Mob mob : this.mobs) {
			if (mob.isAlive()) {
				mob.live(this.mobs, this.players, this.world.getLOS(mob.getPosition(), mob.getChaseRange()));
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
								mob.attack(this.mobs.get(j));
							}
						} else {
							if (j >= 0) {
								mob.attack(this.players.get(j));
							}
						}
					}
				}
			} else {
				mobToDelete.add(mob); // Woops, you're dead
				for (Player player : this.players) {
					player.xp(mob.getXpGain());
				}
			}
		}

		Random random = new Random();
		for (Mob mob : mobToDelete) {
			fireLivingEntityDeletionEvent(new LivingEntityDeletionEvent(mob.getId()));
			if (random.nextInt(101) < 100 * this.mobsLootsQtt / this.mobs.size()) {
				this.mobsLootsQtt--;
				Pair<StorableObject> loot = new Pair<>(lootSelector(random));
				this.objectsOnGround.add(new javafx.util.Pair<>(mob.getPosition(), loot));
				fireStaticEntityCreationEvent(new StaticEntityCreationEvent(
						loot.getId(),
						loot.object.getGType(),
						mob.getPosition(),
						loot.object.getDescription()
				));
			}
			this.mobs.remove(mob);
		}

		/* Scanning for dead players */
		ArrayList<Player> playerToDelete = new ArrayList<>();
		for (Player player : this.players) {
			if (!player.isAlive()) {
				playerToDelete.add(player);
			}
		}

		for (Player player : playerToDelete) {
			fireLivingEntityLOSDefinitionEvent(new LivingEntityLOSDefinitionEvent(
					player.getId(),
					new boolean[0][]
			));
			fireLivingEntityDeletionEvent(new LivingEntityDeletionEvent(player.getId()));
			this.players.remove(player);
			this.deletePlayer(player);
			for (int number = player.getNumber(); number < this.players.size(); number++) {
				this.players.get(number).setNumber(this.players.get(number).getNumber() - 1);
			}
			for (Player player1 : this.playersOnNextLevel) {
				player1.setNumber(player1.getNumber() - 1);
			}
		}
		if (this.players.isEmpty() && this.playersOnNextLevel.isEmpty()) {
			this.isRunning = false;
		}

		/* Letting consumable do their effects */
		ArrayList<Consumable> consumableToDelete = new ArrayList<>();
		for (Consumable consumable : this.triggeredObjects) {
			if (consumable.nextTick()) { // if its effect is finished
				consumableToDelete.add(consumable);
			}
		}

		for (Consumable consumable : consumableToDelete) {
			this.triggeredObjects.remove(consumable);
		}

		/* Scanning for loots&chests to give to players, for traps, next level & light bulb */
		for (int i = 0; i < this.players.size(); ++i) {
			Player player = this.players.get(i);
			pos = player.getPosition();

			/* Loots */
			Iterator<javafx.util.Pair<Vector2i, Pair<StorableObject>>> iter = this.objectsOnGround.iterator();
			while (iter.hasNext()) {
				javafx.util.Pair<Vector2i, Pair<StorableObject>> objPair = iter.next();//TODO
				Pair<StorableObject> objDroppedByPlayer = objectsJustDropped.get(new Integer(player.getNumber()));
				if (objPair.getKey().equals(pos)
						&& (objDroppedByPlayer == null || objDroppedByPlayer.getId() != objPair.getValue().getId())
						&& player.addToInventory(objPair.getValue())) {
					fireStaticEntityDeletionEvent(new StaticEntityDeletionEvent(objPair.getValue().getId()));
					iter.remove();
				}
			}

			/* Next floor */
			if (!player.isARequestPending()) {
				if (player.getPosition().equals(this.world.getStairsDownPosition())) {
					player.setFloor(this.level + 1);
					player.setPosition(new Vector2i(0, 0));
					fireLivingEntityDeletionEvent(new LivingEntityDeletionEvent(player.getId()));
					this.playersOnNextLevel.add(player);
					this.players.remove(i);
					--i;
				}
			}

			/* bulbs */
			for (int j = 0; j < this.bulbsOn.size(); j++) {
				if (this.bulbsOn.get(j).object.equals(player.getPosition())) {
					fireStaticEntityDeletionEvent(new StaticEntityDeletionEvent(this.bulbsOn.get(j).getId()));
					this.bulbsOn.remove(j);
					Sound.player.play(Sounds.BULB_SOUND);
					player.heal(2 * player.getMaxHitPoints() / 3);
					player.xp(this.bulbXp + this.bulbXpGainPerLevel * this.level);
					++this.globalScore;
					fireScoreUpdateEvent(new ScoreUpdateEvent(this.globalScore));
				}
			}

			/* chests and traps */
			ArrayList<Pair<InteractiveObject>> objectsToDelete = new ArrayList<>();
			for (Pair<InteractiveObject> interactiveObject : this.interactiveObjects) {
				if (interactiveObject.object.getPosition().equals(player.getPosition())) {
					if (interactiveObject.object.trigger(player)) {
						objectsToDelete.add(interactiveObject);
					}
				}
			}

			for (Pair<InteractiveObject> interactiveObject : objectsToDelete) {
				fireStaticEntityDeletionEvent(new StaticEntityDeletionEvent(interactiveObject.getId()));
				this.interactiveObjects.remove(interactiveObject);
			}
		}
		this.recomputePlayersLoses();
	}

	//-------------------------------------//
	private boolean isAccessible(Vector2i pos) {
		return !(Tile.isObstructed(this.world.getTile(pos))
				|| this.mobs.contains(new Mob(pos)));
	}

	/**
	 * Loads a save.
	 */
	public void loadSave() {
		try {

			Scanner file = new Scanner(new FileInputStream(this.gameName));
			clearAll();
			Tile[][] tiles = new Tile[1][1];
			for (int i = 0; i < tiles.length; i++) {
				for (int j = 0; j < tiles[i].length; j++) {
					tiles[i][j] = Tile.UNKNOWN;
				}
			}
			fireMapCreationEvent(new MapCreationEvent(tiles));
			this.seed = Seed.parseSeed(file.nextLine());
			this.world = new Map(this.seed);
			String str = file.nextLine();
			this.level = Integer.parseInt(str.substring(str.indexOf('=') + 1));
			str = file.nextLine();
			this.globalScore = Integer.parseInt(str.substring(str.indexOf('=') + 1));
			this.players.clear();
			while (file.hasNext()) {
				str = file.nextLine();
				this.players.add(Player.parsePlayer(str));
				this.players.get(this.players.size() - 1).setFloor(this.level);
			}
			file.close();
			this.currentPlayer = this.players.get(0);
			fireNextTickEvent(new PlayerNextTickEvent(this.currentPlayer.getNumber()));
			fireScoreUpdateEvent(new ScoreUpdateEvent(this.globalScore));
			this.generateLevel();
		} catch (FileNotFoundException e) {
			System.out.println("Failed to open the save file");
			e.printStackTrace();
		}
	}

	//-------------------------------------//
	private void save() {
		PriorityQueue<Player> playerPriorityQueue = new PriorityQueue<>(new Comparator<Player>() {
			@Override
			public int compare(Player o1, Player o2) {
				return o1.getNumber() - o2.getNumber();
			}
		});
		playerPriorityQueue.addAll(this.playersOnNextLevel);
		try {
			FileWriter file = new FileWriter(new File(this.gameName));
			file.write(this.seed.toString() + "\n");
			file.write("level=" + this.level + "\n");
			file.write("score=" + this.globalScore + "\n");
			while (!playerPriorityQueue.isEmpty()) {
				file.write(playerPriorityQueue.poll().toString() + "\n");
			}
			file.close();
		} catch (IOException e) {
			System.out.println("Failed to open the save file");
			e.printStackTrace();
		}
	}

	private void recomputePlayersLoses() {
		for (Player player : this.players) {
			Vector2i pos = player.getPosition();
			boolean[][] playerLOS = this.world.getLOS(pos, player.getLos());
			player.live(this.mobs, this.players, playerLOS);
			fireLivingEntityLOSDefinitionEvent(new LivingEntityLOSDefinitionEvent(
					player.getId(),
					playerLOS
			));
			fireFogAdditionEvent(new FogAdditionEvent(
					pos,
					this.world.getLOS(pos, player.getExploreLOS())
			));
		}
	}

	private void addRandomMob(Vector2i mobPos, Random random) {
		int selectedMob = randomSelector(random, this.mobsLikelihoodSum, this.mobsLikelihood);
		int mobLevel = random.nextInt(3) + this.level - 1;
		Mob mob = new Mob(
				this.mobsName[selectedMob],
				mobLevel,
				mobLevel * this.mobsXpGainPerLevel[selectedMob] + this.mobsBaseXpGain[selectedMob],
				mobLevel * this.mobsHPPerLevel[selectedMob] + this.mobsBaseHP[selectedMob],
				mobLevel * this.mobsAttackPerLevel[selectedMob] + this.mobsBaseAttack[selectedMob],
				mobLevel * this.mobsDefPerLevel[selectedMob] + this.mobsBaseDef[selectedMob],
				this.mobsChaseRange[selectedMob],
				mobPos.copy());
		this.mobs.add(mob);
		fireLivingEntityCreationEvent(new LivingEntityCreationEvent(mob.getId(), this.mobsTypes[selectedMob],
				mobPos.copy(), Direction.DOWN, mob.getDescription()));
	}

	private StorableObject lootSelector(Random random) {
		int chestLevel = random.nextInt(3) + this.level - 1;
		Vector2i selectedChest = stuffSelector(random);
		int selection = selectedChest.y;
		StorableObject ans;
		switch (selectedChest.x) {
			case 0:
				ans = new Armor(this.lootsArmorDefensePerLevel[selection] * chestLevel + this.lootsArmorBaseDefense[selection],
						this.lootsArmorAttackPerLevel[selection] * chestLevel + this.lootsArmorBaseAttack[selection],
						this.lootsArmorType[selection]);
				break;
			case 1:
				ans = new Weapon(this.lootsWeaponBaseAttack[selection] + this.lootsWeaponAttackPerLevel[selection] * chestLevel,
						this.lootsWeaponRange[selection],
						this.lootsWeaponManaCost[selection] + this.lootsWeaponManaCostPerLevel[selection] * chestLevel);
				break;
			case 2:
				ans = new Scroll(this.lootsScrollTurns[selection],
						this.lootsScrollBaseDamagePerTurn[selection] + chestLevel * this.lootsScrollDamagePerTurnPerLevel[selection],
						this.lootsScrollBaseDamageModPerTurn[selection] + chestLevel * this.lootsScrollDamageModPerTurnPerLevel[selection]);
				break;
			case 3:
					/* Falls through */
			default:
				ans = new Pot(this.lootsPotTurns[selection],
						this.lootsPotHeal[selection] + this.lootsPotHealPerLevel[selection] * chestLevel,
						this.lootsPotMana[selection] + this.lootsPotManaPerLevel[selection] * chestLevel,
						this.lootsPotDefMod[selection],
						this.lootsPotAttackMod[selection],
						this.lootsPotHPMod[selection],
						this.lootsPotManaMod[selection]);
				break;
		}
		return ans;
	}

	private Vector2i stuffSelector(Random random) {
		Vector2i ans = new Vector2i();
		int rand = random.nextInt(this.lootsLikelihoodSum);
		int sum = this.lootsArmorLikelihoodSum;
		if (rand <= sum) {
			ans.x = 0;
			ans.y = randomSelector(random, this.lootsArmorLikelihoodSum, this.lootsArmorLikelihood);

		} else {
			sum += this.lootsWeaponLikelihoodSum;
			if (rand <= sum) {
				ans.x = 1;
				ans.y = randomSelector(random, this.lootsWeaponLikelihoodSum, this.lootsWeaponLikelihood);

			} else {
				sum += this.lootsScrollLikelihoodSum;
				if (rand <= sum) {
					ans.x = 2;
					ans.y = randomSelector(random, this.lootsScrollLikelihoodSum, this.lootsScrollLikelihood);

				} else {
					ans.x = 3;
					ans.y = randomSelector(random, this.lootsPotLikelihoodSum, this.lootsPotLikelihood);
				}
			}
		}
		return ans;
	}

	private int randomSelector(Random random, int likelihoodSum, int[] table) {
		int rand = random.nextInt(likelihoodSum);
		int i = 0;
		do {
			rand -= table[i++];
		} while (rand >= 0);
		return i - 1;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void run() {
		if (this.isRunning){
			throw new AlreadyRunningException();
		}
		this.isRunning = true;
		// TODO : run slower and slower up to n
		RequestEvent event;
		do {
			try {
				Thread.sleep(REFRESH_TIME_MS);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if (!this.isPaused) {
				do {
					event = this.requestedEvent.poll();
					while (event != null) {
						try { // debug
							switch (event.getSubType()) {
								case MOVE_REQUEST_EVENT:
									treatRequestEvent((MoveRequestEvent) event);
									break;
								case INTERACTION_REQUEST_EVENT:
									treatRequestEvent((InteractionRequestEvent) event);
									break;
								case USAGE_REQUEST_EVENT:
									treatRequestEvent((UsageRequestEvent) event);
									break;
								case DROP_REQUEST_EVENT:
									treatRequestEvent((DropRequestEvent) event);
									break;
								case CENTER_VIEW_REQUEST_EVENT:
									treatRequestEvent((CenterViewRequestEvent) event);
									break;
								case EQUIP_REQUEST_EVENT:
									treatRequestEvent((EquipRequestEvent) event);
									break;
							}
						} catch (Exception e) {
							// debug
							e.printStackTrace();
						} finally {
							event = this.requestedEvent.poll();
						}
					}
					if (this.currentPlayer.isARequestPending() || this.currentPlayer.getFloor() != this.level) {
						this.nextTick(); // modifies the here-above boolean
					}
				} while (this.currentPlayer.isARequestPending() || this.currentPlayer.getFloor() != this.level);
			}
		} while (this.isRunning);

		boolean[][] bool = new boolean[(this.world.getSize().x / 2) * 2 + 1][(this.world.getSize().y / 2) * 2 + 1];
		for (int i = 0; i < bool.length; i++) {
			for (int j = 0; j < bool[i].length; j++) {
				bool[i][j] = true;
			}
		}
		fireLivingEntityCreationEvent(new LivingEntityCreationEvent(
				Pair.ERROR_VAL,
				LivingEntityType.LITTLE_SATANIC_DUCK,
				new Vector2i(this.world.getSize().x / 2, this.world.getSize().y / 2),
				Direction.DOWN,
				""
		));
		fireLivingEntityLOSDefinitionEvent(new LivingEntityLOSDefinitionEvent(Pair.ERROR_VAL, bool));
		fireLivingEntityDeletionEvent(new LivingEntityDeletionEvent(Pair.ERROR_VAL));
	}

	private void treatRequestEvent(CenterViewRequestEvent e) {
		fireCenterOnTileEvent(new CenterOnTileEvent(this.currentPlayer.getPosition()));
	}

	private void treatRequestEvent(DropRequestEvent e) {
		/* Take the shortest path possible to drop the item */
		if(!Tile.isObstructed(this.world.getTile(e.getDropPosition())) || this.world.getTile(e.getDropPosition()) == Tile.HOLE) {
			Stack<Vector2i> path = this.shortestPathApprox(this.currentPlayer.getPosition(), e.getDropPosition(), false, null);
			if (path != null) {
				this.currentPlayer.setRequestedPath(path);
				this.currentPlayer.setObjectToDrop(e.getObjectId(), e.getDropPosition());
				this.nextTick();
			}
		}
	}

	private void treatRequestEvent(InteractionRequestEvent e) {
		logger.log(this.debugLevel, "interaction requested for player " + this.currentPlayer.getNumber());
		Stack<Vector2i> path = this.world.getPath(this.currentPlayer.getPosition(), e.getTilePosition(), false, null);
		boolean success = true;
		Tile tile = this.world.getTile(e.getTilePosition());
		int distance = Math.max(Math.abs(e.getTilePosition().x - this.currentPlayer.getPosition().x),
		  Math.abs(e.getTilePosition().y - this.currentPlayer.getPosition().y));

		if ((tile == Tile.CLOSED_DOOR || tile == Tile.OPENED_DOOR) && distance > 1) {
			path = shortestPathApprox(this.currentPlayer.getPosition(), e.getTilePosition(), false, null);
			if (path != null) {
				this.currentPlayer.setRequestedPath(path);
				this.currentPlayer.setRequestedInteraction(e.getTilePosition());
			} else {
				success = false;
			}
		} else if (distance <= this.currentPlayer.getLos()) {
		/* Looking for a mob to attack*/
			boolean[][] los = this.world.getLOS(this.currentPlayer.getPosition(), this.currentPlayer.getLos());
			Vector2i p = this.currentPlayer.getPosition();
			if(los[los.length / 2 - p.x + e.getTilePosition().x][los[0].length / 2 - p.y + e.getTilePosition().y]) {
				int i = -1;
				int j = 0;
				while (i == -1 && j < this.mobs.size()) {
					if(this.mobs.get(j).getPosition().equals(e.getTilePosition())) {
						i = j;
					}
					++j;
				}
				if (i >= 0) {
					if (this.objectToUse != null) {
						this.objectToUse.object.trigger(this.mobs.get(i));
						this.currentPlayer.removeFromInventory(new Pair<>(this.objectToUse));
						this.triggeredObjects.add(this.objectToUse.object);
						this.objectToUse = null;
						success = false;
						path = null;
					} else {
						this.currentPlayer.setRequestedAttack(e.getTilePosition());
						if(p.squaredDistance(e.getTilePosition()) > Math.pow(this.currentPlayer.getAttackRange(), 2)) {
							Collection<LivingThing> shadow = new ArrayList<>(this.mobs.size());
							shadow.addAll(this.mobs);
							shadow.addAll(this.players);
							this.currentPlayer.setRequestedPath(shortestPathApprox(p, e.getTilePosition(), false, shadow));
							this.currentPlayer.setSawDuck(false);
						}
					}
				} else if (distance == 1 && (tile == Tile.CLOSED_DOOR || tile == Tile.OPENED_DOOR)) {
					/* interaction with doors */
					this.currentPlayer.setRequestedInteraction(e.getTilePosition());
				} else {
					success = false; // no mob found, neither doors
				}
			} else {
				success = false; // tile not in the los
			}
		} else {
			success = false; // tile too far away
		}

		/* If you weren't able to do anything, just go to the tile */
		if (!success && path != null) {
			this.currentPlayer.setRequestedPath(path);
			success = true;
		}

		/* If you did something, keep going */
		if (success) {
			nextTick();
		} else {
			this.currentPlayer.doNothing();
		}
	}

	private void treatRequestEvent(UsageRequestEvent e) {
		logger.log(this.debugLevel, "Usage requested for player " + this.currentPlayer.getNumber());
		List<Pair<StorableObject>> inventory = this.currentPlayer.getInventory();
		this.objectToUse = null;
		Pair<StorableObject> obj = null;
		for (Pair<StorableObject> pair : inventory) {
			if(pair.getId() == e.getObjectId()) {
				if (pair.object.getType() == StorableObjectType.CONSUMABLE) {
					this.objectToUse = new Pair<>(pair.getId(), (Consumable) pair.object);
				} else {
					obj = pair;
				}
			}
		}
		if (this.objectToUse != null) {
			if (this.objectToUse.object.getConsumableType() == ConsumableType.POT) {
				this.triggeredObjects.add(this.objectToUse.object);
				this.objectToUse.object.trigger(this.currentPlayer);
				this.currentPlayer.removeFromInventory(new Pair<>(this.objectToUse));
				this.objectToUse = null;
			}
		} else if (obj != null) {
			this.currentPlayer.removeFromInventory(obj);
			if (obj.object.getType() == StorableObjectType.ARMOR) {
				this.currentPlayer.equipWithArmor(new Pair<>(obj.getId(), (Armor) obj.object));
			} else if (obj.object.getType() == StorableObjectType.WEAPON) {
				this.currentPlayer.equipWithWeapon(new Pair<>(obj.getId(), (Weapon) obj.object));
			}
		} else {
			this.currentPlayer.unequip(e.getObjectId());
		}
	}

	private void treatRequestEvent(MoveRequestEvent e) {
		logger.log(this.debugLevel, "Move requested for player " + this.currentPlayer.getNumber());
		Vector2i pos = this.currentPlayer.getPosition().copy().add(e.getMoveDirection());
		if (isAccessible(pos)) {
			Stack<Vector2i> stack = new Stack<>();
			stack.add(pos);
			this.currentPlayer.setRequestedPath(stack);
			this.nextTick();
		} else {
			this.requestInteraction(new InteractionRequestEvent(pos));
		}
	}

	private void treatRequestEvent(EquipRequestEvent e) {
		List<Pair<StorableObject>> inventory = this.currentPlayer.getInventory();
		this.objectToUse = null;
		for (Pair<StorableObject> pair : inventory) {
			if(pair.getId() == e.getObjectId()) {
				if (pair.object.getType() == StorableObjectType.CONSUMABLE) {
				} else {
					if (pair != null) {
						this.currentPlayer.removeFromInventory(pair);
						if (pair.object.getType() == StorableObjectType.ARMOR) {
							this.currentPlayer.equipWithArmor(new Pair<>(pair.getId(), (Armor) pair.object));
						} else if (pair.object.getType() == StorableObjectType.WEAPON) {
							this.currentPlayer.equipWithWeapon(new Pair<>(pair.getId(), (Weapon) pair.object));
						}
					}
				}
				return;
			}
		}
		this.currentPlayer.unequip(e.getObjectId());
	}

	/**
	 * @return true if an instance of the game is running, false otherwise
     */
	public boolean isRunning() {
		return this.isRunning;
	}

	private Stack<Vector2i> shortestPathApprox(Vector2i dep, Vector2i arr, boolean ignoreDoors, Collection<LivingThing> entities) {
		Direction[] directions = {Direction.LEFT, Direction.DOWN, Direction.RIGHT, Direction.UP};
		Stack<Vector2i> ans = null;
		Stack<Vector2i> tmp;
		for (Direction direction : directions) {
			tmp = this.world.getPath(dep, arr.copy().add(direction), ignoreDoors, entities);
			if ((tmp != null) && (ans == null || tmp.size() < ans.size())) {
				ans = tmp;
			}
		}
		tmp = this.world.getPath(dep, arr, ignoreDoors, entities);
		if ((tmp != null) && (ans == null || tmp.size() < ans.size())) {
			ans = tmp;
		}
		return ans;
	}

	/**
	 * Pauses the game (ignore any incoming events)
	 */
	public void pause() {
		this.requestedEvent.clear();
		for (Player player : this.players) {
			player.doNothing();
		}
		this.isPaused = true;
	}

	/**
	 * Resumes the game
	 */
	public void resume() {
		this.requestedEvent.clear();
		this.isPaused = false;
	}

	private void clearAll() {
		this.clearLevel();
		for (int i = this.players.size() - 1; i >= 0; i--) {
			this.deletePlayer(this.players.get(i));
		}
		this.players.clear();
		for (int i = this.playersOnNextLevel.size() - 1; i >= 0; i--) {
			this.deletePlayer(this.playersOnNextLevel.get(i));
		}
		this.playersOnNextLevel.clear();
	}

	private void clearLevel() {
		/* Next block deletes all entities on the GUI */
		for (Player player : this.players) {
			fireLivingEntityDeletionEvent(new LivingEntityDeletionEvent(player.getId()));
		}

		for (Mob mob : this.mobs) {
			fireLivingEntityDeletionEvent(new LivingEntityDeletionEvent(mob.getId()));
		}
		this.mobs.clear();

		for (Pair<InteractiveObject> obj : this.interactiveObjects) {
			fireStaticEntityDeletionEvent(new StaticEntityDeletionEvent(obj.getId()));
		}
		this.interactiveObjects.clear();

		for (Pair<Vector2i> bulb : this.bulbsOff) {
			fireStaticEntityDeletionEvent(new StaticEntityDeletionEvent(bulb.getId()));
		}
		this.bulbsOff.clear();

		for (Pair<Vector2i> bulb : this.bulbsOn) {
			fireStaticEntityDeletionEvent(new StaticEntityDeletionEvent(bulb.getId()));
		}
		this.bulbsOn.clear();

		for (javafx.util.Pair<Vector2i, Pair<StorableObject>> pair : this.objectsOnGround) {
			fireStaticEntityDeletionEvent(new StaticEntityDeletionEvent(pair.getValue().getId()));
		}
		this.objectsOnGround.clear();
	}

	private void deletePlayer(Player player) {
		player.deleteEquipedObjects();
		List<Pair<StorableObject>> playerInventory = new ArrayList<>(player.getInventory().size());
		playerInventory.addAll(player.getInventory());
		playerInventory.forEach(player::removeFromInventory);
		firePlayerDeletionEvent(new PlayerDeletionEvent(player.getNumber()));
	}
}
