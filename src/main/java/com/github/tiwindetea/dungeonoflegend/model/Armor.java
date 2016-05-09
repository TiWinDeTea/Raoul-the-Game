package com.github.tiwindetea.dungeonoflegend.model;

import com.github.tiwindetea.dungeonoflegend.view.entities.StaticEntityType;

import java.util.Random;

/**
 * Created by maxime on 4/23/16.
 */
public class Armor implements StorableObject {

	private int defensePowerModifier;
	private int attackPowerModifier;
	private ArmorType type;
	private StaticEntityType gtype;

	public Armor(int defensePowerModifier, int attackPowerModifier, ArmorType type) {
		this.defensePowerModifier = defensePowerModifier;
		this.attackPowerModifier = attackPowerModifier;
		this.type = type;
		Random random = new Random();
		if (random.nextBoolean()) {
			switch (type) {
				case BREAST_PLATE:
					this.gtype = StaticEntityType.BREAST_PLATE1;
					break;
				case GLOVES:
					this.gtype = StaticEntityType.GLOVES1;
					break;
				case HELMET:
					this.gtype = StaticEntityType.HELMET1;
					break;
				case BOOTS:
					this.gtype = StaticEntityType.BOOTS1;
					break;
				case PANTS:
					this.gtype = StaticEntityType.PANTS1;
					break;
			}
		} else {
			switch (type) {
				case BREAST_PLATE:
					this.gtype = StaticEntityType.BREAST_PLATE2;
					break;
				case GLOVES:
					this.gtype = StaticEntityType.GLOVES2;
					break;
				case HELMET:
					this.gtype = StaticEntityType.HELMET2;
					break;
				case BOOTS:
					this.gtype = StaticEntityType.BOOTS2;
					break;
				case PANTS:
					this.gtype = StaticEntityType.PANTS2;
					break;
			}
		}
	}

	public StaticEntityType getGtype() {
		return this.gtype;
	}

	public int getDefensePowerModifier() {
		return this.defensePowerModifier;
	}

	public int getAttackPowerModifier() {
		return this.attackPowerModifier;
	}

	public ArmorType getArmorType() {
		return this.type;
	}

	public StorableObjectType getType() {
		return StorableObjectType.ARMOR;
	}
}
