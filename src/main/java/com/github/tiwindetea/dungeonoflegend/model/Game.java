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
import com.github.tiwindetea.dungeonoflegend.listeners.game.entities.EntityListener;
import com.github.tiwindetea.dungeonoflegend.listeners.request.RequestListener;
import com.github.tiwindetea.dungeonoflegend.view.entities.LivingEntityType;
import com.github.tiwindetea.dungeonoflegend.view.entities.StaticEntityType;

import java.util.*;

/**
 * Created by maxime on 4/24/16.
 */
public class Game implements RequestListener {
	/* Tunning parameters for the entities generation */
	public static final int MIN_MOB_QTT_PER_ROOM = 0;
	public static final int MAX_MOB_QTT_PER_ROOM = 3;
	public static final int MIN_MOB_QTT_PER_LEVEL = 15;
	public static final int MAX_MOB_QTT_PER_LEVEL = 20;
	public static final int MIN_TRAPS_QTT_PER_LEVEL = 2;
	public static final int MAX_TRAPS_QTT_PER_LEVEL = 5;
	public static final int MIN_CHEST_QTT_PER_LEVEL = 1;
	public static final int MAX_CHEST_QTT_PER_LEVEL = 5;


	private int level;
	private Map world;
	private ArrayList<Mob> mobs;
	private ArrayList<InteractiveObject> interactiveObjects;
	private ArrayList<Player> players;
	private Seed seed;
	private final List<EntityListener> listeners = new ArrayList<>();
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

	public Game(int playerNumber, Seed seed, int level) {

		this.mobs = new ArrayList<>();
		this.interactiveObjects = new ArrayList<>();
		this.level = level;
		this.seed = seed;
		this.world = new Map(this.seed);
		this.players = new ArrayList<>(playerNumber);
		this.world.generateLevel(level);
		this.loadMobs();
		this.loadChests();
		this.loadTraps();
	}

	public Game(int playerNumber) {
		this.mobs = new ArrayList<>();
		this.interactiveObjects = new ArrayList<>();
		this.level = 0;
		this.seed = new Seed();
		this.world = new Map(this.seed);
		this.players = new ArrayList<>(playerNumber);
		this.world.generateLevel(this.level);
		this.loadMobs();
		this.loadChests();
		this.loadTraps();
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
			String mobName = new String("mob" + i + ".");
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
	}

	private void loadTraps() {
		ResourceBundle traps = ResourceBundle.getBundle(MainPackage.name + ".Traps", Locale.getDefault());
		int trapsQtt = Integer.parseInt(traps.getString("traps.qtt"));
		this.trapsBaseHP = new int[trapsQtt];
		this.trapsBaseMana = new int[trapsQtt];
		this.trapsHPPerLevel = new int[trapsQtt];
		this.trapsManaPerLevel = new int[trapsQtt];
		for (int i = 0; i < trapsQtt; i++) {
			String trapName = new String("trap" + i + ".");
			this.trapsBaseHP[i] = Integer.parseInt(traps.getString(trapName + "base-hp"));
			this.trapsBaseMana[i] = Integer.parseInt(traps.getString(trapName + "base-mana"));
			this.trapsHPPerLevel[i] = Integer.parseInt(traps.getString(trapName + "hp-per-level"));
			this.trapsManaPerLevel[i] = Integer.parseInt(traps.getString(trapName + "mana-per-level"));
		}
	}

	public void addGameListener(GameListener listener) {
		this.listeners.add(listener);
	}

	public GameListener[] getGameListeners() {
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
		if(e == null) {
			return;
		}
		//TODO
	}

	@Override
	public void requestDrop(DropRequestEvent e) {
		if(e == null) {
			return;
		}
		//TODO
	}

	@Override
	public void requestInteraction(InteractionRequestEvent e) {
		if(e == null) {
			return;
		}
		//TODO
	}

	@Override
	public void requestSimpleMove(SimpleMoveRequestEvent e) {
		if(e == null) {
			return;
		}
		//TODO
	}

	@Override
	public void requestUsage(UsageRequestEvent e) {
		if(e == null) {
			return;
		}
		//TODO
	}

	public void nextLevel() {
		/** TODO : MOVE THE PLAYERS *********************************************************************************************************************************************************************************/
		for (int i = 0; i < this.mobs.size(); ++i) {
			this.fireLivingEntityDeletionEvent(new LivingEntityDeletionEvent(0));
		}
		for (int i = -1; i < this.interactiveObjects.size(); ++i) {
			fireStaticEntityDeletionEvent(new StaticEntityDeletionEvent(0));
		}

		++this.level;
		System.out.println("Entering level " + this.level + " of seed [" + this.seed.getAlphaSeed() + " ; " + this.seed.getBetaSeed() + "]");
		ArrayList<Map.Room> rooms = this.world.generateLevel(this.level);

		fireMapCreationEvent(new MapCreationEvent(this.world.getMapCopy()));
		fireStaticEntityCreationEvent(new StaticEntityCreationEvent(0, StaticEntityType.LIT_BULB, this.world.getBulbPosition()));

		Random random = this.seed.getRandomizer(this.level);
		int mobsNbr = random.nextInt(MAX_MOB_QTT_PER_LEVEL - MIN_MOB_QTT_PER_LEVEL) + MIN_MOB_QTT_PER_LEVEL;
		this.mobs = new ArrayList<>(mobsNbr);
		int selectedMob, mobLevel;
		Map.Room selectedRoom;
		Vector2i mobPos = new Vector2i();
		for (int i = 0; i < mobsNbr; i++) {
			selectedMob = random.nextInt(this.mobsTypes.length);
			selectedRoom = rooms.get(random.nextInt(rooms.size()));
			mobLevel = random.nextInt(3) + this.level - 1;
			if (selectedRoom.top.x != selectedRoom.bottom.x) {
				mobPos.x = random.nextInt(selectedRoom.bottom.x - selectedRoom.top.x) + selectedRoom.top.x;
			} else {
				mobPos.x = selectedRoom.top.x;
			}

			if (selectedRoom.top.y != selectedRoom.bottom.y) {
				mobPos.y = random.nextInt(selectedRoom.bottom.y - selectedRoom.top.y) + selectedRoom.top.y;
			} else {
				mobPos.y = selectedRoom.top.y;
			}
			this.mobs.add(new Mob(mobLevel,
					mobLevel * this.mobsHPPerLevel[selectedMob] + this.mobsBaseHP[selectedMob],
					mobLevel * this.mobsAttackPerLevel[selectedMob] + this.mobsBaseAttack[selectedMob],
					mobLevel * this.mobsDefPerLevel[selectedMob] + this.mobsBaseDef[selectedMob],
					mobPos.copy()
			));
			fireLivingEntityCreationEvent(new LivingEntityCreationEvent(selectedMob, this.mobsTypes[selectedMob], mobPos.copy(), null));
		}

		int chestsNbr = random.nextInt(MAX_CHEST_QTT_PER_LEVEL - MIN_CHEST_QTT_PER_LEVEL) + MIN_CHEST_QTT_PER_LEVEL;
		int trapsNbr = random.nextInt(MAX_TRAPS_QTT_PER_LEVEL - MIN_TRAPS_QTT_PER_LEVEL) + MIN_TRAPS_QTT_PER_LEVEL;
		this.interactiveObjects = new ArrayList<>(chestsNbr + trapsNbr);
		for (int i = 0; i < chestsNbr; i++) {

		}
		for (int i = 0; i < trapsNbr; i++) {

		}
	}

	public void launch(byte numberOfPlayers) {
		//TODO
	}

	private void nextTick() {
		//TODO
	}

	private boolean loadSave() {
		//TODO
		return false;
	}

	private void save() {
		//TODO
	}
}
