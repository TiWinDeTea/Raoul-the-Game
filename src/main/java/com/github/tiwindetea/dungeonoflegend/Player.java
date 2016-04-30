package com.github.tiwindetea.dungeonoflegend;

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

	@Override
	public void live() {
		//TODO
	}

	@Override
	public LivingThingType getType() {
		return LivingThingType.PLAYER;
	}
}
