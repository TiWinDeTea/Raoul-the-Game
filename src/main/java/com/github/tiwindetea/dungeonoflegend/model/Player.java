package com.github.tiwindetea.dungeonoflegend.model;

import java.util.List;

/**
 * Created by maxime on 4/23/16.
 */
public class Player extends LivingThing {
	private List<StorableObject> inventory;
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

	Player() {
		//TODO
	}

	public int getMana() {
		return this.mana;
	}

	public void addToInventory(StorableObject storable) {
		if (this.inventory.size() < this.maxStorageCapacity) {
			this.inventory.add(storable);
		}
	}

	public boolean useMana(int consumption) {
		if(this.mana > consumption) {
			this.mana -= consumption;
			return true;
		}
		return false;
	}

	/*public void equipWith(Equipement equiment) {
		//TODO
	}*/

	public Armor[] getEquipedArmor() {
		return this.armor;
	}

	public Weapon getEquipedWeapon() {
		return this.weapon;
	}

	public StorableObject[] getInventory() {
		return this.inventory.toArray(new StorableObject[this.inventory.size()]);
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
