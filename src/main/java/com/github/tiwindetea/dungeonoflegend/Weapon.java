package com.github.tiwindetea.dungeonoflegend;

/**
 * Created by maxime on 4/23/16.
 */
public class Weapon implements StorableObject {

	private int attackPowerModifier;
	private byte range;
	private byte manaCost;
	private WeaponType type;

	public Weapon() {
		//TODO
	}

	public WeaponType getWeaponType() {
		//TODO
		return null;
	}

	public int getAttackPowerModifier() {
		//TODO
		return 0;
	}

	public byte getRange() {
		//TODO
		return 0;
	}

	public byte getManaCost() {
		//TODO
		return 0;
	}

	@Override
	public StorableObjectType getType() {
		//TODO
		return null;
	}
}
