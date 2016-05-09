package com.github.tiwindetea.dungeonoflegend.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;
import java.util.UUID;

/**
 * Created by maxime on 4/23/16.
 */
public class Player extends LivingThing {
	private HashMap<UUID, StorableObject> inventory;
	private ArrayList<UUID> inventoryIDs;
	private int maxStorageCapacity;
	private int maxMana;
	private int mana;
	private Armor[] armor;
	private Weapon weapon;
	private String name;
	private int hitPointsPerLevel;
	private int manaPerLevel;
	private int attackPowerPerLevel;
	private int defensePowerPerLevel;
	private Stack<Vector2i> path;
	private Vector2i requestMove;
	private boolean requestInteraction;
	private int floor;

	public Player(String name, int level, int maxStorageCapacity, int baseHealth, int baseMana, int baseAttack, int baseDef, int healthPerLevel,
				  int manaPerLevel, int attackPowerPerLevel, int defensePowerPerLevel, int floor) {
		this.inventory = new HashMap<>();
		this.inventoryIDs = new ArrayList<>();
		this.armor = new Armor[5];
		this.weapon = null;
		this.name = name;
		this.level = level;
		this.maxStorageCapacity = maxStorageCapacity;
		this.hitPoints = baseHealth + healthPerLevel * level;
		this.mana = baseMana + manaPerLevel * level;
		this.attackPower = baseAttack + attackPowerPerLevel * level;
		this.defensePower = baseDef + defensePowerPerLevel * level;
		this.hitPointsPerLevel = healthPerLevel;
		this.manaPerLevel = manaPerLevel;
		this.attackPowerPerLevel = attackPowerPerLevel;
		this.defensePowerPerLevel = defensePowerPerLevel;
		this.path = new Stack<>();
		this.requestMove = null;
		this.requestInteraction = false;
		this.floor = floor;
	}

	public int getFloor() {
		return this.floor;
	}

	public void setFloor(int floor) {
		this.floor = floor;
	}

	public int getMana() {
		return this.mana;
	}

	public UUID addToInventory(StorableObject storable) {
		if (this.inventory.size() < this.maxStorageCapacity) {
			UUID uuid = UUID.randomUUID();
			this.inventory.put(uuid, storable);
			this.inventoryIDs.add(uuid);
			return uuid;
		}
		return null;
	}

	public void addToInventory(UUID uuid, StorableObject storable) {
		if (this.inventory.size() < this.maxStorageCapacity) {
			this.inventory.put(uuid, storable);
			this.inventoryIDs.add(uuid);
		}
	}

	public StorableObject removeFromInventory(int index) {
		return this.removeFromInventory(this.inventoryIDs.get(index));
	}

	public StorableObject removeFromInventory(UUID uuid) {
		StorableObject ans = this.inventory.get(uuid);
		this.inventory.remove(uuid);
		return ans;
	}

	public boolean useMana(int consumption) {
		if(this.mana > consumption) {
			this.mana -= consumption;
			return true;
		}
		return false;
	}

	public void equipWith(Armor armor) {
		//TODO
	}

	public void equipWith(Weapon weapon) {
		//TODO
	}

	public void setPath(Stack<Vector2i> path) {
		this.path = path;
	}

	public void setInteractionRequested() {
		this.requestInteraction = true;
	}

	public Armor[] getEquipedArmor() {
		return this.armor;
	}

	public Weapon getEquipedWeapon() {
		return this.weapon;
	}

	public HashMap<UUID, StorableObject> getInventory() {
		return (HashMap<UUID, StorableObject>) this.inventory.clone();
	}

	public void addMana(int mana) {
		this.mana = Math.min(this.maxMana, mana + this.mana);
	}

	public void heal(int hp) {
		this.hitPoints = Math.min(this.maxHitPoints, hp + this.hitPoints);
	}

	public void increaseAttack(int ad) {
		this.attackPower += ad;
	}

	public void increaseHP(int hp) {
		this.maxHitPoints += hp;
		this.hitPoints += hp;
	}

	public void increaseDefense(int def) {
		this.defensePower += def;
	}

	public void increaseMana(int manaModifier) {
		this.maxMana += manaModifier;
		this.mana += manaModifier;
	}

	@Override
	public void live() {
		//TODO
	}

	@Override
	public LivingThingType getType() {
		return LivingThingType.PLAYER;
	}
}
