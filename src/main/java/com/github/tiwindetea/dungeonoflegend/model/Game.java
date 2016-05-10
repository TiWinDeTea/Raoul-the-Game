package com.github.tiwindetea.dungeonoflegend.model;

import com.github.tiwindetea.dungeonoflegend.events.living_entities.*;
import com.github.tiwindetea.dungeonoflegend.events.map.MapCreationEvent;
import com.github.tiwindetea.dungeonoflegend.events.players.PlayerCreationEvent;
import com.github.tiwindetea.dungeonoflegend.events.players.PlayerStatEvent;
import com.github.tiwindetea.dungeonoflegend.events.players.inventory.InventoryAdditionEvent;
import com.github.tiwindetea.dungeonoflegend.events.players.inventory.InventoryDeletionEvent;
import com.github.tiwindetea.dungeonoflegend.events.requests.InteractionRequestEvent;
import com.github.tiwindetea.dungeonoflegend.events.requests.inventory.DropRequestEvent;
import com.github.tiwindetea.dungeonoflegend.events.requests.inventory.UsageRequestEvent;
import com.github.tiwindetea.dungeonoflegend.events.requests.moves.ComplexMoveRequestEvent;
import com.github.tiwindetea.dungeonoflegend.events.requests.moves.SimpleMoveRequestEvent;
import com.github.tiwindetea.dungeonoflegend.events.static_entities.StaticEntityCreationEvent;
import com.github.tiwindetea.dungeonoflegend.events.static_entities.StaticEntityDeletionEvent;
import com.github.tiwindetea.dungeonoflegend.events.static_entities.StaticEntityLOSDefinitionEvent;
import com.github.tiwindetea.dungeonoflegend.listeners.game.GameListener;
import com.github.tiwindetea.dungeonoflegend.listeners.request.RequestListener;
import com.github.tiwindetea.dungeonoflegend.view.entities.LivingEntityType;
import com.github.tiwindetea.dungeonoflegend.view.entities.StaticEntityType;

import java.util.*;
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
	private ArrayList<Pair<Player>> players;
	private ArrayList<Pair<Vector2i>> bulbs = new ArrayList<>();
	private Seed seed;
	private final List<GameListener> listeners = new ArrayList<>();
	private LivingEntityType[] mobsTypes;
	private int[] mobsBaseHP;
	private int[] mobsBaseDef;
	private int[] mobsBaseAttack;
	private int[] mobsHPPerLevel;
	private int[] mobsDefPerLevel;
	private int[] mobsAttackPerLevel;

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

	private int playerTurn;

	String gameName;

	public Game(String gameName) {
		this.gameName = gameName;
	}

	public void init(Collection<Player> players) {
		this.init(players, new Seed(), 0);
	}

	public void init(Collection<Player> players, Seed seed, int level) {
		this.playerTurn = 0;
		this.level = level;
		this.seed = seed;
		this.world = new Map(this.seed);

		this.world.generateLevel(level);
		this.loadMobs();
		this.loadChests();
		this.loadTraps();
		this.players = new ArrayList<>(players.size());
		this.players.addAll(players.stream().map(Pair::new).collect(Collectors.toList()));
	}

	private void loadMobs() {
		ResourceBundle mobs = ResourceBundle.getBundle(MainPackage.name + ".Mobs", Locale.getDefault());
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
	public void requestComplexMove(ComplexMoveRequestEvent e) {
		Stack<Vector2i> path = this.world.getPath(this.players.get(this.playerTurn).object.getPosition(), e.moveArrival, false, null);
		if (path != null) {
			this.players.get(this.playerTurn).object.setPath(path);
			this.nextTurn();
		}
	}

	@Override
	public void requestDrop(DropRequestEvent e) {
		StorableObject object = this.players.get(this.playerTurn).object.removeFromInventory(e.objectId);
		/* TODO **********************************************************************************************************************************************************************
		if (object != null) {
			fireStaticEntityCreationEvent(new StaticEntityCreationEvent());
		}*/
	}

	@Override
	public void requestInteraction(InteractionRequestEvent e) {
		this.players.get(this.playerTurn).object.setInteractionRequested();
		this.nextTurn();
	}

	@Override
	public void requestSimpleMove(SimpleMoveRequestEvent e) {
		if(e == null) {
			return;
		}
		Stack<Vector2i> path = new Stack<>();
		path.add(this.players.get(this.playerTurn).object.getPosition().copy().add(e.moveDirection));
		this.players.get(this.playerTurn).object.setPath(path);
		this.nextTurn();
	}

	@Override
	public void requestUsage(UsageRequestEvent e) {
		if(e == null) {
			return;
		}
		//TODO
	}

	public void generateLevel() {
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

	private void nextTurn() {
		int count = 0;
		do {
			this.playerTurn = (this.playerTurn + 1) % this.players.size();
			++count;
		} while (count != this.players.size() && this.players.get(this.playerTurn).object.getFloor() != this.level);

		if (count == this.players.size()) {
			++this.level;
			this.generateLevel();
			this.save();
		} else if (this.playerTurn == 0) {
			this.nextTick();
		}
	}

	private void nextTick() {
		Player player;
		for (Pair<Player> playerPair : this.players) {
			player = playerPair.object;
			/* TODO ****************************************************************************************************************************************************************/
		}

		Mob mob;
		for (Pair<Mob> mobPair : this.mobs) {
			mob = mobPair.object;
			/* TODO **********************************************************************************************************************************************************************/
		}
	}

	public void launch(byte numberOfPlayers) {
		//TODO
	}

	public void loadSave() {
		// TODO
	}

	private void save() {
		//TODO
	}
}
