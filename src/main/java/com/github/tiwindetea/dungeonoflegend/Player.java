package com.github.tiwindetea.dungeonoflegend;

import java.util.List;

/**
 * Created by maxime on 4/23/16.
 */
public class Player extends LivingThing {
	private List<StorableObject> storableObjects;
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
		//TODO
		return 0;
	}

	public void addToInventory(StorableObject storable) {
		//TODO
	}

	public boolean useMana(int consumption) {
		//TODO
		return false;
	}

	/*public void equipWith(Equipement equiment) {
		//TODO
	}*/

	public Armor[] getEquipedArmor() {
		//TODO
		return new Armor[0];
	}

	public Weapon getEquipedWeapon() {
		//TODO
		return null;
	}

	public StorableObject[] getInventory() {
		//TODO
		return new StorableObject[0];
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
