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
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.Scanner;
import java.util.Stack;
import java.util.stream.Collectors;

/**
 * Created by maxime on 4/24/16.
 */
public class Game implements RequestListener {
	/* Tunning parameters for the entities generation */
	public static final int MIN_MOB_QTT_PER_LEVEL = 15;
	public static final int MAX_MOB_QTT_PER_LEVEL = 20;
	public static final int MIN_TRAPS_QTT_PER_LEVEL = 2;
	public static final int MAX_TRAPS_QTT_PER_LEVEL = 5;
	public static final int MIN_CHEST_QTT_PER_LEVEL = 1;
	public static final int MAX_CHEST_QTT_PER_LEVEL = 5;


	private int level;
	private Map world;
	private ArrayList<Pair<StorableObject>> objectsOnGround = new ArrayList<>();
	private ArrayList<Pair<Mob>> mobs = new ArrayList<>();
	private ArrayList<Pair<InteractiveObject>> interactiveObjects = new ArrayList<>();
	private ArrayList<Pair<Player>> players = new ArrayList<>();
	private ArrayList<Pair<Vector2i>> bulbs = new ArrayList<>();
	private Seed seed;
	private final List<GameListener> listeners = new ArrayList<>();
	private LivingEntityType[] mobsTypes = new LivingEntityType[0];
	private int[] mobsBaseHP = new int[0];
	private int[] mobsBaseDef = new int[0];
	private int[] mobsBaseAttack = new int[0];
	private int[] mobsHPPerLevel = new int[0];
	private int[] mobsDefPerLevel = new int[0];
	private int[] mobsAttackPerLevel = new int[0];

	private int[] trapsBaseHP = new int[0];
	private int[] trapsBaseMana = new int[0];
	private int[] trapsHPPerLevel = new int[0];
	private int[] trapsManaPerLevel = new int[0];

	private ArmorType[] lootsArmorType = new ArmorType[0];
	private int[] lootsArmorBaseDefense = new int[0];
	private int[] lootsArmorBaseAttack = new int[0];
	private int[] lootsArmorDefensePerLevel = new int[0];
	private int[] lootsArmorAttackPerLevel = new int[0];

	private int[] lootsWeaponRange = new int[0];
	private int[] lootsWeaponBaseAttack = new int[0];
	private int[] lootsWeaponManaCost = new int[0];
	private int[] lootsWeaponAttackPerLevel = new int[0];
	private int[] lootsWeaponManaCostPerLevel = new int[0];

	private int[] lootsPotTurns = new int[0];
	private int[] lootsPotHeal = new int[0];
	private int[] lootsPotMana = new int[0];
	private int[] lootsPotAttackMod = new int[0];
	private int[] lootsPotDefMod = new int[0];
	private int[] lootsPotHPMod = new int[0];
	private int[] lootsPotManaMod = new int[0];
	private int[] lootsPotHealPerLevel = new int[0];
	private int[] lootsPotManaPerLevel = new int[0];

	private int[] lootsScrollBaseDamagePerTurn = new int[0];
	private int[] lootsScrollBaseDamageModPerTurn = new int[0];
	private int[] lootsScrollDamagePerTurnPerLevel = new int[0];
	private int[] lootsScrollDamageModPerTurnPerLevel = new int[0];
	private int[] lootsScrollTurns = new int[0];

	private int playerTurn;

	String gameName;

	public Game(String gameName) {
		this.gameName = gameName;
	}

	public void init(Collection<Player> players) {
		this.init(players, new Seed(), 1);
	}

	public void init(int numberOfPlayers) {
		ArrayList<Player> players = new ArrayList<>();
		ResourceBundle playersBundle = ResourceBundle.getBundle(MainPackage.name + ".Players");

		for (int i = 0; i < numberOfPlayers; i++) {
			String pString = "player" + i + ".";
			players.add(new Player(
					playersBundle.getString(pString + "name"),
					Integer.parseInt(playersBundle.getString(pString + "los")),
					Integer.parseInt(playersBundle.getString(pString + "explorelos")),
					1,
					Integer.parseInt(playersBundle.getString(pString + "maxStorageCapacity")),
					Integer.parseInt(playersBundle.getString(pString + "baseHealth")),
					Integer.parseInt(playersBundle.getString(pString + "baseMana")),
					Integer.parseInt(playersBundle.getString(pString + "baseAttack")),
					Integer.parseInt(playersBundle.getString(pString + "baseDef")),
					Integer.parseInt(playersBundle.getString(pString + "healthPerLevel")),
					Integer.parseInt(playersBundle.getString(pString + "anaPerLevel")),
					Integer.parseInt(playersBundle.getString(pString + "attackPerLevel")),
					Integer.parseInt(playersBundle.getString(pString + "defensePerLevel"))
			));
		}
		this.init(players, new Seed(), 1);
	}

	public void init(Collection<Player> players, Seed seed, int level) {
		this.playerTurn = 0;
		this.level = level;
		this.seed = seed;
		this.world = new Map(this.seed);

		this.players = new ArrayList<>(players.size());
		this.players.addAll(players.stream().map(Pair::new).collect(Collectors.toList()));
		this.world.generateLevel(level);
		this.loadMobs();
		this.loadChests();
		this.loadTraps();
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
		for (int i = 0; i < mobQtt; i++) {
			String mobName = "mob" + i + ".";
			this.mobsTypes[i] = LivingEntityType.parseLivingEntity(mobs.getString(mobName + "type"));
			this.mobsBaseHP[i] = Integer.parseInt(mobs.getString(mobName + "base-hp"));
			this.mobsBaseDef[i] = Integer.parseInt(mobs.getString(mobName + "base-def"));
			this.mobsBaseAttack[i] = Integer.parseInt(mobs.getString(mobName + "base-attack"));
			this.mobsHPPerLevel[i] = Integer.parseInt(mobs.getString(mobName + "defense-per-level"));
			this.mobsDefPerLevel[i] = Integer.parseInt(mobs.getString(mobName + "attack-per-level"));
			this.mobsAttackPerLevel[i] = Integer.parseInt(mobs.getString(mobName + "hp-per-level"));
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

	public void addGameListener(GameListener listener) {
		this.listeners.add(listener);
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

	private void fireStaticEntityLOSDefinitionEvent(StaticEntityLOSDefinitionEvent event) {
		for(GameListener listener : this.getGameListeners()) {
			listener.defineStaticEntityLOS(event);
		}
	}

	@Override
	public void requestDrop(DropRequestEvent e) {
		ArrayList<Stack<Vector2i>> paths = new ArrayList<>(4);
		Direction[] dirs = {Direction.LEFT, Direction.RIGHT, Direction.UP, Direction.DOWN};
		for (Direction direction : dirs) {
			paths.add(this.world.getPath(this.player().getPosition().add(direction), e.dropPosition, false, null));
		}
		Stack<Vector2i> theChoosenOne = paths.get(0);
		for (Stack<Vector2i> stack : paths) {
			if (stack != null && stack.size() < theChoosenOne.size()) {
				theChoosenOne = stack;
			}
		}
		if (paths != null) {
			this.player().setRequestedPath(theChoosenOne);
		}
		this.player().setObjectToDropId(e.objectId);
		this.nextTurn();
	}

	@Override
	public void requestInteraction(InteractionRequestEvent e) {
		Tile tile = this.world.getTile(e.tilePosition);
		int distance = e.tilePosition.distance(this.player().getPosition());
		if (distance == 1 && (tile == Tile.CLOSED_DOOR || tile == Tile.OPENED_DOOR)) {
			this.world.triggerTile(e.tilePosition);
			//TODO : fire event
		} else if (distance <= player().getLos()) {
			// TODO : GUI Fires event only if it is a visited tile ?
		}
	}

	@Override
	public void requestUsage(UsageRequestEvent e) {
		//TODO
	}

	@Override
	public void requestMove(MoveRequestEvent e) {

	}

	private void generateLevel() {
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
		ArrayList<Map.Room> rooms = this.world.generateLevel(this.level);

		fireMapCreationEvent(new MapCreationEvent(this.world.getMapCopy()));

		this.bulbs.clear();
		for (Vector2i bulb : this.world.getBulbPosition()) {
			Pair<Vector2i> p = new Pair<>(bulb);
			this.bulbs.add(p);
			fireStaticEntityCreationEvent(new StaticEntityCreationEvent(p.getId(), StaticEntityType.LIT_BULB, p.object));
		}

		Random random = this.seed.getRandomizer(this.level);

		for (Pair<Player> player : this.players) {
			player.object.setPosition(this.world.getStairsUpPosition());
			fireLivingEntityMoveEvent(new LivingEntityMoveEvent(player.getId(), player.object.getPosition()));
		}

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
					mobPos.copy()));
			this.mobs.add(pair);
			fireLivingEntityCreationEvent(new LivingEntityCreationEvent(pair.getId(), this.mobsTypes[selectedMob], mobPos.copy(), null));
		}

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
					fireStaticEntityCreationEvent(new StaticEntityCreationEvent(pair.getId(), armor.getGtype(), pos));
					break;
				case 1:
					selection = random.nextInt(this.lootsWeaponRange.length);
					Weapon weapon = new Weapon(this.lootsWeaponBaseAttack[selection] + this.lootsWeaponAttackPerLevel[selection] * chestLevel,
							this.lootsWeaponRange[selection],
							this.lootsWeaponManaCost[selection] + this.lootsWeaponManaCostPerLevel[selection] * chestLevel);
					pair = new Pair<>(new InteractiveObject(pos, weapon));
					this.interactiveObjects.add(pair);
					fireStaticEntityCreationEvent(new StaticEntityCreationEvent(pair.getId(), weapon.getGtype(), pos));
					break;
				case 2:
					selection = random.nextInt(this.lootsScrollTurns.length);
					Scroll scroll = new Scroll(this.lootsScrollTurns[selection],
							this.lootsScrollBaseDamagePerTurn[selection] + chestLevel * this.lootsScrollDamagePerTurnPerLevel[selection],
							this.lootsScrollBaseDamageModPerTurn[selection] + chestLevel * this.lootsScrollDamageModPerTurnPerLevel[selection]);
					pair = new Pair<>(new InteractiveObject(pos, scroll));
					this.interactiveObjects.add(pair);
					fireStaticEntityCreationEvent(new StaticEntityCreationEvent(pair.getId(), scroll.getGtype(), pos));
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
		for (int i = 0; i < trapsNbr; i++) {
			selection = random.nextInt(this.trapsBaseHP.length);
			pos = selectRandomGroundPosition(rooms, random);
			Pair<InteractiveObject> pair = new Pair<>(new InteractiveObject(pos,
					this.trapsBaseHP[selection] + this.trapsHPPerLevel[selection] * random.nextInt(3) + this.level - 1,
					this.trapsBaseMana[selection] + this.trapsManaPerLevel[selection] * random.nextInt(3) + this.level - 1));
			this.interactiveObjects.add(pair);
			fireStaticEntityCreationEvent(new StaticEntityCreationEvent(pair.getId(), StaticEntityType.TRAP, pos));
		}
	}

	private Vector2i selectRandomGroundPosition(ArrayList<Map.Room> rooms, Random random) {
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

	private Player player() {
		return this.players.get(this.playerTurn).object;
	}

	private void nextTurn() {
		int count = 0;
		do {
			this.playerTurn = (this.playerTurn + 1) % this.players.size();
			++count;
		} while (count != this.players.size() && this.player().getFloor() != this.level);

		if (count == this.players.size()) {
			++this.level;
			this.generateLevel();
			this.save();
		} else if (this.playerTurn == 0) {
			this.nextTick();
		}

		if (this.player().isARequestPending()) {
			this.nextTurn();
		}
		//TODO ********************************************************************************************************************************************************************
		//fireNextTurnEvent(new event)
	}

	private void nextTick() {
		Player player;
		for (Pair<Player> playerPair : this.players) {
			player = playerPair.object;
			//player.live();
			/* TODO ****************************************************************************************************************************************************************/
			// + requests
		}

		Mob mob;
		for (Pair<Mob> mobPair : this.mobs) {
			mob = mobPair.object;
			//mob.live();
			/* TODO **********************************************************************************************************************************************************************/
		}
	}

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
