//////////////////////////////////////////////////////////////////////////////////
//                                                                              //
//     This Source Code Form is subject to the terms of the Mozilla Public      //
//     License, v. 2.0. If a copy of the MPL was not distributed with this      //
//     file, You can obtain one at http://mozilla.org/MPL/2.0/.                 //
//                                                                              //
//////////////////////////////////////////////////////////////////////////////////

package com.github.tiwindetea.dungeonoflegend.model;

import com.github.tiwindetea.dungeonoflegend.events.living_entities.LivingEntityHealthUpdateEvent;
import com.github.tiwindetea.dungeonoflegend.events.players.PlayerCreationEvent;
import com.github.tiwindetea.dungeonoflegend.events.players.PlayerStatEvent;
import com.github.tiwindetea.dungeonoflegend.events.players.inventory.InventoryAdditionEvent;
import com.github.tiwindetea.dungeonoflegend.events.players.inventory.InventoryDeletionEvent;
import com.github.tiwindetea.dungeonoflegend.listeners.game.GameListener;
import com.github.tiwindetea.dungeonoflegend.view.entities.LivingEntityType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Stack;

import static com.github.tiwindetea.dungeonoflegend.model.ArmorType.BOOTS;
import static com.github.tiwindetea.dungeonoflegend.model.ArmorType.BREAST_PLATE;
import static com.github.tiwindetea.dungeonoflegend.model.ArmorType.GLOVES;
import static com.github.tiwindetea.dungeonoflegend.model.ArmorType.HELMET;
import static com.github.tiwindetea.dungeonoflegend.model.ArmorType.PANTS;

/**
 * Player
 *
 * @author Lucas LAZARE
 */
public class Player extends LivingThing {
	private List<Pair<StorableObject>> inventory;
	private static ArmorType[] equipedArmor = {HELMET, BREAST_PLATE, GLOVES, PANTS, BOOTS}; // Order of the armors in the player's amors' array
	private int maxStorageCapacity;
	private double maxMana;
	private double mana;
	private List<Pair<Armor>> armors; // armor of the player (equiped)
	private Pair<Weapon> weapon; // equiped armor
	private double hitPointsPerLevel;
	private double manaPerLevel;
	private double attackPowerPerLevel;
	private double defensePowerPerLevel;
	private Stack<Vector2i> requestedPath = new Stack<>();
	private Pair<StorableObject> objectToDrop;
	private int floor;
	private int xp;
	private int requiredXp;
	private int requiredXpPerLevel;
	private int los;
	private int number;
	private int exploreLOS;
	private Vector2i requestedInteraction;
	private Vector2i dropPos;
	public boolean hasFallen = false;
	private boolean sawDuck = false;
	private int squaredLOS;
	private static final LivingEntityType[] ENUM_VAL = Arrays.copyOfRange(LivingEntityType.values(),
			LivingEntityType.PLAYER1.ordinal(),
			LivingEntityType.values().length
	);

	/**
	 * Instantiates a new Player.
	 *
	 * @param name                 his name
	 * @param number               his number
	 * @param los                  his los
	 * @param exploreLOS           his explore los
	 * @param level                his level
	 * @param maxStorageCapacity   his max storage capacity
	 * @param baseHealth           his base health
	 * @param baseMana             his base mana
	 * @param baseAttack           his base attack
	 * @param baseDef              his base def
	 * @param healthPerLevel       his health per level
	 * @param manaPerLevel         his mana per level
	 * @param attackPowerPerLevel  his attack power per level
	 * @param defensePowerPerLevel his defense power per level
	 */
	public Player(String name, int number, int los, int exploreLOS, int level, int baseRequiredXp, int requiredXpPerLevel,
				  int floor, int maxStorageCapacity, double baseHealth, double baseMana, double baseAttack, double baseDef,
				  double healthPerLevel, double manaPerLevel, double attackPowerPerLevel, double defensePowerPerLevel) {
		super();
		this.number = number;
		this.inventory = new ArrayList<>();
		this.armors = new ArrayList<>(5);
		for (int i = 0; i < 5; i++) {
			this.armors.add(new Pair<>());
		}
		this.weapon = null;
		this.name = name;
		this.level = level;
		this.maxStorageCapacity = maxStorageCapacity;
		this.hitPoints = baseHealth + healthPerLevel * level;
		this.maxHitPoints = this.hitPoints;
		this.mana = baseMana + manaPerLevel * level;
		this.maxMana = this.mana;
		this.attackPower = baseAttack + attackPowerPerLevel * level;
		this.defensePower = baseDef + defensePowerPerLevel * level;
		this.hitPointsPerLevel = healthPerLevel;
		this.manaPerLevel = manaPerLevel;
		this.attackPowerPerLevel = attackPowerPerLevel;
		this.defensePowerPerLevel = defensePowerPerLevel;
		this.floor = floor;
		this.xp = 0;
		this.requiredXp = baseRequiredXp;
		this.requiredXpPerLevel = requiredXpPerLevel;
		this.los = los;
		this.squaredLOS = los * los;
		this.exploreLOS = exploreLOS;
		this.position = new Vector2i(-1, -1);
	}

	private Player() {
		super();
		this.inventory = new ArrayList<>();
		this.armors = new ArrayList<>(5);
		for (int i = 0; i < 5; i++) {
			this.armors.add(new Pair<>());
		}
		this.position = new Vector2i(-1, -1);
	}

	private void fireInventoryAdditionEvent(InventoryAdditionEvent event) {
		for (GameListener listener : this.getPlayersListeners()) {
			listener.addInventory(event);
		}
	}

	private void fireInventoryDeletionEvent(InventoryDeletionEvent event) {
		for (GameListener listener : this.getPlayersListeners()) {
			listener.deleteInventory(event);
		}
	}

	private static void firePlayerStatEvent(PlayerStatEvent event) {
		for (GameListener listener : getPlayersListeners()) {
			listener.changePlayerStat(event);
		}
	}

	private static void firePlayerCreationEvent(PlayerCreationEvent playerCreationEvent) {
		for (GameListener listener : getPlayersListeners()) {
			listener.createPlayer(playerCreationEvent);
		}
	}

	/**
	 * Gets the id of the object to drop (request).
	 *
	 * @return the id of the object to drop
	 */
	public Pair<StorableObject> getObjectToDrop() {
		return this.objectToDrop;
	}

	/**
	 * Sets the id of the object to drop (request for next turn).
	 *
	 * @param objectToDropId the id of the object to drop
	 */
	public void setObjectToDrop(long objectToDropId, Vector2i dropPos) {
		int i = this.inventory.indexOf(new Pair<>(objectToDropId));
		if (i >= 0) {
			this.objectToDrop = this.inventory.get(i);
			this.dropPos = dropPos;
		} else {
			this.objectToDrop = null;
		}
	}

	/**
	 * Sets the requested path (request for next turn).
	 *
	 * @param requestedPath the requested path
	 */
	public void setRequestedPath(Stack<Vector2i> requestedPath) {
		this.doNothing();
		if (requestedPath == null) {
			this.requestedPath.clear();
		} else {
			this.requestedPath = requestedPath;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Vector2i getRequestedMove() {
		return this.requestedPath.size() > 0 ? this.requestedPath.peek() : null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setPosition(Vector2i position) {
		super.setPosition(position);
		if (this.requestedPath.size() > 0 && this.position.equals(this.requestedPath.peek())) {
			this.requestedPath.pop();
		}
	}

	/**
	 * Method to call when a move request was denied
	 */
	public void moveRequestDenied() {
		this.requestedPath.clear();
	}

	/**
	 * Gets the current floor.
	 *
	 * @return the current floor
	 */
	public int getFloor() {
		return this.floor;
	}

	/**
	 * Sets the floor.
	 *
	 * @param floor the floor
	 */
	public void setFloor(int floor) {
		this.floor = floor;
	}

	/**
	 * Gets the mana.
	 *
	 * @return the mana
	 */
	public double getMana() {
		return this.mana;
	}

	/**
	 * Gets max mana.
	 *
	 * @return the max mana
	 */
	public double getMaxMana() {
		return this.maxMana;
	}

	/**
	 * Gets the name.
	 *
	 * @return the name
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Get the equiped armors.
	 *
	 * @return the equiped armors
	 */
	public Pair<Armor>[] getEquipedArmor() {
		return (Pair<Armor>[]) this.armors.toArray();
	}

	/**
	 * Gets the equiped weapon.
	 *
	 * @return the equiped weapon
	 */
	public Pair<Weapon> getEquipedWeapon() {
		return this.weapon;
	}

	/**
	 * Gets the explore los (fogged, on the map).
	 *
	 * @return the explore los
	 */
	public int getExploreLOS() {
		return this.exploreLOS;
	}

	/**
	 * Gets the los.
	 *
	 * @return the los
	 */
	public int getLos() {
		return this.los;
	}


	/**
	 * Gets the xp required to go to the next level.
	 *
	 * @return the xp required to go to the next level
	 */
	public int getMaxXp() {
		return this.requiredXp;
	}

	/**
	 * Sets the requested attack.
	 *
	 * @param requestedAttack the requested attack
	 */
	public void setRequestedAttack(Vector2i requestedAttack) {
		if (requestedAttack == null) {
			this.requestedAttack = null;
		} else {
			this.requestedAttack = requestedAttack.copy();
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void attack(LivingThing target) {
		if (target.getType() == LivingThingType.MOB) {
			double damages = this.attackPower;
			for (Pair<Armor> armorPair : this.armors) {
				if (armorPair != null && armorPair.object != null)
					damages += armorPair.object.getAttackPowerModifier();
			}
			if (this.weapon != null && this.weapon.object != null && useMana(this.weapon.object.getManaCost())) {
				damages += this.weapon.object.getAttackPowerModifier();
			}
			target.damage(damages);
		}
	}

	/**
	 * Gets the number of the player (player 0, player 1, …).
	 *
	 * @return the number of the player
	 */
	public int getNumber() {
		return this.number;
	}

	/**
	 * Sets the requested interaction.
	 *
	 * @param position the position of the interaction
	 */
	public void setRequestedInteraction(Vector2i position) {
		this.requestedInteraction = position;
	}

	/**
	 * Gets the requested interaction.
	 *
	 * @return the requested interaction
	 */
	public Vector2i getRequestedInteraction() {
		if (this.requestedInteraction == null) {
			return null;
		}
		return this.requestedInteraction.copy();
	}

	/**
	 * Gets the inventory.
	 *
	 * @return the inventory.
	 */
	public List<Pair<StorableObject>> getInventory() {
		return this.inventory;
	}

	/**
	 * Add to inventory.
	 *
	 * @param storable a storable item
	 * @return True if the object was added, false otherwise
	 */
	public boolean addToInventory(Pair<StorableObject> storable) {
		if (this.inventory.size() < this.maxStorageCapacity) {
			this.inventory.add(storable);
			fireInventoryAdditionEvent(new InventoryAdditionEvent(
					this.number,
					storable.getId(),
					false,
					storable.object.getGType(),
					storable.object.getDescription()
			));
			return true;
		}
		return false;
	}

	/**
	 * Gets the attack range.
	 *
	 * @return the attack range
	 */
	public int getAttackRange() {
		if (this.weapon != null && this.weapon.object != null && this.weapon.object.getManaCost() <= this.mana) {
			return this.weapon.object.getRange();
		} else {
			return 1;
		}
	}

	/**
	 * Method to call when the drop request was solved successfully
	 *
	 * @return the dropped intem
	 */
	public Pair<StorableObject> dropRequestAccepted() {
		if (this.objectToDrop != null && this.inventory.remove(this.objectToDrop)) {
			fireInventoryDeletionEvent(new InventoryDeletionEvent(this.number, this.objectToDrop.getId()));
			Pair<StorableObject> tmp = this.objectToDrop;
			this.inventory.remove(this.objectToDrop);
			this.objectToDrop = null;
			return tmp;
		}
		return null;
	}

	public Vector2i getDropPos(){
		return this.dropPos.copy();
	}

	/**
	 * Diminishes the quantity of mana disponible.
	 *
	 * @param consumption the mana consumption
	 * @return true if the mana was consumed, false otherwise (ie : not enough mana)
	 */
	public boolean useMana(double consumption) {
		if (this.mana >= consumption) {
			this.mana -= consumption;
			fireStatEvent(new PlayerStatEvent(this.number, PlayerStatEvent.StatType.MANA, PlayerStatEvent.ValueType.ACTUAL, (int) this.mana));
			return true;
		}
		return false;
	}

	/**
	 * Equips with a new armor and adds the previously equiped armor to the inventory
	 *
	 * @param armor the armor to equip
	 * @return The previously equipped armor if there was one, null otherwise.
	 */
	public Pair<Armor> equipWithArmor(Pair<Armor> armor) {
		Pair<Armor> removedArmor = null;
		for (int i = 0; i < equipedArmor.length; i++) {
			if (equipedArmor[i] == armor.object.getArmorType()) {
				if (this.armors.get(i) != null && this.armors.get(i).object != null) {
					removedArmor = this.armors.get(i);
					fireInventoryDeletionEvent(new InventoryDeletionEvent(this.number, removedArmor.getId()));
					addToInventory(new Pair<>(removedArmor));
				}
				fireInventoryAdditionEvent(new InventoryAdditionEvent(
						this.number,
						armor.getId(),
						true,
						armor.object.getGType(),
						armor.object.getDescription()
				));
				this.armors.set(i, armor);
				return removedArmor;
			}
		}
		return null;
	}


	/**
	 * Equips with a new weapon and adds the previously equiped weapon to the inventory
	 *
	 * @param weapon the armor to equip
	 * @return The previously equipped weapon if there was one, null otherwise.
	 */
	public Pair<Weapon> equipWithWeapon(Pair<Weapon> weapon) {
		Pair<Weapon> removedWeapon = this.weapon;
		this.weapon = weapon;
		if (removedWeapon != null) {
			fireInventoryDeletionEvent(new InventoryDeletionEvent(this.number, removedWeapon.getId()));
			this.addToInventory(new Pair<>(removedWeapon));
		}
		fireInventoryAdditionEvent(new InventoryAdditionEvent(
				this.number,
				weapon.getId(),
				true,
				weapon.object.getGType(),
				weapon.object.getDescription()
		));
		return removedWeapon;
	}

	/**
	 * Add mana.
	 *
	 * @param mana the mana
	 */
	public void addMana(double mana) {
		if (mana < 0)
			throw new IllegalArgumentException("mana must be positive");
		this.mana = Math.min(this.maxMana, mana + this.mana);
		this.fireStatEvent(new PlayerStatEvent(this.number, PlayerStatEvent.StatType.MANA,
				PlayerStatEvent.ValueType.ACTUAL, (int) this.mana));
	}

	/**
	 * Heal.
	 *
	 * @param hp the hp
	 */
	public void heal(double hp) {
		if (hp < 0)
			throw new IllegalArgumentException("hp must be positive");
		this.hitPoints = Math.min(this.maxHitPoints, hp + this.hitPoints);
		this.fireStatEvent(new PlayerStatEvent(this.number, PlayerStatEvent.StatType.HEALTH,
				PlayerStatEvent.ValueType.ACTUAL, (int) this.hitPoints));
		super.fireHealthUpdate(new LivingEntityHealthUpdateEvent(this.id, this.hitPoints / this.maxHitPoints));
	}

	/**
	 * Increases the attack.
	 *
	 * @param ad the attack upgrade (or downgrade)
	 */
	public void increaseAttack(double ad) {
		this.attackPower += ad;
	}

	/**
	 * Increases the maxhp.
	 *
	 * @param hp the hp upgrade (or downgrade)
	 */
	public void increaseHP(double hp) {
		this.maxHitPoints = Math.max(hp + this.maxHitPoints, 1);
		this.hitPoints = Math.max(hp + this.hitPoints, 1);
		this.fireStatEvent(new PlayerStatEvent(this.number, PlayerStatEvent.StatType.HEALTH,
				PlayerStatEvent.ValueType.MAX, (int) this.maxHitPoints));
	}

	/**
	 * Increase the defense.
	 *
	 * @param def the defense upgrade (or downgrade)
	 */
	public void increaseDefense(double def) {
		this.defensePower += def;
	}

	/**
	 * Increase the maxmana.
	 *
	 * @param manaModifier the mana upgrade (or downgrade)
	 */
	public void increaseMana(double manaModifier) {
		this.maxMana = Math.max(this.maxMana + manaModifier, 1);
		this.mana = Math.max(this.mana + manaModifier, 1);
		this.fireStatEvent(new PlayerStatEvent(this.number, PlayerStatEvent.StatType.MANA,
				PlayerStatEvent.ValueType.MAX, (int) this.maxMana));
	}

	/**
	 * Parses a player.
	 *
	 * @param str a player's String
	 * @return the player
	 */
	public static Player parsePlayer(String str) {
		if (!str.substring(0, 7).equals("player=")) {
			throw new IllegalArgumentException("Invoking Player.parsePlayer with input string: \"" + str + "\"");
		}

		/* Computing stats values' indexes */
		int name = str.indexOf("name=") + 5;
		int nbr = str.indexOf("nbr=", name) + 4;
		int los = str.indexOf("los=", nbr) + 4;
		int elos = str.indexOf("elos=", los) + 5;
		int hasFallen = str.indexOf("hasFallen=", elos) + 10;
		int capa = str.indexOf("capacity=", hasFallen) + 9;
		int mhp = str.indexOf("maxHitPoints=", capa) + 13;
		int mm = str.indexOf("maxMana=", mhp) + 8;
		int hp = str.indexOf("hitPoints=", mm) + 10;
		int mana = str.indexOf("mana=", hp) + 5;
		int hppl = str.indexOf("hitPointsPerLevel=", mana) + 18;
		int mpl = str.indexOf("manaPerLevel=", hppl) + 13;
		int level = str.indexOf("level=", mpl) + 6;
		int xp = str.indexOf("xp=", level) + 3;
		int rqxp = str.indexOf("requiredXp=", xp) + 11;
		int rqxppl = str.indexOf("requiredXpPerLevel", rqxp) + 19;
		int ap = str.indexOf("attackPower=", rqxppl) + 12;
		int dp = str.indexOf("defensePower=", ap) + 13;
		int appl = str.indexOf("attackPowerPerLevel=", dp) + 20;
		int adpl = str.indexOf("defensePowerPerLevel=", appl) + 21;

		/* Parsing the stats values */
		Player p = new Player();
		p.name = str.substring(name, str.indexOf(',', name));
		p.number = Integer.parseInt(str.substring(nbr, str.indexOf(',', nbr)));
		p.los = Integer.parseInt(str.substring(los, str.indexOf(',', los)));
		p.exploreLOS = Integer.parseInt(str.substring(elos, str.indexOf(',', elos)));
		p.hasFallen = Boolean.parseBoolean(str.substring(hasFallen, str.indexOf(',', hasFallen)));
		p.maxStorageCapacity = Integer.parseInt(str.substring(capa, str.indexOf(',', capa)));
		p.maxHitPoints = Double.parseDouble(str.substring(mhp, str.indexOf(',', mhp)));
		p.maxMana = Double.parseDouble(str.substring(mm, str.indexOf(',', mm)));
		p.hitPoints = Double.parseDouble(str.substring(hp, str.indexOf(',', hp)));
		p.mana = Double.parseDouble(str.substring(mana, str.indexOf(',', mana)));
		p.hitPointsPerLevel = Double.parseDouble(str.substring(hppl, str.indexOf(',', hppl)));
		p.manaPerLevel = Double.parseDouble(str.substring(mpl, str.indexOf(',', mpl)));
		p.level = Integer.parseInt(str.substring(level, str.indexOf(',', level)));
		p.xp = Integer.parseInt(str.substring(xp, str.indexOf(',', xp)));
		p.requiredXp = Integer.parseInt(str.substring(rqxp, str.indexOf(",", rqxp)));
		p.requiredXpPerLevel = Integer.parseInt(str.substring(rqxppl, str.indexOf(",", rqxppl)));
		p.attackPower = Double.parseDouble(str.substring(ap, str.indexOf(',', ap)));
		p.defensePower = Double.parseDouble(str.substring(dp, str.indexOf(',', dp)));
		p.attackPowerPerLevel = Double.parseDouble(str.substring(appl, str.indexOf(',', appl)));
		p.defensePowerPerLevel = Double.parseDouble(str.substring(adpl, str.indexOf(',', adpl)));


		firePlayerCreationEvent(new PlayerCreationEvent(
				p.number,
				p.id,
				new Vector2i(0, 0),
				Direction.DOWN,
				(int) p.maxHitPoints,
				(int) p.maxMana,
				p.requiredXp,
				p.getDescription()
		));

		firePlayerStatEvent(new PlayerStatEvent(p.number, PlayerStatEvent.StatType.XP, PlayerStatEvent.ValueType.ACTUAL, p.xp));


		/* parsing the equipement */
		String weapon = str.substring(str.indexOf("weapon={"));
		weapon = weapon.substring(0, weapon.indexOf('}') + 1);
		Weapon weaponParsed = Weapon.parseWeapon(weapon);
		if (weaponParsed != null) {
			p.equipWithWeapon(new Pair<>(weaponParsed));
		}

		ArmorType[] armorTypes = {
				BREAST_PLATE,
				GLOVES,
				HELMET,
				BOOTS,
				PANTS
		};

		int armorIndex = 0;
		for (int i = 0; i < armorTypes.length; i++) {
			armorIndex = str.indexOf("armor={", armorIndex + 1);
			String armor = str.substring(armorIndex, str.indexOf('}', armorIndex) + 1);
			Armor armorParsed = Armor.parseArmor(armor);
			if (armorParsed != null) {
				p.equipWithArmor(new Pair<>(Armor.parseArmor(armor)));
			}
		}

		/* Parsing for the inventory */
		str = "," + str.substring(str.indexOf("inventory=") + 11);
		String item;
		while (!str.equals(",},}")) {
			item = str.substring(str.indexOf(',') + 1, str.indexOf('}') + 1);
			str = str.substring(str.indexOf("}") + 1);
			switch (item.substring(0, item.indexOf('='))) {
				case "weapon":
					p.addToInventory(new Pair<>(Weapon.parseWeapon(item)));
					break;
				case "armor":
					p.addToInventory(new Pair<>(Armor.parseArmor(item)));
					break;
				case "pot":
					p.addToInventory(new Pair<>(Pot.parsePot(item)));
					break;
				case "scroll":
					p.addToInventory(new Pair<>(Scroll.parseScroll(item)));
					break;
				default:
					System.out.println("Unexpected item when parsing the inventory of " + p.name);
					System.out.println("(got: " + item + ")");
					break;
			}
		}

		return p;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		String ans = "player={name=" + this.name
				+ ",nbr=" + this.number
				+ ",los=" + this.los
				+ ",elos=" + this.exploreLOS
				+ ",hasFallen=" + this.hasFallen
				+ ",capacity=" + this.maxStorageCapacity
				+ ",maxHitPoints=" + Math.round(100 * this.maxHitPoints) / 100
				+ ",maxMana=" + Math.round(100 * this.maxMana) / 100
				+ ",hitPoints=" + Math.round(100 * this.hitPoints) / 100
				+ ",mana=" + Math.round(100 * this.mana) / 100
				+ ",hitPointsPerLevel=" + this.hitPointsPerLevel
				+ ",manaPerLevel=" + this.manaPerLevel
				+ ",level=" + this.level
				+ ",xp=" + this.xp
				+ ",requiredXp=" + this.requiredXp
				+ ",requiredXpPerLevel=" + this.requiredXpPerLevel
				+ ",attackPower=" + Math.round(100 * this.attackPower) / 100
				+ ",defensePower=" + Math.round(100 * this.defensePower) / 100
				+ ",attackPowerPerLevel=" + this.attackPowerPerLevel
				+ ",defensePowerPerLevel=" + this.defensePowerPerLevel;

		if (this.weapon == null) {
			ans += ",weapon={null}";
		} else {
			ans += "," + this.weapon.object;
		}
		for (Pair<Armor> anArmor : this.armors) {
			ans += ",";
			if (anArmor.object == null) {
				ans += "armor={null}";
			} else {
				ans += anArmor.object;
			}
		}
		ans += ",inventory={";
		for (Pair<StorableObject> storableObjectPair : this.inventory) {
			ans += storableObjectPair.object + ",";
		}
		return ans.substring(0, ans.length() - 1) + ",},}";
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void live(List<Mob> mobs, Collection<Player> players, boolean[][] los) {
		boolean localSawDuck = false;
		for (int i = 0; i < mobs.size() && !localSawDuck; ++i) {
			Vector2i pos = mobs.get(i).getPosition();
			if (pos.squaredDistance(this.position) <= this.squaredLOS) {
				if (los[los.length / 2 - this.position.x + pos.x][los[0].length / 2 - this.position.y + pos.y]) {
					localSawDuck = true;
				}
			}
		}
		if (localSawDuck && !this.sawDuck) {
			this.requestedPath.clear();
		}
		this.sawDuck = localSawDuck;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public LivingThingType getType() {
		return LivingThingType.PLAYER;
	}

	/**
	 * @return true if a riquest is pending, false otherwise.
	 */
	public boolean isARequestPending() {
		return this.requestedPath.size() > 0 || this.objectToDrop != null || this.requestedInteraction != null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void damage(double damages) {
		super.damage(damages);
		fireStatEvent(new PlayerStatEvent(this.number, PlayerStatEvent.StatType.HEALTH, PlayerStatEvent.ValueType.ACTUAL, (int) this.hitPoints));
	}

	/**
	 * Gets the graphical type.
	 *
	 * @return the graphical type
	 */
	public LivingEntityType getGType() {
		return ENUM_VAL[this.number];
	}

	/**
	 * Adds xp to the player
	 *
	 * @param earnedXp Earned xp
	 */
	public void xp(int earnedXp) {
		this.xp += earnedXp;
		if (this.xp >= this.requiredXp) {
			int levelUpping = this.xp / this.requiredXp;
			this.xp %= this.requiredXp;
			this.requiredXp += this.requiredXpPerLevel * levelUpping;
			this.level += levelUpping;
			this.maxHitPoints += this.hitPointsPerLevel * levelUpping;
			this.maxMana += this.manaPerLevel * levelUpping;
			this.attackPower += this.attackPowerPerLevel * levelUpping;
			this.defensePower += this.defensePowerPerLevel * levelUpping;
			this.hitPoints = this.maxHitPoints;
			this.mana = this.maxMana;
			fireStatEvent(new PlayerStatEvent(this.number, PlayerStatEvent.StatType.HEALTH, PlayerStatEvent.ValueType.MAX, (int) this.maxHitPoints));
			fireStatEvent(new PlayerStatEvent(this.number, PlayerStatEvent.StatType.HEALTH, PlayerStatEvent.ValueType.ACTUAL, (int) this.hitPoints));
			fireStatEvent(new PlayerStatEvent(this.number, PlayerStatEvent.StatType.MANA, PlayerStatEvent.ValueType.MAX, (int) this.maxMana));
			fireStatEvent(new PlayerStatEvent(this.number, PlayerStatEvent.StatType.MANA, PlayerStatEvent.ValueType.ACTUAL, (int) this.mana));
			fireStatEvent(new PlayerStatEvent(this.number, PlayerStatEvent.StatType.XP, PlayerStatEvent.ValueType.MAX, this.requiredXp));
			super.fireHealthUpdate(new LivingEntityHealthUpdateEvent(this.id, this.hitPoints / this.maxHitPoints));
		}
		fireStatEvent(new PlayerStatEvent(this.number, PlayerStatEvent.StatType.XP, PlayerStatEvent.ValueType.ACTUAL, this.xp));
	}

	/**
	 * Removes an object from the inventory.
	 *
	 * @param storableObject The object to remove
	 */
	public void removeFromInventory(Pair<StorableObject> storableObject) {
		if (this.inventory.remove(storableObject)) {
			fireInventoryDeletionEvent(new InventoryDeletionEvent(this.number, storableObject.getId()));
		}
	}


	/**
	 * Unequips an weapon // armor.
	 *
	 * @param id the id of the equipement
	 */
	public void unequip(long id) {
		if (this.inventory.size() < this.maxStorageCapacity - 1) {
			if (this.weapon != null && this.weapon.getId() == id) {
				fireInventoryDeletionEvent(new InventoryDeletionEvent(this.number, id));
				addToInventory(new Pair<>(this.weapon.getId(), this.weapon.object));
				this.weapon = null;
			} else {
				for (int i = 0; i < this.armors.size(); i++) {
					if (this.armors.get(i) != null && this.armors.get(i).getId() == id) {
						fireInventoryDeletionEvent(new InventoryDeletionEvent(this.number, id));
						addToInventory(new Pair<>(this.armors.get(i).getId(), this.armors.get(i).object));
						this.armors.set(i, null);
					}
				}
			}
		}
	}

	public void doNothing() {
		this.requestedAttack = null;
		this.requestedInteraction = null;
		this.requestedPath.clear();
	}

	public static int parsePlayerNumber(String str) {
		int nbr = str.indexOf("nbr=", 17) + 4;
		return Integer.parseInt(str.substring(nbr, str.indexOf(',', nbr)));
	}
}
