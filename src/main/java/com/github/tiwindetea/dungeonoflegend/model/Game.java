//////////////////////////////////////////////////////////////////////////////////
//                                                                              //
//     This Source Code Form is subject to the terms of the Mozilla Public      //
//     License, v. 2.0. If a copy of the MPL was not distributed with this      //
//     file, You can obtain one at http://mozilla.org/MPL/2.0/.                 //
//                                                                              //
//////////////////////////////////////////////////////////////////////////////////


// TODO: Fog LOS

package com.github.tiwindetea.dungeonoflegend.model;

import com.github.tiwindetea.dungeonoflegend.events.living_entities.LivingEntityCreationEvent;
import com.github.tiwindetea.dungeonoflegend.events.living_entities.LivingEntityDeletionEvent;
import com.github.tiwindetea.dungeonoflegend.events.living_entities.LivingEntityLOSDefinitionEvent;
import com.github.tiwindetea.dungeonoflegend.events.living_entities.LivingEntityLOSModificationEvent;
import com.github.tiwindetea.dungeonoflegend.events.map.CenterOnTileEvent;
import com.github.tiwindetea.dungeonoflegend.events.map.FogAdditionEvent;
import com.github.tiwindetea.dungeonoflegend.events.map.MapCreationEvent;
import com.github.tiwindetea.dungeonoflegend.events.map.TileModificationEvent;
import com.github.tiwindetea.dungeonoflegend.events.players.PlayerCreationEvent;
import com.github.tiwindetea.dungeonoflegend.events.players.PlayerNextTickEvent;
import com.github.tiwindetea.dungeonoflegend.events.requests.CenterViewRequestEvent;
import com.github.tiwindetea.dungeonoflegend.events.requests.InteractionRequestEvent;
import com.github.tiwindetea.dungeonoflegend.events.requests.MoveRequestEvent;
import com.github.tiwindetea.dungeonoflegend.events.requests.RequestEvent;
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
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Queue;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.Scanner;
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;

//FIXME : Next level broken (when you fall into a hole, you can get outside of the map)
//FIXME : level 3 of seed [8626971708176625149 ; -4303329363946094496] : there is a bonus raoul in the map

/**
 * Game.
 * @author Lucas LAZARE
 */
public class Game implements RequestListener, Runnable, Stopable {

	/* Tunning parameters for the entities generation */
	private static final int MIN_MOB_PER_LEVEL = 10;
	private static final int MAX_MOB_PER_LEVEL = 12;
	private static final int MIN_MOB_PER_ROOM = 0; // >= 0 ; extra mobs (doesn't count in min_mob_per_level ; (negative to ignore)
	private static final int MAX_MOB_PER_ROOM = 3; // > min_mob_per_room ; same
	private static final int MIN_TRAPS_QTT_PER_LEVEL = 20;
	private static final int MAX_TRAPS_QTT_PER_LEVEL = 50;
	private static final int MIN_CHEST_QTT_PER_LEVEL = 20;
	private static final int MAX_CHEST_QTT_PER_LEVEL = 30;
	private static final int BULB_LOS = 2;
	private static final int MINIMUN_TURN_TIME_MS = 100;
	private static final int REFRESH_TIME_MS = 250;

	private static Logger logger = Logger.getLogger(MainPackage.name + ".model.Game");

	private Queue<RequestEvent> requestedEvent = new LinkedList<>();
	private boolean isRunning = true;
	private int level;
	private Map world;
	private HashMap<Vector2i, Pair<StorableObject>> objectsOnGround = new HashMap<>();
	private List<Mob> mobs = new ArrayList<>();
	private List<Pair<InteractiveObject>> interactiveObjects = new ArrayList<>();
	private List<Player> players = new ArrayList<>();
	private List<Player> playersOnNextLevel = new ArrayList<>();
	private List<Pair<Vector2i>> bulbsOn = new ArrayList<>();
	private List<Pair<Vector2i>> bulbsOff = new ArrayList<>();
	private Seed seed;
	private final List<GameListener> listeners = new ArrayList<>();

	private int playerTurn;
	private Player currentPlayer;
	private Pair<Consumable> objectToUse = null;
	private List<Consumable> triggeredObjects = new ArrayList<>();

	/**
	 * The Game name.
	 */
	private String gameName;
	private static final Level debugLevel = Level.INFO; //debug


	/* Level generation variables. Charged at the creation of a game */
	private LivingEntityType[] mobsTypes;
	private int[] mobsBaseHP;
	private int[] mobsBaseDef;
	private int[] mobsBaseAttack;
	private int[] mobsHPPerLevel;
	private int[] mobsDefPerLevel;
	private int[] mobsAttackPerLevel;
	private int[] mobsChaseRange;
	private int[] mobsBaseXpGain;
	private int[] mobsXpGainPerLevel;
	private int[] mobsLikelihood;
	private String[] mobsName;
	private int mobsLikelihoodSum;

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
					Integer.parseInt(playersBundle.getString(pString + "base-leveling-xp")),
					Integer.parseInt(playersBundle.getString(pString + "leveling-xp-per-level")),
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
		this.players.addAll(players);
		Tile[][] tiles = new Tile[1][1];
		for (int i = 0; i < tiles.length; i++) {
			for (int j = 0; j < tiles[i].length; j++) {
				tiles[i][j] = Tile.UNKNOWN;
			}
		}
		fireMapCreationEvent(new MapCreationEvent(tiles));
		for (Player player : this.players) {
			firePlayerCreationEvent(new PlayerCreationEvent(
					player.getNumber(),
					player.getId(),
					new Vector2i(0, 0),
					Direction.DOWN,
					player.getMaxHitPoints(),
					player.getMaxMana(),
					player.getDescription()
			));
		}

		for (Player player : players) {
			player.addToInventory(new Pair<>(new Pot(3, 15, 15, 0, 0, 0, 0)));
			player.addToInventory(new Pair<>(new Scroll(10, 1, 1)));
		}
		this.currentPlayer = this.players.get(0);
		this.loadMobs();
		this.loadChests();
		this.loadTraps();
		this.generateLevel();
		fireNextTickEvent(new PlayerNextTickEvent(0));
	}

	private void loadMobs() {
		/* Loads the mobs stats from the .properties */
		this.mobsLikelihoodSum = 0;
		ResourceBundle mobs = ResourceBundle.getBundle(MainPackage.name + ".Mobs");
		int mobQtt = Integer.parseInt(mobs.getString("mobs.qtt"));
		this.mobsName = new String[mobQtt];
		this.mobsTypes = new LivingEntityType[mobQtt];
		this.mobsBaseHP = new int[mobQtt];
		this.mobsBaseDef = new int[mobQtt];
		this.mobsBaseAttack = new int[mobQtt];
		this.mobsHPPerLevel = new int[mobQtt];
		this.mobsDefPerLevel = new int[mobQtt];
		this.mobsAttackPerLevel = new int[mobQtt];
		this.mobsChaseRange = new int[mobQtt];
		this.mobsBaseXpGain = new int[mobQtt];
		this.mobsXpGainPerLevel = new int[mobQtt];
		this.mobsLikelihood = new int[mobQtt];
		for (int i = 0; i < mobQtt; i++) {
			String mobName = "mob" + i + ".";
			this.mobsTypes[i] = LivingEntityType.parseLivingEntity(mobs.getString(mobName + "type"));
			this.mobsName[i] = mobs.getString(mobName + "name");
			this.mobsBaseHP[i] = Integer.parseInt(mobs.getString(mobName + "base-hp"));
			this.mobsBaseDef[i] = Integer.parseInt(mobs.getString(mobName + "base-def"));
			this.mobsBaseAttack[i] = Integer.parseInt(mobs.getString(mobName + "base-attack"));
			this.mobsHPPerLevel[i] = Integer.parseInt(mobs.getString(mobName + "defense-per-level"));
			this.mobsDefPerLevel[i] = Integer.parseInt(mobs.getString(mobName + "attack-per-level"));
			this.mobsAttackPerLevel[i] = Integer.parseInt(mobs.getString(mobName + "hp-per-level"));
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
		/* Loads the possible traps from the .properties */
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
			listener.modifieLivingEntityLOS(event);
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
			listener.modifieTile(event);
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

	@Override
	public void requestCenterView(CenterViewRequestEvent e) {
		this.requestedEvent.add(e);
	}

	@Override
	public void stop() {
		this.isRunning = false;
	}

	/**
	 * generates a level
	 */
	private void generateLevel() {

		/* Next block deletes all entities on the GUI */
		for (Player player : this.players) {
			fireLivingEntityDeletionEvent(new LivingEntityDeletionEvent(player.getId()));
		}
		for (Mob mob : this.mobs) {
			fireLivingEntityDeletionEvent(new LivingEntityDeletionEvent(mob.getId()));
		}
		for (Pair<InteractiveObject> obj : this.interactiveObjects) {
			fireStaticEntityDeletionEvent(new StaticEntityDeletionEvent(obj.getId()));
		}
		for (Pair<Vector2i> bulb : this.bulbsOff) {
			fireStaticEntityDeletionEvent(new StaticEntityDeletionEvent(bulb.getId()));
		}
		for (Pair<Vector2i> bulb : this.bulbsOn) {
			fireStaticEntityDeletionEvent(new StaticEntityDeletionEvent(bulb.getId()));
		}
		for (Pair<InteractiveObject> interactiveObject : this.interactiveObjects) {
			fireStaticEntityDeletionEvent(new StaticEntityDeletionEvent(interactiveObject.getId()));
		}
		System.out.println("Entering level " + this.level + " of seed [" + this.seed.getAlphaSeed() + " ; " + this.seed.getBetaSeed() + "]");

		/* Generate the level and bulbs to turn off */
		List<Map.Room> rooms = this.world.generateLevel(this.level);
		fireMapCreationEvent(new MapCreationEvent(this.world.getMapCopy()));


		this.bulbsOn.clear();
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
		int chestsNbr = random.nextInt(MAX_CHEST_QTT_PER_LEVEL - MIN_CHEST_QTT_PER_LEVEL) + MIN_CHEST_QTT_PER_LEVEL;
		int trapsNbr = random.nextInt(MAX_TRAPS_QTT_PER_LEVEL - MIN_TRAPS_QTT_PER_LEVEL) + MIN_TRAPS_QTT_PER_LEVEL;
		int selection, chestLevel;
		Vector2i pos;

		/* Generate traps */
		this.interactiveObjects.clear();
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
			chestLevel = random.nextInt(3) + this.level - 1;
			pos = selectRandomGroundPosition(rooms, random);
			switch (random.nextInt(4)) {
				case 0:
					selection = random.nextInt(this.lootsArmorType.length);
					Armor armor = new Armor(this.lootsArmorDefensePerLevel[selection] * chestLevel + this.lootsArmorBaseDefense[selection],
							this.lootsArmorAttackPerLevel[selection] * chestLevel + this.lootsArmorBaseAttack[selection],
							this.lootsArmorType[selection]);
					pair = new Pair<>(new InteractiveObject(pos, armor));
					break;
				case 1:
					selection = random.nextInt(this.lootsWeaponRange.length);
					Weapon weapon = new Weapon(this.lootsWeaponBaseAttack[selection] + this.lootsWeaponAttackPerLevel[selection] * chestLevel,
							this.lootsWeaponRange[selection],
							this.lootsWeaponManaCost[selection] + this.lootsWeaponManaCostPerLevel[selection] * chestLevel);
					pair = new Pair<>(new InteractiveObject(pos, weapon));
					break;
				case 2:
					selection = random.nextInt(this.lootsScrollTurns.length);
					Scroll scroll = new Scroll(this.lootsScrollTurns[selection],
							this.lootsScrollBaseDamagePerTurn[selection] + chestLevel * this.lootsScrollDamagePerTurnPerLevel[selection],
							this.lootsScrollBaseDamageModPerTurn[selection] + chestLevel * this.lootsScrollDamageModPerTurnPerLevel[selection]);
					pair = new Pair<>(new InteractiveObject(pos, scroll));
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
					break;
			}
			this.interactiveObjects.add(pair);
			fireStaticEntityCreationEvent(new StaticEntityCreationEvent(pair.getId(), StaticEntityType.CHEST, pos, pair.object.getDescription()));
		}

		/* Generate the mobs */
		Mob.setMap(this.world);
		int mobsNbr = random.nextInt(MAX_MOB_PER_LEVEL - MIN_MOB_PER_LEVEL) + MIN_MOB_PER_LEVEL;
		this.mobs = new ArrayList<>(mobsNbr);
		for (int i = 0; i < mobsNbr; ++i) {
			this.addRandomMob(this.selectRandomGroundPosition(rooms, random), random);
		}

		if (MIN_MOB_PER_ROOM >= 0) {
			for (Map.Room room : rooms) {
				mobsNbr = random.nextInt(MAX_MOB_PER_ROOM - MIN_MOB_PER_ROOM) + MIN_MOB_PER_ROOM;
				for (int i = 0; i < mobsNbr; i++) {
					ArrayList<Map.Room> r = new ArrayList<>();
					r.add(room);
					this.addRandomMob(this.selectRandomGroundPosition(r, random), random);
				}
			}
		}

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
		fireCenterOnTileEvent(new CenterOnTileEvent(this.currentPlayer.getPosition()));
	}

	//-------------------------------------//
	private Vector2i selectRandomGroundPosition(List<Map.Room> rooms, Random random) {
		Map.Room selectedRoom;
		Vector2i pos = new Vector2i();
		do {
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
		} while (Tile.isObstructed(this.world.getTile(pos)));
		return pos;
	}

	/**
	 * Step into the next tick (next player)
	 */
	private void nextTick() {
		this.objectToUse = null;
		do {
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
			fireNextTickEvent(new PlayerNextTickEvent(this.currentPlayer.getNumber()));
		} while (this.currentPlayer.isARequestPending() || this.currentPlayer.getFloor() != this.level);
	}

	/**
	 * Step into the next turn (all entities live)
	 */
	private void nextTurn() {
		logger.log(this.debugLevel, "Starting players lives");
		Vector2i pos;

		/* Letting the players to play */
		for (int i = 0; i < this.players.size(); i++) {
			Player player = this.players.get(i);
			player.live(this.mobs, this.players, this.world.getLOS(player.getPosition(), player.getLos()));
			Pair<StorableObject> drop;
			if (player.getFloor() == this.level) {
				if ((pos = player.getRequestedMove()) != null) {
				/*See if the player can move as he wanted to */
					int distance = Math.abs(player.getPosition().x - pos.x) + Math.abs(player.getPosition().y - pos.y);
					if (distance <= 1 && isAccessible(pos)) {
						player.setPosition(pos);
						fireLivingEntityLOSDefinitionEvent(new LivingEntityLOSDefinitionEvent(player.getId(), this.world.getLOS(pos, player.getLos())));
					} else if (distance <= 1 && this.world.getTile(pos) == Tile.HOLE) {
						player.setFloor(this.level + 1);
						player.hasFallen = true;
						player.damage(player.getMaxHitPoints() / 5);
						fireLivingEntityDeletionEvent(new LivingEntityDeletionEvent(player.getId()));
						this.playersOnNextLevel.add(player);
						this.players.remove(i);
						--i;
					} else {
						logger.log(this.debugLevel, "Player " + player.getName() + " requested an invalid move ! (from "
								+ player.getPosition() + " to " + pos + ")");
						player.moveRequestDenied();
					}
				} else if ((pos = player.getRequestedAttack()) != null) {
				/* See if the player can attack as he wants to (ignoring the los) [TODO ?] */
					if (player.getPosition().squaredDistance(pos) <= Math.pow(player.getAttackRange(), 2)) {
						int j = this.mobs.indexOf(new Mob(pos));
						if (j >= 0) {
							player.attack(this.mobs.get(j));
						} else {
							// Healing by 20%, because why not
							player.heal(player.getMaxHitPoints() / 5);
						}
					}
					player.setRequestedAttack(null);
				} else if ((pos = player.getRequestedInteraction()) != null) {
				/* Make the player to interact with the map */
					this.world.triggerTile(pos);
					fireTileModificationEvent(new TileModificationEvent(pos, this.world.getTile(pos)));
					player.setRequestedInteraction(null);
				} else if ((drop = player.getObjectToDrop()) != null) {
					pos = player.getDropPos();
					int distance = Math.abs(player.getPosition().x - pos.x) + Math.abs(player.getPosition().y - pos.y);
					if (drop.object != null && distance == 1) {
						fireStaticEntityCreationEvent(new StaticEntityCreationEvent(
								drop.getId(),
								drop.object.getGType(),
								pos,
								drop.object.getDescription()
						));
						player.dropRequestAccepted();
						this.objectsOnGround.put(pos, drop);
					}
				} else {
					String msg = "Nothing to do with player " + player.getName()
							+ " (player #" + (player.getNumber() + 1) + ")";
					logger.log(this.debugLevel, msg);
					throw new RuntimeException(msg);
				}
			} else {
				this.playersOnNextLevel.add(player);
				this.players.remove(i);
				--i;
			}
		}

		if (this.players.size() == 0) {
			++this.level;
			this.save();
			this.generateLevel(); // Hot dog !
			return;
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

		for (Mob mob : mobToDelete) {
			fireLivingEntityDeletionEvent(new LivingEntityDeletionEvent(mob.getId()));
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
		for (Player player : this.players) {
			pos = player.getPosition();
			Pair<StorableObject> drop = this.objectsOnGround.get(pos);

			while (drop != null && player.addToInventory(drop)) {
				fireStaticEntityDeletionEvent(new StaticEntityDeletionEvent(drop.getId()));
				this.objectsOnGround.remove(pos, drop);
				drop = this.objectsOnGround.get(pos);
			}

			if (player.getPosition().equals(this.world.getStairsDownPosition())) {
				player.setFloor(this.level + 1);
				player.setPosition(new Vector2i(0, 0));
				fireLivingEntityDeletionEvent(new LivingEntityDeletionEvent(player.getId()));
			}

			for (int i = 0; i < this.bulbsOn.size(); i++) {
				if (this.bulbsOn.get(i).object.equals(player.getPosition())) {
					fireStaticEntityDeletionEvent(new StaticEntityDeletionEvent(this.bulbsOn.get(i).getId()));
					this.bulbsOn.remove(i);
				}
			}

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
		this.recomputeLOS();
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
				this.players.add(Player.parsePlayer(line));
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
			for (Player player : this.players) {
				file.write(player.toString() + "\n");
			}
			file.close();
		} catch (IOException e) {
			System.out.println("Failed to open the save file");
			e.printStackTrace();
		}
	}

	private void recomputeLOS() {
		for (Player player : this.players) {
			Vector2i pos = player.getPosition();
			fireLivingEntityLOSDefinitionEvent(new LivingEntityLOSDefinitionEvent(
					player.getId(),
					this.world.getLOS(pos, player.getLos())
			));
			fireFogAdditionEvent(new FogAdditionEvent(
					pos,
					this.world.getLOS(pos, player.getExploreLOS())
			));
		}
	}

	private void addRandomMob(Vector2i mobPos, Random random) {
		int selectedMob = this.mobSelector(random);
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

	private int mobSelector(Random random) {
		int rand = random.nextInt(this.mobsLikelihoodSum);
		int likelihoodSum = 0;
		int i = 0;
		do {
			likelihoodSum += this.mobsLikelihood[i++];
		} while (likelihoodSum <= rand);
		return i - 1;
	}

	@Override
	public void run() {
		RequestEvent event;
		do {
			try {
				Thread.sleep(REFRESH_TIME_MS);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			event = this.requestedEvent.poll();
			while (event != null) {
				try {
					switch (event.getType()) {
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
					}
					event = this.requestedEvent.poll();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} while (this.isRunning);
	}

	private void treatRequestEvent(CenterViewRequestEvent e) {
		fireCenterOnTileEvent(new CenterOnTileEvent(this.currentPlayer.getPosition()));
	}

	private void treatRequestEvent(DropRequestEvent e) {
		/* Take the shortest path possible to drop the item */
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
				this.nextTick();
			}
		}
	}

	private void treatRequestEvent(InteractionRequestEvent e) {
		logger.log(this.debugLevel, "interaction requested for player " + this.currentPlayer.getNumber());
		Stack<Vector2i> path = this.world.getPath(this.currentPlayer.getPosition(), e.tilePosition, false, null);
		boolean success = true;
		Tile tile = this.world.getTile(e.tilePosition);
		int distance = Math.max(Math.abs(e.tilePosition.x - this.currentPlayer.getPosition().x),
				Math.abs(e.tilePosition.y - this.currentPlayer.getPosition().y));


		if (distance <= this.currentPlayer.getLos()) {
		/* Looking for a mob to attack*/
			boolean[][] los = this.world.getLOS(this.currentPlayer.getPosition(), this.currentPlayer.getLos());
			Vector2i p = this.currentPlayer.getPosition();
			if (los[los.length / 2 - p.x + e.tilePosition.x][los[0].length / 2 - p.y + e.tilePosition.y]) {
				int i = -1;
				int j = 0;
				do {
					if (this.mobs.get(j).getPosition().equals(e.tilePosition)) {
						i = j;
					}
					++j;
				} while (i == -1 && j < this.mobs.size());
				if (i >= 0) {
					if (this.objectToUse != null) {
						System.out.println("Object used : " + this.objectToUse);
						this.objectToUse.object.trigger(this.mobs.get(i));
						this.currentPlayer.removeFromInventory(new Pair<>(this.objectToUse));
						this.triggeredObjects.add(this.objectToUse.object);
						this.objectToUse = null;
						success = false;
						path = null;
					} else {
						this.currentPlayer.setRequestedAttack(e.tilePosition);
					}
				} else if (distance == 1 && (tile == Tile.CLOSED_DOOR || tile == Tile.OPENED_DOOR)) {
					/* interaction with doors */
					this.currentPlayer.setRequestedInteraction(e.tilePosition);
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
		}
	}

	private void treatRequestEvent(UsageRequestEvent e) {
		logger.log(this.debugLevel, "Usage requested for player " + this.currentPlayer.getNumber());
		List<Pair<StorableObject>> inventory = this.currentPlayer.getInventory();
		this.objectToUse = null;
		Pair<StorableObject> obj = null;
		for (Pair<StorableObject> pair : inventory) {
			if (pair.getId() == e.objectId) {
				if (pair.object.getType() == StorableObjectType.CONSUMABLE) {
					System.out.println("Object selected : " + pair.object);
					this.objectToUse = new Pair<>(pair.getId(), (Consumable) pair.object);
				} else {
					obj = pair;
				}
			}
		}
		if (this.objectToUse != null) {
			if (this.objectToUse.object.getConsumableType() == ConsumableType.POT) {
				System.out.println("Selected : Pot");
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
			this.currentPlayer.unequip(e.objectId);
		}
	}

	private void treatRequestEvent(MoveRequestEvent e) {
		logger.log(this.debugLevel, "Move requested for player " + this.currentPlayer.getNumber());
		Vector2i pos = this.currentPlayer.getPosition().copy().add(e.moveDirection);
		if (isAccessible(pos)) {
			Stack<Vector2i> stack = new Stack<>();
			stack.add(pos);
			this.currentPlayer.setRequestedPath(stack);
			this.nextTick();
		} else {
			this.requestInteraction(new InteractionRequestEvent(pos));
		}
	}
}
